package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

@Deprecated
@ThreadSafe
public class AutoRetryHttpClient implements HttpClient {
  private final HttpClient backend;
  
  private final ServiceUnavailableRetryStrategy retryStrategy;
  
  private final Log log = LogFactory.getLog(getClass());
  
  public AutoRetryHttpClient(HttpClient client, ServiceUnavailableRetryStrategy retryStrategy) {
    Args.notNull(client, "HttpClient");
    Args.notNull(retryStrategy, "ServiceUnavailableRetryStrategy");
    this.backend = client;
    this.retryStrategy = retryStrategy;
  }
  
  public AutoRetryHttpClient() {
    this(new DefaultHttpClient(), new DefaultServiceUnavailableRetryStrategy());
  }
  
  public AutoRetryHttpClient(ServiceUnavailableRetryStrategy config) {
    this(new DefaultHttpClient(), config);
  }
  
  public AutoRetryHttpClient(HttpClient client) {
    this(client, new DefaultServiceUnavailableRetryStrategy());
  }
  
  public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException {
    HttpContext defaultContext = null;
    return execute(target, request, defaultContext);
  }
  
  public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException {
    return execute(target, request, responseHandler, null);
  }
  
  public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException {
    HttpResponse resp = execute(target, request, context);
    return (T)responseHandler.handleResponse(resp);
  }
  
  public HttpResponse execute(HttpUriRequest request) throws IOException {
    HttpContext context = null;
    return execute(request, context);
  }
  
  public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException {
    URI uri = request.getURI();
    HttpHost httpHost = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
    return execute(httpHost, (HttpRequest)request, context);
  }
  
  public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException {
    return execute(request, responseHandler, (HttpContext)null);
  }
  
  public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException {
    HttpResponse resp = execute(request, context);
    return (T)responseHandler.handleResponse(resp);
  }
  
  public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException {
    for (int c = 1;; c++) {
      HttpResponse response = this.backend.execute(target, request, context);
      try {
        if (this.retryStrategy.retryRequest(response, c, context)) {
          EntityUtils.consume(response.getEntity());
          long nextInterval = this.retryStrategy.getRetryInterval();
          try {
            this.log.trace("Wait for " + nextInterval);
            Thread.sleep(nextInterval);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException();
          } 
        } else {
          return response;
        } 
      } catch (RuntimeException ex) {
        try {
          EntityUtils.consume(response.getEntity());
        } catch (IOException ioex) {
          this.log.warn("I/O error consuming response content", ioex);
        } 
        throw ex;
      } 
    } 
  }
  
  public ClientConnectionManager getConnectionManager() {
    return this.backend.getConnectionManager();
  }
  
  public HttpParams getParams() {
    return this.backend.getParams();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\AutoRetryHttpClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */