package com.ibm.icu.impl;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CharTrie extends Trie {
  private char m_initialValue_;
  
  private char[] m_data_;
  
  public CharTrie(InputStream inputStream, Trie.DataManipulate dataManipulate) throws IOException {
    super(inputStream, dataManipulate);
    if (!isCharTrie())
      throw new IllegalArgumentException("Data given does not belong to a char trie."); 
  }
  
  public CharTrie(int initialValue, int leadUnitValue, Trie.DataManipulate dataManipulate) {
    super(new char[2080], 512, dataManipulate);
    int latin1Length = 256, dataLength = latin1Length;
    if (leadUnitValue != initialValue)
      dataLength += 32; 
    this.m_data_ = new char[dataLength];
    this.m_dataLength_ = dataLength;
    this.m_initialValue_ = (char)initialValue;
    int i;
    for (i = 0; i < latin1Length; i++)
      this.m_data_[i] = (char)initialValue; 
    if (leadUnitValue != initialValue) {
      char block = (char)(latin1Length >> 2);
      i = 1728;
      int limit = 1760;
      for (; i < limit; i++)
        this.m_index_[i] = block; 
      limit = latin1Length + 32;
      for (i = latin1Length; i < limit; i++)
        this.m_data_[i] = (char)leadUnitValue; 
    } 
  }
  
  public final char getCodePointValue(int ch) {
    if (0 <= ch && ch < 55296) {
      int i = (this.m_index_[ch >> 5] << 2) + (ch & 0x1F);
      return this.m_data_[i];
    } 
    int offset = getCodePointOffset(ch);
    return (offset >= 0) ? this.m_data_[offset] : this.m_initialValue_;
  }
  
  public final char getLeadValue(char ch) {
    return this.m_data_[getLeadOffset(ch)];
  }
  
  public final char getBMPValue(char ch) {
    return this.m_data_[getBMPOffset(ch)];
  }
  
  public final char getSurrogateValue(char lead, char trail) {
    int offset = getSurrogateOffset(lead, trail);
    if (offset > 0)
      return this.m_data_[offset]; 
    return this.m_initialValue_;
  }
  
  public final char getTrailValue(int leadvalue, char trail) {
    if (this.m_dataManipulate_ == null)
      throw new NullPointerException("The field DataManipulate in this Trie is null"); 
    int offset = this.m_dataManipulate_.getFoldingOffset(leadvalue);
    if (offset > 0)
      return this.m_data_[getRawOffset(offset, (char)(trail & 0x3FF))]; 
    return this.m_initialValue_;
  }
  
  public final char getLatin1LinearValue(char ch) {
    return this.m_data_[32 + this.m_dataOffset_ + ch];
  }
  
  public boolean equals(Object other) {
    boolean result = super.equals(other);
    if (result && other instanceof CharTrie) {
      CharTrie othertrie = (CharTrie)other;
      return (this.m_initialValue_ == othertrie.m_initialValue_);
    } 
    return false;
  }
  
  public int hashCode() {
    assert false : "hashCode not designed";
    return 42;
  }
  
  protected final void unserialize(InputStream inputStream) throws IOException {
    DataInputStream input = new DataInputStream(inputStream);
    int indexDataLength = this.m_dataOffset_ + this.m_dataLength_;
    this.m_index_ = new char[indexDataLength];
    for (int i = 0; i < indexDataLength; i++)
      this.m_index_[i] = input.readChar(); 
    this.m_data_ = this.m_index_;
    this.m_initialValue_ = this.m_data_[this.m_dataOffset_];
  }
  
  protected final int getSurrogateOffset(char lead, char trail) {
    if (this.m_dataManipulate_ == null)
      throw new NullPointerException("The field DataManipulate in this Trie is null"); 
    int offset = this.m_dataManipulate_.getFoldingOffset(getLeadValue(lead));
    if (offset > 0)
      return getRawOffset(offset, (char)(trail & 0x3FF)); 
    return -1;
  }
  
  protected final int getValue(int index) {
    return this.m_data_[index];
  }
  
  protected final int getInitialValue() {
    return this.m_initialValue_;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\CharTrie.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */