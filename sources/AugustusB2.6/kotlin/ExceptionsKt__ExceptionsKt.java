// 
// Decompiled by Procyon v0.5.36
// 

package kotlin;

import kotlin.jvm.internal.FunctionBase;
import kotlin.jvm.internal.Lambda;
import kotlin.internal.PlatformImplementations;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.internal.Intrinsics;

public class ExceptionsKt__ExceptionsKt
{
    public static final void addSuppressed(final Throwable $this$addSuppressed, final Throwable exception) {
        Intrinsics.checkParameterIsNotNull($this$addSuppressed, "$this$addSuppressed");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        final PlatformImplementations implementations = PlatformImplementationsKt.IMPLEMENTATIONS;
        PlatformImplementations.addSuppressed($this$addSuppressed, exception);
    }
    
    public String renderLambdaToString(final Lambda lambda) {
        return renderLambdaToString((FunctionBase)lambda);
    }
    
    public static String renderLambdaToString(final FunctionBase lambda) {
        final String result;
        if ((result = lambda.getClass().getGenericInterfaces()[0].toString()).startsWith("kotlin.jvm.functions.")) {
            return result.substring(21);
        }
        return result;
    }
}
