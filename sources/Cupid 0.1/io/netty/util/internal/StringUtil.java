package io.netty.util.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public final class StringUtil {
  public static final String NEWLINE;
  
  private static final String[] BYTE2HEX_PAD;
  
  private static final String[] BYTE2HEX_NOPAD;
  
  private static final String EMPTY_STRING = "";
  
  static {
    String str;
  }
  
  static {
    BYTE2HEX_PAD = new String[256];
    BYTE2HEX_NOPAD = new String[256];
    try {
      str = (new Formatter()).format("%n", new Object[0]).toString();
    } catch (Exception e) {
      str = "\n";
    } 
    NEWLINE = str;
    int i;
    for (i = 0; i < 10; i++) {
      StringBuilder buf = new StringBuilder(2);
      buf.append('0');
      buf.append(i);
      BYTE2HEX_PAD[i] = buf.toString();
      BYTE2HEX_NOPAD[i] = String.valueOf(i);
    } 
    for (; i < 16; i++) {
      StringBuilder buf = new StringBuilder(2);
      char c = (char)(97 + i - 10);
      buf.append('0');
      buf.append(c);
      BYTE2HEX_PAD[i] = buf.toString();
      BYTE2HEX_NOPAD[i] = String.valueOf(c);
    } 
    for (; i < BYTE2HEX_PAD.length; i++) {
      StringBuilder buf = new StringBuilder(2);
      buf.append(Integer.toHexString(i));
      String str1 = buf.toString();
      BYTE2HEX_PAD[i] = str1;
      BYTE2HEX_NOPAD[i] = str1;
    } 
  }
  
  public static String[] split(String value, char delim) {
    int end = value.length();
    List<String> res = new ArrayList<String>();
    int start = 0;
    int i;
    for (i = 0; i < end; i++) {
      if (value.charAt(i) == delim) {
        if (start == i) {
          res.add("");
        } else {
          res.add(value.substring(start, i));
        } 
        start = i + 1;
      } 
    } 
    if (start == 0) {
      res.add(value);
    } else if (start != end) {
      res.add(value.substring(start, end));
    } else {
      for (i = res.size() - 1; i >= 0 && (
        (String)res.get(i)).isEmpty(); i--)
        res.remove(i); 
    } 
    return res.<String>toArray(new String[res.size()]);
  }
  
  public static String byteToHexStringPadded(int value) {
    return BYTE2HEX_PAD[value & 0xFF];
  }
  
  public static <T extends Appendable> T byteToHexStringPadded(T buf, int value) {
    try {
      buf.append(byteToHexStringPadded(value));
    } catch (IOException e) {
      PlatformDependent.throwException(e);
    } 
    return buf;
  }
  
  public static String toHexStringPadded(byte[] src) {
    return toHexStringPadded(src, 0, src.length);
  }
  
  public static String toHexStringPadded(byte[] src, int offset, int length) {
    return ((StringBuilder)toHexStringPadded(new StringBuilder(length << 1), src, offset, length)).toString();
  }
  
  public static <T extends Appendable> T toHexStringPadded(T dst, byte[] src) {
    return toHexStringPadded(dst, src, 0, src.length);
  }
  
  public static <T extends Appendable> T toHexStringPadded(T dst, byte[] src, int offset, int length) {
    int end = offset + length;
    for (int i = offset; i < end; i++)
      byteToHexStringPadded(dst, src[i]); 
    return dst;
  }
  
  public static String byteToHexString(int value) {
    return BYTE2HEX_NOPAD[value & 0xFF];
  }
  
  public static <T extends Appendable> T byteToHexString(T buf, int value) {
    try {
      buf.append(byteToHexString(value));
    } catch (IOException e) {
      PlatformDependent.throwException(e);
    } 
    return buf;
  }
  
  public static String toHexString(byte[] src) {
    return toHexString(src, 0, src.length);
  }
  
  public static String toHexString(byte[] src, int offset, int length) {
    return ((StringBuilder)toHexString(new StringBuilder(length << 1), src, offset, length)).toString();
  }
  
  public static <T extends Appendable> T toHexString(T dst, byte[] src) {
    return toHexString(dst, src, 0, src.length);
  }
  
  public static <T extends Appendable> T toHexString(T dst, byte[] src, int offset, int length) {
    assert length >= 0;
    if (length == 0)
      return dst; 
    int end = offset + length;
    int endMinusOne = end - 1;
    int i;
    for (i = offset; i < endMinusOne && 
      src[i] == 0; i++);
    byteToHexString(dst, src[i++]);
    int remaining = end - i;
    toHexStringPadded(dst, src, i, remaining);
    return dst;
  }
  
  public static String simpleClassName(Object o) {
    if (o == null)
      return "null_object"; 
    return simpleClassName(o.getClass());
  }
  
  public static String simpleClassName(Class<?> clazz) {
    if (clazz == null)
      return "null_class"; 
    Package pkg = clazz.getPackage();
    if (pkg != null)
      return clazz.getName().substring(pkg.getName().length() + 1); 
    return clazz.getName();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\StringUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */