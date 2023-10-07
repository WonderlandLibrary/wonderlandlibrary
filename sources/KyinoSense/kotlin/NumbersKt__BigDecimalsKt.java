/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=5, xi=1, d1={"\u0000$\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0007\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\b\u0002\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0002\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\n\u001a\u00020\u0001*\u00020\u000bH\u0087\b\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\r\u0010\n\u001a\u00020\u0001*\u00020\u000eH\u0087\b\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u000e2\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\r\u0010\n\u001a\u00020\u0001*\u00020\u000fH\u0087\b\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u000f2\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\r\u0010\n\u001a\u00020\u0001*\u00020\u0010H\u0087\b\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u00102\u0006\u0010\f\u001a\u00020\rH\u0087\b\u001a\r\u0010\u0011\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u00a8\u0006\u0012"}, d2={"dec", "Ljava/math/BigDecimal;", "div", "other", "inc", "minus", "mod", "plus", "rem", "times", "toBigDecimal", "", "mathContext", "Ljava/math/MathContext;", "", "", "", "unaryMinus", "kotlin-stdlib"}, xs="kotlin/NumbersKt")
class NumbersKt__BigDecimalsKt {
    @InlineOnly
    private static final BigDecimal plus(@NotNull BigDecimal $this$plus, BigDecimal other) {
        int $i$f$plus = 0;
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        BigDecimal bigDecimal = $this$plus.add(other);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimal, "this.add(other)");
        return bigDecimal;
    }

    @InlineOnly
    private static final BigDecimal minus(@NotNull BigDecimal $this$minus, BigDecimal other) {
        int $i$f$minus = 0;
        Intrinsics.checkParameterIsNotNull($this$minus, "$this$minus");
        BigDecimal bigDecimal = $this$minus.subtract(other);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimal, "this.subtract(other)");
        return bigDecimal;
    }

    @InlineOnly
    private static final BigDecimal times(@NotNull BigDecimal $this$times, BigDecimal other) {
        int $i$f$times = 0;
        Intrinsics.checkParameterIsNotNull($this$times, "$this$times");
        BigDecimal bigDecimal = $this$times.multiply(other);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimal, "this.multiply(other)");
        return bigDecimal;
    }

    @InlineOnly
    private static final BigDecimal div(@NotNull BigDecimal $this$div, BigDecimal other) {
        int $i$f$div = 0;
        Intrinsics.checkParameterIsNotNull($this$div, "$this$div");
        BigDecimal bigDecimal = $this$div.divide(other, RoundingMode.HALF_EVEN);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimal, "this.divide(other, RoundingMode.HALF_EVEN)");
        return bigDecimal;
    }

    @Deprecated(message="Use rem(other) instead", replaceWith=@ReplaceWith(imports={}, expression="rem(other)"), level=DeprecationLevel.ERROR)
    @InlineOnly
    private static final BigDecimal mod(@NotNull BigDecimal $this$mod, BigDecimal other) {
        int $i$f$mod = 0;
        Intrinsics.checkParameterIsNotNull($this$mod, "$this$mod");
        BigDecimal bigDecimal = $this$mod.remainder(other);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimal, "this.remainder(other)");
        return bigDecimal;
    }

    @InlineOnly
    private static final BigDecimal rem(@NotNull BigDecimal $this$rem, BigDecimal other) {
        int $i$f$rem = 0;
        Intrinsics.checkParameterIsNotNull($this$rem, "$this$rem");
        BigDecimal bigDecimal = $this$rem.remainder(other);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimal, "this.remainder(other)");
        return bigDecimal;
    }

    @InlineOnly
    private static final BigDecimal unaryMinus(@NotNull BigDecimal $this$unaryMinus) {
        int $i$f$unaryMinus = 0;
        Intrinsics.checkParameterIsNotNull($this$unaryMinus, "$this$unaryMinus");
        BigDecimal bigDecimal = $this$unaryMinus.negate();
        Intrinsics.checkExpressionValueIsNotNull(bigDecimal, "this.negate()");
        return bigDecimal;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal inc(@NotNull BigDecimal $this$inc) {
        int $i$f$inc = 0;
        Intrinsics.checkParameterIsNotNull($this$inc, "$this$inc");
        BigDecimal bigDecimal = $this$inc.add(BigDecimal.ONE);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimal, "this.add(BigDecimal.ONE)");
        return bigDecimal;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal dec(@NotNull BigDecimal $this$dec) {
        int $i$f$dec = 0;
        Intrinsics.checkParameterIsNotNull($this$dec, "$this$dec");
        BigDecimal bigDecimal = $this$dec.subtract(BigDecimal.ONE);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimal, "this.subtract(BigDecimal.ONE)");
        return bigDecimal;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(int $this$toBigDecimal) {
        int $i$f$toBigDecimal = 0;
        BigDecimal bigDecimal = BigDecimal.valueOf($this$toBigDecimal);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimal, "BigDecimal.valueOf(this.toLong())");
        return bigDecimal;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(int $this$toBigDecimal, MathContext mathContext) {
        int $i$f$toBigDecimal = 0;
        return new BigDecimal($this$toBigDecimal, mathContext);
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(long $this$toBigDecimal) {
        int $i$f$toBigDecimal = 0;
        BigDecimal bigDecimal = BigDecimal.valueOf($this$toBigDecimal);
        Intrinsics.checkExpressionValueIsNotNull(bigDecimal, "BigDecimal.valueOf(this)");
        return bigDecimal;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(long $this$toBigDecimal, MathContext mathContext) {
        int $i$f$toBigDecimal = 0;
        return new BigDecimal($this$toBigDecimal, mathContext);
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(float $this$toBigDecimal) {
        int $i$f$toBigDecimal = 0;
        return new BigDecimal(String.valueOf($this$toBigDecimal));
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(float $this$toBigDecimal, MathContext mathContext) {
        int $i$f$toBigDecimal = 0;
        return new BigDecimal(String.valueOf($this$toBigDecimal), mathContext);
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(double $this$toBigDecimal) {
        int $i$f$toBigDecimal = 0;
        return new BigDecimal(String.valueOf($this$toBigDecimal));
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(double $this$toBigDecimal, MathContext mathContext) {
        int $i$f$toBigDecimal = 0;
        return new BigDecimal(String.valueOf($this$toBigDecimal), mathContext);
    }
}

