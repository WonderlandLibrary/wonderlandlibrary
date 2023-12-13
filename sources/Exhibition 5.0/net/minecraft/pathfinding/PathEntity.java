// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.pathfinding;

import net.minecraft.util.Vec3;
import net.minecraft.entity.Entity;

public class PathEntity
{
    private final PathPoint[] points;
    private int currentPathIndex;
    private int pathLength;
    private static final String __OBFID = "CL_00000575";
    
    public PathEntity(final PathPoint[] p_i2136_1_) {
        this.points = p_i2136_1_;
        this.pathLength = p_i2136_1_.length;
    }
    
    public void incrementPathIndex() {
        ++this.currentPathIndex;
    }
    
    public boolean isFinished() {
        return this.currentPathIndex >= this.pathLength;
    }
    
    public PathPoint getFinalPathPoint() {
        return (this.pathLength > 0) ? this.points[this.pathLength - 1] : null;
    }
    
    public PathPoint getPathPointFromIndex(final int p_75877_1_) {
        return this.points[p_75877_1_];
    }
    
    public int getCurrentPathLength() {
        return this.pathLength;
    }
    
    public void setCurrentPathLength(final int p_75871_1_) {
        this.pathLength = p_75871_1_;
    }
    
    public int getCurrentPathIndex() {
        return this.currentPathIndex;
    }
    
    public void setCurrentPathIndex(final int p_75872_1_) {
        this.currentPathIndex = p_75872_1_;
    }
    
    public Vec3 getVectorFromIndex(final Entity p_75881_1_, final int p_75881_2_) {
        final double var3 = this.points[p_75881_2_].xCoord + (int)(p_75881_1_.width + 1.0f) * 0.5;
        final double var4 = this.points[p_75881_2_].yCoord;
        final double var5 = this.points[p_75881_2_].zCoord + (int)(p_75881_1_.width + 1.0f) * 0.5;
        return new Vec3(var3, var4, var5);
    }
    
    public Vec3 getPosition(final Entity p_75878_1_) {
        return this.getVectorFromIndex(p_75878_1_, this.currentPathIndex);
    }
    
    public boolean isSamePath(final PathEntity p_75876_1_) {
        if (p_75876_1_ == null) {
            return false;
        }
        if (p_75876_1_.points.length != this.points.length) {
            return false;
        }
        for (int var2 = 0; var2 < this.points.length; ++var2) {
            if (this.points[var2].xCoord != p_75876_1_.points[var2].xCoord || this.points[var2].yCoord != p_75876_1_.points[var2].yCoord || this.points[var2].zCoord != p_75876_1_.points[var2].zCoord) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isDestinationSame(final Vec3 p_75880_1_) {
        final PathPoint var2 = this.getFinalPathPoint();
        return var2 != null && (var2.xCoord == (int)p_75880_1_.xCoord && var2.zCoord == (int)p_75880_1_.zCoord);
    }
}
