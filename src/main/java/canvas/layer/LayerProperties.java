package canvas.layer;

import java.io.Serializable;

import name.LayerIdentifier;
/**
 * Represents all the properties a layer can have
 *
 */
public class LayerProperties implements Serializable {
    private static final long serialVersionUID = -5376511100707269511L;
    
    private LayerIdentifier layerIdentifier;
	private double opacity;
	private boolean visible;
	/**
	 * Constructor, starts layer at visible and full opacity
	 * @param layerIdentifier
	 */
	public LayerProperties(LayerIdentifier layerIdentifier){
		this.layerIdentifier = layerIdentifier;
		this.opacity = 1.0;
		this.visible=true;
	}
	/**
	 * Constructor with inputs of opacity and visibility
	 * @param layerIdentifier
	 * @param opacity
	 * @param visible
	 */
	private LayerProperties(LayerIdentifier layerIdentifier, double opacity, boolean visible) {
		this.layerIdentifier=layerIdentifier;
		this.opacity=opacity;
		this.visible=visible;
	}
	/**
	 * Updates the opacity
	 * @param opacity
	 */
	public void setOpacity(double opacity){
		this.opacity = opacity;
	}
	
	/**
	 * Updates visibility of layer
	 * @param visible
	 */
	public void setVisibility(Boolean visible){
		this.visible = visible;
	}
	
	/**
	 * Returns the layer's visibility
	 * @return
	 */
	public boolean getVisibility() {
		return this.visible;
	}
	
	/**
	 * Returns the layer's opacity
	 * @return
	 */
	public double getOpacity() {
		return this.opacity;
	}
	
	/**
	 * Returns the LayerIdentifier
	 * @return
	 */
	public LayerIdentifier layerIdentifier() {
		return layerIdentifier;
	}
	
	/**
	 * Returns the string of layer name
	 */
	public String toString() {
		return this.layerIdentifier.toString();
	}
	
	/**
	 * Returns a deep copy of the layer
	 */
	public LayerProperties clone() {
		return new LayerProperties(this.layerIdentifier, this.opacity, this.visible);
	}
	
	/**
	 * Creates a hash code of the properties
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((layerIdentifier == null) ? 0 : layerIdentifier.hashCode());
		long temp;
		temp = Double.doubleToLongBits(opacity);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (visible ? 1231 : 1237);
		return result;
	}

	

}
