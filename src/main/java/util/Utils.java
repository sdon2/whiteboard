package util;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Random;

/**
 * Contains general utilities
 */

public class Utils {
    /**
     * Takes in string and returns the associated color object
     * @param Name of the color
     * @return The color
     */
    public static Color colorFromString(String colorString) {
        try {
            Field field = Class.forName("java.awt.Color").getField(colorString);
            return (Color)field.get(null);
        } catch (Exception e) {
            throw new IllegalArgumentException("Color " + colorString + " not defined.");
        }
    }
    
    /**
     * Generates random integer in the range (0,10000000]
     * @return ID
     */
    public static int generateId() {
        Random rand = new Random();
        return rand.nextInt(10000000);
    }
}
