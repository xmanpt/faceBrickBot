package image.utils.worker;

import image.utils.ColorUtils;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

class TakeBlackPointsFromImage extends JPanel {


    @Override
    protected void paintComponent(Graphics g) {

        g.drawImage(WindowForReader.image1, 0, 0, this);

        try {
            extractBrickBoard(g);
        } catch (Exception e) {
        }

    }

    private void extractBrickBoard(Graphics g) throws FileNotFoundException, UnsupportedEncodingException {
        int xi = 523;
        int yi = 258;
        int dist = 32;
        int rad = 16;

        Set<Color> colorPallete = new HashSet<>();

        PrintWriter writer = new PrintWriter("files\\positions.txt", "UTF-8");

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                int x0 = (xi + i * dist);
                int y0 = (yi + j * dist);

                Color originalColor = getAvgColor(x0, y0, rad, rad);

                Color c = addToColorPallete(colorPallete, originalColor);

                g.setColor(Color.black);
                g.drawRect(x0 - 1, y0 - 1, rad + 2, rad + 2);
                g.setColor(c);
                g.fillRect(x0, y0, rad, rad);
                writer.println(i + ";" + j + ";" + c.getRed() + ";" + c.getGreen() + ";" + c.getBlue() + ";");
            }
        }

        writer.close();
        System.out.println("Finished! Detected colors: " + colorPallete.size());
        for (Color c : colorPallete) {
            System.out.println(c.toString());
        }
    }

    private Color addToColorPallete(Set<Color> colorPallete, Color c) {
        if (colorPallete.contains(c))
            return c;

        for (Color currColor : colorPallete) {
            if(ColorUtils.isSimilarColors(c, currColor)){
                return currColor;
            }
        }
        colorPallete.add(c);
        return c;
    }


    public Color getAvgColor(int x0, int y0, int w, int h) {
        int x1 = x0 + w;
        int y1 = y0 + h;

        long sumr = 0, sumg = 0, sumb = 0;
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                Color pixel = new Color(WindowForReader.image1.getRGB(x, y));
                sumr += pixel.getRed();
                sumg += pixel.getGreen();
                sumb += pixel.getBlue();
            }
        }
        int num = w * h;
        int avgR = (int) (sumr / num);
        int avgG = (int) (sumg / num);
        int avgB = (int) (sumb / num);
        return new Color(avgR, avgG, avgB);

    }
}
