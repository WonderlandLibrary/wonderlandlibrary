/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;

public class NBTTagShort
extends NBTBase.NBTPrimitive {
    private short data;

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            NBTTagShort nBTTagShort = (NBTTagShort)object;
            return this.data == nBTTagShort.data;
        }
        return false;
    }

    public NBTTagShort() {
    }

    @Override
    public byte getId() {
        return 2;
    }

    @Override
    public String toString() {
        return this.data + "s";
    }

    @Override
    public short getShort() {
        return this.data;
    }

    @Override
    public double getDouble() {
        return this.data;
    }

    @Override
    public long getLong() {
        return this.data;
    }

    @Override
    void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeShort(this.data);
    }

    @Override
    public float getFloat() {
        return this.data;
    }

    @Override
    public int getInt() {
        return this.data;
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ this.data;
    }

    @Override
    public NBTBase copy() {
        return new NBTTagShort(this.data);
    }

    @Override
    void read(DataInput dataInput, int n, NBTSizeTracker nBTSizeTracker) throws IOException {
        nBTSizeTracker.read(80L);
        this.data = dataInput.readShort();
    }

    @Override
    public byte getByte() {
        return (byte)(this.data & 0xFF);
    }

    public NBTTagShort(short s) {
        this.data = s;
    }
}

