/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.texture.ITextureObject
 *  net.minecraft.client.renderer.texture.LayeredColorMaskTexture
 *  net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer
 *  net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer$TimedBannerTexture
 *  net.minecraft.tileentity.TileEntityBanner
 *  net.minecraft.tileentity.TileEntityBanner$EnumBannerPattern
 *  net.minecraft.util.ResourceLocation
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value={TileEntityBannerRenderer.class})
public class TileEntityBannerRendererMixin_ChestDisplay {
    @Shadow
    @Final
    private static Map<String, TileEntityBannerRenderer.TimedBannerTexture> field_178466_c;
    @Shadow
    @Final
    private static ResourceLocation field_178464_d;

    @Overwrite
    private ResourceLocation func_178463_a(TileEntityBanner banner) {
        String texture = banner.func_175116_e();
        if (texture.isEmpty()) {
            return null;
        }
        TileEntityBannerRenderer.TimedBannerTexture timedTexture = field_178466_c.get(texture);
        if (timedTexture == null) {
            if (field_178466_c.size() >= 256 && !this.patcher$freeCacheSlot()) {
                return field_178464_d;
            }
            List patternList = banner.func_175114_c();
            List colorList = banner.func_175110_d();
            ArrayList patternPath = Lists.newArrayList();
            for (TileEntityBanner.EnumBannerPattern pattern : patternList) {
                patternPath.add("textures/entity/banner/" + pattern.func_177271_a() + ".png");
            }
            timedTexture = new TileEntityBannerRenderer.TimedBannerTexture();
            timedTexture.field_178471_b = new ResourceLocation(texture);
            Minecraft.func_71410_x().func_110434_K().func_110579_a(timedTexture.field_178471_b, (ITextureObject)new LayeredColorMaskTexture(field_178464_d, (List)patternPath, colorList));
            field_178466_c.put(texture, timedTexture);
        }
        timedTexture.field_178472_a = System.currentTimeMillis();
        return timedTexture.field_178471_b;
    }

    @Unique
    private boolean patcher$freeCacheSlot() {
        long start = System.currentTimeMillis();
        Iterator<String> iterator2 = field_178466_c.keySet().iterator();
        while (iterator2.hasNext()) {
            String next = iterator2.next();
            TileEntityBannerRenderer.TimedBannerTexture timedTexture = field_178466_c.get(next);
            if (start - timedTexture.field_178472_a <= 5000L) continue;
            Minecraft.func_71410_x().func_110434_K().func_147645_c(timedTexture.field_178471_b);
            iterator2.remove();
            return true;
        }
        return field_178466_c.size() < 256;
    }
}

