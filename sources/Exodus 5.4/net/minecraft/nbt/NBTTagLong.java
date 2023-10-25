/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;

public class NBTTagLong
extends NBTBase.NBTPrimitive {
    private long data;

    @Override
    void read(DataInput dataInput, int n, NBTSizeTracker nBTSizeTracker) throws IOException {
        nBTSizeTracker.read(128L);
        this.data = dataInput.readLong();
    }

    @Override
    void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(this.data);
    }

    @Override
    public String toString() {
        return this.data + "L";
    }

    @Override
    public int getInt() {
        return (int)(this.data & 0xFFFFFFFFFFFFFFFFL);
    }

    @Override
    public byte getId() {
        return 4;
    }

    @Override
    public short getShort() {
        return (short)(this.data & 0xFFFFL);
    }

    @Override
    public float getFloat() {
        return this.data;
    }

    @Override
    public NBTBase copy() {
        return new NBTTagLong(this.data);
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ (int)(this.data ^ this.data >>> 32);
    }

    NBTTagLong() {
    }

    public NBTTagLong(long l) {
        this.data = l;
    }

    @Override
    public byte getByte() {
        return (byte)(this.data & 0xFFL);
    }

    @Override
    public double getDouble() {
        return this.data;
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            NBTTagLong nBTTagLong = (NBTTagLong)object;
            return this.data == nBTTagLong.data;
        }
        return false;
    }

    @Override
    public long getLong() {
        return this.data;
    }
}

