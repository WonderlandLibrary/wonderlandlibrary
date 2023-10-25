package org.apache.http.protocol;

import java.io.IOException;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpServerConnection;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.EncodingUtils;
import org.apache.http.util.EntityUtils;

@Immutable
public class HttpService {
  private volatile HttpParams params = null;
  
  private volatile HttpProcessor processor = null;
  
  private volatile HttpRequestHandlerMapper handlerMapper = null;
  
  private volatile ConnectionReuseStrategy connStrategy = null;
  
  private volatile HttpResponseFactory responseFactory = null;
  
  private volatile HttpExpectationVerifier expectationVerifier = null;
  
  @Deprecated
  public HttpService(HttpProcessor processor, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory, HttpRequestHandlerResolver handlerResolver, HttpExpectationVerifier expectationVerifier, HttpParams params) {
    this(processor, connStrategy, responseFactory, new HttpRequestHandlerResolverAdapter(handlerResolver), expectationVerifier);
    this.params = params;
  }
  
  @Deprecated
  public HttpService(HttpProcessor processor, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory, HttpRequestHandlerResolver handlerResolver, HttpParams params) {
    this(processor, connStrategy, responseFactory, new HttpRequestHandlerResolverAdapter(handlerResolver), (HttpExpectationVerifier)null);
    this.params = params;
  }
  
  @Deprecated
  public HttpService(HttpProcessor proc, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory) {
    setHttpProcessor(proc);
    setConnReuseStrategy(connStrategy);
    setResponseFactory(responseFactory);
  }
  
  public HttpService(HttpProcessor processor, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory, HttpRequestHandlerMapper handlerMapper, HttpExpectationVerifier expectationVerifier) {
    this.processor = (HttpProcessor)Args.notNull(processor, "HTTP processor");
    this.connStrategy = (connStrategy != null) ? connStrategy : (ConnectionReuseStrategy)DefaultConnectionReuseStrategy.INSTANCE;
    this.responseFactory = (responseFactory != null) ? responseFactory : (HttpResponseFactory)DefaultHttpResponseFactory.INSTANCE;
    this.handlerMapper = handlerMapper;
    this.expectationVerifier = expectationVerifier;
  }
  
  public HttpService(HttpProcessor processor, ConnectionReuseStrategy connStrategy, HttpResponseFactory responseFactory, HttpRequestHandlerMapper handlerMapper) {
    this(processor, connStrategy, responseFactory, handlerMapper, (HttpExpectationVerifier)null);
  }
  
  public HttpService(HttpProcessor processor, HttpRequestHandlerMapper handlerMapper) {
    this(processor, (ConnectionReuseStrategy)null, (HttpResponseFactory)null, handlerMapper, (HttpExpectationVerifier)null);
  }
  
  @Deprecated
  public void setHttpProcessor(HttpProcessor processor) {
    Args.notNull(processor, "HTTP processor");
    this.processor = processor;
  }
  
  @Deprecated
  public void setConnReuseStrategy(ConnectionReuseStrategy connStrategy) {
    Args.notNull(connStrategy, "Connection reuse strategy");
    this.connStrategy = connStrategy;
  }
  
  @Deprecated
  public void setResponseFactory(HttpResponseFactory responseFactory) {
    Args.notNull(responseFactory, "Response factory");
    this.responseFactory = responseFactory;
  }
  
  @Deprecated
  public void setParams(HttpParams params) {
    this.params = params;
  }
  
  @Deprecated
  public void setHandlerResolver(HttpRequestHandlerResolver handlerResolver) {
    this.handlerMapper = new HttpRequestHandlerResolverAdapter(handlerResolver);
  }
  
  @Deprecated
  public void setExpectationVerifier(HttpExpectationVerifier expectationVerifier) {
    this.expectationVerifier = expectationVerifier;
  }
  
  @Deprecated
  public HttpParams getParams() {
    return this.params;
  }
  
  public void handleRequest(HttpServerConnection conn, HttpContext context) throws IOException, HttpException {
    context.setAttribute("http.connection", conn);
    HttpResponse response = null;
    try {
      HttpRequest request = conn.receiveRequestHeader();
      if (request instanceof HttpEntityEnclosingRequest)
        if (((HttpEntityEnclosingRequest)request).expectContinue()) {
          response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_1, 100, context);
          if (this.expectationVerifier != null)
            try {
              this.expectationVerifier.verify(request, response, context);
            } catch (HttpException ex) {
              response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_0, 500, context);
              handleException(ex, response);
            }  
          if (response.getStatusLine().getStatusCode() < 200) {
            conn.sendResponseHeader(response);
            conn.flush();
            response = null;
            conn.receiveRequestEntity((HttpEntityEnclosingRequest)request);
          } 
        } else {
          conn.receiveRequestEntity((HttpEntityEnclosingRequest)request);
        }  
      context.setAttribute("http.request", request);
      if (response == null) {
        response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_1, 200, context);
        this.processor.process(request, context);
        doService(request, response, context);
      } 
      if (request instanceof HttpEntityEnclosingRequest) {
        HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
        EntityUtils.consume(entity);
      } 
    } catch (HttpException ex) {
      response = this.responseFactory.newHttpResponse((ProtocolVersion)HttpVersion.HTTP_1_0, 500, context);
      handleException(ex, response);
    } 
    context.setAttribute("http.response", response);
    this.processor.process(response, context);
    conn.sendResponseHeader(response);
    conn.sendResponseEntity(response);
    conn.flush();
    if (!this.connStrategy.keepAlive(response, context))
      conn.close(); 
  }
  
  protected void handleException(HttpException ex, HttpResponse response) {
    if (ex instanceof org.apache.http.MethodNotSupportedException) {
      response.setStatusCode(501);
    } else if (ex instanceof org.apache.http.UnsupportedHttpVersionException) {
      response.setStatusCode(505);
    } else if (ex instanceof org.apache.http.ProtocolException) {
      response.setStatusCode(400);
    } else {
      response.setStatusCode(500);
    } 
    String message = ex.getMessage();
    if (message == null)
      message = ex.toString(); 
    byte[] msg = EncodingUtils.getAsciiBytes(message);
    ByteArrayEntity entity = new ByteArrayEntity(msg);
    entity.setContentType("text/plain; charset=US-ASCII");
    response.setEntity((HttpEntity)entity);
  }
  
  protected void doService(HttpRequest request, HttpResponse response, HttpContext context) throws HttpException, IOException {
    HttpRequestHandler handler = null;
    if (this.handlerMapper != null)
      handler = this.handlerMapper.lookup(request); 
    if (handler != null) {
      handler.handle(request, response, context);
    } else {
      response.setStatusCode(501);
    } 
  }
  
  @Deprecated
  private static class HttpRequestHandlerResolverAdapter implements HttpRequestHandlerMapper {
    private final HttpRequestHandlerResolver resolver;
    
    public HttpRequestHandlerResolverAdapter(HttpRequestHandlerResolver resolver) {
      this.resolver = resolver;
    }
    
    public HttpRequestHandler lookup(HttpRequest request) {
      return this.resolver.lookup(request.getRequestLine().getUri());
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\protocol\HttpService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */