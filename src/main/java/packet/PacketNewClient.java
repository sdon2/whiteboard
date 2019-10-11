package packet;

import name.ClientIdentifier;

/**
 * Class that represents a packet of new client information
 *
 */
public final class PacketNewClient extends Packet {
    private static final long serialVersionUID = -6603635078887371475L;
    
    private final ClientIdentifier senderName;
    
    /**
     * Constructor using the client's identifier name
     * @param senderName
     */
    public PacketNewClient(ClientIdentifier senderName) {
        super();
        this.senderName = senderName;
    }
    
    /**
     * Getter method for the client's name
     * @return ClientIdentifier senderName
     */
    public ClientIdentifier senderName() {
    	return senderName;
    }
    
    /**
     * Handles receiving a NewClient packet
     */
	@Override
	public void process(PacketHandler handler) {
		handler.receivedNewClientPacket(this);
	}
}
