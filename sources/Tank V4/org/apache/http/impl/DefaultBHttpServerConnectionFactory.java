package org.apache.http.impl;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.HttpConnection;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.annotation.Immutable;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;

@Immutable
public class DefaultBHttpServerConnectionFactory implements HttpConnectionFactory {
   public static final DefaultBHttpServerConnectionFactory INSTANCE = new DefaultBHttpServerConnectionFactory();
   private final ConnectionConfig cconfig;
   private final ContentLengthStrategy incomingContentStrategy;
   private final ContentLengthStrategy outgoingContentStrategy;
   private final HttpMessageParserFactory requestParserFactory;
   private final HttpMessageWriterFactory responseWriterFactory;

   public DefaultBHttpServerConnectionFactory(ConnectionConfig var1, ContentLengthStrategy var2, ContentLengthStrategy var3, HttpMessageParserFactory var4, HttpMessageWriterFactory var5) {
      this.cconfig = var1 != null ? var1 : ConnectionConfig.DEFAULT;
      this.incomingContentStrategy = var2;
      this.outgoingContentStrategy = var3;
      this.requestParserFactory = var4;
      this.responseWriterFactory = var5;
   }

   public DefaultBHttpServerConnectionFactory(ConnectionConfig var1, HttpMessageParserFactory var2, HttpMessageWriterFactory var3) {
      this(var1, (ContentLengthStrategy)null, (ContentLengthStrategy)null, var2, var3);
   }

   public DefaultBHttpServerConnectionFactory(ConnectionConfig var1) {
      this(var1, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageParserFactory)null, (HttpMessageWriterFactory)null);
   }

   public DefaultBHttpServerConnectionFactory() {
      this((ConnectionConfig)null, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageParserFactory)null, (HttpMessageWriterFactory)null);
   }

   public DefaultBHttpServerConnection createConnection(Socket var1) throws IOException {
      DefaultBHttpServerConnection var2 = new DefaultBHttpServerConnection(this.cconfig.getBufferSize(), this.cconfig.getFragmentSizeHint(), ConnSupport.createDecoder(this.cconfig), ConnSupport.createEncoder(this.cconfig), this.cconfig.getMessageConstraints(), this.incomingContentStrategy, this.outgoingContentStrategy, this.requestParserFactory, this.responseWriterFactory);
      var2.bind(var1);
      return var2;
   }

   public HttpConnection createConnection(Socket var1) throws IOException {
      return this.createConnection(var1);
   }
}
