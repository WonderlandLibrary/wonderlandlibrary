// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.particle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public class ParticleDrip extends Particle
{
    private final Material materialType;
    private int bobTimer;
    
    protected ParticleDrip(final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final Material p_i1203_8_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        if (p_i1203_8_ == Material.WATER) {
            this.particleRed = 0.0f;
            this.particleGreen = 0.0f;
            this.particleBlue = 1.0f;
        }
        else {
            this.particleRed = 1.0f;
            this.particleGreen = 0.0f;
            this.particleBlue = 0.0f;
        }
        this.setParticleTextureIndex(113);
        this.setSize(0.01f, 0.01f);
        this.particleGravity = 0.06f;
        this.materialType = p_i1203_8_;
        this.bobTimer = 40;
        this.particleMaxAge = (int)(64.0 / (Math.random() * 0.8 + 0.2));
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
    }
    
    @Override
    public int getBrightnessForRender(final float partialTick) {
        return (this.materialType == Material.WATER) ? super.getBrightnessForRender(partialTick) : 257;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.materialType == Material.WATER) {
            this.particleRed = 0.2f;
            this.particleGreen = 0.3f;
            this.particleBlue = 1.0f;
        }
        else {
            this.particleRed = 1.0f;
            this.particleGreen = 16.0f / (40 - this.bobTimer + 16);
            this.particleBlue = 4.0f / (40 - this.bobTimer + 8);
        }
        this.motionY -= this.particleGravity;
        if (this.bobTimer-- > 0) {
            this.motionX *= 0.02;
            this.motionY *= 0.02;
            this.motionZ *= 0.02;
            this.setParticleTextureIndex(113);
        }
        else {
            this.setParticleTextureIndex(112);
        }
        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863;
        this.motionY *= 0.9800000190734863;
        this.motionZ *= 0.9800000190734863;
        if (this.particleMaxAge-- <= 0) {
            this.setExpired();
        }
        if (this.onGround) {
            if (this.materialType == Material.WATER) {
                this.setExpired();
                this.world.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0, new int[0]);
            }
            else {
                this.setParticleTextureIndex(114);
            }
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
        }
        final BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
        final IBlockState iblockstate = this.world.getBlockState(blockpos);
        final Material material = iblockstate.getMaterial();
        if (material.isLiquid() || material.isSolid()) {
            double d0 = 0.0;
            if (iblockstate.getBlock() instanceof BlockLiquid) {
                d0 = BlockLiquid.getLiquidHeightPercent(iblockstate.getValue((IProperty<Integer>)BlockLiquid.LEVEL));
            }
            final double d2 = MathHelper.floor(this.posY) + 1 - d0;
            if (this.posY < d2) {
                this.setExpired();
            }
        }
    }
    
    public static class LavaFactory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new ParticleDrip(worldIn, xCoordIn, yCoordIn, zCoordIn, Material.LAVA);
        }
    }
    
    public static class WaterFactory implements IParticleFactory
    {
        @Override
        public Particle createParticle(final int particleID, final World worldIn, final double xCoordIn, final double yCoordIn, final double zCoordIn, final double xSpeedIn, final double ySpeedIn, final double zSpeedIn, final int... p_178902_15_) {
            return new ParticleDrip(worldIn, xCoordIn, yCoordIn, zCoordIn, Material.WATER);
        }
    }
}
