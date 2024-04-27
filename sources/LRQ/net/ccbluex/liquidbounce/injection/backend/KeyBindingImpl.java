/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.KeyBinding
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.settings.IKeyBinding;
import net.minecraft.client.settings.KeyBinding;
import org.jetbrains.annotations.Nullable;

public final class KeyBindingImpl
implements IKeyBinding {
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
        return other instanceof KeyBindingImpl && ((KeyBindingImpl)other).wrapped.equals(this.wrapped);
    }

    public final KeyBinding getWrapped() {
        return this.wrapped;
    }

    public KeyBindingImpl(KeyBinding wrapped) {
        this.wrapped = wrapped;
    }
}

