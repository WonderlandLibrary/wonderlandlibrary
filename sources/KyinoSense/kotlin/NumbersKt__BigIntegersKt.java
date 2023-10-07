/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import kotlin.Metadata;
import kotlin.NumbersKt__BigDecimalsKt;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=5, xi=1, d1={"\u0000(\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\r\u0010\u0003\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0004\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0005\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\r\u0010\u0006\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u000b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\f\u001a\u00020\rH\u0087\f\u001a\u0015\u0010\u000e\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\f\u001a\u00020\rH\u0087\f\u001a\u0015\u0010\u000f\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0010\u001a\u00020\u0011*\u00020\u0001H\u0087\b\u001a!\u0010\u0010\u001a\u00020\u0011*\u00020\u00012\b\b\u0002\u0010\u0012\u001a\u00020\r2\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u0001*\u00020\rH\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u0001*\u00020\u0016H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0018\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u00a8\u0006\u0019"}, d2={"and", "Ljava/math/BigInteger;", "other", "dec", "div", "inc", "inv", "minus", "or", "plus", "rem", "shl", "n", "", "shr", "times", "toBigDecimal", "Ljava/math/BigDecimal;", "scale", "mathContext", "Ljava/math/MathContext;", "toBigInteger", "", "unaryMinus", "xor", "kotlin-stdlib"}, xs="kotlin/NumbersKt")
class NumbersKt__BigIntegersKt
extends NumbersKt__BigDecimalsKt {
    @InlineOnly
    private static final BigInteger plus(@NotNull BigInteger $this$plus, BigInteger other) {
        int $i$f$plus = 0;
        Intrinsics.checkParameterIsNotNull($this$plus, "$this$plus");
        BigInteger bigInteger = $this$plus.add(other);
        Intrinsics.checkExpressionValueIsNotNull(bigInteger, "this.add(other)");
        return bigInteger;
    }

    @InlineOnly
    private static final BigInteger minus(@NotNull BigInteger $this$minus, BigInteger other) {
        int $i$f$minus = 0;
        Intrinsics.checkParameterIsNotNull($this$minus, "$this$minus");
        BigInteger bigInteger = $this$minus.subtract(other);
        Intrinsics.checkExpressionValueIsNotNull(bigInteger, "this.subtract(other)");
        return bigInteger;
    }

    @InlineOnly
    private static final BigInteger times(@NotNull BigInteger $this$times, BigInteger other) {
        int $i$f$times = 0;
        Intrinsics.checkParameterIsNotNull($this$times, "$this$times");
        BigInteger bigInteger = $this$times.multiply(other);
        Intrinsics.checkExpressionValueIsNotNull(bigInteger, "this.multiply(other)");
        return bigInteger;
    }

    @InlineOnly
    private static final BigInteger div(@NotNull BigInteger $this$div, BigInteger other) {
        int $i$f$div = 0;
        Intrinsics.checkParameterIsNotNull($this$div, "$this$div");
        BigInteger bigInteger = $this$div.divide(other);
        Intrinsics.checkExpressionValueIsNotNull(bigInteger, "this.divide(other)");
        return bigInteger;
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final BigInteger rem(@NotNull BigInteger $this$rem, BigInteger other) {
        int $i$f$rem = 0;
        Intrinsics.checkParameterIsNotNull($this$rem, "$this$rem");
        BigInteger bigInteger = $this$rem.remainder(other);
        Intrinsics.checkExpressionValueIsNotNull(bigInteger, "this.remainder(other)");
        return bigInteger;
    }

    @InlineOnly
    private static final BigInteger unaryMinus(@NotNull BigInteger $this$unaryMinus) {
        int $i$f$unaryMinus = 0;
        Intrinsics.checkParameterIsNotNull($this$unaryMinus, "$this$unaryMinus");
        BigInteger bigInteger = $this$unaryMinus.negate();
        Intrinsics.checkExpressionValueIsNotNull(bigInteger, "this.negate()");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger inc(@NotNull BigInteger $this$inc) {
        int $i$f$inc = 0;
        Intrinsics.checkParameterIsNotNull($this$inc, "$this$inc");
        BigInteger bigInteger = $this$inc.add(BigInteger.ONE);
        Intrinsics.checkExpressionValueIsNotNull(bigInteger, "this.add(BigInteger.ONE)");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger dec(@NotNull BigInteger $this$dec) {
        int $i$f$dec = 0;
        Intrinsics.checkParameterIsNotNull($this$dec, "$this$dec");
        BigInteger bigInteger = $this$dec.subtract(BigInteger.ONE);
        Intrinsics.checkExpressionValueIsNotNull(bigInteger, "this.subtract(BigInteger.ONE)");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger inv(@NotNull BigInteger $this$inv) {
        int $i$f$inv = 0;
        BigInteger bigInteger = $this$inv.not();
        Intrinsics.checkExpressionValueIsNotNull(bigInteger, "this.not()");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger and(@NotNull BigInteger $this$and, BigInteger other) {
        int $i$f$and = 0;
        BigInteger bigInteger = $this$and.and(other);
        Intrinsics.checkExpressionValueIsNotNull(bigInteger, "this.and(other)");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger or(@NotNull BigInteger $this$or, BigInteger other) {
        int $i$f$or = 0;
        BigInteger bigInteger = $this$or.or(other);
        Intrinsics.checkExpressionValueIsNotNull(bigInteger, "this.or(other)");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger xor(@NotNull BigInteger $this$xor, BigInteger other) {
        int $i$f$xor = 0;
        BigInteger bigInteger = $this$xor.xor(other);
        Intrinsics.checkExpressionValueIsNotNull(bigInteger, "this.xor(other)");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger shl(@NotNull BigInteger $this$shl, int n) {
        int $i$f$shl = 0;
        BigInteger bigInteger = $this$shl.shiftLeft(n);
        Intrinsics.checkExpressionValueIsNotNull(bigInteger, "this.shiftLeft(n)");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger shr(@NotNull BigInteger $this$shr, int n) {
        int $i$f$shr = 0;
        BigInteger bigInteger = $this$shr.shiftRight(n);
        Intrinsics.checkExpressionValueIsNotNull(bigInteger, "this.shiftRight(n)");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger toBigInteger(int $this$toBigInteger) {
        int $i$f$toBigInteger = 0;
        BigInteger bigInteger = BigInteger.valueOf($this$toBigInteger);
        Intrinsics.checkExpressionValueIsNotNull(bigInteger, "BigInteger.valueOf(this.toLong())");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger toBigInteger(long $this$toBigInteger) {
        int $i$f$toBigInteger = 0;
        BigInteger bigInteger = BigInteger.valueOf($this$toBigInteger);
        Intrinsics.checkExpressionValueIsNotNull(bigInteger, "BigInteger.valueOf(this)");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(@NotNull BigInteger $this$toBigDecimal) {
        int $i$f$toBigDecimal = 0;
        return new BigDecimal($this$toBigDecimal);
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(@NotNull BigInteger $this$toBigDecimal, int scale, MathContext mathContext) {
        int $i$f$toBigDecimal = 0;
        return new BigDecimal($this$toBigDecimal, scale, mathContext);
    }

    static /* synthetic */ BigDecimal toBigDecimal$default(BigInteger $this$toBigDecimal, int scale, MathContext mathContext, int n, Object object) {
        if ((n & 1) != 0) {
            scale = 0;
        }
        if ((n & 2) != 0) {
            MathContext mathContext2 = MathContext.UNLIMITED;
            Intrinsics.checkExpressionValueIsNotNull(mathContext2, "MathContext.UNLIMITED");
            mathContext = mathContext2;
        }
        boolean $i$f$toBigDecimal = false;
        return new BigDecimal($this$toBigDecimal, scale, mathContext);
    }
}

