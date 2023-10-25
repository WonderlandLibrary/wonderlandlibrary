package com.ibm.icu.impl;

import com.ibm.icu.text.UTF16;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class IntTrieBuilder extends TrieBuilder {
  protected int[] m_data_;
  
  protected int m_initialValue_;
  
  private int m_leadUnitValue_;
  
  public IntTrieBuilder(IntTrieBuilder table) {
    super(table);
    this.m_data_ = new int[this.m_dataCapacity_];
    System.arraycopy(table.m_data_, 0, this.m_data_, 0, this.m_dataLength_);
    this.m_initialValue_ = table.m_initialValue_;
    this.m_leadUnitValue_ = table.m_leadUnitValue_;
  }
  
  public IntTrieBuilder(int[] aliasdata, int maxdatalength, int initialvalue, int leadunitvalue, boolean latin1linear) {
    if (maxdatalength < 32 || (latin1linear && maxdatalength < 1024))
      throw new IllegalArgumentException("Argument maxdatalength is too small"); 
    if (aliasdata != null) {
      this.m_data_ = aliasdata;
    } else {
      this.m_data_ = new int[maxdatalength];
    } 
    int j = 32;
    if (latin1linear) {
      int i = 0;
      do {
        this.m_index_[i++] = j;
        j += 32;
      } while (i < 8);
    } 
    this.m_dataLength_ = j;
    Arrays.fill(this.m_data_, 0, this.m_dataLength_, initialvalue);
    this.m_initialValue_ = initialvalue;
    this.m_leadUnitValue_ = leadunitvalue;
    this.m_dataCapacity_ = maxdatalength;
    this.m_isLatin1Linear_ = latin1linear;
    this.m_isCompacted_ = false;
  }
  
  public int getValue(int ch) {
    if (this.m_isCompacted_ || ch > 1114111 || ch < 0)
      return 0; 
    int block = this.m_index_[ch >> 5];
    return this.m_data_[Math.abs(block) + (ch & 0x1F)];
  }
  
  public int getValue(int ch, boolean[] inBlockZero) {
    if (this.m_isCompacted_ || ch > 1114111 || ch < 0) {
      if (inBlockZero != null)
        inBlockZero[0] = true; 
      return 0;
    } 
    int block = this.m_index_[ch >> 5];
    if (inBlockZero != null)
      inBlockZero[0] = (block == 0); 
    return this.m_data_[Math.abs(block) + (ch & 0x1F)];
  }
  
  public boolean setValue(int ch, int value) {
    if (this.m_isCompacted_ || ch > 1114111 || ch < 0)
      return false; 
    int block = getDataBlock(ch);
    if (block < 0)
      return false; 
    this.m_data_[block + (ch & 0x1F)] = value;
    return true;
  }
  
  public IntTrie serialize(TrieBuilder.DataManipulate datamanipulate, Trie.DataManipulate triedatamanipulate) {
    if (datamanipulate == null)
      throw new IllegalArgumentException("Parameters can not be null"); 
    if (!this.m_isCompacted_) {
      compact(false);
      fold(datamanipulate);
      compact(true);
      this.m_isCompacted_ = true;
    } 
    if (this.m_dataLength_ >= 262144)
      throw new ArrayIndexOutOfBoundsException("Data length too small"); 
    char[] index = new char[this.m_indexLength_];
    int[] data = new int[this.m_dataLength_];
    for (int i = 0; i < this.m_indexLength_; i++)
      index[i] = (char)(this.m_index_[i] >>> 2); 
    System.arraycopy(this.m_data_, 0, data, 0, this.m_dataLength_);
    int options = 37;
    options |= 0x100;
    if (this.m_isLatin1Linear_)
      options |= 0x200; 
    return new IntTrie(index, data, this.m_initialValue_, options, triedatamanipulate);
  }
  
  public int serialize(OutputStream os, boolean reduceTo16Bits, TrieBuilder.DataManipulate datamanipulate) throws IOException {
    if (datamanipulate == null)
      throw new IllegalArgumentException("Parameters can not be null"); 
    if (!this.m_isCompacted_) {
      compact(false);
      fold(datamanipulate);
      compact(true);
      this.m_isCompacted_ = true;
    } 
    if (reduceTo16Bits) {
      length = this.m_dataLength_ + this.m_indexLength_;
    } else {
      length = this.m_dataLength_;
    } 
    if (length >= 262144)
      throw new ArrayIndexOutOfBoundsException("Data length too small"); 
    int length = 16 + 2 * this.m_indexLength_;
    if (reduceTo16Bits) {
      length += 2 * this.m_dataLength_;
    } else {
      length += 4 * this.m_dataLength_;
    } 
    if (os == null)
      return length; 
    DataOutputStream dos = new DataOutputStream(os);
    dos.writeInt(1416784229);
    int options = 37;
    if (!reduceTo16Bits)
      options |= 0x100; 
    if (this.m_isLatin1Linear_)
      options |= 0x200; 
    dos.writeInt(options);
    dos.writeInt(this.m_indexLength_);
    dos.writeInt(this.m_dataLength_);
    if (reduceTo16Bits) {
      int i;
      for (i = 0; i < this.m_indexLength_; i++) {
        int v = this.m_index_[i] + this.m_indexLength_ >>> 2;
        dos.writeChar(v);
      } 
      for (i = 0; i < this.m_dataLength_; i++) {
        int v = this.m_data_[i] & 0xFFFF;
        dos.writeChar(v);
      } 
    } else {
      int i;
      for (i = 0; i < this.m_indexLength_; i++) {
        int v = this.m_index_[i] >>> 2;
        dos.writeChar(v);
      } 
      for (i = 0; i < this.m_dataLength_; i++)
        dos.writeInt(this.m_data_[i]); 
    } 
    return length;
  }
  
  public boolean setRange(int start, int limit, int value, boolean overwrite) {
    if (this.m_isCompacted_ || start < 0 || start > 1114111 || limit < 0 || limit > 1114112 || start > limit)
      return false; 
    if (start == limit)
      return true; 
    if ((start & 0x1F) != 0) {
      int block = getDataBlock(start);
      if (block < 0)
        return false; 
      int nextStart = start + 32 & 0xFFFFFFE0;
      if (nextStart <= limit) {
        fillBlock(block, start & 0x1F, 32, value, overwrite);
        start = nextStart;
      } else {
        fillBlock(block, start & 0x1F, limit & 0x1F, value, overwrite);
        return true;
      } 
    } 
    int rest = limit & 0x1F;
    limit &= 0xFFFFFFE0;
    int repeatBlock = 0;
    if (value != this.m_initialValue_)
      repeatBlock = -1; 
    while (start < limit) {
      int block = this.m_index_[start >> 5];
      if (block > 0) {
        fillBlock(block, 0, 32, value, overwrite);
      } else if (this.m_data_[-block] != value && (block == 0 || overwrite)) {
        if (repeatBlock >= 0) {
          this.m_index_[start >> 5] = -repeatBlock;
        } else {
          repeatBlock = getDataBlock(start);
          if (repeatBlock < 0)
            return false; 
          this.m_index_[start >> 5] = -repeatBlock;
          fillBlock(repeatBlock, 0, 32, value, true);
        } 
      } 
      start += 32;
    } 
    if (rest > 0) {
      int block = getDataBlock(start);
      if (block < 0)
        return false; 
      fillBlock(block, 0, rest, value, overwrite);
    } 
    return true;
  }
  
  private int allocDataBlock() {
    int newBlock = this.m_dataLength_;
    int newTop = newBlock + 32;
    if (newTop > this.m_dataCapacity_)
      return -1; 
    this.m_dataLength_ = newTop;
    return newBlock;
  }
  
  private int getDataBlock(int ch) {
    ch >>= 5;
    int indexValue = this.m_index_[ch];
    if (indexValue > 0)
      return indexValue; 
    int newBlock = allocDataBlock();
    if (newBlock < 0)
      return -1; 
    this.m_index_[ch] = newBlock;
    System.arraycopy(this.m_data_, Math.abs(indexValue), this.m_data_, newBlock, 128);
    return newBlock;
  }
  
  private void compact(boolean overlap) {
    if (this.m_isCompacted_)
      return; 
    findUnusedBlocks();
    int overlapStart = 32;
    if (this.m_isLatin1Linear_)
      overlapStart += 256; 
    int newStart = 32;
    int start;
    for (start = newStart; start < this.m_dataLength_; ) {
      int j;
      if (this.m_map_[start >>> 5] < 0) {
        start += 32;
        continue;
      } 
      if (start >= overlapStart) {
        j = findSameDataBlock(this.m_data_, newStart, start, overlap ? 4 : 32);
        if (j >= 0) {
          this.m_map_[start >>> 5] = j;
          start += 32;
          continue;
        } 
      } 
      if (overlap && start >= overlapStart) {
        j = 28;
        while (j > 0 && !equal_int(this.m_data_, newStart - j, start, j))
          j -= 4; 
      } else {
        j = 0;
      } 
      if (j > 0) {
        this.m_map_[start >>> 5] = newStart - j;
        start += j;
        for (j = 32 - j; j > 0; j--)
          this.m_data_[newStart++] = this.m_data_[start++]; 
        continue;
      } 
      if (newStart < start) {
        this.m_map_[start >>> 5] = newStart;
        for (j = 32; j > 0; j--)
          this.m_data_[newStart++] = this.m_data_[start++]; 
        continue;
      } 
      this.m_map_[start >>> 5] = start;
      newStart += 32;
      start = newStart;
    } 
    for (int i = 0; i < this.m_indexLength_; i++)
      this.m_index_[i] = this.m_map_[Math.abs(this.m_index_[i]) >>> 5]; 
    this.m_dataLength_ = newStart;
  }
  
  private static final int findSameDataBlock(int[] data, int dataLength, int otherBlock, int step) {
    dataLength -= 32;
    int block;
    for (block = 0; block <= dataLength; block += step) {
      if (equal_int(data, block, otherBlock, 32))
        return block; 
    } 
    return -1;
  }
  
  private final void fold(TrieBuilder.DataManipulate manipulate) {
    int[] leadIndexes = new int[32];
    int[] index = this.m_index_;
    System.arraycopy(index, 1728, leadIndexes, 0, 32);
    int block = 0;
    if (this.m_leadUnitValue_ != this.m_initialValue_) {
      block = allocDataBlock();
      if (block < 0)
        throw new IllegalStateException("Internal error: Out of memory space"); 
      fillBlock(block, 0, 32, this.m_leadUnitValue_, true);
      block = -block;
    } 
    for (int c = 1728; c < 1760; c++)
      this.m_index_[c] = block; 
    int indexLength = 2048;
    for (int i = 65536; i < 1114112; ) {
      if (index[i >> 5] != 0) {
        i &= 0xFFFFFC00;
        block = findSameIndexBlock(index, indexLength, i >> 5);
        int value = manipulate.getFoldedValue(i, block + 32);
        if (value != getValue(UTF16.getLeadSurrogate(i))) {
          if (!setValue(UTF16.getLeadSurrogate(i), value))
            throw new ArrayIndexOutOfBoundsException("Data table overflow"); 
          if (block == indexLength) {
            System.arraycopy(index, i >> 5, index, indexLength, 32);
            indexLength += 32;
          } 
        } 
        i += 1024;
        continue;
      } 
      i += 32;
    } 
    if (indexLength >= 34816)
      throw new ArrayIndexOutOfBoundsException("Index table overflow"); 
    System.arraycopy(index, 2048, index, 2080, indexLength - 2048);
    System.arraycopy(leadIndexes, 0, index, 2048, 32);
    indexLength += 32;
    this.m_indexLength_ = indexLength;
  }
  
  private void fillBlock(int block, int start, int limit, int value, boolean overwrite) {
    limit += block;
    block += start;
    if (overwrite) {
      while (block < limit)
        this.m_data_[block++] = value; 
    } else {
      while (block < limit) {
        if (this.m_data_[block] == this.m_initialValue_)
          this.m_data_[block] = value; 
        block++;
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\IntTrieBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */