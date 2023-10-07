// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils.skid.azura;

import net.minecraft.util.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.Vec3;

public class CustomVec3
{
    private final long creationTime;
    private double x;
    private double y;
    private double z;
    
    public CustomVec3(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.creationTime = System.currentTimeMillis();
    }
    
    public CustomVec3(final Vec3 vec) {
        this.x = vec.xCoord;
        this.y = vec.yCoord;
        this.z = vec.zCoord;
        this.creationTime = System.currentTimeMillis();
    }
    
    public CustomVec3(final PathPoint point) {
        this.x = point.xCoord + 0.5;
        this.y = point.yCoord;
        this.z = point.zCoord + 0.5;
        this.creationTime = System.currentTimeMillis();
    }
    
    public CustomVec3(final Entity entity) {
        this(entity.posX, entity.posY, entity.posZ);
    }
    
    public CustomVec3(final BlockPos pos) {
        this(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }
    
    public CustomVec3 offset(final double x, final double y, final double z) {
        return new CustomVec3(this.x + x, this.y + y, this.z + z);
    }
    
    public CustomVec3 copy() {
        return new CustomVec3(this.x, this.y, this.z);
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
    
    public void setZ(final double z) {
        this.z = z;
    }
    
    public double getDistance(final CustomVec3 other) {
        final double diffX = this.x - other.x;
        final double diffY = this.y - other.y;
        final double diffZ = this.z - other.z;
        return Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
    }
    
    @Override
    public boolean equals(final Object other) {
        if (other instanceof CustomVec3) {
            final CustomVec3 v = (CustomVec3)other;
            return this.x == v.getX() && this.y == v.getY() && this.z == v.getZ();
        }
        return false;
    }
    
    public BlockPos convertToBlockPos() {
        return new BlockPos(this.x, this.y, this.z);
    }
    
    public double getDistance(final Entity entity) {
        return this.getDistance(new CustomVec3(entity));
    }
    
    public long getCreationTime() {
        return this.creationTime;
    }
}
