package com.ibm.icu.impl;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.UnicodeSet;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.MissingResourceException;

public final class UCharacterName {
  public static final UCharacterName INSTANCE;
  
  public static final int LINES_PER_GROUP_ = 32;
  
  static {
    try {
      INSTANCE = new UCharacterName();
    } catch (IOException e) {
      throw new MissingResourceException("Could not construct UCharacterName. Missing unames.icu", "", "");
    } 
  }
  
  public int m_groupcount_ = 0;
  
  public String getName(int ch, int choice) {
    if (ch < 0 || ch > 1114111 || choice > 4)
      return null; 
    String result = null;
    result = getAlgName(ch, choice);
    if (result == null || result.length() == 0)
      if (choice == 2) {
        result = getExtendedName(ch);
      } else {
        result = getGroupName(ch, choice);
      }  
    return result;
  }
  
  public int getCharFromName(int choice, String name) {
    if (choice >= 4 || name == null || name.length() == 0)
      return -1; 
    int result = getExtendedChar(name.toLowerCase(Locale.ENGLISH), choice);
    if (result >= -1)
      return result; 
    String upperCaseName = name.toUpperCase(Locale.ENGLISH);
    if (choice == 0 || choice == 2) {
      int count = 0;
      if (this.m_algorithm_ != null)
        count = this.m_algorithm_.length; 
      for (; --count >= 0; count--) {
        result = this.m_algorithm_[count].getChar(upperCaseName);
        if (result >= 0)
          return result; 
      } 
    } 
    if (choice == 2) {
      result = getGroupChar(upperCaseName, 0);
      if (result == -1)
        result = getGroupChar(upperCaseName, 3); 
    } else {
      result = getGroupChar(upperCaseName, choice);
    } 
    return result;
  }
  
  public int getGroupLengths(int index, char[] offsets, char[] lengths) {
    char length = Character.MAX_VALUE;
    byte b = 0;
    byte n = 0;
    index *= this.m_groupsize_;
    int stringoffset = UCharacterUtility.toInt(this.m_groupinfo_[index + 1], this.m_groupinfo_[index + 2]);
    offsets[0] = Character.MIN_VALUE;
    for (int i = 0; i < 32; stringoffset++) {
      b = this.m_groupstring_[stringoffset];
      int shift = 4;
      while (shift >= 0) {
        n = (byte)(b >> shift & 0xF);
        if (length == Character.MAX_VALUE && n > 11) {
          length = (char)(n - 12 << 4);
        } else {
          if (length != Character.MAX_VALUE) {
            lengths[i] = (char)((length | n) + 12);
          } else {
            lengths[i] = (char)n;
          } 
          if (i < 32)
            offsets[i + 1] = (char)(offsets[i] + lengths[i]); 
          length = Character.MAX_VALUE;
          i++;
        } 
        shift -= 4;
      } 
    } 
    return stringoffset;
  }
  
  public String getGroupName(int index, int length, int choice) {
    if (choice != 0 && choice != 2)
      if (59 >= this.m_tokentable_.length || this.m_tokentable_[59] == Character.MAX_VALUE) {
        int fieldIndex = (choice == 4) ? 2 : choice;
        do {
          int oldindex = index;
          index += UCharacterUtility.skipByteSubString(this.m_groupstring_, index, length, (byte)59);
          length -= index - oldindex;
        } while (--fieldIndex > 0);
      } else {
        length = 0;
      }  
    synchronized (this.m_utilStringBuffer_) {
      this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
      for (int i = 0; i < length; ) {
        byte b = this.m_groupstring_[index + i];
        i++;
        if (b >= this.m_tokentable_.length) {
          if (b == 59)
            break; 
          this.m_utilStringBuffer_.append(b);
          continue;
        } 
        char token = this.m_tokentable_[b & 0xFF];
        if (token == '￾') {
          token = this.m_tokentable_[b << 8 | this.m_groupstring_[index + i] & 0xFF];
          i++;
        } 
        if (token == Character.MAX_VALUE) {
          if (b == 59) {
            if (this.m_utilStringBuffer_.length() == 0 && choice == 2)
              continue; 
            break;
          } 
          this.m_utilStringBuffer_.append((char)(b & 0xFF));
          continue;
        } 
        UCharacterUtility.getNullTermByteSubString(this.m_utilStringBuffer_, this.m_tokenstring_, token);
      } 
      if (this.m_utilStringBuffer_.length() > 0)
        return this.m_utilStringBuffer_.toString(); 
    } 
    return null;
  }
  
  public String getExtendedName(int ch) {
    String result = getName(ch, 0);
    if (result == null)
      if (result == null)
        result = getExtendedOr10Name(ch);  
    return result;
  }
  
  public int getGroup(int codepoint) {
    int endGroup = this.m_groupcount_;
    int msb = getCodepointMSB(codepoint);
    int result = 0;
    while (result < endGroup - 1) {
      int gindex = result + endGroup >> 1;
      if (msb < getGroupMSB(gindex)) {
        endGroup = gindex;
        continue;
      } 
      result = gindex;
    } 
    return result;
  }
  
  public String getExtendedOr10Name(int ch) {
    String result = null;
    if (result == null) {
      int type = getType(ch);
      if (type >= TYPE_NAMES_.length) {
        result = "unknown";
      } else {
        result = TYPE_NAMES_[type];
      } 
      synchronized (this.m_utilStringBuffer_) {
        this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
        this.m_utilStringBuffer_.append('<');
        this.m_utilStringBuffer_.append(result);
        this.m_utilStringBuffer_.append('-');
        String chStr = Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
        int zeros = 4 - chStr.length();
        while (zeros > 0) {
          this.m_utilStringBuffer_.append('0');
          zeros--;
        } 
        this.m_utilStringBuffer_.append(chStr);
        this.m_utilStringBuffer_.append('>');
        result = this.m_utilStringBuffer_.toString();
      } 
    } 
    return result;
  }
  
  public int getGroupMSB(int gindex) {
    if (gindex >= this.m_groupcount_)
      return -1; 
    return this.m_groupinfo_[gindex * this.m_groupsize_];
  }
  
  public static int getCodepointMSB(int codepoint) {
    return codepoint >> 5;
  }
  
  public static int getGroupLimit(int msb) {
    return (msb << 5) + 32;
  }
  
  public static int getGroupMin(int msb) {
    return msb << 5;
  }
  
  public static int getGroupOffset(int codepoint) {
    return codepoint & 0x1F;
  }
  
  public static int getGroupMinFromCodepoint(int codepoint) {
    return codepoint & 0xFFFFFFE0;
  }
  
  public int getAlgorithmLength() {
    return this.m_algorithm_.length;
  }
  
  public int getAlgorithmStart(int index) {
    return (this.m_algorithm_[index]).m_rangestart_;
  }
  
  public int getAlgorithmEnd(int index) {
    return (this.m_algorithm_[index]).m_rangeend_;
  }
  
  public String getAlgorithmName(int index, int codepoint) {
    String result = null;
    synchronized (this.m_utilStringBuffer_) {
      this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
      this.m_algorithm_[index].appendName(codepoint, this.m_utilStringBuffer_);
      result = this.m_utilStringBuffer_.toString();
    } 
    return result;
  }
  
  public synchronized String getGroupName(int ch, int choice) {
    int msb = getCodepointMSB(ch);
    int group = getGroup(ch);
    if (msb == this.m_groupinfo_[group * this.m_groupsize_]) {
      int index = getGroupLengths(group, this.m_groupoffsets_, this.m_grouplengths_);
      int offset = ch & 0x1F;
      return getGroupName(index + this.m_groupoffsets_[offset], this.m_grouplengths_[offset], choice);
    } 
    return null;
  }
  
  public int getMaxCharNameLength() {
    if (initNameSetsLengths())
      return this.m_maxNameLength_; 
    return 0;
  }
  
  public int getMaxISOCommentLength() {
    if (initNameSetsLengths())
      return this.m_maxISOCommentLength_; 
    return 0;
  }
  
  public void getCharNameCharacters(UnicodeSet set) {
    convert(this.m_nameSet_, set);
  }
  
  public void getISOCommentCharacters(UnicodeSet set) {
    convert(this.m_ISOCommentSet_, set);
  }
  
  static final class AlgorithmName {
    static final int TYPE_0_ = 0;
    
    static final int TYPE_1_ = 1;
    
    private int m_rangestart_;
    
    private int m_rangeend_;
    
    private byte m_type_;
    
    private byte m_variant_;
    
    private char[] m_factor_;
    
    private String m_prefix_;
    
    private byte[] m_factorstring_;
    
    boolean setInfo(int rangestart, int rangeend, byte type, byte variant) {
      if (rangestart >= 0 && rangestart <= rangeend && rangeend <= 1114111 && (type == 0 || type == 1)) {
        this.m_rangestart_ = rangestart;
        this.m_rangeend_ = rangeend;
        this.m_type_ = type;
        this.m_variant_ = variant;
        return true;
      } 
      return false;
    }
    
    boolean setFactor(char[] factor) {
      if (factor.length == this.m_variant_) {
        this.m_factor_ = factor;
        return true;
      } 
      return false;
    }
    
    boolean setPrefix(String prefix) {
      if (prefix != null && prefix.length() > 0) {
        this.m_prefix_ = prefix;
        return true;
      } 
      return false;
    }
    
    boolean setFactorString(byte[] string) {
      this.m_factorstring_ = string;
      return true;
    }
    
    boolean contains(int ch) {
      return (this.m_rangestart_ <= ch && ch <= this.m_rangeend_);
    }
    
    void appendName(int ch, StringBuffer str) {
      int offset, indexes[];
      str.append(this.m_prefix_);
      switch (this.m_type_) {
        case 0:
          str.append(Utility.hex(ch, this.m_variant_));
          break;
        case 1:
          offset = ch - this.m_rangestart_;
          indexes = this.m_utilIntBuffer_;
          synchronized (this.m_utilIntBuffer_) {
            for (int i = this.m_variant_ - 1; i > 0; i--) {
              int factor = this.m_factor_[i] & 0xFF;
              indexes[i] = offset % factor;
              offset /= factor;
            } 
            indexes[0] = offset;
            str.append(getFactorString(indexes, this.m_variant_));
          } 
          break;
      } 
    }
    
    int getChar(String name) {
      int ch, prefixlen = this.m_prefix_.length();
      if (name.length() < prefixlen || !this.m_prefix_.equals(name.substring(0, prefixlen)))
        return -1; 
      switch (this.m_type_) {
        case 0:
          try {
            int result = Integer.parseInt(name.substring(prefixlen), 16);
            if (this.m_rangestart_ <= result && result <= this.m_rangeend_)
              return result; 
          } catch (NumberFormatException e) {
            return -1;
          } 
          break;
        case 1:
          for (ch = this.m_rangestart_; ch <= this.m_rangeend_; ch++) {
            int offset = ch - this.m_rangestart_;
            int[] indexes = this.m_utilIntBuffer_;
            synchronized (this.m_utilIntBuffer_) {
              for (int i = this.m_variant_ - 1; i > 0; i--) {
                int factor = this.m_factor_[i] & 0xFF;
                indexes[i] = offset % factor;
                offset /= factor;
              } 
              indexes[0] = offset;
              if (compareFactorString(indexes, this.m_variant_, name, prefixlen))
                return ch; 
            } 
          } 
          break;
      } 
      return -1;
    }
    
    int add(int[] set, int maxlength) {
      int i, length = UCharacterName.add(set, this.m_prefix_);
      switch (this.m_type_) {
        case 0:
          length += this.m_variant_;
          break;
        case 1:
          for (i = this.m_variant_ - 1; i > 0; i--) {
            int maxfactorlength = 0;
            int count = 0;
            for (int factor = this.m_factor_[i]; factor > 0; factor--) {
              synchronized (this.m_utilStringBuffer_) {
                this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
                count = UCharacterUtility.getNullTermByteSubString(this.m_utilStringBuffer_, this.m_factorstring_, count);
                UCharacterName.add(set, this.m_utilStringBuffer_);
                if (this.m_utilStringBuffer_.length() > maxfactorlength)
                  maxfactorlength = this.m_utilStringBuffer_.length(); 
              } 
            } 
            length += maxfactorlength;
          } 
          break;
      } 
      if (length > maxlength)
        return length; 
      return maxlength;
    }
    
    private StringBuffer m_utilStringBuffer_ = new StringBuffer();
    
    private int[] m_utilIntBuffer_ = new int[256];
    
    private String getFactorString(int[] index, int length) {
      int size = this.m_factor_.length;
      if (index == null || length != size)
        return null; 
      synchronized (this.m_utilStringBuffer_) {
        this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
        int count = 0;
        size--;
        for (int i = 0; i <= size; i++) {
          int factor = this.m_factor_[i];
          count = UCharacterUtility.skipNullTermByteSubString(this.m_factorstring_, count, index[i]);
          count = UCharacterUtility.getNullTermByteSubString(this.m_utilStringBuffer_, this.m_factorstring_, count);
          if (i != size)
            count = UCharacterUtility.skipNullTermByteSubString(this.m_factorstring_, count, factor - index[i] - 1); 
        } 
        return this.m_utilStringBuffer_.toString();
      } 
    }
    
    private boolean compareFactorString(int[] index, int length, String str, int offset) {
      int size = this.m_factor_.length;
      if (index == null || length != size)
        return false; 
      int count = 0;
      int strcount = offset;
      size--;
      for (int i = 0; i <= size; i++) {
        int factor = this.m_factor_[i];
        count = UCharacterUtility.skipNullTermByteSubString(this.m_factorstring_, count, index[i]);
        strcount = UCharacterUtility.compareNullTermByteSubString(str, this.m_factorstring_, strcount, count);
        if (strcount < 0)
          return false; 
        if (i != size)
          count = UCharacterUtility.skipNullTermByteSubString(this.m_factorstring_, count, factor - index[i]); 
      } 
      if (strcount != str.length())
        return false; 
      return true;
    }
  }
  
  int m_groupsize_ = 0;
  
  private char[] m_tokentable_;
  
  private byte[] m_tokenstring_;
  
  private char[] m_groupinfo_;
  
  private byte[] m_groupstring_;
  
  private AlgorithmName[] m_algorithm_;
  
  boolean setToken(char[] token, byte[] tokenstring) {
    if (token != null && tokenstring != null && token.length > 0 && tokenstring.length > 0) {
      this.m_tokentable_ = token;
      this.m_tokenstring_ = tokenstring;
      return true;
    } 
    return false;
  }
  
  boolean setAlgorithm(AlgorithmName[] alg) {
    if (alg != null && alg.length != 0) {
      this.m_algorithm_ = alg;
      return true;
    } 
    return false;
  }
  
  boolean setGroupCountSize(int count, int size) {
    if (count <= 0 || size <= 0)
      return false; 
    this.m_groupcount_ = count;
    this.m_groupsize_ = size;
    return true;
  }
  
  boolean setGroup(char[] group, byte[] groupstring) {
    if (group != null && groupstring != null && group.length > 0 && groupstring.length > 0) {
      this.m_groupinfo_ = group;
      this.m_groupstring_ = groupstring;
      return true;
    } 
    return false;
  }
  
  private char[] m_groupoffsets_ = new char[33];
  
  private char[] m_grouplengths_ = new char[33];
  
  private static final String NAME_FILE_NAME_ = "data/icudt51b/unames.icu";
  
  private static final int GROUP_SHIFT_ = 5;
  
  private static final int GROUP_MASK_ = 31;
  
  private static final int NAME_BUFFER_SIZE_ = 100000;
  
  private static final int OFFSET_HIGH_OFFSET_ = 1;
  
  private static final int OFFSET_LOW_OFFSET_ = 2;
  
  private static final int SINGLE_NIBBLE_MAX_ = 11;
  
  private int[] m_nameSet_ = new int[8];
  
  private int[] m_ISOCommentSet_ = new int[8];
  
  private StringBuffer m_utilStringBuffer_ = new StringBuffer();
  
  private int[] m_utilIntBuffer_ = new int[2];
  
  private int m_maxISOCommentLength_;
  
  private int m_maxNameLength_;
  
  private static final String[] TYPE_NAMES_ = new String[] { 
      "unassigned", "uppercase letter", "lowercase letter", "titlecase letter", "modifier letter", "other letter", "non spacing mark", "enclosing mark", "combining spacing mark", "decimal digit number", 
      "letter number", "other number", "space separator", "line separator", "paragraph separator", "control", "format", "private use area", "surrogate", "dash punctuation", 
      "start punctuation", "end punctuation", "connector punctuation", "other punctuation", "math symbol", "currency symbol", "modifier symbol", "other symbol", "initial punctuation", "final punctuation", 
      "noncharacter", "lead surrogate", "trail surrogate" };
  
  private static final String UNKNOWN_TYPE_NAME_ = "unknown";
  
  private static final int NON_CHARACTER_ = 30;
  
  private static final int LEAD_SURROGATE_ = 31;
  
  private static final int TRAIL_SURROGATE_ = 32;
  
  static final int EXTENDED_CATEGORY_ = 33;
  
  private UCharacterName() throws IOException {
    InputStream is = ICUData.getRequiredStream("data/icudt51b/unames.icu");
    BufferedInputStream b = new BufferedInputStream(is, 100000);
    UCharacterNameReader reader = new UCharacterNameReader(b);
    reader.read(this);
    b.close();
  }
  
  private String getAlgName(int ch, int choice) {
    if (choice == 0 || choice == 2)
      synchronized (this.m_utilStringBuffer_) {
        this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
        for (int index = this.m_algorithm_.length - 1; index >= 0; index--) {
          if (this.m_algorithm_[index].contains(ch)) {
            this.m_algorithm_[index].appendName(ch, this.m_utilStringBuffer_);
            return this.m_utilStringBuffer_.toString();
          } 
        } 
      }  
    return null;
  }
  
  private synchronized int getGroupChar(String name, int choice) {
    for (int i = 0; i < this.m_groupcount_; i++) {
      int startgpstrindex = getGroupLengths(i, this.m_groupoffsets_, this.m_grouplengths_);
      int result = getGroupChar(startgpstrindex, this.m_grouplengths_, name, choice);
      if (result != -1)
        return this.m_groupinfo_[i * this.m_groupsize_] << 5 | result; 
    } 
    return -1;
  }
  
  private int getGroupChar(int index, char[] length, String name, int choice) {
    byte b = 0;
    int namelen = name.length();
    for (int result = 0; result <= 32; result++) {
      int nindex = 0;
      int len = length[result];
      if (choice != 0 && choice != 2) {
        int fieldIndex = (choice == 4) ? 2 : choice;
        do {
          int oldindex = index;
          index += UCharacterUtility.skipByteSubString(this.m_groupstring_, index, len, (byte)59);
          len -= index - oldindex;
        } while (--fieldIndex > 0);
      } 
      int count;
      for (count = 0; count < len && nindex != -1 && nindex < namelen; ) {
        b = this.m_groupstring_[index + count];
        count++;
        if (b >= this.m_tokentable_.length) {
          if (name.charAt(nindex++) != (b & 0xFF))
            nindex = -1; 
          continue;
        } 
        char token = this.m_tokentable_[b & 0xFF];
        if (token == '￾') {
          token = this.m_tokentable_[b << 8 | this.m_groupstring_[index + count] & 0xFF];
          count++;
        } 
        if (token == Character.MAX_VALUE) {
          if (name.charAt(nindex++) != (b & 0xFF))
            nindex = -1; 
          continue;
        } 
        nindex = UCharacterUtility.compareNullTermByteSubString(name, this.m_tokenstring_, nindex, token);
      } 
      if (namelen == nindex && (count == len || this.m_groupstring_[index + count] == 59))
        return result; 
      index += len;
    } 
    return -1;
  }
  
  private static int getType(int ch) {
    if (UCharacterUtility.isNonCharacter(ch))
      return 30; 
    int result = UCharacter.getType(ch);
    if (result == 18)
      if (ch <= 56319) {
        result = 31;
      } else {
        result = 32;
      }  
    return result;
  }
  
  private static int getExtendedChar(String name, int choice) {
    if (name.charAt(0) == '<') {
      if (choice == 2) {
        int endIndex = name.length() - 1;
        if (name.charAt(endIndex) == '>') {
          int startIndex = name.lastIndexOf('-');
          if (startIndex >= 0) {
            startIndex++;
            int result = -1;
            try {
              result = Integer.parseInt(name.substring(startIndex, endIndex), 16);
            } catch (NumberFormatException e) {
              return -1;
            } 
            String type = name.substring(1, startIndex - 1);
            int length = TYPE_NAMES_.length;
            for (int i = 0; i < length; i++) {
              if (type.compareTo(TYPE_NAMES_[i]) == 0) {
                if (getType(result) == i)
                  return result; 
                break;
              } 
            } 
          } 
        } 
      } 
      return -1;
    } 
    return -2;
  }
  
  private static void add(int[] set, char ch) {
    set[ch >>> 5] = set[ch >>> 5] | 1 << (ch & 0x1F);
  }
  
  private static boolean contains(int[] set, char ch) {
    return ((set[ch >>> 5] & 1 << (ch & 0x1F)) != 0);
  }
  
  private static int add(int[] set, String str) {
    int result = str.length();
    for (int i = result - 1; i >= 0; i--)
      add(set, str.charAt(i)); 
    return result;
  }
  
  private static int add(int[] set, StringBuffer str) {
    int result = str.length();
    for (int i = result - 1; i >= 0; i--)
      add(set, str.charAt(i)); 
    return result;
  }
  
  private int addAlgorithmName(int maxlength) {
    int result = 0;
    for (int i = this.m_algorithm_.length - 1; i >= 0; i--) {
      result = this.m_algorithm_[i].add(this.m_nameSet_, maxlength);
      if (result > maxlength)
        maxlength = result; 
    } 
    return maxlength;
  }
  
  private int addExtendedName(int maxlength) {
    for (int i = TYPE_NAMES_.length - 1; i >= 0; i--) {
      int length = 9 + add(this.m_nameSet_, TYPE_NAMES_[i]);
      if (length > maxlength)
        maxlength = length; 
    } 
    return maxlength;
  }
  
  private int[] addGroupName(int offset, int length, byte[] tokenlength, int[] set) {
    int resultnlength = 0;
    int resultplength = 0;
    while (resultplength < length) {
      char b = (char)(this.m_groupstring_[offset + resultplength] & 0xFF);
      resultplength++;
      if (b == ';')
        break; 
      if (b >= this.m_tokentable_.length) {
        add(set, b);
        resultnlength++;
        continue;
      } 
      char token = this.m_tokentable_[b & 0xFF];
      if (token == '￾') {
        b = (char)(b << 8 | this.m_groupstring_[offset + resultplength] & 0xFF);
        token = this.m_tokentable_[b];
        resultplength++;
      } 
      if (token == Character.MAX_VALUE) {
        add(set, b);
        resultnlength++;
        continue;
      } 
      byte tlength = tokenlength[b];
      if (tlength == 0) {
        synchronized (this.m_utilStringBuffer_) {
          this.m_utilStringBuffer_.delete(0, this.m_utilStringBuffer_.length());
          UCharacterUtility.getNullTermByteSubString(this.m_utilStringBuffer_, this.m_tokenstring_, token);
          tlength = (byte)add(set, this.m_utilStringBuffer_);
        } 
        tokenlength[b] = tlength;
      } 
      resultnlength += tlength;
    } 
    this.m_utilIntBuffer_[0] = resultnlength;
    this.m_utilIntBuffer_[1] = resultplength;
    return this.m_utilIntBuffer_;
  }
  
  private void addGroupName(int maxlength) {
    int maxisolength = 0;
    char[] offsets = new char[34];
    char[] lengths = new char[34];
    byte[] tokenlengths = new byte[this.m_tokentable_.length];
    for (int i = 0; i < this.m_groupcount_; i++) {
      int offset = getGroupLengths(i, offsets, lengths);
      for (int linenumber = 0; linenumber < 32; 
        linenumber++) {
        int lineoffset = offset + offsets[linenumber];
        int length = lengths[linenumber];
        if (length != 0) {
          int[] parsed = addGroupName(lineoffset, length, tokenlengths, this.m_nameSet_);
          if (parsed[0] > maxlength)
            maxlength = parsed[0]; 
          lineoffset += parsed[1];
          if (parsed[1] < length) {
            length -= parsed[1];
            parsed = addGroupName(lineoffset, length, tokenlengths, this.m_nameSet_);
            if (parsed[0] > maxlength)
              maxlength = parsed[0]; 
            lineoffset += parsed[1];
            if (parsed[1] < length) {
              length -= parsed[1];
              parsed = addGroupName(lineoffset, length, tokenlengths, this.m_ISOCommentSet_);
              if (parsed[1] > maxisolength)
                maxisolength = length; 
            } 
          } 
        } 
      } 
    } 
    this.m_maxISOCommentLength_ = maxisolength;
    this.m_maxNameLength_ = maxlength;
  }
  
  private boolean initNameSetsLengths() {
    if (this.m_maxNameLength_ > 0)
      return true; 
    String extra = "0123456789ABCDEF<>-";
    for (int i = extra.length() - 1; i >= 0; i--)
      add(this.m_nameSet_, extra.charAt(i)); 
    this.m_maxNameLength_ = addAlgorithmName(0);
    this.m_maxNameLength_ = addExtendedName(this.m_maxNameLength_);
    addGroupName(this.m_maxNameLength_);
    return true;
  }
  
  private void convert(int[] set, UnicodeSet uset) {
    uset.clear();
    if (!initNameSetsLengths())
      return; 
    for (char c = 'ÿ'; c > '\000'; c = (char)(c - 1)) {
      if (contains(set, c))
        uset.add(c); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\UCharacterName.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */