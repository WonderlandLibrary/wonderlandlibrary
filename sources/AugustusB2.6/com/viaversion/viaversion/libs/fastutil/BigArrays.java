// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil;

import java.util.Objects;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectArrays;
import java.lang.reflect.Array;
import com.viaversion.viaversion.libs.fastutil.objects.ObjectBigArrays;
import com.viaversion.viaversion.libs.fastutil.floats.FloatArrays;
import com.viaversion.viaversion.libs.fastutil.floats.FloatBigArrays;
import com.viaversion.viaversion.libs.fastutil.chars.CharArrays;
import com.viaversion.viaversion.libs.fastutil.chars.CharBigArrays;
import com.viaversion.viaversion.libs.fastutil.shorts.ShortArrays;
import com.viaversion.viaversion.libs.fastutil.shorts.ShortBigArrays;
import com.viaversion.viaversion.libs.fastutil.booleans.BooleanArrays;
import com.viaversion.viaversion.libs.fastutil.booleans.BooleanBigArrays;
import com.viaversion.viaversion.libs.fastutil.doubles.DoubleArrays;
import com.viaversion.viaversion.libs.fastutil.doubles.DoubleBigArrays;
import com.viaversion.viaversion.libs.fastutil.longs.LongArrays;
import com.viaversion.viaversion.libs.fastutil.longs.LongBigArrays;
import java.util.concurrent.atomic.AtomicLongArray;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrays;
import com.viaversion.viaversion.libs.fastutil.ints.IntBigArrays;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.Random;
import com.viaversion.viaversion.libs.fastutil.bytes.ByteArrays;
import java.util.Arrays;
import com.viaversion.viaversion.libs.fastutil.bytes.ByteBigArrays;
import com.viaversion.viaversion.libs.fastutil.longs.LongComparator;

public class BigArrays
{
    public static final int SEGMENT_SHIFT = 27;
    public static final int SEGMENT_SIZE = 134217728;
    public static final int SEGMENT_MASK = 134217727;
    private static final int SMALL = 7;
    private static final int MEDIUM = 40;
    
    protected BigArrays() {
    }
    
    public static int segment(final long index) {
        return (int)(index >>> 27);
    }
    
    public static int displacement(final long index) {
        return (int)(index & 0x7FFFFFFL);
    }
    
    public static long start(final int segment) {
        return (long)segment << 27;
    }
    
    public static long nearestSegmentStart(final long index, final long min, final long max) {
        final long lower = start(segment(index));
        final long upper = start(segment(index) + 1);
        if (upper >= max) {
            if (lower < min) {
                return index;
            }
            return lower;
        }
        else {
            if (lower < min) {
                return upper;
            }
            final long mid = lower + (upper - lower >> 1);
            return (index <= mid) ? lower : upper;
        }
    }
    
    public static long index(final int segment, final int displacement) {
        return start(segment) + displacement;
    }
    
    public static void ensureFromTo(final long bigArrayLength, final long from, final long to) {
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
    
    public static void ensureOffsetLength(final long bigArrayLength, final long offset, final long length) {
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
    
    public static void ensureLength(final long bigArrayLength) {
        if (bigArrayLength < 0L) {
            throw new IllegalArgumentException("Negative big-array size: " + bigArrayLength);
        }
        if (bigArrayLength >= 288230376017494016L) {
            throw new IllegalArgumentException("Big-array size too big: " + bigArrayLength);
        }
    }
    
    private static void inPlaceMerge(final long from, long mid, final long to, final LongComparator comp, final BigSwapper swapper) {
        if (from >= mid || mid >= to) {
            return;
        }
        if (to - from == 2L) {
            if (comp.compare(mid, from) < 0) {
                swapper.swap(from, mid);
            }
            return;
        }
        long firstCut;
        long secondCut;
        if (mid - from > to - mid) {
            firstCut = from + (mid - from) / 2L;
            secondCut = lowerBound(mid, to, firstCut, comp);
        }
        else {
            secondCut = mid + (to - mid) / 2L;
            firstCut = upperBound(from, mid, secondCut, comp);
        }
        final long first2 = firstCut;
        final long middle2 = mid;
        final long last2 = secondCut;
        if (middle2 != first2 && middle2 != last2) {
            long first3 = first2;
            long last3 = middle2;
            while (first3 < --last3) {
                swapper.swap(first3++, last3);
            }
            first3 = middle2;
            last3 = last2;
            while (first3 < --last3) {
                swapper.swap(first3++, last3);
            }
            first3 = first2;
            last3 = last2;
            while (first3 < --last3) {
                swapper.swap(first3++, last3);
            }
        }
        mid = firstCut + (secondCut - mid);
        inPlaceMerge(from, firstCut, mid, comp, swapper);
        inPlaceMerge(mid, secondCut, to, comp, swapper);
    }
    
    private static long lowerBound(long mid, final long to, final long firstCut, final LongComparator comp) {
        long len = to - mid;
        while (len > 0L) {
            final long half = len / 2L;
            final long middle = mid + half;
            if (comp.compare(middle, firstCut) < 0) {
                mid = middle + 1L;
                len -= half + 1L;
            }
            else {
                len = half;
            }
        }
        return mid;
    }
    
    private static long med3(final long a, final long b, final long c, final LongComparator comp) {
        final int ab = comp.compare(a, b);
        final int ac = comp.compare(a, c);
        final int bc = comp.compare(b, c);
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    public static void mergeSort(final long from, final long to, final LongComparator comp, final BigSwapper swapper) {
        final long length = to - from;
        if (length < 7L) {
            for (long i = from; i < to; ++i) {
                for (long j = i; j > from && comp.compare(j - 1L, j) > 0; --j) {
                    swapper.swap(j, j - 1L);
                }
            }
            return;
        }
        final long mid = from + to >>> 1;
        mergeSort(from, mid, comp, swapper);
        mergeSort(mid, to, comp, swapper);
        if (comp.compare(mid - 1L, mid) <= 0) {
            return;
        }
        inPlaceMerge(from, mid, to, comp, swapper);
    }
    
    public static void quickSort(final long from, final long to, final LongComparator comp, final BigSwapper swapper) {
        final long len = to - from;
        if (len < 7L) {
            for (long i = from; i < to; ++i) {
                for (long j = i; j > from && comp.compare(j - 1L, j) > 0; --j) {
                    swapper.swap(j, j - 1L);
                }
            }
            return;
        }
        long m = from + len / 2L;
        if (len > 7L) {
            long l = from;
            long n = to - 1L;
            if (len > 40L) {
                final long s = len / 8L;
                l = med3(l, l + s, l + 2L * s, comp);
                m = med3(m - s, m, m + s, comp);
                n = med3(n - 2L * s, n - s, n, comp);
            }
            m = med3(l, m, n, comp);
        }
        long b;
        long a = b = from;
        long d;
        long c = d = to - 1L;
        while (true) {
            int comparison;
            if (b <= c && (comparison = comp.compare(b, m)) <= 0) {
                if (comparison == 0) {
                    if (a == m) {
                        m = b;
                    }
                    else if (b == m) {
                        m = a;
                    }
                    swapper.swap(a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = comp.compare(c, m)) >= 0) {
                    if (comparison == 0) {
                        if (c == m) {
                            m = d;
                        }
                        else if (d == m) {
                            m = c;
                        }
                        swapper.swap(c, d--);
                    }
                    --c;
                }
                if (b > c) {
                    break;
                }
                if (b == m) {
                    m = d;
                }
                else if (c == m) {
                    m = c;
                }
                swapper.swap(b++, c--);
            }
        }
        final long n2 = from + len;
        long s2 = Math.min(a - from, b - a);
        vecSwap(swapper, from, b - s2, s2);
        s2 = Math.min(d - c, n2 - d - 1L);
        vecSwap(swapper, b, n2 - s2, s2);
        if ((s2 = b - a) > 1L) {
            quickSort(from, from + s2, comp, swapper);
        }
        if ((s2 = d - c) > 1L) {
            quickSort(n2 - s2, n2, comp, swapper);
        }
    }
    
    private static long upperBound(long from, final long mid, final long secondCut, final LongComparator comp) {
        long len = mid - from;
        while (len > 0L) {
            final long half = len / 2L;
            final long middle = from + half;
            if (comp.compare(secondCut, middle) < 0) {
                len = half;
            }
            else {
                from = middle + 1L;
                len -= half + 1L;
            }
        }
        return from;
    }
    
    private static void vecSwap(final BigSwapper swapper, long from, long l, final long s) {
        for (int i = 0; i < s; ++i, ++from, ++l) {
            swapper.swap(from, l);
        }
    }
    
    public static byte get(final byte[][] array, final long index) {
        return array[segment(index)][displacement(index)];
    }
    
    public static void set(final byte[][] array, final long index, final byte value) {
        array[segment(index)][displacement(index)] = value;
    }
    
    public static void swap(final byte[][] array, final long first, final long second) {
        final byte t = array[segment(first)][displacement(first)];
        array[segment(first)][displacement(first)] = array[segment(second)][displacement(second)];
        array[segment(second)][displacement(second)] = t;
    }
    
    public static byte[][] reverse(final byte[][] a) {
        final long length = length(a);
        long i = length / 2L;
        while (i-- != 0L) {
            swap(a, i, length - i - 1L);
        }
        return a;
    }
    
    public static void add(final byte[][] array, final long index, final byte incr) {
        final byte[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        array2[displacement] += incr;
    }
    
    public static void mul(final byte[][] array, final long index, final byte factor) {
        final byte[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        array2[displacement] *= factor;
    }
    
    public static void incr(final byte[][] array, final long index) {
        final byte[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        ++array2[displacement];
    }
    
    public static void decr(final byte[][] array, final long index) {
        final byte[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        --array2[displacement];
    }
    
    public static long length(final byte[][] array) {
        final int length = array.length;
        return (length == 0) ? 0L : (start(length - 1) + array[length - 1].length);
    }
    
    public static void copy(final byte[][] srcArray, final long srcPos, final byte[][] destArray, final long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);
            int destDispl = displacement(destPos);
            while (length > 0L) {
                final int l = (int)Math.min(length, Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l);
                if ((srcDispl += l) == 134217728) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l) == 134217728) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= l;
            }
        }
        else {
            int srcSegment = segment(srcPos + length);
            int destSegment = segment(destPos + length);
            int srcDispl = displacement(srcPos + length);
            int destDispl = displacement(destPos + length);
            while (length > 0L) {
                if (srcDispl == 0) {
                    srcDispl = 134217728;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 134217728;
                    --destSegment;
                }
                final int l = (int)Math.min(length, Math.min(srcDispl, destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl - l, destArray[destSegment], destDispl - l, l);
                srcDispl -= l;
                destDispl -= l;
                length -= l;
            }
        }
    }
    
    public static void copyFromBig(final byte[][] srcArray, final long srcPos, final byte[] destArray, int destPos, int length) {
        int srcSegment = segment(srcPos);
        int srcDispl = displacement(srcPos);
        while (length > 0) {
            final int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            if ((srcDispl += l) == 134217728) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l;
            length -= l;
        }
    }
    
    public static void copyToBig(final byte[] srcArray, int srcPos, final byte[][] destArray, final long destPos, long length) {
        int destSegment = segment(destPos);
        int destDispl = displacement(destPos);
        while (length > 0L) {
            final int l = (int)Math.min(destArray[destSegment].length - destDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            if ((destDispl += l) == 134217728) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l;
            length -= l;
        }
    }
    
    public static byte[][] wrap(final byte[] array) {
        if (array.length == 0) {
            return ByteBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            return new byte[][] { array };
        }
        final byte[][] bigArray = ByteBigArrays.newBigArray((long)array.length);
        for (int i = 0; i < bigArray.length; ++i) {
            System.arraycopy(array, (int)start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }
    
    public static byte[][] ensureCapacity(final byte[][] array, final long length) {
        return ensureCapacity(array, length, length(array));
    }
    
    public static byte[][] forceCapacity(final byte[][] array, final long length, final long preserve) {
        ensureLength(length);
        final int valid = array.length - ((array.length != 0 && (array.length <= 0 || array[array.length - 1].length != 134217728)) ? 1 : 0);
        final int baseLength = (int)(length + 134217727L >>> 27);
        final byte[][] base = Arrays.copyOf(array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i = valid; i < baseLength - 1; ++i) {
                base[i] = new byte[134217728];
            }
            base[baseLength - 1] = new byte[residual];
        }
        else {
            for (int i = valid; i < baseLength; ++i) {
                base[i] = new byte[134217728];
            }
        }
        if (preserve - valid * 134217728L > 0L) {
            copy(array, valid * 134217728L, base, valid * 134217728L, preserve - valid * 134217728L);
        }
        return base;
    }
    
    public static byte[][] ensureCapacity(final byte[][] array, final long length, final long preserve) {
        return (length > length(array)) ? forceCapacity(array, length, preserve) : array;
    }
    
    public static byte[][] grow(final byte[][] array, final long length) {
        final long oldLength = length(array);
        return (length > oldLength) ? grow(array, length, oldLength) : array;
    }
    
    public static byte[][] grow(final byte[][] array, final long length, final long preserve) {
        final long oldLength = length(array);
        return (length > oldLength) ? ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
    }
    
    public static byte[][] trim(final byte[][] array, final long length) {
        ensureLength(length);
        final long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        final int baseLength = (int)(length + 134217727L >>> 27);
        final byte[][] base = Arrays.copyOf(array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = ByteArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }
    
    public static byte[][] setLength(final byte[][] array, final long length) {
        final long oldLength = length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return trim(array, length);
        }
        return ensureCapacity(array, length);
    }
    
    public static byte[][] copy(final byte[][] array, final long offset, final long length) {
        ensureOffsetLength(array, offset, length);
        final byte[][] a = ByteBigArrays.newBigArray(length);
        copy(array, offset, a, 0L, length);
        return a;
    }
    
    public static byte[][] copy(final byte[][] array) {
        final byte[][] base = array.clone();
        int i = base.length;
        while (i-- != 0) {
            base[i] = array[i].clone();
        }
        return base;
    }
    
    public static void fill(final byte[][] array, final byte value) {
        int i = array.length;
        while (i-- != 0) {
            Arrays.fill(array[i], value);
        }
    }
    
    public static void fill(final byte[][] array, final long from, final long to, final byte value) {
        final long length = length(array);
        ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        final int fromSegment = segment(from);
        int toSegment = segment(to);
        final int fromDispl = displacement(from);
        final int toDispl = displacement(to);
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
        Arrays.fill(array[fromSegment], fromDispl, 134217728, value);
    }
    
    public static boolean equals(final byte[][] a1, final byte[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (i-- != 0) {
            final byte[] t = a1[i];
            final byte[] u = a2[i];
            int j = t.length;
            while (j-- != 0) {
                if (t[j] != u[j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static String toString(final byte[][] a) {
        if (a == null) {
            return "null";
        }
        final long last = length(a) - 1L;
        if (last == -1L) {
            return "[]";
        }
        final StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0L;
        while (true) {
            b.append(String.valueOf(get(a, i)));
            if (i == last) {
                break;
            }
            b.append(", ");
            ++i;
        }
        return b.append(']').toString();
    }
    
    public static void ensureFromTo(final byte[][] a, final long from, final long to) {
        ensureFromTo(length(a), from, to);
    }
    
    public static void ensureOffsetLength(final byte[][] a, final long offset, final long length) {
        ensureOffsetLength(length(a), offset, length);
    }
    
    public static void ensureSameLength(final byte[][] a, final byte[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }
    
    public static byte[][] shuffle(final byte[][] a, final long from, final long to, final Random random) {
        long i = to - from;
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final byte t = get(a, from + i);
            set(a, from + i, get(a, from + p));
            set(a, from + p, t);
        }
        return a;
    }
    
    public static byte[][] shuffle(final byte[][] a, final Random random) {
        long i = length(a);
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final byte t = get(a, i);
            set(a, i, get(a, p));
            set(a, p, t);
        }
        return a;
    }
    
    public static int get(final int[][] array, final long index) {
        return array[segment(index)][displacement(index)];
    }
    
    public static void set(final int[][] array, final long index, final int value) {
        array[segment(index)][displacement(index)] = value;
    }
    
    public static long length(final AtomicIntegerArray[] array) {
        final int length = array.length;
        return (length == 0) ? 0L : (start(length - 1) + array[length - 1].length());
    }
    
    public static int get(final AtomicIntegerArray[] array, final long index) {
        return array[segment(index)].get(displacement(index));
    }
    
    public static void set(final AtomicIntegerArray[] array, final long index, final int value) {
        array[segment(index)].set(displacement(index), value);
    }
    
    public static int getAndSet(final AtomicIntegerArray[] array, final long index, final int value) {
        return array[segment(index)].getAndSet(displacement(index), value);
    }
    
    public static int getAndAdd(final AtomicIntegerArray[] array, final long index, final int value) {
        return array[segment(index)].getAndAdd(displacement(index), value);
    }
    
    public static int addAndGet(final AtomicIntegerArray[] array, final long index, final int value) {
        return array[segment(index)].addAndGet(displacement(index), value);
    }
    
    public static int getAndIncrement(final AtomicIntegerArray[] array, final long index) {
        return array[segment(index)].getAndDecrement(displacement(index));
    }
    
    public static int incrementAndGet(final AtomicIntegerArray[] array, final long index) {
        return array[segment(index)].incrementAndGet(displacement(index));
    }
    
    public static int getAndDecrement(final AtomicIntegerArray[] array, final long index) {
        return array[segment(index)].getAndDecrement(displacement(index));
    }
    
    public static int decrementAndGet(final AtomicIntegerArray[] array, final long index) {
        return array[segment(index)].decrementAndGet(displacement(index));
    }
    
    public static boolean compareAndSet(final AtomicIntegerArray[] array, final long index, final int expected, final int value) {
        return array[segment(index)].compareAndSet(displacement(index), expected, value);
    }
    
    public static void swap(final int[][] array, final long first, final long second) {
        final int t = array[segment(first)][displacement(first)];
        array[segment(first)][displacement(first)] = array[segment(second)][displacement(second)];
        array[segment(second)][displacement(second)] = t;
    }
    
    public static int[][] reverse(final int[][] a) {
        final long length = length(a);
        long i = length / 2L;
        while (i-- != 0L) {
            swap(a, i, length - i - 1L);
        }
        return a;
    }
    
    public static void add(final int[][] array, final long index, final int incr) {
        final int[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        array2[displacement] += incr;
    }
    
    public static void mul(final int[][] array, final long index, final int factor) {
        final int[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        array2[displacement] *= factor;
    }
    
    public static void incr(final int[][] array, final long index) {
        final int[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        ++array2[displacement];
    }
    
    public static void decr(final int[][] array, final long index) {
        final int[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        --array2[displacement];
    }
    
    public static long length(final int[][] array) {
        final int length = array.length;
        return (length == 0) ? 0L : (start(length - 1) + array[length - 1].length);
    }
    
    public static void copy(final int[][] srcArray, final long srcPos, final int[][] destArray, final long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);
            int destDispl = displacement(destPos);
            while (length > 0L) {
                final int l = (int)Math.min(length, Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l);
                if ((srcDispl += l) == 134217728) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l) == 134217728) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= l;
            }
        }
        else {
            int srcSegment = segment(srcPos + length);
            int destSegment = segment(destPos + length);
            int srcDispl = displacement(srcPos + length);
            int destDispl = displacement(destPos + length);
            while (length > 0L) {
                if (srcDispl == 0) {
                    srcDispl = 134217728;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 134217728;
                    --destSegment;
                }
                final int l = (int)Math.min(length, Math.min(srcDispl, destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl - l, destArray[destSegment], destDispl - l, l);
                srcDispl -= l;
                destDispl -= l;
                length -= l;
            }
        }
    }
    
    public static void copyFromBig(final int[][] srcArray, final long srcPos, final int[] destArray, int destPos, int length) {
        int srcSegment = segment(srcPos);
        int srcDispl = displacement(srcPos);
        while (length > 0) {
            final int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            if ((srcDispl += l) == 134217728) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l;
            length -= l;
        }
    }
    
    public static void copyToBig(final int[] srcArray, int srcPos, final int[][] destArray, final long destPos, long length) {
        int destSegment = segment(destPos);
        int destDispl = displacement(destPos);
        while (length > 0L) {
            final int l = (int)Math.min(destArray[destSegment].length - destDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            if ((destDispl += l) == 134217728) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l;
            length -= l;
        }
    }
    
    public static int[][] wrap(final int[] array) {
        if (array.length == 0) {
            return IntBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            return new int[][] { array };
        }
        final int[][] bigArray = IntBigArrays.newBigArray((long)array.length);
        for (int i = 0; i < bigArray.length; ++i) {
            System.arraycopy(array, (int)start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }
    
    public static int[][] ensureCapacity(final int[][] array, final long length) {
        return ensureCapacity(array, length, length(array));
    }
    
    public static int[][] forceCapacity(final int[][] array, final long length, final long preserve) {
        ensureLength(length);
        final int valid = array.length - ((array.length != 0 && (array.length <= 0 || array[array.length - 1].length != 134217728)) ? 1 : 0);
        final int baseLength = (int)(length + 134217727L >>> 27);
        final int[][] base = Arrays.copyOf(array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i = valid; i < baseLength - 1; ++i) {
                base[i] = new int[134217728];
            }
            base[baseLength - 1] = new int[residual];
        }
        else {
            for (int i = valid; i < baseLength; ++i) {
                base[i] = new int[134217728];
            }
        }
        if (preserve - valid * 134217728L > 0L) {
            copy(array, valid * 134217728L, base, valid * 134217728L, preserve - valid * 134217728L);
        }
        return base;
    }
    
    public static int[][] ensureCapacity(final int[][] array, final long length, final long preserve) {
        return (length > length(array)) ? forceCapacity(array, length, preserve) : array;
    }
    
    public static int[][] grow(final int[][] array, final long length) {
        final long oldLength = length(array);
        return (length > oldLength) ? grow(array, length, oldLength) : array;
    }
    
    public static int[][] grow(final int[][] array, final long length, final long preserve) {
        final long oldLength = length(array);
        return (length > oldLength) ? ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
    }
    
    public static int[][] trim(final int[][] array, final long length) {
        ensureLength(length);
        final long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        final int baseLength = (int)(length + 134217727L >>> 27);
        final int[][] base = Arrays.copyOf(array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = IntArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }
    
    public static int[][] setLength(final int[][] array, final long length) {
        final long oldLength = length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return trim(array, length);
        }
        return ensureCapacity(array, length);
    }
    
    public static int[][] copy(final int[][] array, final long offset, final long length) {
        ensureOffsetLength(array, offset, length);
        final int[][] a = IntBigArrays.newBigArray(length);
        copy(array, offset, a, 0L, length);
        return a;
    }
    
    public static int[][] copy(final int[][] array) {
        final int[][] base = array.clone();
        int i = base.length;
        while (i-- != 0) {
            base[i] = array[i].clone();
        }
        return base;
    }
    
    public static void fill(final int[][] array, final int value) {
        int i = array.length;
        while (i-- != 0) {
            Arrays.fill(array[i], value);
        }
    }
    
    public static void fill(final int[][] array, final long from, final long to, final int value) {
        final long length = length(array);
        ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        final int fromSegment = segment(from);
        int toSegment = segment(to);
        final int fromDispl = displacement(from);
        final int toDispl = displacement(to);
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
        Arrays.fill(array[fromSegment], fromDispl, 134217728, value);
    }
    
    public static boolean equals(final int[][] a1, final int[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (i-- != 0) {
            final int[] t = a1[i];
            final int[] u = a2[i];
            int j = t.length;
            while (j-- != 0) {
                if (t[j] != u[j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static String toString(final int[][] a) {
        if (a == null) {
            return "null";
        }
        final long last = length(a) - 1L;
        if (last == -1L) {
            return "[]";
        }
        final StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0L;
        while (true) {
            b.append(String.valueOf(get(a, i)));
            if (i == last) {
                break;
            }
            b.append(", ");
            ++i;
        }
        return b.append(']').toString();
    }
    
    public static void ensureFromTo(final int[][] a, final long from, final long to) {
        ensureFromTo(length(a), from, to);
    }
    
    public static void ensureOffsetLength(final int[][] a, final long offset, final long length) {
        ensureOffsetLength(length(a), offset, length);
    }
    
    public static void ensureSameLength(final int[][] a, final int[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }
    
    public static int[][] shuffle(final int[][] a, final long from, final long to, final Random random) {
        long i = to - from;
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final int t = get(a, from + i);
            set(a, from + i, get(a, from + p));
            set(a, from + p, t);
        }
        return a;
    }
    
    public static int[][] shuffle(final int[][] a, final Random random) {
        long i = length(a);
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final int t = get(a, i);
            set(a, i, get(a, p));
            set(a, p, t);
        }
        return a;
    }
    
    public static long get(final long[][] array, final long index) {
        return array[segment(index)][displacement(index)];
    }
    
    public static void set(final long[][] array, final long index, final long value) {
        array[segment(index)][displacement(index)] = value;
    }
    
    public static long length(final AtomicLongArray[] array) {
        final int length = array.length;
        return (length == 0) ? 0L : (start(length - 1) + array[length - 1].length());
    }
    
    public static long get(final AtomicLongArray[] array, final long index) {
        return array[segment(index)].get(displacement(index));
    }
    
    public static void set(final AtomicLongArray[] array, final long index, final long value) {
        array[segment(index)].set(displacement(index), value);
    }
    
    public static long getAndSet(final AtomicLongArray[] array, final long index, final long value) {
        return array[segment(index)].getAndSet(displacement(index), value);
    }
    
    public static long getAndAdd(final AtomicLongArray[] array, final long index, final long value) {
        return array[segment(index)].getAndAdd(displacement(index), value);
    }
    
    public static long addAndGet(final AtomicLongArray[] array, final long index, final long value) {
        return array[segment(index)].addAndGet(displacement(index), value);
    }
    
    public static long getAndIncrement(final AtomicLongArray[] array, final long index) {
        return array[segment(index)].getAndDecrement(displacement(index));
    }
    
    public static long incrementAndGet(final AtomicLongArray[] array, final long index) {
        return array[segment(index)].incrementAndGet(displacement(index));
    }
    
    public static long getAndDecrement(final AtomicLongArray[] array, final long index) {
        return array[segment(index)].getAndDecrement(displacement(index));
    }
    
    public static long decrementAndGet(final AtomicLongArray[] array, final long index) {
        return array[segment(index)].decrementAndGet(displacement(index));
    }
    
    public static boolean compareAndSet(final AtomicLongArray[] array, final long index, final long expected, final long value) {
        return array[segment(index)].compareAndSet(displacement(index), expected, value);
    }
    
    public static void swap(final long[][] array, final long first, final long second) {
        final long t = array[segment(first)][displacement(first)];
        array[segment(first)][displacement(first)] = array[segment(second)][displacement(second)];
        array[segment(second)][displacement(second)] = t;
    }
    
    public static long[][] reverse(final long[][] a) {
        final long length = length(a);
        long i = length / 2L;
        while (i-- != 0L) {
            swap(a, i, length - i - 1L);
        }
        return a;
    }
    
    public static void add(final long[][] array, final long index, final long incr) {
        final long[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        array2[displacement] += incr;
    }
    
    public static void mul(final long[][] array, final long index, final long factor) {
        final long[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        array2[displacement] *= factor;
    }
    
    public static void incr(final long[][] array, final long index) {
        final long[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        ++array2[displacement];
    }
    
    public static void decr(final long[][] array, final long index) {
        final long[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        --array2[displacement];
    }
    
    public static long length(final long[][] array) {
        final int length = array.length;
        return (length == 0) ? 0L : (start(length - 1) + array[length - 1].length);
    }
    
    public static void copy(final long[][] srcArray, final long srcPos, final long[][] destArray, final long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);
            int destDispl = displacement(destPos);
            while (length > 0L) {
                final int l = (int)Math.min(length, Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l);
                if ((srcDispl += l) == 134217728) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l) == 134217728) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= l;
            }
        }
        else {
            int srcSegment = segment(srcPos + length);
            int destSegment = segment(destPos + length);
            int srcDispl = displacement(srcPos + length);
            int destDispl = displacement(destPos + length);
            while (length > 0L) {
                if (srcDispl == 0) {
                    srcDispl = 134217728;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 134217728;
                    --destSegment;
                }
                final int l = (int)Math.min(length, Math.min(srcDispl, destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl - l, destArray[destSegment], destDispl - l, l);
                srcDispl -= l;
                destDispl -= l;
                length -= l;
            }
        }
    }
    
    public static void copyFromBig(final long[][] srcArray, final long srcPos, final long[] destArray, int destPos, int length) {
        int srcSegment = segment(srcPos);
        int srcDispl = displacement(srcPos);
        while (length > 0) {
            final int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            if ((srcDispl += l) == 134217728) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l;
            length -= l;
        }
    }
    
    public static void copyToBig(final long[] srcArray, int srcPos, final long[][] destArray, final long destPos, long length) {
        int destSegment = segment(destPos);
        int destDispl = displacement(destPos);
        while (length > 0L) {
            final int l = (int)Math.min(destArray[destSegment].length - destDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            if ((destDispl += l) == 134217728) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l;
            length -= l;
        }
    }
    
    public static long[][] wrap(final long[] array) {
        if (array.length == 0) {
            return LongBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            return new long[][] { array };
        }
        final long[][] bigArray = LongBigArrays.newBigArray((long)array.length);
        for (int i = 0; i < bigArray.length; ++i) {
            System.arraycopy(array, (int)start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }
    
    public static long[][] ensureCapacity(final long[][] array, final long length) {
        return ensureCapacity(array, length, length(array));
    }
    
    public static long[][] forceCapacity(final long[][] array, final long length, final long preserve) {
        ensureLength(length);
        final int valid = array.length - ((array.length != 0 && (array.length <= 0 || array[array.length - 1].length != 134217728)) ? 1 : 0);
        final int baseLength = (int)(length + 134217727L >>> 27);
        final long[][] base = Arrays.copyOf(array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i = valid; i < baseLength - 1; ++i) {
                base[i] = new long[134217728];
            }
            base[baseLength - 1] = new long[residual];
        }
        else {
            for (int i = valid; i < baseLength; ++i) {
                base[i] = new long[134217728];
            }
        }
        if (preserve - valid * 134217728L > 0L) {
            copy(array, valid * 134217728L, base, valid * 134217728L, preserve - valid * 134217728L);
        }
        return base;
    }
    
    public static long[][] ensureCapacity(final long[][] array, final long length, final long preserve) {
        return (length > length(array)) ? forceCapacity(array, length, preserve) : array;
    }
    
    public static long[][] grow(final long[][] array, final long length) {
        final long oldLength = length(array);
        return (length > oldLength) ? grow(array, length, oldLength) : array;
    }
    
    public static long[][] grow(final long[][] array, final long length, final long preserve) {
        final long oldLength = length(array);
        return (length > oldLength) ? ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
    }
    
    public static long[][] trim(final long[][] array, final long length) {
        ensureLength(length);
        final long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        final int baseLength = (int)(length + 134217727L >>> 27);
        final long[][] base = Arrays.copyOf(array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = LongArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }
    
    public static long[][] setLength(final long[][] array, final long length) {
        final long oldLength = length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return trim(array, length);
        }
        return ensureCapacity(array, length);
    }
    
    public static long[][] copy(final long[][] array, final long offset, final long length) {
        ensureOffsetLength(array, offset, length);
        final long[][] a = LongBigArrays.newBigArray(length);
        copy(array, offset, a, 0L, length);
        return a;
    }
    
    public static long[][] copy(final long[][] array) {
        final long[][] base = array.clone();
        int i = base.length;
        while (i-- != 0) {
            base[i] = array[i].clone();
        }
        return base;
    }
    
    public static void fill(final long[][] array, final long value) {
        int i = array.length;
        while (i-- != 0) {
            Arrays.fill(array[i], value);
        }
    }
    
    public static void fill(final long[][] array, final long from, final long to, final long value) {
        final long length = length(array);
        ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        final int fromSegment = segment(from);
        int toSegment = segment(to);
        final int fromDispl = displacement(from);
        final int toDispl = displacement(to);
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
        Arrays.fill(array[fromSegment], fromDispl, 134217728, value);
    }
    
    public static boolean equals(final long[][] a1, final long[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (i-- != 0) {
            final long[] t = a1[i];
            final long[] u = a2[i];
            int j = t.length;
            while (j-- != 0) {
                if (t[j] != u[j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static String toString(final long[][] a) {
        if (a == null) {
            return "null";
        }
        final long last = length(a) - 1L;
        if (last == -1L) {
            return "[]";
        }
        final StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0L;
        while (true) {
            b.append(String.valueOf(get(a, i)));
            if (i == last) {
                break;
            }
            b.append(", ");
            ++i;
        }
        return b.append(']').toString();
    }
    
    public static void ensureFromTo(final long[][] a, final long from, final long to) {
        ensureFromTo(length(a), from, to);
    }
    
    public static void ensureOffsetLength(final long[][] a, final long offset, final long length) {
        ensureOffsetLength(length(a), offset, length);
    }
    
    public static void ensureSameLength(final long[][] a, final long[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }
    
    public static long[][] shuffle(final long[][] a, final long from, final long to, final Random random) {
        long i = to - from;
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final long t = get(a, from + i);
            set(a, from + i, get(a, from + p));
            set(a, from + p, t);
        }
        return a;
    }
    
    public static long[][] shuffle(final long[][] a, final Random random) {
        long i = length(a);
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final long t = get(a, i);
            set(a, i, get(a, p));
            set(a, p, t);
        }
        return a;
    }
    
    public static double get(final double[][] array, final long index) {
        return array[segment(index)][displacement(index)];
    }
    
    public static void set(final double[][] array, final long index, final double value) {
        array[segment(index)][displacement(index)] = value;
    }
    
    public static void swap(final double[][] array, final long first, final long second) {
        final double t = array[segment(first)][displacement(first)];
        array[segment(first)][displacement(first)] = array[segment(second)][displacement(second)];
        array[segment(second)][displacement(second)] = t;
    }
    
    public static double[][] reverse(final double[][] a) {
        final long length = length(a);
        long i = length / 2L;
        while (i-- != 0L) {
            swap(a, i, length - i - 1L);
        }
        return a;
    }
    
    public static void add(final double[][] array, final long index, final double incr) {
        final double[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        array2[displacement] += incr;
    }
    
    public static void mul(final double[][] array, final long index, final double factor) {
        final double[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        array2[displacement] *= factor;
    }
    
    public static void incr(final double[][] array, final long index) {
        final double[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        ++array2[displacement];
    }
    
    public static void decr(final double[][] array, final long index) {
        final double[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        --array2[displacement];
    }
    
    public static long length(final double[][] array) {
        final int length = array.length;
        return (length == 0) ? 0L : (start(length - 1) + array[length - 1].length);
    }
    
    public static void copy(final double[][] srcArray, final long srcPos, final double[][] destArray, final long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);
            int destDispl = displacement(destPos);
            while (length > 0L) {
                final int l = (int)Math.min(length, Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l);
                if ((srcDispl += l) == 134217728) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l) == 134217728) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= l;
            }
        }
        else {
            int srcSegment = segment(srcPos + length);
            int destSegment = segment(destPos + length);
            int srcDispl = displacement(srcPos + length);
            int destDispl = displacement(destPos + length);
            while (length > 0L) {
                if (srcDispl == 0) {
                    srcDispl = 134217728;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 134217728;
                    --destSegment;
                }
                final int l = (int)Math.min(length, Math.min(srcDispl, destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl - l, destArray[destSegment], destDispl - l, l);
                srcDispl -= l;
                destDispl -= l;
                length -= l;
            }
        }
    }
    
    public static void copyFromBig(final double[][] srcArray, final long srcPos, final double[] destArray, int destPos, int length) {
        int srcSegment = segment(srcPos);
        int srcDispl = displacement(srcPos);
        while (length > 0) {
            final int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            if ((srcDispl += l) == 134217728) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l;
            length -= l;
        }
    }
    
    public static void copyToBig(final double[] srcArray, int srcPos, final double[][] destArray, final long destPos, long length) {
        int destSegment = segment(destPos);
        int destDispl = displacement(destPos);
        while (length > 0L) {
            final int l = (int)Math.min(destArray[destSegment].length - destDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            if ((destDispl += l) == 134217728) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l;
            length -= l;
        }
    }
    
    public static double[][] wrap(final double[] array) {
        if (array.length == 0) {
            return DoubleBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            return new double[][] { array };
        }
        final double[][] bigArray = DoubleBigArrays.newBigArray((long)array.length);
        for (int i = 0; i < bigArray.length; ++i) {
            System.arraycopy(array, (int)start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }
    
    public static double[][] ensureCapacity(final double[][] array, final long length) {
        return ensureCapacity(array, length, length(array));
    }
    
    public static double[][] forceCapacity(final double[][] array, final long length, final long preserve) {
        ensureLength(length);
        final int valid = array.length - ((array.length != 0 && (array.length <= 0 || array[array.length - 1].length != 134217728)) ? 1 : 0);
        final int baseLength = (int)(length + 134217727L >>> 27);
        final double[][] base = Arrays.copyOf(array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i = valid; i < baseLength - 1; ++i) {
                base[i] = new double[134217728];
            }
            base[baseLength - 1] = new double[residual];
        }
        else {
            for (int i = valid; i < baseLength; ++i) {
                base[i] = new double[134217728];
            }
        }
        if (preserve - valid * 134217728L > 0L) {
            copy(array, valid * 134217728L, base, valid * 134217728L, preserve - valid * 134217728L);
        }
        return base;
    }
    
    public static double[][] ensureCapacity(final double[][] array, final long length, final long preserve) {
        return (length > length(array)) ? forceCapacity(array, length, preserve) : array;
    }
    
    public static double[][] grow(final double[][] array, final long length) {
        final long oldLength = length(array);
        return (length > oldLength) ? grow(array, length, oldLength) : array;
    }
    
    public static double[][] grow(final double[][] array, final long length, final long preserve) {
        final long oldLength = length(array);
        return (length > oldLength) ? ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
    }
    
    public static double[][] trim(final double[][] array, final long length) {
        ensureLength(length);
        final long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        final int baseLength = (int)(length + 134217727L >>> 27);
        final double[][] base = Arrays.copyOf(array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = DoubleArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }
    
    public static double[][] setLength(final double[][] array, final long length) {
        final long oldLength = length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return trim(array, length);
        }
        return ensureCapacity(array, length);
    }
    
    public static double[][] copy(final double[][] array, final long offset, final long length) {
        ensureOffsetLength(array, offset, length);
        final double[][] a = DoubleBigArrays.newBigArray(length);
        copy(array, offset, a, 0L, length);
        return a;
    }
    
    public static double[][] copy(final double[][] array) {
        final double[][] base = array.clone();
        int i = base.length;
        while (i-- != 0) {
            base[i] = array[i].clone();
        }
        return base;
    }
    
    public static void fill(final double[][] array, final double value) {
        int i = array.length;
        while (i-- != 0) {
            Arrays.fill(array[i], value);
        }
    }
    
    public static void fill(final double[][] array, final long from, final long to, final double value) {
        final long length = length(array);
        ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        final int fromSegment = segment(from);
        int toSegment = segment(to);
        final int fromDispl = displacement(from);
        final int toDispl = displacement(to);
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
        Arrays.fill(array[fromSegment], fromDispl, 134217728, value);
    }
    
    public static boolean equals(final double[][] a1, final double[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (i-- != 0) {
            final double[] t = a1[i];
            final double[] u = a2[i];
            int j = t.length;
            while (j-- != 0) {
                if (Double.doubleToLongBits(t[j]) != Double.doubleToLongBits(u[j])) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static String toString(final double[][] a) {
        if (a == null) {
            return "null";
        }
        final long last = length(a) - 1L;
        if (last == -1L) {
            return "[]";
        }
        final StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0L;
        while (true) {
            b.append(String.valueOf(get(a, i)));
            if (i == last) {
                break;
            }
            b.append(", ");
            ++i;
        }
        return b.append(']').toString();
    }
    
    public static void ensureFromTo(final double[][] a, final long from, final long to) {
        ensureFromTo(length(a), from, to);
    }
    
    public static void ensureOffsetLength(final double[][] a, final long offset, final long length) {
        ensureOffsetLength(length(a), offset, length);
    }
    
    public static void ensureSameLength(final double[][] a, final double[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }
    
    public static double[][] shuffle(final double[][] a, final long from, final long to, final Random random) {
        long i = to - from;
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final double t = get(a, from + i);
            set(a, from + i, get(a, from + p));
            set(a, from + p, t);
        }
        return a;
    }
    
    public static double[][] shuffle(final double[][] a, final Random random) {
        long i = length(a);
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final double t = get(a, i);
            set(a, i, get(a, p));
            set(a, p, t);
        }
        return a;
    }
    
    public static boolean get(final boolean[][] array, final long index) {
        return array[segment(index)][displacement(index)];
    }
    
    public static void set(final boolean[][] array, final long index, final boolean value) {
        array[segment(index)][displacement(index)] = value;
    }
    
    public static void swap(final boolean[][] array, final long first, final long second) {
        final boolean t = array[segment(first)][displacement(first)];
        array[segment(first)][displacement(first)] = array[segment(second)][displacement(second)];
        array[segment(second)][displacement(second)] = t;
    }
    
    public static boolean[][] reverse(final boolean[][] a) {
        final long length = length(a);
        long i = length / 2L;
        while (i-- != 0L) {
            swap(a, i, length - i - 1L);
        }
        return a;
    }
    
    public static long length(final boolean[][] array) {
        final int length = array.length;
        return (length == 0) ? 0L : (start(length - 1) + array[length - 1].length);
    }
    
    public static void copy(final boolean[][] srcArray, final long srcPos, final boolean[][] destArray, final long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);
            int destDispl = displacement(destPos);
            while (length > 0L) {
                final int l = (int)Math.min(length, Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l);
                if ((srcDispl += l) == 134217728) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l) == 134217728) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= l;
            }
        }
        else {
            int srcSegment = segment(srcPos + length);
            int destSegment = segment(destPos + length);
            int srcDispl = displacement(srcPos + length);
            int destDispl = displacement(destPos + length);
            while (length > 0L) {
                if (srcDispl == 0) {
                    srcDispl = 134217728;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 134217728;
                    --destSegment;
                }
                final int l = (int)Math.min(length, Math.min(srcDispl, destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl - l, destArray[destSegment], destDispl - l, l);
                srcDispl -= l;
                destDispl -= l;
                length -= l;
            }
        }
    }
    
    public static void copyFromBig(final boolean[][] srcArray, final long srcPos, final boolean[] destArray, int destPos, int length) {
        int srcSegment = segment(srcPos);
        int srcDispl = displacement(srcPos);
        while (length > 0) {
            final int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            if ((srcDispl += l) == 134217728) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l;
            length -= l;
        }
    }
    
    public static void copyToBig(final boolean[] srcArray, int srcPos, final boolean[][] destArray, final long destPos, long length) {
        int destSegment = segment(destPos);
        int destDispl = displacement(destPos);
        while (length > 0L) {
            final int l = (int)Math.min(destArray[destSegment].length - destDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            if ((destDispl += l) == 134217728) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l;
            length -= l;
        }
    }
    
    public static boolean[][] wrap(final boolean[] array) {
        if (array.length == 0) {
            return BooleanBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            return new boolean[][] { array };
        }
        final boolean[][] bigArray = BooleanBigArrays.newBigArray((long)array.length);
        for (int i = 0; i < bigArray.length; ++i) {
            System.arraycopy(array, (int)start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }
    
    public static boolean[][] ensureCapacity(final boolean[][] array, final long length) {
        return ensureCapacity(array, length, length(array));
    }
    
    public static boolean[][] forceCapacity(final boolean[][] array, final long length, final long preserve) {
        ensureLength(length);
        final int valid = array.length - ((array.length != 0 && (array.length <= 0 || array[array.length - 1].length != 134217728)) ? 1 : 0);
        final int baseLength = (int)(length + 134217727L >>> 27);
        final boolean[][] base = Arrays.copyOf(array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i = valid; i < baseLength - 1; ++i) {
                base[i] = new boolean[134217728];
            }
            base[baseLength - 1] = new boolean[residual];
        }
        else {
            for (int i = valid; i < baseLength; ++i) {
                base[i] = new boolean[134217728];
            }
        }
        if (preserve - valid * 134217728L > 0L) {
            copy(array, valid * 134217728L, base, valid * 134217728L, preserve - valid * 134217728L);
        }
        return base;
    }
    
    public static boolean[][] ensureCapacity(final boolean[][] array, final long length, final long preserve) {
        return (length > length(array)) ? forceCapacity(array, length, preserve) : array;
    }
    
    public static boolean[][] grow(final boolean[][] array, final long length) {
        final long oldLength = length(array);
        return (length > oldLength) ? grow(array, length, oldLength) : array;
    }
    
    public static boolean[][] grow(final boolean[][] array, final long length, final long preserve) {
        final long oldLength = length(array);
        return (length > oldLength) ? ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
    }
    
    public static boolean[][] trim(final boolean[][] array, final long length) {
        ensureLength(length);
        final long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        final int baseLength = (int)(length + 134217727L >>> 27);
        final boolean[][] base = Arrays.copyOf(array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = BooleanArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }
    
    public static boolean[][] setLength(final boolean[][] array, final long length) {
        final long oldLength = length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return trim(array, length);
        }
        return ensureCapacity(array, length);
    }
    
    public static boolean[][] copy(final boolean[][] array, final long offset, final long length) {
        ensureOffsetLength(array, offset, length);
        final boolean[][] a = BooleanBigArrays.newBigArray(length);
        copy(array, offset, a, 0L, length);
        return a;
    }
    
    public static boolean[][] copy(final boolean[][] array) {
        final boolean[][] base = array.clone();
        int i = base.length;
        while (i-- != 0) {
            base[i] = array[i].clone();
        }
        return base;
    }
    
    public static void fill(final boolean[][] array, final boolean value) {
        int i = array.length;
        while (i-- != 0) {
            Arrays.fill(array[i], value);
        }
    }
    
    public static void fill(final boolean[][] array, final long from, final long to, final boolean value) {
        final long length = length(array);
        ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        final int fromSegment = segment(from);
        int toSegment = segment(to);
        final int fromDispl = displacement(from);
        final int toDispl = displacement(to);
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
        Arrays.fill(array[fromSegment], fromDispl, 134217728, value);
    }
    
    public static boolean equals(final boolean[][] a1, final boolean[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (i-- != 0) {
            final boolean[] t = a1[i];
            final boolean[] u = a2[i];
            int j = t.length;
            while (j-- != 0) {
                if (t[j] != u[j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static String toString(final boolean[][] a) {
        if (a == null) {
            return "null";
        }
        final long last = length(a) - 1L;
        if (last == -1L) {
            return "[]";
        }
        final StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0L;
        while (true) {
            b.append(String.valueOf(get(a, i)));
            if (i == last) {
                break;
            }
            b.append(", ");
            ++i;
        }
        return b.append(']').toString();
    }
    
    public static void ensureFromTo(final boolean[][] a, final long from, final long to) {
        ensureFromTo(length(a), from, to);
    }
    
    public static void ensureOffsetLength(final boolean[][] a, final long offset, final long length) {
        ensureOffsetLength(length(a), offset, length);
    }
    
    public static void ensureSameLength(final boolean[][] a, final boolean[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }
    
    public static boolean[][] shuffle(final boolean[][] a, final long from, final long to, final Random random) {
        long i = to - from;
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final boolean t = get(a, from + i);
            set(a, from + i, get(a, from + p));
            set(a, from + p, t);
        }
        return a;
    }
    
    public static boolean[][] shuffle(final boolean[][] a, final Random random) {
        long i = length(a);
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final boolean t = get(a, i);
            set(a, i, get(a, p));
            set(a, p, t);
        }
        return a;
    }
    
    public static short get(final short[][] array, final long index) {
        return array[segment(index)][displacement(index)];
    }
    
    public static void set(final short[][] array, final long index, final short value) {
        array[segment(index)][displacement(index)] = value;
    }
    
    public static void swap(final short[][] array, final long first, final long second) {
        final short t = array[segment(first)][displacement(first)];
        array[segment(first)][displacement(first)] = array[segment(second)][displacement(second)];
        array[segment(second)][displacement(second)] = t;
    }
    
    public static short[][] reverse(final short[][] a) {
        final long length = length(a);
        long i = length / 2L;
        while (i-- != 0L) {
            swap(a, i, length - i - 1L);
        }
        return a;
    }
    
    public static void add(final short[][] array, final long index, final short incr) {
        final short[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        array2[displacement] += incr;
    }
    
    public static void mul(final short[][] array, final long index, final short factor) {
        final short[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        array2[displacement] *= factor;
    }
    
    public static void incr(final short[][] array, final long index) {
        final short[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        ++array2[displacement];
    }
    
    public static void decr(final short[][] array, final long index) {
        final short[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        --array2[displacement];
    }
    
    public static long length(final short[][] array) {
        final int length = array.length;
        return (length == 0) ? 0L : (start(length - 1) + array[length - 1].length);
    }
    
    public static void copy(final short[][] srcArray, final long srcPos, final short[][] destArray, final long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);
            int destDispl = displacement(destPos);
            while (length > 0L) {
                final int l = (int)Math.min(length, Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l);
                if ((srcDispl += l) == 134217728) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l) == 134217728) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= l;
            }
        }
        else {
            int srcSegment = segment(srcPos + length);
            int destSegment = segment(destPos + length);
            int srcDispl = displacement(srcPos + length);
            int destDispl = displacement(destPos + length);
            while (length > 0L) {
                if (srcDispl == 0) {
                    srcDispl = 134217728;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 134217728;
                    --destSegment;
                }
                final int l = (int)Math.min(length, Math.min(srcDispl, destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl - l, destArray[destSegment], destDispl - l, l);
                srcDispl -= l;
                destDispl -= l;
                length -= l;
            }
        }
    }
    
    public static void copyFromBig(final short[][] srcArray, final long srcPos, final short[] destArray, int destPos, int length) {
        int srcSegment = segment(srcPos);
        int srcDispl = displacement(srcPos);
        while (length > 0) {
            final int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            if ((srcDispl += l) == 134217728) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l;
            length -= l;
        }
    }
    
    public static void copyToBig(final short[] srcArray, int srcPos, final short[][] destArray, final long destPos, long length) {
        int destSegment = segment(destPos);
        int destDispl = displacement(destPos);
        while (length > 0L) {
            final int l = (int)Math.min(destArray[destSegment].length - destDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            if ((destDispl += l) == 134217728) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l;
            length -= l;
        }
    }
    
    public static short[][] wrap(final short[] array) {
        if (array.length == 0) {
            return ShortBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            return new short[][] { array };
        }
        final short[][] bigArray = ShortBigArrays.newBigArray((long)array.length);
        for (int i = 0; i < bigArray.length; ++i) {
            System.arraycopy(array, (int)start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }
    
    public static short[][] ensureCapacity(final short[][] array, final long length) {
        return ensureCapacity(array, length, length(array));
    }
    
    public static short[][] forceCapacity(final short[][] array, final long length, final long preserve) {
        ensureLength(length);
        final int valid = array.length - ((array.length != 0 && (array.length <= 0 || array[array.length - 1].length != 134217728)) ? 1 : 0);
        final int baseLength = (int)(length + 134217727L >>> 27);
        final short[][] base = Arrays.copyOf(array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i = valid; i < baseLength - 1; ++i) {
                base[i] = new short[134217728];
            }
            base[baseLength - 1] = new short[residual];
        }
        else {
            for (int i = valid; i < baseLength; ++i) {
                base[i] = new short[134217728];
            }
        }
        if (preserve - valid * 134217728L > 0L) {
            copy(array, valid * 134217728L, base, valid * 134217728L, preserve - valid * 134217728L);
        }
        return base;
    }
    
    public static short[][] ensureCapacity(final short[][] array, final long length, final long preserve) {
        return (length > length(array)) ? forceCapacity(array, length, preserve) : array;
    }
    
    public static short[][] grow(final short[][] array, final long length) {
        final long oldLength = length(array);
        return (length > oldLength) ? grow(array, length, oldLength) : array;
    }
    
    public static short[][] grow(final short[][] array, final long length, final long preserve) {
        final long oldLength = length(array);
        return (length > oldLength) ? ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
    }
    
    public static short[][] trim(final short[][] array, final long length) {
        ensureLength(length);
        final long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        final int baseLength = (int)(length + 134217727L >>> 27);
        final short[][] base = Arrays.copyOf(array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = ShortArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }
    
    public static short[][] setLength(final short[][] array, final long length) {
        final long oldLength = length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return trim(array, length);
        }
        return ensureCapacity(array, length);
    }
    
    public static short[][] copy(final short[][] array, final long offset, final long length) {
        ensureOffsetLength(array, offset, length);
        final short[][] a = ShortBigArrays.newBigArray(length);
        copy(array, offset, a, 0L, length);
        return a;
    }
    
    public static short[][] copy(final short[][] array) {
        final short[][] base = array.clone();
        int i = base.length;
        while (i-- != 0) {
            base[i] = array[i].clone();
        }
        return base;
    }
    
    public static void fill(final short[][] array, final short value) {
        int i = array.length;
        while (i-- != 0) {
            Arrays.fill(array[i], value);
        }
    }
    
    public static void fill(final short[][] array, final long from, final long to, final short value) {
        final long length = length(array);
        ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        final int fromSegment = segment(from);
        int toSegment = segment(to);
        final int fromDispl = displacement(from);
        final int toDispl = displacement(to);
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
        Arrays.fill(array[fromSegment], fromDispl, 134217728, value);
    }
    
    public static boolean equals(final short[][] a1, final short[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (i-- != 0) {
            final short[] t = a1[i];
            final short[] u = a2[i];
            int j = t.length;
            while (j-- != 0) {
                if (t[j] != u[j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static String toString(final short[][] a) {
        if (a == null) {
            return "null";
        }
        final long last = length(a) - 1L;
        if (last == -1L) {
            return "[]";
        }
        final StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0L;
        while (true) {
            b.append(String.valueOf(get(a, i)));
            if (i == last) {
                break;
            }
            b.append(", ");
            ++i;
        }
        return b.append(']').toString();
    }
    
    public static void ensureFromTo(final short[][] a, final long from, final long to) {
        ensureFromTo(length(a), from, to);
    }
    
    public static void ensureOffsetLength(final short[][] a, final long offset, final long length) {
        ensureOffsetLength(length(a), offset, length);
    }
    
    public static void ensureSameLength(final short[][] a, final short[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }
    
    public static short[][] shuffle(final short[][] a, final long from, final long to, final Random random) {
        long i = to - from;
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final short t = get(a, from + i);
            set(a, from + i, get(a, from + p));
            set(a, from + p, t);
        }
        return a;
    }
    
    public static short[][] shuffle(final short[][] a, final Random random) {
        long i = length(a);
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final short t = get(a, i);
            set(a, i, get(a, p));
            set(a, p, t);
        }
        return a;
    }
    
    public static char get(final char[][] array, final long index) {
        return array[segment(index)][displacement(index)];
    }
    
    public static void set(final char[][] array, final long index, final char value) {
        array[segment(index)][displacement(index)] = value;
    }
    
    public static void swap(final char[][] array, final long first, final long second) {
        final char t = array[segment(first)][displacement(first)];
        array[segment(first)][displacement(first)] = array[segment(second)][displacement(second)];
        array[segment(second)][displacement(second)] = t;
    }
    
    public static char[][] reverse(final char[][] a) {
        final long length = length(a);
        long i = length / 2L;
        while (i-- != 0L) {
            swap(a, i, length - i - 1L);
        }
        return a;
    }
    
    public static void add(final char[][] array, final long index, final char incr) {
        final char[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        array2[displacement] += incr;
    }
    
    public static void mul(final char[][] array, final long index, final char factor) {
        final char[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        array2[displacement] *= factor;
    }
    
    public static void incr(final char[][] array, final long index) {
        final char[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        ++array2[displacement];
    }
    
    public static void decr(final char[][] array, final long index) {
        final char[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        --array2[displacement];
    }
    
    public static long length(final char[][] array) {
        final int length = array.length;
        return (length == 0) ? 0L : (start(length - 1) + array[length - 1].length);
    }
    
    public static void copy(final char[][] srcArray, final long srcPos, final char[][] destArray, final long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);
            int destDispl = displacement(destPos);
            while (length > 0L) {
                final int l = (int)Math.min(length, Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l);
                if ((srcDispl += l) == 134217728) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l) == 134217728) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= l;
            }
        }
        else {
            int srcSegment = segment(srcPos + length);
            int destSegment = segment(destPos + length);
            int srcDispl = displacement(srcPos + length);
            int destDispl = displacement(destPos + length);
            while (length > 0L) {
                if (srcDispl == 0) {
                    srcDispl = 134217728;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 134217728;
                    --destSegment;
                }
                final int l = (int)Math.min(length, Math.min(srcDispl, destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl - l, destArray[destSegment], destDispl - l, l);
                srcDispl -= l;
                destDispl -= l;
                length -= l;
            }
        }
    }
    
    public static void copyFromBig(final char[][] srcArray, final long srcPos, final char[] destArray, int destPos, int length) {
        int srcSegment = segment(srcPos);
        int srcDispl = displacement(srcPos);
        while (length > 0) {
            final int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            if ((srcDispl += l) == 134217728) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l;
            length -= l;
        }
    }
    
    public static void copyToBig(final char[] srcArray, int srcPos, final char[][] destArray, final long destPos, long length) {
        int destSegment = segment(destPos);
        int destDispl = displacement(destPos);
        while (length > 0L) {
            final int l = (int)Math.min(destArray[destSegment].length - destDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            if ((destDispl += l) == 134217728) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l;
            length -= l;
        }
    }
    
    public static char[][] wrap(final char[] array) {
        if (array.length == 0) {
            return CharBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            return new char[][] { array };
        }
        final char[][] bigArray = CharBigArrays.newBigArray((long)array.length);
        for (int i = 0; i < bigArray.length; ++i) {
            System.arraycopy(array, (int)start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }
    
    public static char[][] ensureCapacity(final char[][] array, final long length) {
        return ensureCapacity(array, length, length(array));
    }
    
    public static char[][] forceCapacity(final char[][] array, final long length, final long preserve) {
        ensureLength(length);
        final int valid = array.length - ((array.length != 0 && (array.length <= 0 || array[array.length - 1].length != 134217728)) ? 1 : 0);
        final int baseLength = (int)(length + 134217727L >>> 27);
        final char[][] base = Arrays.copyOf(array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i = valid; i < baseLength - 1; ++i) {
                base[i] = new char[134217728];
            }
            base[baseLength - 1] = new char[residual];
        }
        else {
            for (int i = valid; i < baseLength; ++i) {
                base[i] = new char[134217728];
            }
        }
        if (preserve - valid * 134217728L > 0L) {
            copy(array, valid * 134217728L, base, valid * 134217728L, preserve - valid * 134217728L);
        }
        return base;
    }
    
    public static char[][] ensureCapacity(final char[][] array, final long length, final long preserve) {
        return (length > length(array)) ? forceCapacity(array, length, preserve) : array;
    }
    
    public static char[][] grow(final char[][] array, final long length) {
        final long oldLength = length(array);
        return (length > oldLength) ? grow(array, length, oldLength) : array;
    }
    
    public static char[][] grow(final char[][] array, final long length, final long preserve) {
        final long oldLength = length(array);
        return (length > oldLength) ? ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
    }
    
    public static char[][] trim(final char[][] array, final long length) {
        ensureLength(length);
        final long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        final int baseLength = (int)(length + 134217727L >>> 27);
        final char[][] base = Arrays.copyOf(array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = CharArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }
    
    public static char[][] setLength(final char[][] array, final long length) {
        final long oldLength = length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return trim(array, length);
        }
        return ensureCapacity(array, length);
    }
    
    public static char[][] copy(final char[][] array, final long offset, final long length) {
        ensureOffsetLength(array, offset, length);
        final char[][] a = CharBigArrays.newBigArray(length);
        copy(array, offset, a, 0L, length);
        return a;
    }
    
    public static char[][] copy(final char[][] array) {
        final char[][] base = array.clone();
        int i = base.length;
        while (i-- != 0) {
            base[i] = array[i].clone();
        }
        return base;
    }
    
    public static void fill(final char[][] array, final char value) {
        int i = array.length;
        while (i-- != 0) {
            Arrays.fill(array[i], value);
        }
    }
    
    public static void fill(final char[][] array, final long from, final long to, final char value) {
        final long length = length(array);
        ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        final int fromSegment = segment(from);
        int toSegment = segment(to);
        final int fromDispl = displacement(from);
        final int toDispl = displacement(to);
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
        Arrays.fill(array[fromSegment], fromDispl, 134217728, value);
    }
    
    public static boolean equals(final char[][] a1, final char[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (i-- != 0) {
            final char[] t = a1[i];
            final char[] u = a2[i];
            int j = t.length;
            while (j-- != 0) {
                if (t[j] != u[j]) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static String toString(final char[][] a) {
        if (a == null) {
            return "null";
        }
        final long last = length(a) - 1L;
        if (last == -1L) {
            return "[]";
        }
        final StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0L;
        while (true) {
            b.append(String.valueOf(get(a, i)));
            if (i == last) {
                break;
            }
            b.append(", ");
            ++i;
        }
        return b.append(']').toString();
    }
    
    public static void ensureFromTo(final char[][] a, final long from, final long to) {
        ensureFromTo(length(a), from, to);
    }
    
    public static void ensureOffsetLength(final char[][] a, final long offset, final long length) {
        ensureOffsetLength(length(a), offset, length);
    }
    
    public static void ensureSameLength(final char[][] a, final char[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }
    
    public static char[][] shuffle(final char[][] a, final long from, final long to, final Random random) {
        long i = to - from;
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final char t = get(a, from + i);
            set(a, from + i, get(a, from + p));
            set(a, from + p, t);
        }
        return a;
    }
    
    public static char[][] shuffle(final char[][] a, final Random random) {
        long i = length(a);
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final char t = get(a, i);
            set(a, i, get(a, p));
            set(a, p, t);
        }
        return a;
    }
    
    public static float get(final float[][] array, final long index) {
        return array[segment(index)][displacement(index)];
    }
    
    public static void set(final float[][] array, final long index, final float value) {
        array[segment(index)][displacement(index)] = value;
    }
    
    public static void swap(final float[][] array, final long first, final long second) {
        final float t = array[segment(first)][displacement(first)];
        array[segment(first)][displacement(first)] = array[segment(second)][displacement(second)];
        array[segment(second)][displacement(second)] = t;
    }
    
    public static float[][] reverse(final float[][] a) {
        final long length = length(a);
        long i = length / 2L;
        while (i-- != 0L) {
            swap(a, i, length - i - 1L);
        }
        return a;
    }
    
    public static void add(final float[][] array, final long index, final float incr) {
        final float[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        array2[displacement] += incr;
    }
    
    public static void mul(final float[][] array, final long index, final float factor) {
        final float[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        array2[displacement] *= factor;
    }
    
    public static void incr(final float[][] array, final long index) {
        final float[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        ++array2[displacement];
    }
    
    public static void decr(final float[][] array, final long index) {
        final float[] array2 = array[segment(index)];
        final int displacement = displacement(index);
        --array2[displacement];
    }
    
    public static long length(final float[][] array) {
        final int length = array.length;
        return (length == 0) ? 0L : (start(length - 1) + array[length - 1].length);
    }
    
    public static void copy(final float[][] srcArray, final long srcPos, final float[][] destArray, final long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);
            int destDispl = displacement(destPos);
            while (length > 0L) {
                final int l = (int)Math.min(length, Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l);
                if ((srcDispl += l) == 134217728) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l) == 134217728) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= l;
            }
        }
        else {
            int srcSegment = segment(srcPos + length);
            int destSegment = segment(destPos + length);
            int srcDispl = displacement(srcPos + length);
            int destDispl = displacement(destPos + length);
            while (length > 0L) {
                if (srcDispl == 0) {
                    srcDispl = 134217728;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 134217728;
                    --destSegment;
                }
                final int l = (int)Math.min(length, Math.min(srcDispl, destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl - l, destArray[destSegment], destDispl - l, l);
                srcDispl -= l;
                destDispl -= l;
                length -= l;
            }
        }
    }
    
    public static void copyFromBig(final float[][] srcArray, final long srcPos, final float[] destArray, int destPos, int length) {
        int srcSegment = segment(srcPos);
        int srcDispl = displacement(srcPos);
        while (length > 0) {
            final int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            if ((srcDispl += l) == 134217728) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l;
            length -= l;
        }
    }
    
    public static void copyToBig(final float[] srcArray, int srcPos, final float[][] destArray, final long destPos, long length) {
        int destSegment = segment(destPos);
        int destDispl = displacement(destPos);
        while (length > 0L) {
            final int l = (int)Math.min(destArray[destSegment].length - destDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            if ((destDispl += l) == 134217728) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l;
            length -= l;
        }
    }
    
    public static float[][] wrap(final float[] array) {
        if (array.length == 0) {
            return FloatBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            return new float[][] { array };
        }
        final float[][] bigArray = FloatBigArrays.newBigArray((long)array.length);
        for (int i = 0; i < bigArray.length; ++i) {
            System.arraycopy(array, (int)start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }
    
    public static float[][] ensureCapacity(final float[][] array, final long length) {
        return ensureCapacity(array, length, length(array));
    }
    
    public static float[][] forceCapacity(final float[][] array, final long length, final long preserve) {
        ensureLength(length);
        final int valid = array.length - ((array.length != 0 && (array.length <= 0 || array[array.length - 1].length != 134217728)) ? 1 : 0);
        final int baseLength = (int)(length + 134217727L >>> 27);
        final float[][] base = Arrays.copyOf(array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i = valid; i < baseLength - 1; ++i) {
                base[i] = new float[134217728];
            }
            base[baseLength - 1] = new float[residual];
        }
        else {
            for (int i = valid; i < baseLength; ++i) {
                base[i] = new float[134217728];
            }
        }
        if (preserve - valid * 134217728L > 0L) {
            copy(array, valid * 134217728L, base, valid * 134217728L, preserve - valid * 134217728L);
        }
        return base;
    }
    
    public static float[][] ensureCapacity(final float[][] array, final long length, final long preserve) {
        return (length > length(array)) ? forceCapacity(array, length, preserve) : array;
    }
    
    public static float[][] grow(final float[][] array, final long length) {
        final long oldLength = length(array);
        return (length > oldLength) ? grow(array, length, oldLength) : array;
    }
    
    public static float[][] grow(final float[][] array, final long length, final long preserve) {
        final long oldLength = length(array);
        return (length > oldLength) ? ensureCapacity(array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array;
    }
    
    public static float[][] trim(final float[][] array, final long length) {
        ensureLength(length);
        final long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        final int baseLength = (int)(length + 134217727L >>> 27);
        final float[][] base = Arrays.copyOf(array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = FloatArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }
    
    public static float[][] setLength(final float[][] array, final long length) {
        final long oldLength = length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return trim(array, length);
        }
        return ensureCapacity(array, length);
    }
    
    public static float[][] copy(final float[][] array, final long offset, final long length) {
        ensureOffsetLength(array, offset, length);
        final float[][] a = FloatBigArrays.newBigArray(length);
        copy(array, offset, a, 0L, length);
        return a;
    }
    
    public static float[][] copy(final float[][] array) {
        final float[][] base = array.clone();
        int i = base.length;
        while (i-- != 0) {
            base[i] = array[i].clone();
        }
        return base;
    }
    
    public static void fill(final float[][] array, final float value) {
        int i = array.length;
        while (i-- != 0) {
            Arrays.fill(array[i], value);
        }
    }
    
    public static void fill(final float[][] array, final long from, final long to, final float value) {
        final long length = length(array);
        ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        final int fromSegment = segment(from);
        int toSegment = segment(to);
        final int fromDispl = displacement(from);
        final int toDispl = displacement(to);
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
        Arrays.fill(array[fromSegment], fromDispl, 134217728, value);
    }
    
    public static boolean equals(final float[][] a1, final float[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (i-- != 0) {
            final float[] t = a1[i];
            final float[] u = a2[i];
            int j = t.length;
            while (j-- != 0) {
                if (Float.floatToIntBits(t[j]) != Float.floatToIntBits(u[j])) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static String toString(final float[][] a) {
        if (a == null) {
            return "null";
        }
        final long last = length(a) - 1L;
        if (last == -1L) {
            return "[]";
        }
        final StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0L;
        while (true) {
            b.append(String.valueOf(get(a, i)));
            if (i == last) {
                break;
            }
            b.append(", ");
            ++i;
        }
        return b.append(']').toString();
    }
    
    public static void ensureFromTo(final float[][] a, final long from, final long to) {
        ensureFromTo(length(a), from, to);
    }
    
    public static void ensureOffsetLength(final float[][] a, final long offset, final long length) {
        ensureOffsetLength(length(a), offset, length);
    }
    
    public static void ensureSameLength(final float[][] a, final float[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }
    
    public static float[][] shuffle(final float[][] a, final long from, final long to, final Random random) {
        long i = to - from;
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final float t = get(a, from + i);
            set(a, from + i, get(a, from + p));
            set(a, from + p, t);
        }
        return a;
    }
    
    public static float[][] shuffle(final float[][] a, final Random random) {
        long i = length(a);
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final float t = get(a, i);
            set(a, i, get(a, p));
            set(a, p, t);
        }
        return a;
    }
    
    public static <K> K get(final K[][] array, final long index) {
        return array[segment(index)][displacement(index)];
    }
    
    public static <K> void set(final K[][] array, final long index, final K value) {
        array[segment(index)][displacement(index)] = value;
    }
    
    public static <K> void swap(final K[][] array, final long first, final long second) {
        final K t = array[segment(first)][displacement(first)];
        array[segment(first)][displacement(first)] = array[segment(second)][displacement(second)];
        array[segment(second)][displacement(second)] = t;
    }
    
    public static <K> K[][] reverse(final K[][] a) {
        final long length = length(a);
        long i = length / 2L;
        while (i-- != 0L) {
            swap(a, i, length - i - 1L);
        }
        return a;
    }
    
    public static <K> long length(final K[][] array) {
        final int length = array.length;
        return (length == 0) ? 0L : (start(length - 1) + array[length - 1].length);
    }
    
    public static <K> void copy(final K[][] srcArray, final long srcPos, final K[][] destArray, final long destPos, long length) {
        if (destPos <= srcPos) {
            int srcSegment = segment(srcPos);
            int destSegment = segment(destPos);
            int srcDispl = displacement(srcPos);
            int destDispl = displacement(destPos);
            while (length > 0L) {
                final int l = (int)Math.min(length, Math.min(srcArray[srcSegment].length - srcDispl, destArray[destSegment].length - destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl, destArray[destSegment], destDispl, l);
                if ((srcDispl += l) == 134217728) {
                    srcDispl = 0;
                    ++srcSegment;
                }
                if ((destDispl += l) == 134217728) {
                    destDispl = 0;
                    ++destSegment;
                }
                length -= l;
            }
        }
        else {
            int srcSegment = segment(srcPos + length);
            int destSegment = segment(destPos + length);
            int srcDispl = displacement(srcPos + length);
            int destDispl = displacement(destPos + length);
            while (length > 0L) {
                if (srcDispl == 0) {
                    srcDispl = 134217728;
                    --srcSegment;
                }
                if (destDispl == 0) {
                    destDispl = 134217728;
                    --destSegment;
                }
                final int l = (int)Math.min(length, Math.min(srcDispl, destDispl));
                if (l == 0) {
                    throw new ArrayIndexOutOfBoundsException();
                }
                System.arraycopy(srcArray[srcSegment], srcDispl - l, destArray[destSegment], destDispl - l, l);
                srcDispl -= l;
                destDispl -= l;
                length -= l;
            }
        }
    }
    
    public static <K> void copyFromBig(final K[][] srcArray, final long srcPos, final K[] destArray, int destPos, int length) {
        int srcSegment = segment(srcPos);
        int srcDispl = displacement(srcPos);
        while (length > 0) {
            final int l = Math.min(srcArray[srcSegment].length - srcDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray[srcSegment], srcDispl, destArray, destPos, l);
            if ((srcDispl += l) == 134217728) {
                srcDispl = 0;
                ++srcSegment;
            }
            destPos += l;
            length -= l;
        }
    }
    
    public static <K> void copyToBig(final K[] srcArray, int srcPos, final K[][] destArray, final long destPos, long length) {
        int destSegment = segment(destPos);
        int destDispl = displacement(destPos);
        while (length > 0L) {
            final int l = (int)Math.min(destArray[destSegment].length - destDispl, length);
            if (l == 0) {
                throw new ArrayIndexOutOfBoundsException();
            }
            System.arraycopy(srcArray, srcPos, destArray[destSegment], destDispl, l);
            if ((destDispl += l) == 134217728) {
                destDispl = 0;
                ++destSegment;
            }
            srcPos += l;
            length -= l;
        }
    }
    
    public static <K> K[][] wrap(final K[] array) {
        if (array.length == 0 && array.getClass() == Object[].class) {
            return (K[][])ObjectBigArrays.EMPTY_BIG_ARRAY;
        }
        if (array.length <= 134217728) {
            final K[][] bigArray = (K[][])Array.newInstance(array.getClass(), 1);
            bigArray[0] = array;
            return bigArray;
        }
        final K[][] bigArray = (K[][])ObjectBigArrays.newBigArray((Class)array.getClass(), (long)array.length);
        for (int i = 0; i < bigArray.length; ++i) {
            System.arraycopy(array, (int)start(i), bigArray[i], 0, bigArray[i].length);
        }
        return bigArray;
    }
    
    public static <K> K[][] ensureCapacity(final K[][] array, final long length) {
        return ensureCapacity(array, length, length(array));
    }
    
    public static <K> K[][] forceCapacity(final K[][] array, final long length, final long preserve) {
        ensureLength(length);
        final int valid = array.length - ((array.length != 0 && (array.length <= 0 || array[array.length - 1].length != 134217728)) ? 1 : 0);
        final int baseLength = (int)(length + 134217727L >>> 27);
        final K[][] base = Arrays.copyOf(array, baseLength);
        final Class<?> componentType = array.getClass().getComponentType();
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            for (int i = valid; i < baseLength - 1; ++i) {
                base[i] = (K[])Array.newInstance(componentType.getComponentType(), 134217728);
            }
            base[baseLength - 1] = (K[])Array.newInstance(componentType.getComponentType(), residual);
        }
        else {
            for (int i = valid; i < baseLength; ++i) {
                base[i] = (K[])Array.newInstance(componentType.getComponentType(), 134217728);
            }
        }
        if (preserve - valid * 134217728L > 0L) {
            copy(array, valid * 134217728L, base, valid * 134217728L, preserve - valid * 134217728L);
        }
        return base;
    }
    
    public static <K> K[][] ensureCapacity(final K[][] array, final long length, final long preserve) {
        return (K[][])((length > length(array)) ? forceCapacity((Object[][])array, length, preserve) : array);
    }
    
    public static <K> K[][] grow(final K[][] array, final long length) {
        final long oldLength = length(array);
        return (length > oldLength) ? grow(array, length, oldLength) : array;
    }
    
    public static <K> K[][] grow(final K[][] array, final long length, final long preserve) {
        final long oldLength = length(array);
        return (K[][])((length > oldLength) ? ensureCapacity((Object[][])array, Math.max(oldLength + (oldLength >> 1), length), preserve) : array);
    }
    
    public static <K> K[][] trim(final K[][] array, final long length) {
        ensureLength(length);
        final long oldLength = length(array);
        if (length >= oldLength) {
            return array;
        }
        final int baseLength = (int)(length + 134217727L >>> 27);
        final K[][] base = Arrays.copyOf(array, baseLength);
        final int residual = (int)(length & 0x7FFFFFFL);
        if (residual != 0) {
            base[baseLength - 1] = ObjectArrays.trim(base[baseLength - 1], residual);
        }
        return base;
    }
    
    public static <K> K[][] setLength(final K[][] array, final long length) {
        final long oldLength = length(array);
        if (length == oldLength) {
            return array;
        }
        if (length < oldLength) {
            return (K[][])trim((Object[][])array, length);
        }
        return (K[][])ensureCapacity((Object[][])array, length);
    }
    
    public static <K> K[][] copy(final K[][] array, final long offset, final long length) {
        ensureOffsetLength(array, offset, length);
        final K[][] a = (K[][])ObjectBigArrays.newBigArray((Object[][])array, length);
        copy(array, offset, a, 0L, length);
        return a;
    }
    
    public static <K> K[][] copy(final K[][] array) {
        final K[][] base = array.clone();
        int i = base.length;
        while (i-- != 0) {
            base[i] = array[i].clone();
        }
        return base;
    }
    
    public static <K> void fill(final K[][] array, final K value) {
        int i = array.length;
        while (i-- != 0) {
            Arrays.fill(array[i], value);
        }
    }
    
    public static <K> void fill(final K[][] array, final long from, final long to, final K value) {
        final long length = length(array);
        ensureFromTo(length, from, to);
        if (length == 0L) {
            return;
        }
        final int fromSegment = segment(from);
        int toSegment = segment(to);
        final int fromDispl = displacement(from);
        final int toDispl = displacement(to);
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
        Arrays.fill(array[fromSegment], fromDispl, 134217728, value);
    }
    
    public static <K> boolean equals(final K[][] a1, final K[][] a2) {
        if (length(a1) != length(a2)) {
            return false;
        }
        int i = a1.length;
        while (i-- != 0) {
            final K[] t = a1[i];
            final K[] u = a2[i];
            int j = t.length;
            while (j-- != 0) {
                if (!Objects.equals(t[j], u[j])) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static <K> String toString(final K[][] a) {
        if (a == null) {
            return "null";
        }
        final long last = length(a) - 1L;
        if (last == -1L) {
            return "[]";
        }
        final StringBuilder b = new StringBuilder();
        b.append('[');
        long i = 0L;
        while (true) {
            b.append(String.valueOf(get(a, i)));
            if (i == last) {
                break;
            }
            b.append(", ");
            ++i;
        }
        return b.append(']').toString();
    }
    
    public static <K> void ensureFromTo(final K[][] a, final long from, final long to) {
        ensureFromTo(length(a), from, to);
    }
    
    public static <K> void ensureOffsetLength(final K[][] a, final long offset, final long length) {
        ensureOffsetLength(length(a), offset, length);
    }
    
    public static <K> void ensureSameLength(final K[][] a, final K[][] b) {
        if (length(a) != length(b)) {
            throw new IllegalArgumentException("Array size mismatch: " + length(a) + " != " + length(b));
        }
    }
    
    public static <K> K[][] shuffle(final K[][] a, final long from, final long to, final Random random) {
        long i = to - from;
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final K t = get(a, from + i);
            set(a, from + i, (K)get((K[][])a, from + p));
            set(a, from + p, t);
        }
        return a;
    }
    
    public static <K> K[][] shuffle(final K[][] a, final Random random) {
        long i = length(a);
        while (i-- != 0L) {
            final long p = (random.nextLong() & Long.MAX_VALUE) % (i + 1L);
            final K t = get(a, i);
            set(a, i, (K)get((K[][])a, p));
            set(a, p, t);
        }
        return a;
    }
    
    public static void main(final String[] arg) {
        final int[][] a = IntBigArrays.newBigArray(1L << Integer.parseInt(arg[0]));
        int k = 10;
        while (k-- != 0) {
            long start = -System.currentTimeMillis();
            long x = 0L;
            long i = length(a);
            while (i-- != 0L) {
                x ^= (i ^ (long)get(a, i));
            }
            if (x == 0L) {
                System.err.println();
            }
            System.out.println("Single loop: " + (start + System.currentTimeMillis()) + "ms");
            start = -System.currentTimeMillis();
            long y = 0L;
            int j = a.length;
            while (j-- != 0) {
                final int[] t = a[j];
                int d = t.length;
                while (d-- != 0) {
                    y ^= ((long)t[d] ^ index(j, d));
                }
            }
            if (y == 0L) {
                System.err.println();
            }
            if (x != y) {
                throw new AssertionError();
            }
            System.out.println("Double loop: " + (start + System.currentTimeMillis()) + "ms");
            final long z = 0L;
            long l = length(a);
            int m = a.length;
            while (m-- != 0) {
                final int[] t2 = a[m];
                int d2 = t2.length;
                while (d2-- != 0) {
                    y ^= ((long)t2[d2] ^ --l);
                }
            }
            if (z == 0L) {
                System.err.println();
            }
            if (x != z) {
                throw new AssertionError();
            }
            System.out.println("Double loop (with additional index): " + (start + System.currentTimeMillis()) + "ms");
        }
    }
}
