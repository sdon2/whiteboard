package packet;

import name.BoardIdentifier;

/**
 * Class that represents a packet with all the information from client of a new board
 *
 */
public final class PacketNewBoard extends Packet {
    private static final long serialVersionUID = -3720287069109037134L;
    private final BoardIdentifier boardName;
    private final int width;
    private final int height;
    
    /**
     * Constructor for packet
     * @param boardName
     * @param width
     * @param height
     */
    public PacketNewBoard(BoardIdentifier boardName, int width, int height) {
    	this.boardName = boardName;
        this.width = width;
        this.height = height;
    }
    
    /**
     * Getter method for boardName
     * @return BoardIdentifier boardName
     */
    public BoardIdentifier boardName() {
    	return boardName;
    }
    
    /**
     * Getter method for width of board
     * @return width
     */
    public int width() {
        return width;
    }
    
    /**
     * Getter method for height of board
     * @return height
     */
    public int height() {
        return height;
    }
    
    
    /**
     * Handles receiving a NewBoard packet
     */
	@Override
	public void process(PacketHandler handler) {
		handler.receivedNewBoardPacket(this);
	}
}
