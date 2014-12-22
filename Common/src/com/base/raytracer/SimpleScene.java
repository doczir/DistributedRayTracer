package com.base.raytracer;

import com.base.raytracer.primitives.Primitive;
import com.base.raytracer.primitives.Ray;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class SimpleScene extends Scene {

    private List<Primitive> primitives = new ArrayList<>();

    @Override
    public HitInfo intersect(Ray r) {
        return primitives.stream().map(primitive -> primitive.intersect(r)).min((hi1, hi2) -> (int) (hi1.getT() - hi2.getT())).get();
    }

    @Override
    public void addPrimitive(Primitive p) {
        primitives.add(p);
    }
}
