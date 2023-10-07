// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.HashMap;
import com.mojang.authlib.minecraft.InsecureTextureException;
import com.google.common.collect.Maps;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.util.ResourceLocation;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import java.util.Map;
import com.mojang.authlib.GameProfile;
import com.google.common.cache.CacheLoader;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
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
    private final LoadingCache skinCacheLoader;
    private static final String __OBFID = "CL_00001830";
    
    public SkinManager(final TextureManager textureManagerInstance, final File skinCacheDirectory, final MinecraftSessionService sessionService) {
        this.textureManager = textureManagerInstance;
        this.skinCacheDir = skinCacheDirectory;
        this.sessionService = sessionService;
        this.skinCacheLoader = CacheBuilder.newBuilder().expireAfterAccess(15L, TimeUnit.SECONDS).build((CacheLoader)new CacheLoader() {
            private static final String __OBFID = "CL_00001829";
            
            public Map func_152786_a(final GameProfile p_152786_1_) {
                return Minecraft.getMinecraft().getSessionService().getTextures(p_152786_1_, false);
            }
            
            public Object load(final Object p_load_1_) {
                return this.func_152786_a((GameProfile)p_load_1_);
            }
        });
    }
    
    public ResourceLocation loadSkin(final MinecraftProfileTexture p_152792_1_, final MinecraftProfileTexture.Type p_152792_2_) {
        return this.loadSkin(p_152792_1_, p_152792_2_, null);
    }
    
    public ResourceLocation loadSkin(final MinecraftProfileTexture p_152789_1_, final MinecraftProfileTexture.Type p_152789_2_, final SkinAvailableCallback p_152789_3_) {
        final ResourceLocation var4 = new ResourceLocation("skins/" + p_152789_1_.getHash());
        final ITextureObject var5 = this.textureManager.getTexture(var4);
        if (var5 != null) {
            if (p_152789_3_ != null) {
                p_152789_3_.skinAvailable(p_152789_2_, var4, p_152789_1_);
            }
        }
        else {
            final File var6 = new File(this.skinCacheDir, p_152789_1_.getHash().substring(0, 2));
            final File var7 = new File(var6, p_152789_1_.getHash());
            final ImageBufferDownload var8 = (p_152789_2_ == MinecraftProfileTexture.Type.SKIN) ? new ImageBufferDownload() : null;
            final ThreadDownloadImageData var9 = new ThreadDownloadImageData(var7, p_152789_1_.getUrl(), DefaultPlayerSkin.func_177335_a(), new IImageBuffer() {
                private static final String __OBFID = "CL_00001828";
                
                @Override
                public BufferedImage parseUserSkin(BufferedImage p_78432_1_) {
                    if (var8 != null) {
                        p_78432_1_ = var8.parseUserSkin(p_78432_1_);
                    }
                    return p_78432_1_;
                }
                
                @Override
                public void func_152634_a() {
                    if (var8 != null) {
                        var8.func_152634_a();
                    }
                    if (p_152789_3_ != null) {
                        p_152789_3_.skinAvailable(p_152789_2_, var4, p_152789_1_);
                    }
                }
            });
            this.textureManager.loadTexture(var4, var9);
        }
        return var4;
    }
    
    public void func_152790_a(final GameProfile p_152790_1_, final SkinAvailableCallback p_152790_2_, final boolean p_152790_3_) {
        SkinManager.THREAD_POOL.submit(new Runnable() {
            private static final String __OBFID = "CL_00001827";
            
            @Override
            public void run() {
                final HashMap var1 = Maps.newHashMap();
                try {
                    var1.putAll(SkinManager.this.sessionService.getTextures(p_152790_1_, p_152790_3_));
                }
                catch (InsecureTextureException ex) {}
                if (var1.isEmpty() && p_152790_1_.getId().equals(Minecraft.getMinecraft().getSession().getProfile().getId())) {
                    var1.putAll(SkinManager.this.sessionService.getTextures(SkinManager.this.sessionService.fillProfileProperties(p_152790_1_, false), false));
                }
                Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                    private static final String __OBFID = "CL_00001826";
                    
                    @Override
                    public void run() {
                        if (var1.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                            SkinManager.this.loadSkin(var1.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN, p_152790_2_);
                        }
                        if (var1.containsKey(MinecraftProfileTexture.Type.CAPE)) {
                            SkinManager.this.loadSkin(var1.get(MinecraftProfileTexture.Type.CAPE), MinecraftProfileTexture.Type.CAPE, p_152790_2_);
                        }
                    }
                });
            }
        });
    }
    
    public Map loadSkinFromCache(final GameProfile p_152788_1_) {
        return (Map)this.skinCacheLoader.getUnchecked((Object)p_152788_1_);
    }
    
    static {
        THREAD_POOL = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    }
    
    public interface SkinAvailableCallback
    {
        void skinAvailable(final MinecraftProfileTexture.Type p0, final ResourceLocation p1, final MinecraftProfileTexture p2);
    }
}
