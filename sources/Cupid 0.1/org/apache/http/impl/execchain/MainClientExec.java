package org.apache.http.impl.execchain;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthState;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.NonRepeatableRequestException;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.BasicRouteDirector;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRouteDirector;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.conn.routing.RouteTracker;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.auth.HttpAuthenticator;
import org.apache.http.impl.conn.ConnectionShutdownException;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

@Immutable
public class MainClientExec implements ClientExecChain {
  private final Log log = LogFactory.getLog(getClass());
  
  private final HttpRequestExecutor requestExecutor;
  
  private final HttpClientConnectionManager connManager;
  
  private final ConnectionReuseStrategy reuseStrategy;
  
  private final ConnectionKeepAliveStrategy keepAliveStrategy;
  
  private final HttpProcessor proxyHttpProcessor;
  
  private final AuthenticationStrategy targetAuthStrategy;
  
  private final AuthenticationStrategy proxyAuthStrategy;
  
  private final HttpAuthenticator authenticator;
  
  private final UserTokenHandler userTokenHandler;
  
  private final HttpRouteDirector routeDirector;
  
  public MainClientExec(HttpRequestExecutor requestExecutor, HttpClientConnectionManager connManager, ConnectionReuseStrategy reuseStrategy, ConnectionKeepAliveStrategy keepAliveStrategy, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler) {
    Args.notNull(requestExecutor, "HTTP request executor");
    Args.notNull(connManager, "Client connection manager");
    Args.notNull(reuseStrategy, "Connection reuse strategy");
    Args.notNull(keepAliveStrategy, "Connection keep alive strategy");
    Args.notNull(targetAuthStrategy, "Target authentication strategy");
    Args.notNull(proxyAuthStrategy, "Proxy authentication strategy");
    Args.notNull(userTokenHandler, "User token handler");
    this.authenticator = new HttpAuthenticator();
    this.proxyHttpProcessor = (HttpProcessor)new ImmutableHttpProcessor(new HttpRequestInterceptor[] { (HttpRequestInterceptor)new RequestTargetHost(), (HttpRequestInterceptor)new RequestClientConnControl() });
    this.routeDirector = (HttpRouteDirector)new BasicRouteDirector();
    this.requestExecutor = requestExecutor;
    this.connManager = connManager;
    this.reuseStrategy = reuseStrategy;
    this.keepAliveStrategy = keepAliveStrategy;
    this.targetAuthStrategy = targetAuthStrategy;
    this.proxyAuthStrategy = proxyAuthStrategy;
    this.userTokenHandler = userTokenHandler;
  }
  
  public CloseableHttpResponse execute(HttpRoute route, HttpRequestWrapper request, HttpClientContext context, HttpExecutionAware execAware) throws IOException, HttpException {
    HttpClientConnection managedConn;
    Args.notNull(route, "HTTP route");
    Args.notNull(request, "HTTP request");
    Args.notNull(context, "HTTP context");
    AuthState targetAuthState = context.getTargetAuthState();
    if (targetAuthState == null) {
      targetAuthState = new AuthState();
      context.setAttribute("http.auth.target-scope", targetAuthState);
    } 
    AuthState proxyAuthState = context.getProxyAuthState();
    if (proxyAuthState == null) {
      proxyAuthState = new AuthState();
      context.setAttribute("http.auth.proxy-scope", proxyAuthState);
    } 
    if (request instanceof HttpEntityEnclosingRequest)
      Proxies.enhanceEntity((HttpEntityEnclosingRequest)request); 
    Object userToken = context.getUserToken();
    ConnectionRequest connRequest = this.connManager.requestConnection(route, userToken);
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
    context.setAttribute("http.connection", managedConn);
    if (config.isStaleConnectionCheckEnabled())
      if (managedConn.isOpen()) {
        this.log.debug("Stale connection check");
        if (managedConn.isStale()) {
          this.log.debug("Stale connection detected");
          managedConn.close();
        } 
      }  
    ConnectionHolder connHolder = new ConnectionHolder(this.log, this.connManager, managedConn);
    try {
      HttpResponse response;
      if (execAware != null)
        execAware.setCancellable(connHolder); 
      int execCount = 1;
      while (true) {
        if (execCount > 1 && !Proxies.isRepeatable((HttpRequest)request))
          throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity."); 
        if (execAware != null && execAware.isAborted())
          throw new RequestAbortedException("Request aborted"); 
        if (!managedConn.isOpen()) {
          this.log.debug("Opening connection " + route);
          try {
            establishRoute(proxyAuthState, managedConn, route, (HttpRequest)request, context);
          } catch (TunnelRefusedException ex) {
            if (this.log.isDebugEnabled())
              this.log.debug(ex.getMessage()); 
            HttpResponse httpResponse = ex.getResponse();
            break;
          } 
        } 
        int timeout = config.getSocketTimeout();
        if (timeout >= 0)
          managedConn.setSocketTimeout(timeout); 
        if (execAware != null && execAware.isAborted())
          throw new RequestAbortedException("Request aborted"); 
        if (this.log.isDebugEnabled())
          this.log.debug("Executing request " + request.getRequestLine()); 
        if (!request.containsHeader("Authorization")) {
          if (this.log.isDebugEnabled())
            this.log.debug("Target auth state: " + targetAuthState.getState()); 
          this.authenticator.generateAuthResponse((HttpRequest)request, targetAuthState, (HttpContext)context);
        } 
        if (!request.containsHeader("Proxy-Authorization") && !route.isTunnelled()) {
          if (this.log.isDebugEnabled())
            this.log.debug("Proxy auth state: " + proxyAuthState.getState()); 
          this.authenticator.generateAuthResponse((HttpRequest)request, proxyAuthState, (HttpContext)context);
        } 
        response = this.requestExecutor.execute((HttpRequest)request, managedConn, (HttpContext)context);
        if (this.reuseStrategy.keepAlive(response, (HttpContext)context)) {
          long duration = this.keepAliveStrategy.getKeepAliveDuration(response, (HttpContext)context);
          if (this.log.isDebugEnabled()) {
            String s;
            if (duration > 0L) {
              s = "for " + duration + " " + TimeUnit.MILLISECONDS;
            } else {
              s = "indefinitely";
            } 
            this.log.debug("Connection can be kept alive " + s);
          } 
          connHolder.setValidFor(duration, TimeUnit.MILLISECONDS);
          connHolder.markReusable();
        } else {
          connHolder.markNonReusable();
        } 
        if (needAuthentication(targetAuthState, proxyAuthState, route, response, context)) {
          HttpEntity httpEntity = response.getEntity();
          if (connHolder.isReusable()) {
            EntityUtils.consume(httpEntity);
          } else {
            managedConn.close();
            if (proxyAuthState.getState() == AuthProtocolState.SUCCESS && proxyAuthState.getAuthScheme() != null && proxyAuthState.getAuthScheme().isConnectionBased()) {
              this.log.debug("Resetting proxy auth state");
              proxyAuthState.reset();
            } 
            if (targetAuthState.getState() == AuthProtocolState.SUCCESS && targetAuthState.getAuthScheme() != null && targetAuthState.getAuthScheme().isConnectionBased()) {
              this.log.debug("Resetting target auth state");
              targetAuthState.reset();
            } 
          } 
          HttpRequest original = request.getOriginal();
          if (!original.containsHeader("Authorization"))
            request.removeHeaders("Authorization"); 
          if (!original.containsHeader("Proxy-Authorization"))
            request.removeHeaders("Proxy-Authorization"); 
          execCount++;
        } 
        break;
      } 
      if (userToken == null) {
        userToken = this.userTokenHandler.getUserToken((HttpContext)context);
        context.setAttribute("http.user-token", userToken);
      } 
      if (userToken != null)
        connHolder.setState(userToken); 
      HttpEntity entity = response.getEntity();
      if (entity == null || !entity.isStreaming()) {
        connHolder.releaseConnection();
        return Proxies.enhanceResponse(response, null);
      } 
      return Proxies.enhanceResponse(response, connHolder);
    } catch (ConnectionShutdownException ex) {
      InterruptedIOException ioex = new InterruptedIOException("Connection has been shut down");
      ioex.initCause((Throwable)ex);
      throw ioex;
    } catch (HttpException ex) {
      connHolder.abortConnection();
      throw ex;
    } catch (IOException ex) {
      connHolder.abortConnection();
      throw ex;
    } catch (RuntimeException ex) {
      connHolder.abortConnection();
      throw ex;
    } 
  }
  
  void establishRoute(AuthState proxyAuthState, HttpClientConnection managedConn, HttpRoute route, HttpRequest request, HttpClientContext context) throws HttpException, IOException {
    int step;
    RequestConfig config = context.getRequestConfig();
    int timeout = config.getConnectTimeout();
    RouteTracker tracker = new RouteTracker(route);
    do {
      HttpHost proxy;
      boolean secure;
      int hop;
      boolean bool1;
      HttpRoute fact = tracker.toRoute();
      step = this.routeDirector.nextStep((RouteInfo)route, (RouteInfo)fact);
      switch (step) {
        case 1:
          this.connManager.connect(managedConn, route, (timeout > 0) ? timeout : 0, (HttpContext)context);
          tracker.connectTarget(route.isSecure());
          break;
        case 2:
          this.connManager.connect(managedConn, route, (timeout > 0) ? timeout : 0, (HttpContext)context);
          proxy = route.getProxyHost();
          tracker.connectProxy(proxy, false);
          break;
        case 3:
          secure = createTunnelToTarget(proxyAuthState, managedConn, route, request, context);
          this.log.debug("Tunnel to target created.");
          tracker.tunnelTarget(secure);
          break;
        case 4:
          hop = fact.getHopCount() - 1;
          bool1 = createTunnelToProxy(route, hop, context);
          this.log.debug("Tunnel to proxy created.");
          tracker.tunnelProxy(route.getHopTarget(hop), bool1);
          break;
        case 5:
          this.connManager.upgrade(managedConn, route, (HttpContext)context);
          tracker.layerProtocol(route.isSecure());
          break;
        case -1:
          throw new HttpException("Unable to establish route: planned = " + route + "; current = " + fact);
        case 0:
          this.connManager.routeComplete(managedConn, route, (HttpContext)context);
          break;
        default:
          throw new IllegalStateException("Unknown step indicator " + step + " from RouteDirector.");
      } 
    } while (step > 0);
  }
  
  private boolean createTunnelToTarget(AuthState proxyAuthState, HttpClientConnection managedConn, HttpRoute route, HttpRequest request, HttpClientContext context) throws HttpException, IOException {
    HttpResponse response;
    RequestConfig config = context.getRequestConfig();
    int timeout = config.getConnectTimeout();
    HttpHost target = route.getTargetHost();
    HttpHost proxy = route.getProxyHost();
    String authority = target.toHostString();
    BasicHttpRequest basicHttpRequest = new BasicHttpRequest("CONNECT", authority, request.getProtocolVersion());
    this.requestExecutor.preProcess((HttpRequest)basicHttpRequest, this.proxyHttpProcessor, (HttpContext)context);
    while (true) {
      if (!managedConn.isOpen())
        this.connManager.connect(managedConn, route, (timeout > 0) ? timeout : 0, (HttpContext)context); 
      basicHttpRequest.removeHeaders("Proxy-Authorization");
      this.authenticator.generateAuthResponse((HttpRequest)basicHttpRequest, proxyAuthState, (HttpContext)context);
      response = this.requestExecutor.execute((HttpRequest)basicHttpRequest, managedConn, (HttpContext)context);
      int i = response.getStatusLine().getStatusCode();
      if (i < 200)
        throw new HttpException("Unexpected response to CONNECT request: " + response.getStatusLine()); 
      if (config.isAuthenticationEnabled()) {
        if (this.authenticator.isAuthenticationRequested(proxy, response, this.proxyAuthStrategy, proxyAuthState, (HttpContext)context))
          if (this.authenticator.handleAuthChallenge(proxy, response, this.proxyAuthStrategy, proxyAuthState, (HttpContext)context)) {
            if (this.reuseStrategy.keepAlive(response, (HttpContext)context)) {
              this.log.debug("Connection kept alive");
              HttpEntity entity = response.getEntity();
              EntityUtils.consume(entity);
              continue;
            } 
            managedConn.close();
            continue;
          }  
        break;
      } 
    } 
    int status = response.getStatusLine().getStatusCode();
    if (status > 299) {
      HttpEntity entity = response.getEntity();
      if (entity != null)
        response.setEntity((HttpEntity)new BufferedHttpEntity(entity)); 
      managedConn.close();
      throw new TunnelRefusedException("CONNECT refused by proxy: " + response.getStatusLine(), response);
    } 
    return false;
  }
  
  private boolean createTunnelToProxy(HttpRoute route, int hop, HttpClientContext context) throws HttpException {
    throw new HttpException("Proxy chains are not supported.");
  }
  
  private boolean needAuthentication(AuthState targetAuthState, AuthState proxyAuthState, HttpRoute route, HttpResponse response, HttpClientContext context) {
    RequestConfig config = context.getRequestConfig();
    if (config.isAuthenticationEnabled()) {
      HttpHost target = context.getTargetHost();
      if (target == null)
        target = route.getTargetHost(); 
      if (target.getPort() < 0)
        target = new HttpHost(target.getHostName(), route.getTargetHost().getPort(), target.getSchemeName()); 
      boolean targetAuthRequested = this.authenticator.isAuthenticationRequested(target, response, this.targetAuthStrategy, targetAuthState, (HttpContext)context);
      HttpHost proxy = route.getProxyHost();
      if (proxy == null)
        proxy = route.getTargetHost(); 
      boolean proxyAuthRequested = this.authenticator.isAuthenticationRequested(proxy, response, this.proxyAuthStrategy, proxyAuthState, (HttpContext)context);
      if (targetAuthRequested)
        return this.authenticator.handleAuthChallenge(target, response, this.targetAuthStrategy, targetAuthState, (HttpContext)context); 
      if (proxyAuthRequested)
        return this.authenticator.handleAuthChallenge(proxy, response, this.proxyAuthStrategy, proxyAuthState, (HttpContext)context); 
    } 
    return false;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\execchain\MainClientExec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */