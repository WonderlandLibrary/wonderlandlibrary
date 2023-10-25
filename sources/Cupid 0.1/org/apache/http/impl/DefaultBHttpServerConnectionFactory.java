package org.apache.http.impl;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.HttpConnection;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;

@Immutable
public class DefaultBHttpServerConnectionFactory implements HttpConnectionFactory<DefaultBHttpServerConnection> {
  public static final DefaultBHttpServerConnectionFactory INSTANCE = new DefaultBHttpServerConnectionFactory();
  
  private final ConnectionConfig cconfig;
  
  private final ContentLengthStrategy incomingContentStrategy;
  
  private final ContentLengthStrategy outgoingContentStrategy;
  
  private final HttpMessageParserFactory<HttpRequest> requestParserFactory;
  
  private final HttpMessageWriterFactory<HttpResponse> responseWriterFactory;
  
  public DefaultBHttpServerConnectionFactory(ConnectionConfig cconfig, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, HttpMessageParserFactory<HttpRequest> requestParserFactory, HttpMessageWriterFactory<HttpResponse> responseWriterFactory) {
    this.cconfig = (cconfig != null) ? cconfig : ConnectionConfig.DEFAULT;
    this.incomingContentStrategy = incomingContentStrategy;
    this.outgoingContentStrategy = outgoingContentStrategy;
    this.requestParserFactory = requestParserFactory;
    this.responseWriterFactory = responseWriterFactory;
  }
  
  public DefaultBHttpServerConnectionFactory(ConnectionConfig cconfig, HttpMessageParserFactory<HttpRequest> requestParserFactory, HttpMessageWriterFactory<HttpResponse> responseWriterFactory) {
    this(cconfig, null, null, requestParserFactory, responseWriterFactory);
  }
  
  public DefaultBHttpServerConnectionFactory(ConnectionConfig cconfig) {
    this(cconfig, null, null, null, null);
  }
  
  public DefaultBHttpServerConnectionFactory() {
    this(null, null, null, null, null);
  }
  
  public DefaultBHttpServerConnection createConnection(Socket socket) throws IOException {
    DefaultBHttpServerConnection conn = new DefaultBHttpServerConnection(this.cconfig.getBufferSize(), this.cconfig.getFragmentSizeHint(), ConnSupport.createDecoder(this.cconfig), ConnSupport.createEncoder(this.cconfig), this.cconfig.getMessageConstraints(), this.incomingContentStrategy, this.outgoingContentStrategy, this.requestParserFactory, this.responseWriterFactory);
    conn.bind(socket);
    return conn;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\DefaultBHttpServerConnectionFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */