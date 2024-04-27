/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import kotlin.Metadata;
import kotlin.NumbersKt__BigIntegersKt;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000 \n\u0000\n\u0002\u0010\b\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\t\n\u0002\u0010\n\n\u0000\n\u0002\u0010\u0006\n\u0002\u0010\u0007\n\u0000\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0005*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\bH\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0007*\u00020\b2\u0006\u0010\u0003\u001a\u00020\u0007H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\b*\u00020\b2\u0006\u0010\u0003\u001a\u00020\bH\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0005*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0005*\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0002*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0002H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0001H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0004*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0004H\u0087\b\u001a\u0015\u0010\u0006\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005H\u0087\b\u00a8\u0006\t"}, d2={"floorDiv", "", "", "other", "", "", "mod", "", "", "kotlin-stdlib"}, xs="kotlin/NumbersKt")
class NumbersKt__FloorDivModKt
extends NumbersKt__BigIntegersKt {
    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int floorDiv(byte $this$floorDiv, byte other) {
        byte by = $this$floorDiv;
        byte by2 = other;
        int n = by / by2;
        if ((by ^ by2) < 0 && n * by2 != by) {
            int n2 = n;
            n = n2 + -1;
        }
        return n;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final byte mod(byte $this$mod, byte other) {
        byte by = $this$mod;
        byte by2 = other;
        int n = by % by2;
        return (byte)(n + (by2 & ((n ^ by2) & (n | -n)) >> 31));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int floorDiv(byte $this$floorDiv, short other) {
        byte by = $this$floorDiv;
        short s = other;
        int n = by / s;
        if ((by ^ s) < 0 && n * s != by) {
            int n2 = n;
            n = n2 + -1;
        }
        return n;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final short mod(byte $this$mod, short other) {
        byte by = $this$mod;
        short s = other;
        int n = by % s;
        return (short)(n + (s & ((n ^ s) & (n | -n)) >> 31));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int floorDiv(byte $this$floorDiv, int other) {
        byte by = $this$floorDiv;
        int n = by / other;
        if ((by ^ other) < 0 && n * other != by) {
            int n2 = n;
            n = n2 + -1;
        }
        return n;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int mod(byte $this$mod, int other) {
        byte by = $this$mod;
        int n = by % other;
        return n + (other & ((n ^ other) & (n | -n)) >> 31);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long floorDiv(byte $this$floorDiv, long other) {
        long l = $this$floorDiv;
        long l2 = l / other;
        if ((l ^ other) < 0L && l2 * other != l) {
            long l3 = l2;
            l2 = l3 + -1L;
        }
        return l2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long mod(byte $this$mod, long other) {
        long l = $this$mod;
        long l2 = l % other;
        return l2 + (other & ((l2 ^ other) & (l2 | -l2)) >> 63);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int floorDiv(short $this$floorDiv, byte other) {
        short s = $this$floorDiv;
        byte by = other;
        int n = s / by;
        if ((s ^ by) < 0 && n * by != s) {
            int n2 = n;
            n = n2 + -1;
        }
        return n;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final byte mod(short $this$mod, byte other) {
        short s = $this$mod;
        byte by = other;
        int n = s % by;
        return (byte)(n + (by & ((n ^ by) & (n | -n)) >> 31));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int floorDiv(short $this$floorDiv, short other) {
        short s = $this$floorDiv;
        short s2 = other;
        int n = s / s2;
        if ((s ^ s2) < 0 && n * s2 != s) {
            int n2 = n;
            n = n2 + -1;
        }
        return n;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final short mod(short $this$mod, short other) {
        short s = $this$mod;
        short s2 = other;
        int n = s % s2;
        return (short)(n + (s2 & ((n ^ s2) & (n | -n)) >> 31));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int floorDiv(short $this$floorDiv, int other) {
        short s = $this$floorDiv;
        int n = s / other;
        if ((s ^ other) < 0 && n * other != s) {
            int n2 = n;
            n = n2 + -1;
        }
        return n;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int mod(short $this$mod, int other) {
        short s = $this$mod;
        int n = s % other;
        return n + (other & ((n ^ other) & (n | -n)) >> 31);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long floorDiv(short $this$floorDiv, long other) {
        long l = $this$floorDiv;
        long l2 = l / other;
        if ((l ^ other) < 0L && l2 * other != l) {
            long l3 = l2;
            l2 = l3 + -1L;
        }
        return l2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long mod(short $this$mod, long other) {
        long l = $this$mod;
        long l2 = l % other;
        return l2 + (other & ((l2 ^ other) & (l2 | -l2)) >> 63);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int floorDiv(int $this$floorDiv, byte other) {
        int n = $this$floorDiv;
        byte by = other;
        int n2 = n / by;
        if ((n ^ by) < 0 && n2 * by != n) {
            int n3 = n2;
            n2 = n3 + -1;
        }
        return n2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final byte mod(int $this$mod, byte other) {
        int n = $this$mod;
        byte by = other;
        int n2 = n % by;
        return (byte)(n2 + (by & ((n2 ^ by) & (n2 | -n2)) >> 31));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int floorDiv(int $this$floorDiv, short other) {
        int n = $this$floorDiv;
        short s = other;
        int n2 = n / s;
        if ((n ^ s) < 0 && n2 * s != n) {
            int n3 = n2;
            n2 = n3 + -1;
        }
        return n2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final short mod(int $this$mod, short other) {
        int n = $this$mod;
        short s = other;
        int n2 = n % s;
        return (short)(n2 + (s & ((n2 ^ s) & (n2 | -n2)) >> 31));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int floorDiv(int $this$floorDiv, int other) {
        int q = $this$floorDiv / other;
        if (($this$floorDiv ^ other) < 0 && q * other != $this$floorDiv) {
            int n = q;
            q = n + -1;
        }
        return q;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int mod(int $this$mod, int other) {
        int r = $this$mod % other;
        return r + (other & ((r ^ other) & (r | -r)) >> 31);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long floorDiv(int $this$floorDiv, long other) {
        long l = $this$floorDiv;
        long l2 = l / other;
        if ((l ^ other) < 0L && l2 * other != l) {
            long l3 = l2;
            l2 = l3 + -1L;
        }
        return l2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long mod(int $this$mod, long other) {
        long l = $this$mod;
        long l2 = l % other;
        return l2 + (other & ((l2 ^ other) & (l2 | -l2)) >> 63);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long floorDiv(long $this$floorDiv, byte other) {
        long l = $this$floorDiv;
        long l2 = other;
        long l3 = l / l2;
        if ((l ^ l2) < 0L && l3 * l2 != l) {
            long l4 = l3;
            l3 = l4 + -1L;
        }
        return l3;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final byte mod(long $this$mod, byte other) {
        long l = $this$mod;
        long l2 = other;
        long l3 = l % l2;
        return (byte)(l3 + (l2 & ((l3 ^ l2) & (l3 | -l3)) >> 63));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long floorDiv(long $this$floorDiv, short other) {
        long l = $this$floorDiv;
        long l2 = other;
        long l3 = l / l2;
        if ((l ^ l2) < 0L && l3 * l2 != l) {
            long l4 = l3;
            l3 = l4 + -1L;
        }
        return l3;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final short mod(long $this$mod, short other) {
        long l = $this$mod;
        long l2 = other;
        long l3 = l % l2;
        return (short)(l3 + (l2 & ((l3 ^ l2) & (l3 | -l3)) >> 63));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long floorDiv(long $this$floorDiv, int other) {
        long l = $this$floorDiv;
        long l2 = other;
        long l3 = l / l2;
        if ((l ^ l2) < 0L && l3 * l2 != l) {
            long l4 = l3;
            l3 = l4 + -1L;
        }
        return l3;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final int mod(long $this$mod, int other) {
        long l = $this$mod;
        long l2 = other;
        long l3 = l % l2;
        return (int)(l3 + (l2 & ((l3 ^ l2) & (l3 | -l3)) >> 63));
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long floorDiv(long $this$floorDiv, long other) {
        long q = $this$floorDiv / other;
        if (($this$floorDiv ^ other) < 0L && q * other != $this$floorDiv) {
            long l = q;
            q = l + -1L;
        }
        return q;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final long mod(long $this$mod, long other) {
        long r = $this$mod % other;
        return r + (other & ((r ^ other) & (r | -r)) >> 63);
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final float mod(float $this$mod, float other) {
        float r = $this$mod % other;
        return !(r == 0.0f) && !(Math.signum(r) == Math.signum(other)) ? r + other : r;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final double mod(float $this$mod, double other) {
        double d = $this$mod;
        double d2 = d % other;
        return !(d2 == 0.0) && !(Math.signum(d2) == Math.signum(other)) ? d2 + other : d2;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final double mod(double $this$mod, float other) {
        double d = $this$mod;
        double d2 = other;
        double d3 = d % d2;
        return !(d3 == 0.0) && !(Math.signum(d3) == Math.signum(d2)) ? d3 + d2 : d3;
    }

    @SinceKotlin(version="1.5")
    @InlineOnly
    private static final double mod(double $this$mod, double other) {
        double r = $this$mod % other;
        return !(r == 0.0) && !(Math.signum(r) == Math.signum(other)) ? r + other : r;
    }
}

