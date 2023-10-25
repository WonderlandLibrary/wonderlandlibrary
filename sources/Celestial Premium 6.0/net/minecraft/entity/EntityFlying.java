/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class EntityFlying
extends EntityLiving {
    public EntityFlying(World worldIn) {
        super(worldIn);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos) {
    }

    @Override
    public void travel(float p_191986_1_, float p_191986_2_, float p_191986_3_) {
        if (this.isInWater()) {
            this.moveFlying(p_191986_1_, p_191986_2_, p_191986_3_, 0.02f);
            this.moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double)0.8f;
            this.motionY *= (double)0.8f;
            this.motionZ *= (double)0.8f;
        } else if (this.isInLava()) {
            this.moveFlying(p_191986_1_, p_191986_2_, p_191986_3_, 0.02f);
            this.moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5;
            this.motionY *= 0.5;
            this.motionZ *= 0.5;
        } else {
            float f = 0.91f;
            if (this.onGround) {
                f = this.world.getBlockState((BlockPos)new BlockPos((int)MathHelper.floor((double)this.posX), (int)(MathHelper.floor((double)this.getEntityBoundingBox().minY) - 1), (int)MathHelper.floor((double)this.posZ))).getBlock().slipperiness * 0.91f;
            }
            float f1 = 0.16277136f / (f * f * f);
            this.moveFlying(p_191986_1_, p_191986_2_, p_191986_3_, this.onGround ? 0.1f * f1 : 0.02f);
            f = 0.91f;
            if (this.onGround) {
                f = this.world.getBlockState((BlockPos)new BlockPos((int)MathHelper.floor((double)this.posX), (int)(MathHelper.floor((double)this.getEntityBoundingBox().minY) - 1), (int)MathHelper.floor((double)this.posZ))).getBlock().slipperiness * 0.91f;
            }
            this.moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double)f;
            this.motionY *= (double)f;
            this.motionZ *= (double)f;
        }
        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d1 = this.posX - this.prevPosX;
        double d0 = this.posZ - this.prevPosZ;
        float f2 = MathHelper.sqrt(d1 * d1 + d0 * d0) * 4.0f;
        if (f2 > 1.0f) {
            f2 = 1.0f;
        }
        this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4f;
        this.limbSwing += this.limbSwingAmount;
    }

    @Override
    public boolean isOnLadder() {
        return false;
    }
}

