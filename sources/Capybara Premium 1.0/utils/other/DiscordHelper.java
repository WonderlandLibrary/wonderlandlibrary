package fun.rich.client.utils.other;

import fun.rich.client.Rich;
import fun.rich.client.utils.Helper;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;

public class DiscordHelper implements Helper {
    private static final String discordID = "1002270460559495171";
    private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static final DiscordRPC discordRPC = new DiscordRPC();

    public static void startRPC() {
        try {
            DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
            DiscordRPC.discordInitialize(discordID, eventHandlers, true, null);
            DiscordHelper.discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
            DiscordHelper.discordRichPresence.details = "UID: " + "Null";
            DiscordHelper.discordRichPresence.largeImageKey = "large";
            DiscordHelper.discordRichPresence.largeImageText = "vk.com/capybaraclient";
            DiscordHelper.discordRichPresence.state = "Version: " + Rich.instance.version;
            DiscordRPC.discordUpdatePresence(discordRichPresence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopRPC() {
        DiscordRPC.discordShutdown();
    }
}
