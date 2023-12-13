package com.ibm.icu.impl;

public class ICURWLock {
   private Object writeLock = new Object();
   private Object readLock = new Object();
   private int wwc;
   private int rc;
   private int wrc;
   private ICURWLock.Stats stats = new ICURWLock.Stats();
   private static final int NOTIFY_NONE = 0;
   private static final int NOTIFY_WRITERS = 1;
   private static final int NOTIFY_READERS = 2;

   public synchronized ICURWLock.Stats resetStats() {
      ICURWLock.Stats var1 = this.stats;
      this.stats = new ICURWLock.Stats();
      return var1;
   }

   public synchronized ICURWLock.Stats clearStats() {
      ICURWLock.Stats var1 = this.stats;
      this.stats = null;
      return var1;
   }

   public synchronized ICURWLock.Stats getStats() {
      return this.stats == null ? null : new ICURWLock.Stats(this.stats);
   }

   private synchronized boolean gotRead() {
      ++this.rc;
      if (this.stats != null) {
         ++this.stats._rc;
         if (this.rc > 1) {
            ++this.stats._mrc;
         }
      }

      return true;
   }

   private synchronized boolean gotWrite() {
      this.rc = -1;
      if (this.stats != null) {
         ++this.stats._wc;
      }

      return true;
   }

   private synchronized int finishWrite() {
      if (this.rc < 0) {
         this.rc = 0;
         if (this.wwc > 0) {
            return 1;
         } else {
            return this.wrc > 0 ? 2 : 0;
         }
      } else {
         throw new IllegalStateException("no current writer to release");
      }
   }

   public void acquireRead() {
      if (this >= 0) {
         while(true) {
            try {
               Object var1;
               synchronized(var1 = this.readLock){}
               this.readLock.wait();
               if (this != null) {
                  return;
               }
            } catch (InterruptedException var3) {
            }
         }
      }
   }

   public void releaseRead() {
      if (this > 0) {
         Object var1;
         synchronized(var1 = this.writeLock){}
         this.writeLock.notify();
      }

   }

   public void acquireWrite() {
      if (this == false) {
         while(true) {
            try {
               Object var1;
               synchronized(var1 = this.writeLock){}
               this.writeLock.wait();
               if (this != null) {
                  return;
               }
            } catch (InterruptedException var3) {
            }
         }
      }
   }

   public void releaseWrite() {
      Object var1;
      switch(this.finishWrite()) {
      case 0:
      default:
         break;
      case 1:
         synchronized(var1 = this.writeLock){}
         this.writeLock.notify();
         break;
      case 2:
         synchronized(var1 = this.readLock){}
         this.readLock.notifyAll();
      }

   }

   public static final class Stats {
      public int _rc;
      public int _mrc;
      public int _wrc;
      public int _wc;
      public int _wwc;

      private Stats() {
      }

      private Stats(int var1, int var2, int var3, int var4, int var5) {
         this._rc = var1;
         this._mrc = var2;
         this._wrc = var3;
         this._wc = var4;
         this._wwc = var5;
      }

      private Stats(ICURWLock.Stats var1) {
         this(var1._rc, var1._mrc, var1._wrc, var1._wc, var1._wwc);
      }

      public String toString() {
         return " rc: " + this._rc + " mrc: " + this._mrc + " wrc: " + this._wrc + " wc: " + this._wc + " wwc: " + this._wwc;
      }

      Stats(Object var1) {
         this();
      }

      Stats(ICURWLock.Stats var1, Object var2) {
         this(var1);
      }
   }
}
