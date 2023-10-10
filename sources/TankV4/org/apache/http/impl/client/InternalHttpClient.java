package org.apache.http.impl.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.auth.AuthState;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.Configurable;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.params.HttpClientParamConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.execchain.ClientExecChain;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpParamsNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@ThreadSafe
class InternalHttpClient extends CloseableHttpClient {
   private final Log log = LogFactory.getLog(this.getClass());
   private final ClientExecChain execChain;
   private final HttpClientConnectionManager connManager;
   private final HttpRoutePlanner routePlanner;
   private final Lookup cookieSpecRegistry;
   private final Lookup authSchemeRegistry;
   private final CookieStore cookieStore;
   private final CredentialsProvider credentialsProvider;
   private final RequestConfig defaultConfig;
   private final List closeables;

   public InternalHttpClient(ClientExecChain var1, HttpClientConnectionManager var2, HttpRoutePlanner var3, Lookup var4, Lookup var5, CookieStore var6, CredentialsProvider var7, RequestConfig var8, List var9) {
      Args.notNull(var1, "HTTP client exec chain");
      Args.notNull(var2, "HTTP connection manager");
      Args.notNull(var3, "HTTP route planner");
      this.execChain = var1;
      this.connManager = var2;
      this.routePlanner = var3;
      this.cookieSpecRegistry = var4;
      this.authSchemeRegistry = var5;
      this.cookieStore = var6;
      this.credentialsProvider = var7;
      this.defaultConfig = var8;
      this.closeables = var9;
   }

   private HttpRoute determineRoute(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException {
      HttpHost var4 = var1;
      if (var1 == null) {
         var4 = (HttpHost)var2.getParams().getParameter("http.default-host");
      }

      Asserts.notNull(var4, "Target host");
      return this.routePlanner.determineRoute(var4, var2, var3);
   }

   private void setupContext(HttpClientContext var1) {
      if (var1.getAttribute("http.auth.target-scope") == null) {
         var1.setAttribute("http.auth.target-scope", new AuthState());
      }

      if (var1.getAttribute("http.auth.proxy-scope") == null) {
         var1.setAttribute("http.auth.proxy-scope", new AuthState());
      }

      if (var1.getAttribute("http.authscheme-registry") == null) {
         var1.setAttribute("http.authscheme-registry", this.authSchemeRegistry);
      }

      if (var1.getAttribute("http.cookiespec-registry") == null) {
         var1.setAttribute("http.cookiespec-registry", this.cookieSpecRegistry);
      }

      if (var1.getAttribute("http.cookie-store") == null) {
         var1.setAttribute("http.cookie-store", this.cookieStore);
      }

      if (var1.getAttribute("http.auth.credentials-provider") == null) {
         var1.setAttribute("http.auth.credentials-provider", this.credentialsProvider);
      }

      if (var1.getAttribute("http.request-config") == null) {
         var1.setAttribute("http.request-config", this.defaultConfig);
      }

   }

   protected CloseableHttpResponse doExecute(HttpHost var1, HttpRequest var2, HttpContext var3) throws IOException, ClientProtocolException {
      Args.notNull(var2, "HTTP request");
      HttpExecutionAware var4 = null;
      if (var2 instanceof HttpExecutionAware) {
         var4 = (HttpExecutionAware)var2;
      }

      try {
         HttpRequestWrapper var5 = HttpRequestWrapper.wrap(var2);
         HttpClientContext var6 = HttpClientContext.adapt((HttpContext)(var3 != null ? var3 : new BasicHttpContext()));
         RequestConfig var7 = null;
         if (var2 instanceof Configurable) {
            var7 = ((Configurable)var2).getConfig();
         }

         if (var7 == null) {
            HttpParams var8 = var2.getParams();
            if (var8 instanceof HttpParamsNames) {
               if (!((HttpParamsNames)var8).getNames().isEmpty()) {
                  var7 = HttpClientParamConfig.getRequestConfig(var8);
               }
            } else {
               var7 = HttpClientParamConfig.getRequestConfig(var8);
            }
         }

         if (var7 != null) {
            var6.setRequestConfig(var7);
         }

         this.setupContext(var6);
         HttpRoute var10 = this.determineRoute(var1, var5, var6);
         return this.execChain.execute(var10, var5, var6, var4);
      } catch (HttpException var9) {
         throw new ClientProtocolException(var9);
      }
   }

   public void close() {
      this.connManager.shutdown();
      if (this.closeables != null) {
         Iterator var1 = this.closeables.iterator();

         while(var1.hasNext()) {
            Closeable var2 = (Closeable)var1.next();

            try {
               var2.close();
            } catch (IOException var4) {
               this.log.error(var4.getMessage(), var4);
            }
         }
      }

   }

   public HttpParams getParams() {
      throw new UnsupportedOperationException();
   }

   public ClientConnectionManager getConnectionManager() {
      return new ClientConnectionManager(this) {
         final InternalHttpClient this$0;

         {
            this.this$0 = var1;
         }

         public void shutdown() {
            InternalHttpClient.access$000(this.this$0).shutdown();
         }

         public ClientConnectionRequest requestConnection(HttpRoute var1, Object var2) {
            throw new UnsupportedOperationException();
         }

         public void releaseConnection(ManagedClientConnection var1, long var2, TimeUnit var4) {
            throw new UnsupportedOperationException();
         }

         public SchemeRegistry getSchemeRegistry() {
            throw new UnsupportedOperationException();
         }

         public void closeIdleConnections(long var1, TimeUnit var3) {
            InternalHttpClient.access$000(this.this$0).closeIdleConnections(var1, var3);
         }

         public void closeExpiredConnections() {
            InternalHttpClient.access$000(this.this$0).closeExpiredConnections();
         }
      };
   }

   static HttpClientConnectionManager access$000(InternalHttpClient var0) {
      return var0.connManager;
   }
}
