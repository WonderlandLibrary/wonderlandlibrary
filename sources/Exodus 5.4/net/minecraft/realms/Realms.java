/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.util.concurrent.ListenableFuture
 *  com.mojang.authlib.GameProfile
 *  com.mojang.util.UUIDTypeAdapter
 */
package net.minecraft.realms;

import com.google.common.util.concurrent.ListenableFuture;
import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import java.net.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.util.Session;
import net.minecraft.world.WorldSettings;

public class Realms {
    public static String getSessionId() {
        return Minecraft.getMinecraft().getSession().getSessionID();
    }

    public static String getGameDirectoryPath() {
        return Minecraft.getMinecraft().mcDataDir.getAbsolutePath();
    }

    public static int survivalId() {
        return WorldSettings.GameType.SURVIVAL.getID();
    }

    public static String uuidToName(String string) {
        return Minecraft.getMinecraft().getSessionService().fillProfileProperties(new GameProfile(UUIDTypeAdapter.fromString((String)string), null), false).getName();
    }

    public static int adventureId() {
        return WorldSettings.GameType.ADVENTURE.getID();
    }

    public static long currentTimeMillis() {
        return Minecraft.getSystemTime();
    }

    public static ListenableFuture<Object> downloadResourcePack(String string, String string2) {
        ListenableFuture<Object> listenableFuture = Minecraft.getMinecraft().getResourcePackRepository().downloadResourcePack(string, string2);
        return listenableFuture;
    }

    public static String sessionId() {
        Session session = Minecraft.getMinecraft().getSession();
        return session == null ? null : session.getSessionID();
    }

    public static Proxy getProxy() {
        return Minecraft.getMinecraft().getProxy();
    }

    public static void clearResourcePack() {
        Minecraft.getMinecraft().getResourcePackRepository().func_148529_f();
    }

    public static int creativeId() {
        return WorldSettings.GameType.CREATIVE.getID();
    }

    public static int spectatorId() {
        return WorldSettings.GameType.SPECTATOR.getID();
    }

    public static void setConnectedToRealms(boolean bl) {
        Minecraft.getMinecraft().func_181537_a(bl);
    }

    public static String userName() {
        Session session = Minecraft.getMinecraft().getSession();
        return session == null ? null : session.getUsername();
    }

    public static boolean isTouchScreen() {
        Minecraft.getMinecraft();
        return Minecraft.gameSettings.touchscreen;
    }

    public static String getName() {
        return Minecraft.getMinecraft().getSession().getUsername();
    }

    public static void setScreen(RealmsScreen realmsScreen) {
        Minecraft.getMinecraft().displayGuiScreen(realmsScreen.getProxy());
    }

    public static String getUUID() {
        return Minecraft.getMinecraft().getSession().getPlayerID();
    }
}

