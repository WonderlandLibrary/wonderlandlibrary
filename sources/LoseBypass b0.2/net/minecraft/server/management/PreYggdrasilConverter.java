/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
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
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PreYggdrasilConverter {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final File OLD_IPBAN_FILE = new File("banned-ips.txt");
    public static final File OLD_PLAYERBAN_FILE = new File("banned-players.txt");
    public static final File OLD_OPS_FILE = new File("ops.txt");
    public static final File OLD_WHITELIST_FILE = new File("white-list.txt");

    private static void lookupNames(MinecraftServer server, Collection<String> names, ProfileLookupCallback callback) {
        String[] astring = Iterators.toArray(Iterators.filter(names.iterator(), new Predicate<String>(){

            @Override
            public boolean apply(String p_apply_1_) {
                if (StringUtils.isNullOrEmpty(p_apply_1_)) return false;
                return true;
            }
        }), String.class);
        if (server.isServerInOnlineMode()) {
            server.getGameProfileRepository().findProfilesByNames(astring, Agent.MINECRAFT, callback);
            return;
        }
        String[] stringArray = astring;
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String s = stringArray[n2];
            UUID uuid = EntityPlayer.getUUID(new GameProfile((UUID)null, s));
            GameProfile gameprofile = new GameProfile(uuid, s);
            callback.onProfileLookupSucceeded(gameprofile);
            ++n2;
        }
    }

    public static String getStringUUIDFromName(String p_152719_0_) {
        if (StringUtils.isNullOrEmpty(p_152719_0_)) return p_152719_0_;
        if (p_152719_0_.length() > 16) return p_152719_0_;
        final MinecraftServer minecraftserver = MinecraftServer.getServer();
        GameProfile gameprofile = minecraftserver.getPlayerProfileCache().getGameProfileForUsername(p_152719_0_);
        if (gameprofile != null && gameprofile.getId() != null) {
            return gameprofile.getId().toString();
        }
        if (minecraftserver.isSinglePlayer()) return EntityPlayer.getUUID(new GameProfile((UUID)null, p_152719_0_)).toString();
        if (!minecraftserver.isServerInOnlineMode()) return EntityPlayer.getUUID(new GameProfile((UUID)null, p_152719_0_)).toString();
        final ArrayList list = Lists.newArrayList();
        ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback(){

            public void onProfileLookupSucceeded(GameProfile p_onProfileLookupSucceeded_1_) {
                minecraftserver.getPlayerProfileCache().addEntry(p_onProfileLookupSucceeded_1_);
                list.add(p_onProfileLookupSucceeded_1_);
            }

            public void onProfileLookupFailed(GameProfile p_onProfileLookupFailed_1_, Exception p_onProfileLookupFailed_2_) {
                LOGGER.warn("Could not lookup user whitelist entry for " + p_onProfileLookupFailed_1_.getName(), (Throwable)p_onProfileLookupFailed_2_);
            }
        };
        PreYggdrasilConverter.lookupNames(minecraftserver, Lists.newArrayList(p_152719_0_), profilelookupcallback);
        if (list.size() <= 0) return "";
        if (((GameProfile)list.get(0)).getId() == null) return "";
        String string = ((GameProfile)list.get(0)).getId().toString();
        return string;
    }
}

