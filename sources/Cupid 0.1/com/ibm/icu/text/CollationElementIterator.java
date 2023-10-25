package com.ibm.icu.text;

import com.ibm.icu.impl.CharacterIteratorWrapper;
import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.impl.Normalizer2Impl;
import com.ibm.icu.impl.StringUCharacterIterator;
import com.ibm.icu.impl.UCharacterProperty;
import com.ibm.icu.lang.UCharacter;
import java.text.CharacterIterator;
import java.util.MissingResourceException;

public final class CollationElementIterator {
  public static final int NULLORDER = -1;
  
  public static final int IGNORABLE = 0;
  
  boolean m_isCodePointHiragana_;
  
  int m_FCDStart_;
  
  int m_CEBufferOffset_;
  
  int m_CEBufferSize_;
  
  static final int CE_NOT_FOUND_ = -268435456;
  
  static final int CE_EXPANSION_TAG_ = 1;
  
  static final int CE_CONTRACTION_TAG_ = 2;
  
  static final int CE_DIGIT_TAG_ = 13;
  
  private boolean m_isForwards_;
  
  private UCharacterIterator m_source_;
  
  private int m_bufferOffset_;
  
  private StringBuilder m_buffer_;
  
  private int m_FCDLimit_;
  
  private RuleBasedCollator m_collator_;
  
  private int[] m_CEBuffer_;
  
  private static final int CE_BUFFER_INIT_SIZE_ = 512;
  
  private Backup m_utilSpecialBackUp_;
  
  private Backup m_utilSpecialEntryBackUp_;
  
  private Backup m_utilSpecialDiscontiguousBackUp_;
  
  private StringUCharacterIterator m_srcUtilIter_;
  
  private StringBuilder m_utilStringBuffer_;
  
  private StringBuilder m_utilSkippedBuffer_;
  
  private CollationElementIterator m_utilColEIter_;
  
  public int getOffset() {
    if (this.m_bufferOffset_ != -1) {
      if (this.m_isForwards_)
        return this.m_FCDLimit_; 
      return this.m_FCDStart_;
    } 
    return this.m_source_.getIndex();
  }
  
  public int getMaxExpansion(int ce) {
    int start = 0;
    int limit = this.m_collator_.m_expansionEndCE_.length;
    long unsignedce = ce & 0xFFFFFFFFL;
    while (start < limit - 1) {
      int mid = start + (limit - start >> 1);
      long midce = this.m_collator_.m_expansionEndCE_[mid] & 0xFFFFFFFFL;
      if (unsignedce <= midce) {
        limit = mid;
        continue;
      } 
      start = mid;
    } 
    int result = 1;
    if (this.m_collator_.m_expansionEndCE_[start] == ce) {
      result = this.m_collator_.m_expansionEndCEMaxSize_[start];
    } else if (limit < this.m_collator_.m_expansionEndCE_.length && this.m_collator_.m_expansionEndCE_[limit] == ce) {
      result = this.m_collator_.m_expansionEndCEMaxSize_[limit];
    } else if ((ce & 0xFFFF) == 192) {
      result = 2;
    } 
    return result;
  }
  
  public void reset() {
    this.m_source_.setToStart();
    updateInternalState();
  }
  
  public int next() {
    this.m_isForwards_ = true;
    if (this.m_CEBufferSize_ > 0) {
      if (this.m_CEBufferOffset_ < this.m_CEBufferSize_)
        return this.m_CEBuffer_[this.m_CEBufferOffset_++]; 
      this.m_CEBufferSize_ = 0;
      this.m_CEBufferOffset_ = 0;
    } 
    int result = -1;
    char ch = Character.MIN_VALUE;
    do {
      int ch_int = nextChar();
      if (ch_int == -1)
        return -1; 
      ch = (char)ch_int;
      if (this.m_collator_.m_isHiragana4_)
        this.m_isCodePointHiragana_ = ((this.m_isCodePointHiragana_ && ch >= '゙' && ch <= '゜') || (ch >= '぀' && ch <= 'ゞ' && (ch <= 'ゔ' || ch >= 'ゝ'))); 
      if (ch <= 'ÿ') {
        result = this.m_collator_.m_trie_.getLatin1LinearValue(ch);
      } else {
        result = this.m_collator_.m_trie_.getLeadValue(ch);
      } 
      if (!RuleBasedCollator.isSpecial(result))
        return result; 
      if (result != -268435456)
        result = nextSpecial(this.m_collator_, result, ch); 
      if (result != -268435456)
        continue; 
      if (RuleBasedCollator.UCA_ != null) {
        result = RuleBasedCollator.UCA_.m_trie_.getLeadValue(ch);
        if (RuleBasedCollator.isSpecial(result))
          result = nextSpecial(RuleBasedCollator.UCA_, result, ch); 
      } 
      if (result != -268435456)
        continue; 
      result = nextImplicit(ch);
    } while (result == 0 && ch >= '가' && ch <= '힯');
    return result;
  }
  
  public int previous() {
    if (this.m_source_.getIndex() <= 0 && this.m_isForwards_) {
      this.m_source_.setToLimit();
      updateInternalState();
    } 
    this.m_isForwards_ = false;
    if (this.m_CEBufferSize_ > 0) {
      if (this.m_CEBufferOffset_ > 0)
        return this.m_CEBuffer_[--this.m_CEBufferOffset_]; 
      this.m_CEBufferSize_ = 0;
      this.m_CEBufferOffset_ = 0;
    } 
    int result = -1;
    char ch = Character.MIN_VALUE;
    do {
      int ch_int = previousChar();
      if (ch_int == -1)
        return -1; 
      ch = (char)ch_int;
      if (this.m_collator_.m_isHiragana4_)
        this.m_isCodePointHiragana_ = (ch >= '぀' && ch <= 'ゟ'); 
      if (this.m_collator_.isContractionEnd(ch) && !isBackwardsStart()) {
        result = previousSpecial(this.m_collator_, -234881024, ch);
      } else {
        if (ch <= 'ÿ') {
          result = this.m_collator_.m_trie_.getLatin1LinearValue(ch);
        } else {
          result = this.m_collator_.m_trie_.getLeadValue(ch);
        } 
        if (RuleBasedCollator.isSpecial(result))
          result = previousSpecial(this.m_collator_, result, ch); 
        if (result == -268435456) {
          if (!isBackwardsStart() && this.m_collator_.isContractionEnd(ch)) {
            result = -234881024;
          } else if (RuleBasedCollator.UCA_ != null) {
            result = RuleBasedCollator.UCA_.m_trie_.getLeadValue(ch);
          } 
          if (RuleBasedCollator.isSpecial(result) && 
            RuleBasedCollator.UCA_ != null)
            result = previousSpecial(RuleBasedCollator.UCA_, result, ch); 
        } 
      } 
    } while (result == 0 && ch >= '가' && ch <= '힯');
    if (result == -268435456)
      result = previousImplicit(ch); 
    return result;
  }
  
  public static final int primaryOrder(int ce) {
    return (ce & 0xFFFF0000) >>> 16;
  }
  
  public static final int secondaryOrder(int ce) {
    return (ce & 0xFF00) >> 8;
  }
  
  public static final int tertiaryOrder(int ce) {
    return ce & 0xFF;
  }
  
  public void setOffset(int offset) {
    this.m_source_.setIndex(offset);
    int ch_int = this.m_source_.current();
    char ch = (char)ch_int;
    if (ch_int != -1 && this.m_collator_.isUnsafe(ch))
      if (UTF16.isTrailSurrogate(ch)) {
        char prevch = (char)this.m_source_.previous();
        if (!UTF16.isLeadSurrogate(prevch))
          this.m_source_.setIndex(offset); 
      } else {
        while (this.m_source_.getIndex() > 0 && 
          this.m_collator_.isUnsafe(ch))
          ch = (char)this.m_source_.previous(); 
        updateInternalState();
        int prevoffset = 0;
        while (this.m_source_.getIndex() <= offset) {
          prevoffset = this.m_source_.getIndex();
          next();
        } 
        this.m_source_.setIndex(prevoffset);
      }  
    updateInternalState();
    offset = this.m_source_.getIndex();
    if (offset == 0) {
      this.m_isForwards_ = false;
    } else if (offset == this.m_source_.getLength()) {
      this.m_isForwards_ = true;
    } 
  }
  
  public void setText(String source) {
    this.m_srcUtilIter_.setText(source);
    this.m_source_ = (UCharacterIterator)this.m_srcUtilIter_;
    updateInternalState();
  }
  
  public void setText(UCharacterIterator source) {
    this.m_srcUtilIter_.setText(source.getText());
    this.m_source_ = (UCharacterIterator)this.m_srcUtilIter_;
    updateInternalState();
  }
  
  public void setText(CharacterIterator source) {
    this.m_source_ = (UCharacterIterator)new CharacterIteratorWrapper(source);
    this.m_source_.setToStart();
    updateInternalState();
  }
  
  public boolean equals(Object that) {
    if (that == this)
      return true; 
    if (that instanceof CollationElementIterator) {
      CollationElementIterator thatceiter = (CollationElementIterator)that;
      if (!this.m_collator_.equals(thatceiter.m_collator_))
        return false; 
      return (this.m_source_.getIndex() == thatceiter.m_source_.getIndex() && this.m_source_.getText().equals(thatceiter.m_source_.getText()));
    } 
    return false;
  }
  
  public int hashCode() {
    assert false : "hashCode not designed";
    return 42;
  }
  
  private CollationElementIterator(RuleBasedCollator collator) {
    this.m_utilStringBuffer_ = new StringBuilder();
    this.m_collator_ = collator;
    this.m_CEBuffer_ = new int[512];
    this.m_buffer_ = new StringBuilder();
    this.m_utilSpecialBackUp_ = new Backup();
  }
  
  CollationElementIterator(String source, RuleBasedCollator collator) {
    this(collator);
    this.m_source_ = (UCharacterIterator)(this.m_srcUtilIter_ = new StringUCharacterIterator(source));
    updateInternalState();
  }
  
  CollationElementIterator(CharacterIterator source, RuleBasedCollator collator) {
    this(collator);
    this.m_srcUtilIter_ = new StringUCharacterIterator();
    this.m_source_ = (UCharacterIterator)new CharacterIteratorWrapper(source);
    updateInternalState();
  }
  
  CollationElementIterator(UCharacterIterator source, RuleBasedCollator collator) {
    this(collator);
    this.m_srcUtilIter_ = new StringUCharacterIterator();
    this.m_srcUtilIter_.setText(source.getText());
    this.m_source_ = (UCharacterIterator)this.m_srcUtilIter_;
    updateInternalState();
  }
  
  void setCollator(RuleBasedCollator collator) {
    this.m_collator_ = collator;
    updateInternalState();
  }
  
  void setExactOffset(int offset) {
    this.m_source_.setIndex(offset);
    updateInternalState();
  }
  
  boolean isInBuffer() {
    return (this.m_bufferOffset_ > 0);
  }
  
  void setText(UCharacterIterator source, int offset) {
    this.m_srcUtilIter_.setText(source.getText());
    this.m_source_ = (UCharacterIterator)this.m_srcUtilIter_;
    this.m_source_.setIndex(offset);
    updateInternalState();
  }
  
  private static final class Backup {
    protected int m_FCDLimit_;
    
    protected int m_FCDStart_;
    
    protected boolean m_isCodePointHiragana_;
    
    protected int m_bufferOffset_;
    
    protected int m_offset_;
    
    protected StringBuffer m_buffer_ = new StringBuffer();
  }
  
  private static final Normalizer2Impl m_nfcImpl_ = (Norm2AllModes.getNFCInstance()).impl;
  
  private StringBuilder m_unnormalized_;
  
  private Normalizer2Impl.ReorderingBuffer m_n2Buffer_;
  
  private static final int FULL_ZERO_COMBINING_CLASS_FAST_LIMIT_ = 192;
  
  private static final int LEAD_ZERO_COMBINING_CLASS_FAST_LIMIT_ = 768;
  
  private static final int LAST_BYTE_MASK_ = 255;
  
  private static final int SECOND_LAST_BYTE_SHIFT_ = 8;
  
  private static final int CE_CONTRACTION_ = -234881024;
  
  private static final int CE_NOT_FOUND_TAG_ = 0;
  
  private static final int CE_CHARSET_TAG_ = 4;
  
  private static final int CE_HANGUL_SYLLABLE_TAG_ = 6;
  
  private static final int CE_LEAD_SURROGATE_TAG_ = 7;
  
  private static final int CE_TRAIL_SURROGATE_TAG_ = 8;
  
  private static final int CE_CJK_IMPLICIT_TAG_ = 9;
  
  private static final int CE_IMPLICIT_TAG_ = 10;
  
  static final int CE_SPEC_PROC_TAG_ = 11;
  
  private static final int CE_LONG_PRIMARY_TAG_ = 12;
  
  private static final int CE_BYTE_COMMON_ = 5;
  
  private static final int HANGUL_SBASE_ = 44032;
  
  private static final int HANGUL_LBASE_ = 4352;
  
  private static final int HANGUL_VBASE_ = 4449;
  
  private static final int HANGUL_TBASE_ = 4519;
  
  private static final int HANGUL_VCOUNT_ = 21;
  
  private static final int HANGUL_TCOUNT_ = 28;
  
  private static final boolean DEBUG = ICUDebug.enabled("collator");
  
  private void updateInternalState() {
    this.m_isCodePointHiragana_ = false;
    this.m_buffer_.setLength(0);
    this.m_bufferOffset_ = -1;
    this.m_CEBufferOffset_ = 0;
    this.m_CEBufferSize_ = 0;
    this.m_FCDLimit_ = -1;
    this.m_FCDStart_ = this.m_source_.getLength();
    this.m_isForwards_ = true;
  }
  
  private void backupInternalState(Backup backup) {
    backup.m_offset_ = this.m_source_.getIndex();
    backup.m_FCDLimit_ = this.m_FCDLimit_;
    backup.m_FCDStart_ = this.m_FCDStart_;
    backup.m_isCodePointHiragana_ = this.m_isCodePointHiragana_;
    backup.m_bufferOffset_ = this.m_bufferOffset_;
    backup.m_buffer_.setLength(0);
    if (this.m_bufferOffset_ >= 0)
      backup.m_buffer_.append(this.m_buffer_); 
  }
  
  private void updateInternalState(Backup backup) {
    this.m_source_.setIndex(backup.m_offset_);
    this.m_isCodePointHiragana_ = backup.m_isCodePointHiragana_;
    this.m_bufferOffset_ = backup.m_bufferOffset_;
    this.m_FCDLimit_ = backup.m_FCDLimit_;
    this.m_FCDStart_ = backup.m_FCDStart_;
    this.m_buffer_.setLength(0);
    if (this.m_bufferOffset_ >= 0)
      this.m_buffer_.append(backup.m_buffer_); 
  }
  
  private int getCombiningClass(int ch) {
    if ((ch >= 768 && this.m_collator_.isUnsafe((char)ch)) || ch > 65535)
      return m_nfcImpl_.getCC(m_nfcImpl_.getNorm16(ch)); 
    return 0;
  }
  
  private void normalize() {
    if (this.m_unnormalized_ == null) {
      this.m_unnormalized_ = new StringBuilder();
      this.m_n2Buffer_ = new Normalizer2Impl.ReorderingBuffer(m_nfcImpl_, this.m_buffer_, 10);
    } else {
      this.m_unnormalized_.setLength(0);
      this.m_n2Buffer_.remove();
    } 
    int size = this.m_FCDLimit_ - this.m_FCDStart_;
    this.m_source_.setIndex(this.m_FCDStart_);
    for (int i = 0; i < size; i++)
      this.m_unnormalized_.append((char)this.m_source_.next()); 
    m_nfcImpl_.decomposeShort(this.m_unnormalized_, 0, size, this.m_n2Buffer_);
  }
  
  private boolean FCDCheck(int ch, int offset) {
    int fcd;
    boolean result = true;
    this.m_FCDStart_ = offset - 1;
    this.m_source_.setIndex(offset);
    if (ch < 384) {
      fcd = m_nfcImpl_.getFCD16FromBelow180(ch);
    } else if (m_nfcImpl_.singleLeadMightHaveNonZeroFCD16(ch)) {
      if (Character.isHighSurrogate((char)ch)) {
        int c2 = this.m_source_.next();
        if (c2 < 0) {
          fcd = 0;
        } else if (Character.isLowSurrogate((char)c2)) {
          fcd = m_nfcImpl_.getFCD16FromNormData(Character.toCodePoint((char)ch, (char)c2));
        } else {
          this.m_source_.moveIndex(-1);
          fcd = 0;
        } 
      } else {
        fcd = m_nfcImpl_.getFCD16FromNormData(ch);
      } 
    } else {
      fcd = 0;
    } 
    int prevTrailCC = fcd & 0xFF;
    if (prevTrailCC == 0) {
      offset = this.m_source_.getIndex();
    } else {
      while (true) {
        ch = this.m_source_.nextCodePoint();
        if (ch < 0) {
          offset = this.m_source_.getIndex();
          break;
        } 
        fcd = m_nfcImpl_.getFCD16(ch);
        int leadCC = fcd >> 8;
        if (leadCC == 0) {
          offset = this.m_source_.getIndex() - Character.charCount(ch);
          break;
        } 
        if (leadCC < prevTrailCC)
          result = false; 
        prevTrailCC = fcd & 0xFF;
      } 
    } 
    this.m_FCDLimit_ = offset;
    this.m_source_.setIndex(this.m_FCDStart_ + 1);
    return result;
  }
  
  private int nextChar() {
    int result;
    if (this.m_bufferOffset_ < 0) {
      result = this.m_source_.next();
    } else {
      if (this.m_bufferOffset_ >= this.m_buffer_.length()) {
        this.m_source_.setIndex(this.m_FCDLimit_);
        this.m_bufferOffset_ = -1;
        this.m_buffer_.setLength(0);
        return nextChar();
      } 
      return this.m_buffer_.charAt(this.m_bufferOffset_++);
    } 
    int startoffset = this.m_source_.getIndex();
    if (result < 192 || this.m_collator_.getDecomposition() == 16 || this.m_bufferOffset_ >= 0 || this.m_FCDLimit_ >= startoffset)
      return result; 
    if (result < 768) {
      int next = this.m_source_.current();
      if (next == -1 || next < 768)
        return result; 
    } 
    if (!FCDCheck(result, startoffset)) {
      normalize();
      result = this.m_buffer_.charAt(0);
      this.m_bufferOffset_ = 1;
    } 
    return result;
  }
  
  private void normalizeBackwards() {
    normalize();
    this.m_bufferOffset_ = this.m_buffer_.length();
  }
  
  private boolean FCDCheckBackwards(int ch, int offset) {
    int fcd;
    this.m_FCDLimit_ = offset + 1;
    this.m_source_.setIndex(offset);
    if (ch < 384) {
      fcd = m_nfcImpl_.getFCD16FromBelow180(ch);
    } else if (!Character.isLowSurrogate((char)ch)) {
      if (m_nfcImpl_.singleLeadMightHaveNonZeroFCD16(ch)) {
        fcd = m_nfcImpl_.getFCD16FromNormData(ch);
      } else {
        fcd = 0;
      } 
    } else {
      int c2 = this.m_source_.previous();
      if (c2 < 0) {
        fcd = 0;
      } else if (Character.isHighSurrogate((char)c2)) {
        ch = Character.toCodePoint((char)c2, (char)ch);
        fcd = m_nfcImpl_.getFCD16FromNormData(ch);
        offset--;
      } else {
        this.m_source_.moveIndex(1);
        fcd = 0;
      } 
    } 
    boolean result = true;
    if (fcd != 0)
      while (true) {
        int leadCC = fcd >> 8;
        if (leadCC == 0 || (ch = this.m_source_.previousCodePoint()) < 0) {
          offset = this.m_source_.getIndex();
          break;
        } 
        fcd = m_nfcImpl_.getFCD16(ch);
        int prevTrailCC = fcd & 0xFF;
        if (leadCC < prevTrailCC) {
          result = false;
          continue;
        } 
        if (fcd == 0) {
          offset = this.m_source_.getIndex() + Character.charCount(ch);
          break;
        } 
      }  
    this.m_FCDStart_ = offset;
    this.m_source_.setIndex(this.m_FCDLimit_);
    return result;
  }
  
