package org.apache.http.impl.client;

import java.io.Closeable;
import java.net.ProxySelector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.AuthenticationStrategy;
import org.apache.http.client.BackoffManager;
import org.apache.http.client.ConnectionBackoffStrategy;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.client.protocol.RequestAuthCache;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.client.protocol.RequestDefaultHeaders;
import org.apache.http.client.protocol.RequestExpectContinue;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.client.protocol.ResponseProcessCookies;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.conn.DefaultRoutePlanner;
import org.apache.http.impl.conn.DefaultSchemePortResolver;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.impl.cookie.IgnoreSpecFactory;
import org.apache.http.impl.cookie.NetscapeDraftSpecFactory;
import org.apache.http.impl.cookie.RFC2109SpecFactory;
import org.apache.http.impl.cookie.RFC2965SpecFactory;
import org.apache.http.impl.execchain.BackoffStrategyExec;
import org.apache.http.impl.execchain.ClientExecChain;
import org.apache.http.impl.execchain.MainClientExec;
import org.apache.http.impl.execchain.ProtocolExec;
import org.apache.http.impl.execchain.RedirectExec;
import org.apache.http.impl.execchain.RetryExec;
import org.apache.http.impl.execchain.ServiceUnavailableRetryExec;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.TextUtils;
import org.apache.http.util.VersionInfo;

@NotThreadSafe
public class HttpClientBuilder {
   private HttpRequestExecutor requestExec;
   private X509HostnameVerifier hostnameVerifier;
   private LayeredConnectionSocketFactory sslSocketFactory;
   private SSLContext sslcontext;
   private HttpClientConnectionManager connManager;
   private SchemePortResolver schemePortResolver;
   private ConnectionReuseStrategy reuseStrategy;
   private ConnectionKeepAliveStrategy keepAliveStrategy;
   private AuthenticationStrategy targetAuthStrategy;
   private AuthenticationStrategy proxyAuthStrategy;
   private UserTokenHandler userTokenHandler;
   private HttpProcessor httpprocessor;
   private LinkedList requestFirst;
   private LinkedList requestLast;
   private LinkedList responseFirst;
   private LinkedList responseLast;
   private HttpRequestRetryHandler retryHandler;
   private HttpRoutePlanner routePlanner;
   private RedirectStrategy redirectStrategy;
   private ConnectionBackoffStrategy connectionBackoffStrategy;
   private BackoffManager backoffManager;
   private ServiceUnavailableRetryStrategy serviceUnavailStrategy;
   private Lookup authSchemeRegistry;
   private Lookup cookieSpecRegistry;
   private CookieStore cookieStore;
   private CredentialsProvider credentialsProvider;
   private String userAgent;
   private HttpHost proxy;
   private Collection defaultHeaders;
   private SocketConfig defaultSocketConfig;
   private ConnectionConfig defaultConnectionConfig;
   private RequestConfig defaultRequestConfig;
   private boolean systemProperties;
   private boolean redirectHandlingDisabled;
   private boolean automaticRetriesDisabled;
   private boolean contentCompressionDisabled;
   private boolean cookieManagementDisabled;
   private boolean authCachingDisabled;
   private boolean connectionStateDisabled;
   private int maxConnTotal = 0;
   private int maxConnPerRoute = 0;
   private List closeables;
   static final String DEFAULT_USER_AGENT;

   public static HttpClientBuilder create() {
      return new HttpClientBuilder();
   }

   protected HttpClientBuilder() {
   }

   public final HttpClientBuilder setRequestExecutor(HttpRequestExecutor var1) {
      this.requestExec = var1;
      return this;
   }

   public final HttpClientBuilder setHostnameVerifier(X509HostnameVerifier var1) {
      this.hostnameVerifier = var1;
      return this;
   }

   public final HttpClientBuilder setSslcontext(SSLContext var1) {
      this.sslcontext = var1;
      return this;
   }

   public final HttpClientBuilder setSSLSocketFactory(LayeredConnectionSocketFactory var1) {
      this.sslSocketFactory = var1;
      return this;
   }

