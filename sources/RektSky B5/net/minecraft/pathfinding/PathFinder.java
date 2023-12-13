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
    private PathPoint[] pathOptions = new PathPoint[32];
    private NodeProcessor nodeProcessor;

    public PathFinder(NodeProcessor nodeProcessorIn) {
        this.nodeProcessor = nodeProcessorIn;
    }

    public PathEntity createEntityPathTo(IBlockAccess blockaccess, Entity entityFrom, Entity entityTo, float dist) {
        return this.createEntityPathTo(blockaccess, entityFrom, entityTo.posX, entityTo.getEntityBoundingBox().minY, entityTo.posZ, dist);
    }

    public PathEntity createEntityPathTo(IBlockAccess blockaccess, Entity entityIn, BlockPos targetPos, float dist) {
        return this.createEntityPathTo(blockaccess, entityIn, (float)targetPos.getX() + 0.5f, (float)targetPos.getY() + 0.5f, (float)targetPos.getZ() + 0.5f, dist);
    }

    private PathEntity createEntityPathTo(IBlockAccess blockaccess, Entity entityIn, double x2, double y2, double z2, float distance) {
        this.path.clearPath();
        this.nodeProcessor.initProcessor(blockaccess, entityIn);
        PathPoint pathpoint = this.nodeProcessor.getPathPointTo(entityIn);
        PathPoint pathpoint1 = this.nodeProcessor.getPathPointToCoords(entityIn, x2, y2, z2);
        PathEntity pathentity = this.addToPath(entityIn, pathpoint, pathpoint1, distance);
        this.nodeProcessor.postProcess();
        return pathentity;
    }

    private PathEntity addToPath(Entity entityIn, PathPoint pathpointStart, PathPoint pathpointEnd, float maxDistance) {
        pathpointStart.totalPathDistance = 0.0f;
        pathpointStart.distanceToTarget = pathpointStart.distanceToNext = pathpointStart.distanceToSquared(pathpointEnd);
        this.path.clearPath();
        this.path.addPoint(pathpointStart);
        PathPoint pathpoint = pathpointStart;
        while (!this.path.isPathEmpty()) {
            PathPoint pathpoint1 = this.path.dequeue();
            if (pathpoint1.equals(pathpointEnd)) {
                return this.createEntityPath(pathpointStart, pathpointEnd);
            }
            if (pathpoint1.distanceToSquared(pathpointEnd) < pathpoint.distanceToSquared(pathpointEnd)) {
                pathpoint = pathpoint1;
            }
            pathpoint1.visited = true;
            int i2 = this.nodeProcessor.findPathOptions(this.pathOptions, entityIn, pathpoint1, pathpointEnd, maxDistance);
            for (int j2 = 0; j2 < i2; ++j2) {
                PathPoint pathpoint2 = this.pathOptions[j2];
                float f2 = pathpoint1.totalPathDistance + pathpoint1.distanceToSquared(pathpoint2);
                if (!(f2 < maxDistance * 2.0f) || pathpoint2.isAssigned() && !(f2 < pathpoint2.totalPathDistance)) continue;
                pathpoint2.previous = pathpoint1;
                pathpoint2.totalPathDistance = f2;
                pathpoint2.distanceToNext = pathpoint2.distanceToSquared(pathpointEnd);
                if (pathpoint2.isAssigned()) {
                    this.path.changeDistance(pathpoint2, pathpoint2.totalPathDistance + pathpoint2.distanceToNext);
                    continue;
                }
                pathpoint2.distanceToTarget = pathpoint2.totalPathDistance + pathpoint2.distanceToNext;
                this.path.addPoint(pathpoint2);
            }
        }
        if (pathpoint == pathpointStart) {
            return null;
        }
        return this.createEntityPath(pathpointStart, pathpoint);
    }

    private PathEntity createEntityPath(PathPoint start, PathPoint end) {
        int i2 = 1;
        PathPoint pathpoint = end;
        while (pathpoint.previous != null) {
            ++i2;
            pathpoint = pathpoint.previous;
        }
        PathPoint[] apathpoint = new PathPoint[i2];
        PathPoint pathpoint1 = end;
        apathpoint[--i2] = end;
        while (pathpoint1.previous != null) {
            pathpoint1 = pathpoint1.previous;
            apathpoint[--i2] = pathpoint1;
        }
        return new PathEntity(apathpoint);
    }
}

