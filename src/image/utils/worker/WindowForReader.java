package image.utils.worker;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WindowForReader {

    public static String image = "a.png";
    public static BufferedImage image1 = readImage("files//" + image);

    public static BufferedImage readImage(String fileLocation) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(fileLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

    public static void Window() {

        TakeBlackPointsFromImage example = new TakeBlackPointsFromImage();

        int width = image1.getWidth();
        int height = image1.getHeight() + 30;

        JFrame frame = new JFrame("ReDraw");
        frame.getContentPane().add(example, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setSize(width, height);
    }

    public static void init() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                Window();
            }
        });
    }
} 

