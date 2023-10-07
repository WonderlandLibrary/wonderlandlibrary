/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;
import jdk.nashorn.internal.parser.Lexer;
import jdk.nashorn.internal.parser.Scanner;
import jdk.nashorn.internal.runtime.BitVector;
import jdk.nashorn.internal.runtime.regexp.RegExpFactory;

final class RegExpScanner
extends Scanner {
    private final StringBuilder sb;
    private final Map<Character, Integer> expected = new HashMap<Character, Integer>();
    private final List<Capture> caps = new LinkedList<Capture>();
    private final LinkedList<Integer> forwardReferences = new LinkedList();
    private int negLookaheadLevel;
    private int negLookaheadGroup;
    private boolean inCharClass = false;
    private boolean inNegativeClass = false;
    private static final String NON_IDENT_ESCAPES = "$^*+(){}[]|\\.?-";

    private RegExpScanner(String string) {
        super(string);
        this.sb = new StringBuilder(this.limit);
        this.reset(0);
        this.expected.put(Character.valueOf(']'), 0);
        this.expected.put(Character.valueOf('}'), 0);
    }

    private void processForwardReferences() {
        Iterator<Integer> iterator2 = this.forwardReferences.descendingIterator();
        while (iterator2.hasNext()) {
            int pos = iterator2.next();
            int num = iterator2.next();
            if (num <= this.caps.size()) continue;
            StringBuilder buffer = new StringBuilder();
            RegExpScanner.octalOrLiteral(Integer.toString(num), buffer);
            this.sb.insert(pos, buffer);
        }
        this.forwardReferences.clear();
    }

    public static RegExpScanner scan(String string) {
        RegExpScanner scanner = new RegExpScanner(string);
        try {
            scanner.disjunction();
        }
        catch (Exception e) {
            throw new PatternSyntaxException(e.getMessage(), string, scanner.position);
        }
        scanner.processForwardReferences();
        if (scanner.position != string.length()) {
            String p = scanner.getStringBuilder().toString();
            throw new PatternSyntaxException(string, p, p.length() + 1);
        }
        return scanner;
    }

    final StringBuilder getStringBuilder() {
        return this.sb;
    }

    String getJavaPattern() {
        return this.sb.toString();
    }

    BitVector getGroupsInNegativeLookahead() {
        BitVector vec = null;
        for (int i = 0; i < this.caps.size(); ++i) {
            Capture cap = this.caps.get(i);
            if (cap.negLookaheadLevel <= 0) continue;
            if (vec == null) {
                vec = new BitVector(this.caps.size() + 1);
            }
            vec.set(i + 1);
        }
        return vec;
    }

    private boolean commit(int n) {
        switch (n) {
            case 1: {
                this.sb.append(this.ch0);
                this.skip(1);
                break;
            }
            case 2: {
                this.sb.append(this.ch0);
                this.sb.append(this.ch1);
                this.skip(2);
                break;
            }
            case 3: {
                this.sb.append(this.ch0);
                this.sb.append(this.ch1);
                this.sb.append(this.ch2);
                this.skip(3);
                break;
            }
            default: {
                assert (false) : "Should not reach here";
                break;
            }
        }
        return true;
    }

    private void restart(int startIn, int startOut) {
        this.reset(startIn);
        this.sb.setLength(startOut);
    }

    private void push(char ch) {
        this.expected.put(Character.valueOf(ch), this.expected.get(Character.valueOf(ch)) + 1);
    }

    private void pop(char ch) {
        this.expected.put(Character.valueOf(ch), Math.min(0, this.expected.get(Character.valueOf(ch)) - 1));
    }

    private void disjunction() {
        while (true) {
            this.alternative();
            if (this.ch0 != '|') break;
            this.commit(1);
        }
    }

    private void alternative() {
        while (this.term()) {
        }
    }

    private boolean term() {
        int startIn = this.position;
        int startOut = this.sb.length();
        if (this.assertion()) {
            return true;
        }
        if (this.atom()) {
            this.quantifier();
            return true;
        }
        this.restart(startIn, startOut);
        return false;
    }

    private boolean assertion() {
        int startIn = this.position;
        int startOut = this.sb.length();
        switch (this.ch0) {
            case '$': 
            case '^': {
                return this.commit(1);
            }
            case '\\': {
                if (this.ch1 != 'b' && this.ch1 != 'B') break;
                return this.commit(2);
            }
            case '(': {
                if (this.ch1 != '?' || this.ch2 != '=' && this.ch2 != '!') break;
                boolean isNegativeLookahead = this.ch2 == '!';
                this.commit(3);
                if (isNegativeLookahead) {
                    if (this.negLookaheadLevel == 0) {
                        ++this.negLookaheadGroup;
                    }
                    ++this.negLookaheadLevel;
                }
                this.disjunction();
                if (isNegativeLookahead) {
                    --this.negLookaheadLevel;
                }
                if (this.ch0 != ')') break;
                return this.commit(1);
            }
        }
        this.restart(startIn, startOut);
        return false;
    }

    private boolean quantifier() {
        if (this.quantifierPrefix()) {
            if (this.ch0 == '?') {
                this.commit(1);
            }
            return true;
        }
        return false;
    }

    private boolean quantifierPrefix() {
        int startIn = this.position;
        int startOut = this.sb.length();
        switch (this.ch0) {
            case '*': 
            case '+': 
            case '?': {
                return this.commit(1);
            }
            case '{': {
                this.commit(1);
                if (!this.decimalDigits()) break;
                this.push('}');
                if (this.ch0 == ',') {
                    this.commit(1);
                    this.decimalDigits();
                }
                if (this.ch0 != '}') {
                    this.restart(startIn, startOut);
                    return false;
                }
                this.pop('}');
                this.commit(1);
                return true;
            }
        }
        this.restart(startIn, startOut);
        return false;
    }

    private boolean atom() {
        int startIn = this.position;
        int startOut = this.sb.length();
        if (this.patternCharacter()) {
            return true;
        }
        if (this.ch0 == '.') {
            return this.commit(1);
        }
        if (this.ch0 == '\\') {
            this.commit(1);
            if (this.atomEscape()) {
                return true;
            }
        }
        if (this.characterClass()) {
            return true;
        }
        if (this.ch0 == '(') {
            this.commit(1);
            if (this.ch0 == '?' && this.ch1 == ':') {
                this.commit(2);
            } else {
                this.caps.add(new Capture(this.negLookaheadGroup, this.negLookaheadLevel));
            }
            this.disjunction();
            if (this.ch0 == ')') {
                this.commit(1);
                return true;
            }
        }
        this.restart(startIn, startOut);
        return false;
    }

    private boolean patternCharacter() {
        if (this.atEOF()) {
            return false;
        }
        switch (this.ch0) {
            case '$': 
            case '(': 
            case ')': 
            case '*': 
            case '+': 
            case '.': 
            case '?': 
            case '[': 
            case '\\': 
            case '^': 
            case '|': {
                return false;
            }
            case ']': 
            case '}': {
                int n = this.expected.get(Character.valueOf(this.ch0));
                if (n != 0) {
                    return false;
                }
            }
            case '{': {
                if (!this.quantifierPrefix()) {
                    this.sb.append('\\');
                    return this.commit(1);
                }
                return false;
            }
        }
        return this.commit(1);
    }

    private boolean atomEscape() {
        return this.decimalEscape() || this.characterClassEscape() || this.characterEscape() || this.identityEscape();
    }

    private boolean characterEscape() {
        int startIn = this.position;
        int startOut = this.sb.length();
        if (this.controlEscape()) {
            return true;
        }
        if (this.ch0 == 'c') {
            this.commit(1);
            if (this.controlLetter()) {
                return true;
            }
            this.restart(startIn, startOut);
        }
        if (this.hexEscapeSequence() || this.unicodeEscapeSequence()) {
            return true;
        }
        this.restart(startIn, startOut);
        return false;
    }

    private boolean scanEscapeSequence(char leader, int length) {
        int startIn = this.position;
        int startOut = this.sb.length();
        if (this.ch0 != leader) {
            return false;
        }
        this.commit(1);
        for (int i = 0; i < length; ++i) {
            char ch0l = Character.toLowerCase(this.ch0);
            if (!(ch0l >= 'a' && ch0l <= 'f' || RegExpScanner.isDecimalDigit(this.ch0))) {
                this.restart(startIn, startOut);
                return false;
            }
            this.commit(1);
        }
        return true;
    }

    private boolean hexEscapeSequence() {
        return this.scanEscapeSequence('x', 2);
    }

    private boolean unicodeEscapeSequence() {
        return this.scanEscapeSequence('u', 4);
    }

    private boolean controlEscape() {
        switch (this.ch0) {
            case 'f': 
            case 'n': 
            case 'r': 
            case 't': 
            case 'v': {
                return this.commit(1);
            }
        }
        return false;
    }

    private boolean controlLetter() {
        if (this.ch0 >= 'A' && this.ch0 <= 'Z' || this.ch0 >= 'a' && this.ch0 <= 'z' || this.inCharClass && (RegExpScanner.isDecimalDigit(this.ch0) || this.ch0 == '_')) {
            this.sb.setLength(this.sb.length() - 1);
            RegExpScanner.unicode(this.ch0 % 32, this.sb);
            this.skip(1);
            return true;
        }
        return false;
    }

    private boolean identityEscape() {
        if (this.atEOF()) {
            throw new RuntimeException("\\ at end of pattern");
        }
        if (this.ch0 == 'c') {
            this.sb.append('\\');
        } else if (NON_IDENT_ESCAPES.indexOf(this.ch0) == -1) {
            this.sb.setLength(this.sb.length() - 1);
        }
        return this.commit(1);
    }

    private boolean decimalEscape() {
        int startIn = this.position;
        int startOut = this.sb.length();
        if (this.ch0 == '0' && !RegExpScanner.isOctalDigit(this.ch1)) {
            this.skip(1);
            this.sb.append("\u0000");
            return true;
        }
        if (RegExpScanner.isDecimalDigit(this.ch0)) {
            if (this.ch0 == '0') {
                if (this.inCharClass) {
                    int octalValue = 0;
                    while (RegExpScanner.isOctalDigit(this.ch0)) {
                        octalValue = octalValue * 8 + this.ch0 - 48;
                        this.skip(1);
                    }
                    RegExpScanner.unicode(octalValue, this.sb);
                } else {
                    this.decimalDigits();
                }
            } else {
                int decimalValue = 0;
                while (RegExpScanner.isDecimalDigit(this.ch0)) {
                    decimalValue = decimalValue * 10 + this.ch0 - 48;
                    this.skip(1);
                }
                if (this.inCharClass) {
                    this.sb.setLength(this.sb.length() - 1);
                    RegExpScanner.octalOrLiteral(Integer.toString(decimalValue), this.sb);
                } else if (decimalValue <= this.caps.size()) {
                    Capture capture = this.caps.get(decimalValue - 1);
                    if (!capture.canBeReferencedFrom(this.negLookaheadGroup, this.negLookaheadLevel)) {
                        this.sb.setLength(this.sb.length() - 1);
                    } else {
                        this.sb.append(decimalValue);
                    }
                } else {
                    this.sb.setLength(this.sb.length() - 1);
                    this.forwardReferences.add(decimalValue);
                    this.forwardReferences.add(this.sb.length());
                }
            }
            return true;
        }
        this.restart(startIn, startOut);
        return false;
    }

    private boolean characterClassEscape() {
        switch (this.ch0) {
            case 's': {
                if (RegExpFactory.usesJavaUtilRegex()) {
                    this.sb.setLength(this.sb.length() - 1);
                    if (this.inCharClass) {
                        this.sb.append(Lexer.getWhitespaceRegExp());
                    } else {
                        this.sb.append('[').append(Lexer.getWhitespaceRegExp()).append(']');
                    }
                    this.skip(1);
                    return true;
                }
                return this.commit(1);
            }
            case 'S': {
                if (RegExpFactory.usesJavaUtilRegex()) {
                    this.sb.setLength(this.sb.length() - 1);
                    this.sb.append(this.inNegativeClass ? "&&[" : "[^").append(Lexer.getWhitespaceRegExp()).append(']');
                    this.skip(1);
                    return true;
                }
                return this.commit(1);
            }
            case 'D': 
            case 'W': 
            case 'd': 
            case 'w': {
                return this.commit(1);
            }
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean characterClass() {
        int startIn = this.position;
        int startOut = this.sb.length();
        if (this.ch0 == '[') {
            try {
                this.inCharClass = true;
                this.push(']');
                this.commit(1);
                if (this.ch0 == '^') {
                    this.inNegativeClass = true;
                    this.commit(1);
                }
                if (this.classRanges() && this.ch0 == ']') {
                    this.pop(']');
                    this.commit(1);
                    if (this.position == startIn + 2) {
                        this.sb.setLength(this.sb.length() - 1);
                        this.sb.append("^\\s\\S]");
                    } else if (this.position == startIn + 3 && this.inNegativeClass) {
                        this.sb.setLength(this.sb.length() - 2);
                        this.sb.append("\\s\\S]");
                    }
                    boolean bl = true;
                    return bl;
                }
            }
            finally {
                this.inCharClass = false;
                this.inNegativeClass = false;
            }
        }
        this.restart(startIn, startOut);
        return false;
    }

    private boolean classRanges() {
        this.nonemptyClassRanges();
        return true;
    }

    private boolean nonemptyClassRanges() {
        int startIn = this.position;
        int startOut = this.sb.length();
        if (this.classAtom()) {
            if (this.ch0 == '-') {
                this.commit(1);
                if (this.classAtom() && this.classRanges()) {
                    return true;
                }
            }
            this.nonemptyClassRangesNoDash();
            return true;
        }
        this.restart(startIn, startOut);
        return false;
    }

    private boolean nonemptyClassRangesNoDash() {
        int startIn = this.position;
        int startOut = this.sb.length();
        if (this.classAtomNoDash()) {
            if (this.ch0 == '-') {
                this.commit(1);
                if (this.classAtom() && this.classRanges()) {
                    return true;
                }
            }
            this.nonemptyClassRangesNoDash();
            return true;
        }
        if (this.classAtom()) {
            return true;
        }
        this.restart(startIn, startOut);
        return false;
    }

    private boolean classAtom() {
        if (this.ch0 == '-') {
            return this.commit(1);
        }
        return this.classAtomNoDash();
    }

    private boolean classAtomNoDash() {
        if (this.atEOF()) {
            return false;
        }
        int startIn = this.position;
        int startOut = this.sb.length();
        switch (this.ch0) {
            case '-': 
            case ']': {
                return false;
            }
            case '[': {
                this.sb.append('\\');
                return this.commit(1);
            }
            case '\\': {
                this.commit(1);
                if (this.classEscape()) {
                    return true;
                }
                this.restart(startIn, startOut);
                return false;
            }
        }
        return this.commit(1);
    }

    private boolean classEscape() {
        if (this.decimalEscape()) {
            return true;
        }
        if (this.ch0 == 'b') {
            this.sb.setLength(this.sb.length() - 1);
            this.sb.append('\b');
            this.skip(1);
            return true;
        }
        return this.characterEscape() || this.characterClassEscape() || this.identityEscape();
    }

    private boolean decimalDigits() {
        if (!RegExpScanner.isDecimalDigit(this.ch0)) {
            return false;
        }
        while (RegExpScanner.isDecimalDigit(this.ch0)) {
            this.commit(1);
        }
        return true;
    }

    private static void unicode(int value, StringBuilder buffer) {
        String hex = Integer.toHexString(value);
        buffer.append('u');
        for (int i = 0; i < 4 - hex.length(); ++i) {
            buffer.append('0');
        }
        buffer.append(hex);
    }

    private static void octalOrLiteral(String numberLiteral, StringBuilder buffer) {
        char ch;
        int pos;
        int length = numberLiteral.length();
        int octalValue = 0;
        for (pos = 0; pos < length && octalValue < 32 && RegExpScanner.isOctalDigit(ch = numberLiteral.charAt(pos)); ++pos) {
            octalValue = octalValue * 8 + ch - 48;
        }
        if (octalValue > 0) {
            buffer.append('\\');
            RegExpScanner.unicode(octalValue, buffer);
            buffer.append(numberLiteral.substring(pos));
        } else {
            buffer.append(numberLiteral);
        }
    }

    private static boolean isOctalDigit(char ch) {
        return ch >= '0' && ch <= '7';
    }

    private static boolean isDecimalDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private static class Capture {
        private final int negLookaheadLevel;
        private final int negLookaheadGroup;

        Capture(int negLookaheadGroup, int negLookaheadLevel) {
            this.negLookaheadGroup = negLookaheadGroup;
            this.negLookaheadLevel = negLookaheadLevel;
        }

        boolean canBeReferencedFrom(int group, int level) {
            return this.negLookaheadLevel == 0 || group == this.negLookaheadGroup && level >= this.negLookaheadLevel;
        }
    }
}

