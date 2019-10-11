package canvas.command;

import name.LayerIdentifier;
import canvas.Drawable;
import canvas.Pixel;
/**
 * Draw a pixel onto the canvas
 * 
 */
public class DrawCommandPixel extends DrawCommand {
    private static final long serialVersionUID = 1777067036196897284L;
    
    private final Pixel pixel;
    
    /**
     * Constructor
     * @param id
     * @param pixel
     */
    public DrawCommandPixel(LayerIdentifier id, Pixel pixel) {
    	super(id);
        this.pixel = pixel;
    }
    
    /**
     * Draws the pixel onto the canvas
     */
    @Override
    public void drawOn(Drawable drawable) {
        drawable.drawPixel(id, pixel);
    }
}
