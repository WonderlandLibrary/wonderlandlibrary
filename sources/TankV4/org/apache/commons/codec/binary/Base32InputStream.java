package org.apache.commons.codec.binary;

import java.io.InputStream;

public class Base32InputStream extends BaseNCodecInputStream {
   public Base32InputStream(InputStream var1) {
      this(var1, false);
   }

   public Base32InputStream(InputStream var1, boolean var2) {
      super(var1, new Base32(false), var2);
   }

   public Base32InputStream(InputStream var1, boolean var2, int var3, byte[] var4) {
      super(var1, new Base32(var3, var4), var2);
   }
}
