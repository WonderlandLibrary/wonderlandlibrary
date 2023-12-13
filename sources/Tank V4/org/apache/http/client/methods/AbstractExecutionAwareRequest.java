package org.apache.http.client.methods;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.http.HttpRequest;
import org.apache.http.client.utils.CloneUtils;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionReleaseTrigger;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.message.HeaderGroup;
import org.apache.http.params.HttpParams;

public abstract class AbstractExecutionAwareRequest extends AbstractHttpMessage implements HttpExecutionAware, AbortableHttpRequest, Cloneable, HttpRequest {
   private Lock abortLock = new ReentrantLock();
   private volatile boolean aborted;
   private volatile Cancellable cancellable;

   protected AbstractExecutionAwareRequest() {
   }

   /** @deprecated */
   @Deprecated
   public void setConnectionRequest(ClientConnectionRequest var1) {
      if (!this.aborted) {
         this.abortLock.lock();
         this.cancellable = new Cancellable(this, var1) {
            final ClientConnectionRequest val$connRequest;
            final AbstractExecutionAwareRequest this$0;

            {
               this.this$0 = var1;
               this.val$connRequest = var2;
            }

            public boolean cancel() {
               this.val$connRequest.abortRequest();
               return true;
            }
         };
         this.abortLock.unlock();
      }
   }

   /** @deprecated */
   @Deprecated
   public void setReleaseTrigger(ConnectionReleaseTrigger var1) {
      if (!this.aborted) {
         this.abortLock.lock();
         this.cancellable = new Cancellable(this, var1) {
            final ConnectionReleaseTrigger val$releaseTrigger;
            final AbstractExecutionAwareRequest this$0;

            {
               this.this$0 = var1;
               this.val$releaseTrigger = var2;
            }

            public boolean cancel() {
               try {
                  this.val$releaseTrigger.abortConnection();
                  return true;
               } catch (IOException var2) {
                  return false;
               }
            }
         };
         this.abortLock.unlock();
      }
   }

   private void cancelExecution() {
      if (this.cancellable != null) {
         this.cancellable.cancel();
         this.cancellable = null;
      }

   }

   public void abort() {
      if (!this.aborted) {
         this.abortLock.lock();
         this.aborted = true;
         this.cancelExecution();
         this.abortLock.unlock();
      }
   }

   public boolean isAborted() {
      return this.aborted;
   }

   public void setCancellable(Cancellable var1) {
      if (!this.aborted) {
         this.abortLock.lock();
         this.cancellable = var1;
         this.abortLock.unlock();
      }
   }

   public Object clone() throws CloneNotSupportedException {
      AbstractExecutionAwareRequest var1 = (AbstractExecutionAwareRequest)super.clone();
      var1.headergroup = (HeaderGroup)CloneUtils.cloneObject(this.headergroup);
      var1.params = (HttpParams)CloneUtils.cloneObject(this.params);
      var1.abortLock = new ReentrantLock();
      var1.cancellable = null;
      var1.aborted = false;
      return var1;
   }

   public void completed() {
      this.abortLock.lock();
      this.cancellable = null;
      this.abortLock.unlock();
   }

   public void reset() {
      this.abortLock.lock();
      this.cancelExecution();
      this.aborted = false;
      this.abortLock.unlock();
   }
}
