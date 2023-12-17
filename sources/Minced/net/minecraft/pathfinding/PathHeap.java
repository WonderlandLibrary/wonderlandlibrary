// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

public class PathHeap
{
    private PathPoint[] pathPoints;
    private int count;
    
    public PathHeap() {
        this.pathPoints = new PathPoint[128];
    }
    
    public PathPoint addPoint(final PathPoint point) {
        if (point.index >= 0) {
            throw new IllegalStateException("OW KNOWS!");
        }
        if (this.count == this.pathPoints.length) {
            final PathPoint[] apathpoint = new PathPoint[this.count << 1];
            System.arraycopy(this.pathPoints, 0, apathpoint, 0, this.count);
            this.pathPoints = apathpoint;
        }
        this.pathPoints[this.count] = point;
        point.index = this.count;
        this.sortBack(this.count++);
        return point;
    }
    
    public void clearPath() {
        this.count = 0;
    }
    
    public PathPoint dequeue() {
        final PathPoint pathpoint = this.pathPoints[0];
        final PathPoint[] pathPoints = this.pathPoints;
        final int n = 0;
        final PathPoint[] pathPoints2 = this.pathPoints;
        final int count = this.count - 1;
        this.count = count;
        pathPoints[n] = pathPoints2[count];
        this.pathPoints[this.count] = null;
        if (this.count > 0) {
            this.sortForward(0);
        }
        pathpoint.index = -1;
        return pathpoint;
    }
    
    public void changeDistance(final PathPoint point, final float distance) {
        final float f = point.distanceToTarget;
        point.distanceToTarget = distance;
        if (distance < f) {
            this.sortBack(point.index);
        }
        else {
            this.sortForward(point.index);
        }
    }
    
    private void sortBack(int index) {
        final PathPoint pathpoint = this.pathPoints[index];
        final float f = pathpoint.distanceToTarget;
        while (index > 0) {
            final int i = index - 1 >> 1;
            final PathPoint pathpoint2 = this.pathPoints[i];
            if (f >= pathpoint2.distanceToTarget) {
                break;
            }
            this.pathPoints[index] = pathpoint2;
            pathpoint2.index = index;
            index = i;
        }
        this.pathPoints[index] = pathpoint;
        pathpoint.index = index;
    }
    
    private void sortForward(int index) {
        final PathPoint pathpoint = this.pathPoints[index];
        final float f = pathpoint.distanceToTarget;
        while (true) {
            final int i = 1 + (index << 1);
            final int j = i + 1;
            if (i >= this.count) {
                break;
            }
            final PathPoint pathpoint2 = this.pathPoints[i];
            final float f2 = pathpoint2.distanceToTarget;
            PathPoint pathpoint3;
            float f3;
            if (j >= this.count) {
                pathpoint3 = null;
                f3 = Float.POSITIVE_INFINITY;
            }
            else {
                pathpoint3 = this.pathPoints[j];
                f3 = pathpoint3.distanceToTarget;
            }
            if (f2 < f3) {
                if (f2 >= f) {
                    break;
                }
                this.pathPoints[index] = pathpoint2;
                pathpoint2.index = index;
                index = i;
            }
            else {
                if (f3 >= f) {
                    break;
                }
                this.pathPoints[index] = pathpoint3;
                pathpoint3.index = index;
                index = j;
            }
        }
        this.pathPoints[index] = pathpoint;
        pathpoint.index = index;
    }
    
    public boolean isPathEmpty() {
        return this.count == 0;
    }
}
