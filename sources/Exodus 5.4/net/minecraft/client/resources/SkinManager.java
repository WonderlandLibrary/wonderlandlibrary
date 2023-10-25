/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.cache.CacheBuilder
 *  com.google.common.cache.CacheLoader
 *  com.google.common.cache.LoadingCache
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Multimap
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.minecraft.InsecureTextureException
 *  com.mojang.authlib.minecraft.MinecraftProfileTexture
 *  com.mojang.authlib.minecraft.MinecraftProfileTexture$Type
 *  com.mojang.authlib.minecraft.MinecraftSessionService
 */
package net.minecraft.client.resources;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.InsecureTextureException;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

public class SkinManager {
    private final MinecraftSessionService sessionService;
    private final File skinCacheDir;
    private final LoadingCache<GameProfile, Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>> skinCacheLoader;
    private final TextureManager textureManager;
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());

    public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> loadSkinFromCache(GameProfile gameProfile) {
        return (Map)this.skinCacheLoader.getUnchecked((Object)gameProfile);
    }

    public void loadProfileTextures(final GameProfile gameProfile, final SkinAvailableCallback skinAvailableCallback, final boolean bl) {
        THREAD_POOL.submit(new Runnable(){

            @Override
            public void run() {
                final HashMap hashMap = Maps.newHashMap();
                try {
                    hashMap.putAll(SkinManager.this.sessionService.getTextures(gameProfile, bl));
                }
                catch (InsecureTextureException insecureTextureException) {
                    // empty catch block
                }
                if (hashMap.isEmpty() && gameProfile.getId().equals(Minecraft.getMinecraft().getSession().getProfile().getId())) {
                    gameProfile.getProperties().clear();
                    gameProfile.getProperties().putAll((Multimap)Minecraft.getMinecraft().func_181037_M());
                    hashMap.putAll(SkinManager.this.sessionService.getTextures(gameProfile, false));
                }
                Minecraft.getMinecraft().addScheduledTask(new Runnable(){

                    @Override
                    public void run() {
                        if (hashMap.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                            SkinManager.this.loadSkin((MinecraftProfileTexture)hashMap.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN, skinAvailableCallback);
                        }
                        if (hashMap.containsKey(MinecraftProfileTexture.Type.CAPE)) {
                            SkinManager.this.loadSkin((MinecraftProfileTexture)hashMap.get(MinecraftProfileTexture.Type.CAPE), MinecraftProfileTexture.Type.CAPE, skinAvailableCallback);
                        }
                    }
                });
            }
        });
    }

    public ResourceLocation loadSkin(final MinecraftProfileTexture minecraftProfileTexture, final MinecraftProfileTexture.Type type, final SkinAvailableCallback skinAvailableCallback) {
        final ResourceLocation resourceLocation = new ResourceLocation("skins/" + minecraftProfileTexture.getHash());
        ITextureObject iTextureObject = this.textureManager.getTexture(resourceLocation);
        if (iTextureObject != null) {
            if (skinAvailableCallback != null) {
                skinAvailableCallback.skinAvailable(type, resourceLocation, minecraftProfileTexture);
            }
        } else {
            File file = new File(this.skinCacheDir, minecraftProfileTexture.getHash().length() > 2 ? minecraftProfileTexture.getHash().substring(0, 2) : "xx");
            File file2 = new File(file, minecraftProfileTexture.getHash());
            final ImageBufferDownload imageBufferDownload = type == MinecraftProfileTexture.Type.SKIN ? new ImageBufferDownload() : null;
            ThreadDownloadImageData threadDownloadImageData = new ThreadDownloadImageData(file2, minecraftProfileTexture.getUrl(), DefaultPlayerSkin.getDefaultSkinLegacy(), new IImageBuffer(){

                @Override
                public BufferedImage parseUserSkin(BufferedImage bufferedImage) {
                    if (imageBufferDownload != null) {
                        bufferedImage = imageBufferDownload.parseUserSkin(bufferedImage);
                    }
                    return bufferedImage;
                }

                @Override
                public void skinAvailable() {
                    if (imageBufferDownload != null) {
                        imageBufferDownload.skinAvailable();
                    }
                    if (skinAvailableCallback != null) {
                        skinAvailableCallback.skinAvailable(type, resourceLocation, minecraftProfileTexture);
                    }
                }
            });
            this.textureManager.loadTexture(resourceLocation, threadDownloadImageData);
        }
        return resourceLocation;
    }

    public SkinManager(TextureManager textureManager, File file, MinecraftSessionService minecraftSessionService) {
        this.textureManager = textureManager;
        this.skinCacheDir = file;
        this.sessionService = minecraftSessionService;
        this.skinCacheLoader = CacheBuilder.newBuilder().expireAfterAccess(15L, TimeUnit.SECONDS).build((CacheLoader)new CacheLoader<GameProfile, Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>>(){

            public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> load(GameProfile gameProfile) throws Exception {
                return Minecraft.getMinecraft().getSessionService().getTextures(gameProfile, false);
            }
        });
    }

    public ResourceLocation loadSkin(MinecraftProfileTexture minecraftProfileTexture, MinecraftProfileTexture.Type type) {
        return this.loadSkin(minecraftProfileTexture, type, null);
    }

    public static interface SkinAvailableCallback {
        public void skinAvailable(MinecraftProfileTexture.Type var1, ResourceLocation var2, MinecraftProfileTexture var3);
    }
}