  private int previousChar() {
    if (this.m_bufferOffset_ >= 0) {
      this.m_bufferOffset_--;
      if (this.m_bufferOffset_ >= 0)
        return this.m_buffer_.charAt(this.m_bufferOffset_); 
      this.m_buffer_.setLength(0);
      if (this.m_FCDStart_ == 0) {
        this.m_FCDStart_ = -1;
        this.m_source_.setIndex(0);
        return -1;
      } 
      this.m_FCDLimit_ = this.m_FCDStart_;
      this.m_source_.setIndex(this.m_FCDStart_);
      return previousChar();
    } 
    int result = this.m_source_.previous();
    int startoffset = this.m_source_.getIndex();
    if (result < 768 || this.m_collator_.getDecomposition() == 16 || this.m_FCDStart_ <= startoffset || this.m_source_.getIndex() == 0)
      return result; 
    int ch = this.m_source_.previous();
    if (ch < 192) {
      this.m_source_.next();
      return result;
    } 
    if (!FCDCheckBackwards(result, startoffset)) {
      normalizeBackwards();
      this.m_bufferOffset_--;
      result = this.m_buffer_.charAt(this.m_bufferOffset_);
    } else {
      this.m_source_.setIndex(startoffset);
    } 
    return result;
  }
  
  private final boolean isBackwardsStart() {
    return ((this.m_bufferOffset_ < 0 && this.m_source_.getIndex() == 0) || (this.m_bufferOffset_ == 0 && this.m_FCDStart_ <= 0));
  }
  
  private final boolean isEnd() {
    if (this.m_bufferOffset_ >= 0) {
      if (this.m_bufferOffset_ != this.m_buffer_.length())
        return false; 
      return (this.m_FCDLimit_ == this.m_source_.getLength());
    } 
    return (this.m_source_.getLength() == this.m_source_.getIndex());
  }
  
  private final int nextSurrogate(RuleBasedCollator collator, int ce, char trail) {
    if (!UTF16.isTrailSurrogate(trail)) {
      updateInternalState(this.m_utilSpecialBackUp_);
      return -268435456;
    } 
    int result = collator.m_trie_.getTrailValue(ce, trail);
    if (result == -268435456)
      updateInternalState(this.m_utilSpecialBackUp_); 
    return result;
  }
  
  private int getExpansionOffset(RuleBasedCollator collator, int ce) {
    return ((ce & 0xFFFFF0) >> 4) - collator.m_expansionOffset_;
  }
  
  private int getContractionOffset(RuleBasedCollator collator, int ce) {
    return (ce & 0xFFFFFF) - collator.m_contractionOffset_;
  }
  
  private boolean isSpecialPrefixTag(int ce) {
    return (RuleBasedCollator.isSpecial(ce) && RuleBasedCollator.getTag(ce) == 11);
  }
  
  private int nextSpecialPrefix(RuleBasedCollator collator, int ce, Backup entrybackup) {
    backupInternalState(this.m_utilSpecialBackUp_);
    updateInternalState(entrybackup);
    previousChar();
    do {
      int entryoffset = getContractionOffset(collator, ce);
      int offset = entryoffset;
      if (isBackwardsStart()) {
        ce = collator.m_contractionCE_[offset];
        break;
      } 
      char previous = (char)previousChar();
      while (previous > collator.m_contractionIndex_[offset])
        offset++; 
      if (previous == collator.m_contractionIndex_[offset]) {
        ce = collator.m_contractionCE_[offset];
      } else {
        ce = collator.m_contractionCE_[entryoffset];
      } 
    } while (isSpecialPrefixTag(ce));
    if (ce != -268435456) {
      updateInternalState(this.m_utilSpecialBackUp_);
    } else {
      updateInternalState(entrybackup);
    } 
    return ce;
  }
  
  private boolean isContractionTag(int ce) {
    return (RuleBasedCollator.isSpecial(ce) && RuleBasedCollator.getTag(ce) == 2);
  }
  
  private void setDiscontiguous(StringBuilder skipped) {
    if (this.m_bufferOffset_ >= 0) {
      this.m_buffer_.replace(0, this.m_bufferOffset_, skipped.toString());
    } else {
      this.m_FCDLimit_ = this.m_source_.getIndex();
      this.m_buffer_.setLength(0);
      this.m_buffer_.append(skipped.toString());
    } 
    this.m_bufferOffset_ = 0;
  }
  
  private int currentChar() {
    if (this.m_bufferOffset_ < 0) {
      this.m_source_.previousCodePoint();
      return this.m_source_.nextCodePoint();
    } 
    return UTF16.charAt(this.m_buffer_, this.m_bufferOffset_ - 1);
  }
  
