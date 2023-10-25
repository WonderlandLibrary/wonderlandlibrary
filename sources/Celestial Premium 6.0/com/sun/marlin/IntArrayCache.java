/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.marlin.ArrayCacheConst;
import com.sun.marlin.MarlinConst;
import com.sun.marlin.MarlinUtils;
import java.lang.ref.WeakReference;
import java.util.Arrays;

public final class IntArrayCache
implements MarlinConst {
    final boolean clean;
    private final int bucketCapacity;
    private WeakReference<Bucket[]> refBuckets = null;
    final ArrayCacheConst.CacheStats stats;

    IntArrayCache(boolean bl, int n) {
        this.clean = bl;
        this.bucketCapacity = n;
        this.stats = DO_STATS ? new ArrayCacheConst.CacheStats(IntArrayCache.getLogPrefix(bl) + "IntArrayCache") : null;
    }

    Bucket getCacheBucket(int n) {
        int n2 = ArrayCacheConst.getBucket(n);
        return this.getBuckets()[n2];
    }

    private Bucket[] getBuckets() {
        Bucket[] arrbucket;
        Bucket[] arrbucket2 = arrbucket = this.refBuckets != null ? (Bucket[])this.refBuckets.get() : null;
        if (arrbucket == null) {
            arrbucket = new Bucket[8];
            for (int i = 0; i < 8; ++i) {
                arrbucket[i] = new Bucket(this.clean, ArrayCacheConst.ARRAY_SIZES[i], this.bucketCapacity, DO_STATS ? this.stats.bucketStats[i] : null);
            }
            this.refBuckets = new WeakReference<Bucket[]>(arrbucket);
        }
        return arrbucket;
    }

    Reference createRef(int n) {
        return new Reference(this, n);
    }

    static int[] createArray(int n) {
        return new int[n];
    }

    static void fill(int[] arrn, int n, int n2, int n3) {
        Arrays.fill(arrn, n, n2, n3);
        if (DO_CHECKS) {
            IntArrayCache.check(arrn, n, n2, n3);
        }
    }

    public static void check(int[] arrn, int n, int n2, int n3) {
        if (DO_CHECKS) {
            for (int i = 0; i < arrn.length; ++i) {
                if (arrn[i] == n3) continue;
                MarlinUtils.logException("Invalid value at: " + i + " = " + arrn[i] + " from: " + n + " to: " + n2 + "\n" + Arrays.toString(arrn), new Throwable());
                Arrays.fill(arrn, n3);
                return;
            }
        }
    }

    static String getLogPrefix(boolean bl) {
        return bl ? "Clean" : "Dirty";
    }

    static final class Bucket {
        private int tail = 0;
        private final int arraySize;
        private final boolean clean;
        private final int[][] arrays;
        private final ArrayCacheConst.BucketStats stats;

        Bucket(boolean bl, int n, int n2, ArrayCacheConst.BucketStats bucketStats) {
            this.arraySize = n;
            this.clean = bl;
            this.stats = bucketStats;
            this.arrays = new int[n2][];
        }

        int[] getArray() {
            if (MarlinConst.DO_STATS) {
                ++this.stats.getOp;
            }
            if (this.tail != 0) {
                int[] arrn = this.arrays[--this.tail];
                this.arrays[this.tail] = null;
                return arrn;
            }
            if (MarlinConst.DO_STATS) {
                ++this.stats.createOp;
            }
            return IntArrayCache.createArray(this.arraySize);
        }

        void putArray(int[] arrn) {
            if (MarlinConst.DO_CHECKS && arrn.length != this.arraySize) {
                MarlinUtils.logInfo(IntArrayCache.getLogPrefix(this.clean) + "IntArrayCache: bad length = " + arrn.length);
                return;
            }
            if (MarlinConst.DO_STATS) {
                ++this.stats.returnOp;
            }
            if (this.arrays.length > this.tail) {
                this.arrays[this.tail++] = arrn;
                if (MarlinConst.DO_STATS) {
                    this.stats.updateMaxSize(this.tail);
                }
            } else if (MarlinConst.DO_CHECKS) {
                MarlinUtils.logInfo(IntArrayCache.getLogPrefix(this.clean) + "IntArrayCache: array capacity exceeded !");
            }
        }
    }

    static final class Reference {
        final int[] initial;
        private final boolean clean;
        private final IntArrayCache cache;

        Reference(IntArrayCache intArrayCache, int n) {
            this.cache = intArrayCache;
            this.clean = intArrayCache.clean;
            this.initial = IntArrayCache.createArray(n);
            if (MarlinConst.DO_STATS) {
                intArrayCache.stats.totalInitial += (long)n;
            }
        }

        int[] getArray(int n) {
            if (n <= ArrayCacheConst.MAX_ARRAY_SIZE) {
                return this.cache.getCacheBucket(n).getArray();
            }
            if (MarlinConst.DO_STATS) {
                ++this.cache.stats.oversize;
            }
            if (MarlinConst.DO_LOG_OVERSIZE) {
                MarlinUtils.logInfo(IntArrayCache.getLogPrefix(this.clean) + "IntArrayCache: getArray[oversize]: length=\t" + n);
            }
            return IntArrayCache.createArray(n);
        }

        int[] widenArray(int[] arrn, int n, int n2) {
            int n3 = arrn.length;
            if (MarlinConst.DO_CHECKS && n3 >= n2) {
                return arrn;
            }
            if (MarlinConst.DO_STATS) {
                ++this.cache.stats.resize;
            }
            int[] arrn2 = this.getArray(ArrayCacheConst.getNewSize(n, n2));
            System.arraycopy(arrn, 0, arrn2, 0, n);
            this.putArray(arrn, 0, n);
            if (MarlinConst.DO_LOG_WIDEN_ARRAY) {
                MarlinUtils.logInfo(IntArrayCache.getLogPrefix(this.clean) + "IntArrayCache: widenArray[" + arrn2.length + "]: usedSize=\t" + n + "\tlength=\t" + n3 + "\tneeded length=\t" + n2);
            }
            return arrn2;
        }

        int[] putArray(int[] arrn) {
            return this.putArray(arrn, 0, arrn.length);
        }

        int[] putArray(int[] arrn, int n, int n2) {
            if (arrn.length <= ArrayCacheConst.MAX_ARRAY_SIZE) {
                if (this.clean && n2 != 0) {
                    IntArrayCache.fill(arrn, n, n2, 0);
                }
                if (arrn != this.initial) {
                    this.cache.getCacheBucket(arrn.length).putArray(arrn);
                }
            }
            return this.initial;
        }
    }
}

