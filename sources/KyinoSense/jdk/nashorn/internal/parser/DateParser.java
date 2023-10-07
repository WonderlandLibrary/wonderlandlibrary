/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.parser;

import java.util.HashMap;
import java.util.Locale;

public class DateParser {
    public static final int YEAR = 0;
    public static final int MONTH = 1;
    public static final int DAY = 2;
    public static final int HOUR = 3;
    public static final int MINUTE = 4;
    public static final int SECOND = 5;
    public static final int MILLISECOND = 6;
    public static final int TIMEZONE = 7;
    private final String string;
    private final int length;
    private final Integer[] fields;
    private int pos = 0;
    private Token token;
    private int tokenLength;
    private Name nameValue;
    private int numValue;
    private int currentField = 0;
    private int yearSign = 0;
    private boolean namedMonth = false;
    private static final HashMap<String, Name> names = new HashMap();

    public DateParser(String string) {
        this.string = string;
        this.length = string.length();
        this.fields = new Integer[8];
    }

    public boolean parse() {
        return this.parseEcmaDate() || this.parseLegacyDate();
    }

    public boolean parseEcmaDate() {
        if (this.token == null) {
            this.token = this.next();
        }
        while (this.token != Token.END) {
            block0 : switch (this.token) {
                case NUMBER: {
                    if (this.currentField == 0 && this.yearSign != 0) {
                        if (this.tokenLength != 6) {
                            return false;
                        }
                        this.numValue *= this.yearSign;
                    } else if (!this.checkEcmaField(this.currentField, this.numValue)) {
                        return false;
                    }
                    if (!this.skipEcmaDelimiter()) {
                        return false;
                    }
                    if (this.currentField >= 7) break;
                    this.set(this.currentField++, this.numValue);
                    break;
                }
                case NAME: {
                    if (this.nameValue == null) {
                        return false;
                    }
                    switch (this.nameValue.type) {
                        case 3: {
                            if (this.currentField == 0 || this.currentField > 3) {
                                return false;
                            }
                            this.currentField = 3;
                            break block0;
                        }
                        case 2: {
                            if (this.nameValue.key.equals("z") && this.setTimezone(this.nameValue.value, false)) break block0;
                            return false;
                        }
                        default: {
                            return false;
                        }
                    }
                }
                case SIGN: {
                    if (this.peek() == -1) {
                        return false;
                    }
                    if (this.currentField == 0) {
                        this.yearSign = this.numValue;
                        break;
                    }
                    if (this.currentField >= 5 && this.setTimezone(this.readTimeZoneOffset(), true)) break;
                    return false;
                }
                default: {
                    return false;
                }
            }
            this.token = this.next();
        }
        return this.patchResult(true);
    }

    public boolean parseLegacyDate() {
        if (this.yearSign != 0 || this.currentField > 2) {
            return false;
        }
        if (this.token == null) {
            this.token = this.next();
        }
        while (this.token != Token.END) {
            switch (this.token) {
                case NUMBER: {
                    if (this.skipDelimiter(':')) {
                        if (!this.setTimeField(this.numValue)) {
                            return false;
                        }
                        do {
                            this.token = this.next();
                            if (this.token == Token.NUMBER && this.setTimeField(this.numValue)) continue;
                            return false;
                        } while (this.skipDelimiter(this.isSet(5) ? (char)'.' : ':'));
                        break;
                    }
                    if (!this.setDateField(this.numValue)) {
                        return false;
                    }
                    this.skipDelimiter('-');
                    break;
                }
                case NAME: {
                    if (this.nameValue == null) {
                        return false;
                    }
                    switch (this.nameValue.type) {
                        case 1: {
                            if (this.setAmPm(this.nameValue.value)) break;
                            return false;
                        }
                        case 0: {
                            if (this.setMonth(this.nameValue.value)) break;
                            return false;
                        }
                        case 2: {
                            if (this.setTimezone(this.nameValue.value, false)) break;
                            return false;
                        }
                        case 3: {
                            return false;
                        }
                    }
                    if (this.nameValue.type == 2) break;
                    this.skipDelimiter('-');
                    break;
                }
                case SIGN: {
                    if (this.peek() == -1) {
                        return false;
                    }
                    if (this.setTimezone(this.readTimeZoneOffset(), true)) break;
                    return false;
                }
                case PARENTHESIS: {
                    if (this.skipParentheses()) break;
                    return false;
                }
                case SEPARATOR: {
                    break;
                }
                default: {
                    return false;
                }
            }
            this.token = this.next();
        }
        return this.patchResult(false);
    }

