package com.base.raytracer.messages;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class RenderTaskDone implements Serializable {
    UUID id;

    public RenderTaskDone(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
