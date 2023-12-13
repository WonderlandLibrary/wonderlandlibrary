package org.apache.http.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public class CharsetUtils {
   public static Charset lookup(String var0) {
      if (var0 == null) {
         return null;
      } else {
         try {
            return Charset.forName(var0);
         } catch (UnsupportedCharsetException var2) {
            return null;
         }
      }
   }

   public static Charset get(String var0) throws UnsupportedEncodingException {
      if (var0 == null) {
         return null;
      } else {
         try {
            return Charset.forName(var0);
         } catch (UnsupportedCharsetException var2) {
            throw new UnsupportedEncodingException(var0);
         }
      }
   }
}
