package org.apache.http.impl.io;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.EofSensor;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@NotThreadSafe
public class SocketInputBuffer extends AbstractSessionInputBuffer implements EofSensor {
   private final Socket socket;
   private boolean eof;

   public SocketInputBuffer(Socket var1, int var2, HttpParams var3) throws IOException {
      Args.notNull(var1, "Socket");
      this.socket = var1;
      this.eof = false;
      int var4 = var2;
      if (var2 < 0) {
         var4 = var1.getReceiveBufferSize();
      }

      if (var4 < 1024) {
         var4 = 1024;
      }

      this.init(var1.getInputStream(), var4, var3);
   }

   protected int fillBuffer() throws IOException {
      int var1 = super.fillBuffer();
      this.eof = var1 == -1;
      return var1;
   }

   public boolean isDataAvailable(int var1) throws IOException {
      boolean var2 = this.hasBufferedData();
      if (!var2) {
         int var3 = this.socket.getSoTimeout();
         this.socket.setSoTimeout(var1);
         this.fillBuffer();
         var2 = this.hasBufferedData();
         this.socket.setSoTimeout(var3);
      }

      return var2;
   }

   public boolean isEof() {
      return this.eof;
   }
}
