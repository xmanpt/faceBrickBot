package image.utils;

import image.utils.worker.WindowForReader;

import javax.swing.*;
import java.awt.*;

public class ReadImage {

    private JFrame frmExample;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(
                new Runnable() {
                    public void run() {
                        try {
                            ReadImage window = new ReadImage();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * Create the application.
     */
    public ReadImage() {

        WindowForReader.init();
    }

}
