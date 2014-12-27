package com.base.raytracer;

import com.base.raytracer.materials.EmmissiveMaterial;
import com.base.raytracer.primitives.Primitive;
import com.base.raytracer.primitives.Ray;
import com.base.raytracer.primitives.Sphere;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class SimpleScene extends Scene {

    private List<Primitive> primitives = new ArrayList<>();

    @Override
    public HitInfo intersect(Ray r) {
        HitInfo hit = HitInfo.NO_HIT;
        for (Primitive primitive : primitives) {
            HitInfo intersect = primitive.intersect(r);
            if (intersect.isIntersect() && intersect.getT() < hit.getT())
                hit = intersect;
        }
        return hit;
//        return primitives.stream().map(primitive -> primitive.intersect(r)).filter(HitInfo::isIntersect).min((hi1, hi2) -> (int) (hi2.getT() - hi1.getT())).orElse(HitInfo.NO_HIT);
    }

    @Override
    public int getPrimitiveCount() {
        return primitives.size();
    }

    @Override
    public void addPrimitive(Primitive p) {
        primitives.add(p);
    }

    @Override
    public void addLight(Sphere p) {
        primitives.add(p);
    }

    @Override
    public List<Primitive> getLights() {
        return primitives.stream().filter(primitive -> primitive.getMaterial() instanceof EmmissiveMaterial).collect(Collectors.toList());
    }
}
