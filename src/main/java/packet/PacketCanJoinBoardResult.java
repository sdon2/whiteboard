package packet;

import name.BoardIdentifier;
import name.ClientIdentifier;
import name.Identifiable;

/**
 * Class that represents a packet of a join board operation by a client
 *
 */
public final class PacketCanJoinBoardResult extends Packet {
    private static final long serialVersionUID = 3502327190158127229L;

    private final BoardIdentifier boardName;
    private final ClientIdentifier user;
    private final boolean result;

    /**
     * Constructor from the boardName the client joined
     * @param boardName
     */
    public PacketCanJoinBoardResult(BoardIdentifier boardName, ClientIdentifier user, boolean result) {
        this.boardName = boardName;
        this.user = user;
        this.result = result;
    }
    
    /**
     * Returns the boardName of the packet
     * @return boardName
     */
    public BoardIdentifier boardName() {
    	return boardName;
    }

    public ClientIdentifier user() { return user; }

    public boolean result() { return result; }
    
    /**
     * Handles receiving a JoinBoard packet
     * 
     */
	@Override
	public void process(PacketHandler handler) {
		handler.receivedCanJoinBoardResultPacket(this);
	}
}
