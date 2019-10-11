package packet;
/**
 * Class that represents a packet with a message, used for chat
 *
 */
public class PacketMessage extends Packet {
	private static final long serialVersionUID = 678813893543304091L;
	
	private final String text;
	
	/**
	 * Constructor of packet with the message of the text
	 * @param text
	 */
	public PacketMessage(String text) {
		this.text = text;
	}
	
	/**
	 * Returns the message of the packet
	 * @return String text
	 */
	public String text() {
		return text;
	}
	
	/**
	 * Handles receiving a Message packet
	 */
	@Override
	public void process(PacketHandler handler) {
		handler.receivedMessagePacket(this);
	}
}
