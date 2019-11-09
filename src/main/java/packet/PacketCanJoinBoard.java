package packet;

import name.BoardIdentifier;
import name.ClientIdentifier;
import name.Identifiable;

/**
 * Class that represents a packet of a join board operation by a client
 *
 */
public final class PacketCanJoinBoard extends Packet {
    private static final long serialVersionUID = 3502327190158127229L;

    private final BoardIdentifier boardName;
    private final ClientIdentifier user;
    /**
     * Constructor from the boardName the client joined
     * @param boardName
     */
    public PacketCanJoinBoard(BoardIdentifier boardName, ClientIdentifier user) {
        this.boardName = boardName;
        this.user = user;
    }
    
    /**
     * Returns the boardName of the packet
     * @return boardName
     */
    public BoardIdentifier boardName() {
    	return boardName;
    }

    public ClientIdentifier user() { return user; }
    
    /**
     * Handles receiving a JoinBoard packet
     * 
     */
	@Override
	public void process(PacketHandler handler) {
		handler.receivedCanJoinBoardPacket(this);
	}
}
