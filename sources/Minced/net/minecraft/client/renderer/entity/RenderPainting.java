// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.item.EntityPainting;

public class RenderPainting extends Render<EntityPainting>
{
    private static final ResourceLocation KRISTOFFER_PAINTING_TEXTURE;
    
    public RenderPainting(final RenderManager renderManagerIn) {
        super(renderManagerIn);
    }
    
    @Override
    public void doRender(final EntityPainting entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(180.0f - entityYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.enableRescaleNormal();
        this.bindEntityTexture(entity);
        final EntityPainting.EnumArt entitypainting$enumart = entity.art;
        final float f = 0.0625f;
        GlStateManager.scale(0.0625f, 0.0625f, 0.0625f);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        this.renderPainting(entity, entitypainting$enumart.sizeX, entitypainting$enumart.sizeY, entitypainting$enumart.offsetX, entitypainting$enumart.offsetY);
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityPainting entity) {
        return RenderPainting.KRISTOFFER_PAINTING_TEXTURE;
    }
    
    private void renderPainting(final EntityPainting painting, final int width, final int height, final int textureU, final int textureV) {
        final float f = -width / 2.0f;
        final float f2 = -height / 2.0f;
        final float f3 = 0.5f;
        final float f4 = 0.75f;
        final float f5 = 0.8125f;
        final float f6 = 0.0f;
        final float f7 = 0.0625f;
        final float f8 = 0.75f;
        final float f9 = 0.8125f;
        final float f10 = 0.001953125f;
        final float f11 = 0.001953125f;
        final float f12 = 0.7519531f;
        final float f13 = 0.7519531f;
        final float f14 = 0.0f;
        final float f15 = 0.0625f;
        for (int i = 0; i < width / 16; ++i) {
            for (int j = 0; j < height / 16; ++j) {
                final float f16 = f + (i + 1) * 16;
                final float f17 = f + i * 16;
                final float f18 = f2 + (j + 1) * 16;
                final float f19 = f2 + j * 16;
                this.setLightmap(painting, (f16 + f17) / 2.0f, (f18 + f19) / 2.0f);
                final float f20 = (textureU + width - i * 16) / 256.0f;
                final float f21 = (textureU + width - (i + 1) * 16) / 256.0f;
                final float f22 = (textureV + height - j * 16) / 256.0f;
                final float f23 = (textureV + height - (j + 1) * 16) / 256.0f;
                final Tessellator tessellator = Tessellator.getInstance();
                final BufferBuilder bufferbuilder = tessellator.getBuffer();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
                bufferbuilder.pos(f16, f19, -0.5).tex(f21, f22).normal(0.0f, 0.0f, -1.0f).endVertex();
                bufferbuilder.pos(f17, f19, -0.5).tex(f20, f22).normal(0.0f, 0.0f, -1.0f).endVertex();
                bufferbuilder.pos(f17, f18, -0.5).tex(f20, f23).normal(0.0f, 0.0f, -1.0f).endVertex();
                bufferbuilder.pos(f16, f18, -0.5).tex(f21, f23).normal(0.0f, 0.0f, -1.0f).endVertex();
                bufferbuilder.pos(f16, f18, 0.5).tex(0.75, 0.0).normal(0.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(f17, f18, 0.5).tex(0.8125, 0.0).normal(0.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(f17, f19, 0.5).tex(0.8125, 0.0625).normal(0.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(f16, f19, 0.5).tex(0.75, 0.0625).normal(0.0f, 0.0f, 1.0f).endVertex();
                bufferbuilder.pos(f16, f18, -0.5).tex(0.75, 0.001953125).normal(0.0f, 1.0f, 0.0f).endVertex();
                bufferbuilder.pos(f17, f18, -0.5).tex(0.8125, 0.001953125).normal(0.0f, 1.0f, 0.0f).endVertex();
                bufferbuilder.pos(f17, f18, 0.5).tex(0.8125, 0.001953125).normal(0.0f, 1.0f, 0.0f).endVertex();
                bufferbuilder.pos(f16, f18, 0.5).tex(0.75, 0.001953125).normal(0.0f, 1.0f, 0.0f).endVertex();
                bufferbuilder.pos(f16, f19, 0.5).tex(0.75, 0.001953125).normal(0.0f, -1.0f, 0.0f).endVertex();
                bufferbuilder.pos(f17, f19, 0.5).tex(0.8125, 0.001953125).normal(0.0f, -1.0f, 0.0f).endVertex();
                bufferbuilder.pos(f17, f19, -0.5).tex(0.8125, 0.001953125).normal(0.0f, -1.0f, 0.0f).endVertex();
                bufferbuilder.pos(f16, f19, -0.5).tex(0.75, 0.001953125).normal(0.0f, -1.0f, 0.0f).endVertex();
                bufferbuilder.pos(f16, f18, 0.5).tex(0.751953125, 0.0).normal(-1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(f16, f19, 0.5).tex(0.751953125, 0.0625).normal(-1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(f16, f19, -0.5).tex(0.751953125, 0.0625).normal(-1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(f16, f18, -0.5).tex(0.751953125, 0.0).normal(-1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(f17, f18, -0.5).tex(0.751953125, 0.0).normal(1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(f17, f19, -0.5).tex(0.751953125, 0.0625).normal(1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(f17, f19, 0.5).tex(0.751953125, 0.0625).normal(1.0f, 0.0f, 0.0f).endVertex();
                bufferbuilder.pos(f17, f18, 0.5).tex(0.751953125, 0.0).normal(1.0f, 0.0f, 0.0f).endVertex();
                tessellator.draw();
            }
        }
    }
    
    private void setLightmap(final EntityPainting painting, final float p_77008_2_, final float p_77008_3_) {
        int i = MathHelper.floor(painting.posX);
        final int j = MathHelper.floor(painting.posY + p_77008_3_ / 16.0f);
        int k = MathHelper.floor(painting.posZ);
        final EnumFacing enumfacing = painting.facingDirection;
        if (enumfacing == EnumFacing.NORTH) {
            i = MathHelper.floor(painting.posX + p_77008_2_ / 16.0f);
        }
        if (enumfacing == EnumFacing.WEST) {
            k = MathHelper.floor(painting.posZ - p_77008_2_ / 16.0f);
        }
        if (enumfacing == EnumFacing.SOUTH) {
            i = MathHelper.floor(painting.posX - p_77008_2_ / 16.0f);
        }
        if (enumfacing == EnumFacing.EAST) {
            k = MathHelper.floor(painting.posZ + p_77008_2_ / 16.0f);
        }
        final int l = this.renderManager.world.getCombinedLight(new BlockPos(i, j, k), 0);
        final int i2 = l % 65536;
        final int j2 = l / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i2, (float)j2);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
    }
    
    static {
        KRISTOFFER_PAINTING_TEXTURE = new ResourceLocation("textures/painting/paintings_kristoffer_zetterstrand.png");
    }
}
