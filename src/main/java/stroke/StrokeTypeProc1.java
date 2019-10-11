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
 * Stroke that creates spinners as you draw
 * Implements StrokeType
 *
 */
public class StrokeTypeProc1 implements StrokeType {
	/**
     * Creates a series of draw commands based on the mouse movement
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
        double rand = Math.random() * 2*Math.PI;
        double spinniness = Math.random()*4;
        double length = Math.random() * 4;
        for (int j = 0; j < strokeWidth * length; j++) {
            Pixel pixel = new Pixel((int)(x1 + Math.sin(rand+j/(float)(strokeWidth * spinniness))*j), (int)(y1 + Math.cos(rand+j/(float)(strokeWidth * spinniness))*j), color);

            try {
                if (!canvas.getPixelColor(identifier, pixel).equals(pixel.color())) {
                    result.add(new DrawCommandPixel(identifier, pixel));
                }
            } catch (Exception e) {
            }
        }
        
        return result.toArray(new DrawCommand[result.size()]);
    }
    
    /**
     * Returns string representation of stroke
     */
    @Override
    public String toString() {
        return "Spinners";
    }


}
