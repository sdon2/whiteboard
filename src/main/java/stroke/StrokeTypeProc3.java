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
 * Stroke that represents a follow brush and implements StrokeType
 *
 */
public class StrokeTypeProc3 implements StrokeType {
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
        double length = 1 + Math.random() * 1.5;
        for (int j = 0; j < strokeWidth * length; j++) {
            Pixel pixel = new Pixel((int)(x1 + Math.min(velocity.x()*j, 20)), (int)(y1 + Math.min(velocity.y()*j, 20)), new Color(color.getRed(),color.getGreen(), color.getBlue(), (int)(j/(float)(strokeWidth * length)*255)));

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
        return "Follow";
    }


}
