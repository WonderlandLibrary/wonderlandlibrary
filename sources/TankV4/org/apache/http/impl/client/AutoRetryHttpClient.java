package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.ServiceUnavailableRetryStrategy;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;

/** @deprecated */
@Deprecated
@ThreadSafe
public class AutoRetryHttpClient implements HttpClient {
   private final HttpClient backend;
   private final ServiceUnavailableRetryStrategy retryStrategy;
   private final Log log;

   public AutoRetryHttpClient(HttpClient var1, ServiceUnavailableRetryStrategy var2) {
      this.log = LogFactory.getLog(this.getClass());
      Args.notNull(var1, "HttpClient");
      Args.notNull(var2, "ServiceUnavailableRetryStrategy");
      this.backend = var1;
      this.retryStrategy = var2;
   }

   public AutoRetryHttpClient() {
      this(new DefaultHttpClient(), new DefaultServiceUnavailableRetryStrategy());
   }

   public AutoRetryHttpClient(ServiceUnavailableRetryStrategy var1) {
      this(new DefaultHttpClient(), var1);
   }

   public AutoRetryHttpClient(HttpClient var1) {
      this(var1, new DefaultServiceUnavailableRetryStrategy());
   }

   public HttpResponse execute(HttpHost var1, HttpRequest var2) throws IOException {
      Object var3 = null;
      return this.execute((HttpHost)var1, (HttpRequest)var2, (HttpContext)var3);
   }

   public Object execute(HttpHost var1, HttpRequest var2, ResponseHandler var3) throws IOException {
      return this.execute(var1, var2, var3, (HttpContext)null);
   }

   public Object execute(HttpHost var1, HttpRequest var2, ResponseHandler var3, HttpContext var4) throws IOException {
      HttpResponse var5 = this.execute(var1, var2, var4);
      return var3.handleResponse(var5);
   }

   public HttpResponse execute(HttpUriRequest var1) throws IOException {
      Object var2 = null;
      return this.execute((HttpUriRequest)var1, (HttpContext)var2);
   }

   public HttpResponse execute(HttpUriRequest var1, HttpContext var2) throws IOException {
      URI var3 = var1.getURI();
      HttpHost var4 = new HttpHost(var3.getHost(), var3.getPort(), var3.getScheme());
      return this.execute((HttpHost)var4, (HttpRequest)var1, (HttpContext)var2);
   }

   public Object execute(HttpUriRequest var1, ResponseHandler var2) throws IOException {
      return this.execute((HttpUriRequest)var1, (ResponseHandler)var2, (HttpContext)null);
   }

   public Object execute(HttpUriRequest var1, ResponseHandler var2, HttpContext var3) throws IOException {
      HttpResponse var4 = this.execute(var1, var3);
      return var2.handleResponse(var4);
   }

   public HttpResponse execute(HttpHost var1, HttpRequest var2, HttpContext var3) throws IOException {
      int var4 = 1;

      while(true) {
         HttpResponse var5 = this.backend.execute(var1, var2, var3);

         try {
            if (!this.retryStrategy.retryRequest(var5, var4, var3)) {
               return var5;
            }

            EntityUtils.consume(var5.getEntity());
            long var6 = this.retryStrategy.getRetryInterval();

            try {
               this.log.trace("Wait for " + var6);
               Thread.sleep(var6);
            } catch (InterruptedException var10) {
               Thread.currentThread().interrupt();
               throw new InterruptedIOException();
            }
         } catch (RuntimeException var11) {
            try {
               EntityUtils.consume(var5.getEntity());
            } catch (IOException var9) {
               this.log.warn("I/O error consuming response content", var9);
            }

            throw var11;
         }

         ++var4;
      }
   }

   public ClientConnectionManager getConnectionManager() {
      return this.backend.getConnectionManager();
   }

   public HttpParams getParams() {
      return this.backend.getParams();
   }
}
