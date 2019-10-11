package packet;

import models.BoardModel;
import name.BoardIdentifier;
import name.Identifiable;
import canvas.Canvas;
/**
 * Class that represents a packet of the current BoardModel
 *
 */
public final class PacketBoardModel extends Packet {
    private static final long serialVersionUID = 8105009453719679025L;

    private final BoardIdentifier boardName;
    private final Canvas canvas;
    private final Identifiable[] users;
    
    /**
     * Constructor that makes a Packet from a BoardModel
     * @param boardModel
     */
    public PacketBoardModel(BoardModel boardModel) {
        this.boardName = boardModel.identifier();
        this.canvas = boardModel.canvas();
        this.users = boardModel.users();
 
    }
    /**
     * Getter method to get the BoardModel
     * @return BoardModel
     */
    public BoardIdentifier boardName() {
        return boardName;
    }
    
    public Canvas canvas() {
        return canvas;
    }
    
    public Identifiable[] users() {
        return users;
    }
    
    /**
     * Handler receiving a BoardModelPacket
     * 
     */
	@Override
	public void process(PacketHandler handler) {
		handler.receivedBoardModelPacket(this);
	}
}
