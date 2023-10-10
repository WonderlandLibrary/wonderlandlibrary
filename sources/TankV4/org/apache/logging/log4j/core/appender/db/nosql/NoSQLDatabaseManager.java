package org.apache.logging.log4j.core.appender.db.nosql;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.db.AbstractDatabaseManager;

public final class NoSQLDatabaseManager extends AbstractDatabaseManager {
   private static final NoSQLDatabaseManager.NoSQLDatabaseManagerFactory FACTORY = new NoSQLDatabaseManager.NoSQLDatabaseManagerFactory();
   private final NoSQLProvider provider;
   private NoSQLConnection connection;

   private NoSQLDatabaseManager(String var1, int var2, NoSQLProvider var3) {
      super(var1, var2);
      this.provider = var3;
   }

   protected void connectInternal() {
      this.connection = this.provider.getConnection();
   }

   protected void disconnectInternal() {
      if (this.connection != null && !this.connection.isClosed()) {
         this.connection.close();
      }

   }

   protected void writeInternal(LogEvent var1) {
      if (this.isConnected() && this.connection != null && !this.connection.isClosed()) {
         NoSQLObject var2 = this.connection.createObject();
         var2.set("level", (Object)var1.getLevel());
         var2.set("loggerName", (Object)var1.getLoggerName());
         var2.set("message", (Object)(var1.getMessage() == null ? null : var1.getMessage().getFormattedMessage()));
         StackTraceElement var3 = var1.getSource();
         if (var3 == null) {
            var2.set("source", (Object)null);
         } else {
            var2.set("source", this.convertStackTraceElement(var3));
         }

         Marker var4 = var1.getMarker();
         NoSQLObject var6;
         NoSQLObject var7;
         if (var4 == null) {
            var2.set("marker", (Object)null);
         } else {
            NoSQLObject var5 = this.connection.createObject();
            var6 = var5;
            var5.set("name", (Object)var4.getName());

            while(var4.getParent() != null) {
               var4 = var4.getParent();
               var7 = this.connection.createObject();
               var7.set("name", (Object)var4.getName());
               var6.set("parent", var7);
               var6 = var7;
            }

            var2.set("marker", var5);
         }

         var2.set("threadName", (Object)var1.getThreadName());
         var2.set("millis", (Object)var1.getMillis());
         var2.set("date", (Object)(new Date(var1.getMillis())));
         Throwable var10 = var1.getThrown();
         if (var10 == null) {
            var2.set("thrown", (Object)null);
         } else {
            var6 = this.connection.createObject();
            var7 = var6;
            var6.set("type", (Object)var10.getClass().getName());
            var6.set("message", (Object)var10.getMessage());
            var6.set("stackTrace", this.convertStackTrace(var10.getStackTrace()));

            while(var10.getCause() != null) {
               var10 = var10.getCause();
               NoSQLObject var8 = this.connection.createObject();
               var8.set("type", (Object)var10.getClass().getName());
               var8.set("message", (Object)var10.getMessage());
               var8.set("stackTrace", this.convertStackTrace(var10.getStackTrace()));
               var7.set("cause", var8);
               var7 = var8;
            }

            var2.set("thrown", var6);
         }

         Map var11 = var1.getContextMap();
         if (var11 == null) {
            var2.set("contextMap", (Object)null);
         } else {
            var7 = this.connection.createObject();
            Iterator var12 = var11.entrySet().iterator();

            while(var12.hasNext()) {
               Entry var9 = (Entry)var12.next();
               var7.set((String)var9.getKey(), var9.getValue());
            }

            var2.set("contextMap", var7);
         }

         ThreadContext.ContextStack var13 = var1.getContextStack();
         if (var13 == null) {
            var2.set("contextStack", (Object)null);
         } else {
            var2.set("contextStack", var13.asList().toArray());
         }

         this.connection.insertObject(var2);
      } else {
         throw new AppenderLoggingException("Cannot write logging event; NoSQL manager not connected to the database.");
      }
   }

   private NoSQLObject[] convertStackTrace(StackTraceElement[] var1) {
      NoSQLObject[] var2 = this.connection.createList(var1.length);

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = this.convertStackTraceElement(var1[var3]);
      }

      return var2;
   }

   private NoSQLObject convertStackTraceElement(StackTraceElement var1) {
      NoSQLObject var2 = this.connection.createObject();
      var2.set("className", (Object)var1.getClassName());
      var2.set("methodName", (Object)var1.getMethodName());
      var2.set("fileName", (Object)var1.getFileName());
      var2.set("lineNumber", (Object)var1.getLineNumber());
      return var2;
   }

   public static NoSQLDatabaseManager getNoSQLDatabaseManager(String var0, int var1, NoSQLProvider var2) {
      return (NoSQLDatabaseManager)AbstractDatabaseManager.getManager(var0, new NoSQLDatabaseManager.FactoryData(var1, var2), FACTORY);
   }

   NoSQLDatabaseManager(String var1, int var2, NoSQLProvider var3, Object var4) {
      this(var1, var2, var3);
   }

   private static final class NoSQLDatabaseManagerFactory implements ManagerFactory {
      private NoSQLDatabaseManagerFactory() {
      }

      public NoSQLDatabaseManager createManager(String var1, NoSQLDatabaseManager.FactoryData var2) {
         return new NoSQLDatabaseManager(var1, var2.getBufferSize(), NoSQLDatabaseManager.FactoryData.access$100(var2));
      }

      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (NoSQLDatabaseManager.FactoryData)var2);
      }

      NoSQLDatabaseManagerFactory(Object var1) {
         this();
      }
   }

   private static final class FactoryData extends AbstractDatabaseManager.AbstractFactoryData {
      private final NoSQLProvider provider;

      protected FactoryData(int var1, NoSQLProvider var2) {
         super(var1);
         this.provider = var2;
      }

      static NoSQLProvider access$100(NoSQLDatabaseManager.FactoryData var0) {
         return var0.provider;
      }
   }
}
