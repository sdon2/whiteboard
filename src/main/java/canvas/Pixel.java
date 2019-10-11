package canvas;

import java.awt.Color;
import java.io.Serializable;

import util.Utils;

/**
 * Represents a pixel on the canvas
 *
 */
public class Pixel implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final int x, y;
    private Color color;
    
    /**
     * Constructor, using color
     * @param x
     * @param y
     * @param color
     */
    public Pixel(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
    
    /**
     * Constructor, using string of the color
     * @param x
     * @param y
     * @param colorString
     */
    public Pixel(int x, int y, String colorString) {
        this(x, y, Utils.colorFromString(colorString));
    }
    
    /**
     * Returns x location of pixel
     * @return
     */
    public int x() {
        return x;
    }

    /**
     * Returns y location of pixel
     * @return
     */
    public int y() {
        return y;
    }

    /**
     * Returns pixel's color
     * @return
     */
    public Color color() {
        return color;
    }
    
    /**
     * Returns a deep copy of the current pixel
     */
    public Pixel clone() {
        return new Pixel(x, y, color);
    }
    
    /**
     * Creates a hashCode of the pixel
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }
    
    /**
     * Equals to method for pixels
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pixel other = (Pixel) obj;
        if (color == null) {
            if (other.color != null)
                return false;
        } else if (!color.equals(other.color))
            return false;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }
}
