package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Beta
public abstract class AbstractExecutionThreadService implements Service {
   private static final Logger logger = Logger.getLogger(AbstractExecutionThreadService.class.getName());
   private final Service delegate = new AbstractService(this) {
      final AbstractExecutionThreadService this$0;

      {
         this.this$0 = var1;
      }

      protected final void doStart() {
         Executor var1 = MoreExecutors.renamingDecorator(this.this$0.executor(), new Supplier(this) {
            final <undefinedtype> this$1;

            {
               this.this$1 = var1;
            }

            public String get() {
               return this.this$1.this$0.serviceName();
            }

            public Object get() {
               return this.get();
            }
         });
         var1.execute(new Runnable(this) {
            final <undefinedtype> this$1;

            {
               this.this$1 = var1;
            }

            public void run() {
               try {
                  this.this$1.this$0.startUp();
                  this.this$1.notifyStarted();
                  if (this.this$1.isRunning()) {
                     try {
                        this.this$1.this$0.run();
                     } catch (Throwable var4) {
                        try {
                           this.this$1.this$0.shutDown();
                        } catch (Exception var3) {
                           AbstractExecutionThreadService.access$000().log(Level.WARNING, "Error while attempting to shut down the service after failure.", var3);
                        }

                        throw var4;
                     }
                  }

                  this.this$1.this$0.shutDown();
                  this.this$1.notifyStopped();
               } catch (Throwable var5) {
                  this.this$1.notifyFailed(var5);
                  throw Throwables.propagate(var5);
               }
            }
         });
      }

      protected void doStop() {
         this.this$0.triggerShutdown();
      }
   };

   protected AbstractExecutionThreadService() {
   }

   protected void startUp() throws Exception {
   }

   protected abstract void run() throws Exception;

   protected void shutDown() throws Exception {
   }

   protected void triggerShutdown() {
   }

   protected Executor executor() {
      return new Executor(this) {
         final AbstractExecutionThreadService this$0;

         {
            this.this$0 = var1;
         }

         public void execute(Runnable var1) {
            MoreExecutors.newThread(this.this$0.serviceName(), var1).start();
         }
      };
   }

   public String toString() {
      return this.serviceName() + " [" + this.state() + "]";
   }

   public final boolean isRunning() {
      return this.delegate.isRunning();
   }

   public final Service.State state() {
      return this.delegate.state();
   }

   public final void addListener(Service.Listener var1, Executor var2) {
      this.delegate.addListener(var1, var2);
   }

   public final Throwable failureCause() {
      return this.delegate.failureCause();
   }

   public final Service startAsync() {
      this.delegate.startAsync();
      return this;
   }

   public final Service stopAsync() {
      this.delegate.stopAsync();
      return this;
   }

   public final void awaitRunning() {
      this.delegate.awaitRunning();
   }

   public final void awaitRunning(long var1, TimeUnit var3) throws TimeoutException {
      this.delegate.awaitRunning(var1, var3);
   }

   public final void awaitTerminated() {
      this.delegate.awaitTerminated();
   }

   public final void awaitTerminated(long var1, TimeUnit var3) throws TimeoutException {
      this.delegate.awaitTerminated(var1, var3);
   }

   protected String serviceName() {
      return this.getClass().getSimpleName();
   }

   static Logger access$000() {
      return logger;
   }
}
