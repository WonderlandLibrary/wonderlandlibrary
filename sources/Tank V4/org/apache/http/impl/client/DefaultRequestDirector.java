package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
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
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.BasicRouteDirector;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
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

/** @deprecated */
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
   /** @deprecated */
   @Deprecated
   protected final RedirectHandler redirectHandler;
   protected final RedirectStrategy redirectStrategy;
   /** @deprecated */
   @Deprecated
   protected final AuthenticationHandler targetAuthHandler;
   protected final AuthenticationStrategy targetAuthStrategy;
   /** @deprecated */
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

   /** @deprecated */
   @Deprecated
   public DefaultRequestDirector(HttpRequestExecutor var1, ClientConnectionManager var2, ConnectionReuseStrategy var3, ConnectionKeepAliveStrategy var4, HttpRoutePlanner var5, HttpProcessor var6, HttpRequestRetryHandler var7, RedirectHandler var8, AuthenticationHandler var9, AuthenticationHandler var10, UserTokenHandler var11, HttpParams var12) {
      this(LogFactory.getLog(DefaultRequestDirector.class), var1, var2, var3, var4, var5, var6, var7, new DefaultRedirectStrategyAdaptor(var8), (AuthenticationStrategy)(new AuthenticationStrategyAdaptor(var9)), (AuthenticationStrategy)(new AuthenticationStrategyAdaptor(var10)), var11, var12);
   }

   /** @deprecated */
   @Deprecated
   public DefaultRequestDirector(Log var1, HttpRequestExecutor var2, ClientConnectionManager var3, ConnectionReuseStrategy var4, ConnectionKeepAliveStrategy var5, HttpRoutePlanner var6, HttpProcessor var7, HttpRequestRetryHandler var8, RedirectStrategy var9, AuthenticationHandler var10, AuthenticationHandler var11, UserTokenHandler var12, HttpParams var13) {
      this(LogFactory.getLog(DefaultRequestDirector.class), var2, var3, var4, var5, var6, var7, var8, var9, (AuthenticationStrategy)(new AuthenticationStrategyAdaptor(var10)), (AuthenticationStrategy)(new AuthenticationStrategyAdaptor(var11)), var12, var13);
   }

   public DefaultRequestDirector(Log var1, HttpRequestExecutor var2, ClientConnectionManager var3, ConnectionReuseStrategy var4, ConnectionKeepAliveStrategy var5, HttpRoutePlanner var6, HttpProcessor var7, HttpRequestRetryHandler var8, RedirectStrategy var9, AuthenticationStrategy var10, AuthenticationStrategy var11, UserTokenHandler var12, HttpParams var13) {
      Args.notNull(var1, "Log");
      Args.notNull(var2, "Request executor");
      Args.notNull(var3, "Client connection manager");
      Args.notNull(var4, "Connection reuse strategy");
      Args.notNull(var5, "Connection keep alive strategy");
      Args.notNull(var6, "Route planner");
      Args.notNull(var7, "HTTP protocol processor");
      Args.notNull(var8, "HTTP request retry handler");
      Args.notNull(var9, "Redirect strategy");
      Args.notNull(var10, "Target authentication strategy");
      Args.notNull(var11, "Proxy authentication strategy");
      Args.notNull(var12, "User token handler");
      Args.notNull(var13, "HTTP parameters");
      this.log = var1;
      this.authenticator = new HttpAuthenticator(var1);
      this.requestExec = var2;
      this.connManager = var3;
      this.reuseStrategy = var4;
      this.keepAliveStrategy = var5;
      this.routePlanner = var6;
      this.httpProcessor = var7;
      this.retryHandler = var8;
      this.redirectStrategy = var9;
      this.targetAuthStrategy = var10;
      this.proxyAuthStrategy = var11;
      this.userTokenHandler = var12;
      this.params = var13;
      if (var9 instanceof DefaultRedirectStrategyAdaptor) {
         this.redirectHandler = ((DefaultRedirectStrategyAdaptor)var9).getHandler();
      } else {
         this.redirectHandler = null;
      }

      if (var10 instanceof AuthenticationStrategyAdaptor) {
         this.targetAuthHandler = ((AuthenticationStrategyAdaptor)var10).getHandler();
      } else {
         this.targetAuthHandler = null;
      }

      if (var11 instanceof AuthenticationStrategyAdaptor) {
         this.proxyAuthHandler = ((AuthenticationStrategyAdaptor)var11).getHandler();
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

   private RequestWrapper wrapRequest(HttpRequest var1) throws ProtocolException {
      return (RequestWrapper)(var1 instanceof HttpEntityEnclosingRequest ? new EntityEnclosingRequestWrapper((HttpEntityEnclosingRequest)var1) : new RequestWrapper(var1));
   }

   protected void rewriteRequestURI(RequestWrapper var1, HttpRoute var2) throws ProtocolException {
      try {
         URI var3 = var1.getURI();
         if (var2.getProxyHost() != null && !var2.isTunnelled()) {
            if (!var3.isAbsolute()) {
               HttpHost var4 = var2.getTargetHost();
               var3 = URIUtils.rewriteURI(var3, var4, true);
            } else {
               var3 = URIUtils.rewriteURI(var3);
            }
         } else if (var3.isAbsolute()) {
            var3 = URIUtils.rewriteURI(var3, (HttpHost)null, true);
         } else {
            var3 = URIUtils.rewriteURI(var3);
         }

         var1.setURI(var3);
      } catch (URISyntaxException var5) {
         throw new ProtocolException("Invalid URI: " + var1.getRequestLine().getUri(), var5);
      }
   }

   public HttpResponse execute(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException, IOException {
      var3.setAttribute("http.auth.target-scope", this.targetAuthState);
      var3.setAttribute("http.auth.proxy-scope", this.proxyAuthState);
      HttpHost var4 = var1;
      HttpRequest var5 = var2;
      RequestWrapper var6 = this.wrapRequest(var2);
      var6.setParams(this.params);
      HttpRoute var7 = this.determineRoute(var1, var6, var3);
      this.virtualHost = (HttpHost)var6.getParams().getParameter("http.virtual-host");
      if (this.virtualHost != null && this.virtualHost.getPort() == -1) {
         HttpHost var8 = var1 != null ? var1 : var7.getTargetHost();
         int var9 = var8.getPort();
         if (var9 != -1) {
            this.virtualHost = new HttpHost(this.virtualHost.getHostName(), var9, this.virtualHost.getSchemeName());
         }
      }

      RoutedRequest var25 = new RoutedRequest(var6, var7);
      boolean var26 = false;
      boolean var10 = false;

      try {
         HttpResponse var11 = null;

         while(!var10) {
            RequestWrapper var27 = var25.getRequest();
            HttpRoute var13 = var25.getRoute();
            var11 = null;
            Object var14 = var3.getAttribute("http.user-token");
            long var16;
            if (this.managedConn == null) {
               ClientConnectionRequest var15 = this.connManager.requestConnection(var13, var14);
               if (var5 instanceof AbortableHttpRequest) {
                  ((AbortableHttpRequest)var5).setConnectionRequest(var15);
               }

               var16 = HttpClientParams.getConnectionManagerTimeout(this.params);

               try {
                  this.managedConn = var15.getConnection(var16, TimeUnit.MILLISECONDS);
               } catch (InterruptedException var19) {
                  Thread.currentThread().interrupt();
                  throw new InterruptedIOException();
               }

               if (HttpConnectionParams.isStaleCheckingEnabled(this.params) && this.managedConn.isOpen()) {
                  this.log.debug("Stale connection check");
                  if (this.managedConn.isStale()) {
                     this.log.debug("Stale connection detected");
                     this.managedConn.close();
                  }
               }
            }

            if (var5 instanceof AbortableHttpRequest) {
               ((AbortableHttpRequest)var5).setReleaseTrigger(this.managedConn);
            }

            try {
               this.tryConnect(var25, var3);
            } catch (TunnelRefusedException var20) {
               if (this.log.isDebugEnabled()) {
                  this.log.debug(var20.getMessage());
               }

               var11 = var20.getResponse();
               break;
            }

            String var30 = var27.getURI().getUserInfo();
            if (var30 != null) {
               this.targetAuthState.update(new BasicScheme(), new UsernamePasswordCredentials(var30));
            }

            if (this.virtualHost != null) {
               var4 = this.virtualHost;
            } else {
               URI var31 = var27.getURI();
               if (var31.isAbsolute()) {
                  var4 = URIUtils.extractHost(var31);
               }
            }

            if (var4 == null) {
               var4 = var13.getTargetHost();
            }

            var27.resetHeaders();
            this.rewriteRequestURI(var27, var13);
            var3.setAttribute("http.target_host", var4);
            var3.setAttribute("http.route", var13);
            var3.setAttribute("http.connection", this.managedConn);
            this.requestExec.preProcess(var27, this.httpProcessor, var3);
            var11 = this.tryExecute(var25, var3);
            if (var11 != null) {
               var11.setParams(this.params);
               this.requestExec.postProcess(var11, this.httpProcessor, var3);
               var26 = this.reuseStrategy.keepAlive(var11, var3);
               if (var26) {
                  var16 = this.keepAliveStrategy.getKeepAliveDuration(var11, var3);
                  if (this.log.isDebugEnabled()) {
                     String var18;
                     if (var16 > 0L) {
                        var18 = "for " + var16 + " " + TimeUnit.MILLISECONDS;
                     } else {
                        var18 = "indefinitely";
                     }

                     this.log.debug("Connection can be kept alive " + var18);
                  }

                  this.managedConn.setIdleDuration(var16, TimeUnit.MILLISECONDS);
               }

               RoutedRequest var32 = this.handleResponse(var25, var11, var3);
               if (var32 == null) {
                  var10 = true;
               } else {
                  if (var26) {
                     HttpEntity var17 = var11.getEntity();
                     EntityUtils.consume(var17);
                     this.managedConn.markReusable();
                  } else {
                     this.managedConn.close();
                     if (this.proxyAuthState.getState().compareTo(AuthProtocolState.CHALLENGED) > 0 && this.proxyAuthState.getAuthScheme() != null && this.proxyAuthState.getAuthScheme().isConnectionBased()) {
                        this.log.debug("Resetting proxy auth state");
                        this.proxyAuthState.reset();
                     }

                     if (this.targetAuthState.getState().compareTo(AuthProtocolState.CHALLENGED) > 0 && this.targetAuthState.getAuthScheme() != null && this.targetAuthState.getAuthScheme().isConnectionBased()) {
                        this.log.debug("Resetting target auth state");
                        this.targetAuthState.reset();
                     }
                  }

                  if (!var32.getRoute().equals(var25.getRoute())) {
                     this.releaseConnection();
                  }

                  var25 = var32;
               }

               if (this.managedConn != null) {
                  if (var14 == null) {
                     var14 = this.userTokenHandler.getUserToken(var3);
                     var3.setAttribute("http.user-token", var14);
                  }

                  if (var14 != null) {
                     this.managedConn.setState(var14);
                  }
               }
            }
         }

         if (var11 != null && var11.getEntity() != null && var11.getEntity().isStreaming()) {
            HttpEntity var28 = var11.getEntity();
            BasicManagedEntity var29 = new BasicManagedEntity(var28, this.managedConn, var26);
            var11.setEntity(var29);
         } else {
            if (var26) {
               this.managedConn.markReusable();
            }

            this.releaseConnection();
         }

         return var11;
      } catch (ConnectionShutdownException var21) {
         InterruptedIOException var12 = new InterruptedIOException("Connection has been shut down");
         var12.initCause(var21);
         throw var12;
      } catch (HttpException var22) {
         this.abortConnection();
         throw var22;
      } catch (IOException var23) {
         this.abortConnection();
         throw var23;
      } catch (RuntimeException var24) {
         this.abortConnection();
         throw var24;
      }
   }

   private void tryConnect(RoutedRequest var1, HttpContext var2) throws HttpException, IOException {
      HttpRoute var3 = var1.getRoute();
      RequestWrapper var4 = var1.getRequest();
      int var5 = 0;

      while(true) {
         var2.setAttribute("http.request", var4);
         ++var5;

         try {
            if (!this.managedConn.isOpen()) {
               this.managedConn.open(var3, var2, this.params);
            } else {
               this.managedConn.setSocketTimeout(HttpConnectionParams.getSoTimeout(this.params));
            }

            this.establishRoute(var3, var2);
            return;
         } catch (IOException var9) {
            try {
               this.managedConn.close();
            } catch (IOException var8) {
            }

            if (!this.retryHandler.retryRequest(var9, var5, var2)) {
               throw var9;
            }

            if (this.log.isInfoEnabled()) {
               this.log.info("I/O exception (" + var9.getClass().getName() + ") caught when connecting to " + var3 + ": " + var9.getMessage());
               if (this.log.isDebugEnabled()) {
                  this.log.debug(var9.getMessage(), var9);
               }

               this.log.info("Retrying connect to " + var3);
            }
         }
      }
   }

   private HttpResponse tryExecute(RoutedRequest var1, HttpContext var2) throws HttpException, IOException {
      RequestWrapper var3 = var1.getRequest();
      HttpRoute var4 = var1.getRoute();
      HttpResponse var5 = null;
      IOException var6 = null;

      while(true) {
         ++this.execCount;
         var3.incrementExecCount();
         if (!var3.isRepeatable()) {
            this.log.debug("Cannot retry non-repeatable request");
            if (var6 != null) {
               throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.  The cause lists the reason the original request failed.", var6);
            }

            throw new NonRepeatableRequestException("Cannot retry request with a non-repeatable request entity.");
         }

         try {
            if (!this.managedConn.isOpen()) {
               if (var4.isTunnelled()) {
                  this.log.debug("Proxied connection. Need to start over.");
                  return var5;
               }

               this.log.debug("Reopening the direct connection.");
               this.managedConn.open(var4, var2, this.params);
            }

            if (this.log.isDebugEnabled()) {
               this.log.debug("Attempt " + this.execCount + " to execute request");
            }

            var5 = this.requestExec.execute(var3, this.managedConn, var2);
            return var5;
         } catch (IOException var10) {
            this.log.debug("Closing the connection.");

            try {
               this.managedConn.close();
            } catch (IOException var9) {
            }

            if (!this.retryHandler.retryRequest(var10, var3.getExecCount(), var2)) {
               if (var10 instanceof NoHttpResponseException) {
                  NoHttpResponseException var8 = new NoHttpResponseException(var4.getTargetHost().toHostString() + " failed to respond");
                  var8.setStackTrace(var10.getStackTrace());
                  throw var8;
               }

               throw var10;
            }

            if (this.log.isInfoEnabled()) {
               this.log.info("I/O exception (" + var10.getClass().getName() + ") caught when processing request to " + var4 + ": " + var10.getMessage());
            }

            if (this.log.isDebugEnabled()) {
               this.log.debug(var10.getMessage(), var10);
            }

            if (this.log.isInfoEnabled()) {
               this.log.info("Retrying request to " + var4);
            }

            var6 = var10;
         }
      }
   }

   protected void releaseConnection() {
      try {
         this.managedConn.releaseConnection();
      } catch (IOException var2) {
         this.log.debug("IOException releasing connection", var2);
      }

      this.managedConn = null;
   }

   protected HttpRoute determineRoute(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException {
      return this.routePlanner.determineRoute(var1 != null ? var1 : (HttpHost)var2.getParams().getParameter("http.default-host"), var2, var3);
   }

   protected void establishRoute(HttpRoute var1, HttpContext var2) throws HttpException, IOException {
      BasicRouteDirector var3 = new BasicRouteDirector();

      int var4;
      do {
         HttpRoute var5 = this.managedConn.getRoute();
         var4 = var3.nextStep(var1, var5);
         switch(var4) {
         case -1:
            throw new HttpException("Unable to establish route: planned = " + var1 + "; current = " + var5);
         case 0:
            break;
         case 1:
         case 2:
            this.managedConn.open(var1, var2, this.params);
            break;
         case 3:
            boolean var8 = this.createTunnelToTarget(var1, var2);
            this.log.debug("Tunnel to target created.");
            this.managedConn.tunnelTarget(var8, this.params);
            break;
         case 4:
            int var6 = var5.getHopCount() - 1;
            boolean var7 = this.createTunnelToProxy(var1, var6, var2);
            this.log.debug("Tunnel to proxy created.");
            this.managedConn.tunnelProxy(var1.getHopTarget(var6), var7, this.params);
            break;
         case 5:
            this.managedConn.layerProtocol(var2, this.params);
            break;
         default:
            throw new IllegalStateException("Unknown step indicator " + var4 + " from RouteDirector.");
         }
      } while(var4 > 0);

   }

   protected boolean createTunnelToTarget(HttpRoute var1, HttpContext var2) throws HttpException, IOException {
      HttpHost var3 = var1.getProxyHost();
      HttpHost var4 = var1.getTargetHost();
      HttpResponse var5 = null;

      while(true) {
         do {
            if (!this.managedConn.isOpen()) {
               this.managedConn.open(var1, var2, this.params);
            }

            HttpRequest var6 = this.createConnectRequest(var1, var2);
            var6.setParams(this.params);
            var2.setAttribute("http.target_host", var4);
            var2.setAttribute("http.proxy_host", var3);
            var2.setAttribute("http.connection", this.managedConn);
            var2.setAttribute("http.request", var6);
            this.requestExec.preProcess(var6, this.httpProcessor, var2);
            var5 = this.requestExec.execute(var6, this.managedConn, var2);
            var5.setParams(this.params);
            this.requestExec.postProcess(var5, this.httpProcessor, var2);
            int var7 = var5.getStatusLine().getStatusCode();
            if (var7 < 200) {
               throw new HttpException("Unexpected response to CONNECT request: " + var5.getStatusLine());
            }
         } while(!HttpClientParams.isAuthenticating(this.params));

         if (!this.authenticator.isAuthenticationRequested(var3, var5, this.proxyAuthStrategy, this.proxyAuthState, var2) || !this.authenticator.authenticate(var3, var5, this.proxyAuthStrategy, this.proxyAuthState, var2)) {
            int var9 = var5.getStatusLine().getStatusCode();
            if (var9 > 299) {
               HttpEntity var10 = var5.getEntity();
               if (var10 != null) {
                  var5.setEntity(new BufferedHttpEntity(var10));
               }

               this.managedConn.close();
               throw new TunnelRefusedException("CONNECT refused by proxy: " + var5.getStatusLine(), var5);
            } else {
               this.managedConn.markReusable();
               return false;
            }
         }

         if (this.reuseStrategy.keepAlive(var5, var2)) {
            this.log.debug("Connection kept alive");
            HttpEntity var8 = var5.getEntity();
            EntityUtils.consume(var8);
         } else {
            this.managedConn.close();
         }
      }
   }

   protected boolean createTunnelToProxy(HttpRoute var1, int var2, HttpContext var3) throws HttpException, IOException {
      throw new HttpException("Proxy chains are not supported.");
   }

   protected HttpRequest createConnectRequest(HttpRoute var1, HttpContext var2) {
      HttpHost var3 = var1.getTargetHost();
      String var4 = var3.getHostName();
      int var5 = var3.getPort();
      if (var5 < 0) {
         Scheme var6 = this.connManager.getSchemeRegistry().getScheme(var3.getSchemeName());
         var5 = var6.getDefaultPort();
      }

      StringBuilder var10 = new StringBuilder(var4.length() + 6);
      var10.append(var4);
      var10.append(':');
      var10.append(Integer.toString(var5));
      String var7 = var10.toString();
      ProtocolVersion var8 = HttpProtocolParams.getVersion(this.params);
      BasicHttpRequest var9 = new BasicHttpRequest("CONNECT", var7, var8);
      return var9;
   }

   protected RoutedRequest handleResponse(RoutedRequest var1, HttpResponse var2, HttpContext var3) throws HttpException, IOException {
      HttpRoute var4 = var1.getRoute();
      RequestWrapper var5 = var1.getRequest();
      HttpParams var6 = var5.getParams();
      if (HttpClientParams.isAuthenticating(var6)) {
         HttpHost var7 = (HttpHost)var3.getAttribute("http.target_host");
         if (var7 == null) {
            var7 = var4.getTargetHost();
         }

         if (var7.getPort() < 0) {
            Scheme var8 = this.connManager.getSchemeRegistry().getScheme(var7);
            var7 = new HttpHost(var7.getHostName(), var8.getDefaultPort(), var7.getSchemeName());
         }

         boolean var14 = this.authenticator.isAuthenticationRequested(var7, var2, this.targetAuthStrategy, this.targetAuthState, var3);
         HttpHost var9 = var4.getProxyHost();
         if (var9 == null) {
            var9 = var4.getTargetHost();
         }

         boolean var10 = this.authenticator.isAuthenticationRequested(var9, var2, this.proxyAuthStrategy, this.proxyAuthState, var3);
         if (var14 && this.authenticator.authenticate(var7, var2, this.targetAuthStrategy, this.targetAuthState, var3)) {
            return var1;
         }

         if (var10 && this.authenticator.authenticate(var9, var2, this.proxyAuthStrategy, this.proxyAuthState, var3)) {
            return var1;
         }
      }

      if (HttpClientParams.isRedirecting(var6) && this.redirectStrategy.isRedirected(var5, var2, var3)) {
         if (this.redirectCount >= this.maxRedirects) {
            throw new RedirectException("Maximum redirects (" + this.maxRedirects + ") exceeded");
         } else {
            ++this.redirectCount;
            this.virtualHost = null;
            HttpUriRequest var15 = this.redirectStrategy.getRedirect(var5, var2, var3);
            HttpRequest var16 = var5.getOriginal();
            var15.setHeaders(var16.getAllHeaders());
            URI var17 = var15.getURI();
            HttpHost var18 = URIUtils.extractHost(var17);
            if (var18 == null) {
               throw new ProtocolException("Redirect URI does not specify a valid host name: " + var17);
            } else {
               if (!var4.getTargetHost().equals(var18)) {
                  this.log.debug("Resetting target auth state");
                  this.targetAuthState.reset();
                  AuthScheme var11 = this.proxyAuthState.getAuthScheme();
                  if (var11 != null && var11.isConnectionBased()) {
                     this.log.debug("Resetting proxy auth state");
                     this.proxyAuthState.reset();
                  }
               }

               RequestWrapper var19 = this.wrapRequest(var15);
               var19.setParams(var6);
               HttpRoute var12 = this.determineRoute(var18, var19, var3);
               RoutedRequest var13 = new RoutedRequest(var19, var12);
               if (this.log.isDebugEnabled()) {
                  this.log.debug("Redirecting to '" + var17 + "' via " + var12);
               }

               return var13;
            }
         }
      } else {
         return null;
      }
   }

   private void abortConnection() {
      ManagedClientConnection var1 = this.managedConn;
      if (var1 != null) {
         this.managedConn = null;

         try {
            var1.abortConnection();
         } catch (IOException var4) {
            if (this.log.isDebugEnabled()) {
               this.log.debug(var4.getMessage(), var4);
            }
         }

         try {
            var1.releaseConnection();
         } catch (IOException var3) {
            this.log.debug("Error releasing connection", var3);
         }
      }

   }
}
