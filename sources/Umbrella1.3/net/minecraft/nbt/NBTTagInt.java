/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00001223";

    NBTTagInt() {
    }

    public NBTTagInt(int data) {
        this.data = data;
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeInt(this.data);
    }

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(32L);
        this.data = input.readInt();
    }

    @Override
    public byte getId() {
        return 3;
    }

    @Override
    public String toString() {
        return "" + this.data;
    }

    @Override
    public NBTBase copy() {
        return new NBTTagInt(this.data);
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            NBTTagInt var2 = (NBTTagInt)p_equals_1_;
            return this.data == var2.data;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ this.data;
    }

    @Override
    public long getLong() {
        return this.data;
    }

    @Override
    public int getInt() {
        return this.data;
    }

    @Override
    public short getShort() {
        return (short)(this.data & 0xFFFF);
    }

    @Override
    public byte getByte() {
        return (byte)(this.data & 0xFF);
    }

    @Override
    public double getDouble() {
        return this.data;
    }

    @Override
    public float getFloat() {
        return this.data;
    }
}

