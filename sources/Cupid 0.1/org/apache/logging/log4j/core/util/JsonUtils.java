package org.apache.logging.log4j.core.util;

public final class JsonUtils {
  private static final char[] HC = "0123456789ABCDEF".toCharArray();
  
  private static final int[] ESC_CODES;
  
  static {
    int[] table = new int[128];
    for (int i = 0; i < 32; i++)
      table[i] = -1; 
    table[34] = 34;
    table[92] = 92;
    table[8] = 98;
    table[9] = 116;
    table[12] = 102;
    table[10] = 110;
    table[13] = 114;
    ESC_CODES = table;
  }
  
  private static final ThreadLocal<char[]> _qbufLocal = (ThreadLocal)new ThreadLocal<>();
  
  private static char[] getQBuf() {
    char[] _qbuf = _qbufLocal.get();
    if (_qbuf == null) {
      _qbuf = new char[6];
      _qbuf[0] = '\\';
      _qbuf[2] = '0';
      _qbuf[3] = '0';
      _qbufLocal.set(_qbuf);
    } 
    return _qbuf;
  }
  
  public static void quoteAsString(CharSequence input, StringBuilder output) {
    char[] qbuf = getQBuf();
    int escCodeCount = ESC_CODES.length;
    int inPtr = 0;
    int inputLen = input.length();
    while (inPtr < inputLen) {
      while (true) {
        char d, c = input.charAt(inPtr);
        if (c < escCodeCount && ESC_CODES[c] != 0) {
          d = input.charAt(inPtr++);
          int escCode = ESC_CODES[d];
          if (escCode < 0);
          int length = _appendNamed(escCode, qbuf);
          output.append(qbuf, 0, length);
          continue;
        } 
        output.append(d);
        if (++inPtr >= inputLen)
          break; 
      } 
    } 
  }
  
  private static int _appendNumeric(int value, char[] qbuf) {
    qbuf[1] = 'u';
    qbuf[4] = HC[value >> 4];
    qbuf[5] = HC[value & 0xF];
    return 6;
  }
  
  private static int _appendNamed(int esc, char[] qbuf) {
    qbuf[1] = (char)esc;
    return 2;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\JsonUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */