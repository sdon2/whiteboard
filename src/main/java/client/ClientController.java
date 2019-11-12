package client;

import java.awt.Color;
import java.io.IOException;
import java.net.Socket;

import javax.swing.SwingUtilities;

import dialogs.ConfirmDialog;
import dialogs.ErrorDialog;
import models.BoardModel;
import name.BoardIdentifier;
import name.ClientIdentifier;
import name.LayerIdentifier;
import packet.*;
import server.SocketHandler;
import stroke.StrokeProperties;
import stroke.StrokeType;
import util.Utils;
import canvas.CanvasController;
import canvas.command.DrawCommand;
import canvas.layer.LayerAdjustment;
import canvas.layer.LayerProperties;

/**
 * Controller for client
 */
public class ClientController extends SocketHandler {
	private static final long serialVersionUID = 6673644308053728584L;
	
	private ClientGUI view;
    private final ClientIdentifier user;
    
    private ClientState clientState;
    private final StrokeProperties strokeProperties;
    
    /**
     * Constructor for ClientController
     * @param userName
     * @param hostName
     * @param portNumber
     * @throws IOException
     */
    public ClientController(String userName, String hostName, int portNumber) throws IOException {
        super(new Socket(hostName, portNumber));
        
        this.user = new ClientIdentifier(Utils.generateId(), userName);
        this.clientState = ClientState.IDLE;
        
        // Default stroke.
        this.strokeProperties = new StrokeProperties();
        
        // Send a NewClientPacket to announce yourself.
        sendPacket(new PacketNewClient(user));
    }


    public ClientIdentifier user() {
    	return user;
	}
    
    /**
     * Changes the view of the ClientGUI
     * @param view
     */
    public void setView(ClientGUI view) {
        assert this.view == null;
        
        this.view = view;
    }
	
    /**
     * Handles receiving a BoardModelPacket, and sets the model and DrawingController for the Client's boardd
     * appropriately
     */
    @Override
    public void receivedBoardModelPacket(PacketBoardModel packet) {
        assert model == null;
        assert out != null;
        
        // We should only receive these packets if are loading
        assert clientState == ClientState.IDLE;
        clientState = ClientState.PLAYING;
        
        CanvasController canvasController = new CanvasController(strokeProperties, this, packet.canvas());
        BoardModel newModel = new BoardModel(packet.boardName(), canvasController, packet.users(), packet.getOwner());
        this.model = newModel;
        this.model.setDrawingControllerDefault();
       
        sendPacket(new PacketClientReady());

        view.setModel(newModel);
    }
    
    /**
     * Handles receiving a BoardUsersPacket, which updates the user list
     */
	@Override
	public void receivedBoardUsersPacket(PacketBoardUsers packet) {
        assert model != null;
        assert out != null;
        
        // We should only receive these packets if we have loaded.
        assert clientState == ClientState.PLAYING;
        
        view.setUserList(packet.boardUsers());
	}
	
	/**
	 * Handles receiving a BoardIdentifierListPacket which updates the BoardList on the GUI
	 */
    @Override
    public void receivedBoardIdentifierListPacket(PacketBoardIdentifierList packet) {
        assert out != null;
        
        view.updateBoardList(packet.boards());
    }
    
