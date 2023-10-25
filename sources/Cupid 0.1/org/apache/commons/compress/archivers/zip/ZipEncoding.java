package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface ZipEncoding {
  boolean canEncode(String paramString);
  
  ByteBuffer encode(String paramString) throws IOException;
  
  String decode(byte[] paramArrayOfbyte) throws IOException;
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\compress\archivers\zip\ZipEncoding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */