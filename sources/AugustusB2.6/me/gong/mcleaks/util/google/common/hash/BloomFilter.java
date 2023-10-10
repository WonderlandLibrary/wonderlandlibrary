// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.hash;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import me.gong.mcleaks.util.google.common.primitives.UnsignedBytes;
import me.gong.mcleaks.util.google.common.primitives.SignedBytes;
import java.io.DataOutputStream;
import java.io.OutputStream;
import me.gong.mcleaks.util.google.common.base.Objects;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.VisibleForTesting;
import me.gong.mcleaks.util.google.errorprone.annotations.CanIgnoreReturnValue;
import me.gong.mcleaks.util.google.common.base.Preconditions;
import me.gong.mcleaks.util.google.common.annotations.Beta;
import java.io.Serializable;
import me.gong.mcleaks.util.google.common.base.Predicate;

@Beta
public final class BloomFilter<T> implements Predicate<T>, Serializable
{
    private final BloomFilterStrategies.BitArray bits;
    private final int numHashFunctions;
    private final Funnel<? super T> funnel;
    private final Strategy strategy;
    
    private BloomFilter(final BloomFilterStrategies.BitArray bits, final int numHashFunctions, final Funnel<? super T> funnel, final Strategy strategy) {
        Preconditions.checkArgument(numHashFunctions > 0, "numHashFunctions (%s) must be > 0", numHashFunctions);
        Preconditions.checkArgument(numHashFunctions <= 255, "numHashFunctions (%s) must be <= 255", numHashFunctions);
        this.bits = Preconditions.checkNotNull(bits);
        this.numHashFunctions = numHashFunctions;
        this.funnel = Preconditions.checkNotNull(funnel);
        this.strategy = Preconditions.checkNotNull(strategy);
    }
    
    public BloomFilter<T> copy() {
        return new BloomFilter<T>(this.bits.copy(), this.numHashFunctions, this.funnel, this.strategy);
    }
    
    public boolean mightContain(final T object) {
        return this.strategy.mightContain(object, this.funnel, this.numHashFunctions, this.bits);
    }
    
    @Deprecated
    @Override
    public boolean apply(final T input) {
        return this.mightContain(input);
    }
    
    @CanIgnoreReturnValue
    public boolean put(final T object) {
        return this.strategy.put(object, this.funnel, this.numHashFunctions, this.bits);
    }
    
    public double expectedFpp() {
        return Math.pow(this.bits.bitCount() / (double)this.bitSize(), this.numHashFunctions);
    }
    
    @VisibleForTesting
    long bitSize() {
        return this.bits.bitSize();
    }
    
    public boolean isCompatible(final BloomFilter<T> that) {
        Preconditions.checkNotNull(that);
        return this != that && this.numHashFunctions == that.numHashFunctions && this.bitSize() == that.bitSize() && this.strategy.equals(that.strategy) && this.funnel.equals(that.funnel);
    }
    
    public void putAll(final BloomFilter<T> that) {
        Preconditions.checkNotNull(that);
        Preconditions.checkArgument(this != that, (Object)"Cannot combine a BloomFilter with itself.");
        Preconditions.checkArgument(this.numHashFunctions == that.numHashFunctions, "BloomFilters must have the same number of hash functions (%s != %s)", this.numHashFunctions, that.numHashFunctions);
        Preconditions.checkArgument(this.bitSize() == that.bitSize(), "BloomFilters must have the same size underlying bit arrays (%s != %s)", this.bitSize(), that.bitSize());
        Preconditions.checkArgument(this.strategy.equals(that.strategy), "BloomFilters must have equal strategies (%s != %s)", this.strategy, that.strategy);
        Preconditions.checkArgument(this.funnel.equals(that.funnel), "BloomFilters must have equal funnels (%s != %s)", this.funnel, that.funnel);
        this.bits.putAll(that.bits);
    }
    
    @Override
    public boolean equals(@Nullable final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof BloomFilter) {
            final BloomFilter<?> that = (BloomFilter<?>)object;
            return this.numHashFunctions == that.numHashFunctions && this.funnel.equals(that.funnel) && this.bits.equals(that.bits) && this.strategy.equals(that.strategy);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.numHashFunctions, this.funnel, this.strategy, this.bits);
    }
    
    public static <T> BloomFilter<T> create(final Funnel<? super T> funnel, final int expectedInsertions, final double fpp) {
        return create(funnel, (long)expectedInsertions, fpp);
    }
    
    public static <T> BloomFilter<T> create(final Funnel<? super T> funnel, final long expectedInsertions, final double fpp) {
        return create(funnel, expectedInsertions, fpp, (Strategy)BloomFilterStrategies.MURMUR128_MITZ_64);
    }
    
    @VisibleForTesting
    static <T> BloomFilter<T> create(final Funnel<? super T> funnel, long expectedInsertions, final double fpp, final Strategy strategy) {
        Preconditions.checkNotNull(funnel);
        Preconditions.checkArgument(expectedInsertions >= 0L, "Expected insertions (%s) must be >= 0", expectedInsertions);
        Preconditions.checkArgument(fpp > 0.0, "False positive probability (%s) must be > 0.0", (Object)fpp);
        Preconditions.checkArgument(fpp < 1.0, "False positive probability (%s) must be < 1.0", (Object)fpp);
        Preconditions.checkNotNull(strategy);
        if (expectedInsertions == 0L) {
            expectedInsertions = 1L;
        }
        final long numBits = optimalNumOfBits(expectedInsertions, fpp);
        final int numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, numBits);
        try {
            return new BloomFilter<T>(new BloomFilterStrategies.BitArray(numBits), numHashFunctions, funnel, strategy);
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Could not create BloomFilter of " + numBits + " bits", e);
        }
    }
    
    public static <T> BloomFilter<T> create(final Funnel<? super T> funnel, final int expectedInsertions) {
        return create(funnel, (long)expectedInsertions);
    }
    
    public static <T> BloomFilter<T> create(final Funnel<? super T> funnel, final long expectedInsertions) {
        return create(funnel, expectedInsertions, 0.03);
    }
    
    @VisibleForTesting
    static int optimalNumOfHashFunctions(final long n, final long m) {
        return Math.max(1, (int)Math.round(m / (double)n * Math.log(2.0)));
    }
    
    @VisibleForTesting
    static long optimalNumOfBits(final long n, double p) {
        if (p == 0.0) {
            p = Double.MIN_VALUE;
        }
        return (long)(-n * Math.log(p) / (Math.log(2.0) * Math.log(2.0)));
    }
    
    private Object writeReplace() {
        return new SerialForm((BloomFilter<Object>)this);
    }
    
    public void writeTo(final OutputStream out) throws IOException {
        final DataOutputStream dout = new DataOutputStream(out);
        dout.writeByte(SignedBytes.checkedCast(this.strategy.ordinal()));
        dout.writeByte(UnsignedBytes.checkedCast(this.numHashFunctions));
        dout.writeInt(this.bits.data.length);
        for (final long value : this.bits.data) {
            dout.writeLong(value);
        }
    }
    
    public static <T> BloomFilter<T> readFrom(final InputStream in, final Funnel<T> funnel) throws IOException {
        Preconditions.checkNotNull(in, (Object)"InputStream");
        Preconditions.checkNotNull(funnel, (Object)"Funnel");
        int strategyOrdinal = -1;
        int numHashFunctions = -1;
        int dataLength = -1;
        try {
            final DataInputStream din = new DataInputStream(in);
            strategyOrdinal = din.readByte();
            numHashFunctions = UnsignedBytes.toInt(din.readByte());
            dataLength = din.readInt();
            final Strategy strategy = BloomFilterStrategies.values()[strategyOrdinal];
            final long[] data = new long[dataLength];
            for (int i = 0; i < data.length; ++i) {
                data[i] = din.readLong();
            }
            return new BloomFilter<T>(new BloomFilterStrategies.BitArray(data), numHashFunctions, funnel, strategy);
        }
        catch (RuntimeException e) {
            final String message = "Unable to deserialize BloomFilter from InputStream. strategyOrdinal: " + strategyOrdinal + " numHashFunctions: " + numHashFunctions + " dataLength: " + dataLength;
            throw new IOException(message, e);
        }
    }
    
    private static class SerialForm<T> implements Serializable
    {
        final long[] data;
        final int numHashFunctions;
        final Funnel<? super T> funnel;
        final Strategy strategy;
        private static final long serialVersionUID = 1L;
        
        SerialForm(final BloomFilter<T> bf) {
            this.data = ((BloomFilter<Object>)bf).bits.data;
            this.numHashFunctions = ((BloomFilter<Object>)bf).numHashFunctions;
            this.funnel = ((BloomFilter<Object>)bf).funnel;
            this.strategy = ((BloomFilter<Object>)bf).strategy;
        }
        
        Object readResolve() {
            return new BloomFilter(new BloomFilterStrategies.BitArray(this.data), this.numHashFunctions, this.funnel, this.strategy, null);
        }
    }
    
    interface Strategy extends Serializable
    {
         <T> boolean put(final T p0, final Funnel<? super T> p1, final int p2, final BloomFilterStrategies.BitArray p3);
        
         <T> boolean mightContain(final T p0, final Funnel<? super T> p1, final int p2, final BloomFilterStrategies.BitArray p3);
        
        int ordinal();
    }
}
