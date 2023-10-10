// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import kotlin.jvm.internal.Intrinsics;

class StringsKt__RegexExtensionsKt extends CharsKt__CharJVMKt
{
    public static StringBuilder appendln(final StringBuilder $this$appendln) {
        Intrinsics.checkParameterIsNotNull($this$appendln, "$this$appendln");
        final StringBuilder append = $this$appendln.append(SystemProperties.LINE_SEPARATOR);
        Intrinsics.checkExpressionValueIsNotNull(append, "append(SystemProperties.LINE_SEPARATOR)");
        return append;
    }
}
