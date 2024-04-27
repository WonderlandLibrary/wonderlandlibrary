/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.nbt.NBTBase
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\b\u0016\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00028\u0000\u00a2\u0006\u0002\u0010\u0005J\u0013\u0010\t\u001a\u00020\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0096\u0002R\u0013\u0010\u0004\u001a\u00028\u0000\u00a2\u0006\n\n\u0002\u0010\b\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/NBTBaseImpl;", "T", "Lnet/minecraft/nbt/NBTBase;", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTBase;", "wrapped", "(Lnet/minecraft/nbt/NBTBase;)V", "getWrapped", "()Lnet/minecraft/nbt/NBTBase;", "Lnet/minecraft/nbt/NBTBase;", "equals", "", "other", "", "LiKingSense"})
public class NBTBaseImpl<T extends NBTBase>
implements INBTBase {
    @NotNull
    private final T wrapped;

    public boolean equals(@Nullable Object other) {
        return other instanceof NBTBaseImpl && Intrinsics.areEqual(((NBTBaseImpl)other).wrapped, this.wrapped);
    }

    @NotNull
    public final T getWrapped() {
        return this.wrapped;
    }

    public NBTBaseImpl(@NotNull T wrapped) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

