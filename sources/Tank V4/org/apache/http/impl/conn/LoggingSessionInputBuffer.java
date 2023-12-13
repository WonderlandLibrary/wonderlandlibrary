package org.apache.http.impl.conn;

import java.io.IOException;
import org.apache.http.Consts;
import org.apache.http.annotation.Immutable;
import org.apache.http.io.EofSensor;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.CharArrayBuffer;

/** @deprecated */
@Deprecated
@Immutable
public class LoggingSessionInputBuffer implements SessionInputBuffer, EofSensor {
   private final SessionInputBuffer in;
   private final EofSensor eofSensor;
   private final Wire wire;
   private final String charset;

   public LoggingSessionInputBuffer(SessionInputBuffer var1, Wire var2, String var3) {
      this.in = var1;
      this.eofSensor = var1 instanceof EofSensor ? (EofSensor)var1 : null;
      this.wire = var2;
      this.charset = var3 != null ? var3 : Consts.ASCII.name();
   }

   public LoggingSessionInputBuffer(SessionInputBuffer var1, Wire var2) {
      this(var1, var2, (String)null);
   }

   public boolean isDataAvailable(int var1) throws IOException {
      return this.in.isDataAvailable(var1);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      int var4 = this.in.read(var1, var2, var3);
      if (this.wire.enabled() && var4 > 0) {
         this.wire.input(var1, var2, var4);
      }

      return var4;
   }

   public int read() throws IOException {
      int var1 = this.in.read();
      if (this.wire.enabled() && var1 != -1) {
         this.wire.input(var1);
      }

      return var1;
   }

   public int read(byte[] var1) throws IOException {
      int var2 = this.in.read(var1);
      if (this.wire.enabled() && var2 > 0) {
         this.wire.input(var1, 0, var2);
      }

      return var2;
   }

   public String readLine() throws IOException {
      String var1 = this.in.readLine();
      if (this.wire.enabled() && var1 != null) {
         String var2 = var1 + "\r\n";
         this.wire.input(var2.getBytes(this.charset));
      }

      return var1;
   }

   public int readLine(CharArrayBuffer var1) throws IOException {
      int var2 = this.in.readLine(var1);
      if (this.wire.enabled() && var2 >= 0) {
         int var3 = var1.length() - var2;
         String var4 = new String(var1.buffer(), var3, var2);
         String var5 = var4 + "\r\n";
         this.wire.input(var5.getBytes(this.charset));
      }

      return var2;
   }

   public HttpTransportMetrics getMetrics() {
      return this.in.getMetrics();
   }

   public boolean isEof() {
      return this.eofSensor != null ? this.eofSensor.isEof() : false;
   }
}
