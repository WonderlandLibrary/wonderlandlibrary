/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
 */
package net.minecraft.world.gen.structure;

import com.google.common.base.Objects;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;

public class StructureBoundingBox {
    public int maxY;
    public int maxX;
    public int minX;
    public int maxZ;
    public int minY;
    public int minZ;

    public StructureBoundingBox(int n, int n2, int n3, int n4, int n5, int n6) {
        this.minX = n;
        this.minY = n2;
        this.minZ = n3;
        this.maxX = n4;
        this.maxY = n5;
        this.maxZ = n6;
    }

    public StructureBoundingBox(Vec3i vec3i, Vec3i vec3i2) {
        this.minX = Math.min(vec3i.getX(), vec3i2.getX());
        this.minY = Math.min(vec3i.getY(), vec3i2.getY());
        this.minZ = Math.min(vec3i.getZ(), vec3i2.getZ());
        this.maxX = Math.max(vec3i.getX(), vec3i2.getX());
        this.maxY = Math.max(vec3i.getY(), vec3i2.getY());
        this.maxZ = Math.max(vec3i.getZ(), vec3i2.getZ());
    }

    public void offset(int n, int n2, int n3) {
        this.minX += n;
        this.minY += n2;
        this.minZ += n3;
        this.maxX += n;
        this.maxY += n2;
        this.maxZ += n3;
    }

    public int getXSize() {
        return this.maxX - this.minX + 1;
    }

    public Vec3i getCenter() {
        return new BlockPos(this.minX + (this.maxX - this.minX + 1) / 2, this.minY + (this.maxY - this.minY + 1) / 2, this.minZ + (this.maxZ - this.minZ + 1) / 2);
    }

    public int getZSize() {
        return this.maxZ - this.minZ + 1;
    }

    public boolean intersectsWith(int n, int n2, int n3, int n4) {
        return this.maxX >= n && this.minX <= n3 && this.maxZ >= n2 && this.minZ <= n4;
    }

    public Vec3i func_175896_b() {
        return new Vec3i(this.maxX - this.minX, this.maxY - this.minY, this.maxZ - this.minZ);
    }

    public static StructureBoundingBox getNewBoundingBox() {
        return new StructureBoundingBox(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public void expandTo(StructureBoundingBox structureBoundingBox) {
        this.minX = Math.min(this.minX, structureBoundingBox.minX);
        this.minY = Math.min(this.minY, structureBoundingBox.minY);
        this.minZ = Math.min(this.minZ, structureBoundingBox.minZ);
        this.maxX = Math.max(this.maxX, structureBoundingBox.maxX);
        this.maxY = Math.max(this.maxY, structureBoundingBox.maxY);
        this.maxZ = Math.max(this.maxZ, structureBoundingBox.maxZ);
    }

    public StructureBoundingBox() {
    }

    public static StructureBoundingBox func_175899_a(int n, int n2, int n3, int n4, int n5, int n6) {
        return new StructureBoundingBox(Math.min(n, n4), Math.min(n2, n5), Math.min(n3, n6), Math.max(n, n4), Math.max(n2, n5), Math.max(n3, n6));
    }

    public String toString() {
        return Objects.toStringHelper((Object)this).add("x0", this.minX).add("y0", this.minY).add("z0", this.minZ).add("x1", this.maxX).add("y1", this.maxY).add("z1", this.maxZ).toString();
    }

    public boolean isVecInside(Vec3i vec3i) {
        return vec3i.getX() >= this.minX && vec3i.getX() <= this.maxX && vec3i.getZ() >= this.minZ && vec3i.getZ() <= this.maxZ && vec3i.getY() >= this.minY && vec3i.getY() <= this.maxY;
    }

    public int getYSize() {
        return this.maxY - this.minY + 1;
    }

    public StructureBoundingBox(StructureBoundingBox structureBoundingBox) {
        this.minX = structureBoundingBox.minX;
        this.minY = structureBoundingBox.minY;
        this.minZ = structureBoundingBox.minZ;
        this.maxX = structureBoundingBox.maxX;
        this.maxY = structureBoundingBox.maxY;
        this.maxZ = structureBoundingBox.maxZ;
    }

    public StructureBoundingBox(int[] nArray) {
        if (nArray.length == 6) {
            this.minX = nArray[0];
            this.minY = nArray[1];
            this.minZ = nArray[2];
            this.maxX = nArray[3];
            this.maxY = nArray[4];
            this.maxZ = nArray[5];
        }
    }

    public StructureBoundingBox(int n, int n2, int n3, int n4) {
        this.minX = n;
        this.minZ = n2;
        this.maxX = n3;
        this.maxZ = n4;
        this.minY = 1;
        this.maxY = 512;
    }

    public static StructureBoundingBox getComponentToAddBoundingBox(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, EnumFacing enumFacing) {
        switch (enumFacing) {
            case NORTH: {
                return new StructureBoundingBox(n + n4, n2 + n5, n3 - n9 + 1 + n6, n + n7 - 1 + n4, n2 + n8 - 1 + n5, n3 + n6);
            }
            case SOUTH: {
                return new StructureBoundingBox(n + n4, n2 + n5, n3 + n6, n + n7 - 1 + n4, n2 + n8 - 1 + n5, n3 + n9 - 1 + n6);
            }
            case WEST: {
                return new StructureBoundingBox(n - n9 + 1 + n6, n2 + n5, n3 + n4, n + n6, n2 + n8 - 1 + n5, n3 + n7 - 1 + n4);
            }
            case EAST: {
                return new StructureBoundingBox(n + n6, n2 + n5, n3 + n4, n + n9 - 1 + n6, n2 + n8 - 1 + n5, n3 + n7 - 1 + n4);
            }
        }
        return new StructureBoundingBox(n + n4, n2 + n5, n3 + n6, n + n7 - 1 + n4, n2 + n8 - 1 + n5, n3 + n9 - 1 + n6);
    }

    public boolean intersectsWith(StructureBoundingBox structureBoundingBox) {
        return this.maxX >= structureBoundingBox.minX && this.minX <= structureBoundingBox.maxX && this.maxZ >= structureBoundingBox.minZ && this.minZ <= structureBoundingBox.maxZ && this.maxY >= structureBoundingBox.minY && this.minY <= structureBoundingBox.maxY;
    }

    public NBTTagIntArray toNBTTagIntArray() {
        return new NBTTagIntArray(new int[]{this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ});
    }
}

