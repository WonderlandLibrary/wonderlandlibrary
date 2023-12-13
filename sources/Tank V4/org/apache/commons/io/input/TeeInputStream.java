package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TeeInputStream extends ProxyInputStream {
   private final OutputStream branch;
   private final boolean closeBranch;

   public TeeInputStream(InputStream var1, OutputStream var2) {
      this(var1, var2, false);
   }

   public TeeInputStream(InputStream var1, OutputStream var2, boolean var3) {
      super(var1);
      this.branch = var2;
      this.closeBranch = var3;
   }

   public void close() throws IOException {
      super.close();
      if (this.closeBranch) {
         this.branch.close();
      }

   }

   public int read() throws IOException {
      int var1 = super.read();
      if (var1 != -1) {
         this.branch.write(var1);
      }

      return var1;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = super.read(var1, var2, var3);
      if (var4 != -1) {
         this.branch.write(var1, var2, var4);
      }

      return var4;
   }

   public int read(byte[] var1) throws IOException {
      int var2 = super.read(var1);
      if (var2 != -1) {
         this.branch.write(var1, 0, var2);
      }

      return var2;
   }
}
