/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.coll;

import java.util.Arrays;

public final class CollationWeights {
    private int middleLength;
    private int[] minBytes = new int[5];
    private int[] maxBytes = new int[5];
    private WeightRange[] ranges = new WeightRange[7];
    private int rangeIndex;
    private int rangeCount;

    public void initForPrimary(boolean compressible) {
        this.middleLength = 1;
        this.minBytes[1] = 3;
        this.maxBytes[1] = 255;
        if (compressible) {
            this.minBytes[2] = 4;
            this.maxBytes[2] = 254;
        } else {
            this.minBytes[2] = 2;
            this.maxBytes[2] = 255;
        }
        this.minBytes[3] = 2;
        this.maxBytes[3] = 255;
        this.minBytes[4] = 2;
        this.maxBytes[4] = 255;
    }

    public void initForSecondary() {
        this.middleLength = 3;
        this.minBytes[1] = 0;
        this.maxBytes[1] = 0;
        this.minBytes[2] = 0;
        this.maxBytes[2] = 0;
        this.minBytes[3] = 2;
        this.maxBytes[3] = 255;
        this.minBytes[4] = 2;
        this.maxBytes[4] = 255;
    }

    public void initForTertiary() {
        this.middleLength = 3;
        this.minBytes[1] = 0;
        this.maxBytes[1] = 0;
        this.minBytes[2] = 0;
        this.maxBytes[2] = 0;
        this.minBytes[3] = 2;
        this.maxBytes[3] = 63;
        this.minBytes[4] = 2;
        this.maxBytes[4] = 63;
    }

    public boolean allocWeights(long lowerLimit, long upperLimit, int n2) {
        int minLength;
        if (!this.getWeightRanges(lowerLimit, upperLimit)) {
            return false;
        }
        while (!this.allocWeightsInShortRanges(n2, minLength = this.ranges[0].length)) {
            if (minLength == 4) {
                return false;
            }
            if (this.allocWeightsInMinLengthRanges(n2, minLength)) break;
            for (int i2 = 0; i2 < this.rangeCount && this.ranges[i2].length == minLength; ++i2) {
                this.lengthenRange(this.ranges[i2]);
            }
        }
        this.rangeIndex = 0;
        if (this.rangeCount < this.ranges.length) {
            this.ranges[this.rangeCount] = null;
        }
        return true;
    }

    public long nextWeight() {
        if (this.rangeIndex >= this.rangeCount) {
            return 0xFFFFFFFFL;
        }
        WeightRange range = this.ranges[this.rangeIndex];
        long weight = range.start;
        if (--range.count == 0) {
            ++this.rangeIndex;
        } else {
            range.start = this.incWeight(weight, range.length);
            assert (range.start <= range.end);
        }
        return weight;
    }

    public static int lengthOfWeight(long weight) {
        if ((weight & 0xFFFFFFL) == 0L) {
            return 1;
        }
        if ((weight & 0xFFFFL) == 0L) {
            return 2;
        }
        if ((weight & 0xFFL) == 0L) {
            return 3;
        }
        return 4;
    }

    private static int getWeightTrail(long weight, int length) {
        return (int)(weight >> 8 * (4 - length)) & 0xFF;
    }

    private static long setWeightTrail(long weight, int length, int trail) {
        length = 8 * (4 - length);
        return weight & 0xFFFFFF00L << length | (long)trail << length;
    }

    private static int getWeightByte(long weight, int idx) {
        return CollationWeights.getWeightTrail(weight, idx);
    }

    private static long setWeightByte(long weight, int idx, int b2) {
        long mask = (idx *= 8) < 32 ? 0xFFFFFFFFL >> idx : 0L;
        idx = 32 - idx;
        return weight & (mask |= 0xFFFFFF00L << idx) | (long)b2 << idx;
    }

    private static long truncateWeight(long weight, int length) {
        return weight & 0xFFFFFFFFL << 8 * (4 - length);
    }

    private static long incWeightTrail(long weight, int length) {
        return weight + (1L << 8 * (4 - length));
    }

    private static long decWeightTrail(long weight, int length) {
        return weight - (1L << 8 * (4 - length));
    }

    private int countBytes(int idx) {
        return this.maxBytes[idx] - this.minBytes[idx] + 1;
    }

    private long incWeight(long weight, int length) {
        while (true) {
            int b2;
            if ((b2 = CollationWeights.getWeightByte(weight, length)) < this.maxBytes[length]) {
                return CollationWeights.setWeightByte(weight, length, b2 + 1);
            }
            weight = CollationWeights.setWeightByte(weight, length, this.minBytes[length]);
            assert (--length > 0);
        }
    }

    private long incWeightByOffset(long weight, int length, int offset) {
        while (true) {
            if ((offset += CollationWeights.getWeightByte(weight, length)) <= this.maxBytes[length]) {
                return CollationWeights.setWeightByte(weight, length, offset);
            }
            weight = CollationWeights.setWeightByte(weight, length, this.minBytes[length] + (offset -= this.minBytes[length]) % this.countBytes(length));
            offset /= this.countBytes(length);
            assert (--length > 0);
        }
    }

    private void lengthenRange(WeightRange range) {
        int length = range.length + 1;
        range.start = CollationWeights.setWeightTrail(range.start, length, this.minBytes[length]);
        range.end = CollationWeights.setWeightTrail(range.end, length, this.maxBytes[length]);
        range.count *= this.countBytes(length);
        range.length = length;
    }

    private boolean getWeightRanges(long lowerLimit, long upperLimit) {
        int trail;
        int length;
        assert (lowerLimit != 0L);
        assert (upperLimit != 0L);
        int lowerLength = CollationWeights.lengthOfWeight(lowerLimit);
        int upperLength = CollationWeights.lengthOfWeight(upperLimit);
        assert (lowerLength >= this.middleLength);
        if (lowerLimit >= upperLimit) {
            return false;
        }
        if (lowerLength < upperLength && lowerLimit == CollationWeights.truncateWeight(upperLimit, lowerLength)) {
            return false;
        }
        WeightRange[] lower = new WeightRange[5];
        WeightRange middle = new WeightRange();
        WeightRange[] upper = new WeightRange[5];
        long weight = lowerLimit;
        for (length = lowerLength; length > this.middleLength; --length) {
            trail = CollationWeights.getWeightTrail(weight, length);
            if (trail < this.maxBytes[length]) {
                lower[length] = new WeightRange();
                lower[length].start = CollationWeights.incWeightTrail(weight, length);
                lower[length].end = CollationWeights.setWeightTrail(weight, length, this.maxBytes[length]);
                lower[length].length = length;
                lower[length].count = this.maxBytes[length] - trail;
            }
            weight = CollationWeights.truncateWeight(weight, length - 1);
        }
        middle.start = weight < 0xFF000000L ? CollationWeights.incWeightTrail(weight, this.middleLength) : 0xFFFFFFFFL;
        weight = upperLimit;
        for (length = upperLength; length > this.middleLength; --length) {
            trail = CollationWeights.getWeightTrail(weight, length);
            if (trail > this.minBytes[length]) {
                upper[length] = new WeightRange();
                upper[length].start = CollationWeights.setWeightTrail(weight, length, this.minBytes[length]);
                upper[length].end = CollationWeights.decWeightTrail(weight, length);
                upper[length].length = length;
                upper[length].count = trail - this.minBytes[length];
            }
            weight = CollationWeights.truncateWeight(weight, length - 1);
        }
        middle.end = CollationWeights.decWeightTrail(weight, this.middleLength);
        middle.length = this.middleLength;
        if (middle.end >= middle.start) {
            middle.count = (int)(middle.end - middle.start >> 8 * (4 - this.middleLength)) + 1;
        } else {
            for (length = 4; length > this.middleLength; --length) {
                if (lower[length] == null || upper[length] == null || lower[length].count <= 0 || upper[length].count <= 0) continue;
                long lowerEnd = lower[length].end;
                long upperStart = upper[length].start;
                boolean merged = false;
                if (lowerEnd > upperStart) {
                    assert (CollationWeights.truncateWeight(lowerEnd, length - 1) == CollationWeights.truncateWeight(upperStart, length - 1));
                    lower[length].end = upper[length].end;
                    lower[length].count = CollationWeights.getWeightTrail(lower[length].end, length) - CollationWeights.getWeightTrail(lower[length].start, length) + 1;
                    merged = true;
                } else if (lowerEnd == upperStart) {
                    assert (this.minBytes[length] < this.maxBytes[length]);
                } else if (this.incWeight(lowerEnd, length) == upperStart) {
                    lower[length].end = upper[length].end;
                    lower[length].count += upper[length].count;
                    merged = true;
                }
                if (!merged) continue;
                upper[length].count = 0;
                while (--length > this.middleLength) {
                    upper[length] = null;
                    lower[length] = null;
                }
                break;
            }
        }
        this.rangeCount = 0;
        if (middle.count > 0) {
            this.ranges[0] = middle;
            this.rangeCount = 1;
        }
        for (length = this.middleLength + 1; length <= 4; ++length) {
            if (upper[length] != null && upper[length].count > 0) {
                this.ranges[this.rangeCount++] = upper[length];
            }
            if (lower[length] == null || lower[length].count <= 0) continue;
            this.ranges[this.rangeCount++] = lower[length];
        }
        return this.rangeCount > 0;
    }

    private boolean allocWeightsInShortRanges(int n2, int minLength) {
        for (int i2 = 0; i2 < this.rangeCount && this.ranges[i2].length <= minLength + 1; ++i2) {
            if (n2 <= this.ranges[i2].count) {
                if (this.ranges[i2].length > minLength) {
                    this.ranges[i2].count = n2;
                }
                this.rangeCount = i2 + 1;
                if (this.rangeCount > 1) {
                    Arrays.sort(this.ranges, 0, this.rangeCount);
                }
                return true;
            }
            n2 -= this.ranges[i2].count;
        }
        return false;
    }

    private boolean allocWeightsInMinLengthRanges(int n2, int minLength) {
        int minLengthRangeCount;
        int count = 0;
        for (minLengthRangeCount = 0; minLengthRangeCount < this.rangeCount && this.ranges[minLengthRangeCount].length == minLength; ++minLengthRangeCount) {
            count += this.ranges[minLengthRangeCount].count;
        }
        int nextCountBytes = this.countBytes(minLength + 1);
        if (n2 > count * nextCountBytes) {
            return false;
        }
        long start = this.ranges[0].start;
        long end = this.ranges[0].end;
        for (int i2 = 1; i2 < minLengthRangeCount; ++i2) {
            if (this.ranges[i2].start < start) {
                start = this.ranges[i2].start;
            }
            if (this.ranges[i2].end <= end) continue;
            end = this.ranges[i2].end;
        }
        int count2 = (n2 - count) / (nextCountBytes - 1);
        int count1 = count - count2;
        if (count2 == 0 || count1 + count2 * nextCountBytes < n2) assert (--count1 + ++count2 * nextCountBytes >= n2);
        this.ranges[0].start = start;
        if (count1 == 0) {
            this.ranges[0].end = end;
            this.ranges[0].count = count;
            this.lengthenRange(this.ranges[0]);
            this.rangeCount = 1;
        } else {
            this.ranges[0].end = this.incWeightByOffset(start, minLength, count1 - 1);
            this.ranges[0].count = count1;
            if (this.ranges[1] == null) {
                this.ranges[1] = new WeightRange();
            }
            this.ranges[1].start = this.incWeight(this.ranges[0].end, minLength);
            this.ranges[1].end = end;
            this.ranges[1].length = minLength;
            this.ranges[1].count = count2;
            this.lengthenRange(this.ranges[1]);
            this.rangeCount = 2;
        }
        return true;
    }

    private static final class WeightRange
    implements Comparable<WeightRange> {
        long start;
        long end;
        int length;
        int count;

        private WeightRange() {
        }

        @Override
        public int compareTo(WeightRange other) {
            long l2 = this.start;
            long r2 = other.start;
            if (l2 < r2) {
                return -1;
            }
            if (l2 > r2) {
                return 1;
            }
            return 0;
        }
    }
}

