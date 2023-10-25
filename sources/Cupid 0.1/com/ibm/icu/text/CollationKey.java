package com.ibm.icu.text;

public final class CollationKey implements Comparable<CollationKey> {
  private byte[] m_key_;
  
  private String m_source_;
  
  private int m_hashCode_;
  
  private int m_length_;
  
  private static final int MERGE_SEPERATOR_ = 2;
  
  public static final class BoundMode {
    public static final int LOWER = 0;
    
    public static final int UPPER = 1;
    
    public static final int UPPER_LONG = 2;
    
    public static final int COUNT = 3;
  }
  
  public CollationKey(String source, byte[] key) {
    this(source, key, -1);
  }
  
  private CollationKey(String source, byte[] key, int length) {
    this.m_source_ = source;
    this.m_key_ = key;
    this.m_hashCode_ = 0;
    this.m_length_ = length;
  }
  
  public CollationKey(String source, RawCollationKey key) {
    this.m_source_ = source;
    this.m_key_ = key.releaseBytes();
    this.m_hashCode_ = 0;
    this.m_length_ = -1;
  }
  
  public String getSourceString() {
    return this.m_source_;
  }
  
  public byte[] toByteArray() {
    int length = 0;
    while (this.m_key_[length] != 0)
      length++; 
    length++;
    byte[] result = new byte[length];
    System.arraycopy(this.m_key_, 0, result, 0, length);
    return result;
  }
  
  public int compareTo(CollationKey target) {
    for (int i = 0;; i++) {
      int l = this.m_key_[i] & 0xFF;
      int r = target.m_key_[i] & 0xFF;
      if (l < r)
        return -1; 
      if (l > r)
        return 1; 
      if (l == 0)
        return 0; 
    } 
  }
  
  public boolean equals(Object target) {
    if (!(target instanceof CollationKey))
      return false; 
    return equals((CollationKey)target);
  }
  
  public boolean equals(CollationKey target) {
    if (this == target)
      return true; 
    if (target == null)
      return false; 
    CollationKey other = target;
    int i = 0;
    while (true) {
      if (this.m_key_[i] != other.m_key_[i])
        return false; 
      if (this.m_key_[i] == 0)
        break; 
      i++;
    } 
    return true;
  }
  
  public int hashCode() {
    if (this.m_hashCode_ == 0)
      if (this.m_key_ == null) {
        this.m_hashCode_ = 1;
      } else {
        int size = this.m_key_.length >> 1;
        StringBuilder key = new StringBuilder(size);
        int i = 0;
        while (this.m_key_[i] != 0 && this.m_key_[i + 1] != 0) {
          key.append((char)(this.m_key_[i] << 8 | 0xFF & this.m_key_[i + 1]));
          i += 2;
        } 
        if (this.m_key_[i] != 0)
          key.append((char)(this.m_key_[i] << 8)); 
        this.m_hashCode_ = key.toString().hashCode();
      }  
    return this.m_hashCode_;
  }
  
  public CollationKey getBound(int boundType, int noOfLevels) {
    int offset = 0;
    int keystrength = 0;
    if (noOfLevels > 0)
      while (offset < this.m_key_.length && this.m_key_[offset] != 0) {
        if (this.m_key_[offset++] == 1) {
          keystrength++;
          noOfLevels--;
          if (noOfLevels == 0 || offset == this.m_key_.length || this.m_key_[offset] == 0) {
            offset--;
            break;
          } 
        } 
      }  
    if (noOfLevels > 0)
      throw new IllegalArgumentException("Source collation key has only " + keystrength + " strength level. Call getBound() again " + " with noOfLevels < " + keystrength); 
    byte[] resultkey = new byte[offset + boundType + 1];
    System.arraycopy(this.m_key_, 0, resultkey, 0, offset);
    switch (boundType) {
      case 0:
        resultkey[offset] = 0;
        return new CollationKey(null, resultkey, offset);
      case 1:
        resultkey[offset++] = 2;
      case 2:
        resultkey[offset++] = -1;
        resultkey[offset++] = -1;
    } 
    throw new IllegalArgumentException("Illegal boundType argument");
  }
  
  public CollationKey merge(CollationKey source) {
    if (source == null || source.getLength() == 0)
      throw new IllegalArgumentException("CollationKey argument can not be null or of 0 length"); 
    byte[] result = new byte[getLength() + source.getLength() + 2];
    int rindex = 0;
    int index = 0;
    int sourceindex = 0;
    while (true) {
      if (this.m_key_[index] < 0 || this.m_key_[index] >= 2) {
        result[rindex++] = this.m_key_[index++];
        continue;
      } 
      result[rindex++] = 2;
      while (source.m_key_[sourceindex] < 0 || source.m_key_[sourceindex] >= 2)
        result[rindex++] = source.m_key_[sourceindex++]; 
      if (this.m_key_[index] == 1 && source.m_key_[sourceindex] == 1) {
        index++;
        sourceindex++;
        result[rindex++] = 1;
        continue;
      } 
      break;
    } 
    int remainingLength;
    if ((remainingLength = this.m_length_ - index) > 0) {
      System.arraycopy(this.m_key_, index, result, rindex, remainingLength);
      rindex += remainingLength;
    } else if ((remainingLength = source.m_length_ - sourceindex) > 0) {
      System.arraycopy(source.m_key_, sourceindex, result, rindex, remainingLength);
      rindex += remainingLength;
    } 
    result[rindex] = 0;
    assert rindex == result.length - 1;
    return new CollationKey(null, result, rindex);
  }
  
  private int getLength() {
    if (this.m_length_ >= 0)
      return this.m_length_; 
    int length = this.m_key_.length;
    for (int index = 0; index < length; index++) {
      if (this.m_key_[index] == 0) {
        length = index;
        break;
      } 
    } 
    this.m_length_ = length;
    return this.m_length_;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\CollationKey.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */