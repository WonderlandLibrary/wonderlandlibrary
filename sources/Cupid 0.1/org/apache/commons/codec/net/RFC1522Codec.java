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
  
  protected String encodeText(String text, Charset charset) throws EncoderException {
    if (text == null)
      return null; 
    StringBuilder buffer = new StringBuilder();
    buffer.append("=?");
    buffer.append(charset);
    buffer.append('?');
    buffer.append(getEncoding());
    buffer.append('?');
    byte[] rawData = doEncoding(text.getBytes(charset));
    buffer.append(StringUtils.newStringUsAscii(rawData));
    buffer.append("?=");
    return buffer.toString();
  }
  
  protected String encodeText(String text, String charsetName) throws EncoderException, UnsupportedEncodingException {
    if (text == null)
      return null; 
    return encodeText(text, Charset.forName(charsetName));
  }
  
  protected String decodeText(String text) throws DecoderException, UnsupportedEncodingException {
    if (text == null)
      return null; 
    if (!text.startsWith("=?") || !text.endsWith("?="))
      throw new DecoderException("RFC 1522 violation: malformed encoded content"); 
    int terminator = text.length() - 2;
    int from = 2;
    int to = text.indexOf('?', from);
    if (to == terminator)
      throw new DecoderException("RFC 1522 violation: charset token not found"); 
    String charset = text.substring(from, to);
    if (charset.equals(""))
      throw new DecoderException("RFC 1522 violation: charset not specified"); 
    from = to + 1;
    to = text.indexOf('?', from);
    if (to == terminator)
      throw new DecoderException("RFC 1522 violation: encoding token not found"); 
    String encoding = text.substring(from, to);
    if (!getEncoding().equalsIgnoreCase(encoding))
      throw new DecoderException("This codec cannot decode " + encoding + " encoded content"); 
    from = to + 1;
    to = text.indexOf('?', from);
    byte[] data = StringUtils.getBytesUsAscii(text.substring(from, to));
    data = doDecoding(data);
    return new String(data, charset);
  }
  
  protected abstract String getEncoding();
  
  protected abstract byte[] doEncoding(byte[] paramArrayOfbyte) throws EncoderException;
  
  protected abstract byte[] doDecoding(byte[] paramArrayOfbyte) throws DecoderException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\codec\net\RFC1522Codec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */