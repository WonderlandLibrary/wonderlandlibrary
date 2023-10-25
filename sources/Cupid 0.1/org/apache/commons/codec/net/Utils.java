package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;

class Utils {
  static int digit16(byte b) throws DecoderException {
    int i = Character.digit((char)b, 16);
    if (i == -1)
      throw new DecoderException("Invalid URL encoding: not a valid digit (radix 16): " + b); 
    return i;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\codec\net\Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */