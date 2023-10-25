/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class PathNavigateClimber
extends PathNavigateGround {
    private BlockPos targetPosition;

    public PathNavigateClimber(EntityLiving entityLivingIn, World worldIn) {
        super(entityLivingIn, worldIn);
    }

    @Override
    public Path getPathToPos(BlockPos pos) {
        this.targetPosition = pos;
        return super.getPathToPos(pos);
    }

    @Override
    public Path getPathToEntityLiving(Entity entityIn) {
        this.targetPosition = new BlockPos(entityIn);
        return super.getPathToEntityLiving(entityIn);
    }

    @Override
    public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn) {
        Path path = this.getPathToEntityLiving(entityIn);
        if (path != null) {
            return this.setPath(path, speedIn);
        }
        this.targetPosition = new BlockPos(entityIn);
        this.speed = speedIn;
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void onUpdateNavigation() {
        block4: {
            block5: {
                if (!this.noPath()) {
                    super.onUpdateNavigation();
                    return;
                }
                if (this.targetPosition == null) return;
                double d0 = this.theEntity.width * this.theEntity.width;
                if (!(this.theEntity.getDistanceSqToCenter(this.targetPosition) >= d0)) break block4;
                if (this.theEntity.posY <= (double)this.targetPosition.getY()) break block5;
                BlockPos blockPos = new BlockPos(this.targetPosition.getX(), MathHelper.floor(this.theEntity.posY), this.targetPosition.getZ());
                if (!(this.theEntity.getDistanceSqToCenter(blockPos) >= d0)) break block4;
            }
            this.theEntity.getMoveHelper().setMoveTo(this.targetPosition.getX(), this.targetPosition.getY(), this.targetPosition.getZ(), this.speed);
            return;
        }
        this.targetPosition = null;
    }
}

