// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.client.Minecraft;
import com.google.common.collect.Lists;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.BannerPattern;
import java.util.List;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.ResourceLocation;

public class BannerTextures
{
    public static final Cache BANNER_DESIGNS;
    public static final Cache SHIELD_DESIGNS;
    public static final ResourceLocation SHIELD_BASE_TEXTURE;
    public static final ResourceLocation BANNER_BASE_TEXTURE;
    
    static {
        BANNER_DESIGNS = new Cache("B", new ResourceLocation("textures/entity/banner_base.png"), "textures/entity/banner/");
        SHIELD_DESIGNS = new Cache("S", new ResourceLocation("textures/entity/shield_base.png"), "textures/entity/shield/");
        SHIELD_BASE_TEXTURE = new ResourceLocation("textures/entity/shield_base_nopattern.png");
        BANNER_BASE_TEXTURE = new ResourceLocation("textures/entity/banner/base.png");
    }
    
    public static class Cache
    {
        private final Map<String, CacheEntry> cacheMap;
        private final ResourceLocation cacheResourceLocation;
        private final String cacheResourceBase;
        private final String cacheId;
        
        public Cache(final String id, final ResourceLocation baseResource, final String resourcePath) {
            this.cacheMap = (Map<String, CacheEntry>)Maps.newLinkedHashMap();
            this.cacheId = id;
            this.cacheResourceLocation = baseResource;
            this.cacheResourceBase = resourcePath;
        }
        
        @Nullable
        public ResourceLocation getResourceLocation(String id, final List<BannerPattern> patternList, final List<EnumDyeColor> colorList) {
            if (id.isEmpty()) {
                return null;
            }
            id = this.cacheId + id;
            CacheEntry bannertextures$cacheentry = this.cacheMap.get(id);
            if (bannertextures$cacheentry == null) {
                if (this.cacheMap.size() >= 256 && !this.freeCacheSlot()) {
                    return BannerTextures.BANNER_BASE_TEXTURE;
                }
                final List<String> list = (List<String>)Lists.newArrayList();
                for (final BannerPattern bannerpattern : patternList) {
                    list.add(this.cacheResourceBase + bannerpattern.getFileName() + ".png");
                }
                bannertextures$cacheentry = new CacheEntry();
                bannertextures$cacheentry.textureLocation = new ResourceLocation(id);
                Minecraft.getMinecraft().getTextureManager().loadTexture(bannertextures$cacheentry.textureLocation, new LayeredColorMaskTexture(this.cacheResourceLocation, list, colorList));
                this.cacheMap.put(id, bannertextures$cacheentry);
            }
            bannertextures$cacheentry.lastUseMillis = System.currentTimeMillis();
            return bannertextures$cacheentry.textureLocation;
        }
        
        private boolean freeCacheSlot() {
            final long i = System.currentTimeMillis();
            final Iterator<String> iterator = this.cacheMap.keySet().iterator();
            while (iterator.hasNext()) {
                final String s = iterator.next();
                final CacheEntry bannertextures$cacheentry = this.cacheMap.get(s);
                if (i - bannertextures$cacheentry.lastUseMillis > 5000L) {
                    Minecraft.getMinecraft().getTextureManager().deleteTexture(bannertextures$cacheentry.textureLocation);
                    iterator.remove();
                    return true;
                }
            }
            return this.cacheMap.size() < 256;
        }
    }
    
    static class CacheEntry
    {
        public long lastUseMillis;
        public ResourceLocation textureLocation;
        
        private CacheEntry() {
        }
    }
}
