/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil;

import com.viaversion.viaversion.libs.fastutil.Swapper;
import com.viaversion.viaversion.libs.fastutil.ints.IntComparator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class Arrays {
    public static final int MAX_ARRAY_SIZE = 0x7FFFFFF7;
    private static final int MERGESORT_NO_REC = 16;
    private static final int QUICKSORT_NO_REC = 16;
    private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
    private static final int QUICKSORT_MEDIAN_OF_9 = 128;

    private Arrays() {
    }

    public static void ensureFromTo(int arrayLength, int from, int to) {
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

    public static void ensureOffsetLength(int arrayLength, int offset, int length) {
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

    private static void inPlaceMerge(int from, int mid, int to, IntComparator comp, Swapper swapper) {
        int secondCut;
        int firstCut;
        if (from >= mid || mid >= to) {
            return;
        }
        if (to - from == 2) {
            if (comp.compare(mid, from) < 0) {
                swapper.swap(from, mid);
            }
            return;
        }
        if (mid - from > to - mid) {
            firstCut = from + (mid - from) / 2;
            secondCut = Arrays.lowerBound(mid, to, firstCut, comp);
        } else {
            secondCut = mid + (to - mid) / 2;
            firstCut = Arrays.upperBound(from, mid, secondCut, comp);
        }
        int first2 = firstCut;
        int middle2 = mid;
        int last2 = secondCut;
        if (middle2 != first2 && middle2 != last2) {
            int first1 = first2;
            int last1 = middle2;
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
        Arrays.inPlaceMerge(from, firstCut, mid, comp, swapper);
        Arrays.inPlaceMerge(mid, secondCut, to, comp, swapper);
    }

    private static int lowerBound(int from, int to, int pos, IntComparator comp) {
        int len = to - from;
        while (len > 0) {
            int half = len / 2;
            int middle = from + half;
            if (comp.compare(middle, pos) < 0) {
                from = middle + 1;
                len -= half + 1;
                continue;
            }
            len = half;
        }
        return from;
    }

    private static int upperBound(int from, int mid, int pos, IntComparator comp) {
        int len = mid - from;
        while (len > 0) {
            int half = len / 2;
            int middle = from + half;
            if (comp.compare(pos, middle) < 0) {
                len = half;
                continue;
            }
            from = middle + 1;
            len -= half + 1;
        }
        return from;
    }

    private static int med3(int a2, int b2, int c2, IntComparator comp) {
        int ab = comp.compare(a2, b2);
        int ac = comp.compare(a2, c2);
        int bc = comp.compare(b2, c2);
        return ab < 0 ? (bc < 0 ? b2 : (ac < 0 ? c2 : a2)) : (bc > 0 ? b2 : (ac > 0 ? c2 : a2));
    }

    private static ForkJoinPool getPool() {
        ForkJoinPool current = ForkJoinTask.getPool();
        return current == null ? ForkJoinPool.commonPool() : current;
    }

    public static void mergeSort(int from, int to, IntComparator c2, Swapper swapper) {
        int length = to - from;
        if (length < 16) {
            for (int i2 = from; i2 < to; ++i2) {
                for (int j2 = i2; j2 > from && c2.compare(j2 - 1, j2) > 0; --j2) {
                    swapper.swap(j2, j2 - 1);
                }
            }
            return;
        }
        int mid = from + to >>> 1;
        Arrays.mergeSort(from, mid, c2, swapper);
        Arrays.mergeSort(mid, to, c2, swapper);
        if (c2.compare(mid - 1, mid) <= 0) {
            return;
        }
        Arrays.inPlaceMerge(from, mid, to, c2, swapper);
    }

    protected static void swap(Swapper swapper, int a2, int b2, int n2) {
        int i2 = 0;
        while (i2 < n2) {
            swapper.swap(a2, b2);
            ++i2;
            ++a2;
            ++b2;
        }
    }

    public static void parallelQuickSort(int from, int to, IntComparator comp, Swapper swapper) {
        ForkJoinPool pool = Arrays.getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            Arrays.quickSort(from, to, comp, swapper);
        } else {
            pool.invoke(new ForkJoinGenericQuickSort(from, to, comp, swapper));
        }
    }

    public static void quickSort(int from, int to, IntComparator comp, Swapper swapper) {
        int c2;
        int a2;
        int len = to - from;
        if (len < 16) {
            for (int i2 = from; i2 < to; ++i2) {
                for (int j2 = i2; j2 > from && comp.compare(j2 - 1, j2) > 0; --j2) {
                    swapper.swap(j2, j2 - 1);
                }
            }
            return;
        }
        int m2 = from + len / 2;
        int l2 = from;
        int n2 = to - 1;
        if (len > 128) {
            int s2 = len / 8;
            l2 = Arrays.med3(l2, l2 + s2, l2 + 2 * s2, comp);
            m2 = Arrays.med3(m2 - s2, m2, m2 + s2, comp);
            n2 = Arrays.med3(n2 - 2 * s2, n2 - s2, n2, comp);
        }
        m2 = Arrays.med3(l2, m2, n2, comp);
        int b2 = a2 = from;
        int d2 = c2 = to - 1;
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
        int s3 = Math.min(a2 - from, b2 - a2);
        Arrays.swap(swapper, from, b2 - s3, s3);
        s3 = Math.min(d2 - c2, to - d2 - 1);
        Arrays.swap(swapper, b2, to - s3, s3);
        s3 = b2 - a2;
        if (s3 > 1) {
            Arrays.quickSort(from, from + s3, comp, swapper);
        }
        if ((s3 = d2 - c2) > 1) {
            Arrays.quickSort(to - s3, to, comp, swapper);
        }
    }

    protected static class ForkJoinGenericQuickSort
    extends RecursiveAction {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final IntComparator comp;
        private final Swapper swapper;

        public ForkJoinGenericQuickSort(int from, int to, IntComparator comp, Swapper swapper) {
            this.from = from;
            this.to = to;
            this.comp = comp;
            this.swapper = swapper;
        }

        @Override
        protected void compute() {
            int c2;
            int a2;
            int len = this.to - this.from;
            if (len < 8192) {
                Arrays.quickSort(this.from, this.to, this.comp, this.swapper);
                return;
            }
            int m2 = this.from + len / 2;
            int l2 = this.from;
            int n2 = this.to - 1;
            int s2 = len / 8;
            l2 = Arrays.med3(l2, l2 + s2, l2 + 2 * s2, this.comp);
            m2 = Arrays.med3(m2 - s2, m2, m2 + s2, this.comp);
            n2 = Arrays.med3(n2 - 2 * s2, n2 - s2, n2, this.comp);
            m2 = Arrays.med3(l2, m2, n2, this.comp);
            int b2 = a2 = this.from;
            int d2 = c2 = this.to - 1;
            while (true) {
                int comparison;
                if (b2 <= c2 && (comparison = this.comp.compare(b2, m2)) <= 0) {
                    if (comparison == 0) {
                        if (a2 == m2) {
                            m2 = b2;
                        } else if (b2 == m2) {
                            m2 = a2;
                        }
                        this.swapper.swap(a2++, b2);
                    }
                    ++b2;
                    continue;
                }
                while (c2 >= b2 && (comparison = this.comp.compare(c2, m2)) >= 0) {
                    if (comparison == 0) {
                        if (c2 == m2) {
                            m2 = d2;
                        } else if (d2 == m2) {
                            m2 = c2;
                        }
                        this.swapper.swap(c2, d2--);
                    }
                    --c2;
                }
                if (b2 > c2) break;
                if (b2 == m2) {
                    m2 = d2;
                } else if (c2 == m2) {
                    m2 = c2;
                }
                this.swapper.swap(b2++, c2--);
            }
            s2 = Math.min(a2 - this.from, b2 - a2);
            Arrays.swap(this.swapper, this.from, b2 - s2, s2);
            s2 = Math.min(d2 - c2, this.to - d2 - 1);
            Arrays.swap(this.swapper, b2, this.to - s2, s2);
            s2 = b2 - a2;
            int t2 = d2 - c2;
            if (s2 > 1 && t2 > 1) {
                ForkJoinGenericQuickSort.invokeAll(new ForkJoinGenericQuickSort(this.from, this.from + s2, this.comp, this.swapper), new ForkJoinGenericQuickSort(this.to - t2, this.to, this.comp, this.swapper));
            } else if (s2 > 1) {
                ForkJoinGenericQuickSort.invokeAll(new ForkJoinGenericQuickSort(this.from, this.from + s2, this.comp, this.swapper));
            } else {
                ForkJoinGenericQuickSort.invokeAll(new ForkJoinGenericQuickSort(this.to - t2, this.to, this.comp, this.swapper));
            }
        }
    }
}

