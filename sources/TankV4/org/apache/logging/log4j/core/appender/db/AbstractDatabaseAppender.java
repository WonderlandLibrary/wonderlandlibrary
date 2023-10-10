package org.apache.logging.log4j.core.appender.db;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.logging.log4j.LoggingException;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;

public abstract class AbstractDatabaseAppender extends AbstractAppender {
   private final ReadWriteLock lock = new ReentrantReadWriteLock();
   private final Lock readLock;
   private final Lock writeLock;
   private AbstractDatabaseManager manager;

   protected AbstractDatabaseAppender(String var1, Filter var2, boolean var3, AbstractDatabaseManager var4) {
      super(var1, var2, (Layout)null, var3);
      this.readLock = this.lock.readLock();
      this.writeLock = this.lock.writeLock();
      this.manager = var4;
   }

   public final Layout getLayout() {
      return null;
   }

   public final AbstractDatabaseManager getManager() {
      return this.manager;
   }

   public final void start() {
      if (this.getManager() == null) {
         LOGGER.error("No AbstractDatabaseManager set for the appender named [{}].", this.getName());
      }

      super.start();
      if (this.getManager() != null) {
         this.getManager().connect();
      }

   }

   public final void stop() {
      super.stop();
      if (this.getManager() != null) {
         this.getManager().release();
      }

   }

   public final void append(LogEvent var1) {
      this.readLock.lock();

      try {
         this.getManager().write(var1);
      } catch (LoggingException var4) {
         LOGGER.error("Unable to write to database [{}] for appender [{}].", this.getManager().getName(), this.getName(), var4);
         throw var4;
      } catch (Exception var5) {
         LOGGER.error("Unable to write to database [{}] for appender [{}].", this.getManager().getName(), this.getName(), var5);
         throw new AppenderLoggingException("Unable to write to database in appender: " + var5.getMessage(), var5);
      }

      this.readLock.unlock();
   }

   protected final void replaceManager(AbstractDatabaseManager var1) {
      this.writeLock.lock();
      AbstractDatabaseManager var2 = this.getManager();
      if (!var1.isConnected()) {
         var1.connect();
      }

      this.manager = var1;
      var2.release();
      this.writeLock.unlock();
   }
}
