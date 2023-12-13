// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;

public class EntityHeartFX extends EntityFX
{
    float particleScaleOverTime;
    private static final String __OBFID = "CL_00000909";
    
    protected EntityHeartFX(final World worldIn, final double p_i1211_2_, final double p_i1211_4_, final double p_i1211_6_, final double p_i1211_8_, final double p_i1211_10_, final double p_i1211_12_) {
        this(worldIn, p_i1211_2_, p_i1211_4_, p_i1211_6_, p_i1211_8_, p_i1211_10_, p_i1211_12_, 2.0f);
    }
    
    protected EntityHeartFX(final World worldIn, final double p_i46354_2_, final double p_i46354_4_, final double p_i46354_6_, final double p_i46354_8_, final double p_i46354_10_, final double p_i46354_12_, final float p_i46354_14_) {
        super(worldIn, p_i46354_2_, p_i46354_4_, p_i46354_6_, 0.0, 0.0, 0.0);
        this.motionX *= 0.009999999776482582;
        this.motionY *= 0.009999999776482582;
        this.motionZ *= 0.009999999776482582;
        this.motionY += 0.1;
        this.particleScale *= 0.75f;
        this.particleScale *= p_i46354_14_;
        this.particleScaleOverTime = this.particleScale;
        this.particleMaxAge = 16;
        this.noClip = false;
        this.setParticleTextureIndex(80);
    }
    
    @Override
    public void func_180434_a(final WorldRenderer p_180434_1_, final Entity p_180434_2_, final float p_180434_3_, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        float var9 = (this.particleAge + p_180434_3_) / this.particleMaxAge * 32.0f;
        var9 = MathHelper.clamp_float(var9, 0.0f, 1.0f);
        this.particleScale = this.particleScaleOverTime * var9;
        super.func_180434_a(p_180434_1_, p_180434_2_, p_180434_3_, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        if (this.posY == this.prevPosY) {
            this.motionX *= 1.1;
            this.motionZ *= 1.1;
        }
        this.motionX *= 0.8600000143051147;
        this.motionY *= 0.8600000143051147;
        this.motionZ *= 0.8600000143051147;
        if (this.onGround) {
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
        }
    }
    
    public static class AngryVillagerFactory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002600";
        
        @Override
        public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
            final EntityHeartFX var16 = new EntityHeartFX(worldIn, p_178902_3_, p_178902_5_ + 0.5, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
            var16.setParticleTextureIndex(81);
            var16.setRBGColorF(1.0f, 1.0f, 1.0f);
            return var16;
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002599";
        
        @Override
        public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
            return new EntityHeartFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}
