/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;

public class NBTTagString
extends NBTBase {
    private String data;

    public NBTTagString() {
        this("");
    }

    public NBTTagString(String data) {
        Objects.requireNonNull(data, "Null string not allowed");
        this.data = data;
    }

    @Override
    void write(DataOutput output) throws IOException {
        output.writeUTF(this.data);
    }

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
        sizeTracker.read(288L);
        this.data = input.readUTF();
        sizeTracker.read(16 * this.data.length());
    }

    @Override
    public byte getId() {
        return 8;
    }

    @Override
    public String toString() {
        return NBTTagString.func_193588_a(this.data);
    }

    @Override
    public NBTTagString copy() {
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
        NBTTagString nbttagstring = (NBTTagString)p_equals_1_;
        return this.data == null && nbttagstring.data == null || Objects.equals(this.data, nbttagstring.data);
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ this.data.hashCode();
    }

    @Override
    public String getString() {
        return this.data;
    }

    public static String func_193588_a(String p_193588_0_) {
        StringBuilder stringbuilder = new StringBuilder("\"");
        for (int i = 0; i < p_193588_0_.length(); ++i) {
            char c0 = p_193588_0_.charAt(i);
            if (c0 == '\\' || c0 == '\"') {
                stringbuilder.append('\\');
            }
            stringbuilder.append(c0);
        }
        return stringbuilder.append('\"').toString();
    }
}

