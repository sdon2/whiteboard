package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;

import models.BoardModel;
import name.Identifiable;
import name.Identifier;
import packet.Packet;
import packet.PacketHandler;
/**
 * Abstract class SocketHandler that handles all connections between server and client over sockets
 *
 */
public abstract class SocketHandler 
	implements PacketHandler, Runnable, Identifiable, Serializable 
{
	private static final long serialVersionUID = -2411978741777099054L;

	private transient final Socket socket;

    protected transient final ObjectOutputStream out;
    protected transient final ObjectInputStream in;
    protected transient BoardModel model;
    protected Identifier identifier;
    
    /**
     * Constructor that creates the input and output streams on the socket
     * @param socket
     * @throws IOException
     */
    protected SocketHandler(Socket socket) throws IOException {
        this.socket = socket;
        
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }
    
    /**
     * Runs the SocketHandler and closes the socket if we ever stop handling the connection or it breaks
     */
    @Override
    public final void run() {
        try {
            handleConnection();
        } catch (IOException e) {
            e.printStackTrace(); // but don't terminate the serve()
        } finally {
            // close the socket at the end
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * Handles the connection between server and client by reading in the objects coming on the input
     * Object stream
     * @throws IOException
     */
    protected final void handleConnection() throws IOException {
        try {
            for (Object obj = in.readObject(); obj != null; obj = in.readObject()) {
            	((Packet)obj).process(this);
            }
        } 
        catch (EOFException e) {
        }
        catch (SocketException e) {
        }
        catch (Exception e) {
        	e.printStackTrace();
        }
        finally {
            connectionClosed();
            out.close();
            in.close();
        }   
    }
    
    /**
     * Closes the connection
     */
    protected void connectionClosed() { }

    /**
     * Sends packet across the outputObjectStream
     * @param packet
     */
    public synchronized void sendPacket(Packet packet) {
        try {
            out.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Returns the identifier
     */
    @Override
    public Identifier identifier() {
        return identifier;
    }
}
