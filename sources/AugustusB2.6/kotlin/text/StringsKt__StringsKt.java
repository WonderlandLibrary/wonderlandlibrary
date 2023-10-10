// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.text;

import java.util.TreeSet;
import java.util.SortedSet;
import kotlin.ranges.IntProgression;
import java.util.NoSuchElementException;
import kotlin.ranges.RangesKt___RangesKt;
import kotlin.jvm.internal.Intrinsics;

class StringsKt__StringsKt extends StringsKt__StringNumberConversionsKt
{
    public static final String padEnd(final String $this$padEnd, int length, final char padChar) {
        Intrinsics.checkParameterIsNotNull($this$padEnd, "$this$padEnd");
        final String value = $this$padEnd;
        final int n = 30;
        final char c = ' ';
        length = n;
        final String s = value;
        Intrinsics.checkParameterIsNotNull(value, "$this$padEnd");
        if (length < 0) {
            throw new IllegalArgumentException("Desired length " + length + " is less than zero.");
        }
        CharSequence subSequence;
        if (length <= s.length()) {
            subSequence = s.subSequence(0, s.length());
        }
        else {
            final StringBuilder sb;
            (sb = new StringBuilder(length)).append((CharSequence)s);
            int n2 = 1;
            final int n3 = length - s.length();
            if (n3 > 0) {
                while (true) {
                    sb.append(c);
                    if (n2 == n3) {
                        break;
                    }
                    ++n2;
                }
            }
            subSequence = sb;
        }
        return subSequence.toString();
    }
    
    public static final int getLastIndex(final CharSequence $this$lastIndex) {
        Intrinsics.checkParameterIsNotNull($this$lastIndex, "$this$lastIndex");
        return $this$lastIndex.length() - 1;
    }
    
    public static final int lastIndexOf(CharSequence $this$lastIndexOf, String string, int startIndex, final boolean ignoreCase) {
        Intrinsics.checkParameterIsNotNull($this$lastIndexOf, "$this$lastIndexOf");
        Intrinsics.checkParameterIsNotNull(string, "string");
        if (ignoreCase || !($this$lastIndexOf instanceof String)) {
            final CharSequence charSequence = $this$lastIndexOf;
            final String s = string;
            final int n = startIndex;
            final CharSequence charSequence2 = (CharSequence)0;
            $this$lastIndexOf = (CharSequence)1;
            $this$lastIndexOf = charSequence2;
            startIndex = n;
            string = s;
            $this$lastIndexOf = charSequence;
            final int coerceAtMost = RangesKt___RangesKt.coerceAtMost(startIndex, getLastIndex($this$lastIndexOf));
            final int coerceAtLeast = RangesKt___RangesKt.coerceAtLeast(0, 0);
            final int start = coerceAtMost;
            final IntProgression.Companion companion = IntProgression.Companion;
            startIndex = (int)new IntProgression(start, coerceAtLeast, -1);
            if ($this$lastIndexOf instanceof String && string instanceof String) {
                final int n2 = startIndex;
                startIndex = ((IntProgression)n2).getFirst();
                final int last = ((IntProgression)n2).getLast();
                final int step = ((IntProgression)n2).getStep();
                final int n3 = startIndex;
                final int n4 = last;
                if (step >= 0) {
                    if (n3 > n4) {
                        return -1;
                    }
                }
                else if (n3 < n4) {
                    return -1;
                }
                while (!StringsKt__StringNumberConversionsKt.regionMatches(string, 0, (String)$this$lastIndexOf, startIndex, string.length(), ignoreCase)) {
                    if (startIndex == last) {
                        return -1;
                    }
                    startIndex += step;
                }
                return startIndex;
            }
            final int n5 = startIndex;
            startIndex = ((IntProgression)n5).getFirst();
            final int last2 = ((IntProgression)n5).getLast();
            final int step2 = ((IntProgression)n5).getStep();
            final int n6 = startIndex;
            final int n7 = last2;
            if (step2 >= 0) {
                if (n6 > n7) {
                    return -1;
                }
            }
            else if (n6 < n7) {
                return -1;
            }
            while (true) {
                final String value = string;
                final CharSequence charSequence3 = $this$lastIndexOf;
                final int n8 = startIndex;
                final int length = string.length();
                final int n9 = n8;
                final CharSequence value2 = charSequence3;
                final String s2 = value;
                Intrinsics.checkParameterIsNotNull(value, "$this$regionMatchesImpl");
                Intrinsics.checkParameterIsNotNull(value2, "other");
                boolean b = false;
                Label_0336: {
                    if (n9 < 0 || 0 > s2.length() - length || n9 > value2.length() - length) {
                        b = false;
                    }
                    else {
                        for (int i = 0; i < length; ++i) {
                            if (!CharsKt__CharJVMKt.equals(s2.charAt(i + 0), value2.charAt(n9 + i), ignoreCase)) {
                                b = false;
                                break Label_0336;
                            }
                        }
                        b = true;
                    }
                }
                if (b) {
                    return startIndex;
                }
                if (startIndex == last2) {
                    break;
                }
                startIndex += step2;
            }
            return -1;
        }
        return ((String)$this$lastIndexOf).lastIndexOf(string, startIndex);
    }
    
    public static SortedSet<Character> toSortedSet(final CharSequence $this$toSortedSet) {
        Intrinsics.checkParameterIsNotNull($this$toSortedSet, "$this$toSortedSet");
        return StringsKt___StringsKt.toCollection($this$toSortedSet, new TreeSet<Character>());
    }
}
