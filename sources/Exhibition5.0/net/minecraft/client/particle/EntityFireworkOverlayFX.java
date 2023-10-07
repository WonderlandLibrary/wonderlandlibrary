// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;

public class EntityFireworkOverlayFX extends EntityFX
{
    private static final String __OBFID = "CL_00000904";
    
    protected EntityFireworkOverlayFX(final World worldIn, final double p_i46357_2_, final double p_i46357_4_, final double p_i46357_6_) {
        super(worldIn, p_i46357_2_, p_i46357_4_, p_i46357_6_);
        this.particleMaxAge = 4;
    }
    
    @Override
    public void func_180434_a(final WorldRenderer p_180434_1_, final Entity p_180434_2_, final float p_180434_3_, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        final float var9 = 0.25f;
        final float var10 = var9 + 0.25f;
        final float var11 = 0.125f;
        final float var12 = var11 + 0.25f;
        final float var13 = 7.1f * MathHelper.sin((this.particleAge + p_180434_3_ - 1.0f) * 0.25f * 3.1415927f);
        this.particleAlpha = 0.6f - (this.particleAge + p_180434_3_ - 1.0f) * 0.25f * 0.5f;
        final float var14 = (float)(this.prevPosX + (this.posX - this.prevPosX) * p_180434_3_ - EntityFireworkOverlayFX.interpPosX);
        final float var15 = (float)(this.prevPosY + (this.posY - this.prevPosY) * p_180434_3_ - EntityFireworkOverlayFX.interpPosY);
        final float var16 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * p_180434_3_ - EntityFireworkOverlayFX.interpPosZ);
        p_180434_1_.setColorRGBA(this.particleRed, this.particleGreen, this.particleBlue, this.particleAlpha);
        p_180434_1_.addVertexWithUV(var14 - p_180434_4_ * var13 - p_180434_7_ * var13, var15 - p_180434_5_ * var13, var16 - p_180434_6_ * var13 - p_180434_8_ * var13, var10, var12);
        p_180434_1_.addVertexWithUV(var14 - p_180434_4_ * var13 + p_180434_7_ * var13, var15 + p_180434_5_ * var13, var16 - p_180434_6_ * var13 + p_180434_8_ * var13, var10, var11);
        p_180434_1_.addVertexWithUV(var14 + p_180434_4_ * var13 + p_180434_7_ * var13, var15 + p_180434_5_ * var13, var16 + p_180434_6_ * var13 + p_180434_8_ * var13, var9, var11);
        p_180434_1_.addVertexWithUV(var14 + p_180434_4_ * var13 - p_180434_7_ * var13, var15 - p_180434_5_ * var13, var16 + p_180434_6_ * var13 - p_180434_8_ * var13, var9, var12);
    }
}
