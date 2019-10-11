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
 * Stroke Squares that implements StrokeType
 */
public class StrokeTypeSquares implements StrokeType {
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

        int cx = Math.round(x1 / (float)100) * 100;
        int cy = Math.round(y1 / (float)100) * 100;
        
        int dx = (cx - x1) * 3;
        int dy = (cy - y1) * 3;

        for (int i = 0; i < 4; i++)
        {
                Pixel startPixel = new Pixel(cx, cy, new Color(color.getRed(), color.getGreen(), color.getGreen(), 30));
                Pixel endPixel = new Pixel((int)(cx + Math.random() * dx), (int)(cy + Math.random() * dy), new Color(0,0,0,0));
                result.add(new DrawCommandLine(identifier, startPixel, endPixel, stroke, symetry));
        } 
        return result.toArray(new DrawCommand[result.size()]);
    }
    /**
     * Stroke name returned as string
     */
    @Override
    public String toString() {
        return "Squares";
    }
}

