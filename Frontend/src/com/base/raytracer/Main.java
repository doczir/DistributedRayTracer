package com.base.raytracer;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.base.raytracer.actors.Master;
import com.base.raytracer.messages.RenderScene;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;

/**
 * @author Róbert Dóczi
 *         Date: 2014.12.22.
 */
public class Main extends JFrame {

    private final BufferedImage image;
    private       float[]       components;
    private       Scene         scene;
    private       int           width, height;
    private ImageComponent ic;

    public Main(int w, int h, String s) {
        this.width = w;
        this.height = h;
        setTitle(s);
        setSize(w, h);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        SampleModel sampleModel = new PixelInterleavedSampleModel(DataBuffer.TYPE_FLOAT, w, h, 3, w * 3, new int[]{0, 1, 2});
        DataBuffer dataBuffer = new DataBufferFloat(w * h * 3);

        WritableRaster raster = Raster.createWritableRaster(sampleModel, dataBuffer, null);

        components = ((DataBufferFloat) dataBuffer).getData();

        ColorSpace colorSpace = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ComponentColorModel colorModel = new ComponentColorModel(colorSpace, false, false, Transparency.OPAQUE, DataBuffer.TYPE_FLOAT);

        image = new BufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), null);

        ic = new ImageComponent(image);

        getContentPane().add(ic);

        setVisible(true);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Main main = new Main(800, 600, "Render Window");

            main.initializeScene();
            main.initializeSystem();
            main.render();
        });
    }

    private void initializeScene() {
        scene = new Scene();
    }

    private void initializeSystem() {
        final String port = "5061";

        Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
                .withFallback(ConfigFactory.parseString("akka.cluster.roles = [frontend]"))
                .withFallback(ConfigFactory.load());

        ActorSystem system = ActorSystem.create("RenderCluster", config);
        ActorRef master = system.actorOf(Master.props(width, height, scene, components, ic), "frontend");

        master.tell(new RenderScene(), null);
    }

    private void render() {

    }
}
