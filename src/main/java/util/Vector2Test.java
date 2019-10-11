package util;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
/**
 * Test class that tests Vector2 class and each method in it
 * @author Bindu
 *
 */
public class Vector2Test {  
    
    @Test
    public void testx() {
        // test getting x value
        Vector2 vector = new Vector2(10.5,13.9);
        assertTrue(vector.x() == 10.5);
    }
    
    @Test
    public void testy() {
        // test getting y value
        Vector2 vector = new Vector2(10.5,13.9);
        assertTrue(vector.y() == 13.9);
    }
    
    @Test
    public void testAbs() {
        // test getting length of vector
        Vector2 vector = new Vector2(3,4);
        assertTrue(vector.abs() == 5);
    }
    
    @Test
    public void testNormalized() {
        // test getting length of vector
        Vector2 vector = new Vector2(3,4);
        Vector2 vectorNorm = new Vector2(3.0/5.0, 4.0/5.0);
        assertTrue(vector.normalized().equals(vectorNorm));
    }
    
    @Test
    public void testClone() {
        // test cloning vector
        Vector2 vector = new Vector2(3,4);
        assertTrue(vector != vector.clone());
    }
    
    @Test
    public void testHashCodeSame() {
        // test hashcode equality for two instances with same values
        Vector2 vector = new Vector2(10,13);
        Vector2 vector1 = new Vector2(10,13);
        assertEquals(vector.hashCode(), vector1.hashCode());
    }
    
    @Test
    public void testHashCodeDifferent() {
        // test hashcode equality for two instances with different values
        Vector2 vector = new Vector2(10,13);
        Vector2 vector1 = new Vector2(11,13);
        assertThat(vector.hashCode(), not(vector1.hashCode()));
    }
    
    @Test
    public void testEqualsSame() {
        // test equality for two instances with same values
        Vector2 vector = new Vector2(10,13);
        Vector2 vector1 = new Vector2(10,13);
        assertTrue(vector.equals(vector1));
    }
    
    @Test
    public void testEqualsDifferent() {
        // test equality for two instances with different values
        Vector2 vector = new Vector2(10,13);
        Vector2 vector1 = new Vector2(11,13);
        assertFalse(vector.equals(vector1));
    }
}
