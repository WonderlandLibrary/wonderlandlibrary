package com.ibm.icu.impl;

import com.ibm.icu.util.VersionInfo;

public final class ICUDebug {
  private static String params;
  
  static {
    try {
      params = System.getProperty("ICUDebug");
    } catch (SecurityException e) {}
  }
  
  private static boolean debug = (params != null);
  
  private static boolean help = (debug && (params.equals("") || params.indexOf("help") != -1));
  
  static {
    if (debug)
      System.out.println("\nICUDebug=" + params); 
  }
  
  public static final String javaVersionString = System.getProperty("java.version", "0");
  
  public static final boolean isJDK14OrHigher;
  
  public static VersionInfo getInstanceLenient(String s) {
    int[] ver = new int[4];
    boolean numeric = false;
    int i = 0, vidx = 0;
    while (i < s.length()) {
      char c = s.charAt(i++);
      if (c < '0' || c > '9') {
        if (numeric) {
          if (vidx == 3)
            break; 
          numeric = false;
          vidx++;
        } 
        continue;
      } 
      if (numeric) {
        ver[vidx] = ver[vidx] * 10 + c - 48;
        if (ver[vidx] > 255) {
          ver[vidx] = 0;
          break;
        } 
        continue;
      } 
      numeric = true;
      ver[vidx] = c - 48;
    } 
    return VersionInfo.getInstance(ver[0], ver[1], ver[2], ver[3]);
  }
  
  public static final VersionInfo javaVersion = getInstanceLenient(javaVersionString);
  
  static {
    VersionInfo java14Version = VersionInfo.getInstance("1.4.0");
    isJDK14OrHigher = (javaVersion.compareTo(java14Version) >= 0);
  }
  
  public static boolean enabled() {
    return debug;
  }
  
  public static boolean enabled(String arg) {
    if (debug) {
      boolean result = (params.indexOf(arg) != -1);
      if (help)
        System.out.println("\nICUDebug.enabled(" + arg + ") = " + result); 
      return result;
    } 
    return false;
  }
  
  public static String value(String arg) {
    String result = "false";
    if (debug) {
      int index = params.indexOf(arg);
      if (index != -1) {
        index += arg.length();
        if (params.length() > index && params.charAt(index) == '=') {
          index++;
          int limit = params.indexOf(",", index);
          result = params.substring(index, (limit == -1) ? params.length() : limit);
        } else {
          result = "true";
        } 
      } 
      if (help)
        System.out.println("\nICUDebug.value(" + arg + ") = " + result); 
    } 
    return result;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\ICUDebug.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */