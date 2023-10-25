package com.ibm.icu.impl;

public class Trie2Writable extends Trie2 {
  private static final int UTRIE2_MAX_INDEX_LENGTH = 65535;
  
  private static final int UTRIE2_MAX_DATA_LENGTH = 262140;
  
  private static final int UNEWTRIE2_INITIAL_DATA_LENGTH = 16384;
  
  private static final int UNEWTRIE2_MEDIUM_DATA_LENGTH = 131072;
  
  private static final int UNEWTRIE2_INDEX_2_NULL_OFFSET = 2656;
  
  private static final int UNEWTRIE2_INDEX_2_START_OFFSET = 2720;
  
  private static final int UNEWTRIE2_DATA_NULL_OFFSET = 192;
  
  private static final int UNEWTRIE2_DATA_START_OFFSET = 256;
  
  private static final int UNEWTRIE2_DATA_0800_OFFSET = 2176;
  
  private int[] index1;
  
  private int[] index2;
  
  private int[] data;
  
  private int index2Length;
  
  private int dataCapacity;
  
  private int firstFreeBlock;
  
  private int index2NullOffset;
  
  private boolean isCompacted;
  
  private int[] map;
  
  private boolean UTRIE2_DEBUG;
  
  public Trie2Writable(int initialValueP, int errorValueP) {
    this.index1 = new int[544];
    this.index2 = new int[35488];
    this.map = new int[34852];
    this.UTRIE2_DEBUG = false;
    init(initialValueP, errorValueP);
  }
  
  private void init(int initialValueP, int errorValueP) {
    this.initialValue = initialValueP;
    this.errorValue = errorValueP;
    this.highStart = 1114112;
    this.data = new int[16384];
    this.dataCapacity = 16384;
    this.initialValue = initialValueP;
    this.errorValue = errorValueP;
    this.highStart = 1114112;
    this.firstFreeBlock = 0;
    this.isCompacted = false;
    int i;
    for (i = 0; i < 128; i++)
      this.data[i] = this.initialValue; 
    for (; i < 192; i++)
      this.data[i] = this.errorValue; 
    for (i = 192; i < 256; i++)
      this.data[i] = this.initialValue; 
    this.dataNullOffset = 192;
    this.dataLength = 256;
    int j;
    for (i = 0, j = 0; j < 128; i++, j += 32) {
      this.index2[i] = j;
      this.map[i] = 1;
    } 
    for (; j < 192; i++, j += 32)
      this.map[i] = 0; 
    this.map[i++] = 34845;
    j += 32;
    for (; j < 256; i++, j += 32)
      this.map[i] = 0; 
    for (i = 4; i < 2080; i++)
      this.index2[i] = 192; 
    for (i = 0; i < 576; i++)
      this.index2[2080 + i] = -1; 
    for (i = 0; i < 64; i++)
      this.index2[2656 + i] = 192; 
    this.index2NullOffset = 2656;
    this.index2Length = 2720;
    i = 0;
    j = 0;
    for (; i < 32; i++, j += 64)
      this.index1[i] = j; 
    for (; i < 544; i++)
      this.index1[i] = 2656; 
    for (i = 128; i < 2048; i += 32)
      set(i, this.initialValue); 
  }
  
  public Trie2Writable(Trie2 source) {
    this.index1 = new int[544];
    this.index2 = new int[35488];
    this.map = new int[34852];
    this.UTRIE2_DEBUG = false;
    init(source.initialValue, source.errorValue);
    for (Trie2.Range r : source)
      setRange(r, true); 
  }
  
  private boolean isInNullBlock(int c, boolean forLSCP) {
    int i2;
    if (Character.isHighSurrogate((char)c) && forLSCP) {
      i2 = 320 + (c >> 5);
    } else {
      i2 = this.index1[c >> 11] + (c >> 5 & 0x3F);
    } 
    int block = this.index2[i2];
    return (block == this.dataNullOffset);
  }
  
