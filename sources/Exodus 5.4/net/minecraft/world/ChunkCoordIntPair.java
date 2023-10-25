/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world;

import net.minecraft.util.BlockPos;

public class ChunkCoordIntPair {
    public final int chunkXPos;
    public final int chunkZPos;

    public BlockPos getBlock(int n, int n2, int n3) {
        return new BlockPos((this.chunkXPos << 4) + n, n2, (this.chunkZPos << 4) + n3);
    }

    public int hashCode() {
        int n = 1664525 * this.chunkXPos + 1013904223;
        int n2 = 1664525 * (this.chunkZPos ^ 0xDEADBEEF) + 1013904223;
        return n ^ n2;
    }

    public BlockPos getCenterBlock(int n) {
        return new BlockPos(this.getCenterXPos(), n, this.getCenterZPosition());
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof ChunkCoordIntPair)) {
            return false;
        }
        ChunkCoordIntPair chunkCoordIntPair = (ChunkCoordIntPair)object;
        return this.chunkXPos == chunkCoordIntPair.chunkXPos && this.chunkZPos == chunkCoordIntPair.chunkZPos;
    }

    public int getCenterZPosition() {
        return (this.chunkZPos << 4) + 8;
    }

    public String toString() {
        return "[" + this.chunkXPos + ", " + this.chunkZPos + "]";
    }

    public int getXStart() {
        return this.chunkXPos << 4;
    }

    public ChunkCoordIntPair(int n, int n2) {
        this.chunkXPos = n;
        this.chunkZPos = n2;
    }

    public int getZEnd() {
        return (this.chunkZPos << 4) + 15;
    }

    public static long chunkXZ2Int(int n, int n2) {
        return (long)n & 0xFFFFFFFFL | ((long)n2 & 0xFFFFFFFFL) << 32;
    }

    public int getCenterXPos() {
        return (this.chunkXPos << 4) + 8;
    }

    public int getXEnd() {
        return (this.chunkXPos << 4) + 15;
    }

    public int getZStart() {
        return this.chunkZPos << 4;
    }
}

