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

    NBTTagFloat() {
    }

    public NBTTagFloat(float data) {
        this.data = data;
    }

    void write(DataOutput output) throws IOException {
        output.writeFloat(this.data);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(96L);
        this.data = input.readFloat();
    }

    public byte getId() {
        return 5;
    }

    public String toString() {
        return "" + this.data + "f";
    }

    public NBTBase copy() {
        return new NBTTagFloat(this.data);
    }

    public boolean equals(Object p_equals_1_) {
        if (super.equals(p_equals_1_)) {
            NBTTagFloat nbttagfloat = (NBTTagFloat)((Object)p_equals_1_);
            return this.data == nbttagfloat.data;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode() ^ Float.floatToIntBits((float)this.data);
    }

    public long getLong() {
        return (long)this.data;
    }

    public int getInt() {
        return MathHelper.floor_float((float)this.data);
    }

    public short getShort() {
        return (short)(MathHelper.floor_float((float)this.data) & 0xFFFF);
    }

    public byte getByte() {
        return (byte)(MathHelper.floor_float((float)this.data) & 0xFF);
    }

    public double getDouble() {
        return this.data;
    }

    public float getFloat() {
        return this.data;
    }
}
