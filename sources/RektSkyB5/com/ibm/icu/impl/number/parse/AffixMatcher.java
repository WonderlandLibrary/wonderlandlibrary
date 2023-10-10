/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.number.parse;

import com.ibm.icu.impl.StandardPlural;
import com.ibm.icu.impl.StringSegment;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.impl.number.AffixPatternProvider;
import com.ibm.icu.impl.number.AffixUtils;
import com.ibm.icu.impl.number.PatternStringUtils;
import com.ibm.icu.impl.number.parse.AffixPatternMatcher;
import com.ibm.icu.impl.number.parse.AffixTokenMatcherFactory;
import com.ibm.icu.impl.number.parse.IgnorablesMatcher;
import com.ibm.icu.impl.number.parse.NumberParseMatcher;
import com.ibm.icu.impl.number.parse.NumberParserImpl;
import com.ibm.icu.impl.number.parse.ParsedNumber;
import com.ibm.icu.number.NumberFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AffixMatcher
implements NumberParseMatcher {
    private final AffixPatternMatcher prefix;
    private final AffixPatternMatcher suffix;
    private final int flags;
    public static final Comparator<AffixMatcher> COMPARATOR = new Comparator<AffixMatcher>(){

        @Override
        public int compare(AffixMatcher lhs, AffixMatcher rhs) {
            if (AffixMatcher.length(lhs.prefix) != AffixMatcher.length(rhs.prefix)) {
                return AffixMatcher.length(lhs.prefix) > AffixMatcher.length(rhs.prefix) ? -1 : 1;
            }
            if (AffixMatcher.length(lhs.suffix) != AffixMatcher.length(rhs.suffix)) {
                return AffixMatcher.length(lhs.suffix) > AffixMatcher.length(rhs.suffix) ? -1 : 1;
            }
            if (!lhs.equals(rhs)) {
                return lhs.hashCode() > rhs.hashCode() ? -1 : 1;
            }
            return 0;
        }
    };

    private static boolean isInteresting(AffixPatternProvider patternInfo, IgnorablesMatcher ignorables, int parseFlags) {
        String posPrefixString = patternInfo.getString(256);
        String posSuffixString = patternInfo.getString(0);
        String negPrefixString = null;
        String negSuffixString = null;
        if (patternInfo.hasNegativeSubpattern()) {
            negPrefixString = patternInfo.getString(768);
            negSuffixString = patternInfo.getString(512);
        }
        return 0 != (parseFlags & 0x100) || !AffixUtils.containsOnlySymbolsAndIgnorables(posPrefixString, ignorables.getSet()) || !AffixUtils.containsOnlySymbolsAndIgnorables(posSuffixString, ignorables.getSet()) || !AffixUtils.containsOnlySymbolsAndIgnorables(negPrefixString, ignorables.getSet()) || !AffixUtils.containsOnlySymbolsAndIgnorables(negSuffixString, ignorables.getSet()) || AffixUtils.containsType(posSuffixString, -2) || AffixUtils.containsType(posSuffixString, -1) || AffixUtils.containsType(negSuffixString, -2) || AffixUtils.containsType(negSuffixString, -1);
    }

    public static void createMatchers(AffixPatternProvider patternInfo, NumberParserImpl output, AffixTokenMatcherFactory factory, IgnorablesMatcher ignorables, int parseFlags) {
        if (!AffixMatcher.isInteresting(patternInfo, ignorables, parseFlags)) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        ArrayList<AffixMatcher> matchers = new ArrayList<AffixMatcher>(6);
        boolean includeUnpaired = 0 != (parseFlags & 0x80);
        NumberFormatter.SignDisplay signDisplay = 0 != (parseFlags & 0x400) ? NumberFormatter.SignDisplay.ALWAYS : NumberFormatter.SignDisplay.AUTO;
        AffixPatternMatcher posPrefix = null;
        AffixPatternMatcher posSuffix = null;
        for (int signum = 1; signum >= -1; --signum) {
            PatternStringUtils.patternInfoToStringBuilder(patternInfo, true, signum, signDisplay, StandardPlural.OTHER, false, sb);
            AffixPatternMatcher prefix = AffixPatternMatcher.fromAffixPattern(sb.toString(), factory, parseFlags);
            PatternStringUtils.patternInfoToStringBuilder(patternInfo, false, signum, signDisplay, StandardPlural.OTHER, false, sb);
            AffixPatternMatcher suffix = AffixPatternMatcher.fromAffixPattern(sb.toString(), factory, parseFlags);
            if (signum == 1) {
                posPrefix = prefix;
                posSuffix = suffix;
            } else if (Utility.equals(prefix, posPrefix) && Utility.equals(suffix, posSuffix)) continue;
            int flags = signum == -1 ? 1 : 0;
            matchers.add(AffixMatcher.getInstance(prefix, suffix, flags));
            if (!includeUnpaired || prefix == null || suffix == null) continue;
            if (signum == 1 || !Utility.equals(prefix, posPrefix)) {
                matchers.add(AffixMatcher.getInstance(prefix, null, flags));
            }
            if (signum != 1 && Utility.equals(suffix, posSuffix)) continue;
            matchers.add(AffixMatcher.getInstance(null, suffix, flags));
        }
        Collections.sort(matchers, COMPARATOR);
        output.addMatchers(matchers);
    }

    private static final AffixMatcher getInstance(AffixPatternMatcher prefix, AffixPatternMatcher suffix, int flags) {
        return new AffixMatcher(prefix, suffix, flags);
    }

    private AffixMatcher(AffixPatternMatcher prefix, AffixPatternMatcher suffix, int flags) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.flags = flags;
    }

    @Override
    public boolean match(StringSegment segment, ParsedNumber result) {
        if (!result.seenNumber()) {
            if (result.prefix != null || this.prefix == null) {
                return false;
            }
            int initialOffset = segment.getOffset();
            boolean maybeMore = this.prefix.match(segment, result);
            if (initialOffset != segment.getOffset()) {
                result.prefix = this.prefix.getPattern();
            }
            return maybeMore;
        }
        if (result.suffix != null || this.suffix == null || !AffixMatcher.matched(this.prefix, result.prefix)) {
            return false;
        }
        int initialOffset = segment.getOffset();
        boolean maybeMore = this.suffix.match(segment, result);
        if (initialOffset != segment.getOffset()) {
            result.suffix = this.suffix.getPattern();
        }
        return maybeMore;
    }

    @Override
    public boolean smokeTest(StringSegment segment) {
        return this.prefix != null && this.prefix.smokeTest(segment) || this.suffix != null && this.suffix.smokeTest(segment);
    }

    @Override
    public void postProcess(ParsedNumber result) {
        if (AffixMatcher.matched(this.prefix, result.prefix) && AffixMatcher.matched(this.suffix, result.suffix)) {
            if (result.prefix == null) {
                result.prefix = "";
            }
            if (result.suffix == null) {
                result.suffix = "";
            }
            result.flags |= this.flags;
            if (this.prefix != null) {
                this.prefix.postProcess(result);
            }
            if (this.suffix != null) {
                this.suffix.postProcess(result);
            }
        }
    }

    static boolean matched(AffixPatternMatcher affix, String patternString) {
        return affix == null && patternString == null || affix != null && affix.getPattern().equals(patternString);
    }

    private static int length(AffixPatternMatcher matcher) {
        return matcher == null ? 0 : matcher.getPattern().length();
    }

    public boolean equals(Object _other) {
        if (!(_other instanceof AffixMatcher)) {
            return false;
        }
        AffixMatcher other = (AffixMatcher)_other;
        return Utility.equals(this.prefix, other.prefix) && Utility.equals(this.suffix, other.suffix) && this.flags == other.flags;
    }

    public int hashCode() {
        return Utility.hashCode(this.prefix) ^ Utility.hashCode(this.suffix) ^ this.flags;
    }

    public String toString() {
        boolean isNegative = 0 != (this.flags & 1);
        return "<AffixMatcher" + (isNegative ? ":negative " : " ") + this.prefix + "#" + this.suffix + ">";
    }
}

