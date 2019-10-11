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
 * Stroke that resembles fur and implements StrokeType
 *
 */
public class StrokeTypeFur implements StrokeType {
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

        int bound = 30;
        for (int i = -bound; i < bound; i++) {
            for (int j = -bound; j < bound; j++) {
                Pixel checkPixel =new Pixel(x1 + i, y1 + j, new Color(color.getRed(), color.getGreen(), color.getGreen(),0));
                try {
                    if (canvas.getPixelColor(identifier, checkPixel).equals(color)) {
                        double size = -Math.random() * 1.5;
                        int dx = i;
                        int dy = j;
                        if (Math.random() > .9) {
                            Pixel startPixel = new Pixel((int)(x1 + (dx * size)), (int)(y1 + (dy * size)), new Color(color.getRed(), color.getGreen(), color.getGreen(),0));
                            Pixel middlePixel = new Pixel(x1, y1, new Color(color.getRed(), color.getGreen(), color.getGreen(), 150));
                            Pixel endPixel = new Pixel((int)(x1+1 - (dx * size)), (int)(y1+j - (dy * size)), new Color(color.getRed(), color.getGreen(), color.getGreen(),0));
                            result.add(new DrawCommandLine(identifier, startPixel, middlePixel, stroke, symetry));
                            result.add(new DrawCommandLine(identifier, middlePixel, endPixel, stroke, symetry));
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
        result.add(new DrawCommandLine(identifier, new Pixel(x1, y1, color), new Pixel(x2, y2, color), stroke, symetry));
        return result.toArray(new DrawCommand[result.size()]);
    }
    
    /**
     * Return string representation of stroke
     */
    @Override
    public String toString() {
        return "Fur";
    }
}
