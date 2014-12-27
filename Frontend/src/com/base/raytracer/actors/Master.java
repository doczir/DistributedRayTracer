package com.base.raytracer.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.base.raytracer.Scene;
import com.base.raytracer.math.HDRColor;
import com.base.raytracer.math.Matrix3;
import com.base.raytracer.math.Vector2;
import com.base.raytracer.math.Vector3;
import com.base.raytracer.messages.*;
import com.base.raytracer.messages.Shutdown;
import com.google.common.base.Stopwatch;
import scala.concurrent.duration.Duration;

import java.awt.*;
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
    List<Backend>     backends;
    Queue<RenderTask> taskQueue;
    private Scene scene;
    private int pixelsDone = 0;
    private Stopwatch sw;
    private Map<UUID, Cancellable> cancellables;

    public Master(int width, int height, float[] raster, Component renderFrame) {
        this.raster = raster;
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

                    //toneMap();
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


                    raster[idx] = color.getR();
                    raster[idx + 1] = color.getG();
                    raster[idx + 2] = color.getB();

                    pixelsDone++;

                    if (pixelsDone % 100 == 0) renderFrame.repaint();
                })).build());
    }

    public static Props props(int width, int height, float[] raster, Component renderFrame) {
        return Props.create(Master.class, () -> new Master(width, height, raster, renderFrame));
    }

    private void toneMap() {

        for (int i = 0; i < raster.length; i += 3) {
            HDRColor current = new HDRColor(raster[i], raster[i + 1], raster[i + 2]);
            current = current.scale(2f);
            HDRColor ret = current.div(current.add(1));
            raster[i] = ret.getR();
            raster[i + 1] = ret.getG();
            raster[i + 2] = ret.getB();
        }

//        float maxLuminance = 0;
//        float avgLuminance = 0;
//        for (int i = 0; i < raster.length; i += 3) {
//            float current = luminance(raster[i], raster[i + 1], raster[i + 2]);
//            maxLuminance = Math.max(maxLuminance, current);
//            avgLuminance += current;
//        }
//        avgLuminance /= raster.length / 3;
//
//        float lWhite = maxLuminance * maxLuminance;
//
//        rgb2xyz.transpose();
//        xyz2rgb.transpose();
//
//        for (int i = 0; i < raster.length; i += 3) {
//            float r = raster[i];
//            float g = raster[i + 1];
//            float b = raster[i + 2];
//            float current = luminance(r, g, b);
//            float L = (0.25f / avgLuminance) * current;
//            float Ld = (L * (1.0f + L / lWhite)) / (1.0f + L);
//
//            Vector3 xyY = rgb2xyY(new Vector3(r, g, b));
////            float Lp = (float) (xyY.x * .5f / avgLuminance);
////            xyY.x = (Lp * (1.0f + Lp / (lWhite * lWhite))) / (1.0f + Lp);
//            Vector3 rgb = xyY2rgb(xyY);
//
//            raster[i] = (float) Math.pow(rgb.getR(), 1.0 / 2.2);
//            raster[i + 1] = (float) Math.pow(rgb.getG(), 1.0 / 2.2);
//            raster[i + 2] = (float) Math.pow(rgb.getB(), 1.0 / 2.2);
//        }
    }

    private float luminance(float r, float g, float b) {
        return (0.2126f * r) + (0.7152f * g) + (0.0722f * b);
    }

    private Vector3 rgb2xyY(Vector3 rgb) {
        Vector3 xyz = rgb2xyz.multiply(rgb);

        return new Vector3(xyz.y,
                xyz.x / (xyz.x + xyz.y + xyz.z),
                xyz.y / (xyz.x + xyz.y + xyz.z));
    }

    private Vector3 xyY2rgb(Vector3 xyY) {
        Vector3 xyz = new Vector3(xyY.x * xyY.y / xyY.z,
                xyY.x,
                xyY.x * (1 - xyY.y - xyY.z) / xyY.z);

        return xyz2rgb.multiply(xyz);
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
