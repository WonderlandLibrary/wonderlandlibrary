package net.minecraft.realms;

import com.google.common.util.concurrent.ListenableFuture;
import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import java.net.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraft.world.WorldSettings;

public class Realms {
   public static Proxy getProxy() {
      return Minecraft.getMinecraft().getProxy();
   }

   public static int adventureId() {
      return WorldSettings.GameType.ADVENTURE.getID();
   }

   public static int creativeId() {
      return WorldSettings.GameType.CREATIVE.getID();
   }

   public static String getGameDirectoryPath() {
      return Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
   }

   public static String sessionId() {
      Session var0 = Minecraft.getMinecraft().getSession();
      return var0 == null ? null : var0.getSessionID();
   }

   public static String getName() {
      return Minecraft.getMinecraft().getSession().getUsername();
   }

   public static String getSessionId() {
      return Minecraft.getMinecraft().getSession().getSessionID();
   }

   public static boolean isTouchScreen() {
      return Minecraft.getMinecraft().gameSettings.touchscreen;
   }

   public static ListenableFuture downloadResourcePack(String var0, String var1) {
      ListenableFuture var2 = Minecraft.getMinecraft().getResourcePackRepository().downloadResourcePack(var0, var1);
      return var2;
   }

   public static void clearResourcePack() {
      Minecraft.getMinecraft().getResourcePackRepository().func_148529_f();
   }

   public static int survivalId() {
      return WorldSettings.GameType.SURVIVAL.getID();
   }

   public static void setScreen(RealmsScreen var0) {
      Minecraft.getMinecraft().displayGuiScreen(var0.getProxy());
   }

   public static int spectatorId() {
      return WorldSettings.GameType.SPECTATOR.getID();
   }

   public static long currentTimeMillis() {
      return Minecraft.getSystemTime();
   }

   public static String uuidToName(String var0) {
      return Minecraft.getMinecraft().getSessionService().fillProfileProperties(new GameProfile(UUIDTypeAdapter.fromString(var0), (String)null), false).getName();
   }

   public static String getUUID() {
      return Minecraft.getMinecraft().getSession().getPlayerID();
   }

   public static String userName() {
      Session var0 = Minecraft.getMinecraft().getSession();
      return var0 == null ? null : var0.getUsername();
   }

   public static void setConnectedToRealms(boolean var0) {
      Minecraft.getMinecraft().func_181537_a(var0);
   }
}
