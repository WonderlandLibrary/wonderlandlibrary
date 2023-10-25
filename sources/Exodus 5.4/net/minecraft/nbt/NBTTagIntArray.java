/*
 * Decompiled with CFR 0.152.
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

    @Override
    public boolean equals(Object object) {
        return super.equals(object) ? Arrays.equals(this.intArray, ((NBTTagIntArray)object).intArray) : false;
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.intArray);
    }

    @Override
    void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.intArray.length);
        int n = 0;
        while (n < this.intArray.length) {
            dataOutput.writeInt(this.intArray[n]);
            ++n;
        }
    }

    @Override
    public byte getId() {
        return 11;
    }

    NBTTagIntArray() {
    }

    public int[] getIntArray() {
        return this.intArray;
    }

    public NBTTagIntArray(int[] nArray) {
        this.intArray = nArray;
    }

    @Override
    void read(DataInput dataInput, int n, NBTSizeTracker nBTSizeTracker) throws IOException {
        nBTSizeTracker.read(192L);
        int n2 = dataInput.readInt();
        nBTSizeTracker.read(32 * n2);
        this.intArray = new int[n2];
        int n3 = 0;
        while (n3 < n2) {
            this.intArray[n3] = dataInput.readInt();
            ++n3;
        }
    }

    @Override
    public NBTBase copy() {
        int[] nArray = new int[this.intArray.length];
        System.arraycopy(this.intArray, 0, nArray, 0, this.intArray.length);
        return new NBTTagIntArray(nArray);
    }

    @Override
    public String toString() {
        String string = "[";
        int[] nArray = this.intArray;
        int n = this.intArray.length;
        int n2 = 0;
        while (n2 < n) {
            int n3 = nArray[n2];
            string = String.valueOf(string) + n3 + ",";
            ++n2;
        }
        return String.valueOf(string) + "]";
    }
}

