/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.particle;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityDropParticleFX
extends EntityFX {
    private int bobTimer;
    private Material materialType;

    protected EntityDropParticleFX(World world, double d, double d2, double d3, Material material) {
        super(world, d, d2, d3, 0.0, 0.0, 0.0);
        this.motionZ = 0.0;
        this.motionY = 0.0;
        this.motionX = 0.0;
        if (material == Material.water) {
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
        this.materialType = material;
        this.bobTimer = 40;
        this.particleMaxAge = (int)(64.0 / (Math.random() * 0.8 + 0.2));
        this.motionZ = 0.0;
        this.motionY = 0.0;
        this.motionX = 0.0;
    }

    @Override
    public float getBrightness(float f) {
        return this.materialType == Material.water ? super.getBrightness(f) : 1.0f;
    }

    @Override
    public void onUpdate() {
        BlockPos blockPos;
        IBlockState iBlockState;
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
        if ((material = (iBlockState = this.worldObj.getBlockState(blockPos = new BlockPos(this))).getBlock().getMaterial()).isLiquid() || material.isSolid()) {
            double d;
            double d2 = 0.0;
            if (iBlockState.getBlock() instanceof BlockLiquid) {
                d2 = BlockLiquid.getLiquidHeightPercent(iBlockState.getValue(BlockLiquid.LEVEL));
            }
            if (this.posY < (d = (double)(MathHelper.floor_double(this.posY) + 1) - d2)) {
                this.setDead();
            }
        }
    }

    @Override
    public int getBrightnessForRender(float f) {
        return this.materialType == Material.water ? super.getBrightnessForRender(f) : 257;
    }

    public static class LavaFactory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            return new EntityDropParticleFX(world, d, d2, d3, Material.lava);
        }
    }

    public static class WaterFactory
    implements IParticleFactory {
        @Override
        public EntityFX getEntityFX(int n, World world, double d, double d2, double d3, double d4, double d5, double d6, int ... nArray) {
            return new EntityDropParticleFX(world, d, d2, d3, Material.water);
        }
    }
}

