package packet;

import name.BoardIdentifier;
/**
 * Class that represents a packet of all the boards currently known to the server
 *
 */
public final class PacketBoardIdentifierList extends Packet {
    private static final long serialVersionUID = 3621326113552483907L;
    private final BoardIdentifier[] boards;
    
    /**
     * Constructor that creates a Packet of all the boards
     * @param boards
     */
    public PacketBoardIdentifierList(BoardIdentifier[] boards) {
        this.boards = boards;
    }
    
    /**
     * @return clone of list of boards
     */
    public BoardIdentifier[] boards() {
        return boards.clone();
    }

    /**
     * Handles receiving a BoardIdentifierList packet
     */
	@Override
	public void process(PacketHandler handler) {
		handler.receivedBoardIdentifierListPacket(this);
	}
}
