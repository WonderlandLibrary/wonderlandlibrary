/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.IllegalIcuArgumentException;
import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.FunctionReplacer;
import com.ibm.icu.text.Normalizer;
import com.ibm.icu.text.Quantifier;
import com.ibm.icu.text.RuleBasedTransliterator;
import com.ibm.icu.text.StringMatcher;
import com.ibm.icu.text.StringReplacer;
import com.ibm.icu.text.SymbolTable;
import com.ibm.icu.text.TransliterationRule;
import com.ibm.icu.text.Transliterator;
import com.ibm.icu.text.TransliteratorIDParser;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeMatcher;
import com.ibm.icu.text.UnicodeReplacer;
import com.ibm.icu.text.UnicodeSet;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TransliteratorParser {
    public List<RuleBasedTransliterator.Data> dataVector;
    public List<String> idBlockVector;
    private RuleBasedTransliterator.Data curData;
    public UnicodeSet compoundFilter;
    private int direction;
    private ParseData parseData;
    private List<Object> variablesVector;
    private Map<String, char[]> variableNames;
    private StringBuffer segmentStandins;
    private List<StringMatcher> segmentObjects;
    private char variableNext;
    private char variableLimit;
    private String undefinedVariableName;
    private int dotStandIn = -1;
    private static final String ID_TOKEN = "::";
    private static final int ID_TOKEN_LEN = 2;
    private static final char VARIABLE_DEF_OP = '=';
    private static final char FORWARD_RULE_OP = '>';
    private static final char REVERSE_RULE_OP = '<';
    private static final char FWDREV_RULE_OP = '~';
    private static final String OPERATORS = "=><\u2190\u2192\u2194";
    private static final String HALF_ENDERS = "=><\u2190\u2192\u2194;";
    private static final char QUOTE = '\'';
    private static final char ESCAPE = '\\';
    private static final char END_OF_RULE = ';';
    private static final char RULE_COMMENT_CHAR = '#';
    private static final char CONTEXT_ANTE = '{';
    private static final char CONTEXT_POST = '}';
    private static final char CURSOR_POS = '|';
    private static final char CURSOR_OFFSET = '@';
    private static final char ANCHOR_START = '^';
    private static final char KLEENE_STAR = '*';
    private static final char ONE_OR_MORE = '+';
    private static final char ZERO_OR_ONE = '?';
    private static final char DOT = '.';
    private static final String DOT_SET = "[^[:Zp:][:Zl:]\\r\\n$]";
    private static final char SEGMENT_OPEN = '(';
    private static final char SEGMENT_CLOSE = ')';
    private static final char FUNCTION = '&';
    private static final char ALT_REVERSE_RULE_OP = '\u2190';
    private static final char ALT_FORWARD_RULE_OP = '\u2192';
    private static final char ALT_FWDREV_RULE_OP = '\u2194';
    private static final char ALT_FUNCTION = '\u2206';
    private static UnicodeSet ILLEGAL_TOP = new UnicodeSet("[\\)]");
    private static UnicodeSet ILLEGAL_SEG = new UnicodeSet("[\\{\\}\\|\\@]");
    private static UnicodeSet ILLEGAL_FUNC = new UnicodeSet("[\\^\\(\\.\\*\\+\\?\\{\\}\\|\\@]");

    public void parse(String rules, int dir) {
        this.parseRules(new RuleArray(new String[]{rules}), dir);
    }

    void parseRules(RuleBody ruleArray, int dir) {
        int i2;
        String rule;
        boolean parsingIDs = true;
        int ruleCount = 0;
        this.dataVector = new ArrayList<RuleBasedTransliterator.Data>();
        this.idBlockVector = new ArrayList<String>();
        this.curData = null;
        this.direction = dir;
        this.compoundFilter = null;
        this.variablesVector = new ArrayList<Object>();
        this.variableNames = new HashMap<String, char[]>();
        this.parseData = new ParseData();
        ArrayList<IllegalArgumentException> errors = new ArrayList<IllegalArgumentException>();
        int errorCount = 0;
        ruleArray.reset();
        StringBuilder idBlockResult = new StringBuilder();
        this.compoundFilter = null;
        int compoundFilterOffset = -1;
        block4: while ((rule = ruleArray.nextLine()) != null) {
            int pos = 0;
            int limit = rule.length();
            while (pos < limit) {
                char c2;
                if (PatternProps.isWhiteSpace(c2 = rule.charAt(pos++))) continue;
                if (c2 == '#') {
                    if ((pos = rule.indexOf("\n", pos) + 1) != 0) continue;
                    continue block4;
                }
                if (c2 == ';') continue;
                try {
                    ++ruleCount;
                    if (--pos + 2 + 1 <= limit && rule.regionMatches(pos, ID_TOKEN, 0, 2)) {
                        c2 = rule.charAt(pos += 2);
                        while (PatternProps.isWhiteSpace(c2) && pos < limit) {
                            c2 = rule.charAt(++pos);
                        }
                        int[] p2 = new int[]{pos};
                        if (!parsingIDs) {
                            if (this.curData != null) {
                                if (this.direction == 0) {
                                    this.dataVector.add(this.curData);
                                } else {
                                    this.dataVector.add(0, this.curData);
                                }
                                this.curData = null;
                            }
                            parsingIDs = true;
                        }
                        TransliteratorIDParser.SingleID id = TransliteratorIDParser.parseSingleID(rule, p2, this.direction);
                        if (p2[0] != pos && Utility.parseChar(rule, p2, ';')) {
                            if (this.direction == 0) {
                                idBlockResult.append(id.canonID).append(';');
                            } else {
                                idBlockResult.insert(0, id.canonID + ';');
                            }
                        } else {
                            int[] withParens = new int[]{-1};
                            UnicodeSet f2 = TransliteratorIDParser.parseGlobalFilter(rule, p2, this.direction, withParens, null);
                            if (f2 != null && Utility.parseChar(rule, p2, ';')) {
                                if (this.direction == 0 == (withParens[0] == 0)) {
                                    if (this.compoundFilter != null) {
                                        TransliteratorParser.syntaxError("Multiple global filters", rule, pos);
                                    }
                                    this.compoundFilter = f2;
                                    compoundFilterOffset = ruleCount;
                                }
                            } else {
                                TransliteratorParser.syntaxError("Invalid ::ID", rule, pos);
                            }
                        }
                        pos = p2[0];
                        continue;
                    }
                    if (parsingIDs) {
                        if (this.direction == 0) {
                            this.idBlockVector.add(idBlockResult.toString());
                        } else {
                            this.idBlockVector.add(0, idBlockResult.toString());
                        }
                        idBlockResult.delete(0, idBlockResult.length());
                        parsingIDs = false;
                        this.curData = new RuleBasedTransliterator.Data();
                        this.setVariableRange(61440, 63743);
                    }
                    if (TransliteratorParser.resemblesPragma(rule, pos, limit)) {
                        int ppp = this.parsePragma(rule, pos, limit);
                        if (ppp < 0) {
                            TransliteratorParser.syntaxError("Unrecognized pragma", rule, pos);
                        }
                        pos = ppp;
                        continue;
                    }
                    pos = this.parseRule(rule, pos, limit);
                }
                catch (IllegalArgumentException e2) {
                    if (errorCount == 30) {
                        IllegalIcuArgumentException icuEx = new IllegalIcuArgumentException("\nMore than 30 errors; further messages squelched");
                        icuEx.initCause(e2);
                        errors.add(icuEx);
                        break block4;
                    }
                    e2.fillInStackTrace();
                    errors.add(e2);
                    ++errorCount;
                    pos = TransliteratorParser.ruleEnd(rule, pos, limit) + 1;
                }
            }
        }
        if (parsingIDs && idBlockResult.length() > 0) {
            if (this.direction == 0) {
                this.idBlockVector.add(idBlockResult.toString());
            } else {
                this.idBlockVector.add(0, idBlockResult.toString());
            }
        } else if (!parsingIDs && this.curData != null) {
            if (this.direction == 0) {
                this.dataVector.add(this.curData);
            } else {
                this.dataVector.add(0, this.curData);
            }
        }
        for (i2 = 0; i2 < this.dataVector.size(); ++i2) {
            RuleBasedTransliterator.Data data = this.dataVector.get(i2);
            data.variables = new Object[this.variablesVector.size()];
            this.variablesVector.toArray(data.variables);
            data.variableNames = new HashMap<String, char[]>();
            data.variableNames.putAll(this.variableNames);
        }
        this.variablesVector = null;
        try {
            if (this.compoundFilter != null && (this.direction == 0 && compoundFilterOffset != 1 || this.direction == 1 && compoundFilterOffset != ruleCount)) {
                throw new IllegalIcuArgumentException("Compound filters misplaced");
            }
            for (i2 = 0; i2 < this.dataVector.size(); ++i2) {
                RuleBasedTransliterator.Data data = this.dataVector.get(i2);
                data.ruleSet.freeze();
            }
            if (this.idBlockVector.size() == 1 && this.idBlockVector.get(0).length() == 0) {
                this.idBlockVector.remove(0);
            }
        }
        catch (IllegalArgumentException e3) {
            e3.fillInStackTrace();
            errors.add(e3);
        }
        if (errors.size() != 0) {
            for (int i3 = errors.size() - 1; i3 > 0; --i3) {
                RuntimeException previous = (RuntimeException)errors.get(i3 - 1);
                while (previous.getCause() != null) {
                    previous = (RuntimeException)previous.getCause();
                }
                previous.initCause((Throwable)errors.get(i3));
            }
            throw (RuntimeException)errors.get(0);
        }
    }

    private int parseRule(String rule, int pos, int limit) {
        int i2;
        int start = pos;
        int operator = 0;
        this.segmentStandins = new StringBuffer();
        this.segmentObjects = new ArrayList<StringMatcher>();
        RuleHalf left = new RuleHalf();
        RuleHalf right = new RuleHalf();
        this.undefinedVariableName = null;
        if ((pos = left.parse(rule, pos, limit, this)) == limit || OPERATORS.indexOf(operator = rule.charAt(--pos)) < 0) {
            TransliteratorParser.syntaxError("No operator pos=" + pos, rule, start);
        }
        if (operator == 60 && ++pos < limit && rule.charAt(pos) == '>') {
            ++pos;
            operator = 126;
        }
        switch (operator) {
            case 8594: {
                operator = 62;
                break;
            }
            case 8592: {
                operator = 60;
                break;
            }
            case 8596: {
                operator = 126;
            }
        }
        pos = right.parse(rule, pos, limit, this);
        if (pos < limit) {
            if (rule.charAt(--pos) == ';') {
                ++pos;
            } else {
                TransliteratorParser.syntaxError("Unquoted operator", rule, start);
            }
        }
        if (operator == 61) {
            if (this.undefinedVariableName == null) {
                TransliteratorParser.syntaxError("Missing '$' or duplicate definition", rule, start);
            }
            if (left.text.length() != 1 || left.text.charAt(0) != this.variableLimit) {
                TransliteratorParser.syntaxError("Malformed LHS", rule, start);
            }
            if (left.anchorStart || left.anchorEnd || right.anchorStart || right.anchorEnd) {
                TransliteratorParser.syntaxError("Malformed variable def", rule, start);
            }
            int n2 = right.text.length();
            char[] value = new char[n2];
            right.text.getChars(0, n2, value, 0);
            this.variableNames.put(this.undefinedVariableName, value);
            this.variableLimit = (char)(this.variableLimit + '\u0001');
            return pos;
        }
        if (this.undefinedVariableName != null) {
            TransliteratorParser.syntaxError("Undefined variable $" + this.undefinedVariableName, rule, start);
        }
        if (this.segmentStandins.length() > this.segmentObjects.size()) {
            TransliteratorParser.syntaxError("Undefined segment reference", rule, start);
        }
        for (i2 = 0; i2 < this.segmentStandins.length(); ++i2) {
            if (this.segmentStandins.charAt(i2) != '\u0000') continue;
            TransliteratorParser.syntaxError("Internal error", rule, start);
        }
        for (i2 = 0; i2 < this.segmentObjects.size(); ++i2) {
            if (this.segmentObjects.get(i2) != null) continue;
            TransliteratorParser.syntaxError("Internal error", rule, start);
        }
        if (operator != 126 && this.direction == 0 != (operator == 62)) {
            return pos;
        }
        if (this.direction == 1) {
            RuleHalf temp = left;
            left = right;
            right = temp;
        }
        if (operator == 126) {
            right.removeContext();
            left.cursor = -1;
            left.cursorOffset = 0;
        }
        if (left.ante < 0) {
            left.ante = 0;
        }
        if (left.post < 0) {
            left.post = left.text.length();
        }
        if (right.ante >= 0 || right.post >= 0 || left.cursor >= 0 || right.cursorOffset != 0 && right.cursor < 0 || right.anchorStart || right.anchorEnd || !left.isValidInput(this) || !right.isValidOutput(this) || left.ante > left.post) {
            TransliteratorParser.syntaxError("Malformed rule", rule, start);
        }
        UnicodeMatcher[] segmentsArray = null;
        if (this.segmentObjects.size() > 0) {
            segmentsArray = new UnicodeMatcher[this.segmentObjects.size()];
            this.segmentObjects.toArray(segmentsArray);
        }
        this.curData.ruleSet.addRule(new TransliterationRule(left.text, left.ante, left.post, right.text, right.cursor, right.cursorOffset, segmentsArray, left.anchorStart, left.anchorEnd, this.curData));
        return pos;
    }

    private void setVariableRange(int start, int end) {
        if (start > end || start < 0 || end > 65535) {
            throw new IllegalIcuArgumentException("Invalid variable range " + start + ", " + end);
        }
        this.curData.variablesBase = (char)start;
        if (this.dataVector.size() == 0) {
            this.variableNext = (char)start;
            this.variableLimit = (char)(end + 1);
        }
    }

    private void checkVariableRange(int ch, String rule, int start) {
        if (ch >= this.curData.variablesBase && ch < this.variableLimit) {
            TransliteratorParser.syntaxError("Variable range character in rule", rule, start);
        }
    }

    private void pragmaMaximumBackup(int backup) {
        throw new IllegalIcuArgumentException("use maximum backup pragma not implemented yet");
    }

    private void pragmaNormalizeRules(Normalizer.Mode mode) {
        throw new IllegalIcuArgumentException("use normalize rules pragma not implemented yet");
    }

    static boolean resemblesPragma(String rule, int pos, int limit) {
        return Utility.parsePattern(rule, pos, limit, "use ", null) >= 0;
    }

    private int parsePragma(String rule, int pos, int limit) {
        int[] array = new int[2];
        int p2 = Utility.parsePattern(rule, pos += 4, limit, "~variable range # #~;", array);
        if (p2 >= 0) {
            this.setVariableRange(array[0], array[1]);
            return p2;
        }
        p2 = Utility.parsePattern(rule, pos, limit, "~maximum backup #~;", array);
        if (p2 >= 0) {
            this.pragmaMaximumBackup(array[0]);
            return p2;
        }
        p2 = Utility.parsePattern(rule, pos, limit, "~nfd rules~;", null);
        if (p2 >= 0) {
            this.pragmaNormalizeRules(Normalizer.NFD);
            return p2;
        }
        p2 = Utility.parsePattern(rule, pos, limit, "~nfc rules~;", null);
        if (p2 >= 0) {
            this.pragmaNormalizeRules(Normalizer.NFC);
            return p2;
        }
        return -1;
    }

    static final void syntaxError(String msg, String rule, int start) {
        int end = TransliteratorParser.ruleEnd(rule, start, rule.length());
        throw new IllegalIcuArgumentException(msg + " in \"" + Utility.escape(rule.substring(start, end)) + '\"');
    }

    static final int ruleEnd(String rule, int start, int limit) {
        int end = Utility.quotedIndexOf(rule, start, limit, ";");
        if (end < 0) {
            end = limit;
        }
        return end;
    }

    private final char parseSet(String rule, ParsePosition pos) {
        UnicodeSet set = new UnicodeSet(rule, pos, this.parseData);
        if (this.variableNext >= this.variableLimit) {
            throw new RuntimeException("Private use variables exhausted");
        }
        set.compact();
        return this.generateStandInFor(set);
    }

    char generateStandInFor(Object obj) {
        for (int i2 = 0; i2 < this.variablesVector.size(); ++i2) {
            if (this.variablesVector.get(i2) != obj) continue;
            return (char)(this.curData.variablesBase + i2);
        }
        if (this.variableNext >= this.variableLimit) {
            throw new RuntimeException("Variable range exhausted");
        }
        this.variablesVector.add(obj);
        char c2 = this.variableNext;
        this.variableNext = (char)(c2 + '\u0001');
        return c2;
    }

    public char getSegmentStandin(int seg) {
        char c2;
        if (this.segmentStandins.length() < seg) {
            this.segmentStandins.setLength(seg);
        }
        if ((c2 = this.segmentStandins.charAt(seg - 1)) == '\u0000') {
            if (this.variableNext >= this.variableLimit) {
                throw new RuntimeException("Variable range exhausted");
            }
            char c3 = this.variableNext;
            this.variableNext = (char)(c3 + '\u0001');
            c2 = c3;
            this.variablesVector.add(null);
            this.segmentStandins.setCharAt(seg - 1, c2);
        }
        return c2;
    }

    public void setSegmentObject(int seg, StringMatcher obj) {
        while (this.segmentObjects.size() < seg) {
            this.segmentObjects.add(null);
        }
        int index = this.getSegmentStandin(seg) - this.curData.variablesBase;
        if (this.segmentObjects.get(seg - 1) != null || this.variablesVector.get(index) != null) {
            throw new RuntimeException();
        }
        this.segmentObjects.set(seg - 1, obj);
        this.variablesVector.set(index, obj);
    }

    char getDotStandIn() {
        if (this.dotStandIn == -1) {
            this.dotStandIn = this.generateStandInFor(new UnicodeSet(DOT_SET));
        }
        return (char)this.dotStandIn;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private void appendVariableDef(String name, StringBuffer buf) {
        char[] ch = this.variableNames.get(name);
        if (ch == null) {
            if (this.undefinedVariableName != null) throw new IllegalIcuArgumentException("Undefined variable $" + name);
            this.undefinedVariableName = name;
            if (this.variableNext >= this.variableLimit) {
                throw new RuntimeException("Private use variables exhausted");
            }
            this.variableLimit = (char)(this.variableLimit - '\u0001');
            buf.append(this.variableLimit);
            return;
        } else {
            buf.append(ch);
        }
    }

    private static class RuleHalf {
        public String text;
        public int cursor = -1;
        public int ante = -1;
        public int post = -1;
        public int cursorOffset = 0;
        private int cursorOffsetPos = 0;
        public boolean anchorStart = false;
        public boolean anchorEnd = false;
        private int nextSegmentNumber = 1;

        private RuleHalf() {
        }

        public int parse(String rule, int pos, int limit, TransliteratorParser parser) {
            int start = pos;
            StringBuffer buf = new StringBuffer();
            pos = this.parseSection(rule, pos, limit, parser, buf, ILLEGAL_TOP, false);
            this.text = buf.toString();
            if (this.cursorOffset > 0 && this.cursor != this.cursorOffsetPos) {
                TransliteratorParser.syntaxError("Misplaced |", rule, start);
            }
            return pos;
        }

        private int parseSection(String rule, int pos, int limit, TransliteratorParser parser, StringBuffer buf, UnicodeSet illegal, boolean isSegment) {
            int start = pos;
            ParsePosition pp = null;
            int quoteStart = -1;
            int quoteLimit = -1;
            int varStart = -1;
            int varLimit = -1;
            int[] iref = new int[1];
            int bufStart = buf.length();
            block19: while (pos < limit) {
                char c2;
                if (PatternProps.isWhiteSpace(c2 = rule.charAt(pos++))) continue;
                if (TransliteratorParser.HALF_ENDERS.indexOf(c2) >= 0) {
                    if (!isSegment) break;
                    TransliteratorParser.syntaxError("Unclosed segment", rule, start);
                    break;
                }
                if (this.anchorEnd) {
                    TransliteratorParser.syntaxError("Malformed variable reference", rule, start);
                }
                if (UnicodeSet.resemblesPattern(rule, pos - 1)) {
                    if (pp == null) {
                        pp = new ParsePosition(0);
                    }
                    pp.setIndex(pos - 1);
                    buf.append(parser.parseSet(rule, pp));
                    pos = pp.getIndex();
                    continue;
                }
                if (c2 == '\\') {
                    if (pos == limit) {
                        TransliteratorParser.syntaxError("Trailing backslash", rule, start);
                    }
                    iref[0] = pos;
                    int escaped = Utility.unescapeAt(rule, iref);
                    pos = iref[0];
                    if (escaped == -1) {
                        TransliteratorParser.syntaxError("Malformed escape", rule, start);
                    }
                    parser.checkVariableRange(escaped, rule, start);
                    UTF16.append(buf, escaped);
                    continue;
                }
                if (c2 == '\'') {
                    int iq = rule.indexOf(39, pos);
                    if (iq == pos) {
                        buf.append(c2);
                        ++pos;
                        continue;
                    }
                    quoteStart = buf.length();
                    while (true) {
                        if (iq < 0) {
                            TransliteratorParser.syntaxError("Unterminated quote", rule, start);
                        }
                        buf.append(rule.substring(pos, iq));
                        pos = iq + 1;
                        if (pos >= limit || rule.charAt(pos) != '\'') break;
                        iq = rule.indexOf(39, pos + 1);
                    }
                    quoteLimit = buf.length();
                    for (iq = quoteStart; iq < quoteLimit; ++iq) {
                        parser.checkVariableRange(buf.charAt(iq), rule, start);
                    }
                    continue;
                }
                parser.checkVariableRange(c2, rule, start);
                if (illegal.contains(c2)) {
                    TransliteratorParser.syntaxError("Illegal character '" + c2 + '\'', rule, start);
                }
                switch (c2) {
                    case '^': {
                        if (buf.length() == 0 && !this.anchorStart) {
                            this.anchorStart = true;
                            break;
                        }
                        TransliteratorParser.syntaxError("Misplaced anchor start", rule, start);
                        break;
                    }
                    case '(': {
                        int segmentNumber;
                        int bufSegStart = buf.length();
                        ++this.nextSegmentNumber;
                        pos = this.parseSection(rule, pos, limit, parser, buf, ILLEGAL_SEG, true);
                        UnicodeMatcher m2 = new StringMatcher(buf.substring(bufSegStart), segmentNumber, parser.curData);
                        parser.setSegmentObject(segmentNumber, (StringMatcher)m2);
                        buf.setLength(bufSegStart);
                        buf.append(parser.getSegmentStandin(segmentNumber));
                        break;
                    }
                    case '&': 
                    case '\u2206': {
                        Transliterator t2;
                        iref[0] = pos;
                        TransliteratorIDParser.SingleID single = TransliteratorIDParser.parseFilterID(rule, iref);
                        if (single == null || !Utility.parseChar(rule, iref, '(')) {
                            TransliteratorParser.syntaxError("Invalid function", rule, start);
                        }
                        if ((t2 = single.getInstance()) == null) {
                            TransliteratorParser.syntaxError("Invalid function ID", rule, start);
                        }
                        int bufSegStart = buf.length();
                        pos = this.parseSection(rule, iref[0], limit, parser, buf, ILLEGAL_FUNC, true);
                        FunctionReplacer r2 = new FunctionReplacer(t2, new StringReplacer(buf.substring(bufSegStart), parser.curData));
                        buf.setLength(bufSegStart);
                        buf.append(parser.generateStandInFor(r2));
                        break;
                    }
                    case '$': {
                        if (pos == limit) {
                            this.anchorEnd = true;
                            break;
                        }
                        c2 = rule.charAt(pos);
                        int r3 = UCharacter.digit(c2, 10);
                        if (r3 >= 1 && r3 <= 9) {
                            iref[0] = pos;
                            r3 = Utility.parseNumber(rule, iref, 10);
                            if (r3 < 0) {
                                TransliteratorParser.syntaxError("Undefined segment reference", rule, start);
                            }
                            pos = iref[0];
                            buf.append(parser.getSegmentStandin(r3));
                            break;
                        }
                        if (pp == null) {
                            pp = new ParsePosition(0);
                        }
                        pp.setIndex(pos);
                        String name = parser.parseData.parseReference(rule, pp, limit);
                        if (name == null) {
                            this.anchorEnd = true;
                            break;
                        }
                        pos = pp.getIndex();
                        varStart = buf.length();
                        parser.appendVariableDef(name, buf);
                        varLimit = buf.length();
                        break;
                    }
                    case '.': {
                        buf.append(parser.getDotStandIn());
                        break;
                    }
                    case '*': 
                    case '+': 
                    case '?': {
                        int qlimit;
                        int qstart;
                        UnicodeMatcher m2;
                        if (isSegment && buf.length() == bufStart) {
                            TransliteratorParser.syntaxError("Misplaced quantifier", rule, start);
                            break;
                        }
                        if (buf.length() == quoteLimit) {
                            qstart = quoteStart;
                            qlimit = quoteLimit;
                        } else if (buf.length() == varLimit) {
                            qstart = varStart;
                            qlimit = varLimit;
                        } else {
                            qstart = buf.length() - 1;
                            qlimit = qstart + 1;
                        }
                        try {
                            m2 = new StringMatcher(buf.toString(), qstart, qlimit, 0, parser.curData);
                        }
                        catch (RuntimeException e2) {
                            String precontext = pos < 50 ? rule.substring(0, pos) : "..." + rule.substring(pos - 50, pos);
                            String postContext = limit - pos <= 50 ? rule.substring(pos, limit) : rule.substring(pos, pos + 50) + "...";
                            throw new IllegalIcuArgumentException("Failure in rule: " + precontext + "$$$" + postContext).initCause(e2);
                        }
                        int min = 0;
                        int max = Integer.MAX_VALUE;
                        switch (c2) {
                            case '+': {
                                min = 1;
                                break;
                            }
                            case '?': {
                                min = 0;
                                max = 1;
                            }
                        }
                        m2 = new Quantifier(m2, min, max);
                        buf.setLength(qstart);
                        buf.append(parser.generateStandInFor(m2));
                        break;
                    }
                    case ')': {
                        break block19;
                    }
                    case '{': {
                        if (this.ante >= 0) {
                            TransliteratorParser.syntaxError("Multiple ante contexts", rule, start);
                        }
                        this.ante = buf.length();
                        break;
                    }
                    case '}': {
                        if (this.post >= 0) {
                            TransliteratorParser.syntaxError("Multiple post contexts", rule, start);
                        }
                        this.post = buf.length();
                        break;
                    }
                    case '|': {
                        if (this.cursor >= 0) {
                            TransliteratorParser.syntaxError("Multiple cursors", rule, start);
                        }
                        this.cursor = buf.length();
                        break;
                    }
                    case '@': {
                        if (this.cursorOffset < 0) {
                            if (buf.length() > 0) {
                                TransliteratorParser.syntaxError("Misplaced " + c2, rule, start);
                            }
                            --this.cursorOffset;
                            break;
                        }
                        if (this.cursorOffset > 0) {
                            if (buf.length() != this.cursorOffsetPos || this.cursor >= 0) {
                                TransliteratorParser.syntaxError("Misplaced " + c2, rule, start);
                            }
                            ++this.cursorOffset;
                            break;
                        }
                        if (this.cursor == 0 && buf.length() == 0) {
                            this.cursorOffset = -1;
                            break;
                        }
                        if (this.cursor < 0) {
                            this.cursorOffsetPos = buf.length();
                            this.cursorOffset = 1;
                            break;
                        }
                        TransliteratorParser.syntaxError("Misplaced " + c2, rule, start);
                        break;
                    }
                    default: {
                        if (!(c2 < '!' || c2 > '~' || c2 >= '0' && c2 <= '9' || c2 >= 'A' && c2 <= 'Z' || c2 >= 'a' && c2 <= 'z')) {
                            TransliteratorParser.syntaxError("Unquoted " + c2, rule, start);
                        }
                        buf.append(c2);
                    }
                }
            }
            return pos;
        }

        void removeContext() {
            this.text = this.text.substring(this.ante < 0 ? 0 : this.ante, this.post < 0 ? this.text.length() : this.post);
            this.post = -1;
            this.ante = -1;
            this.anchorEnd = false;
            this.anchorStart = false;
        }

        public boolean isValidOutput(TransliteratorParser parser) {
            int c2;
            for (int i2 = 0; i2 < this.text.length(); i2 += UTF16.getCharCount(c2)) {
                c2 = UTF16.charAt(this.text, i2);
                if (parser.parseData.isReplacer(c2)) continue;
                return false;
            }
            return true;
        }

        public boolean isValidInput(TransliteratorParser parser) {
            int c2;
            for (int i2 = 0; i2 < this.text.length(); i2 += UTF16.getCharCount(c2)) {
                c2 = UTF16.charAt(this.text, i2);
                if (parser.parseData.isMatcher(c2)) continue;
                return false;
            }
            return true;
        }
    }

    private static class RuleArray
    extends RuleBody {
        String[] array;
        int i;

        public RuleArray(String[] array) {
            this.array = array;
            this.i = 0;
        }

        @Override
        public String handleNextLine() {
            return this.i < this.array.length ? this.array[this.i++] : null;
        }

        @Override
        public void reset() {
            this.i = 0;
        }
    }

    private static abstract class RuleBody {
        private RuleBody() {
        }

        String nextLine() {
            String s2 = this.handleNextLine();
            if (s2 != null && s2.length() > 0 && s2.charAt(s2.length() - 1) == '\\') {
                StringBuilder b2 = new StringBuilder(s2);
                do {
                    b2.deleteCharAt(b2.length() - 1);
                    s2 = this.handleNextLine();
                    if (s2 == null) break;
                    b2.append(s2);
                } while (s2.length() > 0 && s2.charAt(s2.length() - 1) == '\\');
                s2 = b2.toString();
            }
            return s2;
        }

        abstract void reset();

        abstract String handleNextLine();
    }

    private class ParseData
    implements SymbolTable {
        private ParseData() {
        }

        @Override
        public char[] lookup(String name) {
            return (char[])TransliteratorParser.this.variableNames.get(name);
        }

        @Override
        public UnicodeMatcher lookupMatcher(int ch) {
            int i2 = ch - ((TransliteratorParser)TransliteratorParser.this).curData.variablesBase;
            if (i2 >= 0 && i2 < TransliteratorParser.this.variablesVector.size()) {
                return (UnicodeMatcher)TransliteratorParser.this.variablesVector.get(i2);
            }
            return null;
        }

        @Override
        public String parseReference(String text, ParsePosition pos, int limit) {
            int start;
            int i2;
            for (i2 = start = pos.getIndex(); i2 < limit; ++i2) {
                char c2 = text.charAt(i2);
                if (i2 == start && !UCharacter.isUnicodeIdentifierStart(c2) || !UCharacter.isUnicodeIdentifierPart(c2)) break;
            }
            if (i2 == start) {
                return null;
            }
            pos.setIndex(i2);
            return text.substring(start, i2);
        }

        public boolean isMatcher(int ch) {
            int i2 = ch - ((TransliteratorParser)TransliteratorParser.this).curData.variablesBase;
            if (i2 >= 0 && i2 < TransliteratorParser.this.variablesVector.size()) {
                return TransliteratorParser.this.variablesVector.get(i2) instanceof UnicodeMatcher;
            }
            return true;
        }

        public boolean isReplacer(int ch) {
            int i2 = ch - ((TransliteratorParser)TransliteratorParser.this).curData.variablesBase;
            if (i2 >= 0 && i2 < TransliteratorParser.this.variablesVector.size()) {
                return TransliteratorParser.this.variablesVector.get(i2) instanceof UnicodeReplacer;
            }
            return true;
        }
    }
}

