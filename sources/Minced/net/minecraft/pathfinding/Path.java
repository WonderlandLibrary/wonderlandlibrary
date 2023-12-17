// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import javax.annotation.Nullable;

public class Path
{
    private final PathPoint[] points;
    private PathPoint[] openSet;
    private PathPoint[] closedSet;
    private PathPoint target;
    private int currentPathIndex;
    private int pathLength;
    
    public Path(final PathPoint[] pathpoints) {
        this.openSet = new PathPoint[0];
        this.closedSet = new PathPoint[0];
        this.points = pathpoints;
        this.pathLength = pathpoints.length;
    }
    
    public void incrementPathIndex() {
        ++this.currentPathIndex;
    }
    
    public boolean isFinished() {
        return this.currentPathIndex >= this.pathLength;
    }
    
    @Nullable
    public PathPoint getFinalPathPoint() {
        return (this.pathLength > 0) ? this.points[this.pathLength - 1] : null;
    }
    
    public PathPoint getPathPointFromIndex(final int index) {
        return this.points[index];
    }
    
    public void setPoint(final int index, final PathPoint point) {
        this.points[index] = point;
    }
    
    public int getCurrentPathLength() {
        return this.pathLength;
    }
    
    public void setCurrentPathLength(final int length) {
        this.pathLength = length;
    }
    
    public int getCurrentPathIndex() {
        return this.currentPathIndex;
    }
    
    public void setCurrentPathIndex(final int currentPathIndexIn) {
        this.currentPathIndex = currentPathIndexIn;
    }
    
    public Vec3d getVectorFromIndex(final Entity entityIn, final int index) {
        final double d0 = this.points[index].x + (int)(entityIn.width + 1.0f) * 0.5;
        final double d2 = this.points[index].y;
        final double d3 = this.points[index].z + (int)(entityIn.width + 1.0f) * 0.5;
        return new Vec3d(d0, d2, d3);
    }
    
    public Vec3d getPosition(final Entity entityIn) {
        return this.getVectorFromIndex(entityIn, this.currentPathIndex);
    }
    
    public Vec3d getCurrentPos() {
        final PathPoint pathpoint = this.points[this.currentPathIndex];
        return new Vec3d(pathpoint.x, pathpoint.y, pathpoint.z);
    }
    
    public boolean isSamePath(final Path pathentityIn) {
        if (pathentityIn == null) {
            return false;
        }
        if (pathentityIn.points.length != this.points.length) {
            return false;
        }
        for (int i = 0; i < this.points.length; ++i) {
            if (this.points[i].x != pathentityIn.points[i].x || this.points[i].y != pathentityIn.points[i].y || this.points[i].z != pathentityIn.points[i].z) {
                return false;
            }
        }
        return true;
    }
    
    public PathPoint[] getOpenSet() {
        return this.openSet;
    }
    
    public PathPoint[] getClosedSet() {
        return this.closedSet;
    }
    
    public PathPoint getTarget() {
        return this.target;
    }
    
    public static Path read(final PacketBuffer buf) {
        final int i = buf.readInt();
        final PathPoint pathpoint = PathPoint.createFromBuffer(buf);
        final PathPoint[] apathpoint = new PathPoint[buf.readInt()];
        for (int j = 0; j < apathpoint.length; ++j) {
            apathpoint[j] = PathPoint.createFromBuffer(buf);
        }
        final PathPoint[] apathpoint2 = new PathPoint[buf.readInt()];
        for (int k = 0; k < apathpoint2.length; ++k) {
            apathpoint2[k] = PathPoint.createFromBuffer(buf);
        }
        final PathPoint[] apathpoint3 = new PathPoint[buf.readInt()];
        for (int l = 0; l < apathpoint3.length; ++l) {
            apathpoint3[l] = PathPoint.createFromBuffer(buf);
        }
        final Path path = new Path(apathpoint);
        path.openSet = apathpoint2;
        path.closedSet = apathpoint3;
        path.target = pathpoint;
        path.currentPathIndex = i;
        return path;
    }
}
