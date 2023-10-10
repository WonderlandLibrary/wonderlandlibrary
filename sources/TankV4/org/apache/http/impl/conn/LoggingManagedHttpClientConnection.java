package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import org.apache.commons.logging.Log;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;

@NotThreadSafe
class LoggingManagedHttpClientConnection extends DefaultManagedHttpClientConnection {
   private final Log log;
   private final Log headerlog;
   private final Wire wire;

   public LoggingManagedHttpClientConnection(String var1, Log var2, Log var3, Log var4, int var5, int var6, CharsetDecoder var7, CharsetEncoder var8, MessageConstraints var9, ContentLengthStrategy var10, ContentLengthStrategy var11, HttpMessageWriterFactory var12, HttpMessageParserFactory var13) {
      super(var1, var5, var6, var7, var8, var9, var10, var11, var12, var13);
      this.log = var2;
      this.headerlog = var3;
      this.wire = new Wire(var4, var1);
   }

   public void close() throws IOException {
      if (this.log.isDebugEnabled()) {
         this.log.debug(this.getId() + ": Close connection");
      }

      super.close();
   }

   public void shutdown() throws IOException {
      if (this.log.isDebugEnabled()) {
         this.log.debug(this.getId() + ": Shutdown connection");
      }

      super.shutdown();
   }

   protected InputStream getSocketInputStream(Socket var1) throws IOException {
      Object var2 = super.getSocketInputStream(var1);
      if (this.wire.enabled()) {
         var2 = new LoggingInputStream((InputStream)var2, this.wire);
      }

      return (InputStream)var2;
   }

   protected OutputStream getSocketOutputStream(Socket var1) throws IOException {
      Object var2 = super.getSocketOutputStream(var1);
      if (this.wire.enabled()) {
         var2 = new LoggingOutputStream((OutputStream)var2, this.wire);
      }

      return (OutputStream)var2;
   }

   protected void onResponseReceived(HttpResponse var1) {
      if (var1 != null && this.headerlog.isDebugEnabled()) {
         this.headerlog.debug(this.getId() + " << " + var1.getStatusLine().toString());
         Header[] var2 = var1.getAllHeaders();
         Header[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Header var6 = var3[var5];
            this.headerlog.debug(this.getId() + " << " + var6.toString());
         }
      }

   }

   protected void onRequestSubmitted(HttpRequest var1) {
      if (var1 != null && this.headerlog.isDebugEnabled()) {
         this.headerlog.debug(this.getId() + " >> " + var1.getRequestLine().toString());
         Header[] var2 = var1.getAllHeaders();
         Header[] var3 = var2;
         int var4 = var2.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            Header var6 = var3[var5];
            this.headerlog.debug(this.getId() + " >> " + var6.toString());
         }
      }

   }
}
