// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.tileentity;

import com.google.common.collect.Maps;
import net.minecraft.tileentity.TileEntity;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.renderer.texture.ITextureObject;
import java.util.List;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.util.ResourceLocation;
import java.util.Map;

public class TileEntityBannerRenderer extends TileEntitySpecialRenderer
{
    private static final Map field_178466_c;
    private static final ResourceLocation field_178464_d;
    private ModelBanner field_178465_e;
    private static final String __OBFID = "CL_00002473";
    
    public TileEntityBannerRenderer() {
        this.field_178465_e = new ModelBanner();
    }
    
    public void func_180545_a(final TileEntityBanner p_180545_1_, final double p_180545_2_, final double p_180545_4_, final double p_180545_6_, final float p_180545_8_, final int p_180545_9_) {
        final boolean var10 = p_180545_1_.getWorld() != null;
        final boolean var11 = !var10 || p_180545_1_.getBlockType() == Blocks.standing_banner;
        final int var12 = var10 ? p_180545_1_.getBlockMetadata() : 0;
        final long var13 = var10 ? p_180545_1_.getWorld().getTotalWorldTime() : 0L;
        GlStateManager.pushMatrix();
        final float var14 = 0.6666667f;
        if (var11) {
            GlStateManager.translate((float)p_180545_2_ + 0.5f, (float)p_180545_4_ + 0.75f * var14, (float)p_180545_6_ + 0.5f);
            final float var15 = var12 * 360 / 16.0f;
            GlStateManager.rotate(-var15, 0.0f, 1.0f, 0.0f);
            this.field_178465_e.bannerStand.showModel = true;
        }
        else {
            float var16 = 0.0f;
            if (var12 == 2) {
                var16 = 180.0f;
            }
            if (var12 == 4) {
                var16 = 90.0f;
            }
            if (var12 == 5) {
                var16 = -90.0f;
            }
            GlStateManager.translate((float)p_180545_2_ + 0.5f, (float)p_180545_4_ - 0.25f * var14, (float)p_180545_6_ + 0.5f);
            GlStateManager.rotate(-var16, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(0.0f, -0.3125f, -0.4375f);
            this.field_178465_e.bannerStand.showModel = false;
        }
        final BlockPos var17 = p_180545_1_.getPos();
        float var16 = var17.getX() * 7 + var17.getY() * 9 + var17.getZ() * 13 + var13 + p_180545_8_;
        this.field_178465_e.bannerSlate.rotateAngleX = (-0.0125f + 0.01f * MathHelper.cos(var16 * 3.1415927f * 0.02f)) * 3.1415927f;
        GlStateManager.enableRescaleNormal();
        final ResourceLocation var18 = this.func_178463_a(p_180545_1_);
        if (var18 != null) {
            this.bindTexture(var18);
            GlStateManager.pushMatrix();
            GlStateManager.scale(var14, -var14, -var14);
            this.field_178465_e.func_178687_a();
            GlStateManager.popMatrix();
        }
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }
    
    private ResourceLocation func_178463_a(final TileEntityBanner p_178463_1_) {
        final String var2 = p_178463_1_.func_175116_e();
        if (var2.isEmpty()) {
            return null;
        }
        TimedBannerTexture var3 = TileEntityBannerRenderer.field_178466_c.get(var2);
        if (var3 == null) {
            if (TileEntityBannerRenderer.field_178466_c.size() >= 256) {
                final long var4 = System.currentTimeMillis();
                final Iterator var5 = TileEntityBannerRenderer.field_178466_c.keySet().iterator();
                while (var5.hasNext()) {
                    final String var6 = var5.next();
                    final TimedBannerTexture var7 = TileEntityBannerRenderer.field_178466_c.get(var6);
                    if (var4 - var7.field_178472_a > 60000L) {
                        Minecraft.getMinecraft().getTextureManager().deleteTexture(var7.field_178471_b);
                        var5.remove();
                    }
                }
                if (TileEntityBannerRenderer.field_178466_c.size() >= 256) {
                    return null;
                }
            }
            final List var8 = p_178463_1_.func_175114_c();
            final List var9 = p_178463_1_.func_175110_d();
            final ArrayList var10 = Lists.newArrayList();
            for (final TileEntityBanner.EnumBannerPattern var12 : var8) {
                var10.add("textures/entity/banner/" + var12.func_177271_a() + ".png");
            }
            var3 = new TimedBannerTexture(null);
            var3.field_178471_b = new ResourceLocation(var2);
            Minecraft.getMinecraft().getTextureManager().loadTexture(var3.field_178471_b, new LayeredColorMaskTexture(TileEntityBannerRenderer.field_178464_d, var10, var9));
            TileEntityBannerRenderer.field_178466_c.put(var2, var3);
        }
        var3.field_178472_a = System.currentTimeMillis();
        return var3.field_178471_b;
    }
    
    @Override
    public void renderTileEntityAt(final TileEntity p_180535_1_, final double p_180535_2_, final double p_180535_4_, final double p_180535_6_, final float p_180535_8_, final int p_180535_9_) {
        this.func_180545_a((TileEntityBanner)p_180535_1_, p_180535_2_, p_180535_4_, p_180535_6_, p_180535_8_, p_180535_9_);
    }
    
    static {
        field_178466_c = Maps.newHashMap();
        field_178464_d = new ResourceLocation("textures/entity/banner_base.png");
    }
    
    static class TimedBannerTexture
    {
        public long field_178472_a;
        public ResourceLocation field_178471_b;
        private static final String __OBFID = "CL_00002471";
        
        private TimedBannerTexture() {
        }
        
        TimedBannerTexture(final Object p_i46209_1_) {
            this();
        }
    }
}
