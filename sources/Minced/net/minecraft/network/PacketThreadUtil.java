// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network;

import net.minecraft.network.play.server.SPacketJoinGame;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.src.Config;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.IThreadListener;

public class PacketThreadUtil
{
    public static int lastDimensionId;
    
    public static <T extends INetHandler> void checkThreadAndEnqueue(final Packet<T> packetIn, final T processor, final IThreadListener scheduler) throws ThreadQuickExitException {
        if (!scheduler.isCallingFromMinecraftThread()) {
            scheduler.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    PacketThreadUtil.clientPreProcessPacket(packetIn);
                    packetIn.processPacket(processor);
                }
            });
            throw ThreadQuickExitException.INSTANCE;
        }
        clientPreProcessPacket(packetIn);
    }
    
    protected static void clientPreProcessPacket(final Packet p_clientPreProcessPacket_0_) {
        if (p_clientPreProcessPacket_0_ instanceof SPacketPlayerPosLook) {
            Config.getRenderGlobal().onPlayerPositionSet();
        }
        if (p_clientPreProcessPacket_0_ instanceof SPacketRespawn) {
            final SPacketRespawn spacketrespawn = (SPacketRespawn)p_clientPreProcessPacket_0_;
            PacketThreadUtil.lastDimensionId = spacketrespawn.getDimensionID();
        }
        else if (p_clientPreProcessPacket_0_ instanceof SPacketJoinGame) {
            final SPacketJoinGame spacketjoingame = (SPacketJoinGame)p_clientPreProcessPacket_0_;
            PacketThreadUtil.lastDimensionId = spacketjoingame.getDimension();
        }
        else {
            PacketThreadUtil.lastDimensionId = Integer.MIN_VALUE;
        }
    }
    
    static {
        PacketThreadUtil.lastDimensionId = Integer.MIN_VALUE;
    }
}
