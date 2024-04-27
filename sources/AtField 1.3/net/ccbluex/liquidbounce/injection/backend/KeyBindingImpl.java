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

    public KeyBindingImpl(KeyBinding keyBinding) {
        this.wrapped = keyBinding;
    }

    @Override
    public void setPressed(boolean bl) {
        this.wrapped.field_74513_e = bl;
    }

    @Override
    public boolean isKeyDown() {
        return this.wrapped.func_151470_d();
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof KeyBindingImpl && ((KeyBindingImpl)object).wrapped.equals(this.wrapped);
    }

    @Override
    public int getKeyCode() {
        return this.wrapped.func_151463_i();
    }

    @Override
    public boolean getPressed() {
        return this.wrapped.field_74513_e;
    }

    public final KeyBinding getWrapped() {
        return this.wrapped;
    }

    @Override
    public void onTick(int n) {
        KeyBinding.func_74507_a((int)n);
    }
}

