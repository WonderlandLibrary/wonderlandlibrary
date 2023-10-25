package com.ibm.icu.text;

import com.ibm.icu.impl.UCharacterProperty;
import java.util.Comparator;

public final class UTF16 {
  public static final int SINGLE_CHAR_BOUNDARY = 1;
  
  public static final int LEAD_SURROGATE_BOUNDARY = 2;
  
  public static final int TRAIL_SURROGATE_BOUNDARY = 5;
  
  public static final int CODEPOINT_MIN_VALUE = 0;
  
  public static final int CODEPOINT_MAX_VALUE = 1114111;
  
  public static final int SUPPLEMENTARY_MIN_VALUE = 65536;
  
  public static final int LEAD_SURROGATE_MIN_VALUE = 55296;
  
  public static final int TRAIL_SURROGATE_MIN_VALUE = 56320;
  
  public static final int LEAD_SURROGATE_MAX_VALUE = 56319;
  
  public static final int TRAIL_SURROGATE_MAX_VALUE = 57343;
  
  public static final int SURROGATE_MIN_VALUE = 55296;
  
  public static final int SURROGATE_MAX_VALUE = 57343;
  
  private static final int LEAD_SURROGATE_BITMASK = -1024;
  
  private static final int TRAIL_SURROGATE_BITMASK = -1024;
  
  private static final int SURROGATE_BITMASK = -2048;
  
  private static final int LEAD_SURROGATE_BITS = 55296;
  
  private static final int TRAIL_SURROGATE_BITS = 56320;
  
  private static final int SURROGATE_BITS = 55296;
  
  private static final int LEAD_SURROGATE_SHIFT_ = 10;
  
  private static final int TRAIL_SURROGATE_MASK_ = 1023;
  
  private static final int LEAD_SURROGATE_OFFSET_ = 55232;
  
  public static int charAt(String source, int offset16) {
    char single = source.charAt(offset16);
    if (single < '?')
      return single; 
    return _charAt(source, offset16, single);
  }
  
  private static int _charAt(String source, int offset16, char single) {
    if (single > '?')
      return single; 
    if (single <= '?') {
      offset16++;
      if (source.length() != offset16) {
        char trail = source.charAt(offset16);
        if (trail >= '?' && trail <= '?')
          return UCharacterProperty.getRawSupplementary(single, trail); 
      } 
    } else {
      offset16--;
      if (offset16 >= 0) {
        char lead = source.charAt(offset16);
        if (lead >= '?' && lead <= '?')
          return UCharacterProperty.getRawSupplementary(lead, single); 
      } 
    } 
    return single;
  }
  
  public static int charAt(CharSequence source, int offset16) {
    char single = source.charAt(offset16);
    if (single < '?')
      return single; 
    return _charAt(source, offset16, single);
  }
  
  private static int _charAt(CharSequence source, int offset16, char single) {
    if (single > '?')
      return single; 
    if (single <= '?') {
      offset16++;
      if (source.length() != offset16) {
        char trail = source.charAt(offset16);
        if (trail >= '?' && trail <= '?')
          return UCharacterProperty.getRawSupplementary(single, trail); 
      } 
    } else {
      offset16--;
      if (offset16 >= 0) {
        char lead = source.charAt(offset16);
        if (lead >= '?' && lead <= '?')
          return UCharacterProperty.getRawSupplementary(lead, single); 
      } 
    } 
    return single;
  }
  
  public static int charAt(StringBuffer source, int offset16) {
    if (offset16 < 0 || offset16 >= source.length())
      throw new StringIndexOutOfBoundsException(offset16); 
    char single = source.charAt(offset16);
    if (!isSurrogate(single))
      return single; 
    if (single <= '?') {
      offset16++;
      if (source.length() != offset16) {
        char trail = source.charAt(offset16);
        if (isTrailSurrogate(trail))
          return UCharacterProperty.getRawSupplementary(single, trail); 
      } 
    } else {
      offset16--;
      if (offset16 >= 0) {
        char lead = source.charAt(offset16);
        if (isLeadSurrogate(lead))
          return UCharacterProperty.getRawSupplementary(lead, single); 
      } 
    } 
    return single;
  }
  
  public static int charAt(char[] source, int start, int limit, int offset16) {
    offset16 += start;
    if (offset16 < start || offset16 >= limit)
      throw new ArrayIndexOutOfBoundsException(offset16); 
    char single = source[offset16];
    if (!isSurrogate(single))
      return single; 
    if (single <= '?') {
      offset16++;
      if (offset16 >= limit)
        return single; 
      char trail = source[offset16];
      if (isTrailSurrogate(trail))
        return UCharacterProperty.getRawSupplementary(single, trail); 
    } else {
      if (offset16 == start)
        return single; 
      offset16--;
      char lead = source[offset16];
      if (isLeadSurrogate(lead))
        return UCharacterProperty.getRawSupplementary(lead, single); 
    } 
    return single;
  }
  
