package org.apache.commons.lang3;

public class BitField {
   private final int _mask;
   private final int _shift_count;

   public BitField(int var1) {
      this._mask = var1;
      int var2 = 0;
      int var3 = var1;
      if (var1 != 0) {
         while((var3 & 1) == 0) {
            ++var2;
            var3 >>= 1;
         }
      }

      this._shift_count = var2;
   }

   public int getValue(int var1) {
      return this.getRawValue(var1) >> this._shift_count;
   }

   public short getShortValue(short var1) {
      return (short)this.getValue(var1);
   }

   public int getRawValue(int var1) {
      return var1 & this._mask;
   }

   public short getShortRawValue(short var1) {
      return (short)this.getRawValue(var1);
   }

   public boolean isSet(int var1) {
      return (var1 & this._mask) != 0;
   }

   public boolean isAllSet(int var1) {
      return (var1 & this._mask) == this._mask;
   }

   public int setValue(int var1, int var2) {
      return var1 & ~this._mask | var2 << this._shift_count & this._mask;
   }

   public short setShortValue(short var1, short var2) {
      return (short)this.setValue(var1, var2);
   }

   public int clear(int var1) {
      return var1 & ~this._mask;
   }

   public short clearShort(short var1) {
      return (short)this.clear(var1);
   }

   public byte clearByte(byte var1) {
      return (byte)this.clear(var1);
   }

   public int set(int var1) {
      return var1 | this._mask;
   }

   public short setShort(short var1) {
      return (short)this.set(var1);
   }

   public byte setByte(byte var1) {
      return (byte)this.set(var1);
   }

   public int setBoolean(int var1, boolean var2) {
      return var2 ? this.set(var1) : this.clear(var1);
   }

   public short setShortBoolean(short var1, boolean var2) {
      return var2 ? this.setShort(var1) : this.clearShort(var1);
   }

   public byte setByteBoolean(byte var1, boolean var2) {
      return var2 ? this.setByte(var1) : this.clearByte(var1);
   }
}
