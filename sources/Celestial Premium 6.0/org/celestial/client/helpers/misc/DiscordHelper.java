/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  club.minnced.discord.rpc.DiscordEventHandlers
 *  club.minnced.discord.rpc.DiscordRPC
 *  club.minnced.discord.rpc.DiscordRichPresence
 */
package org.celestial.client.helpers.misc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import org.celestial.client.Celestial;
import org.celestial.client.helpers.Helper;

public class DiscordHelper
implements Helper {
    private static final String discordID = "899233884783386664";
    private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static final DiscordRPC discordRPC = DiscordRPC.INSTANCE;

    public static void startRPC() {
        try {
            DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
            discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);
            DiscordHelper.discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
            DiscordHelper.discordRichPresence.details = "Name: " + Celestial.instance.getLicenseName();
            DiscordHelper.discordRichPresence.largeImageKey = "kakashka";
            DiscordHelper.discordRichPresence.largeImageText = "vk.com/celestialclient";
            DiscordHelper.discordRichPresence.state = "Build: " + Celestial.version;
            discordRPC.Discord_UpdatePresence(discordRichPresence);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopRPC() {
        discordRPC.Discord_Shutdown();
    }
}

