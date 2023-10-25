package com.ibm.icu.util;

import java.util.concurrent.ConcurrentHashMap;

public final class VersionInfo implements Comparable<VersionInfo> {
  public static final VersionInfo UNICODE_1_0;
  
  public static final VersionInfo UNICODE_1_0_1;
  
  public static final VersionInfo UNICODE_1_1_0;
  
  public static final VersionInfo UNICODE_1_1_5;
  
  public static final VersionInfo UNICODE_2_0;
  
  public static final VersionInfo UNICODE_2_1_2;
  
  public static final VersionInfo UNICODE_2_1_5;
  
  public static final VersionInfo UNICODE_2_1_8;
  
  public static final VersionInfo UNICODE_2_1_9;
  
  public static final VersionInfo UNICODE_3_0;
  
  public static final VersionInfo UNICODE_3_0_1;
  
  public static final VersionInfo UNICODE_3_1_0;
  
  public static final VersionInfo UNICODE_3_1_1;
  
  public static final VersionInfo UNICODE_3_2;
  
  public static final VersionInfo UNICODE_4_0;
  
  public static final VersionInfo UNICODE_4_0_1;
  
  public static final VersionInfo UNICODE_4_1;
  
  public static final VersionInfo UNICODE_5_0;
  
  public static final VersionInfo UNICODE_5_1;
  
  public static final VersionInfo UNICODE_5_2;
  
  public static final VersionInfo UNICODE_6_0;
  
  public static final VersionInfo UNICODE_6_1;
  
  public static final VersionInfo UNICODE_6_2;
  
  public static final VersionInfo ICU_VERSION;
  
  public static final String ICU_DATA_VERSION_PATH = "51b";
  
  public static final VersionInfo ICU_DATA_VERSION;
  
  public static final VersionInfo UCOL_RUNTIME_VERSION;
  
  public static final VersionInfo UCOL_BUILDER_VERSION;
  
  public static final VersionInfo UCOL_TAILORINGS_VERSION;
  
  private static volatile VersionInfo javaVersion;
  
  private static final VersionInfo UNICODE_VERSION;
  
  private int m_version_;
  
  public static VersionInfo getInstance(String version) {
    int length = version.length();
    int[] array = { 0, 0, 0, 0 };
    int count = 0;
    int index = 0;
    while (count < 4 && index < length) {
      char c = version.charAt(index);
      if (c == '.') {
        count++;
      } else {
        c = (char)(c - 48);
        if (c < '\000' || c > '\t')
          throw new IllegalArgumentException("Invalid version number: Version number may be negative or greater than 255"); 
        array[count] = array[count] * 10;
        array[count] = array[count] + c;
      } 
      index++;
    } 
    if (index != length)
      throw new IllegalArgumentException("Invalid version number: String '" + version + "' exceeds version format"); 
    for (int i = 0; i < 4; i++) {
      if (array[i] < 0 || array[i] > 255)
        throw new IllegalArgumentException("Invalid version number: Version number may be negative or greater than 255"); 
    } 
    return getInstance(array[0], array[1], array[2], array[3]);
  }
  
  public static VersionInfo getInstance(int major, int minor, int milli, int micro) {
    if (major < 0 || major > 255 || minor < 0 || minor > 255 || milli < 0 || milli > 255 || micro < 0 || micro > 255)
      throw new IllegalArgumentException("Invalid version number: Version number may be negative or greater than 255"); 
    int version = getInt(major, minor, milli, micro);
    Integer key = Integer.valueOf(version);
    VersionInfo result = MAP_.get(key);
    if (result == null) {
      result = new VersionInfo(version);
      VersionInfo tmpvi = MAP_.putIfAbsent(key, result);
      if (tmpvi != null)
        result = tmpvi; 
    } 
    return result;
  }
  
  public static VersionInfo getInstance(int major, int minor, int milli) {
    return getInstance(major, minor, milli, 0);
  }
  
  public static VersionInfo getInstance(int major, int minor) {
    return getInstance(major, minor, 0, 0);
  }
  
  public static VersionInfo getInstance(int major) {
    return getInstance(major, 0, 0, 0);
  }
  
  public static VersionInfo javaVersion() {
    if (javaVersion == null)
      synchronized (VersionInfo.class) {
        if (javaVersion == null) {
          String s = System.getProperty("java.version");
          char[] chars = s.toCharArray();
          int r = 0, w = 0, count = 0;
          boolean numeric = false;
          while (r < chars.length) {
            char c = chars[r++];
            if (c < '0' || c > '9') {
              if (numeric) {
                if (count == 3)
                  break; 
                numeric = false;
                chars[w++] = '.';
                count++;
              } 
              continue;
            } 
            numeric = true;
            chars[w++] = c;
          } 
          while (w > 0 && chars[w - 1] == '.')
            w--; 
          String vs = new String(chars, 0, w);
          javaVersion = getInstance(vs);
        } 
      }  
    return javaVersion;
  }
  
  public String toString() {
    StringBuilder result = new StringBuilder(7);
    result.append(getMajor());
    result.append('.');
    result.append(getMinor());
    result.append('.');
    result.append(getMilli());
    result.append('.');
    result.append(getMicro());
    return result.toString();
  }
  
  public int getMajor() {
    return this.m_version_ >> 24 & 0xFF;
  }
  
  public int getMinor() {
    return this.m_version_ >> 16 & 0xFF;
  }
  
  public int getMilli() {
    return this.m_version_ >> 8 & 0xFF;
  }
  
  public int getMicro() {
    return this.m_version_ & 0xFF;
  }
  
  public boolean equals(Object other) {
    return (other == this);
  }
  
  public int compareTo(VersionInfo other) {
    return this.m_version_ - other.m_version_;
  }
  
  private static final ConcurrentHashMap<Integer, VersionInfo> MAP_ = new ConcurrentHashMap<Integer, VersionInfo>();
  
  private static final int LAST_BYTE_MASK_ = 255;
  
  private static final String INVALID_VERSION_NUMBER_ = "Invalid version number: Version number may be negative or greater than 255";
  
  static {
    UNICODE_1_0 = getInstance(1, 0, 0, 0);
    UNICODE_1_0_1 = getInstance(1, 0, 1, 0);
    UNICODE_1_1_0 = getInstance(1, 1, 0, 0);
    UNICODE_1_1_5 = getInstance(1, 1, 5, 0);
    UNICODE_2_0 = getInstance(2, 0, 0, 0);
    UNICODE_2_1_2 = getInstance(2, 1, 2, 0);
    UNICODE_2_1_5 = getInstance(2, 1, 5, 0);
    UNICODE_2_1_8 = getInstance(2, 1, 8, 0);
    UNICODE_2_1_9 = getInstance(2, 1, 9, 0);
    UNICODE_3_0 = getInstance(3, 0, 0, 0);
    UNICODE_3_0_1 = getInstance(3, 0, 1, 0);
    UNICODE_3_1_0 = getInstance(3, 1, 0, 0);
    UNICODE_3_1_1 = getInstance(3, 1, 1, 0);
    UNICODE_3_2 = getInstance(3, 2, 0, 0);
    UNICODE_4_0 = getInstance(4, 0, 0, 0);
    UNICODE_4_0_1 = getInstance(4, 0, 1, 0);
    UNICODE_4_1 = getInstance(4, 1, 0, 0);
    UNICODE_5_0 = getInstance(5, 0, 0, 0);
    UNICODE_5_1 = getInstance(5, 1, 0, 0);
    UNICODE_5_2 = getInstance(5, 2, 0, 0);
    UNICODE_6_0 = getInstance(6, 0, 0, 0);
    UNICODE_6_1 = getInstance(6, 1, 0, 0);
    UNICODE_6_2 = getInstance(6, 2, 0, 0);
    ICU_VERSION = getInstance(51, 2, 0, 0);
    ICU_DATA_VERSION = getInstance(51, 2, 0, 0);
    UNICODE_VERSION = UNICODE_6_2;
    UCOL_RUNTIME_VERSION = getInstance(7);
    UCOL_BUILDER_VERSION = getInstance(8);
    UCOL_TAILORINGS_VERSION = getInstance(1);
  }
  
  private VersionInfo(int compactversion) {
    this.m_version_ = compactversion;
  }
  
  private static int getInt(int major, int minor, int milli, int micro) {
    return major << 24 | minor << 16 | milli << 8 | micro;
  }
  
  public static void main(String[] args) {
    String icuApiVer;
    if (ICU_VERSION.getMajor() <= 4) {
      if (ICU_VERSION.getMinor() % 2 != 0) {
        int major = ICU_VERSION.getMajor();
        int minor = ICU_VERSION.getMinor() + 1;
        if (minor >= 10) {
          minor -= 10;
          major++;
        } 
        icuApiVer = "" + major + "." + minor + "M" + ICU_VERSION.getMilli();
      } else {
        icuApiVer = ICU_VERSION.getVersionString(2, 2);
      } 
    } else if (ICU_VERSION.getMinor() == 0) {
      icuApiVer = "" + ICU_VERSION.getMajor() + "M" + ICU_VERSION.getMilli();
    } else {
      icuApiVer = ICU_VERSION.getVersionString(2, 2);
    } 
    System.out.println("International Components for Unicode for Java " + icuApiVer);
    System.out.println("");
    System.out.println("Implementation Version: " + ICU_VERSION.getVersionString(2, 4));
    System.out.println("Unicode Data Version:   " + UNICODE_VERSION.getVersionString(2, 4));
    System.out.println("CLDR Data Version:      " + LocaleData.getCLDRVersion().getVersionString(2, 4));
    System.out.println("Time Zone Data Version: " + TimeZone.getTZDataVersion());
  }
  
  private String getVersionString(int minDigits, int maxDigits) {
    if (minDigits < 1 || maxDigits < 1 || minDigits > 4 || maxDigits > 4 || minDigits > maxDigits)
      throw new IllegalArgumentException("Invalid min/maxDigits range"); 
    int[] digits = new int[4];
    digits[0] = getMajor();
    digits[1] = getMinor();
    digits[2] = getMilli();
    digits[3] = getMicro();
    int numDigits = maxDigits;
    while (numDigits > minDigits && 
      digits[numDigits - 1] == 0)
      numDigits--; 
    StringBuilder verStr = new StringBuilder(7);
    verStr.append(digits[0]);
    for (int i = 1; i < numDigits; i++) {
      verStr.append(".");
      verStr.append(digits[i]);
    } 
    return verStr.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\ic\\util\VersionInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */