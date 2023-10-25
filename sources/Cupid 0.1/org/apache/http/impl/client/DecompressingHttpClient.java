package org.apache.http.impl.client;

import java.io.IOException;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

@Deprecated
public class DecompressingHttpClient implements HttpClient {
  private final HttpClient backend;
  
  private final HttpRequestInterceptor acceptEncodingInterceptor;
  
  private final HttpResponseInterceptor contentEncodingInterceptor;
  
  public DecompressingHttpClient() {
    this(new DefaultHttpClient());
  }
  
  public DecompressingHttpClient(HttpClient backend) {
    this(backend, (HttpRequestInterceptor)new RequestAcceptEncoding(), (HttpResponseInterceptor)new ResponseContentEncoding());
  }
  
  DecompressingHttpClient(HttpClient backend, HttpRequestInterceptor requestInterceptor, HttpResponseInterceptor responseInterceptor) {
    this.backend = backend;
    this.acceptEncodingInterceptor = requestInterceptor;
    this.contentEncodingInterceptor = responseInterceptor;
  }
  
  public HttpParams getParams() {
    return this.backend.getParams();
  }
  
  public ClientConnectionManager getConnectionManager() {
    return this.backend.getConnectionManager();
  }
  
  public HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
    return execute(getHttpHost(request), (HttpRequest)request, (HttpContext)null);
  }
  
  public HttpClient getHttpClient() {
    return this.backend;
  }
  
  HttpHost getHttpHost(HttpUriRequest request) {
    URI uri = request.getURI();
    return URIUtils.extractHost(uri);
  }
  
  public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException, ClientProtocolException {
    return execute(getHttpHost(request), (HttpRequest)request, context);
  }
  
  public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
    return execute(target, request, (HttpContext)null);
  }
  
  public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException, ClientProtocolException {
    try {
      RequestWrapper requestWrapper;
      HttpContext localContext = (context != null) ? context : (HttpContext)new BasicHttpContext();
      if (request instanceof HttpEntityEnclosingRequest) {
        requestWrapper = new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)request);
      } else {
        requestWrapper = new RequestWrapper(request);
      } 
      this.acceptEncodingInterceptor.process((HttpRequest)requestWrapper, localContext);
      HttpResponse response = this.backend.execute(target, (HttpRequest)requestWrapper, localContext);
      try {
        this.contentEncodingInterceptor.process(response, localContext);
        if (Boolean.TRUE.equals(localContext.getAttribute("http.client.response.uncompressed"))) {
          response.removeHeaders("Content-Length");
          response.removeHeaders("Content-Encoding");
          response.removeHeaders("Content-MD5");
        } 
        return response;
      } catch (HttpException ex) {
        EntityUtils.consume(response.getEntity());
        throw ex;
      } catch (IOException ex) {
        EntityUtils.consume(response.getEntity());
        throw ex;
      } catch (RuntimeException ex) {
        EntityUtils.consume(response.getEntity());
        throw ex;
      } 
    } catch (HttpException e) {
      throw new ClientProtocolException(e);
    } 
  }
  
  public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
    return execute(getHttpHost(request), (HttpRequest)request, responseHandler);
  }
  
  public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
    return execute(getHttpHost(request), (HttpRequest)request, responseHandler, context);
  }
  
  public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
    return execute(target, request, responseHandler, null);
  }
  
  public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context) throws IOException, ClientProtocolException {
    HttpResponse response = execute(target, request, context);
    try {
      return (T)responseHandler.handleResponse(response);
    } finally {
      HttpEntity entity = response.getEntity();
      if (entity != null)
        EntityUtils.consume(entity); 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\DecompressingHttpClient.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */