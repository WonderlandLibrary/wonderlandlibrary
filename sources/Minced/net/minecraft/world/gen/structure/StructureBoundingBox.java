// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTTagIntArray;
import com.google.common.base.MoreObjects;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.EnumFacing;

public class StructureBoundingBox
{
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;
    
    public StructureBoundingBox() {
    }
    
    public StructureBoundingBox(final int[] coords) {
        if (coords.length == 6) {
            this.minX = coords[0];
            this.minY = coords[1];
            this.minZ = coords[2];
            this.maxX = coords[3];
            this.maxY = coords[4];
            this.maxZ = coords[5];
        }
    }
    
    public static StructureBoundingBox getNewBoundingBox() {
        return new StructureBoundingBox(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }
    
    public static StructureBoundingBox getComponentToAddBoundingBox(final int structureMinX, final int structureMinY, final int structureMinZ, final int xMin, final int yMin, final int zMin, final int xMax, final int yMax, final int zMax, final EnumFacing facing) {
        switch (facing) {
            case NORTH: {
                return new StructureBoundingBox(structureMinX + xMin, structureMinY + yMin, structureMinZ - zMax + 1 + zMin, structureMinX + xMax - 1 + xMin, structureMinY + yMax - 1 + yMin, structureMinZ + zMin);
            }
            case SOUTH: {
                return new StructureBoundingBox(structureMinX + xMin, structureMinY + yMin, structureMinZ + zMin, structureMinX + xMax - 1 + xMin, structureMinY + yMax - 1 + yMin, structureMinZ + zMax - 1 + zMin);
            }
            case WEST: {
                return new StructureBoundingBox(structureMinX - zMax + 1 + zMin, structureMinY + yMin, structureMinZ + xMin, structureMinX + zMin, structureMinY + yMax - 1 + yMin, structureMinZ + xMax - 1 + xMin);
            }
            case EAST: {
                return new StructureBoundingBox(structureMinX + zMin, structureMinY + yMin, structureMinZ + xMin, structureMinX + zMax - 1 + zMin, structureMinY + yMax - 1 + yMin, structureMinZ + xMax - 1 + xMin);
            }
            default: {
                return new StructureBoundingBox(structureMinX + xMin, structureMinY + yMin, structureMinZ + zMin, structureMinX + xMax - 1 + xMin, structureMinY + yMax - 1 + yMin, structureMinZ + zMax - 1 + zMin);
            }
        }
    }
    
    public static StructureBoundingBox createProper(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
        return new StructureBoundingBox(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
    }
    
    public StructureBoundingBox(final StructureBoundingBox structurebb) {
        this.minX = structurebb.minX;
        this.minY = structurebb.minY;
        this.minZ = structurebb.minZ;
        this.maxX = structurebb.maxX;
        this.maxY = structurebb.maxY;
        this.maxZ = structurebb.maxZ;
    }
    
    public StructureBoundingBox(final int xMin, final int yMin, final int zMin, final int xMax, final int yMax, final int zMax) {
        this.minX = xMin;
        this.minY = yMin;
        this.minZ = zMin;
        this.maxX = xMax;
        this.maxY = yMax;
        this.maxZ = zMax;
    }
    
    public StructureBoundingBox(final Vec3i vec1, final Vec3i vec2) {
        this.minX = Math.min(vec1.getX(), vec2.getX());
        this.minY = Math.min(vec1.getY(), vec2.getY());
        this.minZ = Math.min(vec1.getZ(), vec2.getZ());
        this.maxX = Math.max(vec1.getX(), vec2.getX());
        this.maxY = Math.max(vec1.getY(), vec2.getY());
        this.maxZ = Math.max(vec1.getZ(), vec2.getZ());
    }
    
    public StructureBoundingBox(final int xMin, final int zMin, final int xMax, final int zMax) {
        this.minX = xMin;
        this.minZ = zMin;
        this.maxX = xMax;
        this.maxZ = zMax;
        this.minY = 1;
        this.maxY = 512;
    }
    
    public boolean intersectsWith(final StructureBoundingBox structurebb) {
        return this.maxX >= structurebb.minX && this.minX <= structurebb.maxX && this.maxZ >= structurebb.minZ && this.minZ <= structurebb.maxZ && this.maxY >= structurebb.minY && this.minY <= structurebb.maxY;
    }
    
    public boolean intersectsWith(final int minXIn, final int minZIn, final int maxXIn, final int maxZIn) {
        return this.maxX >= minXIn && this.minX <= maxXIn && this.maxZ >= minZIn && this.minZ <= maxZIn;
    }
    
    public void expandTo(final StructureBoundingBox sbb) {
        this.minX = Math.min(this.minX, sbb.minX);
        this.minY = Math.min(this.minY, sbb.minY);
        this.minZ = Math.min(this.minZ, sbb.minZ);
        this.maxX = Math.max(this.maxX, sbb.maxX);
        this.maxY = Math.max(this.maxY, sbb.maxY);
        this.maxZ = Math.max(this.maxZ, sbb.maxZ);
    }
    
    public void offset(final int x, final int y, final int z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
    }
    
    public boolean isVecInside(final Vec3i vec) {
        return vec.getX() >= this.minX && vec.getX() <= this.maxX && vec.getZ() >= this.minZ && vec.getZ() <= this.maxZ && vec.getY() >= this.minY && vec.getY() <= this.maxY;
    }
    
    public Vec3i getLength() {
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
    
    @Override
    public String toString() {
        return MoreObjects.toStringHelper((Object)this).add("x0", this.minX).add("y0", this.minY).add("z0", this.minZ).add("x1", this.maxX).add("y1", this.maxY).add("z1", this.maxZ).toString();
    }
    
    public NBTTagIntArray toNBTTagIntArray() {
        return new NBTTagIntArray(new int[] { this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ });
    }
}
