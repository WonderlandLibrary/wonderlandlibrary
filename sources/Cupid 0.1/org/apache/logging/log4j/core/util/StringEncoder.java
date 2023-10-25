package org.apache.logging.log4j.core.util;

import java.nio.charset.Charset;

public final class StringEncoder {
  public static byte[] toBytes(String str, Charset charset) {
    if (str != null)
      return str.getBytes((charset != null) ? charset : Charset.defaultCharset()); 
    return null;
  }
  
  @Deprecated
  public static byte[] encodeSingleByteChars(CharSequence s) {
    int length = s.length();
    byte[] result = new byte[length];
    encodeString(s, 0, length, result);
    return result;
  }
  
  @Deprecated
  public static int encodeIsoChars(CharSequence charArray, int charIndex, byte[] byteArray, int byteIndex, int length) {
    int i = 0;
    for (; i < length; i++) {
      char c = charArray.charAt(charIndex++);
      if (c > 'ÿ')
        break; 
      byteArray[byteIndex++] = (byte)c;
    } 
    return i;
  }
  
  @Deprecated
  public static int encodeString(CharSequence charArray, int charOffset, int charLength, byte[] byteArray) {
    int byteOffset = 0;
    int length = Math.min(charLength, byteArray.length);
    int charDoneIndex = charOffset + length;
    while (charOffset < charDoneIndex) {
      int done = encodeIsoChars(charArray, charOffset, byteArray, byteOffset, length);
      charOffset += done;
      byteOffset += done;
      if (done != length) {
        char c = charArray.charAt(charOffset++);
        if (Character.isHighSurrogate(c) && charOffset < charDoneIndex && 
          Character.isLowSurrogate(charArray.charAt(charOffset))) {
          if (charLength > byteArray.length) {
            charDoneIndex++;
            charLength--;
          } 
          charOffset++;
        } 
        byteArray[byteOffset++] = 63;
        length = Math.min(charDoneIndex - charOffset, byteArray.length - byteOffset);
      } 
    } 
    return byteOffset;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\StringEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */