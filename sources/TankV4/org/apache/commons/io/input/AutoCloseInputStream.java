package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class AutoCloseInputStream extends ProxyInputStream {
   public AutoCloseInputStream(InputStream var1) {
      super(var1);
   }

   public void close() throws IOException {
      this.in.close();
      this.in = new ClosedInputStream();
   }

   protected void afterRead(int var1) throws IOException {
      if (var1 == -1) {
         this.close();
      }

   }

   protected void finalize() throws Throwable {
      this.close();
      super.finalize();
   }
}
