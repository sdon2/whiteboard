package packet;

/**
 * Class that represents a packet with an exit board command 
 *
 */
public final class PacketExitBoard extends Packet {
	private static final long serialVersionUID = -1888945253238858839L;
	
	/**
	 * Handles receiving an ExitBoard packet
	 */
	@Override
	public void process(PacketHandler handler) {
		handler.receivedExitBoardPacket(this);
	}
}