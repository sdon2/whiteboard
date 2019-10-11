package packet;
/**
 * Interface to handle different types of packets
 * 
 *
 */
public interface PacketHandler {
    /**
     * Methods for receiving specific packets. Add "assert false" if the implementation does not support 
     * receiving the packet.
     * @param packet
     */
     void receivedNewClientPacket(PacketNewClient packet);
     void receivedNewBoardPacket(PacketNewBoard packet);
     void receivedClientReadyPacket(PacketClientReady packet);
     void receivedJoinBoardPacket(PacketJoinBoard packet);
     void receivedExitBoardPacket(PacketExitBoard packet);
     void receivedBoardModelPacket(PacketBoardModel packet);
     void receivedBoardUsersPacket(PacketBoardUsers packet);
     void receivedBoardIdentifierListPacket(PacketBoardIdentifierList packet);
     void receivedDrawCommandPacket(PacketDrawCommand packet);
     void receivedMessagePacket(PacketMessage packet);
     void receivedLayerAdjustmentPacket(PacketLayerAdjustment packetLayerOrderList);
     void receivedNewLayerPacket(PacketNewLayer packetNewLayer);
}
