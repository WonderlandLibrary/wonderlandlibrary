package net.minecraft.client.resources;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import java.io.File;
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
import net.minecraft.client.resources.SkinManager.2;
import net.minecraft.client.resources.SkinManager.3;
import net.minecraft.client.resources.SkinManager.SkinAvailableCallback;
import net.minecraft.util.ResourceLocation;

public class SkinManager {
   private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue());
   private final TextureManager textureManager;
   private final File skinCacheDir;
   private final MinecraftSessionService sessionService;
   private final LoadingCache skinCacheLoader;

   public SkinManager(TextureManager textureManagerInstance, File skinCacheDirectory, MinecraftSessionService sessionService) {
      this.textureManager = textureManagerInstance;
      this.skinCacheDir = skinCacheDirectory;
      this.sessionService = sessionService;
      this.skinCacheLoader = CacheBuilder.newBuilder().expireAfterAccess(15L, TimeUnit.SECONDS).build(new CacheLoader() {
         public Map load(GameProfile p_load_1_) throws Exception {
            return Minecraft.getMinecraft().getSessionService().getTextures(p_load_1_, false);
         }
      });
   }

   // $FF: synthetic method
   static MinecraftSessionService access$000(SkinManager x0) {
      return x0.sessionService;
   }

   public ResourceLocation loadSkin(MinecraftProfileTexture profileTexture, Type p_152792_2_) {
      return this.loadSkin(profileTexture, p_152792_2_, (SkinAvailableCallback)null);
   }

   public ResourceLocation loadSkin(MinecraftProfileTexture profileTexture, Type p_152789_2_, SkinAvailableCallback skinAvailableCallback) {
      ResourceLocation resourcelocation = new ResourceLocation("skins/" + profileTexture.getHash());
      ITextureObject itextureobject = this.textureManager.getTexture(resourcelocation);
      if(itextureobject != null) {
         if(skinAvailableCallback != null) {
            skinAvailableCallback.skinAvailable(p_152789_2_, resourcelocation, profileTexture);
         }
      } else {
         File file1 = new File(this.skinCacheDir, profileTexture.getHash().length() > 2?profileTexture.getHash().substring(0, 2):"xx");
         File file2 = new File(file1, profileTexture.getHash());
         IImageBuffer iimagebuffer = p_152789_2_ == Type.SKIN?new ImageBufferDownload():null;
         ThreadDownloadImageData threaddownloadimagedata = new ThreadDownloadImageData(file2, profileTexture.getUrl(), DefaultPlayerSkin.getDefaultSkinLegacy(), new 2(this, iimagebuffer, skinAvailableCallback, p_152789_2_, resourcelocation, profileTexture));
         this.textureManager.loadTexture(resourcelocation, threaddownloadimagedata);
      }

      return resourcelocation;
   }

   public void loadProfileTextures(GameProfile profile, SkinAvailableCallback skinAvailableCallback, boolean requireSecure) {
      THREAD_POOL.submit(new 3(this, profile, requireSecure, skinAvailableCallback));
   }

   public Map loadSkinFromCache(GameProfile profile) {
      return (Map)this.skinCacheLoader.getUnchecked(profile);
   }
}
