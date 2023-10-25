package org.apache.http.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriter;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.util.Args;

@NotThreadSafe
public class DefaultBHttpClientConnection extends BHttpConnectionBase implements HttpClientConnection {
  private final HttpMessageParser<HttpResponse> responseParser;
  
  private final HttpMessageWriter<HttpRequest> requestWriter;
  
  public DefaultBHttpClientConnection(int buffersize, int fragmentSizeHint, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints, ContentLengthStrategy incomingContentStrategy, ContentLengthStrategy outgoingContentStrategy, HttpMessageWriterFactory<HttpRequest> requestWriterFactory, HttpMessageParserFactory<HttpResponse> responseParserFactory) {
    super(buffersize, fragmentSizeHint, chardecoder, charencoder, constraints, incomingContentStrategy, outgoingContentStrategy);
    this.requestWriter = ((requestWriterFactory != null) ? requestWriterFactory : DefaultHttpRequestWriterFactory.INSTANCE).create(getSessionOutputBuffer());
    this.responseParser = ((responseParserFactory != null) ? responseParserFactory : DefaultHttpResponseParserFactory.INSTANCE).create(getSessionInputBuffer(), constraints);
  }
  
  public DefaultBHttpClientConnection(int buffersize, CharsetDecoder chardecoder, CharsetEncoder charencoder, MessageConstraints constraints) {
    this(buffersize, buffersize, chardecoder, charencoder, constraints, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageWriterFactory<HttpRequest>)null, (HttpMessageParserFactory<HttpResponse>)null);
  }
  
  public DefaultBHttpClientConnection(int buffersize) {
    this(buffersize, buffersize, (CharsetDecoder)null, (CharsetEncoder)null, (MessageConstraints)null, (ContentLengthStrategy)null, (ContentLengthStrategy)null, (HttpMessageWriterFactory<HttpRequest>)null, (HttpMessageParserFactory<HttpResponse>)null);
  }
  
  protected void onResponseReceived(HttpResponse response) {}
  
  protected void onRequestSubmitted(HttpRequest request) {}
  
  public void bind(Socket socket) throws IOException {
    super.bind(socket);
  }
  
  public boolean isResponseAvailable(int timeout) throws IOException {
    ensureOpen();
    try {
      return awaitInput(timeout);
    } catch (SocketTimeoutException ex) {
      return false;
    } 
  }
  
  public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
    Args.notNull(request, "HTTP request");
    ensureOpen();
    this.requestWriter.write((HttpMessage)request);
    onRequestSubmitted(request);
    incrementRequestCount();
  }
  
  public void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
    Args.notNull(request, "HTTP request");
    ensureOpen();
    HttpEntity entity = request.getEntity();
    if (entity == null)
      return; 
    OutputStream outstream = prepareOutput((HttpMessage)request);
    entity.writeTo(outstream);
    outstream.close();
  }
  
  public HttpResponse receiveResponseHeader() throws HttpException, IOException {
    ensureOpen();
    HttpResponse response = (HttpResponse)this.responseParser.parse();
    onResponseReceived(response);
    if (response.getStatusLine().getStatusCode() >= 200)
      incrementResponseCount(); 
    return response;
  }
  
  public void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
    Args.notNull(response, "HTTP response");
    ensureOpen();
    HttpEntity entity = prepareInput((HttpMessage)response);
    response.setEntity(entity);
  }
  
  public void flush() throws IOException {
    ensureOpen();
    doFlush();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\DefaultBHttpClientConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */