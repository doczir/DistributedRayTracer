package com.base.raytracer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class ImageComponent extends JPanel {

    private BufferedImage image;

    public ImageComponent(BufferedImage image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }
}
