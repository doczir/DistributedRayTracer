package com.base.raytracer.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.base.raytracer.Scene;
import com.base.raytracer.filters.Bloom;
import com.base.raytracer.filters.Filter;
import com.base.raytracer.math.HDRColor;
import com.base.raytracer.math.Matrix3;
import com.base.raytracer.math.Vector2;
import com.base.raytracer.messages.*;
import com.base.raytracer.messages.Shutdown;
import com.google.common.base.Stopwatch;
import scala.concurrent.duration.Duration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class Master extends AbstractActor {

    private static final int     BLOCK_SIZE = 64;
    private static final Matrix3 rgb2xyz    = new Matrix3(new double[]{
            0.5141364, 0.3238786, 0.16036376,
            0.265068, 0.67023428, 0.06409157,
            0.0241188, 0.1228178, 0.84442666
    });
    private static final Matrix3 xyz2rgb    = new Matrix3(new double[]{
            2.5651, -1.1665, -0.3986,
            -1.0217, 1.9777, 0.0439,
            0.0753, -0.2543, 1.1892
    });
    private final float[] raster;
    private final BufferedImage image;
    private final HDRColor[][]  hdrRaster;
    List<Backend>     backends;
    Queue<RenderTask> taskQueue;
    private Scene scene;
    private int pixelsDone = 0;
    private Stopwatch sw;
    private Map<UUID, Cancellable> cancellables;

    public Master(int width, int height, float[] raster, BufferedImage image, Component renderFrame) {
        this.raster = raster;
        this.image = image;
        hdrRaster = new HDRColor[width][height];
        backends = new ArrayList<>();
        cancellables = new HashMap<>();

        receive(ReceiveBuilder
                .match(RenderScene.class, renderScene -> {
                    scene = renderScene.getScene();
                    taskQueue = new ArrayDeque<>();
                    for (int y = 0; y < height; y += BLOCK_SIZE) {
                        for (int x = 0; x < width; x += BLOCK_SIZE) {
                            taskQueue.add(new RenderTask(UUID.randomUUID(), new Vector2(x, y), new Vector2(Math.min(x + BLOCK_SIZE - 1, width - 1), Math.min(y + BLOCK_SIZE - 1, height - 1)), width));
                        }
                    }
                })
                .match(BackendRegistration.class, backendRegistration -> {
                    sender().tell(new RenderScene(scene), self());
                    initializeBackend(new Backend(backendRegistration.getRenderRouter(), backendRegistration.getNrOfWorkers()));
                })
                .match(FailedTask.class, failedTask -> {
                    cancellables.remove(failedTask.getTask().getId());
                    taskQueue.add(failedTask.getTask());
                })
                .match(RenderTaskDone.class, renderTaskDone -> {
                    cancellables.remove(renderTaskDone.getId()).cancel();
                    sendTaskIfApplicable(sender());

                    if (taskQueue.isEmpty() && cancellables.isEmpty()) {
                        self().tell(new RenderDone(), self());
                    }
                })
                .match(RenderDone.class, renderDone -> {

                    applyPreFilters(
                            new Bloom(.1f, 5f, 1f, 1f, 1f)
                    );
                    toneMap();
                    applyPostFilters();
                    swapRaster();

                    renderFrame.repaint();

                    scene.setDone(true);

                    System.out.println(sw);
                    backends.forEach(backend -> backend.getRouter().tell(new Shutdown(), self()));
                    getContext().system().shutdown();
                })
                .match(PixelDone.class, pixelDone -> pixelDone.getPixels().forEach(pixel -> {
                    if (sw == null) sw = Stopwatch.createStarted();
                    int idx = pixel.getIdx() * 3;

                    HDRColor color = pixel.getVal();
                    hdrRaster[pixel.getIdx() - pixel.getIdx() / width * width][pixel.getIdx() / width] = color;

                    color = color.reinhart(scene.getExposure());

                    raster[idx] = color.getR();
                    raster[idx + 1] = color.getG();
                    raster[idx + 2] = color.getB();

                    pixelsDone++;

                    if (pixelsDone % 100 == 0) renderFrame.repaint();
                })).build());
    }

    public static Props props(int width, int height, float[] raster, BufferedImage image, Component renderFrame) {
        return Props.create(Master.class, () -> new Master(width, height, raster, image, renderFrame));
    }

    private void applyPreFilters(Filter... filters) {
        Arrays.stream(filters).forEach(filter -> filter.apply(hdrRaster));
    }

    private void applyPostFilters(Filter... filters) {
        Arrays.stream(filters).forEach(filter -> filter.apply(hdrRaster));
    }

    private void swapRaster() {
        for (int x = 0; x < hdrRaster.length; x++) {
            for (int y = 0; y < hdrRaster[x].length; y++) {
                HDRColor hdrColor = hdrRaster[x][y];

                int i = y * hdrRaster.length + x;

                raster[i * 3] = hdrColor.getR();
                raster[i * 3 + 1] = hdrColor.getG();
                raster[i * 3 + 2] = hdrColor.getB();
            }
        }
    }

    private void toneMap() {
        for (int x = 0; x < hdrRaster.length; x++) {
            for (int y = 0; y < hdrRaster[x].length; y++) {
                hdrRaster[x][y] = hdrRaster[x][y].reinhart(scene.getExposure());
            }
        }
    }

    private void initializeBackend(Backend backend) {
        backends.add(backend);
        for (int i = 0; i < backend.getNrOfWorkers(); i++) {
            sendTaskIfApplicable(backend.getRouter());
        }
    }

    private void sendTaskIfApplicable(ActorRef worker) {
        int waitTime = scene.getSamples() * scene.getPrimitiveCount() * BLOCK_SIZE * BLOCK_SIZE / 10;
        RenderTask task = taskQueue.poll();
        if (task != null) {
            worker.tell(task, self());
            Cancellable cancellable = getContext().system().scheduler().scheduleOnce(Duration.create(waitTime, TimeUnit.MILLISECONDS), self(), new FailedTask(task), getContext().system().dispatcher(), worker);
            cancellables.put(task.getId(), cancellable);
        }
    }

    private static class Backend {
        private ActorRef router;
        private int      nrOfWorkers;

        public Backend(ActorRef router, int nrOfWorkers) {
            this.router = router;
            this.nrOfWorkers = nrOfWorkers;
        }

        public ActorRef getRouter() {
            return router;
        }

        public int getNrOfWorkers() {
            return nrOfWorkers;
        }
    }

    private static class FailedTask {
        private RenderTask task;

        public FailedTask(RenderTask task) {
            this.task = task;
        }

        public RenderTask getTask() {
            return task;
        }
    }

    private static class RenderDone {

    }
}
