package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class IntTrie extends Trie {
  private int m_initialValue_;
  
  private int[] m_data_;
  
  public IntTrie(InputStream inputStream, Trie.DataManipulate dataManipulate) throws IOException {
    super(inputStream, dataManipulate);
    if (!isIntTrie())
      throw new IllegalArgumentException("Data given does not belong to a int trie."); 
  }
  
  public IntTrie(int initialValue, int leadUnitValue, Trie.DataManipulate dataManipulate) {
    super(new char[2080], 512, dataManipulate);
    int latin1Length = 256, dataLength = latin1Length;
    if (leadUnitValue != initialValue)
      dataLength += 32; 
    this.m_data_ = new int[dataLength];
    this.m_dataLength_ = dataLength;
    this.m_initialValue_ = initialValue;
    int i;
    for (i = 0; i < latin1Length; i++)
      this.m_data_[i] = initialValue; 
    if (leadUnitValue != initialValue) {
      char block = (char)(latin1Length >> 2);
      i = 1728;
      int limit = 1760;
      for (; i < limit; i++)
        this.m_index_[i] = block; 
      limit = latin1Length + 32;
      for (i = latin1Length; i < limit; i++)
        this.m_data_[i] = leadUnitValue; 
    } 
  }
  
  public final int getCodePointValue(int ch) {
    if (0 <= ch && ch < 55296) {
      int i = (this.m_index_[ch >> 5] << 2) + (ch & 0x1F);
      return this.m_data_[i];
    } 
    int offset = getCodePointOffset(ch);
    return (offset >= 0) ? this.m_data_[offset] : this.m_initialValue_;
  }
  
  public final int getLeadValue(char ch) {
    return this.m_data_[getLeadOffset(ch)];
  }
  
  public final int getBMPValue(char ch) {
    return this.m_data_[getBMPOffset(ch)];
  }
  
  public final int getSurrogateValue(char lead, char trail) {
    if (!UTF16.isLeadSurrogate(lead) || !UTF16.isTrailSurrogate(trail))
      throw new IllegalArgumentException("Argument characters do not form a supplementary character"); 
    int offset = getSurrogateOffset(lead, trail);
    if (offset > 0)
      return this.m_data_[offset]; 
    return this.m_initialValue_;
  }
  
  public final int getTrailValue(int leadvalue, char trail) {
    if (this.m_dataManipulate_ == null)
      throw new NullPointerException("The field DataManipulate in this Trie is null"); 
    int offset = this.m_dataManipulate_.getFoldingOffset(leadvalue);
    if (offset > 0)
      return this.m_data_[getRawOffset(offset, (char)(trail & 0x3FF))]; 
    return this.m_initialValue_;
  }
  
  public final int getLatin1LinearValue(char ch) {
    return this.m_data_[32 + ch];
  }
  
  public boolean equals(Object other) {
    boolean result = super.equals(other);
    if (result && other instanceof IntTrie) {
      IntTrie othertrie = (IntTrie)other;
      if (this.m_initialValue_ != othertrie.m_initialValue_ || !Arrays.equals(this.m_data_, othertrie.m_data_))
        return false; 
      return true;
    } 
    return false;
  }
  
  public int hashCode() {
    assert false : "hashCode not designed";
    return 42;
  }
  
  protected final void unserialize(InputStream inputStream) throws IOException {
    super.unserialize(inputStream);
    this.m_data_ = new int[this.m_dataLength_];
    DataInputStream input = new DataInputStream(inputStream);
    for (int i = 0; i < this.m_dataLength_; i++)
      this.m_data_[i] = input.readInt(); 
    this.m_initialValue_ = this.m_data_[0];
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
  
  IntTrie(char[] index, int[] data, int initialvalue, int options, Trie.DataManipulate datamanipulate) {
    super(index, options, datamanipulate);
    this.m_data_ = data;
    this.m_dataLength_ = this.m_data_.length;
    this.m_initialValue_ = initialvalue;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\IntTrie.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */