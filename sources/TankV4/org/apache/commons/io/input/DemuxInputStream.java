package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class DemuxInputStream extends InputStream {
   private final InheritableThreadLocal m_streams = new InheritableThreadLocal();

   public InputStream bindStream(InputStream var1) {
      InputStream var2 = (InputStream)this.m_streams.get();
      this.m_streams.set(var1);
      return var2;
   }

   public void close() throws IOException {
      InputStream var1 = (InputStream)this.m_streams.get();
      if (null != var1) {
         var1.close();
      }

   }

   public int read() throws IOException {
      InputStream var1 = (InputStream)this.m_streams.get();
      return null != var1 ? var1.read() : -1;
   }
}
