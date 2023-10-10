package org.apache.commons.codec.net;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.Base64;

public class BCodec extends RFC1522Codec implements StringEncoder, StringDecoder {
   private final Charset charset;

   public BCodec() {
      this(Charsets.UTF_8);
   }

   public BCodec(Charset var1) {
      this.charset = var1;
   }

   public BCodec(String var1) {
      this(Charset.forName(var1));
   }

   protected String getEncoding() {
      return "B";
   }

   protected byte[] doEncoding(byte[] var1) {
      return var1 == null ? null : Base64.encodeBase64(var1);
   }

   protected byte[] doDecoding(byte[] var1) {
      return var1 == null ? null : Base64.decodeBase64(var1);
   }

   public String encode(String var1, Charset var2) throws EncoderException {
      return var1 == null ? null : this.encodeText(var1, var2);
   }

   public String encode(String var1, String var2) throws EncoderException {
      if (var1 == null) {
         return null;
      } else {
         try {
            return this.encodeText(var1, var2);
         } catch (UnsupportedEncodingException var4) {
            throw new EncoderException(var4.getMessage(), var4);
         }
      }
   }

   public String encode(String var1) throws EncoderException {
      return var1 == null ? null : this.encode(var1, this.getCharset());
   }

   public String decode(String var1) throws DecoderException {
      if (var1 == null) {
         return null;
      } else {
         try {
            return this.decodeText(var1);
         } catch (UnsupportedEncodingException var3) {
            throw new DecoderException(var3.getMessage(), var3);
         }
      }
   }

   public Object encode(Object var1) throws EncoderException {
      if (var1 == null) {
         return null;
      } else if (var1 instanceof String) {
         return this.encode((String)var1);
      } else {
         throw new EncoderException("Objects of type " + var1.getClass().getName() + " cannot be encoded using BCodec");
      }
   }

   public Object decode(Object var1) throws DecoderException {
      if (var1 == null) {
         return null;
      } else if (var1 instanceof String) {
         return this.decode((String)var1);
      } else {
         throw new DecoderException("Objects of type " + var1.getClass().getName() + " cannot be decoded using BCodec");
      }
   }

   public Charset getCharset() {
      return this.charset;
   }

   public String getDefaultCharset() {
      return this.charset.name();
   }
}
