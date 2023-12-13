package org.apache.http.impl.client;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.params.HttpClientParamConfig;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.RouteInfo;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.auth.BasicSchemeFactory;
import org.apache.http.impl.auth.DigestSchemeFactory;
import org.apache.http.impl.auth.KerberosSchemeFactory;
import org.apache.http.impl.auth.NTLMSchemeFactory;
import org.apache.http.impl.auth.SPNegoSchemeFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParamConfig;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

public class ProxyClient {
   private final HttpConnectionFactory connFactory;
   private final ConnectionConfig connectionConfig;
   private final RequestConfig requestConfig;
   private final HttpProcessor httpProcessor;
   private final HttpRequestExecutor requestExec;
   private final ProxyAuthenticationStrategy proxyAuthStrategy;
   private final org.apache.http.impl.auth.HttpAuthenticator authenticator;
   private final AuthState proxyAuthState;
   private final AuthSchemeRegistry authSchemeRegistry;
   private final ConnectionReuseStrategy reuseStrategy;

   public ProxyClient(HttpConnectionFactory var1, ConnectionConfig var2, RequestConfig var3) {
      this.connFactory = (HttpConnectionFactory)(var1 != null ? var1 : ManagedHttpClientConnectionFactory.INSTANCE);
      this.connectionConfig = var2 != null ? var2 : ConnectionConfig.DEFAULT;
      this.requestConfig = var3 != null ? var3 : RequestConfig.DEFAULT;
      this.httpProcessor = new ImmutableHttpProcessor(new HttpRequestInterceptor[]{new RequestTargetHost(), new RequestClientConnControl(), new RequestUserAgent()});
      this.requestExec = new HttpRequestExecutor();
      this.proxyAuthStrategy = new ProxyAuthenticationStrategy();
      this.authenticator = new org.apache.http.impl.auth.HttpAuthenticator();
      this.proxyAuthState = new AuthState();
      this.authSchemeRegistry = new AuthSchemeRegistry();
      this.authSchemeRegistry.register("Basic", new BasicSchemeFactory());
      this.authSchemeRegistry.register("Digest", new DigestSchemeFactory());
      this.authSchemeRegistry.register("NTLM", new NTLMSchemeFactory());
      this.authSchemeRegistry.register("negotiate", new SPNegoSchemeFactory());
      this.authSchemeRegistry.register("Kerberos", new KerberosSchemeFactory());
      this.reuseStrategy = new DefaultConnectionReuseStrategy();
   }

   /** @deprecated */
   @Deprecated
   public ProxyClient(HttpParams var1) {
      this((HttpConnectionFactory)null, HttpParamConfig.getConnectionConfig(var1), HttpClientParamConfig.getRequestConfig(var1));
   }

   public ProxyClient(RequestConfig var1) {
      this((HttpConnectionFactory)null, (ConnectionConfig)null, var1);
   }

   public ProxyClient() {
      this((HttpConnectionFactory)null, (ConnectionConfig)null, (RequestConfig)null);
   }

   /** @deprecated */
   @Deprecated
   public HttpParams getParams() {
      return new BasicHttpParams();
   }

   /** @deprecated */
   @Deprecated
   public AuthSchemeRegistry getAuthSchemeRegistry() {
      return this.authSchemeRegistry;
   }

   public Socket tunnel(HttpHost var1, HttpHost var2, Credentials var3) throws IOException, HttpException {
      Args.notNull(var1, "Proxy host");
      Args.notNull(var2, "Target host");
      Args.notNull(var3, "Credentials");
      HttpHost var4 = var2;
      if (var2.getPort() <= 0) {
         var4 = new HttpHost(var2.getHostName(), 80, var2.getSchemeName());
      }

      HttpRoute var5 = new HttpRoute(var4, this.requestConfig.getLocalAddress(), var1, false, RouteInfo.TunnelType.TUNNELLED, RouteInfo.LayerType.PLAIN);
      ManagedHttpClientConnection var6 = (ManagedHttpClientConnection)this.connFactory.create(var5, this.connectionConfig);
      BasicHttpContext var7 = new BasicHttpContext();
      BasicHttpRequest var9 = new BasicHttpRequest("CONNECT", var4.toHostString(), HttpVersion.HTTP_1_1);
      BasicCredentialsProvider var10 = new BasicCredentialsProvider();
      var10.setCredentials(new AuthScope(var1), var3);
      var7.setAttribute("http.target_host", var2);
      var7.setAttribute("http.connection", var6);
      var7.setAttribute("http.request", var9);
      var7.setAttribute("http.route", var5);
      var7.setAttribute("http.auth.proxy-scope", this.proxyAuthState);
      var7.setAttribute("http.auth.credentials-provider", var10);
      var7.setAttribute("http.authscheme-registry", this.authSchemeRegistry);
      var7.setAttribute("http.request-config", this.requestConfig);
      this.requestExec.preProcess(var9, this.httpProcessor, var7);

      while(true) {
         if (!var6.isOpen()) {
            Socket var11 = new Socket(var1.getHostName(), var1.getPort());
            var6.bind(var11);
         }

         this.authenticator.generateAuthResponse(var9, this.proxyAuthState, var7);
         HttpResponse var8 = this.requestExec.execute(var9, var6, var7);
         int var13 = var8.getStatusLine().getStatusCode();
         if (var13 < 200) {
            throw new HttpException("Unexpected response to CONNECT request: " + var8.getStatusLine());
         }

         HttpEntity var12;
         if (!this.authenticator.isAuthenticationRequested(var1, var8, this.proxyAuthStrategy, this.proxyAuthState, var7) || !this.authenticator.handleAuthChallenge(var1, var8, this.proxyAuthStrategy, this.proxyAuthState, var7)) {
            var13 = var8.getStatusLine().getStatusCode();
            if (var13 > 299) {
               var12 = var8.getEntity();
               if (var12 != null) {
                  var8.setEntity(new BufferedHttpEntity(var12));
               }

               var6.close();
               throw new org.apache.http.impl.execchain.TunnelRefusedException("CONNECT refused by proxy: " + var8.getStatusLine(), var8);
            } else {
               return var6.getSocket();
            }
         }

         if (this.reuseStrategy.keepAlive(var8, var7)) {
            var12 = var8.getEntity();
            EntityUtils.consume(var12);
         } else {
            var6.close();
         }

         var9.removeHeaders("Proxy-Authorization");
      }
   }
}