  private int allocIndex2Block() {
    int newBlock = this.index2Length;
    int newTop = newBlock + 64;
    if (newTop > this.index2.length)
      throw new IllegalStateException("Internal error in Trie2 creation."); 
    this.index2Length = newTop;
    System.arraycopy(this.index2, this.index2NullOffset, this.index2, newBlock, 64);
    return newBlock;
  }
  
  private int getIndex2Block(int c, boolean forLSCP) {
    if (c >= 55296 && c < 56320 && forLSCP)
      return 2048; 
    int i1 = c >> 11;
    int i2 = this.index1[i1];
    if (i2 == this.index2NullOffset) {
      i2 = allocIndex2Block();
      this.index1[i1] = i2;
    } 
    return i2;
  }
  
  private int allocDataBlock(int copyBlock) {
    int newBlock;
    if (this.firstFreeBlock != 0) {
      newBlock = this.firstFreeBlock;
      this.firstFreeBlock = -this.map[newBlock >> 5];
    } else {
      newBlock = this.dataLength;
      int newTop = newBlock + 32;
      if (newTop > this.dataCapacity) {
        int capacity;
        if (this.dataCapacity < 131072) {
          capacity = 131072;
        } else if (this.dataCapacity < 1115264) {
          capacity = 1115264;
        } else {
          throw new IllegalStateException("Internal error in Trie2 creation.");
        } 
        int[] newData = new int[capacity];
        System.arraycopy(this.data, 0, newData, 0, this.dataLength);
        this.data = newData;
        this.dataCapacity = capacity;
      } 
      this.dataLength = newTop;
    } 
    System.arraycopy(this.data, copyBlock, this.data, newBlock, 32);
    this.map[newBlock >> 5] = 0;
    return newBlock;
  }
  
  private void releaseDataBlock(int block) {
    this.map[block >> 5] = -this.firstFreeBlock;
    this.firstFreeBlock = block;
  }
  
  private boolean isWritableBlock(int block) {
    return (block != this.dataNullOffset && 1 == this.map[block >> 5]);
  }
  
  private void setIndex2Entry(int i2, int block) {
    this.map[block >> 5] = this.map[block >> 5] + 1;
    int oldBlock = this.index2[i2];
    this.map[oldBlock >> 5] = this.map[oldBlock >> 5] - 1;
    if (0 == this.map[oldBlock >> 5] - 1)
      releaseDataBlock(oldBlock); 
    this.index2[i2] = block;
  }
  
  private int getDataBlock(int c, boolean forLSCP) {
    int i2 = getIndex2Block(c, forLSCP);
    i2 += c >> 5 & 0x3F;
    int oldBlock = this.index2[i2];
    if (isWritableBlock(oldBlock))
      return oldBlock; 
    int newBlock = allocDataBlock(oldBlock);
    setIndex2Entry(i2, newBlock);
    return newBlock;
  }
  
  public Trie2Writable set(int c, int value) {
    if (c < 0 || c > 1114111)
      throw new IllegalArgumentException("Invalid code point."); 
    set(c, true, value);
    this.fHash = 0;
    return this;
  }
  
  private Trie2Writable set(int c, boolean forLSCP, int value) {
    if (this.isCompacted)
      uncompact(); 
    int block = getDataBlock(c, forLSCP);
    this.data[block + (c & 0x1F)] = value;
    return this;
  }
  
  private void uncompact() {
    Trie2Writable tempTrie = new Trie2Writable(this);
    this.index1 = tempTrie.index1;
    this.index2 = tempTrie.index2;
    this.data = tempTrie.data;
    this.index2Length = tempTrie.index2Length;
    this.dataCapacity = tempTrie.dataCapacity;
    this.isCompacted = tempTrie.isCompacted;
    this.header = tempTrie.header;
    this.index = tempTrie.index;
    this.data16 = tempTrie.data16;
    this.data32 = tempTrie.data32;
    this.indexLength = tempTrie.indexLength;
    this.dataLength = tempTrie.dataLength;
    this.index2NullOffset = tempTrie.index2NullOffset;
    this.initialValue = tempTrie.initialValue;
    this.errorValue = tempTrie.errorValue;
    this.highStart = tempTrie.highStart;
    this.highValueIndex = tempTrie.highValueIndex;
    this.dataNullOffset = tempTrie.dataNullOffset;
  }
  
