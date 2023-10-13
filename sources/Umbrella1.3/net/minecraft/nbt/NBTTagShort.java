/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00001227";

    public NBTTagShort() {
    }

    public NBTTagShort(short data) {
        this.data = data;
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeShort(this.data);
    }

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(16L);
        this.data = input.readShort();
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
    public NBTBase copy() {
        return new NBTTagShort(this.data);
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            NBTTagShort var2 = (NBTTagShort)p_equals_1_;
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
        return this.data;
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

