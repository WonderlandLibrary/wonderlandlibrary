package org.apache.commons.codec.binary;

import java.io.InputStream;

public class Base32InputStream extends BaseNCodecInputStream {
  public Base32InputStream(InputStream in) {
    this(in, false);
  }
  
  public Base32InputStream(InputStream in, boolean doEncode) {
    super(in, new Base32(false), doEncode);
  }
  
  public Base32InputStream(InputStream in, boolean doEncode, int lineLength, byte[] lineSeparator) {
    super(in, new Base32(lineLength, lineSeparator), doEncode);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\codec\binary\Base32InputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */