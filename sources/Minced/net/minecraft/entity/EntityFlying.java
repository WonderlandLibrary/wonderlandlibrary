// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

public abstract class EntityFlying extends EntityLiving
{
    public EntityFlying(final World worldIn) {
        super(worldIn);
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
    }
    
    @Override
    protected void updateFallState(final double y, final boolean onGroundIn, final IBlockState state, final BlockPos pos) {
    }
    
    @Override
    public void travel(final float strafe, final float vertical, final float forward) {
        if (this.isInWater()) {
            this.moveRelative(strafe, vertical, forward, 0.02f);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929;
            this.motionY *= 0.800000011920929;
            this.motionZ *= 0.800000011920929;
        }
        else if (this.isInLava()) {
            this.moveRelative(strafe, vertical, forward, 0.02f);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5;
            this.motionY *= 0.5;
            this.motionZ *= 0.5;
        }
        else {
            float f = 0.91f;
            if (this.onGround) {
                f = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.91f;
            }
            final float f2 = 0.16277136f / (f * f * f);
            this.moveRelative(strafe, vertical, forward, this.onGround ? (0.1f * f2) : 0.02f);
            f = 0.91f;
            if (this.onGround) {
                f = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.91f;
            }
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= f;
            this.motionY *= f;
            this.motionZ *= f;
        }
        this.prevLimbSwingAmount = this.limbSwingAmount;
        final double d1 = this.posX - this.prevPosX;
        final double d2 = this.posZ - this.prevPosZ;
        float f3 = MathHelper.sqrt(d1 * d1 + d2 * d2) * 4.0f;
        if (f3 > 1.0f) {
            f3 = 1.0f;
        }
        this.limbSwingAmount += (f3 - this.limbSwingAmount) * 0.4f;
        this.limbSwing += this.limbSwingAmount;
    }
    
    @Override
    public boolean isOnLadder() {
        return false;
    }
}
