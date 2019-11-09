package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import models.BoardModel;
import name.BoardIdentifier;
import name.ClientIdentifier;
import name.Identifiable;
import name.Identifier;
import packet.Packet;
import packet.PacketBoardIdentifierList;
import packet.PacketJoinBoard;
import packet.PacketNewBoard;
import canvas.CanvasController;
/**
 * Class that runs the server that handles all the boards and the clients connecting to each board
 * Current default port is 4444, stores a Map of the BoardIdentifier to the BoardModels and a Set of all the clients as
 * ServerSocketHandlers
 *
 */
public class Server implements Identifiable {
    public static final int DEFAULT_PORT = 4444;
    
	private final ServerSocket serverSocket; 

	private final Map<BoardIdentifier, BoardModel> boards;
    private final Set<ServerSocketHandler> clients;
	 
    /**
     * Constructor for Server using only the port
     * @param port
     * @throws IOException
     */
	public Server(int port) throws IOException {
		this.serverSocket = new ServerSocket(port);
		this.boards = Collections.synchronizedMap(new HashMap<BoardIdentifier, BoardModel>());
        this.clients = Collections.synchronizedSet(new HashSet<ServerSocketHandler>());
	}
	/**
	 * Serves each new connection to the server by starting it in a new thread
	 * @throws IOException
	 */
	public void serve() throws IOException {
		while (true) {
			final Socket socket = serverSocket.accept();
			ServerSocketHandler handler = new ServerSocketHandler(socket, this);
            clients.add(handler);
            
            new Thread(handler).start();
            
            // Update the new client's list of boards.
            handler.sendPacket(constructBoardIdentifierListPacket());
		}
	}
	
	/**
	 * Removes client from set of client ServerSocketHandlers
	 * @param handler
	 */
    public void removeClient(ServerSocketHandler handler) {
        clients.remove(handler);
    }
    
    /**
     * Adds board to the Map of current boards
     * @param boardName
     * @param model
     */
    private void addBoard(BoardIdentifier boardName, BoardModel model) {
        assert !boards.containsKey(boardName);
        boards.put(boardName, model);
    }
    
    /**
     * Broadcast a packet to all clients.
     * @param packet
     */
    private void broadcastPacketToAllClients(Packet packet) {
        synchronized(clients) {
            for (ServerSocketHandler handler : clients) {
                handler.sendPacket(packet);
            }
        }
    }

    /**
     * Broadcast a packet to particular client.
     * @param packet
     */
    public void broadcastPacketToClient(Packet packet, ClientIdentifier client) {
        synchronized(clients) {
            for (ServerSocketHandler handler : clients) {
                if (handler.identifier.id() == client.id()) {
                    handler.sendPacket(packet);
                }
            }
        }
    }
    
    /**
     * Creates a packet of all the current Boards known to the server
     * @return
     */
    private Packet constructBoardIdentifierListPacket() {
        return new PacketBoardIdentifierList(boards.keySet().toArray(new BoardIdentifier[boards.keySet().size()]));
    }
    
    /**
     * Notifies all clients of current board list
     */
    public void notifyBoardListChanged() {
        broadcastPacketToAllClients(constructBoardIdentifierListPacket());
    }
    
    /**
     * Constructor for BoardModel on server that creates models under boardName too
     * @param packet
     * @return
     */
    public BoardModel newBoard(PacketNewBoard packet) {
        BoardIdentifier boardName = packet.boardName();
        int width = packet.width();
        int height = packet.height();
        
        // Create a new model under this boardName.
        CanvasController canvas = new CanvasController(width, height);
        BoardModel model = new BoardModel(boardName, canvas, packet.owner());
        addBoard(boardName, model);
        
        return model;
    }
    
    /**
     * Join board operation by the client, handled on the server
     * @param packet
     * @return
     */
    public BoardModel joinBoard(PacketJoinBoard packet) {
        BoardIdentifier boardName = packet.boardName();
        BoardModel model = boards.get(boardName);
        
        return model;
    }
    
    /**
     * Getter method for identifier in Server
     */
    @Override
    public Identifier identifier() {
    	throw new UnsupportedOperationException();
    }
    
    /**
     * Main method to run the server
     * @param args
     */
    public static void main(String[] args) {
        int port = DEFAULT_PORT;
        
        if (args.length > 0)
            port = Integer.parseInt(args[0].substring(2));
        try {
            Server server = new Server(port);
            System.out.println("Server Started at localhost:" + port);
            server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
