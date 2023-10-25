package net.minecraft.network;

import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.src.Config;
import net.minecraft.util.IThreadListener;

public class PacketThreadUtil
{
    public static int lastDimensionId = Integer.MIN_VALUE;

    public static <T extends INetHandler> void checkThreadAndEnqueue(final Packet<T> packetIn, final T processor, IThreadListener scheduler) throws ThreadQuickExitException
    {
        if (!scheduler.isCallingFromMinecraftThread())
        {
            scheduler.addScheduledTask(new Runnable()
            {
                public void run()
                {
                    PacketThreadUtil.clientPreProcessPacket(packetIn);
                    packetIn.processPacket(processor);
                }
            });
            throw ThreadQuickExitException.INSTANCE;
        }
        else
        {
            clientPreProcessPacket(packetIn);
        }
    }

    protected static void clientPreProcessPacket(Packet p_clientPreProcessPacket_0_)
    {
        if (p_clientPreProcessPacket_0_ instanceof SPacketPlayerPosLook)
        {
            Config.getRenderGlobal().onPlayerPositionSet();
        }

        if (p_clientPreProcessPacket_0_ instanceof SPacketRespawn)
        {
            SPacketRespawn spacketrespawn = (SPacketRespawn)p_clientPreProcessPacket_0_;
            lastDimensionId = spacketrespawn.getDimensionID();
        }
        else if (p_clientPreProcessPacket_0_ instanceof SPacketJoinGame)
        {
            SPacketJoinGame spacketjoingame = (SPacketJoinGame)p_clientPreProcessPacket_0_;
            lastDimensionId = spacketjoingame.getDimension();
        }
        else
        {
            lastDimensionId = Integer.MIN_VALUE;
        }
    }
}
