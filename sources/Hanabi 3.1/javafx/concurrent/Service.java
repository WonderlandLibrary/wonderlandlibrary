package javafx.concurrent;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import sun.util.logging.PlatformLogger;

public abstract class Service implements Worker, EventTarget {
   private static final PlatformLogger LOG = PlatformLogger.getLogger(Service.class.getName());
   private static final int THREAD_POOL_SIZE = 32;
   private static final long THREAD_TIME_OUT = 1000L;
   private static final BlockingQueue IO_QUEUE = new LinkedBlockingQueue() {
      public boolean offer(Runnable var1) {
         return Service.EXECUTOR.getPoolSize() < 32 ? false : super.offer(var1);
      }
   };
   private static final ThreadGroup THREAD_GROUP = (ThreadGroup)AccessController.doPrivileged(() -> {
      return new ThreadGroup("javafx concurrent thread pool");
   });
   private static final Thread.UncaughtExceptionHandler UNCAUGHT_HANDLER = (var0, var1) -> {
      if (!(var1 instanceof IllegalMonitorStateException)) {
         LOG.warning("Uncaught throwable in " + THREAD_GROUP.getName(), var1);
      }

   };
   private static final ThreadFactory THREAD_FACTORY = (var0) -> {
      return (Thread)AccessController.doPrivileged(() -> {
         Thread var1 = new Thread(THREAD_GROUP, var0);
         var1.setUncaughtExceptionHandler(UNCAUGHT_HANDLER);
         var1.setPriority(1);
         var1.setDaemon(true);
         return var1;
      });
   };
   private static final ThreadPoolExecutor EXECUTOR;
   private final ObjectProperty state;
   private final ObjectProperty value;
   private final ObjectProperty exception;
   private final DoubleProperty workDone;
   private final DoubleProperty totalWorkToBeDone;
   private final DoubleProperty progress;
   private final BooleanProperty running;
   private final StringProperty message;
   private final StringProperty title;
   private final ObjectProperty executor;
   private Task task;
   private volatile boolean startedOnce;
   private EventHelper eventHelper;

   public final Worker.State getState() {
      this.checkThread();
      return (Worker.State)this.state.get();
   }

   public final ReadOnlyObjectProperty stateProperty() {
      this.checkThread();
      return this.state;
   }

   public final Object getValue() {
      this.checkThread();
      return this.value.get();
   }

   public final ReadOnlyObjectProperty valueProperty() {
      this.checkThread();
      return this.value;
   }

   public final Throwable getException() {
      this.checkThread();
      return (Throwable)this.exception.get();
   }

   public final ReadOnlyObjectProperty exceptionProperty() {
      this.checkThread();
      return this.exception;
   }

   public final double getWorkDone() {
      this.checkThread();
      return this.workDone.get();
   }

   public final ReadOnlyDoubleProperty workDoneProperty() {
      this.checkThread();
      return this.workDone;
   }

   public final double getTotalWork() {
      this.checkThread();
      return this.totalWorkToBeDone.get();
   }

   public final ReadOnlyDoubleProperty totalWorkProperty() {
      this.checkThread();
      return this.totalWorkToBeDone;
   }

   public final double getProgress() {
      this.checkThread();
      return this.progress.get();
   }

   public final ReadOnlyDoubleProperty progressProperty() {
      this.checkThread();
      return this.progress;
   }

   public final boolean isRunning() {
      this.checkThread();
      return this.running.get();
   }

   public final ReadOnlyBooleanProperty runningProperty() {
      this.checkThread();
      return this.running;
   }

   public final String getMessage() {
      this.checkThread();
      return (String)this.message.get();
   }

   public final ReadOnlyStringProperty messageProperty() {
      this.checkThread();
      return this.message;
   }

   public final String getTitle() {
      this.checkThread();
      return (String)this.title.get();
   }

   public final ReadOnlyStringProperty titleProperty() {
      this.checkThread();
      return this.title;
   }

   public final void setExecutor(Executor var1) {
      this.checkThread();
      this.executor.set(var1);
   }

   public final Executor getExecutor() {
      this.checkThread();
      return (Executor)this.executor.get();
   }

   public final ObjectProperty executorProperty() {
      this.checkThread();
      return this.executor;
   }

   public final ObjectProperty onReadyProperty() {
      this.checkThread();
      return this.getEventHelper().onReadyProperty();
   }

   public final EventHandler getOnReady() {
      this.checkThread();
      return this.eventHelper == null ? null : this.eventHelper.getOnReady();
   }

   public final void setOnReady(EventHandler var1) {
      this.checkThread();
      this.getEventHelper().setOnReady(var1);
   }

   protected void ready() {
   }

   public final ObjectProperty onScheduledProperty() {
      this.checkThread();
      return this.getEventHelper().onScheduledProperty();
   }

   public final EventHandler getOnScheduled() {
      this.checkThread();
      return this.eventHelper == null ? null : this.eventHelper.getOnScheduled();
   }

   public final void setOnScheduled(EventHandler var1) {
      this.checkThread();
      this.getEventHelper().setOnScheduled(var1);
   }

   protected void scheduled() {
   }

   public final ObjectProperty onRunningProperty() {
      this.checkThread();
      return this.getEventHelper().onRunningProperty();
   }

   public final EventHandler getOnRunning() {
      this.checkThread();
      return this.eventHelper == null ? null : this.eventHelper.getOnRunning();
   }

   public final void setOnRunning(EventHandler var1) {
      this.checkThread();
      this.getEventHelper().setOnRunning(var1);
   }

   protected void running() {
   }

   public final ObjectProperty onSucceededProperty() {
      this.checkThread();
      return this.getEventHelper().onSucceededProperty();
   }

   public final EventHandler getOnSucceeded() {
      this.checkThread();
      return this.eventHelper == null ? null : this.eventHelper.getOnSucceeded();
   }

   public final void setOnSucceeded(EventHandler var1) {
      this.checkThread();
      this.getEventHelper().setOnSucceeded(var1);
   }

   protected void succeeded() {
   }

   public final ObjectProperty onCancelledProperty() {
      this.checkThread();
      return this.getEventHelper().onCancelledProperty();
   }

   public final EventHandler getOnCancelled() {
      this.checkThread();
      return this.eventHelper == null ? null : this.eventHelper.getOnCancelled();
   }

   public final void setOnCancelled(EventHandler var1) {
      this.checkThread();
      this.getEventHelper().setOnCancelled(var1);
   }

   protected void cancelled() {
   }

   public final ObjectProperty onFailedProperty() {
      this.checkThread();
      return this.getEventHelper().onFailedProperty();
   }

   public final EventHandler getOnFailed() {
      this.checkThread();
      return this.eventHelper == null ? null : this.eventHelper.getOnFailed();
   }

   public final void setOnFailed(EventHandler var1) {
      this.checkThread();
      this.getEventHelper().setOnFailed(var1);
   }

   protected void failed() {
   }

   protected Service() {
      this.state = new SimpleObjectProperty(this, "state", Worker.State.READY);
      this.value = new SimpleObjectProperty(this, "value");
      this.exception = new SimpleObjectProperty(this, "exception");
      this.workDone = new SimpleDoubleProperty(this, "workDone", -1.0);
      this.totalWorkToBeDone = new SimpleDoubleProperty(this, "totalWork", -1.0);
      this.progress = new SimpleDoubleProperty(this, "progress", -1.0);
      this.running = new SimpleBooleanProperty(this, "running", false);
      this.message = new SimpleStringProperty(this, "message", "");
      this.title = new SimpleStringProperty(this, "title", "");
      this.executor = new SimpleObjectProperty(this, "executor");
      this.startedOnce = false;
      this.eventHelper = null;
      this.state.addListener((var1, var2, var3) -> {
         switch (var3) {
            case CANCELLED:
               this.fireEvent(new WorkerStateEvent(this, WorkerStateEvent.WORKER_STATE_CANCELLED));
               this.cancelled();
               break;
            case FAILED:
               this.fireEvent(new WorkerStateEvent(this, WorkerStateEvent.WORKER_STATE_FAILED));
               this.failed();
               break;
            case READY:
               this.fireEvent(new WorkerStateEvent(this, WorkerStateEvent.WORKER_STATE_READY));
               this.ready();
               break;
            case RUNNING:
               this.fireEvent(new WorkerStateEvent(this, WorkerStateEvent.WORKER_STATE_RUNNING));
               this.running();
               break;
            case SCHEDULED:
               this.fireEvent(new WorkerStateEvent(this, WorkerStateEvent.WORKER_STATE_SCHEDULED));
               this.scheduled();
               break;
            case SUCCEEDED:
               this.fireEvent(new WorkerStateEvent(this, WorkerStateEvent.WORKER_STATE_SUCCEEDED));
               this.succeeded();
               break;
            default:
               throw new AssertionError("Should be unreachable");
         }

      });
   }

   public boolean cancel() {
      this.checkThread();
      if (this.task == null) {
         if (this.state.get() != Worker.State.CANCELLED && this.state.get() != Worker.State.SUCCEEDED) {
            this.state.set(Worker.State.CANCELLED);
            return true;
         } else {
            return false;
         }
      } else {
         return this.task.cancel(true);
      }
   }

   public void restart() {
      this.checkThread();
      if (this.task != null) {
         this.task.cancel();
         this.task = null;
         this.state.unbind();
         this.state.set(Worker.State.CANCELLED);
      }

      this.reset();
      this.start();
   }

   public void reset() {
      this.checkThread();
      Worker.State var1 = this.getState();
      if (var1 != Worker.State.SCHEDULED && var1 != Worker.State.RUNNING) {
         this.task = null;
         this.state.unbind();
         this.state.set(Worker.State.READY);
         this.value.unbind();
         this.value.set((Object)null);
         this.exception.unbind();
         this.exception.set((Object)null);
         this.workDone.unbind();
         this.workDone.set(-1.0);
         this.totalWorkToBeDone.unbind();
         this.totalWorkToBeDone.set(-1.0);
         this.progress.unbind();
         this.progress.set(-1.0);
         this.running.unbind();
         this.running.set(false);
         this.message.unbind();
         this.message.set("");
         this.title.unbind();
         this.title.set("");
      } else {
         throw new IllegalStateException();
      }
   }

   public void start() {
      this.checkThread();
      if (this.getState() != Worker.State.READY) {
         throw new IllegalStateException("Can only start a Service in the READY state. Was in state " + this.getState());
      } else {
         this.task = this.createTask();
         this.state.bind(this.task.stateProperty());
         this.value.bind(this.task.valueProperty());
         this.exception.bind(this.task.exceptionProperty());
         this.workDone.bind(this.task.workDoneProperty());
         this.totalWorkToBeDone.bind(this.task.totalWorkProperty());
         this.progress.bind(this.task.progressProperty());
         this.running.bind(this.task.runningProperty());
         this.message.bind(this.task.messageProperty());
         this.title.bind(this.task.titleProperty());
         this.startedOnce = true;
         if (!this.isFxApplicationThread()) {
            this.runLater(() -> {
               this.task.setState(Worker.State.SCHEDULED);
               this.executeTask(this.task);
            });
         } else {
            this.task.setState(Worker.State.SCHEDULED);
            this.executeTask(this.task);
         }

      }
   }

   void cancelFromReadyState() {
      this.state.set(Worker.State.SCHEDULED);
      this.state.set(Worker.State.CANCELLED);
   }

   protected void executeTask(Task var1) {
      AccessControlContext var2 = AccessController.getContext();
      Object var3 = this.getExecutor() != null ? this.getExecutor() : EXECUTOR;
      ((Executor)var3).execute(() -> {
         AccessController.doPrivileged(() -> {
            var1.run();
            return null;
         }, var2);
      });
   }

   private EventHelper getEventHelper() {
      if (this.eventHelper == null) {
         this.eventHelper = new EventHelper(this);
      }

      return this.eventHelper;
   }

   public final void addEventHandler(EventType var1, EventHandler var2) {
      this.checkThread();
      this.getEventHelper().addEventHandler(var1, var2);
   }

   public final void removeEventHandler(EventType var1, EventHandler var2) {
      this.checkThread();
      this.getEventHelper().removeEventHandler(var1, var2);
   }

   public final void addEventFilter(EventType var1, EventHandler var2) {
      this.checkThread();
      this.getEventHelper().addEventFilter(var1, var2);
   }

   public final void removeEventFilter(EventType var1, EventHandler var2) {
      this.checkThread();
      this.getEventHelper().removeEventFilter(var1, var2);
   }

   protected final void setEventHandler(EventType var1, EventHandler var2) {
      this.checkThread();
      this.getEventHelper().setEventHandler(var1, var2);
   }

   protected final void fireEvent(Event var1) {
      this.checkThread();
      this.getEventHelper().fireEvent(var1);
   }

   public EventDispatchChain buildEventDispatchChain(EventDispatchChain var1) {
      this.checkThread();
      return this.getEventHelper().buildEventDispatchChain(var1);
   }

   protected abstract Task createTask();

   void checkThread() {
      if (this.startedOnce && !this.isFxApplicationThread()) {
         throw new IllegalStateException("Service must only be used from the FX Application Thread");
      }
   }

   void runLater(Runnable var1) {
      Platform.runLater(var1);
   }

   boolean isFxApplicationThread() {
      return Platform.isFxApplicationThread();
   }

   static {
      EXECUTOR = new ThreadPoolExecutor(2, 32, 1000L, TimeUnit.MILLISECONDS, IO_QUEUE, THREAD_FACTORY, new ThreadPoolExecutor.AbortPolicy());
      EXECUTOR.allowCoreThreadTimeOut(true);
   }
}
