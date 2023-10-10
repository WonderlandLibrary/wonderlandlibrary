package org.apache.http.impl;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import org.apache.http.config.ConnectionConfig;

public final class ConnSupport {
   public static CharsetDecoder createDecoder(ConnectionConfig var0) {
      if (var0 == null) {
         return null;
      } else {
         Charset var1 = var0.getCharset();
         CodingErrorAction var2 = var0.getMalformedInputAction();
         CodingErrorAction var3 = var0.getUnmappableInputAction();
         return var1 != null ? var1.newDecoder().onMalformedInput(var2 != null ? var2 : CodingErrorAction.REPORT).onUnmappableCharacter(var3 != null ? var3 : CodingErrorAction.REPORT) : null;
      }
   }

   public static CharsetEncoder createEncoder(ConnectionConfig var0) {
      if (var0 == null) {
         return null;
      } else {
         Charset var1 = var0.getCharset();
         if (var1 != null) {
            CodingErrorAction var2 = var0.getMalformedInputAction();
            CodingErrorAction var3 = var0.getUnmappableInputAction();
            return var1.newEncoder().onMalformedInput(var2 != null ? var2 : CodingErrorAction.REPORT).onUnmappableCharacter(var3 != null ? var3 : CodingErrorAction.REPORT);
         } else {
            return null;
         }
      }
   }
}
