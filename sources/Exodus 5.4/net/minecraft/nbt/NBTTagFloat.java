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

public class NBTTagFloat
extends NBTBase.NBTPrimitive {
    private float data;

    @Override
    public byte getId() {
        return 5;
    }

    @Override
    public double getDouble() {
        return this.data;
    }

    @Override
    public short getShort() {
        return (short)(MathHelper.floor_float(this.data) & 0xFFFF);
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ Float.floatToIntBits(this.data);
    }

    @Override
    public byte getByte() {
        return (byte)(MathHelper.floor_float(this.data) & 0xFF);
    }

    @Override
    public boolean equals(Object object) {
        if (super.equals(object)) {
            NBTTagFloat nBTTagFloat = (NBTTagFloat)object;
            return this.data == nBTTagFloat.data;
        }
        return false;
    }

    @Override
    public int getInt() {
        return MathHelper.floor_float(this.data);
    }

    @Override
    void read(DataInput dataInput, int n, NBTSizeTracker nBTSizeTracker) throws IOException {
        nBTSizeTracker.read(96L);
        this.data = dataInput.readFloat();
    }

    @Override
    public String toString() {
        return this.data + "f";
    }

    @Override
    public float getFloat() {
        return this.data;
    }

    NBTTagFloat() {
    }

    @Override
    public long getLong() {
        return (long)this.data;
    }

    @Override
    public NBTBase copy() {
        return new NBTTagFloat(this.data);
    }

    public NBTTagFloat(float f) {
        this.data = f;
    }

    @Override
    void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeFloat(this.data);
    }
}

