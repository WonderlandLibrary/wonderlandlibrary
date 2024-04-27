/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\b\u0002\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0002\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\t\u001a\u00020\u0001*\u00020\nH\u0087\b\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\r\u0010\t\u001a\u00020\u0001*\u00020\rH\u0087\b\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\r2\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\r\u0010\t\u001a\u00020\u0001*\u00020\u000eH\u0087\b\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u000e2\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\r\u0010\t\u001a\u00020\u0001*\u00020\u000fH\u0087\b\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u000f2\u0006\u0010\u000b\u001a\u00020\fH\u0087\b\u001a\r\u0010\u0010\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u00a8\u0006\u0011"}, d2={"dec", "Ljava/math/BigDecimal;", "div", "other", "inc", "minus", "plus", "rem", "times", "toBigDecimal", "", "mathContext", "Ljava/math/MathContext;", "", "", "", "unaryMinus", "kotlin-stdlib"}, xs="kotlin/NumbersKt")
class NumbersKt__BigDecimalsKt {
    @InlineOnly
    private static final BigDecimal plus(BigDecimal $this$plus, BigDecimal other) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        BigDecimal bigDecimal = $this$plus.add(other);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.add(other)");
        return bigDecimal;
    }

    @InlineOnly
    private static final BigDecimal minus(BigDecimal $this$minus, BigDecimal other) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        BigDecimal bigDecimal = $this$minus.subtract(other);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.subtract(other)");
        return bigDecimal;
    }

    @InlineOnly
    private static final BigDecimal times(BigDecimal $this$times, BigDecimal other) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        BigDecimal bigDecimal = $this$times.multiply(other);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.multiply(other)");
        return bigDecimal;
    }

    @InlineOnly
    private static final BigDecimal div(BigDecimal $this$div, BigDecimal other) {
        Intrinsics.checkNotNullParameter($this$div, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        BigDecimal bigDecimal = $this$div.divide(other, RoundingMode.HALF_EVEN);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.divide(other, RoundingMode.HALF_EVEN)");
        return bigDecimal;
    }

    @InlineOnly
    private static final BigDecimal rem(BigDecimal $this$rem, BigDecimal other) {
        Intrinsics.checkNotNullParameter($this$rem, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        BigDecimal bigDecimal = $this$rem.remainder(other);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.remainder(other)");
        return bigDecimal;
    }

    @InlineOnly
    private static final BigDecimal unaryMinus(BigDecimal $this$unaryMinus) {
        Intrinsics.checkNotNullParameter($this$unaryMinus, "<this>");
        BigDecimal bigDecimal = $this$unaryMinus.negate();
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.negate()");
        return bigDecimal;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal inc(BigDecimal $this$inc) {
        Intrinsics.checkNotNullParameter($this$inc, "<this>");
        BigDecimal bigDecimal = $this$inc.add(BigDecimal.ONE);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.add(BigDecimal.ONE)");
        return bigDecimal;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal dec(BigDecimal $this$dec) {
        Intrinsics.checkNotNullParameter($this$dec, "<this>");
        BigDecimal bigDecimal = $this$dec.subtract(BigDecimal.ONE);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.subtract(BigDecimal.ONE)");
        return bigDecimal;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(int $this$toBigDecimal) {
        BigDecimal bigDecimal = BigDecimal.valueOf($this$toBigDecimal);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "valueOf(this.toLong())");
        return bigDecimal;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(int $this$toBigDecimal, MathContext mathContext) {
        Intrinsics.checkNotNullParameter(mathContext, "mathContext");
        return new BigDecimal($this$toBigDecimal, mathContext);
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(long $this$toBigDecimal) {
        BigDecimal bigDecimal = BigDecimal.valueOf($this$toBigDecimal);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "valueOf(this)");
        return bigDecimal;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(long $this$toBigDecimal, MathContext mathContext) {
        Intrinsics.checkNotNullParameter(mathContext, "mathContext");
        return new BigDecimal($this$toBigDecimal, mathContext);
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(float $this$toBigDecimal) {
        return new BigDecimal(String.valueOf($this$toBigDecimal));
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(float $this$toBigDecimal, MathContext mathContext) {
        Intrinsics.checkNotNullParameter(mathContext, "mathContext");
        return new BigDecimal(String.valueOf($this$toBigDecimal), mathContext);
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(double $this$toBigDecimal) {
        return new BigDecimal(String.valueOf($this$toBigDecimal));
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(double $this$toBigDecimal, MathContext mathContext) {
        Intrinsics.checkNotNullParameter(mathContext, "mathContext");
        return new BigDecimal(String.valueOf($this$toBigDecimal), mathContext);
    }
}

