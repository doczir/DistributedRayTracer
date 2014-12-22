package com.base.raytracer.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.base.raytracer.math.HDRColor;
import com.base.raytracer.messages.Pixel;
import com.base.raytracer.messages.PixelDone;
import com.base.raytracer.messages.RenderTask;
import com.base.raytracer.messages.RenderTaskDone;

import java.util.ArrayList;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class RenderActor extends AbstractActor {

    public RenderActor() {

        receive(ReceiveBuilder
                .match(RenderTask.class, renderTask -> {
                    ArrayList<Pixel> pixels = new ArrayList<>();
                    while (renderTask.hasNext()) {
                        pixels.add(new Pixel(renderTask.next(), new HDRColor(0.0f, 1.0f, 1.0f)));

                        if (pixels.size() > 500) {
                            sender().tell(new PixelDone(pixels), self());
                            pixels.clear();
                        }
                    }
                    if (pixels.size() > 0) {
                        sender().tell(new PixelDone(pixels), self());
                    }
                    sender().tell(new RenderTaskDone(), self());
                }).build());
    }

    public static Props props() {
        return Props.create(RenderActor.class, RenderActor::new);
    }
}
