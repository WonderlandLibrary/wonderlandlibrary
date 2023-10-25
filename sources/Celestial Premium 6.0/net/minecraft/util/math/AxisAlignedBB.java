/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.math;

import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nullable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class AxisAlignedBB {
    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;

    public AxisAlignedBB(double x1, double y1, double z1, double x2, double y2, double z2) {
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }

    public AxisAlignedBB(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }

    public AxisAlignedBB(BlockPos pos1, BlockPos pos2) {
        this(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
    }

    public AxisAlignedBB(Vec3d min, Vec3d max) {
        this(min.x, min.y, min.z, max.x, max.y, max.z);
    }

    public AxisAlignedBB setMaxY(double y2) {
        return new AxisAlignedBB(this.minX, this.minY, this.minZ, this.maxX, y2, this.maxZ);
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof AxisAlignedBB)) {
            return false;
        }
        AxisAlignedBB axisalignedbb = (AxisAlignedBB)p_equals_1_;
        if (Double.compare(axisalignedbb.minX, this.minX) != 0) {
            return false;
        }
        if (Double.compare(axisalignedbb.minY, this.minY) != 0) {
            return false;
        }
        if (Double.compare(axisalignedbb.minZ, this.minZ) != 0) {
            return false;
        }
        if (Double.compare(axisalignedbb.maxX, this.maxX) != 0) {
            return false;
        }
        if (Double.compare(axisalignedbb.maxY, this.maxY) != 0) {
            return false;
        }
        return Double.compare(axisalignedbb.maxZ, this.maxZ) == 0;
    }

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

    public AxisAlignedBB func_191195_a(double p_191195_1_, double p_191195_3_, double p_191195_5_) {
        double d0 = this.minX;
        double d1 = this.minY;
        double d2 = this.minZ;
        double d3 = this.maxX;
        double d4 = this.maxY;
        double d5 = this.maxZ;
        if (p_191195_1_ < 0.0) {
            d0 -= p_191195_1_;
        } else if (p_191195_1_ > 0.0) {
            d3 -= p_191195_1_;
        }
        if (p_191195_3_ < 0.0) {
            d1 -= p_191195_3_;
        } else if (p_191195_3_ > 0.0) {
            d4 -= p_191195_3_;
        }
        if (p_191195_5_ < 0.0) {
            d2 -= p_191195_5_;
        } else if (p_191195_5_ > 0.0) {
            d5 -= p_191195_5_;
        }
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public AxisAlignedBB addCoord(double x, double y, double z) {
        double d0 = this.minX;
        double d1 = this.minY;
        double d2 = this.minZ;
        double d3 = this.maxX;
        double d4 = this.maxY;
        double d5 = this.maxZ;
        if (x < 0.0) {
            d0 += x;
        } else if (x > 0.0) {
            d3 += x;
        }
        if (y < 0.0) {
            d1 += y;
        } else if (y > 0.0) {
            d4 += y;
        }
        if (z < 0.0) {
            d2 += z;
        } else if (z > 0.0) {
            d5 += z;
        }
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public AxisAlignedBB expand(double x, double y, double z) {
        double d0 = this.minX - x;
        double d1 = this.minY - y;
        double d2 = this.minZ - z;
        double d3 = this.maxX + x;
        double d4 = this.maxY + y;
        double d5 = this.maxZ + z;
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public AxisAlignedBB expandXyz(double value) {
        return this.expand(value, value, value);
    }

    public AxisAlignedBB func_191500_a(AxisAlignedBB p_191500_1_) {
        double d0 = Math.max(this.minX, p_191500_1_.minX);
        double d1 = Math.max(this.minY, p_191500_1_.minY);
        double d2 = Math.max(this.minZ, p_191500_1_.minZ);
        double d3 = Math.min(this.maxX, p_191500_1_.maxX);
        double d4 = Math.min(this.maxY, p_191500_1_.maxY);
        double d5 = Math.min(this.maxZ, p_191500_1_.maxZ);
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public AxisAlignedBB union(AxisAlignedBB other) {
        double d0 = Math.min(this.minX, other.minX);
        double d1 = Math.min(this.minY, other.minY);
        double d2 = Math.min(this.minZ, other.minZ);
        double d3 = Math.max(this.maxX, other.maxX);
        double d4 = Math.max(this.maxY, other.maxY);
        double d5 = Math.max(this.maxZ, other.maxZ);
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public AxisAlignedBB offset(double x, double y, double z) {
        return new AxisAlignedBB(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    public AxisAlignedBB offset(BlockPos pos) {
        return new AxisAlignedBB(this.minX + (double)pos.getX(), this.minY + (double)pos.getY(), this.minZ + (double)pos.getZ(), this.maxX + (double)pos.getX(), this.maxY + (double)pos.getY(), this.maxZ + (double)pos.getZ());
    }

    public AxisAlignedBB func_191194_a(Vec3d p_191194_1_) {
        return this.offset(p_191194_1_.x, p_191194_1_.y, p_191194_1_.z);
    }

    public double calculateXOffset(AxisAlignedBB other, double offsetX) {
        if (other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ) {
            double d0;
            if (offsetX > 0.0 && other.maxX <= this.minX) {
                double d1 = this.minX - other.maxX;
                if (d1 < offsetX) {
                    offsetX = d1;
                }
            } else if (offsetX < 0.0 && other.minX >= this.maxX && (d0 = this.maxX - other.minX) > offsetX) {
                offsetX = d0;
            }
            return offsetX;
        }
        return offsetX;
    }

    public double calculateYOffset(AxisAlignedBB other, double offsetY) {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxZ > this.minZ && other.minZ < this.maxZ) {
            double d0;
            if (offsetY > 0.0 && other.maxY <= this.minY) {
                double d1 = this.minY - other.maxY;
                if (d1 < offsetY) {
                    offsetY = d1;
                }
            } else if (offsetY < 0.0 && other.minY >= this.maxY && (d0 = this.maxY - other.minY) > offsetY) {
                offsetY = d0;
            }
            return offsetY;
        }
        return offsetY;
    }

    public double calculateZOffset(AxisAlignedBB other, double offsetZ) {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY) {
            double d0;
            if (offsetZ > 0.0 && other.maxZ <= this.minZ) {
                double d1 = this.minZ - other.maxZ;
                if (d1 < offsetZ) {
                    offsetZ = d1;
                }
            } else if (offsetZ < 0.0 && other.minZ >= this.maxZ && (d0 = this.maxZ - other.minZ) > offsetZ) {
                offsetZ = d0;
            }
            return offsetZ;
        }
        return offsetZ;
    }

    public boolean intersectsWith(AxisAlignedBB other) {
        return this.intersects(other.minX, other.minY, other.minZ, other.maxX, other.maxY, other.maxZ);
    }

    public boolean intersects(double x1, double y1, double z1, double x2, double y2, double z2) {
        return this.minX < x2 && this.maxX > x1 && this.minY < y2 && this.maxY > y1 && this.minZ < z2 && this.maxZ > z1;
    }

    public boolean intersects(Vec3d min, Vec3d max) {
        return this.intersects(Math.min(min.x, max.x), Math.min(min.y, max.y), Math.min(min.z, max.z), Math.max(min.x, max.x), Math.max(min.y, max.y), Math.max(min.z, max.z));
    }

    public boolean isVecInside(Vec3d vec) {
        if (vec.x > this.minX && vec.x < this.maxX) {
            if (vec.y > this.minY && vec.y < this.maxY) {
                return vec.z > this.minZ && vec.z < this.maxZ;
            }
            return false;
        }
        return false;
    }

    public double getAverageEdgeLength() {
        double d0 = this.maxX - this.minX;
        double d1 = this.maxY - this.minY;
        double d2 = this.maxZ - this.minZ;
        return (d0 + d1 + d2) / 3.0;
    }

    public AxisAlignedBB contract(double value) {
        return this.expandXyz(-value);
    }

    @Nullable
    public RayTraceResult calculateIntercept(Vec3d vecA, Vec3d vecB) {
        Vec3d vec3d = this.collideWithXPlane(this.minX, vecA, vecB);
        EnumFacing enumfacing = EnumFacing.WEST;
        Vec3d vec3d1 = this.collideWithXPlane(this.maxX, vecA, vecB);
        if (vec3d1 != null && this.isClosest(vecA, vec3d, vec3d1)) {
            vec3d = vec3d1;
            enumfacing = EnumFacing.EAST;
        }
        if ((vec3d1 = this.collideWithYPlane(this.minY, vecA, vecB)) != null && this.isClosest(vecA, vec3d, vec3d1)) {
            vec3d = vec3d1;
            enumfacing = EnumFacing.DOWN;
        }
        if ((vec3d1 = this.collideWithYPlane(this.maxY, vecA, vecB)) != null && this.isClosest(vecA, vec3d, vec3d1)) {
            vec3d = vec3d1;
            enumfacing = EnumFacing.UP;
        }
        if ((vec3d1 = this.collideWithZPlane(this.minZ, vecA, vecB)) != null && this.isClosest(vecA, vec3d, vec3d1)) {
            vec3d = vec3d1;
            enumfacing = EnumFacing.NORTH;
        }
        if ((vec3d1 = this.collideWithZPlane(this.maxZ, vecA, vecB)) != null && this.isClosest(vecA, vec3d, vec3d1)) {
            vec3d = vec3d1;
            enumfacing = EnumFacing.SOUTH;
        }
        return vec3d == null ? null : new RayTraceResult(vec3d, enumfacing);
    }

    @VisibleForTesting
    boolean isClosest(Vec3d p_186661_1_, @Nullable Vec3d p_186661_2_, Vec3d p_186661_3_) {
        return p_186661_2_ == null || p_186661_1_.squareDistanceTo(p_186661_3_) < p_186661_1_.squareDistanceTo(p_186661_2_);
    }

    @Nullable
    @VisibleForTesting
    Vec3d collideWithXPlane(double p_186671_1_, Vec3d p_186671_3_, Vec3d p_186671_4_) {
        Vec3d vec3d = p_186671_3_.getIntermediateWithXValue(p_186671_4_, p_186671_1_);
        return vec3d != null && this.intersectsWithYZ(vec3d) ? vec3d : null;
    }

    @Nullable
    @VisibleForTesting
    Vec3d collideWithYPlane(double p_186663_1_, Vec3d p_186663_3_, Vec3d p_186663_4_) {
        Vec3d vec3d = p_186663_3_.getIntermediateWithYValue(p_186663_4_, p_186663_1_);
        return vec3d != null && this.intersectsWithXZ(vec3d) ? vec3d : null;
    }

    @Nullable
    @VisibleForTesting
    Vec3d collideWithZPlane(double p_186665_1_, Vec3d p_186665_3_, Vec3d p_186665_4_) {
        Vec3d vec3d = p_186665_3_.getIntermediateWithZValue(p_186665_4_, p_186665_1_);
        return vec3d != null && this.intersectsWithXY(vec3d) ? vec3d : null;
    }

    @VisibleForTesting
    public boolean intersectsWithYZ(Vec3d vec) {
        return vec.y >= this.minY && vec.y <= this.maxY && vec.z >= this.minZ && vec.z <= this.maxZ;
    }

    @VisibleForTesting
    public boolean intersectsWithXZ(Vec3d vec) {
        return vec.x >= this.minX && vec.x <= this.maxX && vec.z >= this.minZ && vec.z <= this.maxZ;
    }

    @VisibleForTesting
    public boolean intersectsWithXY(Vec3d vec) {
        return vec.x >= this.minX && vec.x <= this.maxX && vec.y >= this.minY && vec.y <= this.maxY;
    }

    public String toString() {
        return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }

    public boolean hasNaN() {
        return Double.isNaN(this.minX) || Double.isNaN(this.minY) || Double.isNaN(this.minZ) || Double.isNaN(this.maxX) || Double.isNaN(this.maxY) || Double.isNaN(this.maxZ);
    }

    public Vec3d getCenter2() {
        return new Vec3d(this.minX + (this.maxX - this.minX) * 0.55, this.minY + (this.maxY - this.minY) * 0.55, this.minZ + (this.maxZ - this.minZ) * 0.55);
    }

    public Vec3d getCenter() {
        return new Vec3d(this.minX + (this.maxX - this.minX) * 0.5, this.minY + (this.maxY - this.minY) * 0.5, this.minZ + (this.maxZ - this.minZ) * 0.5);
    }
}

