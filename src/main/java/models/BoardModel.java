package models;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Stroke;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import name.BoardIdentifier;
import name.Identifiable;
import name.LayerIdentifier;
import canvas.Canvas;
import canvas.CanvasController;
import canvas.Drawable;
import canvas.Pixel;
import canvas.layer.LayerAdjustment;
import canvas.layer.LayerProperties;

/**
 * BoardModel is the underlying model that represents the board state and is updated with 
 * draw commands. Also stores the Set of users in the board and stores a CanvasController which updates
 * the canvas of the BoardModel
 *
 */
public class BoardModel extends JPanel implements Drawable, Identifiable, Serializable {
	private static final long serialVersionUID = -7812022750931126889L;

	private final BoardIdentifier boardName;
    private final CanvasController canvas;
    private Identifiable owner;
    private final Set<Identifiable> users;
    
    /**
     * Constructor for server's BoardModel, starts with an empty set of Users
     * @param boardName
     * @param canvas
     */
    // Server only
    public BoardModel(BoardIdentifier boardName, CanvasController canvas, Identifiable owner) {
    	this(boardName, canvas, new Identifiable[]{}, owner);
    }
    
    /**
     * Constructor for client's BoardModel
     * @param boardName
     * @param canvas
     * @param initUsers
     */
    public BoardModel(BoardIdentifier boardName, CanvasController canvas, Identifiable[] initUsers, Identifiable owner) {
        this.boardName = boardName;
        this.canvas = canvas;
        this.users = Collections.synchronizedSet(new HashSet<Identifiable>(Arrays.asList(initUsers)));
        this.setPreferredSize(new Dimension(width(), height()));
        this.owner = owner;
    }
    
    /**
     * Method to add a user to set of users, checks that user isn't in Set already
     * @param user
     */
    public void addUser(Identifiable user) {
        assert !users.contains(user);
        users.add(user);
    }
    
    /**
     * Checks if the user is already in our Set of users
     * @param user
     * @return boolean
     */
    public boolean containsUser(Identifiable user) {
    	return users.contains(user);
    }
    
    /**
     * Removes user from set of users on board, for example when a client disconnects
     * @param user
     */
    public void removeUser(Identifiable user) {
    	assert users.contains(user);
        users.remove(user);
    }
    
    /**
     * Returns an array of all the Users in the current board
     * @return Identifiable[] users
     */
    public Identifiable[] users() {
        return users.toArray(new Identifiable[users.size()]).clone();
    }
    
    /**
     * @return BoardIdentifier
     */
    public BoardIdentifier getBoardName() {
        return boardName;
    }
    
    /**
     * Draws a pixel onto the canvas of the board and repaints it
     */
    @Override
    public void drawPixel(LayerIdentifier identifier, Pixel pixel) {
        canvas.drawPixel(identifier, pixel);
        repaint();
    }
    
    /**
     * Draws a line onto the canvas and repaints it
     */
    @Override
    public void drawLine(LayerIdentifier identifier, Pixel pixelStart, Pixel pixelEnd, Stroke stroke, int symetry) {
        canvas.drawLine(identifier, pixelStart, pixelEnd, stroke, symetry);
        repaint();
    }
    
    /**
     * Draws a fill command onto the canvas and repaints
     */
    @Override
    public void drawFill(LayerIdentifier identifier, Pixel pixel) {
        canvas.drawFill(identifier, pixel);
        repaint();
    }
    
    /**
     * Returns the pixelColor at the input pixel location on the specified Layer
     */
    @Override
    public Color getPixelColor(LayerIdentifier id, Pixel pixel) throws Exception {
        return canvas.getPixelColor(id,pixel);
    }
    
    /**
     * Returns the width of the canvas
     */
    @Override
    public int width() {
        return canvas.width();
    }
    
    /**
     * Returns the height of the canvas
     */
    @Override
    public int height() {
        return canvas.height();
    }
    
    /**
     *  Returns BoardIdentifier
     */
    @Override
    public BoardIdentifier identifier() {
        return boardName;
    }
    
    /**
     * Paints graphics component onto canvas
     */
    @Override
    public void paintComponent(Graphics g) {
        canvas.paintOnGraphics(g);
    }
    
    /**
     * @return Canvas
     */
    public Canvas canvas() {
    	return canvas.canvas();
    }
    
    /**
     * Sets default drawing controller to be the canvas controller
     */
	public void setDrawingControllerDefault() {
		canvas.setDefaultDrawingController(this);
	}
	
	/**
	 * Adds a layer to the canvas
	 * @param layer
	 */
	public void addLayer(LayerIdentifier layer) {
		canvas.canvas().addLayer(layer);
	}
	
	/**
	 * Adjusts the layer provided with the appropriate adjustment
	 * @param properties
	 * @param adjustment
	 */
	public void adjustLayer(LayerProperties properties, LayerAdjustment adjustment) {
		canvas.canvas().adjustLayer(properties, adjustment);
		repaint();
	}

	public Identifiable getOwner() {
	    return this.owner;
    }
	
}
