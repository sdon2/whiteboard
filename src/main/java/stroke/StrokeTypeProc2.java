package stroke;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import name.LayerIdentifier;
import util.Vector2;
import canvas.Drawable;
import canvas.Pixel;
import canvas.command.DrawCommand;
import canvas.command.DrawCommandLine;
/**
 * Stroke that draws hair like movements
 * implements StrokeType
 *
 */
public class StrokeTypeProc2 implements StrokeType {
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
        BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        for (int i = 0; i < 3; i++) {
            double rand = Math.random() * 2*Math.PI;
            double length = 1 + Math.random() * 2.5;
            Pixel startPixel = new Pixel(x1, y1, color);
            Pixel endPixel = new Pixel((int)(x1 + Math.sin(rand)*length*strokeWidth), (int)(y1 + Math.cos(rand)*length*strokeWidth), new Color(color.getRed(),color.getGreen(), color.getBlue(), 0));
            result.add(new DrawCommandLine(identifier, startPixel, endPixel, stroke, symetry));
        }
        return result.toArray(new DrawCommand[result.size()]);
    }
    
    /**
     * Returns string representation of stroke
     */
    @Override
    public String toString() {
        return "Hair";
    }

}
