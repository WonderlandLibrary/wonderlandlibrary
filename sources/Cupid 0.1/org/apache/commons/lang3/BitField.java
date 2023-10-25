package org.apache.commons.lang3;

public class BitField {
  private final int _mask;
  
  private final int _shift_count;
  
  public BitField(int mask) {
    this._mask = mask;
    int count = 0;
    int bit_pattern = mask;
    if (bit_pattern != 0)
      while ((bit_pattern & 0x1) == 0) {
        count++;
        bit_pattern >>= 1;
      }  
    this._shift_count = count;
  }
  
  public int getValue(int holder) {
    return getRawValue(holder) >> this._shift_count;
  }
  
  public short getShortValue(short holder) {
    return (short)getValue(holder);
  }
  
  public int getRawValue(int holder) {
    return holder & this._mask;
  }
  
  public short getShortRawValue(short holder) {
    return (short)getRawValue(holder);
  }
  
  public boolean isSet(int holder) {
    return ((holder & this._mask) != 0);
  }
  
  public boolean isAllSet(int holder) {
    return ((holder & this._mask) == this._mask);
  }
  
  public int setValue(int holder, int value) {
    return holder & (this._mask ^ 0xFFFFFFFF) | value << this._shift_count & this._mask;
  }
  
  public short setShortValue(short holder, short value) {
    return (short)setValue(holder, value);
  }
  
  public int clear(int holder) {
    return holder & (this._mask ^ 0xFFFFFFFF);
  }
  
  public short clearShort(short holder) {
    return (short)clear(holder);
  }
  
  public byte clearByte(byte holder) {
    return (byte)clear(holder);
  }
  
  public int set(int holder) {
    return holder | this._mask;
  }
  
  public short setShort(short holder) {
    return (short)set(holder);
  }
  
  public byte setByte(byte holder) {
    return (byte)set(holder);
  }
  
  public int setBoolean(int holder, boolean flag) {
    return flag ? set(holder) : clear(holder);
  }
  
  public short setShortBoolean(short holder, boolean flag) {
    return flag ? setShort(holder) : clearShort(holder);
  }
  
  public byte setByteBoolean(byte holder, boolean flag) {
    return flag ? setByte(holder) : clearByte(holder);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\BitField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */