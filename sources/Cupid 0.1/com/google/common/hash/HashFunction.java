package com.google.common.hash;

import com.google.common.annotations.Beta;
import java.nio.charset.Charset;

@Beta
public interface HashFunction {
  Hasher newHasher();
  
  Hasher newHasher(int paramInt);
  
  HashCode hashInt(int paramInt);
  
  HashCode hashLong(long paramLong);
  
  HashCode hashBytes(byte[] paramArrayOfbyte);
  
  HashCode hashBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  HashCode hashUnencodedChars(CharSequence paramCharSequence);
  
  HashCode hashString(CharSequence paramCharSequence, Charset paramCharset);
  
  <T> HashCode hashObject(T paramT, Funnel<? super T> paramFunnel);
  
  int bits();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\hash\HashFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */