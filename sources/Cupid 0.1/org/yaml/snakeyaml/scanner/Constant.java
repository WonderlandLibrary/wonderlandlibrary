package org.yaml.snakeyaml.scanner;

import java.util.Arrays;

public final class Constant {
  private static final String ALPHA_S = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
  
  private static final String LINEBR_S = "\n  ";
  
  private static final String FULL_LINEBR_S = "\r\n  ";
  
  private static final String NULL_OR_LINEBR_S = "\000\r\n  ";
  
  private static final String NULL_BL_LINEBR_S = " \000\r\n  ";
  
  private static final String NULL_BL_T_LINEBR_S = "\t \000\r\n  ";
  
  private static final String NULL_BL_T_S = "\000 \t";
  
  private static final String URI_CHARS_S = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_-;/?:@&=+$,_.!~*'()[]%";
  
  public static final Constant LINEBR = new Constant("\n  ");
  
  public static final Constant FULL_LINEBR = new Constant("\r\n  ");
  
  public static final Constant NULL_OR_LINEBR = new Constant("\000\r\n  ");
  
  public static final Constant NULL_BL_LINEBR = new Constant(" \000\r\n  ");
  
  public static final Constant NULL_BL_T_LINEBR = new Constant("\t \000\r\n  ");
  
  public static final Constant NULL_BL_T = new Constant("\000 \t");
  
  public static final Constant URI_CHARS = new Constant("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_-;/?:@&=+$,_.!~*'()[]%");
  
  public static final Constant ALPHA = new Constant("abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-_");
  
  private String content;
  
  boolean[] contains = new boolean[128];
  
  boolean noASCII = false;
  
  private Constant(String content) {
    Arrays.fill(this.contains, false);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < content.length(); i++) {
      int c = content.codePointAt(i);
      if (c < 128) {
        this.contains[c] = true;
      } else {
        sb.appendCodePoint(c);
      } 
    } 
    if (sb.length() > 0) {
      this.noASCII = true;
      this.content = sb.toString();
    } 
  }
  
  public boolean has(int c) {
    return (c < 128) ? this.contains[c] : ((this.noASCII && this.content.indexOf(c, 0) != -1));
  }
  
  public boolean hasNo(int c) {
    return !has(c);
  }
  
  public boolean has(int c, String additional) {
    return (has(c) || additional.indexOf(c, 0) != -1);
  }
  
  public boolean hasNo(int c, String additional) {
    return !has(c, additional);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\scanner\Constant.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */