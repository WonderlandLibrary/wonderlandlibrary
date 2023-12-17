// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.material.Material;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.Path;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.block.BlockDoor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.EntityLiving;

public abstract class EntityAIDoorInteract extends EntityAIBase
{
    protected EntityLiving entity;
    protected BlockPos doorPosition;
    protected BlockDoor doorBlock;
    boolean hasStoppedDoorInteraction;
    float entityPositionX;
    float entityPositionZ;
    
    public EntityAIDoorInteract(final EntityLiving entityIn) {
        this.doorPosition = BlockPos.ORIGIN;
        this.entity = entityIn;
        if (!(entityIn.getNavigator() instanceof PathNavigateGround)) {
            throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
        }
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.entity.collidedHorizontally) {
            return false;
        }
        final PathNavigateGround pathnavigateground = (PathNavigateGround)this.entity.getNavigator();
        final Path path = pathnavigateground.getPath();
        if (path != null && !path.isFinished() && pathnavigateground.getEnterDoors()) {
            for (int i = 0; i < Math.min(path.getCurrentPathIndex() + 2, path.getCurrentPathLength()); ++i) {
                final PathPoint pathpoint = path.getPathPointFromIndex(i);
                this.doorPosition = new BlockPos(pathpoint.x, pathpoint.y + 1, pathpoint.z);
                if (this.entity.getDistanceSq(this.doorPosition.getX(), this.entity.posY, this.doorPosition.getZ()) <= 2.25) {
                    this.doorBlock = this.getBlockDoor(this.doorPosition);
                    if (this.doorBlock != null) {
                        return true;
                    }
                }
            }
            this.doorPosition = new BlockPos(this.entity).up();
            this.doorBlock = this.getBlockDoor(this.doorPosition);
            return this.doorBlock != null;
        }
        return false;
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return !this.hasStoppedDoorInteraction;
    }
    
    @Override
    public void startExecuting() {
        this.hasStoppedDoorInteraction = false;
        this.entityPositionX = (float)(this.doorPosition.getX() + 0.5f - this.entity.posX);
        this.entityPositionZ = (float)(this.doorPosition.getZ() + 0.5f - this.entity.posZ);
    }
    
    @Override
    public void updateTask() {
        final float f = (float)(this.doorPosition.getX() + 0.5f - this.entity.posX);
        final float f2 = (float)(this.doorPosition.getZ() + 0.5f - this.entity.posZ);
        final float f3 = this.entityPositionX * f + this.entityPositionZ * f2;
        if (f3 < 0.0f) {
            this.hasStoppedDoorInteraction = true;
        }
    }
    
    private BlockDoor getBlockDoor(final BlockPos pos) {
        final IBlockState iblockstate = this.entity.world.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        return (block instanceof BlockDoor && iblockstate.getMaterial() == Material.WOOD) ? ((BlockDoor)block) : null;
    }
}
