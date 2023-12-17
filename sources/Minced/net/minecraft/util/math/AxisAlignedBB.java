// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.math;

import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;

public class AxisAlignedBB
{
    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;
    
    public AxisAlignedBB(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }
    
    public AxisAlignedBB(final BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }
    
    public AxisAlignedBB(final BlockPos pos1, final BlockPos pos2) {
        this(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
    }
    
    public AxisAlignedBB(final Vec3d min, final Vec3d max) {
        this(min.x, min.y, min.z, max.x, max.y, max.z);
    }
    
    public AxisAlignedBB setMaxY(final double y2) {
        return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, y2, this.maxZ);
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof AxisAlignedBB)) {
            return false;
        }
        final AxisAlignedBB axisalignedbb = (AxisAlignedBB)p_equals_1_;
        return Double.compare(axisalignedbb.minX, this.minX) == 0 && Double.compare(axisalignedbb.minY, this.minY) == 0 && Double.compare(axisalignedbb.minZ, this.minZ) == 0 && Double.compare(axisalignedbb.maxX, this.maxX) == 0 && Double.compare(axisalignedbb.maxY, this.maxY) == 0 && Double.compare(axisalignedbb.maxZ, this.maxZ) == 0;
    }
    
    @Override
    public int hashCode() {
        long i = Double.doubleToLongBits(this.minX);
        int j = (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(this.minY);
        j = 31 * j + (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(this.minZ);
        j = 31 * j + (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxX);
        j = 31 * j + (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxY);
        j = 31 * j + (int)(i ^ i >>> 32);
        i = Double.doubleToLongBits(this.maxZ);
        j = 31 * j + (int)(i ^ i >>> 32);
        return j;
    }
    
    public AxisAlignedBB contract(final double value) {
        return this.expand(-value, -value, -value);
    }
    
    public AxisAlignedBB contract(final double x, final double y, final double z) {
        double d0 = this.minX;
        double d2 = this.minY;
        double d3 = this.minZ;
        double d4 = this.maxX;
        double d5 = this.maxY;
        double d6 = this.maxZ;
        if (x < 0.0) {
            d0 -= x;
        }
        else if (x > 0.0) {
            d4 -= x;
        }
        if (y < 0.0) {
            d2 -= y;
        }
        else if (y > 0.0) {
            d5 -= y;
        }
        if (z < 0.0) {
            d3 -= z;
        }
        else if (z > 0.0) {
            d6 -= z;
        }
        return new AxisAlignedBB(d0, d2, d3, d4, d5, d6);
    }
    
    public AxisAlignedBB expand(final double x, final double y, final double z) {
        double d0 = this.minX;
        double d2 = this.minY;
        double d3 = this.minZ;
        double d4 = this.maxX;
        double d5 = this.maxY;
        double d6 = this.maxZ;
        if (x < 0.0) {
            d0 += x;
        }
        else if (x > 0.0) {
            d4 += x;
        }
        if (y < 0.0) {
            d2 += y;
        }
        else if (y > 0.0) {
            d5 += y;
        }
        if (z < 0.0) {
            d3 += z;
        }
        else if (z > 0.0) {
            d6 += z;
        }
        return new AxisAlignedBB(d0, d2, d3, d4, d5, d6);
    }
    
    public AxisAlignedBB grow(final double x, final double y, final double z) {
        final double d0 = this.minX - x;
        final double d2 = this.minY - y;
        final double d3 = this.minZ - z;
        final double d4 = this.maxX + x;
        final double d5 = this.maxY + y;
        final double d6 = this.maxZ + z;
        return new AxisAlignedBB(d0, d2, d3, d4, d5, d6);
    }
    
    public AxisAlignedBB grow(final double value) {
        return this.grow(value, value, value);
    }
    
    public AxisAlignedBB intersect(final AxisAlignedBB other) {
        final double d0 = Math.max(this.minX, other.minX);
        final double d2 = Math.max(this.minY, other.minY);
        final double d3 = Math.max(this.minZ, other.minZ);
        final double d4 = Math.min(this.maxX, other.maxX);
        final double d5 = Math.min(this.maxY, other.maxY);
        final double d6 = Math.min(this.maxZ, other.maxZ);
        return new AxisAlignedBB(d0, d2, d3, d4, d5, d6);
    }
    
    public AxisAlignedBB union(final AxisAlignedBB other) {
        final double d0 = Math.min(this.minX, other.minX);
        final double d2 = Math.min(this.minY, other.minY);
        final double d3 = Math.min(this.minZ, other.minZ);
        final double d4 = Math.max(this.maxX, other.maxX);
        final double d5 = Math.max(this.maxY, other.maxY);
        final double d6 = Math.max(this.maxZ, other.maxZ);
        return new AxisAlignedBB(d0, d2, d3, d4, d5, d6);
    }
    
    public AxisAlignedBB offset(final double x, final double y, final double z) {
        return new AxisAlignedBB(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }
    
    public AxisAlignedBB offset(final BlockPos pos) {
        return new AxisAlignedBB(this.minX + pos.getX(), this.minY + pos.getY(), this.minZ + pos.getZ(), this.maxX + pos.getX(), this.maxY + pos.getY(), this.maxZ + pos.getZ());
    }
    
    public AxisAlignedBB offset(final Vec3d vec) {
        return this.offset(vec.x, vec.y, vec.z);
    }
    
    public double calculateXOffset(final AxisAlignedBB other, double offsetX) {
        if (other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ) {
            if (offsetX > 0.0 && other.maxX <= this.minX) {
                final double d1 = this.minX - other.maxX;
                if (d1 < offsetX) {
                    offsetX = d1;
                }
            }
            else if (offsetX < 0.0 && other.minX >= this.maxX) {
                final double d2 = this.maxX - other.minX;
                if (d2 > offsetX) {
                    offsetX = d2;
                }
            }
            return offsetX;
        }
        return offsetX;
    }
    
    public double calculateYOffset(final AxisAlignedBB other, double offsetY) {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxZ > this.minZ && other.minZ < this.maxZ) {
            if (offsetY > 0.0 && other.maxY <= this.minY) {
                final double d1 = this.minY - other.maxY;
                if (d1 < offsetY) {
                    offsetY = d1;
                }
            }
            else if (offsetY < 0.0 && other.minY >= this.maxY) {
                final double d2 = this.maxY - other.minY;
                if (d2 > offsetY) {
                    offsetY = d2;
                }
            }
            return offsetY;
        }
        return offsetY;
    }
    
    public double calculateZOffset(final AxisAlignedBB other, double offsetZ) {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY) {
            if (offsetZ > 0.0 && other.maxZ <= this.minZ) {
                final double d1 = this.minZ - other.maxZ;
                if (d1 < offsetZ) {
                    offsetZ = d1;
                }
            }
            else if (offsetZ < 0.0 && other.minZ >= this.maxZ) {
                final double d2 = this.maxZ - other.minZ;
                if (d2 > offsetZ) {
                    offsetZ = d2;
                }
            }
            return offsetZ;
        }
        return offsetZ;
    }
    
    public boolean intersects(final AxisAlignedBB other) {
        return this.intersects(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ);
    }
    
    public boolean intersects(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        return this.minX < x2 && this.maxX > x1 && this.minY < y2 && this.maxY > y1 && this.minZ < z2 && this.maxZ > z1;
    }
    
    public boolean intersects(final Vec3d min, final Vec3d max) {
        return this.intersects(Math.min(min.x, max.x), Math.min(min.y, max.y), Math.min(min.z, max.z), Math.max(min.x, max.x), Math.max(min.y, max.y), Math.max(min.z, max.z));
    }
    
    public boolean contains(final Vec3d vec) {
        return vec.x > this.minX && vec.x < this.maxX && (vec.y > this.minY && vec.y < this.maxY) && vec.z > this.minZ && vec.z < this.maxZ;
    }
    
    public double getAverageEdgeLength() {
        final double d0 = this.maxX - this.minX;
        final double d2 = this.maxY - this.minY;
        final double d3 = this.maxZ - this.minZ;
        return (d0 + d2 + d3) / 3.0;
    }
    
    public AxisAlignedBB shrink(final double value) {
        return this.grow(-value);
    }
    
    @Nullable
    public RayTraceResult calculateIntercept(final Vec3d vecA, final Vec3d vecB) {
        Vec3d vec3d = this.collideWithXPlane(this.minX, vecA, vecB);
        EnumFacing enumfacing = EnumFacing.WEST;
        Vec3d vec3d2 = this.collideWithXPlane(this.maxX, vecA, vecB);
        if (vec3d2 != null && this.isClosest(vecA, vec3d, vec3d2)) {
            vec3d = vec3d2;
            enumfacing = EnumFacing.EAST;
        }
        vec3d2 = this.collideWithYPlane(this.minY, vecA, vecB);
        if (vec3d2 != null && this.isClosest(vecA, vec3d, vec3d2)) {
            vec3d = vec3d2;
            enumfacing = EnumFacing.DOWN;
        }
        vec3d2 = this.collideWithYPlane(this.maxY, vecA, vecB);
        if (vec3d2 != null && this.isClosest(vecA, vec3d, vec3d2)) {
            vec3d = vec3d2;
            enumfacing = EnumFacing.UP;
        }
        vec3d2 = this.collideWithZPlane(this.minZ, vecA, vecB);
        if (vec3d2 != null && this.isClosest(vecA, vec3d, vec3d2)) {
            vec3d = vec3d2;
            enumfacing = EnumFacing.NORTH;
        }
        vec3d2 = this.collideWithZPlane(this.maxZ, vecA, vecB);
        if (vec3d2 != null && this.isClosest(vecA, vec3d, vec3d2)) {
            vec3d = vec3d2;
            enumfacing = EnumFacing.SOUTH;
        }
        return (vec3d == null) ? null : new RayTraceResult(vec3d, enumfacing);
    }
    
    @VisibleForTesting
    boolean isClosest(final Vec3d p_186661_1_, @Nullable final Vec3d p_186661_2_, final Vec3d p_186661_3_) {
        return p_186661_2_ == null || p_186661_1_.squareDistanceTo(p_186661_3_) < p_186661_1_.squareDistanceTo(p_186661_2_);
    }
    
    @Nullable
    @VisibleForTesting
    Vec3d collideWithXPlane(final double p_186671_1_, final Vec3d p_186671_3_, final Vec3d p_186671_4_) {
        final Vec3d vec3d = p_186671_3_.getIntermediateWithXValue(p_186671_4_, p_186671_1_);
        return (vec3d != null && this.intersectsWithYZ(vec3d)) ? vec3d : null;
    }
    
    @Nullable
    @VisibleForTesting
    Vec3d collideWithYPlane(final double p_186663_1_, final Vec3d p_186663_3_, final Vec3d p_186663_4_) {
        final Vec3d vec3d = p_186663_3_.getIntermediateWithYValue(p_186663_4_, p_186663_1_);
        return (vec3d != null && this.intersectsWithXZ(vec3d)) ? vec3d : null;
    }
    
    @Nullable
    @VisibleForTesting
    Vec3d collideWithZPlane(final double p_186665_1_, final Vec3d p_186665_3_, final Vec3d p_186665_4_) {
        final Vec3d vec3d = p_186665_3_.getIntermediateWithZValue(p_186665_4_, p_186665_1_);
        return (vec3d != null && this.intersectsWithXY(vec3d)) ? vec3d : null;
    }
    
    @VisibleForTesting
    public boolean intersectsWithYZ(final Vec3d vec) {
        return vec.y >= this.minY && vec.y <= this.maxY && vec.z >= this.minZ && vec.z <= this.maxZ;
    }
    
    @VisibleForTesting
    public boolean intersectsWithXZ(final Vec3d vec) {
        return vec.x >= this.minX && vec.x <= this.maxX && vec.z >= this.minZ && vec.z <= this.maxZ;
    }
    
    @VisibleForTesting
    public boolean intersectsWithXY(final Vec3d vec) {
        return vec.x >= this.minX && vec.x <= this.maxX && vec.y >= this.minY && vec.y <= this.maxY;
    }
    
    @Override
    public String toString() {
        return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }
    
    public boolean hasNaN() {
        return Double.isNaN(this.minX) || Double.isNaN(this.minY) || Double.isNaN(this.minZ) || Double.isNaN(this.maxX) || Double.isNaN(this.maxY) || Double.isNaN(this.maxZ);
    }
    
    public Vec3d getCenter() {
        return new Vec3d(this.minX + (this.maxX - this.minX) * 0.5, this.minY + (this.maxY - this.minY) * 0.5, this.minZ + (this.maxZ - this.minZ) * 0.5);
    }
    
    public AxisAlignedBB addCoord(final double x, final double y, final double z) {
        double d0 = this.minX;
        double d2 = this.minY;
        double d3 = this.minZ;
        double d4 = this.maxX;
        double d5 = this.maxY;
        double d6 = this.maxZ;
        if (x < 0.0) {
            d0 += x;
        }
        else if (x > 0.0) {
            d4 += x;
        }
        if (y < 0.0) {
            d2 += y;
        }
        else if (y > 0.0) {
            d5 += y;
        }
        if (z < 0.0) {
            d3 += z;
        }
        else if (z > 0.0) {
            d6 += z;
        }
        return new AxisAlignedBB(d0, d2, d3, d4, d5, d6);
    }
    
    public AxisAlignedBB expandXyz(final double value) {
        return this.expand(value, value, value);
    }
    
    public boolean isVecInside(final Vec3d vec) {
        return vec.x > this.minX && vec.x < this.maxX && (vec.y > this.minY && vec.y < this.maxY) && vec.z > this.minZ && vec.z < this.maxZ;
    }
}
