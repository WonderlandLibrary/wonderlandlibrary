package org.apache.logging.log4j.core.async;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.Sequence;
import com.lmax.disruptor.SequenceReportingEventHandler;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.Util;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.status.StatusLogger;

class AsyncLoggerConfigHelper {
   private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 20;
   private static final int HALF_A_SECOND = 500;
   private static final int RINGBUFFER_MIN_SIZE = 128;
   private static final int RINGBUFFER_DEFAULT_SIZE = 262144;
   private static final Logger LOGGER = StatusLogger.getLogger();
   private static ThreadFactory threadFactory = new DaemonThreadFactory("AsyncLoggerConfig-");
   private static volatile Disruptor disruptor;
   private static ExecutorService executor;
   private static volatile int count = 0;
   private static final EventFactory FACTORY = new EventFactory() {
      public AsyncLoggerConfigHelper.Log4jEventWrapper newInstance() {
         return new AsyncLoggerConfigHelper.Log4jEventWrapper();
      }

      public Object newInstance() {
         return this.newInstance();
      }
   };
   private final EventTranslator translator = new EventTranslator(this) {
      final AsyncLoggerConfigHelper this$0;

      {
         this.this$0 = var1;
      }

      public void translateTo(AsyncLoggerConfigHelper.Log4jEventWrapper var1, long var2) {
         AsyncLoggerConfigHelper.Log4jEventWrapper.access$102(var1, (LogEvent)AsyncLoggerConfigHelper.access$200(this.this$0).get());
         AsyncLoggerConfigHelper.Log4jEventWrapper.access$302(var1, AsyncLoggerConfigHelper.access$400(this.this$0));
      }

      public void translateTo(Object var1, long var2) {
         this.translateTo((AsyncLoggerConfigHelper.Log4jEventWrapper)var1, var2);
      }
   };
   private final ThreadLocal currentLogEvent = new ThreadLocal();
   private final AsyncLoggerConfig asyncLoggerConfig;

   public AsyncLoggerConfigHelper(AsyncLoggerConfig var1) {
      this.asyncLoggerConfig = var1;
      claim();
   }

   private static synchronized void initDisruptor() {
      if (disruptor != null) {
         LOGGER.trace("AsyncLoggerConfigHelper not starting new disruptor, using existing object. Ref count is {}.", count);
      } else {
         LOGGER.trace("AsyncLoggerConfigHelper creating new disruptor. Ref count is {}.", count);
         int var0 = calculateRingBufferSize();
         WaitStrategy var1 = createWaitStrategy();
         executor = Executors.newSingleThreadExecutor(threadFactory);
         disruptor = new Disruptor(FACTORY, var0, executor, ProducerType.MULTI, var1);
         AsyncLoggerConfigHelper.Log4jEventWrapperHandler[] var2 = new AsyncLoggerConfigHelper.Log4jEventWrapperHandler[]{new AsyncLoggerConfigHelper.Log4jEventWrapperHandler()};
         ExceptionHandler var3 = getExceptionHandler();
         disruptor.handleExceptionsWith(var3);
         disruptor.handleEventsWith(var2);
         LOGGER.debug("Starting AsyncLoggerConfig disruptor with ringbuffer size={}, waitStrategy={}, exceptionHandler={}...", disruptor.getRingBuffer().getBufferSize(), var1.getClass().getSimpleName(), var3);
         disruptor.start();
      }
   }

   private static WaitStrategy createWaitStrategy() {
      String var0 = System.getProperty("AsyncLoggerConfig.WaitStrategy");
      LOGGER.debug("property AsyncLoggerConfig.WaitStrategy={}", var0);
      if ("Sleep".equals(var0)) {
         return new SleepingWaitStrategy();
      } else if ("Yield".equals(var0)) {
         return new YieldingWaitStrategy();
      } else {
         return (WaitStrategy)("Block".equals(var0) ? new BlockingWaitStrategy() : new SleepingWaitStrategy());
      }
   }

   private static int calculateRingBufferSize() {
      int var0 = 262144;
      String var1 = System.getProperty("AsyncLoggerConfig.RingBufferSize", String.valueOf(var0));

      try {
         int var2 = Integer.parseInt(var1);
         if (var2 < 128) {
            var2 = 128;
            LOGGER.warn("Invalid RingBufferSize {}, using minimum size {}.", var1, 128);
         }

         var0 = var2;
      } catch (Exception var3) {
         LOGGER.warn("Invalid RingBufferSize {}, using default size {}.", var1, var0);
      }

      return Util.ceilingNextPowerOfTwo(var0);
   }

   private static ExceptionHandler getExceptionHandler() {
      String var0 = System.getProperty("AsyncLoggerConfig.ExceptionHandler");
      if (var0 == null) {
         return null;
      } else {
         try {
            Class var1 = Class.forName(var0);
            ExceptionHandler var2 = (ExceptionHandler)var1.newInstance();
            return var2;
         } catch (Exception var3) {
            LOGGER.debug((String)("AsyncLoggerConfig.ExceptionHandler not set: error creating " + var0 + ": "), (Throwable)var3);
            return null;
         }
      }
   }

   static synchronized void claim() {
      ++count;
      initDisruptor();
   }

   static synchronized void release() {
      if (--count > 0) {
         LOGGER.trace("AsyncLoggerConfigHelper: not shutting down disruptor: ref count is {}.", count);
      } else {
         Disruptor var0 = disruptor;
         if (var0 == null) {
            LOGGER.trace("AsyncLoggerConfigHelper: disruptor already shut down: ref count is {}.", count);
         } else {
            LOGGER.trace("AsyncLoggerConfigHelper: shutting down disruptor: ref count is {}.", count);
            disruptor = null;
            var0.shutdown();
            RingBuffer var1 = var0.getRingBuffer();

            for(int var2 = 0; var2 < 20 && !var1.hasAvailableCapacity(var1.getBufferSize()); ++var2) {
               try {
                  Thread.sleep(500L);
               } catch (InterruptedException var4) {
               }
            }

            executor.shutdown();
            executor = null;
         }
      }
   }

   public void callAppendersFromAnotherThread(LogEvent var1) {
      this.currentLogEvent.set(var1);
      disruptor.publishEvent(this.translator);
   }

   static ThreadLocal access$200(AsyncLoggerConfigHelper var0) {
      return var0.currentLogEvent;
   }

   static AsyncLoggerConfig access$400(AsyncLoggerConfigHelper var0) {
      return var0.asyncLoggerConfig;
   }

   private static class Log4jEventWrapperHandler implements SequenceReportingEventHandler {
      private static final int NOTIFY_PROGRESS_THRESHOLD = 50;
      private Sequence sequenceCallback;
      private int counter;

      private Log4jEventWrapperHandler() {
      }

      public void setSequenceCallback(Sequence var1) {
         this.sequenceCallback = var1;
      }

      public void onEvent(AsyncLoggerConfigHelper.Log4jEventWrapper var1, long var2, boolean var4) throws Exception {
         AsyncLoggerConfigHelper.Log4jEventWrapper.access$100(var1).setEndOfBatch(var4);
         AsyncLoggerConfigHelper.Log4jEventWrapper.access$300(var1).asyncCallAppenders(AsyncLoggerConfigHelper.Log4jEventWrapper.access$100(var1));
         var1.clear();
         if (++this.counter > 50) {
            this.sequenceCallback.set(var2);
            this.counter = 0;
         }

      }

      public void onEvent(Object var1, long var2, boolean var4) throws Exception {
         this.onEvent((AsyncLoggerConfigHelper.Log4jEventWrapper)var1, var2, var4);
      }

      Log4jEventWrapperHandler(Object var1) {
         this();
      }
   }

   private static class Log4jEventWrapper {
      private AsyncLoggerConfig loggerConfig;
      private LogEvent event;

      private Log4jEventWrapper() {
      }

      public void clear() {
         this.loggerConfig = null;
         this.event = null;
      }

      Log4jEventWrapper(Object var1) {
         this();
      }

      static LogEvent access$102(AsyncLoggerConfigHelper.Log4jEventWrapper var0, LogEvent var1) {
         return var0.event = var1;
      }

      static AsyncLoggerConfig access$302(AsyncLoggerConfigHelper.Log4jEventWrapper var0, AsyncLoggerConfig var1) {
         return var0.loggerConfig = var1;
      }

      static LogEvent access$100(AsyncLoggerConfigHelper.Log4jEventWrapper var0) {
         return var0.event;
      }

      static AsyncLoggerConfig access$300(AsyncLoggerConfigHelper.Log4jEventWrapper var0) {
         return var0.loggerConfig;
      }
   }
}
