// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.utils.text;

import java.util.NoSuchElementException;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import dev.tenacity.utils.text.matcher.StringMatcherFactory;
import dev.tenacity.utils.text.matcher.StringMatcher;
import java.util.ListIterator;

public class StringTokenizer implements ListIterator<String>, Cloneable
{
    private static final StringTokenizer CSV_TOKENIZER_PROTOTYPE;
    private static final StringTokenizer TSV_TOKENIZER_PROTOTYPE;
    private char[] chars;
    private String[] tokens;
    private int tokenPos;
    private StringMatcher delimMatcher;
    private StringMatcher quoteMatcher;
    private StringMatcher ignoredMatcher;
    private StringMatcher trimmerMatcher;
    private boolean emptyAsNull;
    private boolean ignoreEmptyTokens;
    
    private static StringTokenizer getCSVClone() {
        return (StringTokenizer)StringTokenizer.CSV_TOKENIZER_PROTOTYPE.clone();
    }
    
    public static StringTokenizer getCSVInstance() {
        return getCSVClone();
    }
    
    public static StringTokenizer getCSVInstance(final String input) {
        final StringTokenizer tok = getCSVClone();
        tok.reset(input);
        return tok;
    }
    
    public static StringTokenizer getCSVInstance(final char[] input) {
        final StringTokenizer tok = getCSVClone();
        tok.reset(input);
        return tok;
    }
    
    private static StringTokenizer getTSVClone() {
        return (StringTokenizer)StringTokenizer.TSV_TOKENIZER_PROTOTYPE.clone();
    }
    
    public static StringTokenizer getTSVInstance() {
        return getTSVClone();
    }
    
    public static StringTokenizer getTSVInstance(final String input) {
        final StringTokenizer tok = getTSVClone();
        tok.reset(input);
        return tok;
    }
    
    public static StringTokenizer getTSVInstance(final char[] input) {
        final StringTokenizer tok = getTSVClone();
        tok.reset(input);
        return tok;
    }
    
    public StringTokenizer() {
        this.delimMatcher = StringMatcherFactory.INSTANCE.splitMatcher();
        this.quoteMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
        this.ignoredMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
        this.trimmerMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
        this.emptyAsNull = false;
        this.ignoreEmptyTokens = true;
        this.chars = null;
    }
    
    public StringTokenizer(final String input) {
        this.delimMatcher = StringMatcherFactory.INSTANCE.splitMatcher();
        this.quoteMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
        this.ignoredMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
        this.trimmerMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
        this.emptyAsNull = false;
        this.ignoreEmptyTokens = true;
        if (input != null) {
            this.chars = input.toCharArray();
        }
        else {
            this.chars = null;
        }
    }
    
    public StringTokenizer(final String input, final char delim) {
        this(input);
        this.setDelimiterChar(delim);
    }
    
    public StringTokenizer(final String input, final String delim) {
        this(input);
        this.setDelimiterString(delim);
    }
    
    public StringTokenizer(final String input, final StringMatcher delim) {
        this(input);
        this.setDelimiterMatcher(delim);
    }
    
    public StringTokenizer(final String input, final char delim, final char quote) {
        this(input, delim);
        this.setQuoteChar(quote);
    }
    
    public StringTokenizer(final String input, final StringMatcher delim, final StringMatcher quote) {
        this(input, delim);
        this.setQuoteMatcher(quote);
    }
    
    public StringTokenizer(final char[] input) {
        this.delimMatcher = StringMatcherFactory.INSTANCE.splitMatcher();
        this.quoteMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
        this.ignoredMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
        this.trimmerMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
        this.emptyAsNull = false;
        this.ignoreEmptyTokens = true;
        if (input == null) {
            this.chars = null;
        }
        else {
            this.chars = input.clone();
        }
    }
    
    public StringTokenizer(final char[] input, final char delim) {
        this(input);
        this.setDelimiterChar(delim);
    }
    
    public StringTokenizer(final char[] input, final String delim) {
        this(input);
        this.setDelimiterString(delim);
    }
    
