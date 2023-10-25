package org.apache.http.impl.conn;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpConnection;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;

@Immutable
public class ManagedHttpClientConnectionFactory implements HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> {
  private static final AtomicLong COUNTER = new AtomicLong();
  
  public static final ManagedHttpClientConnectionFactory INSTANCE = new ManagedHttpClientConnectionFactory();
  
  private final Log log = LogFactory.getLog(DefaultManagedHttpClientConnection.class);
  
  private final Log headerlog = LogFactory.getLog("org.apache.http.headers");
  
  private final Log wirelog = LogFactory.getLog("org.apache.http.wire");
  
  private final HttpMessageWriterFactory<HttpRequest> requestWriterFactory;
  
  private final HttpMessageParserFactory<HttpResponse> responseParserFactory;
  
  public ManagedHttpClientConnectionFactory(HttpMessageWriterFactory<HttpRequest> requestWriterFactory, HttpMessageParserFactory<HttpResponse> responseParserFactory) {
    this.requestWriterFactory = (requestWriterFactory != null) ? requestWriterFactory : (HttpMessageWriterFactory<HttpRequest>)DefaultHttpRequestWriterFactory.INSTANCE;
    this.responseParserFactory = (responseParserFactory != null) ? responseParserFactory : DefaultHttpResponseParserFactory.INSTANCE;
  }
  
  public ManagedHttpClientConnectionFactory(HttpMessageParserFactory<HttpResponse> responseParserFactory) {
    this(null, responseParserFactory);
  }
  
  public ManagedHttpClientConnectionFactory() {
    this(null, null);
  }
  
  public ManagedHttpClientConnection create(HttpRoute route, ConnectionConfig config) {
    ConnectionConfig cconfig = (config != null) ? config : ConnectionConfig.DEFAULT;
    CharsetDecoder chardecoder = null;
    CharsetEncoder charencoder = null;
    Charset charset = cconfig.getCharset();
    CodingErrorAction malformedInputAction = (cconfig.getMalformedInputAction() != null) ? cconfig.getMalformedInputAction() : CodingErrorAction.REPORT;
    CodingErrorAction unmappableInputAction = (cconfig.getUnmappableInputAction() != null) ? cconfig.getUnmappableInputAction() : CodingErrorAction.REPORT;
    if (charset != null) {
      chardecoder = charset.newDecoder();
      chardecoder.onMalformedInput(malformedInputAction);
      chardecoder.onUnmappableCharacter(unmappableInputAction);
      charencoder = charset.newEncoder();
      charencoder.onMalformedInput(malformedInputAction);
      charencoder.onUnmappableCharacter(unmappableInputAction);
    } 
    String id = "http-outgoing-" + Long.toString(COUNTER.getAndIncrement());
    return new LoggingManagedHttpClientConnection(id, this.log, this.headerlog, this.wirelog, cconfig.getBufferSize(), cconfig.getFragmentSizeHint(), chardecoder, charencoder, cconfig.getMessageConstraints(), null, null, this.requestWriterFactory, this.responseParserFactory);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\conn\ManagedHttpClientConnectionFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */