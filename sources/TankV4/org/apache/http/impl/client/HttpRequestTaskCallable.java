package org.apache.http.impl.client;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.protocol.HttpContext;

class HttpRequestTaskCallable implements Callable {
   private final HttpUriRequest request;
   private final HttpClient httpclient;
   private final AtomicBoolean cancelled = new AtomicBoolean(false);
   private final long scheduled = System.currentTimeMillis();
   private long started = -1L;
   private long ended = -1L;
   private final HttpContext context;
   private final ResponseHandler responseHandler;
   private final FutureCallback callback;
   private final FutureRequestExecutionMetrics metrics;

   HttpRequestTaskCallable(HttpClient var1, HttpUriRequest var2, HttpContext var3, ResponseHandler var4, FutureCallback var5, FutureRequestExecutionMetrics var6) {
      this.httpclient = var1;
      this.responseHandler = var4;
      this.request = var2;
      this.context = var3;
      this.callback = var5;
      this.metrics = var6;
   }

   public long getScheduled() {
      return this.scheduled;
   }

   public long getStarted() {
      return this.started;
   }

   public long getEnded() {
      return this.ended;
   }

   public Object call() throws Exception {
      if (!this.cancelled.get()) {
         this.metrics.getActiveConnections().incrementAndGet();
         this.started = System.currentTimeMillis();

         Object var2;
         try {
            this.metrics.getScheduledConnections().decrementAndGet();
            Object var1 = this.httpclient.execute(this.request, this.responseHandler, this.context);
            this.ended = System.currentTimeMillis();
            this.metrics.getSuccessfulConnections().increment(this.started);
            if (this.callback != null) {
               this.callback.completed(var1);
            }

            var2 = var1;
         } catch (Exception var4) {
            this.metrics.getFailedConnections().increment(this.started);
            this.ended = System.currentTimeMillis();
            if (this.callback != null) {
               this.callback.failed(var4);
            }

            throw var4;
         }

         this.metrics.getRequests().increment(this.started);
         this.metrics.getTasks().increment(this.started);
         this.metrics.getActiveConnections().decrementAndGet();
         return var2;
      } else {
         throw new IllegalStateException("call has been cancelled for request " + this.request.getURI());
      }
   }

   public void cancel() {
      this.cancelled.set(true);
      if (this.callback != null) {
         this.callback.cancelled();
      }

   }
}
