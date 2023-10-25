/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.pathfinder.NodeProcessor;

public class PathFinder {
    private Path path = new Path();
    private NodeProcessor nodeProcessor;
    private PathPoint[] pathOptions = new PathPoint[32];

    public PathEntity createEntityPathTo(IBlockAccess iBlockAccess, Entity entity, BlockPos blockPos, float f) {
        return this.createEntityPathTo(iBlockAccess, entity, (float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 0.5f, (float)blockPos.getZ() + 0.5f, f);
    }

    public PathEntity createEntityPathTo(IBlockAccess iBlockAccess, Entity entity, Entity entity2, float f) {
        return this.createEntityPathTo(iBlockAccess, entity, entity2.posX, entity2.getEntityBoundingBox().minY, entity2.posZ, f);
    }

    private PathEntity createEntityPath(PathPoint pathPoint, PathPoint pathPointArray) {
        int n = 1;
        PathPoint[] pathPointArray2 = pathPointArray;
        while (pathPointArray2.previous != null) {
            ++n;
            pathPointArray2 = pathPointArray2.previous;
        }
        pathPointArray2 = new PathPoint[n];
        Object object = pathPointArray;
        pathPointArray2[--n] = pathPointArray;
        while (object.previous != null) {
            object = object.previous;
            pathPointArray2[--n] = object;
        }
        return new PathEntity(pathPointArray2);
    }

    private PathEntity createEntityPathTo(IBlockAccess iBlockAccess, Entity entity, double d, double d2, double d3, float f) {
        this.path.clearPath();
        this.nodeProcessor.initProcessor(iBlockAccess, entity);
        PathPoint pathPoint = this.nodeProcessor.getPathPointTo(entity);
        PathPoint pathPoint2 = this.nodeProcessor.getPathPointToCoords(entity, d, d2, d3);
        PathEntity pathEntity = this.addToPath(entity, pathPoint, pathPoint2, f);
        this.nodeProcessor.postProcess();
        return pathEntity;
    }

    public PathFinder(NodeProcessor nodeProcessor) {
        this.nodeProcessor = nodeProcessor;
    }

    private PathEntity addToPath(Entity entity, PathPoint pathPoint, PathPoint pathPoint2, float f) {
        pathPoint.totalPathDistance = 0.0f;
        pathPoint.distanceToTarget = pathPoint.distanceToNext = pathPoint.distanceToSquared(pathPoint2);
        this.path.clearPath();
        this.path.addPoint(pathPoint);
        PathPoint pathPoint3 = pathPoint;
        while (!this.path.isPathEmpty()) {
            PathPoint pathPoint4 = this.path.dequeue();
            if (pathPoint4.equals(pathPoint2)) {
                return this.createEntityPath(pathPoint, pathPoint2);
            }
            if (pathPoint4.distanceToSquared(pathPoint2) < pathPoint3.distanceToSquared(pathPoint2)) {
                pathPoint3 = pathPoint4;
            }
            pathPoint4.visited = true;
            int n = this.nodeProcessor.findPathOptions(this.pathOptions, entity, pathPoint4, pathPoint2, f);
            int n2 = 0;
            while (n2 < n) {
                PathPoint pathPoint5 = this.pathOptions[n2];
                float f2 = pathPoint4.totalPathDistance + pathPoint4.distanceToSquared(pathPoint5);
                if (f2 < f * 2.0f && (!pathPoint5.isAssigned() || f2 < pathPoint5.totalPathDistance)) {
                    pathPoint5.previous = pathPoint4;
                    pathPoint5.totalPathDistance = f2;
                    pathPoint5.distanceToNext = pathPoint5.distanceToSquared(pathPoint2);
                    if (pathPoint5.isAssigned()) {
                        this.path.changeDistance(pathPoint5, pathPoint5.totalPathDistance + pathPoint5.distanceToNext);
                    } else {
                        pathPoint5.distanceToTarget = pathPoint5.totalPathDistance + pathPoint5.distanceToNext;
                        this.path.addPoint(pathPoint5);
                    }
                }
                ++n2;
            }
        }
        if (pathPoint3 == pathPoint) {
            return null;
        }
        return this.createEntityPath(pathPoint, pathPoint3);
    }
}

