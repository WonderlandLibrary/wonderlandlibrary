/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.Relation;
import com.ibm.icu.lang.CharSequences;
import com.ibm.icu.util.ICUException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class StringRange {
    private static final boolean DEBUG = false;
    public static final Comparator<int[]> COMPARE_INT_ARRAYS = new Comparator<int[]>(){

        @Override
        public int compare(int[] o1, int[] o2) {
            int minIndex = Math.min(o1.length, o2.length);
            for (int i2 = 0; i2 < minIndex; ++i2) {
                int diff = o1[i2] - o2[i2];
                if (diff == 0) continue;
                return diff;
            }
            return o1.length - o2.length;
        }
    };

    public static void compact(Set<String> source, Adder adder, boolean shorterPairs, boolean moreCompact) {
        if (!moreCompact) {
            String start = null;
            String end = null;
            boolean bl = false;
            int prefixLen = 0;
            for (String s2 : source) {
                int n2;
                if (start != null) {
                    int currentCp;
                    if (s2.regionMatches(0, start, 0, prefixLen) && (currentCp = s2.codePointAt(prefixLen)) == 1 + n2 && s2.length() == prefixLen + Character.charCount(currentCp)) {
                        end = s2;
                        n2 = currentCp;
                        continue;
                    }
                    adder.add(start, end == null ? null : (!shorterPairs ? end : end.substring(prefixLen, end.length())));
                }
                start = s2;
                end = null;
                n2 = s2.codePointBefore(s2.length());
                prefixLen = s2.length() - Character.charCount(n2);
            }
            adder.add(start, end == null ? null : (!shorterPairs ? end : end.substring(prefixLen, end.length())));
        } else {
            Relation<Integer, Ranges> lengthToArrays = Relation.of(new TreeMap(), TreeSet.class);
            for (String string : source) {
                Ranges item = new Ranges(string);
                lengthToArrays.put(item.size(), item);
            }
            for (Map.Entry entry : lengthToArrays.keyValuesSet()) {
                LinkedList<Ranges> compacted = StringRange.compact((Integer)entry.getKey(), (Set)entry.getValue());
                for (Ranges ranges : compacted) {
                    adder.add(ranges.start(), ranges.end(shorterPairs));
                }
            }
        }
    }

    public static void compact(Set<String> source, Adder adder, boolean shorterPairs) {
        StringRange.compact(source, adder, shorterPairs, false);
    }

    private static LinkedList<Ranges> compact(int size, Set<Ranges> inputRanges) {
        LinkedList<Ranges> ranges = new LinkedList<Ranges>(inputRanges);
        for (int i2 = size - 1; i2 >= 0; --i2) {
            Ranges last = null;
            Iterator it = ranges.iterator();
            while (it.hasNext()) {
                Ranges item = (Ranges)it.next();
                if (last == null) {
                    last = item;
                    continue;
                }
                if (last.merge(i2, item)) {
                    it.remove();
                    continue;
                }
                last = item;
            }
        }
        return ranges;
    }

    public static Collection<String> expand(String start, String end, boolean requireSameLength, Collection<String> output) {
        if (start == null || end == null) {
            throw new ICUException("Range must have 2 valid strings");
        }
        int[] startCps = CharSequences.codePoints(start);
        int[] endCps = CharSequences.codePoints(end);
        int startOffset = startCps.length - endCps.length;
        if (requireSameLength && startOffset != 0) {
            throw new ICUException("Range must have equal-length strings");
        }
        if (startOffset < 0) {
            throw new ICUException("Range must have start-length \u2265 end-length");
        }
        if (endCps.length == 0) {
            throw new ICUException("Range must have end-length > 0");
        }
        StringBuilder builder = new StringBuilder();
        for (int i2 = 0; i2 < startOffset; ++i2) {
            builder.appendCodePoint(startCps[i2]);
        }
        StringRange.add(0, startOffset, startCps, endCps, builder, output);
        return output;
    }

    private static void add(int endIndex, int startOffset, int[] starts, int[] ends, StringBuilder builder, Collection<String> output) {
        int start = starts[endIndex + startOffset];
        int end = ends[endIndex];
        if (start > end) {
            throw new ICUException("Range must have x\u1d62 \u2264 y\u1d62 for each index i");
        }
        boolean last = endIndex == ends.length - 1;
        int startLen = builder.length();
        for (int i2 = start; i2 <= end; ++i2) {
            builder.appendCodePoint(i2);
            if (last) {
                output.add(builder.toString());
            } else {
                StringRange.add(endIndex + 1, startOffset, starts, ends, builder, output);
            }
            builder.setLength(startLen);
        }
    }

    static final class Ranges
    implements Comparable<Ranges> {
        private final Range[] ranges;

        public Ranges(String s2) {
            int[] array = CharSequences.codePoints(s2);
            this.ranges = new Range[array.length];
            for (int i2 = 0; i2 < array.length; ++i2) {
                this.ranges[i2] = new Range(array[i2], array[i2]);
            }
        }

        public boolean merge(int pivot, Ranges other) {
            for (int i2 = this.ranges.length - 1; i2 >= 0; --i2) {
                if (!(i2 == pivot ? this.ranges[i2].max != other.ranges[i2].min - 1 : !this.ranges[i2].equals(other.ranges[i2]))) continue;
                return false;
            }
            this.ranges[pivot].max = other.ranges[pivot].max;
            return true;
        }

        public String start() {
            StringBuilder result = new StringBuilder();
            for (int i2 = 0; i2 < this.ranges.length; ++i2) {
                result.appendCodePoint(this.ranges[i2].min);
            }
            return result.toString();
        }

        public String end(boolean mostCompact) {
            int i2;
            int firstDiff = this.firstDifference();
            if (firstDiff == this.ranges.length) {
                return null;
            }
            StringBuilder result = new StringBuilder();
            int n2 = i2 = mostCompact ? firstDiff : 0;
            while (i2 < this.ranges.length) {
                result.appendCodePoint(this.ranges[i2].max);
                ++i2;
            }
            return result.toString();
        }

        public int firstDifference() {
            for (int i2 = 0; i2 < this.ranges.length; ++i2) {
                if (this.ranges[i2].min == this.ranges[i2].max) continue;
                return i2;
            }
            return this.ranges.length;
        }

        public Integer size() {
            return this.ranges.length;
        }

        @Override
        public int compareTo(Ranges other) {
            int diff = this.ranges.length - other.ranges.length;
            if (diff != 0) {
                return diff;
            }
            for (int i2 = 0; i2 < this.ranges.length; ++i2) {
                diff = this.ranges[i2].compareTo(other.ranges[i2]);
                if (diff == 0) continue;
                return diff;
            }
            return 0;
        }

        public String toString() {
            String start = this.start();
            String end = this.end(false);
            return end == null ? start : start + "~" + end;
        }
    }

    static final class Range
    implements Comparable<Range> {
        int min;
        int max;

        public Range(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public boolean equals(Object obj) {
            return this == obj || obj != null && obj instanceof Range && this.compareTo((Range)obj) == 0;
        }

        @Override
        public int compareTo(Range that) {
            int diff = this.min - that.min;
            if (diff != 0) {
                return diff;
            }
            return this.max - that.max;
        }

        public int hashCode() {
            return this.min * 37 + this.max;
        }

        public String toString() {
            StringBuilder result = new StringBuilder().appendCodePoint(this.min);
            return this.min == this.max ? result.toString() : result.append('~').appendCodePoint(this.max).toString();
        }
    }

    public static interface Adder {
        public void add(String var1, String var2);
    }
}

