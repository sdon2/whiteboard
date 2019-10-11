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
 * CrossHatch stroke that implements StrokeType
 * 
 *
 */
public class StrokeTypeProc7 implements StrokeType {
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
        List<DrawCommand> result = branchBulder(identifier, color, x1, y1, velocity, symetry);
        return result.toArray(new DrawCommand[result.size()]);
    }
    
    /**
     * Creates a series of draw commands to build the branches
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
    private List<DrawCommand> branchBulder(LayerIdentifier identifier, Color color, int x, int y, Vector2 velocity, int symetry) {
        List<DrawCommand> result = new LinkedList<DrawCommand>();
        BasicStroke stroke = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        double length = 2 + Math.random() * .5;
        int j = (int) (2*length);
        int dx = (int)(Math.min(velocity.x()*j, 2));
        int dy = (int)(Math.min(velocity.y()*j, 2));
        result.add(new DrawCommandLine(identifier, new Pixel(x-dx, y-dy, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0)), new Pixel(x, y, color), stroke, symetry));
        result.add(new DrawCommandLine(identifier, new Pixel(x, y, color), new Pixel(x+dx, y+dy, new Color(color.getRed(), color.getGreen(), color.getBlue(), 0)), stroke, symetry));
            
        if (Math.random() > .5) {
            result.addAll(branchBulder(identifier, color, x+(dx/2), y+(dy/2), new Vector2(velocity.y(), velocity.x()), symetry));
        }
        return result;
    }
    
    /**
     * Returns string name of stroke
     */
    @Override
    public String toString() {
        return "Crosshatch";
    }
    
}
