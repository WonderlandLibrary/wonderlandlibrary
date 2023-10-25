/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.Utility;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;

public class PatternTokenizer {
    private UnicodeSet ignorableCharacters = new UnicodeSet();
    private UnicodeSet syntaxCharacters = new UnicodeSet();
    private UnicodeSet extraQuotingCharacters = new UnicodeSet();
    private UnicodeSet escapeCharacters = new UnicodeSet();
    private boolean usingSlash = false;
    private boolean usingQuote = false;
    private transient UnicodeSet needingQuoteCharacters = null;
    private int start;
    private int limit;
    private String pattern;
    public static final char SINGLE_QUOTE = '\'';
    public static final char BACK_SLASH = '\\';
    private static int NO_QUOTE = -1;
    private static int IN_QUOTE = -2;
    public static final int DONE = 0;
    public static final int SYNTAX = 1;
    public static final int LITERAL = 2;
    public static final int BROKEN_QUOTE = 3;
    public static final int BROKEN_ESCAPE = 4;
    public static final int UNKNOWN = 5;
    private static final int AFTER_QUOTE = -1;
    private static final int NONE = 0;
    private static final int START_QUOTE = 1;
    private static final int NORMAL_QUOTE = 2;
    private static final int SLASH_START = 3;
    private static final int HEX = 4;

    public UnicodeSet getIgnorableCharacters() {
        return (UnicodeSet)this.ignorableCharacters.clone();
    }

    public PatternTokenizer setIgnorableCharacters(UnicodeSet ignorableCharacters) {
        this.ignorableCharacters = (UnicodeSet)ignorableCharacters.clone();
        this.needingQuoteCharacters = null;
        return this;
    }

    public UnicodeSet getSyntaxCharacters() {
        return (UnicodeSet)this.syntaxCharacters.clone();
    }

    public UnicodeSet getExtraQuotingCharacters() {
        return (UnicodeSet)this.extraQuotingCharacters.clone();
    }

    public PatternTokenizer setSyntaxCharacters(UnicodeSet syntaxCharacters) {
        this.syntaxCharacters = (UnicodeSet)syntaxCharacters.clone();
        this.needingQuoteCharacters = null;
        return this;
    }

    public PatternTokenizer setExtraQuotingCharacters(UnicodeSet syntaxCharacters) {
        this.extraQuotingCharacters = (UnicodeSet)syntaxCharacters.clone();
        this.needingQuoteCharacters = null;
        return this;
    }

    public UnicodeSet getEscapeCharacters() {
        return (UnicodeSet)this.escapeCharacters.clone();
    }

    public PatternTokenizer setEscapeCharacters(UnicodeSet escapeCharacters) {
        this.escapeCharacters = (UnicodeSet)escapeCharacters.clone();
        return this;
    }

    public boolean isUsingQuote() {
        return this.usingQuote;
    }

    public PatternTokenizer setUsingQuote(boolean usingQuote) {
        this.usingQuote = usingQuote;
        this.needingQuoteCharacters = null;
        return this;
    }

    public boolean isUsingSlash() {
        return this.usingSlash;
    }

    public PatternTokenizer setUsingSlash(boolean usingSlash) {
        this.usingSlash = usingSlash;
        this.needingQuoteCharacters = null;
        return this;
    }

    public int getLimit() {
        return this.limit;
    }

    public PatternTokenizer setLimit(int limit) {
        this.limit = limit;
        return this;
    }

    public int getStart() {
        return this.start;
    }

    public PatternTokenizer setStart(int start) {
        this.start = start;
        return this;
    }

    public PatternTokenizer setPattern(CharSequence pattern) {
        return this.setPattern(((Object)pattern).toString());
    }

