package com.ibm.icu.text;

import com.ibm.icu.impl.IntTrieBuilder;
import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.impl.Normalizer2Impl;
import com.ibm.icu.impl.Trie;
import com.ibm.icu.impl.TrieBuilder;
import com.ibm.icu.impl.TrieIterator;
import com.ibm.icu.impl.UCharacterProperty;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.RangeValueIterator;
import com.ibm.icu.util.VersionInfo;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class CollationParsedRuleBuilder {
  static final InverseUCA INVERSE_UCA_;
  
  private static final String INV_UCA_VERSION_MISMATCH_ = "UCA versions of UCA and inverse UCA should match";
  
  private static final String UCA_NOT_INSTANTIATED_ = "UCA is not instantiated!";
  
  private static final int CE_BASIC_STRENGTH_LIMIT_ = 3;
  
  private static final int CE_STRENGTH_LIMIT_ = 16;
  
  CollationParsedRuleBuilder(String rules) throws ParseException {
    this.m_utilGens_ = new CEGenerator[] { new CEGenerator(), new CEGenerator(), new CEGenerator() };
    this.m_utilCEBuffer_ = new int[3];
    this.m_utilIntBuffer_ = new int[16];
    this.m_utilElement_ = new Elements();
    this.m_utilElement2_ = new Elements();
    this.m_utilToken_ = new CollationRuleParser.Token();
    this.m_utilCountBuffer_ = new int[6];
    this.m_utilLongBuffer_ = new long[5];
    this.m_utilLowerWeightRange_ = new WeightRange[] { new WeightRange(), new WeightRange(), new WeightRange(), new WeightRange(), new WeightRange() };
    this.m_utilUpperWeightRange_ = new WeightRange[] { new WeightRange(), new WeightRange(), new WeightRange(), new WeightRange(), new WeightRange() };
    this.m_utilWeightRange_ = new WeightRange();
    this.m_nfcImpl_ = (Norm2AllModes.getNFCInstance()).impl;
    this.m_utilCanIter_ = new CanonicalIterator("");
    this.m_utilStringBuffer_ = new StringBuilder("");
    this.m_parser_ = new CollationRuleParser(rules);
    this.m_parser_.assembleTokenList();
    this.m_utilColEIter_ = RuleBasedCollator.UCA_.getCollationElementIterator("");
  }
  
  static class InverseUCA {
    int[] m_table_;
    
    char[] m_continuations_;
    
    VersionInfo m_UCA_version_;
    
    final int getInversePrevCE(int ce, int contce, int strength, int[] prevresult) {
      int result = findInverseCE(ce, contce);
      if (result < 0) {
        prevresult[0] = -1;
        return -1;
      } 
      ce &= CollationParsedRuleBuilder.STRENGTH_MASK_[strength];
      contce &= CollationParsedRuleBuilder.STRENGTH_MASK_[strength];
      prevresult[0] = ce;
      prevresult[1] = contce;
      while ((prevresult[0] & CollationParsedRuleBuilder.STRENGTH_MASK_[strength]) == ce && (prevresult[1] & CollationParsedRuleBuilder.STRENGTH_MASK_[strength]) == contce && result > 0) {
        prevresult[0] = this.m_table_[3 * --result];
        prevresult[1] = this.m_table_[3 * result + 1];
      } 
      return result;
    }
    
    final int getCEStrengthDifference(int CE, int contCE, int prevCE, int prevContCE) {
      int strength = 2;
      while (((prevCE & CollationParsedRuleBuilder.STRENGTH_MASK_[strength]) != (CE & CollationParsedRuleBuilder.STRENGTH_MASK_[strength]) || (prevContCE & CollationParsedRuleBuilder.STRENGTH_MASK_[strength]) != (contCE & CollationParsedRuleBuilder.STRENGTH_MASK_[strength])) && strength != 0)
        strength--; 
      return strength;
    }
    
    private int compareCEs(int source0, int source1, int target0, int target1) {
      int s2, t2, s1 = source0, t1 = target0;
      if (RuleBasedCollator.isContinuation(source1)) {
        s2 = source1;
      } else {
        s2 = 0;
      } 
      if (RuleBasedCollator.isContinuation(target1)) {
        t2 = target1;
      } else {
        t2 = 0;
      } 
      int s = 0, t = 0;
      if (s1 == t1 && s2 == t2)
        return 0; 
      s = s1 & 0xFFFF0000 | (s2 & 0xFFFF0000) >>> 16;
      t = t1 & 0xFFFF0000 | (t2 & 0xFFFF0000) >>> 16;
      if (s == t) {
        s = s1 & 0xFF00 | (s2 & 0xFF00) >> 8;
        t = t1 & 0xFF00 | (t2 & 0xFF00) >> 8;
        if (s == t) {
          s = (s1 & 0xFF) << 8 | s2 & 0xFF;
          t = (t1 & 0xFF) << 8 | t2 & 0xFF;
          return Utility.compareUnsigned(s, t);
        } 
        return Utility.compareUnsigned(s, t);
      } 
      return Utility.compareUnsigned(s, t);
    }
    
    int findInverseCE(int ce, int contce) {
      int bottom = 0;
      int top = this.m_table_.length / 3;
      int result = 0;
      while (bottom < top - 1) {
        result = top + bottom >> 1;
        int first = this.m_table_[3 * result];
        int second = this.m_table_[3 * result + 1];
        int comparison = compareCEs(first, second, ce, contce);
        if (comparison > 0) {
          top = result;
          continue;
        } 
        if (comparison < 0)
          bottom = result; 
      } 
      return result;
    }
    
    void getInverseGapPositions(CollationRuleParser.TokenListHeader listheader) throws Exception {
      CollationRuleParser.Token token = listheader.m_first_;
      int tokenstrength = token.m_strength_;
      for (int i = 0; i < 3; i++) {
        listheader.m_gapsHi_[3 * i] = 0;
        listheader.m_gapsHi_[3 * i + 1] = 0;
        listheader.m_gapsHi_[3 * i + 2] = 0;
        listheader.m_gapsLo_[3 * i] = 0;
        listheader.m_gapsLo_[3 * i + 1] = 0;
        listheader.m_gapsLo_[3 * i + 2] = 0;
        listheader.m_numStr_[i] = 0;
        listheader.m_fStrToken_[i] = null;
        listheader.m_lStrToken_[i] = null;
        listheader.m_pos_[i] = -1;
      } 
      if (listheader.m_baseCE_ >>> 24 >= RuleBasedCollator.UCA_CONSTANTS_.PRIMARY_IMPLICIT_MIN_ && listheader.m_baseCE_ >>> 24 <= RuleBasedCollator.UCA_CONSTANTS_.PRIMARY_IMPLICIT_MAX_) {
        listheader.m_pos_[0] = 0;
        int t1 = listheader.m_baseCE_;
        int t2 = listheader.m_baseContCE_;
        listheader.m_gapsLo_[0] = CollationParsedRuleBuilder.mergeCE(t1, t2, 0);
        listheader.m_gapsLo_[1] = CollationParsedRuleBuilder.mergeCE(t1, t2, 1);
        listheader.m_gapsLo_[2] = CollationParsedRuleBuilder.mergeCE(t1, t2, 2);
        int primaryCE = t1 & 0xFFFF0000 | (t2 & 0xFFFF0000) >>> 16;
        primaryCE = RuleBasedCollator.impCEGen_.getImplicitFromRaw(RuleBasedCollator.impCEGen_.getRawFromImplicit(primaryCE) + 1);
        t1 = primaryCE & 0xFFFF0000 | 0x505;
        t2 = primaryCE << 16 & 0xFFFF0000 | 0xC0;
        listheader.m_gapsHi_[0] = CollationParsedRuleBuilder.mergeCE(t1, t2, 0);
        listheader.m_gapsHi_[1] = CollationParsedRuleBuilder.mergeCE(t1, t2, 1);
        listheader.m_gapsHi_[2] = CollationParsedRuleBuilder.mergeCE(t1, t2, 2);
      } else if (listheader.m_indirect_ == true && listheader.m_nextCE_ != 0) {
        listheader.m_pos_[0] = 0;
        int t1 = listheader.m_baseCE_;
        int t2 = listheader.m_baseContCE_;
        listheader.m_gapsLo_[0] = CollationParsedRuleBuilder.mergeCE(t1, t2, 0);
        listheader.m_gapsLo_[1] = CollationParsedRuleBuilder.mergeCE(t1, t2, 1);
        listheader.m_gapsLo_[2] = CollationParsedRuleBuilder.mergeCE(t1, t2, 2);
        t1 = listheader.m_nextCE_;
        t2 = listheader.m_nextContCE_;
        listheader.m_gapsHi_[0] = CollationParsedRuleBuilder.mergeCE(t1, t2, 0);
        listheader.m_gapsHi_[1] = CollationParsedRuleBuilder.mergeCE(t1, t2, 1);
        listheader.m_gapsHi_[2] = CollationParsedRuleBuilder.mergeCE(t1, t2, 2);
      } else {
        while (true) {
          if (tokenstrength < 3) {
            listheader.m_pos_[tokenstrength] = getInverseNext(listheader, tokenstrength);
            if (listheader.m_pos_[tokenstrength] >= 0) {
              listheader.m_fStrToken_[tokenstrength] = token;
            } else {
              throw new Exception("Internal program error");
            } 
          } 
          while (token != null && token.m_strength_ >= tokenstrength) {
            if (tokenstrength < 3)
              listheader.m_lStrToken_[tokenstrength] = token; 
            token = token.m_next_;
          } 
          if (tokenstrength < 2)
            if (listheader.m_pos_[tokenstrength] == listheader.m_pos_[tokenstrength + 1]) {
              listheader.m_fStrToken_[tokenstrength] = listheader.m_fStrToken_[tokenstrength + 1];
              listheader.m_fStrToken_[tokenstrength + 1] = null;
              listheader.m_lStrToken_[tokenstrength + 1] = null;
              listheader.m_pos_[tokenstrength + 1] = -1;
            }  
          if (token != null) {
            tokenstrength = token.m_strength_;
            continue;
          } 
          break;
        } 
        for (int st = 0; st < 3; st++) {
          int pos = listheader.m_pos_[st];
          if (pos >= 0) {
            int t1 = this.m_table_[3 * pos];
            int t2 = this.m_table_[3 * pos + 1];
            listheader.m_gapsHi_[3 * st] = CollationParsedRuleBuilder.mergeCE(t1, t2, 0);
            listheader.m_gapsHi_[3 * st + 1] = CollationParsedRuleBuilder.mergeCE(t1, t2, 1);
            listheader.m_gapsHi_[3 * st + 2] = (t1 & 0x3F) << 24 | (t2 & 0x3F) << 16;
            t1 = listheader.m_baseCE_;
            t2 = listheader.m_baseContCE_;
            listheader.m_gapsLo_[3 * st] = CollationParsedRuleBuilder.mergeCE(t1, t2, 0);
            listheader.m_gapsLo_[3 * st + 1] = CollationParsedRuleBuilder.mergeCE(t1, t2, 1);
            listheader.m_gapsLo_[3 * st + 2] = (t1 & 0x3F) << 24 | (t2 & 0x3F) << 16;
          } 
        } 
      } 
    }
    
    private final int getInverseNext(CollationRuleParser.TokenListHeader listheader, int strength) {
      int ce = listheader.m_baseCE_;
      int secondce = listheader.m_baseContCE_;
      int result = findInverseCE(ce, secondce);
      if (result < 0)
        return -1; 
      ce &= CollationParsedRuleBuilder.STRENGTH_MASK_[strength];
      secondce &= CollationParsedRuleBuilder.STRENGTH_MASK_[strength];
      int nextce = ce;
      int nextcontce = secondce;
      while ((nextce & CollationParsedRuleBuilder.STRENGTH_MASK_[strength]) == ce && (nextcontce & CollationParsedRuleBuilder.STRENGTH_MASK_[strength]) == secondce) {
        nextce = this.m_table_[3 * ++result];
        nextcontce = this.m_table_[3 * result + 1];
      } 
      listheader.m_nextCE_ = nextce;
      listheader.m_nextContCE_ = nextcontce;
      return result;
    }
  }
  
  static {
    InverseUCA temp = null;
    try {
      temp = CollatorReader.getInverseUCA();
    } catch (IOException e) {}
    if (temp != null && RuleBasedCollator.UCA_ != null) {
      if (!temp.m_UCA_version_.equals(RuleBasedCollator.UCA_.m_UCA_version_))
        throw new RuntimeException("UCA versions of UCA and inverse UCA should match"); 
    } else {
      throw new RuntimeException("UCA is not instantiated!");
    } 
    INVERSE_UCA_ = temp;
  }
  
  void setRules(RuleBasedCollator collator) throws Exception {
    if (this.m_parser_.m_resultLength_ > 0 || this.m_parser_.m_removeSet_ != null) {
      assembleTailoringTable(collator);
    } else {
      collator.setWithUCATables();
    } 
    this.m_parser_.setDefaultOptionsInCollator(collator);
  }
  
  private void copyRangeFromUCA(BuildTable t, int start, int end) {
    int u = 0;
    for (u = start; u <= end; u++) {
      int CE = t.m_mapping_.getValue(u);
      if (CE == -268435456 || (isContractionTableElement(CE) && getCE(t.m_contractions_, CE, 0) == -268435456)) {
        this.m_utilElement_.m_uchars_ = UCharacter.toString(u);
        this.m_utilElement_.m_cPoints_ = this.m_utilElement_.m_uchars_;
        this.m_utilElement_.m_prefix_ = 0;
        this.m_utilElement_.m_CELength_ = 0;
        this.m_utilElement_.m_prefixChars_ = null;
        this.m_utilColEIter_.setText(this.m_utilElement_.m_uchars_);
        while (CE != -1) {
          CE = this.m_utilColEIter_.next();
          if (CE != -1)
            this.m_utilElement_.m_CEs_[this.m_utilElement_.m_CELength_++] = CE; 
        } 
        addAnElement(t, this.m_utilElement_);
      } 
    } 
  }
  
  void assembleTailoringTable(RuleBasedCollator collator) throws Exception {
    for (int i = 0; i < this.m_parser_.m_resultLength_; i++) {
      if ((this.m_parser_.m_listHeader_[i]).m_first_ != null)
        initBuffers(this.m_parser_.m_listHeader_[i]); 
    } 
    if (this.m_parser_.m_variableTop_ != null) {
      this.m_parser_.m_options_.m_variableTopValue_ = this.m_parser_.m_variableTop_.m_CE_[0] >>> 16;
      if (this.m_parser_.m_variableTop_.m_listHeader_.m_first_ == this.m_parser_.m_variableTop_)
        this.m_parser_.m_variableTop_.m_listHeader_.m_first_ = this.m_parser_.m_variableTop_.m_next_; 
      if (this.m_parser_.m_variableTop_.m_listHeader_.m_last_ == this.m_parser_.m_variableTop_)
        this.m_parser_.m_variableTop_.m_listHeader_.m_last_ = this.m_parser_.m_variableTop_.m_previous_; 
      if (this.m_parser_.m_variableTop_.m_next_ != null)
        this.m_parser_.m_variableTop_.m_next_.m_previous_ = this.m_parser_.m_variableTop_.m_previous_; 
      if (this.m_parser_.m_variableTop_.m_previous_ != null)
        this.m_parser_.m_variableTop_.m_previous_.m_next_ = this.m_parser_.m_variableTop_.m_next_; 
    } 
    BuildTable t = new BuildTable(this.m_parser_);
    int j;
    for (j = 0; j < this.m_parser_.m_resultLength_; j++)
      createElements(t, this.m_parser_.m_listHeader_[j]); 
    this.m_utilElement_.clear();
    copyRangeFromUCA(t, 0, 255);
    if (this.m_parser_.m_copySet_ != null) {
      j = 0;
      for (j = 0; j < this.m_parser_.m_copySet_.getRangeCount(); j++)
        copyRangeFromUCA(t, this.m_parser_.m_copySet_.getRangeStart(j), this.m_parser_.m_copySet_.getRangeEnd(j)); 
    } 
    char[] conts = RuleBasedCollator.UCA_CONTRACTIONS_;
    int maxUCAContractionLength = RuleBasedCollator.MAX_UCA_CONTRACTION_LENGTH;
    int offset = 0;
    while (conts[offset] != '\000') {
      int contractionLength = maxUCAContractionLength;
      while (contractionLength > 0 && conts[offset + contractionLength - 1] == '\000')
        contractionLength--; 
      int first = Character.codePointAt(conts, offset);
      int firstLength = Character.charCount(first);
      int tailoredCE = t.m_mapping_.getValue(first);
      Elements prefixElm = null;
      if (tailoredCE != -268435456) {
        boolean needToAdd = true;
        if (isContractionTableElement(tailoredCE) && isTailored(t.m_contractions_, tailoredCE, conts, offset + firstLength) == true)
          needToAdd = false; 
        if (!needToAdd && isPrefix(tailoredCE) && conts[offset + 1] == '\000') {
          Elements elm = new Elements();
          elm.m_CELength_ = 0;
          elm.m_uchars_ = Character.toString(conts[offset]);
          elm.m_cPoints_ = this.m_utilElement_.m_uchars_;
          elm.m_prefixChars_ = Character.toString(conts[offset + 2]);
          elm.m_prefix_ = 0;
          prefixElm = t.m_prefixLookup_.get(elm);
          if (prefixElm == null || prefixElm.m_prefixChars_.charAt(0) != conts[offset + 2])
            needToAdd = true; 
        } 
        if (this.m_parser_.m_removeSet_ != null && this.m_parser_.m_removeSet_.contains(first))
          needToAdd = false; 
        if (needToAdd == true) {
          if (conts[offset + 1] != '\000') {
            this.m_utilElement_.m_prefix_ = 0;
            this.m_utilElement_.m_prefixChars_ = null;
            this.m_utilElement_.m_uchars_ = new String(conts, offset, contractionLength);
            this.m_utilElement_.m_cPoints_ = this.m_utilElement_.m_uchars_;
            this.m_utilElement_.m_CELength_ = 0;
            this.m_utilColEIter_.setText(this.m_utilElement_.m_uchars_);
          } else {
            int preKeyLen = 0;
            this.m_utilElement_.m_uchars_ = Character.toString(conts[offset]);
            this.m_utilElement_.m_cPoints_ = this.m_utilElement_.m_uchars_;
            this.m_utilElement_.m_CELength_ = 0;
            this.m_utilElement_.m_prefixChars_ = Character.toString(conts[offset + 2]);
            if (prefixElm == null) {
              this.m_utilElement_.m_prefix_ = 0;
            } else {
              this.m_utilElement_.m_prefix_ = this.m_utilElement_.m_prefix_;
            } 
            this.m_utilColEIter_.setText(this.m_utilElement_.m_prefixChars_);
            while (this.m_utilColEIter_.next() != -1)
              preKeyLen++; 
            this.m_utilColEIter_.setText(this.m_utilElement_.m_prefixChars_ + this.m_utilElement_.m_uchars_);
            while (preKeyLen-- > 0 && this.m_utilColEIter_.next() != -1);
          } 
          while (true) {
            int CE = this.m_utilColEIter_.next();
            if (CE != -1) {
              this.m_utilElement_.m_CEs_[this.m_utilElement_.m_CELength_++] = CE;
              continue;
            } 
            break;
          } 
          addAnElement(t, this.m_utilElement_);
        } 
      } else if (this.m_parser_.m_removeSet_ != null && this.m_parser_.m_removeSet_.contains(first)) {
        copyRangeFromUCA(t, first, first);
      } 
      offset += maxUCAContractionLength;
    } 
    processUCACompleteIgnorables(t);
    canonicalClosure(t);
    assembleTable(t, collator);
  }
  
  private static class CEGenerator {
    CollationParsedRuleBuilder.WeightRange[] m_ranges_ = new CollationParsedRuleBuilder.WeightRange[7];
    
    int m_rangesLength_;
    
    int m_byteSize_;
    
    int m_start_;
    
    int m_limit_;
    
    int m_maxCount_;
    
    int m_count_;
    
    int m_current_;
    
    int m_fLow_;
    
    int m_fHigh_;
    
    CEGenerator() {
      for (int i = 6; i >= 0; i--)
        this.m_ranges_[i] = new CollationParsedRuleBuilder.WeightRange(); 
    }
  }
  
  private static class WeightRange implements Comparable<WeightRange> {
    int m_start_;
    
    int m_end_;
    
    int m_length_;
    
    int m_count_;
    
    int m_length2_;
    
    int m_count2_;
    
    public int compareTo(WeightRange target) {
      return Utility.compareUnsigned(this.m_start_, target.m_start_);
    }
    
    public void clear() {
      this.m_start_ = 0;
      this.m_end_ = 0;
      this.m_length_ = 0;
      this.m_count_ = 0;
      this.m_length2_ = 0;
      this.m_count2_ = 0;
    }
    
    WeightRange() {
      clear();
    }
    
    WeightRange(WeightRange source) {
      this.m_start_ = source.m_start_;
      this.m_end_ = source.m_end_;
      this.m_length_ = source.m_length_;
      this.m_count_ = source.m_count_;
      this.m_length2_ = source.m_length2_;
      this.m_count2_ = source.m_count2_;
    }
  }
  
  private static class MaxJamoExpansionTable {
    List<Integer> m_endExpansionCE_;
    
    List<Boolean> m_isV_;
    
    byte m_maxLSize_;
    
    byte m_maxVSize_;
    
    byte m_maxTSize_;
    
    MaxJamoExpansionTable() {
      this.m_endExpansionCE_ = new ArrayList<Integer>();
      this.m_isV_ = new ArrayList<Boolean>();
      this.m_endExpansionCE_.add(Integer.valueOf(0));
      this.m_isV_.add(Boolean.FALSE);
      this.m_maxLSize_ = 1;
      this.m_maxVSize_ = 1;
      this.m_maxTSize_ = 1;
    }
    
    MaxJamoExpansionTable(MaxJamoExpansionTable table) {
      this.m_endExpansionCE_ = new ArrayList<Integer>(table.m_endExpansionCE_);
      this.m_isV_ = new ArrayList<Boolean>(table.m_isV_);
      this.m_maxLSize_ = table.m_maxLSize_;
      this.m_maxVSize_ = table.m_maxVSize_;
      this.m_maxTSize_ = table.m_maxTSize_;
    }
  }
  
  private static class MaxExpansionTable {
    List<Integer> m_endExpansionCE_;
    
    List<Byte> m_expansionCESize_;
    
    MaxExpansionTable() {
      this.m_endExpansionCE_ = new ArrayList<Integer>();
      this.m_expansionCESize_ = new ArrayList<Byte>();
      this.m_endExpansionCE_.add(Integer.valueOf(0));
      this.m_expansionCESize_.add(Byte.valueOf((byte)0));
    }
    
    MaxExpansionTable(MaxExpansionTable table) {
      this.m_endExpansionCE_ = new ArrayList<Integer>(table.m_endExpansionCE_);
      this.m_expansionCESize_ = new ArrayList<Byte>(table.m_expansionCESize_);
    }
  }
  
  private static class BasicContractionTable {
    List<Integer> m_CEs_ = new ArrayList<Integer>();
    
    StringBuilder m_codePoints_ = new StringBuilder();
  }
  
  private static class ContractionTable {
    List<CollationParsedRuleBuilder.BasicContractionTable> m_elements_;
    
    IntTrieBuilder m_mapping_;
    
    StringBuilder m_codePoints_;
    
    List<Integer> m_CEs_;
    
    List<Integer> m_offsets_;
    
    int m_currentTag_;
    
    ContractionTable(IntTrieBuilder mapping) {
      this.m_mapping_ = mapping;
      this.m_elements_ = new ArrayList<CollationParsedRuleBuilder.BasicContractionTable>();
      this.m_CEs_ = new ArrayList<Integer>();
      this.m_codePoints_ = new StringBuilder();
      this.m_offsets_ = new ArrayList<Integer>();
      this.m_currentTag_ = 0;
    }
    
    ContractionTable(ContractionTable table) {
      this.m_mapping_ = table.m_mapping_;
      this.m_elements_ = new ArrayList<CollationParsedRuleBuilder.BasicContractionTable>(table.m_elements_);
      this.m_codePoints_ = new StringBuilder(table.m_codePoints_);
      this.m_CEs_ = new ArrayList<Integer>(table.m_CEs_);
      this.m_offsets_ = new ArrayList<Integer>(table.m_offsets_);
      this.m_currentTag_ = table.m_currentTag_;
    }
  }
  
  private static class CombinClassTable {
    int[] index = new int[256];
    
    char[] cPoints;
    
    int size;
    
    int pos;
    
    int curClass;
    
    CombinClassTable() {
      this.cPoints = null;
      this.size = 0;
      this.pos = 0;
      this.curClass = 1;
    }
    
    void generate(char[] cps, int numOfCM, int[] ccIndex) {
      int count = 0;
      this.cPoints = new char[numOfCM];
      for (int i = 0; i < 256; i++) {
        for (int j = 0; j < ccIndex[i]; j++)
          this.cPoints[count++] = cps[(i << 8) + j]; 
        this.index[i] = count;
      } 
      this.size = count;
    }
    
    char GetFirstCM(int cClass) {
      this.curClass = cClass;
      if (this.cPoints == null || cClass == 0 || this.index[cClass] == this.index[cClass - 1])
        return Character.MIN_VALUE; 
      this.pos = 1;
      return this.cPoints[this.index[cClass - 1]];
    }
    
    char GetNextCM() {
      if (this.cPoints == null || this.index[this.curClass] == this.index[this.curClass - 1] + this.pos)
        return Character.MIN_VALUE; 
      return this.cPoints[this.index[this.curClass - 1] + this.pos++];
    }
  }
  
  private static final class BuildTable implements TrieBuilder.DataManipulate {
    RuleBasedCollator m_collator_;
    
    IntTrieBuilder m_mapping_;
    
    List<Integer> m_expansions_;
    
    CollationParsedRuleBuilder.ContractionTable m_contractions_;
    
    CollationRuleParser.OptionSet m_options_;
    
    CollationParsedRuleBuilder.MaxExpansionTable m_maxExpansions_;
    
    CollationParsedRuleBuilder.MaxJamoExpansionTable m_maxJamoExpansions_;
    
    byte[] m_unsafeCP_;
    
    byte[] m_contrEndCP_;
    
    Map<CollationParsedRuleBuilder.Elements, CollationParsedRuleBuilder.Elements> m_prefixLookup_;
    
    CollationParsedRuleBuilder.CombinClassTable cmLookup;
    
    public int getFoldedValue(int cp, int offset) {
      int limit = cp + 1024;
      while (cp < limit) {
        int value = this.m_mapping_.getValue(cp);
        boolean inBlockZero = this.m_mapping_.isInZeroBlock(cp);
        int tag = CollationParsedRuleBuilder.getCETag(value);
        if (inBlockZero == true) {
          cp += 32;
          continue;
        } 
        if (!CollationParsedRuleBuilder.isSpecial(value) || (tag != 10 && tag != 0))
          return 0xF5000000 | offset; 
        cp++;
      } 
      return 0;
    }
    
    BuildTable(CollationRuleParser parser) {
      this.cmLookup = null;
      this.m_collator_ = new RuleBasedCollator();
      this.m_collator_.setWithUCAData();
      CollationParsedRuleBuilder.MaxExpansionTable maxet = new CollationParsedRuleBuilder.MaxExpansionTable();
      CollationParsedRuleBuilder.MaxJamoExpansionTable maxjet = new CollationParsedRuleBuilder.MaxJamoExpansionTable();
      this.m_options_ = parser.m_options_;
      this.m_expansions_ = new ArrayList<Integer>();
      int trieinitialvalue = -268435456;
      this.m_mapping_ = new IntTrieBuilder(null, 196608, trieinitialvalue, trieinitialvalue, true);
      this.m_prefixLookup_ = new HashMap<CollationParsedRuleBuilder.Elements, CollationParsedRuleBuilder.Elements>();
      this.m_contractions_ = new CollationParsedRuleBuilder.ContractionTable(this.m_mapping_);
      this.m_maxExpansions_ = maxet;
      for (int i = 0; i < RuleBasedCollator.UCA_.m_expansionEndCE_.length; i++) {
        maxet.m_endExpansionCE_.add(Integer.valueOf(RuleBasedCollator.UCA_.m_expansionEndCE_[i]));
        maxet.m_expansionCESize_.add(Byte.valueOf(RuleBasedCollator.UCA_.m_expansionEndCEMaxSize_[i]));
      } 
      this.m_maxJamoExpansions_ = maxjet;
      this.m_unsafeCP_ = new byte[1056];
      this.m_contrEndCP_ = new byte[1056];
      Arrays.fill(this.m_unsafeCP_, (byte)0);
      Arrays.fill(this.m_contrEndCP_, (byte)0);
    }
    
    BuildTable(BuildTable table) {
      this.cmLookup = null;
      this.m_collator_ = table.m_collator_;
      this.m_mapping_ = new IntTrieBuilder(table.m_mapping_);
      this.m_expansions_ = new ArrayList<Integer>(table.m_expansions_);
      this.m_contractions_ = new CollationParsedRuleBuilder.ContractionTable(table.m_contractions_);
      this.m_contractions_.m_mapping_ = this.m_mapping_;
      this.m_options_ = table.m_options_;
      this.m_maxExpansions_ = new CollationParsedRuleBuilder.MaxExpansionTable(table.m_maxExpansions_);
      this.m_maxJamoExpansions_ = new CollationParsedRuleBuilder.MaxJamoExpansionTable(table.m_maxJamoExpansions_);
      this.m_unsafeCP_ = new byte[table.m_unsafeCP_.length];
      System.arraycopy(table.m_unsafeCP_, 0, this.m_unsafeCP_, 0, this.m_unsafeCP_.length);
      this.m_contrEndCP_ = new byte[table.m_contrEndCP_.length];
      System.arraycopy(table.m_contrEndCP_, 0, this.m_contrEndCP_, 0, this.m_contrEndCP_.length);
    }
  }
  
  private static class Elements {
    String m_prefixChars_;
    
    int m_prefix_;
    
    String m_uchars_;
    
    String m_cPoints_;
    
    int m_cPointsOffset_;
    
    int[] m_CEs_;
    
    int m_CELength_;
    
    int m_mapCE_;
    
    int[] m_sizePrim_;
    
    int[] m_sizeSec_;
    
    int[] m_sizeTer_;
    
    boolean m_variableTop_;
    
    boolean m_caseBit_;
    
    Elements() {
      this.m_sizePrim_ = new int[128];
      this.m_sizeSec_ = new int[128];
      this.m_sizeTer_ = new int[128];
      this.m_CEs_ = new int[256];
      this.m_CELength_ = 0;
    }
    
    Elements(Elements element) {
      this.m_prefixChars_ = element.m_prefixChars_;
      this.m_prefix_ = element.m_prefix_;
      this.m_uchars_ = element.m_uchars_;
      this.m_cPoints_ = element.m_cPoints_;
      this.m_cPointsOffset_ = element.m_cPointsOffset_;
      this.m_CEs_ = element.m_CEs_;
      this.m_CELength_ = element.m_CELength_;
      this.m_mapCE_ = element.m_mapCE_;
      this.m_sizePrim_ = element.m_sizePrim_;
      this.m_sizeSec_ = element.m_sizeSec_;
      this.m_sizeTer_ = element.m_sizeTer_;
      this.m_variableTop_ = element.m_variableTop_;
      this.m_caseBit_ = element.m_caseBit_;
    }
    
    public void clear() {
      this.m_prefixChars_ = null;
      this.m_prefix_ = 0;
      this.m_uchars_ = null;
      this.m_cPoints_ = null;
      this.m_cPointsOffset_ = 0;
      this.m_CELength_ = 0;
      this.m_mapCE_ = 0;
      Arrays.fill(this.m_sizePrim_, 0);
      Arrays.fill(this.m_sizeSec_, 0);
      Arrays.fill(this.m_sizeTer_, 0);
      this.m_variableTop_ = false;
      this.m_caseBit_ = false;
    }
    
    public int hashCode() {
      String str = this.m_cPoints_.substring(this.m_cPointsOffset_);
      return str.hashCode();
    }
    
    public boolean equals(Object target) {
      if (target == this)
        return true; 
      if (target instanceof Elements) {
        Elements t = (Elements)target;
        int size = this.m_cPoints_.length() - this.m_cPointsOffset_;
        if (size == t.m_cPoints_.length() - t.m_cPointsOffset_)
          return t.m_cPoints_.regionMatches(t.m_cPointsOffset_, this.m_cPoints_, this.m_cPointsOffset_, size); 
      } 
      return false;
    }
  }
  
  private static final int[] STRENGTH_MASK_ = new int[] { -65536, -256, -1 };
  
  private static final int CE_NOT_FOUND_ = -268435456;
  
  private static final int CE_NOT_FOUND_TAG_ = 0;
  
  private static final int CE_EXPANSION_TAG_ = 1;
  
  private static final int CE_CONTRACTION_TAG_ = 2;
  
  private static final int CE_SURROGATE_TAG_ = 5;
  
  private static final int CE_IMPLICIT_TAG_ = 10;
  
  private static final int CE_SPEC_PROC_TAG_ = 11;
  
  private static final int CE_LONG_PRIMARY_TAG_ = 12;
  
  private static final int UNSAFECP_TABLE_SIZE_ = 1056;
  
  private static final int UNSAFECP_TABLE_MASK_ = 8191;
  
  private static final int UPPER_CASE_ = 128;
  
  private static final int MIXED_CASE_ = 64;
  
  private static final int LOWER_CASE_ = 0;
  
  private static final int CONTRACTION_TABLE_NEW_ELEMENT_ = 16777215;
  
  private CollationRuleParser m_parser_;
  
  private CollationElementIterator m_utilColEIter_;
  
  private CEGenerator[] m_utilGens_;
  
  private int[] m_utilCEBuffer_;
  
  private int[] m_utilIntBuffer_;
  
  private Elements m_utilElement_;
  
  private Elements m_utilElement2_;
  
  private CollationRuleParser.Token m_utilToken_;
  
  private int[] m_utilCountBuffer_;
  
  private long[] m_utilLongBuffer_;
  
  private WeightRange[] m_utilLowerWeightRange_;
  
  private WeightRange[] m_utilUpperWeightRange_;
  
  private WeightRange m_utilWeightRange_;
  
  private final Normalizer2Impl m_nfcImpl_;
  
  private CanonicalIterator m_utilCanIter_;
  
  private StringBuilder m_utilStringBuffer_;
  
  private static boolean buildCMTabFlag = false;
  
  private void initBuffers(CollationRuleParser.TokenListHeader listheader) throws Exception {
    CollationRuleParser.Token token = listheader.m_last_;
    Arrays.fill(this.m_utilIntBuffer_, 0, 16, 0);
    token.m_toInsert_ = 1;
    this.m_utilIntBuffer_[token.m_strength_] = 1;
    while (token.m_previous_ != null) {
      if (token.m_previous_.m_strength_ < token.m_strength_) {
        this.m_utilIntBuffer_[token.m_strength_] = 0;
        this.m_utilIntBuffer_[token.m_previous_.m_strength_] = this.m_utilIntBuffer_[token.m_previous_.m_strength_] + 1;
      } else if (token.m_previous_.m_strength_ > token.m_strength_) {
        this.m_utilIntBuffer_[token.m_previous_.m_strength_] = 1;
      } else {
        this.m_utilIntBuffer_[token.m_strength_] = this.m_utilIntBuffer_[token.m_strength_] + 1;
      } 
      token = token.m_previous_;
      token.m_toInsert_ = this.m_utilIntBuffer_[token.m_strength_];
    } 
    token.m_toInsert_ = this.m_utilIntBuffer_[token.m_strength_];
    INVERSE_UCA_.getInverseGapPositions(listheader);
    token = listheader.m_first_;
    int fstrength = 15;
    int initstrength = 15;
    this.m_utilCEBuffer_[0] = mergeCE(listheader.m_baseCE_, listheader.m_baseContCE_, 0);
    this.m_utilCEBuffer_[1] = mergeCE(listheader.m_baseCE_, listheader.m_baseContCE_, 1);
    this.m_utilCEBuffer_[2] = mergeCE(listheader.m_baseCE_, listheader.m_baseContCE_, 2);
    while (token != null) {
      fstrength = token.m_strength_;
      if (fstrength < initstrength) {
        initstrength = fstrength;
        if (listheader.m_pos_[fstrength] == -1) {
          while (listheader.m_pos_[fstrength] == -1 && fstrength > 0)
            fstrength--; 
          if (listheader.m_pos_[fstrength] == -1)
            throw new Exception("Internal program error"); 
        } 
        if (initstrength == 2) {
          this.m_utilCEBuffer_[0] = listheader.m_gapsLo_[fstrength * 3];
          this.m_utilCEBuffer_[1] = listheader.m_gapsLo_[fstrength * 3 + 1];
          this.m_utilCEBuffer_[2] = getCEGenerator(this.m_utilGens_[2], listheader.m_gapsLo_, listheader.m_gapsHi_, token, fstrength);
        } else if (initstrength == 1) {
          this.m_utilCEBuffer_[0] = listheader.m_gapsLo_[fstrength * 3];
          this.m_utilCEBuffer_[1] = getCEGenerator(this.m_utilGens_[1], listheader.m_gapsLo_, listheader.m_gapsHi_, token, fstrength);
          this.m_utilCEBuffer_[2] = getSimpleCEGenerator(this.m_utilGens_[2], token, 2);
        } else {
          this.m_utilCEBuffer_[0] = getCEGenerator(this.m_utilGens_[0], listheader.m_gapsLo_, listheader.m_gapsHi_, token, fstrength);
          this.m_utilCEBuffer_[1] = getSimpleCEGenerator(this.m_utilGens_[1], token, 1);
          this.m_utilCEBuffer_[2] = getSimpleCEGenerator(this.m_utilGens_[2], token, 2);
        } 
      } else if (token.m_strength_ == 2) {
        this.m_utilCEBuffer_[2] = getNextGenerated(this.m_utilGens_[2]);
      } else if (token.m_strength_ == 1) {
        this.m_utilCEBuffer_[1] = getNextGenerated(this.m_utilGens_[1]);
        this.m_utilCEBuffer_[2] = getSimpleCEGenerator(this.m_utilGens_[2], token, 2);
      } else if (token.m_strength_ == 0) {
        this.m_utilCEBuffer_[0] = getNextGenerated(this.m_utilGens_[0]);
        this.m_utilCEBuffer_[1] = getSimpleCEGenerator(this.m_utilGens_[1], token, 1);
        this.m_utilCEBuffer_[2] = getSimpleCEGenerator(this.m_utilGens_[2], token, 2);
      } 
      doCE(this.m_utilCEBuffer_, token);
      token = token.m_next_;
    } 
  }
  
  private int getNextGenerated(CEGenerator g) {
    g.m_current_ = nextWeight(g);
    return g.m_current_;
  }
  
  private int getSimpleCEGenerator(CEGenerator g, CollationRuleParser.Token token, int strength) throws Exception {
    int high, low, count = 1;
    int maxbyte = (strength == 2) ? 63 : 255;
    if (strength == 1) {
      low = -2046820352;
      high = -1;
      count = 121;
    } else {
      low = 83886080;
      high = 1073741824;
      count = 59;
    } 
    if (token.m_next_ != null && token.m_next_.m_strength_ == strength)
      count = token.m_next_.m_toInsert_; 
    g.m_rangesLength_ = allocateWeights(low, high, count, maxbyte, g.m_ranges_);
    g.m_current_ = 83886080;
    if (g.m_rangesLength_ == 0)
      throw new Exception("Internal program error"); 
    return g.m_current_;
  }
  
  private static int mergeCE(int ce1, int ce2, int strength) {
    int mask = 255;
    if (strength == 1) {
      mask = 65280;
    } else if (strength == 0) {
      mask = -65536;
    } 
    ce1 &= mask;
    ce2 &= mask;
    switch (strength) {
      case 0:
        return ce1 | ce2 >>> 16;
      case 1:
        return ce1 << 16 | ce2 << 8;
    } 
    return ce1 << 24 | ce2 << 16;
  }
  
  private int getCEGenerator(CEGenerator g, int[] lows, int[] highs, CollationRuleParser.Token token, int fstrength) throws Exception {
    int strength = token.m_strength_;
    int low = lows[fstrength * 3 + strength];
    int high = highs[fstrength * 3 + strength];
    int maxbyte = 0;
    if (strength == 2) {
      maxbyte = 63;
    } else if (strength == 0) {
      maxbyte = 254;
    } else {
      maxbyte = 255;
    } 
    int count = token.m_toInsert_;
    if (Utility.compareUnsigned(low, high) >= 0 && strength > 0) {
      int s = strength;
      while (true) {
        s--;
        if (lows[fstrength * 3 + s] != highs[fstrength * 3 + s]) {
          if (strength == 1) {
            if (low < -2046820352)
              low = -2046820352; 
            high = -1;
            break;
          } 
          if (low < 83886080)
            low = 83886080; 
          high = 1073741824;
          break;
        } 
        if (s < 0)
          throw new Exception("Internal program error"); 
      } 
    } 
    if (0 <= low && low < 33554432)
      low = 33554432; 
    if (strength == 1) {
      if (Utility.compareUnsigned(low, 83886080) >= 0 && Utility.compareUnsigned(low, -2046820352) < 0)
        low = -2046820352; 
      if (Utility.compareUnsigned(high, 83886080) > 0 && Utility.compareUnsigned(high, -2046820352) < 0)
        high = -2046820352; 
      if (Utility.compareUnsigned(low, 83886080) < 0) {
        g.m_rangesLength_ = allocateWeights(50331648, high, count, maxbyte, g.m_ranges_);
        g.m_current_ = nextWeight(g);
        return g.m_current_;
      } 
    } 
    g.m_rangesLength_ = allocateWeights(low, high, count, maxbyte, g.m_ranges_);
    if (g.m_rangesLength_ == 0)
      throw new Exception("Internal program error"); 
    g.m_current_ = nextWeight(g);
    return g.m_current_;
  }
  
  private void doCE(int[] ceparts, CollationRuleParser.Token token) throws Exception {
    for (int i = 0; i < 3; i++)
      this.m_utilIntBuffer_[i] = countBytes(ceparts[i]); 
    int cei = 0;
    int value = 0;
    while (cei << 1 < this.m_utilIntBuffer_[0] || cei < this.m_utilIntBuffer_[1] || cei < this.m_utilIntBuffer_[2]) {
      if (cei > 0) {
        value = 192;
      } else {
        value = 0;
      } 
      if (cei << 1 < this.m_utilIntBuffer_[0])
        value |= (ceparts[0] >> 32 - (cei + 1 << 4) & 0xFFFF) << 16; 
      if (cei < this.m_utilIntBuffer_[1])
        value |= (ceparts[1] >> 32 - (cei + 1 << 3) & 0xFF) << 8; 
      if (cei < this.m_utilIntBuffer_[2])
        value |= ceparts[2] >> 32 - (cei + 1 << 3) & 0x3F; 
      token.m_CE_[cei] = value;
      cei++;
    } 
    if (cei == 0) {
      token.m_CELength_ = 1;
      token.m_CE_[0] = 0;
    } else {
      token.m_CELength_ = cei;
    } 
    if (token.m_CE_[0] != 0) {
      token.m_CE_[0] = token.m_CE_[0] & 0xFFFFFF3F;
      int cSize = (token.m_source_ & 0xFF000000) >>> 24;
      int startoftokenrule = token.m_source_ & 0xFFFFFF;
      if (cSize > 1) {
        String tokenstr = token.m_rules_.substring(startoftokenrule, startoftokenrule + cSize);
        token.m_CE_[0] = token.m_CE_[0] | getCaseBits(tokenstr);
      } else {
        int caseCE = getFirstCE(token.m_rules_.charAt(startoftokenrule));
        token.m_CE_[0] = token.m_CE_[0] | caseCE & 0xC0;
      } 
    } 
  }
  
  private static final int countBytes(int ce) {
    int mask = -1;
    int result = 0;
    while (mask != 0) {
      if ((ce & mask) != 0)
        result++; 
      mask >>>= 8;
    } 
    return result;
  }
  
  private void createElements(BuildTable t, CollationRuleParser.TokenListHeader lh) {
    CollationRuleParser.Token tok = lh.m_first_;
    this.m_utilElement_.clear();
    while (tok != null) {
      if (tok.m_expansion_ != 0) {
        int len = tok.m_expansion_ >>> 24;
        int currentSequenceLen = len;
        int expOffset = tok.m_expansion_ & 0xFFFFFF;
        this.m_utilToken_.m_source_ = currentSequenceLen | expOffset;
        this.m_utilToken_.m_rules_ = this.m_parser_.m_source_;
        while (len > 0) {
          currentSequenceLen = len;
          while (currentSequenceLen > 0) {
            this.m_utilToken_.m_source_ = currentSequenceLen << 24 | expOffset;
            CollationRuleParser.Token expt = this.m_parser_.m_hashTable_.get(this.m_utilToken_);
            if (expt != null && expt.m_strength_ != -559038737) {
              int noOfCEsToCopy = expt.m_CELength_;
              for (int j = 0; j < noOfCEsToCopy; j++)
                tok.m_expCE_[tok.m_expCELength_ + j] = expt.m_CE_[j]; 
              tok.m_expCELength_ += noOfCEsToCopy;
              expOffset += currentSequenceLen;
              len -= currentSequenceLen;
              break;
            } 
            currentSequenceLen--;
          } 
          if (currentSequenceLen == 0) {
            this.m_utilColEIter_.setText(this.m_parser_.m_source_.substring(expOffset, expOffset + 1));
            while (true) {
              int order = this.m_utilColEIter_.next();
              if (order == -1)
                break; 
              tok.m_expCE_[tok.m_expCELength_++] = order;
            } 
            expOffset++;
            len--;
          } 
        } 
      } else {
        tok.m_expCELength_ = 0;
      } 
      this.m_utilElement_.m_CELength_ = tok.m_CELength_ + tok.m_expCELength_;
      System.arraycopy(tok.m_CE_, 0, this.m_utilElement_.m_CEs_, 0, tok.m_CELength_);
      System.arraycopy(tok.m_expCE_, 0, this.m_utilElement_.m_CEs_, tok.m_CELength_, tok.m_expCELength_);
      this.m_utilElement_.m_prefix_ = 0;
      this.m_utilElement_.m_cPointsOffset_ = 0;
      if (tok.m_prefix_ != 0) {
        int size = tok.m_prefix_ >> 24;
        int offset = tok.m_prefix_ & 0xFFFFFF;
        this.m_utilElement_.m_prefixChars_ = this.m_parser_.m_source_.substring(offset, offset + size);
        size = (tok.m_source_ >> 24) - (tok.m_prefix_ >> 24);
        offset = (tok.m_source_ & 0xFFFFFF) + (tok.m_prefix_ >> 24);
        this.m_utilElement_.m_uchars_ = this.m_parser_.m_source_.substring(offset, offset + size);
      } else {
        this.m_utilElement_.m_prefixChars_ = null;
        int offset = tok.m_source_ & 0xFFFFFF;
        int size = tok.m_source_ >>> 24;
        this.m_utilElement_.m_uchars_ = this.m_parser_.m_source_.substring(offset, offset + size);
      } 
      this.m_utilElement_.m_cPoints_ = this.m_utilElement_.m_uchars_;
      boolean containCombinMarks = false;
      for (int i = 0; i < this.m_utilElement_.m_cPoints_.length() - this.m_utilElement_.m_cPointsOffset_; 
        i++) {
        if (isJamo(this.m_utilElement_.m_cPoints_.charAt(i))) {
          t.m_collator_.m_isJamoSpecial_ = true;
          break;
        } 
        if (!buildCMTabFlag) {
          int fcd = this.m_nfcImpl_.getFCD16(this.m_utilElement_.m_cPoints_.charAt(i));
          if ((fcd & 0xFF) == 0) {
            containCombinMarks = false;
          } else {
            containCombinMarks = true;
          } 
        } 
      } 
      if (!buildCMTabFlag && containCombinMarks)
        buildCMTabFlag = true; 
      addAnElement(t, this.m_utilElement_);
      tok = tok.m_next_;
    } 
  }
  
  private final int getCaseBits(String src) throws Exception {
    int uCount = 0;
    int lCount = 0;
    src = Normalizer.decompose(src, true);
    this.m_utilColEIter_.setText(src);
    for (int i = 0; i < src.length(); i++) {
      this.m_utilColEIter_.setText(src.substring(i, i + 1));
      int order = this.m_utilColEIter_.next();
      if (RuleBasedCollator.isContinuation(order))
        throw new Exception("Internal program error"); 
      if ((order & 0xC0) == 128) {
        uCount++;
      } else {
        char ch = src.charAt(i);
        if (UCharacter.isLowerCase(ch)) {
          lCount++;
        } else if (toSmallKana(ch) == ch && toLargeKana(ch) != ch) {
          lCount++;
        } 
      } 
    } 
    if (uCount != 0 && lCount != 0)
      return 64; 
    if (uCount != 0)
      return 128; 
    return 0;
  }
  
  private static final char toLargeKana(char ch) {
    if ('あ' < ch && ch < 'ワ')
      switch (ch - 12288) {
        case 65:
        case 67:
        case 69:
        case 71:
        case 73:
        case 99:
        case 131:
        case 133:
        case 142:
        case 161:
        case 163:
        case 165:
        case 167:
        case 169:
        case 195:
        case 227:
        case 229:
        case 238:
          ch = (char)(ch + 1);
          break;
        case 245:
          ch = 'カ';
          break;
        case 246:
          ch = 'ケ';
          break;
      }  
    return ch;
  }
  
  private static final char toSmallKana(char ch) {
    if ('あ' < ch && ch < 'ワ')
      switch (ch - 12288) {
        case 66:
        case 68:
        case 70:
        case 72:
        case 74:
        case 100:
        case 132:
        case 134:
        case 143:
        case 162:
        case 164:
        case 166:
        case 168:
        case 170:
        case 196:
        case 228:
        case 230:
        case 239:
          ch = (char)(ch - 1);
          break;
        case 171:
          ch = 'ヵ';
          break;
        case 177:
          ch = 'ヶ';
          break;
      }  
    return ch;
  }
  
  private int getFirstCE(char ch) {
    this.m_utilColEIter_.setText(UCharacter.toString(ch));
    return this.m_utilColEIter_.next();
  }
  
  private int addAnElement(BuildTable t, Elements element) {
    List<Integer> expansions = t.m_expansions_;
    element.m_mapCE_ = 0;
    if (element.m_CELength_ == 1) {
      element.m_mapCE_ = element.m_CEs_[0];
    } else if (element.m_CELength_ == 2 && RuleBasedCollator.isContinuation(element.m_CEs_[1]) && (element.m_CEs_[1] & 0xFFFF3F) == 0 && (element.m_CEs_[0] >> 8 & 0xFF) == 5 && (element.m_CEs_[0] & 0xFF) == 5) {
      element.m_mapCE_ = 0xFC000000 | element.m_CEs_[0] >> 8 & 0xFFFF00 | element.m_CEs_[1] >> 24 & 0xFF;
    } else {
      int expansion = 0xF1000000 | addExpansion(expansions, element.m_CEs_[0]) << 4 & 0xFFFFF0;
      for (int i = 1; i < element.m_CELength_; i++)
        addExpansion(expansions, element.m_CEs_[i]); 
      if (element.m_CELength_ <= 15) {
        expansion |= element.m_CELength_;
      } else {
        addExpansion(expansions, 0);
      } 
      element.m_mapCE_ = expansion;
      setMaxExpansion(element.m_CEs_[element.m_CELength_ - 1], (byte)element.m_CELength_, t.m_maxExpansions_);
      if (isJamo(element.m_cPoints_.charAt(0))) {
        t.m_collator_.m_isJamoSpecial_ = true;
        setMaxJamoExpansion(element.m_cPoints_.charAt(0), element.m_CEs_[element.m_CELength_ - 1], (byte)element.m_CELength_, t.m_maxJamoExpansions_);
      } 
    } 
    int uniChar = 0;
    if (element.m_uchars_.length() == 2 && UTF16.isLeadSurrogate(element.m_uchars_.charAt(0))) {
      uniChar = UCharacterProperty.getRawSupplementary(element.m_uchars_.charAt(0), element.m_uchars_.charAt(1));
    } else if (element.m_uchars_.length() == 1) {
      uniChar = element.m_uchars_.charAt(0);
    } 
    if (uniChar != 0 && UCharacter.isDigit(uniChar)) {
      int expansion = -50331647;
      if (element.m_mapCE_ != 0) {
        expansion |= addExpansion(expansions, element.m_mapCE_) << 4;
      } else {
        expansion |= addExpansion(expansions, element.m_CEs_[0]) << 4;
      } 
      element.m_mapCE_ = expansion;
    } 
    if (element.m_prefixChars_ != null && element.m_prefixChars_.length() - element.m_prefix_ > 0) {
      this.m_utilElement2_.m_caseBit_ = element.m_caseBit_;
      this.m_utilElement2_.m_CELength_ = element.m_CELength_;
      this.m_utilElement2_.m_CEs_ = element.m_CEs_;
      this.m_utilElement2_.m_mapCE_ = element.m_mapCE_;
      this.m_utilElement2_.m_sizePrim_ = element.m_sizePrim_;
      this.m_utilElement2_.m_sizeSec_ = element.m_sizeSec_;
      this.m_utilElement2_.m_sizeTer_ = element.m_sizeTer_;
      this.m_utilElement2_.m_variableTop_ = element.m_variableTop_;
      this.m_utilElement2_.m_prefix_ = element.m_prefix_;
      this.m_utilElement2_.m_prefixChars_ = Normalizer.compose(element.m_prefixChars_, false);
      this.m_utilElement2_.m_uchars_ = element.m_uchars_;
      this.m_utilElement2_.m_cPoints_ = element.m_cPoints_;
      this.m_utilElement2_.m_cPointsOffset_ = 0;
      if (t.m_prefixLookup_ != null) {
        Elements uCE = t.m_prefixLookup_.get(element);
        if (uCE != null) {
          element.m_mapCE_ = addPrefix(t, uCE.m_mapCE_, element);
        } else {
          element.m_mapCE_ = addPrefix(t, -268435456, element);
          uCE = new Elements(element);
          uCE.m_cPoints_ = uCE.m_uchars_;
          t.m_prefixLookup_.put(uCE, uCE);
        } 
        if (this.m_utilElement2_.m_prefixChars_.length() != element.m_prefixChars_.length() - element.m_prefix_ || !this.m_utilElement2_.m_prefixChars_.regionMatches(0, element.m_prefixChars_, element.m_prefix_, this.m_utilElement2_.m_prefixChars_.length()))
          this.m_utilElement2_.m_mapCE_ = addPrefix(t, element.m_mapCE_, this.m_utilElement2_); 
      } 
    } 
    if (element.m_cPoints_.length() - element.m_cPointsOffset_ > 1 && (element.m_cPoints_.length() - element.m_cPointsOffset_ != 2 || !UTF16.isLeadSurrogate(element.m_cPoints_.charAt(0)) || !UTF16.isTrailSurrogate(element.m_cPoints_.charAt(1)))) {
      this.m_utilCanIter_.setSource(element.m_cPoints_);
      String source = this.m_utilCanIter_.next();
      while (source != null && source.length() > 0) {
        if (Normalizer.quickCheck(source, Normalizer.FCD, 0) != Normalizer.NO) {
          element.m_uchars_ = source;
          element.m_cPoints_ = element.m_uchars_;
          finalizeAddition(t, element);
        } 
        source = this.m_utilCanIter_.next();
      } 
      return element.m_mapCE_;
    } 
    return finalizeAddition(t, element);
  }
  
  private static final int addExpansion(List<Integer> expansions, int value) {
    expansions.add(Integer.valueOf(value));
    return expansions.size() - 1;
  }
  
  private static int setMaxExpansion(int endexpansion, byte expansionsize, MaxExpansionTable maxexpansion) {
    int start = 0;
    int limit = maxexpansion.m_endExpansionCE_.size();
    long unsigned = endexpansion;
    unsigned &= 0xFFFFFFFFL;
    int result = -1;
    if (limit > 0) {
      while (start < limit - 1) {
        int mid = start + limit >> 1;
        long unsignedce = ((Integer)maxexpansion.m_endExpansionCE_.get(mid)).intValue();
        unsignedce &= 0xFFFFFFFFL;
        if (unsigned < unsignedce) {
          limit = mid;
          continue;
        } 
        start = mid;
      } 
      if (((Integer)maxexpansion.m_endExpansionCE_.get(start)).intValue() == endexpansion)
        result = start; 
    } 
    if (result > -1) {
      Object currentsize = maxexpansion.m_expansionCESize_.get(result);
      if (((Byte)currentsize).byteValue() < expansionsize)
        maxexpansion.m_expansionCESize_.set(result, Byte.valueOf(expansionsize)); 
    } else {
      maxexpansion.m_endExpansionCE_.add(start + 1, Integer.valueOf(endexpansion));
      maxexpansion.m_expansionCESize_.add(start + 1, Byte.valueOf(expansionsize));
    } 
    return maxexpansion.m_endExpansionCE_.size();
  }
  
  private static int setMaxJamoExpansion(char ch, int endexpansion, byte expansionsize, MaxJamoExpansionTable maxexpansion) {
    boolean isV = true;
    if (ch >= 'ᄀ' && ch <= 'ᄒ') {
      if (maxexpansion.m_maxLSize_ < expansionsize)
        maxexpansion.m_maxLSize_ = expansionsize; 
      return maxexpansion.m_endExpansionCE_.size();
    } 
    if (ch >= 'ᅡ' && ch <= 'ᅵ')
      if (maxexpansion.m_maxVSize_ < expansionsize)
        maxexpansion.m_maxVSize_ = expansionsize;  
    if (ch >= 'ᆨ' && ch <= 'ᇂ') {
      isV = false;
      if (maxexpansion.m_maxTSize_ < expansionsize)
        maxexpansion.m_maxTSize_ = expansionsize; 
    } 
    int pos = maxexpansion.m_endExpansionCE_.size();
    while (pos > 0) {
      pos--;
      if (((Integer)maxexpansion.m_endExpansionCE_.get(pos)).intValue() == endexpansion)
        return maxexpansion.m_endExpansionCE_.size(); 
    } 
    maxexpansion.m_endExpansionCE_.add(Integer.valueOf(endexpansion));
    maxexpansion.m_isV_.add(isV ? Boolean.TRUE : Boolean.FALSE);
    return maxexpansion.m_endExpansionCE_.size();
  }
  
  private int addPrefix(BuildTable t, int CE, Elements element) {
    ContractionTable contractions = t.m_contractions_;
    String oldCP = element.m_cPoints_;
    int oldCPOffset = element.m_cPointsOffset_;
    contractions.m_currentTag_ = 11;
    int size = element.m_prefixChars_.length() - element.m_prefix_;
    int j;
    for (j = 1; j < size; j++) {
      char ch = element.m_prefixChars_.charAt(j + element.m_prefix_);
      if (!UTF16.isTrailSurrogate(ch))
        unsafeCPSet(t.m_unsafeCP_, ch); 
    } 
    this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
    for (j = 0; j < size; j++) {
      int offset = element.m_prefixChars_.length() - j - 1;
      this.m_utilStringBuffer_.append(element.m_prefixChars_.charAt(offset));
    } 
    element.m_prefixChars_ = this.m_utilStringBuffer_.toString();
    element.m_prefix_ = 0;
    if (!UTF16.isTrailSurrogate(element.m_cPoints_.charAt(0)))
      unsafeCPSet(t.m_unsafeCP_, element.m_cPoints_.charAt(0)); 
    element.m_cPoints_ = element.m_prefixChars_;
    element.m_cPointsOffset_ = element.m_prefix_;
    if (!UTF16.isTrailSurrogate(element.m_cPoints_.charAt(element.m_cPoints_.length() - 1)))
      ContrEndCPSet(t.m_contrEndCP_, element.m_cPoints_.charAt(element.m_cPoints_.length() - 1)); 
    if (isJamo(element.m_prefixChars_.charAt(element.m_prefix_)))
      t.m_collator_.m_isJamoSpecial_ = true; 
    if (!isPrefix(CE)) {
      int firstContractionOffset = addContraction(contractions, 16777215, false, CE);
      int newCE = processContraction(contractions, element, -268435456);
      addContraction(contractions, firstContractionOffset, element.m_prefixChars_.charAt(element.m_prefix_), newCE);
      addContraction(contractions, firstContractionOffset, '￿', CE);
      CE = constructSpecialCE(11, firstContractionOffset);
    } else {
      char ch = element.m_prefixChars_.charAt(element.m_prefix_);
      int position = findCP(contractions, CE, ch);
      if (position > 0) {
        int eCE = getCE(contractions, CE, position);
        int newCE = processContraction(contractions, element, eCE);
        setContraction(contractions, CE, position, ch, newCE);
      } else {
        processContraction(contractions, element, -268435456);
        insertContraction(contractions, CE, ch, element.m_mapCE_);
      } 
    } 
    element.m_cPoints_ = oldCP;
    element.m_cPointsOffset_ = oldCPOffset;
    return CE;
  }
  
  private static final boolean isContraction(int CE) {
    return (isSpecial(CE) && getCETag(CE) == 2);
  }
  
  private static final boolean isPrefix(int CE) {
    return (isSpecial(CE) && getCETag(CE) == 11);
  }
  
  private static final boolean isSpecial(int CE) {
    return ((CE & 0xF0000000) == -268435456);
  }
  
  private static final int getCETag(int CE) {
    return (CE & 0xF000000) >>> 24;
  }
  
  private static final int getCE(ContractionTable table, int element, int position) {
    element &= 0xFFFFFF;
    BasicContractionTable tbl = getBasicContractionTable(table, element);
    if (tbl == null)
      return -268435456; 
    if (position > tbl.m_CEs_.size() || position == -1)
      return -268435456; 
    return ((Integer)tbl.m_CEs_.get(position)).intValue();
  }
  
  private static final void unsafeCPSet(byte[] table, char c) {
    int hash = c;
    if (hash >= 8448) {
      if (hash >= 55296 && hash <= 63743)
        return; 
      hash = (hash & 0x1FFF) + 256;
    } 
    table[hash >> 3] = (byte)(table[hash >> 3] | 1 << (hash & 0x7));
  }
  
  private static final void ContrEndCPSet(byte[] table, char c) {
    int hash = c;
    if (hash >= 8448)
      hash = (hash & 0x1FFF) + 256; 
    table[hash >> 3] = (byte)(table[hash >> 3] | 1 << (hash & 0x7));
  }
  
  private static int addContraction(ContractionTable table, int element, char codePoint, int value) {
    BasicContractionTable tbl = getBasicContractionTable(table, element);
    if (tbl == null) {
      tbl = addAContractionElement(table);
      element = table.m_elements_.size() - 1;
    } 
    tbl.m_CEs_.add(Integer.valueOf(value));
    tbl.m_codePoints_.append(codePoint);
    return constructSpecialCE(table.m_currentTag_, element);
  }
  
  private static BasicContractionTable addAContractionElement(ContractionTable table) {
    BasicContractionTable result = new BasicContractionTable();
    table.m_elements_.add(result);
    return result;
  }
  
  private static final int constructSpecialCE(int tag, int CE) {
    return 0xF0000000 | tag << 24 | CE & 0xFFFFFF;
  }
  
  private static int processContraction(ContractionTable contractions, Elements element, int existingCE) {
    int firstContractionOffset = 0;
    if (element.m_cPoints_.length() - element.m_cPointsOffset_ == 1) {
      if (isContractionTableElement(existingCE) && getCETag(existingCE) == contractions.m_currentTag_) {
        changeContraction(contractions, existingCE, false, element.m_mapCE_);
        changeContraction(contractions, existingCE, '￿', element.m_mapCE_);
        return existingCE;
      } 
      return element.m_mapCE_;
    } 
    element.m_cPointsOffset_++;
    if (!isContractionTableElement(existingCE)) {
      firstContractionOffset = addContraction(contractions, 16777215, false, existingCE);
      int newCE = processContraction(contractions, element, -268435456);
      addContraction(contractions, firstContractionOffset, element.m_cPoints_.charAt(element.m_cPointsOffset_), newCE);
      addContraction(contractions, firstContractionOffset, '￿', existingCE);
      existingCE = constructSpecialCE(contractions.m_currentTag_, firstContractionOffset);
    } else {
      int position = findCP(contractions, existingCE, element.m_cPoints_.charAt(element.m_cPointsOffset_));
      if (position > 0) {
        int eCE = getCE(contractions, existingCE, position);
        int newCE = processContraction(contractions, element, eCE);
        setContraction(contractions, existingCE, position, element.m_cPoints_.charAt(element.m_cPointsOffset_), newCE);
      } else {
        int newCE = processContraction(contractions, element, -268435456);
        insertContraction(contractions, existingCE, element.m_cPoints_.charAt(element.m_cPointsOffset_), newCE);
      } 
    } 
    element.m_cPointsOffset_--;
    return existingCE;
  }
  
  private static final boolean isContractionTableElement(int CE) {
    return (isSpecial(CE) && (getCETag(CE) == 2 || getCETag(CE) == 11));
  }
  
  private static int findCP(ContractionTable table, int element, char codePoint) {
    BasicContractionTable tbl = getBasicContractionTable(table, element);
    if (tbl == null)
      return -1; 
    int position = 0;
    while (codePoint > tbl.m_codePoints_.charAt(position)) {
      position++;
      if (position > tbl.m_codePoints_.length())
        return -1; 
    } 
    if (codePoint == tbl.m_codePoints_.charAt(position))
      return position; 
    return -1;
  }
  
  private static final BasicContractionTable getBasicContractionTable(ContractionTable table, int offset) {
    offset &= 0xFFFFFF;
    if (offset == 16777215)
      return null; 
    return table.m_elements_.get(offset);
  }
  
  private static final int changeContraction(ContractionTable table, int element, char codePoint, int newCE) {
    BasicContractionTable tbl = getBasicContractionTable(table, element);
    if (tbl == null)
      return 0; 
    int position = 0;
    while (codePoint > tbl.m_codePoints_.charAt(position)) {
      position++;
      if (position > tbl.m_codePoints_.length())
        return -268435456; 
    } 
    if (codePoint == tbl.m_codePoints_.charAt(position)) {
      tbl.m_CEs_.set(position, Integer.valueOf(newCE));
      return element & 0xFFFFFF;
    } 
    return -268435456;
  }
  
  private static final int setContraction(ContractionTable table, int element, int offset, char codePoint, int value) {
    element &= 0xFFFFFF;
    BasicContractionTable tbl = getBasicContractionTable(table, element);
    if (tbl == null) {
      tbl = addAContractionElement(table);
      element = table.m_elements_.size() - 1;
    } 
    tbl.m_CEs_.set(offset, Integer.valueOf(value));
    tbl.m_codePoints_.setCharAt(offset, codePoint);
    return constructSpecialCE(table.m_currentTag_, element);
  }
  
  private static final int insertContraction(ContractionTable table, int element, char codePoint, int value) {
    element &= 0xFFFFFF;
    BasicContractionTable tbl = getBasicContractionTable(table, element);
    if (tbl == null) {
      tbl = addAContractionElement(table);
      element = table.m_elements_.size() - 1;
    } 
    int offset = 0;
    while (tbl.m_codePoints_.charAt(offset) < codePoint && offset < tbl.m_codePoints_.length())
      offset++; 
    tbl.m_CEs_.add(offset, Integer.valueOf(value));
    tbl.m_codePoints_.insert(offset, codePoint);
    return constructSpecialCE(table.m_currentTag_, element);
  }
  
  private static final int finalizeAddition(BuildTable t, Elements element) {
    int CE = -268435456;
    if (element.m_mapCE_ == 0)
      for (int i = 0; i < element.m_cPoints_.length(); i++) {
        char ch = element.m_cPoints_.charAt(i);
        if (!UTF16.isTrailSurrogate(ch))
          unsafeCPSet(t.m_unsafeCP_, ch); 
      }  
    if (element.m_cPoints_.length() - element.m_cPointsOffset_ > 1) {
      int cp = UTF16.charAt(element.m_cPoints_, element.m_cPointsOffset_);
      CE = t.m_mapping_.getValue(cp);
      CE = addContraction(t, CE, element);
    } else {
      CE = t.m_mapping_.getValue(element.m_cPoints_.charAt(element.m_cPointsOffset_));
      if (CE != -268435456) {
        if (isContractionTableElement(CE)) {
          if (!isPrefix(element.m_mapCE_)) {
            setContraction(t.m_contractions_, CE, 0, false, element.m_mapCE_);
            changeLastCE(t.m_contractions_, CE, element.m_mapCE_);
          } 
        } else {
          t.m_mapping_.setValue(element.m_cPoints_.charAt(element.m_cPointsOffset_), element.m_mapCE_);
          if (element.m_prefixChars_ != null && element.m_prefixChars_.length() > 0 && getCETag(CE) != 10) {
            Elements origElem = new Elements();
            origElem.m_prefixChars_ = null;
            origElem.m_uchars_ = element.m_cPoints_;
            origElem.m_cPoints_ = origElem.m_uchars_;
            origElem.m_CEs_[0] = CE;
            origElem.m_mapCE_ = CE;
            origElem.m_CELength_ = 1;
            finalizeAddition(t, origElem);
          } 
        } 
      } else {
        t.m_mapping_.setValue(element.m_cPoints_.charAt(element.m_cPointsOffset_), element.m_mapCE_);
      } 
    } 
    return CE;
  }
  
  private static int addContraction(BuildTable t, int CE, Elements element) {
    ContractionTable contractions = t.m_contractions_;
    contractions.m_currentTag_ = 2;
    int cp = UTF16.charAt(element.m_cPoints_, 0);
    int cpsize = 1;
    if (UCharacter.isSupplementary(cp))
      cpsize = 2; 
    if (cpsize < element.m_cPoints_.length()) {
      int size = element.m_cPoints_.length() - element.m_cPointsOffset_;
      for (int j = 1; j < size; j++) {
        if (!UTF16.isTrailSurrogate(element.m_cPoints_.charAt(element.m_cPointsOffset_ + j)))
          unsafeCPSet(t.m_unsafeCP_, element.m_cPoints_.charAt(element.m_cPointsOffset_ + j)); 
      } 
      if (!UTF16.isTrailSurrogate(element.m_cPoints_.charAt(element.m_cPoints_.length() - 1)))
        ContrEndCPSet(t.m_contrEndCP_, element.m_cPoints_.charAt(element.m_cPoints_.length() - 1)); 
      if (isJamo(element.m_cPoints_.charAt(element.m_cPointsOffset_)))
        t.m_collator_.m_isJamoSpecial_ = true; 
      element.m_cPointsOffset_ += cpsize;
      if (!isContraction(CE)) {
        int firstContractionOffset = addContraction(contractions, 16777215, false, CE);
        int newCE = processContraction(contractions, element, -268435456);
        addContraction(contractions, firstContractionOffset, element.m_cPoints_.charAt(element.m_cPointsOffset_), newCE);
        addContraction(contractions, firstContractionOffset, '￿', CE);
        CE = constructSpecialCE(2, firstContractionOffset);
      } else {
        int position = findCP(contractions, CE, element.m_cPoints_.charAt(element.m_cPointsOffset_));
        if (position > 0) {
          int eCE = getCE(contractions, CE, position);
          int newCE = processContraction(contractions, element, eCE);
          setContraction(contractions, CE, position, element.m_cPoints_.charAt(element.m_cPointsOffset_), newCE);
        } else {
          int newCE = processContraction(contractions, element, -268435456);
          insertContraction(contractions, CE, element.m_cPoints_.charAt(element.m_cPointsOffset_), newCE);
        } 
      } 
      element.m_cPointsOffset_ -= cpsize;
      t.m_mapping_.setValue(cp, CE);
    } else if (!isContraction(CE)) {
      t.m_mapping_.setValue(cp, element.m_mapCE_);
    } else {
      changeContraction(contractions, CE, false, element.m_mapCE_);
      changeContraction(contractions, CE, '￿', element.m_mapCE_);
    } 
    return CE;
  }
  
  private static final int changeLastCE(ContractionTable table, int element, int value) {
    BasicContractionTable tbl = getBasicContractionTable(table, element);
    if (tbl == null)
      return 0; 
    tbl.m_CEs_.set(tbl.m_CEs_.size() - 1, Integer.valueOf(value));
    return constructSpecialCE(table.m_currentTag_, element & 0xFFFFFF);
  }
  
  private static int nextWeight(CEGenerator cegenerator) {
    if (cegenerator.m_rangesLength_ > 0) {
      int maxByte = (cegenerator.m_ranges_[0]).m_count_;
      int weight = (cegenerator.m_ranges_[0]).m_start_;
      if (weight == (cegenerator.m_ranges_[0]).m_end_) {
        cegenerator.m_rangesLength_--;
        if (cegenerator.m_rangesLength_ > 0) {
          System.arraycopy(cegenerator.m_ranges_, 1, cegenerator.m_ranges_, 0, cegenerator.m_rangesLength_);
          (cegenerator.m_ranges_[0]).m_count_ = maxByte;
        } 
      } else {
        (cegenerator.m_ranges_[0]).m_start_ = incWeight(weight, (cegenerator.m_ranges_[0]).m_length2_, maxByte);
      } 
      return weight;
    } 
    return -1;
  }
  
  private static final int incWeight(int weight, int length, int maxByte) {
    while (true) {
      int b = getWeightByte(weight, length);
      if (b < maxByte)
        return setWeightByte(weight, length, b + 1); 
      weight = setWeightByte(weight, length, 4);
      length--;
    } 
  }
  
  private static final int getWeightByte(int weight, int index) {
    return weight >> 4 - index << 3 & 0xFF;
  }
  
  private static final int setWeightByte(int weight, int index, int b) {
    int mask;
    index <<= 3;
    if (index < 32) {
      mask = -1 >>> index;
    } else {
      mask = 0;
    } 
    index = 32 - index;
    mask |= -256 << index;
    return weight & mask | b << index;
  }
  
  private int allocateWeights(int lowerLimit, int upperLimit, int n, int maxByte, WeightRange[] ranges) {
    int countBytes = maxByte - 4 + 1;
    this.m_utilLongBuffer_[0] = 1L;
    this.m_utilLongBuffer_[1] = countBytes;
    this.m_utilLongBuffer_[2] = this.m_utilLongBuffer_[1] * countBytes;
    this.m_utilLongBuffer_[3] = this.m_utilLongBuffer_[2] * countBytes;
    this.m_utilLongBuffer_[4] = this.m_utilLongBuffer_[3] * countBytes;
    int rangeCount = getWeightRanges(lowerLimit, upperLimit, maxByte, countBytes, ranges);
    if (rangeCount <= 0)
      return 0; 
    long maxCount = 0L;
    int i;
    for (i = 0; i < rangeCount; i++)
      maxCount += (ranges[i]).m_count_ * this.m_utilLongBuffer_[4 - (ranges[i]).m_length_]; 
    if (maxCount < n)
      return 0; 
    for (i = 0; i < rangeCount; i++) {
      (ranges[i]).m_length2_ = (ranges[i]).m_length_;
      (ranges[i]).m_count2_ = (ranges[i]).m_count_;
    } 
    while (true) {
      int minLength = (ranges[0]).m_length2_;
      Arrays.fill(this.m_utilCountBuffer_, 0);
      int j;
      for (j = 0; j < rangeCount; j++)
        this.m_utilCountBuffer_[(ranges[j]).m_length2_] = this.m_utilCountBuffer_[(ranges[j]).m_length2_] + (ranges[j]).m_count2_; 
      if (n <= this.m_utilCountBuffer_[minLength] + this.m_utilCountBuffer_[minLength + 1]) {
        maxCount = 0L;
        rangeCount = 0;
        do {
          maxCount += (ranges[rangeCount]).m_count2_;
          rangeCount++;
        } while (n > maxCount);
        break;
      } 
      if (n <= (ranges[0]).m_count2_ * countBytes) {
        rangeCount = 1;
        long power_1 = this.m_utilLongBuffer_[minLength - (ranges[0]).m_length_];
        long power = power_1 * countBytes;
        int count2 = (int)((n + power - 1L) / power);
        int count1 = (ranges[0]).m_count_ - count2;
        if (count1 < 1) {
          lengthenRange(ranges, 0, maxByte, countBytes);
          break;
        } 
        rangeCount = 2;
        (ranges[1]).m_end_ = (ranges[0]).m_end_;
        (ranges[1]).m_length_ = (ranges[0]).m_length_;
        (ranges[1]).m_length2_ = minLength;
        int k = (ranges[0]).m_length_;
        int b = getWeightByte((ranges[0]).m_start_, k) + count1 - 1;
        if (b <= maxByte) {
          (ranges[0]).m_end_ = setWeightByte((ranges[0]).m_start_, k, b);
        } else {
          (ranges[0]).m_end_ = setWeightByte(incWeight((ranges[0]).m_start_, k - 1, maxByte), k, b - countBytes);
        } 
        b = maxByte << 24 | maxByte << 16 | maxByte << 8 | maxByte;
        (ranges[0]).m_end_ = truncateWeight((ranges[0]).m_end_, k) | b >>> k << 3 & b << 4 - minLength << 3;
        (ranges[1]).m_start_ = incWeight((ranges[0]).m_end_, minLength, maxByte);
        (ranges[0]).m_count_ = count1;
        (ranges[1]).m_count_ = count2;
        (ranges[0]).m_count2_ = (int)(count1 * power_1);
        (ranges[1]).m_count2_ = (int)(count2 * power_1);
        lengthenRange(ranges, 1, maxByte, countBytes);
        break;
      } 
      for (j = 0; (ranges[j]).m_length2_ == minLength; j++)
        lengthenRange(ranges, j, maxByte, countBytes); 
    } 
    if (rangeCount > 1)
      Arrays.sort((Object[])ranges, 0, rangeCount); 
    (ranges[0]).m_count_ = maxByte;
    return rangeCount;
  }
  
  private static final int lengthenRange(WeightRange[] range, int offset, int maxByte, int countBytes) {
    int length = (range[offset]).m_length2_ + 1;
    (range[offset]).m_start_ = setWeightTrail((range[offset]).m_start_, length, 4);
    (range[offset]).m_end_ = setWeightTrail((range[offset]).m_end_, length, maxByte);
    (range[offset]).m_count2_ *= countBytes;
    (range[offset]).m_length2_ = length;
    return length;
  }
  
  private static final int setWeightTrail(int weight, int length, int trail) {
    length = 4 - length << 3;
    return weight & -256 << length | trail << length;
  }
  
  private int getWeightRanges(int lowerLimit, int upperLimit, int maxByte, int countBytes, WeightRange[] ranges) {
    int lowerLength = lengthOfWeight(lowerLimit);
    int upperLength = lengthOfWeight(upperLimit);
    if (Utility.compareUnsigned(lowerLimit, upperLimit) >= 0)
      return 0; 
    if (lowerLength < upperLength && 
      lowerLimit == truncateWeight(upperLimit, lowerLength))
      return 0; 
    for (int length = 0; length < 5; length++) {
      this.m_utilLowerWeightRange_[length].clear();
      this.m_utilUpperWeightRange_[length].clear();
    } 
    this.m_utilWeightRange_.clear();
    int weight = lowerLimit;
    int i;
    for (i = lowerLength; i >= 2; i--) {
      this.m_utilLowerWeightRange_[i].clear();
      int trail = getWeightByte(weight, i);
      if (trail < maxByte) {
        (this.m_utilLowerWeightRange_[i]).m_start_ = incWeightTrail(weight, i);
        (this.m_utilLowerWeightRange_[i]).m_end_ = setWeightTrail(weight, i, maxByte);
        (this.m_utilLowerWeightRange_[i]).m_length_ = i;
        (this.m_utilLowerWeightRange_[i]).m_count_ = maxByte - trail;
      } 
      weight = truncateWeight(weight, i - 1);
    } 
    this.m_utilWeightRange_.m_start_ = incWeightTrail(weight, 1);
    weight = upperLimit;
    for (i = upperLength; i >= 2; i--) {
      int trail = getWeightByte(weight, i);
      if (trail > 4) {
        (this.m_utilUpperWeightRange_[i]).m_start_ = setWeightTrail(weight, i, 4);
        (this.m_utilUpperWeightRange_[i]).m_end_ = decWeightTrail(weight, i);
        (this.m_utilUpperWeightRange_[i]).m_length_ = i;
        (this.m_utilUpperWeightRange_[i]).m_count_ = trail - 4;
      } 
      weight = truncateWeight(weight, i - 1);
    } 
    this.m_utilWeightRange_.m_end_ = decWeightTrail(weight, 1);
    this.m_utilWeightRange_.m_length_ = 1;
    if (Utility.compareUnsigned(this.m_utilWeightRange_.m_end_, this.m_utilWeightRange_.m_start_) >= 0) {
      this.m_utilWeightRange_.m_count_ = (this.m_utilWeightRange_.m_end_ - this.m_utilWeightRange_.m_start_ >>> 24) + 1;
    } else {
      this.m_utilWeightRange_.m_count_ = 0;
      for (i = 4; i >= 2; i--) {
        if ((this.m_utilLowerWeightRange_[i]).m_count_ > 0 && (this.m_utilUpperWeightRange_[i]).m_count_ > 0) {
          int start = (this.m_utilUpperWeightRange_[i]).m_start_;
          int end = (this.m_utilLowerWeightRange_[i]).m_end_;
          if (end >= start || incWeight(end, i, maxByte) == start) {
            start = (this.m_utilLowerWeightRange_[i]).m_start_;
            end = (this.m_utilLowerWeightRange_[i]).m_end_ = (this.m_utilUpperWeightRange_[i]).m_end_;
            (this.m_utilLowerWeightRange_[i]).m_count_ = getWeightByte(end, i) - getWeightByte(start, i) + 1 + countBytes * (getWeightByte(end, i - 1) - getWeightByte(start, i - 1));
            (this.m_utilUpperWeightRange_[i]).m_count_ = 0;
            while (--i >= 2)
              (this.m_utilUpperWeightRange_[i]).m_count_ = 0; 
            break;
          } 
        } 
      } 
    } 
    int rangeCount = 0;
    if (this.m_utilWeightRange_.m_count_ > 0) {
      ranges[0] = new WeightRange(this.m_utilWeightRange_);
      rangeCount = 1;
    } 
    for (int j = 2; j <= 4; j++) {
      if ((this.m_utilUpperWeightRange_[j]).m_count_ > 0) {
        ranges[rangeCount] = new WeightRange(this.m_utilUpperWeightRange_[j]);
        rangeCount++;
      } 
      if ((this.m_utilLowerWeightRange_[j]).m_count_ > 0) {
        ranges[rangeCount] = new WeightRange(this.m_utilLowerWeightRange_[j]);
        rangeCount++;
      } 
    } 
    return rangeCount;
  }
  
  private static final int truncateWeight(int weight, int length) {
    return weight & -1 << 4 - length << 3;
  }
  
  private static final int lengthOfWeight(int weight) {
    if ((weight & 0xFFFFFF) == 0)
      return 1; 
    if ((weight & 0xFFFF) == 0)
      return 2; 
    if ((weight & 0xFF) == 0)
      return 3; 
    return 4;
  }
  
  private static final int incWeightTrail(int weight, int length) {
    return weight + (1 << 4 - length << 3);
  }
  
  private static int decWeightTrail(int weight, int length) {
    return weight - (1 << 4 - length << 3);
  }
  
  private static int findCP(BasicContractionTable tbl, char codePoint) {
    int position = 0;
    while (codePoint > tbl.m_codePoints_.charAt(position)) {
      position++;
      if (position > tbl.m_codePoints_.length())
        return -1; 
    } 
    if (codePoint == tbl.m_codePoints_.charAt(position))
      return position; 
    return -1;
  }
  
  private static int findCE(ContractionTable table, int element, char ch) {
    if (table == null)
      return -268435456; 
    BasicContractionTable tbl = getBasicContractionTable(table, element);
    if (tbl == null)
      return -268435456; 
    int position = findCP(tbl, ch);
    if (position > tbl.m_CEs_.size() || position < 0)
      return -268435456; 
    return ((Integer)tbl.m_CEs_.get(position)).intValue();
  }
  
  private static boolean isTailored(ContractionTable table, int element, char[] array, int offset) {
    while (array[offset] != '\000') {
      element = findCE(table, element, array[offset]);
      if (element == -268435456)
        return false; 
      if (!isContractionTableElement(element))
        return true; 
      offset++;
    } 
    if (getCE(table, element, 0) != -268435456)
      return true; 
    return false;
  }
  
  private void assembleTable(BuildTable t, RuleBasedCollator collator) {
    IntTrieBuilder mapping = t.m_mapping_;
    List<Integer> expansions = t.m_expansions_;
    ContractionTable contractions = t.m_contractions_;
    MaxExpansionTable maxexpansion = t.m_maxExpansions_;
    collator.m_contractionOffset_ = 0;
    int contractionsSize = constructTable(contractions);
    getMaxExpansionJamo(mapping, maxexpansion, t.m_maxJamoExpansions_, collator.m_isJamoSpecial_);
    setAttributes(collator, t.m_options_);
    int size = expansions.size();
    collator.m_expansion_ = new int[size];
    int i;
    for (i = 0; i < size; i++)
      collator.m_expansion_[i] = ((Integer)expansions.get(i)).intValue(); 
    if (contractionsSize != 0) {
      collator.m_contractionIndex_ = new char[contractionsSize];
      contractions.m_codePoints_.getChars(0, contractionsSize, collator.m_contractionIndex_, 0);
      collator.m_contractionCE_ = new int[contractionsSize];
      for (i = 0; i < contractionsSize; i++)
        collator.m_contractionCE_[i] = ((Integer)contractions.m_CEs_.get(i)).intValue(); 
    } 
    collator.m_trie_ = mapping.serialize(t, RuleBasedCollator.DataManipulate.getInstance());
    collator.m_expansionOffset_ = 0;
    size = maxexpansion.m_endExpansionCE_.size();
    collator.m_expansionEndCE_ = new int[size - 1];
    for (i = 1; i < size; i++)
      collator.m_expansionEndCE_[i - 1] = ((Integer)maxexpansion.m_endExpansionCE_.get(i)).intValue(); 
    collator.m_expansionEndCEMaxSize_ = new byte[size - 1];
    for (i = 1; i < size; i++)
      collator.m_expansionEndCEMaxSize_[i - 1] = ((Byte)maxexpansion.m_expansionCESize_.get(i)).byteValue(); 
    unsafeCPAddCCNZ(t);
    for (i = 0; i < 1056; i++)
      t.m_unsafeCP_[i] = (byte)(t.m_unsafeCP_[i] | RuleBasedCollator.UCA_.m_unsafe_[i]); 
    collator.m_unsafe_ = t.m_unsafeCP_;
    for (i = 0; i < 1056; i++)
      t.m_contrEndCP_[i] = (byte)(t.m_contrEndCP_[i] | RuleBasedCollator.UCA_.m_contractionEnd_[i]); 
    collator.m_contractionEnd_ = t.m_contrEndCP_;
  }
  
  private static final void setAttributes(RuleBasedCollator collator, CollationRuleParser.OptionSet option) {
    collator.latinOneFailed_ = true;
    collator.m_caseFirst_ = option.m_caseFirst_;
    collator.setDecomposition(option.m_decomposition_);
    collator.setAlternateHandlingShifted(option.m_isAlternateHandlingShifted_);
    collator.setCaseLevel(option.m_isCaseLevel_);
    collator.setFrenchCollation(option.m_isFrenchCollation_);
    collator.m_isHiragana4_ = option.m_isHiragana4_;
    collator.setStrength(option.m_strength_);
    collator.m_variableTopValue_ = option.m_variableTopValue_;
    collator.m_reorderCodes_ = option.m_scriptOrder_;
    collator.latinOneFailed_ = false;
  }
  
  private int constructTable(ContractionTable table) {
    int tsize = table.m_elements_.size();
    if (tsize == 0)
      return 0; 
    table.m_offsets_.clear();
    int position = 0;
    for (int i = 0; i < tsize; i++) {
      table.m_offsets_.add(Integer.valueOf(position));
      position += ((BasicContractionTable)table.m_elements_.get(i)).m_CEs_.size();
    } 
    table.m_CEs_.clear();
    table.m_codePoints_.delete(0, table.m_codePoints_.length());
    StringBuilder cpPointer = table.m_codePoints_;
    List<Integer> CEPointer = table.m_CEs_;
    int j;
    for (j = 0; j < tsize; j++) {
      BasicContractionTable bct = table.m_elements_.get(j);
      int size = bct.m_CEs_.size();
      char ccMax = Character.MIN_VALUE;
      char ccMin = 'ÿ';
      int offset = CEPointer.size();
      CEPointer.add(bct.m_CEs_.get(0));
      int k;
      for (k = 1; k < size; k++) {
        char ch = bct.m_codePoints_.charAt(k);
        char cc = (char)(UCharacter.getCombiningClass(ch) & 0xFF);
        if (cc > ccMax)
          ccMax = cc; 
        if (cc < ccMin)
          ccMin = cc; 
        cpPointer.append(ch);
        CEPointer.add(bct.m_CEs_.get(k));
      } 
      cpPointer.insert(offset, (char)(((ccMin == ccMax) ? '\001' : Character.MIN_VALUE) | ccMax));
      for (k = 0; k < size; k++) {
        if (isContractionTableElement(((Integer)CEPointer.get(offset + k)).intValue())) {
          int ce = ((Integer)CEPointer.get(offset + k)).intValue();
          CEPointer.set(offset + k, Integer.valueOf(constructSpecialCE(getCETag(ce), ((Integer)table.m_offsets_.get(getContractionOffset(ce))).intValue())));
        } 
      } 
    } 
    for (j = 0; j <= 1114111; j++) {
      int CE = table.m_mapping_.getValue(j);
      if (isContractionTableElement(CE)) {
        CE = constructSpecialCE(getCETag(CE), ((Integer)table.m_offsets_.get(getContractionOffset(CE))).intValue());
        table.m_mapping_.setValue(j, CE);
      } 
    } 
    return position;
  }
  
  private static final int getContractionOffset(int ce) {
    return ce & 0xFFFFFF;
  }
  
  private static void getMaxExpansionJamo(IntTrieBuilder mapping, MaxExpansionTable maxexpansion, MaxJamoExpansionTable maxjamoexpansion, boolean jamospecial) {
    int VBASE = 4449;
    int TBASE = 4520;
    int VCOUNT = 21;
    int TCOUNT = 28;
    int v = VBASE + VCOUNT - 1;
    int t = TBASE + TCOUNT - 1;
    while (v >= VBASE) {
      int ce = mapping.getValue(v);
      if ((ce & 0xF0000000) != -268435456)
        setMaxExpansion(ce, (byte)2, maxexpansion); 
      v--;
    } 
    while (t >= TBASE) {
      int ce = mapping.getValue(t);
      if ((ce & 0xF0000000) != -268435456)
        setMaxExpansion(ce, (byte)3, maxexpansion); 
      t--;
    } 
    if (jamospecial) {
      int count = maxjamoexpansion.m_endExpansionCE_.size();
      byte maxTSize = (byte)(maxjamoexpansion.m_maxLSize_ + maxjamoexpansion.m_maxVSize_ + maxjamoexpansion.m_maxTSize_);
      byte maxVSize = (byte)(maxjamoexpansion.m_maxLSize_ + maxjamoexpansion.m_maxVSize_);
      while (count > 0) {
        count--;
        if (((Boolean)maxjamoexpansion.m_isV_.get(count)).booleanValue() == true) {
          setMaxExpansion(((Integer)maxjamoexpansion.m_endExpansionCE_.get(count)).intValue(), maxVSize, maxexpansion);
          continue;
        } 
        setMaxExpansion(((Integer)maxjamoexpansion.m_endExpansionCE_.get(count)).intValue(), maxTSize, maxexpansion);
      } 
    } 
  }
  
  private final void unsafeCPAddCCNZ(BuildTable t) {
    int i = buildCMTabFlag & ((t.cmLookup == null) ? 1 : 0);
    char[] cm = null;
    int[] index = new int[256];
    int count = 0;
    if (i != 0)
      cm = new char[65536]; 
    char c;
    for (c = Character.MIN_VALUE; c < Character.MAX_VALUE; c = (char)(c + 1)) {
      int fcd;
      if (UTF16.isLeadSurrogate(c)) {
        fcd = 0;
        if (this.m_nfcImpl_.singleLeadMightHaveNonZeroFCD16(c)) {
          int supp = Character.toCodePoint(c, '?');
          int suppLimit = supp + 1024;
          while (supp < suppLimit)
            fcd |= this.m_nfcImpl_.getFCD16FromNormData(supp++); 
        } 
      } else {
        fcd = this.m_nfcImpl_.getFCD16(c);
      } 
      if (fcd >= 256 || (UTF16.isLeadSurrogate(c) && fcd != 0)) {
        unsafeCPSet(t.m_unsafeCP_, c);
        if (i != 0) {
          int cc = fcd & 0xFF;
          int pos = (cc << 8) + index[cc];
          cm[pos] = c;
          index[cc] = index[cc] + 1;
          count++;
        } 
      } 
    } 
    if (t.m_prefixLookup_ != null) {
      Enumeration<Elements> els = Collections.enumeration(t.m_prefixLookup_.values());
      while (els.hasMoreElements()) {
        Elements e = els.nextElement();
        String comp = Normalizer.compose(e.m_cPoints_, false);
        unsafeCPSet(t.m_unsafeCP_, comp.charAt(0));
      } 
    } 
    if (i != 0) {
      t.cmLookup = new CombinClassTable();
      t.cmLookup.generate(cm, count, index);
    } 
  }
  
  private boolean enumCategoryRangeClosureCategory(BuildTable t, RuleBasedCollator collator, CollationElementIterator colEl, int start, int limit, int type) {
    if (type != 0 && type != 17)
      for (int u32 = start; u32 < limit; u32++) {
        String decomp = this.m_nfcImpl_.getDecomposition(u32);
        if (decomp != null) {
          String comp = UCharacter.toString(u32);
          if (!collator.equals(comp, decomp)) {
            this.m_utilElement_.m_cPoints_ = decomp;
            this.m_utilElement_.m_prefix_ = 0;
            Elements prefix = t.m_prefixLookup_.get(this.m_utilElement_);
            if (prefix == null) {
              this.m_utilElement_.m_cPoints_ = comp;
              this.m_utilElement_.m_prefix_ = 0;
              this.m_utilElement_.m_prefixChars_ = null;
              colEl.setText(decomp);
              int ce = colEl.next();
              this.m_utilElement_.m_CELength_ = 0;
              while (ce != -1) {
                this.m_utilElement_.m_CEs_[this.m_utilElement_.m_CELength_++] = ce;
                ce = colEl.next();
              } 
            } else {
              this.m_utilElement_.m_cPoints_ = comp;
              this.m_utilElement_.m_prefix_ = 0;
              this.m_utilElement_.m_prefixChars_ = null;
              this.m_utilElement_.m_CELength_ = 1;
              this.m_utilElement_.m_CEs_[0] = prefix.m_mapCE_;
            } 
            addAnElement(t, this.m_utilElement_);
          } 
        } 
      }  
    return true;
  }
  
  private static final boolean isJamo(char ch) {
    return ((ch >= 'ᄀ' && ch <= 'ᄒ') || (ch >= 'ᅵ' && ch <= 'ᅡ') || (ch >= 'ᆨ' && ch <= 'ᇂ'));
  }
  
  private void canonicalClosure(BuildTable t) {
    BuildTable temp = new BuildTable(t);
    assembleTable(temp, temp.m_collator_);
    CollationElementIterator coleiter = temp.m_collator_.getCollationElementIterator("");
    RangeValueIterator typeiter = UCharacter.getTypeIterator();
    RangeValueIterator.Element element = new RangeValueIterator.Element();
    while (typeiter.next(element))
      enumCategoryRangeClosureCategory(t, temp.m_collator_, coleiter, element.start, element.limit, element.value); 
    t.cmLookup = temp.cmLookup;
    temp.cmLookup = null;
    for (int i = 0; i < this.m_parser_.m_resultLength_; i++) {
      CollationRuleParser.Token tok = (this.m_parser_.m_listHeader_[i]).m_first_;
      this.m_utilElement_.clear();
      while (tok != null) {
        this.m_utilElement_.m_prefix_ = 0;
        this.m_utilElement_.m_cPointsOffset_ = 0;
        if (tok.m_prefix_ != 0) {
          int size = tok.m_prefix_ >> 24;
          int offset = tok.m_prefix_ & 0xFFFFFF;
          this.m_utilElement_.m_prefixChars_ = this.m_parser_.m_source_.substring(offset, offset + size);
          size = (tok.m_source_ >> 24) - (tok.m_prefix_ >> 24);
          offset = (tok.m_source_ & 0xFFFFFF) + (tok.m_prefix_ >> 24);
          this.m_utilElement_.m_uchars_ = this.m_parser_.m_source_.substring(offset, offset + size);
        } else {
          this.m_utilElement_.m_prefixChars_ = null;
          int offset = tok.m_source_ & 0xFFFFFF;
          int size = tok.m_source_ >>> 24;
          this.m_utilElement_.m_uchars_ = this.m_parser_.m_source_.substring(offset, offset + size);
        } 
        this.m_utilElement_.m_cPoints_ = this.m_utilElement_.m_uchars_;
        char firstCM = Character.MIN_VALUE, baseChar = firstCM;
        for (int j = 0; j < this.m_utilElement_.m_cPoints_.length() - this.m_utilElement_.m_cPointsOffset_; 
          j++) {
          int fcd = this.m_nfcImpl_.getFCD16(this.m_utilElement_.m_cPoints_.charAt(j));
          if ((fcd & 0xFF) == 0) {
            baseChar = this.m_utilElement_.m_cPoints_.charAt(j);
          } else if (baseChar != '\000' && firstCM == '\000') {
            firstCM = this.m_utilElement_.m_cPoints_.charAt(j);
          } 
        } 
        if (baseChar != '\000' && firstCM != '\000')
          addTailCanonicalClosures(t, temp.m_collator_, coleiter, baseChar, firstCM); 
        tok = tok.m_next_;
      } 
    } 
  }
  
  private void addTailCanonicalClosures(BuildTable t, RuleBasedCollator m_collator, CollationElementIterator colEl, char baseChar, char cMark) {
    if (t.cmLookup == null)
      return; 
    CombinClassTable cmLookup = t.cmLookup;
    int[] index = cmLookup.index;
    int cClass = this.m_nfcImpl_.getFCD16(cMark) & 0xFF;
    int maxIndex = 0;
    char[] precompCh = new char[256];
    int[] precompClass = new int[256];
    int precompLen = 0;
    Elements element = new Elements();
    if (cClass > 0)
      maxIndex = index[cClass - 1]; 
    for (int i = 0; i < maxIndex; i++) {
      StringBuilder decompBuf = new StringBuilder();
      decompBuf.append(baseChar).append(cmLookup.cPoints[i]);
      String comp = Normalizer.compose(decompBuf.toString(), false);
      if (comp.length() == 1) {
        precompCh[precompLen] = comp.charAt(0);
        precompClass[precompLen] = this.m_nfcImpl_.getFCD16(cmLookup.cPoints[i]) & 0xFF;
        precompLen++;
        StringBuilder decomp = new StringBuilder();
        for (int j = 0; j < this.m_utilElement_.m_cPoints_.length(); j++) {
          if (this.m_utilElement_.m_cPoints_.charAt(j) == cMark) {
            decomp.append(cmLookup.cPoints[i]);
          } else {
            decomp.append(this.m_utilElement_.m_cPoints_.charAt(j));
          } 
        } 
        comp = Normalizer.compose(decomp.toString(), false);
        StringBuilder buf = new StringBuilder(comp);
        buf.append(cMark);
        decomp.append(cMark);
        comp = buf.toString();
        element.m_cPoints_ = decomp.toString();
        element.m_CELength_ = 0;
        element.m_prefix_ = 0;
        Elements prefix = t.m_prefixLookup_.get(element);
        element.m_cPoints_ = comp;
        element.m_uchars_ = comp;
        if (prefix == null) {
          element.m_prefix_ = 0;
          element.m_prefixChars_ = null;
          colEl.setText(decomp.toString());
          int ce = colEl.next();
          element.m_CELength_ = 0;
          while (ce != -1) {
            element.m_CEs_[element.m_CELength_++] = ce;
            ce = colEl.next();
          } 
        } else {
          element.m_cPoints_ = comp;
          element.m_prefix_ = 0;
          element.m_prefixChars_ = null;
          element.m_CELength_ = 1;
          element.m_CEs_[0] = prefix.m_mapCE_;
        } 
        setMapCE(t, element);
        finalizeAddition(t, element);
        if (comp.length() > 2)
          addFCD4AccentedContractions(t, colEl, comp, element); 
        if (precompLen > 1)
          precompLen = addMultiCMontractions(t, colEl, element, precompCh, precompClass, precompLen, cMark, i, decomp.toString()); 
      } 
    } 
  }
  
  private void setMapCE(BuildTable t, Elements element) {
    List<Integer> expansions = t.m_expansions_;
    element.m_mapCE_ = 0;
    if (element.m_CELength_ == 2 && RuleBasedCollator.isContinuation(element.m_CEs_[1]) && (element.m_CEs_[1] & 0xFFFF3F) == 0 && (element.m_CEs_[0] >> 8 & 0xFF) == 5 && (element.m_CEs_[0] & 0xFF) == 5) {
      element.m_mapCE_ = 0xFC000000 | element.m_CEs_[0] >> 8 & 0xFFFF00 | element.m_CEs_[1] >> 24 & 0xFF;
    } else {
      int expansion = 0xF1000000 | addExpansion(expansions, element.m_CEs_[0]) << 4 & 0xFFFFF0;
      for (int i = 1; i < element.m_CELength_; i++)
        addExpansion(expansions, element.m_CEs_[i]); 
      if (element.m_CELength_ <= 15) {
        expansion |= element.m_CELength_;
      } else {
        addExpansion(expansions, 0);
      } 
      element.m_mapCE_ = expansion;
      setMaxExpansion(element.m_CEs_[element.m_CELength_ - 1], (byte)element.m_CELength_, t.m_maxExpansions_);
    } 
  }
  
  private int addMultiCMontractions(BuildTable t, CollationElementIterator colEl, Elements element, char[] precompCh, int[] precompClass, int maxComp, char cMark, int cmPos, String decomp) {
    CombinClassTable cmLookup = t.cmLookup;
    char[] combiningMarks = { cMark };
    int cMarkClass = UCharacter.getCombiningClass(cMark) & 0xFF;
    String comMark = new String(combiningMarks);
    int noOfPrecomposedChs = maxComp;
    for (int j = 0; j < maxComp; j++) {
      int count = 0;
      do {
        StringBuilder temp;
        String newDecomp;
        if (count == 0) {
          newDecomp = Normalizer.decompose(new String(precompCh, j, 1), false);
          temp = new StringBuilder(newDecomp);
          temp.append(cmLookup.cPoints[cmPos]);
          newDecomp = temp.toString();
        } else {
          temp = new StringBuilder(decomp);
          temp.append(precompCh[j]);
          newDecomp = temp.toString();
        } 
        String comp = Normalizer.compose(newDecomp, false);
        if (comp.length() != 1)
          continue; 
        temp.append(cMark);
        element.m_cPoints_ = temp.toString();
        element.m_CELength_ = 0;
        element.m_prefix_ = 0;
        Elements prefix = t.m_prefixLookup_.get(element);
        element.m_cPoints_ = comp + comMark;
        if (prefix == null) {
          element.m_prefix_ = 0;
          element.m_prefixChars_ = null;
          colEl.setText(temp.toString());
          int ce = colEl.next();
          element.m_CELength_ = 0;
          while (ce != -1) {
            element.m_CEs_[element.m_CELength_++] = ce;
            ce = colEl.next();
          } 
        } else {
          element.m_cPoints_ = comp;
          element.m_prefix_ = 0;
          element.m_prefixChars_ = null;
          element.m_CELength_ = 1;
          element.m_CEs_[0] = prefix.m_mapCE_;
        } 
        setMapCE(t, element);
        finalizeAddition(t, element);
        precompCh[noOfPrecomposedChs] = comp.charAt(0);
        precompClass[noOfPrecomposedChs] = cMarkClass;
        noOfPrecomposedChs++;
      } while (++count < 2 && precompClass[j] == cMarkClass);
    } 
    return noOfPrecomposedChs;
  }
  
  private void addFCD4AccentedContractions(BuildTable t, CollationElementIterator colEl, String data, Elements element) {
    String decomp = Normalizer.decompose(data, false);
    String comp = Normalizer.compose(data, false);
    element.m_cPoints_ = decomp;
    element.m_CELength_ = 0;
    element.m_prefix_ = 0;
    Elements prefix = t.m_prefixLookup_.get(element);
    if (prefix == null) {
      element.m_cPoints_ = comp;
      element.m_prefix_ = 0;
      element.m_prefixChars_ = null;
      element.m_CELength_ = 0;
      colEl.setText(decomp);
      int ce = colEl.next();
      element.m_CELength_ = 0;
      while (ce != -1) {
        element.m_CEs_[element.m_CELength_++] = ce;
        ce = colEl.next();
      } 
      addAnElement(t, element);
    } 
  }
  
  private void processUCACompleteIgnorables(BuildTable t) {
    TrieIterator trieiterator = new TrieIterator((Trie)RuleBasedCollator.UCA_.m_trie_);
    RangeValueIterator.Element element = new RangeValueIterator.Element();
    while (trieiterator.next(element)) {
      int start = element.start;
      int limit = element.limit;
      if (element.value == 0)
        while (start < limit) {
          int CE = t.m_mapping_.getValue(start);
          if (CE == -268435456) {
            this.m_utilElement_.m_prefix_ = 0;
            this.m_utilElement_.m_uchars_ = UCharacter.toString(start);
            this.m_utilElement_.m_cPoints_ = this.m_utilElement_.m_uchars_;
            this.m_utilElement_.m_cPointsOffset_ = 0;
            this.m_utilElement_.m_CELength_ = 1;
            this.m_utilElement_.m_CEs_[0] = 0;
            addAnElement(t, this.m_utilElement_);
          } 
          start++;
        }  
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\CollationParsedRuleBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */