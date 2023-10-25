package net.minecraft.server.management;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S44PacketWorldBorder;
import net.minecraft.world.border.IBorderListener;
import net.minecraft.world.border.WorldBorder;

class ServerConfigurationManager.1
implements IBorderListener {
    ServerConfigurationManager.1() {
    }

    public void onSizeChanged(WorldBorder border, double newSize) {
        ServerConfigurationManager.this.sendPacketToAllPlayers((Packet)new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_SIZE));
    }

    public void onTransitionStarted(WorldBorder border, double oldSize, double newSize, long time) {
        ServerConfigurationManager.this.sendPacketToAllPlayers((Packet)new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.LERP_SIZE));
    }

    public void onCenterChanged(WorldBorder border, double x, double z) {
        ServerConfigurationManager.this.sendPacketToAllPlayers((Packet)new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_CENTER));
    }

    public void onWarningTimeChanged(WorldBorder border, int newTime) {
        ServerConfigurationManager.this.sendPacketToAllPlayers((Packet)new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_WARNING_TIME));
    }

    public void onWarningDistanceChanged(WorldBorder border, int newDistance) {
        ServerConfigurationManager.this.sendPacketToAllPlayers((Packet)new S44PacketWorldBorder(border, S44PacketWorldBorder.Action.SET_WARNING_BLOCKS));
    }

    public void onDamageAmountChanged(WorldBorder border, double newAmount) {
    }

    public void onDamageBufferChanged(WorldBorder border, double newSize) {
    }
}
