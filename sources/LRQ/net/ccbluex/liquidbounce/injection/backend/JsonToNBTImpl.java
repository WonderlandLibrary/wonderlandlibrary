/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.JsonToNBT
 *  net.minecraft.nbt.NBTTagCompound
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.nbt.IJsonToNBT;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagCompound;
import net.ccbluex.liquidbounce.injection.backend.NBTTagCompoundImpl;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;

public final class JsonToNBTImpl
implements IJsonToNBT {
    public static final JsonToNBTImpl INSTANCE;

    @Override
    public INBTTagCompound getTagFromJson(String s) {
        NBTTagCompound $this$wrap$iv = JsonToNBT.func_180713_a((String)s);
        boolean $i$f$wrap = false;
        return new NBTTagCompoundImpl($this$wrap$iv);
    }

    private JsonToNBTImpl() {
    }

    static {
        JsonToNBTImpl jsonToNBTImpl;
        INSTANCE = jsonToNBTImpl = new JsonToNBTImpl();
    }
}