  private void writeBlock(int block, int value) {
    int limit = block + 32;
    while (block < limit)
      this.data[block++] = value; 
  }
  
  private void fillBlock(int block, int start, int limit, int value, int initialValue, boolean overwrite) {
    int pLimit = block + limit;
    if (overwrite) {
      for (int i = block + start; i < pLimit; i++)
        this.data[i] = value; 
    } else {
      for (int i = block + start; i < pLimit; i++) {
        if (this.data[i] == initialValue)
          this.data[i] = value; 
      } 
    } 
  }
  
  public Trie2Writable setRange(int start, int end, int value, boolean overwrite) {
    int repeatBlock;
    if (start > 1114111 || start < 0 || end > 1114111 || end < 0 || start > end)
      throw new IllegalArgumentException("Invalid code point range."); 
    if (!overwrite && value == this.initialValue)
      return this; 
    this.fHash = 0;
    if (this.isCompacted)
      uncompact(); 
    int limit = end + 1;
    if ((start & 0x1F) != 0) {
      int block = getDataBlock(start, true);
      int nextStart = start + 32 & 0xFFFFFFE0;
      if (nextStart <= limit) {
        fillBlock(block, start & 0x1F, 32, value, this.initialValue, overwrite);
        start = nextStart;
      } else {
        fillBlock(block, start & 0x1F, limit & 0x1F, value, this.initialValue, overwrite);
        return this;
      } 
    } 
    int rest = limit & 0x1F;
    limit &= 0xFFFFFFE0;
    if (value == this.initialValue) {
      repeatBlock = this.dataNullOffset;
    } else {
      repeatBlock = -1;
    } 
    while (start < limit) {
      boolean setRepeatBlock = false;
      if (value == this.initialValue && isInNullBlock(start, true)) {
        start += 32;
        continue;
      } 
      int i2 = getIndex2Block(start, true);
      i2 += start >> 5 & 0x3F;
      int block = this.index2[i2];
      if (isWritableBlock(block)) {
        if (overwrite && block >= 2176) {
          setRepeatBlock = true;
        } else {
          fillBlock(block, 0, 32, value, this.initialValue, overwrite);
        } 
      } else if (this.data[block] != value && (overwrite || block == this.dataNullOffset)) {
        setRepeatBlock = true;
      } 
      if (setRepeatBlock)
        if (repeatBlock >= 0) {
          setIndex2Entry(i2, repeatBlock);
        } else {
          repeatBlock = getDataBlock(start, true);
          writeBlock(repeatBlock, value);
        }  
      start += 32;
    } 
    if (rest > 0) {
      int block = getDataBlock(start, true);
      fillBlock(block, 0, rest, value, this.initialValue, overwrite);
    } 
    return this;
  }
  
  public Trie2Writable setRange(Trie2.Range range, boolean overwrite) {
    this.fHash = 0;
    if (range.leadSurrogate) {
      for (int c = range.startCodePoint; c <= range.endCodePoint; c++) {
        if (overwrite || getFromU16SingleLead((char)c) == this.initialValue)
          setForLeadSurrogateCodeUnit((char)c, range.value); 
      } 
    } else {
      setRange(range.startCodePoint, range.endCodePoint, range.value, overwrite);
    } 
    return this;
  }
  
  public Trie2Writable setForLeadSurrogateCodeUnit(char codeUnit, int value) {
    this.fHash = 0;
    set(codeUnit, false, value);
    return this;
  }
  
  public int get(int codePoint) {
    if (codePoint < 0 || codePoint > 1114111)
      return this.errorValue; 
    return get(codePoint, true);
  }
  
  private int get(int c, boolean fromLSCP) {
    int i2;
    if (c >= this.highStart && (c < 55296 || c >= 56320 || fromLSCP))
      return this.data[this.dataLength - 4]; 
    if (c >= 55296 && c < 56320 && fromLSCP) {
      i2 = 320 + (c >> 5);
    } else {
      i2 = this.index1[c >> 11] + (c >> 5 & 0x3F);
    } 
    int block = this.index2[i2];
    return this.data[block + (c & 0x1F)];
  }
  
  public int getFromU16SingleLead(char c) {
    return get(c, false);
  }
  
  private boolean equal_int(int[] a, int s, int t, int length) {
    for (int i = 0; i < length; i++) {
      if (a[s + i] != a[t + i])
        return false; 
    } 
    return true;
  }
  
  private int findSameIndex2Block(int index2Length, int otherBlock) {
    index2Length -= 64;
    for (int block = 0; block <= index2Length; block++) {
      if (equal_int(this.index2, block, otherBlock, 64))
        return block; 
    } 
    return -1;
  }
  
  private int findSameDataBlock(int dataLength, int otherBlock, int blockLength) {
    dataLength -= blockLength;
    for (int block = 0; block <= dataLength; block += 4) {
      if (equal_int(this.data, block, otherBlock, blockLength))
        return block; 
    } 
    return -1;
  }
  
  private int findHighStart(int highValue) {
    int prevI2Block, prevBlock;
    if (highValue == this.initialValue) {
      prevI2Block = this.index2NullOffset;
      prevBlock = this.dataNullOffset;
    } else {
      prevI2Block = -1;
      prevBlock = -1;
    } 
    int prev = 1114112;
    int i1 = 544;
    int c = prev;
    while (c > 0) {
      int i2Block = this.index1[--i1];
      if (i2Block == prevI2Block) {
        c -= 2048;
        continue;
      } 
      prevI2Block = i2Block;
      if (i2Block == this.index2NullOffset) {
        if (highValue != this.initialValue)
          return c; 
        c -= 2048;
        continue;
      } 
      for (int i2 = 64; i2 > 0; ) {
        int block = this.index2[i2Block + --i2];
        if (block == prevBlock) {
          c -= 32;
          continue;
        } 
        prevBlock = block;
        if (block == this.dataNullOffset) {
          if (highValue != this.initialValue)
            return c; 
          c -= 32;
          continue;
        } 
        for (int j = 32; j > 0; ) {
          int value = this.data[block + --j];
          if (value != highValue)
            return c; 
          c--;
        } 
      } 
    } 
    return 0;
  }
  
  private void compactData() {
    int newStart = 192;
    int start, i;
    for (start = 0, i = 0; start < newStart; start += 32, i++)
      this.map[i] = start; 
    int blockLength = 64;
    int blockCount = blockLength >> 5;
    for (start = newStart; start < this.dataLength; ) {
      if (start == 2176) {
        blockLength = 32;
        blockCount = 1;
      } 
      if (this.map[start >> 5] <= 0) {
        start += blockLength;
        continue;
      } 
      int movedStart = findSameDataBlock(newStart, start, blockLength);
      if (movedStart >= 0) {
        int j;
        for (i = blockCount, j = start >> 5; i > 0; i--) {
          this.map[j++] = movedStart;
          movedStart += 32;
        } 
        start += blockLength;
        continue;
      } 
      int overlap = blockLength - 4;
      while (overlap > 0 && !equal_int(this.data, newStart - overlap, start, overlap))
        overlap -= 4; 
      if (overlap > 0 || newStart < start) {
        movedStart = newStart - overlap;
        int j;
        for (i = blockCount, j = start >> 5; i > 0; i--) {
          this.map[j++] = movedStart;
          movedStart += 32;
        } 
        start += overlap;
        for (i = blockLength - overlap; i > 0; i--)
          this.data[newStart++] = this.data[start++]; 
        continue;
      } 
      int mapIndex;
      for (i = blockCount, mapIndex = start >> 5; i > 0; i--) {
        this.map[mapIndex++] = start;
        start += 32;
      } 
      newStart = start;
    } 
    for (i = 0; i < this.index2Length; i++) {
      if (i == 2080)
        i += 576; 
      this.index2[i] = this.map[this.index2[i] >> 5];
    } 
    this.dataNullOffset = this.map[this.dataNullOffset >> 5];
    while ((newStart & 0x3) != 0)
      this.data[newStart++] = this.initialValue; 
    if (this.UTRIE2_DEBUG)
      System.out.printf("compacting UTrie2: count of 32-bit data words %d->%d\n", new Object[] { Integer.valueOf(this.dataLength), Integer.valueOf(newStart) }); 
    this.dataLength = newStart;
  }
  
  private void compactIndex2() {
    int newStart = 2080;
    int i, start;
    for (start = 0, i = 0; start < newStart; start += 64, i++)
      this.map[i] = start; 
    newStart += 32 + (this.highStart - 65536 >> 11);
    for (start = 2656; start < this.index2Length; ) {
      int movedStart;
      if ((movedStart = findSameIndex2Block(newStart, start)) >= 0) {
        this.map[start >> 6] = movedStart;
        start += 64;
        continue;
      } 
      int overlap = 63;
      while (overlap > 0 && !equal_int(this.index2, newStart - overlap, start, overlap))
        overlap--; 
      if (overlap > 0 || newStart < start) {
        this.map[start >> 6] = newStart - overlap;
        start += overlap;
        for (i = 64 - overlap; i > 0; i--)
          this.index2[newStart++] = this.index2[start++]; 
        continue;
      } 
      this.map[start >> 6] = start;
      start += 64;
      newStart = start;
    } 
    for (i = 0; i < 544; i++)
      this.index1[i] = this.map[this.index1[i] >> 6]; 
    this.index2NullOffset = this.map[this.index2NullOffset >> 6];
    while ((newStart & 0x3) != 0)
      this.index2[newStart++] = 262140; 
    if (this.UTRIE2_DEBUG)
      System.out.printf("compacting UTrie2: count of 16-bit index-2 words %d->%d\n", new Object[] { Integer.valueOf(this.index2Length), Integer.valueOf(newStart) }); 
    this.index2Length = newStart;
  }
  
  private void compactTrie() {
    int highValue = get(1114111);
    int localHighStart = findHighStart(highValue);
    localHighStart = localHighStart + 2047 & 0xFFFFF800;
    if (localHighStart == 1114112)
      highValue = this.errorValue; 
    this.highStart = localHighStart;
    if (this.UTRIE2_DEBUG)
      System.out.printf("UTrie2: highStart U+%04x  highValue 0x%x  initialValue 0x%x\n", new Object[] { Integer.valueOf(this.highStart), Integer.valueOf(highValue), Integer.valueOf(this.initialValue) }); 
    if (this.highStart < 1114112) {
      int suppHighStart = (this.highStart <= 65536) ? 65536 : this.highStart;
      setRange(suppHighStart, 1114111, this.initialValue, true);
    } 
    compactData();
    if (this.highStart > 65536) {
      compactIndex2();
    } else if (this.UTRIE2_DEBUG) {
      System.out.printf("UTrie2: highStart U+%04x  count of 16-bit index-2 words %d->%d\n", new Object[] { Integer.valueOf(this.highStart), Integer.valueOf(this.index2Length), Integer.valueOf(2112) });
    } 
    this.data[this.dataLength++] = highValue;
    while ((this.dataLength & 0x3) != 0)
      this.data[this.dataLength++] = this.initialValue; 
    this.isCompacted = true;
  }
  
  public Trie2_16 toTrie2_16() {
    Trie2_16 frozenTrie = new Trie2_16();
    freeze(frozenTrie, Trie2.ValueWidth.BITS_16);
    return frozenTrie;
  }
  
  public Trie2_32 toTrie2_32() {
    Trie2_32 frozenTrie = new Trie2_32();
    freeze(frozenTrie, Trie2.ValueWidth.BITS_32);
    return frozenTrie;
  }
  
  private void freeze(Trie2 dest, Trie2.ValueWidth valueBits) {
    int allIndexesLength, dataMove;
    if (!this.isCompacted)
      compactTrie(); 
    if (this.highStart <= 65536) {
      allIndexesLength = 2112;
    } else {
      allIndexesLength = this.index2Length;
    } 
    if (valueBits == Trie2.ValueWidth.BITS_16) {
      dataMove = allIndexesLength;
    } else {
      dataMove = 0;
    } 
    if (allIndexesLength > 65535 || dataMove + this.dataNullOffset > 65535 || dataMove + 2176 > 65535 || dataMove + this.dataLength > 262140)
      throw new UnsupportedOperationException("Trie2 data is too large."); 
    int indexLength = allIndexesLength;
    if (valueBits == Trie2.ValueWidth.BITS_16) {
      indexLength += this.dataLength;
    } else {
      dest.data32 = new int[this.dataLength];
    } 
    dest.index = new char[indexLength];
    dest.indexLength = allIndexesLength;
    dest.dataLength = this.dataLength;
    if (this.highStart <= 65536) {
      dest.index2NullOffset = 65535;
    } else {
      dest.index2NullOffset = 0 + this.index2NullOffset;
    } 
    dest.initialValue = this.initialValue;
    dest.errorValue = this.errorValue;
    dest.highStart = this.highStart;
    dest.highValueIndex = dataMove + this.dataLength - 4;
    dest.dataNullOffset = dataMove + this.dataNullOffset;
    dest.header = new Trie2.UTrie2Header();
    dest.header.signature = 1416784178;
    dest.header.options = (valueBits == Trie2.ValueWidth.BITS_16) ? 0 : 1;
    dest.header.indexLength = dest.indexLength;
    dest.header.shiftedDataLength = dest.dataLength >> 2;
    dest.header.index2NullOffset = dest.index2NullOffset;
    dest.header.dataNullOffset = dest.dataNullOffset;
    dest.header.shiftedHighStart = dest.highStart >> 11;
    int destIdx = 0;
    int i;
    for (i = 0; i < 2080; i++)
      dest.index[destIdx++] = (char)(this.index2[i] + dataMove >> 2); 
    if (this.UTRIE2_DEBUG)
      System.out.println("\n\nIndex2 for BMP limit is " + Integer.toHexString(destIdx)); 
    for (i = 0; i < 2; i++)
      dest.index[destIdx++] = (char)(dataMove + 128); 
    for (; i < 32; i++)
      dest.index[destIdx++] = (char)(dataMove + this.index2[i << 1]); 
    if (this.UTRIE2_DEBUG)
      System.out.println("Index2 for UTF-8 2byte values limit is " + Integer.toHexString(destIdx)); 
    if (this.highStart > 65536) {
      int index1Length = this.highStart - 65536 >> 11;
      int index2Offset = 2112 + index1Length;
      for (i = 0; i < index1Length; i++)
        dest.index[destIdx++] = (char)(0 + this.index1[i + 32]); 
      if (this.UTRIE2_DEBUG)
        System.out.println("Index 1 for supplementals, limit is " + Integer.toHexString(destIdx)); 
      for (i = 0; i < this.index2Length - index2Offset; i++)
        dest.index[destIdx++] = (char)(dataMove + this.index2[index2Offset + i] >> 2); 
      if (this.UTRIE2_DEBUG)
        System.out.println("Index 2 for supplementals, limit is " + Integer.toHexString(destIdx)); 
    } 
    switch (valueBits) {
      case BITS_16:
        assert destIdx == dataMove;
        dest.data16 = destIdx;
        for (i = 0; i < this.dataLength; i++)
          dest.index[destIdx++] = (char)this.data[i]; 
        break;
      case BITS_32:
        for (i = 0; i < this.dataLength; i++)
          dest.data32[i] = this.data[i]; 
        break;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\Trie2Writable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */