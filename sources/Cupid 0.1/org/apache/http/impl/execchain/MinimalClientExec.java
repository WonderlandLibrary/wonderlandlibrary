package org.apache.http.impl.execchain;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.ConnectionShutdownException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.Args;
import org.apache.http.util.VersionInfo;

@Immutable
public class MinimalClientExec implements ClientExecChain {
  private final Log log = LogFactory.getLog(getClass());
  
  private final HttpRequestExecutor requestExecutor;
  
  private final HttpClientConnectionManager connManager;
  
  private final ConnectionReuseStrategy reuseStrategy;
  
  private final ConnectionKeepAliveStrategy keepAliveStrategy;
  
  private final HttpProcessor httpProcessor;
  
  public MinimalClientExec(HttpRequestExecutor requestExecutor, HttpClientConnectionManager connManager, ConnectionReuseStrategy reuseStrategy, ConnectionKeepAliveStrategy keepAliveStrategy) {
    Args.notNull(requestExecutor, "HTTP request executor");
    Args.notNull(connManager, "Client connection manager");
    Args.notNull(reuseStrategy, "Connection reuse strategy");
    Args.notNull(keepAliveStrategy, "Connection keep alive strategy");
    this.httpProcessor = (HttpProcessor)new ImmutableHttpProcessor(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestContent(), (HttpRequestInterceptor)new RequestTargetHost(), (HttpRequestInterceptor)new RequestClientConnControl(), (HttpRequestInterceptor)new RequestUserAgent(VersionInfo.getUserAgent("Apache-HttpClient", "org.apache.http.client", getClass())) });
    this.requestExecutor = requestExecutor;
    this.connManager = connManager;
    this.reuseStrategy = reuseStrategy;
    this.keepAliveStrategy = keepAliveStrategy;
  }
  
  static void rewriteRequestURI(HttpRequestWrapper request, HttpRoute route) throws ProtocolException {
    try {
      URI uri = request.getURI();
      if (uri != null) {
        if (uri.isAbsolute()) {
          uri = URIUtils.rewriteURI(uri, null, true);
        } else {
          uri = URIUtils.rewriteURI(uri);
        } 
        request.setURI(uri);
      } 
    } catch (URISyntaxException ex) {
      throw new ProtocolException("Invalid URI: " + request.getRequestLine().getUri(), ex);
    } 
  }
  
  public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
    HttpClientConnection managedConn;
    Args.notNull(route, "HTTP route");
    Args.notNull(request, "HTTP request");
    Args.notNull(context, "HTTP context");
    rewriteRequestURI(request, route);
    ConnectionRequest connRequest = this.connManager.requestConnection(route, null);
    if (execAware != null) {
      if (execAware.isAborted()) {
        connRequest.cancel();
        throw new RequestAbortedException("Request aborted");
      } 
      execAware.setCancellable((Cancellable)connRequest);
    } 
    RequestConfig config = context.getRequestConfig();
    try {
      int timeout = config.getConnectionRequestTimeout();
      managedConn = connRequest.get((timeout > 0) ? timeout : 0L, TimeUnit.MILLISECONDS);
    } catch (InterruptedException interrupted) {
      Thread.currentThread().interrupt();
      throw new RequestAbortedException("Request aborted", interrupted);
    } catch (ExecutionException ex) {
      Throwable cause = ex.getCause();
      if (cause == null)
        cause = ex; 
      throw new RequestAbortedException("Request execution failed", cause);
    } 
    ConnectionHolder releaseTrigger = new ConnectionHolder(this.log, this.connManager, managedConn);
    try {
      if (execAware != null) {
        if (execAware.isAborted()) {
          releaseTrigger.close();
          throw new RequestAbortedException("Request aborted");
        } 
        execAware.setCancellable(releaseTrigger);
      } 
      if (!managedConn.isOpen()) {
        int i = config.getConnectTimeout();
        this.connManager.connect(managedConn, route, (i > 0) ? i : 0, (HttpContext)context);
        this.connManager.routeComplete(managedConn, route, (HttpContext)context);
      } 
      int timeout = config.getSocketTimeout();
      if (timeout >= 0)
        managedConn.setSocketTimeout(timeout); 
      HttpHost target = null;
      HttpRequest original = request.getOriginal();
      if (original instanceof HttpUriRequest) {
        URI uri = ((HttpUriRequest)original).getURI();
        if (uri.isAbsolute())
          target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme()); 
      } 
      if (target == null)
        target = route.getTargetHost(); 
      context.setAttribute("http.target_host", target);
      context.setAttribute("http.request", request);
      context.setAttribute("http.connection", managedConn);
      context.setAttribute("http.route", route);
      this.httpProcessor.process((HttpRequest)request, (HttpContext)context);
      HttpResponse response = this.requestExecutor.execute((HttpRequest)request, managedConn, (HttpContext)context);
      this.httpProcessor.process(response, (HttpContext)context);
      if (this.reuseStrategy.keepAlive(response, (HttpContext)context)) {
        long duration = this.keepAliveStrategy.getKeepAliveDuration(response, (HttpContext)context);
        releaseTrigger.setValidFor(duration, TimeUnit.MILLISECONDS);
        releaseTrigger.markReusable();
      } else {
        releaseTrigger.markNonReusable();
      } 
      HttpEntity entity = response.getEntity();
      if (entity == null || !entity.isStreaming()) {
        releaseTrigger.releaseConnection();
        return Proxies.enhanceResponse(response, null);
      } 
      return Proxies.enhanceResponse(response, releaseTrigger);
    } catch (ConnectionShutdownException ex) {
      InterruptedIOException ioex = new InterruptedIOException("Connection has been shut down");
      ioex.initCause((Throwable)ex);
      throw ioex;
    } catch (HttpException ex) {
      releaseTrigger.abortConnection();
      throw ex;
    } catch (IOException ex) {
      releaseTrigger.abortConnection();
      throw ex;
    } catch (RuntimeException ex) {
      releaseTrigger.abortConnection();
      throw ex;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\execchain\MinimalClientExec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */