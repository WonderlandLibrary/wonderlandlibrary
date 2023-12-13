// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient;

import club.minnced.discord.rpc.DiscordUser;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import net.minecraft.client.Minecraft;

public class DiscordRP
{
    private boolean running;
    private long created;
    
    public DiscordRP() {
        this.running = true;
        this.created = 0L;
    }
    
    public void start() {
        final Minecraft mc = Minecraft.getMinecraft();
        final DiscordRPC lib = DiscordRPC.INSTANCE;
        final String applicationId = "671154973274275850";
        final String steamId = "";
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.ready = (user -> System.out.println("Ready!"));
        lib.Discord_Initialize(applicationId, handlers, true, steamId);
        final DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000L;
        presence.details = "Playing epicly";
        presence.state = "lol";
        presence.largeImageKey = "xulu2";
        lib.Discord_UpdatePresence(presence);
        final DiscordRPC discordRPC;
        final DiscordRichPresence discordRichPresence;
        final Minecraft minecraft;
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                discordRPC.Discord_RunCallbacks();
                try {
                    discordRichPresence.largeImageKey = "xulurevamp3";
                    discordRichPresence.largeImageText = "Xulu v1.5.2";
                    if (minecraft.isIntegratedServerRunning()) {
                        discordRichPresence.details = "Singleplayer";
                        discordRichPresence.state = "In Game";
                    }
                    else if (minecraft.getCurrentServerData() != null) {
                        if (!minecraft.getCurrentServerData().serverIP.equals(discordRichPresence.state)) {
                            discordRichPresence.details = "Playing a server";
                            discordRichPresence.state = minecraft.getCurrentServerData().serverIP;
                        }
                    }
                    else {
                        discordRichPresence.details = "Menu";
                        discordRichPresence.state = "Idle";
                    }
                    discordRPC.Discord_UpdatePresence(discordRichPresence);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
                try {
                    Thread.sleep(2000L);
                }
                catch (InterruptedException ex) {}
            }
        }, "RPC-Callback-Handler").start();
    }
    
    public void shutdown() {
        final DiscordRPC lib = DiscordRPC.INSTANCE;
        lib.Discord_Shutdown();
    }
}
