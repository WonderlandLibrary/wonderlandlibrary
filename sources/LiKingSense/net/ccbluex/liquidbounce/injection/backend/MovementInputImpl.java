/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.util.MovementInput
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovementInput;
import net.minecraft.util.MovementInput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010\u0000\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u0011\u001a\u00020\u00062\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0096\u0002R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0014\u0010\t\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u0014\u0010\r\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u0014"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/MovementInputImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/util/IMovementInput;", "wrapped", "Lnet/minecraft/util/MovementInput;", "(Lnet/minecraft/util/MovementInput;)V", "jump", "", "getJump", "()Z", "moveForward", "", "getMoveForward", "()F", "moveStrafe", "getMoveStrafe", "getWrapped", "()Lnet/minecraft/util/MovementInput;", "equals", "other", "", "LiKingSense"})
public final class MovementInputImpl
implements IMovementInput {
    @NotNull
    private final MovementInput wrapped;

    @Override
    public float getMoveForward() {
        return this.wrapped.field_192832_b;
    }

    @Override
    public float getMoveStrafe() {
        return this.wrapped.field_78902_a;
    }

    @Override
    public boolean getJump() {
        return this.wrapped.field_78901_c;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof MovementInputImpl && Intrinsics.areEqual((Object)((MovementInputImpl)other).wrapped, (Object)this.wrapped);
    }

    @NotNull
    public final MovementInput getWrapped() {
        return this.wrapped;
    }

    public MovementInputImpl(@NotNull MovementInput wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

