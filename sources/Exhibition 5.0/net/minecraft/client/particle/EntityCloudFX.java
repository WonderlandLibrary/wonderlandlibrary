// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;

public class EntityCloudFX extends EntityFX
{
    float field_70569_a;
    private static final String __OBFID = "CL_00000920";
    
    protected EntityCloudFX(final World worldIn, final double p_i1221_2_, final double p_i1221_4_, final double p_i1221_6_, final double p_i1221_8_, final double p_i1221_10_, final double p_i1221_12_) {
        super(worldIn, p_i1221_2_, p_i1221_4_, p_i1221_6_, 0.0, 0.0, 0.0);
        final float var14 = 2.5f;
        this.motionX *= 0.10000000149011612;
        this.motionY *= 0.10000000149011612;
        this.motionZ *= 0.10000000149011612;
        this.motionX += p_i1221_8_;
        this.motionY += p_i1221_10_;
        this.motionZ += p_i1221_12_;
        final float particleRed = 1.0f - (float)(Math.random() * 0.30000001192092896);
        this.particleBlue = particleRed;
        this.particleGreen = particleRed;
        this.particleRed = particleRed;
        this.particleScale *= 0.75f;
        this.particleScale *= var14;
        this.field_70569_a = this.particleScale;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.3));
        this.particleMaxAge *= (int)var14;
        this.noClip = false;
    }
    
    @Override
    public void func_180434_a(final WorldRenderer p_180434_1_, final Entity p_180434_2_, final float p_180434_3_, final float p_180434_4_, final float p_180434_5_, final float p_180434_6_, final float p_180434_7_, final float p_180434_8_) {
        float var9 = (this.particleAge + p_180434_3_) / this.particleMaxAge * 32.0f;
        var9 = MathHelper.clamp_float(var9, 0.0f, 1.0f);
        this.particleScale = this.field_70569_a * var9;
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
        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9599999785423279;
        this.motionY *= 0.9599999785423279;
        this.motionZ *= 0.9599999785423279;
        final EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 2.0);
        if (var1 != null && this.posY > var1.getEntityBoundingBox().minY) {
            this.posY += (var1.getEntityBoundingBox().minY - this.posY) * 0.2;
            this.motionY += (var1.motionY - this.motionY) * 0.2;
            this.setPosition(this.posX, this.posY, this.posZ);
        }
        if (this.onGround) {
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002591";
        
        @Override
        public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
            return new EntityCloudFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, p_178902_9_, p_178902_11_, p_178902_13_);
        }
    }
}
