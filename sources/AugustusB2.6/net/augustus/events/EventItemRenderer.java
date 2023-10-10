// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.events;

public class EventItemRenderer extends Event
{
    double x;
    double y;
    double z;
    double scale;
    double blockX;
    double blockY;
    double blockZ;
    
    public EventItemRenderer(final double x, final double y, final double z, final double scale, final double blockX, final double blockY, final double blockZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.scale = scale;
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
    }
    
    public double getBlockX() {
        return this.blockX;
    }
    
    public void setBlockX(final double blockX) {
        this.blockX = blockX;
    }
    
    public double getBlockY() {
        return this.blockY;
    }
    
    public void setBlockY(final double blockY) {
        this.blockY = blockY;
    }
    
    public double getBlockZ() {
        return this.blockZ;
    }
    
    public void setBlockZ(final double blockZ) {
        this.blockZ = blockZ;
    }
    
    public double getX() {
        return this.x;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
    
    public double getScale() {
        return this.scale;
    }
    
    public void setScale(final double scale) {
        this.scale = scale;
    }
}
