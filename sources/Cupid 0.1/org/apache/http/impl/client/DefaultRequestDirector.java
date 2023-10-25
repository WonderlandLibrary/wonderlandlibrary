package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ProtocolException;
import org.apache.http.ProtocolVersion;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthProtocolState;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.NonRepeatableRequestException;
import org.apache.http.client.RedirectException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.RequestDirector;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.methods.AbortableHttpRequest;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.BasicManagedEntity;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ConnectionReleaseTrigger;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.BasicRouteDirector;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.conn.ConnectionShutdownException;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

@Deprecated
@NotThreadSafe
public class DefaultRequestDirector implements RequestDirector {
  private final Log log;
  
  protected final ClientConnectionManager connManager;
  
  protected final HttpRoutePlanner routePlanner;
  
  protected final ConnectionReuseStrategy reuseStrategy;
  
  protected final ConnectionKeepAliveStrategy keepAliveStrategy;
  
  protected final HttpRequestExecutor requestExec;
  
  protected final HttpProcessor httpProcessor;
  
  protected final HttpRequestRetryHandler retryHandler;
  
  @Deprecated
  protected final RedirectHandler redirectHandler;
  
  protected final RedirectStrategy redirectStrategy;
  
  @Deprecated
  protected final AuthenticationHandler targetAuthHandler;
  
  protected final AuthenticationStrategy targetAuthStrategy;
  
  @Deprecated
  protected final AuthenticationHandler proxyAuthHandler;
  
  protected final AuthenticationStrategy proxyAuthStrategy;
  
  protected final UserTokenHandler userTokenHandler;
  
  protected final HttpParams params;
  
  protected ManagedClientConnection managedConn;
  
  protected final AuthState targetAuthState;
  
  protected final AuthState proxyAuthState;
  
  private final HttpAuthenticator authenticator;
  
  private int execCount;
  
  private int redirectCount;
  
  private final int maxRedirects;
  
  private HttpHost virtualHost;
  
  @Deprecated
  public DefaultRequestDirector(HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectHandler redirectHandler, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params) {
    this(LogFactory.getLog(DefaultRequestDirector.class), requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, new DefaultRedirectStrategyAdaptor(redirectHandler), new AuthenticationStrategyAdaptor(targetAuthHandler), new AuthenticationStrategyAdaptor(proxyAuthHandler), userTokenHandler, params);
  }
  
  @Deprecated
  public DefaultRequestDirector(Log log, HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationHandler targetAuthHandler, AuthenticationHandler proxyAuthHandler, UserTokenHandler userTokenHandler, HttpParams params) {
    this(LogFactory.getLog(DefaultRequestDirector.class), requestExec, conman, reustrat, kastrat, rouplan, httpProcessor, retryHandler, redirectStrategy, new AuthenticationStrategyAdaptor(targetAuthHandler), new AuthenticationStrategyAdaptor(proxyAuthHandler), userTokenHandler, params);
  }
  
