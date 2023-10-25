package com.ibm.icu.text;

import com.ibm.icu.impl.BOCU;
import com.ibm.icu.impl.ICUDebug;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.ImplicitCEGenerator;
import com.ibm.icu.impl.IntTrie;
import com.ibm.icu.impl.StringUCharacterIterator;
import com.ibm.icu.impl.Trie;
import com.ibm.icu.impl.TrieIterator;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.Output;
import com.ibm.icu.util.RangeValueIterator;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.VersionInfo;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.CharacterIterator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class RuleBasedCollator extends Collator {
  static final byte BYTE_FIRST_TAILORED_ = 4;
  
  static final byte BYTE_COMMON_ = 5;
  
  static final int COMMON_TOP_2_ = 134;
  
  static final int COMMON_BOTTOM_2_ = 5;
  
  static final int COMMON_BOTTOM_3 = 5;
  
  static final int CE_CASE_BIT_MASK_ = 192;
  
  static final int CE_TAG_SHIFT_ = 24;
  
  static final int CE_TAG_MASK_ = 251658240;
  
  static final int CE_SPECIAL_FLAG_ = -268435456;
  
  static final int CE_SURROGATE_TAG_ = 5;
  
  static final int CE_PRIMARY_MASK_ = -65536;
  
  static final int CE_SECONDARY_MASK_ = 65280;
  
  static final int CE_TERTIARY_MASK_ = 255;
  
  static final int CE_PRIMARY_SHIFT_ = 16;
  
  static final int CE_SECONDARY_SHIFT_ = 8;
  
  static final int CE_CONTINUATION_MARKER_ = 192;
  
  int m_expansionOffset_;
  
  int m_contractionOffset_;
  
  boolean m_isJamoSpecial_;
  
  int m_defaultVariableTopValue_;
  
  boolean m_defaultIsFrenchCollation_;
  
  boolean m_defaultIsAlternateHandlingShifted_;
  
  int m_defaultCaseFirst_;
  
  boolean m_defaultIsCaseLevel_;
  
  int m_defaultDecomposition_;
  
  int m_defaultStrength_;
  
  boolean m_defaultIsHiragana4_;
  
  boolean m_defaultIsNumericCollation_;
  
  int[] m_defaultReorderCodes_;
  
  int m_variableTopValue_;
  
  boolean m_isHiragana4_;
  
  int m_caseFirst_;
  
  boolean m_isNumericCollation_;
  
  int[] m_reorderCodes_;
  
  int[] m_expansion_;
  
  char[] m_contractionIndex_;
  
  int[] m_contractionCE_;
  
  IntTrie m_trie_;
  
  int[] m_expansionEndCE_;
  
  byte[] m_expansionEndCEMaxSize_;
  
  byte[] m_unsafe_;
  
  byte[] m_contractionEnd_;
  
  String m_rules_;
  
  char m_minUnsafe_;
  
  char m_minContractionEnd_;
  
  VersionInfo m_version_;
  
  VersionInfo m_UCA_version_;
  
  VersionInfo m_UCD_version_;
  
  int m_leadByteToScripts;
  
  int m_scriptToLeadBytes;
  
  static final RuleBasedCollator UCA_;
  
  static final UCAConstants UCA_CONSTANTS_;
  
  static LeadByteConstants LEADBYTE_CONSTANTS_;
  
  static final char[] UCA_CONTRACTIONS_;
  
  static final int MAX_UCA_CONTRACTION_LENGTH;
  
  private static boolean UCA_INIT_COMPLETE;
  
  static final ImplicitCEGenerator impCEGen_;
  
  static final byte SORT_LEVEL_TERMINATOR_ = 1;
  
  static final int maxRegularPrimary = 122;
  
  static final int minImplicitPrimary = 224;
  
  static final int maxImplicitPrimary = 228;
  
  private static final int DEFAULT_MIN_HEURISTIC_ = 768;
  
  private static final char HEURISTIC_SIZE_ = 'Р';
  
  private static final char HEURISTIC_OVERFLOW_MASK_ = '῿';
  
  private static final int HEURISTIC_SHIFT_ = 3;
  
  private static final char HEURISTIC_OVERFLOW_OFFSET_ = 'Ā';
  
  private static final char HEURISTIC_MASK_ = '\007';
  
  private int m_caseSwitch_;
  
  private int m_common3_;
  
  private int m_mask3_;
  
  private int m_addition3_;
  
  private int m_top3_;
  
  private int m_bottom3_;
  
  private int m_topCount3_;
  
  private int m_bottomCount3_;
  
  private byte[] m_leadBytePermutationTable_;
  
  private static final int CASE_SWITCH_ = 192;
  
  private static final int NO_CASE_SWITCH_ = 0;
  
  private static final int CE_REMOVE_CASE_ = 63;
  
  private static final int CE_KEEP_CASE_ = 255;
  
  private static final int CE_CASE_MASK_3_ = 255;
  
  private static final double PROPORTION_2_ = 0.5D;
  
  private static final double PROPORTION_3_ = 0.667D;
  
  private static final byte BYTE_SHIFT_PREFIX_ = 3;
  
  static final byte BYTE_UNSHIFTED_MIN_ = 3;
  
  static final byte CODAN_PLACEHOLDER = 18;
  
  private static final byte BYTE_FIRST_NON_LATIN_PRIMARY_ = 91;
  
  private static final byte BYTE_UNSHIFTED_MAX_ = -1;
  
  private static final int TOTAL_2_ = 128;
  
  private static final int FLAG_BIT_MASK_CASE_SWITCH_OFF_ = 128;
  
  private static final int FLAG_BIT_MASK_CASE_SWITCH_ON_ = 64;
  
  private static final int COMMON_TOP_CASE_SWITCH_OFF_3_ = 133;
  
  private static final int COMMON_TOP_CASE_SWITCH_LOWER_3_ = 69;
  
  private static final int COMMON_TOP_CASE_SWITCH_UPPER_3_ = 197;
  
  private static final int COMMON_BOTTOM_3_ = 5;
  
  private static final int COMMON_BOTTOM_CASE_SWITCH_UPPER_3_ = 134;
  
  private static final int COMMON_BOTTOM_CASE_SWITCH_LOWER_3_ = 5;
  
  private static final int TOP_COUNT_2_ = 64;
  
  private static final int BOTTOM_COUNT_2_ = 64;
  
  private static final int COMMON_2_ = 5;
  
  private static final int COMMON_UPPER_FIRST_3_ = 197;
  
  private static final int COMMON_NORMAL_3_ = 5;
  
  private boolean m_isSimple3_;
  
  private boolean m_isFrenchCollation_;
  
  private boolean m_isAlternateHandlingShifted_;
  
  private boolean m_isCaseLevel_;
  
  private Lock frozenLock;
  
  private static final int SORT_BUFFER_INIT_SIZE_ = 128;
  
  private static final int SORT_BUFFER_INIT_SIZE_1_ = 1024;
  
  private static final int SORT_BUFFER_INIT_SIZE_2_ = 128;
  
  private static final int SORT_BUFFER_INIT_SIZE_3_ = 128;
  
  private static final int SORT_BUFFER_INIT_SIZE_CASE_ = 32;
  
  private static final int SORT_BUFFER_INIT_SIZE_4_ = 128;
  
  private static final int CE_CONTINUATION_TAG_ = 192;
  
  private static final int CE_REMOVE_CONTINUATION_MASK_ = -193;
  
  private static final int LAST_BYTE_MASK_ = 255;
  
  private static final byte SORT_CASE_BYTE_START_ = -128;
  
  private static final byte SORT_CASE_SHIFT_START_ = 7;
  
  private static final int CE_BUFFER_SIZE_ = 512;
  
  boolean latinOneUse_;
  
  boolean latinOneRegenTable_;
  
  boolean latinOneFailed_;
  
  int latinOneTableLen_;
  
  int[] latinOneCEs_;
  
  private static final int UCOL_REORDER_CODE_IGNORE = 4102;
  
  private static final int ENDOFLATINONERANGE_ = 255;
  
  private static final int LATINONETABLELEN_ = 305;
  
  private static final int BAIL_OUT_CE_ = -16777216;
  
  ContractionInfo m_ContInfo_;
  
  private transient boolean m_reallocLatinOneCEs_;
  
  private CollationBuffer collationBuffer;
  
  public RuleBasedCollator(String rules) throws Exception {
    this.latinOneUse_ = false;
    this.latinOneRegenTable_ = false;
    this.latinOneFailed_ = false;
    this.latinOneTableLen_ = 0;
    this.latinOneCEs_ = null;
    checkUCA();
    if (rules == null)
      throw new IllegalArgumentException("Collation rules can not be null"); 
    init(rules);
  }
  
  public Object clone() throws CloneNotSupportedException {
    return clone(isFrozen());
  }
  
  private Object clone(boolean frozen) throws CloneNotSupportedException {
    RuleBasedCollator result = (RuleBasedCollator)super.clone();
    if (this.latinOneCEs_ != null) {
      result.m_reallocLatinOneCEs_ = true;
      result.m_ContInfo_ = new ContractionInfo();
    } 
    result.collationBuffer = null;
    result.frozenLock = frozen ? new ReentrantLock() : null;
    return result;
  }
  
  public CollationElementIterator getCollationElementIterator(String source) {
    return new CollationElementIterator(source, this);
  }
  
  public CollationElementIterator getCollationElementIterator(CharacterIterator source) {
    CharacterIterator newsource = (CharacterIterator)source.clone();
    return new CollationElementIterator(newsource, this);
  }
  
  public CollationElementIterator getCollationElementIterator(UCharacterIterator source) {
    return new CollationElementIterator(source, this);
  }
  
  public boolean isFrozen() {
    return (this.frozenLock != null);
  }
  
  public Collator freeze() {
    if (!isFrozen())
      this.frozenLock = new ReentrantLock(); 
    return this;
  }
  
  public RuleBasedCollator cloneAsThawed() {
    RuleBasedCollator clone = null;
    try {
      clone = (RuleBasedCollator)clone(false);
    } catch (CloneNotSupportedException e) {}
    return clone;
  }
  
  public void setHiraganaQuaternary(boolean flag) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
  }
  
  public void setHiraganaQuaternaryDefault() {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
  }
  
  public void setUpperCaseFirst(boolean upperfirst) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    if (upperfirst) {
      if (this.m_caseFirst_ != 25)
        this.latinOneRegenTable_ = true; 
      this.m_caseFirst_ = 25;
    } else {
      if (this.m_caseFirst_ != 16)
        this.latinOneRegenTable_ = true; 
      this.m_caseFirst_ = 16;
    } 
    updateInternalState();
  }
  
  public void setLowerCaseFirst(boolean lowerfirst) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    if (lowerfirst) {
      if (this.m_caseFirst_ != 24)
        this.latinOneRegenTable_ = true; 
      this.m_caseFirst_ = 24;
    } else {
      if (this.m_caseFirst_ != 16)
        this.latinOneRegenTable_ = true; 
      this.m_caseFirst_ = 16;
    } 
    updateInternalState();
  }
  
  public final void setCaseFirstDefault() {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    if (this.m_caseFirst_ != this.m_defaultCaseFirst_)
      this.latinOneRegenTable_ = true; 
    this.m_caseFirst_ = this.m_defaultCaseFirst_;
    updateInternalState();
  }
  
  public void setAlternateHandlingDefault() {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    this.m_isAlternateHandlingShifted_ = this.m_defaultIsAlternateHandlingShifted_;
    updateInternalState();
  }
  
  public void setCaseLevelDefault() {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    this.m_isCaseLevel_ = this.m_defaultIsCaseLevel_;
    updateInternalState();
  }
  
  public void setDecompositionDefault() {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    setDecomposition(this.m_defaultDecomposition_);
    updateInternalState();
  }
  
  public void setFrenchCollationDefault() {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    if (this.m_isFrenchCollation_ != this.m_defaultIsFrenchCollation_)
      this.latinOneRegenTable_ = true; 
    this.m_isFrenchCollation_ = this.m_defaultIsFrenchCollation_;
    updateInternalState();
  }
  
  public void setStrengthDefault() {
    setStrength(this.m_defaultStrength_);
    updateInternalState();
  }
  
  public void setNumericCollationDefault() {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    setNumericCollation(this.m_defaultIsNumericCollation_);
    updateInternalState();
  }
  
  public void setFrenchCollation(boolean flag) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    if (this.m_isFrenchCollation_ != flag)
      this.latinOneRegenTable_ = true; 
    this.m_isFrenchCollation_ = flag;
    updateInternalState();
  }
  
  public void setAlternateHandlingShifted(boolean shifted) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    this.m_isAlternateHandlingShifted_ = shifted;
    updateInternalState();
  }
  
  public void setCaseLevel(boolean flag) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    this.m_isCaseLevel_ = flag;
    updateInternalState();
  }
  
  public void setStrength(int newStrength) {
    super.setStrength(newStrength);
    updateInternalState();
  }
  
  public int setVariableTop(String varTop) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    if (varTop == null || varTop.length() == 0)
      throw new IllegalArgumentException("Variable top argument string can not be null or zero in length."); 
    CollationBuffer buffer = null;
    try {
      buffer = getCollationBuffer();
      return setVariableTop(varTop, buffer);
    } finally {
      releaseCollationBuffer(buffer);
    } 
  }
  
  private int setVariableTop(String varTop, CollationBuffer buffer) {
    buffer.m_srcUtilColEIter_.setText(varTop);
    int ce = buffer.m_srcUtilColEIter_.next();
    if (buffer.m_srcUtilColEIter_.getOffset() != varTop.length() || ce == -1)
      throw new IllegalArgumentException("Variable top argument string is a contraction that does not exist in the Collation order"); 
    int nextCE = buffer.m_srcUtilColEIter_.next();
    if (nextCE != -1 && (!isContinuation(nextCE) || (nextCE & 0xFFFF0000) != 0))
      throw new IllegalArgumentException("Variable top argument string can only have a single collation element that has less than or equal to two PRIMARY strength bytes"); 
    this.m_variableTopValue_ = (ce & 0xFFFF0000) >> 16;
    return ce & 0xFFFF0000;
  }
  
  public void setVariableTop(int varTop) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    this.m_variableTopValue_ = (varTop & 0xFFFF0000) >> 16;
  }
  
  public void setNumericCollation(boolean flag) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    this.m_isNumericCollation_ = flag;
    updateInternalState();
  }
  
  public void setReorderCodes(int... order) {
    if (isFrozen())
      throw new UnsupportedOperationException("Attempt to modify frozen object"); 
    if (order != null && order.length > 0) {
      this.m_reorderCodes_ = (int[])order.clone();
    } else {
      this.m_reorderCodes_ = null;
    } 
    buildPermutationTable();
  }
  
  public String getRules() {
    return this.m_rules_;
  }
  
  public String getRules(boolean fullrules) {
    if (!fullrules)
      return this.m_rules_; 
    return UCA_.m_rules_.concat(this.m_rules_);
  }
  
  public UnicodeSet getTailoredSet() {
    try {
      CollationRuleParser src = new CollationRuleParser(getRules());
      return src.getTailoredSet();
    } catch (Exception e) {
      throw new IllegalStateException("A tailoring rule should not have errors. Something is quite wrong!");
    } 
  }
  
  private static class contContext {
    RuleBasedCollator coll;
    
    UnicodeSet contractions;
    
    UnicodeSet expansions;
    
    UnicodeSet removedContractions;
    
    boolean addPrefixes;
    
    contContext(RuleBasedCollator coll, UnicodeSet contractions, UnicodeSet expansions, UnicodeSet removedContractions, boolean addPrefixes) {
      this.coll = coll;
      this.contractions = contractions;
      this.expansions = expansions;
      this.removedContractions = removedContractions;
      this.addPrefixes = addPrefixes;
    }
  }
  
  private void addSpecial(contContext c, StringBuilder buffer, int CE) {
    StringBuilder b = new StringBuilder();
    int offset = (CE & 0xFFFFFF) - c.coll.m_contractionOffset_;
    int newCE = c.coll.m_contractionCE_[offset];
    if (newCE != -268435456) {
      if (isSpecial(CE) && getTag(CE) == 2 && isSpecial(newCE) && getTag(newCE) == 11 && c.addPrefixes)
        addSpecial(c, buffer, newCE); 
      if (buffer.length() > 1) {
        if (c.contractions != null)
          c.contractions.add(buffer.toString()); 
        if (c.expansions != null && isSpecial(CE) && getTag(CE) == 1)
          c.expansions.add(buffer.toString()); 
      } 
    } 
    offset++;
    if (getTag(CE) == 11 && c.addPrefixes) {
      while (c.coll.m_contractionIndex_[offset] != Character.MAX_VALUE) {
        b.delete(0, b.length());
        b.append(buffer);
        newCE = c.coll.m_contractionCE_[offset];
        b.insert(0, c.coll.m_contractionIndex_[offset]);
        if (isSpecial(newCE) && (getTag(newCE) == 2 || getTag(newCE) == 11)) {
          addSpecial(c, b, newCE);
        } else {
          if (c.contractions != null)
            c.contractions.add(b.toString()); 
          if (c.expansions != null && isSpecial(newCE) && getTag(newCE) == 1)
            c.expansions.add(b.toString()); 
        } 
        offset++;
      } 
    } else if (getTag(CE) == 2) {
      while (c.coll.m_contractionIndex_[offset] != Character.MAX_VALUE) {
        b.delete(0, b.length());
        b.append(buffer);
        newCE = c.coll.m_contractionCE_[offset];
        b.append(c.coll.m_contractionIndex_[offset]);
        if (isSpecial(newCE) && (getTag(newCE) == 2 || getTag(newCE) == 11)) {
          addSpecial(c, b, newCE);
        } else {
          if (c.contractions != null)
            c.contractions.add(b.toString()); 
          if (c.expansions != null && isSpecial(newCE) && getTag(newCE) == 1)
            c.expansions.add(b.toString()); 
        } 
        offset++;
      } 
    } 
  }
  
  private void processSpecials(contContext c) {
    int internalBufferSize = 512;
    TrieIterator trieiterator = new TrieIterator((Trie)c.coll.m_trie_);
    RangeValueIterator.Element element = new RangeValueIterator.Element();
    while (trieiterator.next(element)) {
      int start = element.start;
      int limit = element.limit;
      int CE = element.value;
      StringBuilder contraction = new StringBuilder(internalBufferSize);
      if (isSpecial(CE)) {
        if ((getTag(CE) == 11 && c.addPrefixes) || getTag(CE) == 2) {
          while (start < limit) {
            if (c.removedContractions != null && c.removedContractions.contains(start)) {
              start++;
              continue;
            } 
            contraction.append((char)start);
            addSpecial(c, contraction, CE);
            start++;
          } 
          continue;
        } 
        if (c.expansions != null && getTag(CE) == 1)
          while (start < limit)
            c.expansions.add(start++);  
      } 
    } 
  }
  
  public void getContractionsAndExpansions(UnicodeSet contractions, UnicodeSet expansions, boolean addPrefixes) throws Exception {
    if (contractions != null)
      contractions.clear(); 
    if (expansions != null)
      expansions.clear(); 
    String rules = getRules();
    try {
      CollationRuleParser src = new CollationRuleParser(rules);
      contContext c = new contContext(UCA_, contractions, expansions, src.m_removeSet_, addPrefixes);
      processSpecials(c);
      c.coll = this;
      c.removedContractions = null;
      processSpecials(c);
    } catch (Exception e) {
      throw e;
    } 
  }
  
  public CollationKey getCollationKey(String source) {
    if (source == null)
      return null; 
    CollationBuffer buffer = null;
    try {
      buffer = getCollationBuffer();
      return getCollationKey(source, buffer);
    } finally {
      releaseCollationBuffer(buffer);
    } 
  }
  
  private CollationKey getCollationKey(String source, CollationBuffer buffer) {
    buffer.m_utilRawCollationKey_ = getRawCollationKey(source, buffer.m_utilRawCollationKey_, buffer);
    return new CollationKey(source, buffer.m_utilRawCollationKey_);
  }
  
  public RawCollationKey getRawCollationKey(String source, RawCollationKey key) {
    if (source == null)
      return null; 
    CollationBuffer buffer = null;
    try {
      buffer = getCollationBuffer();
      return getRawCollationKey(source, key, buffer);
    } finally {
      releaseCollationBuffer(buffer);
    } 
  }
  
  private RawCollationKey getRawCollationKey(String source, RawCollationKey key, CollationBuffer buffer) {
    int strength = getStrength();
    buffer.m_utilCompare0_ = this.m_isCaseLevel_;
    buffer.m_utilCompare2_ = (strength >= 1);
    buffer.m_utilCompare3_ = (strength >= 2);
    buffer.m_utilCompare4_ = (strength >= 3);
    buffer.m_utilCompare5_ = (strength == 15);
    boolean doFrench = (this.m_isFrenchCollation_ && buffer.m_utilCompare2_);
    int commonBottom4 = (this.m_variableTopValue_ >>> 8) + 1 & 0xFF;
    byte hiragana4 = 0;
    if (this.m_isHiragana4_ && buffer.m_utilCompare4_) {
      hiragana4 = (byte)commonBottom4;
      commonBottom4++;
    } 
    int bottomCount4 = 255 - commonBottom4;
    if (buffer.m_utilCompare5_ && Normalizer.quickCheck(source, Normalizer.NFD, 0) != Normalizer.YES) {
      source = Normalizer.decompose(source, false);
    } else if (getDecomposition() != 16 && Normalizer.quickCheck(source, Normalizer.FCD, 0) != Normalizer.YES) {
      source = Normalizer.normalize(source, Normalizer.FCD);
    } 
    getSortKeyBytes(source, doFrench, hiragana4, commonBottom4, bottomCount4, buffer);
    if (key == null)
      key = new RawCollationKey(); 
    getSortKey(source, doFrench, commonBottom4, bottomCount4, key, buffer);
    return key;
  }
  
  public boolean isUpperCaseFirst() {
    return (this.m_caseFirst_ == 25);
  }
  
  public boolean isLowerCaseFirst() {
    return (this.m_caseFirst_ == 24);
  }
  
  public boolean isAlternateHandlingShifted() {
    return this.m_isAlternateHandlingShifted_;
  }
  
  public boolean isCaseLevel() {
    return this.m_isCaseLevel_;
  }
  
  public boolean isFrenchCollation() {
    return this.m_isFrenchCollation_;
  }
  
  public boolean isHiraganaQuaternary() {
    return this.m_isHiragana4_;
  }
  
  public int getVariableTop() {
    return this.m_variableTopValue_ << 16;
  }
  
  public boolean getNumericCollation() {
    return this.m_isNumericCollation_;
  }
  
  public int[] getReorderCodes() {
    if (this.m_reorderCodes_ != null)
      return (int[])this.m_reorderCodes_.clone(); 
    return LeadByteConstants.EMPTY_INT_ARRAY;
  }
  
  public static int[] getEquivalentReorderCodes(int reorderCode) {
    Set<Integer> equivalentCodesSet = new HashSet<Integer>();
    int[] leadBytes = LEADBYTE_CONSTANTS_.getLeadBytesForReorderCode(reorderCode);
    for (int leadByte : leadBytes) {
      int[] codes = LEADBYTE_CONSTANTS_.getReorderCodesForLeadByte(leadByte);
      for (int code : codes)
        equivalentCodesSet.add(Integer.valueOf(code)); 
    } 
    int[] equivalentCodes = new int[equivalentCodesSet.size()];
    int i = 0;
    for (Iterator<Integer> i$ = equivalentCodesSet.iterator(); i$.hasNext(); ) {
      int code = ((Integer)i$.next()).intValue();
      equivalentCodes[i++] = code;
    } 
    return equivalentCodes;
  }
  
  public boolean equals(Object obj) {
    if (obj == null)
      return false; 
    if (this == obj)
      return true; 
    if (getClass() != obj.getClass())
      return false; 
    RuleBasedCollator other = (RuleBasedCollator)obj;
    if (getStrength() != other.getStrength() || getDecomposition() != other.getDecomposition() || other.m_caseFirst_ != this.m_caseFirst_ || other.m_caseSwitch_ != this.m_caseSwitch_ || other.m_isAlternateHandlingShifted_ != this.m_isAlternateHandlingShifted_ || other.m_isCaseLevel_ != this.m_isCaseLevel_ || other.m_isFrenchCollation_ != this.m_isFrenchCollation_ || other.m_isHiragana4_ != this.m_isHiragana4_)
      return false; 
    if ((((this.m_reorderCodes_ != null) ? 1 : 0) ^ ((other.m_reorderCodes_ != null) ? 1 : 0)) != 0)
      return false; 
    if (this.m_reorderCodes_ != null) {
      if (this.m_reorderCodes_.length != other.m_reorderCodes_.length)
        return false; 
      for (int j = 0; j < this.m_reorderCodes_.length; j++) {
        if (this.m_reorderCodes_[j] != other.m_reorderCodes_[j])
          return false; 
      } 
    } 
    boolean rules = (this.m_rules_ == other.m_rules_);
    if (!rules && this.m_rules_ != null && other.m_rules_ != null)
      rules = this.m_rules_.equals(other.m_rules_); 
    if (!rules || !ICUDebug.enabled("collation"))
      return rules; 
    if (this.m_addition3_ != other.m_addition3_ || this.m_bottom3_ != other.m_bottom3_ || this.m_bottomCount3_ != other.m_bottomCount3_ || this.m_common3_ != other.m_common3_ || this.m_isSimple3_ != other.m_isSimple3_ || this.m_mask3_ != other.m_mask3_ || this.m_minContractionEnd_ != other.m_minContractionEnd_ || this.m_minUnsafe_ != other.m_minUnsafe_ || this.m_top3_ != other.m_top3_ || this.m_topCount3_ != other.m_topCount3_ || !Arrays.equals(this.m_unsafe_, other.m_unsafe_))
      return false; 
    if (!this.m_trie_.equals(other.m_trie_))
      for (int j = 1114111; j >= 0; j--) {
        int v = this.m_trie_.getCodePointValue(j);
        int otherv = other.m_trie_.getCodePointValue(j);
        if (v != otherv) {
          int mask = v & 0xFF000000;
          if (mask == (otherv & 0xFF000000)) {
            v &= 0xFFFFFF;
            otherv &= 0xFFFFFF;
            if (mask == -251658240) {
              v -= this.m_expansionOffset_ << 4;
              otherv -= other.m_expansionOffset_ << 4;
            } else if (mask == -234881024) {
              v -= this.m_contractionOffset_;
              otherv -= other.m_contractionOffset_;
            } 
            if (v == otherv)
              continue; 
          } 
          return false;
        } 
        continue;
      }  
    if (!Arrays.equals(this.m_contractionCE_, other.m_contractionCE_) || !Arrays.equals(this.m_contractionEnd_, other.m_contractionEnd_) || !Arrays.equals(this.m_contractionIndex_, other.m_contractionIndex_) || !Arrays.equals(this.m_expansion_, other.m_expansion_) || !Arrays.equals(this.m_expansionEndCE_, other.m_expansionEndCE_))
      return false; 
    for (int i = 0; i < this.m_expansionEndCE_.length; i++) {
      if (this.m_expansionEndCEMaxSize_[i] != other.m_expansionEndCEMaxSize_[i])
        return false; 
    } 
    return true;
  }
  
  public int hashCode() {
    String rules = getRules();
    if (rules == null)
      rules = ""; 
    return rules.hashCode();
  }
  
  public int compare(String source, String target) {
    if (source.equals(target))
      return 0; 
    CollationBuffer buffer = null;
    try {
      buffer = getCollationBuffer();
      return compare(source, target, buffer);
    } finally {
      releaseCollationBuffer(buffer);
    } 
  }
  
  private int compare(String source, String target, CollationBuffer buffer) {
    int offset = getFirstUnmatchedOffset(source, target);
    if (this.latinOneUse_) {
      if ((offset < source.length() && source.charAt(offset) > 'ÿ') || (offset < target.length() && target.charAt(offset) > 'ÿ'))
        return compareRegular(source, target, offset, buffer); 
      return compareUseLatin1(source, target, offset, buffer);
    } 
    return compareRegular(source, target, offset, buffer);
  }
  
  static interface AttributeValue {
    public static final int DEFAULT_ = -1;
    
    public static final int PRIMARY_ = 0;
    
    public static final int SECONDARY_ = 1;
    
    public static final int TERTIARY_ = 2;
    
    public static final int DEFAULT_STRENGTH_ = 2;
    
    public static final int CE_STRENGTH_LIMIT_ = 3;
    
    public static final int QUATERNARY_ = 3;
    
    public static final int IDENTICAL_ = 15;
    
    public static final int STRENGTH_LIMIT_ = 16;
    
    public static final int OFF_ = 16;
    
    public static final int ON_ = 17;
    
    public static final int SHIFTED_ = 20;
    
    public static final int NON_IGNORABLE_ = 21;
    
    public static final int LOWER_FIRST_ = 24;
    
    public static final int UPPER_FIRST_ = 25;
    
    public static final int LIMIT_ = 29;
  }
  
  static interface Attribute {
    public static final int FRENCH_COLLATION_ = 0;
    
    public static final int ALTERNATE_HANDLING_ = 1;
    
    public static final int CASE_FIRST_ = 2;
    
    public static final int CASE_LEVEL_ = 3;
    
    public static final int NORMALIZATION_MODE_ = 4;
    
    public static final int STRENGTH_ = 5;
    
    public static final int HIRAGANA_QUATERNARY_MODE_ = 6;
    
    public static final int LIMIT_ = 7;
  }
  
  static class DataManipulate implements Trie.DataManipulate {
    private static DataManipulate m_instance_;
    
    public final int getFoldingOffset(int ce) {
      if (RuleBasedCollator.isSpecial(ce) && RuleBasedCollator.getTag(ce) == 5)
        return ce & 0xFFFFFF; 
      return 0;
    }
    
    public static final DataManipulate getInstance() {
      if (m_instance_ == null)
        m_instance_ = new DataManipulate(); 
      return m_instance_;
    }
  }
  
  static final class UCAConstants {
    int[] FIRST_TERTIARY_IGNORABLE_ = new int[2];
    
    int[] LAST_TERTIARY_IGNORABLE_ = new int[2];
    
    int[] FIRST_PRIMARY_IGNORABLE_ = new int[2];
    
    int[] FIRST_SECONDARY_IGNORABLE_ = new int[2];
    
    int[] LAST_SECONDARY_IGNORABLE_ = new int[2];
    
    int[] LAST_PRIMARY_IGNORABLE_ = new int[2];
    
    int[] FIRST_VARIABLE_ = new int[2];
    
    int[] LAST_VARIABLE_ = new int[2];
    
    int[] FIRST_NON_VARIABLE_ = new int[2];
    
    int[] LAST_NON_VARIABLE_ = new int[2];
    
    int[] RESET_TOP_VALUE_ = new int[2];
    
    int[] FIRST_IMPLICIT_ = new int[2];
    
    int[] LAST_IMPLICIT_ = new int[2];
    
    int[] FIRST_TRAILING_ = new int[2];
    
    int[] LAST_TRAILING_ = new int[2];
    
    int PRIMARY_TOP_MIN_;
    
    int PRIMARY_IMPLICIT_MIN_;
    
    int PRIMARY_IMPLICIT_MAX_;
    
    int PRIMARY_TRAILING_MIN_;
    
    int PRIMARY_TRAILING_MAX_;
    
    int PRIMARY_SPECIAL_MIN_;
    
    int PRIMARY_SPECIAL_MAX_;
  }
  
  static final class LeadByteConstants {
    private static final int DATA_MASK_FOR_INDEX = 32768;
    
    private static final int[] EMPTY_INT_ARRAY = new int[0];
    
    private int serializedSize = 0;
    
    private Map<Integer, Integer> SCRIPT_TO_LEAD_BYTES_INDEX;
    
    private byte[] SCRIPT_TO_LEAD_BYTES_DATA;
    
    private int[] LEAD_BYTE_TO_SCRIPTS_INDEX;
    
    private byte[] LEAD_BYTE_TO_SCRIPTS_DATA;
    
    void read(DataInputStream dis) throws IOException {
      int readcount = 0;
      int indexCount = dis.readShort();
      readcount += 2;
      int dataSize = dis.readShort();
      readcount += 2;
      this.SCRIPT_TO_LEAD_BYTES_INDEX = new HashMap<Integer, Integer>();
      int index;
      for (index = 0; index < indexCount; index++) {
        int reorderCode = dis.readShort();
        readcount += 2;
        int dataOffset = 0xFFFF & dis.readShort();
        readcount += 2;
        this.SCRIPT_TO_LEAD_BYTES_INDEX.put(Integer.valueOf(reorderCode), Integer.valueOf(dataOffset));
      } 
      this.SCRIPT_TO_LEAD_BYTES_DATA = new byte[dataSize * 2];
      dis.readFully(this.SCRIPT_TO_LEAD_BYTES_DATA, 0, this.SCRIPT_TO_LEAD_BYTES_DATA.length);
      readcount += this.SCRIPT_TO_LEAD_BYTES_DATA.length;
      indexCount = dis.readShort();
      readcount += 2;
      dataSize = dis.readShort();
      readcount += 2;
      this.LEAD_BYTE_TO_SCRIPTS_INDEX = new int[indexCount];
      for (index = 0; index < indexCount; index++) {
        this.LEAD_BYTE_TO_SCRIPTS_INDEX[index] = 0xFFFF & dis.readShort();
        readcount += 2;
      } 
      this.LEAD_BYTE_TO_SCRIPTS_DATA = new byte[dataSize * 2];
      dis.readFully(this.LEAD_BYTE_TO_SCRIPTS_DATA, 0, this.LEAD_BYTE_TO_SCRIPTS_DATA.length);
      readcount += this.LEAD_BYTE_TO_SCRIPTS_DATA.length;
      this.serializedSize = readcount;
    }
    
    int getSerializedDataSize() {
      return this.serializedSize;
    }
    
    int[] getReorderCodesForLeadByte(int leadByte) {
      int[] reorderCodes;
      if (leadByte >= this.LEAD_BYTE_TO_SCRIPTS_INDEX.length)
        return EMPTY_INT_ARRAY; 
      int offset = this.LEAD_BYTE_TO_SCRIPTS_INDEX[leadByte];
      if (offset == 0)
        return EMPTY_INT_ARRAY; 
      if ((offset & 0x8000) == 32768) {
        reorderCodes = new int[1];
        reorderCodes[0] = offset & 0xFFFF7FFF;
      } else {
        int length = readShort(this.LEAD_BYTE_TO_SCRIPTS_DATA, offset);
        offset++;
        reorderCodes = new int[length];
        for (int code = 0; code < length; code++, offset++)
          reorderCodes[code] = readShort(this.LEAD_BYTE_TO_SCRIPTS_DATA, offset); 
      } 
      return reorderCodes;
    }
    
    int[] getLeadBytesForReorderCode(int reorderCode) {
      int[] leadBytes;
      if (!this.SCRIPT_TO_LEAD_BYTES_INDEX.containsKey(Integer.valueOf(reorderCode)))
        return EMPTY_INT_ARRAY; 
      int offset = ((Integer)this.SCRIPT_TO_LEAD_BYTES_INDEX.get(Integer.valueOf(reorderCode))).intValue();
      if (offset == 0)
        return EMPTY_INT_ARRAY; 
      if ((offset & 0x8000) == 32768) {
        leadBytes = new int[1];
        leadBytes[0] = offset & 0xFFFF7FFF;
      } else {
        int length = readShort(this.SCRIPT_TO_LEAD_BYTES_DATA, offset);
        offset++;
        leadBytes = new int[length];
        for (int leadByte = 0; leadByte < length; leadByte++, offset++)
          leadBytes[leadByte] = readShort(this.SCRIPT_TO_LEAD_BYTES_DATA, offset); 
      } 
      return leadBytes;
    }
    
    private static int readShort(byte[] data, int offset) {
      return (0xFF & data[offset * 2]) << 8 | data[offset * 2 + 1] & 0xFF;
    }
  }
  
  static {
    RuleBasedCollator iUCA_ = null;
    UCAConstants iUCA_CONSTANTS_ = null;
    LeadByteConstants iLEADBYTE_CONSTANTS = null;
    char[] iUCA_CONTRACTIONS_ = null;
    Output<Integer> maxUCAContractionLength = new Output();
    ImplicitCEGenerator iimpCEGen_ = null;
    try {
      iUCA_ = new RuleBasedCollator();
      iUCA_CONSTANTS_ = new UCAConstants();
      iLEADBYTE_CONSTANTS = new LeadByteConstants();
      iUCA_CONTRACTIONS_ = CollatorReader.read(iUCA_, iUCA_CONSTANTS_, iLEADBYTE_CONSTANTS, maxUCAContractionLength);
      iimpCEGen_ = new ImplicitCEGenerator(224, 228);
      iUCA_.init();
      ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/coll", ULocale.ENGLISH);
      iUCA_.m_rules_ = (String)rb.getObject("UCARules");
    } catch (MissingResourceException ex) {
    
    } catch (IOException e) {}
    UCA_ = iUCA_;
    UCA_CONSTANTS_ = iUCA_CONSTANTS_;
    LEADBYTE_CONSTANTS_ = iLEADBYTE_CONSTANTS;
    UCA_CONTRACTIONS_ = iUCA_CONTRACTIONS_;
    MAX_UCA_CONTRACTION_LENGTH = ((Integer)maxUCAContractionLength.value).intValue();
    impCEGen_ = iimpCEGen_;
    UCA_INIT_COMPLETE = true;
  }
  
  private static void checkUCA() throws MissingResourceException {
    if (UCA_INIT_COMPLETE && UCA_ == null)
      throw new MissingResourceException("Collator UCA data unavailable", "", ""); 
  }
  
  RuleBasedCollator() {
    this.latinOneUse_ = false;
    this.latinOneRegenTable_ = false;
    this.latinOneFailed_ = false;
    this.latinOneTableLen_ = 0;
    this.latinOneCEs_ = null;
    checkUCA();
  }
  
  RuleBasedCollator(ULocale locale) {
    this.latinOneUse_ = false;
    this.latinOneRegenTable_ = false;
    this.latinOneFailed_ = false;
    this.latinOneTableLen_ = 0;
    this.latinOneCEs_ = null;
    checkUCA();
    try {
      ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/coll", locale);
      if (rb != null) {
        ICUResourceBundle elements = null;
        String collkey = locale.getKeywordValue("collation");
        if (collkey != null)
          try {
            elements = rb.getWithFallback("collations/" + collkey);
          } catch (MissingResourceException e) {} 
        if (elements == null) {
          collkey = rb.getStringWithFallback("collations/default");
          elements = rb.getWithFallback("collations/" + collkey);
        } 
        ULocale uloc = rb.getULocale();
        setLocale(uloc, uloc);
        this.m_rules_ = elements.getString("Sequence");
        ByteBuffer buf = elements.get("%%CollationBin").getBinary();
        if (buf != null) {
          CollatorReader.initRBC(this, buf);
          if (!this.m_UCA_version_.equals(UCA_.m_UCA_version_) || !this.m_UCD_version_.equals(UCA_.m_UCD_version_)) {
            init(this.m_rules_);
            return;
          } 
          init();
          try {
            UResourceBundle reorderRes = elements.get("%%ReorderCodes");
            if (reorderRes != null) {
              int[] reorderCodes = reorderRes.getIntVector();
              setReorderCodes(reorderCodes);
              this.m_defaultReorderCodes_ = (int[])reorderCodes.clone();
            } 
          } catch (MissingResourceException e) {}
          return;
        } 
        init(this.m_rules_);
        return;
      } 
    } catch (Exception e) {}
    setWithUCAData();
  }
  
  final void setWithUCATables() {
    this.m_contractionOffset_ = UCA_.m_contractionOffset_;
    this.m_expansionOffset_ = UCA_.m_expansionOffset_;
    this.m_expansion_ = UCA_.m_expansion_;
    this.m_contractionIndex_ = UCA_.m_contractionIndex_;
    this.m_contractionCE_ = UCA_.m_contractionCE_;
    this.m_trie_ = UCA_.m_trie_;
    this.m_expansionEndCE_ = UCA_.m_expansionEndCE_;
    this.m_expansionEndCEMaxSize_ = UCA_.m_expansionEndCEMaxSize_;
    this.m_unsafe_ = UCA_.m_unsafe_;
    this.m_contractionEnd_ = UCA_.m_contractionEnd_;
    this.m_minUnsafe_ = UCA_.m_minUnsafe_;
    this.m_minContractionEnd_ = UCA_.m_minContractionEnd_;
  }
  
  final void setWithUCAData() {
    this.latinOneFailed_ = true;
    this.m_addition3_ = UCA_.m_addition3_;
    this.m_bottom3_ = UCA_.m_bottom3_;
    this.m_bottomCount3_ = UCA_.m_bottomCount3_;
    this.m_caseFirst_ = UCA_.m_caseFirst_;
    this.m_caseSwitch_ = UCA_.m_caseSwitch_;
    this.m_common3_ = UCA_.m_common3_;
    this.m_contractionOffset_ = UCA_.m_contractionOffset_;
    setDecomposition(UCA_.getDecomposition());
    this.m_defaultCaseFirst_ = UCA_.m_defaultCaseFirst_;
    this.m_defaultDecomposition_ = UCA_.m_defaultDecomposition_;
    this.m_defaultIsAlternateHandlingShifted_ = UCA_.m_defaultIsAlternateHandlingShifted_;
    this.m_defaultIsCaseLevel_ = UCA_.m_defaultIsCaseLevel_;
    this.m_defaultIsFrenchCollation_ = UCA_.m_defaultIsFrenchCollation_;
    this.m_defaultIsHiragana4_ = UCA_.m_defaultIsHiragana4_;
    this.m_defaultStrength_ = UCA_.m_defaultStrength_;
    this.m_defaultVariableTopValue_ = UCA_.m_defaultVariableTopValue_;
    this.m_defaultIsNumericCollation_ = UCA_.m_defaultIsNumericCollation_;
    this.m_expansionOffset_ = UCA_.m_expansionOffset_;
    this.m_isAlternateHandlingShifted_ = UCA_.m_isAlternateHandlingShifted_;
    this.m_isCaseLevel_ = UCA_.m_isCaseLevel_;
    this.m_isFrenchCollation_ = UCA_.m_isFrenchCollation_;
    this.m_isHiragana4_ = UCA_.m_isHiragana4_;
    this.m_isJamoSpecial_ = UCA_.m_isJamoSpecial_;
    this.m_isSimple3_ = UCA_.m_isSimple3_;
    this.m_mask3_ = UCA_.m_mask3_;
    this.m_minContractionEnd_ = UCA_.m_minContractionEnd_;
    this.m_minUnsafe_ = UCA_.m_minUnsafe_;
    this.m_rules_ = UCA_.m_rules_;
    setStrength(UCA_.getStrength());
    this.m_top3_ = UCA_.m_top3_;
    this.m_topCount3_ = UCA_.m_topCount3_;
    this.m_variableTopValue_ = UCA_.m_variableTopValue_;
    this.m_isNumericCollation_ = UCA_.m_isNumericCollation_;
    setWithUCATables();
    this.latinOneFailed_ = false;
  }
  
  final boolean isUnsafe(char ch) {
    if (ch < this.m_minUnsafe_)
      return false; 
    if (ch >= '℀') {
      if (UTF16.isLeadSurrogate(ch) || UTF16.isTrailSurrogate(ch))
        return true; 
      ch = (char)(ch & 0x1FFF);
      ch = (char)(ch + 256);
    } 
    int value = this.m_unsafe_[ch >> 3];
    return ((value >> (ch & 0x7) & 0x1) != 0);
  }
  
  final boolean isContractionEnd(char ch) {
    if (UTF16.isTrailSurrogate(ch))
      return true; 
    if (ch < this.m_minContractionEnd_)
      return false; 
    if (ch >= '℀') {
      ch = (char)(ch & 0x1FFF);
      ch = (char)(ch + 256);
    } 
    int value = this.m_contractionEnd_[ch >> 3];
    return ((value >> (ch & 0x7) & 0x1) != 0);
  }
  
  static int getTag(int ce) {
    return (ce & 0xF000000) >> 24;
  }
  
  static boolean isSpecial(int ce) {
    return ((ce & 0xF0000000) == -268435456);
  }
  
  static final boolean isContinuation(int ce) {
    return (ce != -1 && (ce & 0xC0) == 192);
  }
  
  private final class CollationBuffer {
    protected StringUCharacterIterator m_srcUtilIter_;
    
    protected CollationElementIterator m_srcUtilColEIter_;
    
    protected StringUCharacterIterator m_tgtUtilIter_;
    
    protected CollationElementIterator m_tgtUtilColEIter_;
    
    protected boolean m_utilCompare0_;
    
    protected boolean m_utilCompare2_;
    
    protected boolean m_utilCompare3_;
    
    protected boolean m_utilCompare4_;
    
    protected boolean m_utilCompare5_;
    
    protected byte[] m_utilBytes0_;
    
    protected byte[] m_utilBytes1_;
    
    protected byte[] m_utilBytes2_;
    
    protected byte[] m_utilBytes3_;
    
    protected byte[] m_utilBytes4_;
    
    protected RawCollationKey m_utilRawCollationKey_;
    
    protected int m_utilBytesCount0_;
    
    protected int m_utilBytesCount1_;
    
    protected int m_utilBytesCount2_;
    
    protected int m_utilBytesCount3_;
    
    protected int m_utilBytesCount4_;
    
    protected int m_utilCount2_;
    
    protected int m_utilCount3_;
    
    protected int m_utilCount4_;
    
    protected int m_utilFrenchStart_;
    
    protected int m_utilFrenchEnd_;
    
    protected int[] m_srcUtilCEBuffer_;
    
    protected int[] m_tgtUtilCEBuffer_;
    
    protected int m_srcUtilCEBufferSize_;
    
    protected int m_tgtUtilCEBufferSize_;
    
    protected int m_srcUtilContOffset_;
    
    protected int m_tgtUtilContOffset_;
    
    protected int m_srcUtilOffset_;
    
    protected int m_tgtUtilOffset_;
    
    private CollationBuffer() {
      initBuffers();
    }
    
    protected final void initBuffers() {
      resetBuffers();
      this.m_srcUtilIter_ = new StringUCharacterIterator();
      this.m_srcUtilColEIter_ = new CollationElementIterator((UCharacterIterator)this.m_srcUtilIter_, RuleBasedCollator.this);
      this.m_tgtUtilIter_ = new StringUCharacterIterator();
      this.m_tgtUtilColEIter_ = new CollationElementIterator((UCharacterIterator)this.m_tgtUtilIter_, RuleBasedCollator.this);
      this.m_utilBytes0_ = new byte[32];
      this.m_utilBytes1_ = new byte[1024];
      this.m_utilBytes2_ = new byte[128];
      this.m_utilBytes3_ = new byte[128];
      this.m_utilBytes4_ = new byte[128];
      this.m_srcUtilCEBuffer_ = new int[512];
      this.m_tgtUtilCEBuffer_ = new int[512];
    }
    
    protected final void resetBuffers() {
      this.m_utilCompare0_ = false;
      this.m_utilCompare2_ = false;
      this.m_utilCompare3_ = false;
      this.m_utilCompare4_ = false;
      this.m_utilCompare5_ = false;
      this.m_utilBytesCount0_ = 0;
      this.m_utilBytesCount1_ = 0;
      this.m_utilBytesCount2_ = 0;
      this.m_utilBytesCount3_ = 0;
      this.m_utilBytesCount4_ = 0;
      this.m_utilCount2_ = 0;
      this.m_utilCount3_ = 0;
      this.m_utilCount4_ = 0;
      this.m_utilFrenchStart_ = 0;
      this.m_utilFrenchEnd_ = 0;
      this.m_srcUtilContOffset_ = 0;
      this.m_tgtUtilContOffset_ = 0;
      this.m_srcUtilOffset_ = 0;
      this.m_tgtUtilOffset_ = 0;
    }
  }
  
  private void init(String rules) throws Exception {
    setWithUCAData();
    CollationParsedRuleBuilder builder = new CollationParsedRuleBuilder(rules);
    builder.setRules(this);
    this.m_rules_ = rules;
    init();
    buildPermutationTable();
  }
  
  private final int compareRegular(String source, String target, int offset, CollationBuffer buffer) {
    buffer.resetBuffers();
    int strength = getStrength();
    buffer.m_utilCompare0_ = this.m_isCaseLevel_;
    buffer.m_utilCompare2_ = (strength >= 1);
    buffer.m_utilCompare3_ = (strength >= 2);
    buffer.m_utilCompare4_ = (strength >= 3);
    buffer.m_utilCompare5_ = (strength == 15);
    boolean doFrench = (this.m_isFrenchCollation_ && buffer.m_utilCompare2_);
    boolean doShift4 = (this.m_isAlternateHandlingShifted_ && buffer.m_utilCompare4_);
    boolean doHiragana4 = (this.m_isHiragana4_ && buffer.m_utilCompare4_);
    if (doHiragana4 && doShift4) {
      String sourcesub = source.substring(offset);
      String targetsub = target.substring(offset);
      return compareBySortKeys(sourcesub, targetsub, buffer);
    } 
    int lowestpvalue = this.m_isAlternateHandlingShifted_ ? (this.m_variableTopValue_ << 16) : 0;
    buffer.m_srcUtilCEBufferSize_ = 0;
    buffer.m_tgtUtilCEBufferSize_ = 0;
    int result = doPrimaryCompare(doHiragana4, lowestpvalue, source, target, offset, buffer);
    if (buffer.m_srcUtilCEBufferSize_ == -1 && buffer.m_tgtUtilCEBufferSize_ == -1)
      return result; 
    int hiraganaresult = result;
    if (buffer.m_utilCompare2_) {
      result = doSecondaryCompare(doFrench, buffer);
      if (result != 0)
        return result; 
    } 
    if (buffer.m_utilCompare0_) {
      result = doCaseCompare(buffer);
      if (result != 0)
        return result; 
    } 
    if (buffer.m_utilCompare3_) {
      result = doTertiaryCompare(buffer);
      if (result != 0)
        return result; 
    } 
    if (doShift4) {
      result = doQuaternaryCompare(lowestpvalue, buffer);
      if (result != 0)
        return result; 
    } else if (doHiragana4 && hiraganaresult != 0) {
      return hiraganaresult;
    } 
    if (buffer.m_utilCompare5_)
      return doIdenticalCompare(source, target, offset, true); 
    return 0;
  }
  
  static boolean isCompressible(int primary1) {
    return (91 <= primary1 && primary1 <= 122);
  }
  
  private final int doPrimaryBytes(int ce, boolean notIsContinuation, boolean doShift, int leadPrimary, int commonBottom4, int bottomCount4, CollationBuffer buffer) {
    int p2 = (ce >>>= 16) & 0xFF;
    int p1 = ce >>> 8;
    int originalP1 = p1;
    if (notIsContinuation && 
      this.m_leadBytePermutationTable_ != null)
      p1 = 0xFF & this.m_leadBytePermutationTable_[p1]; 
    if (doShift) {
      if (buffer.m_utilCount4_ > 0) {
        while (buffer.m_utilCount4_ > bottomCount4) {
          buffer.m_utilBytes4_ = append(buffer.m_utilBytes4_, buffer.m_utilBytesCount4_, (byte)(commonBottom4 + bottomCount4));
          buffer.m_utilBytesCount4_++;
          buffer.m_utilCount4_ -= bottomCount4;
        } 
        buffer.m_utilBytes4_ = append(buffer.m_utilBytes4_, buffer.m_utilBytesCount4_, (byte)(commonBottom4 + buffer.m_utilCount4_ - 1));
        buffer.m_utilBytesCount4_++;
        buffer.m_utilCount4_ = 0;
      } 
      if (p1 != 0) {
        buffer.m_utilBytes4_ = append(buffer.m_utilBytes4_, buffer.m_utilBytesCount4_, (byte)p1);
        buffer.m_utilBytesCount4_++;
      } 
      if (p2 != 0) {
        buffer.m_utilBytes4_ = append(buffer.m_utilBytes4_, buffer.m_utilBytesCount4_, (byte)p2);
        buffer.m_utilBytesCount4_++;
      } 
    } else if (p1 != 0) {
      if (notIsContinuation) {
        if (leadPrimary == p1) {
          buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)p2);
          buffer.m_utilBytesCount1_++;
        } else {
          if (leadPrimary != 0) {
            buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (p1 > leadPrimary) ? -1 : 3);
            buffer.m_utilBytesCount1_++;
          } 
          if (p2 == 0) {
            buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)p1);
            buffer.m_utilBytesCount1_++;
            leadPrimary = 0;
          } else if (isCompressible(originalP1)) {
            leadPrimary = p1;
            buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)p1);
            buffer.m_utilBytesCount1_++;
            buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)p2);
            buffer.m_utilBytesCount1_++;
          } else {
            leadPrimary = 0;
            buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)p1);
            buffer.m_utilBytesCount1_++;
            buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)p2);
            buffer.m_utilBytesCount1_++;
          } 
        } 
      } else {
        buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)p1);
        buffer.m_utilBytesCount1_++;
        if (p2 != 0) {
          buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)p2);
          buffer.m_utilBytesCount1_++;
        } 
      } 
    } 
    return leadPrimary;
  }
  
  private final void doSecondaryBytes(int ce, boolean notIsContinuation, boolean doFrench, CollationBuffer buffer) {
    int s = ce >> 8 & 0xFF;
    if (s != 0)
      if (!doFrench) {
        if (s == 5 && notIsContinuation) {
          buffer.m_utilCount2_++;
        } else {
          if (buffer.m_utilCount2_ > 0) {
            if (s > 5) {
              while (buffer.m_utilCount2_ > 64) {
                buffer.m_utilBytes2_ = append(buffer.m_utilBytes2_, buffer.m_utilBytesCount2_, (byte)70);
                buffer.m_utilBytesCount2_++;
                buffer.m_utilCount2_ -= 64;
              } 
              buffer.m_utilBytes2_ = append(buffer.m_utilBytes2_, buffer.m_utilBytesCount2_, (byte)(134 - buffer.m_utilCount2_ - 1));
              buffer.m_utilBytesCount2_++;
            } else {
              while (buffer.m_utilCount2_ > 64) {
                buffer.m_utilBytes2_ = append(buffer.m_utilBytes2_, buffer.m_utilBytesCount2_, (byte)69);
                buffer.m_utilBytesCount2_++;
                buffer.m_utilCount2_ -= 64;
              } 
              buffer.m_utilBytes2_ = append(buffer.m_utilBytes2_, buffer.m_utilBytesCount2_, (byte)(5 + buffer.m_utilCount2_ - 1));
              buffer.m_utilBytesCount2_++;
            } 
            buffer.m_utilCount2_ = 0;
          } 
          buffer.m_utilBytes2_ = append(buffer.m_utilBytes2_, buffer.m_utilBytesCount2_, (byte)s);
          buffer.m_utilBytesCount2_++;
        } 
      } else {
        buffer.m_utilBytes2_ = append(buffer.m_utilBytes2_, buffer.m_utilBytesCount2_, (byte)s);
        buffer.m_utilBytesCount2_++;
        if (notIsContinuation) {
          if (buffer.m_utilFrenchStart_ != -1) {
            reverseBuffer(buffer.m_utilBytes2_, buffer.m_utilFrenchStart_, buffer.m_utilFrenchEnd_);
            buffer.m_utilFrenchStart_ = -1;
          } 
        } else {
          if (buffer.m_utilFrenchStart_ == -1)
            buffer.m_utilFrenchStart_ = buffer.m_utilBytesCount2_ - 2; 
          buffer.m_utilFrenchEnd_ = buffer.m_utilBytesCount2_ - 1;
        } 
      }  
  }
  
  private static void reverseBuffer(byte[] buffer, int start, int end) {
    while (start < end) {
      byte b = buffer[start];
      buffer[start++] = buffer[end];
      buffer[end--] = b;
    } 
  }
  
  private final int doCaseShift(int caseshift, CollationBuffer buffer) {
    if (caseshift == 0) {
      buffer.m_utilBytes0_ = append(buffer.m_utilBytes0_, buffer.m_utilBytesCount0_, -128);
      buffer.m_utilBytesCount0_++;
      caseshift = 7;
    } 
    return caseshift;
  }
  
  private final int doCaseBytes(int tertiary, boolean notIsContinuation, int caseshift, CollationBuffer buffer) {
    caseshift = doCaseShift(caseshift, buffer);
    if (notIsContinuation && tertiary != 0) {
      byte casebits = (byte)(tertiary & 0xC0);
      if (this.m_caseFirst_ == 25) {
        if (casebits == 0) {
          buffer.m_utilBytes0_[buffer.m_utilBytesCount0_ - 1] = (byte)(buffer.m_utilBytes0_[buffer.m_utilBytesCount0_ - 1] | 1 << --caseshift);
        } else {
          caseshift = doCaseShift(caseshift - 1, buffer);
          buffer.m_utilBytes0_[buffer.m_utilBytesCount0_ - 1] = (byte)(buffer.m_utilBytes0_[buffer.m_utilBytesCount0_ - 1] | (casebits >> 6 & 0x1) << --caseshift);
        } 
      } else if (casebits != 0) {
        buffer.m_utilBytes0_[buffer.m_utilBytesCount0_ - 1] = (byte)(buffer.m_utilBytes0_[buffer.m_utilBytesCount0_ - 1] | 1 << --caseshift);
        caseshift = doCaseShift(caseshift, buffer);
        buffer.m_utilBytes0_[buffer.m_utilBytesCount0_ - 1] = (byte)(buffer.m_utilBytes0_[buffer.m_utilBytesCount0_ - 1] | (casebits >> 7 & 0x1) << --caseshift);
      } else {
        caseshift--;
      } 
    } 
    return caseshift;
  }
  
  private final void doTertiaryBytes(int tertiary, boolean notIsContinuation, CollationBuffer buffer) {
    if (tertiary != 0)
      if (tertiary == this.m_common3_ && notIsContinuation) {
        buffer.m_utilCount3_++;
      } else {
        int common3 = this.m_common3_ & 0xFF;
        if (tertiary > common3 && this.m_common3_ == 5) {
          tertiary += this.m_addition3_;
        } else if (tertiary <= common3 && this.m_common3_ == 197) {
          tertiary -= this.m_addition3_;
        } 
        if (buffer.m_utilCount3_ > 0) {
          if (tertiary > common3) {
            while (buffer.m_utilCount3_ > this.m_topCount3_) {
              buffer.m_utilBytes3_ = append(buffer.m_utilBytes3_, buffer.m_utilBytesCount3_, (byte)(this.m_top3_ - this.m_topCount3_));
              buffer.m_utilBytesCount3_++;
              buffer.m_utilCount3_ -= this.m_topCount3_;
            } 
            buffer.m_utilBytes3_ = append(buffer.m_utilBytes3_, buffer.m_utilBytesCount3_, (byte)(this.m_top3_ - buffer.m_utilCount3_ - 1));
            buffer.m_utilBytesCount3_++;
          } else {
            while (buffer.m_utilCount3_ > this.m_bottomCount3_) {
              buffer.m_utilBytes3_ = append(buffer.m_utilBytes3_, buffer.m_utilBytesCount3_, (byte)(this.m_bottom3_ + this.m_bottomCount3_));
              buffer.m_utilBytesCount3_++;
              buffer.m_utilCount3_ -= this.m_bottomCount3_;
            } 
            buffer.m_utilBytes3_ = append(buffer.m_utilBytes3_, buffer.m_utilBytesCount3_, (byte)(this.m_bottom3_ + buffer.m_utilCount3_ - 1));
            buffer.m_utilBytesCount3_++;
          } 
          buffer.m_utilCount3_ = 0;
        } 
        buffer.m_utilBytes3_ = append(buffer.m_utilBytes3_, buffer.m_utilBytesCount3_, (byte)tertiary);
        buffer.m_utilBytesCount3_++;
      }  
  }
  
  private final void doQuaternaryBytes(boolean isCodePointHiragana, int commonBottom4, int bottomCount4, byte hiragana4, CollationBuffer buffer) {
    if (isCodePointHiragana) {
      if (buffer.m_utilCount4_ > 0) {
        while (buffer.m_utilCount4_ > bottomCount4) {
          buffer.m_utilBytes4_ = append(buffer.m_utilBytes4_, buffer.m_utilBytesCount4_, (byte)(commonBottom4 + bottomCount4));
          buffer.m_utilBytesCount4_++;
          buffer.m_utilCount4_ -= bottomCount4;
        } 
        buffer.m_utilBytes4_ = append(buffer.m_utilBytes4_, buffer.m_utilBytesCount4_, (byte)(commonBottom4 + buffer.m_utilCount4_ - 1));
        buffer.m_utilBytesCount4_++;
        buffer.m_utilCount4_ = 0;
      } 
      buffer.m_utilBytes4_ = append(buffer.m_utilBytes4_, buffer.m_utilBytesCount4_, hiragana4);
      buffer.m_utilBytesCount4_++;
    } else {
      buffer.m_utilCount4_++;
    } 
  }
  
  private final void getSortKeyBytes(String source, boolean doFrench, byte hiragana4, int commonBottom4, int bottomCount4, CollationBuffer buffer) {
    int backupDecomposition = getDecomposition();
    internalSetDecomposition(16);
    buffer.m_srcUtilIter_.setText(source);
    buffer.m_srcUtilColEIter_.setText((UCharacterIterator)buffer.m_srcUtilIter_);
    buffer.m_utilFrenchStart_ = -1;
    buffer.m_utilFrenchEnd_ = -1;
    boolean doShift = false;
    boolean notIsContinuation = false;
    int leadPrimary = 0;
    int caseShift = 0;
    while (true) {
      int ce = buffer.m_srcUtilColEIter_.next();
      if (ce == -1)
        break; 
      if (ce == 0)
        continue; 
      notIsContinuation = !isContinuation(ce);
      boolean isPrimaryByteIgnorable = ((ce & 0xFFFF0000) == 0);
      boolean isSmallerThanVariableTop = (ce >>> 16 <= this.m_variableTopValue_);
      doShift = ((this.m_isAlternateHandlingShifted_ && ((notIsContinuation && isSmallerThanVariableTop && !isPrimaryByteIgnorable) || (!notIsContinuation && doShift))) || (doShift && isPrimaryByteIgnorable));
      if (doShift && isPrimaryByteIgnorable)
        continue; 
      leadPrimary = doPrimaryBytes(ce, notIsContinuation, doShift, leadPrimary, commonBottom4, bottomCount4, buffer);
      if (doShift)
        continue; 
      if (buffer.m_utilCompare2_)
        doSecondaryBytes(ce, notIsContinuation, doFrench, buffer); 
      int t = ce & 0xFF;
      if (!notIsContinuation)
        t = ce & 0xFFFFFF3F; 
      if (buffer.m_utilCompare0_ && (!isPrimaryByteIgnorable || buffer.m_utilCompare2_)) {
        caseShift = doCaseBytes(t, notIsContinuation, caseShift, buffer);
      } else if (notIsContinuation) {
        t ^= this.m_caseSwitch_;
      } 
      t &= this.m_mask3_;
      if (buffer.m_utilCompare3_)
        doTertiaryBytes(t, notIsContinuation, buffer); 
      if (buffer.m_utilCompare4_ && notIsContinuation)
        doQuaternaryBytes(buffer.m_srcUtilColEIter_.m_isCodePointHiragana_, commonBottom4, bottomCount4, hiragana4, buffer); 
    } 
    internalSetDecomposition(backupDecomposition);
    if (buffer.m_utilFrenchStart_ != -1)
      reverseBuffer(buffer.m_utilBytes2_, buffer.m_utilFrenchStart_, buffer.m_utilFrenchEnd_); 
  }
  
  private final void getSortKey(String source, boolean doFrench, int commonBottom4, int bottomCount4, RawCollationKey key, CollationBuffer buffer) {
    if (buffer.m_utilCompare2_)
      doSecondary(doFrench, buffer); 
    if (buffer.m_utilCompare0_)
      doCase(buffer); 
    if (buffer.m_utilCompare3_) {
      doTertiary(buffer);
      if (buffer.m_utilCompare4_) {
        doQuaternary(commonBottom4, bottomCount4, buffer);
        if (buffer.m_utilCompare5_)
          doIdentical(source, buffer); 
      } 
    } 
    buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)0);
    buffer.m_utilBytesCount1_++;
    key.set(buffer.m_utilBytes1_, 0, buffer.m_utilBytesCount1_);
  }
  
  private static final void doFrench(CollationBuffer buffer) {
    for (int i = 0; i < buffer.m_utilBytesCount2_; i++) {
      byte s = buffer.m_utilBytes2_[buffer.m_utilBytesCount2_ - i - 1];
      if (s == 5) {
        buffer.m_utilCount2_++;
      } else {
        if (buffer.m_utilCount2_ > 0) {
          if ((s & 0xFF) > 5) {
            while (buffer.m_utilCount2_ > 64) {
              buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)70);
              buffer.m_utilBytesCount1_++;
              buffer.m_utilCount2_ -= 64;
            } 
            buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)(134 - buffer.m_utilCount2_ - 1));
            buffer.m_utilBytesCount1_++;
          } else {
            while (buffer.m_utilCount2_ > 64) {
              buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)69);
              buffer.m_utilBytesCount1_++;
              buffer.m_utilCount2_ -= 64;
            } 
            buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)(5 + buffer.m_utilCount2_ - 1));
            buffer.m_utilBytesCount1_++;
          } 
          buffer.m_utilCount2_ = 0;
        } 
        buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, s);
        buffer.m_utilBytesCount1_++;
      } 
    } 
    if (buffer.m_utilCount2_ > 0) {
      while (buffer.m_utilCount2_ > 64) {
        buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)69);
        buffer.m_utilBytesCount1_++;
        buffer.m_utilCount2_ -= 64;
      } 
      buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)(5 + buffer.m_utilCount2_ - 1));
      buffer.m_utilBytesCount1_++;
    } 
  }
  
  private static final void doSecondary(boolean doFrench, CollationBuffer buffer) {
    if (buffer.m_utilCount2_ > 0) {
      while (buffer.m_utilCount2_ > 64) {
        buffer.m_utilBytes2_ = append(buffer.m_utilBytes2_, buffer.m_utilBytesCount2_, (byte)69);
        buffer.m_utilBytesCount2_++;
        buffer.m_utilCount2_ -= 64;
      } 
      buffer.m_utilBytes2_ = append(buffer.m_utilBytes2_, buffer.m_utilBytesCount2_, (byte)(5 + buffer.m_utilCount2_ - 1));
      buffer.m_utilBytesCount2_++;
    } 
    buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)1);
    buffer.m_utilBytesCount1_++;
    if (doFrench) {
      doFrench(buffer);
    } else {
      if (buffer.m_utilBytes1_.length <= buffer.m_utilBytesCount1_ + buffer.m_utilBytesCount2_)
        buffer.m_utilBytes1_ = increase(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, buffer.m_utilBytesCount2_); 
      System.arraycopy(buffer.m_utilBytes2_, 0, buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, buffer.m_utilBytesCount2_);
      buffer.m_utilBytesCount1_ += buffer.m_utilBytesCount2_;
    } 
  }
  
  private static final byte[] increase(byte[] buffer, int size, int incrementsize) {
    byte[] result = new byte[buffer.length + incrementsize];
    System.arraycopy(buffer, 0, result, 0, size);
    return result;
  }
  
  private static final int[] increase(int[] buffer, int size, int incrementsize) {
    int[] result = new int[buffer.length + incrementsize];
    System.arraycopy(buffer, 0, result, 0, size);
    return result;
  }
  
  private static final void doCase(CollationBuffer buffer) {
    buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)1);
    buffer.m_utilBytesCount1_++;
    if (buffer.m_utilBytes1_.length <= buffer.m_utilBytesCount1_ + buffer.m_utilBytesCount0_)
      buffer.m_utilBytes1_ = increase(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, buffer.m_utilBytesCount0_); 
    System.arraycopy(buffer.m_utilBytes0_, 0, buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, buffer.m_utilBytesCount0_);
    buffer.m_utilBytesCount1_ += buffer.m_utilBytesCount0_;
  }
  
  private final void doTertiary(CollationBuffer buffer) {
    if (buffer.m_utilCount3_ > 0)
      if (this.m_common3_ != 5) {
        while (buffer.m_utilCount3_ >= this.m_topCount3_) {
          buffer.m_utilBytes3_ = append(buffer.m_utilBytes3_, buffer.m_utilBytesCount3_, (byte)(this.m_top3_ - this.m_topCount3_));
          buffer.m_utilBytesCount3_++;
          buffer.m_utilCount3_ -= this.m_topCount3_;
        } 
        buffer.m_utilBytes3_ = append(buffer.m_utilBytes3_, buffer.m_utilBytesCount3_, (byte)(this.m_top3_ - buffer.m_utilCount3_));
        buffer.m_utilBytesCount3_++;
      } else {
        while (buffer.m_utilCount3_ > this.m_bottomCount3_) {
          buffer.m_utilBytes3_ = append(buffer.m_utilBytes3_, buffer.m_utilBytesCount3_, (byte)(this.m_bottom3_ + this.m_bottomCount3_));
          buffer.m_utilBytesCount3_++;
          buffer.m_utilCount3_ -= this.m_bottomCount3_;
        } 
        buffer.m_utilBytes3_ = append(buffer.m_utilBytes3_, buffer.m_utilBytesCount3_, (byte)(this.m_bottom3_ + buffer.m_utilCount3_ - 1));
        buffer.m_utilBytesCount3_++;
      }  
    buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)1);
    buffer.m_utilBytesCount1_++;
    if (buffer.m_utilBytes1_.length <= buffer.m_utilBytesCount1_ + buffer.m_utilBytesCount3_)
      buffer.m_utilBytes1_ = increase(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, buffer.m_utilBytesCount3_); 
    System.arraycopy(buffer.m_utilBytes3_, 0, buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, buffer.m_utilBytesCount3_);
    buffer.m_utilBytesCount1_ += buffer.m_utilBytesCount3_;
  }
  
  private final void doQuaternary(int commonbottom4, int bottomcount4, CollationBuffer buffer) {
    if (buffer.m_utilCount4_ > 0) {
      while (buffer.m_utilCount4_ > bottomcount4) {
        buffer.m_utilBytes4_ = append(buffer.m_utilBytes4_, buffer.m_utilBytesCount4_, (byte)(commonbottom4 + bottomcount4));
        buffer.m_utilBytesCount4_++;
        buffer.m_utilCount4_ -= bottomcount4;
      } 
      buffer.m_utilBytes4_ = append(buffer.m_utilBytes4_, buffer.m_utilBytesCount4_, (byte)(commonbottom4 + buffer.m_utilCount4_ - 1));
      buffer.m_utilBytesCount4_++;
    } 
    buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)1);
    buffer.m_utilBytesCount1_++;
    if (buffer.m_utilBytes1_.length <= buffer.m_utilBytesCount1_ + buffer.m_utilBytesCount4_)
      buffer.m_utilBytes1_ = increase(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, buffer.m_utilBytesCount4_); 
    System.arraycopy(buffer.m_utilBytes4_, 0, buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, buffer.m_utilBytesCount4_);
    buffer.m_utilBytesCount1_ += buffer.m_utilBytesCount4_;
  }
  
  private static final void doIdentical(String source, CollationBuffer buffer) {
    int isize = BOCU.getCompressionLength(source);
    buffer.m_utilBytes1_ = append(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, (byte)1);
    buffer.m_utilBytesCount1_++;
    if (buffer.m_utilBytes1_.length <= buffer.m_utilBytesCount1_ + isize)
      buffer.m_utilBytes1_ = increase(buffer.m_utilBytes1_, buffer.m_utilBytesCount1_, 1 + isize); 
    buffer.m_utilBytesCount1_ = BOCU.compress(source, buffer.m_utilBytes1_, buffer.m_utilBytesCount1_);
  }
  
  private final int getFirstUnmatchedOffset(String source, String target) {
    int result = 0;
    int slength = source.length();
    int tlength = target.length();
    int minlength = slength;
    if (minlength > tlength)
      minlength = tlength; 
    while (result < minlength && source.charAt(result) == target.charAt(result))
      result++; 
    if (result > 0) {
      char schar = Character.MIN_VALUE;
      char tchar = Character.MIN_VALUE;
      if (result < minlength) {
        schar = source.charAt(result);
        tchar = target.charAt(result);
      } else {
        schar = source.charAt(minlength - 1);
        if (isUnsafe(schar)) {
          tchar = schar;
        } else {
          if (slength == tlength)
            return result; 
          if (slength < tlength) {
            tchar = target.charAt(result);
          } else {
            schar = source.charAt(result);
          } 
        } 
      } 
      if (isUnsafe(schar) || isUnsafe(tchar))
        do {
          result--;
        } while (result > 0 && isUnsafe(source.charAt(result))); 
    } 
    return result;
  }
  
  private static final byte[] append(byte[] array, int appendindex, byte value) {
    try {
      array[appendindex] = value;
    } catch (ArrayIndexOutOfBoundsException e) {
      array = increase(array, appendindex, 128);
      array[appendindex] = value;
    } 
    return array;
  }
  
  private final int compareBySortKeys(String source, String target, CollationBuffer buffer) {
    buffer.m_utilRawCollationKey_ = getRawCollationKey(source, buffer.m_utilRawCollationKey_);
    RawCollationKey targetkey = getRawCollationKey(target, (RawCollationKey)null);
    return buffer.m_utilRawCollationKey_.compareTo(targetkey);
  }
  
  private final int doPrimaryCompare(boolean doHiragana4, int lowestpvalue, String source, String target, int textoffset, CollationBuffer buffer) {
    buffer.m_srcUtilIter_.setText(source);
    buffer.m_srcUtilColEIter_.setText((UCharacterIterator)buffer.m_srcUtilIter_, textoffset);
    buffer.m_tgtUtilIter_.setText(target);
    buffer.m_tgtUtilColEIter_.setText((UCharacterIterator)buffer.m_tgtUtilIter_, textoffset);
    if (!this.m_isAlternateHandlingShifted_) {
      int hiraganaresult = 0;
      while (true) {
        int sorder = 0;
        do {
          sorder = buffer.m_srcUtilColEIter_.next();
          buffer.m_srcUtilCEBuffer_ = append(buffer.m_srcUtilCEBuffer_, buffer.m_srcUtilCEBufferSize_, sorder);
          buffer.m_srcUtilCEBufferSize_++;
          sorder &= 0xFFFF0000;
        } while (sorder == 0);
        int torder = 0;
        do {
          torder = buffer.m_tgtUtilColEIter_.next();
          buffer.m_tgtUtilCEBuffer_ = append(buffer.m_tgtUtilCEBuffer_, buffer.m_tgtUtilCEBufferSize_, torder);
          buffer.m_tgtUtilCEBufferSize_++;
          torder &= 0xFFFF0000;
        } while (torder == 0);
        if (!isContinuation(sorder) && this.m_leadBytePermutationTable_ != null) {
          sorder = this.m_leadBytePermutationTable_[((sorder >> 24) + 256) % 256] << 24 | sorder & 0xFFFFFF;
          torder = this.m_leadBytePermutationTable_[((torder >> 24) + 256) % 256] << 24 | torder & 0xFFFFFF;
        } 
        if (sorder == torder) {
          if (buffer.m_srcUtilCEBuffer_[buffer.m_srcUtilCEBufferSize_ - 1] == -1) {
            if (buffer.m_tgtUtilCEBuffer_[buffer.m_tgtUtilCEBufferSize_ - 1] != -1)
              return -1; 
            break;
          } 
          if (buffer.m_tgtUtilCEBuffer_[buffer.m_tgtUtilCEBufferSize_ - 1] == -1)
            return 1; 
          if (doHiragana4 && hiraganaresult == 0 && buffer.m_srcUtilColEIter_.m_isCodePointHiragana_ != buffer.m_tgtUtilColEIter_.m_isCodePointHiragana_) {
            if (buffer.m_srcUtilColEIter_.m_isCodePointHiragana_) {
              hiraganaresult = -1;
              continue;
            } 
            hiraganaresult = 1;
          } 
          continue;
        } 
        return endPrimaryCompare(sorder, torder, buffer);
      } 
      return hiraganaresult;
    } 
    while (true) {
      int sorder = getPrimaryShiftedCompareCE(buffer.m_srcUtilColEIter_, lowestpvalue, true, buffer);
      int torder = getPrimaryShiftedCompareCE(buffer.m_tgtUtilColEIter_, lowestpvalue, false, buffer);
      if (sorder == torder) {
        if (buffer.m_srcUtilCEBuffer_[buffer.m_srcUtilCEBufferSize_ - 1] == -1)
          break; 
        continue;
      } 
      return endPrimaryCompare(sorder, torder, buffer);
    } 
    return 0;
  }
  
  private static final int endPrimaryCompare(int sorder, int torder, CollationBuffer buffer) {
    boolean isSourceNullOrder = (buffer.m_srcUtilCEBuffer_[buffer.m_srcUtilCEBufferSize_ - 1] == -1);
    boolean isTargetNullOrder = (buffer.m_tgtUtilCEBuffer_[buffer.m_tgtUtilCEBufferSize_ - 1] == -1);
    buffer.m_srcUtilCEBufferSize_ = -1;
    buffer.m_tgtUtilCEBufferSize_ = -1;
    if (isSourceNullOrder)
      return -1; 
    if (isTargetNullOrder)
      return 1; 
    sorder >>>= 16;
    torder >>>= 16;
    if (sorder < torder)
      return -1; 
    return 1;
  }
  
  private static final int getPrimaryShiftedCompareCE(CollationElementIterator coleiter, int lowestpvalue, boolean isSrc, CollationBuffer buffer) {
    boolean shifted = false;
    int result = 0;
    int[] cebuffer = buffer.m_srcUtilCEBuffer_;
    int cebuffersize = buffer.m_srcUtilCEBufferSize_;
    if (!isSrc) {
      cebuffer = buffer.m_tgtUtilCEBuffer_;
      cebuffersize = buffer.m_tgtUtilCEBufferSize_;
    } 
    while (true) {
      result = coleiter.next();
      if (result == -1) {
        cebuffer = append(cebuffer, cebuffersize, result);
        cebuffersize++;
        break;
      } 
      if (result == 0 || (shifted && (result & 0xFFFF0000) == 0))
        continue; 
      if (isContinuation(result)) {
        if ((result & 0xFFFF0000) != 0) {
          if (shifted) {
            result = result & 0xFFFF0000 | 0xC0;
            cebuffer = append(cebuffer, cebuffersize, result);
            cebuffersize++;
            continue;
          } 
          cebuffer = append(cebuffer, cebuffersize, result);
          cebuffersize++;
          break;
        } 
        if (!shifted) {
          cebuffer = append(cebuffer, cebuffersize, result);
          cebuffersize++;
        } 
        continue;
      } 
      if (Utility.compareUnsigned(result & 0xFFFF0000, lowestpvalue) > 0) {
        cebuffer = append(cebuffer, cebuffersize, result);
        cebuffersize++;
        break;
      } 
      if ((result & 0xFFFF0000) != 0) {
        shifted = true;
        result &= 0xFFFF0000;
        cebuffer = append(cebuffer, cebuffersize, result);
        cebuffersize++;
        continue;
      } 
      cebuffer = append(cebuffer, cebuffersize, result);
      cebuffersize++;
      shifted = false;
    } 
    if (isSrc) {
      buffer.m_srcUtilCEBuffer_ = cebuffer;
      buffer.m_srcUtilCEBufferSize_ = cebuffersize;
    } else {
      buffer.m_tgtUtilCEBuffer_ = cebuffer;
      buffer.m_tgtUtilCEBufferSize_ = cebuffersize;
    } 
    result &= 0xFFFF0000;
    return result;
  }
  
  private static final int[] append(int[] array, int appendindex, int value) {
    if (appendindex + 1 >= array.length)
      array = increase(array, appendindex, 512); 
    array[appendindex] = value;
    return array;
  }
  
  private static final int doSecondaryCompare(boolean doFrench, CollationBuffer buffer) {
    if (!doFrench) {
      int soffset = 0;
      int toffset = 0;
      while (true) {
        int sorder = 0;
        while (sorder == 0)
          sorder = buffer.m_srcUtilCEBuffer_[soffset++] & 0xFF00; 
        int torder = 0;
        while (torder == 0)
          torder = buffer.m_tgtUtilCEBuffer_[toffset++] & 0xFF00; 
        if (sorder == torder) {
          if (buffer.m_srcUtilCEBuffer_[soffset - 1] == -1) {
            if (buffer.m_tgtUtilCEBuffer_[toffset - 1] != -1)
              return -1; 
            break;
          } 
          if (buffer.m_tgtUtilCEBuffer_[toffset - 1] == -1)
            return 1; 
          continue;
        } 
        if (buffer.m_srcUtilCEBuffer_[soffset - 1] == -1)
          return -1; 
        if (buffer.m_tgtUtilCEBuffer_[toffset - 1] == -1)
          return 1; 
        return (sorder < torder) ? -1 : 1;
      } 
    } else {
      buffer.m_srcUtilContOffset_ = 0;
      buffer.m_tgtUtilContOffset_ = 0;
      buffer.m_srcUtilOffset_ = buffer.m_srcUtilCEBufferSize_ - 2;
      buffer.m_tgtUtilOffset_ = buffer.m_tgtUtilCEBufferSize_ - 2;
      while (true) {
        int sorder = getSecondaryFrenchCE(true, buffer);
        int torder = getSecondaryFrenchCE(false, buffer);
        if (sorder == torder) {
          if ((buffer.m_srcUtilOffset_ < 0 && buffer.m_tgtUtilOffset_ < 0) || (buffer.m_srcUtilOffset_ >= 0 && buffer.m_srcUtilCEBuffer_[buffer.m_srcUtilOffset_] == -1))
            break; 
          continue;
        } 
        return (sorder < torder) ? -1 : 1;
      } 
    } 
    return 0;
  }
  
  private static final int getSecondaryFrenchCE(boolean isSrc, CollationBuffer buffer) {
    int result = 0;
    int offset = buffer.m_srcUtilOffset_;
    int continuationoffset = buffer.m_srcUtilContOffset_;
    int[] cebuffer = buffer.m_srcUtilCEBuffer_;
    if (!isSrc) {
      offset = buffer.m_tgtUtilOffset_;
      continuationoffset = buffer.m_tgtUtilContOffset_;
      cebuffer = buffer.m_tgtUtilCEBuffer_;
    } 
    while (result == 0 && offset >= 0) {
      if (continuationoffset == 0) {
        result = cebuffer[offset];
        while (isContinuation(cebuffer[offset--]));
        if (isContinuation(cebuffer[offset + 1])) {
          continuationoffset = offset;
          offset += 2;
        } 
      } else {
        result = cebuffer[offset++];
        if (!isContinuation(result)) {
          offset = continuationoffset;
          continuationoffset = 0;
          continue;
        } 
      } 
      result &= 0xFF00;
    } 
    if (isSrc) {
      buffer.m_srcUtilOffset_ = offset;
      buffer.m_srcUtilContOffset_ = continuationoffset;
    } else {
      buffer.m_tgtUtilOffset_ = offset;
      buffer.m_tgtUtilContOffset_ = continuationoffset;
    } 
    return result;
  }
  
  private final int doCaseCompare(CollationBuffer buffer) {
    int soffset = 0;
    int toffset = 0;
    while (true) {
      int sorder = 0;
      int torder = 0;
      while ((sorder & 0x3F) == 0) {
        sorder = buffer.m_srcUtilCEBuffer_[soffset++];
        if (!isContinuation(sorder) && ((sorder & 0xFFFF0000) != 0 || buffer.m_utilCompare2_ == true)) {
          sorder &= 0xFF;
          sorder ^= this.m_caseSwitch_;
          continue;
        } 
        sorder = 0;
      } 
      while ((torder & 0x3F) == 0) {
        torder = buffer.m_tgtUtilCEBuffer_[toffset++];
        if (!isContinuation(torder) && ((torder & 0xFFFF0000) != 0 || buffer.m_utilCompare2_ == true)) {
          torder &= 0xFF;
          torder ^= this.m_caseSwitch_;
          continue;
        } 
        torder = 0;
      } 
      sorder &= 0xC0;
      torder &= 0xC0;
      if (sorder == torder) {
        if (buffer.m_srcUtilCEBuffer_[soffset - 1] == -1) {
          if (buffer.m_tgtUtilCEBuffer_[toffset - 1] != -1)
            return -1; 
          break;
        } 
        if (buffer.m_tgtUtilCEBuffer_[toffset - 1] == -1)
          return 1; 
        continue;
      } 
      if (buffer.m_srcUtilCEBuffer_[soffset - 1] == -1)
        return -1; 
      if (buffer.m_tgtUtilCEBuffer_[soffset - 1] == -1)
        return 1; 
      return (sorder < torder) ? -1 : 1;
    } 
    return 0;
  }
  
  private final int doTertiaryCompare(CollationBuffer buffer) {
    int soffset = 0;
    int toffset = 0;
    while (true) {
      int sorder = 0;
      int torder = 0;
      while ((sorder & 0x3F) == 0) {
        sorder = buffer.m_srcUtilCEBuffer_[soffset++] & this.m_mask3_;
        if (!isContinuation(sorder)) {
          sorder ^= this.m_caseSwitch_;
          continue;
        } 
        sorder &= 0x3F;
      } 
      while ((torder & 0x3F) == 0) {
        torder = buffer.m_tgtUtilCEBuffer_[toffset++] & this.m_mask3_;
        if (!isContinuation(torder)) {
          torder ^= this.m_caseSwitch_;
          continue;
        } 
        torder &= 0x3F;
      } 
      if (sorder == torder) {
        if (buffer.m_srcUtilCEBuffer_[soffset - 1] == -1) {
          if (buffer.m_tgtUtilCEBuffer_[toffset - 1] != -1)
            return -1; 
          break;
        } 
        if (buffer.m_tgtUtilCEBuffer_[toffset - 1] == -1)
          return 1; 
        continue;
      } 
      if (buffer.m_srcUtilCEBuffer_[soffset - 1] == -1)
        return -1; 
      if (buffer.m_tgtUtilCEBuffer_[toffset - 1] == -1)
        return 1; 
      return (sorder < torder) ? -1 : 1;
    } 
    return 0;
  }
  
  private final int doQuaternaryCompare(int lowestpvalue, CollationBuffer buffer) {
    boolean sShifted = true;
    boolean tShifted = true;
    int soffset = 0;
    int toffset = 0;
    while (true) {
      int sorder = 0;
      int torder = 0;
      while (sorder == 0 || (isContinuation(sorder) && !sShifted)) {
        sorder = buffer.m_srcUtilCEBuffer_[soffset++];
        if (isContinuation(sorder)) {
          if (!sShifted);
          continue;
        } 
        if (Utility.compareUnsigned(sorder, lowestpvalue) > 0 || (sorder & 0xFFFF0000) == 0) {
          sorder = -65536;
          sShifted = false;
          continue;
        } 
        sShifted = true;
      } 
      sorder >>>= 16;
      while (torder == 0 || (isContinuation(torder) && !tShifted)) {
        torder = buffer.m_tgtUtilCEBuffer_[toffset++];
        if (isContinuation(torder)) {
          if (!tShifted);
          continue;
        } 
        if (Utility.compareUnsigned(torder, lowestpvalue) > 0 || (torder & 0xFFFF0000) == 0) {
          torder = -65536;
          tShifted = false;
          continue;
        } 
        tShifted = true;
      } 
      torder >>>= 16;
      if (sorder == torder) {
        if (buffer.m_srcUtilCEBuffer_[soffset - 1] == -1) {
          if (buffer.m_tgtUtilCEBuffer_[toffset - 1] != -1)
            return -1; 
          break;
        } 
        if (buffer.m_tgtUtilCEBuffer_[toffset - 1] == -1)
          return 1; 
        continue;
      } 
      if (buffer.m_srcUtilCEBuffer_[soffset - 1] == -1)
        return -1; 
      if (buffer.m_tgtUtilCEBuffer_[toffset - 1] == -1)
        return 1; 
      return (sorder < torder) ? -1 : 1;
    } 
    return 0;
  }
  
  private static final int doIdenticalCompare(String source, String target, int offset, boolean normalize) {
    if (normalize) {
      if (Normalizer.quickCheck(source, Normalizer.NFD, 0) != Normalizer.YES)
        source = Normalizer.decompose(source, false); 
      if (Normalizer.quickCheck(target, Normalizer.NFD, 0) != Normalizer.YES)
        target = Normalizer.decompose(target, false); 
      offset = 0;
    } 
    return doStringCompare(source, target, offset);
  }
  
  private static final int doStringCompare(String source, String target, int offset) {
    char schar = Character.MIN_VALUE;
    char tchar = Character.MIN_VALUE;
    int slength = source.length();
    int tlength = target.length();
    int minlength = Math.min(slength, tlength);
    while (offset < minlength) {
      schar = source.charAt(offset);
      tchar = target.charAt(offset++);
      if (schar != tchar)
        break; 
    } 
    if (schar == tchar && offset == minlength) {
      if (slength > minlength)
        return 1; 
      if (tlength > minlength)
        return -1; 
      return 0;
    } 
    if (schar >= '?' && tchar >= '?') {
      schar = fixupUTF16(schar);
      tchar = fixupUTF16(tchar);
    } 
    return (schar < tchar) ? -1 : 1;
  }
  
  private static final char fixupUTF16(char ch) {
    if (ch >= '') {
      ch = (char)(ch - 2048);
    } else {
      ch = (char)(ch + 8192);
    } 
    return ch;
  }
  
  private void buildPermutationTable() {
    if (this.m_reorderCodes_ == null || this.m_reorderCodes_.length == 0 || (this.m_reorderCodes_.length == 1 && this.m_reorderCodes_[0] == 103)) {
      this.m_leadBytePermutationTable_ = null;
      return;
    } 
    if (this.m_reorderCodes_[0] == -1) {
      if (this.m_reorderCodes_.length != 1)
        throw new IllegalArgumentException("Illegal collation reorder codes - default reorder code must be the only code in the list."); 
      if (this.m_defaultReorderCodes_ == null || this.m_defaultReorderCodes_.length == 0) {
        this.m_leadBytePermutationTable_ = null;
        return;
      } 
      this.m_reorderCodes_ = (int[])this.m_defaultReorderCodes_.clone();
    } 
    int toBottom = 3;
    int toTop = 228;
    boolean[] permutationSlotFilled = new boolean[256];
    boolean[] newLeadByteUsed = new boolean[256];
    if (this.m_leadBytePermutationTable_ == null)
      this.m_leadBytePermutationTable_ = new byte[256]; 
    int[] internalReorderCodes = new int[this.m_reorderCodes_.length + 5];
    int codeIndex;
    for (codeIndex = 0; codeIndex < 5; codeIndex++)
      internalReorderCodes[codeIndex] = 4096 + codeIndex; 
    for (codeIndex = 0; codeIndex < this.m_reorderCodes_.length; codeIndex++) {
      internalReorderCodes[codeIndex + 5] = this.m_reorderCodes_[codeIndex];
      if (this.m_reorderCodes_[codeIndex] >= 4096 && this.m_reorderCodes_[codeIndex] < 4101)
        internalReorderCodes[this.m_reorderCodes_[codeIndex] - 4096] = 4102; 
    } 
    boolean fromTheBottom = true;
    int reorderCodesIndex = -1;
    for (int reorderCodesCount = 0; reorderCodesCount < internalReorderCodes.length; reorderCodesCount++) {
      reorderCodesIndex += fromTheBottom ? 1 : -1;
      int next = internalReorderCodes[reorderCodesIndex];
      if (next != 4102)
        if (next == 103) {
          if (!fromTheBottom) {
            this.m_leadBytePermutationTable_ = null;
            throw new IllegalArgumentException("Illegal collation reorder codes - two \"from the end\" markers.");
          } 
          fromTheBottom = false;
          reorderCodesIndex = internalReorderCodes.length;
        } else {
          int[] leadBytes = LEADBYTE_CONSTANTS_.getLeadBytesForReorderCode(next);
          if (fromTheBottom) {
            for (int leadByte : leadBytes) {
              if (permutationSlotFilled[leadByte]) {
                this.m_leadBytePermutationTable_ = null;
                throw new IllegalArgumentException("Illegal reorder codes specified - multiple codes with the same lead byte.");
              } 
              this.m_leadBytePermutationTable_[leadByte] = (byte)toBottom;
              newLeadByteUsed[toBottom] = true;
              permutationSlotFilled[leadByte] = true;
              toBottom++;
            } 
          } else {
            for (int leadByteIndex = leadBytes.length - 1; leadByteIndex >= 0; leadByteIndex--) {
              int leadByte = leadBytes[leadByteIndex];
              if (permutationSlotFilled[leadByte]) {
                this.m_leadBytePermutationTable_ = null;
                throw new IllegalArgumentException("Illegal reorder codes specified - multiple codes with the same lead byte.");
              } 
              this.m_leadBytePermutationTable_[leadByte] = (byte)toTop;
              newLeadByteUsed[toTop] = true;
              permutationSlotFilled[leadByte] = true;
              toTop--;
            } 
          } 
        }  
    } 
    int reorderCode = 0;
    for (int i = 0; i < 256; i++) {
      if (!permutationSlotFilled[i]) {
        while (newLeadByteUsed[reorderCode]) {
          if (reorderCode > 255)
            throw new IllegalArgumentException("Unable to fill collation reordering table slots - no available reordering code."); 
          reorderCode++;
        } 
        this.m_leadBytePermutationTable_[i] = (byte)reorderCode;
        permutationSlotFilled[i] = true;
        newLeadByteUsed[reorderCode] = true;
      } 
    } 
    this.latinOneRegenTable_ = true;
    updateInternalState();
  }
  
  private void updateInternalState() {
    if (this.m_caseFirst_ == 25) {
      this.m_caseSwitch_ = 192;
    } else {
      this.m_caseSwitch_ = 0;
    } 
    if (this.m_isCaseLevel_ || this.m_caseFirst_ == 16) {
      this.m_mask3_ = 63;
      this.m_common3_ = 5;
      this.m_addition3_ = 128;
      this.m_top3_ = 133;
      this.m_bottom3_ = 5;
    } else {
      this.m_mask3_ = 255;
      this.m_addition3_ = 64;
      if (this.m_caseFirst_ == 25) {
        this.m_common3_ = 197;
        this.m_top3_ = 197;
        this.m_bottom3_ = 134;
      } else {
        this.m_common3_ = 5;
        this.m_top3_ = 69;
        this.m_bottom3_ = 5;
      } 
    } 
    int total3 = this.m_top3_ - this.m_bottom3_ - 1;
    this.m_topCount3_ = (int)(0.667D * total3);
    this.m_bottomCount3_ = total3 - this.m_topCount3_;
    if (!this.m_isCaseLevel_ && getStrength() == 2 && !this.m_isFrenchCollation_ && !this.m_isAlternateHandlingShifted_) {
      this.m_isSimple3_ = true;
    } else {
      this.m_isSimple3_ = false;
    } 
    if (!this.m_isCaseLevel_ && getStrength() <= 2 && !this.m_isNumericCollation_ && !this.m_isAlternateHandlingShifted_ && !this.latinOneFailed_) {
      if (this.latinOneCEs_ == null || this.latinOneRegenTable_) {
        if (setUpLatinOne()) {
          this.latinOneUse_ = true;
        } else {
          this.latinOneUse_ = false;
          this.latinOneFailed_ = true;
        } 
        this.latinOneRegenTable_ = false;
      } else {
        this.latinOneUse_ = true;
      } 
    } else {
      this.latinOneUse_ = false;
    } 
  }
  
  private final void init() {
    for (this.m_minUnsafe_ = Character.MIN_VALUE; this.m_minUnsafe_ < '̀'; this.m_minUnsafe_ = (char)(this.m_minUnsafe_ + 1)) {
      if (isUnsafe(this.m_minUnsafe_))
        break; 
    } 
    for (this.m_minContractionEnd_ = Character.MIN_VALUE; this.m_minContractionEnd_ < '̀'; this.m_minContractionEnd_ = (char)(this.m_minContractionEnd_ + 1)) {
      if (isContractionEnd(this.m_minContractionEnd_))
        break; 
    } 
    this.latinOneFailed_ = true;
    setStrength(this.m_defaultStrength_);
    setDecomposition(this.m_defaultDecomposition_);
    this.m_variableTopValue_ = this.m_defaultVariableTopValue_;
    this.m_isFrenchCollation_ = this.m_defaultIsFrenchCollation_;
    this.m_isAlternateHandlingShifted_ = this.m_defaultIsAlternateHandlingShifted_;
    this.m_isCaseLevel_ = this.m_defaultIsCaseLevel_;
    this.m_caseFirst_ = this.m_defaultCaseFirst_;
    this.m_isHiragana4_ = this.m_defaultIsHiragana4_;
    this.m_isNumericCollation_ = this.m_defaultIsNumericCollation_;
    this.latinOneFailed_ = false;
    if (this.m_defaultReorderCodes_ != null) {
      this.m_reorderCodes_ = (int[])this.m_defaultReorderCodes_.clone();
    } else {
      this.m_reorderCodes_ = null;
    } 
    updateInternalState();
  }
  
  private static class shiftValues {
    private shiftValues() {}
    
    int primShift = 24;
    
    int secShift = 24;
    
    int terShift = 24;
  }
  
  private final void addLatinOneEntry(char ch, int CE, shiftValues sh) {
    int primary1 = 0, primary2 = 0, secondary = 0, tertiary = 0;
    boolean continuation = isContinuation(CE);
    boolean reverseSecondary = false;
    if (!continuation) {
      tertiary = CE & this.m_mask3_;
      tertiary ^= this.m_caseSwitch_;
      reverseSecondary = true;
    } else {
      tertiary = (byte)(CE & 0xFFFFFF3F);
      tertiary &= 0x3F;
      reverseSecondary = false;
    } 
    secondary = (CE >>>= 8) & 0xFF;
    primary2 = (CE >>>= 8) & 0xFF;
    primary1 = CE >>> 8;
    if (primary1 != 0) {
      if (this.m_leadBytePermutationTable_ != null && !continuation)
        primary1 = this.m_leadBytePermutationTable_[primary1]; 
      this.latinOneCEs_[ch] = this.latinOneCEs_[ch] | primary1 << sh.primShift;
      sh.primShift -= 8;
    } 
    if (primary2 != 0) {
      if (sh.primShift < 0) {
        this.latinOneCEs_[ch] = -16777216;
        this.latinOneCEs_[this.latinOneTableLen_ + ch] = -16777216;
        this.latinOneCEs_[2 * this.latinOneTableLen_ + ch] = -16777216;
        return;
      } 
      this.latinOneCEs_[ch] = this.latinOneCEs_[ch] | primary2 << sh.primShift;
      sh.primShift -= 8;
    } 
    if (secondary != 0) {
      if (reverseSecondary && this.m_isFrenchCollation_) {
        this.latinOneCEs_[this.latinOneTableLen_ + ch] = this.latinOneCEs_[this.latinOneTableLen_ + ch] >>> 8;
        this.latinOneCEs_[this.latinOneTableLen_ + ch] = this.latinOneCEs_[this.latinOneTableLen_ + ch] | secondary << 24;
      } else {
        this.latinOneCEs_[this.latinOneTableLen_ + ch] = this.latinOneCEs_[this.latinOneTableLen_ + ch] | secondary << sh.secShift;
      } 
      sh.secShift -= 8;
    } 
    if (tertiary != 0) {
      this.latinOneCEs_[2 * this.latinOneTableLen_ + ch] = this.latinOneCEs_[2 * this.latinOneTableLen_ + ch] | tertiary << sh.terShift;
      sh.terShift -= 8;
    } 
  }
  
  private final void resizeLatinOneTable(int newSize) {
    int[] newTable = new int[3 * newSize];
    int sizeToCopy = (newSize < this.latinOneTableLen_) ? newSize : this.latinOneTableLen_;
    System.arraycopy(this.latinOneCEs_, 0, newTable, 0, sizeToCopy);
    System.arraycopy(this.latinOneCEs_, this.latinOneTableLen_, newTable, newSize, sizeToCopy);
    System.arraycopy(this.latinOneCEs_, 2 * this.latinOneTableLen_, newTable, 2 * newSize, sizeToCopy);
    this.latinOneTableLen_ = newSize;
    this.latinOneCEs_ = newTable;
  }
  
  private final boolean setUpLatinOne() {
    if (this.latinOneCEs_ == null || this.m_reallocLatinOneCEs_) {
      this.latinOneCEs_ = new int[915];
      this.latinOneTableLen_ = 305;
      this.m_reallocLatinOneCEs_ = false;
    } else {
      Arrays.fill(this.latinOneCEs_, 0);
    } 
    if (this.m_ContInfo_ == null)
      this.m_ContInfo_ = new ContractionInfo(); 
    char ch = Character.MIN_VALUE;
    CollationElementIterator it = getCollationElementIterator("");
    shiftValues s = new shiftValues();
    int CE = 0;
    char contractionOffset = 'Ā';
    for (ch = Character.MIN_VALUE; ch <= 'ÿ'; ch = (char)(ch + 1)) {
      s.primShift = 24;
      s.secShift = 24;
      s.terShift = 24;
      if (ch < 'Ā') {
        CE = this.m_trie_.getLatin1LinearValue(ch);
      } else {
        CE = this.m_trie_.getLeadValue(ch);
        if (CE == -268435456)
          CE = UCA_.m_trie_.getLeadValue(ch); 
      } 
      if (!isSpecial(CE)) {
        addLatinOneEntry(ch, CE, s);
      } else {
        int UCharOffset;
        switch (getTag(CE)) {
          case 1:
          case 13:
            it.setText(UCharacter.toString(ch));
            while ((CE = it.next()) != -1) {
              if (s.primShift < 0 || s.secShift < 0 || s.terShift < 0) {
                this.latinOneCEs_[ch] = -16777216;
                this.latinOneCEs_[this.latinOneTableLen_ + ch] = -16777216;
                this.latinOneCEs_[2 * this.latinOneTableLen_ + ch] = -16777216;
                break;
              } 
              addLatinOneEntry(ch, CE, s);
            } 
            break;
          case 2:
            if ((CE & 0xFFF000) != 0) {
              this.latinOneFailed_ = true;
              return false;
            } 
            UCharOffset = (CE & 0xFFFFFF) - this.m_contractionOffset_;
            CE |= (contractionOffset & 0xFFF) << 12;
            this.latinOneCEs_[ch] = CE;
            this.latinOneCEs_[this.latinOneTableLen_ + ch] = CE;
            this.latinOneCEs_[2 * this.latinOneTableLen_ + ch] = CE;
            do {
              CE = this.m_contractionCE_[UCharOffset];
              if (isSpecial(CE) && getTag(CE) == 1) {
                int offset = ((CE & 0xFFFFF0) >> 4) - this.m_expansionOffset_;
                int size = CE & 0xF;
                if (size != 0) {
                  for (int i = 0; i < size; i++) {
                    if (s.primShift < 0 || s.secShift < 0 || s.terShift < 0) {
                      this.latinOneCEs_[contractionOffset] = -16777216;
                      this.latinOneCEs_[this.latinOneTableLen_ + contractionOffset] = -16777216;
                      this.latinOneCEs_[2 * this.latinOneTableLen_ + contractionOffset] = -16777216;
                      break;
                    } 
                    addLatinOneEntry(contractionOffset, this.m_expansion_[offset + i], s);
                  } 
                } else {
                  while (this.m_expansion_[offset] != 0) {
                    if (s.primShift < 0 || s.secShift < 0 || s.terShift < 0) {
                      this.latinOneCEs_[contractionOffset] = -16777216;
                      this.latinOneCEs_[this.latinOneTableLen_ + contractionOffset] = -16777216;
                      this.latinOneCEs_[2 * this.latinOneTableLen_ + contractionOffset] = -16777216;
                      break;
                    } 
                    addLatinOneEntry(contractionOffset, this.m_expansion_[offset++], s);
                  } 
                } 
                contractionOffset = (char)(contractionOffset + 1);
              } else if (!isSpecial(CE)) {
                contractionOffset = (char)(contractionOffset + 1);
                addLatinOneEntry(contractionOffset, CE, s);
              } else {
                this.latinOneCEs_[contractionOffset] = -16777216;
                this.latinOneCEs_[this.latinOneTableLen_ + contractionOffset] = -16777216;
                this.latinOneCEs_[2 * this.latinOneTableLen_ + contractionOffset] = -16777216;
                contractionOffset = (char)(contractionOffset + 1);
              } 
              UCharOffset++;
              s.primShift = 24;
              s.secShift = 24;
              s.terShift = 24;
              if (contractionOffset != this.latinOneTableLen_)
                continue; 
              resizeLatinOneTable(2 * this.latinOneTableLen_);
            } while (this.m_contractionIndex_[UCharOffset] != Character.MAX_VALUE);
            break;
          case 11:
            if (ch == '·') {
              addLatinOneEntry(ch, CE, s);
              break;
            } 
            this.latinOneFailed_ = true;
            return false;
          default:
            this.latinOneFailed_ = true;
            return false;
        } 
      } 
    } 
    if (contractionOffset < this.latinOneTableLen_)
      resizeLatinOneTable(contractionOffset); 
    return true;
  }
  
  private static class ContractionInfo {
    int index;
    
    private ContractionInfo() {}
  }
  
  private int getLatinOneContraction(int strength, int CE, String s) {
    int len = s.length();
    int UCharOffset = (CE & 0xFFF) - this.m_contractionOffset_;
    int offset = 1;
    int latinOneOffset = (CE & 0xFFF000) >>> 12;
    char schar = Character.MIN_VALUE, tchar = Character.MIN_VALUE;
    while (true) {
      if (this.m_ContInfo_.index == len)
        return this.latinOneCEs_[strength * this.latinOneTableLen_ + latinOneOffset]; 
      schar = s.charAt(this.m_ContInfo_.index);
      while (schar > (tchar = this.m_contractionIndex_[UCharOffset + offset]))
        offset++; 
      if (schar == tchar) {
        this.m_ContInfo_.index++;
        return this.latinOneCEs_[strength * this.latinOneTableLen_ + latinOneOffset + offset];
      } 
      if (schar > 'ÿ')
        return -16777216; 
      int isZeroCE = this.m_trie_.getLeadValue(schar);
      if (isZeroCE == 0) {
        this.m_ContInfo_.index++;
        continue;
      } 
      break;
    } 
    return this.latinOneCEs_[strength * this.latinOneTableLen_ + latinOneOffset];
  }
  
  private final int compareUseLatin1(String source, String target, int startOffset, CollationBuffer buffer) {
    int sLen = source.length();
    int tLen = target.length();
    int strength = getStrength();
    int sIndex = startOffset, tIndex = startOffset;
    char sChar = Character.MIN_VALUE, tChar = Character.MIN_VALUE;
    int sOrder = 0, tOrder = 0;
    boolean endOfSource = false;
    boolean haveContractions = false;
    int offset = this.latinOneTableLen_;
    while (true) {
      if (sOrder == 0)
        if (sIndex == sLen) {
          endOfSource = true;
        } else {
          sChar = source.charAt(sIndex++);
          if (sChar > 'ÿ')
            return compareRegular(source, target, startOffset, buffer); 
          sOrder = this.latinOneCEs_[sChar];
          if (isSpecial(sOrder)) {
            if (getTag(sOrder) == 2) {
              this.m_ContInfo_.index = sIndex;
              sOrder = getLatinOneContraction(0, sOrder, source);
              sIndex = this.m_ContInfo_.index;
              haveContractions = true;
            } 
            if (isSpecial(sOrder))
              return compareRegular(source, target, startOffset, buffer); 
          } 
          continue;
        }  
      while (tOrder == 0) {
        if (tIndex == tLen) {
          if (endOfSource)
            break; 
          return 1;
        } 
        tChar = target.charAt(tIndex++);
        if (tChar > 'ÿ')
          return compareRegular(source, target, startOffset, buffer); 
        tOrder = this.latinOneCEs_[tChar];
        if (isSpecial(tOrder)) {
          if (getTag(tOrder) == 2) {
            this.m_ContInfo_.index = tIndex;
            tOrder = getLatinOneContraction(0, tOrder, target);
            tIndex = this.m_ContInfo_.index;
            haveContractions = true;
          } 
          if (isSpecial(tOrder))
            return compareRegular(source, target, startOffset, buffer); 
        } 
      } 
      if (endOfSource)
        return -1; 
      if (sOrder == tOrder) {
        sOrder = 0;
        tOrder = 0;
        continue;
      } 
      if (((sOrder ^ tOrder) & 0xFF000000) != 0) {
        if (sOrder >>> 8 < tOrder >>> 8)
          return -1; 
        return 1;
      } 
      sOrder <<= 8;
      tOrder <<= 8;
    } 
    if (strength >= 1) {
      endOfSource = false;
      if (!this.m_isFrenchCollation_) {
        sIndex = startOffset;
        tIndex = startOffset;
        while (true) {
          if (sOrder == 0)
            if (sIndex == sLen) {
              endOfSource = true;
            } else {
              sChar = source.charAt(sIndex++);
              sOrder = this.latinOneCEs_[offset + sChar];
              if (isSpecial(sOrder)) {
                this.m_ContInfo_.index = sIndex;
                sOrder = getLatinOneContraction(1, sOrder, source);
                sIndex = this.m_ContInfo_.index;
              } 
              continue;
            }  
          while (tOrder == 0) {
            if (tIndex == tLen) {
              if (endOfSource)
                // Byte code: goto -> 799 
              return 1;
            } 
            tChar = target.charAt(tIndex++);
            tOrder = this.latinOneCEs_[offset + tChar];
            if (isSpecial(tOrder)) {
              this.m_ContInfo_.index = tIndex;
              tOrder = getLatinOneContraction(1, tOrder, target);
              tIndex = this.m_ContInfo_.index;
            } 
          } 
          if (endOfSource)
            return -1; 
          if (sOrder == tOrder) {
            sOrder = 0;
            tOrder = 0;
            continue;
          } 
          if (((sOrder ^ tOrder) & 0xFF000000) != 0) {
            if (sOrder >>> 8 < tOrder >>> 8)
              return -1; 
            return 1;
          } 
          sOrder <<= 8;
          tOrder <<= 8;
        } 
      } else {
        if (haveContractions)
          return compareRegular(source, target, startOffset, buffer); 
        sIndex = sLen;
        tIndex = tLen;
        while (true) {
          if (sOrder == 0)
            if (sIndex == startOffset) {
              endOfSource = true;
            } else {
              sChar = source.charAt(--sIndex);
              sOrder = this.latinOneCEs_[offset + sChar];
              continue;
            }  
          while (tOrder == 0) {
            if (tIndex == startOffset) {
              if (endOfSource)
                // Byte code: goto -> 799 
              return 1;
            } 
            tChar = target.charAt(--tIndex);
            tOrder = this.latinOneCEs_[offset + tChar];
          } 
          if (endOfSource)
            return -1; 
          if (sOrder == tOrder) {
            sOrder = 0;
            tOrder = 0;
            continue;
          } 
          if (((sOrder ^ tOrder) & 0xFF000000) != 0) {
            if (sOrder >>> 8 < tOrder >>> 8)
              return -1; 
            return 1;
          } 
          sOrder <<= 8;
          tOrder <<= 8;
        } 
      } 
    } 
    if (strength >= 2) {
      offset += this.latinOneTableLen_;
      sIndex = startOffset;
      tIndex = startOffset;
      endOfSource = false;
      while (true) {
        if (sOrder == 0)
          if (sIndex == sLen) {
            endOfSource = true;
          } else {
            sChar = source.charAt(sIndex++);
            sOrder = this.latinOneCEs_[offset + sChar];
            if (isSpecial(sOrder)) {
              this.m_ContInfo_.index = sIndex;
              sOrder = getLatinOneContraction(2, sOrder, source);
              sIndex = this.m_ContInfo_.index;
            } 
            continue;
          }  
        while (tOrder == 0) {
          if (tIndex == tLen) {
            if (endOfSource)
              return 0; 
            return 1;
          } 
          tChar = target.charAt(tIndex++);
          tOrder = this.latinOneCEs_[offset + tChar];
          if (isSpecial(tOrder)) {
            this.m_ContInfo_.index = tIndex;
            tOrder = getLatinOneContraction(2, tOrder, target);
            tIndex = this.m_ContInfo_.index;
          } 
        } 
        if (endOfSource)
          return -1; 
        if (sOrder == tOrder) {
          sOrder = 0;
          tOrder = 0;
          continue;
        } 
        if (((sOrder ^ tOrder) & 0xFF000000) != 0) {
          if (sOrder >>> 8 < tOrder >>> 8)
            return -1; 
          return 1;
        } 
        sOrder <<= 8;
        tOrder <<= 8;
      } 
    } 
    return 0;
  }
  
  public VersionInfo getVersion() {
    int rtVersion = VersionInfo.UCOL_RUNTIME_VERSION.getMajor();
    int bdVersion = this.m_version_.getMajor();
    int csVersion = 0;
    int cmbVersion = (rtVersion << 11 | bdVersion << 6 | csVersion) & 0xFFFF;
    return VersionInfo.getInstance(cmbVersion >> 8, cmbVersion & 0xFF, this.m_version_.getMinor(), UCA_.m_UCA_version_.getMajor());
  }
  
  public VersionInfo getUCAVersion() {
    return UCA_.m_UCA_version_;
  }
  
  private final CollationBuffer getCollationBuffer() {
    if (isFrozen())
      this.frozenLock.lock(); 
    if (this.collationBuffer == null) {
      this.collationBuffer = new CollationBuffer();
    } else {
      this.collationBuffer.resetBuffers();
    } 
    return this.collationBuffer;
  }
  
  private final void releaseCollationBuffer(CollationBuffer buffer) {
    if (isFrozen())
      this.frozenLock.unlock(); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\RuleBasedCollator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */