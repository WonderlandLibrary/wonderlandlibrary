package com.ibm.icu.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

public abstract class Trie2 implements Iterable<Trie2.Range> {
  public static Trie2 createFromSerialized(InputStream is) throws IOException {
    ValueWidth width;
    Trie2 This;
    DataInputStream dis = new DataInputStream(is);
    boolean needByteSwap = false;
    UTrie2Header header = new UTrie2Header();
    header.signature = dis.readInt();
    switch (header.signature) {
      case 1416784178:
        needByteSwap = false;
        break;
      case 845771348:
        needByteSwap = true;
        header.signature = Integer.reverseBytes(header.signature);
        break;
      default:
        throw new IllegalArgumentException("Stream does not contain a serialized UTrie2");
    } 
    header.options = swapShort(needByteSwap, dis.readUnsignedShort());
    header.indexLength = swapShort(needByteSwap, dis.readUnsignedShort());
    header.shiftedDataLength = swapShort(needByteSwap, dis.readUnsignedShort());
    header.index2NullOffset = swapShort(needByteSwap, dis.readUnsignedShort());
    header.dataNullOffset = swapShort(needByteSwap, dis.readUnsignedShort());
    header.shiftedHighStart = swapShort(needByteSwap, dis.readUnsignedShort());
    if ((header.options & 0xF) > 1)
      throw new IllegalArgumentException("UTrie2 serialized format error."); 
    if ((header.options & 0xF) == 0) {
      width = ValueWidth.BITS_16;
      This = new Trie2_16();
    } else {
      width = ValueWidth.BITS_32;
      This = new Trie2_32();
    } 
    This.header = header;
    This.indexLength = header.indexLength;
    This.dataLength = header.shiftedDataLength << 2;
    This.index2NullOffset = header.index2NullOffset;
    This.dataNullOffset = header.dataNullOffset;
    This.highStart = header.shiftedHighStart << 11;
    This.highValueIndex = This.dataLength - 4;
    if (width == ValueWidth.BITS_16)
      This.highValueIndex += This.indexLength; 
    int indexArraySize = This.indexLength;
    if (width == ValueWidth.BITS_16)
      indexArraySize += This.dataLength; 
    This.index = new char[indexArraySize];
    int i;
    for (i = 0; i < This.indexLength; i++)
      This.index[i] = swapChar(needByteSwap, dis.readChar()); 
    if (width == ValueWidth.BITS_16) {
      This.data16 = This.indexLength;
      for (i = 0; i < This.dataLength; i++)
        This.index[This.data16 + i] = swapChar(needByteSwap, dis.readChar()); 
    } else {
      This.data32 = new int[This.dataLength];
      for (i = 0; i < This.dataLength; i++)
        This.data32[i] = swapInt(needByteSwap, dis.readInt()); 
    } 
    switch (width) {
      case BITS_16:
        This.data32 = null;
        This.initialValue = This.index[This.dataNullOffset];
        This.errorValue = This.index[This.data16 + 128];
        return This;
      case BITS_32:
        This.data16 = 0;
        This.initialValue = This.data32[This.dataNullOffset];
        This.errorValue = This.data32[128];
        return This;
    } 
    throw new IllegalArgumentException("UTrie2 serialized format error.");
  }
  
  private static int swapShort(boolean needSwap, int value) {
    return needSwap ? (Short.reverseBytes((short)value) & 0xFFFF) : value;
  }
  
  private static char swapChar(boolean needSwap, char value) {
    return needSwap ? (char)Short.reverseBytes((short)value) : value;
  }
  
  private static int swapInt(boolean needSwap, int value) {
    return needSwap ? Integer.reverseBytes(value) : value;
  }
  
  public static int getVersion(InputStream is, boolean littleEndianOk) throws IOException {
    if (!is.markSupported())
      throw new IllegalArgumentException("Input stream must support mark()."); 
    is.mark(4);
    byte[] sig = new byte[4];
    int read = is.read(sig);
    is.reset();
    if (read != sig.length)
      return 0; 
    if (sig[0] == 84 && sig[1] == 114 && sig[2] == 105 && sig[3] == 101)
      return 1; 
    if (sig[0] == 84 && sig[1] == 114 && sig[2] == 105 && sig[3] == 50)
      return 2; 
    if (littleEndianOk) {
      if (sig[0] == 101 && sig[1] == 105 && sig[2] == 114 && sig[3] == 84)
        return 1; 
      if (sig[0] == 50 && sig[1] == 105 && sig[2] == 114 && sig[3] == 84)
        return 2; 
    } 
    return 0;
  }
  
  public final boolean equals(Object other) {
    if (!(other instanceof Trie2))
      return false; 
    Trie2 OtherTrie = (Trie2)other;
    Iterator<Range> otherIter = OtherTrie.iterator();
    for (Range rangeFromThis : this) {
      if (!otherIter.hasNext())
        return false; 
      Range rangeFromOther = otherIter.next();
      if (!rangeFromThis.equals(rangeFromOther))
        return false; 
    } 
    if (otherIter.hasNext())
      return false; 
    if (this.errorValue != OtherTrie.errorValue || this.initialValue != OtherTrie.initialValue)
      return false; 
    return true;
  }
  
  public int hashCode() {
    if (this.fHash == 0) {
      int hash = initHash();
      for (Range r : this)
        hash = hashInt(hash, r.hashCode()); 
      if (hash == 0)
        hash = 1; 
      this.fHash = hash;
    } 
    return this.fHash;
  }
  
  public static class Range {
    public int startCodePoint;
    
    public int endCodePoint;
    
    public int value;
    
    public boolean leadSurrogate;
    
    public boolean equals(Object other) {
      if (other == null || !other.getClass().equals(getClass()))
        return false; 
      Range tother = (Range)other;
      return (this.startCodePoint == tother.startCodePoint && this.endCodePoint == tother.endCodePoint && this.value == tother.value && this.leadSurrogate == tother.leadSurrogate);
    }
    
    public int hashCode() {
      int h = Trie2.initHash();
      h = Trie2.hashUChar32(h, this.startCodePoint);
      h = Trie2.hashUChar32(h, this.endCodePoint);
      h = Trie2.hashInt(h, this.value);
      h = Trie2.hashByte(h, this.leadSurrogate ? 1 : 0);
      return h;
    }
  }
  
  public Iterator<Range> iterator() {
    return iterator(defaultValueMapper);
  }
  
  private static ValueMapper defaultValueMapper = new ValueMapper() {
      public int map(int in) {
        return in;
      }
    };
  
  UTrie2Header header;
  
  char[] index;
  
  int data16;
  
  int[] data32;
  
  int indexLength;
  
  int dataLength;
  
  int index2NullOffset;
  
  int initialValue;
  
  int errorValue;
  
  int highStart;
  
  int highValueIndex;
  
  int dataNullOffset;
  
  int fHash;
  
  static final int UTRIE2_OPTIONS_VALUE_BITS_MASK = 15;
  
  static final int UTRIE2_SHIFT_1 = 11;
  
  static final int UTRIE2_SHIFT_2 = 5;
  
  static final int UTRIE2_SHIFT_1_2 = 6;
  
  static final int UTRIE2_OMITTED_BMP_INDEX_1_LENGTH = 32;
  
  static final int UTRIE2_CP_PER_INDEX_1_ENTRY = 2048;
  
  static final int UTRIE2_INDEX_2_BLOCK_LENGTH = 64;
  
  static final int UTRIE2_INDEX_2_MASK = 63;
  
  static final int UTRIE2_DATA_BLOCK_LENGTH = 32;
  
  static final int UTRIE2_DATA_MASK = 31;
  
  static final int UTRIE2_INDEX_SHIFT = 2;
  
  static final int UTRIE2_DATA_GRANULARITY = 4;
  
  static final int UTRIE2_INDEX_2_OFFSET = 0;
  
  static final int UTRIE2_LSCP_INDEX_2_OFFSET = 2048;
  
  static final int UTRIE2_LSCP_INDEX_2_LENGTH = 32;
  
  static final int UTRIE2_INDEX_2_BMP_LENGTH = 2080;
  
  static final int UTRIE2_UTF8_2B_INDEX_2_OFFSET = 2080;
  
  static final int UTRIE2_UTF8_2B_INDEX_2_LENGTH = 32;
  
