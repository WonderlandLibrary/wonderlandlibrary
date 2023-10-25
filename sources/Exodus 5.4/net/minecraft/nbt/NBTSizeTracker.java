/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.nbt;

public class NBTSizeTracker {
    private long read;
    public static final NBTSizeTracker INFINITE = new NBTSizeTracker(0L){

        @Override
        public void read(long l) {
        }
    };
    private final long max;

    public void read(long l) {
        this.read += l / 8L;
        if (this.read > this.max) {
            throw new RuntimeException("Tried to read NBT tag that was too big; tried to allocate: " + this.read + "bytes where max allowed: " + this.max);
        }
    }

    public NBTSizeTracker(long l) {
        this.max = l;
    }
}

