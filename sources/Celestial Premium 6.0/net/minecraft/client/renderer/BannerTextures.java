/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.ResourceLocation;

public class BannerTextures {
    public static final Cache BANNER_DESIGNS = new Cache("B", new ResourceLocation("textures/entity/banner_base.png"), "textures/entity/banner/");
    public static final Cache SHIELD_DESIGNS = new Cache("S", new ResourceLocation("textures/entity/shield_base.png"), "textures/entity/shield/");
    public static final ResourceLocation SHIELD_BASE_TEXTURE = new ResourceLocation("textures/entity/shield_base_nopattern.png");
    public static final ResourceLocation BANNER_BASE_TEXTURE = new ResourceLocation("textures/entity/banner/base.png");

    static class CacheEntry {
        public long lastUseMillis;
        public ResourceLocation textureLocation;

        private CacheEntry() {
        }
    }

    public static class Cache {
        private final Map<String, CacheEntry> cacheMap = Maps.newLinkedHashMap();
        private final ResourceLocation cacheResourceLocation;
        private final String cacheResourceBase;
        private final String cacheId;

        public Cache(String id, ResourceLocation baseResource, String resourcePath) {
            this.cacheId = id;
            this.cacheResourceLocation = baseResource;
            this.cacheResourceBase = resourcePath;
        }

        @Nullable
        public ResourceLocation getResourceLocation(String id, List<BannerPattern> patternList, List<EnumDyeColor> colorList) {
            if (id.isEmpty()) {
                return null;
            }
            id = this.cacheId + id;
            CacheEntry bannertextures$cacheentry = this.cacheMap.get(id);
            if (bannertextures$cacheentry == null) {
                if (this.cacheMap.size() >= 256 && !this.freeCacheSlot()) {
                    return BANNER_BASE_TEXTURE;
                }
                ArrayList<String> list = Lists.newArrayList();
                for (BannerPattern bannerpattern : patternList) {
                    list.add(this.cacheResourceBase + bannerpattern.func_190997_a() + ".png");
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
            long i = System.currentTimeMillis();
            Iterator<String> iterator = this.cacheMap.keySet().iterator();
            while (iterator.hasNext()) {
                String s = iterator.next();
                CacheEntry bannertextures$cacheentry = this.cacheMap.get(s);
                if (i - bannertextures$cacheentry.lastUseMillis <= 5000L) continue;
                Minecraft.getMinecraft().getTextureManager().deleteTexture(bannertextures$cacheentry.textureLocation);
                iterator.remove();
                return true;
            }
            return this.cacheMap.size() < 256;
        }
    }
}

