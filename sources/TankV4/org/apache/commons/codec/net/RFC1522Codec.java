package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.StringUtils;

abstract class RFC1522Codec {
   protected static final char SEP = '?';
   protected static final String POSTFIX = "?=";
   protected static final String PREFIX = "=?";

   protected String encodeText(String var1, Charset var2) throws EncoderException {
      if (var1 == null) {
         return null;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("=?");
         var3.append(var2);
         var3.append('?');
         var3.append(this.getEncoding());
         var3.append('?');
         byte[] var4 = this.doEncoding(var1.getBytes(var2));
         var3.append(StringUtils.newStringUsAscii(var4));
         var3.append("?=");
         return var3.toString();
      }
   }

   protected String encodeText(String var1, String var2) throws EncoderException, UnsupportedEncodingException {
      return var1 == null ? null : this.encodeText(var1, Charset.forName(var2));
   }

   protected String decodeText(String var1) throws DecoderException, UnsupportedEncodingException {
      if (var1 == null) {
         return null;
      } else if (var1.startsWith("=?") && var1.endsWith("?=")) {
         int var2 = var1.length() - 2;
         byte var3 = 2;
         int var4 = var1.indexOf(63, var3);
         if (var4 == var2) {
            throw new DecoderException("RFC 1522 violation: charset token not found");
         } else {
            String var5 = var1.substring(var3, var4);
            if (var5.equals("")) {
               throw new DecoderException("RFC 1522 violation: charset not specified");
            } else {
               int var8 = var4 + 1;
               var4 = var1.indexOf(63, var8);
               if (var4 == var2) {
                  throw new DecoderException("RFC 1522 violation: encoding token not found");
               } else {
                  String var6 = var1.substring(var8, var4);
                  if (!this.getEncoding().equalsIgnoreCase(var6)) {
                     throw new DecoderException("This codec cannot decode " + var6 + " encoded content");
                  } else {
                     var8 = var4 + 1;
                     var4 = var1.indexOf(63, var8);
                     byte[] var7 = StringUtils.getBytesUsAscii(var1.substring(var8, var4));
                     var7 = this.doDecoding(var7);
                     return new String(var7, var5);
                  }
               }
            }
         }
      } else {
         throw new DecoderException("RFC 1522 violation: malformed encoded content");
      }
   }

   protected abstract String getEncoding();

   protected abstract byte[] doEncoding(byte[] var1) throws EncoderException;

   protected abstract byte[] doDecoding(byte[] var1) throws DecoderException;
}
