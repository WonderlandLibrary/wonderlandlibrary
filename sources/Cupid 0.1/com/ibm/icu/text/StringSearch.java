package com.ibm.icu.text;

import com.ibm.icu.impl.CharacterIteratorWrapper;
import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.impl.Normalizer2Impl;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.ULocale;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Locale;

public final class StringSearch extends SearchIterator {
  private int m_textBeginOffset_;
  
  private int m_textLimitOffset_;
  
  private int m_matchedIndex_;
  
  private Pattern m_pattern_;
  
  private RuleBasedCollator m_collator_;
  
  private CollationElementIterator m_colEIter_;
  
  private CollationElementIterator m_utilColEIter_;
  
  private int m_ceMask_;
  
  private StringBuilder m_canonicalPrefixAccents_;
  
  private StringBuilder m_canonicalSuffixAccents_;
  
  private boolean m_isCanonicalMatch_;
  
  private BreakIterator m_charBreakIter_;
  
  private final Normalizer2Impl m_nfcImpl_;
  
  private static final int MAX_TABLE_SIZE_ = 257;
  
  private static final int INITIAL_ARRAY_SIZE_ = 256;
  
  private static final int SECOND_LAST_BYTE_SHIFT_ = 8;
  
  private static final int LAST_BYTE_MASK_ = 255;
  
  private int[] m_utilBuffer_;
  
  private static final long UNSIGNED_32BIT_MASK = 4294967295L;
  
  public StringSearch(String pattern, CharacterIterator target, RuleBasedCollator collator, BreakIterator breakiter) {
    super(target, breakiter);
    this.m_nfcImpl_ = (Norm2AllModes.getNFCInstance()).impl;
    this.m_utilBuffer_ = new int[2];
    this.m_textBeginOffset_ = this.targetText.getBeginIndex();
    this.m_textLimitOffset_ = this.targetText.getEndIndex();
    this.m_collator_ = collator;
    this.m_colEIter_ = this.m_collator_.getCollationElementIterator(target);
    this.m_utilColEIter_ = collator.getCollationElementIterator("");
    this.m_ceMask_ = getMask(this.m_collator_.getStrength());
    this.m_isCanonicalMatch_ = false;
    this.m_pattern_ = new Pattern(pattern);
    this.m_matchedIndex_ = -1;
    this.m_charBreakIter_ = BreakIterator.getCharacterInstance();
    this.m_charBreakIter_.setText(target);
    initialize();
  }
  
  public StringSearch(String pattern, CharacterIterator target, RuleBasedCollator collator) {
    this(pattern, target, collator, (BreakIterator)null);
  }
  
  public StringSearch(String pattern, CharacterIterator target, Locale locale) {
    this(pattern, target, ULocale.forLocale(locale));
  }
  
  public StringSearch(String pattern, CharacterIterator target, ULocale locale) {
    this(pattern, target, (RuleBasedCollator)Collator.getInstance(locale), (BreakIterator)null);
  }
  
  public StringSearch(String pattern, String target) {
    this(pattern, new StringCharacterIterator(target), (RuleBasedCollator)Collator.getInstance(), (BreakIterator)null);
  }
  
  public RuleBasedCollator getCollator() {
    return this.m_collator_;
  }
  
  public String getPattern() {
    return this.m_pattern_.targetText;
  }
  
  public int getIndex() {
    int result = this.m_colEIter_.getOffset();
    if (isOutOfBounds(this.m_textBeginOffset_, this.m_textLimitOffset_, result))
      return -1; 
    return result;
  }
  
  public boolean isCanonical() {
    return this.m_isCanonicalMatch_;
  }
  
  public void setCollator(RuleBasedCollator collator) {
    if (collator == null)
      throw new IllegalArgumentException("Collator can not be null"); 
    this.m_collator_ = collator;
    this.m_ceMask_ = getMask(this.m_collator_.getStrength());
    initialize();
    this.m_colEIter_.setCollator(this.m_collator_);
    this.m_utilColEIter_.setCollator(this.m_collator_);
    this.m_charBreakIter_ = BreakIterator.getCharacterInstance();
    this.m_charBreakIter_.setText(this.targetText);
  }
  
  public void setPattern(String pattern) {
    if (pattern == null || pattern.length() <= 0)
      throw new IllegalArgumentException("Pattern to search for can not be null or of length 0"); 
    this.m_pattern_.targetText = pattern;
    initialize();
  }
  
  public void setTarget(CharacterIterator text) {
    super.setTarget(text);
    this.m_textBeginOffset_ = this.targetText.getBeginIndex();
    this.m_textLimitOffset_ = this.targetText.getEndIndex();
    this.m_colEIter_.setText(this.targetText);
    this.m_charBreakIter_.setText(this.targetText);
  }
  
  public void setIndex(int position) {
    super.setIndex(position);
    this.m_matchedIndex_ = -1;
    this.m_colEIter_.setExactOffset(position);
  }
  
  public void setCanonical(boolean allowCanonical) {
    this.m_isCanonicalMatch_ = allowCanonical;
    if (this.m_isCanonicalMatch_ == true) {
      if (this.m_canonicalPrefixAccents_ == null) {
        this.m_canonicalPrefixAccents_ = new StringBuilder();
      } else {
        this.m_canonicalPrefixAccents_.delete(0, this.m_canonicalPrefixAccents_.length());
      } 
      if (this.m_canonicalSuffixAccents_ == null) {
        this.m_canonicalSuffixAccents_ = new StringBuilder();
      } else {
        this.m_canonicalSuffixAccents_.delete(0, this.m_canonicalSuffixAccents_.length());
      } 
    } 
  }
  
  public void reset() {
    super.reset();
    this.m_isCanonicalMatch_ = false;
    this.m_ceMask_ = getMask(this.m_collator_.getStrength());
    initialize();
    this.m_colEIter_.setCollator(this.m_collator_);
    this.m_colEIter_.reset();
    this.m_utilColEIter_.setCollator(this.m_collator_);
  }
  
  protected int handleNext(int start) {
    if (this.m_pattern_.m_CELength_ == 0) {
      this.matchLength = 0;
      if (this.m_matchedIndex_ == -1 && start == this.m_textBeginOffset_) {
        this.m_matchedIndex_ = start;
        return this.m_matchedIndex_;
      } 
      this.targetText.setIndex(start);
      char ch = this.targetText.current();
      char ch2 = this.targetText.next();
      if (ch2 == Character.MAX_VALUE) {
        this.m_matchedIndex_ = -1;
      } else {
        this.m_matchedIndex_ = this.targetText.getIndex();
      } 
      if (UTF16.isLeadSurrogate(ch) && UTF16.isTrailSurrogate(ch2)) {
        this.targetText.next();
        this.m_matchedIndex_ = this.targetText.getIndex();
      } 
    } else {
      if (this.matchLength <= 0)
        if (start == this.m_textBeginOffset_) {
          this.m_matchedIndex_ = -1;
        } else {
          this.m_matchedIndex_ = start - 1;
        }  
      if (this.m_isCanonicalMatch_) {
        handleNextCanonical(start);
      } else {
        handleNextExact(start);
      } 
    } 
    if (this.m_matchedIndex_ == -1) {
      this.targetText.setIndex(this.m_textLimitOffset_);
    } else {
      this.targetText.setIndex(this.m_matchedIndex_);
    } 
    return this.m_matchedIndex_;
  }
  
  protected int handlePrevious(int start) {
    if (this.m_pattern_.m_CELength_ == 0) {
      this.matchLength = 0;
      this.targetText.setIndex(start);
      char ch = this.targetText.previous();
      if (ch == Character.MAX_VALUE) {
        this.m_matchedIndex_ = -1;
      } else {
        this.m_matchedIndex_ = this.targetText.getIndex();
        if (UTF16.isTrailSurrogate(ch) && UTF16.isLeadSurrogate(this.targetText.previous()))
          this.m_matchedIndex_ = this.targetText.getIndex(); 
      } 
    } else {
      if (this.matchLength == 0)
        this.m_matchedIndex_ = -1; 
      if (this.m_isCanonicalMatch_) {
        handlePreviousCanonical(start);
      } else {
        handlePreviousExact(start);
      } 
    } 
    if (this.m_matchedIndex_ == -1) {
      this.targetText.setIndex(this.m_textBeginOffset_);
    } else {
      this.targetText.setIndex(this.m_matchedIndex_);
    } 
    return this.m_matchedIndex_;
  }
  
  private static class Pattern {
    protected String targetText;
    
    protected int[] m_CE_;
    
    protected int m_CELength_;
    
    protected boolean m_hasPrefixAccents_;
    
    protected boolean m_hasSuffixAccents_;
    
    protected int m_defaultShiftSize_;
    
    protected char[] m_shift_;
    
    protected char[] m_backShift_;
    
    protected Pattern(String pattern) {
      this.targetText = pattern;
      this.m_CE_ = new int[256];
      this.m_CELength_ = 0;
      this.m_hasPrefixAccents_ = false;
      this.m_hasSuffixAccents_ = false;
      this.m_defaultShiftSize_ = 1;
      this.m_shift_ = new char[257];
      this.m_backShift_ = new char[257];
    }
  }
  
  private static final int hash(int ce) {
    return CollationElementIterator.primaryOrder(ce) % 257;
  }
  
  private final char getFCD(int c) {
    return (char)this.m_nfcImpl_.getFCD16(c);
  }
  
  private final char getFCD(CharacterIterator str, int offset) {
    char ch = str.setIndex(offset);
    if (ch < 'ƀ')
      return (char)this.m_nfcImpl_.getFCD16FromBelow180(ch); 
    if (this.m_nfcImpl_.singleLeadMightHaveNonZeroFCD16(ch)) {
      if (!Character.isHighSurrogate(ch))
        return (char)this.m_nfcImpl_.getFCD16FromNormData(ch); 
      char c2 = str.next();
      if (Character.isLowSurrogate(c2))
        return (char)this.m_nfcImpl_.getFCD16FromNormData(Character.toCodePoint(ch, c2)); 
    } 
    return Character.MIN_VALUE;
  }
  
  private final int getFCDBefore(CharacterIterator iter, int offset) {
    iter.setIndex(offset);
    char c = iter.previous();
    if (c < 'ƀ')
      return (char)this.m_nfcImpl_.getFCD16FromBelow180(c); 
    if (!Character.isLowSurrogate(c)) {
      if (this.m_nfcImpl_.singleLeadMightHaveNonZeroFCD16(c))
        return (char)this.m_nfcImpl_.getFCD16FromNormData(c); 
    } else {
      char lead = iter.previous();
      if (Character.isHighSurrogate(lead))
        return (char)this.m_nfcImpl_.getFCD16FromNormData(Character.toCodePoint(lead, c)); 
    } 
    return 0;
  }
  
  private final char getFCD(String str, int offset) {
    char ch = str.charAt(offset);
    if (ch < 'ƀ')
      return (char)this.m_nfcImpl_.getFCD16FromBelow180(ch); 
    if (this.m_nfcImpl_.singleLeadMightHaveNonZeroFCD16(ch)) {
      if (!Character.isHighSurrogate(ch))
        return (char)this.m_nfcImpl_.getFCD16FromNormData(ch); 
      char c2;
      if (++offset < str.length() && Character.isLowSurrogate(c2 = str.charAt(offset)))
        return (char)this.m_nfcImpl_.getFCD16FromNormData(Character.toCodePoint(ch, c2)); 
    } 
    return Character.MIN_VALUE;
  }
  
  private final int getCE(int ce) {
    ce &= this.m_ceMask_;
    if (this.m_collator_.isAlternateHandlingShifted())
      if (((this.m_collator_.m_variableTopValue_ << 16) & 0xFFFFFFFFL) > (ce & 0xFFFFFFFFL))
        if (this.m_collator_.getStrength() == 3) {
          ce = CollationElementIterator.primaryOrder(ce);
        } else {
          ce = 0;
        }   
    return ce;
  }
  
  private static final int[] append(int offset, int value, int[] array) {
    if (offset >= array.length) {
      int[] temp = new int[offset + 256];
      System.arraycopy(array, 0, temp, 0, array.length);
      array = temp;
    } 
    array[offset] = value;
    return array;
  }
  
  private final int initializePatternCETable() {
    this.m_utilColEIter_.setText(this.m_pattern_.targetText);
    int offset = 0;
    int result = 0;
    int ce = this.m_utilColEIter_.next();
    while (ce != -1) {
      int newce = getCE(ce);
      if (newce != 0) {
        this.m_pattern_.m_CE_ = append(offset, newce, this.m_pattern_.m_CE_);
        offset++;
      } 
      result += this.m_utilColEIter_.getMaxExpansion(ce) - 1;
      ce = this.m_utilColEIter_.next();
    } 
    this.m_pattern_.m_CE_ = append(offset, 0, this.m_pattern_.m_CE_);
    this.m_pattern_.m_CELength_ = offset;
    return result;
  }
  
  private final int initializePattern() {
    if (this.m_collator_.getStrength() == 0) {
      this.m_pattern_.m_hasPrefixAccents_ = false;
      this.m_pattern_.m_hasSuffixAccents_ = false;
    } else {
      this.m_pattern_.m_hasPrefixAccents_ = (getFCD(this.m_pattern_.targetText, 0) >> 8 != 0);
      this.m_pattern_.m_hasSuffixAccents_ = ((getFCD(this.m_pattern_.targetText.codePointBefore(this.m_pattern_.targetText.length())) & 0xFF) != 0);
    } 
    return initializePatternCETable();
  }
  
  private final void setShiftTable(char[] shift, char[] backshift, int[] cetable, int cesize, int expansionsize, char defaultforward, char defaultbackward) {
    int count;
    for (count = 0; count < 257; count++)
      shift[count] = defaultforward; 
    cesize--;
    for (count = 0; count < cesize; count++) {
      int temp = defaultforward - count - 1;
      shift[hash(cetable[count])] = (temp > 1) ? (char)temp : '\001';
    } 
    shift[hash(cetable[cesize])] = '\001';
    shift[hash(0)] = '\001';
    for (count = 0; count < 257; count++)
      backshift[count] = defaultbackward; 
    for (count = cesize; count > 0; count--)
      backshift[hash(cetable[count])] = (char)((count > expansionsize) ? (count - expansionsize) : '\001'); 
    backshift[hash(cetable[0])] = '\001';
    backshift[hash(0)] = '\001';
  }
  
  private final void initialize() {
    int expandlength = initializePattern();
    if (this.m_pattern_.m_CELength_ > 0) {
      char minlength = (char)((this.m_pattern_.m_CELength_ > expandlength) ? (this.m_pattern_.m_CELength_ - expandlength) : '\001');
      this.m_pattern_.m_defaultShiftSize_ = minlength;
      setShiftTable(this.m_pattern_.m_shift_, this.m_pattern_.m_backShift_, this.m_pattern_.m_CE_, this.m_pattern_.m_CELength_, expandlength, minlength, minlength);
    } else {
      this.m_pattern_.m_defaultShiftSize_ = 0;
    } 
  }
  
