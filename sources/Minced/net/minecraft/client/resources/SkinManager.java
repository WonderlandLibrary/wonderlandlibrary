// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.LinkedBlockingQueue;
import com.google.common.collect.Multimap;
import com.mojang.authlib.minecraft.InsecureTextureException;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import com.google.common.cache.CacheLoader;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import java.util.Map;
import com.mojang.authlib.GameProfile;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import java.io.File;
import net.minecraft.client.renderer.texture.TextureManager;
import java.util.concurrent.ExecutorService;

public class SkinManager
{
    private static final ExecutorService THREAD_POOL;
    private final TextureManager textureManager;
    private final File skinCacheDir;
    private final MinecraftSessionService sessionService;
    private final LoadingCache<GameProfile, Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>> skinCacheLoader;
    
    public SkinManager(final TextureManager textureManagerInstance, final File skinCacheDirectory, final MinecraftSessionService sessionService) {
        this.textureManager = textureManagerInstance;
        this.skinCacheDir = skinCacheDirectory;
        this.sessionService = sessionService;
        this.skinCacheLoader = (LoadingCache<GameProfile, Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>>)CacheBuilder.newBuilder().expireAfterAccess(15L, TimeUnit.SECONDS).build((CacheLoader)new CacheLoader<GameProfile, Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>>() {
            public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> load(final GameProfile p_load_1_) throws Exception {
                try {
                    return (Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>)Minecraft.getMinecraft().getSessionService().getTextures(p_load_1_, false);
                }
                catch (Throwable var3) {
                    return (Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>)Maps.newHashMap();
                }
            }
        });
    }
    
    public ResourceLocation loadSkin(final MinecraftProfileTexture profileTexture, final MinecraftProfileTexture.Type textureType) {
        return this.loadSkin(profileTexture, textureType, null);
    }
    
    public ResourceLocation loadSkin(final MinecraftProfileTexture profileTexture, final MinecraftProfileTexture.Type textureType, @Nullable final SkinAvailableCallback skinAvailableCallback) {
        final ResourceLocation resourcelocation = new ResourceLocation("skins/" + profileTexture.getHash());
        final ITextureObject itextureobject = this.textureManager.getTexture(resourcelocation);
        if (itextureobject != null) {
            if (skinAvailableCallback != null) {
                skinAvailableCallback.skinAvailable(textureType, resourcelocation, profileTexture);
            }
        }
        else {
            final File file1 = new File(this.skinCacheDir, (profileTexture.getHash().length() > 2) ? profileTexture.getHash().substring(0, 2) : "xx");
            final File file2 = new File(file1, profileTexture.getHash());
            final IImageBuffer iimagebuffer = (textureType == MinecraftProfileTexture.Type.SKIN) ? new ImageBufferDownload() : null;
            final ThreadDownloadImageData threaddownloadimagedata = new ThreadDownloadImageData(file2, profileTexture.getUrl(), DefaultPlayerSkin.getDefaultSkinLegacy(), new IImageBuffer() {
                private BufferedImage cache;
                
                @Override
                public BufferedImage parseUserSkin(BufferedImage image) {
                    if (iimagebuffer != null) {
                        image = iimagebuffer.parseUserSkin(image);
                    }
                    return this.cache = image;
                }
                
                @Override
                public void skinAvailable() {
                    if (iimagebuffer != null) {
                        iimagebuffer.skinAvailable();
                    }
                    if (skinAvailableCallback != null) {
                        skinAvailableCallback.skinAvailable(textureType, resourcelocation, profileTexture);
                    }
                }
                
                @Override
                public BufferedImage cache() {
                    return this.cache;
                }
            });
            this.textureManager.loadTexture(resourcelocation, threaddownloadimagedata);
        }
        return resourcelocation;
    }
    
    public void loadProfileTextures(final GameProfile profile, final SkinAvailableCallback skinAvailableCallback, final boolean requireSecure) {
        SkinManager.THREAD_POOL.submit(new Runnable() {
            @Override
            public void run() {
                final Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = (Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>)Maps.newHashMap();
                try {
                    map.putAll(SkinManager.this.sessionService.getTextures(profile, requireSecure));
                }
                catch (InsecureTextureException ex) {}
                if (map.isEmpty() && profile.getId().equals(Minecraft.getMinecraft().getSession().getProfile().getId())) {
                    profile.getProperties().clear();
                    profile.getProperties().putAll((Multimap)Minecraft.getMinecraft().getProfileProperties());
                    map.putAll(SkinManager.this.sessionService.getTextures(profile, false));
                }
                Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                            SkinManager.this.loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN, skinAvailableCallback);
                        }
                        if (map.containsKey(MinecraftProfileTexture.Type.CAPE)) {
                            SkinManager.this.loadSkin(map.get(MinecraftProfileTexture.Type.CAPE), MinecraftProfileTexture.Type.CAPE, skinAvailableCallback);
                        }
                    }
                });
            }
        });
    }
    
    public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> loadSkinFromCache(final GameProfile profile) {
        return (Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>)this.skinCacheLoader.getUnchecked((Object)profile);
    }
    
    static {
        THREAD_POOL = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    }
    
    public interface SkinAvailableCallback
    {
        void skinAvailable(final MinecraftProfileTexture.Type p0, final ResourceLocation p1, final MinecraftProfileTexture p2);
    }
}
