package org.apache.logging.log4j.core.appender.rolling;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Semaphore;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.FileManager;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.rolling.helper.AbstractAction;
import org.apache.logging.log4j.core.appender.rolling.helper.Action;

public class RollingFileManager extends FileManager {
   private static RollingFileManager.RollingFileManagerFactory factory = new RollingFileManager.RollingFileManagerFactory();
   private long size;
   private long initialTime;
   private final PatternProcessor patternProcessor;
   private final Semaphore semaphore = new Semaphore(1);
   private final TriggeringPolicy policy;
   private final RolloverStrategy strategy;

   protected RollingFileManager(String var1, String var2, OutputStream var3, boolean var4, long var5, long var7, TriggeringPolicy var9, RolloverStrategy var10, String var11, Layout var12) {
      super(var1, var3, var4, false, var11, var12);
      this.size = var5;
      this.initialTime = var7;
      this.policy = var9;
      this.strategy = var10;
      this.patternProcessor = new PatternProcessor(var2);
      var9.initialize(this);
   }

   public static RollingFileManager getFileManager(String var0, String var1, boolean var2, boolean var3, TriggeringPolicy var4, RolloverStrategy var5, String var6, Layout var7) {
      return (RollingFileManager)getManager(var0, new RollingFileManager.FactoryData(var1, var2, var3, var4, var5, var6, var7), factory);
   }

   protected synchronized void write(byte[] var1, int var2, int var3) {
      this.size += (long)var3;
      super.write(var1, var2, var3);
   }

   public long getFileSize() {
      return this.size;
   }

   public long getFileTime() {
      return this.initialTime;
   }

   public synchronized void checkRollover(LogEvent var1) {
      if (this.policy.isTriggeringEvent(var1) && this.strategy != null) {
         try {
            this.size = 0L;
            this.initialTime = System.currentTimeMillis();
            this.createFileAfterRollover();
         } catch (IOException var3) {
            LOGGER.error("FileManager (" + this.getFileName() + ") " + var3);
         }
      }

   }

   protected void createFileAfterRollover() throws IOException {
      FileOutputStream var1 = new FileOutputStream(this.getFileName(), this.isAppend());
      this.setOutputStream(var1);
   }

   public PatternProcessor getPatternProcessor() {
      return this.patternProcessor;
   }

   static Semaphore access$100(RollingFileManager var0) {
      return var0.semaphore;
   }

   static Logger access$200() {
      return LOGGER;
   }

   static Logger access$1000() {
      return LOGGER;
   }

   private static class RollingFileManagerFactory implements ManagerFactory {
      private RollingFileManagerFactory() {
      }

      public RollingFileManager createManager(String var1, RollingFileManager.FactoryData var2) {
         File var3 = new File(var1);
         File var4 = var3.getParentFile();
         if (null != var4 && !var4.exists()) {
            var4.mkdirs();
         }

         try {
            var3.createNewFile();
         } catch (IOException var12) {
            RollingFileManager.access$200().error((String)("Unable to create file " + var1), (Throwable)var12);
            return null;
         }

         long var5 = RollingFileManager.FactoryData.access$300(var2) ? var3.length() : 0L;
         long var7 = var3.lastModified();

         try {
            Object var9 = new FileOutputStream(var1, RollingFileManager.FactoryData.access$300(var2));
            if (RollingFileManager.FactoryData.access$400(var2)) {
               var9 = new BufferedOutputStream((OutputStream)var9);
            }

            return new RollingFileManager(var1, RollingFileManager.FactoryData.access$500(var2), (OutputStream)var9, RollingFileManager.FactoryData.access$300(var2), var5, var7, RollingFileManager.FactoryData.access$600(var2), RollingFileManager.FactoryData.access$700(var2), RollingFileManager.FactoryData.access$800(var2), RollingFileManager.FactoryData.access$900(var2));
         } catch (FileNotFoundException var11) {
            RollingFileManager.access$1000().error("FileManager (" + var1 + ") " + var11);
            return null;
         }
      }

      public Object createManager(String var1, Object var2) {
         return this.createManager(var1, (RollingFileManager.FactoryData)var2);
      }

      RollingFileManagerFactory(Object var1) {
         this();
      }
   }

   private static class FactoryData {
      private final String pattern;
      private final boolean append;
      private final boolean bufferedIO;
      private final TriggeringPolicy policy;
      private final RolloverStrategy strategy;
      private final String advertiseURI;
      private final Layout layout;

      public FactoryData(String var1, boolean var2, boolean var3, TriggeringPolicy var4, RolloverStrategy var5, String var6, Layout var7) {
         this.pattern = var1;
         this.append = var2;
         this.bufferedIO = var3;
         this.policy = var4;
         this.strategy = var5;
         this.advertiseURI = var6;
         this.layout = var7;
      }

      static boolean access$300(RollingFileManager.FactoryData var0) {
         return var0.append;
      }

      static boolean access$400(RollingFileManager.FactoryData var0) {
         return var0.bufferedIO;
      }

      static String access$500(RollingFileManager.FactoryData var0) {
         return var0.pattern;
      }

      static TriggeringPolicy access$600(RollingFileManager.FactoryData var0) {
         return var0.policy;
      }

      static RolloverStrategy access$700(RollingFileManager.FactoryData var0) {
         return var0.strategy;
      }

      static String access$800(RollingFileManager.FactoryData var0) {
         return var0.advertiseURI;
      }

      static Layout access$900(RollingFileManager.FactoryData var0) {
         return var0.layout;
      }
   }

   private static class AsyncAction extends AbstractAction {
      private final Action action;
      private final RollingFileManager manager;

      public AsyncAction(Action var1, RollingFileManager var2) {
         this.action = var1;
         this.manager = var2;
      }

      public boolean execute() throws IOException {
         boolean var1 = this.action.execute();
         RollingFileManager.access$100(this.manager).release();
         return var1;
      }

      public void close() {
         this.action.close();
      }

      public boolean isComplete() {
         return this.action.isComplete();
      }
   }
}
