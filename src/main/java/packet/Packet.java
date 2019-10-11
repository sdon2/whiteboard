package packet;

import java.io.Serializable;
/**
 * Abstract class Packet which is used to send and receive data from client/server
 * Each packet represents a different type of data sent between client/server
 *
 */
public abstract class Packet implements Serializable {
	private static final long serialVersionUID = -2598835099616000879L;
	
	/**
	 * Abstract method to handle processing of Packets
	 * @param handler
	 */
	public abstract void process(PacketHandler handler);
	
	
}
