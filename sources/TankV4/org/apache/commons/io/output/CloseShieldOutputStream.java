package org.apache.commons.io.output;

import java.io.OutputStream;

public class CloseShieldOutputStream extends ProxyOutputStream {
   public CloseShieldOutputStream(OutputStream var1) {
      super(var1);
   }

   public void close() {
      this.out = new ClosedOutputStream();
   }
}
