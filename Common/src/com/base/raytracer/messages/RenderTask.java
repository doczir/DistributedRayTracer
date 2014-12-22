package com.base.raytracer.messages;

import com.base.raytracer.math.Vector2;

import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class RenderTask implements Serializable {

    private Vector2 from;
    private Vector2 to;
    private int     w;

    private Vector2 end;
    private Vector2 current;

    public RenderTask(Vector2 from, Vector2 to, int w) {
        checkArgument(from.x < to.x && from.y < to.y, "Render task range invalid.");
        this.from = from;
        this.to = to;
        this.w = w;

        this.current = from;
        this.end = to.add(0, 1);
        end.x = from.x;
    }

    public boolean hasNext() {
        if (!end.equals(current))
            return true;
        else return false;
    }

    public int next() {
        int ret = (int) (current.y * w + current.x);
        if (current.x != to.x) {
            current = current.add(1, 0);
        } else {
            current = current.add(0, 1);
            current.x = from.x;
        }
        return ret;
    }
}
