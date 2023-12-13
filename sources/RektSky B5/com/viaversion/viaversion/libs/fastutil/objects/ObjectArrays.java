/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.fastutil.objects;

import com.viaversion.viaversion.libs.fastutil.Arrays;
import com.viaversion.viaversion.libs.fastutil.Hash;
import com.viaversion.viaversion.libs.fastutil.ints.IntArrays;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public final class ObjectArrays {
    public static final Object[] EMPTY_ARRAY = new Object[0];
    public static final Object[] DEFAULT_EMPTY_ARRAY = new Object[0];
    private static final int QUICKSORT_NO_REC = 16;
    private static final int PARALLEL_QUICKSORT_NO_FORK = 8192;
    private static final int QUICKSORT_MEDIAN_OF_9 = 128;
    private static final int MERGESORT_NO_REC = 16;
    public static final Hash.Strategy HASH_STRATEGY = new ArrayHashStrategy();

    private ObjectArrays() {
    }

    private static <K> K[] newArray(K[] prototype, int length) {
        Class<?> klass = prototype.getClass();
        if (klass == Object[].class) {
            return length == 0 ? EMPTY_ARRAY : new Object[length];
        }
        return (Object[])Array.newInstance(klass.getComponentType(), length);
    }

    public static <K> K[] forceCapacity(K[] array, int length, int preserve) {
        K[] t2 = ObjectArrays.newArray(array, length);
        System.arraycopy(array, 0, t2, 0, preserve);
        return t2;
    }

    public static <K> K[] ensureCapacity(K[] array, int length) {
        return ObjectArrays.ensureCapacity(array, length, array.length);
    }

    public static <K> K[] ensureCapacity(K[] array, int length, int preserve) {
        return length > array.length ? ObjectArrays.forceCapacity(array, length, preserve) : array;
    }

    public static <K> K[] grow(K[] array, int length) {
        return ObjectArrays.grow(array, length, array.length);
    }

    public static <K> K[] grow(K[] array, int length, int preserve) {
        if (length > array.length) {
            int newLength = (int)Math.max(Math.min((long)array.length + (long)(array.length >> 1), 0x7FFFFFF7L), (long)length);
            K[] t2 = ObjectArrays.newArray(array, newLength);
            System.arraycopy(array, 0, t2, 0, preserve);
            return t2;
        }
        return array;
    }

    public static <K> K[] trim(K[] array, int length) {
        if (length >= array.length) {
            return array;
        }
        K[] t2 = ObjectArrays.newArray(array, length);
        System.arraycopy(array, 0, t2, 0, length);
        return t2;
    }

    public static <K> K[] setLength(K[] array, int length) {
        if (length == array.length) {
            return array;
        }
        if (length < array.length) {
            return ObjectArrays.trim(array, length);
        }
        return ObjectArrays.ensureCapacity(array, length);
    }

    public static <K> K[] copy(K[] array, int offset, int length) {
        ObjectArrays.ensureOffsetLength(array, offset, length);
        K[] a2 = ObjectArrays.newArray(array, length);
        System.arraycopy(array, offset, a2, 0, length);
        return a2;
    }

    public static <K> K[] copy(K[] array) {
        return (Object[])array.clone();
    }

    @Deprecated
    public static <K> void fill(K[] array, K value) {
        int i2 = array.length;
        while (i2-- != 0) {
            array[i2] = value;
        }
    }

    @Deprecated
    public static <K> void fill(K[] array, int from, int to, K value) {
        ObjectArrays.ensureFromTo(array, from, to);
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
    public static <K> boolean equals(K[] a1, K[] a2) {
        int i2 = a1.length;
        if (i2 != a2.length) {
            return false;
        }
        while (i2-- != 0) {
            if (Objects.equals(a1[i2], a2[i2])) continue;
            return false;
        }
        return true;
    }

    public static <K> void ensureFromTo(K[] a2, int from, int to) {
        Arrays.ensureFromTo(a2.length, from, to);
    }

    public static <K> void ensureOffsetLength(K[] a2, int offset, int length) {
        Arrays.ensureOffsetLength(a2.length, offset, length);
    }

    public static <K> void ensureSameLength(K[] a2, K[] b2) {
        if (a2.length != b2.length) {
            throw new IllegalArgumentException("Array size mismatch: " + a2.length + " != " + b2.length);
        }
    }

    private static ForkJoinPool getPool() {
        ForkJoinPool current = ForkJoinTask.getPool();
        return current == null ? ForkJoinPool.commonPool() : current;
    }

    public static <K> void swap(K[] x2, int a2, int b2) {
        K t2 = x2[a2];
        x2[a2] = x2[b2];
        x2[b2] = t2;
    }

    public static <K> void swap(K[] x2, int a2, int b2, int n2) {
        int i2 = 0;
        while (i2 < n2) {
            ObjectArrays.swap(x2, a2, b2);
            ++i2;
            ++a2;
            ++b2;
        }
    }

    private static <K> int med3(K[] x2, int a2, int b2, int c2, Comparator<K> comp) {
        int ab = comp.compare(x2[a2], x2[b2]);
        int ac = comp.compare(x2[a2], x2[c2]);
        int bc = comp.compare(x2[b2], x2[c2]);
        return ab < 0 ? (bc < 0 ? b2 : (ac < 0 ? c2 : a2)) : (bc > 0 ? b2 : (ac > 0 ? c2 : a2));
    }

    private static <K> void selectionSort(K[] a2, int from, int to, Comparator<K> comp) {
        for (int i2 = from; i2 < to - 1; ++i2) {
            int m2 = i2;
            for (int j2 = i2 + 1; j2 < to; ++j2) {
                if (comp.compare(a2[j2], a2[m2]) >= 0) continue;
                m2 = j2;
            }
            if (m2 == i2) continue;
            K u2 = a2[i2];
            a2[i2] = a2[m2];
            a2[m2] = u2;
        }
    }

    private static <K> void insertionSort(K[] a2, int from, int to, Comparator<K> comp) {
        int i2 = from;
        while (++i2 < to) {
            K t2 = a2[i2];
            int j2 = i2;
            K u2 = a2[j2 - 1];
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

    public static <K> void quickSort(K[] x2, int from, int to, Comparator<K> comp) {
        int c2;
        int a2;
        int len = to - from;
        if (len < 16) {
            ObjectArrays.selectionSort(x2, from, to, comp);
            return;
        }
        int m2 = from + len / 2;
        int l2 = from;
        int n2 = to - 1;
        if (len > 128) {
            int s2 = len / 8;
            l2 = ObjectArrays.med3(x2, l2, l2 + s2, l2 + 2 * s2, comp);
            m2 = ObjectArrays.med3(x2, m2 - s2, m2, m2 + s2, comp);
            n2 = ObjectArrays.med3(x2, n2 - 2 * s2, n2 - s2, n2, comp);
        }
        m2 = ObjectArrays.med3(x2, l2, m2, n2, comp);
        K v2 = x2[m2];
        int b2 = a2 = from;
        int d2 = c2 = to - 1;
        while (true) {
            int comparison;
            if (b2 <= c2 && (comparison = comp.compare(x2[b2], v2)) <= 0) {
                if (comparison == 0) {
                    ObjectArrays.swap(x2, a2++, b2);
                }
                ++b2;
                continue;
            }
            while (c2 >= b2 && (comparison = comp.compare(x2[c2], v2)) >= 0) {
                if (comparison == 0) {
                    ObjectArrays.swap(x2, c2, d2--);
                }
                --c2;
            }
            if (b2 > c2) break;
            ObjectArrays.swap(x2, b2++, c2--);
        }
        int s3 = Math.min(a2 - from, b2 - a2);
        ObjectArrays.swap(x2, from, b2 - s3, s3);
        s3 = Math.min(d2 - c2, to - d2 - 1);
        ObjectArrays.swap(x2, b2, to - s3, s3);
        s3 = b2 - a2;
        if (s3 > 1) {
            ObjectArrays.quickSort(x2, from, from + s3, comp);
        }
        if ((s3 = d2 - c2) > 1) {
            ObjectArrays.quickSort(x2, to - s3, to, comp);
        }
    }

    public static <K> void quickSort(K[] x2, Comparator<K> comp) {
        ObjectArrays.quickSort(x2, 0, x2.length, comp);
    }

    public static <K> void parallelQuickSort(K[] x2, int from, int to, Comparator<K> comp) {
        ForkJoinPool pool = ObjectArrays.getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            ObjectArrays.quickSort(x2, from, to, comp);
        } else {
            pool.invoke(new ForkJoinQuickSortComp<K>(x2, from, to, comp));
        }
    }

    public static <K> void parallelQuickSort(K[] x2, Comparator<K> comp) {
        ObjectArrays.parallelQuickSort(x2, 0, x2.length, comp);
    }

    private static <K> int med3(K[] x2, int a2, int b2, int c2) {
        int ab = ((Comparable)x2[a2]).compareTo(x2[b2]);
        int ac = ((Comparable)x2[a2]).compareTo(x2[c2]);
        int bc = ((Comparable)x2[b2]).compareTo(x2[c2]);
        return ab < 0 ? (bc < 0 ? b2 : (ac < 0 ? c2 : a2)) : (bc > 0 ? b2 : (ac > 0 ? c2 : a2));
    }

    private static <K> void selectionSort(K[] a2, int from, int to) {
        for (int i2 = from; i2 < to - 1; ++i2) {
            int m2 = i2;
            for (int j2 = i2 + 1; j2 < to; ++j2) {
                if (((Comparable)a2[j2]).compareTo(a2[m2]) >= 0) continue;
                m2 = j2;
            }
            if (m2 == i2) continue;
            K u2 = a2[i2];
            a2[i2] = a2[m2];
            a2[m2] = u2;
        }
    }

    private static <K> void insertionSort(K[] a2, int from, int to) {
        int i2 = from;
        while (++i2 < to) {
            K t2 = a2[i2];
            int j2 = i2;
            K u2 = a2[j2 - 1];
            while (((Comparable)t2).compareTo(u2) < 0) {
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

    public static <K> void quickSort(K[] x2, int from, int to) {
        int c2;
        int a2;
        int len = to - from;
        if (len < 16) {
            ObjectArrays.selectionSort(x2, from, to);
            return;
        }
        int m2 = from + len / 2;
        int l2 = from;
        int n2 = to - 1;
        if (len > 128) {
            int s2 = len / 8;
            l2 = ObjectArrays.med3(x2, l2, l2 + s2, l2 + 2 * s2);
            m2 = ObjectArrays.med3(x2, m2 - s2, m2, m2 + s2);
            n2 = ObjectArrays.med3(x2, n2 - 2 * s2, n2 - s2, n2);
        }
        m2 = ObjectArrays.med3(x2, l2, m2, n2);
        K v2 = x2[m2];
        int b2 = a2 = from;
        int d2 = c2 = to - 1;
        while (true) {
            int comparison;
            if (b2 <= c2 && (comparison = ((Comparable)x2[b2]).compareTo(v2)) <= 0) {
                if (comparison == 0) {
                    ObjectArrays.swap(x2, a2++, b2);
                }
                ++b2;
                continue;
            }
            while (c2 >= b2 && (comparison = ((Comparable)x2[c2]).compareTo(v2)) >= 0) {
                if (comparison == 0) {
                    ObjectArrays.swap(x2, c2, d2--);
                }
                --c2;
            }
            if (b2 > c2) break;
            ObjectArrays.swap(x2, b2++, c2--);
        }
        int s3 = Math.min(a2 - from, b2 - a2);
        ObjectArrays.swap(x2, from, b2 - s3, s3);
        s3 = Math.min(d2 - c2, to - d2 - 1);
        ObjectArrays.swap(x2, b2, to - s3, s3);
        s3 = b2 - a2;
        if (s3 > 1) {
            ObjectArrays.quickSort(x2, from, from + s3);
        }
        if ((s3 = d2 - c2) > 1) {
            ObjectArrays.quickSort(x2, to - s3, to);
        }
    }

    public static <K> void quickSort(K[] x2) {
        ObjectArrays.quickSort(x2, 0, x2.length);
    }

    public static <K> void parallelQuickSort(K[] x2, int from, int to) {
        ForkJoinPool pool = ObjectArrays.getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            ObjectArrays.quickSort(x2, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSort<K>(x2, from, to));
        }
    }

    public static <K> void parallelQuickSort(K[] x2) {
        ObjectArrays.parallelQuickSort(x2, 0, x2.length);
    }

    private static <K> int med3Indirect(int[] perm, K[] x2, int a2, int b2, int c2) {
        K aa = x2[perm[a2]];
        K bb = x2[perm[b2]];
        K cc = x2[perm[c2]];
        int ab = ((Comparable)aa).compareTo(bb);
        int ac = ((Comparable)aa).compareTo(cc);
        int bc = ((Comparable)bb).compareTo(cc);
        return ab < 0 ? (bc < 0 ? b2 : (ac < 0 ? c2 : a2)) : (bc > 0 ? b2 : (ac > 0 ? c2 : a2));
    }

    private static <K> void insertionSortIndirect(int[] perm, K[] a2, int from, int to) {
        int i2 = from;
        while (++i2 < to) {
            int t2 = perm[i2];
            int j2 = i2;
            int u2 = perm[j2 - 1];
            while (((Comparable)a2[t2]).compareTo(a2[u2]) < 0) {
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

    public static <K> void quickSortIndirect(int[] perm, K[] x2, int from, int to) {
        int c2;
        int a2;
        int len = to - from;
        if (len < 16) {
            ObjectArrays.insertionSortIndirect(perm, x2, from, to);
            return;
        }
        int m2 = from + len / 2;
        int l2 = from;
        int n2 = to - 1;
        if (len > 128) {
            int s2 = len / 8;
            l2 = ObjectArrays.med3Indirect(perm, x2, l2, l2 + s2, l2 + 2 * s2);
            m2 = ObjectArrays.med3Indirect(perm, x2, m2 - s2, m2, m2 + s2);
            n2 = ObjectArrays.med3Indirect(perm, x2, n2 - 2 * s2, n2 - s2, n2);
        }
        m2 = ObjectArrays.med3Indirect(perm, x2, l2, m2, n2);
        K v2 = x2[perm[m2]];
        int b2 = a2 = from;
        int d2 = c2 = to - 1;
        while (true) {
            int comparison;
            if (b2 <= c2 && (comparison = ((Comparable)x2[perm[b2]]).compareTo(v2)) <= 0) {
                if (comparison == 0) {
                    IntArrays.swap(perm, a2++, b2);
                }
                ++b2;
                continue;
            }
            while (c2 >= b2 && (comparison = ((Comparable)x2[perm[c2]]).compareTo(v2)) >= 0) {
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
            ObjectArrays.quickSortIndirect(perm, x2, from, from + s3);
        }
        if ((s3 = d2 - c2) > 1) {
            ObjectArrays.quickSortIndirect(perm, x2, to - s3, to);
        }
    }

    public static <K> void quickSortIndirect(int[] perm, K[] x2) {
        ObjectArrays.quickSortIndirect(perm, x2, 0, x2.length);
    }

    public static <K> void parallelQuickSortIndirect(int[] perm, K[] x2, int from, int to) {
        ForkJoinPool pool = ObjectArrays.getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            ObjectArrays.quickSortIndirect(perm, x2, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSortIndirect<K>(perm, x2, from, to));
        }
    }

    public static <K> void parallelQuickSortIndirect(int[] perm, K[] x2) {
        ObjectArrays.parallelQuickSortIndirect(perm, x2, 0, x2.length);
    }

    public static <K> void stabilize(int[] perm, K[] x2, int from, int to) {
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

    public static <K> void stabilize(int[] perm, K[] x2) {
        ObjectArrays.stabilize(perm, x2, 0, perm.length);
    }

    private static <K> int med3(K[] x2, K[] y2, int a2, int b2, int c2) {
        int bc;
        int t2 = ((Comparable)x2[a2]).compareTo(x2[b2]);
        int ab = t2 == 0 ? ((Comparable)y2[a2]).compareTo(y2[b2]) : t2;
        t2 = ((Comparable)x2[a2]).compareTo(x2[c2]);
        int ac = t2 == 0 ? ((Comparable)y2[a2]).compareTo(y2[c2]) : t2;
        t2 = ((Comparable)x2[b2]).compareTo(x2[c2]);
        int n2 = bc = t2 == 0 ? ((Comparable)y2[b2]).compareTo(y2[c2]) : t2;
        return ab < 0 ? (bc < 0 ? b2 : (ac < 0 ? c2 : a2)) : (bc > 0 ? b2 : (ac > 0 ? c2 : a2));
    }

    private static <K> void swap(K[] x2, K[] y2, int a2, int b2) {
        K t2 = x2[a2];
        K u2 = y2[a2];
        x2[a2] = x2[b2];
        y2[a2] = y2[b2];
        x2[b2] = t2;
        y2[b2] = u2;
    }

    private static <K> void swap(K[] x2, K[] y2, int a2, int b2, int n2) {
        int i2 = 0;
        while (i2 < n2) {
            ObjectArrays.swap(x2, y2, a2, b2);
            ++i2;
            ++a2;
            ++b2;
        }
    }

    private static <K> void selectionSort(K[] a2, K[] b2, int from, int to) {
        for (int i2 = from; i2 < to - 1; ++i2) {
            int m2 = i2;
            for (int j2 = i2 + 1; j2 < to; ++j2) {
                int u2 = ((Comparable)a2[j2]).compareTo(a2[m2]);
                if (u2 >= 0 && (u2 != 0 || ((Comparable)b2[j2]).compareTo(b2[m2]) >= 0)) continue;
                m2 = j2;
            }
            if (m2 == i2) continue;
            K t2 = a2[i2];
            a2[i2] = a2[m2];
            a2[m2] = t2;
            t2 = b2[i2];
            b2[i2] = b2[m2];
            b2[m2] = t2;
        }
    }

    public static <K> void quickSort(K[] x2, K[] y2, int from, int to) {
        int c2;
        int a2;
        int len = to - from;
        if (len < 16) {
            ObjectArrays.selectionSort(x2, y2, from, to);
            return;
        }
        int m2 = from + len / 2;
        int l2 = from;
        int n2 = to - 1;
        if (len > 128) {
            int s2 = len / 8;
            l2 = ObjectArrays.med3(x2, y2, l2, l2 + s2, l2 + 2 * s2);
            m2 = ObjectArrays.med3(x2, y2, m2 - s2, m2, m2 + s2);
            n2 = ObjectArrays.med3(x2, y2, n2 - 2 * s2, n2 - s2, n2);
        }
        m2 = ObjectArrays.med3(x2, y2, l2, m2, n2);
        K v2 = x2[m2];
        K w2 = y2[m2];
        int b2 = a2 = from;
        int d2 = c2 = to - 1;
        while (true) {
            int t2;
            int comparison;
            if (b2 <= c2 && (comparison = (t2 = ((Comparable)x2[b2]).compareTo(v2)) == 0 ? ((Comparable)y2[b2]).compareTo(w2) : t2) <= 0) {
                if (comparison == 0) {
                    ObjectArrays.swap(x2, y2, a2++, b2);
                }
                ++b2;
                continue;
            }
            while (c2 >= b2 && (comparison = (t2 = ((Comparable)x2[c2]).compareTo(v2)) == 0 ? ((Comparable)y2[c2]).compareTo(w2) : t2) >= 0) {
                if (comparison == 0) {
                    ObjectArrays.swap(x2, y2, c2, d2--);
                }
                --c2;
            }
            if (b2 > c2) break;
            ObjectArrays.swap(x2, y2, b2++, c2--);
        }
        int s3 = Math.min(a2 - from, b2 - a2);
        ObjectArrays.swap(x2, y2, from, b2 - s3, s3);
        s3 = Math.min(d2 - c2, to - d2 - 1);
        ObjectArrays.swap(x2, y2, b2, to - s3, s3);
        s3 = b2 - a2;
        if (s3 > 1) {
            ObjectArrays.quickSort(x2, y2, from, from + s3);
        }
        if ((s3 = d2 - c2) > 1) {
            ObjectArrays.quickSort(x2, y2, to - s3, to);
        }
    }

    public static <K> void quickSort(K[] x2, K[] y2) {
        ObjectArrays.ensureSameLength(x2, y2);
        ObjectArrays.quickSort(x2, y2, 0, x2.length);
    }

    public static <K> void parallelQuickSort(K[] x2, K[] y2, int from, int to) {
        ForkJoinPool pool = ObjectArrays.getPool();
        if (to - from < 8192 || pool.getParallelism() == 1) {
            ObjectArrays.quickSort(x2, y2, from, to);
        } else {
            pool.invoke(new ForkJoinQuickSort2<K>(x2, y2, from, to));
        }
    }

    public static <K> void parallelQuickSort(K[] x2, K[] y2) {
        ObjectArrays.ensureSameLength(x2, y2);
        ObjectArrays.parallelQuickSort(x2, y2, 0, x2.length);
    }

    public static <K> void unstableSort(K[] a2, int from, int to) {
        ObjectArrays.quickSort(a2, from, to);
    }

    public static <K> void unstableSort(K[] a2) {
        ObjectArrays.unstableSort(a2, 0, a2.length);
    }

    public static <K> void unstableSort(K[] a2, int from, int to, Comparator<K> comp) {
        ObjectArrays.quickSort(a2, from, to, comp);
    }

    public static <K> void unstableSort(K[] a2, Comparator<K> comp) {
        ObjectArrays.unstableSort(a2, 0, a2.length, comp);
    }

    public static <K> void mergeSort(K[] a2, int from, int to, K[] supp) {
        int len = to - from;
        if (len < 16) {
            ObjectArrays.insertionSort(a2, from, to);
            return;
        }
        if (supp == null) {
            supp = java.util.Arrays.copyOf(a2, to);
        }
        int mid = from + to >>> 1;
        ObjectArrays.mergeSort(supp, from, mid, a2);
        ObjectArrays.mergeSort(supp, mid, to, a2);
        if (((Comparable)supp[mid - 1]).compareTo(supp[mid]) <= 0) {
            System.arraycopy(supp, from, a2, from, len);
            return;
        }
        int p2 = from;
        int q2 = mid;
        for (int i2 = from; i2 < to; ++i2) {
            a2[i2] = q2 >= to || p2 < mid && ((Comparable)supp[p2]).compareTo(supp[q2]) <= 0 ? supp[p2++] : supp[q2++];
        }
    }

    public static <K> void mergeSort(K[] a2, int from, int to) {
        ObjectArrays.mergeSort(a2, from, to, (Object[])null);
    }

    public static <K> void mergeSort(K[] a2) {
        ObjectArrays.mergeSort(a2, 0, a2.length);
    }

    public static <K> void mergeSort(K[] a2, int from, int to, Comparator<K> comp, K[] supp) {
        int len = to - from;
        if (len < 16) {
            ObjectArrays.insertionSort(a2, from, to, comp);
            return;
        }
        if (supp == null) {
            supp = java.util.Arrays.copyOf(a2, to);
        }
        int mid = from + to >>> 1;
        ObjectArrays.mergeSort(supp, from, mid, comp, a2);
        ObjectArrays.mergeSort(supp, mid, to, comp, a2);
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

    public static <K> void mergeSort(K[] a2, int from, int to, Comparator<K> comp) {
        ObjectArrays.mergeSort(a2, from, to, comp, null);
    }

    public static <K> void mergeSort(K[] a2, Comparator<K> comp) {
        ObjectArrays.mergeSort(a2, 0, a2.length, comp);
    }

    public static <K> void stableSort(K[] a2, int from, int to) {
        java.util.Arrays.sort(a2, from, to);
    }

    public static <K> void stableSort(K[] a2) {
        ObjectArrays.stableSort(a2, 0, a2.length);
    }

    public static <K> void stableSort(K[] a2, int from, int to, Comparator<K> comp) {
        java.util.Arrays.sort(a2, from, to, comp);
    }

    public static <K> void stableSort(K[] a2, Comparator<K> comp) {
        ObjectArrays.stableSort(a2, 0, a2.length, comp);
    }

    public static <K> int binarySearch(K[] a2, int from, int to, K key) {
        --to;
        while (from <= to) {
            int mid = from + to >>> 1;
            K midVal = a2[mid];
            int cmp = ((Comparable)midVal).compareTo(key);
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

    public static <K> int binarySearch(K[] a2, K key) {
        return ObjectArrays.binarySearch(a2, 0, a2.length, key);
    }

    public static <K> int binarySearch(K[] a2, int from, int to, K key, Comparator<K> c2) {
        --to;
        while (from <= to) {
            int mid = from + to >>> 1;
            K midVal = a2[mid];
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

    public static <K> int binarySearch(K[] a2, K key, Comparator<K> c2) {
        return ObjectArrays.binarySearch(a2, 0, a2.length, key, c2);
    }

    public static <K> K[] shuffle(K[] a2, int from, int to, Random random) {
        int i2 = to - from;
        while (i2-- != 0) {
            int p2 = random.nextInt(i2 + 1);
            K t2 = a2[from + i2];
            a2[from + i2] = a2[from + p2];
            a2[from + p2] = t2;
        }
        return a2;
    }

    public static <K> K[] shuffle(K[] a2, Random random) {
        int i2 = a2.length;
        while (i2-- != 0) {
            int p2 = random.nextInt(i2 + 1);
            K t2 = a2[i2];
            a2[i2] = a2[p2];
            a2[p2] = t2;
        }
        return a2;
    }

    public static <K> K[] reverse(K[] a2) {
        int length = a2.length;
        int i2 = length / 2;
        while (i2-- != 0) {
            K t2 = a2[length - i2 - 1];
            a2[length - i2 - 1] = a2[i2];
            a2[i2] = t2;
        }
        return a2;
    }

    public static <K> K[] reverse(K[] a2, int from, int to) {
        int length = to - from;
        int i2 = length / 2;
        while (i2-- != 0) {
            K t2 = a2[from + length - i2 - 1];
            a2[from + length - i2 - 1] = a2[from + i2];
            a2[from + i2] = t2;
        }
        return a2;
    }

    protected static class ForkJoinQuickSortComp<K>
    extends RecursiveAction {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final K[] x;
        private final Comparator<K> comp;

        public ForkJoinQuickSortComp(K[] x2, int from, int to, Comparator<K> comp) {
            this.from = from;
            this.to = to;
            this.x = x2;
            this.comp = comp;
        }

        @Override
        protected void compute() {
            int c2;
            int a2;
            Object[] x2 = this.x;
            int len = this.to - this.from;
            if (len < 8192) {
                ObjectArrays.quickSort(x2, this.from, this.to, this.comp);
                return;
            }
            int m2 = this.from + len / 2;
            int l2 = this.from;
            int n2 = this.to - 1;
            int s2 = len / 8;
            l2 = ObjectArrays.med3(x2, l2, l2 + s2, l2 + 2 * s2, this.comp);
            m2 = ObjectArrays.med3(x2, m2 - s2, m2, m2 + s2, this.comp);
            n2 = ObjectArrays.med3(x2, n2 - 2 * s2, n2 - s2, n2, this.comp);
            m2 = ObjectArrays.med3(x2, l2, m2, n2, this.comp);
            Object v2 = x2[m2];
            int b2 = a2 = this.from;
            int d2 = c2 = this.to - 1;
            while (true) {
                int comparison;
                if (b2 <= c2 && (comparison = this.comp.compare(x2[b2], v2)) <= 0) {
                    if (comparison == 0) {
                        ObjectArrays.swap(x2, a2++, b2);
                    }
                    ++b2;
                    continue;
                }
                while (c2 >= b2 && (comparison = this.comp.compare(x2[c2], v2)) >= 0) {
                    if (comparison == 0) {
                        ObjectArrays.swap(x2, c2, d2--);
                    }
                    --c2;
                }
                if (b2 > c2) break;
                ObjectArrays.swap(x2, b2++, c2--);
            }
            s2 = Math.min(a2 - this.from, b2 - a2);
            ObjectArrays.swap(x2, this.from, b2 - s2, s2);
            s2 = Math.min(d2 - c2, this.to - d2 - 1);
            ObjectArrays.swap(x2, b2, this.to - s2, s2);
            s2 = b2 - a2;
            int t2 = d2 - c2;
            if (s2 > 1 && t2 > 1) {
                ForkJoinQuickSortComp.invokeAll(new ForkJoinQuickSortComp<Object>(x2, this.from, this.from + s2, this.comp), new ForkJoinQuickSortComp<Object>(x2, this.to - t2, this.to, this.comp));
            } else if (s2 > 1) {
                ForkJoinQuickSortComp.invokeAll(new ForkJoinQuickSortComp<Object>(x2, this.from, this.from + s2, this.comp));
            } else {
                ForkJoinQuickSortComp.invokeAll(new ForkJoinQuickSortComp<Object>(x2, this.to - t2, this.to, this.comp));
            }
        }
    }

    protected static class ForkJoinQuickSort<K>
    extends RecursiveAction {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final K[] x;

        public ForkJoinQuickSort(K[] x2, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x2;
        }

        @Override
        protected void compute() {
            int c2;
            int a2;
            Object[] x2 = this.x;
            int len = this.to - this.from;
            if (len < 8192) {
                ObjectArrays.quickSort(x2, this.from, this.to);
                return;
            }
            int m2 = this.from + len / 2;
            int l2 = this.from;
            int n2 = this.to - 1;
            int s2 = len / 8;
            l2 = ObjectArrays.med3(x2, l2, l2 + s2, l2 + 2 * s2);
            m2 = ObjectArrays.med3(x2, m2 - s2, m2, m2 + s2);
            n2 = ObjectArrays.med3(x2, n2 - 2 * s2, n2 - s2, n2);
            m2 = ObjectArrays.med3(x2, l2, m2, n2);
            Object v2 = x2[m2];
            int b2 = a2 = this.from;
            int d2 = c2 = this.to - 1;
            while (true) {
                int comparison;
                if (b2 <= c2 && (comparison = ((Comparable)x2[b2]).compareTo(v2)) <= 0) {
                    if (comparison == 0) {
                        ObjectArrays.swap(x2, a2++, b2);
                    }
                    ++b2;
                    continue;
                }
                while (c2 >= b2 && (comparison = ((Comparable)x2[c2]).compareTo(v2)) >= 0) {
                    if (comparison == 0) {
                        ObjectArrays.swap(x2, c2, d2--);
                    }
                    --c2;
                }
                if (b2 > c2) break;
                ObjectArrays.swap(x2, b2++, c2--);
            }
            s2 = Math.min(a2 - this.from, b2 - a2);
            ObjectArrays.swap(x2, this.from, b2 - s2, s2);
            s2 = Math.min(d2 - c2, this.to - d2 - 1);
            ObjectArrays.swap(x2, b2, this.to - s2, s2);
            s2 = b2 - a2;
            int t2 = d2 - c2;
            if (s2 > 1 && t2 > 1) {
                ForkJoinQuickSort.invokeAll(new ForkJoinQuickSort<Object>(x2, this.from, this.from + s2), new ForkJoinQuickSort<Object>(x2, this.to - t2, this.to));
            } else if (s2 > 1) {
                ForkJoinQuickSort.invokeAll(new ForkJoinQuickSort<Object>(x2, this.from, this.from + s2));
            } else {
                ForkJoinQuickSort.invokeAll(new ForkJoinQuickSort<Object>(x2, this.to - t2, this.to));
            }
        }
    }

    protected static class ForkJoinQuickSortIndirect<K>
    extends RecursiveAction {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final int[] perm;
        private final K[] x;

        public ForkJoinQuickSortIndirect(int[] perm, K[] x2, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x2;
            this.perm = perm;
        }

        @Override
        protected void compute() {
            int c2;
            int a2;
            Object[] x2 = this.x;
            int len = this.to - this.from;
            if (len < 8192) {
                ObjectArrays.quickSortIndirect(this.perm, x2, this.from, this.to);
                return;
            }
            int m2 = this.from + len / 2;
            int l2 = this.from;
            int n2 = this.to - 1;
            int s2 = len / 8;
            l2 = ObjectArrays.med3Indirect(this.perm, x2, l2, l2 + s2, l2 + 2 * s2);
            m2 = ObjectArrays.med3Indirect(this.perm, x2, m2 - s2, m2, m2 + s2);
            n2 = ObjectArrays.med3Indirect(this.perm, x2, n2 - 2 * s2, n2 - s2, n2);
            m2 = ObjectArrays.med3Indirect(this.perm, x2, l2, m2, n2);
            Object v2 = x2[this.perm[m2]];
            int b2 = a2 = this.from;
            int d2 = c2 = this.to - 1;
            while (true) {
                int comparison;
                if (b2 <= c2 && (comparison = ((Comparable)x2[this.perm[b2]]).compareTo(v2)) <= 0) {
                    if (comparison == 0) {
                        IntArrays.swap(this.perm, a2++, b2);
                    }
                    ++b2;
                    continue;
                }
                while (c2 >= b2 && (comparison = ((Comparable)x2[this.perm[c2]]).compareTo(v2)) >= 0) {
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
                ForkJoinQuickSortIndirect.invokeAll(new ForkJoinQuickSortIndirect<Object>(this.perm, x2, this.from, this.from + s2), new ForkJoinQuickSortIndirect<Object>(this.perm, x2, this.to - t2, this.to));
            } else if (s2 > 1) {
                ForkJoinQuickSortIndirect.invokeAll(new ForkJoinQuickSortIndirect<Object>(this.perm, x2, this.from, this.from + s2));
            } else {
                ForkJoinQuickSortIndirect.invokeAll(new ForkJoinQuickSortIndirect<Object>(this.perm, x2, this.to - t2, this.to));
            }
        }
    }

    protected static class ForkJoinQuickSort2<K>
    extends RecursiveAction {
        private static final long serialVersionUID = 1L;
        private final int from;
        private final int to;
        private final K[] x;
        private final K[] y;

        public ForkJoinQuickSort2(K[] x2, K[] y2, int from, int to) {
            this.from = from;
            this.to = to;
            this.x = x2;
            this.y = y2;
        }

        @Override
        protected void compute() {
            int c2;
            int a2;
            Object[] x2 = this.x;
            Object[] y2 = this.y;
            int len = this.to - this.from;
            if (len < 8192) {
                ObjectArrays.quickSort(x2, y2, this.from, this.to);
                return;
            }
            int m2 = this.from + len / 2;
            int l2 = this.from;
            int n2 = this.to - 1;
            int s2 = len / 8;
            l2 = ObjectArrays.med3(x2, y2, l2, l2 + s2, l2 + 2 * s2);
            m2 = ObjectArrays.med3(x2, y2, m2 - s2, m2, m2 + s2);
            n2 = ObjectArrays.med3(x2, y2, n2 - 2 * s2, n2 - s2, n2);
            m2 = ObjectArrays.med3(x2, y2, l2, m2, n2);
            Object v2 = x2[m2];
            Object w2 = y2[m2];
            int b2 = a2 = this.from;
            int d2 = c2 = this.to - 1;
            while (true) {
                int t2;
                int comparison;
                if (b2 <= c2 && (comparison = (t2 = ((Comparable)x2[b2]).compareTo(v2)) == 0 ? ((Comparable)y2[b2]).compareTo(w2) : t2) <= 0) {
                    if (comparison == 0) {
                        ObjectArrays.swap(x2, y2, a2++, b2);
                    }
                    ++b2;
                    continue;
                }
                while (c2 >= b2 && (comparison = (t2 = ((Comparable)x2[c2]).compareTo(v2)) == 0 ? ((Comparable)y2[c2]).compareTo(w2) : t2) >= 0) {
                    if (comparison == 0) {
                        ObjectArrays.swap(x2, y2, c2, d2--);
                    }
                    --c2;
                }
                if (b2 > c2) break;
                ObjectArrays.swap(x2, y2, b2++, c2--);
            }
            s2 = Math.min(a2 - this.from, b2 - a2);
            ObjectArrays.swap(x2, y2, this.from, b2 - s2, s2);
            s2 = Math.min(d2 - c2, this.to - d2 - 1);
            ObjectArrays.swap(x2, y2, b2, this.to - s2, s2);
            s2 = b2 - a2;
            int t3 = d2 - c2;
            if (s2 > 1 && t3 > 1) {
                ForkJoinQuickSort2.invokeAll(new ForkJoinQuickSort2<Object>(x2, y2, this.from, this.from + s2), new ForkJoinQuickSort2<Object>(x2, y2, this.to - t3, this.to));
            } else if (s2 > 1) {
                ForkJoinQuickSort2.invokeAll(new ForkJoinQuickSort2<Object>(x2, y2, this.from, this.from + s2));
            } else {
                ForkJoinQuickSort2.invokeAll(new ForkJoinQuickSort2<Object>(x2, y2, this.to - t3, this.to));
            }
        }
    }

    private static final class ArrayHashStrategy<K>
    implements Hash.Strategy<K[]>,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        private ArrayHashStrategy() {
        }

        @Override
        public int hashCode(K[] o2) {
            return java.util.Arrays.hashCode(o2);
        }

        @Override
        public boolean equals(K[] a2, K[] b2) {
            return java.util.Arrays.equals(a2, b2);
        }
    }
}

