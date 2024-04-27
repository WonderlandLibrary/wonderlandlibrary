/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.time;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.time.Duration;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u0000.\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0002\u001a\u0010\u0010\t\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0002\u001a\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\n\u001a\u00020\u000bH\u0000\u001a\u0018\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\n\u001a\u00020\u000bH\u0000\"\u0014\u0010\u0000\u001a\u00020\u0001X\u0080\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003\"\u001c\u0010\u0004\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00070\u00060\u0005X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\b\u00a8\u0006\u0011"}, d2={"durationAssertionsEnabled", "", "getDurationAssertionsEnabled", "()Z", "precisionFormats", "", "Ljava/lang/ThreadLocal;", "Ljava/text/DecimalFormat;", "[Ljava/lang/ThreadLocal;", "createFormatForDecimals", "decimals", "", "formatToExactDecimals", "", "value", "", "formatUpToDecimals", "kotlin-stdlib"})
public final class DurationJvmKt {
    private static final boolean durationAssertionsEnabled = Duration.class.desiredAssertionStatus();
    @NotNull
    private static final ThreadLocal<DecimalFormat>[] precisionFormats;

    public static final boolean getDurationAssertionsEnabled() {
        return durationAssertionsEnabled;
    }

    private static final DecimalFormat createFormatForDecimals(int decimals) {
        DecimalFormat decimalFormat;
        DecimalFormat $this$createFormatForDecimals_u24lambda_u2d0 = decimalFormat = new DecimalFormat("0");
        boolean bl = false;
        if (decimals > 0) {
            $this$createFormatForDecimals_u24lambda_u2d0.setMinimumFractionDigits(decimals);
        }
        $this$createFormatForDecimals_u24lambda_u2d0.setRoundingMode(RoundingMode.HALF_UP);
        return decimalFormat;
    }

    @NotNull
    public static final String formatToExactDecimals(double value, int decimals) {
        DecimalFormat decimalFormat;
        ThreadLocal<DecimalFormat> threadLocal;
        if (decimals < precisionFormats.length) {
            DecimalFormat decimalFormat2;
            threadLocal = precisionFormats[decimals];
            DecimalFormat decimalFormat3 = threadLocal.get();
            if (decimalFormat3 == null) {
                DecimalFormat decimalFormat4;
                boolean bl = false;
                DecimalFormat decimalFormat5 = decimalFormat4 = DurationJvmKt.createFormatForDecimals(decimals);
                threadLocal.set(decimalFormat5);
                decimalFormat2 = decimalFormat4;
            } else {
                decimalFormat2 = decimalFormat3;
            }
            decimalFormat = decimalFormat2;
        } else {
            decimalFormat = DurationJvmKt.createFormatForDecimals(decimals);
        }
        DecimalFormat format = decimalFormat;
        threadLocal = format.format(value);
        Intrinsics.checkNotNullExpressionValue(threadLocal, "format.format(value)");
        return threadLocal;
    }

    @NotNull
    public static final String formatUpToDecimals(double value, int decimals) {
        DecimalFormat decimalFormat;
        DecimalFormat $this$formatUpToDecimals_u24lambda_u2d2 = decimalFormat = DurationJvmKt.createFormatForDecimals(0);
        boolean bl = false;
        $this$formatUpToDecimals_u24lambda_u2d2.setMaximumFractionDigits(decimals);
        String string = decimalFormat.format(value);
        Intrinsics.checkNotNullExpressionValue(string, "createFormatForDecimals(\u2026 }\n        .format(value)");
        return string;
    }

    static {
        int n = 0;
        ThreadLocal[] threadLocalArray = new ThreadLocal[4];
        while (n < 4) {
            int n2 = n++;
            threadLocalArray[n2] = new ThreadLocal();
        }
        precisionFormats = threadLocalArray;
    }
}

