/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;

public class NBTTagEnd
extends NBTBase {
    @Override
    public byte getId() {
        return 0;
    }

    @Override
    public NBTBase copy() {
        return new NBTTagEnd();
    }

    @Override
    void read(DataInput dataInput, int n, NBTSizeTracker nBTSizeTracker) throws IOException {
        nBTSizeTracker.read(64L);
    }

    @Override
    public String toString() {
        return "END";
    }

    @Override
    void write(DataOutput dataOutput) throws IOException {
    }
}

