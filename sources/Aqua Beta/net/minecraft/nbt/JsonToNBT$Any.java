package net.minecraft.nbt;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;

static abstract class JsonToNBT.Any {
    protected String json;

    JsonToNBT.Any() {
    }

    public abstract NBTBase parse() throws NBTException;
}
