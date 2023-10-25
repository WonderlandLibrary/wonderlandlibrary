package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public abstract class Trie {
  protected static final int LEAD_INDEX_OFFSET_ = 320;
  
  protected static final int INDEX_STAGE_1_SHIFT_ = 5;
  
  protected static final int INDEX_STAGE_2_SHIFT_ = 2;
  
  protected static final int DATA_BLOCK_LENGTH = 32;
  
  protected static final int INDEX_STAGE_3_MASK_ = 31;
  
  protected static final int SURROGATE_BLOCK_BITS = 5;
  
  protected static final int SURROGATE_BLOCK_COUNT = 32;
  
  protected static final int BMP_INDEX_LENGTH = 2048;
  
  protected static final int SURROGATE_MASK_ = 1023;
  
  protected char[] m_index_;
  
  protected DataManipulate m_dataManipulate_;
  
  protected int m_dataOffset_;
  
  protected int m_dataLength_;
  
  protected static final int HEADER_LENGTH_ = 16;
  
  protected static final int HEADER_OPTIONS_LATIN1_IS_LINEAR_MASK_ = 512;
  
  protected static final int HEADER_SIGNATURE_ = 1416784229;
  
  private static final int HEADER_OPTIONS_SHIFT_MASK_ = 15;
  
  protected static final int HEADER_OPTIONS_INDEX_SHIFT_ = 4;
  
  protected static final int HEADER_OPTIONS_DATA_IS_32_BIT_ = 256;
  
  private boolean m_isLatin1Linear_;
  
  private int m_options_;
  
  private static class DefaultGetFoldingOffset implements DataManipulate {
    private DefaultGetFoldingOffset() {}
    
    public int getFoldingOffset(int value) {
      return value;
    }
  }
  
  public final boolean isLatin1Linear() {
    return this.m_isLatin1Linear_;
  }
  
  public boolean equals(Object other) {
    if (other == this)
      return true; 
    if (!(other instanceof Trie))
      return false; 
    Trie othertrie = (Trie)other;
    return (this.m_isLatin1Linear_ == othertrie.m_isLatin1Linear_ && this.m_options_ == othertrie.m_options_ && this.m_dataLength_ == othertrie.m_dataLength_ && Arrays.equals(this.m_index_, othertrie.m_index_));
  }
  
  public int hashCode() {
    assert false : "hashCode not designed";
    return 42;
  }
  
  public int getSerializedDataSize() {
    int result = 16;
    result += this.m_dataOffset_ << 1;
    if (isCharTrie()) {
      result += this.m_dataLength_ << 1;
    } else if (isIntTrie()) {
      result += this.m_dataLength_ << 2;
    } 
    return result;
  }
  
  protected Trie(InputStream inputStream, DataManipulate dataManipulate) throws IOException {
    DataInputStream input = new DataInputStream(inputStream);
    int signature = input.readInt();
    this.m_options_ = input.readInt();
    if (!checkHeader(signature))
      throw new IllegalArgumentException("ICU data file error: Trie header authentication failed, please check if you have the most updated ICU data file"); 
    if (dataManipulate != null) {
      this.m_dataManipulate_ = dataManipulate;
    } else {
      this.m_dataManipulate_ = new DefaultGetFoldingOffset();
    } 
    this.m_isLatin1Linear_ = ((this.m_options_ & 0x200) != 0);
    this.m_dataOffset_ = input.readInt();
    this.m_dataLength_ = input.readInt();
    unserialize(inputStream);
  }
  
  protected Trie(char[] index, int options, DataManipulate dataManipulate) {
    this.m_options_ = options;
    if (dataManipulate != null) {
      this.m_dataManipulate_ = dataManipulate;
    } else {
      this.m_dataManipulate_ = new DefaultGetFoldingOffset();
    } 
    this.m_isLatin1Linear_ = ((this.m_options_ & 0x200) != 0);
    this.m_index_ = index;
    this.m_dataOffset_ = this.m_index_.length;
  }
  
  protected abstract int getSurrogateOffset(char paramChar1, char paramChar2);
  
  protected abstract int getValue(int paramInt);
  
  protected abstract int getInitialValue();
  
  protected final int getRawOffset(int offset, char ch) {
    return (this.m_index_[offset + (ch >> 5)] << 2) + (ch & 0x1F);
  }
  
  protected final int getBMPOffset(char ch) {
    return (ch >= '?' && ch <= '?') ? getRawOffset(320, ch) : getRawOffset(0, ch);
  }
  
  protected final int getLeadOffset(char ch) {
    return getRawOffset(0, ch);
  }
  
  protected final int getCodePointOffset(int ch) {
    if (ch < 0)
      return -1; 
    if (ch < 55296)
      return getRawOffset(0, (char)ch); 
    if (ch < 65536)
      return getBMPOffset((char)ch); 
    if (ch <= 1114111)
      return getSurrogateOffset(UTF16.getLeadSurrogate(ch), (char)(ch & 0x3FF)); 
    return -1;
  }
  
  protected void unserialize(InputStream inputStream) throws IOException {
    this.m_index_ = new char[this.m_dataOffset_];
    DataInputStream input = new DataInputStream(inputStream);
    for (int i = 0; i < this.m_dataOffset_; i++)
      this.m_index_[i] = input.readChar(); 
  }
  
  protected final boolean isIntTrie() {
    return ((this.m_options_ & 0x100) != 0);
  }
  
  protected final boolean isCharTrie() {
    return ((this.m_options_ & 0x100) == 0);
  }
  
  private final boolean checkHeader(int signature) {
    if (signature != 1416784229)
      return false; 
    if ((this.m_options_ & 0xF) != 5 || (this.m_options_ >> 4 & 0xF) != 2)
      return false; 
    return true;
  }
  
  public static interface DataManipulate {
    int getFoldingOffset(int param1Int);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\Trie.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */