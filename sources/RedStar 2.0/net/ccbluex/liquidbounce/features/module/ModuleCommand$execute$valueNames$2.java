package net.ccbluex.liquidbounce.features.module;

import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import net.ccbluex.liquidbounce.value.Value;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=3, d1={"\u0000\n\u0000\n\n\u0000\n\n\u0000\u000002\n\b0H\n¢\b"}, d2={"<anonymous>", "", "it", "Lnet/ccbluex/liquidbounce/value/Value;", "invoke"})
final class ModuleCommand$execute$valueNames$2
extends Lambda
implements Function1<Value<?>, String> {
    public static final ModuleCommand$execute$valueNames$2 INSTANCE = new /* invalid duplicate definition of identical inner class */;

    @Override
    @NotNull
    public final String invoke(@NotNull Value<?> it) {
        Intrinsics.checkParameterIsNotNull(it, "it");
        String string = it.getName();
        boolean bl = false;
        String string2 = string;
        if (string2 == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
        }
        String string3 = string2.toLowerCase();
        Intrinsics.checkExpressionValueIsNotNull(string3, "(this as java.lang.String).toLowerCase()");
        return string3;
    }

    ModuleCommand$execute$valueNames$2() {
    }
}