    public Integer[] getDateFields() {
        return this.fields;
    }

    private boolean isSet(int field) {
        return this.fields[field] != null;
    }

    private Integer get(int field) {
        return this.fields[field];
    }

    private void set(int field, int value) {
        this.fields[field] = value;
    }

    private int peek() {
        return this.pos < this.length ? (int)this.string.charAt(this.pos) : -1;
    }

    private boolean skipNumberDelimiter(char c) {
        if (this.pos < this.length - 1 && this.string.charAt(this.pos) == c && Character.getType(this.string.charAt(this.pos + 1)) == 9) {
            this.token = null;
            ++this.pos;
            return true;
        }
        return false;
    }

    private boolean skipDelimiter(char c) {
        if (this.pos < this.length && this.string.charAt(this.pos) == c) {
            this.token = null;
            ++this.pos;
            return true;
        }
        return false;
    }

    private Token next() {
        if (this.pos >= this.length) {
            this.tokenLength = 0;
            return Token.END;
        }
        char c = this.string.charAt(this.pos);
        if (c > '\u0080') {
            this.tokenLength = 1;
            ++this.pos;
            return Token.UNKNOWN;
        }
        int type = Character.getType(c);
        switch (type) {
            case 9: {
                this.numValue = this.readNumber(6);
                return Token.NUMBER;
            }
            case 12: 
            case 24: {
                this.tokenLength = 1;
                ++this.pos;
                return Token.SEPARATOR;
            }
            case 1: 
            case 2: {
                this.nameValue = this.readName();
                return Token.NAME;
            }
        }
        this.tokenLength = 1;
        ++this.pos;
        switch (c) {
            case '(': {
                return Token.PARENTHESIS;
            }
            case '+': 
            case '-': {
                this.numValue = c == '-' ? -1 : 1;
                return Token.SIGN;
            }
        }
        return Token.UNKNOWN;
    }

    private static boolean checkLegacyField(int field, int value) {
        switch (field) {
            case 3: {
                return DateParser.isHour(value);
            }
            case 4: 
            case 5: {
                return DateParser.isMinuteOrSecond(value);
            }
            case 6: {
                return DateParser.isMillisecond(value);
            }
        }
        return true;
    }

    private boolean checkEcmaField(int field, int value) {
        switch (field) {
            case 0: {
                return this.tokenLength == 4;
            }
            case 1: {
                return this.tokenLength == 2 && DateParser.isMonth(value);
            }
            case 2: {
                return this.tokenLength == 2 && DateParser.isDay(value);
            }
            case 3: {
                return this.tokenLength == 2 && DateParser.isHour(value);
            }
            case 4: 
            case 5: {
                return this.tokenLength == 2 && DateParser.isMinuteOrSecond(value);
            }
            case 6: {
                return this.tokenLength < 4 && DateParser.isMillisecond(value);
            }
        }
        return true;
    }

    private boolean skipEcmaDelimiter() {
        switch (this.currentField) {
            case 0: 
            case 1: {
                return this.skipNumberDelimiter('-') || this.peek() == 84 || this.peek() == -1;
            }
            case 2: {
                return this.peek() == 84 || this.peek() == -1;
            }
            case 3: 
            case 4: {
                return this.skipNumberDelimiter(':') || this.endOfTime();
            }
            case 5: {
                return this.skipNumberDelimiter('.') || this.endOfTime();
            }
        }
        return true;
    }

    private boolean endOfTime() {
        int c = this.peek();
        return c == -1 || c == 90 || c == 45 || c == 43 || c == 32;
    }

    private static boolean isAsciiLetter(char ch) {
        return 'A' <= ch && ch <= 'Z' || 'a' <= ch && ch <= 'z';
    }

    private static boolean isAsciiDigit(char ch) {
        return '0' <= ch && ch <= '9';
    }

    private int readNumber(int maxDigits) {
        int start = this.pos;
        int n = 0;
        int max = Math.min(this.length, this.pos + maxDigits);
        while (this.pos < max && DateParser.isAsciiDigit(this.string.charAt(this.pos))) {
            n = n * 10 + this.string.charAt(this.pos++) - 48;
        }
        this.tokenLength = this.pos - start;
        return n;
    }

    private Name readName() {
        int start = this.pos;
        int limit = Math.min(this.pos + 3, this.length);
        while (this.pos < limit && DateParser.isAsciiLetter(this.string.charAt(this.pos))) {
            ++this.pos;
        }
        String key = this.string.substring(start, this.pos).toLowerCase(Locale.ENGLISH);
        Name name = names.get(key);
        while (this.pos < this.length && DateParser.isAsciiLetter(this.string.charAt(this.pos))) {
            ++this.pos;
        }
        this.tokenLength = this.pos - start;
        if (name != null && name.matches(this.string, start, this.tokenLength)) {
            return name;
        }
        return null;
    }

    private int readTimeZoneOffset() {
        int sign = this.string.charAt(this.pos - 1) == '+' ? 1 : -1;
        int offset = this.readNumber(2);
        this.skipDelimiter(':');
        offset = offset * 60 + this.readNumber(2);
        return sign * offset;
    }

    private boolean skipParentheses() {
        int parenCount = 1;
        while (this.pos < this.length && parenCount != 0) {
            char c;
            if ((c = this.string.charAt(this.pos++)) == '(') {
                ++parenCount;
                continue;
            }
            if (c != ')') continue;
            --parenCount;
        }
        return true;
    }

    private static int getDefaultValue(int field) {
        switch (field) {
            case 1: 
            case 2: {
                return 1;
            }
        }
        return 0;
    }

    private static boolean isDay(int n) {
        return 1 <= n && n <= 31;
    }

    private static boolean isMonth(int n) {
        return 1 <= n && n <= 12;
    }

    private static boolean isHour(int n) {
        return 0 <= n && n <= 24;
    }

    private static boolean isMinuteOrSecond(int n) {
        return 0 <= n && n < 60;
    }

    private static boolean isMillisecond(int n) {
        return 0 <= n && n < 1000;
    }

    private boolean setMonth(int m) {
        if (!this.isSet(1)) {
            this.namedMonth = true;
            this.set(1, m);
            return true;
        }
        return false;
    }

    private boolean setDateField(int n) {
        for (int field = 0; field != 3; ++field) {
            if (this.isSet(field)) continue;
            this.set(field, n);
            return true;
        }
        return false;
    }

    private boolean setTimeField(int n) {
        for (int field = 3; field != 7; ++field) {
            if (this.isSet(field)) continue;
            if (DateParser.checkLegacyField(field, n)) {
                this.set(field, n);
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean setTimezone(int offset, boolean asNumericOffset) {
        if (!this.isSet(7) || asNumericOffset && this.get(7) == 0) {
            this.set(7, offset);
            return true;
        }
        return false;
    }

    private boolean setAmPm(int offset) {
        if (!this.isSet(3)) {
            return false;
        }
        int hour = this.get(3);
        if (hour >= 0 && hour <= 12) {
            this.set(3, hour + offset);
        }
        return true;
    }

    private boolean patchResult(boolean strict) {
        if (!this.isSet(0) && !this.isSet(3)) {
            return false;
        }
        if (this.isSet(3) && !this.isSet(4)) {
            return false;
        }
        for (int field = 0; field <= 7; ++field) {
            if (this.get(field) != null || field == 7 && !strict) continue;
            int value = DateParser.getDefaultValue(field);
            this.set(field, value);
        }
        if (!strict) {
            if (DateParser.isDay(this.get(0))) {
                int d = this.get(0);
                this.set(0, this.get(2));
                if (this.namedMonth) {
                    this.set(2, d);
                } else {
                    int d2 = this.get(1);
                    this.set(1, d);
                    this.set(2, d2);
                }
            }
            if (!DateParser.isMonth(this.get(1)) || !DateParser.isDay(this.get(2))) {
                return false;
            }
            int year = this.get(0);
            if (year >= 0 && year < 100) {
                this.set(0, year >= 50 ? 1900 + year : 2000 + year);
            }
        } else if (this.get(3) == 24 && (this.get(4) != 0 || this.get(5) != 0 || this.get(6) != 0)) {
            return false;
        }
        this.set(1, this.get(1) - 1);
        return true;
    }

    private static void addName(String str, int type, int value) {
        Name name = new Name(str, type, value);
        names.put(name.key, name);
    }

    static {
        DateParser.addName("monday", -1, 0);
        DateParser.addName("tuesday", -1, 0);
        DateParser.addName("wednesday", -1, 0);
        DateParser.addName("thursday", -1, 0);
        DateParser.addName("friday", -1, 0);
        DateParser.addName("saturday", -1, 0);
        DateParser.addName("sunday", -1, 0);
        DateParser.addName("january", 0, 1);
        DateParser.addName("february", 0, 2);
        DateParser.addName("march", 0, 3);
        DateParser.addName("april", 0, 4);
        DateParser.addName("may", 0, 5);
        DateParser.addName("june", 0, 6);
        DateParser.addName("july", 0, 7);
        DateParser.addName("august", 0, 8);
        DateParser.addName("september", 0, 9);
        DateParser.addName("october", 0, 10);
        DateParser.addName("november", 0, 11);
        DateParser.addName("december", 0, 12);
        DateParser.addName("am", 1, 0);
        DateParser.addName("pm", 1, 12);
        DateParser.addName("z", 2, 0);
        DateParser.addName("gmt", 2, 0);
        DateParser.addName("ut", 2, 0);
        DateParser.addName("utc", 2, 0);
        DateParser.addName("est", 2, -300);
        DateParser.addName("edt", 2, -240);
        DateParser.addName("cst", 2, -360);
        DateParser.addName("cdt", 2, -300);
        DateParser.addName("mst", 2, -420);
        DateParser.addName("mdt", 2, -360);
        DateParser.addName("pst", 2, -480);
        DateParser.addName("pdt", 2, -420);
        DateParser.addName("t", 3, 0);
    }

    private static class Name {
        final String name;
        final String key;
        final int value;
        final int type;
        static final int DAY_OF_WEEK = -1;
        static final int MONTH_NAME = 0;
        static final int AM_PM = 1;
        static final int TIMEZONE_ID = 2;
        static final int TIME_SEPARATOR = 3;

        Name(String name, int type, int value) {
            assert (name != null);
            assert (name.equals(name.toLowerCase(Locale.ENGLISH)));
            this.name = name;
            this.key = name.substring(0, Math.min(3, name.length()));
            this.type = type;
            this.value = value;
        }

        public boolean matches(String str, int offset, int len) {
            return this.name.regionMatches(true, 0, str, offset, len);
        }

        public String toString() {
            return this.name;
        }
    }

    private static enum Token {
        UNKNOWN,
        NUMBER,
        SEPARATOR,
        PARENTHESIS,
        NAME,
        SIGN,
        END;

    }
}

