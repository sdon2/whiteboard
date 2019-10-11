package stroke;

import java.awt.BasicStroke;
import java.awt.Color;

import name.LayerIdentifier;
import util.Vector2;
import canvas.Drawable;
import canvas.Pixel;
import canvas.command.DrawCommand;
import canvas.command.DrawCommandLine;
/**
 * Stroke that represents an eraser and erasing, implemented by drawing with white
 * Implements StrokeType interface
 *
 */
public final class StrokeTypeEraser implements StrokeType {
	
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
        BasicStroke stroke = new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        DrawCommand command = new DrawCommandLine(identifier, new Pixel(x1, y1, Color.WHITE), new Pixel(x2, y2,  Color.WHITE), stroke, symetry);
        return new DrawCommand[]{ command };
    }
    
    /**
     * Returns string name of stroke
     */
    @Override
    public String toString() {
        return "Eraser";
    }

}
