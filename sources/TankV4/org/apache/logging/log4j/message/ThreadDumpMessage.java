package org.apache.logging.log4j.message;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class ThreadDumpMessage implements Message {
   private static final long serialVersionUID = -1103400781608841088L;
   private static final ThreadDumpMessage.ThreadInfoFactory FACTORY;
   private volatile Map threads;
   private final String title;
   private String formattedMessage;

   public ThreadDumpMessage(String var1) {
      this.title = var1 == null ? "" : var1;
      this.threads = FACTORY.createThreadInfo();
   }

   private ThreadDumpMessage(String var1, String var2) {
      this.formattedMessage = var1;
      this.title = var2 == null ? "" : var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("ThreadDumpMessage[");
      if (this.title.length() > 0) {
         var1.append("Title=\"").append(this.title).append("\"");
      }

      var1.append("]");
      return var1.toString();
   }

   public String getFormattedMessage() {
      if (this.formattedMessage != null) {
         return this.formattedMessage;
      } else {
         StringBuilder var1 = new StringBuilder(this.title);
         if (this.title.length() > 0) {
            var1.append("\n");
         }

         Iterator var2 = this.threads.entrySet().iterator();

         while(var2.hasNext()) {
            Entry var3 = (Entry)var2.next();
            ThreadInformation var4 = (ThreadInformation)var3.getKey();
            var4.printThreadInfo(var1);
            var4.printStack(var1, (StackTraceElement[])var3.getValue());
            var1.append("\n");
         }

         return var1.toString();
      }
   }

   public String getFormat() {
      return this.title == null ? "" : this.title;
   }

   public Object[] getParameters() {
      return null;
   }

   protected Object writeReplace() {
      return new ThreadDumpMessage.ThreadDumpMessageProxy(this);
   }

   private void readObject(ObjectInputStream var1) throws InvalidObjectException {
      throw new InvalidObjectException("Proxy required");
   }

   public Throwable getThrowable() {
      return null;
   }

   static String access$200(ThreadDumpMessage var0) {
      return var0.title;
   }

   ThreadDumpMessage(String var1, String var2, Object var3) {
      this(var1, var2);
   }

   static {
      Method[] var0 = ThreadInfo.class.getMethods();
      boolean var1 = true;
      Method[] var2 = var0;
      int var3 = var0.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Method var5 = var2[var4];
         if (var5.getName().equals("getLockInfo")) {
            var1 = false;
            break;
         }
      }

      FACTORY = (ThreadDumpMessage.ThreadInfoFactory)(var1 ? new ThreadDumpMessage.BasicThreadInfoFactory() : new ThreadDumpMessage.ExtendedThreadInfoFactory());
   }

   private static class ExtendedThreadInfoFactory implements ThreadDumpMessage.ThreadInfoFactory {
      private ExtendedThreadInfoFactory() {
      }

      public Map createThreadInfo() {
         ThreadMXBean var1 = ManagementFactory.getThreadMXBean();
         ThreadInfo[] var2 = var1.dumpAllThreads(true, true);
         HashMap var3 = new HashMap(var2.length);
         ThreadInfo[] var4 = var2;
         int var5 = var2.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            ThreadInfo var7 = var4[var6];
            var3.put(new ExtendedThreadInformation(var7), var7.getStackTrace());
         }

         return var3;
      }

      ExtendedThreadInfoFactory(Object var1) {
         this();
      }
   }

   private static class BasicThreadInfoFactory implements ThreadDumpMessage.ThreadInfoFactory {
      private BasicThreadInfoFactory() {
      }

      public Map createThreadInfo() {
         Map var1 = Thread.getAllStackTraces();
         HashMap var2 = new HashMap(var1.size());
         Iterator var3 = var1.entrySet().iterator();

         while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            var2.put(new BasicThreadInformation((Thread)var4.getKey()), var4.getValue());
         }

         return var2;
      }

      BasicThreadInfoFactory(Object var1) {
         this();
      }
   }

   private interface ThreadInfoFactory {
      Map createThreadInfo();
   }

   private static class ThreadDumpMessageProxy implements Serializable {
      private static final long serialVersionUID = -3476620450287648269L;
      private final String formattedMsg;
      private final String title;

      public ThreadDumpMessageProxy(ThreadDumpMessage var1) {
         this.formattedMsg = var1.getFormattedMessage();
         this.title = ThreadDumpMessage.access$200(var1);
      }

      protected Object readResolve() {
         return new ThreadDumpMessage(this.formattedMsg, this.title);
      }
   }
}
