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
import net.minecraft.server.management.PreYggdrasilConverter;
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
        String[] astring = (String[])Iterators.toArray((Iterator)Iterators.filter((Iterator)names.iterator(), (Predicate)new /* Unavailable Anonymous Inner Class!! */), String.class);
        if (server.isServerInOnlineMode()) {
            server.getGameProfileRepository().findProfilesByNames(astring, Agent.MINECRAFT, callback);
        } else {
            for (String s : astring) {
                UUID uuid = EntityPlayer.getUUID((GameProfile)new GameProfile((UUID)null, s));
                GameProfile gameprofile = new GameProfile(uuid, s);
                callback.onProfileLookupSucceeded(gameprofile);
            }
        }
    }

    public static String getStringUUIDFromName(String p_152719_0_) {
        if (!StringUtils.isNullOrEmpty((String)p_152719_0_) && p_152719_0_.length() <= 16) {
            MinecraftServer minecraftserver = MinecraftServer.getServer();
            GameProfile gameprofile = minecraftserver.getPlayerProfileCache().getGameProfileForUsername(p_152719_0_);
            if (gameprofile != null && gameprofile.getId() != null) {
                return gameprofile.getId().toString();
            }
            if (!minecraftserver.isSinglePlayer() && minecraftserver.isServerInOnlineMode()) {
                ArrayList list = Lists.newArrayList();
                2 profilelookupcallback = new /* Unavailable Anonymous Inner Class!! */;
                PreYggdrasilConverter.lookupNames(minecraftserver, (Collection<String>)Lists.newArrayList((Object[])new String[]{p_152719_0_}), (ProfileLookupCallback)profilelookupcallback);
                return list.size() > 0 && ((GameProfile)list.get(0)).getId() != null ? ((GameProfile)list.get(0)).getId().toString() : "";
            }
            return EntityPlayer.getUUID((GameProfile)new GameProfile((UUID)null, p_152719_0_)).toString();
        }
        return p_152719_0_;
    }

    static /* synthetic */ Logger access$000() {
        return LOGGER;
    }
}