    /**
     * Handles receiving a DrawCommandPacket and sends the command to the model
     */
    @Override
    public void receivedDrawCommandPacket(final PacketDrawCommand packet) {
        assert model != null;
        assert out != null;
        
        // We should only receive these packets if we have loaded.
        assert clientState == ClientState.PLAYING;
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                packet.drawCommand().drawOn(model);
            }
        });
    }
    
    /**
     * Handles receiving an ExitBoardPacket, which updates the model and the clientState
     */
	@Override
	public void receivedExitBoardPacket(PacketExitBoard packet) {
		assert model != null;
		assert clientState == ClientState.PLAYING;
		clientState = ClientState.IDLE;
		model = null;
	}
	
	/**
	 * Handles receiving a MessagePacket and updates the chat client
	 */
	@Override
	public void receivedMessagePacket(PacketMessage packet) {
		view.addChatLine(packet.text());
	}

	/**
	 * Handles receiving a LayerAdjustmentPacket which updates the LayerOrder for the GUI
	 */
	@Override
	public void receivedLayerAdjustmentPacket(
			PacketLayerAdjustment packetLayerOrderList) {
		assert model != null;
		assert clientState == ClientState.PLAYING;
		model.adjustLayer(packetLayerOrderList.layerProperties(), packetLayerOrderList.adjustment());
		setGUILayers();
	}
	
	/**
	 * Handles receiving a NewLayerPacket and updating the model and GUI
	 */
	@Override
	public void receivedNewLayerPacket(PacketNewLayer packetNewLayer) {
		assert model != null;
		assert clientState == ClientState.PLAYING;
		model.addLayer(packetNewLayer.layerName());
		setGUILayers();
	}
	
	/**
	 * Updates the GUI's layers
	 */
	public void setGUILayers() {
		if (model != null) {
	        view.setLayers(model.canvas().layers());
		}
	}
	
    /*
     * Methods for sending packets.
     */
	/**
	 * Sends a NewBoardPacket to server
	 * @param boardName
	 * @param width
	 * @param height
	 */
	public void generateNewBoard(BoardIdentifier boardName, int width, int height) {
    	if (model != null) {
            disconnectFromCurrentBoard();
    	}
		PacketNewBoard packet = new PacketNewBoard(boardName, width, height, this.user);
		sendPacket(packet);
	}
	
	/**
	 * Sends a JoinBoardPacket to server
	 * @param boardName
	 */
    public void connectToBoard(BoardIdentifier boardName) {
    	if (model != null) {
            disconnectFromCurrentBoard();
    	}
    	
        PacketCanJoinBoard packet = new PacketCanJoinBoard(boardName, this.user);
        sendPacket(packet);
    }
    
    /**
     * Disconnect from the current board.
     */
    private void disconnectFromCurrentBoard() {
        assert model != null;
        
        PacketExitBoard packet = new PacketExitBoard();
        sendPacket(packet);
    }
    
    /**
     * Sends DrawCommand to server
     * @param drawCommand
     */
    public void sendDrawCommand(DrawCommand drawCommand) {
        assert model != null;
        PacketDrawCommand packet = new PacketDrawCommand(drawCommand);
        sendPacket(packet);
    }
    
    /**
     * Sends message to server
     * @param text
     */
	public void sendMessage(String text) {
		assert model != null;
		
		PacketMessage packet = new PacketMessage(user.name()+ ": " + text);
		sendPacket(packet);
	}
	
	/**
	 * Sets the Stroke color of the current brush
	 * @param strokeColor
	 */
    public void setStrokeColor(Color strokeColor) {
    	strokeProperties.setStrokeColor(strokeColor);
    }
    
    /**
     * Sets the stroke width
     * @param strokeWidth
     */
    public void setStrokeWidth(int strokeWidth) {
        strokeProperties.setStrokeWidth(strokeWidth);
    }
    
    /**
     * Updates the strokeType
     * @param newStrokeType
     */
    public void setStrokeType(StrokeType newStrokeType) {
        strokeProperties.setStrokeType(newStrokeType);
    }
    
    /**
     * Method to turn eraser on/off
     * @param eraserOn
     */
    public void setEraserOn(boolean eraserOn) {
        strokeProperties.setEraserOn(eraserOn);
    }
    
    /**
     * Method to turn fill on/off
     * @param fillOn
     */
    public void setFillOn(boolean fillOn) {
        strokeProperties.setFillOn(fillOn);
    }
    
    /**
     * Updates symmetry level
     * @param symetry
     */
    public void setSymetry(int symetry) {
        strokeProperties.setSymetry(symetry);
    }
    
    /**
     * Adds a layer to the Board and sends a NewLayerPacket to the server
     * @param identifier
     */
    public void addLayer(LayerIdentifier identifier) {
    	sendPacket(new PacketNewLayer(identifier));
    }
    
    /**
     * Sends LayerAdjustmentPacket to the server
     * @param layerProperties
     * @param adjustment
     */
    public void adjustLayer(LayerProperties layerProperties, LayerAdjustment adjustment) {
    	sendPacket(new PacketLayerAdjustment(layerProperties, adjustment));
    }
    
    /**
     * Handles receiving a NewClientPacket, which should never happen as it's only sent client to server
     */
	@Override
	public void receivedNewClientPacket(PacketNewClient packet) {
		assert false;
	}

	/**
	 * Handles receiving a NewBoardPacket, which should never happen as it's only sent client to server
	 */
	@Override
	public void receivedNewBoardPacket(PacketNewBoard packet) {
		assert false;
	}
	
	/**
	 * Handles receiving a ClientReadyPacket, which should never happen as it's only sent client to server
	 */
	@Override
	public void receivedClientReadyPacket(PacketClientReady packet) {
		assert false;
	}
	
	/**
	 * Handles receiving a JoinBoardPacket, which should never happen as it's only sent client to server
	 */
	@Override
	public void receivedJoinBoardPacket(PacketJoinBoard packet) {
		assert false;
	}

    /**
     * Handles receiving a JoinBoardPacket, which should never happen as it's only sent client to server
     */
    @Override
    public void receivedCanJoinBoardPacket(PacketCanJoinBoard packet) {
		ConfirmDialog dialog = new ConfirmDialog(this.view, "Confirm User Joining Request", packet.user().identifier().name() + " wants to join your board! Would you allow him/her to join?");
		PacketCanJoinBoardResult result = new PacketCanJoinBoardResult(packet.boardName(), packet.user(), dialog.result());
		sendPacket(result);
    }

    /**
     * Handles receiving a JoinBoardPacket, which should never happen as it's only sent client to server
     */
    @Override
    public void receivedCanJoinBoardResultPacket(PacketCanJoinBoardResult result) {
		if (model != null) {
			disconnectFromCurrentBoard();
		}
		if (!result.result()) {
		    ErrorDialog dialog = new ErrorDialog(this.view, "Request Result", "Owner of this board rejected your request");
		    dialog.show();
            this.view.setBoardTitle(this.view.title);
		    return;
        }

		this.view.setBoardTitle(result.boardName().name() + " : " + this.user.name() + " [CLIENT]");
		PacketJoinBoard packet = new PacketJoinBoard(result.boardName(), this.user);
		sendPacket(packet);
    }
	
	/**
	 * Returns board width
	 * @return width
	 */
	public int getBoardWidth() {
		return this.model.width();
	}
	
	/**
	 * Returns board height
	 * @return height
	 */
	public int getBoardHeight() {
		return this.model.height();
	}
	
	/**
	 * Returns selected layer
	 * @return LayerIdentifier
	 */
	public LayerIdentifier selectedLayer() {
		return view.selectedLayer().layerIdentifier();
	}
}
