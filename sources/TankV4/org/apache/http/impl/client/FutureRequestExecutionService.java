package org.apache.http.impl.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.protocol.HttpContext;

@ThreadSafe
public class FutureRequestExecutionService implements Closeable {
   private final HttpClient httpclient;
   private final ExecutorService executorService;
   private final FutureRequestExecutionMetrics metrics = new FutureRequestExecutionMetrics();
   private final AtomicBoolean closed = new AtomicBoolean(false);

   public FutureRequestExecutionService(HttpClient var1, ExecutorService var2) {
      this.httpclient = var1;
      this.executorService = var2;
   }

   public HttpRequestFutureTask execute(HttpUriRequest var1, HttpContext var2, ResponseHandler var3) {
      return this.execute(var1, var2, var3, (FutureCallback)null);
   }

   public HttpRequestFutureTask execute(HttpUriRequest var1, HttpContext var2, ResponseHandler var3, FutureCallback var4) {
      if (this.closed.get()) {
         throw new IllegalStateException("Close has been called on this httpclient instance.");
      } else {
         this.metrics.getScheduledConnections().incrementAndGet();
         HttpRequestTaskCallable var5 = new HttpRequestTaskCallable(this.httpclient, var1, var2, var3, var4, this.metrics);
         HttpRequestFutureTask var6 = new HttpRequestFutureTask(var1, var5);
         this.executorService.execute(var6);
         return var6;
      }
   }

   public FutureRequestExecutionMetrics metrics() {
      return this.metrics;
   }

   public void close() throws IOException {
      this.closed.set(true);
      this.executorService.shutdownNow();
      if (this.httpclient instanceof Closeable) {
         ((Closeable)this.httpclient).close();
      }

   }
}
