package canvas;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.junit.Test;

public class PixelTest {
    // TODO: Pixel tests
    
    @Test
    public void testx() {
        // test getting x value
        Pixel pixel = new Pixel(10,13, Color.RED);
        assertEquals(pixel.x(), 10);
    }
    
    @Test
    public void testNegativex() {
        // test negative x input
        Pixel pixel = new Pixel(-1,2, Color.RED);
        assertEquals(pixel.x(), -1);
    }
    
    @Test
    public void testy() {
        // test getting y value
        Pixel pixel = new Pixel(10,13, Color.RED);
        assertEquals(pixel.y(), 13);
    }
    
    @Test
    public void testNegativeY() {
        // test negative y input
        Pixel pixel = new Pixel(1,-2, Color.RED);
        assertEquals(pixel.y(), -2);
    }
    
    @Test
    public void testColor() {
        // test getting color value
        Pixel pixel = new Pixel(10,13, Color.RED);
        assertTrue(pixel.color() == Color.RED);
    }
    
    @Test
    public void testClone() {
        // test cloning Pixel
        Pixel pixel = new Pixel(10,13, Color.RED);
        assertEquals(pixel.y(), 13);
        assertEquals(pixel.x(), 10);
        assertTrue(pixel.color() == Color.RED);
        assertTrue(pixel != pixel.clone());
    }
    
    @Test
    public void testHashCodeSame() {
        // test hashcode equality for two instanced with same values
        Pixel pixel = new Pixel(10,13, Color.RED);
        Pixel pixel1 = new Pixel(10,13, Color.RED);
        assertEquals(pixel.hashCode(), pixel1.hashCode());
    }
    
    @Test
    public void testHashCodeDifferent() {
        // test hashcode equality for two instanced with different values
        Pixel pixel = new Pixel(10,13, Color.RED);
        Pixel pixel1 = new Pixel(11,13, Color.RED);
        assertThat(pixel.hashCode(), not(pixel1.hashCode()));
    }
    
    @Test
    public void testEqualsSame() {
        // test equality for two instanced with same values
        Pixel pixel = new Pixel(10,13, Color.RED);
        Pixel pixel1 = new Pixel(10,13, Color.RED);
        assertTrue(pixel.equals(pixel1));
    }
    
    @Test
    public void testEqualsDifferent() {
        // test equality for two instanced with different values
        Pixel pixel = new Pixel(10,13, Color.RED);
        Pixel pixel1 = new Pixel(11,13, Color.RED);
        assertFalse(pixel.equals(pixel1));
    }
    
    
}
