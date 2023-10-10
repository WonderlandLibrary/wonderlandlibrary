package org.apache.logging.log4j.core.appender.db;

import java.util.ArrayList;
import java.util.Iterator;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;

public abstract class AbstractDatabaseManager extends AbstractManager {
   private final ArrayList buffer;
   private final int bufferSize;
   private boolean connected = false;

   protected AbstractDatabaseManager(String var1, int var2) {
      super(var1);
      this.bufferSize = var2;
      this.buffer = new ArrayList(var2 + 1);
   }

   protected abstract void connectInternal() throws Exception;

   public final synchronized void connect() {
      if (!this.isConnected()) {
         try {
            this.connectInternal();
            this.connected = true;
         } catch (Exception var2) {
            LOGGER.error("Could not connect to database using logging manager [{}].", this.getName(), var2);
         }
      }

   }

   protected abstract void disconnectInternal() throws Exception;

   public final synchronized void disconnect() {
      this.flush();
      if (this.isConnected()) {
         try {
            this.disconnectInternal();
         } catch (Exception var3) {
            LOGGER.warn("Error while disconnecting from database using logging manager [{}].", this.getName(), var3);
            this.connected = false;
            return;
         }

         this.connected = false;
      }

   }

   public final boolean isConnected() {
      return this.connected;
   }

   protected abstract void writeInternal(LogEvent var1);

   public final synchronized void flush() {
      if (this.isConnected() && this.buffer.size() > 0) {
         Iterator var1 = this.buffer.iterator();

         while(var1.hasNext()) {
            LogEvent var2 = (LogEvent)var1.next();
            this.writeInternal(var2);
         }

         this.buffer.clear();
      }

   }

   public final synchronized void write(LogEvent var1) {
      if (this.bufferSize > 0) {
         this.buffer.add(var1);
         if (this.buffer.size() >= this.bufferSize || var1.isEndOfBatch()) {
            this.flush();
         }
      } else {
         this.writeInternal(var1);
      }

   }

   public final void releaseSub() {
      this.disconnect();
   }

   public final String toString() {
      return this.getName();
   }

   protected static AbstractDatabaseManager getManager(String var0, AbstractDatabaseManager.AbstractFactoryData var1, ManagerFactory var2) {
      return (AbstractDatabaseManager)AbstractManager.getManager(var0, var2, var1);
   }

   protected abstract static class AbstractFactoryData {
      private final int bufferSize;

      protected AbstractFactoryData(int var1) {
         this.bufferSize = var1;
      }

      public int getBufferSize() {
         return this.bufferSize;
      }
   }
}