    public PatternTokenizer setPattern(String pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Inconsistent arguments");
        }
        this.start = 0;
        this.limit = pattern.length();
        this.pattern = pattern;
        return this;
    }

    public String quoteLiteral(CharSequence string) {
        return this.quoteLiteral(((Object)string).toString());
    }

    public String quoteLiteral(String string) {
        int cp;
        if (this.needingQuoteCharacters == null) {
            this.needingQuoteCharacters = new UnicodeSet().addAll(this.syntaxCharacters).addAll(this.ignorableCharacters).addAll(this.extraQuotingCharacters);
            if (this.usingSlash) {
                this.needingQuoteCharacters.add(92);
            }
            if (this.usingQuote) {
                this.needingQuoteCharacters.add(39);
            }
        }
        StringBuffer result = new StringBuffer();
        int quotedChar = NO_QUOTE;
        for (int i = 0; i < string.length(); i += UTF16.getCharCount(cp)) {
            cp = UTF16.charAt(string, i);
            if (this.escapeCharacters.contains(cp)) {
                if (quotedChar == IN_QUOTE) {
                    result.append('\'');
                    quotedChar = NO_QUOTE;
                }
                this.appendEscaped(result, cp);
                continue;
            }
            if (this.needingQuoteCharacters.contains(cp)) {
                if (quotedChar == IN_QUOTE) {
                    UTF16.append(result, cp);
                    if (!this.usingQuote || cp != 39) continue;
                    result.append('\'');
                    continue;
                }
                if (this.usingSlash) {
                    result.append('\\');
                    UTF16.append(result, cp);
                    continue;
                }
                if (this.usingQuote) {
                    if (cp == 39) {
                        result.append('\'');
                        result.append('\'');
                        continue;
                    }
                    result.append('\'');
                    UTF16.append(result, cp);
                    quotedChar = IN_QUOTE;
                    continue;
                }
                this.appendEscaped(result, cp);
                continue;
            }
            if (quotedChar == IN_QUOTE) {
                result.append('\'');
                quotedChar = NO_QUOTE;
            }
            UTF16.append(result, cp);
        }
        if (quotedChar == IN_QUOTE) {
            result.append('\'');
        }
        return result.toString();
    }

    private void appendEscaped(StringBuffer result, int cp) {
        if (cp <= 65535) {
            result.append("\\u").append(Utility.hex(cp, 4));
        } else {
            result.append("\\U").append(Utility.hex(cp, 8));
        }
    }

    public String normalize() {
        int oldStart = this.start;
        StringBuffer result = new StringBuffer();
        StringBuffer buffer = new StringBuffer();
        while (true) {
            buffer.setLength(0);
            int status = this.next(buffer);
            if (status == 0) {
                this.start = oldStart;
                return result.toString();
            }
            if (status != 1) {
                result.append(this.quoteLiteral(buffer));
                continue;
            }
            result.append(buffer);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public int next(StringBuffer buffer) {
        int cp;
        if (this.start >= this.limit) {
            return 0;
        }
        int status = 5;
        int lastQuote = 5;
        int quoteStatus = 0;
        int hexCount = 0;
        int hexValue = 0;
        block21: for (int i = this.start; i < this.limit; i += UTF16.getCharCount(cp)) {
            cp = UTF16.charAt(this.pattern, i);
            switch (quoteStatus) {
                case 3: {
                    switch (cp) {
                        case 117: {
                            quoteStatus = 4;
                            hexCount = 4;
                            hexValue = 0;
                            continue block21;
                        }
                        case 85: {
                            quoteStatus = 4;
                            hexCount = 8;
                            hexValue = 0;
                            continue block21;
                        }
                        default: {
                            if (this.usingSlash) {
                                UTF16.append(buffer, cp);
                                quoteStatus = 0;
                                continue block21;
                            }
                            buffer.append('\\');
                            quoteStatus = 0;
                            break;
                        }
                    }
                    break;
                }
                case 4: {
                    hexValue <<= 4;
                    hexValue += cp;
                    switch (cp) {
                        case 48: 
                        case 49: 
                        case 50: 
                        case 51: 
                        case 52: 
                        case 53: 
                        case 54: 
                        case 55: 
                        case 56: 
                        case 57: {
                            hexValue -= 48;
                            break;
                        }
                        case 97: 
                        case 98: 
                        case 99: 
                        case 100: 
                        case 101: 
                        case 102: {
                            hexValue -= 87;
                            break;
                        }
                        case 65: 
                        case 66: 
                        case 67: 
                        case 68: 
                        case 69: 
                        case 70: {
                            hexValue -= 55;
                            break;
                        }
                        default: {
                            this.start = i;
                            return 4;
                        }
                    }
                    if (--hexCount != 0) continue block21;
                    quoteStatus = 0;
                    UTF16.append(buffer, hexValue);
                    continue block21;
                }
                case -1: {
                    if (cp == lastQuote) {
                        UTF16.append(buffer, cp);
                        quoteStatus = 2;
                        continue block21;
                    }
                    quoteStatus = 0;
                    break;
                }
                case 1: {
                    if (cp == lastQuote) {
                        UTF16.append(buffer, cp);
                        quoteStatus = 0;
                        continue block21;
                    }
                    UTF16.append(buffer, cp);
                    quoteStatus = 2;
                    continue block21;
                }
                case 2: {
                    if (cp == lastQuote) {
                        quoteStatus = -1;
                        continue block21;
                    }
                    UTF16.append(buffer, cp);
                    continue block21;
                }
            }
            if (this.ignorableCharacters.contains(cp)) continue;
            if (this.syntaxCharacters.contains(cp)) {
                if (status == 5) {
                    UTF16.append(buffer, cp);
                    this.start = i + UTF16.getCharCount(cp);
                    return 1;
                }
                this.start = i;
                return status;
            }
            status = 2;
            if (cp == 92) {
                quoteStatus = 3;
                continue;
            }
            if (this.usingQuote && cp == 39) {
                lastQuote = cp;
                quoteStatus = 1;
                continue;
            }
            UTF16.append(buffer, cp);
        }
        this.start = this.limit;
        switch (quoteStatus) {
            case 4: {
                return 4;
            }
            case 3: {
                if (this.usingSlash) {
                    return 4;
                }
                buffer.append('\\');
                return status;
            }
            case 1: 
            case 2: {
                return 3;
            }
        }
        return status;
    }
}

