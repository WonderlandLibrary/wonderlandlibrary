/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

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

    public AxisAlignedBB(BlockPos pos1, BlockPos pos2) {
        this.minX = pos1.getX();
        this.minY = pos1.getY();
        this.minZ = pos1.getZ();
        this.maxX = pos2.getX();
        this.maxY = pos2.getY();
        this.maxZ = pos2.getZ();
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
            return new AxisAlignedBB(d0, d1, d2 += z, d3, d4, d5);
        }
        if (!(z > 0.0)) return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
        d5 += z;
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

    public AxisAlignedBB union(AxisAlignedBB other) {
        double d0 = Math.min(this.minX, other.minX);
        double d1 = Math.min(this.minY, other.minY);
        double d2 = Math.min(this.minZ, other.minZ);
        double d3 = Math.max(this.maxX, other.maxX);
        double d4 = Math.max(this.maxY, other.maxY);
        double d5 = Math.max(this.maxZ, other.maxZ);
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public static AxisAlignedBB fromBounds(double x1, double y1, double z1, double x2, double y2, double z2) {
        double d0 = Math.min(x1, x2);
        double d1 = Math.min(y1, y2);
        double d2 = Math.min(z1, z2);
        double d3 = Math.max(x1, x2);
        double d4 = Math.max(y1, y2);
        double d5 = Math.max(z1, z2);
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public AxisAlignedBB offset(double x, double y, double z) {
        return new AxisAlignedBB(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }

    public double calculateXOffset(AxisAlignedBB other, double offsetX) {
        if (!(other.maxY > this.minY)) return offsetX;
        if (!(other.minY < this.maxY)) return offsetX;
        if (!(other.maxZ > this.minZ)) return offsetX;
        if (!(other.minZ < this.maxZ)) return offsetX;
        if (offsetX > 0.0 && other.maxX <= this.minX) {
            double d1 = this.minX - other.maxX;
            if (!(d1 < offsetX)) return offsetX;
            return d1;
        }
        if (!(offsetX < 0.0)) return offsetX;
        if (!(other.minX >= this.maxX)) return offsetX;
        double d0 = this.maxX - other.minX;
        if (!(d0 > offsetX)) return offsetX;
        return d0;
    }

    public double calculateYOffset(AxisAlignedBB other, double offsetY) {
        if (!(other.maxX > this.minX)) return offsetY;
        if (!(other.minX < this.maxX)) return offsetY;
        if (!(other.maxZ > this.minZ)) return offsetY;
        if (!(other.minZ < this.maxZ)) return offsetY;
        if (offsetY > 0.0 && other.maxY <= this.minY) {
            double d1 = this.minY - other.maxY;
            if (!(d1 < offsetY)) return offsetY;
            return d1;
        }
        if (!(offsetY < 0.0)) return offsetY;
        if (!(other.minY >= this.maxY)) return offsetY;
        double d0 = this.maxY - other.minY;
        if (!(d0 > offsetY)) return offsetY;
        return d0;
    }

    public double calculateZOffset(AxisAlignedBB other, double offsetZ) {
        if (!(other.maxX > this.minX)) return offsetZ;
        if (!(other.minX < this.maxX)) return offsetZ;
        if (!(other.maxY > this.minY)) return offsetZ;
        if (!(other.minY < this.maxY)) return offsetZ;
        if (offsetZ > 0.0 && other.maxZ <= this.minZ) {
            double d1 = this.minZ - other.maxZ;
            if (!(d1 < offsetZ)) return offsetZ;
            return d1;
        }
        if (!(offsetZ < 0.0)) return offsetZ;
        if (!(other.minZ >= this.maxZ)) return offsetZ;
        double d0 = this.maxZ - other.minZ;
        if (!(d0 > offsetZ)) return offsetZ;
        return d0;
    }

    public boolean intersectsWith(AxisAlignedBB other) {
        if (!(other.maxX > this.minX)) return false;
        if (!(other.minX < this.maxX)) return false;
        if (!(other.maxY > this.minY)) return false;
        if (!(other.minY < this.maxY)) return false;
        if (!(other.maxZ > this.minZ)) return false;
        if (!(other.minZ < this.maxZ)) return false;
        return true;
    }

    public boolean isVecInside(Vec3 vec) {
        if (!(vec.xCoord > this.minX)) return false;
        if (!(vec.xCoord < this.maxX)) return false;
        if (!(vec.yCoord > this.minY)) return false;
        if (!(vec.yCoord < this.maxY)) return false;
        if (!(vec.zCoord > this.minZ)) return false;
        if (!(vec.zCoord < this.maxZ)) return false;
        return true;
    }

    public double getAverageEdgeLength() {
        double d0 = this.maxX - this.minX;
        double d1 = this.maxY - this.minY;
        double d2 = this.maxZ - this.minZ;
        return (d0 + d1 + d2) / 3.0;
    }

    public AxisAlignedBB contract(double x, double y, double z) {
        double d0 = this.minX + x;
        double d1 = this.minY + y;
        double d2 = this.minZ + z;
        double d3 = this.maxX - x;
        double d4 = this.maxY - y;
        double d5 = this.maxZ - z;
        return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
    }

    public MovingObjectPosition calculateIntercept(Vec3 vecA, Vec3 vecB) {
        Vec3 vec3 = vecA.getIntermediateWithXValue(vecB, this.minX);
        Vec3 vec31 = vecA.getIntermediateWithXValue(vecB, this.maxX);
        Vec3 vec32 = vecA.getIntermediateWithYValue(vecB, this.minY);
        Vec3 vec33 = vecA.getIntermediateWithYValue(vecB, this.maxY);
        Vec3 vec34 = vecA.getIntermediateWithZValue(vecB, this.minZ);
        Vec3 vec35 = vecA.getIntermediateWithZValue(vecB, this.maxZ);
        if (!this.isVecInYZ(vec3)) {
            vec3 = null;
        }
        if (!this.isVecInYZ(vec31)) {
            vec31 = null;
        }
        if (!this.isVecInXZ(vec32)) {
            vec32 = null;
        }
        if (!this.isVecInXZ(vec33)) {
            vec33 = null;
        }
        if (!this.isVecInXY(vec34)) {
            vec34 = null;
        }
        if (!this.isVecInXY(vec35)) {
            vec35 = null;
        }
        Vec3 vec36 = null;
        if (vec3 != null) {
            vec36 = vec3;
        }
        if (vec31 != null && (vec36 == null || vecA.squareDistanceTo(vec31) < vecA.squareDistanceTo(vec36))) {
            vec36 = vec31;
        }
        if (vec32 != null && (vec36 == null || vecA.squareDistanceTo(vec32) < vecA.squareDistanceTo(vec36))) {
            vec36 = vec32;
        }
        if (vec33 != null && (vec36 == null || vecA.squareDistanceTo(vec33) < vecA.squareDistanceTo(vec36))) {
            vec36 = vec33;
        }
        if (vec34 != null && (vec36 == null || vecA.squareDistanceTo(vec34) < vecA.squareDistanceTo(vec36))) {
            vec36 = vec34;
        }
        if (vec35 != null && (vec36 == null || vecA.squareDistanceTo(vec35) < vecA.squareDistanceTo(vec36))) {
            vec36 = vec35;
        }
        if (vec36 == null) {
            return null;
        }
        EnumFacing enumfacing = null;
        if (vec36 == vec3) {
            enumfacing = EnumFacing.WEST;
            return new MovingObjectPosition(vec36, enumfacing);
        }
        if (vec36 == vec31) {
            enumfacing = EnumFacing.EAST;
            return new MovingObjectPosition(vec36, enumfacing);
        }
        if (vec36 == vec32) {
            enumfacing = EnumFacing.DOWN;
            return new MovingObjectPosition(vec36, enumfacing);
        }
        if (vec36 == vec33) {
            enumfacing = EnumFacing.UP;
            return new MovingObjectPosition(vec36, enumfacing);
        }
        if (vec36 == vec34) {
            enumfacing = EnumFacing.NORTH;
            return new MovingObjectPosition(vec36, enumfacing);
        }
        enumfacing = EnumFacing.SOUTH;
        return new MovingObjectPosition(vec36, enumfacing);
    }

    private boolean isVecInYZ(Vec3 vec) {
        if (vec == null) {
            return false;
        }
        if (!(vec.yCoord >= this.minY)) return false;
        if (!(vec.yCoord <= this.maxY)) return false;
        if (!(vec.zCoord >= this.minZ)) return false;
        if (!(vec.zCoord <= this.maxZ)) return false;
        return true;
    }

    private boolean isVecInXZ(Vec3 vec) {
        if (vec == null) {
            return false;
        }
        if (!(vec.xCoord >= this.minX)) return false;
        if (!(vec.xCoord <= this.maxX)) return false;
        if (!(vec.zCoord >= this.minZ)) return false;
        if (!(vec.zCoord <= this.maxZ)) return false;
        return true;
    }

    private boolean isVecInXY(Vec3 vec) {
        if (vec == null) {
            return false;
        }
        if (!(vec.xCoord >= this.minX)) return false;
        if (!(vec.xCoord <= this.maxX)) return false;
        if (!(vec.yCoord >= this.minY)) return false;
        if (!(vec.yCoord <= this.maxY)) return false;
        return true;
    }

    public String toString() {
        return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }

    public boolean func_181656_b() {
        if (Double.isNaN(this.minX)) return true;
        if (Double.isNaN(this.minY)) return true;
        if (Double.isNaN(this.minZ)) return true;
        if (Double.isNaN(this.maxX)) return true;
        if (Double.isNaN(this.maxY)) return true;
        if (Double.isNaN(this.maxZ)) return true;
        return false;
    }
}