  private int nextDiscontiguous(RuleBasedCollator collator, int entryoffset) {
    int offset = entryoffset;
    boolean multicontraction = false;
    if (this.m_utilSkippedBuffer_ == null) {
      this.m_utilSkippedBuffer_ = new StringBuilder();
    } else {
      this.m_utilSkippedBuffer_.setLength(0);
    } 
    int ch = currentChar();
    this.m_utilSkippedBuffer_.appendCodePoint(ch);
    int prevCC = 0;
    int cc = getCombiningClass(ch);
    if (this.m_utilSpecialDiscontiguousBackUp_ == null)
      this.m_utilSpecialDiscontiguousBackUp_ = new Backup(); 
    backupInternalState(this.m_utilSpecialDiscontiguousBackUp_);
    boolean prevWasLead = false;
    while (true) {
      int ch_int = nextChar();
      char nextch = (char)ch_int;
      if (UTF16.isSurrogate(nextch)) {
        if (prevWasLead) {
          prevWasLead = false;
        } else {
          prevCC = cc;
          cc = 0;
          prevWasLead = false;
          if (Character.isHighSurrogate(nextch)) {
            int trail = nextChar();
            if (Character.isLowSurrogate((char)trail)) {
              cc = getCombiningClass(Character.toCodePoint(nextch, (char)trail));
              prevWasLead = true;
            } 
            if (trail >= 0)
              previousChar(); 
          } 
        } 
      } else {
        prevCC = cc;
        cc = getCombiningClass(ch_int);
        prevWasLead = false;
      } 
      if (ch_int < 0 || cc == 0) {
        if (multicontraction) {
          if (ch_int >= 0)
            previousChar(); 
          setDiscontiguous(this.m_utilSkippedBuffer_);
          return collator.m_contractionCE_[offset];
        } 
        break;
      } 
      offset++;
      while (offset < collator.m_contractionIndex_.length && nextch > collator.m_contractionIndex_[offset])
        offset++; 
      int ce = -268435456;
      if (offset >= collator.m_contractionIndex_.length)
        break; 
      if (nextch != collator.m_contractionIndex_[offset] || cc == prevCC) {
        if (this.m_utilSkippedBuffer_.length() != 1 || (this.m_utilSkippedBuffer_.charAt(0) != nextch && this.m_bufferOffset_ < 0))
          this.m_utilSkippedBuffer_.append(nextch); 
        offset = entryoffset;
        continue;
      } 
      ce = collator.m_contractionCE_[offset];
      if (ce == -268435456)
        break; 
      if (isContractionTag(ce)) {
        offset = getContractionOffset(collator, ce);
        if (collator.m_contractionCE_[offset] != -268435456) {
          multicontraction = true;
          backupInternalState(this.m_utilSpecialDiscontiguousBackUp_);
        } 
        continue;
      } 
      setDiscontiguous(this.m_utilSkippedBuffer_);
      return ce;
    } 
    updateInternalState(this.m_utilSpecialDiscontiguousBackUp_);
    previousChar();
    return collator.m_contractionCE_[entryoffset];
  }
  
  private int nextContraction(RuleBasedCollator collator, int ce) {
    backupInternalState(this.m_utilSpecialBackUp_);
    int entryce = collator.m_contractionCE_[getContractionOffset(collator, ce)];
    while (true) {
      int entryoffset = getContractionOffset(collator, ce);
      int offset = entryoffset;
      if (isEnd()) {
        ce = collator.m_contractionCE_[offset];
        if (ce == -268435456) {
          ce = entryce;
          updateInternalState(this.m_utilSpecialBackUp_);
        } 
        break;
      } 
      int maxCC = collator.m_contractionIndex_[offset] & 0xFF;
      byte allSame = (byte)(collator.m_contractionIndex_[offset] >> 8);
      char ch = (char)nextChar();
      offset++;
      while (ch > collator.m_contractionIndex_[offset])
        offset++; 
      if (ch == collator.m_contractionIndex_[offset]) {
        ce = collator.m_contractionCE_[offset];
      } else {
        int miss = ch;
        if (UTF16.isLeadSurrogate(ch) && !isEnd()) {
          char surrNextChar = (char)nextChar();
          if (UTF16.isTrailSurrogate(surrNextChar)) {
            miss = UCharacterProperty.getRawSupplementary(ch, surrNextChar);
          } else {
            previousChar();
          } 
        } 
        int sCC;
        if (maxCC == 0 || (sCC = getCombiningClass(miss)) == 0 || sCC > maxCC || (allSame != 0 && sCC == maxCC) || isEnd()) {
          previousChar();
          if (miss > 65535)
            previousChar(); 
          ce = collator.m_contractionCE_[entryoffset];
        } else {
          int ch_int = nextChar();
          if (ch_int != -1)
            previousChar(); 
          char nextch = (char)ch_int;
          if (getCombiningClass(nextch) == 0) {
            previousChar();
            if (miss > 65535)
              previousChar(); 
            ce = collator.m_contractionCE_[entryoffset];
          } else {
            ce = nextDiscontiguous(collator, entryoffset);
          } 
        } 
      } 
      if (ce == -268435456) {
        updateInternalState(this.m_utilSpecialBackUp_);
        ce = entryce;
        break;
      } 
      if (!isContractionTag(ce))
        break; 
      if (collator.m_contractionCE_[entryoffset] != -268435456) {
        entryce = collator.m_contractionCE_[entryoffset];
        backupInternalState(this.m_utilSpecialBackUp_);
        if (this.m_utilSpecialBackUp_.m_bufferOffset_ >= 0) {
          this.m_utilSpecialBackUp_.m_bufferOffset_--;
          continue;
        } 
        this.m_utilSpecialBackUp_.m_offset_--;
      } 
    } 
    return ce;
  }
  
  private int nextLongPrimary(int ce) {
    this.m_CEBuffer_[1] = (ce & 0xFF) << 24 | 0xC0;
    this.m_CEBufferOffset_ = 1;
    this.m_CEBufferSize_ = 2;
    this.m_CEBuffer_[0] = (ce & 0xFFFF00) << 8 | 0x500 | 0x5;
    return this.m_CEBuffer_[0];
  }
  
  private int getExpansionCount(int ce) {
    return ce & 0xF;
  }
  
  private int nextExpansion(RuleBasedCollator collator, int ce) {
    int offset = getExpansionOffset(collator, ce);
    this.m_CEBufferSize_ = getExpansionCount(ce);
    this.m_CEBufferOffset_ = 1;
    this.m_CEBuffer_[0] = collator.m_expansion_[offset];
    if (this.m_CEBufferSize_ != 0) {
      for (int i = 1; i < this.m_CEBufferSize_; i++)
        this.m_CEBuffer_[i] = collator.m_expansion_[offset + i]; 
    } else {
      this.m_CEBufferSize_ = 1;
      while (collator.m_expansion_[offset] != 0)
        this.m_CEBuffer_[this.m_CEBufferSize_++] = collator.m_expansion_[++offset]; 
    } 
    if (this.m_CEBufferSize_ == 1) {
      this.m_CEBufferSize_ = 0;
      this.m_CEBufferOffset_ = 0;
    } 
    return this.m_CEBuffer_[0];
  }
  
