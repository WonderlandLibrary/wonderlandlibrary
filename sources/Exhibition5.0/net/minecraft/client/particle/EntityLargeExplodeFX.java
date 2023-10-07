// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class EntityLargeExplodeFX extends EntityFX
{
    private static final ResourceLocation field_110127_a;
    private int field_70581_a;
    private int field_70584_aq;
    private TextureManager theRenderEngine;
    private float field_70582_as;
    private static final String __OBFID = "CL_00000910";
    
    protected EntityLargeExplodeFX(final TextureManager p_i1213_1_, final World worldIn, final double p_i1213_3_, final double p_i1213_5_, final double p_i1213_7_, final double p_i1213_9_, final double p_i1213_11_, final double p_i1213_13_) {
        super(worldIn, p_i1213_3_, p_i1213_5_, p_i1213_7_, 0.0, 0.0, 0.0);
        this.theRenderEngine = p_i1213_1_;
        this.field_70584_aq = 6 + this.rand.nextInt(4);
        final float particleRed = this.rand.nextFloat() * 0.6f + 0.4f;
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        this.field_70582_as = 1.0f - (float)p_i1213_9_ * 0.5f;
    }
    
    @Override
    public void func_180434_a(final WorldRenderer p_180434_1_, final Entity p_180434_2_, final float p_180434_3_, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        final int var9 = (int)((this.field_70581_a + p_180434_3_) * 15.0f / this.field_70584_aq);
        if (var9 <= 15) {
            this.theRenderEngine.bindTexture(EntityLargeExplodeFX.field_110127_a);
            final float var10 = var9 % 4 / 4.0f;
            final float var11 = var10 + 0.24975f;
            final float var12 = var9 / 4 / 4.0f;
            final float var13 = var12 + 0.24975f;
            final float var14 = 2.0f * this.field_70582_as;
            final float var15 = (float)(this.prevPosX + (this.posX - this.prevPosX) * p_180434_3_ - EntityLargeExplodeFX.interpPosX);
            final float var16 = (float)(this.prevPosY + (this.posY - this.prevPosY) * p_180434_3_ - EntityLargeExplodeFX.interpPosY);
            final float var17 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * p_180434_3_ - EntityLargeExplodeFX.interpPosZ);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableLighting();
            RenderHelper.disableStandardItemLighting();
            p_180434_1_.startDrawingQuads();
            p_180434_1_.setColorRGBA(this.particleRed, this.particleGreen, this.particleBlue, 1.0f);
            p_180434_1_.func_178980_d(0.0f, 1.0f, 0.0f);
            p_180434_1_.func_178963_b(240);
            p_180434_1_.addVertexWithUV(var15 - p_180434_4_ * var14 - p_180434_7_ * var14, var16 - p_180434_5_ * var14, var17 - p_180434_6_ * var14 - p_180434_8_ * var14, var11, var13);
            p_180434_1_.addVertexWithUV(var15 - p_180434_4_ * var14 + p_180434_7_ * var14, var16 + p_180434_5_ * var14, var17 - p_180434_6_ * var14 + p_180434_8_ * var14, var11, var12);
            p_180434_1_.addVertexWithUV(var15 + p_180434_4_ * var14 + p_180434_7_ * var14, var16 + p_180434_5_ * var14, var17 + p_180434_6_ * var14 + p_180434_8_ * var14, var10, var12);
            p_180434_1_.addVertexWithUV(var15 + p_180434_4_ * var14 - p_180434_7_ * var14, var16 - p_180434_5_ * var14, var17 + p_180434_6_ * var14 - p_180434_8_ * var14, var10, var13);
            Tessellator.getInstance().draw();
            GlStateManager.doPolygonOffset(0.0f, 0.0f);
            GlStateManager.enableLighting();
        }
    }
    
    @Override
    public int getBrightnessForRender(final float p_70070_1_) {
        return 61680;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        ++this.field_70581_a;
        if (this.field_70581_a == this.field_70584_aq) {
            this.setDead();
        }
    }
    
    @Override
    public int getFXLayer() {
        return 3;
    }
    
    static {
        field_110127_a = new ResourceLocation("textures/entity/explosion.png");
    }
    
    public static class Factory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002598";
        
        @Override
        public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
            return new EntityLargeExplodeFX(Minecraft.getMinecraft().getTextureManager(), worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}
