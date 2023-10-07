// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import exhibition.event.Event;
import org.lwjgl.opengl.GL11;
import exhibition.event.EventSystem;
import exhibition.event.impl.EventNametagRender;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.Block;
import java.util.Iterator;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public abstract class Render
{
    private static final ResourceLocation shadowTextures;
    protected final RenderManager renderManager;
    protected float shadowSize;
    protected float shadowOpaque;
    private static final String __OBFID = "CL_00000992";
    
    protected Render(final RenderManager p_i46179_1_) {
        this.shadowOpaque = 1.0f;
        this.renderManager = p_i46179_1_;
    }
    
    public boolean func_177071_a(final Entity p_177071_1_, final ICamera p_177071_2_, final double p_177071_3_, final double p_177071_5_, final double p_177071_7_) {
        return p_177071_1_.isInRangeToRender3d(p_177071_3_, p_177071_5_, p_177071_7_) && (p_177071_1_.ignoreFrustumCheck || p_177071_2_.isBoundingBoxInFrustum(p_177071_1_.getEntityBoundingBox()));
    }
    
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.func_177067_a(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_);
    }
    
    protected void func_177067_a(final Entity p_177067_1_, final double p_177067_2_, final double p_177067_4_, final double p_177067_6_) {
        if (this.func_177070_b(p_177067_1_)) {
            this.renderLivingLabel(p_177067_1_, p_177067_1_.getDisplayName().getFormattedText(), p_177067_2_, p_177067_4_, p_177067_6_, 64);
        }
    }
    
    protected boolean func_177070_b(final Entity p_177070_1_) {
        return p_177070_1_.getAlwaysRenderNameTagForRender() && p_177070_1_.hasCustomName();
    }
    
    protected void func_177069_a(final Entity p_177069_1_, final double p_177069_2_, final double p_177069_4_, final double p_177069_6_, final String p_177069_8_, final float p_177069_9_, final double p_177069_10_) {
        this.renderLivingLabel(p_177069_1_, p_177069_8_, p_177069_2_, p_177069_4_, p_177069_6_, 64);
    }
    
    protected abstract ResourceLocation getEntityTexture(final Entity p0);
    
    protected boolean bindEntityTexture(final Entity p_180548_1_) {
        final ResourceLocation var2 = this.getEntityTexture(p_180548_1_);
        if (var2 == null) {
            return false;
        }
        this.bindTexture(var2);
        return true;
    }
    
    public void bindTexture(final ResourceLocation p_110776_1_) {
        this.renderManager.renderEngine.bindTexture(p_110776_1_);
    }
    
    private void renderEntityOnFire(final Entity p_76977_1_, final double p_76977_2_, final double p_76977_4_, final double p_76977_6_, final float p_76977_8_) {
        GlStateManager.disableLighting();
        final TextureMap var9 = Minecraft.getMinecraft().getTextureMapBlocks();
        final TextureAtlasSprite var10 = var9.getAtlasSprite("minecraft:blocks/fire_layer_0");
        final TextureAtlasSprite var11 = var9.getAtlasSprite("minecraft:blocks/fire_layer_1");
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)p_76977_2_, (float)p_76977_4_, (float)p_76977_6_);
        final float var12 = p_76977_1_.width * 1.4f;
        GlStateManager.scale(var12, var12, var12);
        final Tessellator var13 = Tessellator.getInstance();
        final WorldRenderer var14 = var13.getWorldRenderer();
        float var15 = 0.5f;
        final float var16 = 0.0f;
        float var17 = p_76977_1_.height / var12;
        float var18 = (float)(p_76977_1_.posY - p_76977_1_.getEntityBoundingBox().minY);
        final RenderManager renderManager = this.renderManager;
        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, -0.3f + (int)var17 * 0.02f);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        float var19 = 0.0f;
        int var20 = 0;
        var14.startDrawingQuads();
        while (var17 > 0.0f) {
            final TextureAtlasSprite var21 = (var20 % 2 == 0) ? var10 : var11;
            this.bindTexture(TextureMap.locationBlocksTexture);
            float var22 = var21.getMinU();
            final float var23 = var21.getMinV();
            float var24 = var21.getMaxU();
            final float var25 = var21.getMaxV();
            if (var20 / 2 % 2 == 0) {
                final float var26 = var24;
                var24 = var22;
                var22 = var26;
            }
            var14.addVertexWithUV(var15 - var16, 0.0f - var18, var19, var24, var25);
            var14.addVertexWithUV(-var15 - var16, 0.0f - var18, var19, var22, var25);
            var14.addVertexWithUV(-var15 - var16, 1.4f - var18, var19, var22, var23);
            var14.addVertexWithUV(var15 - var16, 1.4f - var18, var19, var24, var23);
            var17 -= 0.45f;
            var18 -= 0.45f;
            var15 *= 0.9f;
            var19 += 0.03f;
            ++var20;
        }
        var13.draw();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }
    
    private void renderShadow(final Entity p_76975_1_, final double p_76975_2_, final double p_76975_4_, final double p_76975_6_, final float p_76975_8_, final float p_76975_9_) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        this.renderManager.renderEngine.bindTexture(Render.shadowTextures);
        final World var10 = this.getWorldFromRenderManager();
        GlStateManager.depthMask(false);
        float var11 = this.shadowSize;
        if (p_76975_1_ instanceof EntityLiving) {
            final EntityLiving var12 = (EntityLiving)p_76975_1_;
            var11 *= var12.getRenderSizeModifier();
            if (var12.isChild()) {
                var11 *= 0.5f;
            }
        }
        final double var13 = p_76975_1_.lastTickPosX + (p_76975_1_.posX - p_76975_1_.lastTickPosX) * p_76975_9_;
        final double var14 = p_76975_1_.lastTickPosY + (p_76975_1_.posY - p_76975_1_.lastTickPosY) * p_76975_9_;
        final double var15 = p_76975_1_.lastTickPosZ + (p_76975_1_.posZ - p_76975_1_.lastTickPosZ) * p_76975_9_;
        final int var16 = MathHelper.floor_double(var13 - var11);
        final int var17 = MathHelper.floor_double(var13 + var11);
        final int var18 = MathHelper.floor_double(var14 - var11);
        final int var19 = MathHelper.floor_double(var14);
        final int var20 = MathHelper.floor_double(var15 - var11);
        final int var21 = MathHelper.floor_double(var15 + var11);
        final double var22 = p_76975_2_ - var13;
        final double var23 = p_76975_4_ - var14;
        final double var24 = p_76975_6_ - var15;
        final Tessellator var25 = Tessellator.getInstance();
        final WorldRenderer var26 = var25.getWorldRenderer();
        var26.startDrawingQuads();
        for (final BlockPos var28 : BlockPos.getAllInBox(new BlockPos(var16, var18, var20), new BlockPos(var17, var19, var21))) {
            final Block var29 = var10.getBlockState(var28.offsetDown()).getBlock();
            if (var29.getRenderType() != -1 && var10.getLightFromNeighbors(var28) > 3) {
                this.func_180549_a(var29, p_76975_2_, p_76975_4_, p_76975_6_, var28, p_76975_8_, var11, var22, var23, var24);
            }
        }
        var25.draw();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }
    
    private World getWorldFromRenderManager() {
        return this.renderManager.worldObj;
    }
    
    private void func_180549_a(final Block p_180549_1_, final double p_180549_2_, final double p_180549_4_, final double p_180549_6_, final BlockPos p_180549_8_, final float p_180549_9_, final float p_180549_10_, final double p_180549_11_, final double p_180549_13_, final double p_180549_15_) {
        if (p_180549_1_.isFullCube()) {
            final Tessellator var17 = Tessellator.getInstance();
            final WorldRenderer var18 = var17.getWorldRenderer();
            double var19 = (p_180549_9_ - (p_180549_4_ - (p_180549_8_.getY() + p_180549_13_)) / 2.0) * 0.5 * this.getWorldFromRenderManager().getLightBrightness(p_180549_8_);
            if (var19 >= 0.0) {
                if (var19 > 1.0) {
                    var19 = 1.0;
                }
                var18.setColorRGBA(1.0f, 1.0f, 1.0f, (float)var19);
                final double var20 = p_180549_8_.getX() + p_180549_1_.getBlockBoundsMinX() + p_180549_11_;
                final double var21 = p_180549_8_.getX() + p_180549_1_.getBlockBoundsMaxX() + p_180549_11_;
                final double var22 = p_180549_8_.getY() + p_180549_1_.getBlockBoundsMinY() + p_180549_13_ + 0.015625;
                final double var23 = p_180549_8_.getZ() + p_180549_1_.getBlockBoundsMinZ() + p_180549_15_;
                final double var24 = p_180549_8_.getZ() + p_180549_1_.getBlockBoundsMaxZ() + p_180549_15_;
                final float var25 = (float)((p_180549_2_ - var20) / 2.0 / p_180549_10_ + 0.5);
                final float var26 = (float)((p_180549_2_ - var21) / 2.0 / p_180549_10_ + 0.5);
                final float var27 = (float)((p_180549_6_ - var23) / 2.0 / p_180549_10_ + 0.5);
                final float var28 = (float)((p_180549_6_ - var24) / 2.0 / p_180549_10_ + 0.5);
                var18.addVertexWithUV(var20, var22, var23, var25, var27);
                var18.addVertexWithUV(var20, var22, var24, var25, var28);
                var18.addVertexWithUV(var21, var22, var24, var26, var28);
                var18.addVertexWithUV(var21, var22, var23, var26, var27);
            }
        }
    }
    
    public static void renderOffsetAABB(final AxisAlignedBB p_76978_0_, final double p_76978_1_, final double p_76978_3_, final double p_76978_5_) {
        GlStateManager.disableTextures();
        final Tessellator var7 = Tessellator.getInstance();
        final WorldRenderer var8 = var7.getWorldRenderer();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        var8.startDrawingQuads();
        var8.setTranslation(p_76978_1_, p_76978_3_, p_76978_5_);
        var8.func_178980_d(0.0f, 0.0f, -1.0f);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.minZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.minZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.minZ);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.minZ);
        var8.func_178980_d(0.0f, 0.0f, 1.0f);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.maxZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.maxZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.maxZ);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.maxZ);
        var8.func_178980_d(0.0f, -1.0f, 0.0f);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.minZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.minZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.maxZ);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.maxZ);
        var8.func_178980_d(0.0f, 1.0f, 0.0f);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.maxZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.maxZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.minZ);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.minZ);
        var8.func_178980_d(-1.0f, 0.0f, 0.0f);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.maxZ);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.maxZ);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.maxY, p_76978_0_.minZ);
        var8.addVertex(p_76978_0_.minX, p_76978_0_.minY, p_76978_0_.minZ);
        var8.func_178980_d(1.0f, 0.0f, 0.0f);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.minZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.minZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.maxY, p_76978_0_.maxZ);
        var8.addVertex(p_76978_0_.maxX, p_76978_0_.minY, p_76978_0_.maxZ);
        var8.setTranslation(0.0, 0.0, 0.0);
        var7.draw();
        GlStateManager.enableTextures();
    }
    
    public void doRenderShadowAndFire(final Entity p_76979_1_, final double p_76979_2_, final double p_76979_4_, final double p_76979_6_, final float p_76979_8_, final float p_76979_9_) {
        if (this.renderManager.options != null) {
            if (this.renderManager.options.fancyGraphics && this.shadowSize > 0.0f && !p_76979_1_.isInvisible() && this.renderManager.func_178627_a()) {
                final double var10 = this.renderManager.getDistanceToCamera(p_76979_1_.posX, p_76979_1_.posY, p_76979_1_.posZ);
                final float var11 = (float)((1.0 - var10 / 256.0) * this.shadowOpaque);
                if (var11 > 0.0f) {
                    this.renderShadow(p_76979_1_, p_76979_2_, p_76979_4_, p_76979_6_, var11, p_76979_9_);
                }
            }
            if (p_76979_1_.canRenderOnFire() && (!(p_76979_1_ instanceof EntityPlayer) || !((EntityPlayer)p_76979_1_).func_175149_v())) {
                this.renderEntityOnFire(p_76979_1_, p_76979_2_, p_76979_4_, p_76979_6_, p_76979_9_);
            }
        }
    }
    
    public FontRenderer getFontRendererFromRenderManager() {
        return this.renderManager.getFontRenderer();
    }
    
    protected void renderLivingLabel(final Entity p_147906_1_, final String p_147906_2_, final double p_147906_3_, final double p_147906_5_, final double p_147906_7_, final int p_147906_9_) {
        final double var10 = p_147906_1_.getDistanceSqToEntity(this.renderManager.livingPlayer);
        if (p_147906_1_ instanceof EntityPlayer) {
            final Event event = EventSystem.getInstance(EventNametagRender.class);
            event.fire();
            if (event.isCancelled()) {
                return;
            }
        }
        if (var10 <= p_147906_9_ * p_147906_9_) {
            final FontRenderer var11 = this.getFontRendererFromRenderManager();
            final float var12 = 1.6f;
            final float var13 = 0.016666668f * var12;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)p_147906_3_ + 0.0f, (float)p_147906_5_ + p_147906_1_.height + 0.5f, (float)p_147906_7_);
            GL11.glNormal3f(0.0f, 1.0f, 0.0f);
            final RenderManager renderManager = this.renderManager;
            GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            final RenderManager renderManager2 = this.renderManager;
            GlStateManager.rotate(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
            GlStateManager.scale(-var13, -var13, var13);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            final Tessellator var14 = Tessellator.getInstance();
            final WorldRenderer var15 = var14.getWorldRenderer();
            byte var16 = 0;
            if (p_147906_2_.equals("deadmau5")) {
                var16 = -10;
            }
            GlStateManager.disableTextures();
            var15.startDrawingQuads();
            final int var17 = var11.getStringWidth(p_147906_2_) / 2;
            var15.setColorRGBA(0.0f, 0.0f, 0.0f, 0.25f);
            var15.addVertex(-var17 - 1, -1 + var16, 0.0);
            var15.addVertex(-var17 - 1, 8 + var16, 0.0);
            var15.addVertex(var17 + 1, 8 + var16, 0.0);
            var15.addVertex(var17 + 1, -1 + var16, 0.0);
            var14.draw();
            GlStateManager.enableTextures();
            var11.drawString(p_147906_2_, -var11.getStringWidth(p_147906_2_) / 2, var16, 553648127);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            var11.drawString(p_147906_2_, -var11.getStringWidth(p_147906_2_) / 2, var16, -1);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.popMatrix();
        }
    }
    
    public RenderManager func_177068_d() {
        return this.renderManager;
    }
    
    static {
        shadowTextures = new ResourceLocation("textures/misc/shadow.png");
    }
}
