/*
 * Decompiled with CFR 0.150.
 */
package com.mojang.realmsclient.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.util.UUIDTypeAdapter;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import net.minecraft.realms.Realms;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealmsUtil {
    private static final YggdrasilAuthenticationService authenticationService = new YggdrasilAuthenticationService(Realms.getProxy(), UUID.randomUUID().toString());
    private static final MinecraftSessionService sessionService = authenticationService.createMinecraftSessionService();
    public static LoadingCache<String, GameProfile> gameProfileCache = CacheBuilder.newBuilder().expireAfterWrite(60L, TimeUnit.MINUTES).build(new CacheLoader<String, GameProfile>(){

        @Override
        public GameProfile load(String uuid) throws Exception {
            GameProfile profile = sessionService.fillProfileProperties(new GameProfile(UUIDTypeAdapter.fromString(uuid), null), false);
            if (profile == null) {
                throw new Exception("Couldn't get profile");
            }
            return profile;
        }
    });
    private static final Logger LOGGER = LogManager.getLogger();
    private static final int MINUTES = 60;
    private static final int HOURS = 3600;
    private static final int DAYS = 86400;

    public static String uuidToName(String uuid) throws Exception {
        GameProfile gameProfile = gameProfileCache.get(uuid);
        return gameProfile.getName();
    }

    public static Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures(String uuid) {
        try {
            GameProfile gameProfile = gameProfileCache.get(uuid);
            return sessionService.getTextures(gameProfile, false);
        }
        catch (Exception e) {
            return new HashMap<MinecraftProfileTexture.Type, MinecraftProfileTexture>();
        }
    }

    public static void browseTo(String uri) {
        try {
            URI link = new URI(uri);
            Class<?> desktopClass = Class.forName("java.awt.Desktop");
            Object o = desktopClass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
            desktopClass.getMethod("browse", URI.class).invoke(o, link);
        }
        catch (Throwable ignored) {
            LOGGER.error("Couldn't open link");
        }
    }

    public static String convertToAgePresentation(Long timeDiff) {
        if (timeDiff < 0L) {
            return "right now";
        }
        long timeDiffInSeconds = timeDiff / 1000L;
        if (timeDiffInSeconds < 60L) {
            return (timeDiffInSeconds == 1L ? "1 second" : timeDiffInSeconds + " seconds") + " ago";
        }
        if (timeDiffInSeconds < 3600L) {
            long minutes = timeDiffInSeconds / 60L;
            return (minutes == 1L ? "1 minute" : minutes + " minutes") + " ago";
        }
        if (timeDiffInSeconds < 86400L) {
            long hours = timeDiffInSeconds / 3600L;
            return (hours == 1L ? "1 hour" : hours + " hours") + " ago";
        }
        long days = timeDiffInSeconds / 86400L;
        return (days == 1L ? "1 day" : days + " days") + " ago";
    }
}

