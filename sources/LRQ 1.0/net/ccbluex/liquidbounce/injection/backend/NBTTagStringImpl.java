/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagString
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagString;
import net.ccbluex.liquidbounce.injection.backend.NBTBaseImpl;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;

public final class NBTTagStringImpl<T extends NBTTagString>
extends NBTBaseImpl<T>
implements INBTTagString {
    public NBTTagStringImpl(T wrapped) {
        super((NBTBase)wrapped);
    }
}

