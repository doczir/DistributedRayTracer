package com.base.raytracer.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.base.raytracer.Scene;
import com.base.raytracer.math.HDRColor;
import com.base.raytracer.messages.Pixel;
import com.base.raytracer.messages.PixelDone;
import com.base.raytracer.messages.RenderTask;
import com.base.raytracer.messages.RenderTaskDone;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class RenderActor extends AbstractActor {

    private Scene scene;

    public RenderActor(Scene scene) {
        this.scene = scene;

        Random random = new Random();

        receive(ReceiveBuilder
                .match(RenderTask.class, renderTask -> {
                    ArrayList<Pixel> pixels = new ArrayList<>();
                    while (renderTask.hasNext()) {

                        Thread.sleep(1);
                        pixels.add(new Pixel(renderTask.getId(), renderTask.next(), new HDRColor(random.nextFloat(), random.nextFloat(), random.nextFloat())));

                        if (pixels.size() > 500) {
                            sender().tell(new PixelDone(pixels), self());
                            pixels.clear();
                        }
                    }
                    if (pixels.size() > 0) {
                        sender().tell(new PixelDone(pixels), self());
                    }
                    sender().tell(new RenderTaskDone(renderTask.getId()), self());
                }).build());
    }

    public static Props props(Scene scene) {
        return Props.create(RenderActor.class, () -> new RenderActor(scene));
    }
}
