/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.coll;

import com.ibm.icu.impl.coll.Collation;

public final class CollationRootElements {
    public static final long PRIMARY_SENTINEL = 0xFFFFFF00L;
    public static final int SEC_TER_DELTA_FLAG = 128;
    public static final int PRIMARY_STEP_MASK = 127;
    public static final int IX_FIRST_TERTIARY_INDEX = 0;
    static final int IX_FIRST_SECONDARY_INDEX = 1;
    static final int IX_FIRST_PRIMARY_INDEX = 2;
    static final int IX_COMMON_SEC_AND_TER_CE = 3;
    static final int IX_SEC_TER_BOUNDARIES = 4;
    static final int IX_COUNT = 5;
    private long[] elements;

    public CollationRootElements(long[] rootElements) {
        this.elements = rootElements;
    }

    public int getTertiaryBoundary() {
        return (int)this.elements[4] << 8 & 0xFF00;
    }

    long getFirstTertiaryCE() {
        return this.elements[(int)this.elements[0]] & 0xFFFFFFFFFFFFFF7FL;
    }

    long getLastTertiaryCE() {
        return this.elements[(int)this.elements[1] - 1] & 0xFFFFFFFFFFFFFF7FL;
    }

    public int getLastCommonSecondary() {
        return (int)this.elements[4] >> 16 & 0xFF00;
    }

    public int getSecondaryBoundary() {
        return (int)this.elements[4] >> 8 & 0xFF00;
    }

    long getFirstSecondaryCE() {
        return this.elements[(int)this.elements[1]] & 0xFFFFFFFFFFFFFF7FL;
    }

    long getLastSecondaryCE() {
        return this.elements[(int)this.elements[2] - 1] & 0xFFFFFFFFFFFFFF7FL;
    }

    long getFirstPrimary() {
        return this.elements[(int)this.elements[2]];
    }

    long getFirstPrimaryCE() {
        return Collation.makeCE(this.getFirstPrimary());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    long lastCEWithPrimaryBefore(long p2) {
        long secTer;
        if (p2 == 0L) {
            return 0L;
        }
        assert (p2 > this.elements[(int)this.elements[2]]);
        int index = this.findP(p2);
        long q2 = this.elements[index];
        if (p2 == (q2 & 0xFFFFFF00L)) {
            assert ((q2 & 0x7FL) == 0L);
            secTer = this.elements[index - 1];
            if ((secTer & 0x80L) == 0L) {
                p2 = secTer & 0xFFFFFF00L;
                secTer = 0x5000500L;
                return p2 << 32 | secTer & 0xFFFFFFFFFFFFFF7FL;
            }
            index -= 2;
            while (true) {
                if (((p2 = this.elements[index]) & 0x80L) == 0L) {
                    p2 &= 0xFFFFFF00L;
                    return p2 << 32 | secTer & 0xFFFFFFFFFFFFFF7FL;
                }
                --index;
            }
        }
        p2 = q2 & 0xFFFFFF00L;
        secTer = 0x5000500L;
        while (true) {
            if (((q2 = this.elements[++index]) & 0x80L) == 0L) {
                assert ((q2 & 0x7FL) == 0L);
                return p2 << 32 | secTer & 0xFFFFFFFFFFFFFF7FL;
            }
            secTer = q2;
        }
    }

    long firstCEWithPrimaryAtLeast(long p2) {
        if (p2 == 0L) {
            return 0L;
        }
        int index = this.findP(p2);
        if (p2 != (this.elements[index] & 0xFFFFFF00L)) {
            while (((p2 = this.elements[++index]) & 0x80L) != 0L) {
            }
            assert ((p2 & 0x7FL) == 0L);
        }
        return p2 << 32 | 0x5000500L;
    }

    long getPrimaryBefore(long p2, boolean isCompressible) {
        int step;
        int index = this.findPrimary(p2);
        long q2 = this.elements[index];
        if (p2 == (q2 & 0xFFFFFF00L)) {
            step = (int)q2 & 0x7F;
            if (step == 0) {
                while (((p2 = this.elements[--index]) & 0x80L) != 0L) {
                }
                return p2 & 0xFFFFFF00L;
            }
        } else {
            long nextElement = this.elements[index + 1];
            assert (CollationRootElements.isEndOfPrimaryRange(nextElement));
            step = (int)nextElement & 0x7F;
        }
        if ((p2 & 0xFFFFL) == 0L) {
            return Collation.decTwoBytePrimaryByOneStep(p2, isCompressible, step);
        }
        return Collation.decThreeBytePrimaryByOneStep(p2, isCompressible, step);
    }

    int getSecondaryBefore(long p2, int s2) {
        int sec;
        int previousSec;
        int index;
        if (p2 == 0L) {
            index = (int)this.elements[1];
            previousSec = 0;
            sec = (int)(this.elements[index] >> 16);
        } else {
            index = this.findPrimary(p2) + 1;
            previousSec = 256;
            sec = (int)this.getFirstSecTerForPrimary(index) >>> 16;
        }
        assert (s2 >= sec);
        while (s2 > sec) {
            previousSec = sec;
            assert ((this.elements[index] & 0x80L) != 0L);
            sec = (int)(this.elements[index++] >> 16);
        }
        assert (sec == s2);
        return previousSec;
    }

    int getTertiaryBefore(long p2, int s2, int t2) {
        long secTer;
        int previousTer;
        int index;
        assert ((t2 & 0xFFFFC0C0) == 0);
        if (p2 == 0L) {
            if (s2 == 0) {
                index = (int)this.elements[0];
                previousTer = 0;
            } else {
                index = (int)this.elements[1];
                previousTer = 256;
            }
            secTer = this.elements[index] & 0xFFFFFFFFFFFFFF7FL;
        } else {
            index = this.findPrimary(p2) + 1;
            previousTer = 256;
            secTer = this.getFirstSecTerForPrimary(index);
        }
        long st = (long)s2 << 16 | (long)t2;
        while (st > secTer) {
            if ((int)(secTer >> 16) == s2) {
                previousTer = (int)secTer;
            }
            assert ((this.elements[index] & 0x80L) != 0L);
            secTer = this.elements[index++] & 0xFFFFFFFFFFFFFF7FL;
        }
        assert (secTer == st);
        return previousTer & 0xFFFF;
    }

    int findPrimary(long p2) {
        assert ((p2 & 0xFFL) == 0L);
        int index = this.findP(p2);
        assert (CollationRootElements.isEndOfPrimaryRange(this.elements[index + 1]) || p2 == (this.elements[index] & 0xFFFFFF00L));
        return index;
    }

    long getPrimaryAfter(long p2, int index, boolean isCompressible) {
        int step;
        long q2;
        assert (p2 == (this.elements[index] & 0xFFFFFF00L) || CollationRootElements.isEndOfPrimaryRange(this.elements[index + 1]));
        if (((q2 = this.elements[++index]) & 0x80L) == 0L && (step = (int)q2 & 0x7F) != 0) {
            if ((p2 & 0xFFFFL) == 0L) {
                return Collation.incTwoBytePrimaryByOffset(p2, isCompressible, step);
            }
            return Collation.incThreeBytePrimaryByOffset(p2, isCompressible, step);
        }
        while ((q2 & 0x80L) != 0L) {
            q2 = this.elements[++index];
        }
        assert ((q2 & 0x7FL) == 0L);
        return q2;
    }

    int getSecondaryAfter(int index, int s2) {
        int secLimit;
        long secTer;
        if (index == 0) {
            assert (s2 != 0);
            index = (int)this.elements[1];
            secTer = this.elements[index];
            secLimit = 65536;
        } else {
            assert (index >= (int)this.elements[2]);
            secTer = this.getFirstSecTerForPrimary(index + 1);
            secLimit = this.getSecondaryBoundary();
        }
        do {
            int sec;
            if ((sec = (int)(secTer >> 16)) <= s2) continue;
            return sec;
        } while (((secTer = this.elements[++index]) & 0x80L) != 0L);
        return secLimit;
    }

    int getTertiaryAfter(int index, int s2, int t2) {
        long secTer;
        int terLimit;
        if (index == 0) {
            if (s2 == 0) {
                assert (t2 != 0);
                index = (int)this.elements[0];
                terLimit = 16384;
            } else {
                index = (int)this.elements[1];
                terLimit = this.getTertiaryBoundary();
            }
            secTer = this.elements[index] & 0xFFFFFFFFFFFFFF7FL;
        } else {
            assert (index >= (int)this.elements[2]);
            secTer = this.getFirstSecTerForPrimary(index + 1);
            terLimit = this.getTertiaryBoundary();
        }
        long st = ((long)s2 & 0xFFFFFFFFL) << 16 | (long)t2;
        while (true) {
            if (secTer > st) {
                assert (secTer >> 16 == (long)s2);
                return (int)secTer & 0xFFFF;
            }
            if (((secTer = this.elements[++index]) & 0x80L) == 0L || secTer >> 16 > (long)s2) {
                return terLimit;
            }
            secTer &= 0xFFFFFFFFFFFFFF7FL;
        }
    }

    private long getFirstSecTerForPrimary(int index) {
        long secTer = this.elements[index];
        if ((secTer & 0x80L) == 0L) {
            return 0x5000500L;
        }
        if ((secTer &= 0xFFFFFFFFFFFFFF7FL) > 0x5000500L) {
            return 0x5000500L;
        }
        return secTer;
    }

    private int findP(long p2) {
        assert (p2 >> 24 != 254L);
        int start = (int)this.elements[2];
        assert (p2 >= this.elements[start]);
        int limit = this.elements.length - 1;
        assert (this.elements[limit] >= 0xFFFFFF00L);
        assert (p2 < this.elements[limit]);
        while (start + 1 < limit) {
            int i2 = (int)(((long)start + (long)limit) / 2L);
            long q2 = this.elements[i2];
            if ((q2 & 0x80L) != 0L) {
                int j2;
                for (j2 = i2 + 1; j2 != limit; ++j2) {
                    q2 = this.elements[j2];
                    if ((q2 & 0x80L) != 0L) continue;
                    i2 = j2;
                    break;
                }
                if ((q2 & 0x80L) != 0L) {
                    for (j2 = i2 - 1; j2 != start; --j2) {
                        q2 = this.elements[j2];
                        if ((q2 & 0x80L) != 0L) continue;
                        i2 = j2;
                        break;
                    }
                    if ((q2 & 0x80L) != 0L) break;
                }
            }
            if (p2 < (q2 & 0xFFFFFF00L)) {
                limit = i2;
                continue;
            }
            start = i2;
        }
        return start;
    }

    private static boolean isEndOfPrimaryRange(long q2) {
        return (q2 & 0x80L) == 0L && (q2 & 0x7FL) != 0L;
    }
}

