/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;

public class NBTTagByteArray
extends NBTBase {
    private byte[] data;
    private static final String __OBFID = "CL_00001213";

    NBTTagByteArray() {
    }

    public NBTTagByteArray(byte[] data) {
        this.data = data;
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeInt(this.data.length);
        output.write(this.data);
    }

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        int var4 = input.readInt();
        sizeTracker.read(8 * var4);
        this.data = new byte[var4];
        input.readFully(this.data);
    }

    @Override
    public byte getId() {
        return 7;
    }

    @Override
    public String toString() {
        return "[" + this.data.length + " bytes]";
    }

    @Override
    public NBTBase copy() {
        byte[] var1 = new byte[this.data.length];
        System.arraycopy(this.data, 0, var1, 0, this.data.length);
        return new NBTTagByteArray(var1);
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        return super.equals(p_equals_1_) ? Arrays.equals(this.data, ((NBTTagByteArray)p_equals_1_).data) : false;
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.data);
    }

    public byte[] getByteArray() {
        return this.data;
    }
}