  public static int charAt(Replaceable source, int offset16) {
    if (offset16 < 0 || offset16 >= source.length())
      throw new StringIndexOutOfBoundsException(offset16); 
    char single = source.charAt(offset16);
    if (!isSurrogate(single))
      return single; 
    if (single <= '?') {
      offset16++;
      if (source.length() != offset16) {
        char trail = source.charAt(offset16);
        if (isTrailSurrogate(trail))
          return UCharacterProperty.getRawSupplementary(single, trail); 
      } 
    } else {
      offset16--;
      if (offset16 >= 0) {
        char lead = source.charAt(offset16);
        if (isLeadSurrogate(lead))
          return UCharacterProperty.getRawSupplementary(lead, single); 
      } 
    } 
    return single;
  }
  
  public static int getCharCount(int char32) {
    if (char32 < 65536)
      return 1; 
    return 2;
  }
  
  public static int bounds(String source, int offset16) {
    char ch = source.charAt(offset16);
    if (isSurrogate(ch))
      if (isLeadSurrogate(ch)) {
        if (++offset16 < source.length() && isTrailSurrogate(source.charAt(offset16)))
          return 2; 
      } else {
        offset16--;
        if (offset16 >= 0 && isLeadSurrogate(source.charAt(offset16)))
          return 5; 
      }  
    return 1;
  }
  
  public static int bounds(StringBuffer source, int offset16) {
    char ch = source.charAt(offset16);
    if (isSurrogate(ch))
      if (isLeadSurrogate(ch)) {
        if (++offset16 < source.length() && isTrailSurrogate(source.charAt(offset16)))
          return 2; 
      } else {
        offset16--;
        if (offset16 >= 0 && isLeadSurrogate(source.charAt(offset16)))
          return 5; 
      }  
    return 1;
  }
  
  public static int bounds(char[] source, int start, int limit, int offset16) {
    offset16 += start;
    if (offset16 < start || offset16 >= limit)
      throw new ArrayIndexOutOfBoundsException(offset16); 
    char ch = source[offset16];
    if (isSurrogate(ch))
      if (isLeadSurrogate(ch)) {
        offset16++;
        if (offset16 < limit && isTrailSurrogate(source[offset16]))
          return 2; 
      } else {
        offset16--;
        if (offset16 >= start && isLeadSurrogate(source[offset16]))
          return 5; 
      }  
    return 1;
  }
  
  public static boolean isSurrogate(char char16) {
    return ((char16 & 0xFFFFF800) == 55296);
  }
  
  public static boolean isTrailSurrogate(char char16) {
    return ((char16 & 0xFFFFFC00) == 56320);
  }
  
  public static boolean isLeadSurrogate(char char16) {
    return ((char16 & 0xFFFFFC00) == 55296);
  }
  
  public static char getLeadSurrogate(int char32) {
    if (char32 >= 65536)
      return (char)(55232 + (char32 >> 10)); 
    return Character.MIN_VALUE;
  }
  
  public static char getTrailSurrogate(int char32) {
    if (char32 >= 65536)
      return (char)(56320 + (char32 & 0x3FF)); 
    return (char)char32;
  }
  
  public static String valueOf(int char32) {
    if (char32 < 0 || char32 > 1114111)
      throw new IllegalArgumentException("Illegal codepoint"); 
    return toString(char32);
  }
  
  public static String valueOf(String source, int offset16) {
    switch (bounds(source, offset16)) {
      case 2:
        return source.substring(offset16, offset16 + 2);
      case 5:
        return source.substring(offset16 - 1, offset16 + 1);
    } 
    return source.substring(offset16, offset16 + 1);
  }
  
  public static String valueOf(StringBuffer source, int offset16) {
    switch (bounds(source, offset16)) {
      case 2:
        return source.substring(offset16, offset16 + 2);
      case 5:
        return source.substring(offset16 - 1, offset16 + 1);
    } 
    return source.substring(offset16, offset16 + 1);
  }
  
  public static String valueOf(char[] source, int start, int limit, int offset16) {
    switch (bounds(source, start, limit, offset16)) {
      case 2:
        return new String(source, start + offset16, 2);
      case 5:
        return new String(source, start + offset16 - 1, 2);
    } 
    return new String(source, start + offset16, 1);
  }
  
  public static int findOffsetFromCodePoint(String source, int offset32) {
    int size = source.length(), result = 0, count = offset32;
    if (offset32 < 0 || offset32 > size)
      throw new StringIndexOutOfBoundsException(offset32); 
    while (result < size && count > 0) {
      char ch = source.charAt(result);
      if (isLeadSurrogate(ch) && result + 1 < size && isTrailSurrogate(source.charAt(result + 1)))
        result++; 
      count--;
      result++;
    } 
    if (count != 0)
      throw new StringIndexOutOfBoundsException(offset32); 
    return result;
  }
  
  public static int findOffsetFromCodePoint(StringBuffer source, int offset32) {
    int size = source.length(), result = 0, count = offset32;
    if (offset32 < 0 || offset32 > size)
      throw new StringIndexOutOfBoundsException(offset32); 
    while (result < size && count > 0) {
      char ch = source.charAt(result);
      if (isLeadSurrogate(ch) && result + 1 < size && isTrailSurrogate(source.charAt(result + 1)))
        result++; 
      count--;
      result++;
    } 
    if (count != 0)
      throw new StringIndexOutOfBoundsException(offset32); 
    return result;
  }
  
  public static int findOffsetFromCodePoint(char[] source, int start, int limit, int offset32) {
    int result = start, count = offset32;
    if (offset32 > limit - start)
      throw new ArrayIndexOutOfBoundsException(offset32); 
    while (result < limit && count > 0) {
      char ch = source[result];
      if (isLeadSurrogate(ch) && result + 1 < limit && isTrailSurrogate(source[result + 1]))
        result++; 
      count--;
      result++;
    } 
    if (count != 0)
      throw new ArrayIndexOutOfBoundsException(offset32); 
    return result - start;
  }
  
  public static int findCodePointOffset(String source, int offset16) {
    if (offset16 < 0 || offset16 > source.length())
      throw new StringIndexOutOfBoundsException(offset16); 
    int result = 0;
    boolean hadLeadSurrogate = false;
    for (int i = 0; i < offset16; i++) {
      char ch = source.charAt(i);
      if (hadLeadSurrogate && isTrailSurrogate(ch)) {
        hadLeadSurrogate = false;
      } else {
        hadLeadSurrogate = isLeadSurrogate(ch);
        result++;
      } 
    } 
    if (offset16 == source.length())
      return result; 
    if (hadLeadSurrogate && isTrailSurrogate(source.charAt(offset16)))
      result--; 
    return result;
  }
  
  public static int findCodePointOffset(StringBuffer source, int offset16) {
    if (offset16 < 0 || offset16 > source.length())
      throw new StringIndexOutOfBoundsException(offset16); 
    int result = 0;
    boolean hadLeadSurrogate = false;
    for (int i = 0; i < offset16; i++) {
      char ch = source.charAt(i);
      if (hadLeadSurrogate && isTrailSurrogate(ch)) {
        hadLeadSurrogate = false;
      } else {
        hadLeadSurrogate = isLeadSurrogate(ch);
        result++;
      } 
    } 
    if (offset16 == source.length())
      return result; 
    if (hadLeadSurrogate && isTrailSurrogate(source.charAt(offset16)))
      result--; 
    return result;
  }
  
  public static int findCodePointOffset(char[] source, int start, int limit, int offset16) {
    offset16 += start;
    if (offset16 > limit)
      throw new StringIndexOutOfBoundsException(offset16); 
    int result = 0;
    boolean hadLeadSurrogate = false;
    for (int i = start; i < offset16; i++) {
      char ch = source[i];
      if (hadLeadSurrogate && isTrailSurrogate(ch)) {
        hadLeadSurrogate = false;
      } else {
        hadLeadSurrogate = isLeadSurrogate(ch);
        result++;
      } 
    } 
    if (offset16 == limit)
      return result; 
    if (hadLeadSurrogate && isTrailSurrogate(source[offset16]))
      result--; 
    return result;
  }
  
  public static StringBuffer append(StringBuffer target, int char32) {
    if (char32 < 0 || char32 > 1114111)
      throw new IllegalArgumentException("Illegal codepoint: " + Integer.toHexString(char32)); 
    if (char32 >= 65536) {
      target.append(getLeadSurrogate(char32));
      target.append(getTrailSurrogate(char32));
    } else {
      target.append((char)char32);
    } 
    return target;
  }
  
  public static StringBuffer appendCodePoint(StringBuffer target, int cp) {
    return append(target, cp);
  }
  
  public static int append(char[] target, int limit, int char32) {
    if (char32 < 0 || char32 > 1114111)
      throw new IllegalArgumentException("Illegal codepoint"); 
    if (char32 >= 65536) {
      target[limit++] = getLeadSurrogate(char32);
      target[limit++] = getTrailSurrogate(char32);
    } else {
      target[limit++] = (char)char32;
    } 
    return limit;
  }
  