   public final HttpClientBuilder setMaxConnTotal(int var1) {
      this.maxConnTotal = var1;
      return this;
   }

   public final HttpClientBuilder setMaxConnPerRoute(int var1) {
      this.maxConnPerRoute = var1;
      return this;
   }

   public final HttpClientBuilder setDefaultSocketConfig(SocketConfig var1) {
      this.defaultSocketConfig = var1;
      return this;
   }

   public final HttpClientBuilder setDefaultConnectionConfig(ConnectionConfig var1) {
      this.defaultConnectionConfig = var1;
      return this;
   }

   public final HttpClientBuilder setConnectionManager(HttpClientConnectionManager var1) {
      this.connManager = var1;
      return this;
   }

   public final HttpClientBuilder setConnectionReuseStrategy(ConnectionReuseStrategy var1) {
      this.reuseStrategy = var1;
      return this;
   }

   public final HttpClientBuilder setKeepAliveStrategy(ConnectionKeepAliveStrategy var1) {
      this.keepAliveStrategy = var1;
      return this;
   }

   public final HttpClientBuilder setTargetAuthenticationStrategy(AuthenticationStrategy var1) {
      this.targetAuthStrategy = var1;
      return this;
   }

   public final HttpClientBuilder setProxyAuthenticationStrategy(AuthenticationStrategy var1) {
      this.proxyAuthStrategy = var1;
      return this;
   }

   public final HttpClientBuilder setUserTokenHandler(UserTokenHandler var1) {
      this.userTokenHandler = var1;
      return this;
   }

   public final HttpClientBuilder disableConnectionState() {
      this.connectionStateDisabled = true;
      return this;
   }

   public final HttpClientBuilder setSchemePortResolver(SchemePortResolver var1) {
      this.schemePortResolver = var1;
      return this;
   }

   public final HttpClientBuilder setUserAgent(String var1) {
      this.userAgent = var1;
      return this;
   }

   public final HttpClientBuilder setDefaultHeaders(Collection var1) {
      this.defaultHeaders = var1;
      return this;
   }

   public final HttpClientBuilder addInterceptorFirst(HttpResponseInterceptor var1) {
      if (var1 == null) {
         return this;
      } else {
         if (this.responseFirst == null) {
            this.responseFirst = new LinkedList();
         }

         this.responseFirst.addFirst(var1);
         return this;
      }
   }

   public final HttpClientBuilder addInterceptorLast(HttpResponseInterceptor var1) {
      if (var1 == null) {
         return this;
      } else {
         if (this.responseLast == null) {
            this.responseLast = new LinkedList();
         }

         this.responseLast.addLast(var1);
         return this;
      }
   }

   public final HttpClientBuilder addInterceptorFirst(HttpRequestInterceptor var1) {
      if (var1 == null) {
         return this;
      } else {
         if (this.requestFirst == null) {
            this.requestFirst = new LinkedList();
         }

         this.requestFirst.addFirst(var1);
         return this;
      }
   }

   public final HttpClientBuilder addInterceptorLast(HttpRequestInterceptor var1) {
      if (var1 == null) {
         return this;
      } else {
         if (this.requestLast == null) {
            this.requestLast = new LinkedList();
         }

         this.requestLast.addLast(var1);
         return this;
      }
   }

   public final HttpClientBuilder disableCookieManagement() {
      this.cookieManagementDisabled = true;
      return this;
   }

   public final HttpClientBuilder disableContentCompression() {
      this.contentCompressionDisabled = true;
      return this;
   }

   public final HttpClientBuilder disableAuthCaching() {
      this.authCachingDisabled = true;
      return this;
   }

   public final HttpClientBuilder setHttpProcessor(HttpProcessor var1) {
      this.httpprocessor = var1;
      return this;
   }

   public final HttpClientBuilder setRetryHandler(HttpRequestRetryHandler var1) {
      this.retryHandler = var1;
      return this;
   }

   public final HttpClientBuilder disableAutomaticRetries() {
      this.automaticRetriesDisabled = true;
      return this;
   }

   public final HttpClientBuilder setProxy(HttpHost var1) {
      this.proxy = var1;
      return this;
   }

