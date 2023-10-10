package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class DemuxOutputStream extends OutputStream {
   private final InheritableThreadLocal m_streams = new InheritableThreadLocal();

   public OutputStream bindStream(OutputStream var1) {
      OutputStream var2 = (OutputStream)this.m_streams.get();
      this.m_streams.set(var1);
      return var2;
   }

   public void close() throws IOException {
      OutputStream var1 = (OutputStream)this.m_streams.get();
      if (null != var1) {
         var1.close();
      }

   }

   public void flush() throws IOException {
      OutputStream var1 = (OutputStream)this.m_streams.get();
      if (null != var1) {
         var1.flush();
      }

   }

   public void write(int var1) throws IOException {
      OutputStream var2 = (OutputStream)this.m_streams.get();
      if (null != var2) {
         var2.write(var1);
      }

   }
}
