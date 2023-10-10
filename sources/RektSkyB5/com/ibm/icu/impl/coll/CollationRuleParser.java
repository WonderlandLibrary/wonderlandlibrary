/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.coll;

import com.ibm.icu.impl.IllegalIcuArgumentException;
import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.coll.CollationData;
import com.ibm.icu.impl.coll.CollationSettings;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.ULocale;
import java.text.ParseException;
import java.util.ArrayList;

public final class CollationRuleParser {
    static final Position[] POSITION_VALUES = Position.values();
    static final char POS_LEAD = '\ufffe';
    static final char POS_BASE = '\u2800';
    private static final int UCOL_DEFAULT = -1;
    private static final int UCOL_OFF = 0;
    private static final int UCOL_ON = 1;
    private static final int STRENGTH_MASK = 15;
    private static final int STARRED_FLAG = 16;
    private static final int OFFSET_SHIFT = 8;
    private static final String BEFORE = "[before";
    private final StringBuilder rawBuilder = new StringBuilder();
    private static final String[] positions = new String[]{"first tertiary ignorable", "last tertiary ignorable", "first secondary ignorable", "last secondary ignorable", "first primary ignorable", "last primary ignorable", "first variable", "last variable", "first regular", "last regular", "first implicit", "last implicit", "first trailing", "last trailing"};
    private static final String[] gSpecialReorderCodes = new String[]{"space", "punct", "symbol", "currency", "digit"};
    private static final int U_PARSE_CONTEXT_LEN = 16;
    private Normalizer2 nfd = Normalizer2.getNFDInstance();
    private Normalizer2 nfc = Normalizer2.getNFCInstance();
    private String rules;
    private final CollationData baseData;
    private CollationSettings settings;
    private Sink sink;
    private Importer importer;
    private int ruleIndex;

    CollationRuleParser(CollationData base) {
        this.baseData = base;
    }

    void setSink(Sink sinkAlias) {
        this.sink = sinkAlias;
    }

    void setImporter(Importer importerAlias) {
        this.importer = importerAlias;
    }

    void parse(String ruleString, CollationSettings outSettings) throws ParseException {
        this.settings = outSettings;
        this.parse(ruleString);
    }

    private void parse(String ruleString) throws ParseException {
        this.rules = ruleString;
        this.ruleIndex = 0;
        block7: while (this.ruleIndex < this.rules.length()) {
            char c2 = this.rules.charAt(this.ruleIndex);
            if (PatternProps.isWhiteSpace(c2)) {
                ++this.ruleIndex;
                continue;
            }
            switch (c2) {
                case '&': {
                    this.parseRuleChain();
                    continue block7;
                }
                case '[': {
                    this.parseSetting();
                    continue block7;
                }
                case '#': {
                    this.ruleIndex = this.skipComment(this.ruleIndex + 1);
                    continue block7;
                }
                case '@': {
                    this.settings.setFlag(2048, true);
                    ++this.ruleIndex;
                    continue block7;
                }
                case '!': {
                    ++this.ruleIndex;
                    continue block7;
                }
            }
            this.setParseError("expected a reset or setting or comment");
        }
    }

    private void parseRuleChain() throws ParseException {
        int resetStrength = this.parseResetAndPosition();
        boolean isFirstRelation = true;
        while (true) {
            int result;
            if ((result = this.parseRelationOperator()) < 0) {
                if (this.ruleIndex < this.rules.length() && this.rules.charAt(this.ruleIndex) == '#') {
                    this.ruleIndex = this.skipComment(this.ruleIndex + 1);
                    continue;
                }
                if (isFirstRelation) {
                    this.setParseError("reset not followed by a relation");
                }
                return;
            }
            int strength = result & 0xF;
            if (resetStrength < 15) {
                if (isFirstRelation) {
                    if (strength != resetStrength) {
                        this.setParseError("reset-before strength differs from its first relation");
                        return;
                    }
                } else if (strength < resetStrength) {
                    this.setParseError("reset-before strength followed by a stronger relation");
                    return;
                }
            }
            int i2 = this.ruleIndex + (result >> 8);
            if ((result & 0x10) == 0) {
                this.parseRelationStrings(strength, i2);
            } else {
                this.parseStarredCharacters(strength, i2);
            }
            isFirstRelation = false;
        }
    }

    private int parseResetAndPosition() throws ParseException {
        int resetStrength;
        char c2;
        int j2;
        int i2 = this.skipWhiteSpace(this.ruleIndex + 1);
        if (this.rules.regionMatches(i2, BEFORE, 0, BEFORE.length()) && (j2 = i2 + BEFORE.length()) < this.rules.length() && PatternProps.isWhiteSpace(this.rules.charAt(j2)) && (j2 = this.skipWhiteSpace(j2 + 1)) + 1 < this.rules.length() && '1' <= (c2 = this.rules.charAt(j2)) && c2 <= '3' && this.rules.charAt(j2 + 1) == ']') {
            resetStrength = 0 + (c2 - 49);
            i2 = this.skipWhiteSpace(j2 + 2);
        } else {
            resetStrength = 15;
        }
        if (i2 >= this.rules.length()) {
            this.setParseError("reset without position");
            return -1;
        }
        i2 = this.rules.charAt(i2) == '[' ? this.parseSpecialPosition(i2, this.rawBuilder) : this.parseTailoringString(i2, this.rawBuilder);
        try {
            this.sink.addReset(resetStrength, this.rawBuilder);
        }
        catch (Exception e2) {
            this.setParseError("adding reset failed", e2);
            return -1;
        }
        this.ruleIndex = i2;
        return resetStrength;
    }

    private int parseRelationOperator() {
        int strength;
        this.ruleIndex = this.skipWhiteSpace(this.ruleIndex);
        if (this.ruleIndex >= this.rules.length()) {
            return -1;
        }
        int i2 = this.ruleIndex;
        char c2 = this.rules.charAt(i2++);
        switch (c2) {
            case '<': {
                if (i2 < this.rules.length() && this.rules.charAt(i2) == '<') {
                    if (++i2 < this.rules.length() && this.rules.charAt(i2) == '<') {
                        if (++i2 < this.rules.length() && this.rules.charAt(i2) == '<') {
                            ++i2;
                            strength = 3;
                        } else {
                            strength = 2;
                        }
                    } else {
                        strength = 1;
                    }
                } else {
                    strength = 0;
                }
                if (i2 >= this.rules.length() || this.rules.charAt(i2) != '*') break;
                ++i2;
                strength |= 0x10;
                break;
            }
            case ';': {
                strength = 1;
                break;
            }
            case ',': {
                strength = 2;
                break;
            }
            case '=': {
                strength = 15;
                if (i2 >= this.rules.length() || this.rules.charAt(i2) != '*') break;
                ++i2;
                strength |= 0x10;
                break;
            }
            default: {
                return -1;
            }
        }
        return i2 - this.ruleIndex << 8 | strength;
    }

    private void parseRelationStrings(int strength, int i2) throws ParseException {
        char next;
        String prefix = "";
        CharSequence extension = "";
        char c2 = next = (i2 = this.parseTailoringString(i2, this.rawBuilder)) < this.rules.length() ? this.rules.charAt(i2) : (char)'\u0000';
        if (next == '|') {
            prefix = this.rawBuilder.toString();
            char c3 = next = (i2 = this.parseTailoringString(i2 + 1, this.rawBuilder)) < this.rules.length() ? this.rules.charAt(i2) : (char)'\u0000';
        }
        if (next == '/') {
            StringBuilder extBuilder = new StringBuilder();
            i2 = this.parseTailoringString(i2 + 1, extBuilder);
            extension = extBuilder;
        }
        if (prefix.length() != 0) {
            int prefix0 = prefix.codePointAt(0);
            int c4 = this.rawBuilder.codePointAt(0);
            if (!this.nfc.hasBoundaryBefore(prefix0) || !this.nfc.hasBoundaryBefore(c4)) {
                this.setParseError("in 'prefix|str', prefix and str must each start with an NFC boundary");
                return;
            }
        }
        try {
            this.sink.addRelation(strength, prefix, this.rawBuilder, extension);
        }
        catch (Exception e2) {
            this.setParseError("adding relation failed", e2);
            return;
        }
        this.ruleIndex = i2;
    }

