package org.apache.http.impl.conn.tsccm;

/** @deprecated */
@Deprecated
public class WaitingThreadAborter {
   private WaitingThread waitingThread;
   private boolean aborted;

   public void abort() {
      this.aborted = true;
      if (this.waitingThread != null) {
         this.waitingThread.interrupt();
      }

   }

   public void setWaitingThread(WaitingThread var1) {
      this.waitingThread = var1;
      if (this.aborted) {
         var1.interrupt();
      }

   }
}
