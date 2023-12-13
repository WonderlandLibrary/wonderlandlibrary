package org.apache.http.impl.client;

import java.io.IOException;
import java.lang.reflect.UndeclaredThrowableException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.BackoffManager;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.RequestDirector;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.params.HttpClientParamConfig;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionManagerFactory;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.CookieSpecRegistry;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.impl.conn.DefaultHttpRoutePlanner;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.impl.cookie.IgnoreSpecFactory;
import org.apache.http.impl.cookie.NetscapeDraftSpecFactory;
import org.apache.http.impl.cookie.RFC2109SpecFactory;
import org.apache.http.impl.cookie.RFC2965SpecFactory;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.DefaultedHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.util.Args;

/** @deprecated */
@Deprecated
@ThreadSafe
public abstract class AbstractHttpClient extends CloseableHttpClient {
   private final Log log = LogFactory.getLog(this.getClass());
   @GuardedBy("this")
   private HttpParams defaultParams;
   @GuardedBy("this")
   private HttpRequestExecutor requestExec;
   @GuardedBy("this")
   private ClientConnectionManager connManager;
   @GuardedBy("this")
   private ConnectionReuseStrategy reuseStrategy;
   @GuardedBy("this")
   private ConnectionKeepAliveStrategy keepAliveStrategy;
   @GuardedBy("this")
   private CookieSpecRegistry supportedCookieSpecs;
   @GuardedBy("this")
   private AuthSchemeRegistry supportedAuthSchemes;
   @GuardedBy("this")
   private BasicHttpProcessor mutableProcessor;
   @GuardedBy("this")
   private ImmutableHttpProcessor protocolProcessor;
   @GuardedBy("this")
   private HttpRequestRetryHandler retryHandler;
   @GuardedBy("this")
   private RedirectStrategy redirectStrategy;
   @GuardedBy("this")
   private AuthenticationStrategy targetAuthStrategy;
   @GuardedBy("this")
   private AuthenticationStrategy proxyAuthStrategy;
   @GuardedBy("this")
   private CookieStore cookieStore;
   @GuardedBy("this")
   private CredentialsProvider credsProvider;
   @GuardedBy("this")
   private HttpRoutePlanner routePlanner;
   @GuardedBy("this")
   private UserTokenHandler userTokenHandler;
   @GuardedBy("this")
   private ConnectionBackoffStrategy connectionBackoffStrategy;
   @GuardedBy("this")
   private BackoffManager backoffManager;

   protected AbstractHttpClient(ClientConnectionManager var1, HttpParams var2) {
      this.defaultParams = var2;
      this.connManager = var1;
   }

   protected abstract HttpParams createHttpParams();

   protected abstract BasicHttpProcessor createHttpProcessor();

   protected HttpContext createHttpContext() {
      BasicHttpContext var1 = new BasicHttpContext();
      var1.setAttribute("http.scheme-registry", this.getConnectionManager().getSchemeRegistry());
      var1.setAttribute("http.authscheme-registry", this.getAuthSchemes());
      var1.setAttribute("http.cookiespec-registry", this.getCookieSpecs());
      var1.setAttribute("http.cookie-store", this.getCookieStore());
      var1.setAttribute("http.auth.credentials-provider", this.getCredentialsProvider());
      return var1;
   }

   protected ClientConnectionManager createClientConnectionManager() {
      SchemeRegistry var1 = SchemeRegistryFactory.createDefault();
      Object var2 = null;
      HttpParams var3 = this.getParams();
      ClientConnectionManagerFactory var4 = null;
      String var5 = (String)var3.getParameter("http.connection-manager.factory-class-name");
      if (var5 != null) {
         try {
            Class var6 = Class.forName(var5);
            var4 = (ClientConnectionManagerFactory)var6.newInstance();
         } catch (ClassNotFoundException var7) {
            throw new IllegalStateException("Invalid class name: " + var5);
         } catch (IllegalAccessException var8) {
            throw new IllegalAccessError(var8.getMessage());
         } catch (InstantiationException var9) {
            throw new InstantiationError(var9.getMessage());
         }
      }

      if (var4 != null) {
         var2 = var4.newInstance(var3, var1);
      } else {
         var2 = new BasicClientConnectionManager(var1);
      }

      return (ClientConnectionManager)var2;
   }

   protected AuthSchemeRegistry createAuthSchemeRegistry() {
      AuthSchemeRegistry var1 = new AuthSchemeRegistry();
      var1.register("Basic", new BasicSchemeFactory());
      var1.register("Digest", new DigestSchemeFactory());
      var1.register("NTLM", new NTLMSchemeFactory());
      var1.register("negotiate", new SPNegoSchemeFactory());
      var1.register("Kerberos", new KerberosSchemeFactory());
      return var1;
   }

   protected CookieSpecRegistry createCookieSpecRegistry() {
      CookieSpecRegistry var1 = new CookieSpecRegistry();
      var1.register("best-match", new BestMatchSpecFactory());
      var1.register("compatibility", new BrowserCompatSpecFactory());
      var1.register("netscape", new NetscapeDraftSpecFactory());
      var1.register("rfc2109", new RFC2109SpecFactory());
      var1.register("rfc2965", new RFC2965SpecFactory());
      var1.register("ignoreCookies", new IgnoreSpecFactory());
      return var1;
   }

   protected HttpRequestExecutor createRequestExecutor() {
      return new HttpRequestExecutor();
   }

   protected ConnectionReuseStrategy createConnectionReuseStrategy() {
      return new DefaultConnectionReuseStrategy();
   }

   protected ConnectionKeepAliveStrategy createConnectionKeepAliveStrategy() {
      return new DefaultConnectionKeepAliveStrategy();
   }

   protected HttpRequestRetryHandler createHttpRequestRetryHandler() {
      return new DefaultHttpRequestRetryHandler();
   }

   /** @deprecated */
   @Deprecated
   protected RedirectHandler createRedirectHandler() {
      return new DefaultRedirectHandler();
   }

   protected AuthenticationStrategy createTargetAuthenticationStrategy() {
      return new TargetAuthenticationStrategy();
   }

   /** @deprecated */
   @Deprecated
   protected AuthenticationHandler createTargetAuthenticationHandler() {
      return new DefaultTargetAuthenticationHandler();
   }

   protected AuthenticationStrategy createProxyAuthenticationStrategy() {
      return new ProxyAuthenticationStrategy();
   }

   /** @deprecated */
   @Deprecated
   protected AuthenticationHandler createProxyAuthenticationHandler() {
      return new DefaultProxyAuthenticationHandler();
   }

   protected CookieStore createCookieStore() {
      return new BasicCookieStore();
   }

   protected CredentialsProvider createCredentialsProvider() {
      return new BasicCredentialsProvider();
   }

   protected HttpRoutePlanner createHttpRoutePlanner() {
      return new DefaultHttpRoutePlanner(this.getConnectionManager().getSchemeRegistry());
   }

   protected UserTokenHandler createUserTokenHandler() {
      return new DefaultUserTokenHandler();
   }

   public final synchronized HttpParams getParams() {
      if (this.defaultParams == null) {
         this.defaultParams = this.createHttpParams();
      }

      return this.defaultParams;
   }

   public synchronized void setParams(HttpParams var1) {
      this.defaultParams = var1;
   }

   public final synchronized ClientConnectionManager getConnectionManager() {
      if (this.connManager == null) {
         this.connManager = this.createClientConnectionManager();
      }

      return this.connManager;
   }

   public final synchronized HttpRequestExecutor getRequestExecutor() {
      if (this.requestExec == null) {
         this.requestExec = this.createRequestExecutor();
      }

      return this.requestExec;
   }

   public final synchronized AuthSchemeRegistry getAuthSchemes() {
      if (this.supportedAuthSchemes == null) {
         this.supportedAuthSchemes = this.createAuthSchemeRegistry();
      }

      return this.supportedAuthSchemes;
   }

   public synchronized void setAuthSchemes(AuthSchemeRegistry var1) {
      this.supportedAuthSchemes = var1;
   }

   public final synchronized ConnectionBackoffStrategy getConnectionBackoffStrategy() {
      return this.connectionBackoffStrategy;
   }

   public synchronized void setConnectionBackoffStrategy(ConnectionBackoffStrategy var1) {
      this.connectionBackoffStrategy = var1;
   }

   public final synchronized CookieSpecRegistry getCookieSpecs() {
      if (this.supportedCookieSpecs == null) {
         this.supportedCookieSpecs = this.createCookieSpecRegistry();
      }

      return this.supportedCookieSpecs;
   }

