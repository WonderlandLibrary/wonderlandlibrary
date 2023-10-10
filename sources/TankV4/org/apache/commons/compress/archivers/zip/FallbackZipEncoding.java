package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.nio.ByteBuffer;

class FallbackZipEncoding implements ZipEncoding {
   private final String charsetName;

   public FallbackZipEncoding() {
      this.charsetName = null;
   }

   public FallbackZipEncoding(String var1) {
      this.charsetName = var1;
   }

   public boolean canEncode(String var1) {
      return true;
   }

   public ByteBuffer encode(String var1) throws IOException {
      return this.charsetName == null ? ByteBuffer.wrap(var1.getBytes()) : ByteBuffer.wrap(var1.getBytes(this.charsetName));
   }

   public String decode(byte[] var1) throws IOException {
      return this.charsetName == null ? new String(var1) : new String(var1, this.charsetName);
   }
}
