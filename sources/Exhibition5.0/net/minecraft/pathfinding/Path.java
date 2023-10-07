// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.pathfinding;

public class Path
{
    private PathPoint[] pathPoints;
    private int count;
    private static final String __OBFID = "CL_00000573";
    
    public Path() {
        this.pathPoints = new PathPoint[1024];
    }
    
    public PathPoint addPoint(final PathPoint point) {
        if (point.index >= 0) {
            throw new IllegalStateException("OW KNOWS!");
        }
        if (this.count == this.pathPoints.length) {
            final PathPoint[] var2 = new PathPoint[this.count << 1];
            System.arraycopy(this.pathPoints, 0, var2, 0, this.count);
            this.pathPoints = var2;
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
        final PathPoint var1 = this.pathPoints[0];
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
        var1.index = -1;
        return var1;
    }
    
    public void changeDistance(final PathPoint p_75850_1_, final float p_75850_2_) {
        final float var3 = p_75850_1_.distanceToTarget;
        p_75850_1_.distanceToTarget = p_75850_2_;
        if (p_75850_2_ < var3) {
            this.sortBack(p_75850_1_.index);
        }
        else {
            this.sortForward(p_75850_1_.index);
        }
    }
    
    private void sortBack(int p_75847_1_) {
        final PathPoint var2 = this.pathPoints[p_75847_1_];
        final float var3 = var2.distanceToTarget;
        while (p_75847_1_ > 0) {
            final int var4 = p_75847_1_ - 1 >> 1;
            final PathPoint var5 = this.pathPoints[var4];
            if (var3 >= var5.distanceToTarget) {
                break;
            }
            this.pathPoints[p_75847_1_] = var5;
            var5.index = p_75847_1_;
            p_75847_1_ = var4;
        }
        this.pathPoints[p_75847_1_] = var2;
        var2.index = p_75847_1_;
    }
    
    private void sortForward(int p_75846_1_) {
        final PathPoint var2 = this.pathPoints[p_75846_1_];
        final float var3 = var2.distanceToTarget;
        while (true) {
            final int var4 = 1 + (p_75846_1_ << 1);
            final int var5 = var4 + 1;
            if (var4 >= this.count) {
                break;
            }
            final PathPoint var6 = this.pathPoints[var4];
            final float var7 = var6.distanceToTarget;
            PathPoint var8;
            float var9;
            if (var5 >= this.count) {
                var8 = null;
                var9 = Float.POSITIVE_INFINITY;
            }
            else {
                var8 = this.pathPoints[var5];
                var9 = var8.distanceToTarget;
            }
            if (var7 < var9) {
                if (var7 >= var3) {
                    break;
                }
                this.pathPoints[p_75846_1_] = var6;
                var6.index = p_75846_1_;
                p_75846_1_ = var4;
            }
            else {
                if (var9 >= var3) {
                    break;
                }
                this.pathPoints[p_75846_1_] = var8;
                var8.index = p_75846_1_;
                p_75846_1_ = var5;
            }
        }
        this.pathPoints[p_75846_1_] = var2;
        var2.index = p_75846_1_;
    }
    
    public boolean isPathEmpty() {
        return this.count == 0;
    }
}
