/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.MovementInput
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.util.IMovementInput;
import net.ccbluex.liquidbounce.injection.backend.MovementInputImpl;
import net.minecraft.util.MovementInput;

public final class MovementInputImplKt {
    public static final MovementInput unwrap(IMovementInput iMovementInput) {
        boolean bl = false;
        return ((MovementInputImpl)iMovementInput).getWrapped();
    }

    public static final IMovementInput wrap(MovementInput movementInput) {
        boolean bl = false;
        return new MovementInputImpl(movementInput);
    }
}