    private void parseStarredCharacters(int strength, int i2) throws ParseException {
        String empty = "";
        i2 = this.parseString(this.skipWhiteSpace(i2), this.rawBuilder);
        if (this.rawBuilder.length() == 0) {
            this.setParseError("missing starred-relation string");
            return;
        }
        int prev = -1;
        int j2 = 0;
        while (true) {
            int c2;
            if (j2 < this.rawBuilder.length()) {
                c2 = this.rawBuilder.codePointAt(j2);
                if (!this.nfd.isInert(c2)) {
                    this.setParseError("starred-relation string is not all NFD-inert");
                    return;
                }
                try {
                    this.sink.addRelation(strength, empty, UTF16.valueOf(c2), empty);
                }
                catch (Exception e2) {
                    this.setParseError("adding relation failed", e2);
                    return;
                }
                j2 += Character.charCount(c2);
                prev = c2;
                continue;
            }
            if (i2 >= this.rules.length() || this.rules.charAt(i2) != '-') break;
            if (prev < 0) {
                this.setParseError("range without start in starred-relation string");
                return;
            }
            i2 = this.parseString(i2 + 1, this.rawBuilder);
            if (this.rawBuilder.length() == 0) {
                this.setParseError("range without end in starred-relation string");
                return;
            }
            c2 = this.rawBuilder.codePointAt(0);
            if (c2 < prev) {
                this.setParseError("range start greater than end in starred-relation string");
                return;
            }
            while (++prev <= c2) {
                if (!this.nfd.isInert(prev)) {
                    this.setParseError("starred-relation string range is not all NFD-inert");
                    return;
                }
                if (CollationRuleParser.isSurrogate(prev)) {
                    this.setParseError("starred-relation string range contains a surrogate");
                    return;
                }
                if (65533 <= prev && prev <= 65535) {
                    this.setParseError("starred-relation string range contains U+FFFD, U+FFFE or U+FFFF");
                    return;
                }
                try {
                    this.sink.addRelation(strength, empty, UTF16.valueOf(prev), empty);
                }
                catch (Exception e3) {
                    this.setParseError("adding relation failed", e3);
                    return;
                }
            }
            prev = -1;
            j2 = Character.charCount(c2);
        }
        this.ruleIndex = this.skipWhiteSpace(i2);
    }

    private int parseTailoringString(int i2, StringBuilder raw) throws ParseException {
        i2 = this.parseString(this.skipWhiteSpace(i2), raw);
        if (raw.length() == 0) {
            this.setParseError("missing relation string");
        }
        return this.skipWhiteSpace(i2);
    }

    private int parseString(int i2, StringBuilder raw) throws ParseException {
        int c2;
        raw.setLength(0);
        block0: while (i2 < this.rules.length()) {
            char c3;
            if (CollationRuleParser.isSyntaxChar(c3 = this.rules.charAt(i2++))) {
                if (c3 == '\'') {
                    if (i2 < this.rules.length() && this.rules.charAt(i2) == '\'') {
                        raw.append('\'');
                        ++i2;
                        continue;
                    }
                    while (true) {
                        if (i2 == this.rules.length()) {
                            this.setParseError("quoted literal text missing terminating apostrophe");
                            return i2;
                        }
                        if ((c3 = this.rules.charAt(i2++)) == '\'') {
                            if (i2 >= this.rules.length() || this.rules.charAt(i2) != '\'') continue block0;
                            ++i2;
                        }
                        raw.append(c3);
                    }
                }
                if (c3 == '\\') {
                    if (i2 == this.rules.length()) {
                        this.setParseError("backslash escape at the end of the rule string");
                        return i2;
                    }
                    int cp = this.rules.codePointAt(i2);
                    raw.appendCodePoint(cp);
                    i2 += Character.charCount(cp);
                    continue;
                }
                --i2;
                break;
            }
            if (PatternProps.isWhiteSpace(c3)) {
                --i2;
                break;
            }
            raw.append(c3);
        }
        for (int j2 = 0; j2 < raw.length(); j2 += Character.charCount(c2)) {
            c2 = raw.codePointAt(j2);
            if (CollationRuleParser.isSurrogate(c2)) {
                this.setParseError("string contains an unpaired surrogate");
                return i2;
            }
            if (65533 > c2 || c2 > 65535) continue;
            this.setParseError("string contains U+FFFD, U+FFFE or U+FFFF");
            return i2;
        }
        return i2;
    }

    private static final boolean isSurrogate(int c2) {
        return (c2 & 0xFFFFF800) == 55296;
    }

    private int parseSpecialPosition(int i2, StringBuilder str) throws ParseException {
        int j2 = this.readWords(i2 + 1, this.rawBuilder);
        if (j2 > i2 && this.rules.charAt(j2) == ']' && this.rawBuilder.length() != 0) {
            ++j2;
            String raw = this.rawBuilder.toString();
            str.setLength(0);
            for (int pos = 0; pos < positions.length; ++pos) {
                if (!raw.equals(positions[pos])) continue;
                str.append('\ufffe').append((char)(10240 + pos));
                return j2;
            }
            if (raw.equals("top")) {
                str.append('\ufffe').append((char)(10240 + Position.LAST_REGULAR.ordinal()));
                return j2;
            }
            if (raw.equals("variable top")) {
                str.append('\ufffe').append((char)(10240 + Position.LAST_VARIABLE.ordinal()));
                return j2;
            }
        }
        this.setParseError("not a valid special reset position");
        return i2;
    }

    private void parseSetting() throws ParseException {
        int i2 = this.ruleIndex + 1;
        int j2 = this.readWords(i2, this.rawBuilder);
        if (j2 <= i2 || this.rawBuilder.length() == 0) {
            this.setParseError("expected a setting/option at '['");
        }
        String raw = this.rawBuilder.toString();
        if (this.rules.charAt(j2) == ']') {
            String v2;
            ++j2;
            if (raw.startsWith("reorder") && (raw.length() == 7 || raw.charAt(7) == ' ')) {
                this.parseReordering(raw);
                this.ruleIndex = j2;
                return;
            }
            if (raw.equals("backwards 2")) {
                this.settings.setFlag(2048, true);
                this.ruleIndex = j2;
                return;
            }
            int valueIndex = raw.lastIndexOf(32);
            if (valueIndex >= 0) {
                v2 = raw.substring(valueIndex + 1);
                raw = raw.substring(0, valueIndex);
            } else {
                v2 = "";
            }
            if (raw.equals("strength") && v2.length() == 1) {
                int value = -1;
                char c2 = v2.charAt(0);
                if ('1' <= c2 && c2 <= '4') {
                    value = 0 + (c2 - 49);
                } else if (c2 == 'I') {
                    value = 15;
                }
                if (value != -1) {
                    this.settings.setStrength(value);
                    this.ruleIndex = j2;
                    return;
                }
            } else if (raw.equals("alternate")) {
                int value = -1;
                if (v2.equals("non-ignorable")) {
                    value = 0;
                } else if (v2.equals("shifted")) {
                    value = 1;
                }
                if (value != -1) {
                    this.settings.setAlternateHandlingShifted(value > 0);
                    this.ruleIndex = j2;
                    return;
                }
            } else if (raw.equals("maxVariable")) {
                int value = -1;
                if (v2.equals("space")) {
                    value = 0;
                } else if (v2.equals("punct")) {
                    value = 1;
                } else if (v2.equals("symbol")) {
                    value = 2;
                } else if (v2.equals("currency")) {
                    value = 3;
                }
                if (value != -1) {
                    this.settings.setMaxVariable(value, 0);
                    this.settings.variableTop = this.baseData.getLastPrimaryForGroup(4096 + value);
                    assert (this.settings.variableTop != 0L);
                    this.ruleIndex = j2;
                    return;
                }
            } else if (raw.equals("caseFirst")) {
                int value = -1;
                if (v2.equals("off")) {
                    value = 0;
                } else if (v2.equals("lower")) {
                    value = 512;
                } else if (v2.equals("upper")) {
                    value = 768;
                }
                if (value != -1) {
                    this.settings.setCaseFirst(value);
                    this.ruleIndex = j2;
                    return;
                }
            } else if (raw.equals("caseLevel")) {
                int value = CollationRuleParser.getOnOffValue(v2);
                if (value != -1) {
                    this.settings.setFlag(1024, value > 0);
                    this.ruleIndex = j2;
                    return;
                }
            } else if (raw.equals("normalization")) {
                int value = CollationRuleParser.getOnOffValue(v2);
                if (value != -1) {
                    this.settings.setFlag(1, value > 0);
                    this.ruleIndex = j2;
                    return;
                }
            } else if (raw.equals("numericOrdering")) {
                int value = CollationRuleParser.getOnOffValue(v2);
                if (value != -1) {
                    this.settings.setFlag(2, value > 0);
                    this.ruleIndex = j2;
                    return;
                }
            } else if (raw.equals("hiraganaQ")) {
                int value = CollationRuleParser.getOnOffValue(v2);
                if (value != -1) {
                    if (value == 1) {
                        this.setParseError("[hiraganaQ on] is not supported");
                    }
                    this.ruleIndex = j2;
                    return;
                }
            } else if (raw.equals("import")) {
                ULocale localeID;
                try {
                    localeID = new ULocale.Builder().setLanguageTag(v2).build();
                }
                catch (Exception e2) {
                    this.setParseError("expected language tag in [import langTag]", e2);
                    return;
                }
                String baseID = localeID.getBaseName();
                String collationType = localeID.getKeywordValue("collation");
                if (this.importer == null) {
                    this.setParseError("[import langTag] is not supported");
                } else {
                    String importedRules;
                    try {
                        importedRules = this.importer.getRules(baseID, collationType != null ? collationType : "standard");
                    }
                    catch (Exception e3) {
                        this.setParseError("[import langTag] failed", e3);
                        return;
                    }
                    String outerRules = this.rules;
                    int outerRuleIndex = this.ruleIndex;
                    try {
                        this.parse(importedRules);
                    }
                    catch (Exception e4) {
                        this.ruleIndex = outerRuleIndex;
                        this.setParseError("parsing imported rules failed", e4);
                    }
                    this.rules = outerRules;
                    this.ruleIndex = j2;
                }
                return;
            }
        } else if (this.rules.charAt(j2) == '[') {
            UnicodeSet set = new UnicodeSet();
            j2 = this.parseUnicodeSet(j2, set);
            if (raw.equals("optimize")) {
                try {
                    this.sink.optimize(set);
                }
                catch (Exception e5) {
                    this.setParseError("[optimize set] failed", e5);
                }
                this.ruleIndex = j2;
                return;
            }
            if (raw.equals("suppressContractions")) {
                try {
                    this.sink.suppressContractions(set);
                }
                catch (Exception e6) {
                    this.setParseError("[suppressContractions set] failed", e6);
                }
                this.ruleIndex = j2;
                return;
            }
        }
        this.setParseError("not a valid setting/option");
    }

    private void parseReordering(CharSequence raw) throws ParseException {
        int i2 = 7;
        if (i2 == raw.length()) {
            this.settings.resetReordering();
            return;
        }
        ArrayList<Integer> reorderCodes = new ArrayList<Integer>();
        while (i2 < raw.length()) {
            int limit;
            for (limit = ++i2; limit < raw.length() && raw.charAt(limit) != ' '; ++limit) {
            }
            String word = raw.subSequence(i2, limit).toString();
            int code = CollationRuleParser.getReorderCode(word);
            if (code < 0) {
                this.setParseError("unknown script or reorder code");
                return;
            }
            reorderCodes.add(code);
            i2 = limit;
        }
        if (reorderCodes.isEmpty()) {
            this.settings.resetReordering();
        } else {
            int[] codes = new int[reorderCodes.size()];
            int j2 = 0;
            for (Integer code : reorderCodes) {
                codes[j2++] = code;
            }
            this.settings.setReordering(this.baseData, codes);
        }
    }

    public static int getReorderCode(String word) {
        for (int i2 = 0; i2 < gSpecialReorderCodes.length; ++i2) {
            if (!word.equalsIgnoreCase(gSpecialReorderCodes[i2])) continue;
            return 4096 + i2;
        }
        try {
            int script = UCharacter.getPropertyValueEnum(4106, word);
            if (script >= 0) {
                return script;
            }
        }
        catch (IllegalIcuArgumentException illegalIcuArgumentException) {
            // empty catch block
        }
        if (word.equalsIgnoreCase("others")) {
            return 103;
        }
        return -1;
    }

    private static int getOnOffValue(String s2) {
        if (s2.equals("on")) {
            return 1;
        }
        if (s2.equals("off")) {
            return 0;
        }
        return -1;
    }

