// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

public class AxisAlignedBB
{
    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;
    private static final String __OBFID = "CL_00000607";
    
    public AxisAlignedBB(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }
    
    public AxisAlignedBB(final BlockPos p_i45554_1_, final BlockPos p_i45554_2_) {
        this.minX = p_i45554_1_.getX();
        this.minY = p_i45554_1_.getY();
        this.minZ = p_i45554_1_.getZ();
        this.maxX = p_i45554_2_.getX();
        this.maxY = p_i45554_2_.getY();
        this.maxZ = p_i45554_2_.getZ();
    }
    
    public AxisAlignedBB addCoord(final double x, final double y, final double z) {
        double var7 = this.minX;
        double var8 = this.minY;
        double var9 = this.minZ;
        double var10 = this.maxX;
        double var11 = this.maxY;
        double var12 = this.maxZ;
        if (x < 0.0) {
            var7 += x;
        }
        else if (x > 0.0) {
            var10 += x;
        }
        if (y < 0.0) {
            var8 += y;
        }
        else if (y > 0.0) {
            var11 += y;
        }
        if (z < 0.0) {
            var9 += z;
        }
        else if (z > 0.0) {
            var12 += z;
        }
        return new AxisAlignedBB(var7, var8, var9, var10, var11, var12);
    }
    
    public AxisAlignedBB expand(final double x, final double y, final double z) {
        final double var7 = this.minX - x;
        final double var8 = this.minY - y;
        final double var9 = this.minZ - z;
        final double var10 = this.maxX + x;
        final double var11 = this.maxY + y;
        final double var12 = this.maxZ + z;
        return new AxisAlignedBB(var7, var8, var9, var10, var11, var12);
    }
    
    public AxisAlignedBB union(final AxisAlignedBB other) {
        final double var2 = Math.min(this.minX, other.minX);
        final double var3 = Math.min(this.minY, other.minY);
        final double var4 = Math.min(this.minZ, other.minZ);
        final double var5 = Math.max(this.maxX, other.maxX);
        final double var6 = Math.max(this.maxY, other.maxY);
        final double var7 = Math.max(this.maxZ, other.maxZ);
        return new AxisAlignedBB(var2, var3, var4, var5, var6, var7);
    }
    
    public static AxisAlignedBB fromBounds(final double p_178781_0_, final double p_178781_2_, final double p_178781_4_, final double p_178781_6_, final double p_178781_8_, final double p_178781_10_) {
        final double var12 = Math.min(p_178781_0_, p_178781_6_);
        final double var13 = Math.min(p_178781_2_, p_178781_8_);
        final double var14 = Math.min(p_178781_4_, p_178781_10_);
        final double var15 = Math.max(p_178781_0_, p_178781_6_);
        final double var16 = Math.max(p_178781_2_, p_178781_8_);
        final double var17 = Math.max(p_178781_4_, p_178781_10_);
        return new AxisAlignedBB(var12, var13, var14, var15, var16, var17);
    }
    
    public AxisAlignedBB offset(final double x, final double y, final double z) {
        return new AxisAlignedBB(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }
    
    public double calculateXOffset(final AxisAlignedBB other, double p_72316_2_) {
        if (other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ) {
            if (p_72316_2_ > 0.0 && other.maxX <= this.minX) {
                final double var4 = this.minX - other.maxX;
                if (var4 < p_72316_2_) {
                    p_72316_2_ = var4;
                }
            }
            else if (p_72316_2_ < 0.0 && other.minX >= this.maxX) {
                final double var4 = this.maxX - other.minX;
                if (var4 > p_72316_2_) {
                    p_72316_2_ = var4;
                }
            }
            return p_72316_2_;
        }
        return p_72316_2_;
    }
    
    public double calculateYOffset(final AxisAlignedBB other, double p_72323_2_) {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxZ > this.minZ && other.minZ < this.maxZ) {
            if (p_72323_2_ > 0.0 && other.maxY <= this.minY) {
                final double var4 = this.minY - other.maxY;
                if (var4 < p_72323_2_) {
                    p_72323_2_ = var4;
                }
            }
            else if (p_72323_2_ < 0.0 && other.minY >= this.maxY) {
                final double var4 = this.maxY - other.minY;
                if (var4 > p_72323_2_) {
                    p_72323_2_ = var4;
                }
            }
            return p_72323_2_;
        }
        return p_72323_2_;
    }
    
    public double calculateZOffset(final AxisAlignedBB other, double p_72322_2_) {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY) {
            if (p_72322_2_ > 0.0 && other.maxZ <= this.minZ) {
                final double var4 = this.minZ - other.maxZ;
                if (var4 < p_72322_2_) {
                    p_72322_2_ = var4;
                }
            }
            else if (p_72322_2_ < 0.0 && other.minZ >= this.maxZ) {
                final double var4 = this.maxZ - other.minZ;
                if (var4 > p_72322_2_) {
                    p_72322_2_ = var4;
                }
            }
            return p_72322_2_;
        }
        return p_72322_2_;
    }
    
    public boolean intersectsWith(final AxisAlignedBB other) {
        return other.maxX > this.minX && other.minX < this.maxX && (other.maxY > this.minY && other.minY < this.maxY) && (other.maxZ > this.minZ && other.minZ < this.maxZ);
    }
    
    public boolean isVecInside(final Vec3 vec) {
        return vec.xCoord > this.minX && vec.xCoord < this.maxX && (vec.yCoord > this.minY && vec.yCoord < this.maxY) && (vec.zCoord > this.minZ && vec.zCoord < this.maxZ);
    }
    
    public double getAverageEdgeLength() {
        final double var1 = this.maxX - this.minX;
        final double var2 = this.maxY - this.minY;
        final double var3 = this.maxZ - this.minZ;
        return (var1 + var2 + var3) / 3.0;
    }
    
    public AxisAlignedBB contract(final double x, final double y, final double z) {
        final double var7 = this.minX + x;
        final double var8 = this.minY + y;
        final double var9 = this.minZ + z;
        final double var10 = this.maxX - x;
        final double var11 = this.maxY - y;
        final double var12 = this.maxZ - z;
        return new AxisAlignedBB(var7, var8, var9, var10, var11, var12);
    }
    
    public MovingObjectPosition calculateIntercept(final Vec3 p_72327_1_, final Vec3 p_72327_2_) {
        Vec3 var3 = p_72327_1_.getIntermediateWithXValue(p_72327_2_, this.minX);
        Vec3 var4 = p_72327_1_.getIntermediateWithXValue(p_72327_2_, this.maxX);
        Vec3 var5 = p_72327_1_.getIntermediateWithYValue(p_72327_2_, this.minY);
        Vec3 var6 = p_72327_1_.getIntermediateWithYValue(p_72327_2_, this.maxY);
        Vec3 var7 = p_72327_1_.getIntermediateWithZValue(p_72327_2_, this.minZ);
        Vec3 var8 = p_72327_1_.getIntermediateWithZValue(p_72327_2_, this.maxZ);
        if (!this.isVecInYZ(var3)) {
            var3 = null;
        }
        if (!this.isVecInYZ(var4)) {
            var4 = null;
        }
        if (!this.isVecInXZ(var5)) {
            var5 = null;
        }
        if (!this.isVecInXZ(var6)) {
            var6 = null;
        }
        if (!this.isVecInXY(var7)) {
            var7 = null;
        }
        if (!this.isVecInXY(var8)) {
            var8 = null;
        }
        Vec3 var9 = null;
        if (var3 != null) {
            var9 = var3;
        }
        if (var4 != null && (var9 == null || p_72327_1_.squareDistanceTo(var4) < p_72327_1_.squareDistanceTo(var9))) {
            var9 = var4;
        }
        if (var5 != null && (var9 == null || p_72327_1_.squareDistanceTo(var5) < p_72327_1_.squareDistanceTo(var9))) {
            var9 = var5;
        }
        if (var6 != null && (var9 == null || p_72327_1_.squareDistanceTo(var6) < p_72327_1_.squareDistanceTo(var9))) {
            var9 = var6;
        }
        if (var7 != null && (var9 == null || p_72327_1_.squareDistanceTo(var7) < p_72327_1_.squareDistanceTo(var9))) {
            var9 = var7;
        }
        if (var8 != null && (var9 == null || p_72327_1_.squareDistanceTo(var8) < p_72327_1_.squareDistanceTo(var9))) {
            var9 = var8;
        }
        if (var9 == null) {
            return null;
        }
        EnumFacing var10 = null;
        if (var9 == var3) {
            var10 = EnumFacing.WEST;
        }
        else if (var9 == var4) {
            var10 = EnumFacing.EAST;
        }
        else if (var9 == var5) {
            var10 = EnumFacing.DOWN;
        }
        else if (var9 == var6) {
            var10 = EnumFacing.UP;
        }
        else if (var9 == var7) {
            var10 = EnumFacing.NORTH;
        }
        else {
            var10 = EnumFacing.SOUTH;
        }
        return new MovingObjectPosition(var9, var10);
    }
    
    private boolean isVecInYZ(final Vec3 vec) {
        return vec != null && (vec.yCoord >= this.minY && vec.yCoord <= this.maxY && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ);
    }
    
    private boolean isVecInXZ(final Vec3 vec) {
        return vec != null && (vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ);
    }
    
    private boolean isVecInXY(final Vec3 vec) {
        return vec != null && (vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.yCoord >= this.minY && vec.yCoord <= this.maxY);
    }
    
    @Override
    public String toString() {
        return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }
}
