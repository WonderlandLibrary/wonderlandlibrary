package de.tired.base.interfaces;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Timer;
import net.minecraft.world.World;

public interface IHook {

    Minecraft MC = Minecraft.getMinecraft();


    default EntityPlayerSP getPlayer() {
        return MC.thePlayer;
    }

    default PlayerControllerMP getPlayerController() {
        return MC.playerController;
    }

    default GameSettings getGameSettings() {
        return MC.gameSettings;
    }

    default WorldClient getWorld() {
        return MC.theWorld;
    }

    default Timer getTimer() {
        return MC.timer;
    }

    default Block blockUnder(Entity e, float offsetY) {
        return e.getEntityWorld().getBlockState(new BlockPos(getX(), getY() - offsetY, getZ())).getBlock();
    }

   default Block getBlockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
        return MC.theWorld.getBlockState(new BlockPos(MC.thePlayer.posX + offsetX, MC.thePlayer.posY + offsetY, MC.thePlayer.posZ + offsetZ)).getBlock();
    }

    default Block getBlockUnderPlayer(float offsetY) {
        return getWorld().getBlockState(new BlockPos(getX(), getY() - offsetY, getZ())).getBlock();
    }
    default void setPosition(double x, double y, double z) {
        getPlayer().setPosition(x, y, z);
    }

    default void sendPacket(Packet<? extends INetHandler> packet) {
        getPlayer().sendQueue.addToSendQueue(packet);
    }

    default void sendPacketUnlogged(Packet<? extends INetHandler> packet) {
        MC.getNetHandler().getNetworkManager().sendPacket(packet);
    }

    default String getName(EntityPlayer player) {
        return player.getGameProfile().getName();
    }


    default int getHurtTime() {
        return getPlayer().hurtTime;
    }

    default void setMotion(double motion) {
        getPlayer().motionX = getPlayer().motionZ = motion;
    }


    default float getYaw() {
        return MC.thePlayer.rotationYawHead;
    }

    default float getPitch() {
        return  MC.thePlayer.rotationPitchHead;
    }

    default double getX() {
        return  MC.thePlayer.posX;
    }

    default double getY() {
        return MC.thePlayer.posY;
    }

    default double getZ() {
        return  MC.thePlayer.posZ;
    }


}
