package tech.atani.client.utility.interfaces;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Timer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import tech.atani.client.feature.command.Command;
import tech.atani.client.utility.player.PlayerHandler;

public interface Methods extends ClientInformationAccess {

    Minecraft mc = Minecraft.getMinecraft();
    FontRenderer fr = mc.fontRendererObj;

    default EntityPlayerSP getPlayer() {
        return mc.thePlayer;
    }

    default PlayerControllerMP getPlayerController() {
        return mc.playerController;
    }

    default GameSettings getGameSettings() {
        return mc.gameSettings;
    }

    default WorldClient getWorld() {
        return mc.theWorld;
    }

    default Timer getTimer() {
        return mc.timer;
    }

    default String getName(EntityPlayer player) {
        return player.getGameProfile().getName();
    }

    default float getYaw() {
        return PlayerHandler.yaw;
    }

    default float getPitch() {
        return PlayerHandler.pitch;
    }

    default double getX() {
        return getPlayer().posX;
    }

    default double getY() {
        return getPlayer().posY;
    }

    default double getZ() {
        return getPlayer().posZ;
    }

    default int getHurtTime() {
        return getPlayer().hurtTime;
    }

    default RenderManager getRenderManager() {
        return mc.getRenderManager();
    }

    default void setPosition(double x, double y, double z) {
        getPlayer().setPosition(x, y, z);
    }

    default void addPosition(double x, double y, double z) {
        setPosition(getX() + x, getY() + y, getZ() + z);
    }

    default void sendPacket(Packet<? extends INetHandler> packet) {
        getPlayer().sendQueue.addToSendQueue(packet);
    }

    default void sendPacketUnlogged(Packet<? extends INetHandler> packet) {
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }

    default boolean isMoving() {
        return getPlayer().moveForward != 0 || getPlayer().moveStrafing != 0;
    }

    default boolean isVoidBelow(BlockPos pos) {
        int air = 0;

        for(int y = 0; y < pos.getY(); y++) {
            if(mc.theWorld.isAirBlock(pos)) {
                air++;
            }
        }

        return air >= pos.getY();
    }

    default boolean isMoving(Entity entity) {
        return entity.lastTickPosX != entity.posX || entity.lastTickPosZ != entity.posZ || entity.lastTickPosY != entity.posY;
    }

    default boolean isKeyDown(int keyCode) {
        if (keyCode < 0) {
            int i = Mouse.getEventButton();
            return i - 100 == keyCode;
        } else {
            return Keyboard.isKeyDown(keyCode);
        }
    }

    default void resumeWalk() {
        getGameSettings().keyBindForward.pressed = isKeyDown(getGameSettings().keyBindForward.getKeyCode());
        getGameSettings().keyBindBack.pressed = isKeyDown(getGameSettings().keyBindBack.getKeyCode());
        getGameSettings().keyBindLeft.pressed = isKeyDown(getGameSettings().keyBindLeft.getKeyCode());
        getGameSettings().keyBindRight.pressed = isKeyDown(getGameSettings().keyBindRight.getKeyCode());
    }

    default void stopWalk() {
        getGameSettings().keyBindForward.pressed = false;
        getGameSettings().keyBindBack.pressed = false;
        getGameSettings().keyBindLeft.pressed = false;
        getGameSettings().keyBindRight.pressed = false;
    }

    default void sendMessage(Object o) {
        sendMessage(o, true);
    }

    default void sendMessage(Object o, boolean prefix) {
        getPlayer().addChatMessage(new ChatComponentText((prefix ? PREFIX : "") + o));
    }

    default void sendHelp(Command command, String... usages) {
        for (String usage : usages) {
            String shortestName = command.getName();
            for (String alias : command.getAliases()) {
                if (alias.length() < shortestName.length()) {
                    shortestName = alias;
                }
            }
            sendMessage("§a" + "." + shortestName + " §7" + usage);
        }
    }

    default void sendError(String issue, String help) {
        sendMessage("§c§lERROR: §e" + issue.toUpperCase() + "§7: §a" + help);
    }

}
