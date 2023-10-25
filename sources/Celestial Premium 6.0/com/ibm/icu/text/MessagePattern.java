/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICUConfig;
import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.util.Freezable;
import java.util.ArrayList;
import java.util.Locale;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class MessagePattern
implements Cloneable,
Freezable<MessagePattern> {
    public static final int ARG_NAME_NOT_NUMBER = -1;
    public static final int ARG_NAME_NOT_VALID = -2;
    public static final double NO_NUMERIC_VALUE = -1.23456789E8;
    private static final int MAX_PREFIX_LENGTH = 24;
    private ApostropheMode aposMode;
    private String msg;
    private ArrayList<Part> parts = new ArrayList();
    private ArrayList<Double> numericValues;
    private boolean hasArgNames;
    private boolean hasArgNumbers;
    private boolean needsAutoQuoting;
    private boolean frozen;
    private static final ApostropheMode defaultAposMode = ApostropheMode.valueOf(ICUConfig.get("com.ibm.icu.text.MessagePattern.ApostropheMode", "DOUBLE_OPTIONAL"));
    private static final ArgType[] argTypes = ArgType.values();

    public MessagePattern() {
        this.aposMode = defaultAposMode;
    }

    public MessagePattern(ApostropheMode mode) {
        this.aposMode = mode;
    }

    public MessagePattern(String pattern) {
        this.aposMode = defaultAposMode;
        this.parse(pattern);
    }

    public MessagePattern parse(String pattern) {
        this.preParse(pattern);
        this.parseMessage(0, 0, 0, ArgType.NONE);
        this.postParse();
        return this;
    }

    public MessagePattern parseChoiceStyle(String pattern) {
        this.preParse(pattern);
        this.parseChoiceStyle(0, 0);
        this.postParse();
        return this;
    }

    public MessagePattern parsePluralStyle(String pattern) {
        this.preParse(pattern);
        this.parsePluralOrSelectStyle(ArgType.PLURAL, 0, 0);
        this.postParse();
        return this;
    }

    public MessagePattern parseSelectStyle(String pattern) {
        this.preParse(pattern);
        this.parsePluralOrSelectStyle(ArgType.SELECT, 0, 0);
        this.postParse();
        return this;
    }

    public void clear() {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to clear() a frozen MessagePattern instance.");
        }
        this.msg = null;
        this.hasArgNumbers = false;
        this.hasArgNames = false;
        this.needsAutoQuoting = false;
        this.parts.clear();
        if (this.numericValues != null) {
            this.numericValues.clear();
        }
    }

    public void clearPatternAndSetApostropheMode(ApostropheMode mode) {
        this.clear();
        this.aposMode = mode;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        MessagePattern o = (MessagePattern)other;
        return this.aposMode.equals((Object)o.aposMode) && (this.msg == null ? o.msg == null : this.msg.equals(o.msg)) && this.parts.equals(o.parts);
    }

    public int hashCode() {
        return (this.aposMode.hashCode() * 37 + (this.msg != null ? this.msg.hashCode() : 0)) * 37 + this.parts.hashCode();
    }

    public ApostropheMode getApostropheMode() {
        return this.aposMode;
    }

    boolean jdkAposMode() {
        return this.aposMode == ApostropheMode.DOUBLE_REQUIRED;
    }

    public String getPatternString() {
        return this.msg;
    }

    public boolean hasNamedArguments() {
        return this.hasArgNames;
    }

    public boolean hasNumberedArguments() {
        return this.hasArgNumbers;
    }

    public String toString() {
        return this.msg;
    }

    public static int validateArgumentName(String name) {
        if (!PatternProps.isIdentifier(name)) {
            return -2;
        }
        return MessagePattern.parseArgNumber(name, 0, name.length());
    }

    public String autoQuoteApostropheDeep() {
        int count;
        if (!this.needsAutoQuoting) {
            return this.msg;
        }
        StringBuilder modified = null;
        int i = count = this.countParts();
        while (i > 0) {
            Part part;
            if ((part = this.getPart(--i)).getType() != Part.Type.INSERT_CHAR) continue;
            if (modified == null) {
                modified = new StringBuilder(this.msg.length() + 10).append(this.msg);
            }
            modified.insert(part.index, (char)part.value);
        }
        if (modified == null) {
            return this.msg;
        }
        return modified.toString();
    }

    public int countParts() {
        return this.parts.size();
    }

    public Part getPart(int i) {
        return this.parts.get(i);
    }

    public Part.Type getPartType(int i) {
        return this.parts.get(i).type;
    }

    public int getPatternIndex(int partIndex) {
        return this.parts.get(partIndex).index;
    }

    public String getSubstring(Part part) {
        int index = part.index;
        return this.msg.substring(index, index + part.length);
    }

    public boolean partSubstringMatches(Part part, String s) {
        return this.msg.regionMatches(part.index, s, 0, part.length);
    }

    public double getNumericValue(Part part) {
        Part.Type type = part.type;
        if (type == Part.Type.ARG_INT) {
            return part.value;
        }
        if (type == Part.Type.ARG_DOUBLE) {
            return this.numericValues.get(part.value);
        }
        return -1.23456789E8;
    }

    public double getPluralOffset(int pluralStart) {
        Part part = this.parts.get(pluralStart);
        if (part.type.hasNumericValue()) {
            return this.getNumericValue(part);
        }
        return 0.0;
    }

    public int getLimitPartIndex(int start) {
        int limit = this.parts.get(start).limitPartIndex;
        if (limit < start) {
            return start;
        }
        return limit;
    }

    public Object clone() {
        if (this.isFrozen()) {
            return this;
        }
        return this.cloneAsThawed();
    }

    @Override
    public MessagePattern cloneAsThawed() {
        MessagePattern newMsg;
        try {
            newMsg = (MessagePattern)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        newMsg.parts = (ArrayList)this.parts.clone();
        if (this.numericValues != null) {
            newMsg.numericValues = (ArrayList)this.numericValues.clone();
        }
        newMsg.frozen = false;
        return newMsg;
    }

    @Override
    public MessagePattern freeze() {
        this.frozen = true;
        return this;
    }

    @Override
    public boolean isFrozen() {
        return this.frozen;
    }

    private void preParse(String pattern) {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to parse(" + MessagePattern.prefix(pattern) + ") on frozen MessagePattern instance.");
        }
        this.msg = pattern;
        this.hasArgNumbers = false;
        this.hasArgNames = false;
        this.needsAutoQuoting = false;
        this.parts.clear();
        if (this.numericValues != null) {
            this.numericValues.clear();
        }
    }

    private void postParse() {
    }

    private int parseMessage(int index, int msgStartLength, int nestingLevel, ArgType parentType) {
        if (nestingLevel > 32767) {
            throw new IndexOutOfBoundsException();
        }
        int msgStart = this.parts.size();
        this.addPart(Part.Type.MSG_START, index, msgStartLength, nestingLevel);
        index += msgStartLength;
        block0: while (index < this.msg.length()) {
            char c;
            if ((c = this.msg.charAt(index++)) == '\'') {
                if (index == this.msg.length()) {
                    this.addPart(Part.Type.INSERT_CHAR, index, 0, 39);
                    this.needsAutoQuoting = true;
                    continue;
                }
                c = this.msg.charAt(index);
                if (c == '\'') {
                    this.addPart(Part.Type.SKIP_SYNTAX, index++, 1, 0);
                    continue;
                }
                if (this.aposMode == ApostropheMode.DOUBLE_REQUIRED || c == '{' || c == '}' || parentType == ArgType.CHOICE && c == '|' || parentType.hasPluralStyle() && c == '#') {
                    this.addPart(Part.Type.SKIP_SYNTAX, index - 1, 1, 0);
                    while ((index = this.msg.indexOf(39, index + 1)) >= 0) {
                        if (index + 1 < this.msg.length() && this.msg.charAt(index + 1) == '\'') {
                            this.addPart(Part.Type.SKIP_SYNTAX, ++index, 1, 0);
                            continue;
                        }
                        this.addPart(Part.Type.SKIP_SYNTAX, index++, 1, 0);
                        continue block0;
                    }
                    index = this.msg.length();
                    this.addPart(Part.Type.INSERT_CHAR, index, 0, 39);
                    this.needsAutoQuoting = true;
                    continue;
                }
                this.addPart(Part.Type.INSERT_CHAR, index, 0, 39);
                this.needsAutoQuoting = true;
                continue;
            }
            if (parentType.hasPluralStyle() && c == '#') {
                this.addPart(Part.Type.REPLACE_NUMBER, index - 1, 1, 0);
                continue;
            }
            if (c == '{') {
                index = this.parseArg(index - 1, 1, nestingLevel);
                continue;
            }
            if ((nestingLevel <= 0 || c != '}') && (parentType != ArgType.CHOICE || c != '|')) continue;
            int limitLength = parentType == ArgType.CHOICE && c == '}' ? 0 : 1;
            this.addLimitPart(msgStart, Part.Type.MSG_LIMIT, index - 1, limitLength, nestingLevel);
            if (parentType == ArgType.CHOICE) {
                return index - 1;
            }
            return index;
        }
        if (nestingLevel > 0 && !this.inTopLevelChoiceMessage(nestingLevel, parentType)) {
            throw new IllegalArgumentException("Unmatched '{' braces in message " + this.prefix());
        }
        this.addLimitPart(msgStart, Part.Type.MSG_LIMIT, index, 0, nestingLevel);
        return index;
    }

    private int parseArg(int index, int argStartLength, int nestingLevel) {
        int length;
        int argStart = this.parts.size();
        ArgType argType = ArgType.NONE;
        this.addPart(Part.Type.ARG_START, index, argStartLength, argType.ordinal());
        int nameIndex = index = this.skipWhiteSpace(index + argStartLength);
        if (index == this.msg.length()) {
            throw new IllegalArgumentException("Unmatched '{' braces in message " + this.prefix());
        }
        int number = this.parseArgNumber(nameIndex, index = this.skipIdentifier(index));
        if (number >= 0) {
            length = index - nameIndex;
            if (length > 65535 || number > 32767) {
                throw new IndexOutOfBoundsException("Argument number too large: " + this.prefix(nameIndex));
            }
            this.hasArgNumbers = true;
            this.addPart(Part.Type.ARG_NUMBER, nameIndex, length, number);
        } else if (number == -1) {
            length = index - nameIndex;
            if (length > 65535) {
                throw new IndexOutOfBoundsException("Argument name too long: " + this.prefix(nameIndex));
            }
            this.hasArgNames = true;
            this.addPart(Part.Type.ARG_NAME, nameIndex, length, 0);
        } else {
            throw new IllegalArgumentException("Bad argument syntax: " + this.prefix(nameIndex));
        }
        index = this.skipWhiteSpace(index);
        if (index == this.msg.length()) {
            throw new IllegalArgumentException("Unmatched '{' braces in message " + this.prefix());
        }
        char c = this.msg.charAt(index);
        if (c != '}') {
            if (c != ',') {
                throw new IllegalArgumentException("Bad argument syntax: " + this.prefix(nameIndex));
            }
            int typeIndex = index = this.skipWhiteSpace(index + 1);
            while (index < this.msg.length() && MessagePattern.isArgTypeChar(this.msg.charAt(index))) {
                ++index;
            }
            int length2 = index - typeIndex;
            if ((index = this.skipWhiteSpace(index)) == this.msg.length()) {
                throw new IllegalArgumentException("Unmatched '{' braces in message " + this.prefix());
            }
            if (length2 == 0 || (c = this.msg.charAt(index)) != ',' && c != '}') {
                throw new IllegalArgumentException("Bad argument syntax: " + this.prefix(nameIndex));
            }
            if (length2 > 65535) {
                throw new IndexOutOfBoundsException("Argument type name too long: " + this.prefix(nameIndex));
            }
            argType = ArgType.SIMPLE;
            if (length2 == 6) {
                if (this.isChoice(typeIndex)) {
                    argType = ArgType.CHOICE;
                } else if (this.isPlural(typeIndex)) {
                    argType = ArgType.PLURAL;
                } else if (this.isSelect(typeIndex)) {
                    argType = ArgType.SELECT;
                }
            } else if (length2 == 13 && this.isSelect(typeIndex) && this.isOrdinal(typeIndex + 6)) {
                argType = ArgType.SELECTORDINAL;
            }
            this.parts.get(argStart).value = (short)argType.ordinal();
            if (argType == ArgType.SIMPLE) {
                this.addPart(Part.Type.ARG_TYPE, typeIndex, length2, 0);
            }
            if (c == '}') {
                if (argType != ArgType.SIMPLE) {
                    throw new IllegalArgumentException("No style field for complex argument: " + this.prefix(nameIndex));
                }
            } else {
                ++index;
                index = argType == ArgType.SIMPLE ? this.parseSimpleStyle(index) : (argType == ArgType.CHOICE ? this.parseChoiceStyle(index, nestingLevel) : this.parsePluralOrSelectStyle(argType, index, nestingLevel));
            }
        }
        this.addLimitPart(argStart, Part.Type.ARG_LIMIT, index, 1, argType.ordinal());
        return index + 1;
    }

    private int parseSimpleStyle(int index) {
        int start = index;
        int nestedBraces = 0;
        while (index < this.msg.length()) {
            int length;
            char c;
            if ((c = this.msg.charAt(index++)) == '\'') {
                if ((index = this.msg.indexOf(39, index)) < 0) {
                    throw new IllegalArgumentException("Quoted literal argument style text reaches to the end of the message: " + this.prefix(start));
                }
                ++index;
                continue;
            }
            if (c == '{') {
                ++nestedBraces;
                continue;
            }
            if (c != '}') continue;
            if (nestedBraces > 0) {
                --nestedBraces;
                continue;
            }
            if ((length = --index - start) > 65535) {
                throw new IndexOutOfBoundsException("Argument style text too long: " + this.prefix(start));
            }
            this.addPart(Part.Type.ARG_STYLE, start, length, 0);
            return index;
        }
        throw new IllegalArgumentException("Unmatched '{' braces in message " + this.prefix());
    }

    private int parseChoiceStyle(int index, int nestingLevel) {
        int start = index;
        if ((index = this.skipWhiteSpace(index)) == this.msg.length() || this.msg.charAt(index) == '}') {
            throw new IllegalArgumentException("Missing choice argument pattern in " + this.prefix());
        }
        while (true) {
            int numberIndex = index;
            int length = (index = this.skipDouble(index)) - numberIndex;
            if (length == 0) {
                throw new IllegalArgumentException("Bad choice pattern syntax: " + this.prefix(start));
            }
            if (length > 65535) {
                throw new IndexOutOfBoundsException("Choice number too long: " + this.prefix(numberIndex));
            }
            this.parseDouble(numberIndex, index, true);
            index = this.skipWhiteSpace(index);
            if (index == this.msg.length()) {
                throw new IllegalArgumentException("Bad choice pattern syntax: " + this.prefix(start));
            }
            char c = this.msg.charAt(index);
            if (c != '#' && c != '<' && c != '\u2264') {
                throw new IllegalArgumentException("Expected choice separator (#<\u2264) instead of '" + c + "' in choice pattern " + this.prefix(start));
            }
            this.addPart(Part.Type.ARG_SELECTOR, index, 1, 0);
            ++index;
            index = this.parseMessage(index, 0, nestingLevel + 1, ArgType.CHOICE);
            if (index == this.msg.length()) {
                return index;
            }
            if (this.msg.charAt(index) == '}') {
                if (!this.inMessageFormatPattern(nestingLevel)) {
                    throw new IllegalArgumentException("Bad choice pattern syntax: " + this.prefix(start));
                }
                return index;
            }
            index = this.skipWhiteSpace(index + 1);
        }
    }

    private int parsePluralOrSelectStyle(ArgType argType, int index, int nestingLevel) {
        int start = index;
        boolean isEmpty = true;
        boolean hasOther = false;
        while (true) {
            int length;
            boolean eos;
            boolean bl = eos = (index = this.skipWhiteSpace(index)) == this.msg.length();
            if (eos || this.msg.charAt(index) == '}') {
                if (eos == this.inMessageFormatPattern(nestingLevel)) {
                    throw new IllegalArgumentException("Bad " + argType.toString().toLowerCase(Locale.ENGLISH) + " pattern syntax: " + this.prefix(start));
                }
                if (!hasOther) {
                    throw new IllegalArgumentException("Missing 'other' keyword in " + argType.toString().toLowerCase(Locale.ENGLISH) + " pattern in " + this.prefix());
                }
                return index;
            }
            int selectorIndex = index;
            if (argType.hasPluralStyle() && this.msg.charAt(selectorIndex) == '=') {
                length = (index = this.skipDouble(index + 1)) - selectorIndex;
                if (length == 1) {
                    throw new IllegalArgumentException("Bad " + argType.toString().toLowerCase(Locale.ENGLISH) + " pattern syntax: " + this.prefix(start));
                }
                if (length > 65535) {
                    throw new IndexOutOfBoundsException("Argument selector too long: " + this.prefix(selectorIndex));
                }
                this.addPart(Part.Type.ARG_SELECTOR, selectorIndex, length, 0);
                this.parseDouble(selectorIndex + 1, index, false);
            } else {
                length = (index = this.skipIdentifier(index)) - selectorIndex;
                if (length == 0) {
                    throw new IllegalArgumentException("Bad " + argType.toString().toLowerCase(Locale.ENGLISH) + " pattern syntax: " + this.prefix(start));
                }
                if (argType.hasPluralStyle() && length == 6 && index < this.msg.length() && this.msg.regionMatches(selectorIndex, "offset:", 0, 7)) {
                    if (!isEmpty) {
                        throw new IllegalArgumentException("Plural argument 'offset:' (if present) must precede key-message pairs: " + this.prefix(start));
                    }
                    int valueIndex = this.skipWhiteSpace(index + 1);
                    if ((index = this.skipDouble(valueIndex)) == valueIndex) {
                        throw new IllegalArgumentException("Missing value for plural 'offset:' " + this.prefix(start));
                    }
                    if (index - valueIndex > 65535) {
                        throw new IndexOutOfBoundsException("Plural offset value too long: " + this.prefix(valueIndex));
                    }
                    this.parseDouble(valueIndex, index, false);
                    isEmpty = false;
                    continue;
                }
                if (length > 65535) {
                    throw new IndexOutOfBoundsException("Argument selector too long: " + this.prefix(selectorIndex));
                }
                this.addPart(Part.Type.ARG_SELECTOR, selectorIndex, length, 0);
                if (this.msg.regionMatches(selectorIndex, "other", 0, length)) {
                    hasOther = true;
                }
            }
            index = this.skipWhiteSpace(index);
            if (index == this.msg.length() || this.msg.charAt(index) != '{') {
                throw new IllegalArgumentException("No message fragment after " + argType.toString().toLowerCase(Locale.ENGLISH) + " selector: " + this.prefix(selectorIndex));
            }
            index = this.parseMessage(index, 1, nestingLevel + 1, argType);
            isEmpty = false;
        }
    }

    private static int parseArgNumber(CharSequence s, int start, int limit) {
        boolean badNumber;
        int number;
        char c;
        if (start >= limit) {
            return -2;
        }
        if ((c = s.charAt(start++)) == '0') {
            if (start == limit) {
                return 0;
            }
            number = 0;
            badNumber = true;
        } else if ('1' <= c && c <= '9') {
            number = c - 48;
            badNumber = false;
        } else {
            return -1;
        }
        while (start < limit) {
            if ('0' <= (c = s.charAt(start++)) && c <= '9') {
                if (number >= 0xCCCCCCC) {
                    badNumber = true;
                }
                number = number * 10 + (c - 48);
                continue;
            }
            return -1;
        }
        if (badNumber) {
            return -2;
        }
        return number;
    }

    private int parseArgNumber(int start, int limit) {
        return MessagePattern.parseArgNumber(this.msg, start, limit);
    }

    private void parseDouble(int start, int limit, boolean allowInfinity) {
        block10: {
            char c;
            int index;
            int isNegative;
            int value;
            block11: {
                block9: {
                    assert (start < limit);
                    value = 0;
                    isNegative = 0;
                    index = start;
                    if ((c = this.msg.charAt(index++)) != '-') break block9;
                    isNegative = 1;
                    if (index == limit) break block10;
                    c = this.msg.charAt(index++);
                    break block11;
                }
                if (c != '+') break block11;
                if (index == limit) break block10;
                c = this.msg.charAt(index++);
            }
            if (c == '\u221e') {
                if (allowInfinity && index == limit) {
                    this.addArgDoublePart(isNegative != 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY, start, limit - start);
                    return;
                }
            } else {
                while ('0' <= c && c <= '9' && (value = value * 10 + (c - 48)) <= 32767 + isNegative) {
                    if (index == limit) {
                        this.addPart(Part.Type.ARG_INT, start, limit - start, isNegative != 0 ? -value : value);
                        return;
                    }
                    c = this.msg.charAt(index++);
                }
                double numericValue = Double.parseDouble(this.msg.substring(start, limit));
                this.addArgDoublePart(numericValue, start, limit - start);
                return;
            }
        }
        throw new NumberFormatException("Bad syntax for numeric value: " + this.msg.substring(start, limit));
    }

    static void appendReducedApostrophes(String s, int start, int limit, StringBuilder sb) {
        int doubleApos = -1;
        while (true) {
            int i;
            if ((i = s.indexOf(39, start)) < 0 || i >= limit) break;
            if (i == doubleApos) {
                sb.append('\'');
                ++start;
                doubleApos = -1;
                continue;
            }
            sb.append(s, start, i);
            doubleApos = start = i + 1;
        }
        sb.append(s, start, limit);
    }

    private int skipWhiteSpace(int index) {
        return PatternProps.skipWhiteSpace(this.msg, index);
    }

    private int skipIdentifier(int index) {
        return PatternProps.skipIdentifier(this.msg, index);
    }

    private int skipDouble(int index) {
        char c;
        while (!(index >= this.msg.length() || (c = this.msg.charAt(index)) < '0' && "+-.".indexOf(c) < 0 || c > '9' && c != 'e' && c != 'E' && c != '\u221e')) {
            ++index;
        }
        return index;
    }

    private static boolean isArgTypeChar(int c) {
        return 97 <= c && c <= 122 || 65 <= c && c <= 90;
    }

    private boolean isChoice(int index) {
        char c;
        return !((c = this.msg.charAt(index++)) != 'c' && c != 'C' || (c = this.msg.charAt(index++)) != 'h' && c != 'H' || (c = this.msg.charAt(index++)) != 'o' && c != 'O' || (c = this.msg.charAt(index++)) != 'i' && c != 'I' || (c = this.msg.charAt(index++)) != 'c' && c != 'C' || (c = this.msg.charAt(index)) != 'e' && c != 'E');
    }

    private boolean isPlural(int index) {
        char c;
        return !((c = this.msg.charAt(index++)) != 'p' && c != 'P' || (c = this.msg.charAt(index++)) != 'l' && c != 'L' || (c = this.msg.charAt(index++)) != 'u' && c != 'U' || (c = this.msg.charAt(index++)) != 'r' && c != 'R' || (c = this.msg.charAt(index++)) != 'a' && c != 'A' || (c = this.msg.charAt(index)) != 'l' && c != 'L');
    }

    private boolean isSelect(int index) {
        char c;
        return !((c = this.msg.charAt(index++)) != 's' && c != 'S' || (c = this.msg.charAt(index++)) != 'e' && c != 'E' || (c = this.msg.charAt(index++)) != 'l' && c != 'L' || (c = this.msg.charAt(index++)) != 'e' && c != 'E' || (c = this.msg.charAt(index++)) != 'c' && c != 'C' || (c = this.msg.charAt(index)) != 't' && c != 'T');
    }

    private boolean isOrdinal(int index) {
        char c;
        return !((c = this.msg.charAt(index++)) != 'o' && c != 'O' || (c = this.msg.charAt(index++)) != 'r' && c != 'R' || (c = this.msg.charAt(index++)) != 'd' && c != 'D' || (c = this.msg.charAt(index++)) != 'i' && c != 'I' || (c = this.msg.charAt(index++)) != 'n' && c != 'N' || (c = this.msg.charAt(index++)) != 'a' && c != 'A' || (c = this.msg.charAt(index)) != 'l' && c != 'L');
    }

    private boolean inMessageFormatPattern(int nestingLevel) {
        return nestingLevel > 0 || this.parts.get(0).type == Part.Type.MSG_START;
    }

    private boolean inTopLevelChoiceMessage(int nestingLevel, ArgType parentType) {
        return nestingLevel == 1 && parentType == ArgType.CHOICE && this.parts.get(0).type != Part.Type.MSG_START;
    }

    private void addPart(Part.Type type, int index, int length, int value) {
        this.parts.add(new Part(type, index, length, value));
    }

    private void addLimitPart(int start, Part.Type type, int index, int length, int value) {
        this.parts.get(start).limitPartIndex = this.parts.size();
        this.addPart(type, index, length, value);
    }

    private void addArgDoublePart(double numericValue, int start, int length) {
        int numericIndex;
        if (this.numericValues == null) {
            this.numericValues = new ArrayList();
            numericIndex = 0;
        } else {
            numericIndex = this.numericValues.size();
            if (numericIndex > 32767) {
                throw new IndexOutOfBoundsException("Too many numeric values");
            }
        }
        this.numericValues.add(numericValue);
        this.addPart(Part.Type.ARG_DOUBLE, start, length, numericIndex);
    }

    private static String prefix(String s, int start) {
        StringBuilder prefix = new StringBuilder(44);
        if (start == 0) {
            prefix.append("\"");
        } else {
            prefix.append("[at pattern index ").append(start).append("] \"");
        }
        int substringLength = s.length() - start;
        if (substringLength <= 24) {
            prefix.append(start == 0 ? s : s.substring(start));
        } else {
            int limit = start + 24 - 4;
            if (Character.isHighSurrogate(s.charAt(limit - 1))) {
                --limit;
            }
            prefix.append(s, start, limit).append(" ...");
        }
        return prefix.append("\"").toString();
    }

    private static String prefix(String s) {
        return MessagePattern.prefix(s, 0);
    }

    private String prefix(int start) {
        return MessagePattern.prefix(this.msg, start);
    }

    private String prefix() {
        return MessagePattern.prefix(this.msg, 0);
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum ArgType {
        NONE,
        SIMPLE,
        CHOICE,
        PLURAL,
        SELECT,
        SELECTORDINAL;


        public boolean hasPluralStyle() {
            return this == PLURAL || this == SELECTORDINAL;
        }
    }

    public static final class Part {
        private static final int MAX_LENGTH = 65535;
        private static final int MAX_VALUE = 32767;
        private final Type type;
        private final int index;
        private final char length;
        private short value;
        private int limitPartIndex;

        private Part(Type t, int i, int l, int v) {
            this.type = t;
            this.index = i;
            this.length = (char)l;
            this.value = (short)v;
        }

        public Type getType() {
            return this.type;
        }

        public int getIndex() {
            return this.index;
        }

        public int getLength() {
            return this.length;
        }

        public int getLimit() {
            return this.index + this.length;
        }

        public int getValue() {
            return this.value;
        }

        public ArgType getArgType() {
            Type type = this.getType();
            if (type == Type.ARG_START || type == Type.ARG_LIMIT) {
                return argTypes[this.value];
            }
            return ArgType.NONE;
        }

        public String toString() {
            String valueString = this.type == Type.ARG_START || this.type == Type.ARG_LIMIT ? this.getArgType().name() : Integer.toString(this.value);
            return this.type.name() + "(" + valueString + ")@" + this.index;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (other == null || this.getClass() != other.getClass()) {
                return false;
            }
            Part o = (Part)other;
            return this.type.equals((Object)o.type) && this.index == o.index && this.length == o.length && this.value == o.value && this.limitPartIndex == o.limitPartIndex;
        }

        public int hashCode() {
            return ((this.type.hashCode() * 37 + this.index) * 37 + this.length) * 37 + this.value;
        }

        /*
         * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
         */
        public static enum Type {
            MSG_START,
            MSG_LIMIT,
            SKIP_SYNTAX,
            INSERT_CHAR,
            REPLACE_NUMBER,
            ARG_START,
            ARG_LIMIT,
            ARG_NUMBER,
            ARG_NAME,
            ARG_TYPE,
            ARG_STYLE,
            ARG_SELECTOR,
            ARG_INT,
            ARG_DOUBLE;


            public boolean hasNumericValue() {
                return this == ARG_INT || this == ARG_DOUBLE;
            }
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static enum ApostropheMode {
        DOUBLE_OPTIONAL,
        DOUBLE_REQUIRED;

    }
}

