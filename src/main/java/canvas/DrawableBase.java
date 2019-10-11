package canvas;

import java.awt.Graphics;
import java.io.Serializable;
/**
 * Abstract class that represents a drawable base that we use to draw a drawable onto a graphic
 *
 */
public abstract class DrawableBase implements Drawable, Serializable {
    private static final long serialVersionUID = -6788405670250928889L;
    
    protected final int width;
    protected final int height;

    /**
     * Construct a DrawableBase with width and height.
     * @param width 
     * @param height
     * @throws InvalidArgumentExcpetion if width <= 0 or height <= 0
     */
    public DrawableBase(int width, int height) {
        if (width <= 0 || height <= 0){
            throw new IllegalArgumentException("Invalid width or height.");
        }
        
        this.width = width;
        this.height = height;
    }
    
    /**
     * Specifies how the drawable should be painted on a graphics object.
     * @param g the graphics to draw to.
     */
    public abstract void paintOnGraphics(Graphics g);
    
    public int width() {
        return width;
    }
    
    public int height() {
        return height;
    }
    
    /**
     * 
     * @param pixel the input pixel
     * @return whether the pixel is valid for the drawable
     */
    protected boolean isValidPixel(Pixel pixel) {
        return pixel.x() >= 0 && pixel.x() < width &&
               pixel.y() >= 0 && pixel.y() < height;
    }
}
