package vip.radium.utils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.MinecraftFontRenderer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class Wrapper {

    public static EntityRenderer getEntityRenderer() {
        return getMinecraft().entityRenderer;
    }

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static EntityPlayerSP getPlayer() {
        return getMinecraft().thePlayer;
    }

    public static WorldClient getWorld() {
        return getMinecraft().theWorld;
    }

    public static MinecraftFontRenderer getMinecraftFontRenderer() {
        return getMinecraft().fontRendererObj;
    }

    public static PlayerControllerMP getPlayerController() {
        return getMinecraft().playerController;
    }

    public static NetHandlerPlayClient getNetHandler() {
        return getMinecraft().getNetHandler();
    }

    public static GameSettings getGameSettings() {
        return getMinecraft().gameSettings;
    }

    public static boolean isInFirstPerson() {
        return getGameSettings().thirdPersonView == 0;
    }

    public static ItemStack getStackInSlot(int index) {
        return getPlayer().inventoryContainer.getSlot(index).getStack();
    }

    public static Timer getTimer() {
        return getMinecraft().getTimer();
    }

    public static Block getBlock(BlockPos pos) {
        return getWorld().getBlockState(pos).getBlock();
    }

    public static void addChatMessage(String message) {
        getPlayer().addChatMessage(new ChatComponentText("\2478[\247CR\2478]\2477 " + message));
    }

    public static GuiScreen getCurrentScreen() {
        return getMinecraft().currentScreen;
    }

    public static List<EntityPlayer> getLoadedPlayers() {
        return getWorld().playerEntities;
    }

    public static List<EntityLivingBase> getLivingEntities(Predicate<EntityLivingBase> validator) {
        List<EntityLivingBase> entities = new ArrayList<>();

        for (Entity entity : getWorld().loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase e = (EntityLivingBase) entity;
                if (validator.test(e))
                    entities.add(e);
            }
        }

        return entities;
    }

    public static void sendPacket(Packet<?> packet) {
        getNetHandler().sendPacket(packet);
    }

    public static void sendPacketDirect(Packet<?> packet) {
        getNetHandler().getNetworkManager().sendPacket(packet);
    }
}
