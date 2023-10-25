/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.pathfinder.SwimNodeProcessor;

public class PathNavigateSwimmer
extends PathNavigate {
    @Override
    protected Vec3 getEntityPosition() {
        return new Vec3(this.theEntity.posX, this.theEntity.posY + (double)this.theEntity.height * 0.5, this.theEntity.posZ);
    }

    @Override
    protected boolean canNavigate() {
        return this.isInLiquid();
    }

    @Override
    protected void removeSunnyPath() {
        super.removeSunnyPath();
    }

    @Override
    protected boolean isDirectPathBetweenPoints(Vec3 vec3, Vec3 vec32, int n, int n2, int n3) {
        MovingObjectPosition movingObjectPosition = this.worldObj.rayTraceBlocks(vec3, new Vec3(vec32.xCoord, vec32.yCoord + (double)this.theEntity.height * 0.5, vec32.zCoord), false, true, false);
        return movingObjectPosition == null || movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.MISS;
    }

    public PathNavigateSwimmer(EntityLiving entityLiving, World world) {
        super(entityLiving, world);
    }

    @Override
    protected PathFinder getPathFinder() {
        return new PathFinder(new SwimNodeProcessor());
    }

    @Override
    protected void pathFollow() {
        Vec3 vec3 = this.getEntityPosition();
        float f = this.theEntity.width * this.theEntity.width;
        int n = 6;
        if (vec3.squareDistanceTo(this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex())) < (double)f) {
            this.currentPath.incrementPathIndex();
        }
        int n2 = Math.min(this.currentPath.getCurrentPathIndex() + n, this.currentPath.getCurrentPathLength() - 1);
        while (n2 > this.currentPath.getCurrentPathIndex()) {
            Vec3 vec32 = this.currentPath.getVectorFromIndex(this.theEntity, n2);
            if (vec32.squareDistanceTo(vec3) <= 36.0 && this.isDirectPathBetweenPoints(vec3, vec32, 0, 0, 0)) {
                this.currentPath.setCurrentPathIndex(n2);
                break;
            }
            --n2;
        }
        this.checkForStuck(vec3);
    }
}

