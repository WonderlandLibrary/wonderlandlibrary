package org.apache.http.impl.conn;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpConnection;
import org.apache.http.annotation.Immutable;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;

@Immutable
public class ManagedHttpClientConnectionFactory implements HttpConnectionFactory {
   private static final AtomicLong COUNTER = new AtomicLong();
   public static final ManagedHttpClientConnectionFactory INSTANCE = new ManagedHttpClientConnectionFactory();
   private final Log log;
   private final Log headerlog;
   private final Log wirelog;
   private final HttpMessageWriterFactory requestWriterFactory;
   private final HttpMessageParserFactory responseParserFactory;

   public ManagedHttpClientConnectionFactory(HttpMessageWriterFactory var1, HttpMessageParserFactory var2) {
      this.log = LogFactory.getLog(DefaultManagedHttpClientConnection.class);
      this.headerlog = LogFactory.getLog("org.apache.http.headers");
      this.wirelog = LogFactory.getLog("org.apache.http.wire");
      this.requestWriterFactory = (HttpMessageWriterFactory)(var1 != null ? var1 : DefaultHttpRequestWriterFactory.INSTANCE);
      this.responseParserFactory = (HttpMessageParserFactory)(var2 != null ? var2 : DefaultHttpResponseParserFactory.INSTANCE);
   }

   public ManagedHttpClientConnectionFactory(HttpMessageParserFactory var1) {
      this((HttpMessageWriterFactory)null, var1);
   }

   public ManagedHttpClientConnectionFactory() {
      this((HttpMessageWriterFactory)null, (HttpMessageParserFactory)null);
   }

   public ManagedHttpClientConnection create(HttpRoute var1, ConnectionConfig var2) {
      ConnectionConfig var3 = var2 != null ? var2 : ConnectionConfig.DEFAULT;
      CharsetDecoder var4 = null;
      CharsetEncoder var5 = null;
      Charset var6 = var3.getCharset();
      CodingErrorAction var7 = var3.getMalformedInputAction() != null ? var3.getMalformedInputAction() : CodingErrorAction.REPORT;
      CodingErrorAction var8 = var3.getUnmappableInputAction() != null ? var3.getUnmappableInputAction() : CodingErrorAction.REPORT;
      if (var6 != null) {
         var4 = var6.newDecoder();
         var4.onMalformedInput(var7);
         var4.onUnmappableCharacter(var8);
         var5 = var6.newEncoder();
         var5.onMalformedInput(var7);
         var5.onUnmappableCharacter(var8);
      }

      String var9 = "http-outgoing-" + Long.toString(COUNTER.getAndIncrement());
      return new LoggingManagedHttpClientConnection(var9, this.log, this.headerlog, this.wirelog, var3.getBufferSize(), var3.getFragmentSizeHint(), var4, var5, var3.getMessageConstraints(), (ContentLengthStrategy)null, (ContentLengthStrategy)null, this.requestWriterFactory, this.responseParserFactory);
   }

   public HttpConnection create(Object var1, ConnectionConfig var2) {
      return this.create((HttpRoute)var1, var2);
   }
}
