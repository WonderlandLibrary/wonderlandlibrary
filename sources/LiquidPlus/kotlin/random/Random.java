/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.random;

import java.io.Serializable;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.PlatformRandomKt;
import kotlin.random.RandomKt;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\b'\u0018\u0000 \u00172\u00020\u0001:\u0001\u0017B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0004H&J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0016J$\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\b\b\u0002\u0010\u000b\u001a\u00020\u00042\b\b\u0002\u0010\f\u001a\u00020\u0004H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\u0004H\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0004H\u0016J\u0010\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0016J\u0018\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u0016H\u0016J\u0018\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0011\u001a\u00020\u00162\u0006\u0010\u0010\u001a\u00020\u0016H\u0016\u00a8\u0006\u0018"}, d2={"Lkotlin/random/Random;", "", "()V", "nextBits", "", "bitCount", "nextBoolean", "", "nextBytes", "", "array", "fromIndex", "toIndex", "size", "nextDouble", "", "until", "from", "nextFloat", "", "nextInt", "nextLong", "", "Default", "kotlin-stdlib"})
@SinceKotlin(version="1.3")
public abstract class Random {
    @NotNull
    public static final Default Default = new Default(null);
    @NotNull
    private static final Random defaultRandom = PlatformImplementationsKt.IMPLEMENTATIONS.defaultPlatformRandom();

    public abstract int nextBits(int var1);

    public int nextInt() {
        return this.nextBits(32);
    }

    public int nextInt(int until) {
        return this.nextInt(0, until);
    }

    public int nextInt(int from, int until) {
        int rnd;
        boolean bl;
        RandomKt.checkRangeBounds(from, until);
        int n = until - from;
        if (n > 0 || n == Integer.MIN_VALUE) {
            int n2;
            if ((n & -n) == n) {
                int bitCount = RandomKt.fastLog2(n);
                n2 = this.nextBits(bitCount);
            } else {
                int bits;
                int v = 0;
                while ((bits = this.nextInt() >>> 1) - (v = bits % n) + (n - 1) < 0) {
                }
                n2 = v;
            }
            int rnd2 = n2;
            return from + rnd2;
        }
        do {
            if (from <= (rnd = this.nextInt())) {
                if (rnd < until) {
                    bl = true;
                    continue;
                }
                bl = false;
                continue;
            }
            bl = false;
        } while (!bl);
        return rnd;
    }

    public long nextLong() {
        return ((long)this.nextInt() << 32) + (long)this.nextInt();
    }

    public long nextLong(long until) {
        return this.nextLong(0L, until);
    }

    public long nextLong(long from, long until) {
        long rnd;
        boolean bl;
        RandomKt.checkRangeBounds(from, until);
        long n = until - from;
        if (n > 0L) {
            long rnd2 = 0L;
            if ((n & -n) == n) {
                long l;
                int bitCount;
                int nLow = (int)n;
                int nHigh = (int)(n >>> 32);
                if (nLow != 0) {
                    bitCount = RandomKt.fastLog2(nLow);
                    l = (long)this.nextBits(bitCount) & 0xFFFFFFFFL;
                } else if (nHigh == 1) {
                    l = (long)this.nextInt() & 0xFFFFFFFFL;
                } else {
                    bitCount = RandomKt.fastLog2(nHigh);
                    l = ((long)this.nextBits(bitCount) << 32) + ((long)this.nextInt() & 0xFFFFFFFFL);
                }
                rnd2 = l;
            } else {
                long bits;
                long v = 0L;
                while ((bits = this.nextLong() >>> 1) - (v = bits % n) + (n - 1L) < 0L) {
                }
                rnd2 = v;
            }
            return from + rnd2;
        }
        do {
            if (from <= (rnd = this.nextLong())) {
                if (rnd < until) {
                    bl = true;
                    continue;
                }
                bl = false;
                continue;
            }
            bl = false;
        } while (!bl);
        return rnd;
    }

    public boolean nextBoolean() {
        return this.nextBits(1) != 0;
    }

    public double nextDouble() {
        return PlatformRandomKt.doubleFromParts(this.nextBits(26), this.nextBits(27));
    }

    public double nextDouble(double until) {
        return this.nextDouble(0.0, until);
    }

    public double nextDouble(double from, double until) {
        double d;
        double d2;
        RandomKt.checkRangeBounds(from, until);
        double size = until - from;
        if (Double.isInfinite(size) && !Double.isInfinite(d2 = from) && !Double.isNaN(d2) && !Double.isInfinite(d2 = until) && !Double.isNaN(d2)) {
            double r1 = this.nextDouble() * (until / (double)2 - from / (double)2);
            d = from + r1 + r1;
        } else {
            d = from + this.nextDouble() * size;
        }
        double r = d;
        return r >= until ? Math.nextAfter(until, Double.NEGATIVE_INFINITY) : r;
    }

    public float nextFloat() {
        return (float)this.nextBits(24) / 1.6777216E7f;
    }

    /*
     * Unable to fully structure code
     */
    @NotNull
    public byte[] nextBytes(@NotNull byte[] array, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter(array, "array");
        v0 = 0 <= fromIndex ? fromIndex <= array.length : false;
        if (!v0) ** GOTO lbl-1000
        v1 = 0 <= toIndex ? toIndex <= array.length : false;
        if (v1) {
            v2 = true;
        } else lbl-1000:
        // 2 sources

        {
            v2 = var4_4 = false;
        }
        if (!var4_4) {
            $i$a$-require-Random$nextBytes$1 = false;
            $i$a$-require-Random$nextBytes$1 = "fromIndex (" + fromIndex + ") or toIndex (" + toIndex + ") are out of range: 0.." + array.length + '.';
            throw new IllegalArgumentException($i$a$-require-Random$nextBytes$1.toString());
        }
        v3 = var4_4 = fromIndex <= toIndex;
        if (!var4_4) {
            $i$a$-require-Random$nextBytes$2 = false;
            $i$a$-require-Random$nextBytes$2 = "fromIndex (" + fromIndex + ") must be not greater than toIndex (" + toIndex + ").";
            throw new IllegalArgumentException($i$a$-require-Random$nextBytes$2.toString());
        }
        steps = (toIndex - fromIndex) / 4;
        position = 0;
        position = fromIndex;
        var6_10 = 0;
        while (var6_10 < steps) {
            it = var7_11 = var6_10++;
            $i$a$-repeat-Random$nextBytes$3 = false;
            v = this.nextInt();
            array[position] = (byte)v;
            array[position + 1] = (byte)(v >>> 8);
            array[position + 2] = (byte)(v >>> 16);
            array[position + 3] = (byte)(v >>> 24);
            position += 4;
        }
        remainder = toIndex - position;
        vr = this.nextBits(remainder * 8);
        var8_12 = 0;
        while (var8_12 < remainder) {
            i = var8_12++;
            array[position + i] = (byte)(vr >>> i * 8);
        }
        return array;
    }

    public static /* synthetic */ byte[] nextBytes$default(Random random, byte[] byArray, int n, int n2, int n3, Object object) {
        if (object != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: nextBytes");
        }
        if ((n3 & 2) != 0) {
            n = 0;
        }
        if ((n3 & 4) != 0) {
            n2 = byArray.length;
        }
        return random.nextBytes(byArray, n, n2);
    }

    @NotNull
    public byte[] nextBytes(@NotNull byte[] array) {
        Intrinsics.checkNotNullParameter(array, "array");
        return this.nextBytes(array, 0, array.length);
    }

    @NotNull
    public byte[] nextBytes(int size) {
        return this.nextBytes(new byte[size]);
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0012\n\u0002\b\u0005\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u00012\u00060\u0002j\u0002`\u0003:\u0001\u001cB\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0007H\u0016J\b\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\fH\u0016J \u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\f2\u0006\u0010\u000e\u001a\u00020\u00072\u0006\u0010\u000f\u001a\u00020\u0007H\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u0007H\u0016J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0016J\u0018\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0012H\u0016J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u0007H\u0016J\u0010\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0007H\u0016J\u0018\u0010\u0017\u001a\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u0007H\u0016J\b\u0010\u0018\u001a\u00020\u0019H\u0016J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0013\u001a\u00020\u0019H\u0016J\u0018\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0014\u001a\u00020\u00192\u0006\u0010\u0013\u001a\u00020\u0019H\u0016J\b\u0010\u001a\u001a\u00020\u001bH\u0002R\u000e\u0010\u0005\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2={"Lkotlin/random/Random$Default;", "Lkotlin/random/Random;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "()V", "defaultRandom", "nextBits", "", "bitCount", "nextBoolean", "", "nextBytes", "", "array", "fromIndex", "toIndex", "size", "nextDouble", "", "until", "from", "nextFloat", "", "nextInt", "nextLong", "", "writeReplace", "", "Serialized", "kotlin-stdlib"})
    public static final class Default
    extends Random
    implements Serializable {
        private Default() {
        }

        private final Object writeReplace() {
            return Serialized.INSTANCE;
        }

        @Override
        public int nextBits(int bitCount) {
            return defaultRandom.nextBits(bitCount);
        }

        @Override
        public int nextInt() {
            return defaultRandom.nextInt();
        }

        @Override
        public int nextInt(int until) {
            return defaultRandom.nextInt(until);
        }

        @Override
        public int nextInt(int from, int until) {
            return defaultRandom.nextInt(from, until);
        }

        @Override
        public long nextLong() {
            return defaultRandom.nextLong();
        }

        @Override
        public long nextLong(long until) {
            return defaultRandom.nextLong(until);
        }

        @Override
        public long nextLong(long from, long until) {
            return defaultRandom.nextLong(from, until);
        }

        @Override
        public boolean nextBoolean() {
            return defaultRandom.nextBoolean();
        }

        @Override
        public double nextDouble() {
            return defaultRandom.nextDouble();
        }

        @Override
        public double nextDouble(double until) {
            return defaultRandom.nextDouble(until);
        }

        @Override
        public double nextDouble(double from, double until) {
            return defaultRandom.nextDouble(from, until);
        }

        @Override
        public float nextFloat() {
            return defaultRandom.nextFloat();
        }

        @Override
        @NotNull
        public byte[] nextBytes(@NotNull byte[] array) {
            Intrinsics.checkNotNullParameter(array, "array");
            return defaultRandom.nextBytes(array);
        }

        @Override
        @NotNull
        public byte[] nextBytes(int size) {
            return defaultRandom.nextBytes(size);
        }

        @Override
        @NotNull
        public byte[] nextBytes(@NotNull byte[] array, int fromIndex, int toIndex) {
            Intrinsics.checkNotNullParameter(array, "array");
            return defaultRandom.nextBytes(array, fromIndex, toIndex);
        }

        public /* synthetic */ Default(DefaultConstructorMarker $constructor_marker) {
            this();
        }

        @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0000\n\u0000\b\u00c2\u0002\u0018\u00002\u00060\u0001j\u0002`\u0002B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0006\u001a\u00020\u0007H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2={"Lkotlin/random/Random$Default$Serialized;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "()V", "serialVersionUID", "", "readResolve", "", "kotlin-stdlib"})
        private static final class Serialized
        implements Serializable {
            @NotNull
            public static final Serialized INSTANCE = new Serialized();
            private static final long serialVersionUID = 0L;

            private Serialized() {
            }

            private final Object readResolve() {
                return Default;
            }
        }
    }
}

