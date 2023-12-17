// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.IBlockAccess;
import com.google.common.collect.Sets;
import java.util.Set;

public class PathFinder
{
    private final PathHeap path;
    private final Set<PathPoint> closedSet;
    private final PathPoint[] pathOptions;
    private final NodeProcessor nodeProcessor;
    
    public PathFinder(final NodeProcessor processor) {
        this.path = new PathHeap();
        this.closedSet = (Set<PathPoint>)Sets.newHashSet();
        this.pathOptions = new PathPoint[32];
        this.nodeProcessor = processor;
    }
    
    @Nullable
    public Path findPath(final IBlockAccess worldIn, final EntityLiving entitylivingIn, final Entity targetEntity, final float maxDistance) {
        return this.findPath(worldIn, entitylivingIn, targetEntity.posX, targetEntity.getEntityBoundingBox().minY, targetEntity.posZ, maxDistance);
    }
    
    @Nullable
    public Path findPath(final IBlockAccess worldIn, final EntityLiving entitylivingIn, final BlockPos targetPos, final float maxDistance) {
        return this.findPath(worldIn, entitylivingIn, targetPos.getX() + 0.5f, targetPos.getY() + 0.5f, targetPos.getZ() + 0.5f, maxDistance);
    }
    
    @Nullable
    private Path findPath(final IBlockAccess worldIn, final EntityLiving entitylivingIn, final double x, final double y, final double z, final float maxDistance) {
        this.path.clearPath();
        this.nodeProcessor.init(worldIn, entitylivingIn);
        final PathPoint pathpoint = this.nodeProcessor.getStart();
        final PathPoint pathpoint2 = this.nodeProcessor.getPathPointToCoords(x, y, z);
        final Path path = this.findPath(pathpoint, pathpoint2, maxDistance);
        this.nodeProcessor.postProcess();
        return path;
    }
    
    @Nullable
    private Path findPath(final PathPoint pathFrom, final PathPoint pathTo, final float maxDistance) {
        pathFrom.totalPathDistance = 0.0f;
        pathFrom.distanceToNext = pathFrom.distanceManhattan(pathTo);
        pathFrom.distanceToTarget = pathFrom.distanceToNext;
        this.path.clearPath();
        this.closedSet.clear();
        this.path.addPoint(pathFrom);
        PathPoint pathpoint = pathFrom;
        int i = 0;
        while (!this.path.isPathEmpty()) {
            if (++i >= 200) {
                break;
            }
            final PathPoint pathpoint2 = this.path.dequeue();
            if (pathpoint2.equals(pathTo)) {
                pathpoint = pathTo;
                break;
            }
            if (pathpoint2.distanceManhattan(pathTo) < pathpoint.distanceManhattan(pathTo)) {
                pathpoint = pathpoint2;
            }
            pathpoint2.visited = true;
            for (int j = this.nodeProcessor.findPathOptions(this.pathOptions, pathpoint2, pathTo, maxDistance), k = 0; k < j; ++k) {
                final PathPoint pathpoint3 = this.pathOptions[k];
                final float f = pathpoint2.distanceManhattan(pathpoint3);
                pathpoint3.distanceFromOrigin = pathpoint2.distanceFromOrigin + f;
                pathpoint3.cost = f + pathpoint3.costMalus;
                final float f2 = pathpoint2.totalPathDistance + pathpoint3.cost;
                if (pathpoint3.distanceFromOrigin < maxDistance && (!pathpoint3.isAssigned() || f2 < pathpoint3.totalPathDistance)) {
                    pathpoint3.previous = pathpoint2;
                    pathpoint3.totalPathDistance = f2;
                    pathpoint3.distanceToNext = pathpoint3.distanceManhattan(pathTo) + pathpoint3.costMalus;
                    if (pathpoint3.isAssigned()) {
                        this.path.changeDistance(pathpoint3, pathpoint3.totalPathDistance + pathpoint3.distanceToNext);
                    }
                    else {
                        pathpoint3.distanceToTarget = pathpoint3.totalPathDistance + pathpoint3.distanceToNext;
                        this.path.addPoint(pathpoint3);
                    }
                }
            }
        }
        if (pathpoint == pathFrom) {
            return null;
        }
        final Path path = this.createPath(pathFrom, pathpoint);
        return path;
    }
    
    private Path createPath(final PathPoint start, final PathPoint end) {
        int i = 1;
        for (PathPoint pathpoint = end; pathpoint.previous != null; pathpoint = pathpoint.previous) {
            ++i;
        }
        final PathPoint[] apathpoint = new PathPoint[i];
        PathPoint pathpoint2 = end;
        --i;
        apathpoint[i] = end;
        while (pathpoint2.previous != null) {
            pathpoint2 = pathpoint2.previous;
            --i;
            apathpoint[i] = pathpoint2;
        }
        return new Path(apathpoint);
    }
}
