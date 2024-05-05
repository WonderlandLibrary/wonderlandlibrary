package net.minecraft.client.resources;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import java.io.File;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;

public class SkinManager {
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, (BlockingQueue)new LinkedBlockingQueue());
    private final TextureManager textureManager;
    private final File skinCacheDir;
    private final MinecraftSessionService sessionService;
    private final LoadingCache<GameProfile, Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>> skinCacheLoader;

    public SkinManager(TextureManager textureManagerInstance, File skinCacheDirectory, MinecraftSessionService sessionService) {
        this.textureManager = textureManagerInstance;
        this.skinCacheDir = skinCacheDirectory;
        this.sessionService = sessionService;
        this.skinCacheLoader = CacheBuilder.newBuilder().expireAfterAccess(15L, TimeUnit.SECONDS).build((CacheLoader)new /* Unavailable Anonymous Inner Class!! */);
    }

    public ResourceLocation loadSkin(MinecraftProfileTexture profileTexture, MinecraftProfileTexture.Type p_152792_2_) {
        return this.loadSkin(profileTexture, p_152792_2_, null);
    }

    public ResourceLocation loadSkin(MinecraftProfileTexture profileTexture, MinecraftProfileTexture.Type p_152789_2_, SkinAvailableCallback skinAvailableCallback) {
        ResourceLocation resourcelocation = new ResourceLocation("skins/" + profileTexture.getHash());
        ITextureObject itextureobject = this.textureManager.getTexture(resourcelocation);
        if (itextureobject != null) {
            if (skinAvailableCallback != null) {
                skinAvailableCallback.skinAvailable(p_152789_2_, resourcelocation, profileTexture);
            }
        } else {
            File file1 = new File(this.skinCacheDir, profileTexture.getHash().length() > 2 ? profileTexture.getHash().substring(0, 2) : "xx");
            File file2 = new File(file1, profileTexture.getHash());
            ImageBufferDownload iimagebuffer = p_152789_2_ == MinecraftProfileTexture.Type.SKIN ? new ImageBufferDownload() : null;
            ThreadDownloadImageData threaddownloadimagedata = new ThreadDownloadImageData(file2, profileTexture.getUrl(), DefaultPlayerSkin.getDefaultSkinLegacy(), (IImageBuffer)new /* Unavailable Anonymous Inner Class!! */);
            this.textureManager.loadTexture(resourcelocation, (ITextureObject)threaddownloadimagedata);
        }
        return resourcelocation;
    }

    public void loadProfileTextures(GameProfile profile, SkinAvailableCallback skinAvailableCallback, boolean requireSecure) {
        THREAD_POOL.submit((Runnable)new /* Unavailable Anonymous Inner Class!! */);
    }

    public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> loadSkinFromCache(GameProfile profile) {
        return (Map)this.skinCacheLoader.getUnchecked((Object)profile);
    }

    static /* synthetic */ MinecraftSessionService access$000(SkinManager x0) {
        return x0.sessionService;
    }
}
