package org.apache.http.impl.io;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@NotThreadSafe
public class SocketOutputBuffer extends AbstractSessionOutputBuffer {
   public SocketOutputBuffer(Socket var1, int var2, HttpParams var3) throws IOException {
      Args.notNull(var1, "Socket");
      int var4 = var2;
      if (var2 < 0) {
         var4 = var1.getSendBufferSize();
      }

      if (var4 < 1024) {
         var4 = 1024;
      }

      this.init(var1.getOutputStream(), var4, var3);
   }
}
