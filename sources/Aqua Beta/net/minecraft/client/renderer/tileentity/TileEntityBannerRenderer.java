package net.minecraft.client.renderer.tileentity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBanner;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.LayeredColorMaskTexture;
import net.minecraft.client.renderer.tileentity.TileEntityBannerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class TileEntityBannerRenderer
extends TileEntitySpecialRenderer<TileEntityBanner> {
    private static final Map<String, TimedBannerTexture> DESIGNS = Maps.newHashMap();
    private static final ResourceLocation BANNERTEXTURES = new ResourceLocation("textures/entity/banner_base.png");
    private ModelBanner bannerModel = new ModelBanner();

    public void renderTileEntityAt(TileEntityBanner te, double x, double y, double z, float partialTicks, int destroyStage) {
        boolean flag = te.getWorld() != null;
        boolean flag1 = !flag || te.getBlockType() == Blocks.standing_banner;
        int i = flag ? te.getBlockMetadata() : 0;
        long j = flag ? te.getWorld().getTotalWorldTime() : 0L;
        GlStateManager.pushMatrix();
        float f = 0.6666667f;
        if (flag1) {
            GlStateManager.translate((float)((float)x + 0.5f), (float)((float)y + 0.75f * f), (float)((float)z + 0.5f));
            float f1 = (float)(i * 360) / 16.0f;
            GlStateManager.rotate((float)(-f1), (float)0.0f, (float)1.0f, (float)0.0f);
            this.bannerModel.bannerStand.showModel = true;
        } else {
            float f2 = 0.0f;
            if (i == 2) {
                f2 = 180.0f;
            }
            if (i == 4) {
                f2 = 90.0f;
            }
            if (i == 5) {
                f2 = -90.0f;
            }
            GlStateManager.translate((float)((float)x + 0.5f), (float)((float)y - 0.25f * f), (float)((float)z + 0.5f));
            GlStateManager.rotate((float)(-f2), (float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.translate((float)0.0f, (float)-0.3125f, (float)-0.4375f);
            this.bannerModel.bannerStand.showModel = false;
        }
        BlockPos blockpos = te.getPos();
        float f3 = (float)(blockpos.getX() * 7 + blockpos.getY() * 9 + blockpos.getZ() * 13) + (float)j + partialTicks;
        this.bannerModel.bannerSlate.rotateAngleX = (-0.0125f + 0.01f * MathHelper.cos((float)(f3 * (float)Math.PI * 0.02f))) * (float)Math.PI;
        GlStateManager.enableRescaleNormal();
        ResourceLocation resourcelocation = this.func_178463_a(te);
        if (resourcelocation != null) {
            this.bindTexture(resourcelocation);
            GlStateManager.pushMatrix();
            GlStateManager.scale((float)f, (float)(-f), (float)(-f));
            this.bannerModel.renderBanner();
            GlStateManager.popMatrix();
        }
        GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.popMatrix();
    }

    private ResourceLocation func_178463_a(TileEntityBanner bannerObj) {
        String s = bannerObj.getPatternResourceLocation();
        if (s.isEmpty()) {
            return null;
        }
        TimedBannerTexture tileentitybannerrenderer$timedbannertexture = (TimedBannerTexture)DESIGNS.get((Object)s);
        if (tileentitybannerrenderer$timedbannertexture == null) {
            if (DESIGNS.size() >= 256) {
                long i = System.currentTimeMillis();
                Iterator iterator = DESIGNS.keySet().iterator();
                while (iterator.hasNext()) {
                    String s1 = (String)iterator.next();
                    TimedBannerTexture tileentitybannerrenderer$timedbannertexture1 = (TimedBannerTexture)DESIGNS.get((Object)s1);
                    if (i - tileentitybannerrenderer$timedbannertexture1.systemTime <= 60000L) continue;
                    Minecraft.getMinecraft().getTextureManager().deleteTexture(tileentitybannerrenderer$timedbannertexture1.bannerTexture);
                    iterator.remove();
                }
                if (DESIGNS.size() >= 256) {
                    return null;
                }
            }
            List list1 = bannerObj.getPatternList();
            List list = bannerObj.getColorList();
            ArrayList list2 = Lists.newArrayList();
            for (TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern : list1) {
                list2.add((Object)("textures/entity/banner/" + tileentitybanner$enumbannerpattern.getPatternName() + ".png"));
            }
            tileentitybannerrenderer$timedbannertexture = new TimedBannerTexture(null);
            tileentitybannerrenderer$timedbannertexture.bannerTexture = new ResourceLocation(s);
            Minecraft.getMinecraft().getTextureManager().loadTexture(tileentitybannerrenderer$timedbannertexture.bannerTexture, (ITextureObject)new LayeredColorMaskTexture(BANNERTEXTURES, (List)list2, list));
            DESIGNS.put((Object)s, (Object)tileentitybannerrenderer$timedbannertexture);
        }
        tileentitybannerrenderer$timedbannertexture.systemTime = System.currentTimeMillis();
        return tileentitybannerrenderer$timedbannertexture.bannerTexture;
    }
}
