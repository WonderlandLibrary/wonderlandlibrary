package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.Immutable;

@Beta
public abstract class AbstractService implements Service {
   private static final ListenerCallQueue.Callback STARTING_CALLBACK = new ListenerCallQueue.Callback("starting()") {
      void call(Service.Listener var1) {
         var1.starting();
      }

      void call(Object var1) {
         this.call((Service.Listener)var1);
      }
   };
   private static final ListenerCallQueue.Callback RUNNING_CALLBACK = new ListenerCallQueue.Callback("running()") {
      void call(Service.Listener var1) {
         var1.running();
      }

      void call(Object var1) {
         this.call((Service.Listener)var1);
      }
   };
   private static final ListenerCallQueue.Callback STOPPING_FROM_STARTING_CALLBACK;
   private static final ListenerCallQueue.Callback STOPPING_FROM_RUNNING_CALLBACK;
   private static final ListenerCallQueue.Callback TERMINATED_FROM_NEW_CALLBACK;
   private static final ListenerCallQueue.Callback TERMINATED_FROM_RUNNING_CALLBACK;
   private static final ListenerCallQueue.Callback TERMINATED_FROM_STOPPING_CALLBACK;
   private final Monitor monitor = new Monitor();
   private final Monitor.Guard isStartable;
   private final Monitor.Guard isStoppable;
   private final Monitor.Guard hasReachedRunning;
   private final Monitor.Guard isStopped;
   @GuardedBy("monitor")
   private final List listeners;
   @GuardedBy("monitor")
   private volatile AbstractService.StateSnapshot snapshot;

   private static ListenerCallQueue.Callback terminatedCallback(Service.State var0) {
      return new ListenerCallQueue.Callback("terminated({from = " + var0 + "})", var0) {
         final Service.State val$from;

         {
            this.val$from = var2;
         }

         void call(Service.Listener var1) {
            var1.terminated(this.val$from);
         }

         void call(Object var1) {
            this.call((Service.Listener)var1);
         }
      };
   }

   private static ListenerCallQueue.Callback stoppingCallback(Service.State var0) {
      return new ListenerCallQueue.Callback("stopping({from = " + var0 + "})", var0) {
         final Service.State val$from;

         {
            this.val$from = var2;
         }

         void call(Service.Listener var1) {
            var1.stopping(this.val$from);
         }

         void call(Object var1) {
            this.call((Service.Listener)var1);
         }
      };
   }

   protected AbstractService() {
      this.isStartable = new Monitor.Guard(this, this.monitor) {
         final AbstractService this$0;

         {
            this.this$0 = var1;
         }

         public boolean isSatisfied() {
            return this.this$0.state() == Service.State.NEW;
         }
      };
      this.isStoppable = new Monitor.Guard(this, this.monitor) {
         final AbstractService this$0;

         {
            this.this$0 = var1;
         }

         public boolean isSatisfied() {
            return this.this$0.state().compareTo(Service.State.RUNNING) <= 0;
         }
      };
      this.hasReachedRunning = new Monitor.Guard(this, this.monitor) {
         final AbstractService this$0;

         {
            this.this$0 = var1;
         }

         public boolean isSatisfied() {
            return this.this$0.state().compareTo(Service.State.RUNNING) >= 0;
         }
      };
      this.isStopped = new Monitor.Guard(this, this.monitor) {
         final AbstractService this$0;

         {
            this.this$0 = var1;
         }

         public boolean isSatisfied() {
            return this.this$0.state().isTerminal();
         }
      };
      this.listeners = Collections.synchronizedList(new ArrayList());
      this.snapshot = new AbstractService.StateSnapshot(Service.State.NEW);
   }

   protected abstract void doStart();

   protected abstract void doStop();

   public final Service startAsync() {
      if (this.monitor.enterIf(this.isStartable)) {
         try {
            this.snapshot = new AbstractService.StateSnapshot(Service.State.STARTING);
            this.starting();
            this.doStart();
         } catch (Throwable var3) {
            this.notifyFailed(var3);
            this.monitor.leave();
            this.executeListeners();
            return this;
         }

         this.monitor.leave();
         this.executeListeners();
         return this;
      } else {
         throw new IllegalStateException("Service " + this + " has already been started");
      }
   }

