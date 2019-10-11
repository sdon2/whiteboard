package canvas;

import java.awt.Color;
import java.awt.Stroke;

import name.LayerIdentifier;
/**
 * Interface which has methods of different ways to draw onto a graphics object
 *
 */
public interface Drawable {
    public int width();
    public int height();
    
    /**
     * Fills in pixel on specified layer
     * @param identifier of layer
     * @param pixel
     */
    public void drawPixel(LayerIdentifier identifier, Pixel pixel);
    
    /**
     * Draw line between to points with specified stroke on specified layer. Add symmetry if symmetry value is greater than 1.
     * @param identifier for layer
     * @param pixelStart
     * @param pixelEnd
     * @param stroke
     * @param symetry
     */
    public void drawLine(LayerIdentifier identifier, Pixel pixelStart, Pixel pixelEnd, Stroke stroke, int symetry);
    
    /**
     * fill on layer around specified pixel. Recursively works outward filling in the pixels that are the same color as
     * the pixel selected.
     * @param identifier of current layer
     * @param pixel to fill from
     */
    public void drawFill(LayerIdentifier identifier, Pixel pixel);

    /**
     * Gets pixel color on the specified layer
     * @param id for layer
     * @param pixel
     * @return The pixel color
     * @throws Exception 
     */
    public Color getPixelColor(LayerIdentifier id, Pixel pixel) throws Exception;
}
