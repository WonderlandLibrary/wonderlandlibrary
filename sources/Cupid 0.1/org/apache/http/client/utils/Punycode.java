package org.apache.http.client.utils;

import org.apache.http.annotation.Immutable;

@Immutable
public class Punycode {
  private static final Idn impl;
  
  static {
    Idn idn;
    try {
      idn = new JdkIdn();
    } catch (Exception e) {
      idn = new Rfc3492Idn();
    } 
    impl = idn;
  }
  
  public static String toUnicode(String punycode) {
    return impl.toUnicode(punycode);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\clien\\utils\Punycode.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */