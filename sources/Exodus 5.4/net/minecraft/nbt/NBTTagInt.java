/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;

public class NBTTagInt
extends NBTBase.NBTPrimitive {
    private int data;

    @Override
    public short getShort() {
        return (short)(this.data & 0xFFFF);
    }

    @Override
    void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.data);
    }

    public NBTTagInt(int n) {
        this.data = n;
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ this.data;
    }

    @Override
    public byte getByte() {
        return (byte)(this.data & 0xFF);
    }

    NBTTagInt() {
    }

    @Override
    public String toString() {
        return "" + this.data;
    }

    @Override
    public double getDouble() {
        return this.data;
    }

    @Override
    public byte getId() {
        return 3;
    }

    @Override
    public int getInt() {
        return this.data;
    }

    @Override
    public NBTBase copy() {
        return new NBTTagInt(this.data);
    }

    @Override
    public long getLong() {
        return this.data;
    }

    @Override
    void read(DataInput dataInput, int n, NBTSizeTracker nBTSizeTracker) throws IOException {
        nBTSizeTracker.read(96L);
        this.data = dataInput.readInt();
    }

    @Override
    public float getFloat() {
        return this.data;
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            NBTTagInt nBTTagInt = (NBTTagInt)object;
            return this.data == nBTTagInt.data;
        }
        return false;
    }
}

