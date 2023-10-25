package org.apache.http.impl.client;

import java.util.concurrent.FutureTask;
import org.apache.http.client.methods.HttpUriRequest;

public class HttpRequestFutureTask<V> extends FutureTask<V> {
  private final HttpUriRequest request;
  
  private final HttpRequestTaskCallable<V> callable;
  
  public HttpRequestFutureTask(HttpUriRequest request, HttpRequestTaskCallable<V> httpCallable) {
    super(httpCallable);
    this.request = request;
    this.callable = httpCallable;
  }
  
  public boolean cancel(boolean mayInterruptIfRunning) {
    this.callable.cancel();
    if (mayInterruptIfRunning)
      this.request.abort(); 
    return super.cancel(mayInterruptIfRunning);
  }
  
  public long scheduledTime() {
    return this.callable.getScheduled();
  }
  
  public long startedTime() {
    return this.callable.getStarted();
  }
  
  public long endedTime() {
    if (isDone())
      return this.callable.getEnded(); 
    throw new IllegalStateException("Task is not done yet");
  }
  
  public long requestDuration() {
    if (isDone())
      return endedTime() - startedTime(); 
    throw new IllegalStateException("Task is not done yet");
  }
  
  public long taskDuration() {
    if (isDone())
      return endedTime() - scheduledTime(); 
    throw new IllegalStateException("Task is not done yet");
  }
  
  public String toString() {
    return this.request.getRequestLine().getUri();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\http\impl\client\HttpRequestFutureTask.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */