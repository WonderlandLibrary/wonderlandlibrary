/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.pathfinding;

import net.minecraft.pathfinding.PathPoint;

public class Path {
    private PathPoint[] pathPoints = new PathPoint[1024];
    private int count;

    public void changeDistance(PathPoint pathPoint, float f) {
        float f2 = pathPoint.distanceToTarget;
        pathPoint.distanceToTarget = f;
        if (f < f2) {
            this.sortBack(pathPoint.index);
        } else {
            this.sortForward(pathPoint.index);
        }
    }

    private void sortForward(int n) {
        PathPoint pathPoint = this.pathPoints[n];
        float f = pathPoint.distanceToTarget;
        while (true) {
            float f2;
            PathPoint pathPoint2;
            int n2 = 1 + (n << 1);
            int n3 = n2 + 1;
            if (n2 >= this.count) break;
            PathPoint pathPoint3 = this.pathPoints[n2];
            float f3 = pathPoint3.distanceToTarget;
            if (n3 >= this.count) {
                pathPoint2 = null;
                f2 = Float.POSITIVE_INFINITY;
            } else {
                pathPoint2 = this.pathPoints[n3];
                f2 = pathPoint2.distanceToTarget;
            }
            if (f3 < f2) {
                if (f3 >= f) break;
                this.pathPoints[n] = pathPoint3;
                pathPoint3.index = n;
                n = n2;
                continue;
            }
            if (f2 >= f) break;
            this.pathPoints[n] = pathPoint2;
            pathPoint2.index = n;
            n = n3;
        }
        this.pathPoints[n] = pathPoint;
        pathPoint.index = n;
    }

    public void clearPath() {
        this.count = 0;
    }

    public PathPoint addPoint(PathPoint pathPoint) {
        if (pathPoint.index >= 0) {
            throw new IllegalStateException("OW KNOWS!");
        }
        if (this.count == this.pathPoints.length) {
            PathPoint[] pathPointArray = new PathPoint[this.count << 1];
            System.arraycopy(this.pathPoints, 0, pathPointArray, 0, this.count);
            this.pathPoints = pathPointArray;
        }
        this.pathPoints[this.count] = pathPoint;
        pathPoint.index = this.count;
        this.sortBack(this.count++);
        return pathPoint;
    }

    public boolean isPathEmpty() {
        return this.count == 0;
    }

    public PathPoint dequeue() {
        PathPoint pathPoint = this.pathPoints[0];
        this.pathPoints[0] = this.pathPoints[--this.count];
        this.pathPoints[this.count] = null;
        if (this.count > 0) {
            this.sortForward(0);
        }
        pathPoint.index = -1;
        return pathPoint;
    }

    private void sortBack(int n) {
        PathPoint pathPoint = this.pathPoints[n];
        float f = pathPoint.distanceToTarget;
        while (n > 0) {
            int n2 = n - 1 >> 1;
            PathPoint pathPoint2 = this.pathPoints[n2];
            if (f >= pathPoint2.distanceToTarget) break;
            this.pathPoints[n] = pathPoint2;
            pathPoint2.index = n;
            n = n2;
        }
        this.pathPoints[n] = pathPoint;
        pathPoint.index = n;
    }
}

