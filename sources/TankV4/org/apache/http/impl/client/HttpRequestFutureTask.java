package org.apache.http.impl.client;

import java.util.concurrent.FutureTask;
import org.apache.http.client.methods.HttpUriRequest;

public class HttpRequestFutureTask extends FutureTask {
   private final HttpUriRequest request;
   private final HttpRequestTaskCallable callable;

   public HttpRequestFutureTask(HttpUriRequest var1, HttpRequestTaskCallable var2) {
      super(var2);
      this.request = var1;
      this.callable = var2;
   }

   public boolean cancel(boolean var1) {
      this.callable.cancel();
      if (var1) {
         this.request.abort();
      }

      return super.cancel(var1);
   }

   public long scheduledTime() {
      return this.callable.getScheduled();
   }

   public long startedTime() {
      return this.callable.getStarted();
   }

   public long endedTime() {
      if (this.isDone()) {
         return this.callable.getEnded();
      } else {
         throw new IllegalStateException("Task is not done yet");
      }
   }

   public long requestDuration() {
      if (this.isDone()) {
         return this.endedTime() - this.startedTime();
      } else {
         throw new IllegalStateException("Task is not done yet");
      }
   }

   public long taskDuration() {
      if (this.isDone()) {
         return this.endedTime() - this.scheduledTime();
      } else {
         throw new IllegalStateException("Task is not done yet");
      }
   }

   public String toString() {
      return this.request.getRequestLine().getUri();
   }
}