  public DefaultRequestDirector(Log log, HttpRequestExecutor requestExec, ClientConnectionManager conman, ConnectionReuseStrategy reustrat, ConnectionKeepAliveStrategy kastrat, HttpRoutePlanner rouplan, HttpProcessor httpProcessor, HttpRequestRetryHandler retryHandler, RedirectStrategy redirectStrategy, AuthenticationStrategy targetAuthStrategy, AuthenticationStrategy proxyAuthStrategy, UserTokenHandler userTokenHandler, HttpParams params) {
    Args.notNull(log, "Log");
    Args.notNull(requestExec, "Request executor");
    Args.notNull(conman, "Client connection manager");
    Args.notNull(reustrat, "Connection reuse strategy");
    Args.notNull(kastrat, "Connection keep alive strategy");
    Args.notNull(rouplan, "Route planner");
    Args.notNull(httpProcessor, "HTTP protocol processor");
    Args.notNull(retryHandler, "HTTP request retry handler");
    Args.notNull(redirectStrategy, "Redirect strategy");
    Args.notNull(targetAuthStrategy, "Target authentication strategy");
    Args.notNull(proxyAuthStrategy, "Proxy authentication strategy");
    Args.notNull(userTokenHandler, "User token handler");
    Args.notNull(params, "HTTP parameters");
    this.log = log;
    this.authenticator = new HttpAuthenticator(log);
    this.requestExec = requestExec;
    this.connManager = conman;
    this.reuseStrategy = reustrat;
    this.keepAliveStrategy = kastrat;
    this.routePlanner = rouplan;
    this.httpProcessor = httpProcessor;
    this.retryHandler = retryHandler;
    this.redirectStrategy = redirectStrategy;
    this.targetAuthStrategy = targetAuthStrategy;
    this.proxyAuthStrategy = proxyAuthStrategy;
    this.userTokenHandler = userTokenHandler;
    this.params = params;
    if (redirectStrategy instanceof DefaultRedirectStrategyAdaptor) {
      this.redirectHandler = ((DefaultRedirectStrategyAdaptor)redirectStrategy).getHandler();
    } else {
      this.redirectHandler = null;
    } 
    if (targetAuthStrategy instanceof AuthenticationStrategyAdaptor) {
      this.targetAuthHandler = ((AuthenticationStrategyAdaptor)targetAuthStrategy).getHandler();
    } else {
      this.targetAuthHandler = null;
    } 
    if (proxyAuthStrategy instanceof AuthenticationStrategyAdaptor) {
      this.proxyAuthHandler = ((AuthenticationStrategyAdaptor)proxyAuthStrategy).getHandler();
    } else {
      this.proxyAuthHandler = null;
    } 
    this.managedConn = null;
    this.execCount = 0;
    this.redirectCount = 0;
    this.targetAuthState = new AuthState();
    this.proxyAuthState = new AuthState();
    this.maxRedirects = this.params.getIntParameter("http.protocol.max-redirects", 100);
  }
  
