/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.Vec3;

public class PathEntity {
    private int pathLength;
    private int currentPathIndex;
    private final PathPoint[] points;

    public PathPoint getPathPointFromIndex(int n) {
        return this.points[n];
    }

    public Vec3 getPosition(Entity entity) {
        return this.getVectorFromIndex(entity, this.currentPathIndex);
    }

    public PathPoint getFinalPathPoint() {
        return this.pathLength > 0 ? this.points[this.pathLength - 1] : null;
    }

    public int getCurrentPathIndex() {
        return this.currentPathIndex;
    }

    public int getCurrentPathLength() {
        return this.pathLength;
    }

    public PathEntity(PathPoint[] pathPointArray) {
        this.points = pathPointArray;
        this.pathLength = pathPointArray.length;
    }

    public Vec3 getVectorFromIndex(Entity entity, int n) {
        double d = (double)this.points[n].xCoord + (double)((int)(entity.width + 1.0f)) * 0.5;
        double d2 = this.points[n].yCoord;
        double d3 = (double)this.points[n].zCoord + (double)((int)(entity.width + 1.0f)) * 0.5;
        return new Vec3(d, d2, d3);
    }

    public boolean isDestinationSame(Vec3 vec3) {
        PathPoint pathPoint = this.getFinalPathPoint();
        return pathPoint == null ? false : pathPoint.xCoord == (int)vec3.xCoord && pathPoint.zCoord == (int)vec3.zCoord;
    }

    public boolean isFinished() {
        return this.currentPathIndex >= this.pathLength;
    }

    public void setCurrentPathLength(int n) {
        this.pathLength = n;
    }

    public void incrementPathIndex() {
        ++this.currentPathIndex;
    }

    public void setCurrentPathIndex(int n) {
        this.currentPathIndex = n;
    }

    public boolean isSamePath(PathEntity pathEntity) {
        if (pathEntity == null) {
            return false;
        }
        if (pathEntity.points.length != this.points.length) {
            return false;
        }
        int n = 0;
        while (n < this.points.length) {
            if (this.points[n].xCoord != pathEntity.points[n].xCoord || this.points[n].yCoord != pathEntity.points[n].yCoord || this.points[n].zCoord != pathEntity.points[n].zCoord) {
                return false;
            }
            ++n;
        }
        return true;
    }
}

