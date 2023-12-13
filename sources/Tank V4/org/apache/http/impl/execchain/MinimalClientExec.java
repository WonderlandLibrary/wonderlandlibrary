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
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.ConnectionShutdownException;
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
   private final Log log = LogFactory.getLog(this.getClass());
   private final HttpRequestExecutor requestExecutor;
   private final HttpClientConnectionManager connManager;
   private final ConnectionReuseStrategy reuseStrategy;
   private final ConnectionKeepAliveStrategy keepAliveStrategy;
   private final HttpProcessor httpProcessor;

   public MinimalClientExec(HttpRequestExecutor var1, HttpClientConnectionManager var2, ConnectionReuseStrategy var3, ConnectionKeepAliveStrategy var4) {
      Args.notNull(var1, "HTTP request executor");
      Args.notNull(var2, "Client connection manager");
      Args.notNull(var3, "Connection reuse strategy");
      Args.notNull(var4, "Connection keep alive strategy");
      this.httpProcessor = new ImmutableHttpProcessor(new HttpRequestInterceptor[]{new RequestContent(), new RequestTargetHost(), new RequestClientConnControl(), new RequestUserAgent(VersionInfo.getUserAgent("Apache-HttpClient", "org.apache.http.client", this.getClass()))});
      this.requestExecutor = var1;
      this.connManager = var2;
      this.reuseStrategy = var3;
      this.keepAliveStrategy = var4;
   }

   static void rewriteRequestURI(HttpRequestWrapper var0, HttpRoute var1) throws ProtocolException {
      try {
         URI var2 = var0.getURI();
         if (var2 != null) {
            if (var2.isAbsolute()) {
               var2 = URIUtils.rewriteURI(var2, (HttpHost)null, true);
            } else {
               var2 = URIUtils.rewriteURI(var2);
            }

            var0.setURI(var2);
         }

      } catch (URISyntaxException var3) {
         throw new ProtocolException("Invalid URI: " + var0.getRequestLine().getUri(), var3);
      }
   }

   public CloseableHttpResponse execute(HttpRoute var1, HttpRequestWrapper var2, HttpClientContext var3, HttpExecutionAware var4) throws IOException, HttpException {
      Args.notNull(var1, "HTTP route");
      Args.notNull(var2, "HTTP request");
      Args.notNull(var3, "HTTP context");
      rewriteRequestURI(var2, var1);
      ConnectionRequest var5 = this.connManager.requestConnection(var1, (Object)null);
      if (var4 != null) {
         if (var4.isAborted()) {
            var5.cancel();
            throw new RequestAbortedException("Request aborted");
         }

         var4.setCancellable(var5);
      }

      RequestConfig var6 = var3.getRequestConfig();

      HttpClientConnection var7;
      try {
         int var8 = var6.getConnectionRequestTimeout();
         var7 = var5.get(var8 > 0 ? (long)var8 : 0L, TimeUnit.MILLISECONDS);
      } catch (InterruptedException var19) {
         Thread.currentThread().interrupt();
         throw new RequestAbortedException("Request aborted", var19);
      } catch (ExecutionException var20) {
         Object var9 = var20.getCause();
         if (var9 == null) {
            var9 = var20;
         }

         throw new RequestAbortedException("Request execution failed", (Throwable)var9);
      }

      ConnectionHolder var21 = new ConnectionHolder(this.log, this.connManager, var7);

      try {
         if (var4 != null) {
            if (var4.isAborted()) {
               var21.close();
               throw new RequestAbortedException("Request aborted");
            }

            var4.setCancellable(var21);
         }

         int var22;
         if (!var7.isOpen()) {
            var22 = var6.getConnectTimeout();
            this.connManager.connect(var7, var1, var22 > 0 ? var22 : 0, var3);
            this.connManager.routeComplete(var7, var1, var3);
         }

         var22 = var6.getSocketTimeout();
         if (var22 >= 0) {
            var7.setSocketTimeout(var22);
         }

         HttpHost var23 = null;
         HttpRequest var11 = var2.getOriginal();
         if (var11 instanceof HttpUriRequest) {
            URI var12 = ((HttpUriRequest)var11).getURI();
            if (var12.isAbsolute()) {
               var23 = new HttpHost(var12.getHost(), var12.getPort(), var12.getScheme());
            }
         }

         if (var23 == null) {
            var23 = var1.getTargetHost();
         }

         var3.setAttribute("http.target_host", var23);
         var3.setAttribute("http.request", var2);
         var3.setAttribute("http.connection", var7);
         var3.setAttribute("http.route", var1);
         this.httpProcessor.process(var2, var3);
         HttpResponse var24 = this.requestExecutor.execute(var2, var7, var3);
         this.httpProcessor.process(var24, var3);
         if (this.reuseStrategy.keepAlive(var24, var3)) {
            long var13 = this.keepAliveStrategy.getKeepAliveDuration(var24, var3);
            var21.setValidFor(var13, TimeUnit.MILLISECONDS);
            var21.markReusable();
         } else {
            var21.markNonReusable();
         }

         HttpEntity var25 = var24.getEntity();
         if (var25 != null && var25.isStreaming()) {
            return Proxies.enhanceResponse(var24, var21);
         } else {
            var21.releaseConnection();
            return Proxies.enhanceResponse(var24, (ConnectionHolder)null);
         }
      } catch (ConnectionShutdownException var15) {
         InterruptedIOException var10 = new InterruptedIOException("Connection has been shut down");
         var10.initCause(var15);
         throw var10;
      } catch (HttpException var16) {
         var21.abortConnection();
         throw var16;
      } catch (IOException var17) {
         var21.abortConnection();
         throw var17;
      } catch (RuntimeException var18) {
         var21.abortConnection();
         throw var18;
      }
   }
}
