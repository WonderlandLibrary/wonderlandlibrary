package io.netty.util;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.IdentityHashMap;
import java.util.Map;

public final class CharsetUtil {
   public static final Charset UTF_16 = Charset.forName("UTF-16");
   public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
   public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
   public static final Charset UTF_8 = Charset.forName("UTF-8");
   public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
   public static final Charset US_ASCII = Charset.forName("US-ASCII");
   private static final ThreadLocal encoders = new ThreadLocal() {
      protected Map initialValue() {
         return new IdentityHashMap();
      }

      protected Object initialValue() {
         return this.initialValue();
      }
   };
   private static final ThreadLocal decoders = new ThreadLocal() {
      protected Map initialValue() {
         return new IdentityHashMap();
      }

      protected Object initialValue() {
         return this.initialValue();
      }
   };

   public static CharsetEncoder getEncoder(Charset var0) {
      if (var0 == null) {
         throw new NullPointerException("charset");
      } else {
         Map var1 = (Map)encoders.get();
         CharsetEncoder var2 = (CharsetEncoder)var1.get(var0);
         if (var2 != null) {
            var2.reset();
            var2.onMalformedInput(CodingErrorAction.REPLACE);
            var2.onUnmappableCharacter(CodingErrorAction.REPLACE);
            return var2;
         } else {
            var2 = var0.newEncoder();
            var2.onMalformedInput(CodingErrorAction.REPLACE);
            var2.onUnmappableCharacter(CodingErrorAction.REPLACE);
            var1.put(var0, var2);
            return var2;
         }
      }
   }

   public static CharsetDecoder getDecoder(Charset var0) {
      if (var0 == null) {
         throw new NullPointerException("charset");
      } else {
         Map var1 = (Map)decoders.get();
         CharsetDecoder var2 = (CharsetDecoder)var1.get(var0);
         if (var2 != null) {
            var2.reset();
            var2.onMalformedInput(CodingErrorAction.REPLACE);
            var2.onUnmappableCharacter(CodingErrorAction.REPLACE);
            return var2;
         } else {
            var2 = var0.newDecoder();
            var2.onMalformedInput(CodingErrorAction.REPLACE);
            var2.onUnmappableCharacter(CodingErrorAction.REPLACE);
            var1.put(var0, var2);
            return var2;
         }
      }
   }

   private CharsetUtil() {
   }
}
