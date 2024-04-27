/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.settings.KeyBinding
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.settings.IKeyBinding;
import net.minecraft.client.settings.KeyBinding;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u0013\u001a\u00020\u00062\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0096\u0002J\u0010\u0010\u0016\u001a\u00020\u00172\u0006\u0010\b\u001a\u00020\tH\u0016R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0007R\u0014\u0010\b\u001a\u00020\t8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR$\u0010\r\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u00068V@VX\u0096\u000e\u00a2\u0006\f\u001a\u0004\b\u000e\u0010\u0007\"\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006\u0018"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/KeyBindingImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/client/settings/IKeyBinding;", "wrapped", "Lnet/minecraft/client/settings/KeyBinding;", "(Lnet/minecraft/client/settings/KeyBinding;)V", "isKeyDown", "", "()Z", "keyCode", "", "getKeyCode", "()I", "value", "pressed", "getPressed", "setPressed", "(Z)V", "getWrapped", "()Lnet/minecraft/client/settings/KeyBinding;", "equals", "other", "", "onTick", "", "LiKingSense"})
public final class KeyBindingImpl
implements IKeyBinding {
    @NotNull
    private final KeyBinding wrapped;

    @Override
    public int getKeyCode() {
        return this.wrapped.func_151463_i();
    }

    @Override
    public boolean getPressed() {
        return this.wrapped.field_74513_e;
    }

    @Override
    public void setPressed(boolean value) {
        this.wrapped.field_74513_e = value;
    }

    @Override
    public boolean isKeyDown() {
        return this.wrapped.func_151470_d();
    }

    @Override
    public void onTick(int keyCode) {
        KeyBinding.func_74507_a((int)keyCode);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof KeyBindingImpl && Intrinsics.areEqual((Object)((KeyBindingImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final KeyBinding getWrapped() {
        return this.wrapped;
    }

    public KeyBindingImpl(@NotNull KeyBinding wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

