package com.base.raytracer.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.base.raytracer.HitInfo;
import com.base.raytracer.Scene;
import com.base.raytracer.math.HDRColor;
import com.base.raytracer.messages.Pixel;
import com.base.raytracer.messages.PixelDone;
import com.base.raytracer.messages.RenderTask;
import com.base.raytracer.messages.RenderTask.ConcreteTask;
import com.base.raytracer.messages.RenderTaskDone;
import com.base.raytracer.primitives.Ray;

import java.util.ArrayList;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class RenderActor extends AbstractActor {

    private Scene scene;

    public RenderActor(Scene scene) {
        this.scene = scene;

        receive(ReceiveBuilder
                .match(RenderTask.class, renderTask -> {
                    ArrayList<Pixel> pixels = new ArrayList<>();
                    while (renderTask.hasNext()) {
                        ConcreteTask task = renderTask.next();

                        //Thread.sleep(1);

                        Ray ray = this.scene.getCamera().getRay(task.pos);

                        HitInfo intersect = this.scene.intersect(ray);

                        HDRColor color;

                        if (intersect.isIntersect())
                            color = intersect.getPrimitive().getMaterial().getColor();
                        else
                            color = new HDRColor();

                        pixels.add(new Pixel(renderTask.getId(), task.idx, color));

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
