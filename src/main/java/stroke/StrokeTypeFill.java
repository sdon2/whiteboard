package stroke;

import java.awt.Color;

import name.LayerIdentifier;
import util.Vector2;
import canvas.Drawable;
import canvas.Pixel;
import canvas.command.DrawCommand;
import canvas.command.DrawCommandFill;
/**
 * Stroke that represents the fill bucket
 * Implements StrokeType
 *
 */
public class StrokeTypeFill implements StrokeType {
	
	/**
     * Creates a draw command fill based on the mouse click location
     * @param identifier of layer
     * @param canvas to draw on
     * @param color of stroke
     * @param strokeWidth
     * @param x1 start x value
     * @param y1 start y value
     * @param x2 end x value
     * @param y2 end y value
     * @param velocity vector of stroke
     * @param symetry of image
     * @return Drawcommands for layer
     */
    @Override
    public DrawCommand[] paintLine(LayerIdentifier identifier, Drawable canvas, Color color, int strokeWidth, int x1, int y1, int x2, int y2, Vector2 velocity, int symetry) {
        if (x1 == x2 && y1 == y2) {
            DrawCommand command = new DrawCommandFill(identifier, new Pixel(x1, y1, color));
            return new DrawCommand[]{ command };
        }
        return new DrawCommand[]{ };
    } 
    
    /**
     * Returns string name of stroke
     */
    @Override
    public String toString() {
        return "Fill";
    }

}