   public final Service stopAsync() {
      if (this.monitor.enterIf(this.isStoppable)) {
         try {
            Service.State var1 = this.state();
            switch(var1) {
            case NEW:
               this.snapshot = new AbstractService.StateSnapshot(Service.State.TERMINATED);
               this.terminated(Service.State.NEW);
               break;
            case STARTING:
               this.snapshot = new AbstractService.StateSnapshot(Service.State.STARTING, true, (Throwable)null);
               this.stopping(Service.State.STARTING);
               break;
            case RUNNING:
               this.snapshot = new AbstractService.StateSnapshot(Service.State.STOPPING);
               this.stopping(Service.State.RUNNING);
               this.doStop();
               break;
            case STOPPING:
            case TERMINATED:
            case FAILED:
               throw new AssertionError("isStoppable is incorrectly implemented, saw: " + var1);
            default:
               throw new AssertionError("Unexpected state: " + var1);
            }
         } catch (Throwable var3) {
            this.notifyFailed(var3);
            this.monitor.leave();
            this.executeListeners();
            return this;
         }

         this.monitor.leave();
         this.executeListeners();
      }

      return this;
   }

   public final void awaitRunning() {
      this.monitor.enterWhenUninterruptibly(this.hasReachedRunning);
      this.checkCurrentState(Service.State.RUNNING);
      this.monitor.leave();
   }

   public final void awaitRunning(long var1, TimeUnit var3) throws TimeoutException {
      if (this.monitor.enterWhenUninterruptibly(this.hasReachedRunning, var1, var3)) {
         this.checkCurrentState(Service.State.RUNNING);
         this.monitor.leave();
      } else {
         throw new TimeoutException("Timed out waiting for " + this + " to reach the RUNNING state. " + "Current state: " + this.state());
      }
   }

   public final void awaitTerminated() {
      this.monitor.enterWhenUninterruptibly(this.isStopped);
      this.checkCurrentState(Service.State.TERMINATED);
      this.monitor.leave();
   }

   public final void awaitTerminated(long var1, TimeUnit var3) throws TimeoutException {
      if (this.monitor.enterWhenUninterruptibly(this.isStopped, var1, var3)) {
         this.checkCurrentState(Service.State.TERMINATED);
         this.monitor.leave();
      } else {
         throw new TimeoutException("Timed out waiting for " + this + " to reach a terminal state. " + "Current state: " + this.state());
      }
   }

   @GuardedBy("monitor")
   private void checkCurrentState(Service.State var1) {
      Service.State var2 = this.state();
      if (var2 != var1) {
         if (var2 == Service.State.FAILED) {
            throw new IllegalStateException("Expected the service to be " + var1 + ", but the service has FAILED", this.failureCause());
         } else {
            throw new IllegalStateException("Expected the service to be " + var1 + ", but was " + var2);
         }
      }
   }

   protected final void notifyStarted() {
      this.monitor.enter();
      if (this.snapshot.state != Service.State.STARTING) {
         IllegalStateException var1 = new IllegalStateException("Cannot notifyStarted() when the service is " + this.snapshot.state);
         this.notifyFailed(var1);
         throw var1;
      } else {
         if (this.snapshot.shutdownWhenStartupFinishes) {
            this.snapshot = new AbstractService.StateSnapshot(Service.State.STOPPING);
            this.doStop();
         } else {
            this.snapshot = new AbstractService.StateSnapshot(Service.State.RUNNING);
            this.running();
         }

         this.monitor.leave();
         this.executeListeners();
      }
   }

   protected final void notifyStopped() {
      this.monitor.enter();
      Service.State var1 = this.snapshot.state;
      if (var1 != Service.State.STOPPING && var1 != Service.State.RUNNING) {
         IllegalStateException var2 = new IllegalStateException("Cannot notifyStopped() when the service is " + var1);
         this.notifyFailed(var2);
         throw var2;
      } else {
         this.snapshot = new AbstractService.StateSnapshot(Service.State.TERMINATED);
         this.terminated(var1);
         this.monitor.leave();
         this.executeListeners();
      }
   }

   protected final void notifyFailed(Throwable var1) {
      Preconditions.checkNotNull(var1);
      this.monitor.enter();
      Service.State var2 = this.state();
      switch(var2) {
      case NEW:
      case TERMINATED:
         throw new IllegalStateException("Failed while in state:" + var2, var1);
      case STARTING:
      case RUNNING:
      case STOPPING:
         this.snapshot = new AbstractService.StateSnapshot(Service.State.FAILED, false, var1);
         this.failed(var2, var1);
      case FAILED:
         this.monitor.leave();
         this.executeListeners();
         return;
      default:
         throw new AssertionError("Unexpected state: " + var2);
      }
   }

   public final boolean isRunning() {
      return this.state() == Service.State.RUNNING;
   }

