package com.base.raytracer;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.base.raytracer.actors.Master;
import com.base.raytracer.materials.DiffuseMaterial;
import com.base.raytracer.materials.EmmissiveMaterial;
import com.base.raytracer.materials.GlassMaterial;
import com.base.raytracer.materials.GlossyMaterial;
import com.base.raytracer.math.HDRColor;
import com.base.raytracer.math.Vector3;
import com.base.raytracer.messages.RenderScene;
import com.base.raytracer.primitives.Plane;
import com.base.raytracer.primitives.Sphere;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

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
    private ActorRef master;

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

        Container cp = getContentPane();
        cp.add(ic);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyChar() != 's' && e.getKeyCode() == KeyEvent.VK_S && scene.isDone()) {
                    JFileChooser jFileChooser = new JFileChooser();
                    FileNameExtensionFilter png = new FileNameExtensionFilter("PNG image", "png");
                    jFileChooser.addChoosableFileFilter(png);
                    jFileChooser.setFileFilter(png);
                    if (jFileChooser.showSaveDialog(cp) == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = jFileChooser.getSelectedFile();
                        if (!selectedFile.getName().endsWith(".png")) selectedFile = new File(selectedFile + ".png");
                        try {
                            BufferedImage convImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

                            for (int i = 0; i < components.length / 3; i++) {
                                int idx = i * 3;
                                convImage.setRGB(i - (i / width * width), i / width, new Color(components[idx], components[idx + 1], components[idx + 2]).getRGB());
                            }
                            System.out.println("a");

                            if (ImageIO.write(convImage, "png", selectedFile))
                                System.out.println("Save complete!");
                            else System.out.println("Save failed!");
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
        EventQueue.invokeLater(() -> {
            Main main = new Main(1280, 720, "Render Window");

            main.initializeScene();
            main.initializeSystem();
            main.render();
        });
    }

    private void initializeScene() {
        scene = new SimpleScene();
        scene.setAmbient(new HDRColor(1, 0.25f, 0.75f));
//        scene.addPrimitive(new Sphere(new DiffuseMaterial(new HDRColor(.8f,.8f,.8f)), new Vector3(0, 0, -2), 0.25));

//        scene.addPrimitive(new Sphere(new DiffuseMaterial(new HDRColor(.75f,.25f,.25f)), new Vector3(11, 0, 0), 10));
//        scene.addPrimitive(new Sphere(new DiffuseMaterial(new HDRColor(.25f,.25f,.75f)), new Vector3(-11, 0, 0), 10));
        scene.addPrimitive(new Sphere(new DiffuseMaterial(new HDRColor(.8f, .8f, .8f)), new Vector3(0, -0.5, -2), 0.50));
        scene.addPrimitive(new Sphere(new GlossyMaterial(new HDRColor(1f, 1f, 1f)), new Vector3(1, -0.5, -3), 0.50));
        scene.addPrimitive(new Sphere(new GlassMaterial(new HDRColor(1f, 1f, 1f), 1.5f), new Vector3(-1, -0.5, -1.5), 0.50));

        scene.addPrimitive(new Plane(new DiffuseMaterial(new HDRColor(.75f, .25f, .25f)), new Vector3(-3, 0, 0), new Vector3(1, 0, 0)));
        scene.addPrimitive(new Plane(new DiffuseMaterial(new HDRColor(.25f, .25f, .75f)), new Vector3(3, 0, 0), new Vector3(-1, 0, 0)));
        scene.addPrimitive(new Plane(new DiffuseMaterial(new HDRColor(.8f, .8f, .8f)), new Vector3(0, 0, -10), new Vector3(0, 0, 1)));
        scene.addPrimitive(new Plane(new DiffuseMaterial(new HDRColor(.8f, .8f, .8f)), new Vector3(0, -1, 0), new Vector3(0, 1, 0)));
        scene.addPrimitive(new Plane(new DiffuseMaterial(new HDRColor(.8f, .8f, .8f)), new Vector3(0, 5.02, 0), new Vector3(0, -1, 0)));

        scene.addPrimitive(new Sphere(new EmmissiveMaterial(new HDRColor(12, 12, 12)), new Vector3(0, 105, -6), 100));
        scene.setCamera(new Camera(70, width, height, new Vector3(0, 0, 1), Vector3.ZERO, Vector3.AXIS_Y));
        scene.setSamples(100);
    }

    private void initializeSystem() {
        final String port = "5061";

        Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port)
                .withFallback(ConfigFactory.parseString("akka.cluster.roles = [frontend]"))
                .withFallback(ConfigFactory.load());

        ActorSystem system = ActorSystem.create("RenderCluster", config);
        master = system.actorOf(Master.props(width, height, components, ic), "frontend");
    }

    private void render() {
        master.tell(new RenderScene(scene), null);
    }
}