    public StringTokenizer(final char[] input, final StringMatcher delim) {
        this(input);
        this.setDelimiterMatcher(delim);
    }
    
    public StringTokenizer(final char[] input, final char delim, final char quote) {
        this(input, delim);
        this.setQuoteChar(quote);
    }
    
    public StringTokenizer(final char[] input, final StringMatcher delim, final StringMatcher quote) {
        this(input, delim);
        this.setQuoteMatcher(quote);
    }
    
    public int size() {
        this.checkTokenized();
        return this.tokens.length;
    }
    
    public String nextToken() {
        if (this.hasNext()) {
            return this.tokens[this.tokenPos++];
        }
        return null;
    }
    
    public String previousToken() {
        if (this.hasPrevious()) {
            final String[] tokens = this.tokens;
            final int tokenPos = this.tokenPos - 1;
            this.tokenPos = tokenPos;
            return tokens[tokenPos];
        }
        return null;
    }
    
    public String[] getTokenArray() {
        this.checkTokenized();
        return this.tokens.clone();
    }
    
    public List<String> getTokenList() {
        this.checkTokenized();
        final List<String> list = new ArrayList<String>(this.tokens.length);
        Collections.addAll(list, this.tokens);
        return list;
    }
    
    public StringTokenizer reset() {
        this.tokenPos = 0;
        this.tokens = null;
        return this;
    }
    
    public StringTokenizer reset(final String input) {
        this.reset();
        if (input != null) {
            this.chars = input.toCharArray();
        }
        else {
            this.chars = null;
        }
        return this;
    }
    
    public StringTokenizer reset(final char[] input) {
        this.reset();
        if (input != null) {
            this.chars = input.clone();
        }
        else {
            this.chars = null;
        }
        return this;
    }
    
    @Override
    public boolean hasNext() {
        this.checkTokenized();
        return this.tokenPos < this.tokens.length;
    }
    
    @Override
    public String next() {
        if (this.hasNext()) {
            return this.tokens[this.tokenPos++];
        }
        throw new NoSuchElementException();
    }
    
    @Override
    public int nextIndex() {
        return this.tokenPos;
    }
    
    @Override
    public boolean hasPrevious() {
        this.checkTokenized();
        return this.tokenPos > 0;
    }
    
    @Override
    public String previous() {
        if (this.hasPrevious()) {
            final String[] tokens = this.tokens;
            final int tokenPos = this.tokenPos - 1;
            this.tokenPos = tokenPos;
            return tokens[tokenPos];
        }
        throw new NoSuchElementException();
    }
    
    @Override
    public int previousIndex() {
        return this.tokenPos - 1;
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove() is unsupported");
    }
    
    @Override
    public void set(final String obj) {
        throw new UnsupportedOperationException("set() is unsupported");
    }
    
    @Override
    public void add(final String obj) {
        throw new UnsupportedOperationException("add() is unsupported");
    }
    
    private void checkTokenized() {
        if (this.tokens == null) {
            if (this.chars == null) {
                final List<String> split = this.tokenize(null, 0, 0);
                this.tokens = split.toArray(new String[0]);
            }
            else {
                final List<String> split = this.tokenize(this.chars, 0, this.chars.length);
                this.tokens = split.toArray(new String[0]);
            }
        }
    }
    
    protected List<String> tokenize(final char[] srcChars, final int offset, final int count) {
        if (srcChars == null || count == 0) {
            return Collections.emptyList();
        }
        final TextStringBuilder buf = new TextStringBuilder();
        final List<String> tokenList = new ArrayList<String>();
        int pos = offset;
        while (pos >= 0 && pos < count) {
            pos = this.readNextToken(srcChars, pos, count, buf, tokenList);
            if (pos >= count) {
                this.addToken(tokenList, "");
            }
        }
        return tokenList;
    }
    
    private void addToken(final List<String> list, String tok) {
        if (tok == null || tok.length() == 0) {
            if (this.isIgnoreEmptyTokens()) {
                return;
            }
            if (this.isEmptyTokenAsNull()) {
                tok = null;
            }
        }
        list.add(tok);
    }
    
    private int readNextToken(final char[] srcChars, int start, final int len, final TextStringBuilder workArea, final List<String> tokenList) {
        while (start < len) {
            final int removeLen = Math.max(this.getIgnoredMatcher().isMatch(srcChars, start, start, len), this.getTrimmerMatcher().isMatch(srcChars, start, start, len));
            if (removeLen == 0 || this.getDelimiterMatcher().isMatch(srcChars, start, start, len) > 0) {
                break;
            }
            if (this.getQuoteMatcher().isMatch(srcChars, start, start, len) > 0) {
                break;
            }
            start += removeLen;
        }
        if (start >= len) {
            this.addToken(tokenList, "");
            return -1;
        }
        final int delimLen = this.getDelimiterMatcher().isMatch(srcChars, start, start, len);
        if (delimLen > 0) {
            this.addToken(tokenList, "");
            return start + delimLen;
        }
        final int quoteLen = this.getQuoteMatcher().isMatch(srcChars, start, start, len);
        if (quoteLen > 0) {
            return this.readWithQuotes(srcChars, start + quoteLen, len, workArea, tokenList, start, quoteLen);
        }
        return this.readWithQuotes(srcChars, start, len, workArea, tokenList, 0, 0);
    }
    
    private int readWithQuotes(final char[] srcChars, final int start, final int len, final TextStringBuilder workArea, final List<String> tokenList, final int quoteStart, final int quoteLen) {
        workArea.clear();
        int pos = start;
        boolean quoting = quoteLen > 0;
        int trimStart = 0;
        while (pos < len) {
            if (quoting) {
                if (this.isQuote(srcChars, pos, len, quoteStart, quoteLen)) {
                    if (this.isQuote(srcChars, pos + quoteLen, len, quoteStart, quoteLen)) {
                        workArea.append(srcChars, pos, quoteLen);
                        pos += quoteLen * 2;
                        trimStart = workArea.size();
                        continue;
                    }
                    quoting = false;
                    pos += quoteLen;
                    continue;
                }
            }
            else {
                final int delimLen = this.getDelimiterMatcher().isMatch(srcChars, pos, start, len);
                if (delimLen > 0) {
                    this.addToken(tokenList, workArea.substring(0, trimStart));
                    return pos + delimLen;
                }
                if (quoteLen > 0 && this.isQuote(srcChars, pos, len, quoteStart, quoteLen)) {
                    quoting = true;
                    pos += quoteLen;
                    continue;
                }
                final int ignoredLen = this.getIgnoredMatcher().isMatch(srcChars, pos, start, len);
                if (ignoredLen > 0) {
                    pos += ignoredLen;
                    continue;
                }
                final int trimmedLen = this.getTrimmerMatcher().isMatch(srcChars, pos, start, len);
                if (trimmedLen > 0) {
                    workArea.append(srcChars, pos, trimmedLen);
                    pos += trimmedLen;
                    continue;
                }
            }
            workArea.append(srcChars[pos++]);
            trimStart = workArea.size();
        }
        this.addToken(tokenList, workArea.substring(0, trimStart));
        return -1;
    }
    
    private boolean isQuote(final char[] srcChars, final int pos, final int len, final int quoteStart, final int quoteLen) {
        for (int i = 0; i < quoteLen; ++i) {
            if (pos + i >= len || srcChars[pos + i] != srcChars[quoteStart + i]) {
                return false;
            }
        }
        return true;
    }
    
    public StringMatcher getDelimiterMatcher() {
        return this.delimMatcher;
    }
    
    public StringTokenizer setDelimiterMatcher(final StringMatcher delim) {
        if (delim == null) {
            this.delimMatcher = StringMatcherFactory.INSTANCE.noneMatcher();
        }
        else {
            this.delimMatcher = delim;
        }
        return this;
    }
    
    public StringTokenizer setDelimiterChar(final char delim) {
        return this.setDelimiterMatcher(StringMatcherFactory.INSTANCE.charMatcher(delim));
    }
    
    public StringTokenizer setDelimiterString(final String delim) {
        return this.setDelimiterMatcher(StringMatcherFactory.INSTANCE.stringMatcher(delim));
    }
    
    public StringMatcher getQuoteMatcher() {
        return this.quoteMatcher;
    }
    
    public StringTokenizer setQuoteMatcher(final StringMatcher quote) {
        if (quote != null) {
            this.quoteMatcher = quote;
        }
        return this;
    }
    
    public StringTokenizer setQuoteChar(final char quote) {
        return this.setQuoteMatcher(StringMatcherFactory.INSTANCE.charMatcher(quote));
    }
    
    public StringMatcher getIgnoredMatcher() {
        return this.ignoredMatcher;
    }
    
    public StringTokenizer setIgnoredMatcher(final StringMatcher ignored) {
        if (ignored != null) {
            this.ignoredMatcher = ignored;
        }
        return this;
    }
    
    public StringTokenizer setIgnoredChar(final char ignored) {
        return this.setIgnoredMatcher(StringMatcherFactory.INSTANCE.charMatcher(ignored));
    }
    
    public StringMatcher getTrimmerMatcher() {
        return this.trimmerMatcher;
    }
    
    public StringTokenizer setTrimmerMatcher(final StringMatcher trimmer) {
        if (trimmer != null) {
            this.trimmerMatcher = trimmer;
        }
        return this;
    }
    
    public boolean isEmptyTokenAsNull() {
        return this.emptyAsNull;
    }
    
    public StringTokenizer setEmptyTokenAsNull(final boolean emptyAsNull) {
        this.emptyAsNull = emptyAsNull;
        return this;
    }
    
    public boolean isIgnoreEmptyTokens() {
        return this.ignoreEmptyTokens;
    }
    
    public StringTokenizer setIgnoreEmptyTokens(final boolean ignoreEmptyTokens) {
        this.ignoreEmptyTokens = ignoreEmptyTokens;
        return this;
    }
    
    public String getContent() {
        if (this.chars == null) {
            return null;
        }
        return new String(this.chars);
    }
    
    public Object clone() {
        try {
            return this.cloneReset();
        }
        catch (CloneNotSupportedException ex) {
            return null;
        }
    }
    
    Object cloneReset() throws CloneNotSupportedException {
        final StringTokenizer cloned = (StringTokenizer)super.clone();
        if (cloned.chars != null) {
            cloned.chars = cloned.chars.clone();
        }
        cloned.reset();
        return cloned;
    }
    
    @Override
    public String toString() {
        if (this.tokens == null) {
            return "StringTokenizer[not tokenized yet]";
        }
        return "StringTokenizer" + this.getTokenList();
    }
    
    static {
        (CSV_TOKENIZER_PROTOTYPE = new StringTokenizer()).setDelimiterMatcher(StringMatcherFactory.INSTANCE.commaMatcher());
        StringTokenizer.CSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StringMatcherFactory.INSTANCE.doubleQuoteMatcher());
        StringTokenizer.CSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StringMatcherFactory.INSTANCE.noneMatcher());
        StringTokenizer.CSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StringMatcherFactory.INSTANCE.trimMatcher());
        StringTokenizer.CSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
        StringTokenizer.CSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
        (TSV_TOKENIZER_PROTOTYPE = new StringTokenizer()).setDelimiterMatcher(StringMatcherFactory.INSTANCE.tabMatcher());
        StringTokenizer.TSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StringMatcherFactory.INSTANCE.doubleQuoteMatcher());
        StringTokenizer.TSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StringMatcherFactory.INSTANCE.noneMatcher());
        StringTokenizer.TSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StringMatcherFactory.INSTANCE.trimMatcher());
        StringTokenizer.TSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
        StringTokenizer.TSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
    }
}
