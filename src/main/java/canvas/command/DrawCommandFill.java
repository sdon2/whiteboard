package canvas.command;

import name.LayerIdentifier;
import canvas.Drawable;
import canvas.Pixel;
/**
 * Represents the fill draw command, only sends the pixel to start the fill from
 *
 */
public class DrawCommandFill extends DrawCommand {
    private static final long serialVersionUID = -4272516979645753409L;
    
    private final Pixel pixel;
    /**
     * Constructor
     * @param id
     * @param pixel
     */
    public DrawCommandFill(LayerIdentifier id, Pixel pixel) {
    	super(id);
        this.pixel = pixel;
    }
    
    /**
     * Draws the fill command on the canvas
     */
    @Override
    public void drawOn(Drawable drawable) {
        drawable.drawFill(id, pixel);
    }

}
