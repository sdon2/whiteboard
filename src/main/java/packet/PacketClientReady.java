package packet;
/**
 * Class representing a packet that a client is ready to accept drawing commands
 *
 */
public class PacketClientReady extends Packet {
	private static final long serialVersionUID = 5713340681218216132L;
	
	/**
	 * Handles receiving a ClientReadyPacket
	 */
	@Override
	public void process(PacketHandler handler) {
		handler.receivedClientReadyPacket(this);
	}
}
