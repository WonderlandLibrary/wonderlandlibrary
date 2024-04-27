/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.MovementInput
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.IMovementInput;
import net.minecraft.util.MovementInput;
import org.jetbrains.annotations.Nullable;

public final class MovementInputImpl
implements IMovementInput {
    private final MovementInput wrapped;

    @Override
    public boolean getJump() {
        return this.wrapped.field_78901_c;
    }

    @Override
    public float getMoveForward() {
        return this.wrapped.field_192832_b;
    }

    @Override
    public float getMoveStrafe() {
        return this.wrapped.field_78902_a;
    }

    public final MovementInput getWrapped() {
        return this.wrapped;
    }

    public MovementInputImpl(MovementInput movementInput) {
        this.wrapped = movementInput;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof MovementInputImpl && ((MovementInputImpl)object).wrapped.equals(this.wrapped);
    }
}

