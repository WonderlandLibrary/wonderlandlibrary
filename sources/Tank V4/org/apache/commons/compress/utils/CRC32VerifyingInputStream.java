package org.apache.commons.compress.utils;

import java.io.InputStream;
import java.util.zip.CRC32;

public class CRC32VerifyingInputStream extends ChecksumVerifyingInputStream {
   public CRC32VerifyingInputStream(InputStream var1, long var2, int var4) {
      this(var1, var2, (long)var4 & 4294967295L);
   }

   public CRC32VerifyingInputStream(InputStream var1, long var2, long var4) {
      super(new CRC32(), var1, var2, var4);
   }
}
