// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityLavaFX extends EntityFX
{
    private float lavaParticleScale;
    private static final String __OBFID = "CL_00000912";
    
    protected EntityLavaFX(final World worldIn, final double p_i1215_2_, final double p_i1215_4_, final double p_i1215_6_) {
        super(worldIn, p_i1215_2_, p_i1215_4_, p_i1215_6_, 0.0, 0.0, 0.0);
        this.motionX *= 0.800000011920929;
        this.motionY *= 0.800000011920929;
        this.motionZ *= 0.800000011920929;
        this.motionY = this.rand.nextFloat() * 0.4f + 0.05f;
        final float particleRed = 1.0f;
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        this.particleScale *= this.rand.nextFloat() * 2.0f + 0.2f;
        this.lavaParticleScale = this.particleScale;
        this.particleMaxAge = (int)(16.0 / (Math.random() * 0.8 + 0.2));
        this.noClip = false;
        this.setParticleTextureIndex(49);
    }
    
    @Override
    public int getBrightnessForRender(final float p_70070_1_) {
        float var2 = (this.particleAge + p_70070_1_) / this.particleMaxAge;
        var2 = MathHelper.clamp_float(var2, 0.0f, 1.0f);
        final int var3 = super.getBrightnessForRender(p_70070_1_);
        final short var4 = 240;
        final int var5 = var3 >> 16 & 0xFF;
        return var4 | var5 << 16;
    }
    
    @Override
    public float getBrightness(final float p_70013_1_) {
        return 1.0f;
    }
    
    @Override
    public void func_180434_a(final WorldRenderer p_180434_1_, final Entity p_180434_2_, final float p_180434_3_, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        final float var9 = (this.particleAge + p_180434_3_) / this.particleMaxAge;
        this.particleScale = this.lavaParticleScale * (1.0f - var9 * var9);
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
        final float var1 = this.particleAge / this.particleMaxAge;
        if (this.rand.nextFloat() > var1) {
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, new int[0]);
        }
        this.motionY -= 0.03;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9990000128746033;
        this.motionY *= 0.9990000128746033;
        this.motionZ *= 0.9990000128746033;
        if (this.onGround) {
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002595";
        
        @Override
        public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
            return new EntityLavaFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_);
        }
    }
}