  public static int countCodePoint(String source) {
    if (source == null || source.length() == 0)
      return 0; 
    return findCodePointOffset(source, source.length());
  }
  
  public static int countCodePoint(StringBuffer source) {
    if (source == null || source.length() == 0)
      return 0; 
    return findCodePointOffset(source, source.length());
  }
  
  public static int countCodePoint(char[] source, int start, int limit) {
    if (source == null || source.length == 0)
      return 0; 
    return findCodePointOffset(source, start, limit, limit - start);
  }
  
  public static void setCharAt(StringBuffer target, int offset16, int char32) {
    int count = 1;
    char single = target.charAt(offset16);
    if (isSurrogate(single))
      if (isLeadSurrogate(single) && target.length() > offset16 + 1 && isTrailSurrogate(target.charAt(offset16 + 1))) {
        count++;
      } else if (isTrailSurrogate(single) && offset16 > 0 && isLeadSurrogate(target.charAt(offset16 - 1))) {
        offset16--;
        count++;
      }  
    target.replace(offset16, offset16 + count, valueOf(char32));
  }
  
  public static int setCharAt(char[] target, int limit, int offset16, int char32) {
    if (offset16 >= limit)
      throw new ArrayIndexOutOfBoundsException(offset16); 
    int count = 1;
    char single = target[offset16];
    if (isSurrogate(single))
      if (isLeadSurrogate(single) && target.length > offset16 + 1 && isTrailSurrogate(target[offset16 + 1])) {
        count++;
      } else if (isTrailSurrogate(single) && offset16 > 0 && isLeadSurrogate(target[offset16 - 1])) {
        offset16--;
        count++;
      }  
    String str = valueOf(char32);
    int result = limit;
    int strlength = str.length();
    target[offset16] = str.charAt(0);
    if (count == strlength) {
      if (count == 2)
        target[offset16 + 1] = str.charAt(1); 
    } else {
      System.arraycopy(target, offset16 + count, target, offset16 + strlength, limit - offset16 + count);
      if (count < strlength) {
        target[offset16 + 1] = str.charAt(1);
        result++;
        if (result < target.length)
          target[result] = Character.MIN_VALUE; 
      } else {
        result--;
        target[result] = Character.MIN_VALUE;
      } 
    } 
    return result;
  }
  
  public static int moveCodePointOffset(String source, int offset16, int shift32) {
    int count, result = offset16;
    int size = source.length();
    if (offset16 < 0 || offset16 > size)
      throw new StringIndexOutOfBoundsException(offset16); 
    if (shift32 > 0) {
      if (shift32 + offset16 > size)
        throw new StringIndexOutOfBoundsException(offset16); 
      count = shift32;
      while (result < size && count > 0) {
        char ch = source.charAt(result);
        if (isLeadSurrogate(ch) && result + 1 < size && isTrailSurrogate(source.charAt(result + 1)))
          result++; 
        count--;
        result++;
      } 
    } else {
      if (offset16 + shift32 < 0)
        throw new StringIndexOutOfBoundsException(offset16); 
      count = -shift32;
      result--;
      for (; count > 0 && result >= 0; count--) {
        char ch = source.charAt(result);
        if (isTrailSurrogate(ch) && result > 0 && isLeadSurrogate(source.charAt(result - 1)))
          result--; 
      } 
    } 
    if (count != 0)
      throw new StringIndexOutOfBoundsException(shift32); 
    return result;
  }
  
  public static int moveCodePointOffset(StringBuffer source, int offset16, int shift32) {
    int count, result = offset16;
    int size = source.length();
    if (offset16 < 0 || offset16 > size)
      throw new StringIndexOutOfBoundsException(offset16); 
    if (shift32 > 0) {
      if (shift32 + offset16 > size)
        throw new StringIndexOutOfBoundsException(offset16); 
      count = shift32;
      while (result < size && count > 0) {
        char ch = source.charAt(result);
        if (isLeadSurrogate(ch) && result + 1 < size && isTrailSurrogate(source.charAt(result + 1)))
          result++; 
        count--;
        result++;
      } 
    } else {
      if (offset16 + shift32 < 0)
        throw new StringIndexOutOfBoundsException(offset16); 
      count = -shift32;
      result--;
      for (; count > 0 && result >= 0; count--) {
        char ch = source.charAt(result);
        if (isTrailSurrogate(ch) && result > 0 && isLeadSurrogate(source.charAt(result - 1)))
          result--; 
      } 
    } 
    if (count != 0)
      throw new StringIndexOutOfBoundsException(shift32); 
    return result;
  }
  
  public static int moveCodePointOffset(char[] source, int start, int limit, int offset16, int shift32) {
    int count, size = source.length;
    int result = offset16 + start;
    if (start < 0 || limit < start)
      throw new StringIndexOutOfBoundsException(start); 
    if (limit > size)
      throw new StringIndexOutOfBoundsException(limit); 
    if (offset16 < 0 || result > limit)
      throw new StringIndexOutOfBoundsException(offset16); 
    if (shift32 > 0) {
      if (shift32 + result > size)
        throw new StringIndexOutOfBoundsException(result); 
      count = shift32;
      while (result < limit && count > 0) {
        char ch = source[result];
        if (isLeadSurrogate(ch) && result + 1 < limit && isTrailSurrogate(source[result + 1]))
          result++; 
        count--;
        result++;
      } 
    } else {
      if (result + shift32 < start)
        throw new StringIndexOutOfBoundsException(result); 
      count = -shift32;
      result--;
      for (; count > 0 && result >= start; count--) {
        char ch = source[result];
        if (isTrailSurrogate(ch) && result > start && isLeadSurrogate(source[result - 1]))
          result--; 
      } 
    } 
    if (count != 0)
      throw new StringIndexOutOfBoundsException(shift32); 
    result -= start;
    return result;
  }
  
  public static StringBuffer insert(StringBuffer target, int offset16, int char32) {
    String str = valueOf(char32);
    if (offset16 != target.length() && bounds(target, offset16) == 5)
      offset16++; 
    target.insert(offset16, str);
    return target;
  }
  
  public static int insert(char[] target, int limit, int offset16, int char32) {
    String str = valueOf(char32);
    if (offset16 != limit && bounds(target, 0, limit, offset16) == 5)
      offset16++; 
    int size = str.length();
    if (limit + size > target.length)
      throw new ArrayIndexOutOfBoundsException(offset16 + size); 
    System.arraycopy(target, offset16, target, offset16 + size, limit - offset16);
    target[offset16] = str.charAt(0);
    if (size == 2)
      target[offset16 + 1] = str.charAt(1); 
    return limit + size;
  }
  
  public static StringBuffer delete(StringBuffer target, int offset16) {
    int count = 1;
    switch (bounds(target, offset16)) {
      case 2:
        count++;
        break;
      case 5:
        count++;
        offset16--;
        break;
    } 
    target.delete(offset16, offset16 + count);
    return target;
  }
  
  public static int delete(char[] target, int limit, int offset16) {
    int count = 1;
    switch (bounds(target, 0, limit, offset16)) {
      case 2:
        count++;
        break;
      case 5:
        count++;
        offset16--;
        break;
    } 
    System.arraycopy(target, offset16 + count, target, offset16, limit - offset16 + count);
    target[limit - count] = Character.MIN_VALUE;
    return limit - count;
  }
  
  public static int indexOf(String source, int char32) {
    if (char32 < 0 || char32 > 1114111)
      throw new IllegalArgumentException("Argument char32 is not a valid codepoint"); 
    if (char32 < 55296 || (char32 > 57343 && char32 < 65536))
      return source.indexOf((char)char32); 
    if (char32 < 65536) {
      int result = source.indexOf((char)char32);
      if (result >= 0) {
        if (isLeadSurrogate((char)char32) && result < source.length() - 1 && isTrailSurrogate(source.charAt(result + 1)))
          return indexOf(source, char32, result + 1); 
        if (result > 0 && isLeadSurrogate(source.charAt(result - 1)))
          return indexOf(source, char32, result + 1); 
      } 
      return result;
    } 
    String char32str = toString(char32);
    return source.indexOf(char32str);
  }
  
  public static int indexOf(String source, String str) {
    int strLength = str.length();
    if (!isTrailSurrogate(str.charAt(0)) && !isLeadSurrogate(str.charAt(strLength - 1)))
      return source.indexOf(str); 
    int result = source.indexOf(str);
    int resultEnd = result + strLength;
    if (result >= 0) {
      if (isLeadSurrogate(str.charAt(strLength - 1)) && result < source.length() - 1 && isTrailSurrogate(source.charAt(resultEnd + 1)))
        return indexOf(source, str, resultEnd + 1); 
      if (isTrailSurrogate(str.charAt(0)) && result > 0 && isLeadSurrogate(source.charAt(result - 1)))
        return indexOf(source, str, resultEnd + 1); 
    } 
    return result;
  }
  
  public static int indexOf(String source, int char32, int fromIndex) {
    if (char32 < 0 || char32 > 1114111)
      throw new IllegalArgumentException("Argument char32 is not a valid codepoint"); 
    if (char32 < 55296 || (char32 > 57343 && char32 < 65536))
      return source.indexOf((char)char32, fromIndex); 
    if (char32 < 65536) {
      int result = source.indexOf((char)char32, fromIndex);
      if (result >= 0) {
        if (isLeadSurrogate((char)char32) && result < source.length() - 1 && isTrailSurrogate(source.charAt(result + 1)))
          return indexOf(source, char32, result + 1); 
        if (result > 0 && isLeadSurrogate(source.charAt(result - 1)))
          return indexOf(source, char32, result + 1); 
      } 
      return result;
    } 
    String char32str = toString(char32);
    return source.indexOf(char32str, fromIndex);
  }
  
  public static int indexOf(String source, String str, int fromIndex) {
    int strLength = str.length();
    if (!isTrailSurrogate(str.charAt(0)) && !isLeadSurrogate(str.charAt(strLength - 1)))
      return source.indexOf(str, fromIndex); 
    int result = source.indexOf(str, fromIndex);
    int resultEnd = result + strLength;
    if (result >= 0) {
      if (isLeadSurrogate(str.charAt(strLength - 1)) && result < source.length() - 1 && isTrailSurrogate(source.charAt(resultEnd)))
        return indexOf(source, str, resultEnd + 1); 
      if (isTrailSurrogate(str.charAt(0)) && result > 0 && isLeadSurrogate(source.charAt(result - 1)))
        return indexOf(source, str, resultEnd + 1); 
    } 
    return result;
  }
  
  public static int lastIndexOf(String source, int char32) {
    if (char32 < 0 || char32 > 1114111)
      throw new IllegalArgumentException("Argument char32 is not a valid codepoint"); 
    if (char32 < 55296 || (char32 > 57343 && char32 < 65536))
      return source.lastIndexOf((char)char32); 
    if (char32 < 65536) {
      int result = source.lastIndexOf((char)char32);
      if (result >= 0) {
        if (isLeadSurrogate((char)char32) && result < source.length() - 1 && isTrailSurrogate(source.charAt(result + 1)))
          return lastIndexOf(source, char32, result - 1); 
        if (result > 0 && isLeadSurrogate(source.charAt(result - 1)))
          return lastIndexOf(source, char32, result - 1); 
      } 
      return result;
    } 
    String char32str = toString(char32);
    return source.lastIndexOf(char32str);
  }
  
  public static int lastIndexOf(String source, String str) {
    int strLength = str.length();
    if (!isTrailSurrogate(str.charAt(0)) && !isLeadSurrogate(str.charAt(strLength - 1)))
      return source.lastIndexOf(str); 
    int result = source.lastIndexOf(str);
    if (result >= 0) {
      if (isLeadSurrogate(str.charAt(strLength - 1)) && result < source.length() - 1 && isTrailSurrogate(source.charAt(result + strLength + 1)))
        return lastIndexOf(source, str, result - 1); 
      if (isTrailSurrogate(str.charAt(0)) && result > 0 && isLeadSurrogate(source.charAt(result - 1)))
        return lastIndexOf(source, str, result - 1); 
    } 
    return result;
  }
  
  public static int lastIndexOf(String source, int char32, int fromIndex) {
    if (char32 < 0 || char32 > 1114111)
      throw new IllegalArgumentException("Argument char32 is not a valid codepoint"); 
    if (char32 < 55296 || (char32 > 57343 && char32 < 65536))
      return source.lastIndexOf((char)char32, fromIndex); 
    if (char32 < 65536) {
      int result = source.lastIndexOf((char)char32, fromIndex);
      if (result >= 0) {
        if (isLeadSurrogate((char)char32) && result < source.length() - 1 && isTrailSurrogate(source.charAt(result + 1)))
          return lastIndexOf(source, char32, result - 1); 
        if (result > 0 && isLeadSurrogate(source.charAt(result - 1)))
          return lastIndexOf(source, char32, result - 1); 
      } 
      return result;
    } 
    String char32str = toString(char32);
    return source.lastIndexOf(char32str, fromIndex);
  }
  
  public static int lastIndexOf(String source, String str, int fromIndex) {
    int strLength = str.length();
    if (!isTrailSurrogate(str.charAt(0)) && !isLeadSurrogate(str.charAt(strLength - 1)))
      return source.lastIndexOf(str, fromIndex); 
    int result = source.lastIndexOf(str, fromIndex);
    if (result >= 0) {
      if (isLeadSurrogate(str.charAt(strLength - 1)) && result < source.length() - 1 && isTrailSurrogate(source.charAt(result + strLength)))
        return lastIndexOf(source, str, result - 1); 
      if (isTrailSurrogate(str.charAt(0)) && result > 0 && isLeadSurrogate(source.charAt(result - 1)))
        return lastIndexOf(source, str, result - 1); 
    } 
    return result;
  }
  
  public static String replace(String source, int oldChar32, int newChar32) {
    if (oldChar32 <= 0 || oldChar32 > 1114111)
      throw new IllegalArgumentException("Argument oldChar32 is not a valid codepoint"); 
    if (newChar32 <= 0 || newChar32 > 1114111)
      throw new IllegalArgumentException("Argument newChar32 is not a valid codepoint"); 
    int index = indexOf(source, oldChar32);
    if (index == -1)
      return source; 
    String newChar32Str = toString(newChar32);
    int oldChar32Size = 1;
    int newChar32Size = newChar32Str.length();
    StringBuffer result = new StringBuffer(source);
    int resultIndex = index;
    if (oldChar32 >= 65536)
      oldChar32Size = 2; 
    while (index != -1) {
      int endResultIndex = resultIndex + oldChar32Size;
      result.replace(resultIndex, endResultIndex, newChar32Str);
      int lastEndIndex = index + oldChar32Size;
      index = indexOf(source, oldChar32, lastEndIndex);
      resultIndex += newChar32Size + index - lastEndIndex;
    } 
    return result.toString();
  }
  
  public static String replace(String source, String oldStr, String newStr) {
    int index = indexOf(source, oldStr);
    if (index == -1)
      return source; 
    int oldStrSize = oldStr.length();
    int newStrSize = newStr.length();
    StringBuffer result = new StringBuffer(source);
    int resultIndex = index;
    while (index != -1) {
      int endResultIndex = resultIndex + oldStrSize;
      result.replace(resultIndex, endResultIndex, newStr);
      int lastEndIndex = index + oldStrSize;
      index = indexOf(source, oldStr, lastEndIndex);
      resultIndex += newStrSize + index - lastEndIndex;
    } 
    return result.toString();
  }
  
  public static StringBuffer reverse(StringBuffer source) {
    int length = source.length();
    StringBuffer result = new StringBuffer(length);
    for (int i = length; i-- > 0; ) {
      char ch = source.charAt(i);
      if (isTrailSurrogate(ch) && i > 0) {
        char ch2 = source.charAt(i - 1);
        if (isLeadSurrogate(ch2)) {
          result.append(ch2);
          result.append(ch);
          i--;
          continue;
        } 
      } 
      result.append(ch);
    } 
    return result;
  }
  
  public static boolean hasMoreCodePointsThan(String source, int number) {
    if (number < 0)
      return true; 
    if (source == null)
      return false; 
    int length = source.length();
    if (length + 1 >> 1 > number)
      return true; 
    int maxsupplementary = length - number;
    if (maxsupplementary <= 0)
      return false; 
    int start = 0;
    while (true) {
      if (length == 0)
        return false; 
      if (number == 0)
        return true; 
      if (isLeadSurrogate(source.charAt(start++)) && start != length && isTrailSurrogate(source.charAt(start))) {
        start++;
        if (--maxsupplementary <= 0)
          return false; 
      } 
      number--;
    } 
  }
  
  public static boolean hasMoreCodePointsThan(char[] source, int start, int limit, int number) {
    int length = limit - start;
    if (length < 0 || start < 0 || limit < 0)
      throw new IndexOutOfBoundsException("Start and limit indexes should be non-negative and start <= limit"); 
    if (number < 0)
      return true; 
    if (source == null)
      return false; 
    if (length + 1 >> 1 > number)
      return true; 
    int maxsupplementary = length - number;
    if (maxsupplementary <= 0)
      return false; 
    while (true) {
      if (length == 0)
        return false; 
      if (number == 0)
        return true; 
      if (isLeadSurrogate(source[start++]) && start != limit && isTrailSurrogate(source[start])) {
        start++;
        if (--maxsupplementary <= 0)
          return false; 
      } 
      number--;
    } 
  }
  
  public static boolean hasMoreCodePointsThan(StringBuffer source, int number) {
    if (number < 0)
      return true; 
    if (source == null)
      return false; 
    int length = source.length();
    if (length + 1 >> 1 > number)
      return true; 
    int maxsupplementary = length - number;
    if (maxsupplementary <= 0)
      return false; 
    int start = 0;
    while (true) {
      if (length == 0)
        return false; 
      if (number == 0)
        return true; 
      if (isLeadSurrogate(source.charAt(start++)) && start != length && isTrailSurrogate(source.charAt(start))) {
        start++;
        if (--maxsupplementary <= 0)
          return false; 
      } 
      number--;
    } 
  }
  
