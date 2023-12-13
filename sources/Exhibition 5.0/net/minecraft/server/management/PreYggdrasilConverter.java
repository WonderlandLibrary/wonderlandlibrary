// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.server.management;

import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import com.mojang.authlib.Agent;
import java.util.Iterator;
import com.google.common.collect.Iterators;
import net.minecraft.util.StringUtils;
import com.google.common.base.Predicate;
import com.mojang.authlib.ProfileLookupCallback;
import java.util.Collection;
import net.minecraft.server.MinecraftServer;
import java.io.File;
import org.apache.logging.log4j.Logger;

public class PreYggdrasilConverter
{
    private static final Logger LOGGER;
    public static final File OLD_IPBAN_FILE;
    public static final File OLD_PLAYERBAN_FILE;
    public static final File OLD_OPS_FILE;
    public static final File OLD_WHITELIST_FILE;
    private static final String __OBFID = "CL_00001882";
    
    private static void lookupNames(final MinecraftServer server, final Collection names, final ProfileLookupCallback callback) {
        final String[] var3 = (String[])Iterators.toArray((Iterator)Iterators.filter((Iterator)names.iterator(), (Predicate)new Predicate() {
            private static final String __OBFID = "CL_00001881";
            
            public boolean func_152733_a(final String p_152733_1_) {
                return !StringUtils.isNullOrEmpty(p_152733_1_);
            }
            
            public boolean apply(final Object p_apply_1_) {
                return this.func_152733_a((String)p_apply_1_);
            }
        }), (Class)String.class);
        if (server.isServerInOnlineMode()) {
            server.getGameProfileRepository().findProfilesByNames(var3, Agent.MINECRAFT, callback);
        }
        else {
            final String[] var4 = var3;
            for (int var5 = var3.length, var6 = 0; var6 < var5; ++var6) {
                final String var7 = var4[var6];
                final UUID var8 = EntityPlayer.getUUID(new GameProfile((UUID)null, var7));
                final GameProfile var9 = new GameProfile(var8, var7);
                callback.onProfileLookupSucceeded(var9);
            }
        }
    }
    
    public static String func_152719_a(final String p_152719_0_) {
        if (StringUtils.isNullOrEmpty(p_152719_0_) || p_152719_0_.length() > 16) {
            return p_152719_0_;
        }
        final MinecraftServer var1 = MinecraftServer.getServer();
        final GameProfile var2 = var1.getPlayerProfileCache().getGameProfileForUsername(p_152719_0_);
        if (var2 != null && var2.getId() != null) {
            return var2.getId().toString();
        }
        if (!var1.isSinglePlayer() && var1.isServerInOnlineMode()) {
            final ArrayList var3 = Lists.newArrayList();
            final ProfileLookupCallback var4 = (ProfileLookupCallback)new ProfileLookupCallback() {
                private static final String __OBFID = "CL_00001880";
                
                public void onProfileLookupSucceeded(final GameProfile p_onProfileLookupSucceeded_1_) {
                    var1.getPlayerProfileCache().func_152649_a(p_onProfileLookupSucceeded_1_);
                    var3.add(p_onProfileLookupSucceeded_1_);
                }
                
                public void onProfileLookupFailed(final GameProfile p_onProfileLookupFailed_1_, final Exception p_onProfileLookupFailed_2_) {
                    PreYggdrasilConverter.LOGGER.warn("Could not lookup user whitelist entry for " + p_onProfileLookupFailed_1_.getName(), (Throwable)p_onProfileLookupFailed_2_);
                }
            };
            lookupNames(var1, Lists.newArrayList((Object[])new String[] { p_152719_0_ }), var4);
            return (var3.size() > 0 && var3.get(0).getId() != null) ? var3.get(0).getId().toString() : "";
        }
        return EntityPlayer.getUUID(new GameProfile((UUID)null, p_152719_0_)).toString();
    }
    
    static {
        LOGGER = LogManager.getLogger();
        OLD_IPBAN_FILE = new File("banned-ips.txt");
        OLD_PLAYERBAN_FILE = new File("banned-players.txt");
        OLD_OPS_FILE = new File("ops.txt");
        OLD_WHITELIST_FILE = new File("white-list.txt");
    }
}
