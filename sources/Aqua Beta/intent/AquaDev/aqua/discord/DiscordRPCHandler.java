package intent.AquaDev.aqua.discord;

import intent.AquaDev.aqua.discord.DiscordRPC;
import net.aql.Lib;
import net.minecraft.client.Minecraft;

public class DiscordRPCHandler {
    public static final DiscordRPCHandler instance = new DiscordRPCHandler();
    private final DiscordRPC discordRPC = new DiscordRPC();
    String serverIP;
    public static String second = "Client  " + Lib.getUID() + "";

    public DiscordRPCHandler() {
     ///   this.serverIP = Minecraft.getMinecraft().isSingleplayer() ? "Singleplayer" : Minecraft.getMinecraft().getCurrentServerData().serverIP;
    }

    public void init() {
   //     this.discordRPC.start();
    }

    public void shutdown() {
 //       this.discordRPC.shutdown();
    }

    public DiscordRPC getDiscordRPC() {
        return this.discordRPC;
    }
}
