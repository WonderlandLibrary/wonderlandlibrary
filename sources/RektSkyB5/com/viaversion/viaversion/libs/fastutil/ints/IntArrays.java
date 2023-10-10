/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.ints;

import com.viaversion.viaversion.libs.fastutil.Arrays;
import com.viaversion.viaversion.libs.fastutil.Hash;
import com.viaversion.viaversion.libs.fastutil.ints.IntComparator;
import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

public final class IntArrays {
    public static final int[] EMPTY_ARRAY = new int[0];
    public static final int[] DEFAULT_EMPTY_ARRAY = new int[0];
    private static final int QUICKSORT_NO_REC = 16;
    private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
    private static final int QUICKSORT_MEDIAN_OF_9 = 128;
    private static final int MERGESORT_NO_REC = 16;
    private static final int DIGIT_BITS = 8;
    private static final int DIGIT_MASK = 255;
    private static final int DIGITS_PER_ELEMENT = 4;
    private static final int RADIXSORT_NO_REC = 1024;
    private static final int PARALLEL_RADIXSORT_NO_FORK = 1024;
    static final int RADIX_SORT_MIN_THRESHOLD = 2000;
    protected static final Segment POISON_PILL = new Segment(-1, -1, -1);
    public static final Hash.Strategy<int[]> HASH_STRATEGY = new ArrayHashStrategy();

    private IntArrays() {
    }

    public static int[] forceCapacity(int[] array, int length, int preserve) {
        int[] t2 = new int[length];
        System.arraycopy(array, 0, t2, 0, preserve);
        return t2;
    }

    public static int[] ensureCapacity(int[] array, int length) {
        return IntArrays.ensureCapacity(array, length, array.length);
    }

    public static int[] ensureCapacity(int[] array, int length, int preserve) {
        return length > array.length ? IntArrays.forceCapacity(array, length, preserve) : array;
    }

    public static int[] grow(int[] array, int length) {
        return IntArrays.grow(array, length, array.length);
    }

    public static int[] grow(int[] array, int length, int preserve) {
        if (length > array.length) {
            int newLength = (int)Math.max(Math.min((long)array.length + (long)(array.length >> 1), 0x7FFFFFF7L), (long)length);
            int[] t2 = new int[newLength];
            System.arraycopy(array, 0, t2, 0, preserve);
            return t2;
        }
        return array;
    }

    public static int[] trim(int[] array, int length) {
        if (length >= array.length) {
            return array;
        }
        int[] t2 = length == 0 ? EMPTY_ARRAY : new int[length];
        System.arraycopy(array, 0, t2, 0, length);
        return t2;
    }

    public static int[] setLength(int[] array, int length) {
        if (length == array.length) {
            return array;
        }
        if (length < array.length) {
            return IntArrays.trim(array, length);
        }
        return IntArrays.ensureCapacity(array, length);
    }

    public static int[] copy(int[] array, int offset, int length) {
        IntArrays.ensureOffsetLength(array, offset, length);
        int[] a2 = length == 0 ? EMPTY_ARRAY : new int[length];
        System.arraycopy(array, offset, a2, 0, length);
        return a2;
    }

    public static int[] copy(int[] array) {
        return (int[])array.clone();
    }

    @Deprecated
    public static void fill(int[] array, int value) {
        int i2 = array.length;
        while (i2-- != 0) {
            array[i2] = value;
        }
    }

    @Deprecated
    public static void fill(int[] array, int from, int to, int value) {
        IntArrays.ensureFromTo(array, from, to);
        if (from == 0) {
            while (to-- != 0) {
                array[to] = value;
            }
        } else {
            for (int i2 = from; i2 < to; ++i2) {
                array[i2] = value;
            }
        }
    }

    @Deprecated
    public static boolean equals(int[] a1, int[] a2) {
        int i2 = a1.length;
        if (i2 != a2.length) {
            return false;
        }
        while (i2-- != 0) {
            if (a1[i2] == a2[i2]) continue;
            return false;
        }
        return true;
    }

    public static void ensureFromTo(int[] a2, int from, int to) {
        Arrays.ensureFromTo(a2.length, from, to);
    }

    public static void ensureOffsetLength(int[] a2, int offset, int length) {
        Arrays.ensureOffsetLength(a2.length, offset, length);
    }

    public static void ensureSameLength(int[] a2, int[] b2) {
        if (a2.length != b2.length) {
            throw new IllegalArgumentException("Array size mismatch: " + a2.length + " != " + b2.length);
        }
    }

    private static ForkJoinPool getPool() {
        ForkJoinPool current = ForkJoinTask.getPool();
        return current == null ? ForkJoinPool.commonPool() : current;
    }

    public static void swap(int[] x2, int a2, int b2) {
        int t2 = x2[a2];
        x2[a2] = x2[b2];
        x2[b2] = t2;
    }

    public static void swap(int[] x2, int a2, int b2, int n2) {
        int i2 = 0;
        while (i2 < n2) {
            IntArrays.swap(x2, a2, b2);
            ++i2;
            ++a2;
            ++b2;
        }
    }

    private static int med3(int[] x2, int a2, int b2, int c2, IntComparator comp) {
        int ab = comp.compare(x2[a2], x2[b2]);
        int ac = comp.compare(x2[a2], x2[c2]);
        int bc = comp.compare(x2[b2], x2[c2]);
        return ab < 0 ? (bc < 0 ? b2 : (ac < 0 ? c2 : a2)) : (bc > 0 ? b2 : (ac > 0 ? c2 : a2));
    }

    private static void selectionSort(int[] a2, int from, int to, IntComparator comp) {
        for (int i2 = from; i2 < to - 1; ++i2) {
            int m2 = i2;
            for (int j2 = i2 + 1; j2 < to; ++j2) {
                if (comp.compare(a2[j2], a2[m2]) >= 0) continue;
                m2 = j2;
            }
            if (m2 == i2) continue;
            int u2 = a2[i2];
            a2[i2] = a2[m2];
            a2[m2] = u2;
        }
    }

    private static void insertionSort(int[] a2, int from, int to, IntComparator comp) {
        int i2 = from;
        while (++i2 < to) {
            int t2 = a2[i2];
            int j2 = i2;
            int u2 = a2[j2 - 1];
            while (comp.compare(t2, u2) < 0) {
                a2[j2] = u2;
                if (from == j2 - 1) {
                    --j2;
                    break;
                }
                u2 = a2[--j2 - 1];
            }
            a2[j2] = t2;
        }
    }

    public static void quickSort(int[] x2, int from, int to, IntComparator comp) {
        int c2;
        int a2;
        int len = to - from;
        if (len < 16) {
            IntArrays.selectionSort(x2, from, to, comp);
            return;
        }
        int m2 = from + len / 2;
        int l2 = from;
        int n2 = to - 1;
        if (len > 128) {
            int s2 = len / 8;
            l2 = IntArrays.med3(x2, l2, l2 + s2, l2 + 2 * s2, comp);
            m2 = IntArrays.med3(x2, m2 - s2, m2, m2 + s2, comp);
            n2 = IntArrays.med3(x2, n2 - 2 * s2, n2 - s2, n2, comp);
        }
        m2 = IntArrays.med3(x2, l2, m2, n2, comp);
        int v2 = x2[m2];
        int b2 = a2 = from;
        int d2 = c2 = to - 1;
        while (true) {
            int comparison;
            if (b2 <= c2 && (comparison = comp.compare(x2[b2], v2)) <= 0) {
                if (comparison == 0) {
                    IntArrays.swap(x2, a2++, b2);
                }
                ++b2;
                continue;
            }
            while (c2 >= b2 && (comparison = comp.compare(x2[c2], v2)) >= 0) {
                if (comparison == 0) {
                    IntArrays.swap(x2, c2, d2--);
                }
                --c2;
            }
            if (b2 > c2) break;
            IntArrays.swap(x2, b2++, c2--);
        }
        int s3 = Math.min(a2 - from, b2 - a2);
        IntArrays.swap(x2, from, b2 - s3, s3);
        s3 = Math.min(d2 - c2, to - d2 - 1);
        IntArrays.swap(x2, b2, to - s3, s3);
        s3 = b2 - a2;
        if (s3 > 1) {
            IntArrays.quickSort(x2, from, from + s3, comp);
        }
        if ((s3 = d2 - c2) > 1) {
            IntArrays.quickSort(x2, to - s3, to, comp);
        }
    }

