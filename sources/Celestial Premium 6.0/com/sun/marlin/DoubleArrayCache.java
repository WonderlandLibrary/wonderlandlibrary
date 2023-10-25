/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.marlin.ArrayCacheConst;
import com.sun.marlin.MarlinConst;
import com.sun.marlin.MarlinUtils;
import java.lang.ref.WeakReference;
import java.util.Arrays;

public final class DoubleArrayCache
implements MarlinConst {
    final boolean clean;
    private final int bucketCapacity;
    private WeakReference<Bucket[]> refBuckets = null;
    final ArrayCacheConst.CacheStats stats;

    DoubleArrayCache(boolean bl, int n) {
        this.clean = bl;
        this.bucketCapacity = n;
        this.stats = DO_STATS ? new ArrayCacheConst.CacheStats(DoubleArrayCache.getLogPrefix(bl) + "DoubleArrayCache") : null;
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

    static double[] createArray(int n) {
        return new double[n];
    }

    static void fill(double[] arrd, int n, int n2, double d) {
        Arrays.fill(arrd, n, n2, d);
        if (DO_CHECKS) {
            DoubleArrayCache.check(arrd, n, n2, d);
        }
    }

    public static void check(double[] arrd, int n, int n2, double d) {
        if (DO_CHECKS) {
            for (int i = 0; i < arrd.length; ++i) {
                if (arrd[i] == d) continue;
                MarlinUtils.logException("Invalid value at: " + i + " = " + arrd[i] + " from: " + n + " to: " + n2 + "\n" + Arrays.toString(arrd), new Throwable());
                Arrays.fill(arrd, d);
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
        private final double[][] arrays;
        private final ArrayCacheConst.BucketStats stats;

        Bucket(boolean bl, int n, int n2, ArrayCacheConst.BucketStats bucketStats) {
            this.arraySize = n;
            this.clean = bl;
            this.stats = bucketStats;
            this.arrays = new double[n2][];
        }

        double[] getArray() {
            if (MarlinConst.DO_STATS) {
                ++this.stats.getOp;
            }
            if (this.tail != 0) {
                double[] arrd = this.arrays[--this.tail];
                this.arrays[this.tail] = null;
                return arrd;
            }
            if (MarlinConst.DO_STATS) {
                ++this.stats.createOp;
            }
            return DoubleArrayCache.createArray(this.arraySize);
        }

        void putArray(double[] arrd) {
            if (MarlinConst.DO_CHECKS && arrd.length != this.arraySize) {
                MarlinUtils.logInfo(DoubleArrayCache.getLogPrefix(this.clean) + "DoubleArrayCache: bad length = " + arrd.length);
                return;
            }
            if (MarlinConst.DO_STATS) {
                ++this.stats.returnOp;
            }
            if (this.arrays.length > this.tail) {
                this.arrays[this.tail++] = arrd;
                if (MarlinConst.DO_STATS) {
                    this.stats.updateMaxSize(this.tail);
                }
            } else if (MarlinConst.DO_CHECKS) {
                MarlinUtils.logInfo(DoubleArrayCache.getLogPrefix(this.clean) + "DoubleArrayCache: array capacity exceeded !");
            }
        }
    }

    static final class Reference {
        final double[] initial;
        private final boolean clean;
        private final DoubleArrayCache cache;

        Reference(DoubleArrayCache doubleArrayCache, int n) {
            this.cache = doubleArrayCache;
            this.clean = doubleArrayCache.clean;
            this.initial = DoubleArrayCache.createArray(n);
            if (MarlinConst.DO_STATS) {
                doubleArrayCache.stats.totalInitial += (long)n;
            }
        }

        double[] getArray(int n) {
            if (n <= ArrayCacheConst.MAX_ARRAY_SIZE) {
                return this.cache.getCacheBucket(n).getArray();
            }
            if (MarlinConst.DO_STATS) {
                ++this.cache.stats.oversize;
            }
            if (MarlinConst.DO_LOG_OVERSIZE) {
                MarlinUtils.logInfo(DoubleArrayCache.getLogPrefix(this.clean) + "DoubleArrayCache: getArray[oversize]: length=\t" + n);
            }
            return DoubleArrayCache.createArray(n);
        }

        double[] widenArray(double[] arrd, int n, int n2) {
            int n3 = arrd.length;
            if (MarlinConst.DO_CHECKS && n3 >= n2) {
                return arrd;
            }
            if (MarlinConst.DO_STATS) {
                ++this.cache.stats.resize;
            }
            double[] arrd2 = this.getArray(ArrayCacheConst.getNewSize(n, n2));
            System.arraycopy(arrd, 0, arrd2, 0, n);
            this.putArray(arrd, 0, n);
            if (MarlinConst.DO_LOG_WIDEN_ARRAY) {
                MarlinUtils.logInfo(DoubleArrayCache.getLogPrefix(this.clean) + "DoubleArrayCache: widenArray[" + arrd2.length + "]: usedSize=\t" + n + "\tlength=\t" + n3 + "\tneeded length=\t" + n2);
            }
            return arrd2;
        }

        double[] putArray(double[] arrd) {
            return this.putArray(arrd, 0, arrd.length);
        }

        double[] putArray(double[] arrd, int n, int n2) {
            if (arrd.length <= ArrayCacheConst.MAX_ARRAY_SIZE) {
                if (this.clean && n2 != 0) {
                    DoubleArrayCache.fill(arrd, n, n2, 0.0);
                }
                if (arrd != this.initial) {
                    this.cache.getCacheBucket(arrd.length).putArray(arrd);
                }
            }
            return this.initial;
        }
    }
}

