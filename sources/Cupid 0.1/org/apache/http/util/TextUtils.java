package org.apache.http.util;

public final class TextUtils {
  public static boolean isEmpty(CharSequence s) {
    if (s == null)
      return true; 
    return (s.length() == 0);
  }
  
  public static boolean isBlank(CharSequence s) {
    if (s == null)
      return true; 
    for (int i = 0; i < s.length(); i++) {
      if (!Character.isWhitespace(s.charAt(i)))
        return false; 
    } 
    return true;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\htt\\util\TextUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */