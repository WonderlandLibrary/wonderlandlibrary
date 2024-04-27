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
    public static final MovementInput unwrap(IMovementInput $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((MovementInputImpl)$this$unwrap).getWrapped();
    }

    public static final IMovementInput wrap(MovementInput $this$wrap) {
        int $i$f$wrap = 0;
        return new MovementInputImpl($this$wrap);
    }
}

