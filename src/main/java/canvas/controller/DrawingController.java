package canvas.controller;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import stroke.StrokeProperties;
import canvas.Drawable;
import client.ClientController;
/**
 * Represents the drawing controller for the canvas, which handles how things are drawn based off of the current
 * stroke properties and the client controller
 *
 */
public abstract class DrawingController implements MouseListener, MouseMotionListener {
	protected ClientController clientController;
	protected StrokeProperties strokeProperties;
	protected Drawable canvas;
	
	/**
	 * Constructor
	 * @param clientController
	 * @param strokeProperties
	 * @param canvas
	 */
	public DrawingController(ClientController clientController,
			StrokeProperties strokeProperties, Drawable canvas) {
		this.clientController = clientController;
		this.strokeProperties = strokeProperties;
		this.canvas = canvas;
	}
}
