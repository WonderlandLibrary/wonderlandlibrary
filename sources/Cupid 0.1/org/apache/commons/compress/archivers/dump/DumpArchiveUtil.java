package org.apache.commons.compress.archivers.dump;

import java.io.IOException;
import org.apache.commons.compress.archivers.zip.ZipEncoding;

class DumpArchiveUtil {
  public static int calculateChecksum(byte[] buffer) {
    int calc = 0;
    for (int i = 0; i < 256; i++)
      calc += convert32(buffer, 4 * i); 
    return 84446 - calc - convert32(buffer, 28);
  }
  
  public static final boolean verify(byte[] buffer) {
    int magic = convert32(buffer, 24);
    if (magic != 60012)
      return false; 
    int checksum = convert32(buffer, 28);
    if (checksum != calculateChecksum(buffer))
      return false; 
    return true;
  }
  
  public static final int getIno(byte[] buffer) {
    return convert32(buffer, 20);
  }
  
  public static final long convert64(byte[] buffer, int offset) {
    long i = 0L;
    i += buffer[offset + 7] << 56L;
    i += buffer[offset + 6] << 48L & 0xFF000000000000L;
    i += buffer[offset + 5] << 40L & 0xFF0000000000L;
    i += buffer[offset + 4] << 32L & 0xFF00000000L;
    i += buffer[offset + 3] << 24L & 0xFF000000L;
    i += buffer[offset + 2] << 16L & 0xFF0000L;
    i += buffer[offset + 1] << 8L & 0xFF00L;
    i += buffer[offset] & 0xFFL;
    return i;
  }
  
  public static final int convert32(byte[] buffer, int offset) {
    int i = 0;
    i = buffer[offset + 3] << 24;
    i += buffer[offset + 2] << 16 & 0xFF0000;
    i += buffer[offset + 1] << 8 & 0xFF00;
    i += buffer[offset] & 0xFF;
    return i;
  }
  
  public static final int convert16(byte[] buffer, int offset) {
    int i = 0;
    i += buffer[offset + 1] << 8 & 0xFF00;
    i += buffer[offset] & 0xFF;
    return i;
  }
  
  static String decode(ZipEncoding encoding, byte[] b, int offset, int len) throws IOException {
    byte[] copy = new byte[len];
    System.arraycopy(b, offset, copy, 0, len);
    return encoding.decode(copy);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\dump\DumpArchiveUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */