/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagDouble
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagDouble;
import net.ccbluex.liquidbounce.injection.backend.NBTBaseImpl;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;

public final class NBTTagDoubleImpl
extends NBTBaseImpl
implements INBTTagDouble {
    public NBTTagDoubleImpl(NBTTagDouble nBTTagDouble) {
        super((NBTBase)nBTTagDouble);
    }
}

