package me.earth.earthhack.impl.core.ducks;

import net.minecraft.network.Packet;

public interface INetworkManager
{
    /**
     * Sends a Packet without creating PacketEvent.Send.
     * Note that a PacketEvent.Post will be created.
     *
     * @param packetIn the packet to send.
     * @return the packet sent, or <tt>null</tt> if the channel is closed.
     */
    Packet<?> sendPacketNoEvent(Packet<?> packetIn);

}
