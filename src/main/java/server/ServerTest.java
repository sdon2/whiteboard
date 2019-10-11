package server;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import name.BoardIdentifier;
import name.ClientIdentifier;

import org.junit.Before;
import org.junit.Test;

import packet.Packet;
import packet.PacketBoardIdentifierList;
import packet.PacketBoardModel;
import packet.PacketBoardUsers;
import packet.PacketClientReady;
import packet.PacketNewBoard;
import packet.PacketNewClient;
import util.TestUtil;

/**
 * End to tend tests for the server connection protocol.
 * @author Matthew
 * @category no_didit
 */

public class ServerTest {
    private static final ClientIdentifier ID = new ClientIdentifier(0, "Name");
    private static final BoardIdentifier BOARD_ID = new BoardIdentifier(1, "Board Name");
    
    @Before
    public void setUp() {
        TestUtil.startServer();
    }

    @Test(timeout = 10000)
    public void connectTest() {
        // Test connecting to a server receiving the correct packets.
        try {
            Socket connect = TestUtil.connect();
            ObjectOutputStream out = new ObjectOutputStream(connect.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(connect.getInputStream());
            
            Packet packet = new PacketNewClient(ID);
            out.writeObject(packet);
            
            Packet inPacket = TestUtil.nextPacket(in);
            assertTrue(inPacket instanceof PacketBoardIdentifierList);
            
            packet = new PacketNewBoard(BOARD_ID, 256, 256);
            out.writeObject(packet);
            inPacket = TestUtil.nextPacket(in);
            assertTrue(inPacket instanceof PacketBoardModel);
            
            PacketBoardModel boardPacket = (PacketBoardModel) inPacket;
            assertTrue(boardPacket.boardName().equals(BOARD_ID));
            assertTrue(boardPacket.canvas().width() == 256);
            assertTrue(boardPacket.canvas().height() == 256);
            
            packet = new PacketClientReady();
            out.writeObject(packet);
            inPacket = TestUtil.nextPacket(in);
            assertTrue(inPacket instanceof PacketBoardIdentifierList);
            inPacket = TestUtil.nextPacket(in);
            assertTrue(inPacket instanceof PacketBoardUsers);
        } catch (IOException e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }
}
