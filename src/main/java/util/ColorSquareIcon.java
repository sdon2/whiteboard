package util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;

/**
 * Creates custom square icons for buttons.
 */
public class ColorSquareIcon implements Icon {

    private int size;
    private int forcedSize;
    private Color color;

    /**
     * Creates square for Icon
     * @param size pixel width of square
     * @param color color of square
     */
    public ColorSquareIcon(int size, Color color) {
        this.size = size;
        this.color = color;
        this.forcedSize = 10;
    }
    
    /**
     * Paints the icon on the canvas
     */
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.black);
        g2d.fillRect(x + (forcedSize - size) / 2, y + (forcedSize - size) / 2, size, size);
        g2d.setColor(color);
        g2d.fillRect(x + (forcedSize - size) / 2, y + (forcedSize - size) / 2, size - 2, size -2);
    }
    
    /**
     * Returns icon width
     */
    @Override
    public int getIconWidth() {
        return forcedSize;
    }

    /**
     * Returns icon height
     */
    @Override
    public int getIconHeight() {
        return forcedSize;
    }
}
