package org.apache.logging.log4j.core.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import org.apache.logging.log4j.util.Strings;

public final class NameUtil {
  public static String getSubName(String name) {
    if (Strings.isEmpty(name))
      return null; 
    int i = name.lastIndexOf('.');
    return (i > 0) ? name.substring(0, i) : "";
  }
  
  public static String md5(String input) {
    Objects.requireNonNull(input, "input");
    try {
      byte[] inputBytes = input.getBytes();
      MessageDigest digest = MessageDigest.getInstance("MD5");
      byte[] bytes = digest.digest(inputBytes);
      StringBuilder md5 = new StringBuilder(bytes.length * 2);
      for (byte b : bytes) {
        String hex = Integer.toHexString(0xFF & b);
        if (hex.length() == 1)
          md5.append('0'); 
        md5.append(hex);
      } 
      return md5.toString();
    } catch (NoSuchAlgorithmException error) {
      throw new RuntimeException(error);
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4j\cor\\util\NameUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */