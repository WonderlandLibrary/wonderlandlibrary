// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil.ints;

import java.io.Serializable;
import java.util.concurrent.RecursiveAction;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinPool;
import com.viaversion.viaversion.libs.fastutil.Arrays;
import com.viaversion.viaversion.libs.fastutil.Hash;

public final class IntArrays
{
    public static final int[] EMPTY_ARRAY;
    public static final int[] DEFAULT_EMPTY_ARRAY;
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
    protected static final Segment POISON_PILL;
    public static final Hash.Strategy<int[]> HASH_STRATEGY;
    
    private IntArrays() {
    }
    
    public static int[] forceCapacity(final int[] array, final int length, final int preserve) {
        final int[] t = new int[length];
        System.arraycopy(array, 0, t, 0, preserve);
        return t;
    }
    
    public static int[] ensureCapacity(final int[] array, final int length) {
        return ensureCapacity(array, length, array.length);
    }
    
    public static int[] ensureCapacity(final int[] array, final int length, final int preserve) {
        return (length > array.length) ? forceCapacity(array, length, preserve) : array;
    }
    
    public static int[] grow(final int[] array, final int length) {
        return grow(array, length, array.length);
    }
    
    public static int[] grow(final int[] array, final int length, final int preserve) {
        if (length > array.length) {
            final int newLength = (int)Math.max(Math.min(array.length + (long)(array.length >> 1), 2147483639L), length);
            final int[] t = new int[newLength];
            System.arraycopy(array, 0, t, 0, preserve);
            return t;
        }
        return array;
    }
    
    public static int[] trim(final int[] array, final int length) {
        if (length >= array.length) {
            return array;
        }
        final int[] t = (length == 0) ? IntArrays.EMPTY_ARRAY : new int[length];
        System.arraycopy(array, 0, t, 0, length);
        return t;
    }
    
    public static int[] setLength(final int[] array, final int length) {
        if (length == array.length) {
            return array;
        }
        if (length < array.length) {
            return trim(array, length);
        }
        return ensureCapacity(array, length);
    }
    
    public static int[] copy(final int[] array, final int offset, final int length) {
        ensureOffsetLength(array, offset, length);
        final int[] a = (length == 0) ? IntArrays.EMPTY_ARRAY : new int[length];
        System.arraycopy(array, offset, a, 0, length);
        return a;
    }
    
    public static int[] copy(final int[] array) {
        return array.clone();
    }
    
    @Deprecated
    public static void fill(final int[] array, final int value) {
        int i = array.length;
        while (i-- != 0) {
            array[i] = value;
        }
    }
    
    @Deprecated
    public static void fill(final int[] array, final int from, int to, final int value) {
        ensureFromTo(array, from, to);
        if (from == 0) {
            while (to-- != 0) {
                array[to] = value;
            }
        }
        else {
            for (int i = from; i < to; ++i) {
                array[i] = value;
            }
        }
    }
    
    @Deprecated
    public static boolean equals(final int[] a1, final int[] a2) {
        int i = a1.length;
        if (i != a2.length) {
            return false;
        }
        while (i-- != 0) {
            if (a1[i] != a2[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static void ensureFromTo(final int[] a, final int from, final int to) {
        Arrays.ensureFromTo(a.length, from, to);
    }
    
    public static void ensureOffsetLength(final int[] a, final int offset, final int length) {
        Arrays.ensureOffsetLength(a.length, offset, length);
    }
    
    public static void ensureSameLength(final int[] a, final int[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Array size mismatch: " + a.length + " != " + b.length);
        }
    }
    
    private static ForkJoinPool getPool() {
        final ForkJoinPool current = ForkJoinTask.getPool();
        return (current == null) ? ForkJoinPool.commonPool() : current;
    }
    
    public static void swap(final int[] x, final int a, final int b) {
        final int t = x[a];
        x[a] = x[b];
        x[b] = t;
    }
    
    public static void swap(final int[] x, int a, int b, final int n) {
        for (int i = 0; i < n; ++i, ++a, ++b) {
            swap(x, a, b);
        }
    }
    
    private static int med3(final int[] x, final int a, final int b, final int c, final IntComparator comp) {
        final int ab = comp.compare(x[a], x[b]);
        final int ac = comp.compare(x[a], x[c]);
        final int bc = comp.compare(x[b], x[c]);
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static void selectionSort(final int[] a, final int from, final int to, final IntComparator comp) {
        for (int i = from; i < to - 1; ++i) {
            int m = i;
            for (int j = i + 1; j < to; ++j) {
                if (comp.compare(a[j], a[m]) < 0) {
                    m = j;
                }
            }
            if (m != i) {
                final int u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }
    
    private static void insertionSort(final int[] a, final int from, final int to, final IntComparator comp) {
        int i = from;
        while (++i < to) {
            final int t = a[i];
            int j = i;
            for (int u = a[j - 1]; comp.compare(t, u) < 0; u = a[--j - 1]) {
                a[j] = u;
                if (from == j - 1) {
                    --j;
                    break;
                }
            }
            a[j] = t;
        }
    }
    
    public static void quickSort(final int[] x, final int from, final int to, final IntComparator comp) {
        final int len = to - from;
        if (len < 16) {
            selectionSort(x, from, to, comp);
            return;
        }
        int m = from + len / 2;
        int l = from;
        int n = to - 1;
        if (len > 128) {
            final int s = len / 8;
            l = med3(x, l, l + s, l + 2 * s, comp);
            m = med3(x, m - s, m, m + s, comp);
            n = med3(x, n - 2 * s, n - s, n, comp);
        }
        m = med3(x, l, m, n, comp);
        final int v = x[m];
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
        while (true) {
            int comparison;
            if (b <= c && (comparison = comp.compare(x[b], v)) <= 0) {
                if (comparison == 0) {
                    swap(x, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = comp.compare(x[c], v)) >= 0) {
                    if (comparison == 0) {
                        swap(x, c, d--);
                    }
                    --c;
                }
                if (b > c) {
                    break;
                }
                swap(x, b++, c--);
            }
        }
        int s2 = Math.min(a - from, b - a);
        swap(x, from, b - s2, s2);
        s2 = Math.min(d - c, to - d - 1);
        swap(x, b, to - s2, s2);
        if ((s2 = b - a) > 1) {
            quickSort(x, from, from + s2, comp);
        }
        if ((s2 = d - c) > 1) {
            quickSort(x, to - s2, to, comp);
        }
    }
    
    public static void quickSort(final int[] x, final IntComparator comp) {
        quickSort(x, 0, x.length, comp);
    }
    
    public static void parallelQuickSort(final int[] x, final int from, final int to, final IntComparator comp) {
        final ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(x, from, to, comp);
        }
        else {
            pool.invoke((ForkJoinTask<Object>)new ForkJoinQuickSortComp(x, from, to, comp));
        }
    }
    
    public static void parallelQuickSort(final int[] x, final IntComparator comp) {
        parallelQuickSort(x, 0, x.length, comp);
    }
    
    private static int med3(final int[] x, final int a, final int b, final int c) {
        final int ab = Integer.compare(x[a], x[b]);
        final int ac = Integer.compare(x[a], x[c]);
        final int bc = Integer.compare(x[b], x[c]);
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static void selectionSort(final int[] a, final int from, final int to) {
        for (int i = from; i < to - 1; ++i) {
            int m = i;
            for (int j = i + 1; j < to; ++j) {
                if (a[j] < a[m]) {
                    m = j;
                }
            }
            if (m != i) {
                final int u = a[i];
                a[i] = a[m];
                a[m] = u;
            }
        }
    }
    
    private static void insertionSort(final int[] a, final int from, final int to) {
        int i = from;
        while (++i < to) {
            final int t = a[i];
            int j = i;
            for (int u = a[j - 1]; t < u; u = a[--j - 1]) {
                a[j] = u;
                if (from == j - 1) {
                    --j;
                    break;
                }
            }
            a[j] = t;
        }
    }
    
    public static void quickSort(final int[] x, final int from, final int to) {
        final int len = to - from;
        if (len < 16) {
            selectionSort(x, from, to);
            return;
        }
        int m = from + len / 2;
        int l = from;
        int n = to - 1;
        if (len > 128) {
            final int s = len / 8;
            l = med3(x, l, l + s, l + 2 * s);
            m = med3(x, m - s, m, m + s);
            n = med3(x, n - 2 * s, n - s, n);
        }
        m = med3(x, l, m, n);
        final int v = x[m];
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
        while (true) {
            int comparison;
            if (b <= c && (comparison = Integer.compare(x[b], v)) <= 0) {
                if (comparison == 0) {
                    swap(x, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = Integer.compare(x[c], v)) >= 0) {
                    if (comparison == 0) {
                        swap(x, c, d--);
                    }
                    --c;
                }
                if (b > c) {
                    break;
                }
                swap(x, b++, c--);
            }
        }
        int s2 = Math.min(a - from, b - a);
        swap(x, from, b - s2, s2);
        s2 = Math.min(d - c, to - d - 1);
        swap(x, b, to - s2, s2);
        if ((s2 = b - a) > 1) {
            quickSort(x, from, from + s2);
        }
        if ((s2 = d - c) > 1) {
            quickSort(x, to - s2, to);
        }
    }
    
    public static void quickSort(final int[] x) {
        quickSort(x, 0, x.length);
    }
    
    public static void parallelQuickSort(final int[] x, final int from, final int to) {
        final ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(x, from, to);
        }
        else {
            pool.invoke((ForkJoinTask<Object>)new ForkJoinQuickSort(x, from, to));
        }
    }
    
    public static void parallelQuickSort(final int[] x) {
        parallelQuickSort(x, 0, x.length);
    }
    
    private static int med3Indirect(final int[] perm, final int[] x, final int a, final int b, final int c) {
        final int aa = x[perm[a]];
        final int bb = x[perm[b]];
        final int cc = x[perm[c]];
        final int ab = Integer.compare(aa, bb);
        final int ac = Integer.compare(aa, cc);
        final int bc = Integer.compare(bb, cc);
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static void insertionSortIndirect(final int[] perm, final int[] a, final int from, final int to) {
        int i = from;
        while (++i < to) {
            final int t = perm[i];
            int j = i;
            for (int u = perm[j - 1]; a[t] < a[u]; u = perm[--j - 1]) {
                perm[j] = u;
                if (from == j - 1) {
                    --j;
                    break;
                }
            }
            perm[j] = t;
        }
    }
    
    public static void quickSortIndirect(final int[] perm, final int[] x, final int from, final int to) {
        final int len = to - from;
        if (len < 16) {
            insertionSortIndirect(perm, x, from, to);
            return;
        }
        int m = from + len / 2;
        int l = from;
        int n = to - 1;
        if (len > 128) {
            final int s = len / 8;
            l = med3Indirect(perm, x, l, l + s, l + 2 * s);
            m = med3Indirect(perm, x, m - s, m, m + s);
            n = med3Indirect(perm, x, n - 2 * s, n - s, n);
        }
        m = med3Indirect(perm, x, l, m, n);
        final int v = x[perm[m]];
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
        while (true) {
            int comparison;
            if (b <= c && (comparison = Integer.compare(x[perm[b]], v)) <= 0) {
                if (comparison == 0) {
                    swap(perm, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = Integer.compare(x[perm[c]], v)) >= 0) {
                    if (comparison == 0) {
                        swap(perm, c, d--);
                    }
                    --c;
                }
                if (b > c) {
                    break;
                }
                swap(perm, b++, c--);
            }
        }
        int s2 = Math.min(a - from, b - a);
        swap(perm, from, b - s2, s2);
        s2 = Math.min(d - c, to - d - 1);
        swap(perm, b, to - s2, s2);
        if ((s2 = b - a) > 1) {
            quickSortIndirect(perm, x, from, from + s2);
        }
        if ((s2 = d - c) > 1) {
            quickSortIndirect(perm, x, to - s2, to);
        }
    }
    
    public static void quickSortIndirect(final int[] perm, final int[] x) {
        quickSortIndirect(perm, x, 0, x.length);
    }
    
    public static void parallelQuickSortIndirect(final int[] perm, final int[] x, final int from, final int to) {
        final ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSortIndirect(perm, x, from, to);
        }
        else {
            pool.invoke((ForkJoinTask<Object>)new ForkJoinQuickSortIndirect(perm, x, from, to));
        }
    }
    
    public static void parallelQuickSortIndirect(final int[] perm, final int[] x) {
        parallelQuickSortIndirect(perm, x, 0, x.length);
    }
    
    public static void stabilize(final int[] perm, final int[] x, final int from, final int to) {
        int curr = from;
        for (int i = from + 1; i < to; ++i) {
            if (x[perm[i]] != x[perm[curr]]) {
                if (i - curr > 1) {
                    parallelQuickSort(perm, curr, i);
                }
                curr = i;
            }
        }
        if (to - curr > 1) {
            parallelQuickSort(perm, curr, to);
        }
    }
    
    public static void stabilize(final int[] perm, final int[] x) {
        stabilize(perm, x, 0, perm.length);
    }
    
    private static int med3(final int[] x, final int[] y, final int a, final int b, final int c) {
        int t;
        final int ab = ((t = Integer.compare(x[a], x[b])) == 0) ? Integer.compare(y[a], y[b]) : t;
        final int ac = ((t = Integer.compare(x[a], x[c])) == 0) ? Integer.compare(y[a], y[c]) : t;
        final int bc = ((t = Integer.compare(x[b], x[c])) == 0) ? Integer.compare(y[b], y[c]) : t;
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static void swap(final int[] x, final int[] y, final int a, final int b) {
        final int t = x[a];
        final int u = y[a];
        x[a] = x[b];
        y[a] = y[b];
        x[b] = t;
        y[b] = u;
    }
    
    private static void swap(final int[] x, final int[] y, int a, int b, final int n) {
        for (int i = 0; i < n; ++i, ++a, ++b) {
            swap(x, y, a, b);
        }
    }
    
    private static void selectionSort(final int[] a, final int[] b, final int from, final int to) {
        for (int i = from; i < to - 1; ++i) {
            int m = i;
            for (int j = i + 1; j < to; ++j) {
                final int u;
                if ((u = Integer.compare(a[j], a[m])) < 0 || (u == 0 && b[j] < b[m])) {
                    m = j;
                }
            }
            if (m != i) {
                int t = a[i];
                a[i] = a[m];
                a[m] = t;
                t = b[i];
                b[i] = b[m];
                b[m] = t;
            }
        }
    }
    
    public static void quickSort(final int[] x, final int[] y, final int from, final int to) {
        final int len = to - from;
        if (len < 16) {
            selectionSort(x, y, from, to);
            return;
        }
        int m = from + len / 2;
        int l = from;
        int n = to - 1;
        if (len > 128) {
            final int s = len / 8;
            l = med3(x, y, l, l + s, l + 2 * s);
            m = med3(x, y, m - s, m, m + s);
            n = med3(x, y, n - 2 * s, n - s, n);
        }
        m = med3(x, y, l, m, n);
        final int v = x[m];
        final int w = y[m];
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
        while (true) {
            int t;
            int comparison;
            if (b <= c && (comparison = (((t = Integer.compare(x[b], v)) == 0) ? Integer.compare(y[b], w) : t)) <= 0) {
                if (comparison == 0) {
                    swap(x, y, a++, b);
                }
                ++b;
            }
            else {
                while (c >= b && (comparison = (((t = Integer.compare(x[c], v)) == 0) ? Integer.compare(y[c], w) : t)) >= 0) {
                    if (comparison == 0) {
                        swap(x, y, c, d--);
                    }
                    --c;
                }
                if (b > c) {
                    break;
                }
                swap(x, y, b++, c--);
            }
        }
        int s2 = Math.min(a - from, b - a);
        swap(x, y, from, b - s2, s2);
        s2 = Math.min(d - c, to - d - 1);
        swap(x, y, b, to - s2, s2);
        if ((s2 = b - a) > 1) {
            quickSort(x, y, from, from + s2);
        }
        if ((s2 = d - c) > 1) {
            quickSort(x, y, to - s2, to);
        }
    }
    
    public static void quickSort(final int[] x, final int[] y) {
        ensureSameLength(x, y);
        quickSort(x, y, 0, x.length);
    }
    
    public static void parallelQuickSort(final int[] x, final int[] y, final int from, final int to) {
        final ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(x, y, from, to);
        }
        else {
            pool.invoke((ForkJoinTask<Object>)new ForkJoinQuickSort2(x, y, from, to));
        }
    }
    
    public static void parallelQuickSort(final int[] x, final int[] y) {
        ensureSameLength(x, y);
        parallelQuickSort(x, y, 0, x.length);
    }
    
    public static void unstableSort(final int[] a, final int from, final int to) {
        if (to - from >= 2000) {
            radixSort(a, from, to);
        }
        else {
            quickSort(a, from, to);
        }
    }
    
    public static void unstableSort(final int[] a) {
        unstableSort(a, 0, a.length);
    }
    
    public static void unstableSort(final int[] a, final int from, final int to, final IntComparator comp) {
        quickSort(a, from, to, comp);
    }
    
    public static void unstableSort(final int[] a, final IntComparator comp) {
        unstableSort(a, 0, a.length, comp);
    }
    
    public static void mergeSort(final int[] a, final int from, final int to, int[] supp) {
        final int len = to - from;
        if (len < 16) {
            insertionSort(a, from, to);
            return;
        }
        if (supp == null) {
            supp = java.util.Arrays.copyOf(a, to);
        }
        final int mid = from + to >>> 1;
        mergeSort(supp, from, mid, a);
        mergeSort(supp, mid, to, a);
        if (supp[mid - 1] <= supp[mid]) {
            System.arraycopy(supp, from, a, from, len);
            return;
        }
        int i = from;
        int p = from;
        int q = mid;
        while (i < to) {
            if (q >= to || (p < mid && supp[p] <= supp[q])) {
                a[i] = supp[p++];
            }
            else {
                a[i] = supp[q++];
            }
            ++i;
        }
    }
    
    public static void mergeSort(final int[] a, final int from, final int to) {
        mergeSort(a, from, to, (int[])null);
    }
    
    public static void mergeSort(final int[] a) {
        mergeSort(a, 0, a.length);
    }
    
    public static void mergeSort(final int[] a, final int from, final int to, final IntComparator comp, int[] supp) {
        final int len = to - from;
        if (len < 16) {
            insertionSort(a, from, to, comp);
            return;
        }
        if (supp == null) {
            supp = java.util.Arrays.copyOf(a, to);
        }
        final int mid = from + to >>> 1;
        mergeSort(supp, from, mid, comp, a);
        mergeSort(supp, mid, to, comp, a);
        if (comp.compare(supp[mid - 1], supp[mid]) <= 0) {
            System.arraycopy(supp, from, a, from, len);
            return;
        }
        int i = from;
        int p = from;
        int q = mid;
        while (i < to) {
            if (q >= to || (p < mid && comp.compare(supp[p], supp[q]) <= 0)) {
                a[i] = supp[p++];
            }
            else {
                a[i] = supp[q++];
            }
            ++i;
        }
    }
    
    public static void mergeSort(final int[] a, final int from, final int to, final IntComparator comp) {
        mergeSort(a, from, to, comp, null);
    }
    
    public static void mergeSort(final int[] a, final IntComparator comp) {
        mergeSort(a, 0, a.length, comp);
    }
    
    public static void stableSort(final int[] a, final int from, final int to) {
        unstableSort(a, from, to);
    }
    
    public static void stableSort(final int[] a) {
        stableSort(a, 0, a.length);
    }
    
    public static void stableSort(final int[] a, final int from, final int to, final IntComparator comp) {
        mergeSort(a, from, to, comp);
    }
    
    public static void stableSort(final int[] a, final IntComparator comp) {
        stableSort(a, 0, a.length, comp);
    }
    
    public static int binarySearch(final int[] a, int from, int to, final int key) {
        --to;
        while (from <= to) {
            final int mid = from + to >>> 1;
            final int midVal = a[mid];
            if (midVal < key) {
                from = mid + 1;
            }
            else {
                if (midVal <= key) {
                    return mid;
                }
                to = mid - 1;
            }
        }
        return -(from + 1);
    }
    
    public static int binarySearch(final int[] a, final int key) {
        return binarySearch(a, 0, a.length, key);
    }
    
    public static int binarySearch(final int[] a, int from, int to, final int key, final IntComparator c) {
        --to;
        while (from <= to) {
            final int mid = from + to >>> 1;
            final int midVal = a[mid];
            final int cmp = c.compare(midVal, key);
            if (cmp < 0) {
                from = mid + 1;
            }
            else {
                if (cmp <= 0) {
                    return mid;
                }
                to = mid - 1;
            }
        }
        return -(from + 1);
    }
    
    public static int binarySearch(final int[] a, final int key, final IntComparator c) {
        return binarySearch(a, 0, a.length, key, c);
    }
    
    public static void radixSort(final int[] a) {
        radixSort(a, 0, a.length);
    }
    
    public static void radixSort(final int[] a, final int from, final int to) {
        if (to - from < 1024) {
            quickSort(a, from, to);
            return;
        }
        final int maxLevel = 3;
        final int stackSize = 766;
        int stackPos = 0;
        final int[] offsetStack = new int[766];
        final int[] lengthStack = new int[766];
        final int[] levelStack = new int[766];
        lengthStack[stackPos] = to - (offsetStack[stackPos] = from);
        levelStack[stackPos++] = 0;
        final int[] count = new int[256];
        final int[] pos = new int[256];
        while (stackPos > 0) {
            final int first = offsetStack[--stackPos];
            final int length = lengthStack[stackPos];
            final int level = levelStack[stackPos];
            final int signMask = (level % 4 == 0) ? 128 : 0;
            final int shift = (3 - level % 4) * 8;
            int i = first + length;
            while (i-- != first) {
                final int[] array = count;
                final int n = (a[i] >>> shift & 0xFF) ^ signMask;
                ++array[n];
            }
            int lastUsed = -1;
            int j = 0;
            int p = first;
            while (j < 256) {
                if (count[j] != 0) {
                    lastUsed = j;
                }
                p = (pos[j] = p + count[j]);
                ++j;
            }
            for (int end = first + length - count[lastUsed], k = first, c = -1; k <= end; k += count[c], count[c] = 0) {
                int t = a[k];
                c = ((t >>> shift & 0xFF) ^ signMask);
                if (k < end) {
                    while (true) {
                        final int[] array2 = pos;
                        final int n2 = c;
                        final int n3 = array2[n2] - 1;
                        array2[n2] = n3;
                        final int d;
                        if ((d = n3) <= k) {
                            break;
                        }
                        final int z = t;
                        t = a[d];
                        a[d] = z;
                        c = ((t >>> shift & 0xFF) ^ signMask);
                    }
                    a[k] = t;
                }
                if (level < 3 && count[c] > 1) {
                    if (count[c] < 1024) {
                        quickSort(a, k, k + count[c]);
                    }
                    else {
                        offsetStack[stackPos] = k;
                        lengthStack[stackPos] = count[c];
                        levelStack[stackPos++] = level + 1;
                    }
                }
            }
        }
    }
    
    public static void parallelRadixSort(final int[] a, final int from, final int to) {
        final ForkJoinPool pool = getPool();
        if (to - from < 1024 || pool.getParallelism() == 1) {
            quickSort(a, from, to);
            return;
        }
        final int maxLevel = 3;
        final LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<Segment>();
        queue.add(new Segment(from, to - from, 0));
        final AtomicInteger queueSize = new AtomicInteger(1);
        final int numberOfThreads = pool.getParallelism();
        final ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<Void>(pool);
        int j = numberOfThreads;
        while (j-- != 0) {
            final int[] count;
            final int[] pos;
            final AtomicInteger atomicInteger;
            final int n;
            int i;
            final Object o;
            final LinkedBlockingQueue<Segment> linkedBlockingQueue;
            Segment segment;
            int first;
            int length;
            int level;
            int signMask;
            int shift;
            int k;
            final Object o2;
            int n2;
            final Object o3;
            int lastUsed;
            int l;
            int p;
            final Object o4;
            final int n3;
            int end;
            int m;
            int c;
            int t;
            int c2;
            final Object o5;
            final int n5;
            int n4;
            int d;
            int z;
            executorCompletionService.submit(() -> {
                count = new int[256];
                pos = new int[256];
                while (true) {
                    if (atomicInteger.get() == 0) {
                        i = n;
                        while (true) {
                            i--;
                            if (o != 0) {
                                linkedBlockingQueue.add(IntArrays.POISON_PILL);
                            }
                            else {
                                break;
                            }
                        }
                    }
                    segment = linkedBlockingQueue.take();
                    if (segment == IntArrays.POISON_PILL) {
                        break;
                    }
                    else {
                        first = segment.offset;
                        length = segment.length;
                        level = segment.level;
                        signMask = ((level % 4 == 0) ? 128 : 0);
                        shift = (3 - level % 4) * 8;
                        k = first + length;
                        while (true) {
                            k--;
                            if (o2 != first) {
                                n2 = ((a[k] >>> shift & 0xFF) ^ signMask);
                                ++o3[n2];
                            }
                            else {
                                break;
                            }
                        }
                        lastUsed = -1;
                        l = 0;
                        p = first;
                        while (l < 256) {
                            if (count[l] != 0) {
                                lastUsed = l;
                            }
                            p = (o4[n3] = p + count[l]);
                            ++l;
                        }
                        for (end = first + length - count[lastUsed], m = first, c = -1; m <= end; m += count[c2], count[c2] = 0) {
                            t = a[m];
                            c2 = ((t >>> shift & 0xFF) ^ signMask);
                            if (m < end) {
                                while (true) {
                                    n4 = o5[n5] - 1;
                                    o5[n5] = n4;
                                    if ((d = n4) > m) {
                                        z = t;
                                        t = a[d];
                                        a[d] = z;
                                        c2 = ((t >>> shift & 0xFF) ^ signMask);
                                    }
                                    else {
                                        break;
                                    }
                                }
                                a[m] = t;
                            }
                            if (level < 3 && count[c2] > 1) {
                                if (count[c2] < 1024) {
                                    quickSort(a, m, m + count[c2]);
                                }
                                else {
                                    atomicInteger.incrementAndGet();
                                    linkedBlockingQueue.add(new Segment(m, count[c2], level + 1));
                                }
                            }
                        }
                        atomicInteger.decrementAndGet();
                    }
                }
                return null;
            });
        }
        Throwable problem = null;
        int i2 = numberOfThreads;
        while (i2-- != 0) {
            try {
                executorCompletionService.take().get();
            }
            catch (Exception e) {
                problem = e.getCause();
            }
        }
        if (problem != null) {
            throw (problem instanceof RuntimeException) ? problem : new RuntimeException(problem);
        }
    }
    
    public static void parallelRadixSort(final int[] a) {
        parallelRadixSort(a, 0, a.length);
    }
    
    public static void radixSortIndirect(final int[] perm, final int[] a, final boolean stable) {
        radixSortIndirect(perm, a, 0, perm.length, stable);
    }
    
    public static void radixSortIndirect(final int[] perm, final int[] a, final int from, final int to, final boolean stable) {
        if (to - from < 1024) {
            insertionSortIndirect(perm, a, from, to);
            return;
        }
        final int maxLevel = 3;
        final int stackSize = 766;
        int stackPos = 0;
        final int[] offsetStack = new int[766];
        final int[] lengthStack = new int[766];
        final int[] levelStack = new int[766];
        lengthStack[stackPos] = to - (offsetStack[stackPos] = from);
        levelStack[stackPos++] = 0;
        final int[] count = new int[256];
        final int[] pos = new int[256];
        final int[] support = (int[])(stable ? new int[perm.length] : null);
        while (stackPos > 0) {
            final int first = offsetStack[--stackPos];
            final int length = lengthStack[stackPos];
            final int level = levelStack[stackPos];
            final int signMask = (level % 4 == 0) ? 128 : 0;
            final int shift = (3 - level % 4) * 8;
            int i = first + length;
            while (i-- != first) {
                final int[] array = count;
                final int n = (a[perm[i]] >>> shift & 0xFF) ^ signMask;
                ++array[n];
            }
            int lastUsed = -1;
            int j = 0;
            int p = stable ? 0 : first;
            while (j < 256) {
                if (count[j] != 0) {
                    lastUsed = j;
                }
                p = (pos[j] = p + count[j]);
                ++j;
            }
            if (stable) {
                j = first + length;
                while (j-- != first) {
                    final int[] array2 = support;
                    final int[] array3 = pos;
                    final int n2 = (a[perm[j]] >>> shift & 0xFF) ^ signMask;
                    array2[--array3[n2]] = perm[j];
                }
                System.arraycopy(support, 0, perm, first, length);
                j = 0;
                p = first;
                while (j <= lastUsed) {
                    if (level < 3 && count[j] > 1) {
                        if (count[j] < 1024) {
                            insertionSortIndirect(perm, a, p, p + count[j]);
                        }
                        else {
                            offsetStack[stackPos] = p;
                            lengthStack[stackPos] = count[j];
                            levelStack[stackPos++] = level + 1;
                        }
                    }
                    p += count[j];
                    ++j;
                }
                java.util.Arrays.fill(count, 0);
            }
            else {
                for (int end = first + length - count[lastUsed], k = first, c = -1; k <= end; k += count[c], count[c] = 0) {
                    int t = perm[k];
                    c = ((a[t] >>> shift & 0xFF) ^ signMask);
                    if (k < end) {
                        while (true) {
                            final int[] array4 = pos;
                            final int n3 = c;
                            final int n4 = array4[n3] - 1;
                            array4[n3] = n4;
                            final int d;
                            if ((d = n4) <= k) {
                                break;
                            }
                            final int z = t;
                            t = perm[d];
                            perm[d] = z;
                            c = ((a[t] >>> shift & 0xFF) ^ signMask);
                        }
                        perm[k] = t;
                    }
                    if (level < 3 && count[c] > 1) {
                        if (count[c] < 1024) {
                            insertionSortIndirect(perm, a, k, k + count[c]);
                        }
                        else {
                            offsetStack[stackPos] = k;
                            lengthStack[stackPos] = count[c];
                            levelStack[stackPos++] = level + 1;
                        }
                    }
                }
            }
        }
    }
    
    public static void parallelRadixSortIndirect(final int[] perm, final int[] a, final int from, final int to, final boolean stable) {
        final ForkJoinPool pool = getPool();
        if (to - from < 1024 || pool.getParallelism() == 1) {
            radixSortIndirect(perm, a, from, to, stable);
            return;
        }
        final int maxLevel = 3;
        final LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<Segment>();
        queue.add(new Segment(from, to - from, 0));
        final AtomicInteger queueSize = new AtomicInteger(1);
        final int numberOfThreads = pool.getParallelism();
        final ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<Void>(pool);
        final int[] support = (int[])(stable ? new int[perm.length] : null);
        int j = numberOfThreads;
        while (j-- != 0) {
            final int[] count;
            final int[] pos;
            final AtomicInteger atomicInteger;
            final int n;
            int i;
            final Object o;
            final LinkedBlockingQueue<Segment> linkedBlockingQueue;
            Segment segment;
            int first;
            int length;
            int level;
            int signMask;
            int shift;
            int k;
            final Object o2;
            int n2;
            final Object o3;
            int lastUsed;
            int l;
            int p;
            final Object o4;
            final int n3;
            int m;
            final Object o5;
            int n4;
            final Object o6;
            int n5;
            final Object o7;
            int i2;
            int p2;
            int end;
            int i3;
            int c;
            int t;
            int c2;
            final Object o8;
            final int n7;
            int n6;
            int d;
            int z;
            executorCompletionService.submit(() -> {
                count = new int[256];
                pos = new int[256];
                while (true) {
                    if (atomicInteger.get() == 0) {
                        i = n;
                        while (true) {
                            i--;
                            if (o != 0) {
                                linkedBlockingQueue.add(IntArrays.POISON_PILL);
                            }
                            else {
                                break;
                            }
                        }
                    }
                    segment = linkedBlockingQueue.take();
                    if (segment == IntArrays.POISON_PILL) {
                        break;
                    }
                    else {
                        first = segment.offset;
                        length = segment.length;
                        level = segment.level;
                        signMask = ((level % 4 == 0) ? 128 : 0);
                        shift = (3 - level % 4) * 8;
                        k = first + length;
                        while (true) {
                            k--;
                            if (o2 != first) {
                                n2 = ((a[perm[k]] >>> shift & 0xFF) ^ signMask);
                                ++o3[n2];
                            }
                            else {
                                break;
                            }
                        }
                        lastUsed = -1;
                        l = 0;
                        p = first;
                        while (l < 256) {
                            if (count[l] != 0) {
                                lastUsed = l;
                            }
                            p = (o4[n3] = p + count[l]);
                            ++l;
                        }
                        if (stable) {
                            m = first + length;
                            while (true) {
                                m--;
                                if (o5 != first) {
                                    n4 = ((a[perm[m]] >>> shift & 0xFF) ^ signMask);
                                    n5 = o6[n4] - 1;
                                    o7[o6[n4] = n5] = perm[m];
                                }
                                else {
                                    break;
                                }
                            }
                            System.arraycopy(o7, first, perm, first, length);
                            i2 = 0;
                            p2 = first;
                            while (i2 <= lastUsed) {
                                if (level < 3 && count[i2] > 1) {
                                    if (count[i2] < 1024) {
                                        radixSortIndirect(perm, a, p2, p2 + count[i2], stable);
                                    }
                                    else {
                                        atomicInteger.incrementAndGet();
                                        linkedBlockingQueue.add(new Segment(p2, count[i2], level + 1));
                                    }
                                }
                                p2 += count[i2];
                                ++i2;
                            }
                            java.util.Arrays.fill(count, 0);
                        }
                        else {
                            for (end = first + length - count[lastUsed], i3 = first, c = -1; i3 <= end; i3 += count[c2], count[c2] = 0) {
                                t = perm[i3];
                                c2 = ((a[t] >>> shift & 0xFF) ^ signMask);
                                if (i3 < end) {
                                    while (true) {
                                        n6 = o8[n7] - 1;
                                        o8[n7] = n6;
                                        if ((d = n6) > i3) {
                                            z = t;
                                            t = perm[d];
                                            perm[d] = z;
                                            c2 = ((a[t] >>> shift & 0xFF) ^ signMask);
                                        }
                                        else {
                                            break;
                                        }
                                    }
                                    perm[i3] = t;
                                }
                                if (level < 3 && count[c2] > 1) {
                                    if (count[c2] < 1024) {
                                        radixSortIndirect(perm, a, i3, i3 + count[c2], stable);
                                    }
                                    else {
                                        atomicInteger.incrementAndGet();
                                        linkedBlockingQueue.add(new Segment(i3, count[c2], level + 1));
                                    }
                                }
                            }
                        }
                        atomicInteger.decrementAndGet();
                    }
                }
                return null;
            });
        }
        Throwable problem = null;
        int i4 = numberOfThreads;
        while (i4-- != 0) {
            try {
                executorCompletionService.take().get();
            }
            catch (Exception e) {
                problem = e.getCause();
            }
        }
        if (problem != null) {
            throw (problem instanceof RuntimeException) ? problem : new RuntimeException(problem);
        }
    }
    
    public static void parallelRadixSortIndirect(final int[] perm, final int[] a, final boolean stable) {
        parallelRadixSortIndirect(perm, a, 0, a.length, stable);
    }
    
    public static void radixSort(final int[] a, final int[] b) {
        ensureSameLength(a, b);
        radixSort(a, b, 0, a.length);
    }
    
    public static void radixSort(final int[] a, final int[] b, final int from, final int to) {
        if (to - from < 1024) {
            selectionSort(a, b, from, to);
            return;
        }
        final int layers = 2;
        final int maxLevel = 7;
        final int stackSize = 1786;
        int stackPos = 0;
        final int[] offsetStack = new int[1786];
        final int[] lengthStack = new int[1786];
        final int[] levelStack = new int[1786];
        lengthStack[stackPos] = to - (offsetStack[stackPos] = from);
        levelStack[stackPos++] = 0;
        final int[] count = new int[256];
        final int[] pos = new int[256];
        while (stackPos > 0) {
            final int first = offsetStack[--stackPos];
            final int length = lengthStack[stackPos];
            final int level = levelStack[stackPos];
            final int signMask = (level % 4 == 0) ? 128 : 0;
            final int[] k = (level < 4) ? a : b;
            final int shift = (3 - level % 4) * 8;
            int i = first + length;
            while (i-- != first) {
                final int[] array = count;
                final int n = (k[i] >>> shift & 0xFF) ^ signMask;
                ++array[n];
            }
            int lastUsed = -1;
            int j = 0;
            int p = first;
            while (j < 256) {
                if (count[j] != 0) {
                    lastUsed = j;
                }
                p = (pos[j] = p + count[j]);
                ++j;
            }
            for (int end = first + length - count[lastUsed], l = first, c = -1; l <= end; l += count[c], count[c] = 0) {
                int t = a[l];
                int u = b[l];
                c = ((k[l] >>> shift & 0xFF) ^ signMask);
                if (l < end) {
                    while (true) {
                        final int[] array2 = pos;
                        final int n2 = c;
                        final int n3 = array2[n2] - 1;
                        array2[n2] = n3;
                        final int d;
                        if ((d = n3) <= l) {
                            break;
                        }
                        c = ((k[d] >>> shift & 0xFF) ^ signMask);
                        int z = t;
                        t = a[d];
                        a[d] = z;
                        z = u;
                        u = b[d];
                        b[d] = z;
                    }
                    a[l] = t;
                    b[l] = u;
                }
                if (level < 7 && count[c] > 1) {
                    if (count[c] < 1024) {
                        selectionSort(a, b, l, l + count[c]);
                    }
                    else {
                        offsetStack[stackPos] = l;
                        lengthStack[stackPos] = count[c];
                        levelStack[stackPos++] = level + 1;
                    }
                }
            }
        }
    }
    
    public static void parallelRadixSort(final int[] a, final int[] b, final int from, final int to) {
        final ForkJoinPool pool = getPool();
        if (to - from < 1024 || pool.getParallelism() == 1) {
            quickSort(a, b, from, to);
            return;
        }
        final int layers = 2;
        if (a.length != b.length) {
            throw new IllegalArgumentException("Array size mismatch.");
        }
        final int maxLevel = 7;
        final LinkedBlockingQueue<Segment> queue = new LinkedBlockingQueue<Segment>();
        queue.add(new Segment(from, to - from, 0));
        final AtomicInteger queueSize = new AtomicInteger(1);
        final int numberOfThreads = pool.getParallelism();
        final ExecutorCompletionService<Void> executorCompletionService = new ExecutorCompletionService<Void>(pool);
        int j = numberOfThreads;
        while (j-- != 0) {
            final int[] count;
            final int[] pos;
            final AtomicInteger atomicInteger;
            final int n;
            int i;
            final Object o;
            final LinkedBlockingQueue<Segment> linkedBlockingQueue;
            Segment segment;
            int first;
            int length;
            int level;
            int signMask;
            int[] k;
            int shift;
            int l;
            final Object o2;
            int n2;
            final Object o3;
            int lastUsed;
            int m;
            int p;
            final Object o4;
            final int n3;
            int end;
            int i2;
            int c;
            int t;
            int u;
            int c2;
            final Object o5;
            final int n5;
            int n4;
            int d;
            int z;
            int w;
            executorCompletionService.submit(() -> {
                count = new int[256];
                pos = new int[256];
                while (true) {
                    if (atomicInteger.get() == 0) {
                        i = n;
                        while (true) {
                            i--;
                            if (o != 0) {
                                linkedBlockingQueue.add(IntArrays.POISON_PILL);
                            }
                            else {
                                break;
                            }
                        }
                    }
                    segment = linkedBlockingQueue.take();
                    if (segment == IntArrays.POISON_PILL) {
                        break;
                    }
                    else {
                        first = segment.offset;
                        length = segment.length;
                        level = segment.level;
                        signMask = ((level % 4 == 0) ? 128 : 0);
                        k = ((level < 4) ? a : b);
                        shift = (3 - level % 4) * 8;
                        l = first + length;
                        while (true) {
                            l--;
                            if (o2 != first) {
                                n2 = ((k[l] >>> shift & 0xFF) ^ signMask);
                                ++o3[n2];
                            }
                            else {
                                break;
                            }
                        }
                        lastUsed = -1;
                        m = 0;
                        p = first;
                        while (m < 256) {
                            if (count[m] != 0) {
                                lastUsed = m;
                            }
                            p = (o4[n3] = p + count[m]);
                            ++m;
                        }
                        for (end = first + length - count[lastUsed], i2 = first, c = -1; i2 <= end; i2 += count[c2], count[c2] = 0) {
                            t = a[i2];
                            u = b[i2];
                            c2 = ((k[i2] >>> shift & 0xFF) ^ signMask);
                            if (i2 < end) {
                                while (true) {
                                    n4 = o5[n5] - 1;
                                    o5[n5] = n4;
                                    if ((d = n4) > i2) {
                                        c2 = ((k[d] >>> shift & 0xFF) ^ signMask);
                                        z = t;
                                        w = u;
                                        t = a[d];
                                        u = b[d];
                                        a[d] = z;
                                        b[d] = w;
                                    }
                                    else {
                                        break;
                                    }
                                }
                                a[i2] = t;
                                b[i2] = u;
                            }
                            if (level < 7 && count[c2] > 1) {
                                if (count[c2] < 1024) {
                                    quickSort(a, b, i2, i2 + count[c2]);
                                }
                                else {
                                    atomicInteger.incrementAndGet();
                                    linkedBlockingQueue.add(new Segment(i2, count[c2], level + 1));
                                }
                            }
                        }
                        atomicInteger.decrementAndGet();
                    }
                }
                return null;
            });
        }
        Throwable problem = null;
        int i3 = numberOfThreads;
        while (i3-- != 0) {
            try {
                executorCompletionService.take().get();
            }
            catch (Exception e) {
                problem = e.getCause();
            }
        }
        if (problem != null) {
            throw (problem instanceof RuntimeException) ? problem : new RuntimeException(problem);
        }
    }
    
    public static void parallelRadixSort(final int[] a, final int[] b) {
        ensureSameLength(a, b);
        parallelRadixSort(a, b, 0, a.length);
    }
    
    private static void insertionSortIndirect(final int[] perm, final int[] a, final int[] b, final int from, final int to) {
        int i = from;
        while (++i < to) {
            final int t = perm[i];
            int j = i;
            for (int u = perm[j - 1]; a[t] < a[u] || (a[t] == a[u] && b[t] < b[u]); u = perm[--j - 1]) {
                perm[j] = u;
                if (from == j - 1) {
                    --j;
                    break;
                }
            }
            perm[j] = t;
        }
    }
    
    public static void radixSortIndirect(final int[] perm, final int[] a, final int[] b, final boolean stable) {
        ensureSameLength(a, b);
        radixSortIndirect(perm, a, b, 0, a.length, stable);
    }
    
    public static void radixSortIndirect(final int[] perm, final int[] a, final int[] b, final int from, final int to, final boolean stable) {
        if (to - from < 1024) {
            insertionSortIndirect(perm, a, b, from, to);
            return;
        }
        final int layers = 2;
        final int maxLevel = 7;
        final int stackSize = 1786;
        int stackPos = 0;
        final int[] offsetStack = new int[1786];
        final int[] lengthStack = new int[1786];
        final int[] levelStack = new int[1786];
        lengthStack[stackPos] = to - (offsetStack[stackPos] = from);
        levelStack[stackPos++] = 0;
        final int[] count = new int[256];
        final int[] pos = new int[256];
        final int[] support = (int[])(stable ? new int[perm.length] : null);
        while (stackPos > 0) {
            final int first = offsetStack[--stackPos];
            final int length = lengthStack[stackPos];
            final int level = levelStack[stackPos];
            final int signMask = (level % 4 == 0) ? 128 : 0;
            final int[] k = (level < 4) ? a : b;
            final int shift = (3 - level % 4) * 8;
            int i = first + length;
            while (i-- != first) {
                final int[] array = count;
                final int n = (k[perm[i]] >>> shift & 0xFF) ^ signMask;
                ++array[n];
            }
            int lastUsed = -1;
            int j = 0;
            int p = stable ? 0 : first;
            while (j < 256) {
                if (count[j] != 0) {
                    lastUsed = j;
                }
                p = (pos[j] = p + count[j]);
                ++j;
            }
            if (stable) {
                j = first + length;
                while (j-- != first) {
                    final int[] array2 = support;
                    final int[] array3 = pos;
                    final int n2 = (k[perm[j]] >>> shift & 0xFF) ^ signMask;
                    array2[--array3[n2]] = perm[j];
                }
                System.arraycopy(support, 0, perm, first, length);
                j = 0;
                p = first;
                while (j < 256) {
                    if (level < 7 && count[j] > 1) {
                        if (count[j] < 1024) {
                            insertionSortIndirect(perm, a, b, p, p + count[j]);
                        }
                        else {
                            offsetStack[stackPos] = p;
                            lengthStack[stackPos] = count[j];
                            levelStack[stackPos++] = level + 1;
                        }
                    }
                    p += count[j];
                    ++j;
                }
                java.util.Arrays.fill(count, 0);
            }
            else {
                for (int end = first + length - count[lastUsed], l = first, c = -1; l <= end; l += count[c], count[c] = 0) {
                    int t = perm[l];
                    c = ((k[t] >>> shift & 0xFF) ^ signMask);
                    if (l < end) {
                        while (true) {
                            final int[] array4 = pos;
                            final int n3 = c;
                            final int n4 = array4[n3] - 1;
                            array4[n3] = n4;
                            final int d;
                            if ((d = n4) <= l) {
                                break;
                            }
                            final int z = t;
                            t = perm[d];
                            perm[d] = z;
                            c = ((k[t] >>> shift & 0xFF) ^ signMask);
                        }
                        perm[l] = t;
                    }
                    if (level < 7 && count[c] > 1) {
                        if (count[c] < 1024) {
                            insertionSortIndirect(perm, a, b, l, l + count[c]);
                        }
                        else {
                            offsetStack[stackPos] = l;
                            lengthStack[stackPos] = count[c];
                            levelStack[stackPos++] = level + 1;
                        }
                    }
                }
            }
        }
    }
    
    private static void selectionSort(final int[][] a, final int from, final int to, final int level) {
        final int layers = a.length;
        final int firstLayer = level / 4;
        for (int i = from; i < to - 1; ++i) {
            int m = i;
            for (int j = i + 1; j < to; ++j) {
                for (int p = firstLayer; p < layers; ++p) {
                    if (a[p][j] < a[p][m]) {
                        m = j;
                        break;
                    }
                    if (a[p][j] > a[p][m]) {
                        break;
                    }
                }
            }
            if (m != i) {
                int p2 = layers;
                while (p2-- != 0) {
                    final int u = a[p2][i];
                    a[p2][i] = a[p2][m];
                    a[p2][m] = u;
                }
            }
        }
    }
    
    public static void radixSort(final int[][] a) {
        radixSort(a, 0, a[0].length);
    }
    
    public static void radixSort(final int[][] a, final int from, final int to) {
        if (to - from < 1024) {
            selectionSort(a, from, to, 0);
            return;
        }
        final int layers = a.length;
        final int maxLevel = 4 * layers - 1;
        int p = layers;
        final int l = a[0].length;
        while (p-- != 0) {
            if (a[p].length != l) {
                throw new IllegalArgumentException("The array of index " + p + " has not the same length of the array of index 0.");
            }
        }
        final int stackSize = 255 * (layers * 4 - 1) + 1;
        int stackPos = 0;
        final int[] offsetStack = new int[stackSize];
        final int[] lengthStack = new int[stackSize];
        final int[] levelStack = new int[stackSize];
        lengthStack[stackPos] = to - (offsetStack[stackPos] = from);
        levelStack[stackPos++] = 0;
        final int[] count = new int[256];
        final int[] pos = new int[256];
        final int[] t = new int[layers];
        while (stackPos > 0) {
            final int first = offsetStack[--stackPos];
            final int length = lengthStack[stackPos];
            final int level = levelStack[stackPos];
            final int signMask = (level % 4 == 0) ? 128 : 0;
            final int[] k = a[level / 4];
            final int shift = (3 - level % 4) * 8;
            int i = first + length;
            while (i-- != first) {
                final int[] array = count;
                final int n = (k[i] >>> shift & 0xFF) ^ signMask;
                ++array[n];
            }
            int lastUsed = -1;
            int j = 0;
            int p2 = first;
            while (j < 256) {
                if (count[j] != 0) {
                    lastUsed = j;
                }
                p2 = (pos[j] = p2 + count[j]);
                ++j;
            }
            for (int end = first + length - count[lastUsed], m = first, c = -1; m <= end; m += count[c], count[c] = 0) {
                int p3 = layers;
                while (p3-- != 0) {
                    t[p3] = a[p3][m];
                }
                c = ((k[m] >>> shift & 0xFF) ^ signMask);
                if (m < end) {
                    while (true) {
                        final int[] array2 = pos;
                        final int n2 = c;
                        final int n3 = array2[n2] - 1;
                        array2[n2] = n3;
                        final int d;
                        if ((d = n3) <= m) {
                            break;
                        }
                        c = ((k[d] >>> shift & 0xFF) ^ signMask);
                        p3 = layers;
                        while (p3-- != 0) {
                            final int u = t[p3];
                            t[p3] = a[p3][d];
                            a[p3][d] = u;
                        }
                    }
                    p3 = layers;
                    while (p3-- != 0) {
                        a[p3][m] = t[p3];
                    }
                }
                if (level < maxLevel && count[c] > 1) {
                    if (count[c] < 1024) {
                        selectionSort(a, m, m + count[c], level + 1);
                    }
                    else {
                        offsetStack[stackPos] = m;
                        lengthStack[stackPos] = count[c];
                        levelStack[stackPos++] = level + 1;
                    }
                }
            }
        }
    }
    
    public static int[] shuffle(final int[] a, final int from, final int to, final Random random) {
        int i = to - from;
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final int t = a[from + i];
            a[from + i] = a[from + p];
            a[from + p] = t;
        }
        return a;
    }
    
    public static int[] shuffle(final int[] a, final Random random) {
        int i = a.length;
        while (i-- != 0) {
            final int p = random.nextInt(i + 1);
            final int t = a[i];
            a[i] = a[p];
            a[p] = t;
        }
        return a;
    }
    
    public static int[] reverse(final int[] a) {
        final int length = a.length;
        int i = length / 2;
        while (i-- != 0) {
            final int t = a[length - i - 1];
            a[length - i - 1] = a[i];
            a[i] = t;
        }
        return a;
    }
    
    public static int[] reverse(final int[] a, final int from, final int to) {
        final int length = to - from;
        int i = length / 2;
        while (i-- != 0) {
            final int t = a[from + length - i - 1];
            a[from + length - i - 1] = a[from + i];
            a[from + i] = t;
        }
        return a;
    }
    
    static {
        EMPTY_ARRAY = new int[0];
        DEFAULT_EMPTY_ARRAY = new int[0];
        POISON_PILL = new Segment(-1, -1, -1);
        HASH_STRATEGY = new ArrayHashStrategy();
    }
    
    protected static class ForkJoinQuickSortComp extends RecursiveAction
    {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final int[] x;
        private final IntComparator comp;
        
        public ForkJoinQuickSortComp(final int[] x, final int from, final int to, final IntComparator comp) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.comp = comp;
        }
        
        @Override
        protected void compute() {
            final int[] x = this.x;
            final int len = this.to - this.from;
            if (len < 8192) {
                IntArrays.quickSort(x, this.from, this.to, this.comp);
                return;
            }
            int m = this.from + len / 2;
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            l = med3(x, l, l + s, l + 2 * s, this.comp);
            m = med3(x, m - s, m, m + s, this.comp);
            n = med3(x, n - 2 * s, n - s, n, this.comp);
            m = med3(x, l, m, n, this.comp);
            final int v = x[m];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int comparison;
                if (b <= c && (comparison = this.comp.compare(x[b], v)) <= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(x, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = this.comp.compare(x[c], v)) >= 0) {
                        if (comparison == 0) {
                            IntArrays.swap(x, c, d--);
                        }
                        --c;
                    }
                    if (b > c) {
                        break;
                    }
                    IntArrays.swap(x, b++, c--);
                }
            }
            s = Math.min(a - this.from, b - a);
            IntArrays.swap(x, this.from, b - s, s);
            s = Math.min(d - c, this.to - d - 1);
            IntArrays.swap(x, b, this.to - s, s);
            s = b - a;
            final int t = d - c;
            if (s > 1 && t > 1) {
                ForkJoinTask.invokeAll(new ForkJoinQuickSortComp(x, this.from, this.from + s, this.comp), new ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp));
            }
            else if (s > 1) {
                ForkJoinTask.invokeAll(new ForkJoinQuickSortComp(x, this.from, this.from + s, this.comp));
            }
            else {
                ForkJoinTask.invokeAll(new ForkJoinQuickSortComp(x, this.to - t, this.to, this.comp));
            }
        }
    }
    
    protected static class ForkJoinQuickSort extends RecursiveAction
    {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final int[] x;
        
        public ForkJoinQuickSort(final int[] x, final int from, final int to) {
            this.from = from;
            this.to = to;
            this.x = x;
        }
        
        @Override
        protected void compute() {
            final int[] x = this.x;
            final int len = this.to - this.from;
            if (len < 8192) {
                IntArrays.quickSort(x, this.from, this.to);
                return;
            }
            int m = this.from + len / 2;
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            l = med3(x, l, l + s, l + 2 * s);
            m = med3(x, m - s, m, m + s);
            n = med3(x, n - 2 * s, n - s, n);
            m = med3(x, l, m, n);
            final int v = x[m];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int comparison;
                if (b <= c && (comparison = Integer.compare(x[b], v)) <= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(x, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = Integer.compare(x[c], v)) >= 0) {
                        if (comparison == 0) {
                            IntArrays.swap(x, c, d--);
                        }
                        --c;
                    }
                    if (b > c) {
                        break;
                    }
                    IntArrays.swap(x, b++, c--);
                }
            }
            s = Math.min(a - this.from, b - a);
            IntArrays.swap(x, this.from, b - s, s);
            s = Math.min(d - c, this.to - d - 1);
            IntArrays.swap(x, b, this.to - s, s);
            s = b - a;
            final int t = d - c;
            if (s > 1 && t > 1) {
                ForkJoinTask.invokeAll(new ForkJoinQuickSort(x, this.from, this.from + s), new ForkJoinQuickSort(x, this.to - t, this.to));
            }
            else if (s > 1) {
                ForkJoinTask.invokeAll(new ForkJoinQuickSort(x, this.from, this.from + s));
            }
            else {
                ForkJoinTask.invokeAll(new ForkJoinQuickSort(x, this.to - t, this.to));
            }
        }
    }
    
    protected static class ForkJoinQuickSortIndirect extends RecursiveAction
    {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final int[] perm;
        private final int[] x;
        
        public ForkJoinQuickSortIndirect(final int[] perm, final int[] x, final int from, final int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.perm = perm;
        }
        
        @Override
        protected void compute() {
            final int[] x = this.x;
            final int len = this.to - this.from;
            if (len < 8192) {
                IntArrays.quickSortIndirect(this.perm, x, this.from, this.to);
                return;
            }
            int m = this.from + len / 2;
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            l = med3Indirect(this.perm, x, l, l + s, l + 2 * s);
            m = med3Indirect(this.perm, x, m - s, m, m + s);
            n = med3Indirect(this.perm, x, n - 2 * s, n - s, n);
            m = med3Indirect(this.perm, x, l, m, n);
            final int v = x[this.perm[m]];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int comparison;
                if (b <= c && (comparison = Integer.compare(x[this.perm[b]], v)) <= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(this.perm, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = Integer.compare(x[this.perm[c]], v)) >= 0) {
                        if (comparison == 0) {
                            IntArrays.swap(this.perm, c, d--);
                        }
                        --c;
                    }
                    if (b > c) {
                        break;
                    }
                    IntArrays.swap(this.perm, b++, c--);
                }
            }
            s = Math.min(a - this.from, b - a);
            IntArrays.swap(this.perm, this.from, b - s, s);
            s = Math.min(d - c, this.to - d - 1);
            IntArrays.swap(this.perm, b, this.to - s, s);
            s = b - a;
            final int t = d - c;
            if (s > 1 && t > 1) {
                ForkJoinTask.invokeAll(new ForkJoinQuickSortIndirect(this.perm, x, this.from, this.from + s), new ForkJoinQuickSortIndirect(this.perm, x, this.to - t, this.to));
            }
            else if (s > 1) {
                ForkJoinTask.invokeAll(new ForkJoinQuickSortIndirect(this.perm, x, this.from, this.from + s));
            }
            else {
                ForkJoinTask.invokeAll(new ForkJoinQuickSortIndirect(this.perm, x, this.to - t, this.to));
            }
        }
    }
    
    protected static class ForkJoinQuickSort2 extends RecursiveAction
    {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final int[] x;
        private final int[] y;
        
        public ForkJoinQuickSort2(final int[] x, final int[] y, final int from, final int to) {
            this.from = from;
            this.to = to;
            this.x = x;
            this.y = y;
        }
        
        @Override
        protected void compute() {
            final int[] x = this.x;
            final int[] y = this.y;
            final int len = this.to - this.from;
            if (len < 8192) {
                IntArrays.quickSort(x, y, this.from, this.to);
                return;
            }
            int m = this.from + len / 2;
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            l = med3(x, y, l, l + s, l + 2 * s);
            m = med3(x, y, m - s, m, m + s);
            n = med3(x, y, n - 2 * s, n - s, n);
            m = med3(x, y, l, m, n);
            final int v = x[m];
            final int w = y[m];
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int t;
                int comparison;
                if (b <= c && (comparison = (((t = Integer.compare(x[b], v)) == 0) ? Integer.compare(y[b], w) : t)) <= 0) {
                    if (comparison == 0) {
                        swap(x, y, a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = (((t = Integer.compare(x[c], v)) == 0) ? Integer.compare(y[c], w) : t)) >= 0) {
                        if (comparison == 0) {
                            swap(x, y, c, d--);
                        }
                        --c;
                    }
                    if (b > c) {
                        break;
                    }
                    swap(x, y, b++, c--);
                }
            }
            s = Math.min(a - this.from, b - a);
            swap(x, y, this.from, b - s, s);
            s = Math.min(d - c, this.to - d - 1);
            swap(x, y, b, this.to - s, s);
            s = b - a;
            final int t2 = d - c;
            if (s > 1 && t2 > 1) {
                ForkJoinTask.invokeAll(new ForkJoinQuickSort2(x, y, this.from, this.from + s), new ForkJoinQuickSort2(x, y, this.to - t2, this.to));
            }
            else if (s > 1) {
                ForkJoinTask.invokeAll(new ForkJoinQuickSort2(x, y, this.from, this.from + s));
            }
            else {
                ForkJoinTask.invokeAll(new ForkJoinQuickSort2(x, y, this.to - t2, this.to));
            }
        }
    }
    
    protected static final class Segment
    {
        protected final int offset;
        protected final int length;
        protected final int level;
        
        protected Segment(final int offset, final int length, final int level) {
            this.offset = offset;
            this.length = length;
            this.level = level;
        }
        
        @Override
        public String toString() {
            return "Segment [offset=" + this.offset + ", length=" + this.length + ", level=" + this.level + "]";
        }
    }
    
    private static final class ArrayHashStrategy implements Hash.Strategy<int[]>, Serializable
    {
        private static final long serialVersionUID = -7046029254386353129L;
        
        @Override
        public int hashCode(final int[] o) {
            return java.util.Arrays.hashCode(o);
        }
        
        @Override
        public boolean equals(final int[] a, final int[] b) {
            return java.util.Arrays.equals(a, b);
        }
    }
}
