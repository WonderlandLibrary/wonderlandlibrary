// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIFollowOwner extends EntityAIBase
{
    private final EntityTameable tameable;
    private EntityLivingBase owner;
    World world;
    private final double followSpeed;
    private final PathNavigate petPathfinder;
    private int timeToRecalcPath;
    float maxDist;
    float minDist;
    private float oldWaterCost;
    
    public EntityAIFollowOwner(final EntityTameable tameableIn, final double followSpeedIn, final float minDistIn, final float maxDistIn) {
        this.tameable = tameableIn;
        this.world = tameableIn.world;
        this.followSpeed = followSpeedIn;
        this.petPathfinder = tameableIn.getNavigator();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setMutexBits(3);
        if (!(tameableIn.getNavigator() instanceof PathNavigateGround) && !(tameableIn.getNavigator() instanceof PathNavigateFlying)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }
    
    @Override
    public boolean shouldExecute() {
        final EntityLivingBase entitylivingbase = this.tameable.getOwner();
        if (entitylivingbase == null) {
            return false;
        }
        if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).isSpectator()) {
            return false;
        }
        if (this.tameable.isSitting()) {
            return false;
        }
        if (this.tameable.getDistanceSq(entitylivingbase) < this.minDist * this.minDist) {
            return false;
        }
        this.owner = entitylivingbase;
        return true;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return !this.petPathfinder.noPath() && this.tameable.getDistanceSq(this.owner) > this.maxDist * this.maxDist && !this.tameable.isSitting();
    }
    
    @Override
    public void startExecuting() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.tameable.getPathPriority(PathNodeType.WATER);
        this.tameable.setPathPriority(PathNodeType.WATER, 0.0f);
    }
    
    @Override
    public void resetTask() {
        this.owner = null;
        this.petPathfinder.clearPath();
        this.tameable.setPathPriority(PathNodeType.WATER, this.oldWaterCost);
    }
    
    @Override
    public void updateTask() {
        this.tameable.getLookHelper().setLookPositionWithEntity(this.owner, 10.0f, (float)this.tameable.getVerticalFaceSpeed());
        if (!this.tameable.isSitting() && --this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = 10;
            if (!this.petPathfinder.tryMoveToEntityLiving(this.owner, this.followSpeed) && !this.tameable.getLeashed() && !this.tameable.isRiding() && this.tameable.getDistanceSq(this.owner) >= 144.0) {
                final int i = MathHelper.floor(this.owner.posX) - 2;
                final int j = MathHelper.floor(this.owner.posZ) - 2;
                final int k = MathHelper.floor(this.owner.getEntityBoundingBox().minY);
                for (int l = 0; l <= 4; ++l) {
                    for (int i2 = 0; i2 <= 4; ++i2) {
                        if ((l < 1 || i2 < 1 || l > 3 || i2 > 3) && this.isTeleportFriendlyBlock(i, j, k, l, i2)) {
                            this.tameable.setLocationAndAngles(i + l + 0.5f, k, j + i2 + 0.5f, this.tameable.rotationYaw, this.tameable.rotationPitch);
                            this.petPathfinder.clearPath();
                            return;
                        }
                    }
                }
            }
        }
    }
    
    protected boolean isTeleportFriendlyBlock(final int x, final int z, final int y, final int xOffset, final int zOffset) {
        final BlockPos blockpos = new BlockPos(x + xOffset, y - 1, z + zOffset);
        final IBlockState iblockstate = this.world.getBlockState(blockpos);
        return iblockstate.getBlockFaceShape(this.world, blockpos, EnumFacing.DOWN) == BlockFaceShape.SOLID && iblockstate.canEntitySpawn(this.tameable) && this.world.isAirBlock(blockpos.up()) && this.world.isAirBlock(blockpos.up(2));
    }
}
