package com.base.raytracer.actors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.base.raytracer.HitInfo;
import com.base.raytracer.Scene;
import com.base.raytracer.materials.*;
import com.base.raytracer.math.HDRColor;
import com.base.raytracer.math.Vector3;
import com.base.raytracer.messages.Pixel;
import com.base.raytracer.messages.PixelDone;
import com.base.raytracer.messages.RenderTask;
import com.base.raytracer.messages.RenderTask.ConcreteTask;
import com.base.raytracer.messages.RenderTaskDone;
import com.base.raytracer.primitives.Ray;
import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static java.lang.Math.*;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class RenderActor extends AbstractActor {

    private final Random random;
    private       Scene  scene;


    public RenderActor(Scene scene) {
        this.scene = scene;
        random = new Random();

        receive(ReceiveBuilder
                .match(RenderTask.class, renderTask -> {
                    Map<Integer, HDRColor> colorSamples = new HashMap<>();

                    for (int sample = 1; sample <= scene.getSamples(); sample++) {
                        RenderTask sampleTask = renderTask.copy();

                        while (sampleTask.hasNext()) {
                            ConcreteTask task = sampleTask.next();

                            Ray ray = this.scene.getCamera().getRay(task.pos.add(random.nextFloat(), random.nextFloat()));

                            HDRColor color = radiance(ray, 0);

                            if (!colorSamples.containsKey(task.idx)) {
                                colorSamples.put(task.idx, color);
                            } else {
                                colorSamples.put(task.idx, colorSamples.get(task.idx).add(color));
                            }
                        }

                        if (sample % 20 == 0) {
                            int currentSample = sample; //lambda needs final.....
                            List<Pixel> pixels = colorSamples.entrySet().stream().map(integerHDRColorEntry -> new Pixel(renderTask.getId(), integerHDRColorEntry.getKey(), integerHDRColorEntry.getValue().scale(1.0f / currentSample).reinhart(2))).collect(Collectors.toList());
                            List<List<Pixel>> partition = Lists.partition(pixels, 2048);
                            for (List<Pixel> pixelList : partition) {
                                sender().tell(new PixelDone(pixelList), self());
                            }
                        }
                    }

                    int currentSample = scene.getSamples(); //lambda needs final.....
                    List<Pixel> pixels = colorSamples.entrySet().stream().map(integerHDRColorEntry -> new Pixel(renderTask.getId(), integerHDRColorEntry.getKey(), integerHDRColorEntry.getValue().scale(1.0f / currentSample).reinhart(2))).collect(Collectors.toList());
                    List<List<Pixel>> partition = Lists.partition(pixels, 2048);
                    for (List<Pixel> pixelList : partition) {
                        sender().tell(new PixelDone(pixelList), self());
                    }
                    sender().tell(new RenderTaskDone(renderTask.getId()), self());
                }).build());
    }

    public static Props props(Scene scene) {
        return Props.create(RenderActor.class, () -> new RenderActor(scene));
    }

    private HDRColor radiance(Ray r, int depth) {
        HitInfo hit = scene.intersect(r);

        if (!hit.isIntersect()) return scene.getAmbient();
        if (depth > 200) return scene.getAmbient();

        Vector3 x = hit.getHitPoint();
        Vector3 n = hit.getNormal();
        Vector3 nl = hit.getOrientedNormal();

        Material material = hit.getPrimitive().getMaterial();
        HDRColor f = material.getColor();

        //Russian roulette
        float p = f.getMax();
        if (++depth > 5)
            if (random.nextFloat() < p)
                f = f.scale(1 / p);
            else
                return material.getEmmission();

        if (material instanceof DiffuseMaterial || material instanceof EmmissiveMaterial) {
            double r1 = 2 * PI * random.nextDouble();
            double r2 = random.nextDouble(), r2s = sqrt(r2);
            Vector3 w = nl;
            Vector3 u = (abs(w.x) > .1 ? Vector3.AXIS_Y : Vector3.AXIS_X).cross(w).normalize();
            Vector3 v = w.cross(u);
            Vector3 d = (u.scale(cos(r1) * r2s).add(v.scale(sin(r1) * r2s)).add(w.scale(sqrt(1 - r2)))).normalize();

            return material.getEmmission().add(f.mult(radiance(new Ray(d, x.add(nl.scale(1e-4))), depth)));
        } else if (material instanceof GlossyMaterial) {
            Ray reflRay = new Ray(r.getDir().subtract(n.scale(2 * n.dot(r.getDir()))), x.add(nl.scale(1e-4)));
            return material.getEmmission().add(f.mult(radiance(reflRay, depth)));
        } else if (material instanceof GlassMaterial) {
            Ray reflRay = new Ray(r.getDir().subtract(n.scale(2 * n.dot(r.getDir()))), x.add(nl.scale(1e-4)));
            boolean into = n.dot(nl) > 0;
            double nc = 1.0;
            double nt = ((GlassMaterial) material).getIor();
            double nnt = into ? 1.0 / nc / nt : nt / nc;
            double ddn = r.getDir().dot(nl);
            double cos2t;

            if ((cos2t = 1 - nnt * nnt * (1 - ddn * ddn)) < 0)
                return material.getEmmission().add(f.mult(radiance(reflRay, depth)));

            if (depth == 1) {
                int a = 0;
            }

            Vector3 tDir = r.getDir().scale(nnt).subtract(n.scale((into ? 1 : -1) * (ddn * nnt + sqrt(cos2t)))).normalize();
            Ray refrRay = new Ray(tDir, x.add(n.scale(into ? -1e-4 : 1e-4)));
            float a = (float) (nt - nc);
            float b = (float) (nt + nc);
            float c = (float) (1 - (into ? -ddn : tDir.dot(n)));
            float R0 = (a * a) / (b * b);

            float Re = R0 + (1 - R0) * c * c * c * c * c, Tr = 1 - Re, P = .25f + .5f * Re, RP = Re / P, TP = Tr / (1 - P);
            return material.getEmmission().add(f.mult(depth > 2 ? (random.nextDouble() < P ?
                    radiance(reflRay, depth).scale(RP) : radiance(refrRay, depth).scale(TP)) :
                    radiance(reflRay, depth).scale(Re).add(radiance(refrRay, depth).scale(Tr))));
        }
        return scene.getAmbient();
    }
}
