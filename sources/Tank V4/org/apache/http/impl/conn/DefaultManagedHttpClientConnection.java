package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.protocol.HttpContext;

@NotThreadSafe
public class DefaultManagedHttpClientConnection extends DefaultBHttpClientConnection implements ManagedHttpClientConnection, HttpContext {
   private final String id;
   private final Map attributes;
   private volatile boolean shutdown;

   public DefaultManagedHttpClientConnection(String var1, int var2, int var3, CharsetDecoder var4, CharsetEncoder var5, MessageConstraints var6, ContentLengthStrategy var7, ContentLengthStrategy var8, HttpMessageWriterFactory var9, HttpMessageParserFactory var10) {
      super(var2, var3, var4, var5, var6, var7, var8, var9, var10);
      this.id = var1;
      this.attributes = new ConcurrentHashMap();
   }

   public DefaultManagedHttpClientConnection(String var1, int var2) {
      this(var1, var2, var2, (CharsetDecoder)null, (CharsetEncoder)null, (MessageConstraints)null, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageWriterFactory)null, (HttpMessageParserFactory)null);
   }

   public String getId() {
      return this.id;
   }

   public void shutdown() throws IOException {
      this.shutdown = true;
      super.shutdown();
   }

   public Object getAttribute(String var1) {
      return this.attributes.get(var1);
   }

   public Object removeAttribute(String var1) {
      return this.attributes.remove(var1);
   }

   public void setAttribute(String var1, Object var2) {
      this.attributes.put(var1, var2);
   }

   public void bind(Socket var1) throws IOException {
      if (this.shutdown) {
         var1.close();
         throw new InterruptedIOException("Connection already shutdown");
      } else {
         super.bind(var1);
      }
   }

   public Socket getSocket() {
      return super.getSocket();
   }

   public SSLSession getSSLSession() {
      Socket var1 = super.getSocket();
      return var1 instanceof SSLSocket ? ((SSLSocket)var1).getSession() : null;
   }
}
