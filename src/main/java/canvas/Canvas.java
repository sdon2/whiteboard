package canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Stroke;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import name.LayerIdentifier;
import util.Utils;
import canvas.layer.Layer;
import canvas.layer.LayerAdjustment;
import canvas.layer.LayerProperties;
/**
 * Canvas represents the canvas on which we have layers and we can draw on the layers
 *
 */
public class Canvas extends DrawableBase {
    private static final long serialVersionUID = -6329493755553689791L;

    private static final String BASE_NAME = "Default";
    
    // image where the user's drawing is stored
    private final List<Layer> layers;
    private final Map<LayerIdentifier, Layer> layerSet;
    
    /**
     * Make a canvas. Only used by the server.
     * @param width width in pixels
     * @param height height in pixels
     */
    public Canvas(int width, int height) {
        super(width, height);
        this.layers = new LinkedList<Layer>();
        this.layerSet = new HashMap<LayerIdentifier, Layer>();
        addLayer(new LayerIdentifier(Utils.generateId(), BASE_NAME));
    }
    
    private void checkRep() {
    	assert layers.size() == layerSet.size();
    }
    
    /**
     * Add layer to the top of the stack.
     * @param layerIdentifier
     */
    public synchronized void addLayer(LayerIdentifier layerIdentifier) {
    	Layer layer = new Layer(width, height, layerIdentifier, layers.size());
    	layerSet.put(layerIdentifier, layer);
    	layers.add(layer);
    	checkRep();
    }
    
    /**
     * Adjust layer.
     * @param properties the properties that should be adjusted
     * @param adjustment the adjustment that should be made
     */
    public synchronized void adjustLayer(LayerProperties properties, LayerAdjustment adjustment) {
    	Layer layer = layerSet.get(properties.layerIdentifier());
    	
    	int level = layer.level();

    	int modifier = 0;
    	switch (adjustment) {
    	case UP:
    		modifier = 1;
    		break;
    	case DOWN:
    		modifier = -1;
    		break;
    	case PROPERTIES:
    		layer.setProperties(properties);
    		break;
    	}
    	
    	int newLevel = level + modifier;
    	
    	if (newLevel < 0 || newLevel >= layers.size()) {
    		return;
    	}
    	
    	layer.setLevel(newLevel);
    	Layer flippedLayer = layers.get(newLevel);
    	flippedLayer.setLevel(level);
    	
    	Collections.swap(layers, level, newLevel);
    	checkRep();
    }
    
    /**
     * Returns all the layers as an array
     */
    public Layer[] layers() {
    	checkRep();
    	return layers.toArray(new Layer[layers.size()]);
    }
    
    /**
     * Synchronized method to draw a pixel onto a layer
     */
    @Override
    public synchronized void drawPixel(LayerIdentifier identifier, Pixel pixel) {
    	layerSet.get(identifier).drawPixel(identifier, pixel);
    }
    
    /**
     * Synchronized method to draw a line onto a layer
     */
    @Override
    public synchronized void drawLine(LayerIdentifier identifier, Pixel pixelStart, Pixel pixelEnd, Stroke stroke, int symetry) {
    	layerSet.get(identifier).drawLine(identifier, pixelStart, pixelEnd, stroke, symetry);
    }
    
    /**
     * Synchronized method to draw a fill command onto a layer
     */
    @Override
    public synchronized void drawFill(LayerIdentifier identifier, Pixel pixel) {
    	layerSet.get(identifier).drawFill(identifier, pixel);
    }
    
    /**
     * Synchronized method to get the pixel color of a pixel
     */
    @Override
	public synchronized Color getPixelColor(LayerIdentifier id, Pixel pixel) throws Exception {
	    Color pixelColor = layerSet.get(id).getPixelColor(id, pixel);
	    return pixelColor;
	}
    
    /**
     * Synchronized method to draw all the layers onto the graphics object
     */
    @Override
    public synchronized void paintOnGraphics(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
    	for (Layer layer : layers) {
    	    layer.paintOnGraphics(g);
    	}
    }
}