  private final boolean isBreakUnit(int start, int end) {
    if (this.breakIterator != null) {
      int startindex = this.breakIterator.first();
      int endindex = this.breakIterator.last();
      if (start < startindex || start > endindex || end < startindex || end > endindex)
        return false; 
      boolean result = ((start == startindex || this.breakIterator.following(start - 1) == start) && (end == endindex || this.breakIterator.following(end - 1) == end));
      if (result) {
        this.m_utilColEIter_.setText((UCharacterIterator)new CharacterIteratorWrapper(this.targetText), start);
        for (int count = 0; count < this.m_pattern_.m_CELength_; 
          count++) {
          int ce = getCE(this.m_utilColEIter_.next());
          if (ce == 0) {
            count--;
          } else if (ce != this.m_pattern_.m_CE_[count]) {
            return false;
          } 
        } 
        int nextce = this.m_utilColEIter_.next();
        while (this.m_utilColEIter_.getOffset() == end && getCE(nextce) == 0)
          nextce = this.m_utilColEIter_.next(); 
        if (nextce != -1 && this.m_utilColEIter_.getOffset() == end)
          return false; 
      } 
      return result;
    } 
    return true;
  }
  
  private final int getNextBaseOffset(CharacterIterator text, int textoffset) {
    if (textoffset >= text.getEndIndex())
      return textoffset; 
    char c = text.setIndex(textoffset);
    while (true) {
      if (c < '̀' || !this.m_nfcImpl_.singleLeadMightHaveNonZeroFCD16(c))
        return textoffset; 
      char next = text.next();
      if (Character.isSurrogatePair(c, next)) {
        int fcd = this.m_nfcImpl_.getFCD16FromNormData(Character.toCodePoint(c, next));
        if (fcd >> 8 == 0)
          return textoffset; 
        next = text.next();
        textoffset += 2;
      } else {
        int fcd = this.m_nfcImpl_.getFCD16FromNormData(c);
        if (fcd >> 8 == 0)
          return textoffset; 
        textoffset++;
      } 
      c = next;
    } 
  }
  
  private final int getNextBaseOffset(int textoffset) {
    if (this.m_pattern_.m_hasSuffixAccents_ && textoffset < this.m_textLimitOffset_ && (
      getFCDBefore(this.targetText, textoffset) & 0xFF) != 0)
      return getNextBaseOffset(this.targetText, textoffset); 
    return textoffset;
  }
  
  private int shiftForward(int textoffset, int ce, int patternceindex) {
    if (ce != -1) {
      int shift = this.m_pattern_.m_shift_[hash(ce)];
      int adjust = this.m_pattern_.m_CELength_ - patternceindex;
      if (adjust > 1 && shift >= adjust)
        shift -= adjust - 1; 
      textoffset += shift;
    } else {
      textoffset += this.m_pattern_.m_defaultShiftSize_;
    } 
    textoffset = getNextBaseOffset(textoffset);
    return textoffset;
  }
  
  private final int getNextSafeOffset(int textoffset, int end) {
    int result = textoffset;
    this.targetText.setIndex(result);
    while (result != end && this.m_collator_.isUnsafe(this.targetText.current())) {
      result++;
      this.targetText.setIndex(result);
    } 
    return result;
  }
  
  private final boolean checkExtraMatchAccents(int start, int end) {
    boolean result = false;
    if (this.m_pattern_.m_hasPrefixAccents_) {
      this.targetText.setIndex(start);
      if (UTF16.isLeadSurrogate(this.targetText.next()) && 
        !UTF16.isTrailSurrogate(this.targetText.next()))
        this.targetText.previous(); 
      String str = getString(this.targetText, start, end);
      if (Normalizer.quickCheck(str, Normalizer.NFD, 0) == Normalizer.NO) {
        int safeoffset = getNextSafeOffset(start, end);
        if (safeoffset != end)
          safeoffset++; 
        String decomp = Normalizer.decompose(str.substring(0, safeoffset - start), false);
        this.m_utilColEIter_.setText(decomp);
        int firstce = this.m_pattern_.m_CE_[0];
        boolean ignorable = true;
        int ce = 0;
        int offset = 0;
        while (ce != firstce) {
          offset = this.m_utilColEIter_.getOffset();
          if (ce != firstce && ce != 0)
            ignorable = false; 
          ce = this.m_utilColEIter_.next();
        } 
        this.m_utilColEIter_.setExactOffset(offset);
        this.m_utilColEIter_.previous();
        offset = this.m_utilColEIter_.getOffset();
        result = (!ignorable && UCharacter.getCombiningClass(UTF16.charAt(decomp, offset)) != 0);
      } 
    } 
    return result;
  }
  
  private final boolean hasAccentsBeforeMatch(int start, int end) {
    if (this.m_pattern_.m_hasPrefixAccents_) {
      boolean ignorable = true;
      int firstce = this.m_pattern_.m_CE_[0];
      this.m_colEIter_.setExactOffset(start);
      int ce = getCE(this.m_colEIter_.next());
      while (ce != firstce) {
        if (ce != 0)
          ignorable = false; 
        ce = getCE(this.m_colEIter_.next());
      } 
      if (!ignorable && this.m_colEIter_.isInBuffer())
        return true; 
      boolean accent = (getFCD(this.targetText, start) >> 8 != 0);
      if (!accent)
        return checkExtraMatchAccents(start, end); 
      if (!ignorable)
        return true; 
      if (start > this.m_textBeginOffset_) {
        this.targetText.setIndex(start);
        this.targetText.previous();
        if ((getFCD(this.targetText, this.targetText.getIndex()) & 0xFF) != 0) {
          this.m_colEIter_.setExactOffset(start);
          ce = this.m_colEIter_.previous();
          if (ce != -1 && ce != 0)
            return true; 
        } 
      } 
    } 
    return false;
  }
  
  private final boolean hasAccentsAfterMatch(int start, int end) {
    if (this.m_pattern_.m_hasSuffixAccents_) {
      this.targetText.setIndex(end);
      if (end > this.m_textBeginOffset_ && UTF16.isTrailSurrogate(this.targetText.previous()))
        if (this.targetText.getIndex() > this.m_textBeginOffset_ && !UTF16.isLeadSurrogate(this.targetText.previous()))
          this.targetText.next();  
      if ((getFCD(this.targetText, this.targetText.getIndex()) & 0xFF) != 0) {
        int firstce = this.m_pattern_.m_CE_[0];
        this.m_colEIter_.setExactOffset(start);
        while (getCE(this.m_colEIter_.next()) != firstce);
        int count = 1;
        while (count < this.m_pattern_.m_CELength_) {
          if (getCE(this.m_colEIter_.next()) == 0)
            count--; 
          count++;
        } 
        int ce = this.m_colEIter_.next();
        if (ce != -1 && ce != 0)
          ce = getCE(ce); 
        if (ce != -1 && ce != 0) {
          if (this.m_colEIter_.getOffset() <= end)
            return true; 
          if (getFCD(this.targetText, end) >> 8 != 0)
            return true; 
        } 
      } 
    } 
    return false;
  }
  
  private static final boolean isOutOfBounds(int textstart, int textlimit, int offset) {
    return (offset < textstart || offset > textlimit);
  }
  
  private final boolean checkIdentical(int start, int end) {
    if (this.m_collator_.getStrength() != 15)
      return true; 
    String textstr = getString(this.targetText, start, end - start);
    if (Normalizer.quickCheck(textstr, Normalizer.NFD, 0) == Normalizer.NO)
      textstr = Normalizer.decompose(textstr, false); 
    String patternstr = this.m_pattern_.targetText;
    if (Normalizer.quickCheck(patternstr, Normalizer.NFD, 0) == Normalizer.NO)
      patternstr = Normalizer.decompose(patternstr, false); 
    return textstr.equals(patternstr);
  }
  
