package rip.autumn.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

public interface Methods {

    Minecraft mc = Minecraft.getMinecraft();

    Tessellator tessellator = Tessellator.getInstance();
    WorldRenderer worldrenderer = tessellator.getWorldRenderer();

    default EntityPlayerSP getPlayer() { return mc.thePlayer; }
    default WorldClient getWorld() { return mc.theWorld; }

    default boolean isMoving() { return mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0; }

    default void sendPacket(Packet<? extends INetHandler> packet) { mc.thePlayer.sendQueue.addToSendQueue(packet); }

    default void sendPacketUnlogged(Packet<? extends INetHandler> packet) { mc.getNetHandler().getNetworkManager().sendPacket(packet); }

    default double getX() { return mc.thePlayer.posX; }
    default double getY() { return mc.thePlayer.posY; }
    default double getZ() { return mc.thePlayer.posZ; }
    default float getYaw() { return mc.thePlayer.rotationYaw; }
    default float getPitch() { return mc.thePlayer.rotationPitch; }
    default boolean getGround() { return mc.thePlayer.onGround; }
    default boolean getDead() { return mc.thePlayer.isDead; }



}
