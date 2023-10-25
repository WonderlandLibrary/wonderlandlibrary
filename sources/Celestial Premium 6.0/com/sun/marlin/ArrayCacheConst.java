/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

import com.sun.marlin.MarlinConst;
import com.sun.marlin.MarlinUtils;
import java.util.Arrays;

public final class ArrayCacheConst
implements MarlinConst {
    static final int BUCKETS = 8;
    static final int MIN_ARRAY_SIZE = 4096;
    static final int MAX_ARRAY_SIZE;
    static final int THRESHOLD_SMALL_ARRAY_SIZE = 0x400000;
    static final int THRESHOLD_ARRAY_SIZE;
    static final long THRESHOLD_HUGE_ARRAY_SIZE;
    static final int[] ARRAY_SIZES;

    private ArrayCacheConst() {
    }

    static int getBucket(int n) {
        for (int i = 0; i < ARRAY_SIZES.length; ++i) {
            if (n > ARRAY_SIZES[i]) continue;
            return i;
        }
        return -1;
    }

    public static int getNewSize(int n, int n2) {
        if (n2 < 0) {
            throw new ArrayIndexOutOfBoundsException("array exceeds maximum capacity !");
        }
        assert (n >= 0);
        int n3 = n;
        int n4 = n3 > THRESHOLD_ARRAY_SIZE ? n3 + (n3 >> 1) : n3 << 1;
        if (n4 < n2) {
            n4 = (n2 >> 12) + 1 << 12;
        }
        if (n4 < 0) {
            n4 = Integer.MAX_VALUE;
        }
        return n4;
    }

    public static long getNewLargeSize(long l, long l2) {
        if (l2 >> 31 != 0L) {
            throw new ArrayIndexOutOfBoundsException("array exceeds maximum capacity !");
        }
        assert (l >= 0L);
        long l3 = l > THRESHOLD_HUGE_ARRAY_SIZE ? l + (l >> 2) : (l > (long)THRESHOLD_ARRAY_SIZE ? l + (l >> 1) : (l > 0x400000L ? l << 1 : l << 2));
        if (l3 < l2) {
            l3 = (l2 >> 12) + 1L << 12;
        }
        if (l3 > Integer.MAX_VALUE) {
            l3 = Integer.MAX_VALUE;
        }
        return l3;
    }

    static {
        ARRAY_SIZES = new int[8];
        int n = 4096;
        int n2 = 2;
        int n3 = 0;
        while (n3 < 8) {
            ArrayCacheConst.ARRAY_SIZES[n3] = n;
            if (DO_TRACE) {
                MarlinUtils.logInfo("arraySize[" + n3 + "]: " + n);
            }
            if (n >= 0x400000) {
                n2 = 1;
            }
            ++n3;
            n <<= n2;
        }
        MAX_ARRAY_SIZE = n >> n2;
        if (MAX_ARRAY_SIZE <= 0) {
            throw new IllegalStateException("Invalid max array size !");
        }
        THRESHOLD_ARRAY_SIZE = 0x1000000;
        THRESHOLD_HUGE_ARRAY_SIZE = 0x3000000L;
        if (DO_STATS) {
            MarlinUtils.logInfo("ArrayCache.BUCKETS        = 8");
            MarlinUtils.logInfo("ArrayCache.MIN_ARRAY_SIZE = 4096");
            MarlinUtils.logInfo("ArrayCache.MAX_ARRAY_SIZE = " + MAX_ARRAY_SIZE);
            MarlinUtils.logInfo("ArrayCache.ARRAY_SIZES = " + Arrays.toString(ARRAY_SIZES));
            MarlinUtils.logInfo("ArrayCache.THRESHOLD_ARRAY_SIZE = " + THRESHOLD_ARRAY_SIZE);
            MarlinUtils.logInfo("ArrayCache.THRESHOLD_HUGE_ARRAY_SIZE = " + THRESHOLD_HUGE_ARRAY_SIZE);
        }
    }

    static final class BucketStats {
        int getOp = 0;
        int createOp = 0;
        int returnOp = 0;
        int maxSize = 0;

        BucketStats() {
        }

        void reset() {
            this.getOp = 0;
            this.createOp = 0;
            this.returnOp = 0;
            this.maxSize = 0;
        }

        void updateMaxSize(int n) {
            if (n > this.maxSize) {
                this.maxSize = n;
            }
        }
    }

    static final class CacheStats {
        final String name;
        final BucketStats[] bucketStats;
        int resize = 0;
        int oversize = 0;
        long totalInitial = 0L;

        CacheStats(String string) {
            this.name = string;
            this.bucketStats = new BucketStats[8];
            for (int i = 0; i < 8; ++i) {
                this.bucketStats[i] = new BucketStats();
            }
        }

        void reset() {
            this.resize = 0;
            this.oversize = 0;
            for (int i = 0; i < 8; ++i) {
                this.bucketStats[i].reset();
            }
        }

        long dumpStats() {
            long l = 0L;
            if (MarlinConst.DO_STATS) {
                BucketStats bucketStats;
                int n;
                for (n = 0; n < 8; ++n) {
                    bucketStats = this.bucketStats[n];
                    if (bucketStats.maxSize == 0) continue;
                    l += (long)(this.getByteFactor() * (bucketStats.maxSize * ARRAY_SIZES[n]));
                }
                if (this.totalInitial != 0L || l != 0L || this.resize != 0 || this.oversize != 0) {
                    MarlinUtils.logInfo(this.name + ": resize: " + this.resize + " - oversize: " + this.oversize + " - initial: " + this.getTotalInitialBytes() + " bytes (" + this.totalInitial + " elements) - cache: " + l + " bytes");
                }
                if (l != 0L) {
                    MarlinUtils.logInfo(this.name + ": usage stats:");
                    for (n = 0; n < 8; ++n) {
                        bucketStats = this.bucketStats[n];
                        if (bucketStats.getOp == 0) continue;
                        MarlinUtils.logInfo("  Bucket[" + ARRAY_SIZES[n] + "]: get: " + bucketStats.getOp + " - put: " + bucketStats.returnOp + " - create: " + bucketStats.createOp + " :: max size: " + bucketStats.maxSize);
                    }
                }
            }
            return l;
        }

        private int getByteFactor() {
            int n = 1;
            if (this.name.contains("Int") || this.name.contains("Float")) {
                n = 4;
            } else if (this.name.contains("Double")) {
                n = 8;
            }
            return n;
        }

        long getTotalInitialBytes() {
            return (long)this.getByteFactor() * this.totalInitial;
        }
    }
}