   public final HttpClientBuilder setRoutePlanner(HttpRoutePlanner var1) {
      this.routePlanner = var1;
      return this;
   }

   public final HttpClientBuilder setRedirectStrategy(RedirectStrategy var1) {
      this.redirectStrategy = var1;
      return this;
   }

   public final HttpClientBuilder disableRedirectHandling() {
      this.redirectHandlingDisabled = true;
      return this;
   }

   public final HttpClientBuilder setConnectionBackoffStrategy(ConnectionBackoffStrategy var1) {
      this.connectionBackoffStrategy = var1;
      return this;
   }

   public final HttpClientBuilder setBackoffManager(BackoffManager var1) {
      this.backoffManager = var1;
      return this;
   }

   public final HttpClientBuilder setServiceUnavailableRetryStrategy(ServiceUnavailableRetryStrategy var1) {
      this.serviceUnavailStrategy = var1;
      return this;
   }

   public final HttpClientBuilder setDefaultCookieStore(CookieStore var1) {
      this.cookieStore = var1;
      return this;
   }

   public final HttpClientBuilder setDefaultCredentialsProvider(CredentialsProvider var1) {
      this.credentialsProvider = var1;
      return this;
   }

   public final HttpClientBuilder setDefaultAuthSchemeRegistry(Lookup var1) {
      this.authSchemeRegistry = var1;
      return this;
   }

   public final HttpClientBuilder setDefaultCookieSpecRegistry(Lookup var1) {
      this.cookieSpecRegistry = var1;
      return this;
   }

   public final HttpClientBuilder setDefaultRequestConfig(RequestConfig var1) {
      this.defaultRequestConfig = var1;
      return this;
   }

   public final HttpClientBuilder useSystemProperties() {
      this.systemProperties = true;
      return this;
   }

   protected ClientExecChain decorateMainExec(ClientExecChain var1) {
      return var1;
   }

   protected ClientExecChain decorateProtocolExec(ClientExecChain var1) {
      return var1;
   }

   protected void addCloseable(Closeable var1) {
      if (var1 != null) {
         if (this.closeables == null) {
            this.closeables = new ArrayList();
         }

         this.closeables.add(var1);
      }
   }

   private static String[] split(String var0) {
      return TextUtils.isBlank(var0) ? null : var0.split(" *, *");
   }

