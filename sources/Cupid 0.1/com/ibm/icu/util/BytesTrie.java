package com.ibm.icu.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public final class BytesTrie implements Cloneable, Iterable<BytesTrie.Entry> {
  public BytesTrie(byte[] trieBytes, int offset) {
    this.bytes_ = trieBytes;
    this.pos_ = this.root_ = offset;
    this.remainingMatchLength_ = -1;
  }
  
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
  
  public BytesTrie reset() {
    this.pos_ = this.root_;
    this.remainingMatchLength_ = -1;
    return this;
  }
  
  public static final class State {
    private byte[] bytes;
    
    private int root;
    
    private int pos;
    
    private int remainingMatchLength;
  }
  
  public BytesTrie saveState(State state) {
    state.bytes = this.bytes_;
    state.root = this.root_;
    state.pos = this.pos_;
    state.remainingMatchLength = this.remainingMatchLength_;
    return this;
  }
  
  public BytesTrie resetToState(State state) {
    if (this.bytes_ == state.bytes && this.bytes_ != null && this.root_ == state.root) {
      this.pos_ = state.pos;
      this.remainingMatchLength_ = state.remainingMatchLength;
    } else {
      throw new IllegalArgumentException("incompatible trie state");
    } 
    return this;
  }
  
  public enum Result {
    NO_MATCH, NO_VALUE, FINAL_VALUE, INTERMEDIATE_VALUE;
    
    public boolean matches() {
      return (this != NO_MATCH);
    }
    
    public boolean hasValue() {
      return (ordinal() >= 2);
    }
    
    public boolean hasNext() {
      return ((ordinal() & 0x1) != 0);
    }
  }
  
  public Result current() {
    int pos = this.pos_;
    if (pos < 0)
      return Result.NO_MATCH; 
    int node;
    return (this.remainingMatchLength_ < 0 && (node = this.bytes_[pos] & 0xFF) >= 32) ? valueResults_[node & 0x1] : Result.NO_VALUE;
  }
  
  public Result first(int inByte) {
    this.remainingMatchLength_ = -1;
    if (inByte < 0)
      inByte += 256; 
    return nextImpl(this.root_, inByte);
  }
  
  public Result next(int inByte) {
    int pos = this.pos_;
    if (pos < 0)
      return Result.NO_MATCH; 
    if (inByte < 0)
      inByte += 256; 
    int length = this.remainingMatchLength_;
    if (length >= 0) {
      if (inByte == (this.bytes_[pos++] & 0xFF)) {
        this.remainingMatchLength_ = --length;
        this.pos_ = pos;
        int node;
        return (length < 0 && (node = this.bytes_[pos] & 0xFF) >= 32) ? valueResults_[node & 0x1] : Result.NO_VALUE;
      } 
      stop();
      return Result.NO_MATCH;
    } 
    return nextImpl(pos, inByte);
  }
  
  public Result next(byte[] s, int sIndex, int sLimit) {
    if (sIndex >= sLimit)
      return current(); 
    int pos = this.pos_;
    if (pos < 0)
      return Result.NO_MATCH; 
    int length = this.remainingMatchLength_;
    label50: while (true) {
      if (sIndex == sLimit) {
        this.remainingMatchLength_ = length;
        this.pos_ = pos;
        int node;
        return (length < 0 && (node = this.bytes_[pos] & 0xFF) >= 32) ? valueResults_[node & 0x1] : Result.NO_VALUE;
      } 
      byte inByte = s[sIndex++];
      if (length < 0) {
        this.remainingMatchLength_ = length;
      } else {
        if (inByte != this.bytes_[pos]) {
          stop();
          return Result.NO_MATCH;
        } 
        pos++;
        length--;
        continue;
      } 
      while (true) {
        int node = this.bytes_[pos++] & 0xFF;
        if (node < 16) {
          Result result = branchNext(pos, node, inByte & 0xFF);
          if (result == Result.NO_MATCH)
            return Result.NO_MATCH; 
          if (sIndex == sLimit)
            return result; 
          if (result == Result.FINAL_VALUE) {
            stop();
            return Result.NO_MATCH;
          } 
          inByte = s[sIndex++];
          pos = this.pos_;
          continue;
        } 
        if (node < 32) {
          length = node - 16;
          if (inByte != this.bytes_[pos]) {
            stop();
            return Result.NO_MATCH;
          } 
          pos++;
          length--;
          continue label50;
        } 
        if ((node & 0x1) != 0) {
          stop();
          return Result.NO_MATCH;
        } 
        pos = skipValue(pos, node);
        assert (this.bytes_[pos] & 0xFF) < 32;
      } 
      break;
    } 
  }
  
  public int getValue() {
    int pos = this.pos_;
    int leadByte = this.bytes_[pos++] & 0xFF;
    assert leadByte >= 32;
    return readValue(this.bytes_, pos, leadByte >> 1);
  }
  
  public long getUniqueValue() {
    int pos = this.pos_;
    if (pos < 0)
      return 0L; 
    long uniqueValue = findUniqueValue(this.bytes_, pos + this.remainingMatchLength_ + 1, 0L);
    return uniqueValue << 31L >> 31L;
  }
  
  public int getNextBytes(Appendable out) {
    int pos = this.pos_;
    if (pos < 0)
      return 0; 
    if (this.remainingMatchLength_ >= 0) {
      append(out, this.bytes_[pos] & 0xFF);
      return 1;
    } 
    int node = this.bytes_[pos++] & 0xFF;
    if (node >= 32) {
      if ((node & 0x1) != 0)
        return 0; 
      pos = skipValue(pos, node);
      node = this.bytes_[pos++] & 0xFF;
      assert node < 32;
    } 
    if (node < 16) {
      if (node == 0)
        node = this.bytes_[pos++] & 0xFF; 
      getNextBranchBytes(this.bytes_, pos, ++node, out);
      return node;
    } 
    append(out, this.bytes_[pos] & 0xFF);
    return 1;
  }
  
  public Iterator iterator() {
    return new Iterator(this.bytes_, this.pos_, this.remainingMatchLength_, 0);
  }
  
  public Iterator iterator(int maxStringLength) {
    return new Iterator(this.bytes_, this.pos_, this.remainingMatchLength_, maxStringLength);
  }
  
  public static Iterator iterator(byte[] trieBytes, int offset, int maxStringLength) {
    return new Iterator(trieBytes, offset, -1, maxStringLength);
  }
  
  public static final class Entry {
    public int value;
    
    private byte[] bytes;
    
    private int length;
    
    private Entry(int capacity) {
      this.bytes = new byte[capacity];
    }
    
    public int bytesLength() {
      return this.length;
    }
    
    public byte byteAt(int index) {
      return this.bytes[index];
    }
    
    public void copyBytesTo(byte[] dest, int destOffset) {
      System.arraycopy(this.bytes, 0, dest, destOffset, this.length);
    }
    
    public ByteBuffer bytesAsByteBuffer() {
      return ByteBuffer.wrap(this.bytes, 0, this.length).asReadOnlyBuffer();
    }
    
    private void ensureCapacity(int len) {
      if (this.bytes.length < len) {
        byte[] newBytes = new byte[Math.min(2 * this.bytes.length, 2 * len)];
        System.arraycopy(this.bytes, 0, newBytes, 0, this.length);
        this.bytes = newBytes;
      } 
    }
    
    private void append(byte b) {
      ensureCapacity(this.length + 1);
      this.bytes[this.length++] = b;
    }
    
    private void append(byte[] b, int off, int len) {
      ensureCapacity(this.length + len);
      System.arraycopy(b, off, this.bytes, this.length, len);
      this.length += len;
    }
    
    private void truncateString(int newLength) {
      this.length = newLength;
    }
  }
  
  public static final class Iterator implements java.util.Iterator<Entry> {
    private byte[] bytes_;
    
    private int pos_;
    
    private int initialPos_;
    
    private int remainingMatchLength_;
    
    private int initialRemainingMatchLength_;
    
    private int maxLength_;
    
    private BytesTrie.Entry entry_;
    
    private ArrayList<Long> stack_;
    
    private Iterator(byte[] trieBytes, int offset, int remainingMatchLength, int maxStringLength) {
      this.stack_ = new ArrayList<Long>();
      this.bytes_ = trieBytes;
      this.pos_ = this.initialPos_ = offset;
      this.remainingMatchLength_ = this.initialRemainingMatchLength_ = remainingMatchLength;
      this.maxLength_ = maxStringLength;
      this.entry_ = new BytesTrie.Entry((this.maxLength_ != 0) ? this.maxLength_ : 32);
      int length = this.remainingMatchLength_;
      if (length >= 0) {
        length++;
        if (this.maxLength_ > 0 && length > this.maxLength_)
          length = this.maxLength_; 
        this.entry_.append(this.bytes_, this.pos_, length);
        this.pos_ += length;
        this.remainingMatchLength_ -= length;
      } 
    }
    
    public Iterator reset() {
      this.pos_ = this.initialPos_;
      this.remainingMatchLength_ = this.initialRemainingMatchLength_;
      int length = this.remainingMatchLength_ + 1;
      if (this.maxLength_ > 0 && length > this.maxLength_)
        length = this.maxLength_; 
      this.entry_.truncateString(length);
      this.pos_ += length;
      this.remainingMatchLength_ -= length;
      this.stack_.clear();
      return this;
    }
    
    public boolean hasNext() {
      return (this.pos_ >= 0 || !this.stack_.isEmpty());
    }
    
    public BytesTrie.Entry next() {
      int pos = this.pos_;
      if (pos < 0) {
        if (this.stack_.isEmpty())
          throw new NoSuchElementException(); 
        long top = ((Long)this.stack_.remove(this.stack_.size() - 1)).longValue();
        int length = (int)top;
        pos = (int)(top >> 32L);
        this.entry_.truncateString(length & 0xFFFF);
        length >>>= 16;
        if (length > 1) {
          pos = branchNext(pos, length);
          if (pos < 0)
            return this.entry_; 
        } else {
          this.entry_.append(this.bytes_[pos++]);
        } 
      } 
      if (this.remainingMatchLength_ >= 0)
        return truncateAndStop(); 
      while (true) {
        int node = this.bytes_[pos++] & 0xFF;
        if (node >= 32) {
          boolean isFinal = ((node & 0x1) != 0);
          this.entry_.value = BytesTrie.readValue(this.bytes_, pos, node >> 1);
          if (isFinal || (this.maxLength_ > 0 && this.entry_.length == this.maxLength_)) {
            this.pos_ = -1;
          } else {
            this.pos_ = BytesTrie.skipValue(pos, node);
          } 
          return this.entry_;
        } 
        if (this.maxLength_ > 0 && this.entry_.length == this.maxLength_)
          return truncateAndStop(); 
        if (node < 16) {
          if (node == 0)
            node = this.bytes_[pos++] & 0xFF; 
          pos = branchNext(pos, node + 1);
          if (pos < 0)
            return this.entry_; 
          continue;
        } 
        int length = node - 16 + 1;
        if (this.maxLength_ > 0 && this.entry_.length + length > this.maxLength_) {
          this.entry_.append(this.bytes_, pos, this.maxLength_ - this.entry_.length);
          return truncateAndStop();
        } 
        this.entry_.append(this.bytes_, pos, length);
        pos += length;
      } 
    }
    
    public void remove() {
      throw new UnsupportedOperationException();
    }
    
    private BytesTrie.Entry truncateAndStop() {
      this.pos_ = -1;
      this.entry_.value = -1;
      return this.entry_;
    }
    
    private int branchNext(int pos, int length) {
      while (length > 5) {
        pos++;
        this.stack_.add(Long.valueOf(BytesTrie.skipDelta(this.bytes_, pos) << 32L | (length - (length >> 1) << 16) | this.entry_.length));
        length >>= 1;
        pos = BytesTrie.jumpByDelta(this.bytes_, pos);
      } 
      byte trieByte = this.bytes_[pos++];
      int node = this.bytes_[pos++] & 0xFF;
      boolean isFinal = ((node & 0x1) != 0);
      int value = BytesTrie.readValue(this.bytes_, pos, node >> 1);
      pos = BytesTrie.skipValue(pos, node);
      this.stack_.add(Long.valueOf(pos << 32L | (length - 1 << 16) | this.entry_.length));
      this.entry_.append(trieByte);
      if (isFinal) {
        this.pos_ = -1;
        this.entry_.value = value;
        return -1;
      } 
      return pos + value;
    }
  }
  
  private void stop() {
    this.pos_ = -1;
  }
  
  private static int readValue(byte[] bytes, int pos, int leadByte) {
    int value;
    if (leadByte < 81) {
      value = leadByte - 16;
    } else if (leadByte < 108) {
      value = leadByte - 81 << 8 | bytes[pos] & 0xFF;
    } else if (leadByte < 126) {
      value = leadByte - 108 << 16 | (bytes[pos] & 0xFF) << 8 | bytes[pos + 1] & 0xFF;
    } else if (leadByte == 126) {
      value = (bytes[pos] & 0xFF) << 16 | (bytes[pos + 1] & 0xFF) << 8 | bytes[pos + 2] & 0xFF;
    } else {
      value = bytes[pos] << 24 | (bytes[pos + 1] & 0xFF) << 16 | (bytes[pos + 2] & 0xFF) << 8 | bytes[pos + 3] & 0xFF;
    } 
    return value;
  }
  
  private static int skipValue(int pos, int leadByte) {
    assert leadByte >= 32;
    if (leadByte >= 162)
      if (leadByte < 216) {
        pos++;
      } else if (leadByte < 252) {
        pos += 2;
      } else {
        pos += 3 + (leadByte >> 1 & 0x1);
      }  
    return pos;
  }
  
  private static int skipValue(byte[] bytes, int pos) {
    int leadByte = bytes[pos++] & 0xFF;
    return skipValue(pos, leadByte);
  }
  
  private static int jumpByDelta(byte[] bytes, int pos) {
    int delta = bytes[pos++] & 0xFF;
    if (delta >= 192)
      if (delta < 240) {
        delta = delta - 192 << 8 | bytes[pos++] & 0xFF;
      } else if (delta < 254) {
        delta = delta - 240 << 16 | (bytes[pos] & 0xFF) << 8 | bytes[pos + 1] & 0xFF;
        pos += 2;
      } else if (delta == 254) {
        delta = (bytes[pos] & 0xFF) << 16 | (bytes[pos + 1] & 0xFF) << 8 | bytes[pos + 2] & 0xFF;
        pos += 3;
      } else {
        delta = bytes[pos] << 24 | (bytes[pos + 1] & 0xFF) << 16 | (bytes[pos + 2] & 0xFF) << 8 | bytes[pos + 3] & 0xFF;
        pos += 4;
      }  
    return pos + delta;
  }
  
  private static int skipDelta(byte[] bytes, int pos) {
    int delta = bytes[pos++] & 0xFF;
    if (delta >= 192)
      if (delta < 240) {
        pos++;
      } else if (delta < 254) {
        pos += 2;
      } else {
        pos += 3 + (delta & 0x1);
      }  
    return pos;
  }
  
  private static Result[] valueResults_ = new Result[] { Result.INTERMEDIATE_VALUE, Result.FINAL_VALUE };
  
  static final int kMaxBranchLinearSubNodeLength = 5;
  
  static final int kMinLinearMatch = 16;
  
  static final int kMaxLinearMatchLength = 16;
  
  static final int kMinValueLead = 32;
  
  private static final int kValueIsFinal = 1;
  
  static final int kMinOneByteValueLead = 16;
  
  static final int kMaxOneByteValue = 64;
  
  static final int kMinTwoByteValueLead = 81;
  
  static final int kMaxTwoByteValue = 6911;
  
  static final int kMinThreeByteValueLead = 108;
  
  static final int kFourByteValueLead = 126;
  
  static final int kMaxThreeByteValue = 1179647;
  
  static final int kFiveByteValueLead = 127;
  
  static final int kMaxOneByteDelta = 191;
  
  static final int kMinTwoByteDeltaLead = 192;
  
  static final int kMinThreeByteDeltaLead = 240;
  
  static final int kFourByteDeltaLead = 254;
  
  static final int kFiveByteDeltaLead = 255;
  
  static final int kMaxTwoByteDelta = 12287;
  
  static final int kMaxThreeByteDelta = 917503;
  
  private byte[] bytes_;
  
  private int root_;
  
  private int pos_;
  
  private int remainingMatchLength_;
  
  private Result branchNext(int pos, int length, int inByte) {
    if (length == 0)
      length = this.bytes_[pos++] & 0xFF; 
    length++;
    while (length > 5) {
      if (inByte < (this.bytes_[pos++] & 0xFF)) {
        length >>= 1;
        pos = jumpByDelta(this.bytes_, pos);
        continue;
      } 
      length -= length >> 1;
      pos = skipDelta(this.bytes_, pos);
    } 
    while (true) {
      if (inByte == (this.bytes_[pos++] & 0xFF)) {
        Result result;
        int node = this.bytes_[pos] & 0xFF;
        assert node >= 32;
        if ((node & 0x1) != 0) {
          result = Result.FINAL_VALUE;
        } else {
          int delta;
          pos++;
          node >>= 1;
          if (node < 81) {
            delta = node - 16;
          } else if (node < 108) {
            delta = node - 81 << 8 | this.bytes_[pos++] & 0xFF;
          } else if (node < 126) {
            delta = node - 108 << 16 | (this.bytes_[pos] & 0xFF) << 8 | this.bytes_[pos + 1] & 0xFF;
            pos += 2;
          } else if (node == 126) {
            delta = (this.bytes_[pos] & 0xFF) << 16 | (this.bytes_[pos + 1] & 0xFF) << 8 | this.bytes_[pos + 2] & 0xFF;
            pos += 3;
          } else {
            delta = this.bytes_[pos] << 24 | (this.bytes_[pos + 1] & 0xFF) << 16 | (this.bytes_[pos + 2] & 0xFF) << 8 | this.bytes_[pos + 3] & 0xFF;
            pos += 4;
          } 
          pos += delta;
          node = this.bytes_[pos] & 0xFF;
          result = (node >= 32) ? valueResults_[node & 0x1] : Result.NO_VALUE;
        } 
        this.pos_ = pos;
        return result;
      } 
      length--;
      pos = skipValue(this.bytes_, pos);
      if (length <= 1) {
        if (inByte == (this.bytes_[pos++] & 0xFF)) {
          this.pos_ = pos;
          int node = this.bytes_[pos] & 0xFF;
          return (node >= 32) ? valueResults_[node & 0x1] : Result.NO_VALUE;
        } 
        stop();
        return Result.NO_MATCH;
      } 
    } 
  }
  
  private Result nextImpl(int pos, int inByte) {
    while (true) {
      int node = this.bytes_[pos++] & 0xFF;
      if (node < 16)
        return branchNext(pos, node, inByte); 
      if (node < 32) {
        int length = node - 16;
        if (inByte == (this.bytes_[pos++] & 0xFF)) {
          this.remainingMatchLength_ = --length;
          this.pos_ = pos;
          return (length < 0 && (node = this.bytes_[pos] & 0xFF) >= 32) ? valueResults_[node & 0x1] : Result.NO_VALUE;
        } 
        break;
      } 
      if ((node & 0x1) != 0)
        break; 
      pos = skipValue(pos, node);
      assert (this.bytes_[pos] & 0xFF) < 32;
    } 
    stop();
    return Result.NO_MATCH;
  }
  
  private static long findUniqueValueFromBranch(byte[] bytes, int pos, int length, long uniqueValue) {
    while (length > 5) {
      pos++;
      uniqueValue = findUniqueValueFromBranch(bytes, jumpByDelta(bytes, pos), length >> 1, uniqueValue);
      if (uniqueValue == 0L)
        return 0L; 
      length -= length >> 1;
      pos = skipDelta(bytes, pos);
    } 
    do {
      pos++;
      int node = bytes[pos++] & 0xFF;
      boolean isFinal = ((node & 0x1) != 0);
      int value = readValue(bytes, pos, node >> 1);
      pos = skipValue(pos, node);
      if (isFinal) {
        if (uniqueValue != 0L) {
          if (value != (int)(uniqueValue >> 1L))
            return 0L; 
        } else {
          uniqueValue = value << 1L | 0x1L;
        } 
      } else {
        uniqueValue = findUniqueValue(bytes, pos + value, uniqueValue);
        if (uniqueValue == 0L)
          return 0L; 
      } 
    } while (--length > 1);
    return (pos + 1) << 33L | uniqueValue & 0x1FFFFFFFFL;
  }
  
  private static long findUniqueValue(byte[] bytes, int pos, long uniqueValue) {
    while (true) {
      int node = bytes[pos++] & 0xFF;
      if (node < 16) {
        if (node == 0)
          node = bytes[pos++] & 0xFF; 
        uniqueValue = findUniqueValueFromBranch(bytes, pos, node + 1, uniqueValue);
        if (uniqueValue == 0L)
          return 0L; 
        pos = (int)(uniqueValue >>> 33L);
        continue;
      } 
      if (node < 32) {
        pos += node - 16 + 1;
        continue;
      } 
      boolean isFinal = ((node & 0x1) != 0);
      int value = readValue(bytes, pos, node >> 1);
      if (uniqueValue != 0L) {
        if (value != (int)(uniqueValue >> 1L))
          return 0L; 
      } else {
        uniqueValue = value << 1L | 0x1L;
      } 
      if (isFinal)
        return uniqueValue; 
      pos = skipValue(pos, node);
    } 
  }
  
  private static void getNextBranchBytes(byte[] bytes, int pos, int length, Appendable out) {
    while (length > 5) {
      pos++;
      getNextBranchBytes(bytes, jumpByDelta(bytes, pos), length >> 1, out);
      length -= length >> 1;
      pos = skipDelta(bytes, pos);
    } 
    while (true) {
      append(out, bytes[pos++] & 0xFF);
      pos = skipValue(bytes, pos);
      if (--length <= 1) {
        append(out, bytes[pos] & 0xFF);
        return;
      } 
    } 
  }
  
  private static void append(Appendable out, int c) {
    try {
      out.append((char)c);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\BytesTrie.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */