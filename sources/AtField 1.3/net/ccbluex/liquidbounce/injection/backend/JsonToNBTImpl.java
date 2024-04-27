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

    private JsonToNBTImpl() {
    }

    @Override
    public INBTTagCompound getTagFromJson(String string) {
        NBTTagCompound nBTTagCompound = JsonToNBT.func_180713_a((String)string);
        boolean bl = false;
        return new NBTTagCompoundImpl(nBTTagCompound);
    }

    static {
        JsonToNBTImpl jsonToNBTImpl;
        INSTANCE = jsonToNBTImpl = new JsonToNBTImpl();
    }
}

