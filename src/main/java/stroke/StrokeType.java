package stroke;

import java.awt.Color;

import name.LayerIdentifier;
import util.Vector2;
import canvas.Drawable;
import canvas.command.DrawCommand;

/**
 * All strokes use this interface
 * Some strokes inspired by Mr.Dood's harmony -> "http://www.mrdoob.com/projects/harmony/" (cool stuff)
 */

public interface StrokeType {
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
    public DrawCommand[] paintLine(LayerIdentifier identifier, Drawable canvas, Color color, int strokeWidth, int x1, int y1, int x2, int y2, Vector2 velocity, int symetry);
}
