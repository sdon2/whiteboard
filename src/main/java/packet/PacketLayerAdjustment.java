package packet;

import name.LayerIdentifier;
import canvas.layer.LayerAdjustment;
import canvas.layer.LayerProperties;

/**
 * Represents a LayerAdjustmentPacket, which says that we've changed the properties or the orders
 * of the layers
 *
 */
public class PacketLayerAdjustment extends Packet {
	private static final long serialVersionUID = 5126362530793078834L;
	private final LayerAdjustment adjustment;
	private final LayerProperties properties;
	
	/**
	 * Constructor for packet with properties and the adjustment
	 * @param properties
	 * @param adjustment
	 */
	public PacketLayerAdjustment(LayerProperties properties, LayerAdjustment adjustment){
		this.properties = properties;
		this.adjustment = adjustment;
	}
	
	/**
	 * Returns LayerProperties
	 * @return LayerProperties
	 */
	public LayerProperties layerProperties() {
		return properties;
	}
	
	/**
	 * Returns LayerIdentifier associated with properties
	 * @return LayerIdentifier
	 */
	public LayerIdentifier layer() {
		return properties.layerIdentifier();
	}
	
	/**
	 * 
	 * @return LayerAdjustment
	 */
	public LayerAdjustment adjustment() {
		return adjustment;
	}
	
	/**
	 * Handles receiving a LayerAdjustmentPacket
	 */
	@Override
	public void process(PacketHandler handler) {
		handler.receivedLayerAdjustmentPacket(this);
	}
	
}
