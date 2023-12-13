package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Beta
public abstract class AbstractIdleService implements Service {
   private final Supplier threadNameSupplier = new Supplier(this) {
      final AbstractIdleService this$0;

      {
         this.this$0 = var1;
      }

      public String get() {
         return this.this$0.serviceName() + " " + this.this$0.state();
      }

      public Object get() {
         return this.get();
      }
   };
   private final Service delegate = new AbstractService(this) {
      final AbstractIdleService this$0;

      {
         this.this$0 = var1;
      }

      protected final void doStart() {
         MoreExecutors.renamingDecorator(this.this$0.executor(), AbstractIdleService.access$000(this.this$0)).execute(new Runnable(this) {
            final <undefinedtype> this$1;

            {
               this.this$1 = var1;
            }

            public void run() {
               try {
                  this.this$1.this$0.startUp();
                  this.this$1.notifyStarted();
               } catch (Throwable var2) {
                  this.this$1.notifyFailed(var2);
                  throw Throwables.propagate(var2);
               }
            }
         });
      }

      protected final void doStop() {
         MoreExecutors.renamingDecorator(this.this$0.executor(), AbstractIdleService.access$000(this.this$0)).execute(new Runnable(this) {
            final <undefinedtype> this$1;

            {
               this.this$1 = var1;
            }

            public void run() {
               try {
                  this.this$1.this$0.shutDown();
                  this.this$1.notifyStopped();
               } catch (Throwable var2) {
                  this.this$1.notifyFailed(var2);
                  throw Throwables.propagate(var2);
               }
            }
         });
      }
   };

   protected AbstractIdleService() {
   }

   protected abstract void startUp() throws Exception;

   protected abstract void shutDown() throws Exception;

   protected Executor executor() {
      return new Executor(this) {
         final AbstractIdleService this$0;

         {
            this.this$0 = var1;
         }

         public void execute(Runnable var1) {
            MoreExecutors.newThread((String)AbstractIdleService.access$000(this.this$0).get(), var1).start();
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

   static Supplier access$000(AbstractIdleService var0) {
      return var0.threadNameSupplier;
   }
}
