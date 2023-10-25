package org.apache.http.impl;

import java.io.IOException;
import java.net.SocketTimeoutException;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.entity.EntityDeserializer;
import org.apache.http.impl.entity.EntitySerializer;
import org.apache.http.impl.entity.LaxContentLengthStrategy;
import org.apache.http.impl.entity.StrictContentLengthStrategy;
import org.apache.http.impl.io.DefaultHttpResponseParser;
import org.apache.http.impl.io.HttpRequestWriter;
import org.apache.http.io.EofSensor;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageWriter;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
@NotThreadSafe
public abstract class AbstractHttpClientConnection implements HttpClientConnection {
  private final EntitySerializer entityserializer;
  
  private final EntityDeserializer entitydeserializer;
  
  private SessionInputBuffer inbuffer = null;
  
  private SessionOutputBuffer outbuffer = null;
  
  private EofSensor eofSensor = null;
  
  private HttpMessageParser<HttpResponse> responseParser = null;
  
  private HttpMessageWriter<HttpRequest> requestWriter = null;
  
  private HttpConnectionMetricsImpl metrics = null;
  
  public AbstractHttpClientConnection() {
    this.entityserializer = createEntitySerializer();
    this.entitydeserializer = createEntityDeserializer();
  }
  
  protected abstract void assertOpen() throws IllegalStateException;
  
  protected EntityDeserializer createEntityDeserializer() {
    return new EntityDeserializer((ContentLengthStrategy)new LaxContentLengthStrategy());
  }
  
  protected EntitySerializer createEntitySerializer() {
    return new EntitySerializer((ContentLengthStrategy)new StrictContentLengthStrategy());
  }
  
  protected HttpResponseFactory createHttpResponseFactory() {
    return DefaultHttpResponseFactory.INSTANCE;
  }
  
  protected HttpMessageParser<HttpResponse> createResponseParser(SessionInputBuffer buffer, HttpResponseFactory responseFactory, HttpParams params) {
    return (HttpMessageParser<HttpResponse>)new DefaultHttpResponseParser(buffer, null, responseFactory, params);
  }
  
  protected HttpMessageWriter<HttpRequest> createRequestWriter(SessionOutputBuffer buffer, HttpParams params) {
    return (HttpMessageWriter<HttpRequest>)new HttpRequestWriter(buffer, null, params);
  }
  
  protected HttpConnectionMetricsImpl createConnectionMetrics(HttpTransportMetrics inTransportMetric, HttpTransportMetrics outTransportMetric) {
    return new HttpConnectionMetricsImpl(inTransportMetric, outTransportMetric);
  }
  
  protected void init(SessionInputBuffer inbuffer, SessionOutputBuffer outbuffer, HttpParams params) {
    this.inbuffer = (SessionInputBuffer)Args.notNull(inbuffer, "Input session buffer");
    this.outbuffer = (SessionOutputBuffer)Args.notNull(outbuffer, "Output session buffer");
    if (inbuffer instanceof EofSensor)
      this.eofSensor = (EofSensor)inbuffer; 
    this.responseParser = createResponseParser(inbuffer, createHttpResponseFactory(), params);
    this.requestWriter = createRequestWriter(outbuffer, params);
    this.metrics = createConnectionMetrics(inbuffer.getMetrics(), outbuffer.getMetrics());
  }
  
  public boolean isResponseAvailable(int timeout) throws IOException {
    assertOpen();
    try {
      return this.inbuffer.isDataAvailable(timeout);
    } catch (SocketTimeoutException ex) {
      return false;
    } 
  }
  
  public void sendRequestHeader(HttpRequest request) throws HttpException, IOException {
    Args.notNull(request, "HTTP request");
    assertOpen();
    this.requestWriter.write((HttpMessage)request);
    this.metrics.incrementRequestCount();
  }
  
  public void sendRequestEntity(HttpEntityEnclosingRequest request) throws HttpException, IOException {
    Args.notNull(request, "HTTP request");
    assertOpen();
    if (request.getEntity() == null)
      return; 
    this.entityserializer.serialize(this.outbuffer, (HttpMessage)request, request.getEntity());
  }
  
  protected void doFlush() throws IOException {
    this.outbuffer.flush();
  }
  
  public void flush() throws IOException {
    assertOpen();
    doFlush();
  }
  
  public HttpResponse receiveResponseHeader() throws HttpException, IOException {
    assertOpen();
    HttpResponse response = (HttpResponse)this.responseParser.parse();
    if (response.getStatusLine().getStatusCode() >= 200)
      this.metrics.incrementResponseCount(); 
    return response;
  }
  
  public void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
    Args.notNull(response, "HTTP response");
    assertOpen();
    HttpEntity entity = this.entitydeserializer.deserialize(this.inbuffer, (HttpMessage)response);
    response.setEntity(entity);
  }
  
  protected boolean isEof() {
    return (this.eofSensor != null && this.eofSensor.isEof());
  }
  
  public boolean isStale() {
    if (!isOpen())
      return true; 
    if (isEof())
      return true; 
    try {
      this.inbuffer.isDataAvailable(1);
      return isEof();
    } catch (SocketTimeoutException ex) {
      return false;
    } catch (IOException ex) {
      return true;
    } 
  }
  
  public HttpConnectionMetrics getMetrics() {
    return this.metrics;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\AbstractHttpClientConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */