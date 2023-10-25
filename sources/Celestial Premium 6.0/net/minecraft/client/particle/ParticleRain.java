/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.particle;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ParticleRain
extends Particle {
    protected ParticleRain(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
        this.motionX *= (double)0.3f;
        this.motionY = Math.random() * (double)0.2f + (double)0.1f;
        this.motionZ *= (double)0.3f;
        this.particleRed = 1.0f;
        this.particleGreen = 1.0f;
        this.particleBlue = 1.0f;
        this.setParticleTextureIndex(19 + this.rand.nextInt(4));
        this.setSize(0.01f, 0.01f);
        this.particleGravity = 0.06f;
        this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
    }

    @Override
    public void onUpdate() {
        BlockPos blockpos;
        IBlockState iblockstate;
        Material material;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= (double)this.particleGravity;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= (double)0.98f;
        this.motionY *= (double)0.98f;
        this.motionZ *= (double)0.98f;
        if (this.particleMaxAge-- <= 0) {
            this.setExpired();
        }
        if (this.isCollided) {
            if (Math.random() < 0.5) {
                this.setExpired();
            }
            this.motionX *= (double)0.7f;
            this.motionZ *= (double)0.7f;
        }
        if ((material = (iblockstate = this.worldObj.getBlockState(blockpos = new BlockPos(this.posX, this.posY, this.posZ))).getMaterial()).isLiquid() || material.isSolid()) {
            double d0 = iblockstate.getBlock() instanceof BlockLiquid ? (double)(1.0f - BlockLiquid.getLiquidHeightPercent(iblockstate.getValue(BlockLiquid.LEVEL))) : iblockstate.getBoundingBox((IBlockAccess)this.worldObj, (BlockPos)blockpos).maxY;
            double d1 = (double)MathHelper.floor(this.posY) + d0;
            if (this.posY < d1) {
                this.setExpired();
            }
        }
    }

    public static class Factory
    implements IParticleFactory {
        @Override
        public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int ... p_178902_15_) {
            return new ParticleRain(worldIn, xCoordIn, yCoordIn, zCoordIn);
        }
    }
}

