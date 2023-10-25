package com.ibm.icu.text;

import com.ibm.icu.impl.BMPSet;
import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.RuleCharacterIterator;
import com.ibm.icu.impl.SortedSetRelation;
import com.ibm.icu.impl.UBiDiProps;
import com.ibm.icu.impl.UCaseProps;
import com.ibm.icu.impl.UCharacterProperty;
import com.ibm.icu.impl.UPropertyAliases;
import com.ibm.icu.impl.UnicodeSetStringSpan;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.lang.CharSequences;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UScript;
import com.ibm.icu.util.Freezable;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.VersionInfo;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeSet;

public class UnicodeSet extends UnicodeFilter implements Iterable<String>, Comparable<UnicodeSet>, Freezable<UnicodeSet> {
  public static final UnicodeSet EMPTY = (new UnicodeSet()).freeze();
  
  public static final UnicodeSet ALL_CODE_POINTS = (new UnicodeSet(0, 1114111)).freeze();
  
  private static XSymbolTable XSYMBOL_TABLE = null;
  
  private static final int LOW = 0;
  
  private static final int HIGH = 1114112;
  
  public static final int MIN_VALUE = 0;
  
  public static final int MAX_VALUE = 1114111;
  
  private int len;
  
  private int[] list;
  
  private int[] rangeList;
  
  private int[] buffer;
  
  TreeSet<String> strings = new TreeSet<String>();
  
  private String pat = null;
  
  private static final int START_EXTRA = 16;
  
  private static final int GROW_EXTRA = 16;
  
  private static final String ANY_ID = "ANY";
  
  private static final String ASCII_ID = "ASCII";
  
  private static final String ASSIGNED = "Assigned";
  
  private static UnicodeSet[] INCLUSIONS = null;
  
  private BMPSet bmpSet;
  
  private UnicodeSetStringSpan stringSpan;
  
  public UnicodeSet() {
    this.list = new int[17];
    this.list[this.len++] = 1114112;
  }
  
  public UnicodeSet(UnicodeSet other) {
    set(other);
  }
  
  public UnicodeSet(int start, int end) {
    this();
    complement(start, end);
  }
  
  public UnicodeSet(int... pairs) {
    if ((pairs.length & 0x1) != 0)
      throw new IllegalArgumentException("Must have even number of integers"); 
    this.list = new int[pairs.length + 1];
    this.len = this.list.length;
    int last = -1;
    int i = 0;
    while (i < pairs.length) {
      int start = pairs[i];
      if (last >= start)
        throw new IllegalArgumentException("Must be monotonically increasing."); 
      this.list[i++] = last = start;
      int end = pairs[i] + 1;
      if (last >= end)
        throw new IllegalArgumentException("Must be monotonically increasing."); 
      this.list[i++] = last = end;
    } 
    this.list[i] = 1114112;
  }
  
  public UnicodeSet(String pattern) {
    this();
    applyPattern(pattern, (ParsePosition)null, (SymbolTable)null, 1);
  }
  
  public UnicodeSet(String pattern, boolean ignoreWhitespace) {
    this();
    applyPattern(pattern, (ParsePosition)null, (SymbolTable)null, ignoreWhitespace ? 1 : 0);
  }
  
  public UnicodeSet(String pattern, int options) {
    this();
    applyPattern(pattern, (ParsePosition)null, (SymbolTable)null, options);
  }
  
  public UnicodeSet(String pattern, ParsePosition pos, SymbolTable symbols) {
    this();
    applyPattern(pattern, pos, symbols, 1);
  }
  
  public UnicodeSet(String pattern, ParsePosition pos, SymbolTable symbols, int options) {
    this();
    applyPattern(pattern, pos, symbols, options);
  }
  
  public Object clone() {
    UnicodeSet result = new UnicodeSet(this);
    result.bmpSet = this.bmpSet;
    result.stringSpan = this.stringSpan;
    return result;
  }
  
  public UnicodeSet set(int start, int end) {
    checkFrozen();
    clear();
    complement(start, end);
    return this;
  }
  
  public UnicodeSet set(UnicodeSet other) {
    checkFrozen();
    this.list = (int[])other.list.clone();
    this.len = other.len;
    this.pat = other.pat;
    this.strings = new TreeSet<String>(other.strings);
    return this;
  }
  
  public final UnicodeSet applyPattern(String pattern) {
    checkFrozen();
    return applyPattern(pattern, (ParsePosition)null, (SymbolTable)null, 1);
  }
  
  public UnicodeSet applyPattern(String pattern, boolean ignoreWhitespace) {
    checkFrozen();
    return applyPattern(pattern, (ParsePosition)null, (SymbolTable)null, ignoreWhitespace ? 1 : 0);
  }
  
  public UnicodeSet applyPattern(String pattern, int options) {
    checkFrozen();
    return applyPattern(pattern, (ParsePosition)null, (SymbolTable)null, options);
  }
  
  public static boolean resemblesPattern(String pattern, int pos) {
    return ((pos + 1 < pattern.length() && pattern.charAt(pos) == '[') || resemblesPropertyPattern(pattern, pos));
  }
  
  private static void _appendToPat(StringBuffer buf, String s, boolean escapeUnprintable) {
    int i;
    for (i = 0; i < s.length(); i += Character.charCount(cp)) {
      int cp = s.codePointAt(i);
      _appendToPat(buf, cp, escapeUnprintable);
    } 
  }
  
  private static void _appendToPat(StringBuffer buf, int c, boolean escapeUnprintable) {
    if (escapeUnprintable && Utility.isUnprintable(c))
      if (Utility.escapeUnprintable(buf, c))
        return;  
    switch (c) {
      case 36:
      case 38:
      case 45:
      case 58:
      case 91:
      case 92:
      case 93:
      case 94:
      case 123:
      case 125:
        buf.append('\\');
        break;
      default:
        if (PatternProps.isWhiteSpace(c))
          buf.append('\\'); 
        break;
    } 
    UTF16.append(buf, c);
  }
  
  public String toPattern(boolean escapeUnprintable) {
    StringBuffer result = new StringBuffer();
    return _toPattern(result, escapeUnprintable).toString();
  }
  
  private StringBuffer _toPattern(StringBuffer result, boolean escapeUnprintable) {
    if (this.pat != null) {
      int backslashCount = 0;
      for (int i = 0; i < this.pat.length(); ) {
        int c = UTF16.charAt(this.pat, i);
        i += UTF16.getCharCount(c);
        if (escapeUnprintable && Utility.isUnprintable(c)) {
          if (backslashCount % 2 != 0)
            result.setLength(result.length() - 1); 
          Utility.escapeUnprintable(result, c);
          backslashCount = 0;
          continue;
        } 
        UTF16.append(result, c);
        if (c == 92) {
          backslashCount++;
          continue;
        } 
        backslashCount = 0;
      } 
      return result;
    } 
    return _generatePattern(result, escapeUnprintable, true);
  }
  
  public StringBuffer _generatePattern(StringBuffer result, boolean escapeUnprintable) {
    return _generatePattern(result, escapeUnprintable, true);
  }
  
  public StringBuffer _generatePattern(StringBuffer result, boolean escapeUnprintable, boolean includeStrings) {
    result.append('[');
    int count = getRangeCount();
    if (count > 1 && getRangeStart(0) == 0 && getRangeEnd(count - 1) == 1114111) {
      result.append('^');
      for (int i = 1; i < count; i++) {
        int start = getRangeEnd(i - 1) + 1;
        int end = getRangeStart(i) - 1;
        _appendToPat(result, start, escapeUnprintable);
        if (start != end) {
          if (start + 1 != end)
            result.append('-'); 
          _appendToPat(result, end, escapeUnprintable);
        } 
      } 
    } else {
      for (int i = 0; i < count; i++) {
        int start = getRangeStart(i);
        int end = getRangeEnd(i);
        _appendToPat(result, start, escapeUnprintable);
        if (start != end) {
          if (start + 1 != end)
            result.append('-'); 
          _appendToPat(result, end, escapeUnprintable);
        } 
      } 
    } 
    if (includeStrings && this.strings.size() > 0)
      for (String s : this.strings) {
        result.append('{');
        _appendToPat(result, s, escapeUnprintable);
        result.append('}');
      }  
    return result.append(']');
  }
  
  public int size() {
    int n = 0;
    int count = getRangeCount();
    for (int i = 0; i < count; i++)
      n += getRangeEnd(i) - getRangeStart(i) + 1; 
    return n + this.strings.size();
  }
  
  public boolean isEmpty() {
    return (this.len == 1 && this.strings.size() == 0);
  }
  
  public boolean matchesIndexValue(int v) {
    for (int i = 0; i < getRangeCount(); i++) {
      int low = getRangeStart(i);
      int high = getRangeEnd(i);
      if ((low & 0xFFFFFF00) == (high & 0xFFFFFF00)) {
        if ((low & 0xFF) <= v && v <= (high & 0xFF))
          return true; 
      } else if ((low & 0xFF) <= v || v <= (high & 0xFF)) {
        return true;
      } 
    } 
    if (this.strings.size() != 0)
      for (String s : this.strings) {
        int c = UTF16.charAt(s, 0);
        if ((c & 0xFF) == v)
          return true; 
      }  
    return false;
  }
  
  public int matches(Replaceable text, int[] offset, int limit, boolean incremental) {
    if (offset[0] == limit) {
      if (contains(65535))
        return incremental ? 1 : 2; 
      return 0;
    } 
    if (this.strings.size() != 0) {
      boolean forward = (offset[0] < limit);
      char firstChar = text.charAt(offset[0]);
      int highWaterLength = 0;
      for (String trial : this.strings) {
        char c = trial.charAt(forward ? 0 : (trial.length() - 1));
        if (forward && c > firstChar)
          break; 
        if (c != firstChar)
          continue; 
        int length = matchRest(text, offset[0], limit, trial);
        if (incremental) {
          int maxLen = forward ? (limit - offset[0]) : (offset[0] - limit);
          if (length == maxLen)
            return 1; 
        } 
        if (length == trial.length()) {
          if (length > highWaterLength)
            highWaterLength = length; 
          if (forward && length < highWaterLength)
            break; 
        } 
      } 
      if (highWaterLength != 0) {
        offset[0] = offset[0] + (forward ? highWaterLength : -highWaterLength);
        return 2;
      } 
    } 
    return super.matches(text, offset, limit, incremental);
  }
  
  private static int matchRest(Replaceable text, int start, int limit, String s) {
    int maxLen, slen = s.length();
    if (start < limit) {
      maxLen = limit - start;
      if (maxLen > slen)
        maxLen = slen; 
      for (int i = 1; i < maxLen; i++) {
        if (text.charAt(start + i) != s.charAt(i))
          return 0; 
      } 
    } else {
      maxLen = start - limit;
      if (maxLen > slen)
        maxLen = slen; 
      slen--;
      for (int i = 1; i < maxLen; i++) {
        if (text.charAt(start - i) != s.charAt(slen - i))
          return 0; 
      } 
    } 
    return maxLen;
  }
  
  public int matchesAt(CharSequence text, int offset) {
    int lastLen = -1;
    if (this.strings.size() != 0) {
      char firstChar = text.charAt(offset);
      String trial = null;
      Iterator<String> it = this.strings.iterator();
      while (it.hasNext()) {
        trial = it.next();
        char firstStringChar = trial.charAt(0);
        if (firstStringChar >= firstChar && 
          firstStringChar > firstChar)
          // Byte code: goto -> 135 
      } 
      while (true) {
        int tempLen = matchesAt(text, offset, trial);
        if (lastLen > tempLen)
          break; 
        lastLen = tempLen;
        if (!it.hasNext())
          break; 
        trial = it.next();
      } 
    } 
    if (lastLen < 2) {
      int cp = UTF16.charAt(text, offset);
      if (contains(cp))
        lastLen = UTF16.getCharCount(cp); 
    } 
    return offset + lastLen;
  }
  
  private static int matchesAt(CharSequence text, int offsetInText, CharSequence substring) {
    int len = substring.length();
    int textLength = text.length();
    if (textLength + offsetInText > len)
      return -1; 
    int i = 0;
    for (int j = offsetInText; i < len; i++, j++) {
      char pc = substring.charAt(i);
      char tc = text.charAt(j);
      if (pc != tc)
        return -1; 
    } 
    return i;
  }
  
  public void addMatchSetTo(UnicodeSet toUnionTo) {
    toUnionTo.addAll(this);
  }
  
  public int indexOf(int c) {
    if (c < 0 || c > 1114111)
      throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(c, 6)); 
    int i = 0;
    int n = 0;
    while (true) {
      int start = this.list[i++];
      if (c < start)
        return -1; 
      int limit = this.list[i++];
      if (c < limit)
        return n + c - start; 
      n += limit - start;
    } 
  }
  
  public int charAt(int index) {
    if (index >= 0) {
      int len2 = this.len & 0xFFFFFFFE;
      for (int i = 0; i < len2; ) {
        int start = this.list[i++];
        int count = this.list[i++] - start;
        if (index < count)
          return start + index; 
        index -= count;
      } 
    } 
    return -1;
  }
  
  public UnicodeSet add(int start, int end) {
    checkFrozen();
    return add_unchecked(start, end);
  }
  
  public UnicodeSet addAll(int start, int end) {
    checkFrozen();
    return add_unchecked(start, end);
  }
  
  private UnicodeSet add_unchecked(int start, int end) {
    if (start < 0 || start > 1114111)
      throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(start, 6)); 
    if (end < 0 || end > 1114111)
      throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(end, 6)); 
    if (start < end) {
      add(range(start, end), 2, 0);
    } else if (start == end) {
      add(start);
    } 
    return this;
  }
  
  public final UnicodeSet add(int c) {
    checkFrozen();
    return add_unchecked(c);
  }
  
  private final UnicodeSet add_unchecked(int c) {
    if (c < 0 || c > 1114111)
      throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(c, 6)); 
    int i = findCodePoint(c);
    if ((i & 0x1) != 0)
      return this; 
    if (c == this.list[i] - 1) {
      this.list[i] = c;
      if (c == 1114111) {
        ensureCapacity(this.len + 1);
        this.list[this.len++] = 1114112;
      } 
      if (i > 0 && c == this.list[i - 1]) {
        System.arraycopy(this.list, i + 1, this.list, i - 1, this.len - i - 1);
        this.len -= 2;
      } 
    } else if (i > 0 && c == this.list[i - 1]) {
      this.list[i - 1] = this.list[i - 1] + 1;
    } else {
      if (this.len + 2 > this.list.length) {
        int[] temp = new int[this.len + 2 + 16];
        if (i != 0)
          System.arraycopy(this.list, 0, temp, 0, i); 
        System.arraycopy(this.list, i, temp, i + 2, this.len - i);
        this.list = temp;
      } else {
        System.arraycopy(this.list, i, this.list, i + 2, this.len - i);
      } 
      this.list[i] = c;
      this.list[i + 1] = c + 1;
      this.len += 2;
    } 
    this.pat = null;
    return this;
  }
  
  public final UnicodeSet add(CharSequence s) {
    checkFrozen();
    int cp = getSingleCP(s);
    if (cp < 0) {
      this.strings.add(s.toString());
      this.pat = null;
    } else {
      add_unchecked(cp, cp);
    } 
    return this;
  }
  
  private static int getSingleCP(CharSequence s) {
    if (s.length() < 1)
      throw new IllegalArgumentException("Can't use zero-length strings in UnicodeSet"); 
    if (s.length() > 2)
      return -1; 
    if (s.length() == 1)
      return s.charAt(0); 
    int cp = UTF16.charAt(s, 0);
    if (cp > 65535)
      return cp; 
    return -1;
  }
  
  public final UnicodeSet addAll(CharSequence s) {
    checkFrozen();
    for (int i = 0; i < s.length(); i += UTF16.getCharCount(cp)) {
      int cp = UTF16.charAt(s, i);
      add_unchecked(cp, cp);
    } 
    return this;
  }
  
  public final UnicodeSet retainAll(String s) {
    return retainAll(fromAll(s));
  }
  
  public final UnicodeSet complementAll(String s) {
    return complementAll(fromAll(s));
  }
  
  public final UnicodeSet removeAll(String s) {
    return removeAll(fromAll(s));
  }
  
  public final UnicodeSet removeAllStrings() {
    checkFrozen();
    if (this.strings.size() != 0) {
      this.strings.clear();
      this.pat = null;
    } 
    return this;
  }
  
  public static UnicodeSet from(String s) {
    return (new UnicodeSet()).add(s);
  }
  
  public static UnicodeSet fromAll(String s) {
    return (new UnicodeSet()).addAll(s);
  }
  
  public UnicodeSet retain(int start, int end) {
    checkFrozen();
    if (start < 0 || start > 1114111)
      throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(start, 6)); 
    if (end < 0 || end > 1114111)
      throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(end, 6)); 
    if (start <= end) {
      retain(range(start, end), 2, 0);
    } else {
      clear();
    } 
    return this;
  }
  
  public final UnicodeSet retain(int c) {
    return retain(c, c);
  }
  
  public final UnicodeSet retain(String s) {
    int cp = getSingleCP(s);
    if (cp < 0) {
      boolean isIn = this.strings.contains(s);
      if (isIn && size() == 1)
        return this; 
      clear();
      this.strings.add(s);
      this.pat = null;
    } else {
      retain(cp, cp);
    } 
    return this;
  }
  
  public UnicodeSet remove(int start, int end) {
    checkFrozen();
    if (start < 0 || start > 1114111)
      throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(start, 6)); 
    if (end < 0 || end > 1114111)
      throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(end, 6)); 
    if (start <= end)
      retain(range(start, end), 2, 2); 
    return this;
  }
  
  public final UnicodeSet remove(int c) {
    return remove(c, c);
  }
  
  public final UnicodeSet remove(String s) {
    int cp = getSingleCP(s);
    if (cp < 0) {
      this.strings.remove(s);
      this.pat = null;
    } else {
      remove(cp, cp);
    } 
    return this;
  }
  
  public UnicodeSet complement(int start, int end) {
    checkFrozen();
    if (start < 0 || start > 1114111)
      throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(start, 6)); 
    if (end < 0 || end > 1114111)
      throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(end, 6)); 
    if (start <= end)
      xor(range(start, end), 2, 0); 
    this.pat = null;
    return this;
  }
  
  public final UnicodeSet complement(int c) {
    return complement(c, c);
  }
  
  public UnicodeSet complement() {
    checkFrozen();
    if (this.list[0] == 0) {
      System.arraycopy(this.list, 1, this.list, 0, this.len - 1);
      this.len--;
    } else {
      ensureCapacity(this.len + 1);
      System.arraycopy(this.list, 0, this.list, 1, this.len);
      this.list[0] = 0;
      this.len++;
    } 
    this.pat = null;
    return this;
  }
  
  public final UnicodeSet complement(String s) {
    checkFrozen();
    int cp = getSingleCP(s);
    if (cp < 0) {
      if (this.strings.contains(s)) {
        this.strings.remove(s);
      } else {
        this.strings.add(s);
      } 
      this.pat = null;
    } else {
      complement(cp, cp);
    } 
    return this;
  }
  
  public boolean contains(int c) {
    if (c < 0 || c > 1114111)
      throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(c, 6)); 
    if (this.bmpSet != null)
      return this.bmpSet.contains(c); 
    if (this.stringSpan != null)
      return this.stringSpan.contains(c); 
    int i = findCodePoint(c);
    return ((i & 0x1) != 0);
  }
  
  private final int findCodePoint(int c) {
    if (c < this.list[0])
      return 0; 
    if (this.len >= 2 && c >= this.list[this.len - 2])
      return this.len - 1; 
    int lo = 0;
    int hi = this.len - 1;
    while (true) {
      int i = lo + hi >>> 1;
      if (i == lo)
        return hi; 
      if (c < this.list[i]) {
        hi = i;
        continue;
      } 
      lo = i;
    } 
  }
  
  public boolean contains(int start, int end) {
    if (start < 0 || start > 1114111)
      throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(start, 6)); 
    if (end < 0 || end > 1114111)
      throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(end, 6)); 
    int i = findCodePoint(start);
    return ((i & 0x1) != 0 && end < this.list[i]);
  }
  
  public final boolean contains(String s) {
    int cp = getSingleCP(s);
    if (cp < 0)
      return this.strings.contains(s); 
    return contains(cp);
  }
  
  public boolean containsAll(UnicodeSet b) {
    int[] listB = b.list;
    boolean needA = true;
    boolean needB = true;
    int aPtr = 0;
    int bPtr = 0;
    int aLen = this.len - 1;
    int bLen = b.len - 1;
    int startA = 0, startB = 0, limitA = 0, limitB = 0;
    while (true) {
      if (needA) {
        if (aPtr >= aLen) {
          if (needB && bPtr >= bLen)
            break; 
          return false;
        } 
        startA = this.list[aPtr++];
        limitA = this.list[aPtr++];
      } 
      if (needB) {
        if (bPtr >= bLen)
          break; 
        startB = listB[bPtr++];
        limitB = listB[bPtr++];
      } 
      if (startB >= limitA) {
        needA = true;
        needB = false;
        continue;
      } 
      if (startB >= startA && limitB <= limitA) {
        needA = false;
        needB = true;
        continue;
      } 
      return false;
    } 
    if (!this.strings.containsAll(b.strings))
      return false; 
    return true;
  }
  
  public boolean containsAll(String s) {
    for (int i = 0; i < s.length(); i += UTF16.getCharCount(cp)) {
      int cp = UTF16.charAt(s, i);
      if (!contains(cp)) {
        if (this.strings.size() == 0)
          return false; 
        return containsAll(s, 0);
      } 
    } 
    return true;
  }
  
  private boolean containsAll(String s, int i) {
    if (i >= s.length())
      return true; 
    int cp = UTF16.charAt(s, i);
    if (contains(cp) && containsAll(s, i + UTF16.getCharCount(cp)))
      return true; 
    for (String setStr : this.strings) {
      if (s.startsWith(setStr, i) && containsAll(s, i + setStr.length()))
        return true; 
    } 
    return false;
  }
  
  public String getRegexEquivalent() {
    if (this.strings.size() == 0)
      return toString(); 
    StringBuffer result = new StringBuffer("(?:");
    _generatePattern(result, true, false);
    for (String s : this.strings) {
      result.append('|');
      _appendToPat(result, s, true);
    } 
    return result.append(")").toString();
  }
  
  public boolean containsNone(int start, int end) {
    if (start < 0 || start > 1114111)
      throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(start, 6)); 
    if (end < 0 || end > 1114111)
      throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(end, 6)); 
    int i = -1;
    do {
    
    } while (start >= this.list[++i]);
    return ((i & 0x1) == 0 && end < this.list[i]);
  }
  
  public boolean containsNone(UnicodeSet b) {
    int[] listB = b.list;
    boolean needA = true;
    boolean needB = true;
    int aPtr = 0;
    int bPtr = 0;
    int aLen = this.len - 1;
    int bLen = b.len - 1;
    int startA = 0, startB = 0, limitA = 0, limitB = 0;
    while (true) {
      if (needA) {
        if (aPtr >= aLen)
          break; 
        startA = this.list[aPtr++];
        limitA = this.list[aPtr++];
      } 
      if (needB) {
        if (bPtr >= bLen)
          break; 
        startB = listB[bPtr++];
        limitB = listB[bPtr++];
      } 
      if (startB >= limitA) {
        needA = true;
        needB = false;
        continue;
      } 
      if (startA >= limitB) {
        needA = false;
        needB = true;
        continue;
      } 
      return false;
    } 
    if (!SortedSetRelation.hasRelation(this.strings, 5, b.strings))
      return false; 
    return true;
  }
  
  public boolean containsNone(String s) {
    return (span(s, SpanCondition.NOT_CONTAINED) == s.length());
  }
  
  public final boolean containsSome(int start, int end) {
    return !containsNone(start, end);
  }
  
  public final boolean containsSome(UnicodeSet s) {
    return !containsNone(s);
  }
  
  public final boolean containsSome(String s) {
    return !containsNone(s);
  }
  
  public UnicodeSet addAll(UnicodeSet c) {
    checkFrozen();
    add(c.list, c.len, 0);
    this.strings.addAll(c.strings);
    return this;
  }
  
  public UnicodeSet retainAll(UnicodeSet c) {
    checkFrozen();
    retain(c.list, c.len, 0);
    this.strings.retainAll(c.strings);
    return this;
  }
  
  public UnicodeSet removeAll(UnicodeSet c) {
    checkFrozen();
    retain(c.list, c.len, 2);
    this.strings.removeAll(c.strings);
    return this;
  }
  
  public UnicodeSet complementAll(UnicodeSet c) {
    checkFrozen();
    xor(c.list, c.len, 0);
    SortedSetRelation.doOperation(this.strings, 5, c.strings);
    return this;
  }
  
  public UnicodeSet clear() {
    checkFrozen();
    this.list[0] = 1114112;
    this.len = 1;
    this.pat = null;
    this.strings.clear();
    return this;
  }
  
  public int getRangeCount() {
    return this.len / 2;
  }
  
  public int getRangeStart(int index) {
    return this.list[index * 2];
  }
  
  public int getRangeEnd(int index) {
    return this.list[index * 2 + 1] - 1;
  }
  
  public UnicodeSet compact() {
    checkFrozen();
    if (this.len != this.list.length) {
      int[] temp = new int[this.len];
      System.arraycopy(this.list, 0, temp, 0, this.len);
      this.list = temp;
    } 
    this.rangeList = null;
    this.buffer = null;
    return this;
  }
  
  public boolean equals(Object o) {
    if (o == null)
      return false; 
    if (this == o)
      return true; 
    try {
      UnicodeSet that = (UnicodeSet)o;
      if (this.len != that.len)
        return false; 
      for (int i = 0; i < this.len; i++) {
        if (this.list[i] != that.list[i])
          return false; 
      } 
      if (!this.strings.equals(that.strings))
        return false; 
    } catch (Exception e) {
      return false;
    } 
    return true;
  }
  
  public int hashCode() {
    int result = this.len;
    for (int i = 0; i < this.len; i++) {
      result *= 1000003;
      result += this.list[i];
    } 
    return result;
  }
  
  public String toString() {
    return toPattern(true);
  }
  
  public UnicodeSet applyPattern(String pattern, ParsePosition pos, SymbolTable symbols, int options) {
    boolean parsePositionWasNull = (pos == null);
    if (parsePositionWasNull)
      pos = new ParsePosition(0); 
    StringBuffer rebuiltPat = new StringBuffer();
    RuleCharacterIterator chars = new RuleCharacterIterator(pattern, symbols, pos);
    applyPattern(chars, symbols, rebuiltPat, options);
    if (chars.inVariable())
      syntaxError(chars, "Extra chars in variable value"); 
    this.pat = rebuiltPat.toString();
    if (parsePositionWasNull) {
      int i = pos.getIndex();
      if ((options & 0x1) != 0)
        i = PatternProps.skipWhiteSpace(pattern, i); 
      if (i != pattern.length())
        throw new IllegalArgumentException("Parse of \"" + pattern + "\" failed at " + i); 
    } 
    return this;
  }
  
  void applyPattern(RuleCharacterIterator chars, SymbolTable symbols, StringBuffer rebuiltPat, int options) {
    int opts = 3;
    if ((options & 0x1) != 0)
      opts |= 0x4; 
    StringBuffer patBuf = new StringBuffer(), buf = null;
    boolean usePat = false;
    UnicodeSet scratch = null;
    Object backup = null;
    int lastItem = 0, lastChar = 0, mode = 0;
    char op = Character.MIN_VALUE;
    boolean invert = false;
    clear();
    while (mode != 2 && !chars.atEnd()) {
      int c = 0;
      boolean literal = false;
      UnicodeSet nested = null;
      int setMode = 0;
      if (resemblesPropertyPattern(chars, opts)) {
        setMode = 2;
      } else {
        backup = chars.getPos(backup);
        c = chars.next(opts);
        literal = chars.isEscaped();
        if (c == 91 && !literal) {
          if (mode == 1) {
            chars.setPos(backup);
            setMode = 1;
          } else {
            mode = 1;
            patBuf.append('[');
            backup = chars.getPos(backup);
            c = chars.next(opts);
            literal = chars.isEscaped();
            if (c == 94 && !literal) {
              invert = true;
              patBuf.append('^');
              backup = chars.getPos(backup);
              c = chars.next(opts);
              literal = chars.isEscaped();
            } 
            if (c == 45) {
              literal = true;
            } else {
              chars.setPos(backup);
              continue;
            } 
          } 
        } else if (symbols != null) {
          UnicodeMatcher m = symbols.lookupMatcher(c);
          if (m != null)
            try {
              nested = (UnicodeSet)m;
              setMode = 3;
            } catch (ClassCastException e) {
              syntaxError(chars, "Syntax error");
            }  
        } 
      } 
      if (setMode != 0) {
        if (lastItem == 1) {
          if (op != '\000')
            syntaxError(chars, "Char expected after operator"); 
          add_unchecked(lastChar, lastChar);
          _appendToPat(patBuf, lastChar, false);
          lastItem = op = Character.MIN_VALUE;
        } 
        if (op == '-' || op == '&')
          patBuf.append(op); 
        if (nested == null) {
          if (scratch == null)
            scratch = new UnicodeSet(); 
          nested = scratch;
        } 
        switch (setMode) {
          case 1:
            nested.applyPattern(chars, symbols, patBuf, options);
            break;
          case 2:
            chars.skipIgnored(opts);
            nested.applyPropertyPattern(chars, patBuf, symbols);
            break;
          case 3:
            nested._toPattern(patBuf, false);
            break;
        } 
        usePat = true;
        if (mode == 0) {
          set(nested);
          mode = 2;
          break;
        } 
        switch (op) {
          case '-':
            removeAll(nested);
            break;
          case '&':
            retainAll(nested);
            break;
          case '\000':
            addAll(nested);
            break;
        } 
        op = Character.MIN_VALUE;
        lastItem = 2;
        continue;
      } 
      if (mode == 0)
        syntaxError(chars, "Missing '['"); 
      if (!literal) {
        boolean ok;
        boolean anchor;
        switch (c) {
          case 93:
            if (lastItem == 1) {
              add_unchecked(lastChar, lastChar);
              _appendToPat(patBuf, lastChar, false);
            } 
            if (op == '-') {
              add_unchecked(op, op);
              patBuf.append(op);
            } else if (op == '&') {
              syntaxError(chars, "Trailing '&'");
            } 
            patBuf.append(']');
            mode = 2;
            continue;
          case 45:
            if (op == '\000') {
              if (lastItem != 0) {
                op = (char)c;
                continue;
              } 
              add_unchecked(c, c);
              c = chars.next(opts);
              literal = chars.isEscaped();
              if (c == 93 && !literal) {
                patBuf.append("-]");
                mode = 2;
                continue;
              } 
            } 
            syntaxError(chars, "'-' not after char or set");
            break;
          case 38:
            if (lastItem == 2 && op == '\000') {
              op = (char)c;
              continue;
            } 
            syntaxError(chars, "'&' not after set");
            break;
          case 94:
            syntaxError(chars, "'^' not after '['");
            break;
          case 123:
            if (op != '\000')
              syntaxError(chars, "Missing operand after operator"); 
            if (lastItem == 1) {
              add_unchecked(lastChar, lastChar);
              _appendToPat(patBuf, lastChar, false);
            } 
            lastItem = 0;
            if (buf == null) {
              buf = new StringBuffer();
            } else {
              buf.setLength(0);
            } 
            ok = false;
            while (!chars.atEnd()) {
              c = chars.next(opts);
              literal = chars.isEscaped();
              if (c == 125 && !literal) {
                ok = true;
                break;
              } 
              UTF16.append(buf, c);
            } 
            if (buf.length() < 1 || !ok)
              syntaxError(chars, "Invalid multicharacter string"); 
            add(buf.toString());
            patBuf.append('{');
            _appendToPat(patBuf, buf.toString(), false);
            patBuf.append('}');
            continue;
          case 36:
            backup = chars.getPos(backup);
            c = chars.next(opts);
            literal = chars.isEscaped();
            anchor = (c == 93 && !literal);
            if (symbols == null && !anchor) {
              c = 36;
              chars.setPos(backup);
              break;
            } 
            if (anchor && op == '\000') {
              if (lastItem == 1) {
                add_unchecked(lastChar, lastChar);
                _appendToPat(patBuf, lastChar, false);
              } 
              add_unchecked(65535);
              usePat = true;
              patBuf.append('$').append(']');
              mode = 2;
              continue;
            } 
            syntaxError(chars, "Unquoted '$'");
            break;
        } 
      } 
      switch (lastItem) {
        case 0:
          lastItem = 1;
          lastChar = c;
        case 1:
          if (op == '-') {
            if (lastChar >= c)
              syntaxError(chars, "Invalid range"); 
            add_unchecked(lastChar, c);
            _appendToPat(patBuf, lastChar, false);
            patBuf.append(op);
            _appendToPat(patBuf, c, false);
            lastItem = op = Character.MIN_VALUE;
            continue;
          } 
          add_unchecked(lastChar, lastChar);
          _appendToPat(patBuf, lastChar, false);
          lastChar = c;
        case 2:
          if (op != '\000')
            syntaxError(chars, "Set expected after operator"); 
          lastChar = c;
          lastItem = 1;
      } 
    } 
    if (mode != 2)
      syntaxError(chars, "Missing ']'"); 
    chars.skipIgnored(opts);
    if ((options & 0x2) != 0)
      closeOver(2); 
    if (invert)
      complement(); 
    if (usePat) {
      rebuiltPat.append(patBuf.toString());
    } else {
      _generatePattern(rebuiltPat, false, true);
    } 
  }
  
  private static void syntaxError(RuleCharacterIterator chars, String msg) {
    throw new IllegalArgumentException("Error: " + msg + " at \"" + Utility.escape(chars.toString()) + '"');
  }
  
  public <T extends Collection<String>> T addAllTo(T target) {
    return addAllTo(this, target);
  }
  
  public String[] addAllTo(String[] target) {
    return addAllTo(this, target);
  }
  
  public static String[] toArray(UnicodeSet set) {
    return addAllTo(set, new String[set.size()]);
  }
  
  public UnicodeSet add(Collection<?> source) {
    return addAll(source);
  }
  
  public UnicodeSet addAll(Collection<?> source) {
    checkFrozen();
    for (Object o : source)
      add(o.toString()); 
    return this;
  }
  
  private void ensureCapacity(int newLen) {
    if (newLen <= this.list.length)
      return; 
    int[] temp = new int[newLen + 16];
    System.arraycopy(this.list, 0, temp, 0, this.len);
    this.list = temp;
  }
  
  private void ensureBufferCapacity(int newLen) {
    if (this.buffer != null && newLen <= this.buffer.length)
      return; 
    this.buffer = new int[newLen + 16];
  }
  
  private int[] range(int start, int end) {
    if (this.rangeList == null) {
      this.rangeList = new int[] { start, end + 1, 1114112 };
    } else {
      this.rangeList[0] = start;
      this.rangeList[1] = end + 1;
    } 
    return this.rangeList;
  }
  
  private UnicodeSet xor(int[] other, int otherLen, int polarity) {
    int b;
    ensureBufferCapacity(this.len + otherLen);
    int i = 0, j = 0, k = 0;
    int a = this.list[i++];
    if (polarity == 1 || polarity == 2) {
      b = 0;
      if (other[j] == 0) {
        j++;
        b = other[j];
      } 
    } else {
      b = other[j++];
    } 
    while (true) {
      while (a < b) {
        this.buffer[k++] = a;
        a = this.list[i++];
      } 
      if (b < a) {
        this.buffer[k++] = b;
        b = other[j++];
        continue;
      } 
      if (a != 1114112) {
        a = this.list[i++];
        b = other[j++];
        continue;
      } 
      break;
    } 
    this.buffer[k++] = 1114112;
    this.len = k;
    int[] temp = this.list;
    this.list = this.buffer;
    this.buffer = temp;
    this.pat = null;
    return this;
  }
  
  private UnicodeSet add(int[] other, int otherLen, int polarity) {
    ensureBufferCapacity(this.len + otherLen);
    int i = 0, j = 0, k = 0;
    int a = this.list[i++];
    int b = other[j++];
    while (true) {
      switch (polarity) {
        case 0:
          if (a < b) {
            if (k > 0 && a <= this.buffer[k - 1]) {
              a = max(this.list[i], this.buffer[--k]);
            } else {
              this.buffer[k++] = a;
              a = this.list[i];
            } 
            i++;
            polarity ^= 0x1;
            continue;
          } 
          if (b < a) {
            if (k > 0 && b <= this.buffer[k - 1]) {
              b = max(other[j], this.buffer[--k]);
            } else {
              this.buffer[k++] = b;
              b = other[j];
            } 
            j++;
            polarity ^= 0x2;
            continue;
          } 
          if (a == 1114112)
            break; 
          if (k > 0 && a <= this.buffer[k - 1]) {
            a = max(this.list[i], this.buffer[--k]);
          } else {
            this.buffer[k++] = a;
            a = this.list[i];
          } 
          i++;
          polarity ^= 0x1;
          b = other[j++];
          polarity ^= 0x2;
        case 3:
          if (b <= a) {
            if (a == 1114112)
              break; 
            this.buffer[k++] = a;
          } else {
            if (b == 1114112)
              break; 
            this.buffer[k++] = b;
          } 
          a = this.list[i++];
          polarity ^= 0x1;
          b = other[j++];
          polarity ^= 0x2;
        case 1:
          if (a < b) {
            this.buffer[k++] = a;
            a = this.list[i++];
            polarity ^= 0x1;
            continue;
          } 
          if (b < a) {
            b = other[j++];
            polarity ^= 0x2;
            continue;
          } 
          if (a == 1114112)
            break; 
          a = this.list[i++];
          polarity ^= 0x1;
          b = other[j++];
          polarity ^= 0x2;
        case 2:
          if (b < a) {
            this.buffer[k++] = b;
            b = other[j++];
            polarity ^= 0x2;
            continue;
          } 
          if (a < b) {
            a = this.list[i++];
            polarity ^= 0x1;
            continue;
          } 
          if (a == 1114112)
            break; 
          a = this.list[i++];
          polarity ^= 0x1;
          b = other[j++];
          polarity ^= 0x2;
      } 
    } 
    this.buffer[k++] = 1114112;
    this.len = k;
    int[] temp = this.list;
    this.list = this.buffer;
    this.buffer = temp;
    this.pat = null;
    return this;
  }
  
  private UnicodeSet retain(int[] other, int otherLen, int polarity) {
    ensureBufferCapacity(this.len + otherLen);
    int i = 0, j = 0, k = 0;
    int a = this.list[i++];
    int b = other[j++];
    while (true) {
      switch (polarity) {
        case 0:
          if (a < b) {
            a = this.list[i++];
            polarity ^= 0x1;
            continue;
          } 
          if (b < a) {
            b = other[j++];
            polarity ^= 0x2;
            continue;
          } 
          if (a == 1114112)
            break; 
          this.buffer[k++] = a;
          a = this.list[i++];
          polarity ^= 0x1;
          b = other[j++];
          polarity ^= 0x2;
        case 3:
          if (a < b) {
            this.buffer[k++] = a;
            a = this.list[i++];
            polarity ^= 0x1;
            continue;
          } 
          if (b < a) {
            this.buffer[k++] = b;
            b = other[j++];
            polarity ^= 0x2;
            continue;
          } 
          if (a == 1114112)
            break; 
          this.buffer[k++] = a;
          a = this.list[i++];
          polarity ^= 0x1;
          b = other[j++];
          polarity ^= 0x2;
        case 1:
          if (a < b) {
            a = this.list[i++];
            polarity ^= 0x1;
            continue;
          } 
          if (b < a) {
            this.buffer[k++] = b;
            b = other[j++];
            polarity ^= 0x2;
            continue;
          } 
          if (a == 1114112)
            break; 
          a = this.list[i++];
          polarity ^= 0x1;
          b = other[j++];
          polarity ^= 0x2;
        case 2:
          if (b < a) {
            b = other[j++];
            polarity ^= 0x2;
            continue;
          } 
          if (a < b) {
            this.buffer[k++] = a;
            a = this.list[i++];
            polarity ^= 0x1;
            continue;
          } 
          if (a == 1114112)
            break; 
          a = this.list[i++];
          polarity ^= 0x1;
          b = other[j++];
          polarity ^= 0x2;
      } 
    } 
    this.buffer[k++] = 1114112;
    this.len = k;
    int[] temp = this.list;
    this.list = this.buffer;
    this.buffer = temp;
    this.pat = null;
    return this;
  }
  
  private static final int max(int a, int b) {
    return (a > b) ? a : b;
  }
  
  private static interface Filter {
    boolean contains(int param1Int);
  }
  
  private static class NumericValueFilter implements Filter {
    double value;
    
    NumericValueFilter(double value) {
      this.value = value;
    }
    
    public boolean contains(int ch) {
      return (UCharacter.getUnicodeNumericValue(ch) == this.value);
    }
  }
  
  private static class GeneralCategoryMaskFilter implements Filter {
    int mask;
    
    GeneralCategoryMaskFilter(int mask) {
      this.mask = mask;
    }
    
    public boolean contains(int ch) {
      return ((1 << UCharacter.getType(ch) & this.mask) != 0);
    }
  }
  
  private static class IntPropertyFilter implements Filter {
    int prop;
    
    int value;
    
    IntPropertyFilter(int prop, int value) {
      this.prop = prop;
      this.value = value;
    }
    
    public boolean contains(int ch) {
      return (UCharacter.getIntPropertyValue(ch, this.prop) == this.value);
    }
  }
  
  private static class ScriptExtensionsFilter implements Filter {
    int script;
    
    ScriptExtensionsFilter(int script) {
      this.script = script;
    }
    
    public boolean contains(int c) {
      return UScript.hasScript(c, this.script);
    }
  }
  
  private static final VersionInfo NO_VERSION = VersionInfo.getInstance(0, 0, 0, 0);
  
  public static final int IGNORE_SPACE = 1;
  
  public static final int CASE = 2;
  
  public static final int CASE_INSENSITIVE = 2;
  
  public static final int ADD_CASE_MAPPINGS = 4;
  
  private static class VersionFilter implements Filter {
    VersionInfo version;
    
    VersionFilter(VersionInfo version) {
      this.version = version;
    }
    
    public boolean contains(int ch) {
      VersionInfo v = UCharacter.getAge(ch);
      return (v != UnicodeSet.NO_VERSION && v.compareTo(this.version) <= 0);
    }
  }
  
  private static synchronized UnicodeSet getInclusions(int src) {
    if (INCLUSIONS == null)
      INCLUSIONS = new UnicodeSet[12]; 
    if (INCLUSIONS[src] == null) {
      UnicodeSet incl = new UnicodeSet();
      switch (src) {
        case 1:
          UCharacterProperty.INSTANCE.addPropertyStarts(incl);
          break;
        case 2:
          UCharacterProperty.INSTANCE.upropsvec_addPropertyStarts(incl);
          break;
        case 6:
          UCharacterProperty.INSTANCE.addPropertyStarts(incl);
          UCharacterProperty.INSTANCE.upropsvec_addPropertyStarts(incl);
          break;
        case 7:
          (Norm2AllModes.getNFCInstance()).impl.addPropertyStarts(incl);
          UCaseProps.INSTANCE.addPropertyStarts(incl);
          break;
        case 8:
          (Norm2AllModes.getNFCInstance()).impl.addPropertyStarts(incl);
          break;
        case 9:
          (Norm2AllModes.getNFKCInstance()).impl.addPropertyStarts(incl);
          break;
        case 10:
          (Norm2AllModes.getNFKC_CFInstance()).impl.addPropertyStarts(incl);
          break;
        case 11:
          (Norm2AllModes.getNFCInstance()).impl.addCanonIterPropertyStarts(incl);
          break;
        case 4:
          UCaseProps.INSTANCE.addPropertyStarts(incl);
          break;
        case 5:
          UBiDiProps.INSTANCE.addPropertyStarts(incl);
          break;
        default:
          throw new IllegalStateException("UnicodeSet.getInclusions(unknown src " + src + ")");
      } 
      INCLUSIONS[src] = incl;
    } 
    return INCLUSIONS[src];
  }
  
  private UnicodeSet applyFilter(Filter filter, int src) {
    clear();
    int startHasProperty = -1;
    UnicodeSet inclusions = getInclusions(src);
    int limitRange = inclusions.getRangeCount();
    for (int j = 0; j < limitRange; j++) {
      int start = inclusions.getRangeStart(j);
      int end = inclusions.getRangeEnd(j);
      for (int ch = start; ch <= end; ch++) {
        if (filter.contains(ch)) {
          if (startHasProperty < 0)
            startHasProperty = ch; 
        } else if (startHasProperty >= 0) {
          add_unchecked(startHasProperty, ch - 1);
          startHasProperty = -1;
        } 
      } 
    } 
    if (startHasProperty >= 0)
      add_unchecked(startHasProperty, 1114111); 
    return this;
  }
  
  private static String mungeCharName(String source) {
    source = PatternProps.trimWhiteSpace(source);
    StringBuilder buf = null;
    for (int i = 0; i < source.length(); i++) {
      char ch = source.charAt(i);
      if (PatternProps.isWhiteSpace(ch)) {
        if (buf == null) {
          buf = (new StringBuilder()).append(source, 0, i);
        } else if (buf.charAt(buf.length() - 1) == ' ') {
          continue;
        } 
        ch = ' ';
      } 
      if (buf != null)
        buf.append(ch); 
      continue;
    } 
    return (buf == null) ? source : buf.toString();
  }
  
  public UnicodeSet applyIntPropertyValue(int prop, int value) {
    checkFrozen();
    if (prop == 8192) {
      applyFilter(new GeneralCategoryMaskFilter(value), 1);
    } else if (prop == 28672) {
      applyFilter(new ScriptExtensionsFilter(value), 2);
    } else {
      applyFilter(new IntPropertyFilter(prop, value), UCharacterProperty.INSTANCE.getSource(prop));
    } 
    return this;
  }
  
  public UnicodeSet applyPropertyAlias(String propertyAlias, String valueAlias) {
    return applyPropertyAlias(propertyAlias, valueAlias, null);
  }
  
  public UnicodeSet applyPropertyAlias(String propertyAlias, String valueAlias, SymbolTable symbols) {
    int p, v;
    checkFrozen();
    boolean mustNotBeEmpty = false, invert = false;
    if (symbols != null && symbols instanceof XSymbolTable && ((XSymbolTable)symbols).applyPropertyAlias(propertyAlias, valueAlias, this))
      return this; 
    if (XSYMBOL_TABLE != null && 
      XSYMBOL_TABLE.applyPropertyAlias(propertyAlias, valueAlias, this))
      return this; 
    if (valueAlias.length() > 0) {
      p = UCharacter.getPropertyEnum(propertyAlias);
      if (p == 4101)
        p = 8192; 
      if ((p >= 0 && p < 57) || (p >= 4096 && p < 4117) || (p >= 8192 && p < 8193)) {
        try {
          v = UCharacter.getPropertyValueEnum(p, valueAlias);
        } catch (IllegalArgumentException e) {
          if (p == 4098 || p == 4112 || p == 4113) {
            v = Integer.parseInt(PatternProps.trimWhiteSpace(valueAlias));
            if (v < 0 || v > 255)
              throw e; 
          } else {
            throw e;
          } 
        } 
      } else {
        double value;
        String buf;
        VersionInfo version;
        int ch;
        switch (p) {
          case 12288:
            value = Double.parseDouble(PatternProps.trimWhiteSpace(valueAlias));
            applyFilter(new NumericValueFilter(value), 1);
            return this;
          case 16389:
            buf = mungeCharName(valueAlias);
            ch = UCharacter.getCharFromExtendedName(buf);
            if (ch == -1)
              throw new IllegalArgumentException("Invalid character name"); 
            clear();
            add_unchecked(ch);
            return this;
          case 16395:
            throw new IllegalArgumentException("Unicode_1_Name (na1) not supported");
          case 16384:
            version = VersionInfo.getInstance(mungeCharName(valueAlias));
            applyFilter(new VersionFilter(version), 2);
            return this;
          case 28672:
            v = UCharacter.getPropertyValueEnum(4106, valueAlias);
            break;
          default:
            throw new IllegalArgumentException("Unsupported property");
        } 
      } 
    } else {
      UPropertyAliases pnames = UPropertyAliases.INSTANCE;
      p = 8192;
      v = pnames.getPropertyValueEnum(p, propertyAlias);
      if (v == -1) {
        p = 4106;
        v = pnames.getPropertyValueEnum(p, propertyAlias);
        if (v == -1) {
          p = pnames.getPropertyEnum(propertyAlias);
          if (p == -1)
            p = -1; 
          if (p >= 0 && p < 57) {
            v = 1;
          } else if (p == -1) {
            if (0 == UPropertyAliases.compare("ANY", propertyAlias)) {
              set(0, 1114111);
              return this;
            } 
            if (0 == UPropertyAliases.compare("ASCII", propertyAlias)) {
              set(0, 127);
              return this;
            } 
            if (0 == UPropertyAliases.compare("Assigned", propertyAlias)) {
              p = 8192;
              v = 1;
              invert = true;
            } else {
              throw new IllegalArgumentException("Invalid property alias: " + propertyAlias + "=" + valueAlias);
            } 
          } else {
            throw new IllegalArgumentException("Missing property value");
          } 
        } 
      } 
    } 
    applyIntPropertyValue(p, v);
    if (invert)
      complement(); 
    if (mustNotBeEmpty && isEmpty())
      throw new IllegalArgumentException("Invalid property value"); 
    return this;
  }
  
  private static boolean resemblesPropertyPattern(String pattern, int pos) {
    if (pos + 5 > pattern.length())
      return false; 
    return (pattern.regionMatches(pos, "[:", 0, 2) || pattern.regionMatches(true, pos, "\\p", 0, 2) || pattern.regionMatches(pos, "\\N", 0, 2));
  }
  
  private static boolean resemblesPropertyPattern(RuleCharacterIterator chars, int iterOpts) {
    boolean result = false;
    iterOpts &= 0xFFFFFFFD;
    Object pos = chars.getPos(null);
    int c = chars.next(iterOpts);
    if (c == 91 || c == 92) {
      int d = chars.next(iterOpts & 0xFFFFFFFB);
      result = (c == 91) ? ((d == 58)) : ((d == 78 || d == 112 || d == 80));
    } 
    chars.setPos(pos);
    return result;
  }
  
  private UnicodeSet applyPropertyPattern(String pattern, ParsePosition ppos, SymbolTable symbols) {
    String propName, valueName;
    int pos = ppos.getIndex();
    if (pos + 5 > pattern.length())
      return null; 
    boolean posix = false;
    boolean isName = false;
    boolean invert = false;
    if (pattern.regionMatches(pos, "[:", 0, 2)) {
      posix = true;
      pos = PatternProps.skipWhiteSpace(pattern, pos + 2);
      if (pos < pattern.length() && pattern.charAt(pos) == '^') {
        pos++;
        invert = true;
      } 
    } else if (pattern.regionMatches(true, pos, "\\p", 0, 2) || pattern.regionMatches(pos, "\\N", 0, 2)) {
      char c = pattern.charAt(pos + 1);
      invert = (c == 'P');
      isName = (c == 'N');
      pos = PatternProps.skipWhiteSpace(pattern, pos + 2);
      if (pos == pattern.length() || pattern.charAt(pos++) != '{')
        return null; 
    } else {
      return null;
    } 
    int close = pattern.indexOf(posix ? ":]" : "}", pos);
    if (close < 0)
      return null; 
    int equals = pattern.indexOf('=', pos);
    if (equals >= 0 && equals < close && !isName) {
      propName = pattern.substring(pos, equals);
      valueName = pattern.substring(equals + 1, close);
    } else {
      propName = pattern.substring(pos, close);
      valueName = "";
      if (isName) {
        valueName = propName;
        propName = "na";
      } 
    } 
    applyPropertyAlias(propName, valueName, symbols);
    if (invert)
      complement(); 
    ppos.setIndex(close + (posix ? 2 : 1));
    return this;
  }
  
  private void applyPropertyPattern(RuleCharacterIterator chars, StringBuffer rebuiltPat, SymbolTable symbols) {
    String patStr = chars.lookahead();
    ParsePosition pos = new ParsePosition(0);
    applyPropertyPattern(patStr, pos, symbols);
    if (pos.getIndex() == 0)
      syntaxError(chars, "Invalid property pattern"); 
    chars.jumpahead(pos.getIndex());
    rebuiltPat.append(patStr.substring(0, pos.getIndex()));
  }
  
  private static final void addCaseMapping(UnicodeSet set, int result, StringBuilder full) {
    if (result >= 0)
      if (result > 31) {
        set.add(result);
      } else {
        set.add(full.toString());
        full.setLength(0);
      }  
  }
  
  public UnicodeSet closeOver(int attribute) {
    checkFrozen();
    if ((attribute & 0x6) != 0) {
      UCaseProps csp = UCaseProps.INSTANCE;
      UnicodeSet foldSet = new UnicodeSet(this);
      ULocale root = ULocale.ROOT;
      if ((attribute & 0x2) != 0)
        foldSet.strings.clear(); 
      int n = getRangeCount();
      StringBuilder full = new StringBuilder();
      int[] locCache = new int[1];
      for (int i = 0; i < n; i++) {
        int start = getRangeStart(i);
        int end = getRangeEnd(i);
        if ((attribute & 0x2) != 0) {
          for (int cp = start; cp <= end; cp++)
            csp.addCaseClosure(cp, foldSet); 
        } else {
          for (int cp = start; cp <= end; cp++) {
            int result = csp.toFullLower(cp, null, full, root, locCache);
            addCaseMapping(foldSet, result, full);
            result = csp.toFullTitle(cp, null, full, root, locCache);
            addCaseMapping(foldSet, result, full);
            result = csp.toFullUpper(cp, null, full, root, locCache);
            addCaseMapping(foldSet, result, full);
            result = csp.toFullFolding(cp, full, 0);
            addCaseMapping(foldSet, result, full);
          } 
        } 
      } 
      if (!this.strings.isEmpty())
        if ((attribute & 0x2) != 0) {
          for (String s : this.strings) {
            String str = UCharacter.foldCase(s, 0);
            if (!csp.addStringCaseClosure(str, foldSet))
              foldSet.add(str); 
          } 
        } else {
          BreakIterator bi = BreakIterator.getWordInstance(root);
          for (String str : this.strings) {
            foldSet.add(UCharacter.toLowerCase(root, str));
            foldSet.add(UCharacter.toTitleCase(root, str, bi));
            foldSet.add(UCharacter.toUpperCase(root, str));
            foldSet.add(UCharacter.foldCase(str, 0));
          } 
        }  
      set(foldSet);
    } 
    return this;
  }
  
  public static abstract class XSymbolTable implements SymbolTable {
    public UnicodeMatcher lookupMatcher(int i) {
      return null;
    }
    
    public boolean applyPropertyAlias(String propertyName, String propertyValue, UnicodeSet result) {
      return false;
    }
    
    public char[] lookup(String s) {
      return null;
    }
    
    public String parseReference(String text, ParsePosition pos, int limit) {
      return null;
    }
  }
  
  public boolean isFrozen() {
    return (this.bmpSet != null || this.stringSpan != null);
  }
  
  public UnicodeSet freeze() {
    if (!isFrozen()) {
      this.buffer = null;
      if (this.list.length > this.len + 16) {
        int capacity = (this.len == 0) ? 1 : this.len;
        int[] oldList = this.list;
        this.list = new int[capacity];
        for (int i = capacity; i-- > 0;)
          this.list[i] = oldList[i]; 
      } 
      if (!this.strings.isEmpty()) {
        this.stringSpan = new UnicodeSetStringSpan(this, new ArrayList<String>(this.strings), 63);
        if (!this.stringSpan.needsStringSpanUTF16())
          this.stringSpan = null; 
      } 
      if (this.stringSpan == null)
        this.bmpSet = new BMPSet(this.list, this.len); 
    } 
    return this;
  }
  
  public int span(CharSequence s, SpanCondition spanCondition) {
    return span(s, 0, spanCondition);
  }
  
  public int span(CharSequence s, int start, SpanCondition spanCondition) {
    int end = s.length();
    if (start < 0) {
      start = 0;
    } else if (start >= end) {
      return end;
    } 
    if (this.bmpSet != null)
      return start + this.bmpSet.span(s, start, end, spanCondition); 
    int len = end - start;
    if (this.stringSpan != null)
      return start + this.stringSpan.span(s, start, len, spanCondition); 
    if (!this.strings.isEmpty()) {
      int which = (spanCondition == SpanCondition.NOT_CONTAINED) ? 41 : 42;
      UnicodeSetStringSpan strSpan = new UnicodeSetStringSpan(this, new ArrayList<String>(this.strings), which);
      if (strSpan.needsStringSpanUTF16())
        return start + strSpan.span(s, start, len, spanCondition); 
    } 
    boolean spanContained = (spanCondition != SpanCondition.NOT_CONTAINED);
    int next = start;
    do {
      int c = Character.codePointAt(s, next);
      if (spanContained != contains(c))
        break; 
      next = Character.offsetByCodePoints(s, next, 1);
    } while (next < end);
    return next;
  }
  
  public int spanBack(CharSequence s, SpanCondition spanCondition) {
    return spanBack(s, s.length(), spanCondition);
  }
  
  public int spanBack(CharSequence s, int fromIndex, SpanCondition spanCondition) {
    if (fromIndex <= 0)
      return 0; 
    if (fromIndex > s.length())
      fromIndex = s.length(); 
    if (this.bmpSet != null)
      return this.bmpSet.spanBack(s, fromIndex, spanCondition); 
    if (this.stringSpan != null)
      return this.stringSpan.spanBack(s, fromIndex, spanCondition); 
    if (!this.strings.isEmpty()) {
      int which = (spanCondition == SpanCondition.NOT_CONTAINED) ? 25 : 26;
      UnicodeSetStringSpan strSpan = new UnicodeSetStringSpan(this, new ArrayList<String>(this.strings), which);
      if (strSpan.needsStringSpanUTF16())
        return strSpan.spanBack(s, fromIndex, spanCondition); 
    } 
    boolean spanContained = (spanCondition != SpanCondition.NOT_CONTAINED);
    int prev = fromIndex;
    do {
      int c = Character.codePointBefore(s, prev);
      if (spanContained != contains(c))
        break; 
      prev = Character.offsetByCodePoints(s, prev, -1);
    } while (prev > 0);
    return prev;
  }
  
  public UnicodeSet cloneAsThawed() {
    UnicodeSet result = (UnicodeSet)clone();
    result.bmpSet = null;
    result.stringSpan = null;
    return result;
  }
  
  private void checkFrozen() {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
  }
  
  public Iterator<String> iterator() {
    return new UnicodeSetIterator2(this);
  }
  
  private static class UnicodeSetIterator2 implements Iterator<String> {
    private int[] sourceList;
    
    private int len;
    
    private int item;
    
    private int current;
    
    private int limit;
    
    private TreeSet<String> sourceStrings;
    
    private Iterator<String> stringIterator;
    
    private char[] buffer;
    
    UnicodeSetIterator2(UnicodeSet source) {
      this.len = source.len - 1;
      if (this.item >= this.len) {
        this.stringIterator = source.strings.iterator();
        this.sourceList = null;
      } else {
        this.sourceStrings = source.strings;
        this.sourceList = source.list;
        this.current = this.sourceList[this.item++];
        this.limit = this.sourceList[this.item++];
      } 
    }
    
    public boolean hasNext() {
      return (this.sourceList != null || this.stringIterator.hasNext());
    }
    
    public String next() {
      if (this.sourceList == null)
        return this.stringIterator.next(); 
      int codepoint = this.current++;
      if (this.current >= this.limit)
        if (this.item >= this.len) {
          this.stringIterator = this.sourceStrings.iterator();
          this.sourceList = null;
        } else {
          this.current = this.sourceList[this.item++];
          this.limit = this.sourceList[this.item++];
        }  
      if (codepoint <= 65535)
        return String.valueOf((char)codepoint); 
      if (this.buffer == null)
        this.buffer = new char[2]; 
      int offset = codepoint - 65536;
      this.buffer[0] = (char)((offset >>> 10) + 55296);
      this.buffer[1] = (char)((offset & 0x3FF) + 56320);
      return String.valueOf(this.buffer);
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
  
  public boolean containsAll(Collection<String> collection) {
    for (String o : collection) {
      if (!contains(o))
        return false; 
    } 
    return true;
  }
  
  public boolean containsNone(Collection<String> collection) {
    for (String o : collection) {
      if (contains(o))
        return false; 
    } 
    return true;
  }
  
  public final boolean containsSome(Collection<String> collection) {
    return !containsNone(collection);
  }
  
  public UnicodeSet addAll(String... collection) {
    checkFrozen();
    for (String str : collection)
      add(str); 
    return this;
  }
  
  public UnicodeSet removeAll(Collection<String> collection) {
    checkFrozen();
    for (String o : collection)
      remove(o); 
    return this;
  }
  
  public UnicodeSet retainAll(Collection<String> collection) {
    checkFrozen();
    UnicodeSet toRetain = new UnicodeSet();
    toRetain.addAll(collection);
    retainAll(toRetain);
    return this;
  }
  
  public enum ComparisonStyle {
    SHORTER_FIRST, LEXICOGRAPHIC, LONGER_FIRST;
  }
  
  public int compareTo(UnicodeSet o) {
    return compareTo(o, ComparisonStyle.SHORTER_FIRST);
  }
  
  public int compareTo(UnicodeSet o, ComparisonStyle style) {
    if (style != ComparisonStyle.LEXICOGRAPHIC) {
      int diff = size() - o.size();
      if (diff != 0)
        return (((diff < 0) ? true : false) == ((style == ComparisonStyle.SHORTER_FIRST) ? true : false)) ? -1 : 1; 
    } 
    for (int i = 0;; i++) {
      int result;
      if (0 != (result = this.list[i] - o.list[i])) {
        if (this.list[i] == 1114112) {
          if (this.strings.isEmpty())
            return 1; 
          String item = this.strings.first();
          return compare(item, o.list[i]);
        } 
        if (o.list[i] == 1114112) {
          if (o.strings.isEmpty())
            return -1; 
          String item = o.strings.first();
          return -compare(item, this.list[i]);
        } 
        return ((i & 0x1) == 0) ? result : -result;
      } 
      if (this.list[i] == 1114112)
        break; 
    } 
    return compare(this.strings, o.strings);
  }
  
  public int compareTo(Iterable<String> other) {
    return compare(this, other);
  }
  
  public static int compare(String string, int codePoint) {
    return CharSequences.compare(string, codePoint);
  }
  
  public static int compare(int codePoint, String string) {
    return -CharSequences.compare(string, codePoint);
  }
  
  public static <T extends Comparable<T>> int compare(Iterable<T> collection1, Iterable<T> collection2) {
    return compare(collection1.iterator(), collection2.iterator());
  }
  
  public static <T extends Comparable<T>> int compare(Iterator<T> first, Iterator<T> other) {
    while (true) {
      if (!first.hasNext())
        return other.hasNext() ? -1 : 0; 
      if (!other.hasNext())
        return 1; 
      Comparable<Comparable> comparable = (Comparable)first.next();
      Comparable comparable1 = (Comparable)other.next();
      int result = comparable.compareTo(comparable1);
      if (result != 0)
        return result; 
    } 
  }
  
  public static <T extends Comparable<T>> int compare(Collection<T> collection1, Collection<T> collection2, ComparisonStyle style) {
    if (style != ComparisonStyle.LEXICOGRAPHIC) {
      int diff = collection1.size() - collection2.size();
      if (diff != 0)
        return (((diff < 0) ? true : false) == ((style == ComparisonStyle.SHORTER_FIRST) ? true : false)) ? -1 : 1; 
    } 
    return compare(collection1, collection2);
  }
  
  public static <T, U extends Collection<T>> U addAllTo(Iterable<T> source, U target) {
    for (T item : source)
      target.add(item); 
    return target;
  }
  
  public static <T> T[] addAllTo(Iterable<T> source, T[] target) {
    int i = 0;
    for (T item : source)
      target[i++] = item; 
    return target;
  }
  
  public Iterable<String> strings() {
    return Collections.unmodifiableSortedSet(this.strings);
  }
  
  public static int getSingleCodePoint(CharSequence s) {
    return CharSequences.getSingleCodePoint(s);
  }
  
  public UnicodeSet addBridges(UnicodeSet dontCare) {
    UnicodeSet notInInput = (new UnicodeSet(this)).complement();
    for (UnicodeSetIterator it = new UnicodeSetIterator(notInInput); it.nextRange();) {
      if (it.codepoint != 0 && it.codepoint != UnicodeSetIterator.IS_STRING && it.codepointEnd != 1114111 && dontCare.contains(it.codepoint, it.codepointEnd))
        add(it.codepoint, it.codepointEnd); 
    } 
    return this;
  }
  
  public int findIn(CharSequence value, int fromIndex, boolean findNot) {
    for (; fromIndex < value.length(); fromIndex += UTF16.getCharCount(cp)) {
      int cp = UTF16.charAt(value, fromIndex);
      if (contains(cp) != findNot)
        break; 
    } 
    return fromIndex;
  }
  
  public int findLastIn(CharSequence value, int fromIndex, boolean findNot) {
    fromIndex--;
    for (; fromIndex >= 0; fromIndex -= UTF16.getCharCount(cp)) {
      int cp = UTF16.charAt(value, fromIndex);
      if (contains(cp) != findNot)
        break; 
    } 
    return (fromIndex < 0) ? -1 : fromIndex;
  }
  
  public String stripFrom(CharSequence source, boolean matches) {
    StringBuilder result = new StringBuilder();
    int pos;
    for (pos = 0; pos < source.length(); ) {
      int inside = findIn(source, pos, !matches);
      result.append(source.subSequence(pos, inside));
      pos = findIn(source, inside, matches);
    } 
    return result.toString();
  }
  
  public enum SpanCondition {
    NOT_CONTAINED, CONTAINED, SIMPLE, CONDITION_COUNT;
  }
  
  public static XSymbolTable getDefaultXSymbolTable() {
    return XSYMBOL_TABLE;
  }
  
  public static void setDefaultXSymbolTable(XSymbolTable xSymbolTable) {
    XSYMBOL_TABLE = xSymbolTable;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\UnicodeSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */