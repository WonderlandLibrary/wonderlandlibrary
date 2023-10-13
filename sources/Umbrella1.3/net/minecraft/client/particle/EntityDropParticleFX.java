/*
 * Decompiled with CFR 0.150.
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
    private Material materialType;
    private int bobTimer;
    private static final String __OBFID = "CL_00000901";

    protected EntityDropParticleFX(World worldIn, double p_i1203_2_, double p_i1203_4_, double p_i1203_6_, Material p_i1203_8_) {
        super(worldIn, p_i1203_2_, p_i1203_4_, p_i1203_6_, 0.0, 0.0, 0.0);
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

    @Override
    public int getBrightnessForRender(float p_70070_1_) {
        return this.materialType == Material.water ? super.getBrightnessForRender(p_70070_1_) : 257;
    }

    @Override
    public float getBrightness(float p_70013_1_) {
        return this.materialType == Material.water ? super.getBrightness(p_70013_1_) : 1.0f;
    }

    @Override
    public void onUpdate() {
        BlockPos var1;
        IBlockState var2;
        Material var3;
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
        if ((var3 = (var2 = this.worldObj.getBlockState(var1 = new BlockPos(this))).getBlock().getMaterial()).isLiquid() || var3.isSolid()) {
            double var6;
            double var4 = 0.0;
            if (var2.getBlock() instanceof BlockLiquid) {
                var4 = BlockLiquid.getLiquidHeightPercent((Integer)var2.getValue(BlockLiquid.LEVEL));
            }
            if (this.posY < (var6 = (double)(MathHelper.floor_double(this.posY) + 1) - var4)) {
                this.setDead();
            }
        }
    }

    public static class LavaFactory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002607";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            return new EntityDropParticleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, Material.lava);
        }
    }

    public static class WaterFactory
    implements IParticleFactory {
        private static final String __OBFID = "CL_00002606";

        @Override
        public EntityFX func_178902_a(int p_178902_1_, World worldIn, double p_178902_3_, double p_178902_5_, double p_178902_7_, double p_178902_9_, double p_178902_11_, double p_178902_13_, int ... p_178902_15_) {
            return new EntityDropParticleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, Material.water);
        }
    }
}

