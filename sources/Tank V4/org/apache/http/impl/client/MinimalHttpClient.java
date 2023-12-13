package org.apache.http.impl.client;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.Configurable;
import org.apache.http.client.methods.HttpExecutionAware;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.execchain.MinimalClientExec;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.util.Args;

@ThreadSafe
class MinimalHttpClient extends CloseableHttpClient {
   private final HttpClientConnectionManager connManager;
   private final MinimalClientExec requestExecutor;
   private final HttpParams params;

   public MinimalHttpClient(HttpClientConnectionManager var1) {
      this.connManager = (HttpClientConnectionManager)Args.notNull(var1, "HTTP connection manager");
      this.requestExecutor = new MinimalClientExec(new HttpRequestExecutor(), var1, DefaultConnectionReuseStrategy.INSTANCE, DefaultConnectionKeepAliveStrategy.INSTANCE);
      this.params = new BasicHttpParams();
   }

   protected CloseableHttpResponse doExecute(HttpHost var1, HttpRequest var2, HttpContext var3) throws IOException, ClientProtocolException {
      Args.notNull(var1, "Target host");
      Args.notNull(var2, "HTTP request");
      HttpExecutionAware var4 = null;
      if (var2 instanceof HttpExecutionAware) {
         var4 = (HttpExecutionAware)var2;
      }

      try {
         HttpRequestWrapper var5 = HttpRequestWrapper.wrap(var2);
         HttpClientContext var6 = HttpClientContext.adapt((HttpContext)(var3 != null ? var3 : new BasicHttpContext()));
         HttpRoute var7 = new HttpRoute(var1);
         RequestConfig var8 = null;
         if (var2 instanceof Configurable) {
            var8 = ((Configurable)var2).getConfig();
         }

         if (var8 != null) {
            var6.setRequestConfig(var8);
         }

         return this.requestExecutor.execute(var7, var5, var6, var4);
      } catch (HttpException var9) {
         throw new ClientProtocolException(var9);
      }
   }

   public HttpParams getParams() {
      return this.params;
   }

   public void close() {
      this.connManager.shutdown();
   }

   public ClientConnectionManager getConnectionManager() {
      return new ClientConnectionManager(this) {
         final MinimalHttpClient this$0;

         {
            this.this$0 = var1;
         }

         public void shutdown() {
            MinimalHttpClient.access$000(this.this$0).shutdown();
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
            MinimalHttpClient.access$000(this.this$0).closeIdleConnections(var1, var3);
         }

         public void closeExpiredConnections() {
            MinimalHttpClient.access$000(this.this$0).closeExpiredConnections();
         }
      };
   }

   static HttpClientConnectionManager access$000(MinimalHttpClient var0) {
      return var0.connManager;
   }
}
