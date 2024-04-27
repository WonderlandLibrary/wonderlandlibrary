/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.Slot
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.inventory.IContainer;
import net.ccbluex.liquidbounce.api.minecraft.inventory.ISlot;
import net.ccbluex.liquidbounce.injection.backend.SlotImpl;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0096\u0002J\u0010\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0006H\u0016R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/ContainerImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/inventory/IContainer;", "wrapped", "Lnet/minecraft/inventory/Container;", "(Lnet/minecraft/inventory/Container;)V", "windowId", "", "getWindowId", "()I", "getWrapped", "()Lnet/minecraft/inventory/Container;", "equals", "", "other", "", "getSlot", "Lnet/ccbluex/liquidbounce/api/minecraft/inventory/ISlot;", "id", "LiKingSense"})
public final class ContainerImpl
implements IContainer {
    @NotNull
    private final Container wrapped;

    @Override
    public int getWindowId() {
        return this.wrapped.field_75152_c;
    }

    @Override
    @NotNull
    public ISlot getSlot(int id) {
        Slot slot = this.wrapped.func_75139_a(id);
        Intrinsics.checkExpressionValueIsNotNull((Object)slot, (String)"wrapped.getSlot(id)");
        Slot $this$wrap$iv = slot;
        boolean $i$f$wrap = false;
        return new SlotImpl($this$wrap$iv);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof ContainerImpl && Intrinsics.areEqual((Object)((ContainerImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final Container getWrapped() {
        return this.wrapped;
    }

    public ContainerImpl(@NotNull Container wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

