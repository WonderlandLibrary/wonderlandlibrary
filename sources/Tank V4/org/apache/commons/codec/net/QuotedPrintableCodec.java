package org.apache.commons.codec.net;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.BitSet;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;

public class QuotedPrintableCodec implements BinaryEncoder, BinaryDecoder, StringEncoder, StringDecoder {
   private final Charset charset;
   private static final BitSet PRINTABLE_CHARS = new BitSet(256);
   private static final byte ESCAPE_CHAR = 61;
   private static final byte TAB = 9;
   private static final byte SPACE = 32;

   public QuotedPrintableCodec() {
      this(Charsets.UTF_8);
   }

   public QuotedPrintableCodec(Charset var1) {
      this.charset = var1;
   }

   public QuotedPrintableCodec(String var1) throws IllegalCharsetNameException, IllegalArgumentException, UnsupportedCharsetException {
      this(Charset.forName(var1));
   }

   private static final void encodeQuotedPrintable(int var0, ByteArrayOutputStream var1) {
      var1.write(61);
      char var2 = Character.toUpperCase(Character.forDigit(var0 >> 4 & 15, 16));
      char var3 = Character.toUpperCase(Character.forDigit(var0 & 15, 16));
      var1.write(var2);
      var1.write(var3);
   }

   public static final byte[] encodeQuotedPrintable(BitSet var0, byte[] var1) {
      if (var1 == null) {
         return null;
      } else {
         if (var0 == null) {
            var0 = PRINTABLE_CHARS;
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
               var2.write(var7);
            } else {
               encodeQuotedPrintable(var7, var2);
            }
         }

         return var2.toByteArray();
      }
   }

   public static final byte[] decodeQuotedPrintable(byte[] var0) throws DecoderException {
      if (var0 == null) {
         return null;
      } else {
         ByteArrayOutputStream var1 = new ByteArrayOutputStream();

         for(int var2 = 0; var2 < var0.length; ++var2) {
            byte var3 = var0[var2];
            if (var3 == 61) {
               try {
                  ++var2;
                  int var4 = Utils.digit16(var0[var2]);
                  ++var2;
                  int var5 = Utils.digit16(var0[var2]);
                  var1.write((char)((var4 << 4) + var5));
               } catch (ArrayIndexOutOfBoundsException var6) {
                  throw new DecoderException("Invalid quoted-printable encoding", var6);
               }
            } else {
               var1.write(var3);
            }
         }

         return var1.toByteArray();
      }
   }

   public byte[] encode(byte[] var1) {
      return encodeQuotedPrintable(PRINTABLE_CHARS, var1);
   }

   public byte[] decode(byte[] var1) throws DecoderException {
      return decodeQuotedPrintable(var1);
   }

   public String encode(String var1) throws EncoderException {
      return this.encode(var1, this.getCharset());
   }

   public String decode(String var1, Charset var2) throws DecoderException {
      return var1 == null ? null : new String(this.decode(StringUtils.getBytesUsAscii(var1)), var2);
   }

   public String decode(String var1, String var2) throws DecoderException, UnsupportedEncodingException {
      return var1 == null ? null : new String(this.decode(StringUtils.getBytesUsAscii(var1)), var2);
   }

   public String decode(String var1) throws DecoderException {
      return this.decode(var1, this.getCharset());
   }

   public Object encode(Object var1) throws EncoderException {
      if (var1 == null) {
         return null;
      } else if (var1 instanceof byte[]) {
         return this.encode((byte[])((byte[])var1));
      } else if (var1 instanceof String) {
         return this.encode((String)var1);
      } else {
         throw new EncoderException("Objects of type " + var1.getClass().getName() + " cannot be quoted-printable encoded");
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
         throw new DecoderException("Objects of type " + var1.getClass().getName() + " cannot be quoted-printable decoded");
      }
   }

   public Charset getCharset() {
      return this.charset;
   }

   public String getDefaultCharset() {
      return this.charset.name();
   }

   public String encode(String var1, Charset var2) {
      return var1 == null ? null : StringUtils.newStringUsAscii(this.encode(var1.getBytes(var2)));
   }

   public String encode(String var1, String var2) throws UnsupportedEncodingException {
      return var1 == null ? null : StringUtils.newStringUsAscii(this.encode(var1.getBytes(var2)));
   }

   static {
      int var0;
      for(var0 = 33; var0 <= 60; ++var0) {
         PRINTABLE_CHARS.set(var0);
      }

      for(var0 = 62; var0 <= 126; ++var0) {
         PRINTABLE_CHARS.set(var0);
      }

      PRINTABLE_CHARS.set(9);
      PRINTABLE_CHARS.set(32);
   }
}
