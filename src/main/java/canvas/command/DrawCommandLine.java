package canvas.command;

import java.awt.BasicStroke;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import name.LayerIdentifier;
import canvas.Drawable;
import canvas.Pixel;
/**
 * Represents draw line command
 *
 */
public class DrawCommandLine extends DrawCommand {
    private static final long serialVersionUID = 1777067036196897234L;
    
    private final Pixel pixelStart;
    private final Pixel pixelEnd;
    private transient BasicStroke stroke;
    private final int symetry;
    
    /**
     * Constructor
     * @param id
     * @param pixelStart
     * @param pixelEnd
     * @param stroke
     * @param symetry
     */
    public DrawCommandLine(LayerIdentifier id, Pixel pixelStart, Pixel pixelEnd, BasicStroke stroke, int symetry) {
    	super(id);
    	this.pixelStart = pixelStart;
        this.pixelEnd = pixelEnd;
        this.stroke = stroke;
        this.symetry = symetry;
    }
    
    /**
     * Draws the line onto the canvas
     */
    @Override
    public void drawOn(Drawable drawable) {
        drawable.drawLine(id, pixelStart, pixelEnd, stroke, symetry);
    }
    
    /**
     * Custom serialization of the draw command line
     * @param out
     * @throws IOException
     */
    private synchronized void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeFloat(stroke.getLineWidth());
        out.writeInt(stroke.getEndCap());
        out.writeInt(stroke.getLineJoin());
    }

    /**
     * Deserialize the draw command line object that comes in and create the new stroke
     * @param in
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private synchronized void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        float width = in.readFloat();
        int endCap = in.readInt();
        int lineJoin = in.readInt();
        stroke = new BasicStroke(width, endCap, lineJoin);
    }
}
