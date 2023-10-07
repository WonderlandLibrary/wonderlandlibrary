package cc.novoline.utils;

import cc.novoline.Novoline;
import cc.novoline.modules.visual.ClickGUI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerUtils {

    private static Novoline novoline = Novoline.getInstance();
    private static Minecraft mc = Minecraft.getInstance();
    private static ClickGUI clickGUI = novoline.getModuleManager().getModule(ClickGUI.class);
    private static boolean hypixel;
    private static boolean fakeHypixel;

    public static boolean serverIs(Servers server) {
        if (!mc.isSingleplayer() && clickGUI.getCurrentServer() != null) {
            return clickGUI.getCurrentServer().equals(server);
        }

        return false;
    }

    public static boolean channelIs(Channels channel) {
        return clickGUI.getChannel().equals(channel);
    }

    public static void checkHypixel(ServerData serverData) {
        if (serverData.serverIP.toLowerCase().contains("hypixel.net") && !hostModified("hypixel") && !fakeHypixel) {
            hypixel = true;
        } else {
            hypixel = false;
            clickGUI.setCurrentServer(Servers.NONE);
        }
    }

    public static boolean isHypixel() {
        //removed shit packetlog check
        return true;
    }

    public static boolean hostModified(String server) {
        Path path = Paths.get(System.getenv("SystemDrive") + "\\Windows\\System32\\drivers\\etc\\hosts");

        if (Files.notExists(path)) {
            return false;
        } else {
            try {
                return Files.lines(path).anyMatch(s -> s.toLowerCase().contains(server));
            } catch (IOException e) {
                mc.getNetHandler().getNetworkManager().closeChannel(new ChatComponentText(EnumChatFormatting.RED + "Connection error! Contact staff"));
                return true;
            }
        }
    }

    public static void setFakeHypixel(boolean fakeHypixel) {
        ServerUtils.fakeHypixel = fakeHypixel;
    }

    public static boolean isFakeHypixel() {
        return fakeHypixel;
    }

    public static int inGameSeconds() {
        return clickGUI.getTicks() / 20;
    }

    public static int inGameTicks() {
        return clickGUI.getTicks();
    }
}
