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
  
  @Deprecated
  protected String charset;
  
  protected static final byte ESCAPE_CHAR = 37;
  
  protected static final BitSet WWW_FORM_URL = new BitSet(256);
  
  static {
    int i;
    for (i = 97; i <= 122; i++)
      WWW_FORM_URL.set(i); 
    for (i = 65; i <= 90; i++)
      WWW_FORM_URL.set(i); 
    for (i = 48; i <= 57; i++)
      WWW_FORM_URL.set(i); 
    WWW_FORM_URL.set(45);
    WWW_FORM_URL.set(95);
    WWW_FORM_URL.set(46);
    WWW_FORM_URL.set(42);
    WWW_FORM_URL.set(32);
  }
  
  public URLCodec() {
    this("UTF-8");
  }
  
  public URLCodec(String charset) {
    this.charset = charset;
  }
  
  public static final byte[] encodeUrl(BitSet urlsafe, byte[] bytes) {
    if (bytes == null)
      return null; 
    if (urlsafe == null)
      urlsafe = WWW_FORM_URL; 
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    for (byte c : bytes) {
      int b = c;
      if (b < 0)
        b = 256 + b; 
      if (urlsafe.get(b)) {
        if (b == 32)
          b = 43; 
        buffer.write(b);
      } else {
        buffer.write(37);
        char hex1 = Character.toUpperCase(Character.forDigit(b >> 4 & 0xF, 16));
        char hex2 = Character.toUpperCase(Character.forDigit(b & 0xF, 16));
        buffer.write(hex1);
        buffer.write(hex2);
      } 
    } 
    return buffer.toByteArray();
  }
  
  public static final byte[] decodeUrl(byte[] bytes) throws DecoderException {
    if (bytes == null)
      return null; 
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    for (int i = 0; i < bytes.length; i++) {
      int b = bytes[i];
      if (b == 43) {
        buffer.write(32);
      } else if (b == 37) {
        try {
          int u = Utils.digit16(bytes[++i]);
          int l = Utils.digit16(bytes[++i]);
          buffer.write((char)((u << 4) + l));
        } catch (ArrayIndexOutOfBoundsException e) {
          throw new DecoderException("Invalid URL encoding: ", e);
        } 
      } else {
        buffer.write(b);
      } 
    } 
    return buffer.toByteArray();
  }
  
  public byte[] encode(byte[] bytes) {
    return encodeUrl(WWW_FORM_URL, bytes);
  }
  
  public byte[] decode(byte[] bytes) throws DecoderException {
    return decodeUrl(bytes);
  }
  
  public String encode(String str, String charset) throws UnsupportedEncodingException {
    if (str == null)
      return null; 
    return StringUtils.newStringUsAscii(encode(str.getBytes(charset)));
  }
  
  public String encode(String str) throws EncoderException {
    if (str == null)
      return null; 
    try {
      return encode(str, getDefaultCharset());
    } catch (UnsupportedEncodingException e) {
      throw new EncoderException(e.getMessage(), e);
    } 
  }
  
  public String decode(String str, String charset) throws DecoderException, UnsupportedEncodingException {
    if (str == null)
      return null; 
    return new String(decode(StringUtils.getBytesUsAscii(str)), charset);
  }
  
  public String decode(String str) throws DecoderException {
    if (str == null)
      return null; 
    try {
      return decode(str, getDefaultCharset());
    } catch (UnsupportedEncodingException e) {
      throw new DecoderException(e.getMessage(), e);
    } 
  }
  
  public Object encode(Object obj) throws EncoderException {
    if (obj == null)
      return null; 
    if (obj instanceof byte[])
      return encode((byte[])obj); 
    if (obj instanceof String)
      return encode((String)obj); 
    throw new EncoderException("Objects of type " + obj.getClass().getName() + " cannot be URL encoded");
  }
  
  public Object decode(Object obj) throws DecoderException {
    if (obj == null)
      return null; 
    if (obj instanceof byte[])
      return decode((byte[])obj); 
    if (obj instanceof String)
      return decode((String)obj); 
    throw new DecoderException("Objects of type " + obj.getClass().getName() + " cannot be URL decoded");
  }
  
  public String getDefaultCharset() {
    return this.charset;
  }
  
  @Deprecated
  public String getEncoding() {
    return this.charset;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\codec\net\URLCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */