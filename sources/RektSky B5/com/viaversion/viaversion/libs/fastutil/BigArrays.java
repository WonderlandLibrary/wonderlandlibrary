/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.viaversion.viaversion.libs.fastutil.booleans.BooleanArrays
 *  com.viaversion.viaversion.libs.fastutil.booleans.BooleanBigArrays
 *  com.viaversion.viaversion.libs.fastutil.bytes.ByteArrays
 *  com.viaversion.viaversion.libs.fastutil.bytes.ByteBigArrays
 *  com.viaversion.viaversion.libs.fastutil.chars.CharArrays
 *  com.viaversion.viaversion.libs.fastutil.chars.CharBigArrays
 *  com.viaversion.viaversion.libs.fastutil.doubles.DoubleArrays
 *  com.viaversion.viaversion.libs.fastutil.doubles.DoubleBigArrays
 *  com.viaversion.viaversion.libs.fastutil.floats.FloatArrays
 *  com.viaversion.viaversion.libs.fastutil.floats.FloatBigArrays
 *  com.viaversion.viaversion.libs.fastutil.ints.IntBigArrays
 *  com.viaversion.viaversion.libs.fastutil.longs.LongArrays
 *  com.viaversion.viaversion.libs.fastutil.longs.LongBigArrays
 *  com.viaversion.viaversion.libs.fastutil.longs.LongComparator
 *  com.viaversion.viaversion.libs.fastutil.objects.ObjectBigArrays
 *  com.viaversion.viaversion.libs.fastutil.shorts.ShortArrays
 *  com.viaversion.viaversion.libs.fastutil.shorts.ShortBigArrays
 */
package com.viaversion.viaversion.libs.fastutil;

import com.viaversion.viaversion.libs.fastutil.BigSwapper;
import com.viaversion.viaversion.libs.fastutil.booleans.BooleanArrays;
import com.viaversion.viaversion.libs.fastutil.booleans.BooleanBigArrays;
import com.viaversion.viaversion.libs.fastutil.bytes.ByteArrays;
import com.viaversion.viaversion.libs.fastutil.bytes.ByteBigArrays;
import com.viaversion.viaversion.libs.fastutil.chars.CharArrays;
import com.viaversion.viaversion.libs.fastutil.chars.CharBigArrays;
import com.viaversion.viaversion.libs.fastutil.doubles.DoubleArrays;
import com.viaversion.viaversion.libs.fastutil.doubles.DoubleBigArrays;
import com.viaversion.viaversion.libs.fastutil.floats.FloatArrays;
import com.viaversion.viaversion.libs.fastutil.floats.FloatBigArrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntBigArrays;
import com.viaversion.viaversion.libs.fastutil.longs.LongArrays;
import com.viaversion.viaversion.libs.fastutil.longs.LongBigArrays;
import com.viaversion.viaversion.libs.fastutil.longs.LongComparator;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrays;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectBigArrays;
import com.viaversion.viaversion.libs.fastutil.shorts.ShortArrays;
import com.viaversion.viaversion.libs.fastutil.shorts.ShortBigArrays;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLongArray;

public class BigArrays {
    public static final int SEGMENT_SHIFT = 27;
    public static final int SEGMENT_SIZE = 0x8000000;
    public static final int SEGMENT_MASK = 0x7FFFFFF;
    private static final int SMALL = 7;
    private static final int MEDIUM = 40;

    protected BigArrays() {
    }

    public static int segment(long index) {
        return (int)(index >>> 27);
    }

    public static int displacement(long index) {
        return (int)(index & 0x7FFFFFFL);
    }

    public static long start(int segment) {
        return (long)segment << 27;
    }

    public static long nearestSegmentStart(long index, long min, long max) {
        long lower = BigArrays.start(BigArrays.segment(index));
        long upper = BigArrays.start(BigArrays.segment(index) + 1);
        if (upper >= max) {
            if (lower < min) {
                return index;
            }
            return lower;
        }
        if (lower < min) {
            return upper;
        }
        long mid = lower + (upper - lower >> 1);
        return index <= mid ? lower : upper;
    }

    public static long index(int segment, int displacement) {
        return BigArrays.start(segment) + (long)displacement;
    }

    public static void ensureFromTo(long bigArrayLength, long from, long to) {
        if (from < 0L) {
            throw new ArrayIndexOutOfBoundsException("Start index (" + from + ") is negative");
        }
        if (from > to) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        if (to > bigArrayLength) {
            throw new ArrayIndexOutOfBoundsException("End index (" + to + ") is greater than big-array length (" + bigArrayLength + ")");
        }
    }

    public static void ensureOffsetLength(long bigArrayLength, long offset, long length) {
        if (offset < 0L) {
            throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
        }
        if (length < 0L) {
            throw new IllegalArgumentException("Length (" + length + ") is negative");
        }
        if (offset + length > bigArrayLength) {
            throw new ArrayIndexOutOfBoundsException("Last index (" + (offset + length) + ") is greater than big-array length (" + bigArrayLength + ")");
        }
    }

    public static void ensureLength(long bigArrayLength) {
        if (bigArrayLength < 0L) {
            throw new IllegalArgumentException("Negative big-array size: " + bigArrayLength);
        }
        if (bigArrayLength >= 288230376017494016L) {
            throw new IllegalArgumentException("Big-array size too big: " + bigArrayLength);
        }
    }

    private static void inPlaceMerge(long from, long mid, long to, LongComparator comp, BigSwapper swapper) {
        long secondCut;
        long firstCut;
        if (from >= mid || mid >= to) {
            return;
        }
        if (to - from == 2L) {
            if (comp.compare(mid, from) < 0) {
                swapper.swap(from, mid);
            }
            return;
        }
        if (mid - from > to - mid) {
            firstCut = from + (mid - from) / 2L;
            secondCut = BigArrays.lowerBound(mid, to, firstCut, comp);
        } else {
            secondCut = mid + (to - mid) / 2L;
            firstCut = BigArrays.upperBound(from, mid, secondCut, comp);
        }
        long first2 = firstCut;
        long middle2 = mid;
        long last2 = secondCut;
        if (middle2 != first2 && middle2 != last2) {
            long first1 = first2;
            long last1 = middle2;
            while (first1 < --last1) {
                swapper.swap(first1++, last1);
            }
            first1 = middle2;
            last1 = last2;
            while (first1 < --last1) {
                swapper.swap(first1++, last1);
            }
            first1 = first2;
            last1 = last2;
            while (first1 < --last1) {
                swapper.swap(first1++, last1);
            }
        }
        mid = firstCut + (secondCut - mid);
        BigArrays.inPlaceMerge(from, firstCut, mid, comp, swapper);
        BigArrays.inPlaceMerge(mid, secondCut, to, comp, swapper);
    }

    private static long lowerBound(long mid, long to, long firstCut, LongComparator comp) {
        long len = to - mid;
        while (len > 0L) {
            long half = len / 2L;
            long middle = mid + half;
            if (comp.compare(middle, firstCut) < 0) {
                mid = middle + 1L;
                len -= half + 1L;
                continue;
            }
            len = half;
        }
        return mid;
    }

    private static long med3(long a2, long b2, long c2, LongComparator comp) {
        int ab = comp.compare(a2, b2);
        int ac = comp.compare(a2, c2);
        int bc = comp.compare(b2, c2);
        return ab < 0 ? (bc < 0 ? b2 : (ac < 0 ? c2 : a2)) : (bc > 0 ? b2 : (ac > 0 ? c2 : a2));
    }

    public static void mergeSort(long from, long to, LongComparator comp, BigSwapper swapper) {
        long length = to - from;
        if (length < 7L) {
            for (long i2 = from; i2 < to; ++i2) {
                for (long j2 = i2; j2 > from && comp.compare(j2 - 1L, j2) > 0; --j2) {
                    swapper.swap(j2, j2 - 1L);
                }
            }
            return;
        }
        long mid = from + to >>> 1;
        BigArrays.mergeSort(from, mid, comp, swapper);
        BigArrays.mergeSort(mid, to, comp, swapper);
        if (comp.compare(mid - 1L, mid) <= 0) {
            return;
        }
        BigArrays.inPlaceMerge(from, mid, to, comp, swapper);
    }

    public static void quickSort(long from, long to, LongComparator comp, BigSwapper swapper) {
        long c2;
        long a2;
        long len = to - from;
        if (len < 7L) {
            for (long i2 = from; i2 < to; ++i2) {
                for (long j2 = i2; j2 > from && comp.compare(j2 - 1L, j2) > 0; --j2) {
                    swapper.swap(j2, j2 - 1L);
                }
            }
            return;
        }
        long m2 = from + len / 2L;
        if (len > 7L) {
            long l2 = from;
            long n2 = to - 1L;
            if (len > 40L) {
                long s2 = len / 8L;
                l2 = BigArrays.med3(l2, l2 + s2, l2 + 2L * s2, comp);
                m2 = BigArrays.med3(m2 - s2, m2, m2 + s2, comp);
                n2 = BigArrays.med3(n2 - 2L * s2, n2 - s2, n2, comp);
            }
            m2 = BigArrays.med3(l2, m2, n2, comp);
        }
        long b2 = a2 = from;
        long d2 = c2 = to - 1L;
        while (true) {
            int comparison;
            if (b2 <= c2 && (comparison = comp.compare(b2, m2)) <= 0) {
                if (comparison == 0) {
                    if (a2 == m2) {
                        m2 = b2;
                    } else if (b2 == m2) {
                        m2 = a2;
                    }
                    swapper.swap(a2++, b2);
                }
                ++b2;
                continue;
            }
            while (c2 >= b2 && (comparison = comp.compare(c2, m2)) >= 0) {
                if (comparison == 0) {
                    if (c2 == m2) {
                        m2 = d2;
                    } else if (d2 == m2) {
                        m2 = c2;
                    }
                    swapper.swap(c2, d2--);
                }
                --c2;
            }
            if (b2 > c2) break;
            if (b2 == m2) {
                m2 = d2;
            } else if (c2 == m2) {
                m2 = c2;
            }
            swapper.swap(b2++, c2--);
        }
        long n3 = from + len;
        long s3 = Math.min(a2 - from, b2 - a2);
        BigArrays.vecSwap(swapper, from, b2 - s3, s3);
        s3 = Math.min(d2 - c2, n3 - d2 - 1L);
        BigArrays.vecSwap(swapper, b2, n3 - s3, s3);
        s3 = b2 - a2;
        if (s3 > 1L) {
            BigArrays.quickSort(from, from + s3, comp, swapper);
        }
        if ((s3 = d2 - c2) > 1L) {
            BigArrays.quickSort(n3 - s3, n3, comp, swapper);
        }
    }

    private static long upperBound(long from, long mid, long secondCut, LongComparator comp) {
        long len = mid - from;
        while (len > 0L) {
            long half = len / 2L;
            long middle = from + half;
            if (comp.compare(secondCut, middle) < 0) {
                len = half;
                continue;
            }
            from = middle + 1L;
            len -= half + 1L;
        }
        return from;
    }

    private static void vecSwap(BigSwapper swapper, long from, long l2, long s2) {
        int i2 = 0;
        while ((long)i2 < s2) {
            swapper.swap(from, l2);
            ++i2;
            ++from;
            ++l2;
        }
    }

    public static byte get(byte[][] array, long index) {
        return array[BigArrays.segment(index)][BigArrays.displacement(index)];
    }

    public static void set(byte[][] array, long index, byte value) {
        array[BigArrays.segment((long)index)][BigArrays.displacement((long)index)] = value;
    }

    public static void swap(byte[][] array, long first, long second) {
        byte t2 = array[BigArrays.segment(first)][BigArrays.displacement(first)];
        array[BigArrays.segment((long)first)][BigArrays.displacement((long)first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
        array[BigArrays.segment((long)second)][BigArrays.displacement((long)second)] = t2;
    }

    public static byte[][] reverse(byte[][] a2) {
        long length = BigArrays.length(a2);
        long i2 = length / 2L;
        while (i2-- != 0L) {
            BigArrays.swap(a2, i2, length - i2 - 1L);
        }
        return a2;
    }

    public static void add(byte[][] array, long index, byte incr) {
        byte[] byArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        byArray[n2] = (byte)(byArray[n2] + incr);
    }

    public static void mul(byte[][] array, long index, byte factor) {
        byte[] byArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        byArray[n2] = (byte)(byArray[n2] * factor);
    }

    public static void incr(byte[][] array, long index) {
        byte[] byArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        byArray[n2] = (byte)(byArray[n2] + 1);
    }

    public static void decr(byte[][] array, long index) {
        byte[] byArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        byArray[n2] = (byte)(byArray[n2] - 1);
    }

    public static long length(byte[][] array) {
        int length = array.length;
        return length == 0 ? 0L : BigArrays.start(length - 1) + (long)array[length - 1].length;
    }

    public static void copy(byte[][] srcArray, long srcPos, byte[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = BigArrays.segment(srcPos);
            int destSegment = BigArrays.segment(destPos);
            int srcDispl = BigArrays.displacement(srcPos);
            int destDispl = BigArrays.displacement(destPos);
            while (length > 0L) {
                int l2 = (int)Math.min(length, (long)Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l2 == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l2);
                if ((srcDispl += l2) == 0x8000000) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l2) == 0x8000000) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= (long)l2;
            }
        } else {
            int srcSegment = BigArrays.segment(srcPos + length);
            int destSegment = BigArrays.segment(destPos + length);
            int srcDispl = BigArrays.displacement(srcPos + length);
            int destDispl = BigArrays.displacement(destPos + length);
            while (length > 0L) {
                int l3;
                if (srcDispl == 0) {
                    srcDispl = 0x8000000;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 0x8000000;
                    --destSegment;
                }
                if ((l3 = (int)Math.min(length, (long)Math.min(srcDispl, destDispl))) == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl - l3, destArray[destSegment], destDispl - l3, l3);
                srcDispl -= l3;
                destDispl -= l3;
                length -= (long)l3;
            }
        }
    }

    public static void copyFromBig(byte[][] srcArray, long srcPos, byte[] destArray, int destPos, int length) {
        int srcSegment = BigArrays.segment(srcPos);
        int srcDispl = BigArrays.displacement(srcPos);
        while (length > 0) {
            int l2 = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l2);
            if ((srcDispl += l2) == 0x8000000) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l2;
            length -= l2;
        }
    }

    public static void copyToBig(byte[] srcArray, int srcPos, byte[][] destArray, long destPos, long length) {
        int destSegment = BigArrays.segment(destPos);
        int destDispl = BigArrays.displacement(destPos);
        while (length > 0L) {
            int l2 = (int)Math.min((long)(destArray[destSegment].length - destDispl), length);
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l2);
            if ((destDispl += l2) == 0x8000000) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l2;
            length -= (long)l2;
        }
    }

    public static byte[][] wrap(byte[] array) {
        if (array.length == 0) {
            return ByteBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 0x8000000) {
            return new byte[][]{array};
        }
        byte[][] bigArray = ByteBigArrays.newBigArray((long)array.length);
        for (int i2 = 0; i2 < bigArray.length; ++i2) {
            System.arraycopy(array, (int)BigArrays.start(i2), bigArray[i2], 0, bigArray[i2].length);
        }
        return bigArray;
    }

    public static byte[][] ensureCapacity(byte[][] array, long length) {
        return BigArrays.ensureCapacity(array, length, BigArrays.length(array));
    }

    public static byte[][] forceCapacity(byte[][] array, long length, long preserve) {
        BigArrays.ensureLength(length);
        int valid = array.length - (array.length == 0 || array.length > 0 && array[array.length - 1].length == 0x8000000 ? 0 : 1);
        int baseLength = (int)(length + 0x7FFFFFFL >>> 27);
        byte[][] base = (byte[][])Arrays.copyOf(array, baseLength);
        int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i2 = valid; i2 < baseLength - 1; ++i2) {
                base[i2] = new byte[0x8000000];
            }
            base[baseLength - 1] = new byte[residual];
        } else {
            for (int i3 = valid; i3 < baseLength; ++i3) {
                base[i3] = new byte[0x8000000];
            }
        }
        if (preserve - (long)valid * 0x8000000L > 0L) {
            BigArrays.copy(array, (long)valid * 0x8000000L, base, (long)valid * 0x8000000L, preserve - (long)valid * 0x8000000L);
        }
        return base;
    }

    public static byte[][] ensureCapacity(byte[][] array, long length, long preserve) {
        return length > BigArrays.length(array) ? BigArrays.forceCapacity(array, length, preserve) : array;
    }

    public static byte[][] grow(byte[][] array, long length) {
        long oldLength = BigArrays.length(array);
        return length > oldLength ? BigArrays.grow(array, length, oldLength) : array;
    }

    public static byte[][] grow(byte[][] array, long length, long preserve) {
        long oldLength = BigArrays.length(array);
        return length > oldLength ? BigArrays.ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
    }

    public static byte[][] trim(byte[][] array, long length) {
        BigArrays.ensureLength(length);
        long oldLength = BigArrays.length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int)(length + 0x7FFFFFFL >>> 27);
        byte[][] base = (byte[][])Arrays.copyOf(array, baseLength);
        int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = ByteArrays.trim((byte[])base[baseLength - 1], (int)residual);
        }
        return base;
    }

    public static byte[][] setLength(byte[][] array, long length) {
        long oldLength = BigArrays.length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return BigArrays.trim(array, length);
        }
        return BigArrays.ensureCapacity(array, length);
    }

    public static byte[][] copy(byte[][] array, long offset, long length) {
        BigArrays.ensureOffsetLength(array, offset, length);
        byte[][] a2 = ByteBigArrays.newBigArray((long)length);
        BigArrays.copy(array, offset, a2, 0L, length);
        return a2;
    }

    public static byte[][] copy(byte[][] array) {
        byte[][] base = (byte[][])array.clone();
        int i2 = base.length;
        while (i2-- != 0) {
            base[i2] = (byte[])array[i2].clone();
        }
        return base;
    }

    public static void fill(byte[][] array, byte value) {
        int i2 = array.length;
        while (i2-- != 0) {
            Arrays.fill(array[i2], value);
        }
    }

    public static void fill(byte[][] array, long from, long to, byte value) {
        long length = BigArrays.length(array);
        BigArrays.ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        int fromSegment = BigArrays.segment(from);
        int toSegment = BigArrays.segment(to);
        int fromDispl = BigArrays.displacement(from);
        int toDispl = BigArrays.displacement(to);
        if (fromSegment == toSegment) {
            Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (--toSegment > fromSegment) {
            Arrays.fill(array[toSegment], value);
        }
        Arrays.fill(array[fromSegment], fromDispl, 0x8000000, value);
    }

    public static boolean equals(byte[][] a1, byte[][] a2) {
        if (BigArrays.length(a1) != BigArrays.length(a2)) {
            return false;
        }
        int i2 = a1.length;
        while (i2-- != 0) {
            byte[] t2 = a1[i2];
            byte[] u2 = a2[i2];
            int j2 = t2.length;
            while (j2-- != 0) {
                if (t2[j2] == u2[j2]) continue;
                return false;
            }
        }
        return true;
    }

    public static String toString(byte[][] a2) {
        if (a2 == null) {
            return "null";
        }
        long last = BigArrays.length(a2) - 1L;
        if (last == -1L) {
            return "[]";
        }
        StringBuilder b2 = new StringBuilder();
        b2.append('[');
        long i2 = 0L;
        while (true) {
            b2.append(String.valueOf(BigArrays.get(a2, i2)));
            if (i2 == last) {
                return b2.append(']').toString();
            }
            b2.append(", ");
            ++i2;
        }
    }

    public static void ensureFromTo(byte[][] a2, long from, long to) {
        BigArrays.ensureFromTo(BigArrays.length(a2), from, to);
    }

    public static void ensureOffsetLength(byte[][] a2, long offset, long length) {
        BigArrays.ensureOffsetLength(BigArrays.length(a2), offset, length);
    }

    public static void ensureSameLength(byte[][] a2, byte[][] b2) {
        if (BigArrays.length(a2) != BigArrays.length(b2)) {
            throw new IllegalArgumentException("Array size mismatch: " + BigArrays.length(a2) + " != " + BigArrays.length(b2));
        }
    }

    public static byte[][] shuffle(byte[][] a2, long from, long to, Random random) {
        long i2 = to - from;
        while (i2-- != 0L) {
            long p2 = (random.nextLong() & Long.MAX_VALUE) % (i2 + 1L);
            byte t2 = BigArrays.get(a2, from + i2);
            BigArrays.set(a2, from + i2, BigArrays.get(a2, from + p2));
            BigArrays.set(a2, from + p2, t2);
        }
        return a2;
    }

    public static byte[][] shuffle(byte[][] a2, Random random) {
        long i2 = BigArrays.length(a2);
        while (i2-- != 0L) {
            long p2 = (random.nextLong() & Long.MAX_VALUE) % (i2 + 1L);
            byte t2 = BigArrays.get(a2, i2);
            BigArrays.set(a2, i2, BigArrays.get(a2, p2));
            BigArrays.set(a2, p2, t2);
        }
        return a2;
    }

    public static int get(int[][] array, long index) {
        return array[BigArrays.segment(index)][BigArrays.displacement(index)];
    }

    public static void set(int[][] array, long index, int value) {
        array[BigArrays.segment((long)index)][BigArrays.displacement((long)index)] = value;
    }

    public static long length(AtomicIntegerArray[] array) {
        int length = array.length;
        return length == 0 ? 0L : BigArrays.start(length - 1) + (long)array[length - 1].length();
    }

    public static int get(AtomicIntegerArray[] array, long index) {
        return array[BigArrays.segment(index)].get(BigArrays.displacement(index));
    }

    public static void set(AtomicIntegerArray[] array, long index, int value) {
        array[BigArrays.segment(index)].set(BigArrays.displacement(index), value);
    }

    public static int getAndSet(AtomicIntegerArray[] array, long index, int value) {
        return array[BigArrays.segment(index)].getAndSet(BigArrays.displacement(index), value);
    }

    public static int getAndAdd(AtomicIntegerArray[] array, long index, int value) {
        return array[BigArrays.segment(index)].getAndAdd(BigArrays.displacement(index), value);
    }

    public static int addAndGet(AtomicIntegerArray[] array, long index, int value) {
        return array[BigArrays.segment(index)].addAndGet(BigArrays.displacement(index), value);
    }

    public static int getAndIncrement(AtomicIntegerArray[] array, long index) {
        return array[BigArrays.segment(index)].getAndDecrement(BigArrays.displacement(index));
    }

    public static int incrementAndGet(AtomicIntegerArray[] array, long index) {
        return array[BigArrays.segment(index)].incrementAndGet(BigArrays.displacement(index));
    }

    public static int getAndDecrement(AtomicIntegerArray[] array, long index) {
        return array[BigArrays.segment(index)].getAndDecrement(BigArrays.displacement(index));
    }

    public static int decrementAndGet(AtomicIntegerArray[] array, long index) {
        return array[BigArrays.segment(index)].decrementAndGet(BigArrays.displacement(index));
    }

    public static boolean compareAndSet(AtomicIntegerArray[] array, long index, int expected, int value) {
        return array[BigArrays.segment(index)].compareAndSet(BigArrays.displacement(index), expected, value);
    }

    public static void swap(int[][] array, long first, long second) {
        int t2 = array[BigArrays.segment(first)][BigArrays.displacement(first)];
        array[BigArrays.segment((long)first)][BigArrays.displacement((long)first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
        array[BigArrays.segment((long)second)][BigArrays.displacement((long)second)] = t2;
    }

    public static int[][] reverse(int[][] a2) {
        long length = BigArrays.length(a2);
        long i2 = length / 2L;
        while (i2-- != 0L) {
            BigArrays.swap(a2, i2, length - i2 - 1L);
        }
        return a2;
    }

    public static void add(int[][] array, long index, int incr) {
        int[] nArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        nArray[n2] = nArray[n2] + incr;
    }

    public static void mul(int[][] array, long index, int factor) {
        int[] nArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        nArray[n2] = nArray[n2] * factor;
    }

    public static void incr(int[][] array, long index) {
        int[] nArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        nArray[n2] = nArray[n2] + 1;
    }

    public static void decr(int[][] array, long index) {
        int[] nArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        nArray[n2] = nArray[n2] - 1;
    }

    public static long length(int[][] array) {
        int length = array.length;
        return length == 0 ? 0L : BigArrays.start(length - 1) + (long)array[length - 1].length;
    }

    public static void copy(int[][] srcArray, long srcPos, int[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = BigArrays.segment(srcPos);
            int destSegment = BigArrays.segment(destPos);
            int srcDispl = BigArrays.displacement(srcPos);
            int destDispl = BigArrays.displacement(destPos);
            while (length > 0L) {
                int l2 = (int)Math.min(length, (long)Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l2 == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l2);
                if ((srcDispl += l2) == 0x8000000) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l2) == 0x8000000) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= (long)l2;
            }
        } else {
            int srcSegment = BigArrays.segment(srcPos + length);
            int destSegment = BigArrays.segment(destPos + length);
            int srcDispl = BigArrays.displacement(srcPos + length);
            int destDispl = BigArrays.displacement(destPos + length);
            while (length > 0L) {
                int l3;
                if (srcDispl == 0) {
                    srcDispl = 0x8000000;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 0x8000000;
                    --destSegment;
                }
                if ((l3 = (int)Math.min(length, (long)Math.min(srcDispl, destDispl))) == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl - l3, destArray[destSegment], destDispl - l3, l3);
                srcDispl -= l3;
                destDispl -= l3;
                length -= (long)l3;
            }
        }
    }

    public static void copyFromBig(int[][] srcArray, long srcPos, int[] destArray, int destPos, int length) {
        int srcSegment = BigArrays.segment(srcPos);
        int srcDispl = BigArrays.displacement(srcPos);
        while (length > 0) {
            int l2 = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l2);
            if ((srcDispl += l2) == 0x8000000) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l2;
            length -= l2;
        }
    }

    public static void copyToBig(int[] srcArray, int srcPos, int[][] destArray, long destPos, long length) {
        int destSegment = BigArrays.segment(destPos);
        int destDispl = BigArrays.displacement(destPos);
        while (length > 0L) {
            int l2 = (int)Math.min((long)(destArray[destSegment].length - destDispl), length);
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l2);
            if ((destDispl += l2) == 0x8000000) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l2;
            length -= (long)l2;
        }
    }

    public static int[][] wrap(int[] array) {
        if (array.length == 0) {
            return IntBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 0x8000000) {
            return new int[][]{array};
        }
        int[][] bigArray = IntBigArrays.newBigArray((long)array.length);
        for (int i2 = 0; i2 < bigArray.length; ++i2) {
            System.arraycopy(array, (int)BigArrays.start(i2), bigArray[i2], 0, bigArray[i2].length);
        }
        return bigArray;
    }

    public static int[][] ensureCapacity(int[][] array, long length) {
        return BigArrays.ensureCapacity(array, length, BigArrays.length(array));
    }

    public static int[][] forceCapacity(int[][] array, long length, long preserve) {
        BigArrays.ensureLength(length);
        int valid = array.length - (array.length == 0 || array.length > 0 && array[array.length - 1].length == 0x8000000 ? 0 : 1);
        int baseLength = (int)(length + 0x7FFFFFFL >>> 27);
        int[][] base = (int[][])Arrays.copyOf(array, baseLength);
        int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i2 = valid; i2 < baseLength - 1; ++i2) {
                base[i2] = new int[0x8000000];
            }
            base[baseLength - 1] = new int[residual];
        } else {
            for (int i3 = valid; i3 < baseLength; ++i3) {
                base[i3] = new int[0x8000000];
            }
        }
        if (preserve - (long)valid * 0x8000000L > 0L) {
            BigArrays.copy(array, (long)valid * 0x8000000L, base, (long)valid * 0x8000000L, preserve - (long)valid * 0x8000000L);
        }
        return base;
    }

    public static int[][] ensureCapacity(int[][] array, long length, long preserve) {
        return length > BigArrays.length(array) ? BigArrays.forceCapacity(array, length, preserve) : array;
    }

    public static int[][] grow(int[][] array, long length) {
        long oldLength = BigArrays.length(array);
        return length > oldLength ? BigArrays.grow(array, length, oldLength) : array;
    }

    public static int[][] grow(int[][] array, long length, long preserve) {
        long oldLength = BigArrays.length(array);
        return length > oldLength ? BigArrays.ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
    }

    public static int[][] trim(int[][] array, long length) {
        BigArrays.ensureLength(length);
        long oldLength = BigArrays.length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int)(length + 0x7FFFFFFL >>> 27);
        int[][] base = (int[][])Arrays.copyOf(array, baseLength);
        int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = IntArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }

    public static int[][] setLength(int[][] array, long length) {
        long oldLength = BigArrays.length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return BigArrays.trim(array, length);
        }
        return BigArrays.ensureCapacity(array, length);
    }

    public static int[][] copy(int[][] array, long offset, long length) {
        BigArrays.ensureOffsetLength(array, offset, length);
        int[][] a2 = IntBigArrays.newBigArray((long)length);
        BigArrays.copy(array, offset, a2, 0L, length);
        return a2;
    }

    public static int[][] copy(int[][] array) {
        int[][] base = (int[][])array.clone();
        int i2 = base.length;
        while (i2-- != 0) {
            base[i2] = (int[])array[i2].clone();
        }
        return base;
    }

    public static void fill(int[][] array, int value) {
        int i2 = array.length;
        while (i2-- != 0) {
            Arrays.fill(array[i2], value);
        }
    }

    public static void fill(int[][] array, long from, long to, int value) {
        long length = BigArrays.length(array);
        BigArrays.ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        int fromSegment = BigArrays.segment(from);
        int toSegment = BigArrays.segment(to);
        int fromDispl = BigArrays.displacement(from);
        int toDispl = BigArrays.displacement(to);
        if (fromSegment == toSegment) {
            Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (--toSegment > fromSegment) {
            Arrays.fill(array[toSegment], value);
        }
        Arrays.fill(array[fromSegment], fromDispl, 0x8000000, value);
    }

    public static boolean equals(int[][] a1, int[][] a2) {
        if (BigArrays.length(a1) != BigArrays.length(a2)) {
            return false;
        }
        int i2 = a1.length;
        while (i2-- != 0) {
            int[] t2 = a1[i2];
            int[] u2 = a2[i2];
            int j2 = t2.length;
            while (j2-- != 0) {
                if (t2[j2] == u2[j2]) continue;
                return false;
            }
        }
        return true;
    }

    public static String toString(int[][] a2) {
        if (a2 == null) {
            return "null";
        }
        long last = BigArrays.length(a2) - 1L;
        if (last == -1L) {
            return "[]";
        }
        StringBuilder b2 = new StringBuilder();
        b2.append('[');
        long i2 = 0L;
        while (true) {
            b2.append(String.valueOf(BigArrays.get(a2, i2)));
            if (i2 == last) {
                return b2.append(']').toString();
            }
            b2.append(", ");
            ++i2;
        }
    }

    public static void ensureFromTo(int[][] a2, long from, long to) {
        BigArrays.ensureFromTo(BigArrays.length(a2), from, to);
    }

    public static void ensureOffsetLength(int[][] a2, long offset, long length) {
        BigArrays.ensureOffsetLength(BigArrays.length(a2), offset, length);
    }

    public static void ensureSameLength(int[][] a2, int[][] b2) {
        if (BigArrays.length(a2) != BigArrays.length(b2)) {
            throw new IllegalArgumentException("Array size mismatch: " + BigArrays.length(a2) + " != " + BigArrays.length(b2));
        }
    }

    public static int[][] shuffle(int[][] a2, long from, long to, Random random) {
        long i2 = to - from;
        while (i2-- != 0L) {
            long p2 = (random.nextLong() & Long.MAX_VALUE) % (i2 + 1L);
            int t2 = BigArrays.get(a2, from + i2);
            BigArrays.set(a2, from + i2, BigArrays.get(a2, from + p2));
            BigArrays.set(a2, from + p2, t2);
        }
        return a2;
    }

    public static int[][] shuffle(int[][] a2, Random random) {
        long i2 = BigArrays.length(a2);
        while (i2-- != 0L) {
            long p2 = (random.nextLong() & Long.MAX_VALUE) % (i2 + 1L);
            int t2 = BigArrays.get(a2, i2);
            BigArrays.set(a2, i2, BigArrays.get(a2, p2));
            BigArrays.set(a2, p2, t2);
        }
        return a2;
    }

    public static long get(long[][] array, long index) {
        return array[BigArrays.segment(index)][BigArrays.displacement(index)];
    }

    public static void set(long[][] array, long index, long value) {
        array[BigArrays.segment((long)index)][BigArrays.displacement((long)index)] = value;
    }

    public static long length(AtomicLongArray[] array) {
        int length = array.length;
        return length == 0 ? 0L : BigArrays.start(length - 1) + (long)array[length - 1].length();
    }

    public static long get(AtomicLongArray[] array, long index) {
        return array[BigArrays.segment(index)].get(BigArrays.displacement(index));
    }

    public static void set(AtomicLongArray[] array, long index, long value) {
        array[BigArrays.segment(index)].set(BigArrays.displacement(index), value);
    }

    public static long getAndSet(AtomicLongArray[] array, long index, long value) {
        return array[BigArrays.segment(index)].getAndSet(BigArrays.displacement(index), value);
    }

    public static long getAndAdd(AtomicLongArray[] array, long index, long value) {
        return array[BigArrays.segment(index)].getAndAdd(BigArrays.displacement(index), value);
    }

    public static long addAndGet(AtomicLongArray[] array, long index, long value) {
        return array[BigArrays.segment(index)].addAndGet(BigArrays.displacement(index), value);
    }

    public static long getAndIncrement(AtomicLongArray[] array, long index) {
        return array[BigArrays.segment(index)].getAndDecrement(BigArrays.displacement(index));
    }

    public static long incrementAndGet(AtomicLongArray[] array, long index) {
        return array[BigArrays.segment(index)].incrementAndGet(BigArrays.displacement(index));
    }

    public static long getAndDecrement(AtomicLongArray[] array, long index) {
        return array[BigArrays.segment(index)].getAndDecrement(BigArrays.displacement(index));
    }

    public static long decrementAndGet(AtomicLongArray[] array, long index) {
        return array[BigArrays.segment(index)].decrementAndGet(BigArrays.displacement(index));
    }

    public static boolean compareAndSet(AtomicLongArray[] array, long index, long expected, long value) {
        return array[BigArrays.segment(index)].compareAndSet(BigArrays.displacement(index), expected, value);
    }

    public static void swap(long[][] array, long first, long second) {
        long t2 = array[BigArrays.segment(first)][BigArrays.displacement(first)];
        array[BigArrays.segment((long)first)][BigArrays.displacement((long)first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
        array[BigArrays.segment((long)second)][BigArrays.displacement((long)second)] = t2;
    }

    public static long[][] reverse(long[][] a2) {
        long length = BigArrays.length(a2);
        long i2 = length / 2L;
        while (i2-- != 0L) {
            BigArrays.swap(a2, i2, length - i2 - 1L);
        }
        return a2;
    }

    public static void add(long[][] array, long index, long incr) {
        long[] lArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        lArray[n2] = lArray[n2] + incr;
    }

    public static void mul(long[][] array, long index, long factor) {
        long[] lArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        lArray[n2] = lArray[n2] * factor;
    }

    public static void incr(long[][] array, long index) {
        long[] lArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        lArray[n2] = lArray[n2] + 1L;
    }

    public static void decr(long[][] array, long index) {
        long[] lArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        lArray[n2] = lArray[n2] - 1L;
    }

    public static long length(long[][] array) {
        int length = array.length;
        return length == 0 ? 0L : BigArrays.start(length - 1) + (long)array[length - 1].length;
    }

    public static void copy(long[][] srcArray, long srcPos, long[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = BigArrays.segment(srcPos);
            int destSegment = BigArrays.segment(destPos);
            int srcDispl = BigArrays.displacement(srcPos);
            int destDispl = BigArrays.displacement(destPos);
            while (length > 0L) {
                int l2 = (int)Math.min(length, (long)Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l2 == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l2);
                if ((srcDispl += l2) == 0x8000000) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l2) == 0x8000000) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= (long)l2;
            }
        } else {
            int srcSegment = BigArrays.segment(srcPos + length);
            int destSegment = BigArrays.segment(destPos + length);
            int srcDispl = BigArrays.displacement(srcPos + length);
            int destDispl = BigArrays.displacement(destPos + length);
            while (length > 0L) {
                int l3;
                if (srcDispl == 0) {
                    srcDispl = 0x8000000;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 0x8000000;
                    --destSegment;
                }
                if ((l3 = (int)Math.min(length, (long)Math.min(srcDispl, destDispl))) == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl - l3, destArray[destSegment], destDispl - l3, l3);
                srcDispl -= l3;
                destDispl -= l3;
                length -= (long)l3;
            }
        }
    }

    public static void copyFromBig(long[][] srcArray, long srcPos, long[] destArray, int destPos, int length) {
        int srcSegment = BigArrays.segment(srcPos);
        int srcDispl = BigArrays.displacement(srcPos);
        while (length > 0) {
            int l2 = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l2);
            if ((srcDispl += l2) == 0x8000000) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l2;
            length -= l2;
        }
    }

    public static void copyToBig(long[] srcArray, int srcPos, long[][] destArray, long destPos, long length) {
        int destSegment = BigArrays.segment(destPos);
        int destDispl = BigArrays.displacement(destPos);
        while (length > 0L) {
            int l2 = (int)Math.min((long)(destArray[destSegment].length - destDispl), length);
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l2);
            if ((destDispl += l2) == 0x8000000) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l2;
            length -= (long)l2;
        }
    }

    public static long[][] wrap(long[] array) {
        if (array.length == 0) {
            return LongBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 0x8000000) {
            return new long[][]{array};
        }
        long[][] bigArray = LongBigArrays.newBigArray((long)array.length);
        for (int i2 = 0; i2 < bigArray.length; ++i2) {
            System.arraycopy(array, (int)BigArrays.start(i2), bigArray[i2], 0, bigArray[i2].length);
        }
        return bigArray;
    }

    public static long[][] ensureCapacity(long[][] array, long length) {
        return BigArrays.ensureCapacity(array, length, BigArrays.length(array));
    }

    public static long[][] forceCapacity(long[][] array, long length, long preserve) {
        BigArrays.ensureLength(length);
        int valid = array.length - (array.length == 0 || array.length > 0 && array[array.length - 1].length == 0x8000000 ? 0 : 1);
        int baseLength = (int)(length + 0x7FFFFFFL >>> 27);
        long[][] base = (long[][])Arrays.copyOf(array, baseLength);
        int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i2 = valid; i2 < baseLength - 1; ++i2) {
                base[i2] = new long[0x8000000];
            }
            base[baseLength - 1] = new long[residual];
        } else {
            for (int i3 = valid; i3 < baseLength; ++i3) {
                base[i3] = new long[0x8000000];
            }
        }
        if (preserve - (long)valid * 0x8000000L > 0L) {
            BigArrays.copy(array, (long)valid * 0x8000000L, base, (long)valid * 0x8000000L, preserve - (long)valid * 0x8000000L);
        }
        return base;
    }

    public static long[][] ensureCapacity(long[][] array, long length, long preserve) {
        return length > BigArrays.length(array) ? BigArrays.forceCapacity(array, length, preserve) : array;
    }

    public static long[][] grow(long[][] array, long length) {
        long oldLength = BigArrays.length(array);
        return length > oldLength ? BigArrays.grow(array, length, oldLength) : array;
    }

    public static long[][] grow(long[][] array, long length, long preserve) {
        long oldLength = BigArrays.length(array);
        return length > oldLength ? BigArrays.ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
    }

    public static long[][] trim(long[][] array, long length) {
        BigArrays.ensureLength(length);
        long oldLength = BigArrays.length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int)(length + 0x7FFFFFFL >>> 27);
        long[][] base = (long[][])Arrays.copyOf(array, baseLength);
        int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = LongArrays.trim((long[])base[baseLength - 1], (int)residual);
        }
        return base;
    }

    public static long[][] setLength(long[][] array, long length) {
        long oldLength = BigArrays.length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return BigArrays.trim(array, length);
        }
        return BigArrays.ensureCapacity(array, length);
    }

    public static long[][] copy(long[][] array, long offset, long length) {
        BigArrays.ensureOffsetLength(array, offset, length);
        long[][] a2 = LongBigArrays.newBigArray((long)length);
        BigArrays.copy(array, offset, a2, 0L, length);
        return a2;
    }

    public static long[][] copy(long[][] array) {
        long[][] base = (long[][])array.clone();
        int i2 = base.length;
        while (i2-- != 0) {
            base[i2] = (long[])array[i2].clone();
        }
        return base;
    }

    public static void fill(long[][] array, long value) {
        int i2 = array.length;
        while (i2-- != 0) {
            Arrays.fill(array[i2], value);
        }
    }

    public static void fill(long[][] array, long from, long to, long value) {
        long length = BigArrays.length(array);
        BigArrays.ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        int fromSegment = BigArrays.segment(from);
        int toSegment = BigArrays.segment(to);
        int fromDispl = BigArrays.displacement(from);
        int toDispl = BigArrays.displacement(to);
        if (fromSegment == toSegment) {
            Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (--toSegment > fromSegment) {
            Arrays.fill(array[toSegment], value);
        }
        Arrays.fill(array[fromSegment], fromDispl, 0x8000000, value);
    }

    public static boolean equals(long[][] a1, long[][] a2) {
        if (BigArrays.length(a1) != BigArrays.length(a2)) {
            return false;
        }
        int i2 = a1.length;
        while (i2-- != 0) {
            long[] t2 = a1[i2];
            long[] u2 = a2[i2];
            int j2 = t2.length;
            while (j2-- != 0) {
                if (t2[j2] == u2[j2]) continue;
                return false;
            }
        }
        return true;
    }

    public static String toString(long[][] a2) {
        if (a2 == null) {
            return "null";
        }
        long last = BigArrays.length(a2) - 1L;
        if (last == -1L) {
            return "[]";
        }
        StringBuilder b2 = new StringBuilder();
        b2.append('[');
        long i2 = 0L;
        while (true) {
            b2.append(String.valueOf(BigArrays.get(a2, i2)));
            if (i2 == last) {
                return b2.append(']').toString();
            }
            b2.append(", ");
            ++i2;
        }
    }

    public static void ensureFromTo(long[][] a2, long from, long to) {
        BigArrays.ensureFromTo(BigArrays.length(a2), from, to);
    }

    public static void ensureOffsetLength(long[][] a2, long offset, long length) {
        BigArrays.ensureOffsetLength(BigArrays.length(a2), offset, length);
    }

    public static void ensureSameLength(long[][] a2, long[][] b2) {
        if (BigArrays.length(a2) != BigArrays.length(b2)) {
            throw new IllegalArgumentException("Array size mismatch: " + BigArrays.length(a2) + " != " + BigArrays.length(b2));
        }
    }

    public static long[][] shuffle(long[][] a2, long from, long to, Random random) {
        long i2 = to - from;
        while (i2-- != 0L) {
            long p2 = (random.nextLong() & Long.MAX_VALUE) % (i2 + 1L);
            long t2 = BigArrays.get(a2, from + i2);
            BigArrays.set(a2, from + i2, BigArrays.get(a2, from + p2));
            BigArrays.set(a2, from + p2, t2);
        }
        return a2;
    }

    public static long[][] shuffle(long[][] a2, Random random) {
        long i2 = BigArrays.length(a2);
        while (i2-- != 0L) {
            long p2 = (random.nextLong() & Long.MAX_VALUE) % (i2 + 1L);
            long t2 = BigArrays.get(a2, i2);
            BigArrays.set(a2, i2, BigArrays.get(a2, p2));
            BigArrays.set(a2, p2, t2);
        }
        return a2;
    }

    public static double get(double[][] array, long index) {
        return array[BigArrays.segment(index)][BigArrays.displacement(index)];
    }

    public static void set(double[][] array, long index, double value) {
        array[BigArrays.segment((long)index)][BigArrays.displacement((long)index)] = value;
    }

    public static void swap(double[][] array, long first, long second) {
        double t2 = array[BigArrays.segment(first)][BigArrays.displacement(first)];
        array[BigArrays.segment((long)first)][BigArrays.displacement((long)first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
        array[BigArrays.segment((long)second)][BigArrays.displacement((long)second)] = t2;
    }

    public static double[][] reverse(double[][] a2) {
        long length = BigArrays.length(a2);
        long i2 = length / 2L;
        while (i2-- != 0L) {
            BigArrays.swap(a2, i2, length - i2 - 1L);
        }
        return a2;
    }

    public static void add(double[][] array, long index, double incr) {
        double[] dArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        dArray[n2] = dArray[n2] + incr;
    }

    public static void mul(double[][] array, long index, double factor) {
        double[] dArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        dArray[n2] = dArray[n2] * factor;
    }

    public static void incr(double[][] array, long index) {
        double[] dArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        dArray[n2] = dArray[n2] + 1.0;
    }

    public static void decr(double[][] array, long index) {
        double[] dArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        dArray[n2] = dArray[n2] - 1.0;
    }

    public static long length(double[][] array) {
        int length = array.length;
        return length == 0 ? 0L : BigArrays.start(length - 1) + (long)array[length - 1].length;
    }

    public static void copy(double[][] srcArray, long srcPos, double[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = BigArrays.segment(srcPos);
            int destSegment = BigArrays.segment(destPos);
            int srcDispl = BigArrays.displacement(srcPos);
            int destDispl = BigArrays.displacement(destPos);
            while (length > 0L) {
                int l2 = (int)Math.min(length, (long)Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l2 == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l2);
                if ((srcDispl += l2) == 0x8000000) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l2) == 0x8000000) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= (long)l2;
            }
        } else {
            int srcSegment = BigArrays.segment(srcPos + length);
            int destSegment = BigArrays.segment(destPos + length);
            int srcDispl = BigArrays.displacement(srcPos + length);
            int destDispl = BigArrays.displacement(destPos + length);
            while (length > 0L) {
                int l3;
                if (srcDispl == 0) {
                    srcDispl = 0x8000000;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 0x8000000;
                    --destSegment;
                }
                if ((l3 = (int)Math.min(length, (long)Math.min(srcDispl, destDispl))) == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl - l3, destArray[destSegment], destDispl - l3, l3);
                srcDispl -= l3;
                destDispl -= l3;
                length -= (long)l3;
            }
        }
    }

    public static void copyFromBig(double[][] srcArray, long srcPos, double[] destArray, int destPos, int length) {
        int srcSegment = BigArrays.segment(srcPos);
        int srcDispl = BigArrays.displacement(srcPos);
        while (length > 0) {
            int l2 = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l2);
            if ((srcDispl += l2) == 0x8000000) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l2;
            length -= l2;
        }
    }

    public static void copyToBig(double[] srcArray, int srcPos, double[][] destArray, long destPos, long length) {
        int destSegment = BigArrays.segment(destPos);
        int destDispl = BigArrays.displacement(destPos);
        while (length > 0L) {
            int l2 = (int)Math.min((long)(destArray[destSegment].length - destDispl), length);
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l2);
            if ((destDispl += l2) == 0x8000000) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l2;
            length -= (long)l2;
        }
    }

    public static double[][] wrap(double[] array) {
        if (array.length == 0) {
            return DoubleBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 0x8000000) {
            return new double[][]{array};
        }
        double[][] bigArray = DoubleBigArrays.newBigArray((long)array.length);
        for (int i2 = 0; i2 < bigArray.length; ++i2) {
            System.arraycopy(array, (int)BigArrays.start(i2), bigArray[i2], 0, bigArray[i2].length);
        }
        return bigArray;
    }

    public static double[][] ensureCapacity(double[][] array, long length) {
        return BigArrays.ensureCapacity(array, length, BigArrays.length(array));
    }

    public static double[][] forceCapacity(double[][] array, long length, long preserve) {
        BigArrays.ensureLength(length);
        int valid = array.length - (array.length == 0 || array.length > 0 && array[array.length - 1].length == 0x8000000 ? 0 : 1);
        int baseLength = (int)(length + 0x7FFFFFFL >>> 27);
        double[][] base = (double[][])Arrays.copyOf(array, baseLength);
        int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i2 = valid; i2 < baseLength - 1; ++i2) {
                base[i2] = new double[0x8000000];
            }
            base[baseLength - 1] = new double[residual];
        } else {
            for (int i3 = valid; i3 < baseLength; ++i3) {
                base[i3] = new double[0x8000000];
            }
        }
        if (preserve - (long)valid * 0x8000000L > 0L) {
            BigArrays.copy(array, (long)valid * 0x8000000L, base, (long)valid * 0x8000000L, preserve - (long)valid * 0x8000000L);
        }
        return base;
    }

    public static double[][] ensureCapacity(double[][] array, long length, long preserve) {
        return length > BigArrays.length(array) ? BigArrays.forceCapacity(array, length, preserve) : array;
    }

    public static double[][] grow(double[][] array, long length) {
        long oldLength = BigArrays.length(array);
        return length > oldLength ? BigArrays.grow(array, length, oldLength) : array;
    }

    public static double[][] grow(double[][] array, long length, long preserve) {
        long oldLength = BigArrays.length(array);
        return length > oldLength ? BigArrays.ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
    }

    public static double[][] trim(double[][] array, long length) {
        BigArrays.ensureLength(length);
        long oldLength = BigArrays.length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int)(length + 0x7FFFFFFL >>> 27);
        double[][] base = (double[][])Arrays.copyOf(array, baseLength);
        int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = DoubleArrays.trim((double[])base[baseLength - 1], (int)residual);
        }
        return base;
    }

    public static double[][] setLength(double[][] array, long length) {
        long oldLength = BigArrays.length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return BigArrays.trim(array, length);
        }
        return BigArrays.ensureCapacity(array, length);
    }

    public static double[][] copy(double[][] array, long offset, long length) {
        BigArrays.ensureOffsetLength(array, offset, length);
        double[][] a2 = DoubleBigArrays.newBigArray((long)length);
        BigArrays.copy(array, offset, a2, 0L, length);
        return a2;
    }

    public static double[][] copy(double[][] array) {
        double[][] base = (double[][])array.clone();
        int i2 = base.length;
        while (i2-- != 0) {
            base[i2] = (double[])array[i2].clone();
        }
        return base;
    }

    public static void fill(double[][] array, double value) {
        int i2 = array.length;
        while (i2-- != 0) {
            Arrays.fill(array[i2], value);
        }
    }

    public static void fill(double[][] array, long from, long to, double value) {
        long length = BigArrays.length(array);
        BigArrays.ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        int fromSegment = BigArrays.segment(from);
        int toSegment = BigArrays.segment(to);
        int fromDispl = BigArrays.displacement(from);
        int toDispl = BigArrays.displacement(to);
        if (fromSegment == toSegment) {
            Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (--toSegment > fromSegment) {
            Arrays.fill(array[toSegment], value);
        }
        Arrays.fill(array[fromSegment], fromDispl, 0x8000000, value);
    }

    public static boolean equals(double[][] a1, double[][] a2) {
        if (BigArrays.length(a1) != BigArrays.length(a2)) {
            return false;
        }
        int i2 = a1.length;
        while (i2-- != 0) {
            double[] t2 = a1[i2];
            double[] u2 = a2[i2];
            int j2 = t2.length;
            while (j2-- != 0) {
                if (Double.doubleToLongBits(t2[j2]) == Double.doubleToLongBits(u2[j2])) continue;
                return false;
            }
        }
        return true;
    }

    public static String toString(double[][] a2) {
        if (a2 == null) {
            return "null";
        }
        long last = BigArrays.length(a2) - 1L;
        if (last == -1L) {
            return "[]";
        }
        StringBuilder b2 = new StringBuilder();
        b2.append('[');
        long i2 = 0L;
        while (true) {
            b2.append(String.valueOf(BigArrays.get(a2, i2)));
            if (i2 == last) {
                return b2.append(']').toString();
            }
            b2.append(", ");
            ++i2;
        }
    }

    public static void ensureFromTo(double[][] a2, long from, long to) {
        BigArrays.ensureFromTo(BigArrays.length(a2), from, to);
    }

    public static void ensureOffsetLength(double[][] a2, long offset, long length) {
        BigArrays.ensureOffsetLength(BigArrays.length(a2), offset, length);
    }

    public static void ensureSameLength(double[][] a2, double[][] b2) {
        if (BigArrays.length(a2) != BigArrays.length(b2)) {
            throw new IllegalArgumentException("Array size mismatch: " + BigArrays.length(a2) + " != " + BigArrays.length(b2));
        }
    }

    public static double[][] shuffle(double[][] a2, long from, long to, Random random) {
        long i2 = to - from;
        while (i2-- != 0L) {
            long p2 = (random.nextLong() & Long.MAX_VALUE) % (i2 + 1L);
            double t2 = BigArrays.get(a2, from + i2);
            BigArrays.set(a2, from + i2, BigArrays.get(a2, from + p2));
            BigArrays.set(a2, from + p2, t2);
        }
        return a2;
    }

    public static double[][] shuffle(double[][] a2, Random random) {
        long i2 = BigArrays.length(a2);
        while (i2-- != 0L) {
            long p2 = (random.nextLong() & Long.MAX_VALUE) % (i2 + 1L);
            double t2 = BigArrays.get(a2, i2);
            BigArrays.set(a2, i2, BigArrays.get(a2, p2));
            BigArrays.set(a2, p2, t2);
        }
        return a2;
    }

    public static boolean get(boolean[][] array, long index) {
        return array[BigArrays.segment(index)][BigArrays.displacement(index)];
    }

    public static void set(boolean[][] array, long index, boolean value) {
        array[BigArrays.segment((long)index)][BigArrays.displacement((long)index)] = value;
    }

    public static void swap(boolean[][] array, long first, long second) {
        boolean t2 = array[BigArrays.segment(first)][BigArrays.displacement(first)];
        array[BigArrays.segment((long)first)][BigArrays.displacement((long)first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
        array[BigArrays.segment((long)second)][BigArrays.displacement((long)second)] = t2;
    }

    public static boolean[][] reverse(boolean[][] a2) {
        long length = BigArrays.length(a2);
        long i2 = length / 2L;
        while (i2-- != 0L) {
            BigArrays.swap(a2, i2, length - i2 - 1L);
        }
        return a2;
    }

    public static long length(boolean[][] array) {
        int length = array.length;
        return length == 0 ? 0L : BigArrays.start(length - 1) + (long)array[length - 1].length;
    }

    public static void copy(boolean[][] srcArray, long srcPos, boolean[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = BigArrays.segment(srcPos);
            int destSegment = BigArrays.segment(destPos);
            int srcDispl = BigArrays.displacement(srcPos);
            int destDispl = BigArrays.displacement(destPos);
            while (length > 0L) {
                int l2 = (int)Math.min(length, (long)Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l2 == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l2);
                if ((srcDispl += l2) == 0x8000000) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l2) == 0x8000000) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= (long)l2;
            }
        } else {
            int srcSegment = BigArrays.segment(srcPos + length);
            int destSegment = BigArrays.segment(destPos + length);
            int srcDispl = BigArrays.displacement(srcPos + length);
            int destDispl = BigArrays.displacement(destPos + length);
            while (length > 0L) {
                int l3;
                if (srcDispl == 0) {
                    srcDispl = 0x8000000;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 0x8000000;
                    --destSegment;
                }
                if ((l3 = (int)Math.min(length, (long)Math.min(srcDispl, destDispl))) == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl - l3, destArray[destSegment], destDispl - l3, l3);
                srcDispl -= l3;
                destDispl -= l3;
                length -= (long)l3;
            }
        }
    }

    public static void copyFromBig(boolean[][] srcArray, long srcPos, boolean[] destArray, int destPos, int length) {
        int srcSegment = BigArrays.segment(srcPos);
        int srcDispl = BigArrays.displacement(srcPos);
        while (length > 0) {
            int l2 = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l2);
            if ((srcDispl += l2) == 0x8000000) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l2;
            length -= l2;
        }
    }

    public static void copyToBig(boolean[] srcArray, int srcPos, boolean[][] destArray, long destPos, long length) {
        int destSegment = BigArrays.segment(destPos);
        int destDispl = BigArrays.displacement(destPos);
        while (length > 0L) {
            int l2 = (int)Math.min((long)(destArray[destSegment].length - destDispl), length);
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l2);
            if ((destDispl += l2) == 0x8000000) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l2;
            length -= (long)l2;
        }
    }

    public static boolean[][] wrap(boolean[] array) {
        if (array.length == 0) {
            return BooleanBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 0x8000000) {
            return new boolean[][]{array};
        }
        boolean[][] bigArray = BooleanBigArrays.newBigArray((long)array.length);
        for (int i2 = 0; i2 < bigArray.length; ++i2) {
            System.arraycopy(array, (int)BigArrays.start(i2), bigArray[i2], 0, bigArray[i2].length);
        }
        return bigArray;
    }

    public static boolean[][] ensureCapacity(boolean[][] array, long length) {
        return BigArrays.ensureCapacity(array, length, BigArrays.length(array));
    }

    public static boolean[][] forceCapacity(boolean[][] array, long length, long preserve) {
        BigArrays.ensureLength(length);
        int valid = array.length - (array.length == 0 || array.length > 0 && array[array.length - 1].length == 0x8000000 ? 0 : 1);
        int baseLength = (int)(length + 0x7FFFFFFL >>> 27);
        boolean[][] base = (boolean[][])Arrays.copyOf(array, baseLength);
        int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i2 = valid; i2 < baseLength - 1; ++i2) {
                base[i2] = new boolean[0x8000000];
            }
            base[baseLength - 1] = new boolean[residual];
        } else {
            for (int i3 = valid; i3 < baseLength; ++i3) {
                base[i3] = new boolean[0x8000000];
            }
        }
        if (preserve - (long)valid * 0x8000000L > 0L) {
            BigArrays.copy(array, (long)valid * 0x8000000L, base, (long)valid * 0x8000000L, preserve - (long)valid * 0x8000000L);
        }
        return base;
    }

    public static boolean[][] ensureCapacity(boolean[][] array, long length, long preserve) {
        return length > BigArrays.length(array) ? BigArrays.forceCapacity(array, length, preserve) : array;
    }

    public static boolean[][] grow(boolean[][] array, long length) {
        long oldLength = BigArrays.length(array);
        return length > oldLength ? BigArrays.grow(array, length, oldLength) : array;
    }

    public static boolean[][] grow(boolean[][] array, long length, long preserve) {
        long oldLength = BigArrays.length(array);
        return length > oldLength ? BigArrays.ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
    }

    public static boolean[][] trim(boolean[][] array, long length) {
        BigArrays.ensureLength(length);
        long oldLength = BigArrays.length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int)(length + 0x7FFFFFFL >>> 27);
        boolean[][] base = (boolean[][])Arrays.copyOf(array, baseLength);
        int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = BooleanArrays.trim((boolean[])base[baseLength - 1], (int)residual);
        }
        return base;
    }

    public static boolean[][] setLength(boolean[][] array, long length) {
        long oldLength = BigArrays.length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return BigArrays.trim(array, length);
        }
        return BigArrays.ensureCapacity(array, length);
    }

    public static boolean[][] copy(boolean[][] array, long offset, long length) {
        BigArrays.ensureOffsetLength(array, offset, length);
        boolean[][] a2 = BooleanBigArrays.newBigArray((long)length);
        BigArrays.copy(array, offset, a2, 0L, length);
        return a2;
    }

    public static boolean[][] copy(boolean[][] array) {
        boolean[][] base = (boolean[][])array.clone();
        int i2 = base.length;
        while (i2-- != 0) {
            base[i2] = (boolean[])array[i2].clone();
        }
        return base;
    }

    public static void fill(boolean[][] array, boolean value) {
        int i2 = array.length;
        while (i2-- != 0) {
            Arrays.fill(array[i2], value);
        }
    }

    public static void fill(boolean[][] array, long from, long to, boolean value) {
        long length = BigArrays.length(array);
        BigArrays.ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        int fromSegment = BigArrays.segment(from);
        int toSegment = BigArrays.segment(to);
        int fromDispl = BigArrays.displacement(from);
        int toDispl = BigArrays.displacement(to);
        if (fromSegment == toSegment) {
            Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (--toSegment > fromSegment) {
            Arrays.fill(array[toSegment], value);
        }
        Arrays.fill(array[fromSegment], fromDispl, 0x8000000, value);
    }

    public static boolean equals(boolean[][] a1, boolean[][] a2) {
        if (BigArrays.length(a1) != BigArrays.length(a2)) {
            return false;
        }
        int i2 = a1.length;
        while (i2-- != 0) {
            boolean[] t2 = a1[i2];
            boolean[] u2 = a2[i2];
            int j2 = t2.length;
            while (j2-- != 0) {
                if (t2[j2] == u2[j2]) continue;
                return false;
            }
        }
        return true;
    }

    public static String toString(boolean[][] a2) {
        if (a2 == null) {
            return "null";
        }
        long last = BigArrays.length(a2) - 1L;
        if (last == -1L) {
            return "[]";
        }
        StringBuilder b2 = new StringBuilder();
        b2.append('[');
        long i2 = 0L;
        while (true) {
            b2.append(String.valueOf(BigArrays.get(a2, i2)));
            if (i2 == last) {
                return b2.append(']').toString();
            }
            b2.append(", ");
            ++i2;
        }
    }

    public static void ensureFromTo(boolean[][] a2, long from, long to) {
        BigArrays.ensureFromTo(BigArrays.length(a2), from, to);
    }

    public static void ensureOffsetLength(boolean[][] a2, long offset, long length) {
        BigArrays.ensureOffsetLength(BigArrays.length(a2), offset, length);
    }

    public static void ensureSameLength(boolean[][] a2, boolean[][] b2) {
        if (BigArrays.length(a2) != BigArrays.length(b2)) {
            throw new IllegalArgumentException("Array size mismatch: " + BigArrays.length(a2) + " != " + BigArrays.length(b2));
        }
    }

    public static boolean[][] shuffle(boolean[][] a2, long from, long to, Random random) {
        long i2 = to - from;
        while (i2-- != 0L) {
            long p2 = (random.nextLong() & Long.MAX_VALUE) % (i2 + 1L);
            boolean t2 = BigArrays.get(a2, from + i2);
            BigArrays.set(a2, from + i2, BigArrays.get(a2, from + p2));
            BigArrays.set(a2, from + p2, t2);
        }
        return a2;
    }

    public static boolean[][] shuffle(boolean[][] a2, Random random) {
        long i2 = BigArrays.length(a2);
        while (i2-- != 0L) {
            long p2 = (random.nextLong() & Long.MAX_VALUE) % (i2 + 1L);
            boolean t2 = BigArrays.get(a2, i2);
            BigArrays.set(a2, i2, BigArrays.get(a2, p2));
            BigArrays.set(a2, p2, t2);
        }
        return a2;
    }

    public static short get(short[][] array, long index) {
        return array[BigArrays.segment(index)][BigArrays.displacement(index)];
    }

    public static void set(short[][] array, long index, short value) {
        array[BigArrays.segment((long)index)][BigArrays.displacement((long)index)] = value;
    }

    public static void swap(short[][] array, long first, long second) {
        short t2 = array[BigArrays.segment(first)][BigArrays.displacement(first)];
        array[BigArrays.segment((long)first)][BigArrays.displacement((long)first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
        array[BigArrays.segment((long)second)][BigArrays.displacement((long)second)] = t2;
    }

    public static short[][] reverse(short[][] a2) {
        long length = BigArrays.length(a2);
        long i2 = length / 2L;
        while (i2-- != 0L) {
            BigArrays.swap(a2, i2, length - i2 - 1L);
        }
        return a2;
    }

    public static void add(short[][] array, long index, short incr) {
        short[] sArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        sArray[n2] = (short)(sArray[n2] + incr);
    }

    public static void mul(short[][] array, long index, short factor) {
        short[] sArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        sArray[n2] = (short)(sArray[n2] * factor);
    }

    public static void incr(short[][] array, long index) {
        short[] sArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        sArray[n2] = (short)(sArray[n2] + 1);
    }

    public static void decr(short[][] array, long index) {
        short[] sArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        sArray[n2] = (short)(sArray[n2] - 1);
    }

    public static long length(short[][] array) {
        int length = array.length;
        return length == 0 ? 0L : BigArrays.start(length - 1) + (long)array[length - 1].length;
    }

    public static void copy(short[][] srcArray, long srcPos, short[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = BigArrays.segment(srcPos);
            int destSegment = BigArrays.segment(destPos);
            int srcDispl = BigArrays.displacement(srcPos);
            int destDispl = BigArrays.displacement(destPos);
            while (length > 0L) {
                int l2 = (int)Math.min(length, (long)Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l2 == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l2);
                if ((srcDispl += l2) == 0x8000000) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l2) == 0x8000000) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= (long)l2;
            }
        } else {
            int srcSegment = BigArrays.segment(srcPos + length);
            int destSegment = BigArrays.segment(destPos + length);
            int srcDispl = BigArrays.displacement(srcPos + length);
            int destDispl = BigArrays.displacement(destPos + length);
            while (length > 0L) {
                int l3;
                if (srcDispl == 0) {
                    srcDispl = 0x8000000;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 0x8000000;
                    --destSegment;
                }
                if ((l3 = (int)Math.min(length, (long)Math.min(srcDispl, destDispl))) == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl - l3, destArray[destSegment], destDispl - l3, l3);
                srcDispl -= l3;
                destDispl -= l3;
                length -= (long)l3;
            }
        }
    }

    public static void copyFromBig(short[][] srcArray, long srcPos, short[] destArray, int destPos, int length) {
        int srcSegment = BigArrays.segment(srcPos);
        int srcDispl = BigArrays.displacement(srcPos);
        while (length > 0) {
            int l2 = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l2);
            if ((srcDispl += l2) == 0x8000000) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l2;
            length -= l2;
        }
    }

    public static void copyToBig(short[] srcArray, int srcPos, short[][] destArray, long destPos, long length) {
        int destSegment = BigArrays.segment(destPos);
        int destDispl = BigArrays.displacement(destPos);
        while (length > 0L) {
            int l2 = (int)Math.min((long)(destArray[destSegment].length - destDispl), length);
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l2);
            if ((destDispl += l2) == 0x8000000) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l2;
            length -= (long)l2;
        }
    }

    public static short[][] wrap(short[] array) {
        if (array.length == 0) {
            return ShortBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 0x8000000) {
            return new short[][]{array};
        }
        short[][] bigArray = ShortBigArrays.newBigArray((long)array.length);
        for (int i2 = 0; i2 < bigArray.length; ++i2) {
            System.arraycopy(array, (int)BigArrays.start(i2), bigArray[i2], 0, bigArray[i2].length);
        }
        return bigArray;
    }

    public static short[][] ensureCapacity(short[][] array, long length) {
        return BigArrays.ensureCapacity(array, length, BigArrays.length(array));
    }

    public static short[][] forceCapacity(short[][] array, long length, long preserve) {
        BigArrays.ensureLength(length);
        int valid = array.length - (array.length == 0 || array.length > 0 && array[array.length - 1].length == 0x8000000 ? 0 : 1);
        int baseLength = (int)(length + 0x7FFFFFFL >>> 27);
        short[][] base = (short[][])Arrays.copyOf(array, baseLength);
        int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i2 = valid; i2 < baseLength - 1; ++i2) {
                base[i2] = new short[0x8000000];
            }
            base[baseLength - 1] = new short[residual];
        } else {
            for (int i3 = valid; i3 < baseLength; ++i3) {
                base[i3] = new short[0x8000000];
            }
        }
        if (preserve - (long)valid * 0x8000000L > 0L) {
            BigArrays.copy(array, (long)valid * 0x8000000L, base, (long)valid * 0x8000000L, preserve - (long)valid * 0x8000000L);
        }
        return base;
    }

    public static short[][] ensureCapacity(short[][] array, long length, long preserve) {
        return length > BigArrays.length(array) ? BigArrays.forceCapacity(array, length, preserve) : array;
    }

    public static short[][] grow(short[][] array, long length) {
        long oldLength = BigArrays.length(array);
        return length > oldLength ? BigArrays.grow(array, length, oldLength) : array;
    }

    public static short[][] grow(short[][] array, long length, long preserve) {
        long oldLength = BigArrays.length(array);
        return length > oldLength ? BigArrays.ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
    }

    public static short[][] trim(short[][] array, long length) {
        BigArrays.ensureLength(length);
        long oldLength = BigArrays.length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int)(length + 0x7FFFFFFL >>> 27);
        short[][] base = (short[][])Arrays.copyOf(array, baseLength);
        int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = ShortArrays.trim((short[])base[baseLength - 1], (int)residual);
        }
        return base;
    }

    public static short[][] setLength(short[][] array, long length) {
        long oldLength = BigArrays.length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return BigArrays.trim(array, length);
        }
        return BigArrays.ensureCapacity(array, length);
    }

    public static short[][] copy(short[][] array, long offset, long length) {
        BigArrays.ensureOffsetLength(array, offset, length);
        short[][] a2 = ShortBigArrays.newBigArray((long)length);
        BigArrays.copy(array, offset, a2, 0L, length);
        return a2;
    }

    public static short[][] copy(short[][] array) {
        short[][] base = (short[][])array.clone();
        int i2 = base.length;
        while (i2-- != 0) {
            base[i2] = (short[])array[i2].clone();
        }
        return base;
    }

    public static void fill(short[][] array, short value) {
        int i2 = array.length;
        while (i2-- != 0) {
            Arrays.fill(array[i2], value);
        }
    }

    public static void fill(short[][] array, long from, long to, short value) {
        long length = BigArrays.length(array);
        BigArrays.ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        int fromSegment = BigArrays.segment(from);
        int toSegment = BigArrays.segment(to);
        int fromDispl = BigArrays.displacement(from);
        int toDispl = BigArrays.displacement(to);
        if (fromSegment == toSegment) {
            Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (--toSegment > fromSegment) {
            Arrays.fill(array[toSegment], value);
        }
        Arrays.fill(array[fromSegment], fromDispl, 0x8000000, value);
    }

    public static boolean equals(short[][] a1, short[][] a2) {
        if (BigArrays.length(a1) != BigArrays.length(a2)) {
            return false;
        }
        int i2 = a1.length;
        while (i2-- != 0) {
            short[] t2 = a1[i2];
            short[] u2 = a2[i2];
            int j2 = t2.length;
            while (j2-- != 0) {
                if (t2[j2] == u2[j2]) continue;
                return false;
            }
        }
        return true;
    }

    public static String toString(short[][] a2) {
        if (a2 == null) {
            return "null";
        }
        long last = BigArrays.length(a2) - 1L;
        if (last == -1L) {
            return "[]";
        }
        StringBuilder b2 = new StringBuilder();
        b2.append('[');
        long i2 = 0L;
        while (true) {
            b2.append(String.valueOf(BigArrays.get(a2, i2)));
            if (i2 == last) {
                return b2.append(']').toString();
            }
            b2.append(", ");
            ++i2;
        }
    }

    public static void ensureFromTo(short[][] a2, long from, long to) {
        BigArrays.ensureFromTo(BigArrays.length(a2), from, to);
    }

    public static void ensureOffsetLength(short[][] a2, long offset, long length) {
        BigArrays.ensureOffsetLength(BigArrays.length(a2), offset, length);
    }

    public static void ensureSameLength(short[][] a2, short[][] b2) {
        if (BigArrays.length(a2) != BigArrays.length(b2)) {
            throw new IllegalArgumentException("Array size mismatch: " + BigArrays.length(a2) + " != " + BigArrays.length(b2));
        }
    }

    public static short[][] shuffle(short[][] a2, long from, long to, Random random) {
        long i2 = to - from;
        while (i2-- != 0L) {
            long p2 = (random.nextLong() & Long.MAX_VALUE) % (i2 + 1L);
            short t2 = BigArrays.get(a2, from + i2);
            BigArrays.set(a2, from + i2, BigArrays.get(a2, from + p2));
            BigArrays.set(a2, from + p2, t2);
        }
        return a2;
    }

    public static short[][] shuffle(short[][] a2, Random random) {
        long i2 = BigArrays.length(a2);
        while (i2-- != 0L) {
            long p2 = (random.nextLong() & Long.MAX_VALUE) % (i2 + 1L);
            short t2 = BigArrays.get(a2, i2);
            BigArrays.set(a2, i2, BigArrays.get(a2, p2));
            BigArrays.set(a2, p2, t2);
        }
        return a2;
    }

    public static char get(char[][] array, long index) {
        return array[BigArrays.segment(index)][BigArrays.displacement(index)];
    }

    public static void set(char[][] array, long index, char value) {
        array[BigArrays.segment((long)index)][BigArrays.displacement((long)index)] = value;
    }

    public static void swap(char[][] array, long first, long second) {
        char t2 = array[BigArrays.segment(first)][BigArrays.displacement(first)];
        array[BigArrays.segment((long)first)][BigArrays.displacement((long)first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
        array[BigArrays.segment((long)second)][BigArrays.displacement((long)second)] = t2;
    }

    public static char[][] reverse(char[][] a2) {
        long length = BigArrays.length(a2);
        long i2 = length / 2L;
        while (i2-- != 0L) {
            BigArrays.swap(a2, i2, length - i2 - 1L);
        }
        return a2;
    }

    public static void add(char[][] array, long index, char incr) {
        char[] cArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        cArray[n2] = (char)(cArray[n2] + incr);
    }

    public static void mul(char[][] array, long index, char factor) {
        char[] cArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        cArray[n2] = (char)(cArray[n2] * factor);
    }

    public static void incr(char[][] array, long index) {
        char[] cArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        cArray[n2] = (char)(cArray[n2] + '\u0001');
    }

    public static void decr(char[][] array, long index) {
        char[] cArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        cArray[n2] = (char)(cArray[n2] - '\u0001');
    }

    public static long length(char[][] array) {
        int length = array.length;
        return length == 0 ? 0L : BigArrays.start(length - 1) + (long)array[length - 1].length;
    }

    public static void copy(char[][] srcArray, long srcPos, char[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = BigArrays.segment(srcPos);
            int destSegment = BigArrays.segment(destPos);
            int srcDispl = BigArrays.displacement(srcPos);
            int destDispl = BigArrays.displacement(destPos);
            while (length > 0L) {
                int l2 = (int)Math.min(length, (long)Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l2 == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l2);
                if ((srcDispl += l2) == 0x8000000) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l2) == 0x8000000) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= (long)l2;
            }
        } else {
            int srcSegment = BigArrays.segment(srcPos + length);
            int destSegment = BigArrays.segment(destPos + length);
            int srcDispl = BigArrays.displacement(srcPos + length);
            int destDispl = BigArrays.displacement(destPos + length);
            while (length > 0L) {
                int l3;
                if (srcDispl == 0) {
                    srcDispl = 0x8000000;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 0x8000000;
                    --destSegment;
                }
                if ((l3 = (int)Math.min(length, (long)Math.min(srcDispl, destDispl))) == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl - l3, destArray[destSegment], destDispl - l3, l3);
                srcDispl -= l3;
                destDispl -= l3;
                length -= (long)l3;
            }
        }
    }

    public static void copyFromBig(char[][] srcArray, long srcPos, char[] destArray, int destPos, int length) {
        int srcSegment = BigArrays.segment(srcPos);
        int srcDispl = BigArrays.displacement(srcPos);
        while (length > 0) {
            int l2 = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l2);
            if ((srcDispl += l2) == 0x8000000) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l2;
            length -= l2;
        }
    }

    public static void copyToBig(char[] srcArray, int srcPos, char[][] destArray, long destPos, long length) {
        int destSegment = BigArrays.segment(destPos);
        int destDispl = BigArrays.displacement(destPos);
        while (length > 0L) {
            int l2 = (int)Math.min((long)(destArray[destSegment].length - destDispl), length);
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l2);
            if ((destDispl += l2) == 0x8000000) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l2;
            length -= (long)l2;
        }
    }

    public static char[][] wrap(char[] array) {
        if (array.length == 0) {
            return CharBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 0x8000000) {
            return new char[][]{array};
        }
        char[][] bigArray = CharBigArrays.newBigArray((long)array.length);
        for (int i2 = 0; i2 < bigArray.length; ++i2) {
            System.arraycopy(array, (int)BigArrays.start(i2), bigArray[i2], 0, bigArray[i2].length);
        }
        return bigArray;
    }

    public static char[][] ensureCapacity(char[][] array, long length) {
        return BigArrays.ensureCapacity(array, length, BigArrays.length(array));
    }

    public static char[][] forceCapacity(char[][] array, long length, long preserve) {
        BigArrays.ensureLength(length);
        int valid = array.length - (array.length == 0 || array.length > 0 && array[array.length - 1].length == 0x8000000 ? 0 : 1);
        int baseLength = (int)(length + 0x7FFFFFFL >>> 27);
        char[][] base = (char[][])Arrays.copyOf(array, baseLength);
        int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i2 = valid; i2 < baseLength - 1; ++i2) {
                base[i2] = new char[0x8000000];
            }
            base[baseLength - 1] = new char[residual];
        } else {
            for (int i3 = valid; i3 < baseLength; ++i3) {
                base[i3] = new char[0x8000000];
            }
        }
        if (preserve - (long)valid * 0x8000000L > 0L) {
            BigArrays.copy(array, (long)valid * 0x8000000L, base, (long)valid * 0x8000000L, preserve - (long)valid * 0x8000000L);
        }
        return base;
    }

    public static char[][] ensureCapacity(char[][] array, long length, long preserve) {
        return length > BigArrays.length(array) ? BigArrays.forceCapacity(array, length, preserve) : array;
    }

    public static char[][] grow(char[][] array, long length) {
        long oldLength = BigArrays.length(array);
        return length > oldLength ? BigArrays.grow(array, length, oldLength) : array;
    }

    public static char[][] grow(char[][] array, long length, long preserve) {
        long oldLength = BigArrays.length(array);
        return length > oldLength ? BigArrays.ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
    }

    public static char[][] trim(char[][] array, long length) {
        BigArrays.ensureLength(length);
        long oldLength = BigArrays.length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int)(length + 0x7FFFFFFL >>> 27);
        char[][] base = (char[][])Arrays.copyOf(array, baseLength);
        int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = CharArrays.trim((char[])base[baseLength - 1], (int)residual);
        }
        return base;
    }

    public static char[][] setLength(char[][] array, long length) {
        long oldLength = BigArrays.length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return BigArrays.trim(array, length);
        }
        return BigArrays.ensureCapacity(array, length);
    }

    public static char[][] copy(char[][] array, long offset, long length) {
        BigArrays.ensureOffsetLength(array, offset, length);
        char[][] a2 = CharBigArrays.newBigArray((long)length);
        BigArrays.copy(array, offset, a2, 0L, length);
        return a2;
    }

    public static char[][] copy(char[][] array) {
        char[][] base = (char[][])array.clone();
        int i2 = base.length;
        while (i2-- != 0) {
            base[i2] = (char[])array[i2].clone();
        }
        return base;
    }

    public static void fill(char[][] array, char value) {
        int i2 = array.length;
        while (i2-- != 0) {
            Arrays.fill(array[i2], value);
        }
    }

    public static void fill(char[][] array, long from, long to, char value) {
        long length = BigArrays.length(array);
        BigArrays.ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        int fromSegment = BigArrays.segment(from);
        int toSegment = BigArrays.segment(to);
        int fromDispl = BigArrays.displacement(from);
        int toDispl = BigArrays.displacement(to);
        if (fromSegment == toSegment) {
            Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (--toSegment > fromSegment) {
            Arrays.fill(array[toSegment], value);
        }
        Arrays.fill(array[fromSegment], fromDispl, 0x8000000, value);
    }

    public static boolean equals(char[][] a1, char[][] a2) {
        if (BigArrays.length(a1) != BigArrays.length(a2)) {
            return false;
        }
        int i2 = a1.length;
        while (i2-- != 0) {
            char[] t2 = a1[i2];
            char[] u2 = a2[i2];
            int j2 = t2.length;
            while (j2-- != 0) {
                if (t2[j2] == u2[j2]) continue;
                return false;
            }
        }
        return true;
    }

    public static String toString(char[][] a2) {
        if (a2 == null) {
            return "null";
        }
        long last = BigArrays.length(a2) - 1L;
        if (last == -1L) {
            return "[]";
        }
        StringBuilder b2 = new StringBuilder();
        b2.append('[');
        long i2 = 0L;
        while (true) {
            b2.append(String.valueOf(BigArrays.get(a2, i2)));
            if (i2 == last) {
                return b2.append(']').toString();
            }
            b2.append(", ");
            ++i2;
        }
    }

    public static void ensureFromTo(char[][] a2, long from, long to) {
        BigArrays.ensureFromTo(BigArrays.length(a2), from, to);
    }

    public static void ensureOffsetLength(char[][] a2, long offset, long length) {
        BigArrays.ensureOffsetLength(BigArrays.length(a2), offset, length);
    }

    public static void ensureSameLength(char[][] a2, char[][] b2) {
        if (BigArrays.length(a2) != BigArrays.length(b2)) {
            throw new IllegalArgumentException("Array size mismatch: " + BigArrays.length(a2) + " != " + BigArrays.length(b2));
        }
    }

    public static char[][] shuffle(char[][] a2, long from, long to, Random random) {
        long i2 = to - from;
        while (i2-- != 0L) {
            long p2 = (random.nextLong() & Long.MAX_VALUE) % (i2 + 1L);
            char t2 = BigArrays.get(a2, from + i2);
            BigArrays.set(a2, from + i2, BigArrays.get(a2, from + p2));
            BigArrays.set(a2, from + p2, t2);
        }
        return a2;
    }

    public static char[][] shuffle(char[][] a2, Random random) {
        long i2 = BigArrays.length(a2);
        while (i2-- != 0L) {
            long p2 = (random.nextLong() & Long.MAX_VALUE) % (i2 + 1L);
            char t2 = BigArrays.get(a2, i2);
            BigArrays.set(a2, i2, BigArrays.get(a2, p2));
            BigArrays.set(a2, p2, t2);
        }
        return a2;
    }

    public static float get(float[][] array, long index) {
        return array[BigArrays.segment(index)][BigArrays.displacement(index)];
    }

    public static void set(float[][] array, long index, float value) {
        array[BigArrays.segment((long)index)][BigArrays.displacement((long)index)] = value;
    }

    public static void swap(float[][] array, long first, long second) {
        float t2 = array[BigArrays.segment(first)][BigArrays.displacement(first)];
        array[BigArrays.segment((long)first)][BigArrays.displacement((long)first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
        array[BigArrays.segment((long)second)][BigArrays.displacement((long)second)] = t2;
    }

    public static float[][] reverse(float[][] a2) {
        long length = BigArrays.length(a2);
        long i2 = length / 2L;
        while (i2-- != 0L) {
            BigArrays.swap(a2, i2, length - i2 - 1L);
        }
        return a2;
    }

    public static void add(float[][] array, long index, float incr) {
        float[] fArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        fArray[n2] = fArray[n2] + incr;
    }

    public static void mul(float[][] array, long index, float factor) {
        float[] fArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        fArray[n2] = fArray[n2] * factor;
    }

    public static void incr(float[][] array, long index) {
        float[] fArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        fArray[n2] = fArray[n2] + 1.0f;
    }

    public static void decr(float[][] array, long index) {
        float[] fArray = array[BigArrays.segment(index)];
        int n2 = BigArrays.displacement(index);
        fArray[n2] = fArray[n2] - 1.0f;
    }

    public static long length(float[][] array) {
        int length = array.length;
        return length == 0 ? 0L : BigArrays.start(length - 1) + (long)array[length - 1].length;
    }

    public static void copy(float[][] srcArray, long srcPos, float[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = BigArrays.segment(srcPos);
            int destSegment = BigArrays.segment(destPos);
            int srcDispl = BigArrays.displacement(srcPos);
            int destDispl = BigArrays.displacement(destPos);
            while (length > 0L) {
                int l2 = (int)Math.min(length, (long)Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l2 == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l2);
                if ((srcDispl += l2) == 0x8000000) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l2) == 0x8000000) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= (long)l2;
            }
        } else {
            int srcSegment = BigArrays.segment(srcPos + length);
            int destSegment = BigArrays.segment(destPos + length);
            int srcDispl = BigArrays.displacement(srcPos + length);
            int destDispl = BigArrays.displacement(destPos + length);
            while (length > 0L) {
                int l3;
                if (srcDispl == 0) {
                    srcDispl = 0x8000000;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 0x8000000;
                    --destSegment;
                }
                if ((l3 = (int)Math.min(length, (long)Math.min(srcDispl, destDispl))) == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl - l3, destArray[destSegment], destDispl - l3, l3);
                srcDispl -= l3;
                destDispl -= l3;
                length -= (long)l3;
            }
        }
    }

    public static void copyFromBig(float[][] srcArray, long srcPos, float[] destArray, int destPos, int length) {
        int srcSegment = BigArrays.segment(srcPos);
        int srcDispl = BigArrays.displacement(srcPos);
        while (length > 0) {
            int l2 = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l2);
            if ((srcDispl += l2) == 0x8000000) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l2;
            length -= l2;
        }
    }

    public static void copyToBig(float[] srcArray, int srcPos, float[][] destArray, long destPos, long length) {
        int destSegment = BigArrays.segment(destPos);
        int destDispl = BigArrays.displacement(destPos);
        while (length > 0L) {
            int l2 = (int)Math.min((long)(destArray[destSegment].length - destDispl), length);
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l2);
            if ((destDispl += l2) == 0x8000000) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l2;
            length -= (long)l2;
        }
    }

    public static float[][] wrap(float[] array) {
        if (array.length == 0) {
            return FloatBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 0x8000000) {
            return new float[][]{array};
        }
        float[][] bigArray = FloatBigArrays.newBigArray((long)array.length);
        for (int i2 = 0; i2 < bigArray.length; ++i2) {
            System.arraycopy(array, (int)BigArrays.start(i2), bigArray[i2], 0, bigArray[i2].length);
        }
        return bigArray;
    }

    public static float[][] ensureCapacity(float[][] array, long length) {
        return BigArrays.ensureCapacity(array, length, BigArrays.length(array));
    }

    public static float[][] forceCapacity(float[][] array, long length, long preserve) {
        BigArrays.ensureLength(length);
        int valid = array.length - (array.length == 0 || array.length > 0 && array[array.length - 1].length == 0x8000000 ? 0 : 1);
        int baseLength = (int)(length + 0x7FFFFFFL >>> 27);
        float[][] base = (float[][])Arrays.copyOf(array, baseLength);
        int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i2 = valid; i2 < baseLength - 1; ++i2) {
                base[i2] = new float[0x8000000];
            }
            base[baseLength - 1] = new float[residual];
        } else {
            for (int i3 = valid; i3 < baseLength; ++i3) {
                base[i3] = new float[0x8000000];
            }
        }
        if (preserve - (long)valid * 0x8000000L > 0L) {
            BigArrays.copy(array, (long)valid * 0x8000000L, base, (long)valid * 0x8000000L, preserve - (long)valid * 0x8000000L);
        }
        return base;
    }

    public static float[][] ensureCapacity(float[][] array, long length, long preserve) {
        return length > BigArrays.length(array) ? BigArrays.forceCapacity(array, length, preserve) : array;
    }

    public static float[][] grow(float[][] array, long length) {
        long oldLength = BigArrays.length(array);
        return length > oldLength ? BigArrays.grow(array, length, oldLength) : array;
    }

    public static float[][] grow(float[][] array, long length, long preserve) {
        long oldLength = BigArrays.length(array);
        return length > oldLength ? BigArrays.ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
    }

    public static float[][] trim(float[][] array, long length) {
        BigArrays.ensureLength(length);
        long oldLength = BigArrays.length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int)(length + 0x7FFFFFFL >>> 27);
        float[][] base = (float[][])Arrays.copyOf(array, baseLength);
        int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = FloatArrays.trim((float[])base[baseLength - 1], (int)residual);
        }
        return base;
    }

    public static float[][] setLength(float[][] array, long length) {
        long oldLength = BigArrays.length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return BigArrays.trim(array, length);
        }
        return BigArrays.ensureCapacity(array, length);
    }

    public static float[][] copy(float[][] array, long offset, long length) {
        BigArrays.ensureOffsetLength(array, offset, length);
        float[][] a2 = FloatBigArrays.newBigArray((long)length);
        BigArrays.copy(array, offset, a2, 0L, length);
        return a2;
    }

    public static float[][] copy(float[][] array) {
        float[][] base = (float[][])array.clone();
        int i2 = base.length;
        while (i2-- != 0) {
            base[i2] = (float[])array[i2].clone();
        }
        return base;
    }

    public static void fill(float[][] array, float value) {
        int i2 = array.length;
        while (i2-- != 0) {
            Arrays.fill(array[i2], value);
        }
    }

    public static void fill(float[][] array, long from, long to, float value) {
        long length = BigArrays.length(array);
        BigArrays.ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        int fromSegment = BigArrays.segment(from);
        int toSegment = BigArrays.segment(to);
        int fromDispl = BigArrays.displacement(from);
        int toDispl = BigArrays.displacement(to);
        if (fromSegment == toSegment) {
            Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (--toSegment > fromSegment) {
            Arrays.fill(array[toSegment], value);
        }
        Arrays.fill(array[fromSegment], fromDispl, 0x8000000, value);
    }

    public static boolean equals(float[][] a1, float[][] a2) {
        if (BigArrays.length(a1) != BigArrays.length(a2)) {
            return false;
        }
        int i2 = a1.length;
        while (i2-- != 0) {
            float[] t2 = a1[i2];
            float[] u2 = a2[i2];
            int j2 = t2.length;
            while (j2-- != 0) {
                if (Float.floatToIntBits(t2[j2]) == Float.floatToIntBits(u2[j2])) continue;
                return false;
            }
        }
        return true;
    }

    public static String toString(float[][] a2) {
        if (a2 == null) {
            return "null";
        }
        long last = BigArrays.length(a2) - 1L;
        if (last == -1L) {
            return "[]";
        }
        StringBuilder b2 = new StringBuilder();
        b2.append('[');
        long i2 = 0L;
        while (true) {
            b2.append(String.valueOf(BigArrays.get(a2, i2)));
            if (i2 == last) {
                return b2.append(']').toString();
            }
            b2.append(", ");
            ++i2;
        }
    }

    public static void ensureFromTo(float[][] a2, long from, long to) {
        BigArrays.ensureFromTo(BigArrays.length(a2), from, to);
    }

    public static void ensureOffsetLength(float[][] a2, long offset, long length) {
        BigArrays.ensureOffsetLength(BigArrays.length(a2), offset, length);
    }

    public static void ensureSameLength(float[][] a2, float[][] b2) {
        if (BigArrays.length(a2) != BigArrays.length(b2)) {
            throw new IllegalArgumentException("Array size mismatch: " + BigArrays.length(a2) + " != " + BigArrays.length(b2));
        }
    }

    public static float[][] shuffle(float[][] a2, long from, long to, Random random) {
        long i2 = to - from;
        while (i2-- != 0L) {
            long p2 = (random.nextLong() & Long.MAX_VALUE) % (i2 + 1L);
            float t2 = BigArrays.get(a2, from + i2);
            BigArrays.set(a2, from + i2, BigArrays.get(a2, from + p2));
            BigArrays.set(a2, from + p2, t2);
        }
        return a2;
    }

    public static float[][] shuffle(float[][] a2, Random random) {
        long i2 = BigArrays.length(a2);
        while (i2-- != 0L) {
            long p2 = (random.nextLong() & Long.MAX_VALUE) % (i2 + 1L);
            float t2 = BigArrays.get(a2, i2);
            BigArrays.set(a2, i2, BigArrays.get(a2, p2));
            BigArrays.set(a2, p2, t2);
        }
        return a2;
    }

    public static <K> K get(K[][] array, long index) {
        return array[BigArrays.segment(index)][BigArrays.displacement(index)];
    }

    public static <K> void set(K[][] array, long index, K value) {
        array[BigArrays.segment((long)index)][BigArrays.displacement((long)index)] = value;
    }

    public static <K> void swap(K[][] array, long first, long second) {
        K t2 = array[BigArrays.segment(first)][BigArrays.displacement(first)];
        array[BigArrays.segment((long)first)][BigArrays.displacement((long)first)] = array[BigArrays.segment(second)][BigArrays.displacement(second)];
        array[BigArrays.segment((long)second)][BigArrays.displacement((long)second)] = t2;
    }

    public static <K> K[][] reverse(K[][] a2) {
        long length = BigArrays.length(a2);
        long i2 = length / 2L;
        while (i2-- != 0L) {
            BigArrays.swap(a2, i2, length - i2 - 1L);
        }
        return a2;
    }

    public static <K> long length(K[][] array) {
        int length = array.length;
        return length == 0 ? 0L : BigArrays.start(length - 1) + (long)array[length - 1].length;
    }

    public static <K> void copy(K[][] srcArray, long srcPos, K[][] destArray, long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = BigArrays.segment(srcPos);
            int destSegment = BigArrays.segment(destPos);
            int srcDispl = BigArrays.displacement(srcPos);
            int destDispl = BigArrays.displacement(destPos);
            while (length > 0L) {
                int l2 = (int)Math.min(length, (long)Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l2 == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l2);
                if ((srcDispl += l2) == 0x8000000) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l2) == 0x8000000) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= (long)l2;
            }
        } else {
            int srcSegment = BigArrays.segment(srcPos + length);
            int destSegment = BigArrays.segment(destPos + length);
            int srcDispl = BigArrays.displacement(srcPos + length);
            int destDispl = BigArrays.displacement(destPos + length);
            while (length > 0L) {
                int l3;
                if (srcDispl == 0) {
                    srcDispl = 0x8000000;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 0x8000000;
                    --destSegment;
                }
                if ((l3 = (int)Math.min(length, (long)Math.min(srcDispl, destDispl))) == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl - l3, destArray[destSegment], destDispl - l3, l3);
                srcDispl -= l3;
                destDispl -= l3;
                length -= (long)l3;
            }
        }
    }

    public static <K> void copyFromBig(K[][] srcArray, long srcPos, K[] destArray, int destPos, int length) {
        int srcSegment = BigArrays.segment(srcPos);
        int srcDispl = BigArrays.displacement(srcPos);
        while (length > 0) {
            int l2 = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l2);
            if ((srcDispl += l2) == 0x8000000) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l2;
            length -= l2;
        }
    }

    public static <K> void copyToBig(K[] srcArray, int srcPos, K[][] destArray, long destPos, long length) {
        int destSegment = BigArrays.segment(destPos);
        int destDispl = BigArrays.displacement(destPos);
        while (length > 0L) {
            int l2 = (int)Math.min((long)(destArray[destSegment].length - destDispl), length);
            if (l2 == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l2);
            if ((destDispl += l2) == 0x8000000) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l2;
            length -= (long)l2;
        }
    }

    public static <K> K[][] wrap(K[] array) {
        if (array.length == 0 && array.getClass() == Object[].class) {
            return ObjectBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 0x8000000) {
            Object[][] bigArray = (Object[][])Array.newInstance(array.getClass(), 1);
            bigArray[0] = array;
            return bigArray;
        }
        Object[][] bigArray = ObjectBigArrays.newBigArray(array.getClass(), (long)array.length);
        for (int i2 = 0; i2 < bigArray.length; ++i2) {
            System.arraycopy(array, (int)BigArrays.start(i2), bigArray[i2], 0, bigArray[i2].length);
        }
        return bigArray;
    }

    public static <K> K[][] ensureCapacity(K[][] array, long length) {
        return BigArrays.ensureCapacity(array, length, BigArrays.length(array));
    }

    public static <K> K[][] forceCapacity(K[][] array, long length, long preserve) {
        BigArrays.ensureLength(length);
        int valid = array.length - (array.length == 0 || array.length > 0 && array[array.length - 1].length == 0x8000000 ? 0 : 1);
        int baseLength = (int)(length + 0x7FFFFFFL >>> 27);
        Object[][] base = (Object[][])Arrays.copyOf(array, baseLength);
        Class<?> componentType = array.getClass().getComponentType();
        int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i2 = valid; i2 < baseLength - 1; ++i2) {
                base[i2] = (Object[])Array.newInstance(componentType.getComponentType(), 0x8000000);
            }
            base[baseLength - 1] = (Object[])Array.newInstance(componentType.getComponentType(), residual);
        } else {
            for (int i3 = valid; i3 < baseLength; ++i3) {
                base[i3] = (Object[])Array.newInstance(componentType.getComponentType(), 0x8000000);
            }
        }
        if (preserve - (long)valid * 0x8000000L > 0L) {
            BigArrays.copy(array, (long)valid * 0x8000000L, base, (long)valid * 0x8000000L, preserve - (long)valid * 0x8000000L);
        }
        return base;
    }

    public static <K> K[][] ensureCapacity(K[][] array, long length, long preserve) {
        return length > BigArrays.length(array) ? BigArrays.forceCapacity(array, length, preserve) : array;
    }

    public static <K> K[][] grow(K[][] array, long length) {
        long oldLength = BigArrays.length(array);
        return length > oldLength ? BigArrays.grow(array, length, oldLength) : array;
    }

    public static <K> K[][] grow(K[][] array, long length, long preserve) {
        long oldLength = BigArrays.length(array);
        return length > oldLength ? BigArrays.ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
    }

    public static <K> K[][] trim(K[][] array, long length) {
        BigArrays.ensureLength(length);
        long oldLength = BigArrays.length(array);
        if (length >= oldLength) {
            return array;
        }
        int baseLength = (int)(length + 0x7FFFFFFL >>> 27);
        Object[][] base = (Object[][])Arrays.copyOf(array, baseLength);
        int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = ObjectArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }

    public static <K> K[][] setLength(K[][] array, long length) {
        long oldLength = BigArrays.length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return BigArrays.trim(array, length);
        }
        return BigArrays.ensureCapacity(array, length);
    }

    public static <K> K[][] copy(K[][] array, long offset, long length) {
        BigArrays.ensureOffsetLength(array, offset, length);
        Object[][] a2 = ObjectBigArrays.newBigArray((Object[][])array, (long)length);
        BigArrays.copy(array, offset, a2, 0L, length);
        return a2;
    }

    public static <K> K[][] copy(K[][] array) {
        Object[][] base = (Object[][])array.clone();
        int i2 = base.length;
        while (i2-- != 0) {
            base[i2] = (Object[])array[i2].clone();
        }
        return base;
    }

    public static <K> void fill(K[][] array, K value) {
        int i2 = array.length;
        while (i2-- != 0) {
            Arrays.fill(array[i2], value);
        }
    }

    public static <K> void fill(K[][] array, long from, long to, K value) {
        long length = BigArrays.length(array);
        BigArrays.ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        int fromSegment = BigArrays.segment(from);
        int toSegment = BigArrays.segment(to);
        int fromDispl = BigArrays.displacement(from);
        int toDispl = BigArrays.displacement(to);
        if (fromSegment == toSegment) {
            Arrays.fill(array[fromSegment], fromDispl, toDispl, value);
            return;
        }
        if (toDispl != 0) {
            Arrays.fill(array[toSegment], 0, toDispl, value);
        }
        while (--toSegment > fromSegment) {
            Arrays.fill(array[toSegment], value);
        }
        Arrays.fill(array[fromSegment], fromDispl, 0x8000000, value);
    }

    public static <K> boolean equals(K[][] a1, K[][] a2) {
        if (BigArrays.length(a1) != BigArrays.length(a2)) {
            return false;
        }
        int i2 = a1.length;
        while (i2-- != 0) {
            K[] t2 = a1[i2];
            K[] u2 = a2[i2];
            int j2 = t2.length;
            while (j2-- != 0) {
                if (Objects.equals(t2[j2], u2[j2])) continue;
                return false;
            }
        }
        return true;
    }

    public static <K> String toString(K[][] a2) {
        if (a2 == null) {
            return "null";
        }
        long last = BigArrays.length(a2) - 1L;
        if (last == -1L) {
            return "[]";
        }
        StringBuilder b2 = new StringBuilder();
        b2.append('[');
        long i2 = 0L;
        while (true) {
            b2.append(String.valueOf(BigArrays.get(a2, i2)));
            if (i2 == last) {
                return b2.append(']').toString();
            }
            b2.append(", ");
            ++i2;
        }
    }

    public static <K> void ensureFromTo(K[][] a2, long from, long to) {
        BigArrays.ensureFromTo(BigArrays.length(a2), from, to);
    }

    public static <K> void ensureOffsetLength(K[][] a2, long offset, long length) {
        BigArrays.ensureOffsetLength(BigArrays.length(a2), offset, length);
    }

    public static <K> void ensureSameLength(K[][] a2, K[][] b2) {
        if (BigArrays.length(a2) != BigArrays.length(b2)) {
            throw new IllegalArgumentException("Array size mismatch: " + BigArrays.length(a2) + " != " + BigArrays.length(b2));
        }
    }

    public static <K> K[][] shuffle(K[][] a2, long from, long to, Random random) {
        long i2 = to - from;
        while (i2-- != 0L) {
            long p2 = (random.nextLong() & Long.MAX_VALUE) % (i2 + 1L);
            K t2 = BigArrays.get(a2, from + i2);
            BigArrays.set(a2, from + i2, BigArrays.get(a2, from + p2));
            BigArrays.set(a2, from + p2, t2);
        }
        return a2;
    }

    public static <K> K[][] shuffle(K[][] a2, Random random) {
        long i2 = BigArrays.length(a2);
        while (i2-- != 0L) {
            long p2 = (random.nextLong() & Long.MAX_VALUE) % (i2 + 1L);
            K t2 = BigArrays.get(a2, i2);
            BigArrays.set(a2, i2, BigArrays.get(a2, p2));
            BigArrays.set(a2, p2, t2);
        }
        return a2;
    }

    public static void main(String[] arg) {
        int[][] a2 = IntBigArrays.newBigArray((long)(1L << Integer.parseInt(arg[0])));
        int k2 = 10;
        while (k2-- != 0) {
            long start = -System.currentTimeMillis();
            long x2 = 0L;
            long i2 = BigArrays.length(a2);
            while (i2-- != 0L) {
                x2 ^= i2 ^ (long)BigArrays.get(a2, i2);
            }
            if (x2 == 0L) {
                System.err.println();
            }
            System.out.println("Single loop: " + (start + System.currentTimeMillis()) + "ms");
            start = -System.currentTimeMillis();
            long y2 = 0L;
            int i22 = a2.length;
            while (i22-- != 0) {
                int[] t2 = a2[i22];
                int d2 = t2.length;
                while (d2-- != 0) {
                    y2 ^= (long)t2[d2] ^ BigArrays.index(i22, d2);
                }
            }
            if (y2 == 0L) {
                System.err.println();
            }
            if (x2 != y2) {
                throw new AssertionError();
            }
            System.out.println("Double loop: " + (start + System.currentTimeMillis()) + "ms");
            long z2 = 0L;
            long j2 = BigArrays.length(a2);
            int i3 = a2.length;
            while (i3-- != 0) {
                int[] t3 = a2[i3];
                int d3 = t3.length;
                while (d3-- != 0) {
                    y2 ^= (long)t3[d3] ^ --j2;
                }
            }
            if (z2 == 0L) {
                System.err.println();
            }
            if (x2 != z2) {
                throw new AssertionError();
            }
            System.out.println("Double loop (with additional index): " + (start + System.currentTimeMillis()) + "ms");
        }
    }
}

