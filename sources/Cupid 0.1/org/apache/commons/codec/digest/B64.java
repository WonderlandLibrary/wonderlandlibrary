package org.apache.commons.codec.digest;

import java.util.Random;

class B64 {
  static final String B64T = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
  
  static void b64from24bit(byte b2, byte b1, byte b0, int outLen, StringBuilder buffer) {
    int w = b2 << 16 & 0xFFFFFF | b1 << 8 & 0xFFFF | b0 & 0xFF;
    int n = outLen;
    while (n-- > 0) {
      buffer.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(w & 0x3F));
      w >>= 6;
    } 
  }
  
  static String getRandomSalt(int num) {
    StringBuilder saltString = new StringBuilder();
    for (int i = 1; i <= num; i++)
      saltString.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt((new Random()).nextInt("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length()))); 
    return saltString.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\codec\digest\B64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */