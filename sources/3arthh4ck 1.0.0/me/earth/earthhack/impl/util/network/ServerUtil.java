package me.earth.earthhack.impl.util.network;

import me.earth.earthhack.api.util.Globals;
import me.earth.earthhack.impl.modules.client.pingbypass.PingBypass;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.text.TextComponentString;

public class ServerUtil implements Globals
{
    public static void disconnectFromMC()
    {
        NetHandlerPlayClient connection = mc.getConnection();
        if(connection != null)
        {
            connection.getNetworkManager().closeChannel(new TextComponentString("Quitting"));
        }
    }

    public static int getPing()
    {
        if (PingBypass.getInstance().isEnabled())
        {
            return PingBypass.getInstance().getServerPing();
        }

        NetHandlerPlayClient connection = mc.getConnection();
        if (connection != null)
        {
            NetworkPlayerInfo info = connection.getPlayerInfo(mc.getConnection().getGameProfile().getId());
            //noinspection ConstantConditions
            if (info != null)
            {
                return info.getResponseTime();
            }
        }

        return 0;
    }

}
