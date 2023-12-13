/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityFlying
extends EntityLiving {
    private static final String __OBFID = "CL_00001545";

    public EntityFlying(World worldIn) {
        super(worldIn);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void func_180433_a(double p_180433_1_, boolean p_180433_3_, Block p_180433_4_, BlockPos p_180433_5_) {
    }

    @Override
    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
        if (this.isInWater()) {
            this.moveFlying(p_70612_1_, p_70612_2_, 0.02f);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double)0.8f;
            this.motionY *= (double)0.8f;
            this.motionZ *= (double)0.8f;
        } else if (this.func_180799_ab()) {
            this.moveFlying(p_70612_1_, p_70612_2_, 0.02f);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5;
            this.motionY *= 0.5;
            this.motionZ *= 0.5;
        } else {
            float var3 = 0.91f;
            if (this.onGround) {
                var3 = this.worldObj.getBlockState((BlockPos)new BlockPos((int)MathHelper.floor_double((double)this.posX), (int)(MathHelper.floor_double((double)this.getEntityBoundingBox().minY) - 1), (int)MathHelper.floor_double((double)this.posZ))).getBlock().slipperiness * 0.91f;
            }
            float var4 = 0.16277136f / (var3 * var3 * var3);
            this.moveFlying(p_70612_1_, p_70612_2_, this.onGround ? 0.1f * var4 : 0.02f);
            var3 = 0.91f;
            if (this.onGround) {
                var3 = this.worldObj.getBlockState((BlockPos)new BlockPos((int)MathHelper.floor_double((double)this.posX), (int)(MathHelper.floor_double((double)this.getEntityBoundingBox().minY) - 1), (int)MathHelper.floor_double((double)this.posZ))).getBlock().slipperiness * 0.91f;
            }
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double)var3;
            this.motionY *= (double)var3;
            this.motionZ *= (double)var3;
        }
        this.prevLimbSwingAmount = this.limbSwingAmount;
        double var8 = this.posX - this.prevPosX;
        double var5 = this.posZ - this.prevPosZ;
        float var7 = MathHelper.sqrt_double(var8 * var8 + var5 * var5) * 4.0f;
        if (var7 > 1.0f) {
            var7 = 1.0f;
        }
        this.limbSwingAmount += (var7 - this.limbSwingAmount) * 0.4f;
        this.limbSwing += this.limbSwingAmount;
    }

    @Override
    public boolean isOnLadder() {
        return false;
    }
}

