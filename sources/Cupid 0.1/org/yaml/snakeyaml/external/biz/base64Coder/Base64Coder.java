package org.yaml.snakeyaml.external.biz.base64Coder;

public class Base64Coder {
  private static final String systemLineSeparator = System.getProperty("line.separator");
  
  private static char[] map1 = new char[64];
  
  static {
    int i = 0;
    char c;
    for (c = 'A'; c <= 'Z'; c = (char)(c + 1))
      map1[i++] = c; 
    for (c = 'a'; c <= 'z'; c = (char)(c + 1))
      map1[i++] = c; 
    for (c = '0'; c <= '9'; c = (char)(c + 1))
      map1[i++] = c; 
    map1[i++] = '+';
    map1[i++] = '/';
  }
  
  private static byte[] map2 = new byte[128];
  
  static {
    for (i = 0; i < map2.length; i++)
      map2[i] = -1; 
    for (i = 0; i < 64; i++)
      map2[map1[i]] = (byte)i; 
  }
  
  public static String encodeString(String s) {
    return new String(encode(s.getBytes()));
  }
  
  public static String encodeLines(byte[] in) {
    return encodeLines(in, 0, in.length, 76, systemLineSeparator);
  }
  
  public static String encodeLines(byte[] in, int iOff, int iLen, int lineLen, String lineSeparator) {
    int blockLen = lineLen * 3 / 4;
    if (blockLen <= 0)
      throw new IllegalArgumentException(); 
    int lines = (iLen + blockLen - 1) / blockLen;
    int bufLen = (iLen + 2) / 3 * 4 + lines * lineSeparator.length();
    StringBuilder buf = new StringBuilder(bufLen);
    int ip = 0;
    while (ip < iLen) {
      int l = Math.min(iLen - ip, blockLen);
      buf.append(encode(in, iOff + ip, l));
      buf.append(lineSeparator);
      ip += l;
    } 
    return buf.toString();
  }
  
  public static char[] encode(byte[] in) {
    return encode(in, 0, in.length);
  }
  
  public static char[] encode(byte[] in, int iLen) {
    return encode(in, 0, iLen);
  }
  
  public static char[] encode(byte[] in, int iOff, int iLen) {
    int oDataLen = (iLen * 4 + 2) / 3;
    int oLen = (iLen + 2) / 3 * 4;
    char[] out = new char[oLen];
    int ip = iOff;
    int iEnd = iOff + iLen;
    int op = 0;
    while (ip < iEnd) {
      int i0 = in[ip++] & 0xFF;
      int i1 = (ip < iEnd) ? (in[ip++] & 0xFF) : 0;
      int i2 = (ip < iEnd) ? (in[ip++] & 0xFF) : 0;
      int o0 = i0 >>> 2;
      int o1 = (i0 & 0x3) << 4 | i1 >>> 4;
      int o2 = (i1 & 0xF) << 2 | i2 >>> 6;
      int o3 = i2 & 0x3F;
      out[op++] = map1[o0];
      out[op++] = map1[o1];
      out[op] = (op < oDataLen) ? map1[o2] : '=';
      op++;
      out[op] = (op < oDataLen) ? map1[o3] : '=';
      op++;
    } 
    return out;
  }
  
  public static String decodeString(String s) {
    return new String(decode(s));
  }
  
  public static byte[] decodeLines(String s) {
    char[] buf = new char[s.length()];
    int p = 0;
    for (int ip = 0; ip < s.length(); ip++) {
      char c = s.charAt(ip);
      if (c != ' ' && c != '\r' && c != '\n' && c != '\t')
        buf[p++] = c; 
    } 
    return decode(buf, 0, p);
  }
  
  public static byte[] decode(String s) {
    return decode(s.toCharArray());
  }
  
  public static byte[] decode(char[] in) {
    return decode(in, 0, in.length);
  }
  
  public static byte[] decode(char[] in, int iOff, int iLen) {
    if (iLen % 4 != 0)
      throw new IllegalArgumentException("Length of Base64 encoded input string is not a multiple of 4."); 
    while (iLen > 0 && in[iOff + iLen - 1] == '=')
      iLen--; 
    int oLen = iLen * 3 / 4;
    byte[] out = new byte[oLen];
    int ip = iOff;
    int iEnd = iOff + iLen;
    int op = 0;
    while (ip < iEnd) {
      int i0 = in[ip++];
      int i1 = in[ip++];
      int i2 = (ip < iEnd) ? in[ip++] : 65;
      int i3 = (ip < iEnd) ? in[ip++] : 65;
      if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
        throw new IllegalArgumentException("Illegal character in Base64 encoded data."); 
      int b0 = map2[i0];
      int b1 = map2[i1];
      int b2 = map2[i2];
      int b3 = map2[i3];
      if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
        throw new IllegalArgumentException("Illegal character in Base64 encoded data."); 
      int o0 = b0 << 2 | b1 >>> 4;
      int o1 = (b1 & 0xF) << 4 | b2 >>> 2;
      int o2 = (b2 & 0x3) << 6 | b3;
      out[op++] = (byte)o0;
      if (op < oLen)
        out[op++] = (byte)o1; 
      if (op < oLen)
        out[op++] = (byte)o2; 
    } 
    return out;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\external\biz\base64Coder\Base64Coder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */