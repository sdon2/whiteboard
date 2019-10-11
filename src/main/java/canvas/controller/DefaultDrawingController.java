package canvas.controller;

import java.awt.event.MouseEvent;

import stroke.StrokeProperties;
import util.Vector2;
import canvas.Drawable;
import canvas.command.DrawCommand;
import client.ClientController;
/**
 * The Default Drawing Controller used by the client, handles drawing to the canvas by user clicks and drags
 *
 */
public class DefaultDrawingController extends DrawingController {

	// store the coordinates of the last mouse event, so we can
    // draw a line segment from that last point to the point of the next mouse event.
    private int lastX, lastY; 
    /**
     * Constructor
     * @param clientController
     * @param strokeProperties
     * @param canvas
     */
    public DefaultDrawingController(ClientController clientController,
			StrokeProperties strokeProperties, Drawable canvas) {
		super(clientController, strokeProperties, canvas);
	}
    
    /**
     * When mouse button is pressed down, start drawing.
     */
    public void mousePressed(MouseEvent e) {
        lastX = e.getX();
        lastY = e.getY();
        
        DrawCommand[] commands = strokeProperties.paintLine(clientController.selectedLayer(), canvas, lastX, lastY, lastX, lastY, 
        new Vector2(0,0));
        for (DrawCommand command : commands) {
            clientController.sendDrawCommand(command);
        }
    }

    /**
     * When mouse moves while a button is pressed down,
     * draw a line segment.
     */
    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        
        DrawCommand[] commands = strokeProperties.paintLine(clientController.selectedLayer(), canvas, lastX, lastY, x, y, 
                                                            new Vector2(x-lastX,y-lastY));
        for (DrawCommand command : commands) {
            clientController.sendDrawCommand(command);
        }

        lastX = x;
        lastY = y;
    }
    
    /**
     * Upon releasing the mouse, update the GUI and thumbnail images for the layer
     */
    public void mouseReleased(MouseEvent e) {
    	clientController.setGUILayers();
    }
    
    // Ignore all these other mouse events.
    public void mouseMoved(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
}
