package org.apache.logging.log4j.core.config.plugins.convert;

public class HexConverter {
  public static byte[] parseHexBinary(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2)
      data[i / 2] = 
        (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16)); 
    return data;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\core\config\plugins\convert\HexConverter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */