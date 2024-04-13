package dev.tenacity.util.misc;

import dev.tenacity.util.player.MovementUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Timer;

public final class ChatUtil {

    private static final Minecraft MC = Minecraft.getMinecraft();

    private ChatUtil() {
    }

    public static void error(final String message) {
        MC.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§r[§c§lERROR§r]: " + message));
    }
    public static void print(final String message) {
        MC.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§r[§d§lTENACITY§7§r]: " + message));
    }
    public static void success (final String message) {
        MC.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§r[§2SUCCESS§r]: " + message));
    }
    public static void warn (final String message) {
        MC.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§r[§6WARN§r]: " + message));
    }
    public static void credit(final String message) {
        MC.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§a[Credits]:§r " + message));
    }
    public static void enable(final String message) {
        MC.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§r[§dNotification§f]:§r " + message));
    }
    public static void disable(final String message) {
        MC.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§r[§dNotification§f]:§r " + message));
    }
    public static void notify(final String message) {
        MC.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§r[§dNotification§f]:§r " + message));
    }
    public static void cat(final String message) {
        MC.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§d[§fCAT§d]§f: " + message));
    }
    public static void hacker(final String message) {
        MC.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§4[§cHACKER§4]:§r " + message));
    }
}