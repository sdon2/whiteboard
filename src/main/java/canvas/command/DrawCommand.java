package canvas.command;

import java.io.Serializable;

import name.LayerIdentifier;
import canvas.Drawable;
/**
 * Abstract class DrawCommand which represents all the types of draws we can do and on which layer they're done
 *
 */
public abstract class DrawCommand implements Serializable {
    private static final long serialVersionUID = -4765442741260871845L;
    
    protected final LayerIdentifier id;
    /**
     * Constructor
     * @param id
     */
    public DrawCommand(LayerIdentifier id) {
    	this.id = id;
    }
    /**
     * Returns Layer id
     * @return
     */
    public LayerIdentifier id() {
    	return id;
    }
    
    /**
     * Abstract method implemented by different draw commands to draw on the canvas 
     * @param drawable
     */
    public abstract void drawOn(Drawable drawable);
}