  static final int UTRIE2_INDEX_1_OFFSET = 2112;
  
  static final int UTRIE2_MAX_INDEX_1_LENGTH = 512;
  
  static final int UTRIE2_BAD_UTF8_DATA_OFFSET = 128;
  
  static final int UTRIE2_DATA_START_OFFSET = 192;
  
  static final int UNEWTRIE2_INDEX_GAP_OFFSET = 2080;
  
  static final int UNEWTRIE2_INDEX_GAP_LENGTH = 576;
  
  static final int UNEWTRIE2_MAX_INDEX_2_LENGTH = 35488;
  
  static final int UNEWTRIE2_INDEX_1_LENGTH = 544;
  
  static final int UNEWTRIE2_MAX_DATA_LENGTH = 1115264;
  
  public Iterator<Range> iterator(ValueMapper mapper) {
    return new Trie2Iterator(mapper);
  }
  
  public Iterator<Range> iteratorForLeadSurrogate(char lead, ValueMapper mapper) {
    return new Trie2Iterator(lead, mapper);
  }
  
  public Iterator<Range> iteratorForLeadSurrogate(char lead) {
    return new Trie2Iterator(lead, defaultValueMapper);
  }
  
  protected int serializeHeader(DataOutputStream dos) throws IOException {
    int bytesWritten = 0;
    dos.writeInt(this.header.signature);
    dos.writeShort(this.header.options);
    dos.writeShort(this.header.indexLength);
    dos.writeShort(this.header.shiftedDataLength);
    dos.writeShort(this.header.index2NullOffset);
    dos.writeShort(this.header.dataNullOffset);
    dos.writeShort(this.header.shiftedHighStart);
    bytesWritten += 16;
    for (int i = 0; i < this.header.indexLength; i++)
      dos.writeChar(this.index[i]); 
    bytesWritten += this.header.indexLength;
    return bytesWritten;
  }
  
  public static class CharSequenceValues {
    public int index;
    
    public int codePoint;
    
    public int value;
  }
  
  public CharSequenceIterator charSequenceIterator(CharSequence text, int index) {
    return new CharSequenceIterator(text, index);
  }
  
  public class CharSequenceIterator implements Iterator<CharSequenceValues> {
    private CharSequence text;
    
    private int textLength;
    
    private int index;
    
    private Trie2.CharSequenceValues fResults;
    
    CharSequenceIterator(CharSequence t, int index) {
      this.fResults = new Trie2.CharSequenceValues();
      this.text = t;
      this.textLength = this.text.length();
      set(index);
    }
    
    public void set(int i) {
      if (i < 0 || i > this.textLength)
        throw new IndexOutOfBoundsException(); 
      this.index = i;
    }
    
    public final boolean hasNext() {
      return (this.index < this.textLength);
    }
    
    public final boolean hasPrevious() {
      return (this.index > 0);
    }
    
    public Trie2.CharSequenceValues next() {
      int c = Character.codePointAt(this.text, this.index);
      int val = Trie2.this.get(c);
      this.fResults.index = this.index;
      this.fResults.codePoint = c;
      this.fResults.value = val;
      this.index++;
      if (c >= 65536)
        this.index++; 
      return this.fResults;
    }
    
    public Trie2.CharSequenceValues previous() {
      int c = Character.codePointBefore(this.text, this.index);
      int val = Trie2.this.get(c);
      this.index--;
      if (c >= 65536)
        this.index--; 
      this.fResults.index = this.index;
      this.fResults.codePoint = c;
      this.fResults.value = val;
      return this.fResults;
    }
    
    public void remove() {
      throw new UnsupportedOperationException("Trie2.CharSequenceIterator does not support remove().");
    }
  }
  
  enum ValueWidth {
    BITS_16, BITS_32;
  }
  
  static class UTrie2Header {
    int signature;
    
    int options;
    
    int indexLength;
    
    int shiftedDataLength;
    
    int index2NullOffset;
    
    int dataNullOffset;
    
    int shiftedHighStart;
  }
  
  class Trie2Iterator implements Iterator<Range> {
    private Trie2.ValueMapper mapper;
    
    private Trie2.Range returnValue;
    
    private int nextStart;
    
    private int limitCP;
    
    private boolean doingCodePoints;
    
    private boolean doLeadSurrogates;
    
    Trie2Iterator(Trie2.ValueMapper vm) {
      this.returnValue = new Trie2.Range();
      this.doingCodePoints = true;
      this.doLeadSurrogates = true;
      this.mapper = vm;
      this.nextStart = 0;
      this.limitCP = 1114112;
      this.doLeadSurrogates = true;
    }
    
    Trie2Iterator(char leadSurrogate, Trie2.ValueMapper vm) {
      this.returnValue = new Trie2.Range();
      this.doingCodePoints = true;
      this.doLeadSurrogates = true;
      if (leadSurrogate < '?' || leadSurrogate > '?')
        throw new IllegalArgumentException("Bad lead surrogate value."); 
      this.mapper = vm;
      this.nextStart = leadSurrogate - 55232 << 10;
      this.limitCP = this.nextStart + 1024;
      this.doLeadSurrogates = false;
    }
    
    public Trie2.Range next() {
      if (!hasNext())
        throw new NoSuchElementException(); 
      if (this.nextStart >= this.limitCP) {
        this.doingCodePoints = false;
        this.nextStart = 55296;
      } 
      int endOfRange = 0;
      int val = 0;
      int mappedVal = 0;
      if (this.doingCodePoints) {
        val = Trie2.this.get(this.nextStart);
        mappedVal = this.mapper.map(val);
        endOfRange = Trie2.this.rangeEnd(this.nextStart, this.limitCP, val);
        while (endOfRange < this.limitCP - 1) {
          val = Trie2.this.get(endOfRange + 1);
          if (this.mapper.map(val) != mappedVal)
            break; 
          endOfRange = Trie2.this.rangeEnd(endOfRange + 1, this.limitCP, val);
        } 
      } else {
        val = Trie2.this.getFromU16SingleLead((char)this.nextStart);
        mappedVal = this.mapper.map(val);
        endOfRange = rangeEndLS((char)this.nextStart);
        while (endOfRange < 56319) {
          val = Trie2.this.getFromU16SingleLead((char)(endOfRange + 1));
          if (this.mapper.map(val) != mappedVal)
            break; 
          endOfRange = rangeEndLS((char)(endOfRange + 1));
        } 
      } 
      this.returnValue.startCodePoint = this.nextStart;
      this.returnValue.endCodePoint = endOfRange;
      this.returnValue.value = mappedVal;
      this.returnValue.leadSurrogate = !this.doingCodePoints;
      this.nextStart = endOfRange + 1;
      return this.returnValue;
    }
    
    public boolean hasNext() {
      return ((this.doingCodePoints && (this.doLeadSurrogates || this.nextStart < this.limitCP)) || this.nextStart < 56320);
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
    
    private int rangeEndLS(char startingLS) {
      if (startingLS >= '?')
        return 56319; 
      int val = Trie2.this.getFromU16SingleLead(startingLS);
      int c;
      for (c = startingLS + 1; c <= 56319 && Trie2.this.getFromU16SingleLead((char)c) == val; c++);
      return c - 1;
    }
  }
  
  int rangeEnd(int start, int limitp, int val) {
    int limit = Math.min(this.highStart, limitp);
    int c;
    for (c = start + 1; c < limit && 
      get(c) == val; c++);
    if (c >= this.highStart)
      c = limitp; 
    return c - 1;
  }
  
  private static int initHash() {
    return -2128831035;
  }
  
  private static int hashByte(int h, int b) {
    h *= 16777619;
    h ^= b;
    return h;
  }
  
  private static int hashUChar32(int h, int c) {
    h = hashByte(h, c & 0xFF);
    h = hashByte(h, c >> 8 & 0xFF);
    h = hashByte(h, c >> 16);
    return h;
  }
  
  private static int hashInt(int h, int i) {
    h = hashByte(h, i & 0xFF);
    h = hashByte(h, i >> 8 & 0xFF);
    h = hashByte(h, i >> 16 & 0xFF);
    h = hashByte(h, i >> 24 & 0xFF);
    return h;
  }
  
  public abstract int get(int paramInt);
  
  public abstract int getFromU16SingleLead(char paramChar);
  
  public static interface ValueMapper {
    int map(int param1Int);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\Trie2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */