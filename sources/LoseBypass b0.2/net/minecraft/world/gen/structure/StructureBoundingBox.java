/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.structure;

import com.google.common.base.Objects;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;

public class StructureBoundingBox {
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;

    public StructureBoundingBox() {
    }

    public StructureBoundingBox(int[] coords) {
        if (coords.length != 6) return;
        this.minX = coords[0];
        this.minY = coords[1];
        this.minZ = coords[2];
        this.maxX = coords[3];
        this.maxY = coords[4];
        this.maxZ = coords[5];
    }

    public static StructureBoundingBox getNewBoundingBox() {
        return new StructureBoundingBox(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public static StructureBoundingBox getComponentToAddBoundingBox(int p_175897_0_, int p_175897_1_, int p_175897_2_, int p_175897_3_, int p_175897_4_, int p_175897_5_, int p_175897_6_, int p_175897_7_, int p_175897_8_, EnumFacing p_175897_9_) {
        switch (1.$SwitchMap$net$minecraft$util$EnumFacing[p_175897_9_.ordinal()]) {
            case 1: {
                return new StructureBoundingBox(p_175897_0_ + p_175897_3_, p_175897_1_ + p_175897_4_, p_175897_2_ - p_175897_8_ + 1 + p_175897_5_, p_175897_0_ + p_175897_6_ - 1 + p_175897_3_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_5_);
            }
            case 2: {
                return new StructureBoundingBox(p_175897_0_ + p_175897_3_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_5_, p_175897_0_ + p_175897_6_ - 1 + p_175897_3_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_8_ - 1 + p_175897_5_);
            }
            case 3: {
                return new StructureBoundingBox(p_175897_0_ - p_175897_8_ + 1 + p_175897_5_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_3_, p_175897_0_ + p_175897_5_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_6_ - 1 + p_175897_3_);
            }
            case 4: {
                return new StructureBoundingBox(p_175897_0_ + p_175897_5_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_3_, p_175897_0_ + p_175897_8_ - 1 + p_175897_5_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_6_ - 1 + p_175897_3_);
            }
        }
        return new StructureBoundingBox(p_175897_0_ + p_175897_3_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_5_, p_175897_0_ + p_175897_6_ - 1 + p_175897_3_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_8_ - 1 + p_175897_5_);
    }

    public static StructureBoundingBox func_175899_a(int p_175899_0_, int p_175899_1_, int p_175899_2_, int p_175899_3_, int p_175899_4_, int p_175899_5_) {
        return new StructureBoundingBox(Math.min(p_175899_0_, p_175899_3_), Math.min(p_175899_1_, p_175899_4_), Math.min(p_175899_2_, p_175899_5_), Math.max(p_175899_0_, p_175899_3_), Math.max(p_175899_1_, p_175899_4_), Math.max(p_175899_2_, p_175899_5_));
    }

    public StructureBoundingBox(StructureBoundingBox structurebb) {
        this.minX = structurebb.minX;
        this.minY = structurebb.minY;
        this.minZ = structurebb.minZ;
        this.maxX = structurebb.maxX;
        this.maxY = structurebb.maxY;
        this.maxZ = structurebb.maxZ;
    }

    public StructureBoundingBox(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax) {
        this.minX = xMin;
        this.minY = yMin;
        this.minZ = zMin;
        this.maxX = xMax;
        this.maxY = yMax;
        this.maxZ = zMax;
    }

    public StructureBoundingBox(Vec3i vec1, Vec3i vec2) {
        this.minX = Math.min(vec1.getX(), vec2.getX());
        this.minY = Math.min(vec1.getY(), vec2.getY());
        this.minZ = Math.min(vec1.getZ(), vec2.getZ());
        this.maxX = Math.max(vec1.getX(), vec2.getX());
        this.maxY = Math.max(vec1.getY(), vec2.getY());
        this.maxZ = Math.max(vec1.getZ(), vec2.getZ());
    }

    public StructureBoundingBox(int xMin, int zMin, int xMax, int zMax) {
        this.minX = xMin;
        this.minZ = zMin;
        this.maxX = xMax;
        this.maxZ = zMax;
        this.minY = 1;
        this.maxY = 512;
    }

    public boolean intersectsWith(StructureBoundingBox structurebb) {
        if (this.maxX < structurebb.minX) return false;
        if (this.minX > structurebb.maxX) return false;
        if (this.maxZ < structurebb.minZ) return false;
        if (this.minZ > structurebb.maxZ) return false;
        if (this.maxY < structurebb.minY) return false;
        if (this.minY > structurebb.maxY) return false;
        return true;
    }

    public boolean intersectsWith(int minXIn, int minZIn, int maxXIn, int maxZIn) {
        if (this.maxX < minXIn) return false;
        if (this.minX > maxXIn) return false;
        if (this.maxZ < minZIn) return false;
        if (this.minZ > maxZIn) return false;
        return true;
    }

    public void expandTo(StructureBoundingBox sbb) {
        this.minX = Math.min(this.minX, sbb.minX);
        this.minY = Math.min(this.minY, sbb.minY);
        this.minZ = Math.min(this.minZ, sbb.minZ);
        this.maxX = Math.max(this.maxX, sbb.maxX);
        this.maxY = Math.max(this.maxY, sbb.maxY);
        this.maxZ = Math.max(this.maxZ, sbb.maxZ);
    }

    public void offset(int x, int y, int z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
    }

    public boolean isVecInside(Vec3i vec) {
        if (vec.getX() < this.minX) return false;
        if (vec.getX() > this.maxX) return false;
        if (vec.getZ() < this.minZ) return false;
        if (vec.getZ() > this.maxZ) return false;
        if (vec.getY() < this.minY) return false;
        if (vec.getY() > this.maxY) return false;
        return true;
    }

    public Vec3i func_175896_b() {
        return new Vec3i(this.maxX - this.minX, this.maxY - this.minY, this.maxZ - this.minZ);
    }

    public int getXSize() {
        return this.maxX - this.minX + 1;
    }

    public int getYSize() {
        return this.maxY - this.minY + 1;
    }

    public int getZSize() {
        return this.maxZ - this.minZ + 1;
    }

    public Vec3i getCenter() {
        return new BlockPos(this.minX + (this.maxX - this.minX + 1) / 2, this.minY + (this.maxY - this.minY + 1) / 2, this.minZ + (this.maxZ - this.minZ + 1) / 2);
    }

    public String toString() {
        return Objects.toStringHelper((Object)this).add("x0", this.minX).add("y0", this.minY).add("z0", this.minZ).add("x1", this.maxX).add("y1", this.maxY).add("z1", this.maxZ).toString();
    }

    public NBTTagIntArray toNBTTagIntArray() {
        return new NBTTagIntArray(new int[]{this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ});
    }
}

