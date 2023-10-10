package org.apache.commons.codec.binary;

import java.io.OutputStream;

public class Base64OutputStream extends BaseNCodecOutputStream {
   public Base64OutputStream(OutputStream var1) {
      this(var1, true);
   }

   public Base64OutputStream(OutputStream var1, boolean var2) {
      super(var1, new Base64(false), var2);
   }

   public Base64OutputStream(OutputStream var1, boolean var2, int var3, byte[] var4) {
      super(var1, new Base64(var3, var4), var2);
   }
}
