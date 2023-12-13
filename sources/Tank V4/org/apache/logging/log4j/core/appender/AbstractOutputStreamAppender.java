package org.apache.logging.log4j.core.appender;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;

public abstract class AbstractOutputStreamAppender extends AbstractAppender {
   protected final boolean immediateFlush;
   private volatile OutputStreamManager manager;
   private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
   private final Lock readLock;
   private final Lock writeLock;

   protected AbstractOutputStreamAppender(String var1, Layout var2, Filter var3, boolean var4, boolean var5, OutputStreamManager var6) {
      super(var1, var3, var2, var4);
      this.readLock = this.rwLock.readLock();
      this.writeLock = this.rwLock.writeLock();
      this.manager = var6;
      this.immediateFlush = var5;
   }

   protected OutputStreamManager getManager() {
      return this.manager;
   }

   protected void replaceManager(OutputStreamManager var1) {
      this.writeLock.lock();
      OutputStreamManager var2 = this.manager;
      this.manager = var1;
      var2.release();
      this.writeLock.unlock();
   }

   public void start() {
      if (this.getLayout() == null) {
         LOGGER.error("No layout set for the appender named [" + this.getName() + "].");
      }

      if (this.manager == null) {
         LOGGER.error("No OutputStreamManager set for the appender named [" + this.getName() + "].");
      }

      super.start();
   }

   public void stop() {
      super.stop();
      this.manager.release();
   }

   public void append(LogEvent var1) {
      this.readLock.lock();

      try {
         byte[] var2 = this.getLayout().toByteArray(var1);
         if (var2.length > 0) {
            this.manager.write(var2);
            if (this.immediateFlush || var1.isEndOfBatch()) {
               this.manager.flush();
            }
         }
      } catch (AppenderLoggingException var4) {
         this.error("Unable to write to stream " + this.manager.getName() + " for appender " + this.getName());
         throw var4;
      }

      this.readLock.unlock();
   }
}
