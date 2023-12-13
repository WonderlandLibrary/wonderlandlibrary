/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;

public class NBTTagEnd
extends NBTBase {
    private static final String __OBFID = "CL_00001219";

    @Override
    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
    }

    @Override
    void write(DataOutput output) throws IOException {
    }

    @Override
    public byte getId() {
        return 0;
    }

    @Override
    public String toString() {
        return "END";
    }

    @Override
    public NBTBase copy() {
        return new NBTTagEnd();
    }
}

