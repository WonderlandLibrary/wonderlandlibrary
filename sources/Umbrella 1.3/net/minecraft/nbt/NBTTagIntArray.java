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

public class NBTTagIntArray
extends NBTBase {
    private int[] intArray;
    private static final String __OBFID = "CL_00001221";

    NBTTagIntArray() {
    }

    public NBTTagIntArray(int[] p_i45132_1_) {
        this.intArray = p_i45132_1_;
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeInt(this.intArray.length);
        for (int var2 = 0; var2 < this.intArray.length; ++var2) {
            output.writeInt(this.intArray[var2]);
        }
    }

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        int var4 = input.readInt();
        sizeTracker.read(32 * var4);
        this.intArray = new int[var4];
        for (int var5 = 0; var5 < var4; ++var5) {
            this.intArray[var5] = input.readInt();
        }
    }

    @Override
    public byte getId() {
        return 11;
    }

    @Override
    public String toString() {
        String var1 = "[";
        for (int var5 : this.intArray) {
            var1 = String.valueOf(var1) + var5 + ",";
        }
        return String.valueOf(var1) + "]";
    }

    @Override
    public NBTBase copy() {
        int[] var1 = new int[this.intArray.length];
        System.arraycopy(this.intArray, 0, var1, 0, this.intArray.length);
        return new NBTTagIntArray(var1);
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        return super.equals(p_equals_1_) ? Arrays.equals(this.intArray, ((NBTTagIntArray)p_equals_1_).intArray) : false;
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.intArray);
    }

    public int[] getIntArray() {
        return this.intArray;
    }
}

