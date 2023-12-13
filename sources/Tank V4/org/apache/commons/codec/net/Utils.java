package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;

class Utils {
   static int digit16(byte var0) throws DecoderException {
      int var1 = Character.digit((char)var0, 16);
      if (var1 == -1) {
         throw new DecoderException("Invalid URL encoding: not a valid digit (radix 16): " + var0);
      } else {
         return var1;
      }
   }
}
