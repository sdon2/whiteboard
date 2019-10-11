package packet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import name.BoardIdentifier;

import org.junit.Test;

public class PacketTest {
    
      @Test
          public void joinBoardPacketConstTest() {
              // test creating a JoinBoardPacket
          PacketJoinBoard packet = new PacketJoinBoard(new BoardIdentifier(4, "board"));
          assertTrue(packet.boardName().id() == 4);
          assertTrue(packet.boardName().name().equals("board"));
      }
  
    @Test
    public void boardStatePacketConstTest() {
        //Test constructing a boardStatePacket
        BoardIdentifier[] boards = new BoardIdentifier[2];
        
        boards[0] = new BoardIdentifier(4, "name1");
        boards[1] = new BoardIdentifier(7, "name2");
        
        PacketBoardIdentifierList packet = new PacketBoardIdentifierList(boards);   
        assertArrayEquals(packet.boards(), boards);
    }
}