    private int parseUnicodeSet(int i2, UnicodeSet set) throws ParseException {
        int level = 0;
        int j2 = i2;
        while (true) {
            char c2;
            if (j2 == this.rules.length()) {
                this.setParseError("unbalanced UnicodeSet pattern brackets");
                return j2;
            }
            if ((c2 = this.rules.charAt(j2++)) == '[') {
                ++level;
                continue;
            }
            if (c2 == ']' && --level == 0) break;
        }
        try {
            set.applyPattern(this.rules.substring(i2, j2));
        }
        catch (Exception e2) {
            this.setParseError("not a valid UnicodeSet pattern: " + e2.getMessage());
        }
        j2 = this.skipWhiteSpace(j2);
        if (j2 == this.rules.length() || this.rules.charAt(j2) != ']') {
            this.setParseError("missing option-terminating ']' after UnicodeSet pattern");
            return j2;
        }
        return ++j2;
    }

    private int readWords(int i2, StringBuilder raw) {
        raw.setLength(0);
        i2 = this.skipWhiteSpace(i2);
        while (i2 < this.rules.length()) {
            char c2 = this.rules.charAt(i2);
            if (CollationRuleParser.isSyntaxChar(c2) && c2 != '-' && c2 != '_') {
                if (raw.length() == 0) {
                    return i2;
                }
                int lastIndex = raw.length() - 1;
                if (raw.charAt(lastIndex) == ' ') {
                    raw.setLength(lastIndex);
                }
                return i2;
            }
            if (PatternProps.isWhiteSpace(c2)) {
                raw.append(' ');
                i2 = this.skipWhiteSpace(i2 + 1);
                continue;
            }
            raw.append(c2);
            ++i2;
        }
        return 0;
    }

    private int skipComment(int i2) {
        char c2;
        while (i2 < this.rules.length() && (c2 = this.rules.charAt(i2++)) != '\n' && c2 != '\f' && c2 != '\r' && c2 != '\u0085' && c2 != '\u2028' && c2 != '\u2029') {
        }
        return i2;
    }

    private void setParseError(String reason) throws ParseException {
        throw this.makeParseException(reason);
    }

    private void setParseError(String reason, Exception e2) throws ParseException {
        ParseException newExc = this.makeParseException(reason + ": " + e2.getMessage());
        newExc.initCause(e2);
        throw newExc;
    }

    private ParseException makeParseException(String reason) {
        return new ParseException(this.appendErrorContext(reason), this.ruleIndex);
    }

    private String appendErrorContext(String reason) {
        StringBuilder msg = new StringBuilder(reason);
        msg.append(" at index ").append(this.ruleIndex);
        msg.append(" near \"");
        int start = this.ruleIndex - 15;
        if (start < 0) {
            start = 0;
        } else if (start > 0 && Character.isLowSurrogate(this.rules.charAt(start))) {
            ++start;
        }
        msg.append(this.rules, start, this.ruleIndex);
        msg.append('!');
        int length = this.rules.length() - this.ruleIndex;
        if (length >= 16 && Character.isHighSurrogate(this.rules.charAt(this.ruleIndex + (length = 15) - 1))) {
            --length;
        }
        msg.append(this.rules, this.ruleIndex, this.ruleIndex + length);
        return msg.append('\"').toString();
    }

    private static boolean isSyntaxChar(int c2) {
        return 33 <= c2 && c2 <= 126 && (c2 <= 47 || 58 <= c2 && c2 <= 64 || 91 <= c2 && c2 <= 96 || 123 <= c2);
    }

    private int skipWhiteSpace(int i2) {
        while (i2 < this.rules.length() && PatternProps.isWhiteSpace(this.rules.charAt(i2))) {
            ++i2;
        }
        return i2;
    }

    static interface Importer {
        public String getRules(String var1, String var2);
    }

    static abstract class Sink {
        Sink() {
        }

        abstract void addReset(int var1, CharSequence var2);

        abstract void addRelation(int var1, CharSequence var2, CharSequence var3, CharSequence var4);

        void suppressContractions(UnicodeSet set) {
        }

        void optimize(UnicodeSet set) {
        }
    }

    static enum Position {
        FIRST_TERTIARY_IGNORABLE,
        LAST_TERTIARY_IGNORABLE,
        FIRST_SECONDARY_IGNORABLE,
        LAST_SECONDARY_IGNORABLE,
        FIRST_PRIMARY_IGNORABLE,
        LAST_PRIMARY_IGNORABLE,
        FIRST_VARIABLE,
        LAST_VARIABLE,
        FIRST_REGULAR,
        LAST_REGULAR,
        FIRST_IMPLICIT,
        LAST_IMPLICIT,
        FIRST_TRAILING,
        LAST_TRAILING;

    }
}

