// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.particle;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.MathHelper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public class EntityDropParticleFX extends EntityFX
{
    private Material materialType;
    private int bobTimer;
    private static final String __OBFID = "CL_00000901";
    
    protected EntityDropParticleFX(final World worldIn, final double p_i1203_2_, final double p_i1203_4_, final double p_i1203_6_, final Material p_i1203_8_) {
        super(worldIn, p_i1203_2_, p_i1203_4_, p_i1203_6_, 0.0, 0.0, 0.0);
        final double motionX = 0.0;
        this.motionZ = motionX;
        this.motionY = motionX;
        this.motionX = motionX;
        if (p_i1203_8_ == Material.water) {
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
        final double motionX2 = 0.0;
        this.motionZ = motionX2;
        this.motionY = motionX2;
        this.motionX = motionX2;
    }
    
    @Override
    public int getBrightnessForRender(final float p_70070_1_) {
        return (this.materialType == Material.water) ? super.getBrightnessForRender(p_70070_1_) : 257;
    }
    
    @Override
    public float getBrightness(final float p_70013_1_) {
        return (this.materialType == Material.water) ? super.getBrightness(p_70013_1_) : 1.0f;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        if (this.materialType == Material.water) {
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
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863;
        this.motionY *= 0.9800000190734863;
        this.motionZ *= 0.9800000190734863;
        if (this.particleMaxAge-- <= 0) {
            this.setDead();
        }
        if (this.onGround) {
            if (this.materialType == Material.water) {
                this.setDead();
                this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0, new int[0]);
            }
            else {
                this.setParticleTextureIndex(114);
            }
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
        }
        final BlockPos var1 = new BlockPos(this);
        final IBlockState var2 = this.worldObj.getBlockState(var1);
        final Material var3 = var2.getBlock().getMaterial();
        if (var3.isLiquid() || var3.isSolid()) {
            double var4 = 0.0;
            if (var2.getBlock() instanceof BlockLiquid) {
                var4 = BlockLiquid.getLiquidHeightPercent((int)var2.getValue(BlockLiquid.LEVEL));
            }
            final double var5 = MathHelper.floor_double(this.posY) + 1 - var4;
            if (this.posY < var5) {
                this.setDead();
            }
        }
    }
    
    public static class LavaFactory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002607";
        
        @Override
        public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
            return new EntityDropParticleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, Material.lava);
        }
    }
    
    public static class WaterFactory implements IParticleFactory
    {
        private static final String __OBFID = "CL_00002606";
        
        @Override
        public EntityFX func_178902_a(final int p_178902_1_, final World worldIn, final double p_178902_3_, final double p_178902_5_, final double p_178902_7_, final double p_178902_9_, final double p_178902_11_, final double p_178902_13_, final int... p_178902_15_) {
            return new EntityDropParticleFX(worldIn, p_178902_3_, p_178902_5_, p_178902_7_, Material.water);
        }
    }
}
