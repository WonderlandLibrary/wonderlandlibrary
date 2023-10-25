package org.apache.commons.lang3.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class StrTokenizer implements ListIterator<String>, Cloneable {
  private static final StrTokenizer CSV_TOKENIZER_PROTOTYPE = new StrTokenizer();
  
  static {
    CSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StrMatcher.commaMatcher());
    CSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StrMatcher.doubleQuoteMatcher());
    CSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StrMatcher.noneMatcher());
    CSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StrMatcher.trimMatcher());
    CSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
    CSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
  }
  
  private static final StrTokenizer TSV_TOKENIZER_PROTOTYPE = new StrTokenizer();
  
  private char[] chars;
  
  private String[] tokens;
  
  private int tokenPos;
  
  static {
    TSV_TOKENIZER_PROTOTYPE.setDelimiterMatcher(StrMatcher.tabMatcher());
    TSV_TOKENIZER_PROTOTYPE.setQuoteMatcher(StrMatcher.doubleQuoteMatcher());
    TSV_TOKENIZER_PROTOTYPE.setIgnoredMatcher(StrMatcher.noneMatcher());
    TSV_TOKENIZER_PROTOTYPE.setTrimmerMatcher(StrMatcher.trimMatcher());
    TSV_TOKENIZER_PROTOTYPE.setEmptyTokenAsNull(false);
    TSV_TOKENIZER_PROTOTYPE.setIgnoreEmptyTokens(false);
  }
  
  private StrMatcher delimMatcher = StrMatcher.splitMatcher();
  
  private StrMatcher quoteMatcher = StrMatcher.noneMatcher();
  
  private StrMatcher ignoredMatcher = StrMatcher.noneMatcher();
  
  private StrMatcher trimmerMatcher = StrMatcher.noneMatcher();
  
  private boolean emptyAsNull = false;
  
  private boolean ignoreEmptyTokens = true;
  
  private static StrTokenizer getCSVClone() {
    return (StrTokenizer)CSV_TOKENIZER_PROTOTYPE.clone();
  }
  
  public static StrTokenizer getCSVInstance() {
    return getCSVClone();
  }
  
  public static StrTokenizer getCSVInstance(String input) {
    StrTokenizer tok = getCSVClone();
    tok.reset(input);
    return tok;
  }
  
  public static StrTokenizer getCSVInstance(char[] input) {
    StrTokenizer tok = getCSVClone();
    tok.reset(input);
    return tok;
  }
  
  private static StrTokenizer getTSVClone() {
    return (StrTokenizer)TSV_TOKENIZER_PROTOTYPE.clone();
  }
  
  public static StrTokenizer getTSVInstance() {
    return getTSVClone();
  }
  
  public static StrTokenizer getTSVInstance(String input) {
    StrTokenizer tok = getTSVClone();
    tok.reset(input);
    return tok;
  }
  
  public static StrTokenizer getTSVInstance(char[] input) {
    StrTokenizer tok = getTSVClone();
    tok.reset(input);
    return tok;
  }
  
  public StrTokenizer() {
    this.chars = null;
  }
  
  public StrTokenizer(String input) {
    if (input != null) {
      this.chars = input.toCharArray();
    } else {
      this.chars = null;
    } 
  }
  
  public StrTokenizer(String input, char delim) {
    this(input);
    setDelimiterChar(delim);
  }
  
  public StrTokenizer(String input, String delim) {
    this(input);
    setDelimiterString(delim);
  }
  
  public StrTokenizer(String input, StrMatcher delim) {
    this(input);
    setDelimiterMatcher(delim);
  }
  
  public StrTokenizer(String input, char delim, char quote) {
    this(input, delim);
    setQuoteChar(quote);
  }
  
  public StrTokenizer(String input, StrMatcher delim, StrMatcher quote) {
    this(input, delim);
    setQuoteMatcher(quote);
  }
  
  public StrTokenizer(char[] input) {
    this.chars = ArrayUtils.clone(input);
  }
  
  public StrTokenizer(char[] input, char delim) {
    this(input);
    setDelimiterChar(delim);
  }
  
  public StrTokenizer(char[] input, String delim) {
    this(input);
    setDelimiterString(delim);
  }
  
  public StrTokenizer(char[] input, StrMatcher delim) {
    this(input);
    setDelimiterMatcher(delim);
  }
  
  public StrTokenizer(char[] input, char delim, char quote) {
    this(input, delim);
    setQuoteChar(quote);
  }
  
  public StrTokenizer(char[] input, StrMatcher delim, StrMatcher quote) {
    this(input, delim);
    setQuoteMatcher(quote);
  }
  
  public int size() {
    checkTokenized();
    return this.tokens.length;
  }
  
  public String nextToken() {
    if (hasNext())
      return this.tokens[this.tokenPos++]; 
    return null;
  }
  
  public String previousToken() {
    if (hasPrevious())
      return this.tokens[--this.tokenPos]; 
    return null;
  }
  
  public String[] getTokenArray() {
    checkTokenized();
    return (String[])this.tokens.clone();
  }
  
  public List<String> getTokenList() {
    checkTokenized();
    List<String> list = new ArrayList<String>(this.tokens.length);
    for (String element : this.tokens)
      list.add(element); 
    return list;
  }
  
  public StrTokenizer reset() {
    this.tokenPos = 0;
    this.tokens = null;
    return this;
  }
  
  public StrTokenizer reset(String input) {
    reset();
    if (input != null) {
      this.chars = input.toCharArray();
    } else {
      this.chars = null;
    } 
    return this;
  }
  
  public StrTokenizer reset(char[] input) {
    reset();
    this.chars = ArrayUtils.clone(input);
    return this;
  }
  
  public boolean hasNext() {
    checkTokenized();
    return (this.tokenPos < this.tokens.length);
  }
  
  public String next() {
    if (hasNext())
      return this.tokens[this.tokenPos++]; 
    throw new NoSuchElementException();
  }
  
  public int nextIndex() {
    return this.tokenPos;
  }
  
  public boolean hasPrevious() {
    checkTokenized();
    return (this.tokenPos > 0);
  }
  
  public String previous() {
    if (hasPrevious())
      return this.tokens[--this.tokenPos]; 
    throw new NoSuchElementException();
  }
  
  public int previousIndex() {
    return this.tokenPos - 1;
  }
  
  public void remove() {
    throw new UnsupportedOperationException("remove() is unsupported");
  }
  
  public void set(String obj) {
    throw new UnsupportedOperationException("set() is unsupported");
  }
  
  public void add(String obj) {
    throw new UnsupportedOperationException("add() is unsupported");
  }
  
  private void checkTokenized() {
    if (this.tokens == null)
      if (this.chars == null) {
        List<String> split = tokenize(null, 0, 0);
        this.tokens = split.<String>toArray(new String[split.size()]);
      } else {
        List<String> split = tokenize(this.chars, 0, this.chars.length);
        this.tokens = split.<String>toArray(new String[split.size()]);
      }  
  }
  
  protected List<String> tokenize(char[] srcChars, int offset, int count) {
    if (srcChars == null || count == 0)
      return Collections.emptyList(); 
    StrBuilder buf = new StrBuilder();
    List<String> tokenList = new ArrayList<String>();
    int pos = offset;
    while (pos >= 0 && pos < count) {
      pos = readNextToken(srcChars, pos, count, buf, tokenList);
      if (pos >= count)
        addToken(tokenList, ""); 
    } 
    return tokenList;
  }
  
  private void addToken(List<String> list, String tok) {
    if (StringUtils.isEmpty(tok)) {
      if (isIgnoreEmptyTokens())
        return; 
      if (isEmptyTokenAsNull())
        tok = null; 
    } 
    list.add(tok);
  }
  
  private int readNextToken(char[] srcChars, int start, int len, StrBuilder workArea, List<String> tokenList) {
    while (start < len) {
      int removeLen = Math.max(getIgnoredMatcher().isMatch(srcChars, start, start, len), getTrimmerMatcher().isMatch(srcChars, start, start, len));
      if (removeLen == 0 || getDelimiterMatcher().isMatch(srcChars, start, start, len) > 0 || getQuoteMatcher().isMatch(srcChars, start, start, len) > 0)
        break; 
      start += removeLen;
    } 
    if (start >= len) {
      addToken(tokenList, "");
      return -1;
    } 
    int delimLen = getDelimiterMatcher().isMatch(srcChars, start, start, len);
    if (delimLen > 0) {
      addToken(tokenList, "");
      return start + delimLen;
    } 
    int quoteLen = getQuoteMatcher().isMatch(srcChars, start, start, len);
    if (quoteLen > 0)
      return readWithQuotes(srcChars, start + quoteLen, len, workArea, tokenList, start, quoteLen); 
    return readWithQuotes(srcChars, start, len, workArea, tokenList, 0, 0);
  }
  
  private int readWithQuotes(char[] srcChars, int start, int len, StrBuilder workArea, List<String> tokenList, int quoteStart, int quoteLen) {
    workArea.clear();
    int pos = start;
    boolean quoting = (quoteLen > 0);
    int trimStart = 0;
    while (pos < len) {
      if (quoting) {
        if (isQuote(srcChars, pos, len, quoteStart, quoteLen)) {
          if (isQuote(srcChars, pos + quoteLen, len, quoteStart, quoteLen)) {
            workArea.append(srcChars, pos, quoteLen);
            pos += quoteLen * 2;
            trimStart = workArea.size();
            continue;
          } 
          quoting = false;
          pos += quoteLen;
          continue;
        } 
        workArea.append(srcChars[pos++]);
        trimStart = workArea.size();
        continue;
      } 
      int delimLen = getDelimiterMatcher().isMatch(srcChars, pos, start, len);
      if (delimLen > 0) {
        addToken(tokenList, workArea.substring(0, trimStart));
        return pos + delimLen;
      } 
      if (quoteLen > 0 && isQuote(srcChars, pos, len, quoteStart, quoteLen)) {
        quoting = true;
        pos += quoteLen;
        continue;
      } 
      int ignoredLen = getIgnoredMatcher().isMatch(srcChars, pos, start, len);
      if (ignoredLen > 0) {
        pos += ignoredLen;
        continue;
      } 
      int trimmedLen = getTrimmerMatcher().isMatch(srcChars, pos, start, len);
      if (trimmedLen > 0) {
        workArea.append(srcChars, pos, trimmedLen);
        pos += trimmedLen;
        continue;
      } 
      workArea.append(srcChars[pos++]);
      trimStart = workArea.size();
    } 
    addToken(tokenList, workArea.substring(0, trimStart));
    return -1;
  }
  
  private boolean isQuote(char[] srcChars, int pos, int len, int quoteStart, int quoteLen) {
    for (int i = 0; i < quoteLen; i++) {
      if (pos + i >= len || srcChars[pos + i] != srcChars[quoteStart + i])
        return false; 
    } 
    return true;
  }
  
  public StrMatcher getDelimiterMatcher() {
    return this.delimMatcher;
  }
  
  public StrTokenizer setDelimiterMatcher(StrMatcher delim) {
    if (delim == null) {
      this.delimMatcher = StrMatcher.noneMatcher();
    } else {
      this.delimMatcher = delim;
    } 
    return this;
  }
  
  public StrTokenizer setDelimiterChar(char delim) {
    return setDelimiterMatcher(StrMatcher.charMatcher(delim));
  }
  
  public StrTokenizer setDelimiterString(String delim) {
    return setDelimiterMatcher(StrMatcher.stringMatcher(delim));
  }
  
  public StrMatcher getQuoteMatcher() {
    return this.quoteMatcher;
  }
  
  public StrTokenizer setQuoteMatcher(StrMatcher quote) {
    if (quote != null)
      this.quoteMatcher = quote; 
    return this;
  }
  
  public StrTokenizer setQuoteChar(char quote) {
    return setQuoteMatcher(StrMatcher.charMatcher(quote));
  }
  
  public StrMatcher getIgnoredMatcher() {
    return this.ignoredMatcher;
  }
  
  public StrTokenizer setIgnoredMatcher(StrMatcher ignored) {
    if (ignored != null)
      this.ignoredMatcher = ignored; 
    return this;
  }
  
  public StrTokenizer setIgnoredChar(char ignored) {
    return setIgnoredMatcher(StrMatcher.charMatcher(ignored));
  }
  
  public StrMatcher getTrimmerMatcher() {
    return this.trimmerMatcher;
  }
  
  public StrTokenizer setTrimmerMatcher(StrMatcher trimmer) {
    if (trimmer != null)
      this.trimmerMatcher = trimmer; 
    return this;
  }
  
  public boolean isEmptyTokenAsNull() {
    return this.emptyAsNull;
  }
  
  public StrTokenizer setEmptyTokenAsNull(boolean emptyAsNull) {
    this.emptyAsNull = emptyAsNull;
    return this;
  }
  
  public boolean isIgnoreEmptyTokens() {
    return this.ignoreEmptyTokens;
  }
  
  public StrTokenizer setIgnoreEmptyTokens(boolean ignoreEmptyTokens) {
    this.ignoreEmptyTokens = ignoreEmptyTokens;
    return this;
  }
  
  public String getContent() {
    if (this.chars == null)
      return null; 
    return new String(this.chars);
  }
  
  public Object clone() {
    try {
      return cloneReset();
    } catch (CloneNotSupportedException ex) {
      return null;
    } 
  }
  
  Object cloneReset() throws CloneNotSupportedException {
    StrTokenizer cloned = (StrTokenizer)super.clone();
    if (cloned.chars != null)
      cloned.chars = (char[])cloned.chars.clone(); 
    cloned.reset();
    return cloned;
  }
  
  public String toString() {
    if (this.tokens == null)
      return "StrTokenizer[not tokenized yet]"; 
    return "StrTokenizer" + getTokenList();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\text\StrTokenizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */