/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin;

import java.io.PrintStream;
import java.io.PrintWriter;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.internal.InlineOnly;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=5, xi=1, d1={"\u0000&\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0012\u0010\b\u001a\u00020\t*\u00020\u00032\u0006\u0010\n\u001a\u00020\u0003\u001a\r\u0010\u000b\u001a\u00020\t*\u00020\u0003H\u0087\b\u001a\u0015\u0010\u000b\u001a\u00020\t*\u00020\u00032\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\u0015\u0010\u000b\u001a\u00020\t*\u00020\u00032\u0006\u0010\u000e\u001a\u00020\u000fH\u0087\b\"!\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u00038F\u00a2\u0006\f\u0012\u0004\b\u0004\u0010\u0005\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0010"}, d2={"stackTrace", "", "Ljava/lang/StackTraceElement;", "", "stackTrace$annotations", "(Ljava/lang/Throwable;)V", "getStackTrace", "(Ljava/lang/Throwable;)[Ljava/lang/StackTraceElement;", "addSuppressed", "", "exception", "printStackTrace", "stream", "Ljava/io/PrintStream;", "writer", "Ljava/io/PrintWriter;", "kotlin-stdlib"}, xs="kotlin/ExceptionsKt")
class ExceptionsKt__ExceptionsKt {
    @InlineOnly
    private static final void printStackTrace(@NotNull Throwable $this$printStackTrace) {
        int $i$f$printStackTrace = 0;
        Throwable throwable = $this$printStackTrace;
        if (throwable == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.Throwable");
        }
        throwable.printStackTrace();
    }

    @InlineOnly
    private static final void printStackTrace(@NotNull Throwable $this$printStackTrace, PrintWriter writer) {
        int $i$f$printStackTrace = 0;
        Throwable throwable = $this$printStackTrace;
        if (throwable == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.Throwable");
        }
        throwable.printStackTrace(writer);
    }

    @InlineOnly
    private static final void printStackTrace(@NotNull Throwable $this$printStackTrace, PrintStream stream) {
        int $i$f$printStackTrace = 0;
        Throwable throwable = $this$printStackTrace;
        if (throwable == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.lang.Throwable");
        }
        throwable.printStackTrace(stream);
    }

    public static /* synthetic */ void stackTrace$annotations(Throwable throwable) {
    }

    @NotNull
    public static final StackTraceElement[] getStackTrace(@NotNull Throwable $this$stackTrace) {
        Intrinsics.checkParameterIsNotNull($this$stackTrace, "$this$stackTrace");
        StackTraceElement[] stackTraceElementArray = $this$stackTrace.getStackTrace();
        if (stackTraceElementArray == null) {
            Intrinsics.throwNpe();
        }
        return stackTraceElementArray;
    }

    public static final void addSuppressed(@NotNull Throwable $this$addSuppressed, @NotNull Throwable exception) {
        Intrinsics.checkParameterIsNotNull($this$addSuppressed, "$this$addSuppressed");
        Intrinsics.checkParameterIsNotNull(exception, "exception");
        PlatformImplementationsKt.IMPLEMENTATIONS.addSuppressed($this$addSuppressed, exception);
    }
}