    public static void quickSort(int[] x2, IntComparator comp) {
        IntArrays.quickSort(x2, 0, x2.length, comp);
    }

    public static void parallelQuickSort(int[] x2, int from, int to, IntComparator comp) {
        ForkJoinPool pool = IntArrays.getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            IntArrays.quickSort(x2, from, to, comp);
        } else {
            pool.invoke(new ForkJoinQuickSortComp(x2, from, to, comp));
        }
    }

    public static void parallelQuickSort(int[] x2, IntComparator comp) {
        IntArrays.parallelQuickSort(x2, 0, x2.length, comp);
    }

    private static int med3(int[] x2, int a2, int b2, int c2) {
        int ab = Integer.compare(x2[a2], x2[b2]);
        int ac = Integer.compare(x2[a2], x2[c2]);
        int bc = Integer.compare(x2[b2], x2[c2]);
        return ab < 0 ? (bc < 0 ? b2 : (ac < 0 ? c2 : a2)) : (bc > 0 ? b2 : (ac > 0 ? c2 : a2));
    }

    private static void selectionSort(int[] a2, int from, int to) {
        for (int i2 = from; i2 < to - 1; ++i2) {
            int m2 = i2;
            for (int j2 = i2 + 1; j2 < to; ++j2) {
                if (a2[j2] >= a2[m2]) continue;
                m2 = j2;
            }
            if (m2 == i2) continue;
            int u2 = a2[i2];
            a2[i2] = a2[m2];
            a2[m2] = u2;
        }
    }

    private static void insertionSort(int[] a2, int from, int to) {
        int i2 = from;
        while (++i2 < to) {
            int t2 = a2[i2];
            int j2 = i2;
            int u2 = a2[j2 - 1];
            while (t2 < u2) {
                a2[j2] = u2;
                if (from == j2 - 1) {
                    --j2;
                    break;
                }
                u2 = a2[--j2 - 1];
            }
            a2[j2] = t2;
        }
    }

    public static void quickSort(int[] x2, int from, int to) {
        int c2;
        int a2;
        int len = to - from;
        if (len < 16) {
            IntArrays.selectionSort(x2, from, to);
            return;
        }
        int m2 = from + len / 2;
        int l2 = from;
        int n2 = to - 1;
        if (len > 128) {
            int s2 = len / 8;
            l2 = IntArrays.med3(x2, l2, l2 + s2, l2 + 2 * s2);
            m2 = IntArrays.med3(x2, m2 - s2, m2, m2 + s2);
            n2 = IntArrays.med3(x2, n2 - 2 * s2, n2 - s2, n2);
        }
        m2 = IntArrays.med3(x2, l2, m2, n2);
        int v2 = x2[m2];
        int b2 = a2 = from;
        int d2 = c2 = to - 1;
        while (true) {
            int comparison;
            if (b2 <= c2 && (comparison = Integer.compare(x2[b2], v2)) <= 0) {
                if (comparison == 0) {
                    IntArrays.swap(x2, a2++, b2);
                }
                ++b2;
                continue;
            }
            while (c2 >= b2 && (comparison = Integer.compare(x2[c2], v2)) >= 0) {
                if (comparison == 0) {
                    IntArrays.swap(x2, c2, d2--);
                }
                --c2;
            }
            if (b2 > c2) break;
            IntArrays.swap(x2, b2++, c2--);
        }
        int s3 = Math.min(a2 - from, b2 - a2);
        IntArrays.swap(x2, from, b2 - s3, s3);
        s3 = Math.min(d2 - c2, to - d2 - 1);
        IntArrays.swap(x2, b2, to - s3, s3);
        s3 = b2 - a2;
        if (s3 > 1) {
            IntArrays.quickSort(x2, from, from + s3);
        }
        if ((s3 = d2 - c2) > 1) {
            IntArrays.quickSort(x2, to - s3, to);
        }
    }

    public static void quickSort(int[] x2) {
        IntArrays.quickSort(x2, 0, x2.length);
    }

    public static void parallelQuickSort(int[] x2, int from, int to) {
        ForkJoinPool pool = IntArrays.getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            IntArrays.quickSort(x2, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSort(x2, from, to));
        }
    }

    public static void parallelQuickSort(int[] x2) {
        IntArrays.parallelQuickSort(x2, 0, x2.length);
    }

    private static int med3Indirect(int[] perm, int[] x2, int a2, int b2, int c2) {
        int aa = x2[perm[a2]];
        int bb = x2[perm[b2]];
        int cc = x2[perm[c2]];
        int ab = Integer.compare(aa, bb);
        int ac = Integer.compare(aa, cc);
        int bc = Integer.compare(bb, cc);
        return ab < 0 ? (bc < 0 ? b2 : (ac < 0 ? c2 : a2)) : (bc > 0 ? b2 : (ac > 0 ? c2 : a2));
    }

    private static void insertionSortIndirect(int[] perm, int[] a2, int from, int to) {
        int i2 = from;
        while (++i2 < to) {
            int t2 = perm[i2];
            int j2 = i2;
            int u2 = perm[j2 - 1];
            while (a2[t2] < a2[u2]) {
                perm[j2] = u2;
                if (from == j2 - 1) {
                    --j2;
                    break;
                }
                u2 = perm[--j2 - 1];
            }
            perm[j2] = t2;
        }
    }

    public static void quickSortIndirect(int[] perm, int[] x2, int from, int to) {
        int c2;
        int a2;
        int len = to - from;
        if (len < 16) {
            IntArrays.insertionSortIndirect(perm, x2, from, to);
            return;
        }
        int m2 = from + len / 2;
        int l2 = from;
        int n2 = to - 1;
        if (len > 128) {
            int s2 = len / 8;
            l2 = IntArrays.med3Indirect(perm, x2, l2, l2 + s2, l2 + 2 * s2);
            m2 = IntArrays.med3Indirect(perm, x2, m2 - s2, m2, m2 + s2);
            n2 = IntArrays.med3Indirect(perm, x2, n2 - 2 * s2, n2 - s2, n2);
        }
        m2 = IntArrays.med3Indirect(perm, x2, l2, m2, n2);
        int v2 = x2[perm[m2]];
        int b2 = a2 = from;
        int d2 = c2 = to - 1;
        while (true) {
            int comparison;
            if (b2 <= c2 && (comparison = Integer.compare(x2[perm[b2]], v2)) <= 0) {
                if (comparison == 0) {
                    IntArrays.swap(perm, a2++, b2);
                }
                ++b2;
                continue;
            }
            while (c2 >= b2 && (comparison = Integer.compare(x2[perm[c2]], v2)) >= 0) {
                if (comparison == 0) {
                    IntArrays.swap(perm, c2, d2--);
                }
                --c2;
            }
            if (b2 > c2) break;
            IntArrays.swap(perm, b2++, c2--);
        }
        int s3 = Math.min(a2 - from, b2 - a2);
        IntArrays.swap(perm, from, b2 - s3, s3);
        s3 = Math.min(d2 - c2, to - d2 - 1);
        IntArrays.swap(perm, b2, to - s3, s3);
        s3 = b2 - a2;
        if (s3 > 1) {
            IntArrays.quickSortIndirect(perm, x2, from, from + s3);
        }
        if ((s3 = d2 - c2) > 1) {
            IntArrays.quickSortIndirect(perm, x2, to - s3, to);
        }
    }

    public static void quickSortIndirect(int[] perm, int[] x2) {
        IntArrays.quickSortIndirect(perm, x2, 0, x2.length);
    }

    public static void parallelQuickSortIndirect(int[] perm, int[] x2, int from, int to) {
        ForkJoinPool pool = IntArrays.getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            IntArrays.quickSortIndirect(perm, x2, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSortIndirect(perm, x2, from, to));
        }
    }

    public static void parallelQuickSortIndirect(int[] perm, int[] x2) {
        IntArrays.parallelQuickSortIndirect(perm, x2, 0, x2.length);
    }

    public static void stabilize(int[] perm, int[] x2, int from, int to) {
        int curr = from;
        for (int i2 = from + 1; i2 < to; ++i2) {
            if (x2[perm[i2]] == x2[perm[curr]]) continue;
            if (i2 - curr > 1) {
                IntArrays.parallelQuickSort(perm, curr, i2);
            }
            curr = i2;
        }
        if (to - curr > 1) {
            IntArrays.parallelQuickSort(perm, curr, to);
        }
    }

    public static void stabilize(int[] perm, int[] x2) {
        IntArrays.stabilize(perm, x2, 0, perm.length);
    }

    private static int med3(int[] x2, int[] y2, int a2, int b2, int c2) {
        int bc;
        int t2 = Integer.compare(x2[a2], x2[b2]);
        int ab = t2 == 0 ? Integer.compare(y2[a2], y2[b2]) : t2;
        t2 = Integer.compare(x2[a2], x2[c2]);
        int ac = t2 == 0 ? Integer.compare(y2[a2], y2[c2]) : t2;
        t2 = Integer.compare(x2[b2], x2[c2]);
        int n2 = bc = t2 == 0 ? Integer.compare(y2[b2], y2[c2]) : t2;
        return ab < 0 ? (bc < 0 ? b2 : (ac < 0 ? c2 : a2)) : (bc > 0 ? b2 : (ac > 0 ? c2 : a2));
    }

    private static void swap(int[] x2, int[] y2, int a2, int b2) {
        int t2 = x2[a2];
        int u2 = y2[a2];
        x2[a2] = x2[b2];
        y2[a2] = y2[b2];
        x2[b2] = t2;
        y2[b2] = u2;
    }

    private static void swap(int[] x2, int[] y2, int a2, int b2, int n2) {
        int i2 = 0;
        while (i2 < n2) {
            IntArrays.swap(x2, y2, a2, b2);
            ++i2;
            ++a2;
            ++b2;
        }
    }

    private static void selectionSort(int[] a2, int[] b2, int from, int to) {
        for (int i2 = from; i2 < to - 1; ++i2) {
            int m2 = i2;
            for (int j2 = i2 + 1; j2 < to; ++j2) {
                int u2 = Integer.compare(a2[j2], a2[m2]);
                if (u2 >= 0 && (u2 != 0 || b2[j2] >= b2[m2])) continue;
                m2 = j2;
            }
            if (m2 == i2) continue;
            int t2 = a2[i2];
            a2[i2] = a2[m2];
            a2[m2] = t2;
            t2 = b2[i2];
            b2[i2] = b2[m2];
            b2[m2] = t2;
        }
    }

    public static void quickSort(int[] x2, int[] y2, int from, int to) {
        int c2;
        int a2;
        int len = to - from;
        if (len < 16) {
            IntArrays.selectionSort(x2, y2, from, to);
            return;
        }
        int m2 = from + len / 2;
        int l2 = from;
        int n2 = to - 1;
        if (len > 128) {
            int s2 = len / 8;
            l2 = IntArrays.med3(x2, y2, l2, l2 + s2, l2 + 2 * s2);
            m2 = IntArrays.med3(x2, y2, m2 - s2, m2, m2 + s2);
            n2 = IntArrays.med3(x2, y2, n2 - 2 * s2, n2 - s2, n2);
        }
        m2 = IntArrays.med3(x2, y2, l2, m2, n2);
        int v2 = x2[m2];
        int w2 = y2[m2];
        int b2 = a2 = from;
        int d2 = c2 = to - 1;
        while (true) {
            int t2;
            int comparison;
            if (b2 <= c2 && (comparison = (t2 = Integer.compare(x2[b2], v2)) == 0 ? Integer.compare(y2[b2], w2) : t2) <= 0) {
                if (comparison == 0) {
                    IntArrays.swap(x2, y2, a2++, b2);
                }
                ++b2;
                continue;
            }
            while (c2 >= b2 && (comparison = (t2 = Integer.compare(x2[c2], v2)) == 0 ? Integer.compare(y2[c2], w2) : t2) >= 0) {
                if (comparison == 0) {
                    IntArrays.swap(x2, y2, c2, d2--);
                }
                --c2;
            }
            if (b2 > c2) break;
            IntArrays.swap(x2, y2, b2++, c2--);
        }
        int s3 = Math.min(a2 - from, b2 - a2);
        IntArrays.swap(x2, y2, from, b2 - s3, s3);
        s3 = Math.min(d2 - c2, to - d2 - 1);
        IntArrays.swap(x2, y2, b2, to - s3, s3);
        s3 = b2 - a2;
        if (s3 > 1) {
            IntArrays.quickSort(x2, y2, from, from + s3);
        }
        if ((s3 = d2 - c2) > 1) {
            IntArrays.quickSort(x2, y2, to - s3, to);
        }
    }

    public static void quickSort(int[] x2, int[] y2) {
        IntArrays.ensureSameLength(x2, y2);
        IntArrays.quickSort(x2, y2, 0, x2.length);
    }

    public static void parallelQuickSort(int[] x2, int[] y2, int from, int to) {
        ForkJoinPool pool = IntArrays.getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            IntArrays.quickSort(x2, y2, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSort2(x2, y2, from, to));
        }
    }

    public static void parallelQuickSort(int[] x2, int[] y2) {
        IntArrays.ensureSameLength(x2, y2);
        IntArrays.parallelQuickSort(x2, y2, 0, x2.length);
    }

    public static void unstableSort(int[] a2, int from, int to) {
        if (to - from >= 2000) {
            IntArrays.radixSort(a2, from, to);
        } else {
            IntArrays.quickSort(a2, from, to);
        }
    }

    public static void unstableSort(int[] a2) {
        IntArrays.unstableSort(a2, 0, a2.length);
    }

    public static void unstableSort(int[] a2, int from, int to, IntComparator comp) {
        IntArrays.quickSort(a2, from, to, comp);
    }

    public static void unstableSort(int[] a2, IntComparator comp) {
        IntArrays.unstableSort(a2, 0, a2.length, comp);
    }

    public static void mergeSort(int[] a2, int from, int to, int[] supp) {
        int len = to - from;
        if (len < 16) {
            IntArrays.insertionSort(a2, from, to);
            return;
        }
        if (supp == null) {
            supp = java.util.Arrays.copyOf(a2, to);
        }
        int mid = from + to >>> 1;
        IntArrays.mergeSort(supp, from, mid, a2);
        IntArrays.mergeSort(supp, mid, to, a2);
        if (supp[mid - 1] <= supp[mid]) {
            System.arraycopy(supp, from, a2, from, len);
            return;
        }
        int p2 = from;
        int q2 = mid;
        for (int i2 = from; i2 < to; ++i2) {
            a2[i2] = q2 >= to || p2 < mid && supp[p2] <= supp[q2] ? supp[p2++] : supp[q2++];
        }
    }

    public static void mergeSort(int[] a2, int from, int to) {
        IntArrays.mergeSort(a2, from, to, (int[])null);
    }

    public static void mergeSort(int[] a2) {
        IntArrays.mergeSort(a2, 0, a2.length);
    }

    public static void mergeSort(int[] a2, int from, int to, IntComparator comp, int[] supp) {
        int len = to - from;
        if (len < 16) {
            IntArrays.insertionSort(a2, from, to, comp);
            return;
        }
        if (supp == null) {
            supp = java.util.Arrays.copyOf(a2, to);
        }
        int mid = from + to >>> 1;
        IntArrays.mergeSort(supp, from, mid, comp, a2);
        IntArrays.mergeSort(supp, mid, to, comp, a2);
        if (comp.compare(supp[mid - 1], supp[mid]) <= 0) {
            System.arraycopy(supp, from, a2, from, len);
            return;
        }
        int p2 = from;
        int q2 = mid;
        for (int i2 = from; i2 < to; ++i2) {
            a2[i2] = q2 >= to || p2 < mid && comp.compare(supp[p2], supp[q2]) <= 0 ? supp[p2++] : supp[q2++];
        }
    }

    public static void mergeSort(int[] a2, int from, int to, IntComparator comp) {
        IntArrays.mergeSort(a2, from, to, comp, null);
    }

    public static void mergeSort(int[] a2, IntComparator comp) {
        IntArrays.mergeSort(a2, 0, a2.length, comp);
    }

    public static void stableSort(int[] a2, int from, int to) {
        IntArrays.unstableSort(a2, from, to);
    }

    public static void stableSort(int[] a2) {
        IntArrays.stableSort(a2, 0, a2.length);
    }

    public static void stableSort(int[] a2, int from, int to, IntComparator comp) {
        IntArrays.mergeSort(a2, from, to, comp);
    }

    public static void stableSort(int[] a2, IntComparator comp) {
        IntArrays.stableSort(a2, 0, a2.length, comp);
    }

    public static int binarySearch(int[] a2, int from, int to, int key) {
        --to;
        while (from <= to) {
            int mid = from + to >>> 1;
            int midVal = a2[mid];
            if (midVal < key) {
                from = mid + 1;
                continue;
            }
            if (midVal > key) {
                to = mid - 1;
                continue;
            }
            return mid;
        }
        return -(from + 1);
    }

    public static int binarySearch(int[] a2, int key) {
        return IntArrays.binarySearch(a2, 0, a2.length, key);
    }

    public static int binarySearch(int[] a2, int from, int to, int key, IntComparator c2) {
        --to;
        while (from <= to) {
            int mid = from + to >>> 1;
            int midVal = a2[mid];
            int cmp = c2.compare(midVal, key);
            if (cmp < 0) {
                from = mid + 1;
                continue;
            }
            if (cmp > 0) {
                to = mid - 1;
                continue;
            }
            return mid;
        }
        return -(from + 1);
    }

    public static int binarySearch(int[] a2, int key, IntComparator c2) {
        return IntArrays.binarySearch(a2, 0, a2.length, key, c2);
    }

    public static void radixSort(int[] a2) {
        IntArrays.radixSort(a2, 0, a2.length);
    }

    public static void radixSort(int[] a2, int from, int to) {
        if (to - from < 1024) {
            IntArrays.quickSort(a2, from, to);
            return;
        }
        int maxLevel = 3;
        int stackSize = 766;
        int stackPos = 0;
        int[] offsetStack = new int[766];
        int[] lengthStack = new int[766];
        int[] levelStack = new int[766];
        offsetStack[stackPos] = from;
        lengthStack[stackPos] = to - from;
        levelStack[stackPos++] = 0;
        int[] count = new int[256];
        int[] pos = new int[256];
        while (stackPos > 0) {
            int first = offsetStack[--stackPos];
            int length = lengthStack[stackPos];
            int level = levelStack[stackPos];
            int signMask = level % 4 == 0 ? 128 : 0;
            int shift = (3 - level % 4) * 8;
            int i2 = first + length;
            while (i2-- != first) {
                int n2 = a2[i2] >>> shift & 0xFF ^ signMask;
                count[n2] = count[n2] + 1;
            }
            int lastUsed = -1;
            int p2 = first;
            for (int i3 = 0; i3 < 256; ++i3) {
                if (count[i3] != 0) {
                    lastUsed = i3;
                }
                pos[i3] = p2 += count[i3];
            }
            int end = first + length - count[lastUsed];
            int c2 = -1;
            for (int i4 = first; i4 <= end; i4 += count[c2]) {
                int t2 = a2[i4];
                c2 = t2 >>> shift & 0xFF ^ signMask;
                if (i4 < end) {
                    while (true) {
                        int n3 = c2;
                        int n4 = pos[n3] - 1;
                        pos[n3] = n4;
                        int d2 = n4;
                        if (n4 <= i4) break;
                        int z2 = t2;
                        t2 = a2[d2];
                        a2[d2] = z2;
                        c2 = t2 >>> shift & 0xFF ^ signMask;
                    }
                    a2[i4] = t2;
                }
                if (level < 3 && count[c2] > 1) {
                    if (count[c2] < 1024) {
                        IntArrays.quickSort(a2, i4, i4 + count[c2]);
                    } else {
                        offsetStack[stackPos] = i4;
                        lengthStack[stackPos] = count[c2];
                        levelStack[stackPos++] = level + 1;
                    }
                }
                count[c2] = 0;
            }
        }
    }

    public static void parallelRadixSort(int[] a2, int from, int to) {
        ForkJoinPool pool = IntArrays.getPool();
        if (to - from < 1024 || pool.getParallelism() == 1) {
            IntArrays.quickSort(a2, from, to);
            return;
        }
        int maxLevel = 3;
        LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<Segment>();
        queue.add(new Segment(from, to - from, 0));
        AtomicInteger queueSize = new AtomicInteger(1);
        int numberOfThreads = pool.getParallelism();
        ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<Void>(pool);
        int j2 = numberOfThreads;
        while (j2-- != 0) {
            executorCompletionService.submit(() -> {
                int[] count = new int[256];
                int[] pos = new int[256];
                while (true) {
                    Segment segment;
                    if (queueSize.get() == 0) {
                        int i2 = numberOfThreads;
                        while (i2-- != 0) {
                            queue.add(POISON_PILL);
                        }
                    }
                    if ((segment = (Segment)queue.take()) == POISON_PILL) {
                        return null;
                    }
                    int first = segment.offset;
                    int length = segment.length;
                    int level = segment.level;
                    int signMask = level % 4 == 0 ? 128 : 0;
                    int shift = (3 - level % 4) * 8;
                    int i3 = first + length;
                    while (i3-- != first) {
                        int n2 = a2[i3] >>> shift & 0xFF ^ signMask;
                        count[n2] = count[n2] + 1;
                    }
                    int lastUsed = -1;
                    int p2 = first;
                    for (int i4 = 0; i4 < 256; ++i4) {
                        if (count[i4] != 0) {
                            lastUsed = i4;
                        }
                        pos[i4] = p2 += count[i4];
                    }
                    int end = first + length - count[lastUsed];
                    int c2 = -1;
                    for (int i5 = first; i5 <= end; i5 += count[c2]) {
                        int t2 = a2[i5];
                        c2 = t2 >>> shift & 0xFF ^ signMask;
                        if (i5 < end) {
                            while (true) {
                                int n3 = c2;
                                int n4 = pos[n3] - 1;
                                pos[n3] = n4;
                                int d2 = n4;
                                if (n4 <= i5) break;
                                int z2 = t2;
                                t2 = a2[d2];
                                a2[d2] = z2;
                                c2 = t2 >>> shift & 0xFF ^ signMask;
                            }
                            a2[i5] = t2;
                        }
                        if (level < 3 && count[c2] > 1) {
                            if (count[c2] < 1024) {
                                IntArrays.quickSort(a2, i5, i5 + count[c2]);
                            } else {
                                queueSize.incrementAndGet();
                                queue.add(new Segment(i5, count[c2], level + 1));
                            }
                        }
                        count[c2] = 0;
                    }
                    queueSize.decrementAndGet();
                }
            });
        }
        Throwable problem = null;
        int i2 = numberOfThreads;
        while (i2-- != 0) {
            try {
                executorCompletionService.take().get();
            }
            catch (Exception e2) {
                problem = e2.getCause();
            }
        }
        if (problem != null) {
            throw problem instanceof RuntimeException ? (RuntimeException)problem : new RuntimeException(problem);
        }
    }

    public static void parallelRadixSort(int[] a2) {
        IntArrays.parallelRadixSort(a2, 0, a2.length);
    }

    public static void radixSortIndirect(int[] perm, int[] a2, boolean stable) {
        IntArrays.radixSortIndirect(perm, a2, 0, perm.length, stable);
    }

    public static void radixSortIndirect(int[] perm, int[] a2, int from, int to, boolean stable) {
        int[] support;
        if (to - from < 1024) {
            IntArrays.insertionSortIndirect(perm, a2, from, to);
            return;
        }
        int maxLevel = 3;
        int stackSize = 766;
        int stackPos = 0;
        int[] offsetStack = new int[766];
        int[] lengthStack = new int[766];
        int[] levelStack = new int[766];
        offsetStack[stackPos] = from;
        lengthStack[stackPos] = to - from;
        levelStack[stackPos++] = 0;
        int[] count = new int[256];
        int[] pos = new int[256];
        int[] nArray = support = stable ? new int[perm.length] : null;
        while (stackPos > 0) {
            int i2;
            int p2;
            int first = offsetStack[--stackPos];
            int length = lengthStack[stackPos];
            int level = levelStack[stackPos];
            int signMask = level % 4 == 0 ? 128 : 0;
            int shift = (3 - level % 4) * 8;
            int i3 = first + length;
            while (i3-- != first) {
                int n2 = a2[perm[i3]] >>> shift & 0xFF ^ signMask;
                count[n2] = count[n2] + 1;
            }
            int lastUsed = -1;
            int n3 = p2 = stable ? 0 : first;
            for (i2 = 0; i2 < 256; ++i2) {
                if (count[i2] != 0) {
                    lastUsed = i2;
                }
                pos[i2] = p2 += count[i2];
            }
            if (stable) {
                i2 = first + length;
                while (i2-- != first) {
                    int n4 = a2[perm[i2]] >>> shift & 0xFF ^ signMask;
                    int n5 = pos[n4] - 1;
                    pos[n4] = n5;
                    support[n5] = perm[i2];
                }
                System.arraycopy(support, 0, perm, first, length);
                p2 = first;
                for (i2 = 0; i2 <= lastUsed; ++i2) {
                    if (level < 3 && count[i2] > 1) {
                        if (count[i2] < 1024) {
                            IntArrays.insertionSortIndirect(perm, a2, p2, p2 + count[i2]);
                        } else {
                            offsetStack[stackPos] = p2;
                            lengthStack[stackPos] = count[i2];
                            levelStack[stackPos++] = level + 1;
                        }
                    }
                    p2 += count[i2];
                }
                java.util.Arrays.fill(count, 0);
                continue;
            }
            int end = first + length - count[lastUsed];
            int c2 = -1;
            for (int i4 = first; i4 <= end; i4 += count[c2]) {
                int t2 = perm[i4];
                c2 = a2[t2] >>> shift & 0xFF ^ signMask;
                if (i4 < end) {
                    while (true) {
                        int n6 = c2;
                        int n7 = pos[n6] - 1;
                        pos[n6] = n7;
                        int d2 = n7;
                        if (n7 <= i4) break;
                        int z2 = t2;
                        t2 = perm[d2];
                        perm[d2] = z2;
                        c2 = a2[t2] >>> shift & 0xFF ^ signMask;
                    }
                    perm[i4] = t2;
                }
                if (level < 3 && count[c2] > 1) {
                    if (count[c2] < 1024) {
                        IntArrays.insertionSortIndirect(perm, a2, i4, i4 + count[c2]);
                    } else {
                        offsetStack[stackPos] = i4;
                        lengthStack[stackPos] = count[c2];
                        levelStack[stackPos++] = level + 1;
                    }
                }
                count[c2] = 0;
            }
        }
    }

    public static void parallelRadixSortIndirect(int[] perm, int[] a2, int from, int to, boolean stable) {
        ForkJoinPool pool = IntArrays.getPool();
        if (to - from < 1024 || pool.getParallelism() == 1) {
            IntArrays.radixSortIndirect(perm, a2, from, to, stable);
            return;
        }
        int maxLevel = 3;
        LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<Segment>();
        queue.add(new Segment(from, to - from, 0));
        AtomicInteger queueSize = new AtomicInteger(1);
        int numberOfThreads = pool.getParallelism();
        ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<Void>(pool);
        int[] support = stable ? new int[perm.length] : null;
        int j2 = numberOfThreads;
        while (j2-- != 0) {
            executorCompletionService.submit(() -> {
                int[] count = new int[256];
                int[] pos = new int[256];
                while (true) {
                    int i2;
                    Segment segment;
                    if (queueSize.get() == 0) {
                        int i3 = numberOfThreads;
                        while (i3-- != 0) {
                            queue.add(POISON_PILL);
                        }
                    }
                    if ((segment = (Segment)queue.take()) == POISON_PILL) {
                        return null;
                    }
                    int first = segment.offset;
                    int length = segment.length;
                    int level = segment.level;
                    int signMask = level % 4 == 0 ? 128 : 0;
                    int shift = (3 - level % 4) * 8;
                    int i4 = first + length;
                    while (i4-- != first) {
                        int n2 = a2[perm[i4]] >>> shift & 0xFF ^ signMask;
                        count[n2] = count[n2] + 1;
                    }
                    int lastUsed = -1;
                    int p2 = first;
                    for (i2 = 0; i2 < 256; ++i2) {
                        if (count[i2] != 0) {
                            lastUsed = i2;
                        }
                        pos[i2] = p2 += count[i2];
                    }
                    if (stable) {
                        i2 = first + length;
                        while (i2-- != first) {
                            int n3 = a2[perm[i2]] >>> shift & 0xFF ^ signMask;
                            int n4 = pos[n3] - 1;
                            pos[n3] = n4;
                            support[n4] = perm[i2];
                        }
                        System.arraycopy(support, first, perm, first, length);
                        p2 = first;
                        for (i2 = 0; i2 <= lastUsed; ++i2) {
                            if (level < 3 && count[i2] > 1) {
                                if (count[i2] < 1024) {
                                    IntArrays.radixSortIndirect(perm, a2, p2, p2 + count[i2], stable);
                                } else {
                                    queueSize.incrementAndGet();
                                    queue.add(new Segment(p2, count[i2], level + 1));
                                }
                            }
                            p2 += count[i2];
                        }
                        java.util.Arrays.fill(count, 0);
                    } else {
                        int end = first + length - count[lastUsed];
                        int c2 = -1;
                        for (int i5 = first; i5 <= end; i5 += count[c2]) {
                            int t2 = perm[i5];
                            c2 = a2[t2] >>> shift & 0xFF ^ signMask;
                            if (i5 < end) {
                                while (true) {
                                    int n5 = c2;
                                    int n6 = pos[n5] - 1;
                                    pos[n5] = n6;
                                    int d2 = n6;
                                    if (n6 <= i5) break;
                                    int z2 = t2;
                                    t2 = perm[d2];
                                    perm[d2] = z2;
                                    c2 = a2[t2] >>> shift & 0xFF ^ signMask;
                                }
                                perm[i5] = t2;
                            }
                            if (level < 3 && count[c2] > 1) {
                                if (count[c2] < 1024) {
                                    IntArrays.radixSortIndirect(perm, a2, i5, i5 + count[c2], stable);
                                } else {
                                    queueSize.incrementAndGet();
                                    queue.add(new Segment(i5, count[c2], level + 1));
                                }
                            }
                            count[c2] = 0;
                        }
                    }
                    queueSize.decrementAndGet();
                }
            });
        }
        Throwable problem = null;
        int i2 = numberOfThreads;
        while (i2-- != 0) {
            try {
                executorCompletionService.take().get();
            }
            catch (Exception e2) {
                problem = e2.getCause();
            }
        }
        if (problem != null) {
            throw problem instanceof RuntimeException ? (RuntimeException)problem : new RuntimeException(problem);
        }
    }

    public static void parallelRadixSortIndirect(int[] perm, int[] a2, boolean stable) {
        IntArrays.parallelRadixSortIndirect(perm, a2, 0, a2.length, stable);
    }

    public static void radixSort(int[] a2, int[] b2) {
        IntArrays.ensureSameLength(a2, b2);
        IntArrays.radixSort(a2, b2, 0, a2.length);
    }

    public static void radixSort(int[] a2, int[] b2, int from, int to) {
        if (to - from < 1024) {
            IntArrays.selectionSort(a2, b2, from, to);
            return;
        }
        int layers = 2;
        int maxLevel = 7;
        int stackSize = 1786;
        int stackPos = 0;
        int[] offsetStack = new int[1786];
        int[] lengthStack = new int[1786];
        int[] levelStack = new int[1786];
        offsetStack[stackPos] = from;
        lengthStack[stackPos] = to - from;
        levelStack[stackPos++] = 0;
        int[] count = new int[256];
        int[] pos = new int[256];
        while (stackPos > 0) {
            int first = offsetStack[--stackPos];
            int length = lengthStack[stackPos];
            int level = levelStack[stackPos];
            int signMask = level % 4 == 0 ? 128 : 0;
            int[] k2 = level < 4 ? a2 : b2;
            int shift = (3 - level % 4) * 8;
            int i2 = first + length;
            while (i2-- != first) {
                int n2 = k2[i2] >>> shift & 0xFF ^ signMask;
                count[n2] = count[n2] + 1;
            }
            int lastUsed = -1;
            int p2 = first;
            for (int i3 = 0; i3 < 256; ++i3) {
                if (count[i3] != 0) {
                    lastUsed = i3;
                }
                pos[i3] = p2 += count[i3];
            }
            int end = first + length - count[lastUsed];
            int c2 = -1;
            for (int i4 = first; i4 <= end; i4 += count[c2]) {
                int t2 = a2[i4];
                int u2 = b2[i4];
                c2 = k2[i4] >>> shift & 0xFF ^ signMask;
                if (i4 < end) {
                    while (true) {
                        int n3 = c2;
                        int n4 = pos[n3] - 1;
                        pos[n3] = n4;
                        int d2 = n4;
                        if (n4 <= i4) break;
                        c2 = k2[d2] >>> shift & 0xFF ^ signMask;
                        int z2 = t2;
                        t2 = a2[d2];
                        a2[d2] = z2;
                        z2 = u2;
                        u2 = b2[d2];
                        b2[d2] = z2;
                    }
                    a2[i4] = t2;
                    b2[i4] = u2;
                }
                if (level < 7 && count[c2] > 1) {
                    if (count[c2] < 1024) {
                        IntArrays.selectionSort(a2, b2, i4, i4 + count[c2]);
                    } else {
                        offsetStack[stackPos] = i4;
                        lengthStack[stackPos] = count[c2];
                        levelStack[stackPos++] = level + 1;
                    }
                }
                count[c2] = 0;
            }
        }
    }

    public static void parallelRadixSort(int[] a2, int[] b2, int from, int to) {
        ForkJoinPool pool = IntArrays.getPool();
        if (to - from < 1024 || pool.getParallelism() == 1) {
            IntArrays.quickSort(a2, b2, from, to);
            return;
        }
        int layers = 2;
        if (a2.length != b2.length) {
            throw new IllegalArgumentException("Array size mismatch.");
        }
        int maxLevel = 7;
        LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<Segment>();
        queue.add(new Segment(from, to - from, 0));
        AtomicInteger queueSize = new AtomicInteger(1);
        int numberOfThreads = pool.getParallelism();
        ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<Void>(pool);
        int j2 = numberOfThreads;
        while (j2-- != 0) {
            executorCompletionService.submit(() -> {
                int[] count = new int[256];
                int[] pos = new int[256];
                while (true) {
                    Segment segment;
                    if (queueSize.get() == 0) {
                        int i2 = numberOfThreads;
                        while (i2-- != 0) {
                            queue.add(POISON_PILL);
                        }
                    }
                    if ((segment = (Segment)queue.take()) == POISON_PILL) {
                        return null;
                    }
                    int first = segment.offset;
                    int length = segment.length;
                    int level = segment.level;
                    int signMask = level % 4 == 0 ? 128 : 0;
                    int[] k2 = level < 4 ? a2 : b2;
                    int shift = (3 - level % 4) * 8;
                    int i3 = first + length;
                    while (i3-- != first) {
                        int n2 = k2[i3] >>> shift & 0xFF ^ signMask;
                        count[n2] = count[n2] + 1;
                    }
                    int lastUsed = -1;
                    int p2 = first;
                    for (int i4 = 0; i4 < 256; ++i4) {
                        if (count[i4] != 0) {
                            lastUsed = i4;
                        }
                        pos[i4] = p2 += count[i4];
                    }
                    int end = first + length - count[lastUsed];
                    int c2 = -1;
                    for (int i5 = first; i5 <= end; i5 += count[c2]) {
                        int t2 = a2[i5];
                        int u2 = b2[i5];
                        c2 = k2[i5] >>> shift & 0xFF ^ signMask;
                        if (i5 < end) {
                            while (true) {
                                int n3 = c2;
                                int n4 = pos[n3] - 1;
                                pos[n3] = n4;
                                int d2 = n4;
                                if (n4 <= i5) break;
                                c2 = k2[d2] >>> shift & 0xFF ^ signMask;
                                int z2 = t2;
                                int w2 = u2;
                                t2 = a2[d2];
                                u2 = b2[d2];
                                a2[d2] = z2;
                                b2[d2] = w2;
                            }
                            a2[i5] = t2;
                            b2[i5] = u2;
                        }
                        if (level < 7 && count[c2] > 1) {
                            if (count[c2] < 1024) {
                                IntArrays.quickSort(a2, b2, i5, i5 + count[c2]);
                            } else {
                                queueSize.incrementAndGet();
                                queue.add(new Segment(i5, count[c2], level + 1));
                            }
                        }
                        count[c2] = 0;
                    }
                    queueSize.decrementAndGet();
                }
            });
        }
        Throwable problem = null;
        int i2 = numberOfThreads;
        while (i2-- != 0) {
            try {
                executorCompletionService.take().get();
            }
            catch (Exception e2) {
                problem = e2.getCause();
            }
        }
        if (problem != null) {
            throw problem instanceof RuntimeException ? (RuntimeException)problem : new RuntimeException(problem);
        }
    }

    public static void parallelRadixSort(int[] a2, int[] b2) {
        IntArrays.ensureSameLength(a2, b2);
        IntArrays.parallelRadixSort(a2, b2, 0, a2.length);
    }

    private static void insertionSortIndirect(int[] perm, int[] a2, int[] b2, int from, int to) {
        int i2 = from;
        while (++i2 < to) {
            int t2 = perm[i2];
            int j2 = i2;
            int u2 = perm[j2 - 1];
            while (a2[t2] < a2[u2] || a2[t2] == a2[u2] && b2[t2] < b2[u2]) {
                perm[j2] = u2;
                if (from == j2 - 1) {
                    --j2;
                    break;
                }
                u2 = perm[--j2 - 1];
            }
            perm[j2] = t2;
        }
    }

    public static void radixSortIndirect(int[] perm, int[] a2, int[] b2, boolean stable) {
        IntArrays.ensureSameLength(a2, b2);
        IntArrays.radixSortIndirect(perm, a2, b2, 0, a2.length, stable);
    }

    public static void radixSortIndirect(int[] perm, int[] a2, int[] b2, int from, int to, boolean stable) {
        int[] support;
        if (to - from < 1024) {
            IntArrays.insertionSortIndirect(perm, a2, b2, from, to);
            return;
        }
        int layers = 2;
        int maxLevel = 7;
        int stackSize = 1786;
        int stackPos = 0;
        int[] offsetStack = new int[1786];
        int[] lengthStack = new int[1786];
        int[] levelStack = new int[1786];
        offsetStack[stackPos] = from;
        lengthStack[stackPos] = to - from;
        levelStack[stackPos++] = 0;
        int[] count = new int[256];
        int[] pos = new int[256];
        int[] nArray = support = stable ? new int[perm.length] : null;
        while (stackPos > 0) {
            int i2;
            int p2;
            int first = offsetStack[--stackPos];
            int length = lengthStack[stackPos];
            int level = levelStack[stackPos];
            int signMask = level % 4 == 0 ? 128 : 0;
            int[] k2 = level < 4 ? a2 : b2;
            int shift = (3 - level % 4) * 8;
            int i3 = first + length;
            while (i3-- != first) {
                int n2 = k2[perm[i3]] >>> shift & 0xFF ^ signMask;
                count[n2] = count[n2] + 1;
            }
            int lastUsed = -1;
            int n3 = p2 = stable ? 0 : first;
            for (i2 = 0; i2 < 256; ++i2) {
                if (count[i2] != 0) {
                    lastUsed = i2;
                }
                pos[i2] = p2 += count[i2];
            }
            if (stable) {
                i2 = first + length;
                while (i2-- != first) {
                    int n4 = k2[perm[i2]] >>> shift & 0xFF ^ signMask;
                    int n5 = pos[n4] - 1;
                    pos[n4] = n5;
                    support[n5] = perm[i2];
                }
                System.arraycopy(support, 0, perm, first, length);
                p2 = first;
                for (i2 = 0; i2 < 256; ++i2) {
                    if (level < 7 && count[i2] > 1) {
                        if (count[i2] < 1024) {
                            IntArrays.insertionSortIndirect(perm, a2, b2, p2, p2 + count[i2]);
                        } else {
                            offsetStack[stackPos] = p2;
                            lengthStack[stackPos] = count[i2];
                            levelStack[stackPos++] = level + 1;
                        }
                    }
                    p2 += count[i2];
                }
                java.util.Arrays.fill(count, 0);
                continue;
            }
            int end = first + length - count[lastUsed];
            int c2 = -1;
            for (int i4 = first; i4 <= end; i4 += count[c2]) {
                int t2 = perm[i4];
                c2 = k2[t2] >>> shift & 0xFF ^ signMask;
                if (i4 < end) {
                    while (true) {
                        int n6 = c2;
                        int n7 = pos[n6] - 1;
                        pos[n6] = n7;
                        int d2 = n7;
                        if (n7 <= i4) break;
                        int z2 = t2;
                        t2 = perm[d2];
                        perm[d2] = z2;
                        c2 = k2[t2] >>> shift & 0xFF ^ signMask;
                    }
                    perm[i4] = t2;
                }
                if (level < 7 && count[c2] > 1) {
                    if (count[c2] < 1024) {
                        IntArrays.insertionSortIndirect(perm, a2, b2, i4, i4 + count[c2]);
                    } else {
                        offsetStack[stackPos] = i4;
                        lengthStack[stackPos] = count[c2];
                        levelStack[stackPos++] = level + 1;
                    }
                }
                count[c2] = 0;
            }
        }
    }

    private static void selectionSort(int[][] a2, int from, int to, int level) {
        int layers = a2.length;
        int firstLayer = level / 4;
        for (int i2 = from; i2 < to - 1; ++i2) {
            int m2 = i2;
            block1: for (int j2 = i2 + 1; j2 < to; ++j2) {
                for (int p2 = firstLayer; p2 < layers; ++p2) {
                    if (a2[p2][j2] < a2[p2][m2]) {
                        m2 = j2;
                        continue block1;
                    }
                    if (a2[p2][j2] > a2[p2][m2]) continue block1;
                }
            }
            if (m2 == i2) continue;
            int p3 = layers;
            while (p3-- != 0) {
                int u2 = a2[p3][i2];
                a2[p3][i2] = a2[p3][m2];
                a2[p3][m2] = u2;
            }
        }
    }

    public static void radixSort(int[][] a2) {
        IntArrays.radixSort(a2, 0, a2[0].length);
    }

    public static void radixSort(int[][] a2, int from, int to) {
        if (to - from < 1024) {
            IntArrays.selectionSort(a2, from, to, 0);
            return;
        }
        int layers = a2.length;
        int maxLevel = 4 * layers - 1;
        int p2 = layers;
        int l2 = a2[0].length;
        while (p2-- != 0) {
            if (a2[p2].length == l2) continue;
            throw new IllegalArgumentException("The array of index " + p2 + " has not the same length of the array of index 0.");
        }
        int stackSize = 255 * (layers * 4 - 1) + 1;
        int stackPos = 0;
        int[] offsetStack = new int[stackSize];
        int[] lengthStack = new int[stackSize];
        int[] levelStack = new int[stackSize];
        offsetStack[stackPos] = from;
        lengthStack[stackPos] = to - from;
        levelStack[stackPos++] = 0;
        int[] count = new int[256];
        int[] pos = new int[256];
        int[] t2 = new int[layers];
        while (stackPos > 0) {
            int first = offsetStack[--stackPos];
            int length = lengthStack[stackPos];
            int level = levelStack[stackPos];
            int signMask = level % 4 == 0 ? 128 : 0;
            int[] k2 = a2[level / 4];
            int shift = (3 - level % 4) * 8;
            int i2 = first + length;
            while (i2-- != first) {
                int n2 = k2[i2] >>> shift & 0xFF ^ signMask;
                count[n2] = count[n2] + 1;
            }
            int lastUsed = -1;
            int p3 = first;
            for (int i3 = 0; i3 < 256; ++i3) {
                if (count[i3] != 0) {
                    lastUsed = i3;
                }
                pos[i3] = p3 += count[i3];
            }
            int end = first + length - count[lastUsed];
            int c2 = -1;
            for (int i4 = first; i4 <= end; i4 += count[c2]) {
                int p4 = layers;
                while (p4-- != 0) {
                    t2[p4] = a2[p4][i4];
                }
                c2 = k2[i4] >>> shift & 0xFF ^ signMask;
                if (i4 < end) {
                    block6: while (true) {
                        int n3 = c2;
                        int n4 = pos[n3] - 1;
                        pos[n3] = n4;
                        int d2 = n4;
                        if (n4 <= i4) break;
                        c2 = k2[d2] >>> shift & 0xFF ^ signMask;
                        p4 = layers;
                        while (true) {
                            if (p4-- == 0) continue block6;
                            int u2 = t2[p4];
                            t2[p4] = a2[p4][d2];
                            a2[p4][d2] = u2;
                        }
                        break;
                    }
                    p4 = layers;
                    while (p4-- != 0) {
                        a2[p4][i4] = t2[p4];
                    }
                }
                if (level < maxLevel && count[c2] > 1) {
                    if (count[c2] < 1024) {
                        IntArrays.selectionSort(a2, i4, i4 + count[c2], level + 1);
                    } else {
                        offsetStack[stackPos] = i4;
                        lengthStack[stackPos] = count[c2];
                        levelStack[stackPos++] = level + 1;
                    }
                }
                count[c2] = 0;
            }
        }
    }

    public static int[] shuffle(int[] a2, int from, int to, Random random) {
        int i2 = to - from;
        while (i2-- != 0) {
            int p2 = random.nextInt(i2 + 1);
            int t2 = a2[from + i2];
            a2[from + i2] = a2[from + p2];
            a2[from + p2] = t2;
        }
        return a2;
    }

    public static int[] shuffle(int[] a2, Random random) {
        int i2 = a2.length;
        while (i2-- != 0) {
            int p2 = random.nextInt(i2 + 1);
            int t2 = a2[i2];
            a2[i2] = a2[p2];
            a2[p2] = t2;
        }
        return a2;
    }

    public static int[] reverse(int[] a2) {
        int length = a2.length;
        int i2 = length / 2;
        while (i2-- != 0) {
            int t2 = a2[length - i2 - 1];
            a2[length - i2 - 1] = a2[i2];
            a2[i2] = t2;
        }
        return a2;
    }

    public static int[] reverse(int[] a2, int from, int to) {
        int length = to - from;
        int i2 = length / 2;
        while (i2-- != 0) {
            int t2 = a2[from + length - i2 - 1];
            a2[from + length - i2 - 1] = a2[from + i2];
            a2[from + i2] = t2;
        }
        return a2;
    }

    protected static class ForkJoinQuickSortComp
    extends RecursiveAction {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final int[] x;
        private final IntComparator comp;

        public ForkJoinQuickSortComp(int[] x2, int from, int to, IntComparator comp) {
            this.from = from;
            this.to = to;
            this.x = x2;
            this.comp = comp;
        }

        @Override
        protected void compute() {
            int c2;
            int a2;
            int[] x2 = this.x;
            int len = this.to - this.from;
            if (len < 8192) {
                IntArrays.quickSort(x2, this.from, this.to, this.comp);
                return;
            }
            int m2 = this.from + len / 2;
            int l2 = this.from;
            int n2 = this.to - 1;
            int s2 = len / 8;
            l2 = IntArrays.med3(x2, l2, l2 + s2, l2 + 2 * s2, this.comp);
            m2 = IntArrays.med3(x2, m2 - s2, m2, m2 + s2, this.comp);
            n2 = IntArrays.med3(x2, n2 - 2 * s2, n2 - s2, n2, this.comp);
            m2 = IntArrays.med3(x2, l2, m2, n2, this.comp);
            int v2 = x2[m2];
            int b2 = a2 = this.from;
            int d2 = c2 = this.to - 1;
            while (true) {
                int comparison;
                if (b2 <= c2 && (comparison = this.comp.compare(x2[b2], v2)) <= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(x2, a2++, b2);
                    }
                    ++b2;
                    continue;
                }
                while (c2 >= b2 && (comparison = this.comp.compare(x2[c2], v2)) >= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(x2, c2, d2--);
                    }
                    --c2;
                }
                if (b2 > c2) break;
                IntArrays.swap(x2, b2++, c2--);
            }
            s2 = Math.min(a2 - this.from, b2 - a2);
            IntArrays.swap(x2, this.from, b2 - s2, s2);
            s2 = Math.min(d2 - c2, this.to - d2 - 1);
            IntArrays.swap(x2, b2, this.to - s2, s2);
            s2 = b2 - a2;
            int t2 = d2 - c2;
            if (s2 > 1 && t2 > 1) {
                ForkJoinQuickSortComp.invokeAll(new ForkJoinQuickSortComp(x2, this.from, this.from + s2, this.comp), new ForkJoinQuickSortComp(x2, this.to - t2, this.to, this.comp));
            } else if (s2 > 1) {
                ForkJoinQuickSortComp.invokeAll(new ForkJoinQuickSortComp(x2, this.from, this.from + s2, this.comp));
            } else {
                ForkJoinQuickSortComp.invokeAll(new ForkJoinQuickSortComp(x2, this.to - t2, this.to, this.comp));
            }
        }
    }

    protected static class ForkJoinQuickSort
    extends RecursiveAction {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final int[] x;

        public ForkJoinQuickSort(int[] x2, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x2;
        }

        @Override
        protected void compute() {
            int c2;
            int a2;
            int[] x2 = this.x;
            int len = this.to - this.from;
            if (len < 8192) {
                IntArrays.quickSort(x2, this.from, this.to);
                return;
            }
            int m2 = this.from + len / 2;
            int l2 = this.from;
            int n2 = this.to - 1;
            int s2 = len / 8;
            l2 = IntArrays.med3(x2, l2, l2 + s2, l2 + 2 * s2);
            m2 = IntArrays.med3(x2, m2 - s2, m2, m2 + s2);
            n2 = IntArrays.med3(x2, n2 - 2 * s2, n2 - s2, n2);
            m2 = IntArrays.med3(x2, l2, m2, n2);
            int v2 = x2[m2];
            int b2 = a2 = this.from;
            int d2 = c2 = this.to - 1;
            while (true) {
                int comparison;
                if (b2 <= c2 && (comparison = Integer.compare(x2[b2], v2)) <= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(x2, a2++, b2);
                    }
                    ++b2;
                    continue;
                }
                while (c2 >= b2 && (comparison = Integer.compare(x2[c2], v2)) >= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(x2, c2, d2--);
                    }
                    --c2;
                }
                if (b2 > c2) break;
                IntArrays.swap(x2, b2++, c2--);
            }
            s2 = Math.min(a2 - this.from, b2 - a2);
            IntArrays.swap(x2, this.from, b2 - s2, s2);
            s2 = Math.min(d2 - c2, this.to - d2 - 1);
            IntArrays.swap(x2, b2, this.to - s2, s2);
            s2 = b2 - a2;
            int t2 = d2 - c2;
            if (s2 > 1 && t2 > 1) {
                ForkJoinQuickSort.invokeAll(new ForkJoinQuickSort(x2, this.from, this.from + s2), new ForkJoinQuickSort(x2, this.to - t2, this.to));
            } else if (s2 > 1) {
                ForkJoinQuickSort.invokeAll(new ForkJoinQuickSort(x2, this.from, this.from + s2));
            } else {
                ForkJoinQuickSort.invokeAll(new ForkJoinQuickSort(x2, this.to - t2, this.to));
            }
        }
    }

    protected static class ForkJoinQuickSortIndirect
    extends RecursiveAction {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final int[] perm;
        private final int[] x;

        public ForkJoinQuickSortIndirect(int[] perm, int[] x2, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x2;
            this.perm = perm;
        }

        @Override
        protected void compute() {
            int c2;
            int a2;
            int[] x2 = this.x;
            int len = this.to - this.from;
            if (len < 8192) {
                IntArrays.quickSortIndirect(this.perm, x2, this.from, this.to);
                return;
            }
            int m2 = this.from + len / 2;
            int l2 = this.from;
            int n2 = this.to - 1;
            int s2 = len / 8;
            l2 = IntArrays.med3Indirect(this.perm, x2, l2, l2 + s2, l2 + 2 * s2);
            m2 = IntArrays.med3Indirect(this.perm, x2, m2 - s2, m2, m2 + s2);
            n2 = IntArrays.med3Indirect(this.perm, x2, n2 - 2 * s2, n2 - s2, n2);
            m2 = IntArrays.med3Indirect(this.perm, x2, l2, m2, n2);
            int v2 = x2[this.perm[m2]];
            int b2 = a2 = this.from;
            int d2 = c2 = this.to - 1;
            while (true) {
                int comparison;
                if (b2 <= c2 && (comparison = Integer.compare(x2[this.perm[b2]], v2)) <= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(this.perm, a2++, b2);
                    }
                    ++b2;
                    continue;
                }
                while (c2 >= b2 && (comparison = Integer.compare(x2[this.perm[c2]], v2)) >= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(this.perm, c2, d2--);
                    }
                    --c2;
                }
                if (b2 > c2) break;
                IntArrays.swap(this.perm, b2++, c2--);
            }
            s2 = Math.min(a2 - this.from, b2 - a2);
            IntArrays.swap(this.perm, this.from, b2 - s2, s2);
            s2 = Math.min(d2 - c2, this.to - d2 - 1);
            IntArrays.swap(this.perm, b2, this.to - s2, s2);
            s2 = b2 - a2;
            int t2 = d2 - c2;
            if (s2 > 1 && t2 > 1) {
                ForkJoinQuickSortIndirect.invokeAll(new ForkJoinQuickSortIndirect(this.perm, x2, this.from, this.from + s2), new ForkJoinQuickSortIndirect(this.perm, x2, this.to - t2, this.to));
            } else if (s2 > 1) {
                ForkJoinQuickSortIndirect.invokeAll(new ForkJoinQuickSortIndirect(this.perm, x2, this.from, this.from + s2));
            } else {
                ForkJoinQuickSortIndirect.invokeAll(new ForkJoinQuickSortIndirect(this.perm, x2, this.to - t2, this.to));
            }
        }
    }

    protected static class ForkJoinQuickSort2
    extends RecursiveAction {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final int[] x;
        private final int[] y;

        public ForkJoinQuickSort2(int[] x2, int[] y2, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x2;
            this.y = y2;
        }

        @Override
        protected void compute() {
            int c2;
            int a2;
            int[] x2 = this.x;
            int[] y2 = this.y;
            int len = this.to - this.from;
            if (len < 8192) {
                IntArrays.quickSort(x2, y2, this.from, this.to);
                return;
            }
            int m2 = this.from + len / 2;
            int l2 = this.from;
            int n2 = this.to - 1;
            int s2 = len / 8;
            l2 = IntArrays.med3(x2, y2, l2, l2 + s2, l2 + 2 * s2);
            m2 = IntArrays.med3(x2, y2, m2 - s2, m2, m2 + s2);
            n2 = IntArrays.med3(x2, y2, n2 - 2 * s2, n2 - s2, n2);
            m2 = IntArrays.med3(x2, y2, l2, m2, n2);
            int v2 = x2[m2];
            int w2 = y2[m2];
            int b2 = a2 = this.from;
            int d2 = c2 = this.to - 1;
            while (true) {
                int t2;
                int comparison;
                if (b2 <= c2 && (comparison = (t2 = Integer.compare(x2[b2], v2)) == 0 ? Integer.compare(y2[b2], w2) : t2) <= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(x2, y2, a2++, b2);
                    }
                    ++b2;
                    continue;
                }
                while (c2 >= b2 && (comparison = (t2 = Integer.compare(x2[c2], v2)) == 0 ? Integer.compare(y2[c2], w2) : t2) >= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(x2, y2, c2, d2--);
                    }
                    --c2;
                }
                if (b2 > c2) break;
                IntArrays.swap(x2, y2, b2++, c2--);
            }
            s2 = Math.min(a2 - this.from, b2 - a2);
            IntArrays.swap(x2, y2, this.from, b2 - s2, s2);
            s2 = Math.min(d2 - c2, this.to - d2 - 1);
            IntArrays.swap(x2, y2, b2, this.to - s2, s2);
            s2 = b2 - a2;
            int t3 = d2 - c2;
            if (s2 > 1 && t3 > 1) {
                ForkJoinQuickSort2.invokeAll(new ForkJoinQuickSort2(x2, y2, this.from, this.from + s2), new ForkJoinQuickSort2(x2, y2, this.to - t3, this.to));
            } else if (s2 > 1) {
                ForkJoinQuickSort2.invokeAll(new ForkJoinQuickSort2(x2, y2, this.from, this.from + s2));
            } else {
                ForkJoinQuickSort2.invokeAll(new ForkJoinQuickSort2(x2, y2, this.to - t3, this.to));
            }
        }
    }

    protected static final class Segment {
        protected final int offset;
        protected final int length;
        protected final int level;

        protected Segment(int offset, int length, int level) {
            this.offset = offset;
            this.length = length;
            this.level = level;
        }

        public String toString() {
            return "Segment [offset=" + this.offset + ", length=" + this.length + ", level=" + this.level + "]";
        }
    }

    private static final class ArrayHashStrategy
    implements Hash.Strategy<int[]>,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        private ArrayHashStrategy() {
        }

        @Override
        public int hashCode(int[] o2) {
            return java.util.Arrays.hashCode(o2);
        }

        @Override
        public boolean equals(int[] a2, int[] b2) {
            return java.util.Arrays.equals(a2, b2);
        }
    }
}

