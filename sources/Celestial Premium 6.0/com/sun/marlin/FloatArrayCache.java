/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.marlin.ArrayCacheConst;
import com.sun.marlin.MarlinConst;
import com.sun.marlin.MarlinUtils;
import java.lang.ref.WeakReference;
import java.util.Arrays;

public final class FloatArrayCache
implements MarlinConst {
    final boolean clean;
    private final int bucketCapacity;
    private WeakReference<Bucket[]> refBuckets = null;
    final ArrayCacheConst.CacheStats stats;

    FloatArrayCache(boolean bl, int n) {
        this.clean = bl;
        this.bucketCapacity = n;
        this.stats = DO_STATS ? new ArrayCacheConst.CacheStats(FloatArrayCache.getLogPrefix(bl) + "FloatArrayCache") : null;
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

    static float[] createArray(int n) {
        return new float[n];
    }

    static void fill(float[] arrf, int n, int n2, float f) {
        Arrays.fill(arrf, n, n2, f);
        if (DO_CHECKS) {
            FloatArrayCache.check(arrf, n, n2, f);
        }
    }

    public static void check(float[] arrf, int n, int n2, float f) {
        if (DO_CHECKS) {
            for (int i = 0; i < arrf.length; ++i) {
                if (arrf[i] == f) continue;
                MarlinUtils.logException("Invalid value at: " + i + " = " + arrf[i] + " from: " + n + " to: " + n2 + "\n" + Arrays.toString(arrf), new Throwable());
                Arrays.fill(arrf, f);
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
        private final float[][] arrays;
        private final ArrayCacheConst.BucketStats stats;

        Bucket(boolean bl, int n, int n2, ArrayCacheConst.BucketStats bucketStats) {
            this.arraySize = n;
            this.clean = bl;
            this.stats = bucketStats;
            this.arrays = new float[n2][];
        }

        float[] getArray() {
            if (MarlinConst.DO_STATS) {
                ++this.stats.getOp;
            }
            if (this.tail != 0) {
                float[] arrf = this.arrays[--this.tail];
                this.arrays[this.tail] = null;
                return arrf;
            }
            if (MarlinConst.DO_STATS) {
                ++this.stats.createOp;
            }
            return FloatArrayCache.createArray(this.arraySize);
        }

        void putArray(float[] arrf) {
            if (MarlinConst.DO_CHECKS && arrf.length != this.arraySize) {
                MarlinUtils.logInfo(FloatArrayCache.getLogPrefix(this.clean) + "FloatArrayCache: bad length = " + arrf.length);
                return;
            }
            if (MarlinConst.DO_STATS) {
                ++this.stats.returnOp;
            }
            if (this.arrays.length > this.tail) {
                this.arrays[this.tail++] = arrf;
                if (MarlinConst.DO_STATS) {
                    this.stats.updateMaxSize(this.tail);
                }
            } else if (MarlinConst.DO_CHECKS) {
                MarlinUtils.logInfo(FloatArrayCache.getLogPrefix(this.clean) + "FloatArrayCache: array capacity exceeded !");
            }
        }
    }

    static final class Reference {
        final float[] initial;
        private final boolean clean;
        private final FloatArrayCache cache;

        Reference(FloatArrayCache floatArrayCache, int n) {
            this.cache = floatArrayCache;
            this.clean = floatArrayCache.clean;
            this.initial = FloatArrayCache.createArray(n);
            if (MarlinConst.DO_STATS) {
                floatArrayCache.stats.totalInitial += (long)n;
            }
        }

        float[] getArray(int n) {
            if (n <= ArrayCacheConst.MAX_ARRAY_SIZE) {
                return this.cache.getCacheBucket(n).getArray();
            }
            if (MarlinConst.DO_STATS) {
                ++this.cache.stats.oversize;
            }
            if (MarlinConst.DO_LOG_OVERSIZE) {
                MarlinUtils.logInfo(FloatArrayCache.getLogPrefix(this.clean) + "FloatArrayCache: getArray[oversize]: length=\t" + n);
            }
            return FloatArrayCache.createArray(n);
        }

        float[] widenArray(float[] arrf, int n, int n2) {
            int n3 = arrf.length;
            if (MarlinConst.DO_CHECKS && n3 >= n2) {
                return arrf;
            }
            if (MarlinConst.DO_STATS) {
                ++this.cache.stats.resize;
            }
            float[] arrf2 = this.getArray(ArrayCacheConst.getNewSize(n, n2));
            System.arraycopy(arrf, 0, arrf2, 0, n);
            this.putArray(arrf, 0, n);
            if (MarlinConst.DO_LOG_WIDEN_ARRAY) {
                MarlinUtils.logInfo(FloatArrayCache.getLogPrefix(this.clean) + "FloatArrayCache: widenArray[" + arrf2.length + "]: usedSize=\t" + n + "\tlength=\t" + n3 + "\tneeded length=\t" + n2);
            }
            return arrf2;
        }

        float[] putArray(float[] arrf) {
            return this.putArray(arrf, 0, arrf.length);
        }

        float[] putArray(float[] arrf, int n, int n2) {
            if (arrf.length <= ArrayCacheConst.MAX_ARRAY_SIZE) {
                if (this.clean && n2 != 0) {
                    FloatArrayCache.fill(arrf, n, n2, 0.0f);
                }
                if (arrf != this.initial) {
                    this.cache.getCacheBucket(arrf.length).putArray(arrf);
                }
            }
            return this.initial;
        }
    }
}

