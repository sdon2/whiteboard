package server;

import java.io.IOException;
import java.net.Socket;

import name.Identifiable;
import packet.Packet;
import packet.PacketBoardIdentifierList;
import packet.PacketBoardModel;
import packet.PacketBoardUsers;
import packet.PacketClientReady;
import packet.PacketDrawCommand;
import packet.PacketExitBoard;
import packet.PacketJoinBoard;
import packet.PacketLayerAdjustment;
import packet.PacketMessage;
import packet.PacketNewBoard;
import packet.PacketNewClient;
import packet.PacketNewLayer;
import canvas.command.DrawCommand;

/**
 * The socket created when a client joins the server. Handles communication with
 * just one client, and talks to the |Server| to receive information about the board.
 * @author Matthew
 *
 */
public class ServerSocketHandler extends SocketHandler {
	private static final long serialVersionUID = 5321337579877366643L;
	
	private final transient Server server;
    private transient ServerSocketState state;
    
    /**
     * Constructor which creates SocketHandler for server
     * @param socket
     * @param server
     * @throws IOException
     */
    public ServerSocketHandler(Socket socket, Server server) throws IOException {
        super(socket);
        this.server = server;
        this.state = ServerSocketState.INITIALIZING;
    }
    
    /**
     * Disconnects a client from the server
     */
    @Override 
    protected void connectionClosed() {
        server.removeClient(this);
        if (model != null && model.containsUser(this)) {
        	model.removeUser(this);
        }
    }
    
    /**
     * Handles receiving a NewClientPacket
     */
    @Override
    public void receivedNewClientPacket(PacketNewClient packet) {
        assert this.identifier == null;
        assert state == ServerSocketState.INITIALIZING;
        state = ServerSocketState.IDLE;
        
        this.identifier = packet.senderName();
    }
    
    /**
     * Handles receiving a NewBoardPacket and adds it to the server
     */
    @Override
    public void receivedNewBoardPacket(PacketNewBoard packet) {
        assert model == null;
        model = server.newBoard(packet);
        
        // Tell the user to start his board.
        sendPacket(new PacketBoardModel(model));
        
        // Announce that a new board has been added.
        server.notifyBoardListChanged();    
    }
    
    /**
     * Handles receiving a JoinBoardPacket and adds the client to the server
     * returns BoardModelPacket to the clients
     */
    @Override
    public void receivedJoinBoardPacket(PacketJoinBoard packet) {
        assert model == null;
        // First send a GameState packet, then start sending the client new pixel locations.
        model = server.joinBoard(packet);

        sendPacket(new PacketBoardModel(model));
    }
    
    /**
     * Handles receiving an ExitBoardPacket and remove client from the board
     * Notify all clients that BoardUsers have changed
     */
    @Override
    public void receivedExitBoardPacket(PacketExitBoard packet) {
        assert state == ServerSocketState.PLAYING;
        state = ServerSocketState.IDLE;
        
        assert model != null;

        model.removeUser(this);
        sendPacket(new PacketExitBoard());
        notifyBoardUsersChanged();
        model = null;
    }
    /**
     * Notifies all clients in Board of current users
     */
	private void notifyBoardUsersChanged() {
		assert model != null;
		broadcastPacketToBoard(new PacketBoardUsers(model.users()));
	}
	
	/**
	 * Handles receiving a ClientReadyPacket
	 */
	@Override
	public void receivedClientReadyPacket(PacketClientReady packet) {
        assert state == ServerSocketState.IDLE;
        state = ServerSocketState.PLAYING;
        
		model.addUser(this);
        notifyBoardUsersChanged();
	}
	
	/**
	 * Handles receiving a DrawCommandPacket
	 */
    @Override
    public void receivedDrawCommandPacket(PacketDrawCommand packet) {
        assert state == ServerSocketState.PLAYING;
        assert model != null;
        
        DrawCommand command = packet.drawCommand();
        assert command != null;
        command.drawOn(model);
        
        broadcastPacketToBoard(packet);
    }
    /**
     * Handles receiving a MessagePacket and broadcasting it to all the users
     * to update the chat client
     */
	@Override
	public void receivedMessagePacket(PacketMessage packet) {
		broadcastPacketToBoard(packet);
	}

	/**
	 * Handles receiving a LayerAdjustmentPacket and then broadcasting the new layering ordering
	 * to all the clients on the current Board
	 */
	@Override
	public void receivedLayerAdjustmentPacket(
			PacketLayerAdjustment packet) {
        assert state == ServerSocketState.PLAYING;
        assert model != null;
		model.adjustLayer(packet.layerProperties(), packet.adjustment());
		broadcastPacketToBoard(packet);
	}
	
	/**
	 * Handles receiving a NewLayerPacket and adding the layer to the model and then sending it out
	 * to the other clients on the Board
	 */
	@Override
	public void receivedNewLayerPacket(PacketNewLayer packet) {
        assert state == ServerSocketState.PLAYING;
        assert model != null;
		model.addLayer(packet.layerName());
		broadcastPacketToBoard(packet);
	}
	
	/**
	 * Handles Receiving a BoardModelPacket, which should never happen because these should only be
	 * handled by the clients, never by the server
	 */
    @Override
    public void receivedBoardModelPacket(PacketBoardModel packet) {
        // We only send GameStatePackets from the server.
        assert false;
    }
    
    /**
     * Handles receiving a BoardIdentifierListPacket which should never happen because these are
     * handled by the clients, never by the server
     */
    @Override
    public void receivedBoardIdentifierListPacket(PacketBoardIdentifierList packet) {
        // We only send BoardStatePackets from the server.
        assert false;
    }
    
    /**
     * Handles receiving a BoardUsersPacket which is asserted false because that is only server
     * to client
     */
	@Override
	public void receivedBoardUsersPacket(PacketBoardUsers packet) {
		// Only server to client.
		assert false;
	}
	
    /**
     * Broadcast a packet to all the clients of a specific model.
     * @param model
     * @param packet
     */
    private void broadcastPacketToBoard(Packet packet) {
        for (Identifiable handler : model.users()) {
            ((ServerSocketHandler) handler).sendPacket(packet);
        }
    }
}