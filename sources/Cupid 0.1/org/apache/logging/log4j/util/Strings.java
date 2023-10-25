package org.apache.logging.log4j.util;

import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

public final class Strings {
  private static final ThreadLocal<StringBuilder> tempStr = ThreadLocal.withInitial(StringBuilder::new);
  
  public static final String EMPTY = "";
  
  public static final String[] EMPTY_ARRAY = new String[0];
  
  public static final String LINE_SEPARATOR = PropertiesUtil.getProperties().getStringProperty("line.separator", "\n");
  
  public static String dquote(String str) {
    return '"' + str + '"';
  }
  
  public static boolean isBlank(String s) {
    if (s == null || s.isEmpty())
      return true; 
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (!Character.isWhitespace(c))
        return false; 
    } 
    return true;
  }
  
  public static boolean isEmpty(CharSequence cs) {
    return (cs == null || cs.length() == 0);
  }
  
  public static boolean isNotBlank(String s) {
    return !isBlank(s);
  }
  
  public static boolean isNotEmpty(CharSequence cs) {
    return !isEmpty(cs);
  }
  
  public static String join(Iterable<?> iterable, char separator) {
    if (iterable == null)
      return null; 
    return join(iterable.iterator(), separator);
  }
  
  public static String join(Iterator<?> iterator, char separator) {
    if (iterator == null)
      return null; 
    if (!iterator.hasNext())
      return ""; 
    Object first = iterator.next();
    if (!iterator.hasNext())
      return Objects.toString(first, ""); 
    StringBuilder buf = new StringBuilder(256);
    if (first != null)
      buf.append(first); 
    while (iterator.hasNext()) {
      buf.append(separator);
      Object obj = iterator.next();
      if (obj != null)
        buf.append(obj); 
    } 
    return buf.toString();
  }
  
  public static String left(String str, int len) {
    if (str == null)
      return null; 
    if (len < 0)
      return ""; 
    if (str.length() <= len)
      return str; 
    return str.substring(0, len);
  }
  
  public static String quote(String str) {
    return '\'' + str + '\'';
  }
  
  public static String trimToNull(String str) {
    String ts = (str == null) ? null : str.trim();
    return isEmpty(ts) ? null : ts;
  }
  
  public static String toRootUpperCase(String str) {
    return str.toUpperCase(Locale.ROOT);
  }
  
  public static String concat(String str1, String str2) {
    if (isEmpty(str1))
      return str2; 
    if (isEmpty(str2))
      return str1; 
    StringBuilder sb = tempStr.get();
    try {
      return sb.append(str1).append(str2).toString();
    } finally {
      sb.setLength(0);
    } 
  }
  
  public static String repeat(String str, int count) {
    Objects.requireNonNull(str, "str");
    if (count < 0)
      throw new IllegalArgumentException("count"); 
    StringBuilder sb = tempStr.get();
    try {
      for (int index = 0; index < count; index++)
        sb.append(str); 
      return sb.toString();
    } finally {
      sb.setLength(0);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4\\util\Strings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */