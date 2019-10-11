package stroke;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import name.LayerIdentifier;
import util.Vector2;
import canvas.Drawable;
import canvas.Pixel;
import canvas.command.DrawCommand;
import canvas.command.DrawCommandPixel;
/**
 * Spray stroke that implements StrokeType but isn't in the final GUI currently
 * 
 *
 */
public class StrokeTypeSpray implements StrokeType {
	/**
     * Creates a series of draw commands to paint lines based on the mouse movement
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
        List<DrawCommand> result = new LinkedList<DrawCommand>();      
        int extraWidth = 8-(int)Math.sqrt(Math.pow(velocity.x(), 2) + Math.pow(velocity.y(), 2));
        if (extraWidth < 0) {extraWidth = 0;}
        for (int i = x1+strokeWidth; i <= x1+strokeWidth; i++) {
            for (int j = y1-strokeWidth; j <= y1+strokeWidth; j++) {
                if (Math.random() > .5) {
                    Pixel pixel = new Pixel(i, j, color);

                    try {
                        if (!canvas.getPixelColor(identifier, pixel).equals(pixel.color())) {
                            result.add(new DrawCommandPixel(identifier, pixel));
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }  
        return result.toArray(new DrawCommand[result.size()]);
    }
    
    /**
     * Returns string representation of name of stroke
     */
    @Override
    public String toString() {
        return "Spray";
    }

}
