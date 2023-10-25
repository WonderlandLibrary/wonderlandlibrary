/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.util.MathHelper;

public class NBTTagDouble
extends NBTBase.NBTPrimitive {
    private double data;

    @Override
    public byte getByte() {
        return (byte)(MathHelper.floor_double(this.data) & 0xFF);
    }

    @Override
    void read(DataInput dataInput, int n, NBTSizeTracker nBTSizeTracker) throws IOException {
        nBTSizeTracker.read(128L);
        this.data = dataInput.readDouble();
    }

    @Override
    public short getShort() {
        return (short)(MathHelper.floor_double(this.data) & 0xFFFF);
    }

    @Override
    public long getLong() {
        return (long)Math.floor(this.data);
    }

    public NBTTagDouble(double d) {
        this.data = d;
    }

    @Override
    void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeDouble(this.data);
    }

    @Override
    public double getDouble() {
        return this.data;
    }

    @Override
    public byte getId() {
        return 6;
    }

    @Override
    public NBTBase copy() {
        return new NBTTagDouble(this.data);
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            NBTTagDouble nBTTagDouble = (NBTTagDouble)object;
            return this.data == nBTTagDouble.data;
        }
        return false;
    }

    @Override
    public String toString() {
        return this.data + "d";
    }

    NBTTagDouble() {
    }

    @Override
    public int getInt() {
        return MathHelper.floor_double(this.data);
    }

    @Override
    public int hashCode() {
        long l = Double.doubleToLongBits(this.data);
        return super.hashCode() ^ (int)(l ^ l >>> 32);
    }

    @Override
    public float getFloat() {
        return (float)this.data;
    }
}

