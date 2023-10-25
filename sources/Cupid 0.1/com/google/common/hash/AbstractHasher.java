package com.google.common.hash;

import java.nio.charset.Charset;

abstract class AbstractHasher implements Hasher {
  public final Hasher putBoolean(boolean b) {
    return putByte(b ? 1 : 0);
  }
  
  public final Hasher putDouble(double d) {
    return putLong(Double.doubleToRawLongBits(d));
  }
  
  public final Hasher putFloat(float f) {
    return putInt(Float.floatToRawIntBits(f));
  }
  
  public Hasher putUnencodedChars(CharSequence charSequence) {
    for (int i = 0, len = charSequence.length(); i < len; i++)
      putChar(charSequence.charAt(i)); 
    return this;
  }
  
  public Hasher putString(CharSequence charSequence, Charset charset) {
    return putBytes(charSequence.toString().getBytes(charset));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\hash\AbstractHasher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */