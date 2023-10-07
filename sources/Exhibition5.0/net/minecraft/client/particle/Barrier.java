// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class Barrier extends EntityFX
{
    private static final String __OBFID = "CL_00002615";
    
    protected Barrier(final World worldIn, final double p_i46286_2_, final double p_i46286_4_, final double p_i46286_6_, final Item p_i46286_8_) {
        super(worldIn, p_i46286_2_, p_i46286_4_, p_i46286_6_, 0.0, 0.0, 0.0);
        this.func_180435_a(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(p_i46286_8_));
        final float particleRed = 1.0f;
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        final double motionX = 0.0;
        this.motionZ = motionX;
        this.motionY = motionX;
        this.motionX = motionX;
        this.particleGravity = 0.0f;
        this.particleMaxAge = 80;
    }
    
    @Override
    public int getFXLayer() {
        return 1;
    }
    
    @Override
    public void func_180434_a(final WorldRenderer p_180434_1_, final Entity p_180434_2_, final float p_180434_3_, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        final float var9 = this.particleIcon.getMinU();
        final float var10 = this.particleIcon.getMaxU();
        final float var11 = this.particleIcon.getMinV();
        final float var12 = this.particleIcon.getMaxV();
        final float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * p_180434_3_ - Barrier.interpPosX);
        final float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * p_180434_3_ - Barrier.interpPosY);
        final float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * p_180434_3_ - Barrier.interpPosZ);
        p_180434_1_.func_178986_b(this.particleRed, this.particleGreen, this.particleBlue);
        final float var16 = 0.5f;
        p_180434_1_.addVertexWithUV(var13 - p_180434_4_ * var16 - p_180434_7_ * var16, var14 - p_180434_5_ * var16, var15 - p_180434_6_ * var16 - p_180434_8_ * var16, var10, var12);
        p_180434_1_.addVertexWithUV(var13 - p_180434_4_ * var16 + p_180434_7_ * var16, var14 + p_180434_5_ * var16, var15 - p_180434_6_ * var16 + p_180434_8_ * var16, var10, var11);
        p_180434_1_.addVertexWithUV(var13 + p_180434_4_ * var16 + p_180434_7_ * var16, var14 + p_180434_5_ * var16, var15 + p_180434_6_ * var16 + p_180434_8_ * var16, var9, var11);
        p_180434_1_.addVertexWithUV(var13 + p_180434_4_ * var16 - p_180434_7_ * var16, var14 - p_180434_5_ * var16, var15 + p_180434_6_ * var16 - p_180434_8_ * var16, var9, var12);
    }
    
    public static class Factory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002614";
        
        @Override
        public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
            return new Barrier(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, Item.getItemFromBlock(Blocks.barrier));
        }
    }
}
