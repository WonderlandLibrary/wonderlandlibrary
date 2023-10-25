/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class PathNavigateClimber
extends PathNavigateGround {
    private BlockPos targetPosition;

    public PathNavigateClimber(EntityLiving entityLiving, World world) {
        super(entityLiving, world);
    }

    @Override
    public boolean tryMoveToEntityLiving(Entity entity, double d) {
        PathEntity pathEntity = this.getPathToEntityLiving(entity);
        if (pathEntity != null) {
            return this.setPath(pathEntity, d);
        }
        this.targetPosition = new BlockPos(entity);
        this.speed = d;
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
                double d = this.theEntity.width * this.theEntity.width;
                if (!(this.theEntity.getDistanceSqToCenter(this.targetPosition) >= d)) break block4;
                if (this.theEntity.posY <= (double)this.targetPosition.getY()) break block5;
                BlockPos blockPos = new BlockPos(this.targetPosition.getX(), MathHelper.floor_double(this.theEntity.posY), this.targetPosition.getZ());
                if (!(this.theEntity.getDistanceSqToCenter(blockPos) >= d)) break block4;
            }
            this.theEntity.getMoveHelper().setMoveTo(this.targetPosition.getX(), this.targetPosition.getY(), this.targetPosition.getZ(), this.speed);
            return;
        }
        this.targetPosition = null;
    }

    @Override
    public PathEntity getPathToPos(BlockPos blockPos) {
        this.targetPosition = blockPos;
        return super.getPathToPos(blockPos);
    }

    @Override
    public PathEntity getPathToEntityLiving(Entity entity) {
        this.targetPosition = new BlockPos(entity);
        return super.getPathToEntityLiving(entity);
    }
}