  private final boolean checkRepeatedMatch(int start, int limit) {
    if (this.m_matchedIndex_ == -1)
      return false; 
    int end = limit - 1;
    int lastmatchend = this.m_matchedIndex_ + this.matchLength - 1;
    if (!isOverlapping())
      return ((start >= this.m_matchedIndex_ && start <= lastmatchend) || (end >= this.m_matchedIndex_ && end <= lastmatchend) || (start <= this.m_matchedIndex_ && end >= lastmatchend)); 
    return (start <= this.m_matchedIndex_ && end >= lastmatchend);
  }
  
  private final boolean checkNextExactContractionMatch(int start, int end) {
    char endchar = Character.MIN_VALUE;
    if (end < this.m_textLimitOffset_) {
      this.targetText.setIndex(end);
      endchar = this.targetText.current();
    } 
    char poststartchar = Character.MIN_VALUE;
    if (start + 1 < this.m_textLimitOffset_) {
      this.targetText.setIndex(start + 1);
      poststartchar = this.targetText.current();
    } 
    if (this.m_collator_.isUnsafe(endchar) || this.m_collator_.isUnsafe(poststartchar)) {
      int bufferedCEOffset = this.m_colEIter_.m_CEBufferOffset_;
      boolean hasBufferedCE = (bufferedCEOffset > 0);
      this.m_colEIter_.setExactOffset(start);
      int temp = start;
      while (bufferedCEOffset > 0) {
        this.m_colEIter_.next();
        if (this.m_colEIter_.getOffset() != temp) {
          start = temp;
          temp = this.m_colEIter_.getOffset();
        } 
        bufferedCEOffset--;
      } 
      int count = 0;
      while (count < this.m_pattern_.m_CELength_) {
        int ce = getCE(this.m_colEIter_.next());
        if (ce == 0)
          continue; 
        if (hasBufferedCE && count == 0 && this.m_colEIter_.getOffset() != temp) {
          start = temp;
          temp = this.m_colEIter_.getOffset();
        } 
        if (ce != this.m_pattern_.m_CE_[count]) {
          end++;
          end = getNextBaseOffset(end);
          this.m_utilBuffer_[0] = start;
          this.m_utilBuffer_[1] = end;
          return false;
        } 
        count++;
      } 
    } 
    this.m_utilBuffer_[0] = start;
    this.m_utilBuffer_[1] = end;
    return true;
  }
  
  private final boolean checkNextExactMatch(int textoffset) {
    int start = this.m_colEIter_.getOffset();
    if (!checkNextExactContractionMatch(start, textoffset)) {
      this.m_utilBuffer_[0] = this.m_utilBuffer_[1];
      return false;
    } 
    start = this.m_utilBuffer_[0];
    textoffset = this.m_utilBuffer_[1];
    if (!isBreakUnit(start, textoffset) || checkRepeatedMatch(start, textoffset) || hasAccentsBeforeMatch(start, textoffset) || !checkIdentical(start, textoffset) || hasAccentsAfterMatch(start, textoffset)) {
      textoffset++;
      textoffset = getNextBaseOffset(textoffset);
      this.m_utilBuffer_[0] = textoffset;
      return false;
    } 
    if (this.m_collator_.getStrength() == 0)
      textoffset = checkBreakBoundary(textoffset); 
    this.m_matchedIndex_ = start;
    this.matchLength = textoffset - start;
    return true;
  }
  
  private final int getPreviousBaseOffset(CharacterIterator text, int textoffset) {
    if (textoffset > this.m_textBeginOffset_)
      while (true) {
        int result = textoffset;
        text.setIndex(result);
        if (UTF16.isTrailSurrogate(text.previous()) && 
          text.getIndex() != text.getBeginIndex() && !UTF16.isLeadSurrogate(text.previous()))
          text.next(); 
        textoffset = text.getIndex();
        char fcd = getFCD(text, textoffset);
        if (fcd >> 8 == 0) {
          if ((fcd & 0xFF) != 0)
            return textoffset; 
          return result;
        } 
        if (textoffset == this.m_textBeginOffset_)
          return this.m_textBeginOffset_; 
      }  
    return textoffset;
  }
  
  private int getUnblockedAccentIndex(StringBuilder accents, int[] accentsindex) {
    int index = 0;
    int length = accents.length();
    int cclass = 0;
    int result = 0;
    while (index < length) {
      int codepoint = UTF16.charAt(accents, index);
      int tempclass = UCharacter.getCombiningClass(codepoint);
      if (tempclass != cclass) {
        cclass = tempclass;
        accentsindex[result] = index;
        result++;
      } 
      if (UCharacter.isSupplementary(codepoint)) {
        index += 2;
        continue;
      } 
      index++;
    } 
    accentsindex[result] = length;
    return result;
  }
  
  private static final StringBuilder merge(StringBuilder source1, CharacterIterator source2, int start2, int end2, StringBuilder source3) {
    StringBuilder result = new StringBuilder();
    if (source1 != null && source1.length() != 0)
      result.append(source1); 
    source2.setIndex(start2);
    while (source2.getIndex() < end2) {
      result.append(source2.current());
      source2.next();
    } 
    if (source3 != null && source3.length() != 0)
      result.append(source3); 
    return result;
  }
  
  private final boolean checkCollationMatch(CollationElementIterator coleiter) {
    int patternceindex = this.m_pattern_.m_CELength_;
    int offset = 0;
    while (patternceindex > 0) {
      int ce = getCE(coleiter.next());
      if (ce == 0)
        continue; 
      if (ce != this.m_pattern_.m_CE_[offset])
        return false; 
      offset++;
      patternceindex--;
    } 
    return true;
  }
  
  private int doNextCanonicalPrefixMatch(int start, int end) {
    if ((getFCD(this.targetText, start) & 0xFF) == 0)
      return -1; 
    start = this.targetText.getIndex();
    int offset = getNextBaseOffset(this.targetText, start);
    start = getPreviousBaseOffset(start);
    StringBuilder accents = new StringBuilder();
    String accentstr = getString(this.targetText, start, offset - start);
    if (Normalizer.quickCheck(accentstr, Normalizer.NFD, 0) == Normalizer.NO)
      accentstr = Normalizer.decompose(accentstr, false); 
    accents.append(accentstr);
    int[] accentsindex = new int[256];
    int accentsize = getUnblockedAccentIndex(accents, accentsindex);
    int count = (2 << accentsize - 1) - 1;
    while (count > 0) {
      this.m_canonicalPrefixAccents_.delete(0, this.m_canonicalPrefixAccents_.length());
      int k = 0;
      for (; k < accentsindex[0]; k++)
        this.m_canonicalPrefixAccents_.append(accents.charAt(k)); 
      for (int i = 0; i <= accentsize - 1; i++) {
        int mask = 1 << accentsize - i - 1;
        if ((count & mask) != 0)
          for (int j = accentsindex[i]; j < accentsindex[i + 1]; 
            j++)
            this.m_canonicalPrefixAccents_.append(accents.charAt(j));  
      } 
      StringBuilder match = merge(this.m_canonicalPrefixAccents_, this.targetText, offset, end, this.m_canonicalSuffixAccents_);
      this.m_utilColEIter_.setText(match.toString());
      if (checkCollationMatch(this.m_utilColEIter_))
        return start; 
      count--;
    } 
    return -1;
  }
  
