package packet;

import name.BoardIdentifier;
import name.Identifiable;

/**
 * Class that represents a packet of a join board operation by a client
 *
 */
public final class PacketJoinBoard extends Packet {
    private static final long serialVersionUID = 3502327190158127229L;

    private final BoardIdentifier boardName;
    private Identifiable user;
    /**
     * Constructor from the boardName the client joined
     * @param boardName
     */
    public PacketJoinBoard(BoardIdentifier boardName, Identifiable user) {
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
    
    /**
     * Handles receiving a JoinBoard packet
     * 
     */
	@Override
	public void process(PacketHandler handler) {
		handler.receivedJoinBoardPacket(this);
	}
}
