/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.value;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.value.IntegerValue;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006B#\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\u0002\u0010\n\u00a8\u0006\u000b"}, d2={"Lnet/ccbluex/liquidbounce/value/BlockValue;", "Lnet/ccbluex/liquidbounce/value/IntegerValue;", "name", "", "value", "", "(Ljava/lang/String;I)V", "displayable", "Lkotlin/Function0;", "", "(Ljava/lang/String;ILkotlin/jvm/functions/Function0;)V", "KyinoClient"})
public final class BlockValue
extends IntegerValue {
    public BlockValue(@NotNull String name, int value, @NotNull Function0<Boolean> displayable) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        Intrinsics.checkParameterIsNotNull(displayable, "displayable");
        super(name, value, 1, 197, displayable);
    }

    public BlockValue(@NotNull String name, int value) {
        Intrinsics.checkParameterIsNotNull(name, "name");
        this(name, value, (Function0<Boolean>)1.INSTANCE);
    }
}

