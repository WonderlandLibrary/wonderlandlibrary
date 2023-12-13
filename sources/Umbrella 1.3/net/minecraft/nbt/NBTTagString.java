/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;

public class NBTTagString
extends NBTBase {
    private String data;
    private static final String __OBFID = "CL_00001228";

    public NBTTagString() {
        this.data = "";
    }

    public NBTTagString(String data) {
        this.data = data;
        if (data == null) {
            throw new IllegalArgumentException("Empty string not allowed");
        }
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeUTF(this.data);
    }

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        this.data = input.readUTF();
        sizeTracker.read(16 * this.data.length());
    }

    @Override
    public byte getId() {
        return 8;
    }

    @Override
    public String toString() {
        return "\"" + this.data.replace("\"", "\\\"") + "\"";
    }

    @Override
    public NBTBase copy() {
        return new NBTTagString(this.data);
    }

    @Override
    public boolean hasNoTags() {
        return this.data.isEmpty();
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (!super.equals(p_equals_1_)) {
            return false;
        }
        NBTTagString var2 = (NBTTagString)p_equals_1_;
        return this.data == null && var2.data == null || this.data != null && this.data.equals(var2.data);
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ this.data.hashCode();
    }

    @Override
    public String getString() {
        return this.data;
    }
}

