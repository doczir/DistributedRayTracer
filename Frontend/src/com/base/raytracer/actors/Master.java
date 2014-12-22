package com.base.raytracer.actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.base.raytracer.Scene;
import com.base.raytracer.math.HDRColor;
import com.base.raytracer.math.Vector2;
import com.base.raytracer.messages.*;
import com.google.common.base.Stopwatch;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class Master extends AbstractActor {

    private static final int BLOCK_SIZE = 64;

    List<Backend>     backends;
    Queue<RenderTask> taskQueue;
    private int pixelsDone = 0;
    private Stopwatch sw;

    public Master(int width, int height, Scene scene, float[] raster, Component renderFrame) {
        backends = new ArrayList<>();

        receive(ReceiveBuilder
                .match(RenderScene.class, renderScene -> {
                    taskQueue = new ArrayDeque<>();
                    for (int y = 0; y < height; y += BLOCK_SIZE) {
                        for (int x = 0; x < width; x += BLOCK_SIZE) {
                            taskQueue.add(new RenderTask(new Vector2(x, y), new Vector2(Math.min(x + BLOCK_SIZE - 1, width - 1), Math.min(y + BLOCK_SIZE - 1, height - 1)), width));
                        }
                    }
                })
                .match(BackendRegistration.class, backendRegistration -> initializeBackend(new Backend(backendRegistration.getRenderRouter(), backendRegistration.getNrOfWorkers())))
                .match(RenderTaskDone.class, renderTaskDone -> sendTaskIfApplicable(sender()))
                .match(PixelDone.class, pixelDone -> pixelDone.getPixels().forEach(pixel -> {
                    if (sw == null) sw = Stopwatch.createStarted();
                    int idx = pixel.getIdx() * 3;
                    HDRColor color = pixel.getVal();


                    raster[idx] = color.getR();
                    raster[idx + 1] = color.getG();
                    raster[idx + 2] = color.getB();

                    pixelsDone++;

                    if (pixelsDone % 100 == 0) renderFrame.repaint();

                    if (pixelsDone == width * height) {
                        System.out.println(sw);
//                        backends.forEach(backend -> backend.getRouter().tell(new Shutdown(), self()));
//                        getContext().system().shutdown();
                    }
                })).build());
    }

    public static Props props(int width, int height, Scene scene, float[] raster, Component renderFrame) {
        return Props.create(Master.class, () -> new Master(width, height, scene, raster, renderFrame));
    }

    private void initializeBackend(Backend backend) {
        for (int i = 0; i < backend.getNrOfWorkers(); i++) {
            sendTaskIfApplicable(backend.getRouter());
        }
    }

    private void sendTaskIfApplicable(ActorRef worker) {
        RenderTask task = taskQueue.poll();
        if (task != null)
            worker.tell(task, self());
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
}
