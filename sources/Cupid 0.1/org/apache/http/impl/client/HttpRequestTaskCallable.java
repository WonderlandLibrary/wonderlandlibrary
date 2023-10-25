package org.apache.http.impl.client;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.protocol.HttpContext;

class HttpRequestTaskCallable<V> implements Callable<V> {
  private final HttpUriRequest request;
  
  private final HttpClient httpclient;
  
  private final AtomicBoolean cancelled = new AtomicBoolean(false);
  
  private final long scheduled = System.currentTimeMillis();
  
  private long started = -1L;
  
  private long ended = -1L;
  
  private final HttpContext context;
  
  private final ResponseHandler<V> responseHandler;
  
  private final FutureCallback<V> callback;
  
  private final FutureRequestExecutionMetrics metrics;
  
  HttpRequestTaskCallable(HttpClient httpClient, HttpUriRequest request, HttpContext context, ResponseHandler<V> responseHandler, FutureCallback<V> callback, FutureRequestExecutionMetrics metrics) {
    this.httpclient = httpClient;
    this.responseHandler = responseHandler;
    this.request = request;
    this.context = context;
    this.callback = callback;
    this.metrics = metrics;
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
  
  public V call() throws Exception {
    if (!this.cancelled.get())
      try {
      
      } finally {
        this.metrics.getRequests().increment(this.started);
        this.metrics.getTasks().increment(this.started);
        this.metrics.getActiveConnections().decrementAndGet();
      }  
    throw new IllegalStateException("call has been cancelled for request " + this.request.getURI());
  }
  
  public void cancel() {
    this.cancelled.set(true);
    if (this.callback != null)
      this.callback.cancelled(); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\HttpRequestTaskCallable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */