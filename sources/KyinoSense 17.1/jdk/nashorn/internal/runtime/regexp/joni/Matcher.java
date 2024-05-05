/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.EncodingHelper;
import jdk.nashorn.internal.runtime.regexp.joni.Option;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import jdk.nashorn.internal.runtime.regexp.joni.Region;
import jdk.nashorn.internal.runtime.regexp.joni.SearchAlgorithm;
import jdk.nashorn.internal.runtime.regexp.joni.encoding.IntHolder;

public abstract class Matcher
extends IntHolder {
    protected final Regex regex;
    protected final char[] chars;
    protected final int str;
    protected final int end;
    protected int msaStart;
    protected int msaOptions;
    protected final Region msaRegion;
    protected int msaBestLen;
    protected int msaBestS;
    protected int msaBegin;
    protected int msaEnd;
    int low;
    int high;

    public Matcher(Regex regex, char[] chars) {
        this(regex, chars, 0, chars.length);
    }

    public Matcher(Regex regex, char[] chars, int p, int end) {
        this.regex = regex;
        this.chars = chars;
        this.str = p;
        this.end = end;
        this.msaRegion = regex.numMem == 0 ? null : new Region(regex.numMem + 1);
    }

    protected abstract int matchAt(int var1, int var2, int var3);

    public final Region getRegion() {
        return this.msaRegion;
    }

    public final int getBegin() {
        return this.msaBegin;
    }

    public final int getEnd() {
        return this.msaEnd;
    }

    protected final void msaInit(int option, int start) {
        this.msaOptions = option;
        this.msaStart = start;
        this.msaBestLen = -1;
    }

    public final int match(int at, int range, int option) {
        this.msaInit(option, at);
        int prev = EncodingHelper.prevCharHead(this.str, at);
        return this.matchAt(range, at, prev);
    }

    private boolean forwardSearchRange(char[] ch, int string, int e, int s, int range, IntHolder lowPrev) {
        int pprev = -1;
        int p = s;
        if (this.regex.dMin > 0) {
            p += this.regex.dMin;
        }
        block4: while ((p = this.regex.searchAlgorithm.search(this.regex, ch, p, e, range)) != -1 && p < range) {
            if (p - this.regex.dMin < s) {
                pprev = p++;
                continue;
            }
            if (this.regex.subAnchor != 0) {
                switch (this.regex.subAnchor) {
                    case 2: {
                        int prev;
                        if (p == string || EncodingHelper.isNewLine(ch, prev = EncodingHelper.prevCharHead(pprev != -1 ? pprev : string, p), e)) break;
                        pprev = p++;
                        continue block4;
                    }
                    case 32: {
                        if (p == e || EncodingHelper.isNewLine(ch, p, e)) break;
                        pprev = p++;
                        continue block4;
                    }
                }
            }
            if (this.regex.dMax == 0) {
                this.low = p;
                if (lowPrev != null) {
                    lowPrev.value = this.low > s ? EncodingHelper.prevCharHead(s, p) : EncodingHelper.prevCharHead(pprev != -1 ? pprev : string, p);
                }
            } else if (this.regex.dMax != Integer.MAX_VALUE) {
                this.low = p - this.regex.dMax;
                if (this.low > s) {
                    this.low = EncodingHelper.rightAdjustCharHeadWithPrev(this.low, lowPrev);
                    if (lowPrev != null && lowPrev.value == -1) {
                        lowPrev.value = EncodingHelper.prevCharHead(pprev != -1 ? pprev : s, this.low);
                    }
                } else if (lowPrev != null) {
                    lowPrev.value = EncodingHelper.prevCharHead(pprev != -1 ? pprev : string, this.low);
                }
            }
            this.high = p - this.regex.dMin;
            return true;
        }
        return false;
    }

    private boolean backwardSearchRange(char[] ch, int string, int e, int s, int range, int adjrange) {
        int r = range;
        r += this.regex.dMin;
        int p = s;
        block4: while ((p = this.regex.searchAlgorithm.searchBackward(this.regex, ch, r, adjrange, e, p, s, r)) != -1) {
            if (this.regex.subAnchor != 0) {
                switch (this.regex.subAnchor) {
                    case 2: {
                        int prev;
                        if (p == string || EncodingHelper.isNewLine(ch, prev = EncodingHelper.prevCharHead(string, p), e)) break;
                        p = prev;
                        continue block4;
                    }
                    case 32: {
                        if (p == e || EncodingHelper.isNewLine(ch, p, e)) break;
                        if ((p = EncodingHelper.prevCharHead(adjrange, p)) != -1) continue block4;
                        return false;
                    }
                }
            }
            if (this.regex.dMax != Integer.MAX_VALUE) {
                this.low = p - this.regex.dMax;
                this.high = p - this.regex.dMin;
            }
            return true;
        }
        return false;
    }

    private boolean matchCheck(int upperRange, int s, int prev) {
        return this.matchAt(this.end, s, prev) != -1 && !Option.isFindLongest(this.regex.options);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final int search(int startp, int rangep, int option) {
        int prev;
        int start = startp;
        int range = rangep;
        int origStart = start;
        int origRange = range;
        if (start > this.end || start < this.str) {
            return -1;
        }
        if (this.regex.anchor != 0 && this.str < this.end) {
            int minSemiEnd;
            int maxSemiEnd;
            if ((this.regex.anchor & 4) != 0) {
                range = range > start ? start + 1 : start;
            } else if ((this.regex.anchor & 1) != 0) {
                if (range > start) {
                    if (start != this.str) {
                        return -1;
                    }
                    range = this.str + 1;
                } else {
                    if (range > this.str) return -1;
                    start = this.str;
                    range = this.str;
                }
            } else if ((this.regex.anchor & 8) != 0) {
                maxSemiEnd = this.end;
                minSemiEnd = maxSemiEnd;
                if (this.endBuf(start, range, minSemiEnd, maxSemiEnd)) {
                    return -1;
                }
            } else if ((this.regex.anchor & 0x10) != 0) {
                int preEnd = EncodingHelper.stepBack(this.str, this.end, 1);
                maxSemiEnd = this.end;
                if (EncodingHelper.isNewLine(this.chars, preEnd, this.end) ? (minSemiEnd = preEnd) > this.str && start <= minSemiEnd && this.endBuf(start, range, minSemiEnd, maxSemiEnd) : this.endBuf(start, range, minSemiEnd = this.end, maxSemiEnd)) {
                    return -1;
                }
            } else if ((this.regex.anchor & 0x8000) != 0) {
                range = range > start ? start + 1 : start;
            }
        } else if (this.str == this.end) {
            if (this.regex.thresholdLength != 0) return -1;
            int s = start = this.str;
            int prev2 = -1;
            this.msaInit(option, start);
            if (!this.matchCheck(this.end, s, prev2)) return this.mismatch();
            return this.match(s);
        }
        this.msaInit(option, origStart);
        int s = start;
        if (range > start) {
            int prev3 = s > this.str ? EncodingHelper.prevCharHead(this.str, s) : 0;
            if (this.regex.searchAlgorithm != SearchAlgorithm.NONE) {
                int schRange = range;
                if (this.regex.dMax != 0) {
                    if (this.regex.dMax == Integer.MAX_VALUE) {
                        schRange = this.end;
                    } else if ((schRange += this.regex.dMax) > this.end) {
                        schRange = this.end;
                    }
                }
                if (this.end - start < this.regex.thresholdLength) {
                    return this.mismatch();
                }
                if (this.regex.dMax != Integer.MAX_VALUE) {
                    do {
                        if (!this.forwardSearchRange(this.chars, this.str, this.end, s, schRange, this)) {
                            return this.mismatch();
                        }
                        if (s < this.low) {
                            s = this.low;
                            prev3 = this.value;
                        }
                        while (s <= this.high) {
                            if (this.matchCheck(origRange, s, prev3)) {
                                return this.match(s);
                            }
                            prev3 = s++;
                        }
                    } while (s < range);
                }
                if (!this.forwardSearchRange(this.chars, this.str, this.end, s, schRange, null)) {
                    return this.mismatch();
                }
                if ((this.regex.anchor & 0x4000) != 0) {
                    do {
                        if (this.matchCheck(origRange, s, prev3)) {
                            return this.match(s);
                        }
                        prev3 = s++;
                    } while (s < range);
                    return this.mismatch();
                }
            }
            do {
                if (this.matchCheck(origRange, s, prev3)) {
                    return this.match(s);
                }
                prev3 = s++;
            } while (s < range);
            if (s != range || !this.matchCheck(origRange, s, prev3)) return this.mismatch();
            return this.match(s);
        }
        if (this.regex.searchAlgorithm != SearchAlgorithm.NONE) {
            int schStart;
            int adjrange = range < this.end ? range : this.end;
            if (this.regex.dMax != Integer.MAX_VALUE && this.end - range >= this.regex.thresholdLength) {
                do {
                    if ((schStart = s + this.regex.dMax) > this.end) {
                        schStart = this.end;
                    }
                    if (!this.backwardSearchRange(this.chars, this.str, this.end, schStart, range, adjrange)) {
                        return this.mismatch();
                    }
                    if (s > this.high) {
                        s = this.high;
                    }
                    while (s != -1 && s >= this.low) {
                        int prev4 = EncodingHelper.prevCharHead(this.str, s);
                        if (this.matchCheck(origStart, s, prev4)) {
                            return this.match(s);
                        }
                        s = prev4;
                    }
                } while (s >= range);
                return this.mismatch();
            }
            if (this.end - range < this.regex.thresholdLength) {
                return this.mismatch();
            }
            schStart = s;
            if (this.regex.dMax != 0) {
                if (this.regex.dMax == Integer.MAX_VALUE) {
                    schStart = this.end;
                } else if ((schStart += this.regex.dMax) > this.end) {
                    schStart = this.end;
                }
            }
            if (!this.backwardSearchRange(this.chars, this.str, this.end, schStart, range, adjrange)) {
                return this.mismatch();
            }
        }
        do {
            if (!this.matchCheck(origStart, s, prev = EncodingHelper.prevCharHead(this.str, s))) continue;
            return this.match(s);
        } while ((s = prev) >= range);
        return this.mismatch();
    }

    private boolean endBuf(int startp, int rangep, int minSemiEnd, int maxSemiEnd) {
        int start = startp;
        int range = rangep;
        if (maxSemiEnd - this.str < this.regex.anchorDmin) {
            return true;
        }
        if (range > start) {
            if (minSemiEnd - start > this.regex.anchorDmax && (start = minSemiEnd - this.regex.anchorDmax) >= this.end) {
                start = EncodingHelper.prevCharHead(this.str, this.end);
            }
            if (maxSemiEnd - (range - 1) < this.regex.anchorDmin) {
                range = maxSemiEnd - this.regex.anchorDmin + 1;
            }
            if (start >= range) {
                return true;
            }
        } else {
            if (minSemiEnd - range > this.regex.anchorDmax) {
                range = minSemiEnd - this.regex.anchorDmax;
            }
            if (maxSemiEnd - start < this.regex.anchorDmin) {
                start = maxSemiEnd - this.regex.anchorDmin;
            }
            if (range > start) {
                return true;
            }
        }
        return false;
    }

    private int match(int s) {
        return s - this.str;
    }

    private int mismatch() {
        if (this.msaBestLen >= 0) {
            int s = this.msaBestS;
            return this.match(s);
        }
        return -1;
    }
}

