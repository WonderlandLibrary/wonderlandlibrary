package org.apache.commons.compress.utils;

import java.io.InputStream;
import java.util.zip.CRC32;

public class CRC32VerifyingInputStream extends ChecksumVerifyingInputStream {
  public CRC32VerifyingInputStream(InputStream in, long size, int expectedCrc32) {
    this(in, size, expectedCrc32 & 0xFFFFFFFFL);
  }
  
  public CRC32VerifyingInputStream(InputStream in, long size, long expectedCrc32) {
    super(new CRC32(), in, size, expectedCrc32);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compres\\utils\CRC32VerifyingInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */