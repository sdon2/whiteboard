package name;
/**
 * Class that represents an identifier for a Board
 * Contains a NULL_BOARD that client connects to automatically when they connect to the server
 *
 */
public final class BoardIdentifier extends Identifier {
	private static final long serialVersionUID = -7136909813853854025L;
	private final ClientIdentifier owner;
	
	public static final BoardIdentifier NULL_BOARD = new BoardIdentifier(0, "", null);
	/**
	 * Constructor for BoardIdentifier which takes id and name as arguments
	 * @param id
	 * @param name
	 */
    public BoardIdentifier(int id, String name, ClientIdentifier owner) {
        super(id, name);
        this.owner = owner;
    }

    public ClientIdentifier owner() {
    	return owner;
	}

}
