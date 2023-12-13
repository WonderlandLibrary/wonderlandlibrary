// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.primitives;

import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtIncompatible;
import java.math.BigInteger;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class UnsignedInteger extends Number implements Comparable<UnsignedInteger>
{
    public static final UnsignedInteger ZERO;
    public static final UnsignedInteger ONE;
    public static final UnsignedInteger MAX_VALUE;
    private final int value;
    
    private UnsignedInteger(final int value) {
        this.value = (value & -1);
    }
    
    public static UnsignedInteger fromIntBits(final int bits) {
        return new UnsignedInteger(bits);
    }
    
    public static UnsignedInteger valueOf(final long value) {
        Preconditions.checkArgument((value & 0xFFFFFFFFL) == value, "value (%s) is outside the range for an unsigned integer value", value);
        return fromIntBits((int)value);
    }
    
    public static UnsignedInteger valueOf(final BigInteger value) {
        Preconditions.checkNotNull(value);
        Preconditions.checkArgument(value.signum() >= 0 && value.bitLength() <= 32, "value (%s) is outside the range for an unsigned integer value", value);
        return fromIntBits(value.intValue());
    }
    
    public static UnsignedInteger valueOf(final String string) {
        return valueOf(string, 10);
    }
    
    public static UnsignedInteger valueOf(final String string, final int radix) {
        return fromIntBits(UnsignedInts.parseUnsignedInt(string, radix));
    }
    
    public UnsignedInteger plus(final UnsignedInteger val) {
        return fromIntBits(this.value + Preconditions.checkNotNull(val).value);
    }
    
    public UnsignedInteger minus(final UnsignedInteger val) {
        return fromIntBits(this.value - Preconditions.checkNotNull(val).value);
    }
    
    @GwtIncompatible
    public UnsignedInteger times(final UnsignedInteger val) {
        return fromIntBits(this.value * Preconditions.checkNotNull(val).value);
    }
    
    public UnsignedInteger dividedBy(final UnsignedInteger val) {
        return fromIntBits(UnsignedInts.divide(this.value, Preconditions.checkNotNull(val).value));
    }
    
    public UnsignedInteger mod(final UnsignedInteger val) {
        return fromIntBits(UnsignedInts.remainder(this.value, Preconditions.checkNotNull(val).value));
    }
    
    @Override
    public int intValue() {
        return this.value;
    }
    
    @Override
    public long longValue() {
        return UnsignedInts.toLong(this.value);
    }
    
    @Override
    public float floatValue() {
        return (float)this.longValue();
    }
    
    @Override
    public double doubleValue() {
        return (double)this.longValue();
    }
    
    public BigInteger bigIntegerValue() {
        return BigInteger.valueOf(this.longValue());
    }
    
    @Override
    public int compareTo(final UnsignedInteger other) {
        Preconditions.checkNotNull(other);
        return UnsignedInts.compare(this.value, other.value);
    }
    
    @Override
    public int hashCode() {
        return this.value;
    }
    
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj instanceof UnsignedInteger) {
            final UnsignedInteger other = (UnsignedInteger)obj;
            return this.value == other.value;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return this.toString(10);
    }
    
    public String toString(final int radix) {
        return UnsignedInts.toString(this.value, radix);
    }
    
    static {
        ZERO = fromIntBits(0);
        ONE = fromIntBits(1);
        MAX_VALUE = fromIntBits(-1);
    }
}
