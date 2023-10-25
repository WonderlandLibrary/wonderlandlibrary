package com.ibm.icu.impl.duration.impl;

import java.util.Locale;

public class Utils {
  public static final Locale localeFromString(String s) {
    String language = s;
    String region = "";
    String variant = "";
    int x = language.indexOf("_");
    if (x != -1) {
      region = language.substring(x + 1);
      language = language.substring(0, x);
    } 
    x = region.indexOf("_");
    if (x != -1) {
      variant = region.substring(x + 1);
      region = region.substring(0, x);
    } 
    return new Locale(language, region, variant);
  }
  
  public static String chineseNumber(long n, ChineseDigits zh) {
    if (n < 0L)
      n = -n; 
    if (n <= 10L) {
      if (n == 2L)
        return String.valueOf(zh.liang); 
      return String.valueOf(zh.digits[(int)n]);
    } 
    char[] buf = new char[40];
    char[] digits = String.valueOf(n).toCharArray();
    boolean inZero = true;
    boolean forcedZero = false;
    int x = buf.length;
    int i, u, l;
    for (i = digits.length, u = -1, l = -1; --i >= 0; ) {
      if (u == -1) {
        if (l != -1) {
          buf[--x] = zh.levels[l];
          inZero = true;
          forcedZero = false;
        } 
        u++;
      } else {
        buf[--x] = zh.units[u++];
        if (u == 3) {
          u = -1;
          l++;
        } 
      } 
      int d = digits[i] - 48;
      if (d == 0) {
        if (x < buf.length - 1 && u != 0)
          buf[x] = '*'; 
        if (inZero || forcedZero) {
          buf[--x] = '*';
          continue;
        } 
        buf[--x] = zh.digits[0];
        inZero = true;
        forcedZero = (u == 1);
        continue;
      } 
      inZero = false;
      buf[--x] = zh.digits[d];
    } 
    if (n > 1000000L) {
      boolean last = true;
      int j = buf.length - 3;
      while (buf[j] != '0') {
        j -= 8;
        last = !last;
        if (j <= x)
          break; 
      } 
      j = buf.length - 7;
      do {
        if (buf[j] == zh.digits[0] && !last)
          buf[j] = '*'; 
        j -= 8;
        last = !last;
      } while (j > x);
      if (n >= 100000000L) {
        j = buf.length - 8;
        do {
          boolean empty = true;
          for (int k = j - 1, e = Math.max(x - 1, j - 8); k > e; k--) {
            if (buf[k] != '*') {
              empty = false;
              break;
            } 
          } 
          if (!empty)
            continue; 
          if (buf[j + 1] != '*' && buf[j + 1] != zh.digits[0]) {
            buf[j] = zh.digits[0];
          } else {
            buf[j] = '*';
          } 
          j -= 8;
        } while (j > x);
      } 
    } 
    for (i = x; i < buf.length; i++) {
      if (buf[i] == zh.digits[2] && (
        i >= buf.length - 1 || buf[i + 1] != zh.units[0]) && (
        i <= x || (buf[i - 1] != zh.units[0] && buf[i - 1] != zh.digits[0] && buf[i - 1] != '*')))
        buf[i] = zh.liang; 
    } 
    if (buf[x] == zh.digits[1] && (zh.ko || buf[x + 1] == zh.units[0]))
      x++; 
    int w = x;
    for (int r = x; r < buf.length; r++) {
      if (buf[r] != '*')
        buf[w++] = buf[r]; 
    } 
    return new String(buf, x, w - x);
  }
  
  public static class ChineseDigits {
    final char[] digits;
    
    final char[] units;
    
    final char[] levels;
    
    final char liang;
    
    final boolean ko;
    
    ChineseDigits(String digits, String units, String levels, char liang, boolean ko) {
      this.digits = digits.toCharArray();
      this.units = units.toCharArray();
      this.levels = levels.toCharArray();
      this.liang = liang;
      this.ko = ko;
    }
    
    public static final ChineseDigits DEBUG = new ChineseDigits("0123456789s", "sbq", "WYZ", 'L', false);
    
    public static final ChineseDigits TRADITIONAL = new ChineseDigits("零一二三四五六七八九十", "十百千", "萬億兆", '兩', false);
    
    public static final ChineseDigits SIMPLIFIED = new ChineseDigits("零一二三四五六七八九十", "十百千", "万亿兆", '两', false);
    
    public static final ChineseDigits KOREAN = new ChineseDigits("영일이삼사오육칠팔구십", "십백천", "만억?", '이', true);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\impl\Utils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */