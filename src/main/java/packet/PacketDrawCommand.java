package packet;

import canvas.command.DrawCommand;

/**
 * Class that represents a DrawCommand packet
 *
 */
public final class PacketDrawCommand extends Packet {
    private static final long serialVersionUID = 8957926132249921422L;
    private final DrawCommand drawCommand;

    public PacketDrawCommand(DrawCommand drawCommand) {
        this.drawCommand = drawCommand;
    }
    
    public DrawCommand drawCommand() {
        return drawCommand;
    }

    /**
     * Handles receiving a DrawCommandPacket
     */
	@Override
	public void process(PacketHandler handler) {
		handler.receivedDrawCommandPacket(this);
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((drawCommand == null) ? 0 : drawCommand.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PacketDrawCommand other = (PacketDrawCommand) obj;
        if (drawCommand == null) {
            if (other.drawCommand != null)
                return false;
        } else if (!drawCommand.equals(other.drawCommand))
            return false;
        return true;
    }
	
	
}
    