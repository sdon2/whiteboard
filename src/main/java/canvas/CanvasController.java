package canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Stroke;

import javax.swing.JPanel;

import name.LayerIdentifier;
import stroke.StrokeProperties;
import canvas.controller.DefaultDrawingController;
import canvas.controller.DrawingController;
import client.ClientController;

/**
 * Canvas represents a drawing surface that allows the user to draw
 * on it freehand, with the mouse.
 */
public class CanvasController extends DrawableBase {
    private static final long serialVersionUID = -7112257891818505133L;

    private final DrawingController defaultDrawingController;
    private final Canvas canvas;
    /**
     * Constructor
     * @param strokeProperties
     * @param clientController
     * @param canvas
     */
    public CanvasController(StrokeProperties strokeProperties, ClientController clientController, Canvas canvas) {
        super(canvas.width, canvas.height);
        this.canvas = canvas;
        this.defaultDrawingController = new DefaultDrawingController(clientController, strokeProperties, canvas);
    }

    // An empty canvas controller, no drawing support
    public CanvasController(int width, int height) {
    	super(width, height);
    	this.canvas = new Canvas(width, height);
    	this.defaultDrawingController = null;
    }
    
    /**
     * Add the mouse listener that supports the user's freehand drawing.
     */
    public void setDefaultDrawingController(JPanel obj) {
    	assert this.defaultDrawingController != null;
        obj.addMouseListener(defaultDrawingController);
        obj.addMouseMotionListener(defaultDrawingController);
    }
    
    public Canvas canvas() {
    	return canvas;
    }
    
    @Override
    public void paintOnGraphics(Graphics g) {
    	canvas.paintOnGraphics(g);
    }
    
    /**
     * Draw a pixel onto the appropriate layer on the canvas
     */
    @Override
    public void drawPixel(LayerIdentifier identifier, Pixel pixel) {
        canvas.drawPixel(identifier, pixel);
    }
    /**
     * Draws a line onto the canvas
     */
    @Override
    public void drawLine(LayerIdentifier identifier, Pixel pixelStart, Pixel pixelEnd, Stroke stroke, int symetry) {
        canvas.drawLine(identifier, pixelStart, pixelEnd, stroke, symetry);
    }
    
    /**
     * Fill command onto the canvas
     */
    @Override
    public void drawFill(LayerIdentifier identifier, Pixel pixel) {
        canvas.drawFill(identifier, pixel);
    }
    
    /**
     * Return the pixel color from the canvas
     */
    @Override
    public Color getPixelColor(LayerIdentifier id, Pixel pixel) throws Exception {
        return canvas.getPixelColor(id, pixel);
    }
}
