package name;

import java.io.Serializable;
/**
 * Abstract class that represents a way of identifying boards and users
 * Each identifier has an id and a name
 *
 */
public abstract class Identifier implements Identifiable, Serializable {
    private static final long serialVersionUID = 1611778700707259153L;
    
    private int id;
    private String name;
    /**
     * Constructor for an identifiable object
     * @param id
     * @param name
     */
    public Identifier(int id, String name) {
        this.id = id;
        this.name = name;
    }
    
    /**
     * Getter method for id of Identifier
     * @return id
     */
    public int id() {
        return id;
    }
    
    /**
     * Getter method for name of Identifier
     * @return name
     */
    public String name() {
        return name;
    }
    
    /**
     * Getter method for this Identifier
     * @return identifier
     */
    @Override
    public Identifier identifier() {
        return this;
    }
    
    /**
     * Generates a hash value for this Identifier
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }
    
    /**
     * Checks if this Identifier is equal to another object
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Identifier other = (Identifier) obj;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

	public String toString() {
		return this.name();
	}
}