   public final Service.State state() {
      return this.snapshot.externalState();
   }

   public final Throwable failureCause() {
      return this.snapshot.failureCause();
   }

   public final void addListener(Service.Listener var1, Executor var2) {
      Preconditions.checkNotNull(var1, "listener");
      Preconditions.checkNotNull(var2, "executor");
      this.monitor.enter();
      if (!this.state().isTerminal()) {
         this.listeners.add(new ListenerCallQueue(var1, var2));
      }

      this.monitor.leave();
   }

   public String toString() {
      return this.getClass().getSimpleName() + " [" + this.state() + "]";
   }

   private void executeListeners() {
      if (!this.monitor.isOccupiedByCurrentThread()) {
         for(int var1 = 0; var1 < this.listeners.size(); ++var1) {
            ((ListenerCallQueue)this.listeners.get(var1)).execute();
         }
      }

   }

   @GuardedBy("monitor")
   private void starting() {
      STARTING_CALLBACK.enqueueOn(this.listeners);
   }

   @GuardedBy("monitor")
   private void running() {
      RUNNING_CALLBACK.enqueueOn(this.listeners);
   }

   @GuardedBy("monitor")
   private void stopping(Service.State var1) {
      if (var1 == Service.State.STARTING) {
         STOPPING_FROM_STARTING_CALLBACK.enqueueOn(this.listeners);
      } else {
         if (var1 != Service.State.RUNNING) {
            throw new AssertionError();
         }

         STOPPING_FROM_RUNNING_CALLBACK.enqueueOn(this.listeners);
      }

   }

   @GuardedBy("monitor")
   private void terminated(Service.State var1) {
      switch(var1) {
      case NEW:
         TERMINATED_FROM_NEW_CALLBACK.enqueueOn(this.listeners);
         break;
      case STARTING:
      case TERMINATED:
      case FAILED:
      default:
         throw new AssertionError();
      case RUNNING:
         TERMINATED_FROM_RUNNING_CALLBACK.enqueueOn(this.listeners);
         break;
      case STOPPING:
         TERMINATED_FROM_STOPPING_CALLBACK.enqueueOn(this.listeners);
      }

   }

   @GuardedBy("monitor")
   private void failed(Service.State var1, Throwable var2) {
      (new ListenerCallQueue.Callback(this, "failed({from = " + var1 + ", cause = " + var2 + "})", var1, var2) {
         final Service.State val$from;
         final Throwable val$cause;
         final AbstractService this$0;

         {
            this.this$0 = var1;
            this.val$from = var3;
            this.val$cause = var4;
         }

         void call(Service.Listener var1) {
            var1.failed(this.val$from, this.val$cause);
         }

         void call(Object var1) {
            this.call((Service.Listener)var1);
         }
      }).enqueueOn(this.listeners);
   }

   static {
      STOPPING_FROM_STARTING_CALLBACK = stoppingCallback(Service.State.STARTING);
      STOPPING_FROM_RUNNING_CALLBACK = stoppingCallback(Service.State.RUNNING);
      TERMINATED_FROM_NEW_CALLBACK = terminatedCallback(Service.State.NEW);
      TERMINATED_FROM_RUNNING_CALLBACK = terminatedCallback(Service.State.RUNNING);
      TERMINATED_FROM_STOPPING_CALLBACK = terminatedCallback(Service.State.STOPPING);
   }

   @Immutable
   private static final class StateSnapshot {
      final Service.State state;
      final boolean shutdownWhenStartupFinishes;
      @Nullable
      final Throwable failure;

      StateSnapshot(Service.State var1) {
         this(var1, false, (Throwable)null);
      }

      StateSnapshot(Service.State var1, boolean var2, @Nullable Throwable var3) {
         Preconditions.checkArgument(!var2 || var1 == Service.State.STARTING, "shudownWhenStartupFinishes can only be set if state is STARTING. Got %s instead.", var1);
         Preconditions.checkArgument(!(var3 != null ^ var1 == Service.State.FAILED), "A failure cause should be set if and only if the state is failed.  Got %s and %s instead.", var1, var3);
         this.state = var1;
         this.shutdownWhenStartupFinishes = var2;
         this.failure = var3;
      }

      Service.State externalState() {
         return this.shutdownWhenStartupFinishes && this.state == Service.State.STARTING ? Service.State.STOPPING : this.state;
      }

      Throwable failureCause() {
         Preconditions.checkState(this.state == Service.State.FAILED, "failureCause() is only valid if the service has failed, service is %s", this.state);
         return this.failure;
      }
   }
}
