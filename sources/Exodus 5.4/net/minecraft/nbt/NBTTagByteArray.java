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

public class NBTTagByteArray
extends NBTBase {
    private byte[] data;

    @Override
    void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(this.data.length);
        dataOutput.write(this.data);
    }

    @Override
    public NBTBase copy() {
        byte[] byArray = new byte[this.data.length];
        System.arraycopy(this.data, 0, byArray, 0, this.data.length);
        return new NBTTagByteArray(byArray);
    }

    public NBTTagByteArray(byte[] byArray) {
        this.data = byArray;
    }

    public byte[] getByteArray() {
        return this.data;
    }

    @Override
    void read(DataInput dataInput, int n, NBTSizeTracker nBTSizeTracker) throws IOException {
        nBTSizeTracker.read(192L);
        int n2 = dataInput.readInt();
        nBTSizeTracker.read(8 * n2);
        this.data = new byte[n2];
        dataInput.readFully(this.data);
    }

    @Override
    public byte getId() {
        return 7;
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object) ? Arrays.equals(this.data, ((NBTTagByteArray)object).data) : false;
    }

    @Override
    public String toString() {
        return "[" + this.data.length + " bytes]";
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ Arrays.hashCode(this.data);
    }

    NBTTagByteArray() {
    }
}