  private RequestWrapper wrapRequest(HttpRequest request) throws ProtocolException {
    if (request instanceof HttpEntityEnclosingRequest)
      return new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)request); 
    return new RequestWrapper(request);
  }
  
  protected void rewriteRequestURI(RequestWrapper request, HttpRoute route) throws ProtocolException {
    try {
      URI uri = request.getURI();
      if (route.getProxyHost() != null && !route.isTunnelled()) {
        if (!uri.isAbsolute()) {
          HttpHost target = route.getTargetHost();
          uri = URIUtils.rewriteURI(uri, target, true);
        } else {
          uri = URIUtils.rewriteURI(uri);
        } 
      } else if (uri.isAbsolute()) {
        uri = URIUtils.rewriteURI(uri, null, true);
      } else {
        uri = URIUtils.rewriteURI(uri);
      } 
      request.setURI(uri);
    } catch (URISyntaxException ex) {
      throw new ProtocolException("Invalid URI: " + request.getRequestLine().getUri(), ex);
    } 
  }
  
  public HttpResponse execute(HttpHost targetHost, HttpRequest request, HttpContext context) throws HttpException, IOException {
    context.setAttribute("http.auth.target-scope", this.targetAuthState);
    context.setAttribute("http.auth.proxy-scope", this.proxyAuthState);
    HttpHost target = targetHost;
    HttpRequest orig = request;
    RequestWrapper origWrapper = wrapRequest(orig);
    origWrapper.setParams(this.params);
    HttpRoute origRoute = determineRoute(target, (HttpRequest)origWrapper, context);
    this.virtualHost = (HttpHost)origWrapper.getParams().getParameter("http.virtual-host");
    if (this.virtualHost != null && this.virtualHost.getPort() == -1) {
      HttpHost host = (target != null) ? target : origRoute.getTargetHost();
      int port = host.getPort();
      if (port != -1)
        this.virtualHost = new HttpHost(this.virtualHost.getHostName(), port, this.virtualHost.getSchemeName()); 
    } 
    RoutedRequest roureq = new RoutedRequest(origWrapper, origRoute);
    boolean reuse = false;
    boolean done = false;
    try {
      HttpResponse response = null;
      while (!done) {
        RequestWrapper wrapper = roureq.getRequest();
        HttpRoute route = roureq.getRoute();
        response = null;
        Object userToken = context.getAttribute("http.user-token");
        if (this.managedConn == null) {
          ClientConnectionRequest connRequest = this.connManager.requestConnection(route, userToken);
          if (orig instanceof AbortableHttpRequest)
            ((AbortableHttpRequest)orig).setConnectionRequest(connRequest); 
          long timeout = HttpClientParams.getConnectionManagerTimeout(this.params);
          try {
            this.managedConn = connRequest.getConnection(timeout, TimeUnit.MILLISECONDS);
          } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException();
          } 
          if (HttpConnectionParams.isStaleCheckingEnabled(this.params))
            if (this.managedConn.isOpen()) {
              this.log.debug("Stale connection check");
              if (this.managedConn.isStale()) {
                this.log.debug("Stale connection detected");
                this.managedConn.close();
              } 
            }  
        } 
        if (orig instanceof AbortableHttpRequest)
          ((AbortableHttpRequest)orig).setReleaseTrigger((ConnectionReleaseTrigger)this.managedConn); 
        try {
          tryConnect(roureq, context);
        } catch (TunnelRefusedException ex) {
          if (this.log.isDebugEnabled())
            this.log.debug(ex.getMessage()); 
          response = ex.getResponse();
          break;
        } 
        String userinfo = wrapper.getURI().getUserInfo();
        if (userinfo != null)
          this.targetAuthState.update((AuthScheme)new BasicScheme(), (Credentials)new UsernamePasswordCredentials(userinfo)); 
        if (this.virtualHost != null) {
          target = this.virtualHost;
        } else {
          URI requestURI = wrapper.getURI();
          if (requestURI.isAbsolute())
            target = URIUtils.extractHost(requestURI); 
        } 
        if (target == null)
          target = route.getTargetHost(); 
        wrapper.resetHeaders();
        rewriteRequestURI(wrapper, route);
        context.setAttribute("http.target_host", target);
        context.setAttribute("http.route", route);
        context.setAttribute("http.connection", this.managedConn);
        this.requestExec.preProcess((HttpRequest)wrapper, this.httpProcessor, context);
        response = tryExecute(roureq, context);
        if (response == null)
          continue; 
        response.setParams(this.params);
        this.requestExec.postProcess(response, this.httpProcessor, context);
        reuse = this.reuseStrategy.keepAlive(response, context);
        if (reuse) {
          long duration = this.keepAliveStrategy.getKeepAliveDuration(response, context);
          if (this.log.isDebugEnabled()) {
            String s;
            if (duration > 0L) {
              s = "for " + duration + " " + TimeUnit.MILLISECONDS;
            } else {
              s = "indefinitely";
            } 
            this.log.debug("Connection can be kept alive " + s);
          } 
          this.managedConn.setIdleDuration(duration, TimeUnit.MILLISECONDS);
        } 
        RoutedRequest followup = handleResponse(roureq, response, context);
        if (followup == null) {
          done = true;
        } else {
          if (reuse) {
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
            this.managedConn.markReusable();
          } else {
            this.managedConn.close();
            if (this.proxyAuthState.getState().compareTo((Enum)AuthProtocolState.CHALLENGED) > 0 && this.proxyAuthState.getAuthScheme() != null && this.proxyAuthState.getAuthScheme().isConnectionBased()) {
              this.log.debug("Resetting proxy auth state");
              this.proxyAuthState.reset();
            } 
            if (this.targetAuthState.getState().compareTo((Enum)AuthProtocolState.CHALLENGED) > 0 && this.targetAuthState.getAuthScheme() != null && this.targetAuthState.getAuthScheme().isConnectionBased()) {
              this.log.debug("Resetting target auth state");
              this.targetAuthState.reset();
            } 
          } 
          if (!followup.getRoute().equals(roureq.getRoute()))
            releaseConnection(); 
          roureq = followup;
        } 
        if (this.managedConn != null) {
          if (userToken == null) {
            userToken = this.userTokenHandler.getUserToken(context);
            context.setAttribute("http.user-token", userToken);
          } 
          if (userToken != null)
            this.managedConn.setState(userToken); 
        } 
      } 
      if (response == null || response.getEntity() == null || !response.getEntity().isStreaming()) {
        if (reuse)
          this.managedConn.markReusable(); 
        releaseConnection();
      } else {
        HttpEntity entity = response.getEntity();
        BasicManagedEntity basicManagedEntity = new BasicManagedEntity(entity, this.managedConn, reuse);
        response.setEntity((HttpEntity)basicManagedEntity);
      } 
      return response;
    } catch (ConnectionShutdownException ex) {
      InterruptedIOException ioex = new InterruptedIOException("Connection has been shut down");
      ioex.initCause((Throwable)ex);
      throw ioex;
    } catch (HttpException ex) {
      abortConnection();
      throw ex;
    } catch (IOException ex) {
      abortConnection();
      throw ex;
    } catch (RuntimeException ex) {
      abortConnection();
      throw ex;
    } 
  }
  
  private void tryConnect(RoutedRequest req, HttpContext context) throws HttpException, IOException {
    HttpRoute route = req.getRoute();
    RequestWrapper requestWrapper = req.getRequest();
    int connectCount = 0;
    while (true) {
      context.setAttribute("http.request", requestWrapper);
      connectCount++;
      try {
        if (!this.managedConn.isOpen()) {
          this.managedConn.open(route, context, this.params);
        } else {
          this.managedConn.setSocketTimeout(HttpConnectionParams.getSoTimeout(this.params));
        } 
        establishRoute(route, context);
      } catch (IOException ex) {
        try {
          this.managedConn.close();
        } catch (IOException ignore) {}
        if (this.retryHandler.retryRequest(ex, connectCount, context)) {
          if (this.log.isInfoEnabled()) {
            this.log.info("I/O exception (" + ex.getClass().getName() + ") caught when connecting to " + route + ": " + ex.getMessage());
            if (this.log.isDebugEnabled())
              this.log.debug(ex.getMessage(), ex); 
            this.log.info("Retrying connect to " + route);
          } 
          continue;
        } 
        throw ex;
      } 
      break;
    } 
  }
  
  private HttpResponse tryExecute(RoutedRequest req, HttpContext context) throws HttpException, IOException {
    RequestWrapper wrapper = req.getRequest();
    HttpRoute route = req.getRoute();
    HttpResponse response = null;
    Exception retryReason = null;
    while (true) {
      this.execCount++;
      wrapper.incrementExecCount();
      if (!wrapper.isRepeatable()) {
        this.log.debug("Cannot retry non-repeatable request");
        if (retryReason != null)
          throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.  The cause lists the reason the original request failed.", retryReason); 
        throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.");
      } 
      try {
        if (!this.managedConn.isOpen())
          if (!route.isTunnelled()) {
            this.log.debug("Reopening the direct connection.");
            this.managedConn.open(route, context, this.params);
          } else {
            this.log.debug("Proxied connection. Need to start over.");
            break;
          }  
        if (this.log.isDebugEnabled())
          this.log.debug("Attempt " + this.execCount + " to execute request"); 
        response = this.requestExec.execute((HttpRequest)wrapper, (HttpClientConnection)this.managedConn, context);
      } catch (IOException ex) {
        this.log.debug("Closing the connection.");
        try {
          this.managedConn.close();
        } catch (IOException ignore) {}
        if (this.retryHandler.retryRequest(ex, wrapper.getExecCount(), context)) {
          if (this.log.isInfoEnabled())
            this.log.info("I/O exception (" + ex.getClass().getName() + ") caught when processing request to " + route + ": " + ex.getMessage()); 
          if (this.log.isDebugEnabled())
            this.log.debug(ex.getMessage(), ex); 
          if (this.log.isInfoEnabled())
            this.log.info("Retrying request to " + route); 
          retryReason = ex;
          continue;
        } 
        if (ex instanceof NoHttpResponseException) {
          NoHttpResponseException updatedex = new NoHttpResponseException(route.getTargetHost().toHostString() + " failed to respond");
          updatedex.setStackTrace(ex.getStackTrace());
          throw updatedex;
        } 
        throw ex;
      } 
      break;
    } 
    return response;
  }
  
  protected void releaseConnection() {
    try {
      this.managedConn.releaseConnection();
    } catch (IOException ignored) {
      this.log.debug("IOException releasing connection", ignored);
    } 
    this.managedConn = null;
  }
  
  protected HttpRoute determineRoute(HttpHost targetHost, HttpRequest request, HttpContext context) throws HttpException {
    return this.routePlanner.determineRoute((targetHost != null) ? targetHost : (HttpHost)request.getParams().getParameter("http.default-host"), request, context);
  }
  
  protected void establishRoute(HttpRoute route, HttpContext context) throws HttpException, IOException {
    int step;
    BasicRouteDirector basicRouteDirector = new BasicRouteDirector();
    do {
      boolean secure;
      int hop;
      boolean bool1;
      HttpRoute fact = this.managedConn.getRoute();
      step = basicRouteDirector.nextStep((RouteInfo)route, (RouteInfo)fact);
      switch (step) {
        case 1:
        case 2:
          this.managedConn.open(route, context, this.params);
          break;
        case 3:
          secure = createTunnelToTarget(route, context);
          this.log.debug("Tunnel to target created.");
          this.managedConn.tunnelTarget(secure, this.params);
          break;
        case 4:
          hop = fact.getHopCount() - 1;
          bool1 = createTunnelToProxy(route, hop, context);
          this.log.debug("Tunnel to proxy created.");
          this.managedConn.tunnelProxy(route.getHopTarget(hop), bool1, this.params);
          break;
        case 5:
          this.managedConn.layerProtocol(context, this.params);
          break;
        case -1:
          throw new HttpException("Unable to establish route: planned = " + route + "; current = " + fact);
        case 0:
          break;
        default:
          throw new IllegalStateException("Unknown step indicator " + step + " from RouteDirector.");
      } 
    } while (step > 0);
  }
  
  protected boolean createTunnelToTarget(HttpRoute route, HttpContext context) throws HttpException, IOException {
    HttpHost proxy = route.getProxyHost();
    HttpHost target = route.getTargetHost();
    HttpResponse response = null;
    while (true) {
      if (!this.managedConn.isOpen())
        this.managedConn.open(route, context, this.params); 
      HttpRequest connect = createConnectRequest(route, context);
      connect.setParams(this.params);
      context.setAttribute("http.target_host", target);
      context.setAttribute("http.proxy_host", proxy);
      context.setAttribute("http.connection", this.managedConn);
      context.setAttribute("http.request", connect);
      this.requestExec.preProcess(connect, this.httpProcessor, context);
      response = this.requestExec.execute(connect, (HttpClientConnection)this.managedConn, context);
      response.setParams(this.params);
      this.requestExec.postProcess(response, this.httpProcessor, context);
      int i = response.getStatusLine().getStatusCode();
      if (i < 200)
        throw new HttpException("Unexpected response to CONNECT request: " + response.getStatusLine()); 
      if (HttpClientParams.isAuthenticating(this.params)) {
        if (this.authenticator.isAuthenticationRequested(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, context))
          if (this.authenticator.authenticate(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, context)) {
            if (this.reuseStrategy.keepAlive(response, context)) {
              this.log.debug("Connection kept alive");
              HttpEntity entity = response.getEntity();
              EntityUtils.consume(entity);
              continue;
            } 
            this.managedConn.close();
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
      this.managedConn.close();
      throw new TunnelRefusedException("CONNECT refused by proxy: " + response.getStatusLine(), response);
    } 
    this.managedConn.markReusable();
    return false;
  }
  
  protected boolean createTunnelToProxy(HttpRoute route, int hop, HttpContext context) throws HttpException, IOException {
    throw new HttpException("Proxy chains are not supported.");
  }
  
  protected HttpRequest createConnectRequest(HttpRoute route, HttpContext context) {
    HttpHost target = route.getTargetHost();
    String host = target.getHostName();
    int port = target.getPort();
    if (port < 0) {
      Scheme scheme = this.connManager.getSchemeRegistry().getScheme(target.getSchemeName());
      port = scheme.getDefaultPort();
    } 
    StringBuilder buffer = new StringBuilder(host.length() + 6);
    buffer.append(host);
    buffer.append(':');
    buffer.append(Integer.toString(port));
    String authority = buffer.toString();
    ProtocolVersion ver = HttpProtocolParams.getVersion(this.params);
    return (HttpRequest)new BasicHttpRequest("CONNECT", authority, ver);
  }
  
  protected RoutedRequest handleResponse(RoutedRequest roureq, HttpResponse response, HttpContext context) throws HttpException, IOException {
    HttpRoute route = roureq.getRoute();
    RequestWrapper request = roureq.getRequest();
    HttpParams params = request.getParams();
    if (HttpClientParams.isAuthenticating(params)) {
      HttpHost target = (HttpHost)context.getAttribute("http.target_host");
      if (target == null)
        target = route.getTargetHost(); 
      if (target.getPort() < 0) {
        Scheme scheme = this.connManager.getSchemeRegistry().getScheme(target);
        target = new HttpHost(target.getHostName(), scheme.getDefaultPort(), target.getSchemeName());
      } 
      boolean targetAuthRequested = this.authenticator.isAuthenticationRequested(target, response, this.targetAuthStrategy, this.targetAuthState, context);
      HttpHost proxy = route.getProxyHost();
      if (proxy == null)
        proxy = route.getTargetHost(); 
      boolean proxyAuthRequested = this.authenticator.isAuthenticationRequested(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, context);
      if (targetAuthRequested && 
        this.authenticator.authenticate(target, response, this.targetAuthStrategy, this.targetAuthState, context))
        return roureq; 
      if (proxyAuthRequested && 
        this.authenticator.authenticate(proxy, response, this.proxyAuthStrategy, this.proxyAuthState, context))
        return roureq; 
    } 
    if (HttpClientParams.isRedirecting(params) && this.redirectStrategy.isRedirected((HttpRequest)request, response, context)) {
      if (this.redirectCount >= this.maxRedirects)
        throw new RedirectException("Maximum redirects (" + this.maxRedirects + ") exceeded"); 
      this.redirectCount++;
      this.virtualHost = null;
      HttpUriRequest redirect = this.redirectStrategy.getRedirect((HttpRequest)request, response, context);
      HttpRequest orig = request.getOriginal();
      redirect.setHeaders(orig.getAllHeaders());
      URI uri = redirect.getURI();
      HttpHost newTarget = URIUtils.extractHost(uri);
      if (newTarget == null)
        throw new ProtocolException("Redirect URI does not specify a valid host name: " + uri); 
      if (!route.getTargetHost().equals(newTarget)) {
        this.log.debug("Resetting target auth state");
        this.targetAuthState.reset();
        AuthScheme authScheme = this.proxyAuthState.getAuthScheme();
        if (authScheme != null && authScheme.isConnectionBased()) {
          this.log.debug("Resetting proxy auth state");
          this.proxyAuthState.reset();
        } 
      } 
      RequestWrapper wrapper = wrapRequest((HttpRequest)redirect);
      wrapper.setParams(params);
      HttpRoute newRoute = determineRoute(newTarget, (HttpRequest)wrapper, context);
      RoutedRequest newRequest = new RoutedRequest(wrapper, newRoute);
      if (this.log.isDebugEnabled())
        this.log.debug("Redirecting to '" + uri + "' via " + newRoute); 
      return newRequest;
    } 
    return null;
  }
  
  private void abortConnection() {
    ManagedClientConnection mcc = this.managedConn;
    if (mcc != null) {
      this.managedConn = null;
      try {
        mcc.abortConnection();
      } catch (IOException ex) {
        if (this.log.isDebugEnabled())
          this.log.debug(ex.getMessage(), ex); 
      } 
      try {
        mcc.releaseConnection();
      } catch (IOException ignored) {
        this.log.debug("Error releasing connection", ignored);
      } 
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\DefaultRequestDirector.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */