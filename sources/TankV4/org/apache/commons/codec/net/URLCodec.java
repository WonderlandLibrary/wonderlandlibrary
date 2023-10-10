package org.apache.commons.codec.net;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;

public class URLCodec implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder {
   static final int RADIX = 16;
   /** @deprecated */
   @Deprecated
   protected String charset;
   protected static final byte ESCAPE_CHAR = 37;
   protected static final BitSet WWW_FORM_URL = new BitSet(256);

   public URLCodec() {
      this("UTF-8");
   }

   public URLCodec(String var1) {
      this.charset = var1;
   }

   public static final byte[] encodeUrl(BitSet var0, byte[] var1) {
      if (var1 == null) {
         return null;
      } else {
         if (var0 == null) {
            var0 = WWW_FORM_URL;
         }

         ByteArrayOutputStream var2 = new ByteArrayOutputStream();
         byte[] var3 = var1;
         int var4 = var1.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            byte var6 = var3[var5];
            int var7 = var6;
            if (var6 < 0) {
               var7 = 256 + var6;
            }

            if (var0.get(var7)) {
               if (var7 == 32) {
                  var7 = 43;
               }

               var2.write(var7);
            } else {
               var2.write(37);
               char var8 = Character.toUpperCase(Character.forDigit(var7 >> 4 & 15, 16));
               char var9 = Character.toUpperCase(Character.forDigit(var7 & 15, 16));
               var2.write(var8);
               var2.write(var9);
            }
         }

         return var2.toByteArray();
      }
   }

   public static final byte[] decodeUrl(byte[] var0) throws DecoderException {
      if (var0 == null) {
         return null;
      } else {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();

         for(int var2 = 0; var2 < var0.length; ++var2) {
            byte var3 = var0[var2];
            if (var3 == 43) {
               var1.write(32);
            } else if (var3 == 37) {
               try {
                  ++var2;
                  int var4 = Utils.digit16(var0[var2]);
                  ++var2;
                  int var5 = Utils.digit16(var0[var2]);
                  var1.write((char)((var4 << 4) + var5));
               } catch (ArrayIndexOutOfBoundsException var6) {
                  throw new DecoderException("Invalid URL encoding: ", var6);
               }
            } else {
               var1.write(var3);
            }
         }

         return var1.toByteArray();
      }
   }

   public byte[] encode(byte[] var1) {
      return encodeUrl(WWW_FORM_URL, var1);
   }

   public byte[] decode(byte[] var1) throws DecoderException {
      return decodeUrl(var1);
   }

   public String encode(String var1, String var2) throws UnsupportedEncodingException {
      return var1 == null ? null : StringUtils.newStringUsAscii(this.encode(var1.getBytes(var2)));
   }

   public String encode(String var1) throws EncoderException {
      if (var1 == null) {
         return null;
      } else {
         try {
            return this.encode(var1, this.getDefaultCharset());
         } catch (UnsupportedEncodingException var3) {
            throw new EncoderException(var3.getMessage(), var3);
         }
      }
   }

   public String decode(String var1, String var2) throws DecoderException, UnsupportedEncodingException {
      return var1 == null ? null : new String(this.decode(StringUtils.getBytesUsAscii(var1)), var2);
   }

   public String decode(String var1) throws DecoderException {
      if (var1 == null) {
         return null;
      } else {
         try {
            return this.decode(var1, this.getDefaultCharset());
         } catch (UnsupportedEncodingException var3) {
            throw new DecoderException(var3.getMessage(), var3);
         }
      }
   }

   public Object encode(Object var1) throws EncoderException {
      if (var1 == null) {
         return null;
      } else if (var1 instanceof byte[]) {
         return this.encode((byte[])((byte[])var1));
      } else if (var1 instanceof String) {
         return this.encode((String)var1);
      } else {
         throw new EncoderException("Objects of type " + var1.getClass().getName() + " cannot be URL encoded");
      }
   }

   public Object decode(Object var1) throws DecoderException {
      if (var1 == null) {
         return null;
      } else if (var1 instanceof byte[]) {
         return this.decode((byte[])((byte[])var1));
      } else if (var1 instanceof String) {
         return this.decode((String)var1);
      } else {
         throw new DecoderException("Objects of type " + var1.getClass().getName() + " cannot be URL decoded");
      }
   }

   public String getDefaultCharset() {
      return this.charset;
   }

   /** @deprecated */
   @Deprecated
   public String getEncoding() {
      return this.charset;
   }

   static {
      int var0;
      for(var0 = 97; var0 <= 122; ++var0) {
         WWW_FORM_URL.set(var0);
      }

      for(var0 = 65; var0 <= 90; ++var0) {
         WWW_FORM_URL.set(var0);
      }

      for(var0 = 48; var0 <= 57; ++var0) {
         WWW_FORM_URL.set(var0);
      }

      WWW_FORM_URL.set(45);
      WWW_FORM_URL.set(95);
      WWW_FORM_URL.set(46);
      WWW_FORM_URL.set(42);
      WWW_FORM_URL.set(32);
   }
}