  private int nextDigit(RuleBasedCollator collator, int ce, int cp) {
    if (this.m_collator_.m_isNumericCollation_) {
      int collateVal = 0;
      int trailingZeroIndex = 0;
      boolean nonZeroValReached = false;
      this.m_utilStringBuffer_.setLength(3);
      int digVal = UCharacter.digit(cp);
      int digIndx = 1;
      while (true) {
        if (digIndx >= this.m_utilStringBuffer_.length() - 2 << 1)
          this.m_utilStringBuffer_.setLength(this.m_utilStringBuffer_.length() << 1); 
        if (digVal != 0 || nonZeroValReached) {
          if (digVal != 0 && !nonZeroValReached)
            nonZeroValReached = true; 
          if (digIndx % 2 != 0) {
            collateVal += digVal;
            if (collateVal == 0 && trailingZeroIndex == 0) {
              trailingZeroIndex = (digIndx - 1 >>> 1) + 2;
            } else if (trailingZeroIndex != 0) {
              trailingZeroIndex = 0;
            } 
            this.m_utilStringBuffer_.setCharAt((digIndx - 1 >>> 1) + 2, (char)((collateVal << 1) + 6));
            collateVal = 0;
          } else {
            collateVal = digVal * 10;
            if (collateVal == 0) {
              if (trailingZeroIndex != 0)
                trailingZeroIndex = (digIndx >>> 1) + 2; 
            } else {
              trailingZeroIndex = 0;
            } 
            this.m_utilStringBuffer_.setCharAt((digIndx >>> 1) + 2, (char)((collateVal << 1) + 6));
          } 
          digIndx++;
        } 
        if (!isEnd()) {
          backupInternalState(this.m_utilSpecialBackUp_);
          int char32 = nextChar();
          char ch = (char)char32;
          if (UTF16.isLeadSurrogate(ch) && 
            !isEnd()) {
            char trail = (char)nextChar();
            if (UTF16.isTrailSurrogate(trail)) {
              char32 = UCharacterProperty.getRawSupplementary(ch, trail);
            } else {
              goBackOne();
            } 
          } 
          digVal = UCharacter.digit(char32);
          if (digVal == -1) {
            updateInternalState(this.m_utilSpecialBackUp_);
            break;
          } 
          continue;
        } 
        break;
      } 
      if (!nonZeroValReached) {
        digIndx = 2;
        this.m_utilStringBuffer_.setCharAt(2, '\006');
      } 
      int endIndex = (trailingZeroIndex != 0) ? trailingZeroIndex : ((digIndx >>> 1) + 2);
      if (digIndx % 2 != 0) {
        for (int j = 2; j < endIndex; j++)
          this.m_utilStringBuffer_.setCharAt(j, (char)(((this.m_utilStringBuffer_.charAt(j) - 6 >>> 1) % 10 * 10 + (this.m_utilStringBuffer_.charAt(j + 1) - 6 >>> 1) / 10 << 1) + 6)); 
        digIndx--;
      } 
      this.m_utilStringBuffer_.setCharAt(endIndex - 1, (char)(this.m_utilStringBuffer_.charAt(endIndex - 1) - 1));
      this.m_utilStringBuffer_.setCharAt(0, '\022');
      this.m_utilStringBuffer_.setCharAt(1, (char)(128 + (digIndx >>> 1 & 0x7F)));
      ce = (this.m_utilStringBuffer_.charAt(0) << 8 | this.m_utilStringBuffer_.charAt(1)) << 16 | 0x500 | 0x5;
      int i = 2;
      this.m_CEBuffer_[0] = ce;
      this.m_CEBufferSize_ = 1;
      this.m_CEBufferOffset_ = 1;
      while (i < endIndex) {
        int primWeight = this.m_utilStringBuffer_.charAt(i++) << 8;
        if (i < endIndex)
          primWeight |= this.m_utilStringBuffer_.charAt(i++); 
        this.m_CEBuffer_[this.m_CEBufferSize_++] = primWeight << 16 | 0xC0;
      } 
      return ce;
    } 
    return collator.m_expansion_[getExpansionOffset(collator, ce)];
  }
  
  private int nextImplicit(int codepoint) {
    int result = RuleBasedCollator.impCEGen_.getImplicitFromCodePoint(codepoint);
    this.m_CEBuffer_[0] = result & 0xFFFF0000 | 0x505;
    this.m_CEBuffer_[1] = (result & 0xFFFF) << 16 | 0xC0;
    this.m_CEBufferOffset_ = 1;
    this.m_CEBufferSize_ = 2;
    return this.m_CEBuffer_[0];
  }
  
  private int nextSurrogate(char ch) {
    int ch_int = nextChar();
    char nextch = (char)ch_int;
    if (ch_int != 65535 && UTF16.isTrailSurrogate(nextch)) {
      int codepoint = UCharacterProperty.getRawSupplementary(ch, nextch);
      return nextImplicit(codepoint);
    } 
    if (nextch != Character.MAX_VALUE)
      previousChar(); 
    return -268435456;
  }
  
  private int nextHangul(RuleBasedCollator collator, char ch) {
    char L = (char)(ch - 44032);
    char T = (char)(L % 28);
    L = (char)(L / 28);
    char V = (char)(L % 21);
    L = (char)(L / 21);
    L = (char)(L + 4352);
    V = (char)(V + 4449);
    T = (char)(T + 4519);
    this.m_CEBufferSize_ = 0;
    if (!this.m_collator_.m_isJamoSpecial_) {
      this.m_CEBuffer_[this.m_CEBufferSize_++] = collator.m_trie_.getLeadValue(L);
      this.m_CEBuffer_[this.m_CEBufferSize_++] = collator.m_trie_.getLeadValue(V);
      if (T != 'ᆧ')
        this.m_CEBuffer_[this.m_CEBufferSize_++] = collator.m_trie_.getLeadValue(T); 
      this.m_CEBufferOffset_ = 1;
      return this.m_CEBuffer_[0];
    } 
    this.m_buffer_.append(L);
    this.m_buffer_.append(V);
    if (T != 'ᆧ')
      this.m_buffer_.append(T); 
    this.m_bufferOffset_ = 0;
    this.m_FCDLimit_ = this.m_source_.getIndex();
    this.m_FCDStart_ = this.m_FCDLimit_ - 1;
    return 0;
  }
  
  private int nextSpecial(RuleBasedCollator collator, int ce, char ch) {
    int codepoint = ch;
    Backup entrybackup = this.m_utilSpecialEntryBackUp_;
    if (entrybackup != null) {
      this.m_utilSpecialEntryBackUp_ = null;
    } else {
      entrybackup = new Backup();
    } 
    backupInternalState(entrybackup);
    try {
      do {
        int i;
        char trail;
        int j;
        switch (RuleBasedCollator.getTag(ce)) {
          case 0:
            i = ce;
            return i;
          case 5:
            if (isEnd()) {
              i = -268435456;
              return i;
            } 
            backupInternalState(this.m_utilSpecialBackUp_);
            trail = (char)nextChar();
            ce = nextSurrogate(collator, ce, trail);
            codepoint = UCharacterProperty.getRawSupplementary(ch, trail);
            break;
          case 11:
            ce = nextSpecialPrefix(collator, ce, entrybackup);
            break;
          case 2:
            ce = nextContraction(collator, ce);
            break;
          case 12:
            j = nextLongPrimary(ce);
            return j;
          case 1:
            j = nextExpansion(collator, ce);
            return j;
          case 13:
            ce = nextDigit(collator, ce, codepoint);
            break;
          case 9:
            j = nextImplicit(codepoint);
            return j;
          case 10:
            j = nextImplicit(codepoint);
            return j;
          case 8:
            j = -268435456;
            return j;
          case 7:
            j = nextSurrogate(ch);
            return j;
          case 6:
            j = nextHangul(collator, ch);
            return j;
          case 4:
            j = -268435456;
            return j;
          default:
            ce = 0;
            break;
        } 
      } while (RuleBasedCollator.isSpecial(ce));
    } finally {
      this.m_utilSpecialEntryBackUp_ = entrybackup;
    } 
    return ce;
  }
  
  private int previousSpecialPrefix(RuleBasedCollator collator, int ce) {
    backupInternalState(this.m_utilSpecialBackUp_);
    while (true) {
      int offset = getContractionOffset(collator, ce);
      int entryoffset = offset;
      if (isBackwardsStart()) {
        ce = collator.m_contractionCE_[offset];
        break;
      } 
      char prevch = (char)previousChar();
      while (prevch > collator.m_contractionIndex_[offset])
        offset++; 
      if (prevch == collator.m_contractionIndex_[offset]) {
        ce = collator.m_contractionCE_[offset];
      } else {
        int isZeroCE = collator.m_trie_.getLeadValue(prevch);
        if (isZeroCE == 0)
          continue; 
        if (UTF16.isTrailSurrogate(prevch) || UTF16.isLeadSurrogate(prevch))
          if (!isBackwardsStart()) {
            char lead = (char)previousChar();
            if (UTF16.isLeadSurrogate(lead)) {
              isZeroCE = collator.m_trie_.getLeadValue(lead);
              if (RuleBasedCollator.getTag(isZeroCE) == 5) {
                int finalCE = collator.m_trie_.getTrailValue(isZeroCE, prevch);
                if (finalCE == 0)
                  continue; 
              } 
            } else {
              nextChar();
              continue;
            } 
            nextChar();
          } else {
            continue;
          }  
        ce = collator.m_contractionCE_[entryoffset];
      } 
      if (!isSpecialPrefixTag(ce))
        break; 
    } 
    updateInternalState(this.m_utilSpecialBackUp_);
    return ce;
  }
  
  private int previousContraction(RuleBasedCollator collator, int ce, char ch) {
    this.m_utilStringBuffer_.setLength(0);
    char prevch = (char)previousChar();
    boolean atStart = false;
    while (collator.isUnsafe(ch)) {
      this.m_utilStringBuffer_.insert(0, ch);
      ch = prevch;
      if (isBackwardsStart()) {
        atStart = true;
        break;
      } 
      prevch = (char)previousChar();
    } 
    if (!atStart)
      nextChar(); 
    this.m_utilStringBuffer_.insert(0, ch);
    int originaldecomp = collator.getDecomposition();
    collator.setDecomposition(16);
    if (this.m_utilColEIter_ == null) {
      this.m_utilColEIter_ = new CollationElementIterator(this.m_utilStringBuffer_.toString(), collator);
    } else {
      this.m_utilColEIter_.m_collator_ = collator;
      this.m_utilColEIter_.setText(this.m_utilStringBuffer_.toString());
    } 
    ce = this.m_utilColEIter_.next();
    this.m_CEBufferSize_ = 0;
    while (ce != -1) {
      if (this.m_CEBufferSize_ == this.m_CEBuffer_.length)
        try {
          int[] tempbuffer = new int[this.m_CEBuffer_.length + 50];
          System.arraycopy(this.m_CEBuffer_, 0, tempbuffer, 0, this.m_CEBuffer_.length);
          this.m_CEBuffer_ = tempbuffer;
        } catch (MissingResourceException e) {
          throw e;
        } catch (Exception e) {
          if (DEBUG)
            e.printStackTrace(); 
          return -1;
        }  
      this.m_CEBuffer_[this.m_CEBufferSize_++] = ce;
      ce = this.m_utilColEIter_.next();
    } 
    collator.setDecomposition(originaldecomp);
    this.m_CEBufferOffset_ = this.m_CEBufferSize_ - 1;
    return this.m_CEBuffer_[this.m_CEBufferOffset_];
  }
  
  private int previousLongPrimary(int ce) {
    this.m_CEBufferSize_ = 0;
    this.m_CEBuffer_[this.m_CEBufferSize_++] = (ce & 0xFFFF00) << 8 | 0x500 | 0x5;
    this.m_CEBuffer_[this.m_CEBufferSize_++] = (ce & 0xFF) << 24 | 0xC0;
    this.m_CEBufferOffset_ = this.m_CEBufferSize_ - 1;
    return this.m_CEBuffer_[this.m_CEBufferOffset_];
  }
  
  private int previousExpansion(RuleBasedCollator collator, int ce) {
    int offset = getExpansionOffset(collator, ce);
    this.m_CEBufferSize_ = getExpansionCount(ce);
    if (this.m_CEBufferSize_ != 0) {
      for (int i = 0; i < this.m_CEBufferSize_; i++)
        this.m_CEBuffer_[i] = collator.m_expansion_[offset + i]; 
    } else {
      while (collator.m_expansion_[offset + this.m_CEBufferSize_] != 0) {
        this.m_CEBuffer_[this.m_CEBufferSize_] = collator.m_expansion_[offset + this.m_CEBufferSize_];
        this.m_CEBufferSize_++;
      } 
    } 
    this.m_CEBufferOffset_ = this.m_CEBufferSize_ - 1;
    return this.m_CEBuffer_[this.m_CEBufferOffset_];
  }
  
  private int previousDigit(RuleBasedCollator collator, int ce, char ch) {
    if (this.m_collator_.m_isNumericCollation_) {
      int leadingZeroIndex = 0;
      int collateVal = 0;
      boolean nonZeroValReached = false;
      this.m_utilStringBuffer_.setLength(3);
      int char32 = ch;
      if (UTF16.isTrailSurrogate(ch) && 
        !isBackwardsStart()) {
        char lead = (char)previousChar();
        if (UTF16.isLeadSurrogate(lead)) {
          char32 = UCharacterProperty.getRawSupplementary(lead, ch);
        } else {
          goForwardOne();
        } 
      } 
      int digVal = UCharacter.digit(char32);
      int digIndx = 0;
      while (true) {
        if (digIndx >= this.m_utilStringBuffer_.length() - 2 << 1)
          this.m_utilStringBuffer_.setLength(this.m_utilStringBuffer_.length() << 1); 
        if (digVal != 0 || nonZeroValReached) {
          if (digVal != 0 && !nonZeroValReached)
            nonZeroValReached = true; 
          if (digIndx % 2 != 0) {
            collateVal += digVal * 10;
            if (collateVal == 0 && leadingZeroIndex == 0) {
              leadingZeroIndex = (digIndx - 1 >>> 1) + 2;
            } else if (leadingZeroIndex != 0) {
              leadingZeroIndex = 0;
            } 
            this.m_utilStringBuffer_.setCharAt((digIndx - 1 >>> 1) + 2, (char)((collateVal << 1) + 6));
            collateVal = 0;
          } else {
            collateVal = digVal;
          } 
        } 
        digIndx++;
        if (!isBackwardsStart()) {
          backupInternalState(this.m_utilSpecialBackUp_);
          char32 = previousChar();
          if (UTF16.isTrailSurrogate(ch) && 
            !isBackwardsStart()) {
            char lead = (char)previousChar();
            if (UTF16.isLeadSurrogate(lead)) {
              char32 = UCharacterProperty.getRawSupplementary(lead, ch);
            } else {
              updateInternalState(this.m_utilSpecialBackUp_);
            } 
          } 
          digVal = UCharacter.digit(char32);
          if (digVal == -1) {
            updateInternalState(this.m_utilSpecialBackUp_);
            break;
          } 
          continue;
        } 
        break;
      } 
      if (!nonZeroValReached) {
        digIndx = 2;
        this.m_utilStringBuffer_.setCharAt(2, '\006');
      } 
      if (digIndx % 2 != 0)
        if (collateVal == 0 && leadingZeroIndex == 0) {
          leadingZeroIndex = (digIndx - 1 >>> 1) + 2;
        } else {
          this.m_utilStringBuffer_.setCharAt((digIndx >>> 1) + 2, (char)((collateVal << 1) + 6));
          digIndx++;
        }  
      int endIndex = (leadingZeroIndex != 0) ? leadingZeroIndex : ((digIndx >>> 1) + 2);
      digIndx = (endIndex - 2 << 1) + 1;
      this.m_utilStringBuffer_.setCharAt(2, (char)(this.m_utilStringBuffer_.charAt(2) - 1));
      this.m_utilStringBuffer_.setCharAt(0, '\022');
      this.m_utilStringBuffer_.setCharAt(1, (char)(128 + (digIndx >>> 1 & 0x7F)));
      this.m_CEBufferSize_ = 0;
      this.m_CEBuffer_[this.m_CEBufferSize_++] = (this.m_utilStringBuffer_.charAt(0) << 8 | this.m_utilStringBuffer_.charAt(1)) << 16 | 0x500 | 0x5;
      int i = endIndex - 1;
      while (i >= 2) {
        int primWeight = this.m_utilStringBuffer_.charAt(i--) << 8;
        if (i >= 2)
          primWeight |= this.m_utilStringBuffer_.charAt(i--); 
        this.m_CEBuffer_[this.m_CEBufferSize_++] = primWeight << 16 | 0xC0;
      } 
      this.m_CEBufferOffset_ = this.m_CEBufferSize_ - 1;
      return this.m_CEBuffer_[this.m_CEBufferOffset_];
    } 
    return collator.m_expansion_[getExpansionOffset(collator, ce)];
  }
  
  private int previousHangul(RuleBasedCollator collator, char ch) {
    char L = (char)(ch - 44032);
    char T = (char)(L % 28);
    L = (char)(L / 28);
    char V = (char)(L % 21);
    L = (char)(L / 21);
    L = (char)(L + 4352);
    V = (char)(V + 4449);
    T = (char)(T + 4519);
    this.m_CEBufferSize_ = 0;
    if (!this.m_collator_.m_isJamoSpecial_) {
      this.m_CEBuffer_[this.m_CEBufferSize_++] = collator.m_trie_.getLeadValue(L);
      this.m_CEBuffer_[this.m_CEBufferSize_++] = collator.m_trie_.getLeadValue(V);
      if (T != 'ᆧ')
        this.m_CEBuffer_[this.m_CEBufferSize_++] = collator.m_trie_.getLeadValue(T); 
      this.m_CEBufferOffset_ = this.m_CEBufferSize_ - 1;
      return this.m_CEBuffer_[this.m_CEBufferOffset_];
    } 
    this.m_buffer_.append(L);
    this.m_buffer_.append(V);
    if (T != 'ᆧ')
      this.m_buffer_.append(T); 
    this.m_bufferOffset_ = this.m_buffer_.length();
    this.m_FCDStart_ = this.m_source_.getIndex();
    this.m_FCDLimit_ = this.m_FCDStart_ + 1;
    return 0;
  }
  
  private int previousImplicit(int codepoint) {
    int result = RuleBasedCollator.impCEGen_.getImplicitFromCodePoint(codepoint);
    this.m_CEBufferSize_ = 2;
    this.m_CEBufferOffset_ = 1;
    this.m_CEBuffer_[0] = result & 0xFFFF0000 | 0x505;
    this.m_CEBuffer_[1] = (result & 0xFFFF) << 16 | 0xC0;
    return this.m_CEBuffer_[1];
  }
  
  private int previousSurrogate(char ch) {
    if (isBackwardsStart())
      return -268435456; 
    char prevch = (char)previousChar();
    if (UTF16.isLeadSurrogate(prevch))
      return previousImplicit(UCharacterProperty.getRawSupplementary(prevch, ch)); 
    if (prevch != Character.MAX_VALUE)
      nextChar(); 
    return -268435456;
  }
  
  private int previousSpecial(RuleBasedCollator collator, int ce, char ch) {
    do {
      switch (RuleBasedCollator.getTag(ce)) {
        case 0:
          return ce;
        case 5:
          return -268435456;
        case 11:
          ce = previousSpecialPrefix(collator, ce);
          break;
        case 2:
          if (isBackwardsStart()) {
            ce = collator.m_contractionCE_[getContractionOffset(collator, ce)];
            break;
          } 
          return previousContraction(collator, ce, ch);
        case 12:
          return previousLongPrimary(ce);
        case 1:
          return previousExpansion(collator, ce);
        case 13:
          ce = previousDigit(collator, ce, ch);
          break;
        case 6:
          return previousHangul(collator, ch);
        case 7:
          return -268435456;
        case 8:
          return previousSurrogate(ch);
        case 9:
          return previousImplicit(ch);
        case 10:
          return previousImplicit(ch);
        case 4:
          return -268435456;
        default:
          ce = 0;
          break;
      } 
    } while (RuleBasedCollator.isSpecial(ce));
    return ce;
  }
  
  private void goBackOne() {
    if (this.m_bufferOffset_ >= 0) {
      this.m_bufferOffset_--;
    } else {
      this.m_source_.setIndex(this.m_source_.getIndex() - 1);
    } 
  }
  
  private void goForwardOne() {
    if (this.m_bufferOffset_ < 0) {
      this.m_source_.setIndex(this.m_source_.getIndex() + 1);
    } else {
      this.m_bufferOffset_++;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\CollationElementIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */