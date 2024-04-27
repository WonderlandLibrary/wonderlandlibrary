/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.nbt.NBTBase
 *  net.minecraft.nbt.NBTTagDouble
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagDouble;
import net.ccbluex.liquidbounce.injection.backend.NBTBaseImpl;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagDouble;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u0016\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00020\u0004B\r\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0006\u00a8\u0006\u0007"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/NBTTagDoubleImpl;", "T", "Lnet/minecraft/nbt/NBTTagDouble;", "Lnet/ccbluex/liquidbounce/injection/backend/NBTBaseImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagDouble;", "wrapped", "(Lnet/minecraft/nbt/NBTTagDouble;)V", "LiKingSense"})
public final class NBTTagDoubleImpl<T extends NBTTagDouble>
extends NBTBaseImpl<T>
implements INBTTagDouble {
    public NBTTagDoubleImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        super((NBTBase)wrapped);
    }
}