   public final synchronized BackoffManager getBackoffManager() {
      return this.backoffManager;
   }

   public synchronized void setBackoffManager(BackoffManager var1) {
      this.backoffManager = var1;
   }

   public synchronized void setCookieSpecs(CookieSpecRegistry var1) {
      this.supportedCookieSpecs = var1;
   }

   public final synchronized ConnectionReuseStrategy getConnectionReuseStrategy() {
      if (this.reuseStrategy == null) {
         this.reuseStrategy = this.createConnectionReuseStrategy();
      }

      return this.reuseStrategy;
   }

   public synchronized void setReuseStrategy(ConnectionReuseStrategy var1) {
      this.reuseStrategy = var1;
   }

   public final synchronized ConnectionKeepAliveStrategy getConnectionKeepAliveStrategy() {
      if (this.keepAliveStrategy == null) {
         this.keepAliveStrategy = this.createConnectionKeepAliveStrategy();
      }

      return this.keepAliveStrategy;
   }

   public synchronized void setKeepAliveStrategy(ConnectionKeepAliveStrategy var1) {
      this.keepAliveStrategy = var1;
   }

   public final synchronized HttpRequestRetryHandler getHttpRequestRetryHandler() {
      if (this.retryHandler == null) {
         this.retryHandler = this.createHttpRequestRetryHandler();
      }

      return this.retryHandler;
   }

   public synchronized void setHttpRequestRetryHandler(HttpRequestRetryHandler var1) {
      this.retryHandler = var1;
   }

   /** @deprecated */
   @Deprecated
   public final synchronized RedirectHandler getRedirectHandler() {
      return this.createRedirectHandler();
   }

   /** @deprecated */
   @Deprecated
   public synchronized void setRedirectHandler(RedirectHandler var1) {
      this.redirectStrategy = new DefaultRedirectStrategyAdaptor(var1);
   }

   public final synchronized RedirectStrategy getRedirectStrategy() {
      if (this.redirectStrategy == null) {
         this.redirectStrategy = new DefaultRedirectStrategy();
      }

      return this.redirectStrategy;
   }

   public synchronized void setRedirectStrategy(RedirectStrategy var1) {
      this.redirectStrategy = var1;
   }

   /** @deprecated */
   @Deprecated
   public final synchronized AuthenticationHandler getTargetAuthenticationHandler() {
      return this.createTargetAuthenticationHandler();
   }

   /** @deprecated */
   @Deprecated
   public synchronized void setTargetAuthenticationHandler(AuthenticationHandler var1) {
      this.targetAuthStrategy = new AuthenticationStrategyAdaptor(var1);
   }

   public final synchronized AuthenticationStrategy getTargetAuthenticationStrategy() {
      if (this.targetAuthStrategy == null) {
         this.targetAuthStrategy = this.createTargetAuthenticationStrategy();
      }

      return this.targetAuthStrategy;
   }

   public synchronized void setTargetAuthenticationStrategy(AuthenticationStrategy var1) {
      this.targetAuthStrategy = var1;
   }

   /** @deprecated */
   @Deprecated
   public final synchronized AuthenticationHandler getProxyAuthenticationHandler() {
      return this.createProxyAuthenticationHandler();
   }

   /** @deprecated */
   @Deprecated
   public synchronized void setProxyAuthenticationHandler(AuthenticationHandler var1) {
      this.proxyAuthStrategy = new AuthenticationStrategyAdaptor(var1);
   }

   public final synchronized AuthenticationStrategy getProxyAuthenticationStrategy() {
      if (this.proxyAuthStrategy == null) {
         this.proxyAuthStrategy = this.createProxyAuthenticationStrategy();
      }

      return this.proxyAuthStrategy;
   }

   public synchronized void setProxyAuthenticationStrategy(AuthenticationStrategy var1) {
      this.proxyAuthStrategy = var1;
   }

   public final synchronized CookieStore getCookieStore() {
      if (this.cookieStore == null) {
         this.cookieStore = this.createCookieStore();
      }

      return this.cookieStore;
   }

   public synchronized void setCookieStore(CookieStore var1) {
      this.cookieStore = var1;
   }

   public final synchronized CredentialsProvider getCredentialsProvider() {
      if (this.credsProvider == null) {
         this.credsProvider = this.createCredentialsProvider();
      }

      return this.credsProvider;
   }

   public synchronized void setCredentialsProvider(CredentialsProvider var1) {
      this.credsProvider = var1;
   }

   public final synchronized HttpRoutePlanner getRoutePlanner() {
      if (this.routePlanner == null) {
         this.routePlanner = this.createHttpRoutePlanner();
      }

      return this.routePlanner;
   }

   public synchronized void setRoutePlanner(HttpRoutePlanner var1) {
      this.routePlanner = var1;
   }

   public final synchronized UserTokenHandler getUserTokenHandler() {
      if (this.userTokenHandler == null) {
         this.userTokenHandler = this.createUserTokenHandler();
      }

      return this.userTokenHandler;
   }

   public synchronized void setUserTokenHandler(UserTokenHandler var1) {
      this.userTokenHandler = var1;
   }

   protected final synchronized BasicHttpProcessor getHttpProcessor() {
      if (this.mutableProcessor == null) {
         this.mutableProcessor = this.createHttpProcessor();
      }

      return this.mutableProcessor;
   }

   private synchronized HttpProcessor getProtocolProcessor() {
      if (this.protocolProcessor == null) {
         BasicHttpProcessor var1 = this.getHttpProcessor();
         int var2 = var1.getRequestInterceptorCount();
         HttpRequestInterceptor[] var3 = new HttpRequestInterceptor[var2];

         int var4;
         for(var4 = 0; var4 < var2; ++var4) {
            var3[var4] = var1.getRequestInterceptor(var4);
         }

         var4 = var1.getResponseInterceptorCount();
         HttpResponseInterceptor[] var5 = new HttpResponseInterceptor[var4];

         for(int var6 = 0; var6 < var4; ++var6) {
            var5[var6] = var1.getResponseInterceptor(var6);
         }

         this.protocolProcessor = new ImmutableHttpProcessor(var3, var5);
      }

      return this.protocolProcessor;
   }

   public synchronized int getResponseInterceptorCount() {
      return this.getHttpProcessor().getResponseInterceptorCount();
   }

   public synchronized HttpResponseInterceptor getResponseInterceptor(int var1) {
      return this.getHttpProcessor().getResponseInterceptor(var1);
   }

   public synchronized HttpRequestInterceptor getRequestInterceptor(int var1) {
      return this.getHttpProcessor().getRequestInterceptor(var1);
   }

   public synchronized int getRequestInterceptorCount() {
      return this.getHttpProcessor().getRequestInterceptorCount();
   }

   public synchronized void addResponseInterceptor(HttpResponseInterceptor var1) {
      this.getHttpProcessor().addInterceptor(var1);
      this.protocolProcessor = null;
   }

   public synchronized void addResponseInterceptor(HttpResponseInterceptor var1, int var2) {
      this.getHttpProcessor().addInterceptor(var1, var2);
      this.protocolProcessor = null;
   }

   public synchronized void clearResponseInterceptors() {
      this.getHttpProcessor().clearResponseInterceptors();
      this.protocolProcessor = null;
   }

   public synchronized void removeResponseInterceptorByClass(Class var1) {
      this.getHttpProcessor().removeResponseInterceptorByClass(var1);
      this.protocolProcessor = null;
   }

   public synchronized void addRequestInterceptor(HttpRequestInterceptor var1) {
      this.getHttpProcessor().addInterceptor(var1);
      this.protocolProcessor = null;
   }

   public synchronized void addRequestInterceptor(HttpRequestInterceptor var1, int var2) {
      this.getHttpProcessor().addInterceptor(var1, var2);
      this.protocolProcessor = null;
   }

   public synchronized void clearRequestInterceptors() {
      this.getHttpProcessor().clearRequestInterceptors();
      this.protocolProcessor = null;
   }

   public synchronized void removeRequestInterceptorByClass(Class var1) {
      this.getHttpProcessor().removeRequestInterceptorByClass(var1);
      this.protocolProcessor = null;
   }

