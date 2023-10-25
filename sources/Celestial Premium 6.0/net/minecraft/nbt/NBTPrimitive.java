/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.nbt;

import net.minecraft.nbt.NBTBase;

abstract class NBTPrimitive
extends NBTBase {
    NBTPrimitive() {
    }

    public abstract long getLong();

    public abstract int getInt();

    public abstract short getShort();

    public abstract byte getByte();

    public abstract double getDouble();

    public abstract float getFloat();
}

