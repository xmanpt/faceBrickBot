package image.utils;

import java.awt.*;
import java.util.Set;

public class ColorUtils {
    public static final int colorThreeshold = 5;

    public static boolean isSimilarColors(Color c1, Color c2){
        if(c1==null || c2 == null)
            return false;

        return
                Math.abs(c1.getRed() - c2.getRed()) < colorThreeshold &&
                Math.abs(c1.getGreen() - c2.getGreen()) < colorThreeshold &&
                Math.abs(c1.getBlue() - c2.getBlue()) < colorThreeshold;

    }


}
