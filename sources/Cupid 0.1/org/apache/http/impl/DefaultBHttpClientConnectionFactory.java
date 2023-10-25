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
public class DefaultBHttpClientConnectionFactory implements HttpConnectionFactory<DefaultBHttpClientConnection> {
  public static final DefaultBHttpClientConnectionFactory INSTANCE = new DefaultBHttpClientConnectionFactory();
  
  private final ConnectionConfig cconfig;
  
  private final ContentLengthStrategy incomingContentStrategy;
  
  private final ContentLengthStrategy outgoingContentStrategy;
  
  private final HttpMessageWriterFactory<HttpRequest> requestWriterFactory;
  
  private final HttpMessageParserFactory<HttpResponse> responseParserFactory;
  
  public DefaultBHttpClientConnectionFactory(ConnectionConfig cconfig, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, HttpMessageWriterFactory<HttpRequest> requestWriterFactory, HttpMessageParserFactory<HttpResponse> responseParserFactory) {
    this.cconfig = (cconfig != null) ? cconfig : ConnectionConfig.DEFAULT;
    this.incomingContentStrategy = incomingContentStrategy;
    this.outgoingContentStrategy = outgoingContentStrategy;
    this.requestWriterFactory = requestWriterFactory;
    this.responseParserFactory = responseParserFactory;
  }
  
  public DefaultBHttpClientConnectionFactory(ConnectionConfig cconfig, HttpMessageWriterFactory<HttpRequest> requestWriterFactory, HttpMessageParserFactory<HttpResponse> responseParserFactory) {
    this(cconfig, null, null, requestWriterFactory, responseParserFactory);
  }
  
  public DefaultBHttpClientConnectionFactory(ConnectionConfig cconfig) {
    this(cconfig, null, null, null, null);
  }
  
  public DefaultBHttpClientConnectionFactory() {
    this(null, null, null, null, null);
  }
  
  public DefaultBHttpClientConnection createConnection(Socket socket) throws IOException {
    DefaultBHttpClientConnection conn = new DefaultBHttpClientConnection(this.cconfig.getBufferSize(), this.cconfig.getFragmentSizeHint(), ConnSupport.createDecoder(this.cconfig), ConnSupport.createEncoder(this.cconfig), this.cconfig.getMessageConstraints(), this.incomingContentStrategy, this.outgoingContentStrategy, this.requestWriterFactory, this.responseParserFactory);
    conn.bind(socket);
    return conn;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\DefaultBHttpClientConnectionFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */