package stroke;

import java.awt.Color;

import name.LayerIdentifier;
import util.Vector2;
import canvas.Drawable;
import canvas.command.DrawCommand;

/**
 * Contains all of the information for the current users stroke properties, including strokeColor, strokeWidth,
 * strokeType, if eraser is on, if fillOn and current symmetry level
 * 
 */
public class StrokeProperties {
    public final static Color DEFAULT_STROKE_COLOR = Color.BLACK;
    public final static int DEFAULT_STROKE_WIDTH = 5;
    
    private Color strokeColor;
    private int strokeWidth;
    private StrokeType strokeType;
    private StrokeType eraserStroke;
    private Boolean eraserOn;
    private StrokeType fillStroke;
    private Boolean fillOn;
    private int symetry;
    
    /**
     * Construct with specified color and stroke width
     * @param strokeColor
     * @param strokeWidth
     */
    public StrokeProperties(Color strokeColor, int strokeWidth) {
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        this.strokeType = new StrokeTypeBasic();
        this.eraserOn = false;
        this.eraserStroke = new StrokeTypeEraser();
        this.fillOn = false;
        this.fillStroke = new StrokeTypeFill();
        this.symetry = 1;
    }    
    
    /**
     * Construct with default color and stroke width
     */
    public StrokeProperties() {
        this(DEFAULT_STROKE_COLOR, DEFAULT_STROKE_WIDTH);
    }
    
    /**
     * Set stroke color
     * @param strokeColor
     */
    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }
    
    /**
     * Set stroke width
     * @param strokeWidth
     */
    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    /**
     * Set stroke type to draw with when in brush mode
     * @param strokeType
     */
    public void setStrokeType(StrokeType strokeType) {
        this.strokeType = strokeType;
    }
    
    /**
     * turn the eraser on
     * @param eraserOn
     */
    public void setEraserOn(Boolean eraserOn) {
        this.eraserOn = eraserOn;
    }
    
    /**
     * turn fill on
     * @param fillOn
     */
    public void setFillOn(Boolean fillOn) {
        this.fillOn = fillOn;
    }
    
    /**
     * set symmetry of image
     * @param symetry
     */
    public void setSymetry(int symetry) {
        this.symetry = symetry;
    }
    
    /**
     * Creates a series of draw commands based on the mouse movement
     * @param identifier of layer
     * @param canvas to draw on
     * @param x1 start x value
     * @param y1 start y value
     * @param x2 end x value
     * @param y2 end y value
     * @param velocity vector of stroke
     * @return Drawcommands for layer
     */
    public DrawCommand[] paintLine(LayerIdentifier identifier, Drawable canvas, int x1, int y1, int x2, int y2, Vector2 velocity) {
        if (eraserOn) {
            return eraserStroke.paintLine(identifier, canvas, Color.WHITE, strokeWidth, x1, y1, x2, y2, velocity, symetry);
        }
        else if (fillOn) {
            return fillStroke.paintLine(identifier, canvas, strokeColor, strokeWidth, x1, y1, x2, y2, velocity, symetry);
        }
        else {
            return strokeType.paintLine(identifier, canvas, strokeColor, strokeWidth, x1, y1, x2, y2, velocity, symetry);
        }
    }
}