  public static String newString(int[] codePoints, int offset, int count) {
    if (count < 0)
      throw new IllegalArgumentException(); 
    char[] chars = new char[count];
    int w = 0;
    for (int r = offset, e = offset + count; r < e; r++) {
      int cp = codePoints[r];
      if (cp < 0 || cp > 1114111)
        throw new IllegalArgumentException(); 
      while (true) {
        try {
          if (cp < 65536) {
            chars[w] = (char)cp;
            w++;
            break;
          } 
          chars[w] = (char)(55232 + (cp >> 10));
          chars[w + 1] = (char)(56320 + (cp & 0x3FF));
          w += 2;
          break;
        } catch (IndexOutOfBoundsException ex) {
          int newlen = (int)Math.ceil(codePoints.length * (w + 2) / (r - offset + 1));
          char[] temp = new char[newlen];
          System.arraycopy(chars, 0, temp, 0, w);
          chars = temp;
        } 
      } 
    } 
    return new String(chars, 0, w);
  }
  
  public static final class StringComparator implements Comparator<String> {
    public static final int FOLD_CASE_DEFAULT = 0;
    
    public static final int FOLD_CASE_EXCLUDE_SPECIAL_I = 1;
    
    private int m_codePointCompare_;
    
    private int m_foldCase_;
    
    private boolean m_ignoreCase_;
    
    private static final int CODE_POINT_COMPARE_SURROGATE_OFFSET_ = 10240;
    
    public StringComparator() {
      this(false, false, 0);
    }
    
    public StringComparator(boolean codepointcompare, boolean ignorecase, int foldcaseoption) {
      setCodePointCompare(codepointcompare);
      this.m_ignoreCase_ = ignorecase;
      if (foldcaseoption < 0 || foldcaseoption > 1)
        throw new IllegalArgumentException("Invalid fold case option"); 
      this.m_foldCase_ = foldcaseoption;
    }
    
    public void setCodePointCompare(boolean flag) {
      if (flag) {
        this.m_codePointCompare_ = 32768;
      } else {
        this.m_codePointCompare_ = 0;
      } 
    }
    
    public void setIgnoreCase(boolean ignorecase, int foldcaseoption) {
      this.m_ignoreCase_ = ignorecase;
      if (foldcaseoption < 0 || foldcaseoption > 1)
        throw new IllegalArgumentException("Invalid fold case option"); 
      this.m_foldCase_ = foldcaseoption;
    }
    
    public boolean getCodePointCompare() {
      return (this.m_codePointCompare_ == 32768);
    }
    
    public boolean getIgnoreCase() {
      return this.m_ignoreCase_;
    }
    
    public int getIgnoreCaseOption() {
      return this.m_foldCase_;
    }
    
    public int compare(String a, String b) {
      if (a == b)
        return 0; 
      if (a == null)
        return -1; 
      if (b == null)
        return 1; 
      if (this.m_ignoreCase_)
        return compareCaseInsensitive(a, b); 
      return compareCaseSensitive(a, b);
    }
    
    private int compareCaseInsensitive(String s1, String s2) {
      return Normalizer.cmpEquivFold(s1, s2, this.m_foldCase_ | this.m_codePointCompare_ | 0x10000);
    }
    
    private int compareCaseSensitive(String s1, String s2) {
      int length1 = s1.length();
      int length2 = s2.length();
      int minlength = length1;
      int result = 0;
      if (length1 < length2) {
        result = -1;
      } else if (length1 > length2) {
        result = 1;
        minlength = length2;
      } 
      char c1 = Character.MIN_VALUE;
      char c2 = Character.MIN_VALUE;
      int index = 0;
      for (; index < minlength; index++) {
        c1 = s1.charAt(index);
        c2 = s2.charAt(index);
        if (c1 != c2)
          break; 
      } 
      if (index == minlength)
        return result; 
      boolean codepointcompare = (this.m_codePointCompare_ == 32768);
      if (c1 >= '?' && c2 >= '?' && codepointcompare) {
        if ((c1 > '?' || index + 1 == length1 || !UTF16.isTrailSurrogate(s1.charAt(index + 1))) && (!UTF16.isTrailSurrogate(c1) || index == 0 || !UTF16.isLeadSurrogate(s1.charAt(index - 1))))
          c1 = (char)(c1 - 10240); 
        if ((c2 > '?' || index + 1 == length2 || !UTF16.isTrailSurrogate(s2.charAt(index + 1))) && (!UTF16.isTrailSurrogate(c2) || index == 0 || !UTF16.isLeadSurrogate(s2.charAt(index - 1))))
          c2 = (char)(c2 - 10240); 
      } 
      return c1 - c2;
    }
  }
  
  private static String toString(int ch) {
    if (ch < 65536)
      return String.valueOf((char)ch); 
    StringBuilder result = new StringBuilder();
    result.append(getLeadSurrogate(ch));
    result.append(getTrailSurrogate(ch));
    return result.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\UTF16.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */