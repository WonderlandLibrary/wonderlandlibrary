package wtf.diablo.utils.discord;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.minecraft.client.Minecraft;
import wtf.diablo.Diablo;

public class DiscordRPCUtil {
    public static int killCount = 0;
    public static String serverIp = "none";
    public static long timestamp = System.currentTimeMillis() / 1000;
    public static boolean inGame;

    public static void updateRPC(){
        try {
            serverIp = Minecraft.getMinecraft().getCurrentServerData() != null ? Minecraft.getMinecraft().getCurrentServerData().serverIP : "none";
            boolean isHypixel = serverIp.toLowerCase().contains("hypixel.net");
            DiscordRichPresence richPresence = new DiscordRichPresence.Builder(Minecraft.getMinecraft().thePlayer != null ? "Xraying on Survival" : "Deciding what to play.").setDetails(Minecraft.getMinecraft().thePlayer != null  ? "Playing on vanilla" : "In the main menu").setStartTimestamps(timestamp).setBigImage("logo", Diablo.version + " (" + Diablo.buildType + ")").build();

            DiscordRPC.discordInitialize("916790950322376704", new DiscordEventHandlers(), true);
            if(isHypixel){
                richPresence = new DiscordRichPresence.Builder(inGame ? "Kills: " + Diablo.hypixelStatus.getSessionKills() : "Vibing in the lobby").setDetails("Killing skids on Hypixel.net").setStartTimestamps(timestamp).setBigImage("logo", Diablo.version + " (" + Diablo.buildType + ")").build();
            }
            else if(serverIp != "none")
            richPresence = new DiscordRichPresence.Builder("intent.store").setDetails("Playing on " + serverIp)
                    .setStartTimestamps(timestamp).setBigImage("logo", Diablo.version + " (" + Diablo.buildType + ")").build();
            DiscordRPC.discordUpdatePresence(richPresence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
