package com.base.raytracer.messages;

import com.base.raytracer.Scene;

import java.io.Serializable;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class RenderScene implements Serializable {

    Scene scene;

    public RenderScene(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }
}
