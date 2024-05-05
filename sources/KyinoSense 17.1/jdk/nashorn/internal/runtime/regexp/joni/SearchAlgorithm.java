/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

public abstract class SearchAlgorithm {
    public static final SearchAlgorithm NONE = new SearchAlgorithm(){

        @Override
        public final String getName() {
            return "NONE";
        }

        @Override
        public final int search(Regex regex, char[] text, int textP, int textEnd, int textRange) {
            return textP;
        }

        @Override
        public final int searchBackward(Regex regex, char[] text, int textP, int adjustText, int textEnd, int textStart, int s_, int range_) {
            return textP;
        }
    };
    public static final SearchAlgorithm SLOW = new SearchAlgorithm(){

        @Override
        public final String getName() {
            return "EXACT";
        }

        @Override
        public final int search(Regex regex, char[] text, int textP, int textEnd, int textRange) {
            char[] target = regex.exact;
            int targetP = regex.exactP;
            int targetEnd = regex.exactEnd;
            int end = textEnd;
            if ((end -= targetEnd - targetP - 1) > textRange) {
                end = textRange;
            }
            for (int s = textP; s < end; ++s) {
                int t;
                if (text[s] != target[targetP]) continue;
                int p = s + 1;
                for (t = targetP + 1; t < targetEnd && target[t] == text[p++]; ++t) {
                }
                if (t != targetEnd) continue;
                return s;
            }
            return -1;
        }

        @Override
        public final int searchBackward(Regex regex, char[] text, int textP, int adjustText, int textEnd, int textStart, int s_, int range_) {
            char[] target = regex.exact;
            int targetP = regex.exactP;
            int targetEnd = regex.exactEnd;
            int s = textEnd;
            if ((s -= targetEnd - targetP) > textStart) {
                s = textStart;
            }
            while (s >= textP) {
                if (text[s] == target[targetP]) {
                    int t;
                    int p = s + 1;
                    for (t = targetP + 1; t < targetEnd && target[t] == text[p++]; ++t) {
                    }
                    if (t == targetEnd) {
                        return s;
                    }
                }
                --s;
            }
            return -1;
        }
    };
    public static final SearchAlgorithm BM = new SearchAlgorithm(){
        private static final int BM_BACKWARD_SEARCH_LENGTH_THRESHOLD = 100;

        @Override
        public final String getName() {
            return "EXACT_BM";
        }

        @Override
        public final int search(Regex regex, char[] text, int textP, int textEnd, int textRange) {
            int s;
            char[] target = regex.exact;
            int targetEnd = regex.exactEnd;
            int targetP = regex.exactP;
            int end = textRange + (targetEnd - targetP) - 1;
            if (end > textEnd) {
                end = textEnd;
            }
            int tail = targetEnd - 1;
            if (regex.intMap == null) {
                for (s = textP + (targetEnd - targetP) - 1; s < end; s += regex.map[text[s] & 0xFF]) {
                    int p = s;
                    int t = tail;
                    while (text[p] == target[t]) {
                        if (t == targetP) {
                            return p;
                        }
                        --p;
                        --t;
                    }
                }
            } else {
                while (s < end) {
                    int p = s;
                    int t = tail;
                    while (text[p] == target[t]) {
                        if (t == targetP) {
                            return p;
                        }
                        --p;
                        --t;
                    }
                    s += regex.intMap[text[s] & 0xFF];
                }
            }
            return -1;
        }

        @Override
        public final int searchBackward(Regex regex, char[] text, int textP, int adjustText, int textEnd, int textStart, int s_, int range_) {
            int s;
            char[] target = regex.exact;
            int targetP = regex.exactP;
            int targetEnd = regex.exactEnd;
            if (regex.intMapBackward == null) {
                if (s_ - range_ < 100) {
                    return SLOW.searchBackward(regex, text, textP, adjustText, textEnd, textStart, s_, range_);
                }
                this.setBmBackwardSkip(regex, target, targetP, targetEnd);
            }
            if (textStart < (s = textEnd - (targetEnd - targetP))) {
                s = textStart;
            }
            while (s >= textP) {
                int t;
                int p = s;
                for (t = targetP; t < targetEnd && text[p] == target[t]; ++t) {
                    ++p;
                }
                if (t == targetEnd) {
                    return s;
                }
                s -= regex.intMapBackward[text[s] & 0xFF];
            }
            return -1;
        }

        private void setBmBackwardSkip(Regex regex, char[] chars, int p, int end) {
            int i;
            int[] skip;
            if (regex.intMapBackward == null) {
                skip = new int[256];
                regex.intMapBackward = skip;
            } else {
                skip = regex.intMapBackward;
            }
            int len = end - p;
            for (i = 0; i < 256; ++i) {
                skip[i] = len;
            }
            for (i = len - 1; i > 0; --i) {
                skip[chars[i] & 0xFF] = i;
            }
        }
    };
    public static final SearchAlgorithm MAP = new SearchAlgorithm(){

        @Override
        public final String getName() {
            return "MAP";
        }

        @Override
        public final int search(Regex regex, char[] text, int textP, int textEnd, int textRange) {
            byte[] map = regex.map;
            for (int s = textP; s < textRange; ++s) {
                if (text[s] <= '\u00ff' && map[text[s]] == 0) continue;
                return s;
            }
            return -1;
        }

        @Override
        public final int searchBackward(Regex regex, char[] text, int textP, int adjustText, int textEnd, int textStart, int s_, int range_) {
            byte[] map = regex.map;
            int s = textStart;
            if (s >= textEnd) {
                s = textEnd - 1;
            }
            while (s >= textP) {
                if (text[s] > '\u00ff' || map[text[s]] != 0) {
                    return s;
                }
                --s;
            }
            return -1;
        }
    };

    public abstract String getName();

    public abstract int search(Regex var1, char[] var2, int var3, int var4, int var5);

    public abstract int searchBackward(Regex var1, char[] var2, int var3, int var4, int var5, int var6, int var7, int var8);

    public static final class SLOW_IC
    extends SearchAlgorithm {
        public SLOW_IC(Regex regex) {
        }

        @Override
        public final String getName() {
            return "EXACT_IC";
        }

        @Override
        public final int search(Regex regex, char[] text, int textP, int textEnd, int textRange) {
            char[] target = regex.exact;
            int targetP = regex.exactP;
            int targetEnd = regex.exactEnd;
            int end = textEnd;
            if ((end -= targetEnd - targetP - 1) > textRange) {
                end = textRange;
            }
            for (int s = textP; s < end; ++s) {
                if (!SLOW_IC.lowerCaseMatch(target, targetP, targetEnd, text, s, textEnd)) continue;
                return s;
            }
            return -1;
        }

        @Override
        public final int searchBackward(Regex regex, char[] text, int textP, int adjustText, int textEnd, int textStart, int s_, int range_) {
            char[] target = regex.exact;
            int targetP = regex.exactP;
            int targetEnd = regex.exactEnd;
            int s = textEnd;
            if ((s -= targetEnd - targetP) > textStart) {
                s = textStart;
            }
            while (s >= textP) {
                if (SLOW_IC.lowerCaseMatch(target, targetP, targetEnd, text, s, textEnd)) {
                    return s;
                }
                s = EncodingHelper.prevCharHead(adjustText, s);
            }
            return -1;
        }

        private static boolean lowerCaseMatch(char[] t, int tPp, int tEnd, char[] chars, int pp, int end) {
            int tP = tPp;
            int p = pp;
            while (tP < tEnd) {
                if (t[tP++] == EncodingHelper.toLowerCase(chars[p++])) continue;
                return false;
            }
            return true;
        }
    }
}

