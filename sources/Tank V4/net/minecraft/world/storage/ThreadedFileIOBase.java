package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

public class ThreadedFileIOBase implements Runnable {
   private volatile long writeQueuedCounter;
   private volatile boolean isThreadWaiting;
   private List threadedIOQueue = Collections.synchronizedList(Lists.newArrayList());
   private volatile long savedIOCounter;
   private static final ThreadedFileIOBase threadedIOInstance = new ThreadedFileIOBase();

   private void processQueue() {
      for(int var1 = 0; var1 < this.threadedIOQueue.size(); ++var1) {
         IThreadedFileIO var2 = (IThreadedFileIO)this.threadedIOQueue.get(var1);
         boolean var3 = var2.writeNextIO();
         if (!var3) {
            this.threadedIOQueue.remove(var1--);
            ++this.savedIOCounter;
         }

         try {
            Thread.sleep(this.isThreadWaiting ? 0L : 10L);
         } catch (InterruptedException var6) {
            var6.printStackTrace();
         }
      }

      if (this.threadedIOQueue.isEmpty()) {
         try {
            Thread.sleep(25L);
         } catch (InterruptedException var5) {
            var5.printStackTrace();
         }
      }

   }

   public static ThreadedFileIOBase getThreadedIOInstance() {
      return threadedIOInstance;
   }

   private ThreadedFileIOBase() {
      Thread var1 = new Thread(this, "File IO Thread");
      var1.setPriority(1);
      var1.start();
   }

   public void run() {
      this.processQueue();
   }

   public void waitForFinish() throws InterruptedException {
      this.isThreadWaiting = true;

      while(this.writeQueuedCounter != this.savedIOCounter) {
         Thread.sleep(10L);
      }

      this.isThreadWaiting = false;
   }

   public void queueIO(IThreadedFileIO var1) {
      if (!this.threadedIOQueue.contains(var1)) {
         ++this.writeQueuedCounter;
         this.threadedIOQueue.add(var1);
      }

   }
}
