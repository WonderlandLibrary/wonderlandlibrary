package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;

public class NBTTagLong
extends NBTBase.NBTPrimitive {
    private long data;

    NBTTagLong() {
    }

    public NBTTagLong(long data) {
        this.data = data;
    }

    void write(DataOutput output) throws IOException {
        output.writeLong(this.data);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(128L);
        this.data = input.readLong();
    }

    public byte getId() {
        return 4;
    }

    public String toString() {
        return "" + this.data + "L";
    }

    public NBTBase copy() {
        return new NBTTagLong(this.data);
    }

    public boolean equals(Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            NBTTagLong nbttaglong = (NBTTagLong)((Object)p_equals_1_);
            return this.data == nbttaglong.data;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode() ^ (int)(this.data ^ this.data >>> 32);
    }

    public long getLong() {
        return this.data;
    }

    public int getInt() {
        return (int)(this.data & 0xFFFFFFFFFFFFFFFFL);
    }

    public short getShort() {
        return (short)(this.data & 0xFFFFL);
    }

    public byte getByte() {
        return (byte)(this.data & 0xFFL);
    }

    public double getDouble() {
        return this.data;
    }

    public float getFloat() {
        return this.data;
    }
}
