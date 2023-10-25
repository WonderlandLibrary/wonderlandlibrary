package com.google.common.net;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.escape.UnicodeEscaper;

@Beta
@GwtCompatible
public final class PercentEscaper extends UnicodeEscaper {
  private static final char[] PLUS_SIGN = new char[] { '+' };
  
  private static final char[] UPPER_HEX_DIGITS = "0123456789ABCDEF".toCharArray();
  
  private final boolean plusForSpace;
  
  private final boolean[] safeOctets;
  
  public PercentEscaper(String safeChars, boolean plusForSpace) {
    Preconditions.checkNotNull(safeChars);
    if (safeChars.matches(".*[0-9A-Za-z].*"))
      throw new IllegalArgumentException("Alphanumeric characters are always 'safe' and should not be explicitly specified"); 
    safeChars = safeChars + "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    if (plusForSpace && safeChars.contains(" "))
      throw new IllegalArgumentException("plusForSpace cannot be specified when space is a 'safe' character"); 
    this.plusForSpace = plusForSpace;
    this.safeOctets = createSafeOctets(safeChars);
  }
  
  private static boolean[] createSafeOctets(String safeChars) {
    int maxChar = -1;
    char[] safeCharArray = safeChars.toCharArray();
    for (char c : safeCharArray)
      maxChar = Math.max(c, maxChar); 
    boolean[] octets = new boolean[maxChar + 1];
    for (char c : safeCharArray)
      octets[c] = true; 
    return octets;
  }
  
  protected int nextEscapeIndex(CharSequence csq, int index, int end) {
    Preconditions.checkNotNull(csq);
    for (; index < end; index++) {
      char c = csq.charAt(index);
      if (c >= this.safeOctets.length || !this.safeOctets[c])
        break; 
    } 
    return index;
  }
  
  public String escape(String s) {
    Preconditions.checkNotNull(s);
    int slen = s.length();
    for (int index = 0; index < slen; index++) {
      char c = s.charAt(index);
      if (c >= this.safeOctets.length || !this.safeOctets[c])
        return escapeSlow(s, index); 
    } 
    return s;
  }
  
  protected char[] escape(int cp) {
    if (cp < this.safeOctets.length && this.safeOctets[cp])
      return null; 
    if (cp == 32 && this.plusForSpace)
      return PLUS_SIGN; 
    if (cp <= 127) {
      char[] dest = new char[3];
      dest[0] = '%';
      dest[2] = UPPER_HEX_DIGITS[cp & 0xF];
      dest[1] = UPPER_HEX_DIGITS[cp >>> 4];
      return dest;
    } 
    if (cp <= 2047) {
      char[] dest = new char[6];
      dest[0] = '%';
      dest[3] = '%';
      dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
      cp >>>= 4;
      dest[4] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
      cp >>>= 2;
      dest[2] = UPPER_HEX_DIGITS[cp & 0xF];
      cp >>>= 4;
      dest[1] = UPPER_HEX_DIGITS[0xC | cp];
      return dest;
    } 
    if (cp <= 65535) {
      char[] dest = new char[9];
      dest[0] = '%';
      dest[1] = 'E';
      dest[3] = '%';
      dest[6] = '%';
      dest[8] = UPPER_HEX_DIGITS[cp & 0xF];
      cp >>>= 4;
      dest[7] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
      cp >>>= 2;
      dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
      cp >>>= 4;
      dest[4] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
      cp >>>= 2;
      dest[2] = UPPER_HEX_DIGITS[cp];
      return dest;
    } 
    if (cp <= 1114111) {
      char[] dest = new char[12];
      dest[0] = '%';
      dest[1] = 'F';
      dest[3] = '%';
      dest[6] = '%';
      dest[9] = '%';
      dest[11] = UPPER_HEX_DIGITS[cp & 0xF];
      cp >>>= 4;
      dest[10] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
      cp >>>= 2;
      dest[8] = UPPER_HEX_DIGITS[cp & 0xF];
      cp >>>= 4;
      dest[7] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
      cp >>>= 2;
      dest[5] = UPPER_HEX_DIGITS[cp & 0xF];
      cp >>>= 4;
      dest[4] = UPPER_HEX_DIGITS[0x8 | cp & 0x3];
      cp >>>= 2;
      dest[2] = UPPER_HEX_DIGITS[cp & 0x7];
      return dest;
    } 
    throw new IllegalArgumentException("Invalid unicode character value " + cp);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\common\net\PercentEscaper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */