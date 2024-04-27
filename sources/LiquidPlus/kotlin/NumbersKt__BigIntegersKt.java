/*
 * Decompiled with CFR 0.152.
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

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000(\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0003\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\r\u0010\u0003\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0004\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0005\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\r\u0010\u0006\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0007\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\t\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\n\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\u0015\u0010\u000b\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\f\u001a\u00020\rH\u0087\f\u001a\u0015\u0010\u000e\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\f\u001a\u00020\rH\u0087\f\u001a\u0015\u0010\u000f\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\n\u001a\r\u0010\u0010\u001a\u00020\u0011*\u00020\u0001H\u0087\b\u001a!\u0010\u0010\u001a\u00020\u0011*\u00020\u00012\b\b\u0002\u0010\u0012\u001a\u00020\r2\b\b\u0002\u0010\u0013\u001a\u00020\u0014H\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u0001*\u00020\rH\u0087\b\u001a\r\u0010\u0015\u001a\u00020\u0001*\u00020\u0016H\u0087\b\u001a\r\u0010\u0017\u001a\u00020\u0001*\u00020\u0001H\u0087\n\u001a\u0015\u0010\u0018\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u00a8\u0006\u0019"}, d2={"and", "Ljava/math/BigInteger;", "other", "dec", "div", "inc", "inv", "minus", "or", "plus", "rem", "shl", "n", "", "shr", "times", "toBigDecimal", "Ljava/math/BigDecimal;", "scale", "mathContext", "Ljava/math/MathContext;", "toBigInteger", "", "unaryMinus", "xor", "kotlin-stdlib"}, xs="kotlin/NumbersKt")
class NumbersKt__BigIntegersKt
extends NumbersKt__BigDecimalsKt {
    @InlineOnly
    private static final BigInteger plus(BigInteger $this$plus, BigInteger other) {
        Intrinsics.checkNotNullParameter($this$plus, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        BigInteger bigInteger = $this$plus.add(other);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "this.add(other)");
        return bigInteger;
    }

    @InlineOnly
    private static final BigInteger minus(BigInteger $this$minus, BigInteger other) {
        Intrinsics.checkNotNullParameter($this$minus, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        BigInteger bigInteger = $this$minus.subtract(other);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "this.subtract(other)");
        return bigInteger;
    }

    @InlineOnly
    private static final BigInteger times(BigInteger $this$times, BigInteger other) {
        Intrinsics.checkNotNullParameter($this$times, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        BigInteger bigInteger = $this$times.multiply(other);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "this.multiply(other)");
        return bigInteger;
    }

    @InlineOnly
    private static final BigInteger div(BigInteger $this$div, BigInteger other) {
        Intrinsics.checkNotNullParameter($this$div, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        BigInteger bigInteger = $this$div.divide(other);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "this.divide(other)");
        return bigInteger;
    }

    @SinceKotlin(version="1.1")
    @InlineOnly
    private static final BigInteger rem(BigInteger $this$rem, BigInteger other) {
        Intrinsics.checkNotNullParameter($this$rem, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        BigInteger bigInteger = $this$rem.remainder(other);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "this.remainder(other)");
        return bigInteger;
    }

    @InlineOnly
    private static final BigInteger unaryMinus(BigInteger $this$unaryMinus) {
        Intrinsics.checkNotNullParameter($this$unaryMinus, "<this>");
        BigInteger bigInteger = $this$unaryMinus.negate();
        Intrinsics.checkNotNullExpressionValue(bigInteger, "this.negate()");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger inc(BigInteger $this$inc) {
        Intrinsics.checkNotNullParameter($this$inc, "<this>");
        BigInteger bigInteger = $this$inc.add(BigInteger.ONE);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "this.add(BigInteger.ONE)");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger dec(BigInteger $this$dec) {
        Intrinsics.checkNotNullParameter($this$dec, "<this>");
        BigInteger bigInteger = $this$dec.subtract(BigInteger.ONE);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "this.subtract(BigInteger.ONE)");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger inv(BigInteger $this$inv) {
        Intrinsics.checkNotNullParameter($this$inv, "<this>");
        BigInteger bigInteger = $this$inv.not();
        Intrinsics.checkNotNullExpressionValue(bigInteger, "this.not()");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger and(BigInteger $this$and, BigInteger other) {
        Intrinsics.checkNotNullParameter($this$and, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        BigInteger bigInteger = $this$and.and(other);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "this.and(other)");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger or(BigInteger $this$or, BigInteger other) {
        Intrinsics.checkNotNullParameter($this$or, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        BigInteger bigInteger = $this$or.or(other);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "this.or(other)");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger xor(BigInteger $this$xor, BigInteger other) {
        Intrinsics.checkNotNullParameter($this$xor, "<this>");
        Intrinsics.checkNotNullParameter(other, "other");
        BigInteger bigInteger = $this$xor.xor(other);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "this.xor(other)");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger shl(BigInteger $this$shl, int n) {
        Intrinsics.checkNotNullParameter($this$shl, "<this>");
        BigInteger bigInteger = $this$shl.shiftLeft(n);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "this.shiftLeft(n)");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger shr(BigInteger $this$shr, int n) {
        Intrinsics.checkNotNullParameter($this$shr, "<this>");
        BigInteger bigInteger = $this$shr.shiftRight(n);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "this.shiftRight(n)");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger toBigInteger(int $this$toBigInteger) {
        BigInteger bigInteger = BigInteger.valueOf($this$toBigInteger);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "valueOf(this.toLong())");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigInteger toBigInteger(long $this$toBigInteger) {
        BigInteger bigInteger = BigInteger.valueOf($this$toBigInteger);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "valueOf(this)");
        return bigInteger;
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(BigInteger $this$toBigDecimal) {
        Intrinsics.checkNotNullParameter($this$toBigDecimal, "<this>");
        return new BigDecimal($this$toBigDecimal);
    }

    @SinceKotlin(version="1.2")
    @InlineOnly
    private static final BigDecimal toBigDecimal(BigInteger $this$toBigDecimal, int scale, MathContext mathContext) {
        Intrinsics.checkNotNullParameter($this$toBigDecimal, "<this>");
        Intrinsics.checkNotNullParameter(mathContext, "mathContext");
        return new BigDecimal($this$toBigDecimal, scale, mathContext);
    }

    static /* synthetic */ BigDecimal toBigDecimal$default(BigInteger $this$toBigDecimal_u24default, int scale, MathContext mathContext, int n, Object object) {
        if ((n & 1) != 0) {
            scale = 0;
        }
        if ((n & 2) != 0) {
            MathContext mathContext2 = MathContext.UNLIMITED;
            Intrinsics.checkNotNullExpressionValue(mathContext2, "UNLIMITED");
            mathContext = mathContext2;
        }
        Intrinsics.checkNotNullParameter($this$toBigDecimal_u24default, "<this>");
        Intrinsics.checkNotNullParameter(mathContext, "mathContext");
        return new BigDecimal($this$toBigDecimal_u24default, scale, mathContext);
    }
}

