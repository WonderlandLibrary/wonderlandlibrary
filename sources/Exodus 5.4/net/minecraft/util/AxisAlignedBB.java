/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class AxisAlignedBB {
    public final double minZ;
    public final double minY;
    public final double minX;
    public final double maxZ;
    public final double maxX;
    public final double maxY;

    public AxisAlignedBB(BlockPos blockPos, BlockPos blockPos2) {
        this.minX = blockPos.getX();
        this.minY = blockPos.getY();
        this.minZ = blockPos.getZ();
        this.maxX = blockPos2.getX();
        this.maxY = blockPos2.getY();
        this.maxZ = blockPos2.getZ();
    }

    public AxisAlignedBB addCoord(double d, double d2, double d3) {
        double d4 = this.minX;
        double d5 = this.minY;
        double d6 = this.minZ;
        double d7 = this.maxX;
        double d8 = this.maxY;
        double d9 = this.maxZ;
        if (d < 0.0) {
            d4 += d;
        } else if (d > 0.0) {
            d7 += d;
        }
        if (d2 < 0.0) {
            d5 += d2;
        } else if (d2 > 0.0) {
            d8 += d2;
        }
        if (d3 < 0.0) {
            d6 += d3;
        } else if (d3 > 0.0) {
            d9 += d3;
        }
        return new AxisAlignedBB(d4, d5, d6, d7, d8, d9);
    }

    public AxisAlignedBB contract(double d, double d2, double d3) {
        double d4 = this.minX + d;
        double d5 = this.minY + d2;
        double d6 = this.minZ + d3;
        double d7 = this.maxX - d;
        double d8 = this.maxY - d2;
        double d9 = this.maxZ - d3;
        return new AxisAlignedBB(d4, d5, d6, d7, d8, d9);
    }

    public MovingObjectPosition calculateIntercept(Vec3 vec3, Vec3 vec32) {
        Vec3 vec33 = vec3.getIntermediateWithXValue(vec32, this.minX);
        Vec3 vec34 = vec3.getIntermediateWithXValue(vec32, this.maxX);
        Vec3 vec35 = vec3.getIntermediateWithYValue(vec32, this.minY);
        Vec3 vec36 = vec3.getIntermediateWithYValue(vec32, this.maxY);
        Vec3 vec37 = vec3.getIntermediateWithZValue(vec32, this.minZ);
        Vec3 vec38 = vec3.getIntermediateWithZValue(vec32, this.maxZ);
        if (!this.isVecInYZ(vec33)) {
            vec33 = null;
        }
        if (!this.isVecInYZ(vec34)) {
            vec34 = null;
        }
        if (!this.isVecInXZ(vec35)) {
            vec35 = null;
        }
        if (!this.isVecInXZ(vec36)) {
            vec36 = null;
        }
        if (!this.isVecInXY(vec37)) {
            vec37 = null;
        }
        if (!this.isVecInXY(vec38)) {
            vec38 = null;
        }
        Vec3 vec39 = null;
        if (vec33 != null) {
            vec39 = vec33;
        }
        if (vec34 != null && (vec39 == null || vec3.squareDistanceTo(vec34) < vec3.squareDistanceTo(vec39))) {
            vec39 = vec34;
        }
        if (vec35 != null && (vec39 == null || vec3.squareDistanceTo(vec35) < vec3.squareDistanceTo(vec39))) {
            vec39 = vec35;
        }
        if (vec36 != null && (vec39 == null || vec3.squareDistanceTo(vec36) < vec3.squareDistanceTo(vec39))) {
            vec39 = vec36;
        }
        if (vec37 != null && (vec39 == null || vec3.squareDistanceTo(vec37) < vec3.squareDistanceTo(vec39))) {
            vec39 = vec37;
        }
        if (vec38 != null && (vec39 == null || vec3.squareDistanceTo(vec38) < vec3.squareDistanceTo(vec39))) {
            vec39 = vec38;
        }
        if (vec39 == null) {
            return null;
        }
        EnumFacing enumFacing = null;
        enumFacing = vec39 == vec33 ? EnumFacing.WEST : (vec39 == vec34 ? EnumFacing.EAST : (vec39 == vec35 ? EnumFacing.DOWN : (vec39 == vec36 ? EnumFacing.UP : (vec39 == vec37 ? EnumFacing.NORTH : EnumFacing.SOUTH))));
        return new MovingObjectPosition(vec39, enumFacing);
    }

    public boolean func_181656_b() {
        return Double.isNaN(this.minX) || Double.isNaN(this.minY) || Double.isNaN(this.minZ) || Double.isNaN(this.maxX) || Double.isNaN(this.maxY) || Double.isNaN(this.maxZ);
    }

    public boolean intersectsWith(AxisAlignedBB axisAlignedBB) {
        return axisAlignedBB.maxX > this.minX && axisAlignedBB.minX < this.maxX ? (axisAlignedBB.maxY > this.minY && axisAlignedBB.minY < this.maxY ? axisAlignedBB.maxZ > this.minZ && axisAlignedBB.minZ < this.maxZ : false) : false;
    }

    private boolean isVecInYZ(Vec3 vec3) {
        return vec3 == null ? false : vec3.yCoord >= this.minY && vec3.yCoord <= this.maxY && vec3.zCoord >= this.minZ && vec3.zCoord <= this.maxZ;
    }

    public AxisAlignedBB union(AxisAlignedBB axisAlignedBB) {
        double d = Math.min(this.minX, axisAlignedBB.minX);
        double d2 = Math.min(this.minY, axisAlignedBB.minY);
        double d3 = Math.min(this.minZ, axisAlignedBB.minZ);
        double d4 = Math.max(this.maxX, axisAlignedBB.maxX);
        double d5 = Math.max(this.maxY, axisAlignedBB.maxY);
        double d6 = Math.max(this.maxZ, axisAlignedBB.maxZ);
        return new AxisAlignedBB(d, d2, d3, d4, d5, d6);
    }

    public double getAverageEdgeLength() {
        double d = this.maxX - this.minX;
        double d2 = this.maxY - this.minY;
        double d3 = this.maxZ - this.minZ;
        return (d + d2 + d3) / 3.0;
    }

    public double calculateZOffset(AxisAlignedBB axisAlignedBB, double d) {
        if (axisAlignedBB.maxX > this.minX && axisAlignedBB.minX < this.maxX && axisAlignedBB.maxY > this.minY && axisAlignedBB.minY < this.maxY) {
            double d2;
            if (d > 0.0 && axisAlignedBB.maxZ <= this.minZ) {
                double d3 = this.minZ - axisAlignedBB.maxZ;
                if (d3 < d) {
                    d = d3;
                }
            } else if (d < 0.0 && axisAlignedBB.minZ >= this.maxZ && (d2 = this.maxZ - axisAlignedBB.minZ) > d) {
                d = d2;
            }
            return d;
        }
        return d;
    }

    public AxisAlignedBB expand(double d, double d2, double d3) {
        double d4 = this.minX - d;
        double d5 = this.minY - d2;
        double d6 = this.minZ - d3;
        double d7 = this.maxX + d;
        double d8 = this.maxY + d2;
        double d9 = this.maxZ + d3;
        return new AxisAlignedBB(d4, d5, d6, d7, d8, d9);
    }

    public AxisAlignedBB offset(double d, double d2, double d3) {
        return new AxisAlignedBB(this.minX + d, this.minY + d2, this.minZ + d3, this.maxX + d, this.maxY + d2, this.maxZ + d3);
    }

    public boolean isVecInside(Vec3 vec3) {
        return vec3.xCoord > this.minX && vec3.xCoord < this.maxX ? (vec3.yCoord > this.minY && vec3.yCoord < this.maxY ? vec3.zCoord > this.minZ && vec3.zCoord < this.maxZ : false) : false;
    }

    public String toString() {
        return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }

    public static AxisAlignedBB fromBounds(double d, double d2, double d3, double d4, double d5, double d6) {
        double d7 = Math.min(d, d4);
        double d8 = Math.min(d2, d5);
        double d9 = Math.min(d3, d6);
        double d10 = Math.max(d, d4);
        double d11 = Math.max(d2, d5);
        double d12 = Math.max(d3, d6);
        return new AxisAlignedBB(d7, d8, d9, d10, d11, d12);
    }

    private boolean isVecInXZ(Vec3 vec3) {
        return vec3 == null ? false : vec3.xCoord >= this.minX && vec3.xCoord <= this.maxX && vec3.zCoord >= this.minZ && vec3.zCoord <= this.maxZ;
    }

    public AxisAlignedBB(double d, double d2, double d3, double d4, double d5, double d6) {
        this.minX = Math.min(d, d4);
        this.minY = Math.min(d2, d5);
        this.minZ = Math.min(d3, d6);
        this.maxX = Math.max(d, d4);
        this.maxY = Math.max(d2, d5);
        this.maxZ = Math.max(d3, d6);
    }

    public double calculateXOffset(AxisAlignedBB axisAlignedBB, double d) {
        if (axisAlignedBB.maxY > this.minY && axisAlignedBB.minY < this.maxY && axisAlignedBB.maxZ > this.minZ && axisAlignedBB.minZ < this.maxZ) {
            double d2;
            if (d > 0.0 && axisAlignedBB.maxX <= this.minX) {
                double d3 = this.minX - axisAlignedBB.maxX;
                if (d3 < d) {
                    d = d3;
                }
            } else if (d < 0.0 && axisAlignedBB.minX >= this.maxX && (d2 = this.maxX - axisAlignedBB.minX) > d) {
                d = d2;
            }
            return d;
        }
        return d;
    }

    public double calculateYOffset(AxisAlignedBB axisAlignedBB, double d) {
        if (axisAlignedBB.maxX > this.minX && axisAlignedBB.minX < this.maxX && axisAlignedBB.maxZ > this.minZ && axisAlignedBB.minZ < this.maxZ) {
            double d2;
            if (d > 0.0 && axisAlignedBB.maxY <= this.minY) {
                double d3 = this.minY - axisAlignedBB.maxY;
                if (d3 < d) {
                    d = d3;
                }
            } else if (d < 0.0 && axisAlignedBB.minY >= this.maxY && (d2 = this.maxY - axisAlignedBB.minY) > d) {
                d = d2;
            }
            return d;
        }
        return d;
    }

    private boolean isVecInXY(Vec3 vec3) {
        return vec3 == null ? false : vec3.xCoord >= this.minX && vec3.xCoord <= this.maxX && vec3.yCoord >= this.minY && vec3.yCoord <= this.maxY;
    }
}

