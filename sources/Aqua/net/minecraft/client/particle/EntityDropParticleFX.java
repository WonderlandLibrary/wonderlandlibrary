package net.minecraft.client.particle;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityDropParticleFX
extends EntityFX {
    private Material materialType;
    private int bobTimer;

    protected EntityDropParticleFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, Material p_i1203_8_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
        this.motionZ = 0.0;
        this.motionY = 0.0;
        this.motionX = 0.0;
        if (p_i1203_8_ == Material.water) {
            this.particleRed = 0.0f;
            this.particleGreen = 0.0f;
            this.particleBlue = 1.0f;
        } else {
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
        this.motionZ = 0.0;
        this.motionY = 0.0;
        this.motionX = 0.0;
    }

    public int getBrightnessForRender(float partialTicks) {
        return this.materialType == Material.water ? super.getBrightnessForRender(partialTicks) : 257;
    }

    public float getBrightness(float partialTicks) {
        return this.materialType == Material.water ? super.getBrightness(partialTicks) : 1.0f;
    }

    public void onUpdate() {
        BlockPos blockpos;
        IBlockState iblockstate;
        Material material;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.materialType == Material.water) {
            this.particleRed = 0.2f;
            this.particleGreen = 0.3f;
            this.particleBlue = 1.0f;
        } else {
            this.particleRed = 1.0f;
            this.particleGreen = 16.0f / (float)(40 - this.bobTimer + 16);
            this.particleBlue = 4.0f / (float)(40 - this.bobTimer + 8);
        }
        this.motionY -= (double)this.particleGravity;
        if (this.bobTimer-- > 0) {
            this.motionX *= 0.02;
            this.motionY *= 0.02;
            this.motionZ *= 0.02;
            this.setParticleTextureIndex(113);
        } else {
            this.setParticleTextureIndex(112);
        }
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= (double)0.98f;
        this.motionY *= (double)0.98f;
        this.motionZ *= (double)0.98f;
        if (this.particleMaxAge-- <= 0) {
            this.setDead();
        }
        if (this.onGround) {
            if (this.materialType == Material.water) {
                this.setDead();
                this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0, new int[0]);
            } else {
                this.setParticleTextureIndex(114);
            }
            this.motionX *= (double)0.7f;
            this.motionZ *= (double)0.7f;
        }
        if ((material = (iblockstate = this.worldObj.getBlockState(blockpos = new BlockPos((Entity)this))).getBlock().getMaterial()).isLiquid() || material.isSolid()) {
            double d1;
            double d0 = 0.0;
            if (iblockstate.getBlock() instanceof BlockLiquid) {
                d0 = BlockLiquid.getLiquidHeightPercent((int)((Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL)));
            }
            if (this.posY < (d1 = (double)(MathHelper.floor_double((double)this.posY) + 1) - d0)) {
                this.setDead();
            }
        }
    }
}
