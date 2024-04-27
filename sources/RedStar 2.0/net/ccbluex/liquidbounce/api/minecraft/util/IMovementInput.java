package net.ccbluex.liquidbounce.api.minecraft.util;

import kotlin.Metadata;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000\n\n\u0000\n\u0000\n\n\b\n\n\b\bf\u000020R0X¦¢\bR0X¦¢\b\b\tR\n0X¦¢\b\t¨\f"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovementInput;", "", "jump", "", "getJump", "()Z", "moveForward", "", "getMoveForward", "()F", "moveStrafe", "getMoveStrafe", "Pride"})
public interface IMovementInput {
    public float getMoveForward();

    public float getMoveStrafe();

    public boolean getJump();
}