   public CloseableHttpClient build() {
      HttpRequestExecutor var1 = this.requestExec;
      if (var1 == null) {
         var1 = new HttpRequestExecutor();
      }

      Object var2 = this.connManager;
      Object var3;
      if (var2 == null) {
         var3 = this.sslSocketFactory;
         if (var3 == null) {
            String[] var4 = this.systemProperties ? split(System.getProperty("https.protocols")) : null;
            String[] var5 = this.systemProperties ? split(System.getProperty("https.cipherSuites")) : null;
            X509HostnameVerifier var6 = this.hostnameVerifier;
            if (var6 == null) {
               var6 = SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER;
            }

            if (this.sslcontext != null) {
               var3 = new SSLConnectionSocketFactory(this.sslcontext, var4, var5, var6);
            } else if (this.systemProperties) {
               var3 = new SSLConnectionSocketFactory((SSLSocketFactory)SSLSocketFactory.getDefault(), var4, var5, var6);
            } else {
               var3 = new SSLConnectionSocketFactory(SSLContexts.createDefault(), var6);
            }
         }

         PoolingHttpClientConnectionManager var18 = new PoolingHttpClientConnectionManager(RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", var3).build());
         if (this.defaultSocketConfig != null) {
            var18.setDefaultSocketConfig(this.defaultSocketConfig);
         }

         if (this.defaultConnectionConfig != null) {
            var18.setDefaultConnectionConfig(this.defaultConnectionConfig);
         }

         if (this.systemProperties) {
            String var21 = System.getProperty("http.keepAlive", "true");
            if ("true".equalsIgnoreCase(var21)) {
               var21 = System.getProperty("http.maxConnections", "5");
               int var23 = Integer.parseInt(var21);
               var18.setDefaultMaxPerRoute(var23);
               var18.setMaxTotal(2 * var23);
            }
         }

         if (this.maxConnTotal > 0) {
            var18.setMaxTotal(this.maxConnTotal);
         }

         if (this.maxConnPerRoute > 0) {
            var18.setDefaultMaxPerRoute(this.maxConnPerRoute);
         }

         var2 = var18;
      }

      var3 = this.reuseStrategy;
      if (var3 == null) {
         if (this.systemProperties) {
            String var19 = System.getProperty("http.keepAlive", "true");
            if ("true".equalsIgnoreCase(var19)) {
               var3 = DefaultConnectionReuseStrategy.INSTANCE;
            } else {
               var3 = NoConnectionReuseStrategy.INSTANCE;
            }
         } else {
            var3 = DefaultConnectionReuseStrategy.INSTANCE;
         }
      }

      Object var20 = this.keepAliveStrategy;
      if (var20 == null) {
         var20 = DefaultConnectionKeepAliveStrategy.INSTANCE;
      }

      Object var22 = this.targetAuthStrategy;
      if (var22 == null) {
         var22 = TargetAuthenticationStrategy.INSTANCE;
      }

      Object var24 = this.proxyAuthStrategy;
      if (var24 == null) {
         var24 = ProxyAuthenticationStrategy.INSTANCE;
      }

      Object var7 = this.userTokenHandler;
      if (var7 == null) {
         if (!this.connectionStateDisabled) {
            var7 = DefaultUserTokenHandler.INSTANCE;
         } else {
            var7 = NoopUserTokenHandler.INSTANCE;
         }
      }

      MainClientExec var8 = new MainClientExec(var1, (HttpClientConnectionManager)var2, (ConnectionReuseStrategy)var3, (ConnectionKeepAliveStrategy)var20, (AuthenticationStrategy)var22, (AuthenticationStrategy)var24, (UserTokenHandler)var7);
      ClientExecChain var25 = this.decorateMainExec(var8);
      HttpProcessor var9 = this.httpprocessor;
      if (var9 == null) {
         String var10 = this.userAgent;
         if (var10 == null) {
            if (this.systemProperties) {
               var10 = System.getProperty("http.agent");
            }

            if (var10 == null) {
               var10 = DEFAULT_USER_AGENT;
            }
         }

         HttpProcessorBuilder var11 = HttpProcessorBuilder.create();
         Iterator var12;
         HttpRequestInterceptor var13;
         if (this.requestFirst != null) {
            var12 = this.requestFirst.iterator();

            while(var12.hasNext()) {
               var13 = (HttpRequestInterceptor)var12.next();
               var11.addFirst(var13);
            }
         }

         HttpResponseInterceptor var31;
         if (this.responseFirst != null) {
            var12 = this.responseFirst.iterator();

            while(var12.hasNext()) {
               var31 = (HttpResponseInterceptor)var12.next();
               var11.addFirst(var31);
            }
         }

         var11.addAll(new RequestDefaultHeaders(this.defaultHeaders), new RequestContent(), new RequestTargetHost(), new RequestClientConnControl(), new RequestUserAgent(var10), new RequestExpectContinue());
         if (!this.cookieManagementDisabled) {
            var11.add((HttpRequestInterceptor)(new RequestAddCookies()));
         }

         if (!this.contentCompressionDisabled) {
            var11.add((HttpRequestInterceptor)(new RequestAcceptEncoding()));
         }

         if (!this.authCachingDisabled) {
            var11.add((HttpRequestInterceptor)(new RequestAuthCache()));
         }

         if (!this.cookieManagementDisabled) {
            var11.add((HttpResponseInterceptor)(new ResponseProcessCookies()));
         }

         if (!this.contentCompressionDisabled) {
            var11.add((HttpResponseInterceptor)(new ResponseContentEncoding()));
         }

         if (this.requestLast != null) {
            var12 = this.requestLast.iterator();

            while(var12.hasNext()) {
               var13 = (HttpRequestInterceptor)var12.next();
               var11.addLast(var13);
            }
         }

         if (this.responseLast != null) {
            var12 = this.responseLast.iterator();

            while(var12.hasNext()) {
               var31 = (HttpResponseInterceptor)var12.next();
               var11.addLast(var31);
            }
         }

         var9 = var11.build();
      }

      ProtocolExec var26 = new ProtocolExec(var25, var9);
      Object var27 = this.decorateProtocolExec(var26);
      Object var29;
      if (!this.automaticRetriesDisabled) {
         var29 = this.retryHandler;
         if (var29 == null) {
            var29 = DefaultHttpRequestRetryHandler.INSTANCE;
         }

         var27 = new RetryExec((ClientExecChain)var27, (HttpRequestRetryHandler)var29);
      }

      var29 = this.routePlanner;
      Object var28;
      if (var29 == null) {
         var28 = this.schemePortResolver;
         if (var28 == null) {
            var28 = DefaultSchemePortResolver.INSTANCE;
         }

         if (this.proxy != null) {
            var29 = new DefaultProxyRoutePlanner(this.proxy, (SchemePortResolver)var28);
         } else if (this.systemProperties) {
            var29 = new SystemDefaultRoutePlanner((SchemePortResolver)var28, ProxySelector.getDefault());
         } else {
            var29 = new DefaultRoutePlanner((SchemePortResolver)var28);
         }
      }

      if (!this.redirectHandlingDisabled) {
         var28 = this.redirectStrategy;
         if (var28 == null) {
            var28 = DefaultRedirectStrategy.INSTANCE;
         }

         var27 = new RedirectExec((ClientExecChain)var27, (HttpRoutePlanner)var29, (RedirectStrategy)var28);
      }

      ServiceUnavailableRetryStrategy var32 = this.serviceUnavailStrategy;
      if (var32 != null) {
         var27 = new ServiceUnavailableRetryExec((ClientExecChain)var27, var32);
      }

      BackoffManager var30 = this.backoffManager;
      ConnectionBackoffStrategy var33 = this.connectionBackoffStrategy;
      if (var30 != null && var33 != null) {
         var27 = new BackoffStrategyExec((ClientExecChain)var27, var33, var30);
      }

      Object var14 = this.authSchemeRegistry;
      if (var14 == null) {
         var14 = RegistryBuilder.create().register("Basic", new BasicSchemeFactory()).register("Digest", new DigestSchemeFactory()).register("NTLM", new NTLMSchemeFactory()).register("negotiate", new SPNegoSchemeFactory()).register("Kerberos", new KerberosSchemeFactory()).build();
      }

      Object var15 = this.cookieSpecRegistry;
      if (var15 == null) {
         var15 = RegistryBuilder.create().register("best-match", new BestMatchSpecFactory()).register("standard", new RFC2965SpecFactory()).register("compatibility", new BrowserCompatSpecFactory()).register("netscape", new NetscapeDraftSpecFactory()).register("ignoreCookies", new IgnoreSpecFactory()).register("rfc2109", new RFC2109SpecFactory()).register("rfc2965", new RFC2965SpecFactory()).build();
      }

      Object var16 = this.cookieStore;
      if (var16 == null) {
         var16 = new BasicCookieStore();
      }

      Object var17 = this.credentialsProvider;
      if (var17 == null) {
         if (this.systemProperties) {
            var17 = new SystemDefaultCredentialsProvider();
         } else {
            var17 = new BasicCredentialsProvider();
         }
      }

      return new InternalHttpClient((ClientExecChain)var27, (HttpClientConnectionManager)var2, (HttpRoutePlanner)var29, (Lookup)var15, (Lookup)var14, (CookieStore)var16, (CredentialsProvider)var17, this.defaultRequestConfig != null ? this.defaultRequestConfig : RequestConfig.DEFAULT, this.closeables != null ? new ArrayList(this.closeables) : null);
   }

   static {
      VersionInfo var0 = VersionInfo.loadVersionInfo("org.apache.http.client", HttpClientBuilder.class.getClassLoader());
      String var1 = var0 != null ? var0.getRelease() : "UNAVAILABLE";
      DEFAULT_USER_AGENT = "Apache-HttpClient/" + var1 + " (java 1.5)";
   }
}
