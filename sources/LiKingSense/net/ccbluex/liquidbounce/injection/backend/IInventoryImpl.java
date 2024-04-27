/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.inventory.IInventory
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory.IIInventory;
import net.minecraft.inventory.IInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0096\u0002R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/IInventoryImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/inventory/IIInventory;", "wrapped", "Lnet/minecraft/inventory/IInventory;", "(Lnet/minecraft/inventory/IInventory;)V", "name", "", "getName", "()Ljava/lang/String;", "getWrapped", "()Lnet/minecraft/inventory/IInventory;", "equals", "", "other", "", "LiKingSense"})
public final class IInventoryImpl
implements IIInventory {
    @NotNull
    private final IInventory wrapped;

    @Override
    @NotNull
    public String getName() {
        String string = this.wrapped.func_70005_c_();
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.name");
        return string;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof IInventoryImpl && Intrinsics.areEqual((Object)((IInventoryImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final IInventory getWrapped() {
        return this.wrapped;
    }

    public IInventoryImpl(@NotNull IInventory wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

