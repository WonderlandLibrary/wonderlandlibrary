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
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PreYggdrasilConverter {
   public static final File OLD_OPS_FILE = new File("ops.txt");
   private static final Logger LOGGER = LogManager.getLogger();
   public static final File OLD_IPBAN_FILE = new File("banned-ips.txt");
   public static final File OLD_WHITELIST_FILE = new File("white-list.txt");
   public static final File OLD_PLAYERBAN_FILE = new File("banned-players.txt");

   static Logger access$0() {
      return LOGGER;
   }

   public static String getStringUUIDFromName(String var0) {
      if (!StringUtils.isNullOrEmpty(var0) && var0.length() <= 16) {
         MinecraftServer var1 = MinecraftServer.getServer();
         GameProfile var2 = var1.getPlayerProfileCache().getGameProfileForUsername(var0);
         if (var2 != null && var2.getId() != null) {
            return var2.getId().toString();
         } else if (!var1.isSinglePlayer() && var1.isServerInOnlineMode()) {
            ArrayList var3 = Lists.newArrayList();
            ProfileLookupCallback var4 = new ProfileLookupCallback(var1, var3) {
               private final List val$list;
               private final MinecraftServer val$minecraftserver;

               {
                  this.val$minecraftserver = var1;
                  this.val$list = var2;
               }

               public void onProfileLookupFailed(GameProfile var1, Exception var2) {
                  PreYggdrasilConverter.access$0().warn((String)("Could not lookup user whitelist entry for " + var1.getName()), (Throwable)var2);
               }

               public void onProfileLookupSucceeded(GameProfile var1) {
                  this.val$minecraftserver.getPlayerProfileCache().addEntry(var1);
                  this.val$list.add(var1);
               }
            };
            lookupNames(var1, Lists.newArrayList((Object[])(var0)), var4);
            return var3.size() > 0 && ((GameProfile)var3.get(0)).getId() != null ? ((GameProfile)var3.get(0)).getId().toString() : "";
         } else {
            return EntityPlayer.getUUID(new GameProfile((UUID)null, var0)).toString();
         }
      } else {
         return var0;
      }
   }

   private static void lookupNames(MinecraftServer var0, Collection var1, ProfileLookupCallback var2) {
      String[] var3 = (String[])Iterators.toArray(Iterators.filter(var1.iterator(), new Predicate() {
         public boolean apply(Object var1) {
            return this.apply((String)var1);
         }

         public boolean apply(String var1) {
            return !StringUtils.isNullOrEmpty(var1);
         }
      }), String.class);
      if (var0.isServerInOnlineMode()) {
         var0.getGameProfileRepository().findProfilesByNames(var3, Agent.MINECRAFT, var2);
      } else {
         String[] var7 = var3;
         int var6 = var3.length;

         for(int var5 = 0; var5 < var6; ++var5) {
            String var4 = var7[var5];
            UUID var8 = EntityPlayer.getUUID(new GameProfile((UUID)null, var4));
            GameProfile var9 = new GameProfile(var8, var4);
            var2.onProfileLookupSucceeded(var9);
         }
      }

   }
}
