// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.world.World;

public class EntityFishWakeFX extends EntityFX
{
    private static final String __OBFID = "CL_00000933";
    
    protected EntityFishWakeFX(final World worldIn, final double p_i45073_2_, final double p_i45073_4_, final double p_i45073_6_, final double p_i45073_8_, final double p_i45073_10_, final double p_i45073_12_) {
        super(worldIn, p_i45073_2_, p_i45073_4_, p_i45073_6_, 0.0, 0.0, 0.0);
        this.motionX *= 0.30000001192092896;
        this.motionY = Math.random() * 0.20000000298023224 + 0.10000000149011612;
        this.motionZ *= 0.30000001192092896;
        this.particleRed = 1.0f;
        this.particleGreen = 1.0f;
        this.particleBlue = 1.0f;
        this.setParticleTextureIndex(19);
        this.setSize(0.01f, 0.01f);
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
        this.particleGravity = 0.0f;
        this.motionX = p_i45073_8_;
        this.motionY = p_i45073_10_;
        this.motionZ = p_i45073_12_;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= this.particleGravity;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863;
        this.motionY *= 0.9800000190734863;
        this.motionZ *= 0.9800000190734863;
        final int var1 = 60 - this.particleMaxAge;
        final float var2 = var1 * 0.001f;
        this.setSize(var2, var2);
        this.setParticleTextureIndex(19 + var1 % 4);
        if (this.particleMaxAge-- <= 0) {
            this.setDead();
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002573";
        
        @Override
        public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
            return new EntityFishWakeFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}
