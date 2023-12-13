package org.apache.commons.io.input;

import java.io.InputStream;

public class CloseShieldInputStream extends ProxyInputStream {
   public CloseShieldInputStream(InputStream var1) {
      super(var1);
   }

   public void close() {
      this.in = new ClosedInputStream();
   }
}