   protected final CloseableHttpResponse doExecute(HttpHost var1, HttpRequest var2, HttpContext var3) throws IOException, ClientProtocolException {
      Args.notNull(var2, "HTTP request");
      Object var4 = null;
      RequestDirector var5 = null;
      HttpRoutePlanner var6 = null;
      ConnectionBackoffStrategy var7 = null;
      BackoffManager var8 = null;
      synchronized(this){}
      HttpContext var10 = this.createHttpContext();
      if (var3 == null) {
         var4 = var10;
      } else {
         var4 = new DefaultedHttpContext(var3, var10);
      }

      HttpParams var11 = this.determineParams(var2);
      RequestConfig var12 = HttpClientParamConfig.getRequestConfig(var11);
      ((HttpContext)var4).setAttribute("http.request-config", var12);
      var5 = this.createClientRequestDirector(this.getRequestExecutor(), this.getConnectionManager(), this.getConnectionReuseStrategy(), this.getConnectionKeepAliveStrategy(), this.getRoutePlanner(), this.getProtocolProcessor(), this.getHttpRequestRetryHandler(), this.getRedirectStrategy(), this.getTargetAuthenticationStrategy(), this.getProxyAuthenticationStrategy(), this.getUserTokenHandler(), var11);
      var6 = this.getRoutePlanner();
      var7 = this.getConnectionBackoffStrategy();
      var8 = this.getBackoffManager();

      try {
         if (var7 != null && var8 != null) {
            HttpHost var9 = var1 != null ? var1 : (HttpHost)this.determineParams(var2).getParameter("http.default-host");
            HttpRoute var17 = var6.determineRoute(var9, var2, (HttpContext)var4);

            CloseableHttpResponse var18;
            try {
               var18 = CloseableHttpResponseProxy.newProxy(var5.execute(var1, var2, (HttpContext)var4));
            } catch (RuntimeException var14) {
               if (var7.shouldBackoff((Throwable)var14)) {
                  var8.backOff(var17);
               }

               throw var14;
            } catch (Exception var15) {
               if (var7.shouldBackoff((Throwable)var15)) {
                  var8.backOff(var17);
               }

               if (var15 instanceof HttpException) {
                  throw (HttpException)var15;
               }

               if (var15 instanceof IOException) {
                  throw (IOException)var15;
               }

               throw new UndeclaredThrowableException(var15);
            }

            if (var7.shouldBackoff((HttpResponse)var18)) {
               var8.backOff(var17);
            } else {
               var8.probe(var17);
            }

            return var18;
         } else {
            return CloseableHttpResponseProxy.newProxy(var5.execute(var1, var2, (HttpContext)var4));
         }
      } catch (HttpException var16) {
         throw new ClientProtocolException(var16);
      }
   }

   /** @deprecated */
   @Deprecated
   protected RequestDirector createClientRequestDirector(HttpRequestExecutor var1, ClientConnectionManager var2, ConnectionReuseStrategy var3, ConnectionKeepAliveStrategy var4, HttpRoutePlanner var5, HttpProcessor var6, HttpRequestRetryHandler var7, RedirectHandler var8, AuthenticationHandler var9, AuthenticationHandler var10, UserTokenHandler var11, HttpParams var12) {
      return new DefaultRequestDirector(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
   }

   /** @deprecated */
   @Deprecated
   protected RequestDirector createClientRequestDirector(HttpRequestExecutor var1, ClientConnectionManager var2, ConnectionReuseStrategy var3, ConnectionKeepAliveStrategy var4, HttpRoutePlanner var5, HttpProcessor var6, HttpRequestRetryHandler var7, RedirectStrategy var8, AuthenticationHandler var9, AuthenticationHandler var10, UserTokenHandler var11, HttpParams var12) {
      return new DefaultRequestDirector(this.log, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
   }

   protected RequestDirector createClientRequestDirector(HttpRequestExecutor var1, ClientConnectionManager var2, ConnectionReuseStrategy var3, ConnectionKeepAliveStrategy var4, HttpRoutePlanner var5, HttpProcessor var6, HttpRequestRetryHandler var7, RedirectStrategy var8, AuthenticationStrategy var9, AuthenticationStrategy var10, UserTokenHandler var11, HttpParams var12) {
      return new DefaultRequestDirector(this.log, var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12);
   }

   protected HttpParams determineParams(HttpRequest var1) {
      return new ClientParamsStack((HttpParams)null, this.getParams(), var1.getParams(), (HttpParams)null);
   }

   public void close() {
      this.getConnectionManager().shutdown();
   }
}
