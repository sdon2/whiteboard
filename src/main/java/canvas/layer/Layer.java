package canvas.layer;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import name.LayerIdentifier;
import canvas.DrawableBase;
import canvas.Pixel;
/**
 * Represents a Layer in our GUI
 *
 */
public class Layer extends DrawableBase {
	private static final long serialVersionUID = -5558390701591471173L;
	
	// image where the user's drawing is stored
    private transient BufferedImage image;
    private LayerProperties layerProperties;
    private int level;
    
	/**
     * Make a canvas.
     * @param width width in pixels
     * @param height height in pixels
	 * @param layerIdentifier 
     */
    public Layer(int width, int height, LayerIdentifier layerIdentifier, int level) {
        super(width, height);
        this.layerProperties = new LayerProperties(layerIdentifier);
        this.level = level;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
    
    /**
     * @return layerProperties
     */
    public LayerProperties layerProperties() {
    	return layerProperties;
    }
    
    /**
     * Set level of the layer
     * @param level
     */
    public void setLevel(int level) {
    	this.level = level;
    }
    
    /**
     * @return level
     */
    public int level() {
    	return level;
    }
    
    /**
     * Make the drawing buffer entirely white.
     */
    public synchronized void fillWithColor(Color color) {
    	
        final Graphics2D g = (Graphics2D) image.getGraphics();

        g.setColor(color);
        g.fillRect(0, 0, width, height);
    }
    
    /**
     * Draws pixel to the image
     */
    @Override
    public synchronized void drawPixel(LayerIdentifier identifier, Pixel pixel) {
        if (!isValidPixel(pixel)) {
            return;
        }

        final Graphics2D g = (Graphics2D) image.getGraphics();

        g.setColor(Color.BLACK);
        
        image.setRGB(pixel.x(), pixel.y(), pixel.color().getRGB());
    }
    
    /**
     * Update the properties of the layer
     * @param properties
     */
	public void setProperties(LayerProperties properties) {
		this.layerProperties = properties;
	}
	
	/**
	 * Draws a line to the canvas
	 */
    @Override
    public synchronized void drawLine(LayerIdentifier identifier, Pixel pixelStart, Pixel pixelEnd, Stroke stroke, int symetry) {
        if (!isValidPixel(pixelStart) || !isValidPixel(pixelEnd)) {
            return;
        }
        
        final Graphics2D g = (Graphics2D) image.getGraphics();
        if (pixelStart.color() != pixelEnd.color()) {
            g.setPaint(new GradientPaint(pixelStart.x(), pixelStart.y(), pixelStart.color(),
                                                       pixelEnd.x(), pixelEnd.y(), pixelEnd.color()));
        }
        else {
            g.setColor(pixelStart.color());
        }
        g.setStroke(stroke);
        
        if (symetry > 1) {
            double y1 = -pixelStart.y() + height/2;
            double x1 = pixelStart.x() - width/2;
            double distanceStart = Math.sqrt(Math.pow(x1, 2) + Math.pow(y1, 2));
            double thetaStart = Math.atan2(y1, x1);
            
            double y2 = -pixelEnd.y() + height/2;
            double x2 = pixelEnd.x() - width/2;
            double distanceEnd = Math.sqrt(Math.pow(x2, 2) + Math.pow(y2, 2));
            double thetaEnd = Math.atan2(y2,x2);
            
            double rotAmmount = 2 * Math.PI / symetry;
            
            
            for (int i = 0; i < symetry; i++) {
                g.drawLine(width/2 + (int)(Math.cos(thetaStart) * distanceStart), height/2 - (int)(Math.sin(thetaStart) * distanceStart), 
                        width/2 + (int)(Math.cos(thetaEnd) * distanceEnd), height/2 - (int)(Math.sin(thetaEnd) * distanceEnd));
                g.drawLine(width/2 - (int)(Math.sin(thetaStart) * distanceStart), height/2 + (int)(Math.cos(thetaStart) * distanceStart), 
                        width/2 - (int)(Math.sin(thetaEnd) * distanceEnd), height/2 + (int)(Math.cos(thetaEnd) * distanceEnd));
                thetaStart += rotAmmount;
                thetaEnd += rotAmmount;
            }
        }
        else {
            g.drawLine(pixelStart.x(), pixelStart.y(), pixelEnd.x(), pixelEnd.y());
        }
    }
    
    /**
     * Draws a fill command to the canvas
     */
    public synchronized void drawFill(LayerIdentifier identifier, Pixel pixel) {
        if (!isValidPixel(pixel)) {
            return;
        }

        HashSet<Pixel> pixels = new HashSet<Pixel>();
        Queue<Pixel> queue = new LinkedList<Pixel>();
        queue.add(pixel);
        
        Color initialColor = null;
        try {
            initialColor = getPixelColor(identifier, pixel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        while (!queue.isEmpty()) {
            Pixel newPixel = queue.remove();
            pixels.add(newPixel);
            try {
                Color newPixelColor = getPixelColor(identifier, newPixel);
                if (newPixelColor.equals(initialColor)) { 
                    drawPixel(identifier, newPixel);
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            Pixel nextPixel = new Pixel(newPixel.x() + i,  newPixel.y() + j, pixel.color());
                            if (!pixels.contains(nextPixel)) {
                                pixels.add(nextPixel);
                                queue.add(nextPixel);
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }
    
    /**
     * Returns pixelColor at pixel location
     */
    @Override
	public synchronized Color getPixelColor(LayerIdentifier id, Pixel pixel) throws Exception {
		if (!isValidPixel(pixel)) {
			throw new Exception();
		}
		return new Color(image.getRGB(pixel.x(), pixel.y()), true);
	}
    
    /**
     * Paints graphics onto image
     */
    public synchronized void paintOnGraphics(Graphics g) {
    	Graphics2D g2d = (Graphics2D) g;
    	if (isVisible()) {
    		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)opacity());
    		g2d.setComposite(ac);
            g.drawImage(image, 0, 0, null);
    	}
    }
	
    /**
     * Returns if layer is visible
     * @return boolean
     */
    public synchronized boolean isVisible() {
    	return layerProperties.getVisibility();
    }
    
    /**
     * Returns opacity of layer
     * @return
     */
    public synchronized double opacity() {
    	return layerProperties.getOpacity();
    }
    
    /**
     * Serializes and sends the Layer to the server
     * @param s
     * @throws IOException
     */
	private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        
        int w = image.getWidth();
        int h = image.getHeight();
        int[] pixels = image != null? new int[w * h] : null;

        if (image != null) {
            try {
                PixelGrabber pg = new PixelGrabber(image, 0, 0, w, h, pixels, 0, w);
                pg.grabPixels();
                if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
                    throw new IOException("failed to load image contents");
                }
            }
            catch (InterruptedException e) {
                throw new IOException("image load interrupted");
            }
        }
        s.writeInt(w);
        s.writeInt(h);
        s.writeObject(pixels);
    }
	
	/**
	 * Reads the serialized Pixel data and creates the image
	 * @param s
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
    private void readObject(ObjectInputStream s) throws ClassNotFoundException, IOException {
        s.defaultReadObject();

        int w = s.readInt();
        int h = s.readInt();
        int[] pixels = (int[])(s.readObject());

        if (pixels != null) {
            Toolkit tk = Toolkit.getDefaultToolkit();
            ColorModel cm = ColorModel.getRGBdefault();
            Image temp = tk.createImage(new MemoryImageSource(w, h, cm, pixels, 0, w));
            image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            image.getGraphics().drawImage(temp, 0, 0, null);
        }
    }
    
    /**
     * Returns the image
     * @return image
     */
	public BufferedImage image() {
		return image;
	} 
}
