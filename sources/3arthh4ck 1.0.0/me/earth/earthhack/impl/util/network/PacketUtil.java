package me.earth.earthhack.impl.util.network;

import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.core.ducks.INetworkManager;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;

public class PacketUtil implements Globals
{
    public static Packet<?> sendPacketNoEvent(Packet<?> packet)
    {
        NetHandlerPlayClient connection = mc.getConnection();
        if (connection != null)
        {
            INetworkManager manager = (INetworkManager) connection.getNetworkManager();
            return manager.sendPacketNoEvent(packet);
        }

        return null;
    }

}
