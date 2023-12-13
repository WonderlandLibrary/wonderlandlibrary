// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;

public class EntityDiggingFX extends EntityFX
{
    private IBlockState field_174847_a;
    private static final String __OBFID = "CL_00000932";
    
    protected EntityDiggingFX(final World worldIn, final double p_i46280_2_, final double p_i46280_4_, final double p_i46280_6_, final double p_i46280_8_, final double p_i46280_10_, final double p_i46280_12_, final IBlockState p_i46280_14_) {
        super(worldIn, p_i46280_2_, p_i46280_4_, p_i46280_6_, p_i46280_8_, p_i46280_10_, p_i46280_12_);
        this.field_174847_a = p_i46280_14_;
        this.func_180435_a(Minecraft.getMinecraft().getBlockRendererDispatcher().func_175023_a().func_178122_a(p_i46280_14_));
        this.particleGravity = p_i46280_14_.getBlock().blockParticleGravity;
        final float particleRed = 0.6f;
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        this.particleScale /= 2.0f;
    }
    
    public EntityDiggingFX func_174846_a(final BlockPos p_174846_1_) {
        if (this.field_174847_a.getBlock() == Blocks.grass) {
            return this;
        }
        final int var2 = this.field_174847_a.getBlock().colorMultiplier(this.worldObj, p_174846_1_);
        this.particleRed *= (var2 >> 16 & 0xFF) / 255.0f;
        this.particleGreen *= (var2 >> 8 & 0xFF) / 255.0f;
        this.particleBlue *= (var2 & 0xFF) / 255.0f;
        return this;
    }
    
    public EntityDiggingFX func_174845_l() {
        final Block var1 = this.field_174847_a.getBlock();
        if (var1 == Blocks.grass) {
            return this;
        }
        final int var2 = var1.getRenderColor(this.field_174847_a);
        this.particleRed *= (var2 >> 16 & 0xFF) / 255.0f;
        this.particleGreen *= (var2 >> 8 & 0xFF) / 255.0f;
        this.particleBlue *= (var2 & 0xFF) / 255.0f;
        return this;
    }
    
    @Override
    public int getFXLayer() {
        return 1;
    }
    
    @Override
    public void func_180434_a(final WorldRenderer p_180434_1_, final Entity p_180434_2_, final float p_180434_3_, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        float var9 = (this.particleTextureIndexX + this.particleTextureJitterX / 4.0f) / 16.0f;
        float var10 = var9 + 0.015609375f;
        float var11 = (this.particleTextureIndexY + this.particleTextureJitterY / 4.0f) / 16.0f;
        float var12 = var11 + 0.015609375f;
        final float var13 = 0.1f * this.particleScale;
        if (this.particleIcon != null) {
            var9 = this.particleIcon.getInterpolatedU(this.particleTextureJitterX / 4.0f * 16.0f);
            var10 = this.particleIcon.getInterpolatedU((this.particleTextureJitterX + 1.0f) / 4.0f * 16.0f);
            var11 = this.particleIcon.getInterpolatedV(this.particleTextureJitterY / 4.0f * 16.0f);
            var12 = this.particleIcon.getInterpolatedV((this.particleTextureJitterY + 1.0f) / 4.0f * 16.0f);
        }
        final float var14 = (float)(this.prevPosX + (this.posX - this.prevPosX) * p_180434_3_ - EntityDiggingFX.interpPosX);
        final float var15 = (float)(this.prevPosY + (this.posY - this.prevPosY) * p_180434_3_ - EntityDiggingFX.interpPosY);
        final float var16 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * p_180434_3_ - EntityDiggingFX.interpPosZ);
        p_180434_1_.func_178986_b(this.particleRed, this.particleGreen, this.particleBlue);
        p_180434_1_.addVertexWithUV(var14 - p_180434_4_ * var13 - p_180434_7_ * var13, var15 - p_180434_5_ * var13, var16 - p_180434_6_ * var13 - p_180434_8_ * var13, var9, var12);
        p_180434_1_.addVertexWithUV(var14 - p_180434_4_ * var13 + p_180434_7_ * var13, var15 + p_180434_5_ * var13, var16 - p_180434_6_ * var13 + p_180434_8_ * var13, var9, var11);
        p_180434_1_.addVertexWithUV(var14 + p_180434_4_ * var13 + p_180434_7_ * var13, var15 + p_180434_5_ * var13, var16 + p_180434_6_ * var13 + p_180434_8_ * var13, var10, var11);
        p_180434_1_.addVertexWithUV(var14 + p_180434_4_ * var13 - p_180434_7_ * var13, var15 - p_180434_5_ * var13, var16 + p_180434_6_ * var13 - p_180434_8_ * var13, var10, var12);
    }
    
    public static class Factory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002575";
        
        @Override
        public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
            return new EntityDiggingFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_, Block.getStateById(p_178902_15_[0])).func_174845_l();
        }
    }
}
