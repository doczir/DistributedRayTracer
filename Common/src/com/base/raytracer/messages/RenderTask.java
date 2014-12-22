package com.base.raytracer.messages;

import com.base.raytracer.math.Vector2;

import java.io.Serializable;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class RenderTask implements Serializable {

    private UUID id;

    private Vector2 from;
    private Vector2 to;
    private int     w;

    private Vector2 end;
    private Vector2 current;

    public RenderTask(UUID id, Vector2 from, Vector2 to, int w) {
        checkArgument(from.x < to.x && from.y < to.y, "Render task range invalid.");
        this.id = id;
        this.from = from;
        this.to = to;
        this.w = w;

        this.current = from;
        this.end = to.add(0, 1);
        end.x = from.x;
    }

    public UUID getId() {
        return id;
    }

    public boolean hasNext() {
        return !end.equals(current);

    }

    public ConcreteTask next() {
        ConcreteTask ret = new ConcreteTask((int) (current.y * w + current.x), current.copy());
        if (current.x != to.x) {
            current = current.add(1, 0);
        } else {
            current = current.add(0, 1);
            current.x = from.x;
        }
        return ret;
    }

    public static class ConcreteTask {
        public int     idx;
        public Vector2 pos;

        public ConcreteTask(int idx, Vector2 pos) {
            this.idx = idx;
            this.pos = pos;
        }
    }
}
