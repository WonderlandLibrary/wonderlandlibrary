package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.IServerPlayNetHandler;

public class CClientStatusPacket implements IPacket<IServerPlayNetHandler>
{
    private State status;

    public CClientStatusPacket()
    {
    }

    public CClientStatusPacket(State status)
    {
        this.status = status;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.status = buf.readEnumValue(State.class);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeEnumValue(this.status);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(IServerPlayNetHandler handler)
    {
        handler.processClientStatus(this);
    }

    public State getStatus()
    {
        return this.status;
    }

    public static enum State
    {
        PERFORM_RESPAWN,
        REQUEST_STATS;
    }
}