  private final int getPreviousSafeOffset(int start, int textoffset) {
    int result = textoffset;
    this.targetText.setIndex(textoffset);
    while (result >= start && this.m_collator_.isUnsafe(this.targetText.previous()))
      result = this.targetText.getIndex(); 
    if (result != start)
      result = this.targetText.getIndex(); 
    return result;
  }
  
  private int doNextCanonicalSuffixMatch(int textoffset) {
    StringBuilder safetext;
    int safelength = 0;
    int safeoffset = this.m_textBeginOffset_;
    if (textoffset != this.m_textBeginOffset_ && this.m_canonicalSuffixAccents_.length() > 0 && this.m_collator_.isUnsafe(this.m_canonicalSuffixAccents_.charAt(0))) {
      safeoffset = getPreviousSafeOffset(this.m_textBeginOffset_, textoffset);
      safelength = textoffset - safeoffset;
      safetext = merge((StringBuilder)null, this.targetText, safeoffset, textoffset, this.m_canonicalSuffixAccents_);
    } else {
      safetext = this.m_canonicalSuffixAccents_;
    } 
    CollationElementIterator coleiter = this.m_utilColEIter_;
    coleiter.setText(safetext.toString());
    int ceindex = this.m_pattern_.m_CELength_ - 1;
    boolean isSafe = true;
    while (ceindex >= 0) {
      int textce = coleiter.previous();
      if (textce == -1) {
        if (coleiter == this.m_colEIter_)
          return -1; 
        coleiter = this.m_colEIter_;
        if (safetext != this.m_canonicalSuffixAccents_)
          safetext.delete(0, safetext.length()); 
        coleiter.setExactOffset(safeoffset);
        isSafe = false;
        continue;
      } 
      textce = getCE(textce);
      if (textce != 0 && textce != this.m_pattern_.m_CE_[ceindex]) {
        int failedoffset = coleiter.getOffset();
        if (isSafe && failedoffset >= safelength)
          return -1; 
        if (isSafe)
          failedoffset += safeoffset; 
        int result = doNextCanonicalPrefixMatch(failedoffset, textoffset);
        if (result != -1)
          this.m_colEIter_.setExactOffset(result); 
        return result;
      } 
      if (textce == this.m_pattern_.m_CE_[ceindex])
        ceindex--; 
    } 
    if (isSafe) {
      int result = coleiter.getOffset();
      int leftoverces = coleiter.m_CEBufferOffset_;
      if (result >= safelength) {
        result = textoffset;
      } else {
        result += safeoffset;
      } 
      this.m_colEIter_.setExactOffset(result);
      this.m_colEIter_.m_CEBufferOffset_ = leftoverces;
      return result;
    } 
    return coleiter.getOffset();
  }
  
  private boolean doNextCanonicalMatch(int textoffset) {
    int offset = this.m_colEIter_.getOffset();
    this.targetText.setIndex(textoffset);
    if (UTF16.isTrailSurrogate(this.targetText.previous()) && this.targetText.getIndex() > this.m_textBeginOffset_)
      if (!UTF16.isLeadSurrogate(this.targetText.previous()))
        this.targetText.next();  
    if ((getFCD(this.targetText, this.targetText.getIndex()) & 0xFF) == 0) {
      if (this.m_pattern_.m_hasPrefixAccents_) {
        offset = doNextCanonicalPrefixMatch(offset, textoffset);
        if (offset != -1) {
          this.m_colEIter_.setExactOffset(offset);
          return true;
        } 
      } 
      return false;
    } 
    if (!this.m_pattern_.m_hasSuffixAccents_)
      return false; 
    StringBuilder accents = new StringBuilder();
    int baseoffset = getPreviousBaseOffset(this.targetText, textoffset);
    String accentstr = getString(this.targetText, baseoffset, textoffset - baseoffset);
    if (Normalizer.quickCheck(accentstr, Normalizer.NFD, 0) == Normalizer.NO)
      accentstr = Normalizer.decompose(accentstr, false); 
    accents.append(accentstr);
    int[] accentsindex = new int[256];
    int size = getUnblockedAccentIndex(accents, accentsindex);
    int count = (2 << size - 1) - 1;
    while (count > 0) {
      this.m_canonicalSuffixAccents_.delete(0, this.m_canonicalSuffixAccents_.length());
      for (int k = 0; k < accentsindex[0]; k++)
        this.m_canonicalSuffixAccents_.append(accents.charAt(k)); 
      for (int i = 0; i <= size - 1; i++) {
        int mask = 1 << size - i - 1;
        if ((count & mask) != 0)
          for (int j = accentsindex[i]; j < accentsindex[i + 1]; 
            j++)
            this.m_canonicalSuffixAccents_.append(accents.charAt(j));  
      } 
      offset = doNextCanonicalSuffixMatch(baseoffset);
      if (offset != -1)
        return true; 
      count--;
    } 
    return false;
  }
  
  private final int getPreviousBaseOffset(int textoffset) {
    if (this.m_pattern_.m_hasPrefixAccents_ && textoffset > this.m_textBeginOffset_) {
      int offset = textoffset;
      if (getFCD(this.targetText, offset) >> 8 != 0)
        return getPreviousBaseOffset(this.targetText, textoffset); 
    } 
    return textoffset;
  }
  
  private boolean checkNextCanonicalContractionMatch(int start, int end) {
    char schar = Character.MIN_VALUE;
    char echar = Character.MIN_VALUE;
    if (end < this.m_textLimitOffset_) {
      this.targetText.setIndex(end);
      echar = this.targetText.current();
    } 
    if (start < this.m_textLimitOffset_) {
      this.targetText.setIndex(start + 1);
      schar = this.targetText.current();
    } 
    if (this.m_collator_.isUnsafe(echar) || this.m_collator_.isUnsafe(schar)) {
      int expansion = this.m_colEIter_.m_CEBufferOffset_;
      boolean hasExpansion = (expansion > 0);
      this.m_colEIter_.setExactOffset(start);
      int temp = start;
      while (expansion > 0) {
        this.m_colEIter_.next();
        if (this.m_colEIter_.getOffset() != temp) {
          start = temp;
          temp = this.m_colEIter_.getOffset();
        } 
        expansion--;
      } 
      int count = 0;
      while (count < this.m_pattern_.m_CELength_) {
        int ce = getCE(this.m_colEIter_.next());
        if (ce == 0)
          continue; 
        if (hasExpansion && count == 0 && this.m_colEIter_.getOffset() != temp) {
          start = temp;
          temp = this.m_colEIter_.getOffset();
        } 
        if (count == 0 && ce != this.m_pattern_.m_CE_[0]) {
          int expected = this.m_pattern_.m_CE_[0];
          if ((getFCD(this.targetText, start) & 0xFF) != 0) {
            ce = getCE(this.m_colEIter_.next());
            while (ce != expected && ce != -1 && this.m_colEIter_.getOffset() <= end)
              ce = getCE(this.m_colEIter_.next()); 
          } 
        } 
        if (ce != this.m_pattern_.m_CE_[count]) {
          end++;
          end = getNextBaseOffset(end);
          this.m_utilBuffer_[0] = start;
          this.m_utilBuffer_[1] = end;
          return false;
        } 
        count++;
      } 
    } 
    this.m_utilBuffer_[0] = start;
    this.m_utilBuffer_[1] = end;
    return true;
  }
  
  private boolean checkNextCanonicalMatch(int textoffset) {
    if ((this.m_pattern_.m_hasSuffixAccents_ && this.m_canonicalSuffixAccents_.length() != 0) || (this.m_pattern_.m_hasPrefixAccents_ && this.m_canonicalPrefixAccents_.length() != 0)) {
      this.m_matchedIndex_ = getPreviousBaseOffset(this.m_colEIter_.getOffset());
      this.matchLength = textoffset - this.m_matchedIndex_;
      return true;
    } 
    int start = this.m_colEIter_.getOffset();
    if (!checkNextCanonicalContractionMatch(start, textoffset)) {
      this.m_utilBuffer_[0] = this.m_utilBuffer_[1];
      return false;
    } 
    start = this.m_utilBuffer_[0];
    textoffset = this.m_utilBuffer_[1];
    start = getPreviousBaseOffset(start);
    if (checkRepeatedMatch(start, textoffset) || !isBreakUnit(start, textoffset) || !checkIdentical(start, textoffset)) {
      textoffset++;
      textoffset = getNextBaseOffset(this.targetText, textoffset);
      this.m_utilBuffer_[0] = textoffset;
      return false;
    } 
    this.m_matchedIndex_ = start;
    this.matchLength = textoffset - start;
    return true;
  }
  
  private int reverseShift(int textoffset, int ce, int patternceindex) {
    if (isOverlapping()) {
      if (textoffset != this.m_textLimitOffset_) {
        textoffset--;
      } else {
        textoffset -= this.m_pattern_.m_defaultShiftSize_;
      } 
    } else if (ce != -1) {
      int shift = this.m_pattern_.m_backShift_[hash(ce)];
      int adjust = patternceindex;
      if (adjust > 1 && shift > adjust)
        shift -= adjust - 1; 
      textoffset -= shift;
    } else {
      textoffset -= this.m_pattern_.m_defaultShiftSize_;
    } 
    textoffset = getPreviousBaseOffset(textoffset);
    return textoffset;
  }
  
  private boolean checkPreviousExactContractionMatch(int start, int end) {
    char echar = Character.MIN_VALUE;
    if (end < this.m_textLimitOffset_) {
      this.targetText.setIndex(end);
      echar = this.targetText.current();
    } 
    char schar = Character.MIN_VALUE;
    if (start + 1 < this.m_textLimitOffset_) {
      this.targetText.setIndex(start + 1);
      schar = this.targetText.current();
    } 
    if (this.m_collator_.isUnsafe(echar) || this.m_collator_.isUnsafe(schar)) {
      int expansion = this.m_colEIter_.m_CEBufferSize_ - this.m_colEIter_.m_CEBufferOffset_;
      boolean hasExpansion = (expansion > 0);
      this.m_colEIter_.setExactOffset(end);
      int temp = end;
      while (expansion > 0) {
        this.m_colEIter_.previous();
        if (this.m_colEIter_.getOffset() != temp) {
          end = temp;
          temp = this.m_colEIter_.getOffset();
        } 
        expansion--;
      } 
      int count = this.m_pattern_.m_CELength_;
      while (count > 0) {
        int ce = getCE(this.m_colEIter_.previous());
        if (ce == 0)
          continue; 
        if (hasExpansion && count == 0 && this.m_colEIter_.getOffset() != temp) {
          end = temp;
          temp = this.m_colEIter_.getOffset();
        } 
        if (ce != this.m_pattern_.m_CE_[count - 1]) {
          start--;
          start = getPreviousBaseOffset(this.targetText, start);
          this.m_utilBuffer_[0] = start;
          this.m_utilBuffer_[1] = end;
          return false;
        } 
        count--;
      } 
    } 
    this.m_utilBuffer_[0] = start;
    this.m_utilBuffer_[1] = end;
    return true;
  }
  
  private final boolean checkPreviousExactMatch(int textoffset) {
    int end = this.m_colEIter_.getOffset();
    if (!checkPreviousExactContractionMatch(textoffset, end))
      return false; 
    textoffset = this.m_utilBuffer_[0];
    end = this.m_utilBuffer_[1];
    if (checkRepeatedMatch(textoffset, end) || !isBreakUnit(textoffset, end) || hasAccentsBeforeMatch(textoffset, end) || !checkIdentical(textoffset, end) || hasAccentsAfterMatch(textoffset, end)) {
      textoffset--;
      textoffset = getPreviousBaseOffset(this.targetText, textoffset);
      this.m_utilBuffer_[0] = textoffset;
      return false;
    } 
    if (this.m_collator_.getStrength() == 0)
      end = checkBreakBoundary(end); 
    this.m_matchedIndex_ = textoffset;
    this.matchLength = end - textoffset;
    return true;
  }
  
  private int doPreviousCanonicalSuffixMatch(int start, int end) {
    this.targetText.setIndex(end);
    if (UTF16.isTrailSurrogate(this.targetText.previous()) && this.targetText.getIndex() > this.m_textBeginOffset_)
      if (!UTF16.isLeadSurrogate(this.targetText.previous()))
        this.targetText.next();  
    if ((getFCD(this.targetText, this.targetText.getIndex()) & 0xFF) == 0)
      return -1; 
    end = getNextBaseOffset(this.targetText, end);
    StringBuilder accents = new StringBuilder();
    int offset = getPreviousBaseOffset(this.targetText, end);
    String accentstr = getString(this.targetText, offset, end - offset);
    if (Normalizer.quickCheck(accentstr, Normalizer.NFD, 0) == Normalizer.NO)
      accentstr = Normalizer.decompose(accentstr, false); 
    accents.append(accentstr);
    int[] accentsindex = new int[256];
    int accentsize = getUnblockedAccentIndex(accents, accentsindex);
    int count = (2 << accentsize - 1) - 1;
    while (count > 0) {
      this.m_canonicalSuffixAccents_.delete(0, this.m_canonicalSuffixAccents_.length());
      for (int k = 0; k < accentsindex[0]; k++)
        this.m_canonicalSuffixAccents_.append(accents.charAt(k)); 
      for (int i = 0; i <= accentsize - 1; i++) {
        int mask = 1 << accentsize - i - 1;
        if ((count & mask) != 0)
          for (int j = accentsindex[i]; j < accentsindex[i + 1]; 
            j++)
            this.m_canonicalSuffixAccents_.append(accents.charAt(j));  
      } 
      StringBuilder match = merge(this.m_canonicalPrefixAccents_, this.targetText, start, offset, this.m_canonicalSuffixAccents_);
      this.m_utilColEIter_.setText(match.toString());
      if (checkCollationMatch(this.m_utilColEIter_))
        return end; 
      count--;
    } 
    return -1;
  }
  
  private int doPreviousCanonicalPrefixMatch(int textoffset) {
    StringBuilder safetext;
    int safeoffset = textoffset;
    if (textoffset > this.m_textBeginOffset_ && this.m_collator_.isUnsafe(this.m_canonicalPrefixAccents_.charAt(this.m_canonicalPrefixAccents_.length() - 1))) {
      safeoffset = getNextSafeOffset(textoffset, this.m_textLimitOffset_);
      safetext = merge(this.m_canonicalPrefixAccents_, this.targetText, textoffset, safeoffset, (StringBuilder)null);
    } else {
      safetext = this.m_canonicalPrefixAccents_;
    } 
    CollationElementIterator coleiter = this.m_utilColEIter_;
    coleiter.setText(safetext.toString());
    int ceindex = 0;
    boolean isSafe = true;
    int prefixlength = this.m_canonicalPrefixAccents_.length();
    while (ceindex < this.m_pattern_.m_CELength_) {
      int textce = coleiter.next();
      if (textce == -1) {
        if (coleiter == this.m_colEIter_)
          return -1; 
        if (safetext != this.m_canonicalPrefixAccents_)
          safetext.delete(0, safetext.length()); 
        coleiter = this.m_colEIter_;
        coleiter.setExactOffset(safeoffset);
        isSafe = false;
        continue;
      } 
      textce = getCE(textce);
      if (textce != 0 && textce != this.m_pattern_.m_CE_[ceindex]) {
        int failedoffset = coleiter.getOffset();
        if (isSafe && failedoffset <= prefixlength)
          return -1; 
        if (isSafe) {
          failedoffset = safeoffset - failedoffset;
          if (safetext != this.m_canonicalPrefixAccents_)
            safetext.delete(0, safetext.length()); 
        } 
        int result = doPreviousCanonicalSuffixMatch(textoffset, failedoffset);
        if (result != -1)
          this.m_colEIter_.setExactOffset(result); 
        return result;
      } 
      if (textce == this.m_pattern_.m_CE_[ceindex])
        ceindex++; 
    } 
    if (isSafe) {
      int result = coleiter.getOffset();
      int leftoverces = coleiter.m_CEBufferSize_ - coleiter.m_CEBufferOffset_;
      if (result <= prefixlength) {
        result = textoffset;
      } else {
        result = textoffset + safeoffset - result;
      } 
      this.m_colEIter_.setExactOffset(result);
      this.m_colEIter_.m_CEBufferOffset_ = this.m_colEIter_.m_CEBufferSize_ - leftoverces;
      return result;
    } 
    return coleiter.getOffset();
  }
  
  private boolean doPreviousCanonicalMatch(int textoffset) {
    int offset = this.m_colEIter_.getOffset();
    if (getFCD(this.targetText, textoffset) >> 8 == 0) {
      if (this.m_pattern_.m_hasSuffixAccents_) {
        offset = doPreviousCanonicalSuffixMatch(textoffset, offset);
        if (offset != -1) {
          this.m_colEIter_.setExactOffset(offset);
          return true;
        } 
      } 
      return false;
    } 
    if (!this.m_pattern_.m_hasPrefixAccents_)
      return false; 
    StringBuilder accents = new StringBuilder();
    int baseoffset = getNextBaseOffset(this.targetText, textoffset);
    String textstr = getString(this.targetText, textoffset, baseoffset - textoffset);
    if (Normalizer.quickCheck(textstr, Normalizer.NFD, 0) == Normalizer.NO)
      textstr = Normalizer.decompose(textstr, false); 
    accents.append(textstr);
    int[] accentsindex = new int[256];
    int size = getUnblockedAccentIndex(accents, accentsindex);
    int count = (2 << size - 1) - 1;
    while (count > 0) {
      this.m_canonicalPrefixAccents_.delete(0, this.m_canonicalPrefixAccents_.length());
      for (int k = 0; k < accentsindex[0]; k++)
        this.m_canonicalPrefixAccents_.append(accents.charAt(k)); 
      for (int i = 0; i <= size - 1; i++) {
        int mask = 1 << size - i - 1;
        if ((count & mask) != 0)
          for (int j = accentsindex[i]; j < accentsindex[i + 1]; 
            j++)
            this.m_canonicalPrefixAccents_.append(accents.charAt(j));  
      } 
      offset = doPreviousCanonicalPrefixMatch(baseoffset);
      if (offset != -1)
        return true; 
      count--;
    } 
    return false;
  }
  
  private boolean checkPreviousCanonicalContractionMatch(int start, int end) {
    int temp = end;
    char echar = Character.MIN_VALUE;
    char schar = Character.MIN_VALUE;
    if (end < this.m_textLimitOffset_) {
      this.targetText.setIndex(end);
      echar = this.targetText.current();
    } 
    if (start + 1 < this.m_textLimitOffset_) {
      this.targetText.setIndex(start + 1);
      schar = this.targetText.current();
    } 
    if (this.m_collator_.isUnsafe(echar) || this.m_collator_.isUnsafe(schar)) {
      int expansion = this.m_colEIter_.m_CEBufferSize_ - this.m_colEIter_.m_CEBufferOffset_;
      boolean hasExpansion = (expansion > 0);
      this.m_colEIter_.setExactOffset(end);
      while (expansion > 0) {
        this.m_colEIter_.previous();
        if (this.m_colEIter_.getOffset() != temp) {
          end = temp;
          temp = this.m_colEIter_.getOffset();
        } 
        expansion--;
      } 
      int count = this.m_pattern_.m_CELength_;
      while (count > 0) {
        int ce = getCE(this.m_colEIter_.previous());
        if (ce == 0)
          continue; 
        if (hasExpansion && count == 0 && this.m_colEIter_.getOffset() != temp) {
          end = temp;
          temp = this.m_colEIter_.getOffset();
        } 
        if (count == this.m_pattern_.m_CELength_ && ce != this.m_pattern_.m_CE_[this.m_pattern_.m_CELength_ - 1]) {
          int expected = this.m_pattern_.m_CE_[this.m_pattern_.m_CELength_ - 1];
          this.targetText.setIndex(end);
          if (UTF16.isTrailSurrogate(this.targetText.previous()) && 
            this.targetText.getIndex() > this.m_textBeginOffset_ && !UTF16.isLeadSurrogate(this.targetText.previous()))
            this.targetText.next(); 
          end = this.targetText.getIndex();
          if ((getFCD(this.targetText, end) & 0xFF) != 0) {
            ce = getCE(this.m_colEIter_.previous());
            while (ce != expected && ce != -1 && this.m_colEIter_.getOffset() <= start)
              ce = getCE(this.m_colEIter_.previous()); 
          } 
        } 
        if (ce != this.m_pattern_.m_CE_[count - 1]) {
          start--;
          start = getPreviousBaseOffset(start);
          this.m_utilBuffer_[0] = start;
          this.m_utilBuffer_[1] = end;
          return false;
        } 
        count--;
      } 
    } 
    this.m_utilBuffer_[0] = start;
    this.m_utilBuffer_[1] = end;
    return true;
  }
  
  private boolean checkPreviousCanonicalMatch(int textoffset) {
    if ((this.m_pattern_.m_hasSuffixAccents_ && this.m_canonicalSuffixAccents_.length() != 0) || (this.m_pattern_.m_hasPrefixAccents_ && this.m_canonicalPrefixAccents_.length() != 0)) {
      this.m_matchedIndex_ = textoffset;
      this.matchLength = getNextBaseOffset(this.m_colEIter_.getOffset()) - textoffset;
      return true;
    } 
    int end = this.m_colEIter_.getOffset();
    if (!checkPreviousCanonicalContractionMatch(textoffset, end))
      return false; 
    textoffset = this.m_utilBuffer_[0];
    end = this.m_utilBuffer_[1];
    end = getNextBaseOffset(end);
    if (checkRepeatedMatch(textoffset, end) || !isBreakUnit(textoffset, end) || !checkIdentical(textoffset, end)) {
      textoffset--;
      textoffset = getPreviousBaseOffset(textoffset);
      this.m_utilBuffer_[0] = textoffset;
      return false;
    } 
    this.m_matchedIndex_ = textoffset;
    this.matchLength = end - textoffset;
    return true;
  }
  
  private void handleNextExact(int start) {
    int textoffset = shiftForward(start, -1, this.m_pattern_.m_CELength_);
    int targetce = 0;
    while (textoffset <= this.m_textLimitOffset_) {
      this.m_colEIter_.setExactOffset(textoffset);
      int patternceindex = this.m_pattern_.m_CELength_ - 1;
      boolean found = false;
      int lastce = -1;
      while (true) {
        targetce = this.m_colEIter_.previous();
        if (targetce == -1) {
          found = false;
          break;
        } 
        targetce = getCE(targetce);
        if (targetce == 0 && this.m_colEIter_.isInBuffer())
          continue; 
        if (lastce == -1 || lastce == 0)
          lastce = targetce; 
        if (targetce == this.m_pattern_.m_CE_[patternceindex]) {
          found = true;
          break;
        } 
        if (this.m_colEIter_.m_CEBufferOffset_ <= 0) {
          found = false;
          break;
        } 
      } 
      while (found && patternceindex > 0) {
        lastce = targetce;
        targetce = this.m_colEIter_.previous();
        if (targetce == -1) {
          found = false;
          break;
        } 
        targetce = getCE(targetce);
        if (targetce == 0)
          continue; 
        patternceindex--;
        found = (found && targetce == this.m_pattern_.m_CE_[patternceindex]);
      } 
      targetce = lastce;
      if (!found) {
        textoffset = shiftForward(textoffset, lastce, patternceindex);
        patternceindex = this.m_pattern_.m_CELength_;
        continue;
      } 
      if (checkNextExactMatch(textoffset))
        return; 
      textoffset = this.m_utilBuffer_[0];
    } 
    setMatchNotFound();
  }
  
  private void handleNextCanonical(int start) {
    boolean hasPatternAccents = (this.m_pattern_.m_hasSuffixAccents_ || this.m_pattern_.m_hasPrefixAccents_);
    int textoffset = shiftForward(start, -1, this.m_pattern_.m_CELength_);
    this.m_canonicalPrefixAccents_.delete(0, this.m_canonicalPrefixAccents_.length());
    this.m_canonicalSuffixAccents_.delete(0, this.m_canonicalSuffixAccents_.length());
    int targetce = 0;
    while (textoffset <= this.m_textLimitOffset_) {
      this.m_colEIter_.setExactOffset(textoffset);
      int patternceindex = this.m_pattern_.m_CELength_ - 1;
      boolean found = false;
      int lastce = -1;
      while (true) {
        targetce = this.m_colEIter_.previous();
        if (targetce == -1) {
          found = false;
          break;
        } 
        targetce = getCE(targetce);
        if (lastce == -1 || lastce == 0)
          lastce = targetce; 
        if (targetce == this.m_pattern_.m_CE_[patternceindex]) {
          found = true;
          break;
        } 
        if (this.m_colEIter_.m_CEBufferOffset_ <= 0) {
          found = false;
          break;
        } 
      } 
      while (found && patternceindex > 0) {
        targetce = this.m_colEIter_.previous();
        if (targetce == -1) {
          found = false;
          break;
        } 
        targetce = getCE(targetce);
        if (targetce == 0)
          continue; 
        patternceindex--;
        found = (found && targetce == this.m_pattern_.m_CE_[patternceindex]);
      } 
      if (hasPatternAccents && !found)
        found = doNextCanonicalMatch(textoffset); 
      if (!found) {
        textoffset = shiftForward(textoffset, lastce, patternceindex);
        patternceindex = this.m_pattern_.m_CELength_;
        continue;
      } 
      if (checkNextCanonicalMatch(textoffset))
        return; 
      textoffset = this.m_utilBuffer_[0];
    } 
    setMatchNotFound();
  }
  
  private void handlePreviousExact(int start) {
    int textoffset = reverseShift(start, -1, this.m_pattern_.m_CELength_);
    while (textoffset >= this.m_textBeginOffset_) {
      this.m_colEIter_.setExactOffset(textoffset);
      int patternceindex = 1;
      int targetce = 0;
      boolean found = false;
      int firstce = -1;
      while (true) {
        targetce = this.m_colEIter_.next();
        if (targetce == -1) {
          found = false;
          break;
        } 
        targetce = getCE(targetce);
        if (firstce == -1 || firstce == 0)
          firstce = targetce; 
        if (targetce == 0 && this.m_collator_.getStrength() != 0)
          continue; 
        if (targetce == this.m_pattern_.m_CE_[0]) {
          found = true;
          break;
        } 
        if (this.m_colEIter_.m_CEBufferOffset_ == -1 || this.m_colEIter_.m_CEBufferOffset_ == this.m_colEIter_.m_CEBufferSize_) {
          found = false;
          break;
        } 
      } 
      while (found && patternceindex < this.m_pattern_.m_CELength_) {
        firstce = targetce;
        targetce = this.m_colEIter_.next();
        if (targetce == -1) {
          found = false;
          break;
        } 
        targetce = getCE(targetce);
        if (targetce == 0)
          continue; 
        found = (found && targetce == this.m_pattern_.m_CE_[patternceindex]);
        patternceindex++;
      } 
      targetce = firstce;
      if (!found) {
        textoffset = reverseShift(textoffset, targetce, patternceindex);
        patternceindex = 0;
        continue;
      } 
      if (checkPreviousExactMatch(textoffset))
        return; 
      textoffset = this.m_utilBuffer_[0];
    } 
    setMatchNotFound();
  }
  
  private void handlePreviousCanonical(int start) {
    boolean hasPatternAccents = (this.m_pattern_.m_hasSuffixAccents_ || this.m_pattern_.m_hasPrefixAccents_);
    int textoffset = reverseShift(start, -1, this.m_pattern_.m_CELength_);
    this.m_canonicalPrefixAccents_.delete(0, this.m_canonicalPrefixAccents_.length());
    this.m_canonicalSuffixAccents_.delete(0, this.m_canonicalSuffixAccents_.length());
    while (textoffset >= this.m_textBeginOffset_) {
      this.m_colEIter_.setExactOffset(textoffset);
      int patternceindex = 1;
      int targetce = 0;
      boolean found = false;
      int firstce = -1;
      while (true) {
        targetce = this.m_colEIter_.next();
        if (targetce == -1) {
          found = false;
          break;
        } 
        targetce = getCE(targetce);
        if (firstce == -1 || firstce == 0)
          firstce = targetce; 
        if (targetce == this.m_pattern_.m_CE_[0]) {
          found = true;
          break;
        } 
        if (this.m_colEIter_.m_CEBufferOffset_ == -1 || this.m_colEIter_.m_CEBufferOffset_ == this.m_colEIter_.m_CEBufferSize_) {
          found = false;
          break;
        } 
      } 
      targetce = firstce;
      while (found && patternceindex < this.m_pattern_.m_CELength_) {
        targetce = this.m_colEIter_.next();
        if (targetce == -1) {
          found = false;
          break;
        } 
        targetce = getCE(targetce);
        if (targetce == 0)
          continue; 
        found = (found && targetce == this.m_pattern_.m_CE_[patternceindex]);
        patternceindex++;
      } 
      if (hasPatternAccents && !found)
        found = doPreviousCanonicalMatch(textoffset); 
      if (!found) {
        textoffset = reverseShift(textoffset, targetce, patternceindex);
        patternceindex = 0;
        continue;
      } 
      if (checkPreviousCanonicalMatch(textoffset))
        return; 
      textoffset = this.m_utilBuffer_[0];
    } 
    setMatchNotFound();
  }
  
  private static final String getString(CharacterIterator text, int start, int length) {
    StringBuilder result = new StringBuilder(length);
    int offset = text.getIndex();
    text.setIndex(start);
    for (int i = 0; i < length; i++) {
      result.append(text.current());
      text.next();
    } 
    text.setIndex(offset);
    return result.toString();
  }
  
  private static final int getMask(int strength) {
    switch (strength) {
      case 0:
        return -65536;
      case 1:
        return -256;
    } 
    return -1;
  }
  
  private void setMatchNotFound() {
    this.m_matchedIndex_ = -1;
    setMatchLength(0);
  }
  
  private int checkBreakBoundary(int end) {
    if (!this.m_charBreakIter_.isBoundary(end))
      end = this.m_charBreakIter_.following(end); 
    return end;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\StringSearch.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */