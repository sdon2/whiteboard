package packet;

import name.LayerIdentifier;
/**
 * Class that represents a packet a new layer created
 *
 */
public class PacketNewLayer extends Packet {
	
	private static final long serialVersionUID = 2258037891058384987L;
	private final LayerIdentifier layerName;
	
	/**
	 * Constructor for packet
	 * @param layerName
	 */
	public PacketNewLayer(LayerIdentifier layerName){
		this.layerName = layerName;
	}
	
	/**
	 * Returns LayerIdentifier of the layer
	 * @return LayerIdentifier
	 */
	public LayerIdentifier layerName() {
		return layerName;
	}
	
	/**
	 * Sends packet to Handler to process receiving NewLayerPacket
	 */
	@Override
	public void process(PacketHandler handler) {
		handler.receivedNewLayerPacket(this);	
	}
	

}
