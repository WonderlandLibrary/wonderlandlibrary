package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.util.VersionInfo;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public final class Normalizer2Impl {
  public static final class Hangul {
    public static final int JAMO_L_BASE = 4352;
    
    public static final int JAMO_V_BASE = 4449;
    
    public static final int JAMO_T_BASE = 4519;
    
    public static final int HANGUL_BASE = 44032;
    
    public static final int JAMO_L_COUNT = 19;
    
    public static final int JAMO_V_COUNT = 21;
    
    public static final int JAMO_T_COUNT = 28;
    
    public static final int JAMO_L_LIMIT = 4371;
    
    public static final int JAMO_V_LIMIT = 4470;
    
    public static final int JAMO_VT_COUNT = 588;
    
    public static final int HANGUL_COUNT = 11172;
    
    public static final int HANGUL_LIMIT = 55204;
    
    public static boolean isHangul(int c) {
      return (44032 <= c && c < 55204);
    }
    
    public static boolean isHangulWithoutJamoT(char c) {
      c = (char)(c - 44032);
      return (c < '⮤' && c % 28 == 0);
    }
    
    public static boolean isJamoL(int c) {
      return (4352 <= c && c < 4371);
    }
    
    public static boolean isJamoV(int c) {
      return (4449 <= c && c < 4470);
    }
    
    public static int decompose(int c, Appendable buffer) {
      try {
        c -= 44032;
        int c2 = c % 28;
        c /= 28;
        buffer.append((char)(4352 + c / 21));
        buffer.append((char)(4449 + c % 21));
        if (c2 == 0)
          return 2; 
        buffer.append((char)(4519 + c2));
        return 3;
      } catch (IOException e) {
        throw new RuntimeException(e);
      } 
    }
    
    public static void getRawDecomposition(int c, Appendable buffer) {
      try {
        int orig = c;
        c -= 44032;
        int c2 = c % 28;
        if (c2 == 0) {
          c /= 28;
          buffer.append((char)(4352 + c / 21));
          buffer.append((char)(4449 + c % 21));
        } else {
          buffer.append((char)(orig - c2));
          buffer.append((char)(4519 + c2));
        } 
      } catch (IOException e) {
        throw new RuntimeException(e);
      } 
    }
  }
  
  public static final class ReorderingBuffer implements Appendable {
    private final Normalizer2Impl impl;
    
    private final Appendable app;
    
    private final StringBuilder str;
    
    private final boolean appIsStringBuilder;
    
    private int reorderStart;
    
    private int lastCC;
    
    private int codePointStart;
    
    private int codePointLimit;
    
    public ReorderingBuffer(Normalizer2Impl ni, Appendable dest, int destCapacity) {
      this.impl = ni;
      this.app = dest;
      if (this.app instanceof StringBuilder) {
        this.appIsStringBuilder = true;
        this.str = (StringBuilder)dest;
        this.str.ensureCapacity(destCapacity);
        this.reorderStart = 0;
        if (this.str.length() == 0) {
          this.lastCC = 0;
        } else {
          setIterator();
          this.lastCC = previousCC();
          if (this.lastCC > 1)
            while (previousCC() > 1); 
          this.reorderStart = this.codePointLimit;
        } 
      } else {
        this.appIsStringBuilder = false;
        this.str = new StringBuilder();
        this.reorderStart = 0;
        this.lastCC = 0;
      } 
    }
    
    public boolean isEmpty() {
      return (this.str.length() == 0);
    }
    
    public int length() {
      return this.str.length();
    }
    
    public int getLastCC() {
      return this.lastCC;
    }
    
    public StringBuilder getStringBuilder() {
      return this.str;
    }
    
    public boolean equals(CharSequence s, int start, int limit) {
      return Normalizer2Impl.UTF16Plus.equal(this.str, 0, this.str.length(), s, start, limit);
    }
    
    public void setLastChar(char c) {
      this.str.setCharAt(this.str.length() - 1, c);
    }
    
    public void append(int c, int cc) {
      if (this.lastCC <= cc || cc == 0) {
        this.str.appendCodePoint(c);
        this.lastCC = cc;
        if (cc <= 1)
          this.reorderStart = this.str.length(); 
      } else {
        insert(c, cc);
      } 
    }
    
    public void append(CharSequence s, int start, int limit, int leadCC, int trailCC) {
      if (start == limit)
        return; 
      if (this.lastCC <= leadCC || leadCC == 0) {
        if (trailCC <= 1) {
          this.reorderStart = this.str.length() + limit - start;
        } else if (leadCC <= 1) {
          this.reorderStart = this.str.length() + 1;
        } 
        this.str.append(s, start, limit);
        this.lastCC = trailCC;
      } else {
        int c = Character.codePointAt(s, start);
        start += Character.charCount(c);
        insert(c, leadCC);
        while (start < limit) {
          c = Character.codePointAt(s, start);
          start += Character.charCount(c);
          if (start < limit) {
            leadCC = Normalizer2Impl.getCCFromYesOrMaybe(this.impl.getNorm16(c));
          } else {
            leadCC = trailCC;
          } 
          append(c, leadCC);
        } 
      } 
    }
    
    public ReorderingBuffer append(char c) {
      this.str.append(c);
      this.lastCC = 0;
      this.reorderStart = this.str.length();
      return this;
    }
    
    public void appendZeroCC(int c) {
      this.str.appendCodePoint(c);
      this.lastCC = 0;
      this.reorderStart = this.str.length();
    }
    
    public ReorderingBuffer append(CharSequence s) {
      if (s.length() != 0) {
        this.str.append(s);
        this.lastCC = 0;
        this.reorderStart = this.str.length();
      } 
      return this;
    }
    
    public ReorderingBuffer append(CharSequence s, int start, int limit) {
      if (start != limit) {
        this.str.append(s, start, limit);
        this.lastCC = 0;
        this.reorderStart = this.str.length();
      } 
      return this;
    }
    
    public void flush() {
      if (this.appIsStringBuilder) {
        this.reorderStart = this.str.length();
      } else {
        try {
          this.app.append(this.str);
          this.str.setLength(0);
          this.reorderStart = 0;
        } catch (IOException e) {
          throw new RuntimeException(e);
        } 
      } 
      this.lastCC = 0;
    }
    
    public ReorderingBuffer flushAndAppendZeroCC(CharSequence s, int start, int limit) {
      if (this.appIsStringBuilder) {
        this.str.append(s, start, limit);
        this.reorderStart = this.str.length();
      } else {
        try {
          this.app.append(this.str).append(s, start, limit);
          this.str.setLength(0);
          this.reorderStart = 0;
        } catch (IOException e) {
          throw new RuntimeException(e);
        } 
      } 
      this.lastCC = 0;
      return this;
    }
    
    public void remove() {
      this.str.setLength(0);
      this.lastCC = 0;
      this.reorderStart = 0;
    }
    
    public void removeSuffix(int suffixLength) {
      int oldLength = this.str.length();
      this.str.delete(oldLength - suffixLength, oldLength);
      this.lastCC = 0;
      this.reorderStart = this.str.length();
    }
    
    private void insert(int c, int cc) {
      setIterator();
      skipPrevious();
      while (previousCC() > cc);
      if (c <= 65535) {
        this.str.insert(this.codePointLimit, (char)c);
        if (cc <= 1)
          this.reorderStart = this.codePointLimit + 1; 
      } else {
        this.str.insert(this.codePointLimit, Character.toChars(c));
        if (cc <= 1)
          this.reorderStart = this.codePointLimit + 2; 
      } 
    }
    
    private void setIterator() {
      this.codePointStart = this.str.length();
    }
    
    private void skipPrevious() {
      this.codePointLimit = this.codePointStart;
      this.codePointStart = this.str.offsetByCodePoints(this.codePointStart, -1);
    }
    
    private int previousCC() {
      this.codePointLimit = this.codePointStart;
      if (this.reorderStart >= this.codePointStart)
        return 0; 
      int c = this.str.codePointBefore(this.codePointStart);
      this.codePointStart -= Character.charCount(c);
      if (c < 768)
        return 0; 
      return Normalizer2Impl.getCCFromYesOrMaybe(this.impl.getNorm16(c));
    }
  }
  
  public static final class UTF16Plus {
    public static boolean isSurrogateLead(int c) {
      return ((c & 0x400) == 0);
    }
    
    public static boolean equal(CharSequence s1, CharSequence s2) {
      if (s1 == s2)
        return true; 
      int length = s1.length();
      if (length != s2.length())
        return false; 
      for (int i = 0; i < length; i++) {
        if (s1.charAt(i) != s2.charAt(i))
          return false; 
      } 
      return true;
    }
    
    public static boolean equal(CharSequence s1, int start1, int limit1, CharSequence s2, int start2, int limit2) {
      if (limit1 - start1 != limit2 - start2)
        return false; 
      if (s1 == s2 && start1 == start2)
        return true; 
      while (start1 < limit1) {
        if (s1.charAt(start1++) != s2.charAt(start2++))
          return false; 
      } 
      return true;
    }
  }
  
  private static final class IsAcceptable implements ICUBinary.Authenticate {
    private IsAcceptable() {}
    
    public boolean isDataVersionAcceptable(byte[] version) {
      return (version[0] == 2);
    }
  }
  
  private static final IsAcceptable IS_ACCEPTABLE = new IsAcceptable();
  
  private static final byte[] DATA_FORMAT = new byte[] { 78, 114, 109, 50 };
  
  public Normalizer2Impl load(InputStream data) {
    try {
      BufferedInputStream bis = new BufferedInputStream(data);
      this.dataVersion = ICUBinary.readHeaderAndDataVersion(bis, DATA_FORMAT, IS_ACCEPTABLE);
      DataInputStream ds = new DataInputStream(bis);
      int indexesLength = ds.readInt() / 4;
      if (indexesLength <= 13)
        throw new IOException("Normalizer2 data: not enough indexes"); 
      int[] inIndexes = new int[indexesLength];
      inIndexes[0] = indexesLength * 4;
      for (int i = 1; i < indexesLength; i++)
        inIndexes[i] = ds.readInt(); 
      this.minDecompNoCP = inIndexes[8];
      this.minCompNoMaybeCP = inIndexes[9];
      this.minYesNo = inIndexes[10];
      this.minYesNoMappingsOnly = inIndexes[14];
      this.minNoNo = inIndexes[11];
      this.limitNoNo = inIndexes[12];
      this.minMaybeYes = inIndexes[13];
      int offset = inIndexes[0];
      int nextOffset = inIndexes[1];
      this.normTrie = Trie2_16.createFromSerialized(ds);
      int trieLength = this.normTrie.getSerializedLength();
      if (trieLength > nextOffset - offset)
        throw new IOException("Normalizer2 data: not enough bytes for normTrie"); 
      ds.skipBytes(nextOffset - offset - trieLength);
      offset = nextOffset;
      nextOffset = inIndexes[2];
      int numChars = (nextOffset - offset) / 2;
      if (numChars != 0) {
        char[] chars = new char[numChars];
        for (int k = 0; k < numChars; k++)
          chars[k] = ds.readChar(); 
        this.maybeYesCompositions = new String(chars);
        this.extraData = this.maybeYesCompositions.substring(65024 - this.minMaybeYes);
      } 
      offset = nextOffset;
      this.smallFCD = new byte[256];
      for (int j = 0; j < 256; j++)
        this.smallFCD[j] = ds.readByte(); 
      this.tccc180 = new int[384];
      int bits = 0;
      for (int c = 0; c < 384; bits >>= 1) {
        if ((c & 0xFF) == 0)
          bits = this.smallFCD[c >> 8]; 
        if ((bits & 0x1) != 0) {
          for (int k = 0; k < 32; k++, c++)
            this.tccc180[c] = getFCD16FromNormData(c) & 0xFF; 
        } else {
          c += 32;
        } 
      } 
      data.close();
      return this;
    } catch (IOException e) {
      throw new RuntimeException(e);
    } 
  }
  
  public Normalizer2Impl load(String name) {
    return load(ICUData.getRequiredStream(name));
  }
  
  public void addPropertyStarts(UnicodeSet set) {
    Iterator<Trie2.Range> trieIterator = this.normTrie.iterator();
    Trie2.Range range;
    while (trieIterator.hasNext() && !(range = trieIterator.next()).leadSurrogate)
      set.add(range.startCodePoint); 
    for (int c = 44032; c < 55204; c += 28) {
      set.add(c);
      set.add(c + 1);
    } 
    set.add(55204);
  }
  
  public void addCanonIterPropertyStarts(UnicodeSet set) {
    ensureCanonIterData();
    Iterator<Trie2.Range> trieIterator = this.canonIterData.iterator(segmentStarterMapper);
    Trie2.Range range;
    while (trieIterator.hasNext() && !(range = trieIterator.next()).leadSurrogate)
      set.add(range.startCodePoint); 
  }
  
  private static final Trie2.ValueMapper segmentStarterMapper = new Trie2.ValueMapper() {
      public int map(int in) {
        return in & Integer.MIN_VALUE;
      }
    };
  
  public static final int MIN_CCC_LCCC_CP = 768;
  
  public static final int MIN_YES_YES_WITH_CC = 65281;
  
  public static final int JAMO_VT = 65280;
  
  public static final int MIN_NORMAL_MAYBE_YES = 65024;
  
  public static final int JAMO_L = 1;
  
  public static final int MAX_DELTA = 64;
  
  public static final int IX_NORM_TRIE_OFFSET = 0;
  
  public static final int IX_EXTRA_DATA_OFFSET = 1;
  
  public static final int IX_SMALL_FCD_OFFSET = 2;
  
  public static final int IX_RESERVED3_OFFSET = 3;
  
  public static final int IX_TOTAL_SIZE = 7;
  
  public static final int IX_MIN_DECOMP_NO_CP = 8;
  
  public static final int IX_MIN_COMP_NO_MAYBE_CP = 9;
  
  public static final int IX_MIN_YES_NO = 10;
  
  public static final int IX_MIN_NO_NO = 11;
  
  public static final int IX_LIMIT_NO_NO = 12;
  
  public static final int IX_MIN_MAYBE_YES = 13;
  
  public static final int IX_MIN_YES_NO_MAPPINGS_ONLY = 14;
  
  public static final int IX_COUNT = 16;
  
  public static final int MAPPING_HAS_CCC_LCCC_WORD = 128;
  
  public static final int MAPPING_HAS_RAW_MAPPING = 64;
  
  public static final int MAPPING_NO_COMP_BOUNDARY_AFTER = 32;
  
  public static final int MAPPING_LENGTH_MASK = 31;
  
  public static final int COMP_1_LAST_TUPLE = 32768;
  
  public static final int COMP_1_TRIPLE = 1;
  
  public static final int COMP_1_TRAIL_LIMIT = 13312;
  
  public static final int COMP_1_TRAIL_MASK = 32766;
  
  public static final int COMP_1_TRAIL_SHIFT = 9;
  
  public static final int COMP_2_TRAIL_SHIFT = 6;
  
  public static final int COMP_2_TRAIL_MASK = 65472;
  
  private VersionInfo dataVersion;
  
  private int minDecompNoCP;
  
  private int minCompNoMaybeCP;
  
  private int minYesNo;
  
  private int minYesNoMappingsOnly;
  
  private int minNoNo;
  
  private int limitNoNo;
  
  private int minMaybeYes;
  
  private Trie2_16 normTrie;
  
  private String maybeYesCompositions;
  
  private String extraData;
  
  private byte[] smallFCD;
  
  private int[] tccc180;
  
  private Trie2_32 canonIterData;
  
  private ArrayList<UnicodeSet> canonStartSets;
  
  private static final int CANON_NOT_SEGMENT_STARTER = -2147483648;
  
  private static final int CANON_HAS_COMPOSITIONS = 1073741824;
  
  private static final int CANON_HAS_SET = 2097152;
  
  private static final int CANON_VALUE_MASK = 2097151;
  
  public Trie2_16 getNormTrie() {
    return this.normTrie;
  }
  
  public synchronized Normalizer2Impl ensureCanonIterData() {
    if (this.canonIterData == null) {
      Trie2Writable newData = new Trie2Writable(0, 0);
      this.canonStartSets = new ArrayList<UnicodeSet>();
      Iterator<Trie2.Range> trieIterator = this.normTrie.iterator();
      Trie2.Range range;
      while (trieIterator.hasNext() && !(range = trieIterator.next()).leadSurrogate) {
        int norm16 = range.value;
        if (norm16 == 0 || (this.minYesNo <= norm16 && norm16 < this.minNoNo))
          continue; 
        for (int c = range.startCodePoint; c <= range.endCodePoint; c++) {
          int oldValue = newData.get(c);
          int newValue = oldValue;
          if (norm16 >= this.minMaybeYes) {
            newValue |= Integer.MIN_VALUE;
            if (norm16 < 65024)
              newValue |= 0x40000000; 
          } else if (norm16 < this.minYesNo) {
            newValue |= 0x40000000;
          } else {
            int c2 = c;
            int norm16_2 = norm16;
            while (this.limitNoNo <= norm16_2 && norm16_2 < this.minMaybeYes) {
              c2 = mapAlgorithmic(c2, norm16_2);
              norm16_2 = getNorm16(c2);
            } 
            if (this.minYesNo <= norm16_2 && norm16_2 < this.limitNoNo) {
              int firstUnit = this.extraData.charAt(norm16_2);
              int length = firstUnit & 0x1F;
              if ((firstUnit & 0x80) != 0 && 
                c == c2 && (this.extraData.charAt(norm16_2 - 1) & 0xFF) != 0)
                newValue |= Integer.MIN_VALUE; 
              if (length != 0) {
                norm16_2++;
                int limit = norm16_2 + length;
                c2 = this.extraData.codePointAt(norm16_2);
                addToStartSet(newData, c, c2);
                if (norm16_2 >= this.minNoNo)
                  while ((norm16_2 += Character.charCount(c2)) < limit) {
                    c2 = this.extraData.codePointAt(norm16_2);
                    int c2Value = newData.get(c2);
                    if ((c2Value & Integer.MIN_VALUE) == 0)
                      newData.set(c2, c2Value | Integer.MIN_VALUE); 
                  }  
              } 
            } else {
              addToStartSet(newData, c, c2);
            } 
          } 
          if (newValue != oldValue)
            newData.set(c, newValue); 
        } 
      } 
      this.canonIterData = newData.toTrie2_32();
    } 
    return this;
  }
  
  public int getNorm16(int c) {
    return this.normTrie.get(c);
  }
  
  public int getCompQuickCheck(int norm16) {
    if (norm16 < this.minNoNo || 65281 <= norm16)
      return 1; 
    if (this.minMaybeYes <= norm16)
      return 2; 
    return 0;
  }
  
  public boolean isCompNo(int norm16) {
    return (this.minNoNo <= norm16 && norm16 < this.minMaybeYes);
  }
  
  public boolean isDecompYes(int norm16) {
    return (norm16 < this.minYesNo || this.minMaybeYes <= norm16);
  }
  
  public int getCC(int norm16) {
    if (norm16 >= 65024)
      return norm16 & 0xFF; 
    if (norm16 < this.minNoNo || this.limitNoNo <= norm16)
      return 0; 
    return getCCFromNoNo(norm16);
  }
  
  public static int getCCFromYesOrMaybe(int norm16) {
    return (norm16 >= 65024) ? (norm16 & 0xFF) : 0;
  }
  
  public int getFCD16(int c) {
    if (c < 0)
      return 0; 
    if (c < 384)
      return this.tccc180[c]; 
    if (c <= 65535 && 
      !singleLeadMightHaveNonZeroFCD16(c))
      return 0; 
    return getFCD16FromNormData(c);
  }
  
  public int getFCD16FromBelow180(int c) {
    return this.tccc180[c];
  }
  
  public boolean singleLeadMightHaveNonZeroFCD16(int lead) {
    byte bits = this.smallFCD[lead >> 8];
    if (bits == 0)
      return false; 
    return ((bits >> (lead >> 5 & 0x7) & 0x1) != 0);
  }
  
  public int getFCD16FromNormData(int c) {
    int norm16;
    while (true) {
      norm16 = getNorm16(c);
      if (norm16 <= this.minYesNo)
        return 0; 
      if (norm16 >= 65024) {
        norm16 &= 0xFF;
        return norm16 | norm16 << 8;
      } 
      if (norm16 >= this.minMaybeYes)
        return 0; 
      if (isDecompNoAlgorithmic(norm16)) {
        c = mapAlgorithmic(c, norm16);
        continue;
      } 
      break;
    } 
    int firstUnit = this.extraData.charAt(norm16);
    if ((firstUnit & 0x1F) == 0)
      return 511; 
    int fcd16 = firstUnit >> 8;
    if ((firstUnit & 0x80) != 0)
      fcd16 |= this.extraData.charAt(norm16 - 1) & 0xFF00; 
    return fcd16;
  }
  
  public String getDecomposition(int c) {
    int decomp = -1;
    int norm16;
    while (c >= this.minDecompNoCP && !isDecompYes(norm16 = getNorm16(c))) {
      if (isHangul(norm16)) {
        StringBuilder buffer = new StringBuilder();
        Hangul.decompose(c, buffer);
        return buffer.toString();
      } 
      if (isDecompNoAlgorithmic(norm16)) {
        decomp = c = mapAlgorithmic(c, norm16);
        continue;
      } 
      int length = this.extraData.charAt(norm16++) & 0x1F;
      return this.extraData.substring(norm16, norm16 + length);
    } 
    if (decomp < 0)
      return null; 
    return UTF16.valueOf(decomp);
  }
  
  public String getRawDecomposition(int c) {
    int norm16;
    if (c < this.minDecompNoCP || isDecompYes(norm16 = getNorm16(c)))
      return null; 
    if (isHangul(norm16)) {
      StringBuilder buffer = new StringBuilder();
      Hangul.getRawDecomposition(c, buffer);
      return buffer.toString();
    } 
    if (isDecompNoAlgorithmic(norm16))
      return UTF16.valueOf(mapAlgorithmic(c, norm16)); 
    int firstUnit = this.extraData.charAt(norm16);
    int mLength = firstUnit & 0x1F;
    if ((firstUnit & 0x40) != 0) {
      int rawMapping = norm16 - (firstUnit >> 7 & 0x1) - 1;
      char rm0 = this.extraData.charAt(rawMapping);
      if (rm0 <= '\037')
        return this.extraData.substring(rawMapping - rm0, rawMapping); 
      StringBuilder buffer = (new StringBuilder(mLength - 1)).append(rm0);
      norm16 += 3;
      return buffer.append(this.extraData, norm16, norm16 + mLength - 2).toString();
    } 
    norm16++;
    return this.extraData.substring(norm16, norm16 + mLength);
  }
  
  public boolean isCanonSegmentStarter(int c) {
    return (this.canonIterData.get(c) >= 0);
  }
  
  public boolean getCanonStartSet(int c, UnicodeSet set) {
    int canonValue = this.canonIterData.get(c) & Integer.MAX_VALUE;
    if (canonValue == 0)
      return false; 
    set.clear();
    int value = canonValue & 0x1FFFFF;
    if ((canonValue & 0x200000) != 0) {
      set.addAll(this.canonStartSets.get(value));
    } else if (value != 0) {
      set.add(value);
    } 
    if ((canonValue & 0x40000000) != 0) {
      int norm16 = getNorm16(c);
      if (norm16 == 1) {
        int syllable = 44032 + (c - 4352) * 588;
        set.add(syllable, syllable + 588 - 1);
      } else {
        addComposites(getCompositionsList(norm16), set);
      } 
    } 
    return true;
  }
  
  public int decompose(CharSequence s, int src, int limit, ReorderingBuffer buffer) {
    int minNoCP = this.minDecompNoCP;
    int c = 0;
    int norm16 = 0;
    int prevBoundary = src;
    int prevCC = 0;
    while (true) {
      int prevSrc;
      for (prevSrc = src; src != limit; ) {
        if ((c = s.charAt(src)) < minNoCP || isMostDecompYesAndZeroCC(norm16 = this.normTrie.getFromU16SingleLead((char)c))) {
          src++;
          continue;
        } 
        if (!UTF16.isSurrogate((char)c))
          break; 
        if (UTF16Plus.isSurrogateLead(c)) {
          char c2;
          if (src + 1 != limit && Character.isLowSurrogate(c2 = s.charAt(src + 1)))
            c = Character.toCodePoint((char)c, c2); 
        } else {
          char c2;
          if (prevSrc < src && Character.isHighSurrogate(c2 = s.charAt(src - 1))) {
            src--;
            c = Character.toCodePoint(c2, (char)c);
          } 
        } 
        if (isMostDecompYesAndZeroCC(norm16 = getNorm16(c)))
          src += Character.charCount(c); 
      } 
      if (src != prevSrc)
        if (buffer != null) {
          buffer.flushAndAppendZeroCC(s, prevSrc, src);
        } else {
          prevCC = 0;
          prevBoundary = src;
        }  
      if (src == limit)
        break; 
      src += Character.charCount(c);
      if (buffer != null) {
        decompose(c, norm16, buffer);
        continue;
      } 
      if (isDecompYes(norm16)) {
        int cc = getCCFromYesOrMaybe(norm16);
        if (prevCC <= cc || cc == 0) {
          prevCC = cc;
          if (cc <= 1)
            prevBoundary = src; 
          continue;
        } 
      } 
      return prevBoundary;
    } 
    return src;
  }
  
  public void decomposeAndAppend(CharSequence s, boolean doDecompose, ReorderingBuffer buffer) {
    int limit = s.length();
    if (limit == 0)
      return; 
    if (doDecompose) {
      decompose(s, 0, limit, buffer);
      return;
    } 
    int c = Character.codePointAt(s, 0);
    int src = 0;
    int cc = getCC(getNorm16(c)), prevCC = cc, firstCC = prevCC;
    while (cc != 0) {
      prevCC = cc;
      src += Character.charCount(c);
      if (src >= limit)
        break; 
      c = Character.codePointAt(s, src);
      cc = getCC(getNorm16(c));
    } 
    buffer.append(s, 0, src, firstCC, prevCC);
    buffer.append(s, src, limit);
  }
  
  public boolean compose(CharSequence s, int src, int limit, boolean onlyContiguous, boolean doCompose, ReorderingBuffer buffer) {
    int minNoMaybeCP = this.minCompNoMaybeCP;
    int prevBoundary = src;
    int c = 0;
    int norm16 = 0;
    int prevCC = 0;
    while (true) {
      int prevSrc;
      for (prevSrc = src; src != limit; ) {
        if ((c = s.charAt(src)) < minNoMaybeCP || isCompYesAndZeroCC(norm16 = this.normTrie.getFromU16SingleLead((char)c))) {
          src++;
          continue;
        } 
        if (!UTF16.isSurrogate((char)c))
          break; 
        if (UTF16Plus.isSurrogateLead(c)) {
          char c2;
          if (src + 1 != limit && Character.isLowSurrogate(c2 = s.charAt(src + 1)))
            c = Character.toCodePoint((char)c, c2); 
        } else {
          char c2;
          if (prevSrc < src && Character.isHighSurrogate(c2 = s.charAt(src - 1))) {
            src--;
            c = Character.toCodePoint(c2, (char)c);
          } 
        } 
        if (isCompYesAndZeroCC(norm16 = getNorm16(c)))
          src += Character.charCount(c); 
      } 
      if (src != prevSrc) {
        if (src == limit) {
          if (doCompose)
            buffer.flushAndAppendZeroCC(s, prevSrc, src); 
          break;
        } 
        prevBoundary = src - 1;
        if (Character.isLowSurrogate(s.charAt(prevBoundary)) && prevSrc < prevBoundary && Character.isHighSurrogate(s.charAt(prevBoundary - 1)))
          prevBoundary--; 
        if (doCompose) {
          buffer.flushAndAppendZeroCC(s, prevSrc, prevBoundary);
          buffer.append(s, prevBoundary, src);
        } else {
          prevCC = 0;
        } 
        prevSrc = src;
      } else if (src == limit) {
        break;
      } 
      src += Character.charCount(c);
      if (isJamoVT(norm16) && prevBoundary != prevSrc) {
        char prev = s.charAt(prevSrc - 1);
        boolean needToDecompose = false;
        if (c < 4519) {
          prev = (char)(prev - 4352);
          if (prev < '\023') {
            if (!doCompose)
              return false; 
            char syllable = (char)(44032 + (prev * 21 + c - 4449) * 28);
            char t;
            if (src != limit && (t = (char)(s.charAt(src) - 4519)) < '\034') {
              src++;
              syllable = (char)(syllable + t);
              prevBoundary = src;
              buffer.setLastChar(syllable);
              continue;
            } 
            needToDecompose = true;
          } 
        } else if (Hangul.isHangulWithoutJamoT(prev)) {
          if (!doCompose)
            return false; 
          buffer.setLastChar((char)(prev + c - 4519));
          prevBoundary = src;
          continue;
        } 
        if (!needToDecompose) {
          if (doCompose) {
            buffer.append((char)c);
            continue;
          } 
          prevCC = 0;
          continue;
        } 
      } 
      if (norm16 >= 65281) {
        int cc = norm16 & 0xFF;
        if (onlyContiguous && (doCompose ? buffer.getLastCC() : prevCC) == 0 && prevBoundary < prevSrc && getTrailCCFromCompYesAndZeroCC(s, prevBoundary, prevSrc) > cc) {
          if (!doCompose)
            return false; 
        } else {
          if (doCompose) {
            buffer.append(c, cc);
            continue;
          } 
          if (prevCC <= cc) {
            prevCC = cc;
            continue;
          } 
          return false;
        } 
      } else if (!doCompose && !isMaybeOrNonZeroCC(norm16)) {
        return false;
      } 
      if (hasCompBoundaryBefore(c, norm16)) {
        prevBoundary = prevSrc;
      } else if (doCompose) {
        buffer.removeSuffix(prevSrc - prevBoundary);
      } 
      src = findNextCompBoundary(s, src, limit);
      int recomposeStartIndex = buffer.length();
      decomposeShort(s, prevBoundary, src, buffer);
      recompose(buffer, recomposeStartIndex, onlyContiguous);
      if (!doCompose) {
        if (!buffer.equals(s, prevBoundary, src))
          return false; 
        buffer.remove();
        prevCC = 0;
      } 
      prevBoundary = src;
    } 
    return true;
  }
  
  public int composeQuickCheck(CharSequence s, int src, int limit, boolean onlyContiguous, boolean doSpan) {
    int qcResult = 0;
    int minNoMaybeCP = this.minCompNoMaybeCP;
    int prevBoundary = src;
    int c = 0;
    int norm16 = 0;
    int prevCC = 0;
    while (true) {
      int prevSrc = src;
      while (true) {
        if (src == limit)
          return src << 1 | qcResult; 
        if ((c = s.charAt(src)) < minNoMaybeCP || isCompYesAndZeroCC(norm16 = this.normTrie.getFromU16SingleLead((char)c))) {
          src++;
          continue;
        } 
        if (!UTF16.isSurrogate((char)c))
          break; 
        if (UTF16Plus.isSurrogateLead(c)) {
          char c2;
          if (src + 1 != limit && Character.isLowSurrogate(c2 = s.charAt(src + 1)))
            c = Character.toCodePoint((char)c, c2); 
        } else {
          char c2;
          if (prevSrc < src && Character.isHighSurrogate(c2 = s.charAt(src - 1))) {
            src--;
            c = Character.toCodePoint(c2, (char)c);
          } 
        } 
        if (isCompYesAndZeroCC(norm16 = getNorm16(c))) {
          src += Character.charCount(c);
          continue;
        } 
        break;
      } 
      if (src != prevSrc) {
        prevBoundary = src - 1;
        if (Character.isLowSurrogate(s.charAt(prevBoundary)) && prevSrc < prevBoundary && Character.isHighSurrogate(s.charAt(prevBoundary - 1)))
          prevBoundary--; 
        prevCC = 0;
        prevSrc = src;
      } 
      src += Character.charCount(c);
      if (isMaybeOrNonZeroCC(norm16)) {
        int cc = getCCFromYesOrMaybe(norm16);
        if (onlyContiguous && cc != 0 && prevCC == 0 && prevBoundary < prevSrc && getTrailCCFromCompYesAndZeroCC(s, prevBoundary, prevSrc) > cc)
          break; 
        if (prevCC <= cc || cc == 0) {
          prevCC = cc;
          if (norm16 < 65281) {
            if (!doSpan) {
              qcResult = 1;
              continue;
            } 
            return prevBoundary << 1;
          } 
          continue;
        } 
      } 
      break;
    } 
    return prevBoundary << 1;
  }
  
  public void composeAndAppend(CharSequence s, boolean doCompose, boolean onlyContiguous, ReorderingBuffer buffer) {
    int src = 0, limit = s.length();
    if (!buffer.isEmpty()) {
      int firstStarterInSrc = findNextCompBoundary(s, 0, limit);
      if (0 != firstStarterInSrc) {
        int lastStarterInDest = findPreviousCompBoundary(buffer.getStringBuilder(), buffer.length());
        StringBuilder middle = new StringBuilder(buffer.length() - lastStarterInDest + firstStarterInSrc + 16);
        middle.append(buffer.getStringBuilder(), lastStarterInDest, buffer.length());
        buffer.removeSuffix(buffer.length() - lastStarterInDest);
        middle.append(s, 0, firstStarterInSrc);
        compose(middle, 0, middle.length(), onlyContiguous, true, buffer);
        src = firstStarterInSrc;
      } 
    } 
    if (doCompose) {
      compose(s, src, limit, onlyContiguous, true, buffer);
    } else {
      buffer.append(s, src, limit);
    } 
  }
  
  public int makeFCD(CharSequence s, int src, int limit, ReorderingBuffer buffer) {
    int prevBoundary = src;
    int c = 0;
    int prevFCD16 = 0;
    int fcd16 = 0;
    while (true) {
      int prevSrc;
      for (prevSrc = src; src != limit; ) {
        if ((c = s.charAt(src)) < 768) {
          prevFCD16 = c ^ 0xFFFFFFFF;
          src++;
          continue;
        } 
        if (!singleLeadMightHaveNonZeroFCD16(c)) {
          prevFCD16 = 0;
          src++;
          continue;
        } 
        if (UTF16.isSurrogate((char)c))
          if (UTF16Plus.isSurrogateLead(c)) {
            char c2;
            if (src + 1 != limit && Character.isLowSurrogate(c2 = s.charAt(src + 1)))
              c = Character.toCodePoint((char)c, c2); 
          } else {
            char c2;
            if (prevSrc < src && Character.isHighSurrogate(c2 = s.charAt(src - 1))) {
              src--;
              c = Character.toCodePoint(c2, (char)c);
            } 
          }  
        if ((fcd16 = getFCD16FromNormData(c)) <= 255) {
          prevFCD16 = fcd16;
          src += Character.charCount(c);
        } 
      } 
      if (src != prevSrc) {
        if (src == limit) {
          if (buffer != null)
            buffer.flushAndAppendZeroCC(s, prevSrc, src); 
          break;
        } 
        prevBoundary = src;
        if (prevFCD16 < 0) {
          int prev = prevFCD16 ^ 0xFFFFFFFF;
          prevFCD16 = (prev < 384) ? this.tccc180[prev] : getFCD16FromNormData(prev);
          if (prevFCD16 > 1)
            prevBoundary--; 
        } else {
          int p = src - 1;
          if (Character.isLowSurrogate(s.charAt(p)) && prevSrc < p && Character.isHighSurrogate(s.charAt(p - 1))) {
            p--;
            prevFCD16 = getFCD16FromNormData(Character.toCodePoint(s.charAt(p), s.charAt(p + 1)));
          } 
          if (prevFCD16 > 1)
            prevBoundary = p; 
        } 
        if (buffer != null) {
          buffer.flushAndAppendZeroCC(s, prevSrc, prevBoundary);
          buffer.append(s, prevBoundary, src);
        } 
        prevSrc = src;
      } else if (src == limit) {
        break;
      } 
      src += Character.charCount(c);
      if ((prevFCD16 & 0xFF) <= fcd16 >> 8) {
        if ((fcd16 & 0xFF) <= 1)
          prevBoundary = src; 
        if (buffer != null)
          buffer.appendZeroCC(c); 
        prevFCD16 = fcd16;
        continue;
      } 
      if (buffer == null)
        return prevBoundary; 
      buffer.removeSuffix(prevSrc - prevBoundary);
      src = findNextFCDBoundary(s, src, limit);
      decomposeShort(s, prevBoundary, src, buffer);
      prevBoundary = src;
      prevFCD16 = 0;
    } 
    return src;
  }
  
  public void makeFCDAndAppend(CharSequence s, boolean doMakeFCD, ReorderingBuffer buffer) {
    int src = 0, limit = s.length();
    if (!buffer.isEmpty()) {
      int firstBoundaryInSrc = findNextFCDBoundary(s, 0, limit);
      if (0 != firstBoundaryInSrc) {
        int lastBoundaryInDest = findPreviousFCDBoundary(buffer.getStringBuilder(), buffer.length());
        StringBuilder middle = new StringBuilder(buffer.length() - lastBoundaryInDest + firstBoundaryInSrc + 16);
        middle.append(buffer.getStringBuilder(), lastBoundaryInDest, buffer.length());
        buffer.removeSuffix(buffer.length() - lastBoundaryInDest);
        middle.append(s, 0, firstBoundaryInSrc);
        makeFCD(middle, 0, middle.length(), buffer);
        src = firstBoundaryInSrc;
      } 
    } 
    if (doMakeFCD) {
      makeFCD(s, src, limit, buffer);
    } else {
      buffer.append(s, src, limit);
    } 
  }
  
  public boolean hasDecompBoundary(int c, boolean before) {
    int norm16;
    while (true) {
      if (c < this.minDecompNoCP)
        return true; 
      norm16 = getNorm16(c);
      if (isHangul(norm16) || isDecompYesAndZeroCC(norm16))
        return true; 
      if (norm16 > 65024)
        return false; 
      if (isDecompNoAlgorithmic(norm16)) {
        c = mapAlgorithmic(c, norm16);
        continue;
      } 
      break;
    } 
    int firstUnit = this.extraData.charAt(norm16);
    if ((firstUnit & 0x1F) == 0)
      return false; 
    if (!before) {
      if (firstUnit > 511)
        return false; 
      if (firstUnit <= 255)
        return true; 
    } 
    return ((firstUnit & 0x80) == 0 || (this.extraData.charAt(norm16 - 1) & 0xFF00) == 0);
  }
  
  public boolean isDecompInert(int c) {
    return isDecompYesAndZeroCC(getNorm16(c));
  }
  
  public boolean hasCompBoundaryBefore(int c) {
    return (c < this.minCompNoMaybeCP || hasCompBoundaryBefore(c, getNorm16(c)));
  }
  
  public boolean hasCompBoundaryAfter(int c, boolean onlyContiguous, boolean testInert) {
    int norm16;
    while (true) {
      norm16 = getNorm16(c);
      if (isInert(norm16))
        return true; 
      if (norm16 <= this.minYesNo)
        return (isHangul(norm16) && !Hangul.isHangulWithoutJamoT((char)c)); 
      if (norm16 >= (testInert ? this.minNoNo : this.minMaybeYes))
        return false; 
      if (isDecompNoAlgorithmic(norm16)) {
        c = mapAlgorithmic(c, norm16);
        continue;
      } 
      break;
    } 
    int firstUnit = this.extraData.charAt(norm16);
    return ((firstUnit & 0x20) == 0 && (!onlyContiguous || firstUnit <= 511));
  }
  
  public boolean hasFCDBoundaryBefore(int c) {
    return (c < 768 || getFCD16(c) <= 255);
  }
  
  public boolean hasFCDBoundaryAfter(int c) {
    int fcd16 = getFCD16(c);
    return (fcd16 <= 1 || (fcd16 & 0xFF) == 0);
  }
  
  public boolean isFCDInert(int c) {
    return (getFCD16(c) <= 1);
  }
  
  private boolean isMaybe(int norm16) {
    return (this.minMaybeYes <= norm16 && norm16 <= 65280);
  }
  
  private boolean isMaybeOrNonZeroCC(int norm16) {
    return (norm16 >= this.minMaybeYes);
  }
  
  private static boolean isInert(int norm16) {
    return (norm16 == 0);
  }
  
  private static boolean isJamoL(int norm16) {
    return (norm16 == 1);
  }
  
  private static boolean isJamoVT(int norm16) {
    return (norm16 == 65280);
  }
  
  private boolean isHangul(int norm16) {
    return (norm16 == this.minYesNo);
  }
  
  private boolean isCompYesAndZeroCC(int norm16) {
    return (norm16 < this.minNoNo);
  }
  
  private boolean isDecompYesAndZeroCC(int norm16) {
    return (norm16 < this.minYesNo || norm16 == 65280 || (this.minMaybeYes <= norm16 && norm16 <= 65024));
  }
  
  private boolean isMostDecompYesAndZeroCC(int norm16) {
    return (norm16 < this.minYesNo || norm16 == 65024 || norm16 == 65280);
  }
  
  private boolean isDecompNoAlgorithmic(int norm16) {
    return (norm16 >= this.limitNoNo);
  }
  
  private int getCCFromNoNo(int norm16) {
    if ((this.extraData.charAt(norm16) & 0x80) != 0)
      return this.extraData.charAt(norm16 - 1) & 0xFF; 
    return 0;
  }
  
  int getTrailCCFromCompYesAndZeroCC(CharSequence s, int cpStart, int cpLimit) {
    int c;
    if (cpStart == cpLimit - 1) {
      c = s.charAt(cpStart);
    } else {
      c = Character.codePointAt(s, cpStart);
    } 
    int prevNorm16 = getNorm16(c);
    if (prevNorm16 <= this.minYesNo)
      return 0; 
    return this.extraData.charAt(prevNorm16) >> 8;
  }
  
  private int mapAlgorithmic(int c, int norm16) {
    return c + norm16 - this.minMaybeYes - 64 - 1;
  }
  
  private int getCompositionsListForDecompYes(int norm16) {
    if (norm16 == 0 || 65024 <= norm16)
      return -1; 
    if ((norm16 -= this.minMaybeYes) < 0)
      norm16 += 65024; 
    return norm16;
  }
  
  private int getCompositionsListForComposite(int norm16) {
    int firstUnit = this.extraData.charAt(norm16);
    return 65024 - this.minMaybeYes + norm16 + 1 + (firstUnit & 0x1F);
  }
  
  private int getCompositionsList(int norm16) {
    return isDecompYes(norm16) ? getCompositionsListForDecompYes(norm16) : getCompositionsListForComposite(norm16);
  }
  
  public void decomposeShort(CharSequence s, int src, int limit, ReorderingBuffer buffer) {
    while (src < limit) {
      int c = Character.codePointAt(s, src);
      src += Character.charCount(c);
      decompose(c, getNorm16(c), buffer);
    } 
  }
  
  private void decompose(int c, int norm16, ReorderingBuffer buffer) {
    while (true) {
      int leadCC;
      if (isDecompYes(norm16)) {
        buffer.append(c, getCCFromYesOrMaybe(norm16));
        break;
      } 
      if (isHangul(norm16)) {
        Hangul.decompose(c, buffer);
        break;
      } 
      if (isDecompNoAlgorithmic(norm16)) {
        c = mapAlgorithmic(c, norm16);
        norm16 = getNorm16(c);
        continue;
      } 
      int firstUnit = this.extraData.charAt(norm16);
      int length = firstUnit & 0x1F;
      int trailCC = firstUnit >> 8;
      if ((firstUnit & 0x80) != 0) {
        leadCC = this.extraData.charAt(norm16 - 1) >> 8;
      } else {
        leadCC = 0;
      } 
      norm16++;
      buffer.append(this.extraData, norm16, norm16 + length, leadCC, trailCC);
      break;
    } 
  }
  
  private static int combine(String compositions, int list, int trail) {
    if (trail < 13312) {
      int key1 = trail << 1;
      int firstUnit;
      while (key1 > (firstUnit = compositions.charAt(list)))
        list += 2 + (firstUnit & 0x1); 
      if (key1 == (firstUnit & 0x7FFE)) {
        if ((firstUnit & 0x1) != 0)
          return compositions.charAt(list + 1) << 16 | compositions.charAt(list + 2); 
        return compositions.charAt(list + 1);
      } 
    } else {
      int key1 = 13312 + (trail >> 9 & 0xFFFFFFFE);
      int key2 = trail << 6 & 0xFFFF;
      while (true) {
        int firstUnit;
        while (key1 > (firstUnit = compositions.charAt(list)))
          list += 2 + (firstUnit & 0x1); 
        if (key1 == (firstUnit & 0x7FFE)) {
          int secondUnit;
          if (key2 > (secondUnit = compositions.charAt(list + 1))) {
            if ((firstUnit & 0x8000) != 0)
              break; 
            list += 3;
            continue;
          } 
          if (key2 == (secondUnit & 0xFFC0))
            return (secondUnit & 0xFFFF003F) << 16 | compositions.charAt(list + 2); 
        } 
        break;
      } 
    } 
    return -1;
  }
  
  private void addComposites(int list, UnicodeSet set) {
    int firstUnit;
    do {
      int compositeAndFwd;
      firstUnit = this.maybeYesCompositions.charAt(list);
      if ((firstUnit & 0x1) == 0) {
        compositeAndFwd = this.maybeYesCompositions.charAt(list + 1);
        list += 2;
      } else {
        compositeAndFwd = (this.maybeYesCompositions.charAt(list + 1) & 0xFFFF003F) << 16 | this.maybeYesCompositions.charAt(list + 2);
        list += 3;
      } 
      int composite = compositeAndFwd >> 1;
      if ((compositeAndFwd & 0x1) != 0)
        addComposites(getCompositionsListForComposite(getNorm16(composite)), set); 
      set.add(composite);
    } while ((firstUnit & 0x8000) == 0);
  }
  
  private void recompose(ReorderingBuffer buffer, int recomposeStartIndex, boolean onlyContiguous) {
    StringBuilder sb = buffer.getStringBuilder();
    int p = recomposeStartIndex;
    if (p == sb.length())
      return; 
    int compositionsList = -1;
    int starter = -1;
    boolean starterIsSupplementary = false;
    int prevCC = 0;
    while (true) {
      int c = sb.codePointAt(p);
      p += Character.charCount(c);
      int norm16 = getNorm16(c);
      int cc = getCCFromYesOrMaybe(norm16);
      if (isMaybe(norm16) && compositionsList >= 0 && (prevCC < cc || prevCC == 0)) {
        if (isJamoVT(norm16)) {
          if (c < 4519) {
            char prev = (char)(sb.charAt(starter) - 4352);
            if (prev < '\023') {
              int pRemove = p - 1;
              char syllable = (char)(44032 + (prev * 21 + c - 4449) * 28);
              char t;
              if (p != sb.length() && (t = (char)(sb.charAt(p) - 4519)) < '\034') {
                p++;
                syllable = (char)(syllable + t);
              } 
              sb.setCharAt(starter, syllable);
              sb.delete(pRemove, p);
              p = pRemove;
            } 
          } 
          if (p == sb.length())
            break; 
          compositionsList = -1;
          continue;
        } 
        int compositeAndFwd;
        if ((compositeAndFwd = combine(this.maybeYesCompositions, compositionsList, c)) >= 0) {
          int composite = compositeAndFwd >> 1;
          int pRemove = p - Character.charCount(c);
          sb.delete(pRemove, p);
          p = pRemove;
          if (starterIsSupplementary) {
            if (composite > 65535) {
              sb.setCharAt(starter, UTF16.getLeadSurrogate(composite));
              sb.setCharAt(starter + 1, UTF16.getTrailSurrogate(composite));
            } else {
              sb.setCharAt(starter, (char)c);
              sb.deleteCharAt(starter + 1);
              starterIsSupplementary = false;
              p--;
            } 
          } else if (composite > 65535) {
            starterIsSupplementary = true;
            sb.setCharAt(starter, UTF16.getLeadSurrogate(composite));
            sb.insert(starter + 1, UTF16.getTrailSurrogate(composite));
            p++;
          } else {
            sb.setCharAt(starter, (char)composite);
          } 
          if (p == sb.length())
            break; 
          if ((compositeAndFwd & 0x1) != 0) {
            compositionsList = getCompositionsListForComposite(getNorm16(composite));
            continue;
          } 
          compositionsList = -1;
          continue;
        } 
      } 
      prevCC = cc;
      if (p == sb.length())
        break; 
      if (cc == 0) {
        if ((compositionsList = getCompositionsListForDecompYes(norm16)) >= 0) {
          if (c <= 65535) {
            starterIsSupplementary = false;
            starter = p - 1;
            continue;
          } 
          starterIsSupplementary = true;
          starter = p - 2;
        } 
        continue;
      } 
      if (onlyContiguous)
        compositionsList = -1; 
    } 
    buffer.flush();
  }
  
  public int composePair(int a, int b) {
    int list, norm16 = getNorm16(a);
    if (isInert(norm16))
      return -1; 
    if (norm16 < this.minYesNoMappingsOnly) {
      if (isJamoL(norm16)) {
        b -= 4449;
        if (0 <= b && b < 21)
          return 44032 + ((a - 4352) * 21 + b) * 28; 
        return -1;
      } 
      if (isHangul(norm16)) {
        b -= 4519;
        if (Hangul.isHangulWithoutJamoT((char)a) && 0 < b && b < 28)
          return a + b; 
        return -1;
      } 
      list = norm16;
      if (norm16 > this.minYesNo)
        list += 1 + (this.extraData.charAt(list) & 0x1F); 
      list += 65024 - this.minMaybeYes;
    } else {
      if (norm16 < this.minMaybeYes || 65024 <= norm16)
        return -1; 
      list = norm16 - this.minMaybeYes;
    } 
    if (b < 0 || 1114111 < b)
      return -1; 
    return combine(this.maybeYesCompositions, list, b) >> 1;
  }
  
  private boolean hasCompBoundaryBefore(int c, int norm16) {
    while (true) {
      if (isCompYesAndZeroCC(norm16))
        return true; 
      if (isMaybeOrNonZeroCC(norm16))
        return false; 
      if (isDecompNoAlgorithmic(norm16)) {
        c = mapAlgorithmic(c, norm16);
        norm16 = getNorm16(c);
        continue;
      } 
      break;
    } 
    int firstUnit = this.extraData.charAt(norm16);
    if ((firstUnit & 0x1F) == 0)
      return false; 
    if ((firstUnit & 0x80) != 0 && (this.extraData.charAt(norm16 - 1) & 0xFF00) != 0)
      return false; 
    return isCompYesAndZeroCC(getNorm16(Character.codePointAt(this.extraData, norm16 + 1)));
  }
  
  private int findPreviousCompBoundary(CharSequence s, int p) {
    while (p > 0) {
      int c = Character.codePointBefore(s, p);
      p -= Character.charCount(c);
      if (hasCompBoundaryBefore(c))
        break; 
    } 
    return p;
  }
  
  private int findNextCompBoundary(CharSequence s, int p, int limit) {
    while (p < limit) {
      int c = Character.codePointAt(s, p);
      int norm16 = this.normTrie.get(c);
      if (hasCompBoundaryBefore(c, norm16))
        break; 
      p += Character.charCount(c);
    } 
    return p;
  }
  
  private int findPreviousFCDBoundary(CharSequence s, int p) {
    while (p > 0) {
      int c = Character.codePointBefore(s, p);
      p -= Character.charCount(c);
      if (c < 768 || getFCD16(c) <= 255)
        break; 
    } 
    return p;
  }
  
  private int findNextFCDBoundary(CharSequence s, int p, int limit) {
    while (p < limit) {
      int c = Character.codePointAt(s, p);
      if (c < 768 || getFCD16(c) <= 255)
        break; 
      p += Character.charCount(c);
    } 
    return p;
  }
  
  private void addToStartSet(Trie2Writable newData, int origin, int decompLead) {
    int canonValue = newData.get(decompLead);
    if ((canonValue & 0x3FFFFF) == 0 && origin != 0) {
      newData.set(decompLead, canonValue | origin);
    } else {
      UnicodeSet set;
      if ((canonValue & 0x200000) == 0) {
        int firstOrigin = canonValue & 0x1FFFFF;
        canonValue = canonValue & 0xFFE00000 | 0x200000 | this.canonStartSets.size();
        newData.set(decompLead, canonValue);
        this.canonStartSets.add(set = new UnicodeSet());
        if (firstOrigin != 0)
          set.add(firstOrigin); 
      } else {
        set = this.canonStartSets.get(canonValue & 0x1FFFFF);
      } 
      set.add(origin);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\Normalizer2Impl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */