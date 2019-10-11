package util;

/**
 * Representation of a 2D vector
 */
public class Vector2 {
    private final double x, y;
    
    /**
     * Create a vector point at the location (x,y) in a 2D grid 
     * @param x coordinate
     * @param y coordinate
     */
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * @return x coordinate
     */
    public double x() {
        return x;
    }
    
    /**
     * @return y coordinate
     */
    public double y() {
        return y;
    }

    /**
     * computes the length of the vector between the origin and (x,y) point
     * @return vector length
     */
    public double abs() {
        return Math.sqrt(x*x + y*y);
    }
    
    /**
     * create a normalized version of the vector
     * @return normalized vector
     */
    public Vector2 normalized() {
        double abs = abs();
        return new Vector2(x / abs, y / abs); 
    }
    
    /**
     * @return clone of object
     */
    public Vector2 clone() {
        return new Vector2(x, y);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vector2 other = (Vector2) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
            return false;
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
            return false;
        return true;
    }
}
