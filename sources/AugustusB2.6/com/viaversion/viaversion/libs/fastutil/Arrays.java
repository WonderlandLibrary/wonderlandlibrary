// 
// Decompiled by Procyon v0.5.36
// 

package com.viaversion.viaversion.libs.fastutil;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.ForkJoinPool;
import com.viaversion.viaversion.libs.fastutil.ints.IntComparator;

public class Arrays
{
    public static final int MAX_ARRAY_SIZE = 2147483639;
    private static final int MERGESORT_NO_REC = 16;
    private static final int QUICKSORT_NO_REC = 16;
    private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
    private static final int QUICKSORT_MEDIAN_OF_9 = 128;
    
    private Arrays() {
    }
    
    public static void ensureFromTo(final int arrayLength, final int from, final int to) {
        if (from < 0) {
            throw new ArrayIndexOutOfBoundsException("Start index (" + from + ") is negative");
        }
        if (from > to) {
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        }
        if (to > arrayLength) {
            throw new ArrayIndexOutOfBoundsException("End index (" + to + ") is greater than array length (" + arrayLength + ")");
        }
    }
    
    public static void ensureOffsetLength(final int arrayLength, final int offset, final int length) {
        if (offset < 0) {
            throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
        }
        if (length < 0) {
            throw new IllegalArgumentException("Length (" + length + ") is negative");
        }
        if (offset + length > arrayLength) {
            throw new ArrayIndexOutOfBoundsException("Last index (" + (offset + length) + ") is greater than array length (" + arrayLength + ")");
        }
    }
    
    private static void inPlaceMerge(final int from, int mid, final int to, final IntComparator comp, final Swapper swapper) {
        if (from >= mid || mid >= to) {
            return;
        }
        if (to - from == 2) {
            if (comp.compare(mid, from) < 0) {
                swapper.swap(from, mid);
            }
            return;
        }
        int firstCut;
        int secondCut;
        if (mid - from > to - mid) {
            firstCut = from + (mid - from) / 2;
            secondCut = lowerBound(mid, to, firstCut, comp);
        }
        else {
            secondCut = mid + (to - mid) / 2;
            firstCut = upperBound(from, mid, secondCut, comp);
        }
        final int first2 = firstCut;
        final int middle2 = mid;
        final int last2 = secondCut;
        if (middle2 != first2 && middle2 != last2) {
            int first3 = first2;
            int last3 = middle2;
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
    
    private static int lowerBound(int from, final int to, final int pos, final IntComparator comp) {
        int len = to - from;
        while (len > 0) {
            final int half = len / 2;
            final int middle = from + half;
            if (comp.compare(middle, pos) < 0) {
                from = middle + 1;
                len -= half + 1;
            }
            else {
                len = half;
            }
        }
        return from;
    }
    
    private static int upperBound(int from, final int mid, final int pos, final IntComparator comp) {
        int len = mid - from;
        while (len > 0) {
            final int half = len / 2;
            final int middle = from + half;
            if (comp.compare(pos, middle) < 0) {
                len = half;
            }
            else {
                from = middle + 1;
                len -= half + 1;
            }
        }
        return from;
    }
    
    private static int med3(final int a, final int b, final int c, final IntComparator comp) {
        final int ab = comp.compare(a, b);
        final int ac = comp.compare(a, c);
        final int bc = comp.compare(b, c);
        return (ab < 0) ? ((bc < 0) ? b : ((ac < 0) ? c : a)) : ((bc > 0) ? b : ((ac > 0) ? c : a));
    }
    
    private static ForkJoinPool getPool() {
        final ForkJoinPool current = ForkJoinTask.getPool();
        return (current == null) ? ForkJoinPool.commonPool() : current;
    }
    
    public static void mergeSort(final int from, final int to, final IntComparator c, final Swapper swapper) {
        final int length = to - from;
        if (length < 16) {
            for (int i = from; i < to; ++i) {
                for (int j = i; j > from && c.compare(j - 1, j) > 0; --j) {
                    swapper.swap(j, j - 1);
                }
            }
            return;
        }
        final int mid = from + to >>> 1;
        mergeSort(from, mid, c, swapper);
        mergeSort(mid, to, c, swapper);
        if (c.compare(mid - 1, mid) <= 0) {
            return;
        }
        inPlaceMerge(from, mid, to, c, swapper);
    }
    
    protected static void swap(final Swapper swapper, int a, int b, final int n) {
        for (int i = 0; i < n; ++i, ++a, ++b) {
            swapper.swap(a, b);
        }
    }
    
    public static void parallelQuickSort(final int from, final int to, final IntComparator comp, final Swapper swapper) {
        final ForkJoinPool pool = getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            quickSort(from, to, comp, swapper);
        }
        else {
            pool.invoke((ForkJoinTask<Object>)new ForkJoinGenericQuickSort(from, to, comp, swapper));
        }
    }
    
    public static void quickSort(final int from, final int to, final IntComparator comp, final Swapper swapper) {
        final int len = to - from;
        if (len < 16) {
            for (int i = from; i < to; ++i) {
                for (int j = i; j > from && comp.compare(j - 1, j) > 0; --j) {
                    swapper.swap(j, j - 1);
                }
            }
            return;
        }
        int m = from + len / 2;
        int l = from;
        int n = to - 1;
        if (len > 128) {
            final int s = len / 8;
            l = med3(l, l + s, l + 2 * s, comp);
            m = med3(m - s, m, m + s, comp);
            n = med3(n - 2 * s, n - s, n, comp);
        }
        m = med3(l, m, n, comp);
        int b;
        int a = b = from;
        int d;
        int c = d = to - 1;
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
        int s2 = Math.min(a - from, b - a);
        swap(swapper, from, b - s2, s2);
        s2 = Math.min(d - c, to - d - 1);
        swap(swapper, b, to - s2, s2);
        if ((s2 = b - a) > 1) {
            quickSort(from, from + s2, comp, swapper);
        }
        if ((s2 = d - c) > 1) {
            quickSort(to - s2, to, comp, swapper);
        }
    }
    
    protected static class ForkJoinGenericQuickSort extends RecursiveAction
    {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final IntComparator comp;
        private final Swapper swapper;
        
        public ForkJoinGenericQuickSort(final int from, final int to, final IntComparator comp, final Swapper swapper) {
            this.from = from;
            this.to = to;
            this.comp = comp;
            this.swapper = swapper;
        }
        
        @Override
        protected void compute() {
            final int len = this.to - this.from;
            if (len < 8192) {
                Arrays.quickSort(this.from, this.to, this.comp, this.swapper);
                return;
            }
            int m = this.from + len / 2;
            int l = this.from;
            int n = this.to - 1;
            int s = len / 8;
            l = med3(l, l + s, l + 2 * s, this.comp);
            m = med3(m - s, m, m + s, this.comp);
            n = med3(n - 2 * s, n - s, n, this.comp);
            m = med3(l, m, n, this.comp);
            int b;
            int a = b = this.from;
            int d;
            int c = d = this.to - 1;
            while (true) {
                int comparison;
                if (b <= c && (comparison = this.comp.compare(b, m)) <= 0) {
                    if (comparison == 0) {
                        if (a == m) {
                            m = b;
                        }
                        else if (b == m) {
                            m = a;
                        }
                        this.swapper.swap(a++, b);
                    }
                    ++b;
                }
                else {
                    while (c >= b && (comparison = this.comp.compare(c, m)) >= 0) {
                        if (comparison == 0) {
                            if (c == m) {
                                m = d;
                            }
                            else if (d == m) {
                                m = c;
                            }
                            this.swapper.swap(c, d--);
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
                    this.swapper.swap(b++, c--);
                }
            }
            s = Math.min(a - this.from, b - a);
            Arrays.swap(this.swapper, this.from, b - s, s);
            s = Math.min(d - c, this.to - d - 1);
            Arrays.swap(this.swapper, b, this.to - s, s);
            s = b - a;
            final int t = d - c;
            if (s > 1 && t > 1) {
                ForkJoinTask.invokeAll(new ForkJoinGenericQuickSort(this.from, this.from + s, this.comp, this.swapper), new ForkJoinGenericQuickSort(this.to - t, this.to, this.comp, this.swapper));
            }
            else if (s > 1) {
                ForkJoinTask.invokeAll(new ForkJoinGenericQuickSort(this.from, this.from + s, this.comp, this.swapper));
            }
            else {
                ForkJoinTask.invokeAll(new ForkJoinGenericQuickSort(this.to - t, this.to, this.comp, this.swapper));
            }
        }
    }
}
