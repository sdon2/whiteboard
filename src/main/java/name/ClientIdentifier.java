package name;
/**
 * Class that represents an identifier for a client
 *
 */
public final class ClientIdentifier extends Identifier {
	private static final long serialVersionUID = -4826118369750688172L;

	/**
	 * Constructor for client which takes client's id and name as params
	 * @param id
	 * @param name
	 */
	public ClientIdentifier(int id, String name) {
        super(id, name);
    }

}
