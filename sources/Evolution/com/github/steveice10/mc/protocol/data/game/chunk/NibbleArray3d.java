/*
 * Decompiled with CFR 0.152.
 */
package com.github.steveice10.mc.protocol.data.game.chunk;

import com.github.steveice10.mc.protocol.util.ObjectUtil;
import com.github.steveice10.packetlib.io.NetInput;
import com.github.steveice10.packetlib.io.NetOutput;
import java.io.IOException;
import java.util.Arrays;

public class NibbleArray3d {
    private byte[] data;

    public NibbleArray3d(int size) {
        this.data = new byte[size >> 1];
    }

    public NibbleArray3d(byte[] array) {
        this.data = array;
    }

    public NibbleArray3d(NetInput in, int size) throws IOException {
        this.data = in.readBytes(size);
    }

    public void write(NetOutput out) throws IOException {
        out.writeBytes(this.data);
    }

    public byte[] getData() {
        return this.data;
    }

    public int get(int x, int y, int z) {
        int key = y << 8 | z << 4 | x;
        int index = key >> 1;
        int part = key & 1;
        return part == 0 ? this.data[index] & 0xF : this.data[index] >> 4 & 0xF;
    }

    public void set(int x, int y, int z, int val) {
        int key = y << 8 | z << 4 | x;
        int index = key >> 1;
        int part = key & 1;
        this.data[index] = part == 0 ? (byte)(this.data[index] & 0xF0 | val & 0xF) : (byte)(this.data[index] & 0xF | (val & 0xF) << 4);
    }

    public void fill(int val) {
        int index = 0;
        while (index < this.data.length << 1) {
            int ind = index >> 1;
            int part = index & 1;
            this.data[ind] = part == 0 ? (byte)(this.data[ind] & 0xF0 | val & 0xF) : (byte)(this.data[ind] & 0xF | (val & 0xF) << 4);
            ++index;
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NibbleArray3d)) {
            return false;
        }
        NibbleArray3d that = (NibbleArray3d)o;
        return Arrays.equals(this.data, that.data);
    }

    public int hashCode() {
        return Arrays.hashCode(this.data);
    }

    public String toString() {
        return ObjectUtil.toString(this);
    }
}

