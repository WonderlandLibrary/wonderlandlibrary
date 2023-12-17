// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.World;

public class ParticleCloud extends Particle
{
    float oSize;
    
    protected ParticleCloud(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double p_i1221_8_, final double p_i1221_10_, final double p_i1221_12_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
        final float f = 2.5f;
        this.motionX *= 0.10000000149011612;
        this.motionY *= 0.10000000149011612;
        this.motionZ *= 0.10000000149011612;
        this.motionX += p_i1221_8_;
        this.motionY += p_i1221_10_;
        this.motionZ += p_i1221_12_;
        final float f2 = 1.0f - (float)(Math.random() * 0.30000001192092896);
        this.particleRed = f2;
        this.particleGreen = f2;
        this.particleBlue = f2;
        this.particleScale *= 0.75f;
        this.particleScale *= 2.5f;
        this.oSize = this.particleScale;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.3));
        this.particleMaxAge *= (int)2.5f;
    }
    
    @Override
    public void renderParticle(final BufferBuilder buffer, final Entity entityIn, final float partialTicks, final float rotationX, final float rotationZ, final float rotationYZ, final float rotationXY, final float rotationXZ) {
        float f = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0f;
        f = MathHelper.clamp(f, 0.0f, 1.0f);
        this.particleScale = this.oSize * f;
        super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setExpired();
        }
        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9599999785423279;
        this.motionY *= 0.9599999785423279;
        this.motionZ *= 0.9599999785423279;
        final EntityPlayer entityplayer = this.world.getClosestPlayer(this.posX, this.posY, this.posZ, 2.0, false);
        if (entityplayer != null) {
            final AxisAlignedBB axisalignedbb = entityplayer.getEntityBoundingBox();
            if (this.posY > axisalignedbb.minY) {
                this.posY += (axisalignedbb.minY - this.posY) * 0.2;
                this.motionY += (entityplayer.motionY - this.motionY) * 0.2;
                this.setPosition(this.posX, this.posY, this.posZ);
            }
        }
        if (this.onGround) {
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
        }
    }
    
    public static class Factory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new ParticleCloud(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
        }
    }
}
