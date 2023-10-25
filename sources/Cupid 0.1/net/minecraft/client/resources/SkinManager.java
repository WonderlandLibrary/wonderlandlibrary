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
import net.minecraft.util.ResourceLocation;

public class SkinManager {
  private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<>());
  
  private final TextureManager textureManager;
  
  private final File skinCacheDir;
  
  private final MinecraftSessionService sessionService;
  
  private final LoadingCache<GameProfile, Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>> skinCacheLoader;
  
  public SkinManager(TextureManager textureManagerInstance, File skinCacheDirectory, MinecraftSessionService sessionService) {
    this.textureManager = textureManagerInstance;
    this.skinCacheDir = skinCacheDirectory;
    this.sessionService = sessionService;
    this.skinCacheLoader = CacheBuilder.newBuilder().expireAfterAccess(15L, TimeUnit.SECONDS).build(new CacheLoader<GameProfile, Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>>() {
          public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> load(GameProfile p_load_1_) throws Exception {
            return Minecraft.getMinecraft().getSessionService().getTextures(p_load_1_, false);
          }
        });
  }
  
  public ResourceLocation loadSkin(MinecraftProfileTexture profileTexture, MinecraftProfileTexture.Type p_152792_2_) {
    return loadSkin(profileTexture, p_152792_2_, (SkinAvailableCallback)null);
  }
  
  public ResourceLocation loadSkin(final MinecraftProfileTexture profileTexture, final MinecraftProfileTexture.Type p_152789_2_, final SkinAvailableCallback skinAvailableCallback) {
    final ResourceLocation resourcelocation = new ResourceLocation("skins/" + profileTexture.getHash());
    ITextureObject itextureobject = this.textureManager.getTexture(resourcelocation);
    if (itextureobject != null) {
      if (skinAvailableCallback != null)
        skinAvailableCallback.skinAvailable(p_152789_2_, resourcelocation, profileTexture); 
    } else {
      File file1 = new File(this.skinCacheDir, (profileTexture.getHash().length() > 2) ? profileTexture.getHash().substring(0, 2) : "xx");
      File file2 = new File(file1, profileTexture.getHash());
      final ImageBufferDownload iimagebuffer = (p_152789_2_ == MinecraftProfileTexture.Type.SKIN) ? new ImageBufferDownload() : null;
      ThreadDownloadImageData threaddownloadimagedata = new ThreadDownloadImageData(file2, profileTexture.getUrl(), DefaultPlayerSkin.getDefaultSkinLegacy(), new IImageBuffer() {
            public BufferedImage parseUserSkin(BufferedImage image) {
              if (iimagebuffer != null)
                image = iimagebuffer.parseUserSkin(image); 
              return image;
            }
            
            public void skinAvailable() {
              if (iimagebuffer != null)
                iimagebuffer.skinAvailable(); 
              if (skinAvailableCallback != null)
                skinAvailableCallback.skinAvailable(p_152789_2_, resourcelocation, profileTexture); 
            }
          });
      this.textureManager.loadTexture(resourcelocation, (ITextureObject)threaddownloadimagedata);
    } 
    return resourcelocation;
  }
  
  public void loadProfileTextures(final GameProfile profile, final SkinAvailableCallback skinAvailableCallback, final boolean requireSecure) {
    THREAD_POOL.submit(new Runnable() {
          public void run() {
            final Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = Maps.newHashMap();
            try {
              map.putAll(SkinManager.this.sessionService.getTextures(profile, requireSecure));
            } catch (InsecureTextureException insecureTextureException) {}
            if (map.isEmpty() && profile.getId().equals(Minecraft.getMinecraft().getSession().getProfile().getId())) {
              profile.getProperties().clear();
              profile.getProperties().putAll((Multimap)Minecraft.getMinecraft().func_181037_M());
              map.putAll(SkinManager.this.sessionService.getTextures(profile, false));
            } 
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                  public void run() {
                    if (map.containsKey(MinecraftProfileTexture.Type.SKIN))
                      SkinManager.this.loadSkin((MinecraftProfileTexture)map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN, skinAvailableCallback); 
                    if (map.containsKey(MinecraftProfileTexture.Type.CAPE))
                      SkinManager.this.loadSkin((MinecraftProfileTexture)map.get(MinecraftProfileTexture.Type.CAPE), MinecraftProfileTexture.Type.CAPE, skinAvailableCallback); 
                  }
                });
          }
        });
  }
  
  public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> loadSkinFromCache(GameProfile profile) {
    return (Map<MinecraftProfileTexture.Type, MinecraftProfileTexture>)this.skinCacheLoader.getUnchecked(profile);
  }
  
  public static interface SkinAvailableCallback {
    void skinAvailable(MinecraftProfileTexture.Type param1Type, ResourceLocation param1ResourceLocation, MinecraftProfileTexture param1MinecraftProfileTexture);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\client\resources\SkinManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */