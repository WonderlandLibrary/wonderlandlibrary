package com.google.common.hash;

import com.google.common.annotations.Beta;
import java.nio.charset.Charset;

@Beta
public interface Hasher extends PrimitiveSink {
  Hasher putByte(byte paramByte);
  
  Hasher putBytes(byte[] paramArrayOfbyte);
  
  Hasher putBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  Hasher putShort(short paramShort);
  
  Hasher putInt(int paramInt);
  
  Hasher putLong(long paramLong);
  
  Hasher putFloat(float paramFloat);
  
  Hasher putDouble(double paramDouble);
  
  Hasher putBoolean(boolean paramBoolean);
  
  Hasher putChar(char paramChar);
  
  Hasher putUnencodedChars(CharSequence paramCharSequence);
  
  Hasher putString(CharSequence paramCharSequence, Charset paramCharset);
  
  <T> Hasher putObject(T paramT, Funnel<? super T> paramFunnel);
  
  HashCode hash();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\hash\Hasher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */