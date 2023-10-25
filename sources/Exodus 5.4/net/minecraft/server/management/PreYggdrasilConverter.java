/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.collect.Iterators
 *  com.google.common.collect.Lists
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.ProfileLookupCallback
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.server.management;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PreYggdrasilConverter {
    public static final File OLD_WHITELIST_FILE;
    private static final Logger LOGGER;
    public static final File OLD_OPS_FILE;
    public static final File OLD_PLAYERBAN_FILE;
    public static final File OLD_IPBAN_FILE;

    private static void lookupNames(MinecraftServer minecraftServer, Collection<String> collection, ProfileLookupCallback profileLookupCallback) {
        String[] stringArray = (String[])Iterators.toArray((Iterator)Iterators.filter(collection.iterator(), (Predicate)new Predicate<String>(){

            public boolean apply(String string) {
                return !StringUtils.isNullOrEmpty(string);
            }
        }), String.class);
        if (minecraftServer.isServerInOnlineMode()) {
            minecraftServer.getGameProfileRepository().findProfilesByNames(stringArray, Agent.MINECRAFT, profileLookupCallback);
        } else {
            String[] stringArray2 = stringArray;
            int n = stringArray.length;
            int n2 = 0;
            while (n2 < n) {
                String string = stringArray2[n2];
                UUID uUID = EntityPlayer.getUUID(new GameProfile(null, string));
                GameProfile gameProfile = new GameProfile(uUID, string);
                profileLookupCallback.onProfileLookupSucceeded(gameProfile);
                ++n2;
            }
        }
    }

    public static String getStringUUIDFromName(String string) {
        if (!StringUtils.isNullOrEmpty(string) && string.length() <= 16) {
            final MinecraftServer minecraftServer = MinecraftServer.getServer();
            GameProfile gameProfile = minecraftServer.getPlayerProfileCache().getGameProfileForUsername(string);
            if (gameProfile != null && gameProfile.getId() != null) {
                return gameProfile.getId().toString();
            }
            if (!minecraftServer.isSinglePlayer() && minecraftServer.isServerInOnlineMode()) {
                final ArrayList arrayList = Lists.newArrayList();
                ProfileLookupCallback profileLookupCallback = new ProfileLookupCallback(){

                    public void onProfileLookupSucceeded(GameProfile gameProfile) {
                        minecraftServer.getPlayerProfileCache().addEntry(gameProfile);
                        arrayList.add(gameProfile);
                    }

                    public void onProfileLookupFailed(GameProfile gameProfile, Exception exception) {
                        LOGGER.warn("Could not lookup user whitelist entry for " + gameProfile.getName(), (Throwable)exception);
                    }
                };
                PreYggdrasilConverter.lookupNames(minecraftServer, Lists.newArrayList((Object[])new String[]{string}), profileLookupCallback);
                return arrayList.size() > 0 && ((GameProfile)arrayList.get(0)).getId() != null ? ((GameProfile)arrayList.get(0)).getId().toString() : "";
            }
            return EntityPlayer.getUUID(new GameProfile(null, string)).toString();
        }
        return string;
    }

    static {
        LOGGER = LogManager.getLogger();
        OLD_IPBAN_FILE = new File("banned-ips.txt");
        OLD_PLAYERBAN_FILE = new File("banned-players.txt");
        OLD_OPS_FILE = new File("ops.txt");
        OLD_WHITELIST_FILE = new File("white-list.txt");
    }
}

