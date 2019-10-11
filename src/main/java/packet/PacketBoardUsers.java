package packet;

import name.Identifiable;
/**
 * Class that represents a packet of all the users of a Board
 *
 */
public class PacketBoardUsers extends Packet {
	private static final long serialVersionUID = 6138270598524186918L;
	
	private final Identifiable[] users;
    /**
     * Constructor that creates a packet from all the users
     * @param users
     */
    public PacketBoardUsers(Identifiable[] users) {
        this.users = users;
    }
    /**
     * Getter method for the users associated with the board
     * @return Identifiable[] users
     */
    public Identifiable[] boardUsers() {
        return users;
    }
    
    /**
     * Handles receiving a BoardUsersPacket
     */
	@Override
	public void process(PacketHandler handler) {
		handler.receivedBoardUsersPacket(this);
	}

}
