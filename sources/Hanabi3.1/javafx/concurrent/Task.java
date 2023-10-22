package javafx.concurrent;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;
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

public abstract class Task extends FutureTask implements Worker, EventTarget {
   private AtomicReference progressUpdate;
   private AtomicReference messageUpdate;
   private AtomicReference titleUpdate;
   private AtomicReference valueUpdate;
   private volatile boolean started;
   private ObjectProperty state;
   private final ObjectProperty value;
   private final ObjectProperty exception;
   private final DoubleProperty workDone;
   private final DoubleProperty totalWork;
   private final DoubleProperty progress;
   private final BooleanProperty running;
   private final StringProperty message;
   private final StringProperty title;
   private static final Permission modifyThreadPerm = new RuntimePermission("modifyThread");
   private EventHelper eventHelper;

   public Task() {
      this(new TaskCallable());
   }

   private Task(TaskCallable var1) {
      super(var1);
      this.progressUpdate = new AtomicReference();
      this.messageUpdate = new AtomicReference();
      this.titleUpdate = new AtomicReference();
      this.valueUpdate = new AtomicReference();
      this.started = false;
      this.state = new SimpleObjectProperty(this, "state", Worker.State.READY);
      this.value = new SimpleObjectProperty(this, "value");
      this.exception = new SimpleObjectProperty(this, "exception");
      this.workDone = new SimpleDoubleProperty(this, "workDone", -1.0);
      this.totalWork = new SimpleDoubleProperty(this, "totalWork", -1.0);
      this.progress = new SimpleDoubleProperty(this, "progress", -1.0);
      this.running = new SimpleBooleanProperty(this, "running", false);
      this.message = new SimpleStringProperty(this, "message", "");
      this.title = new SimpleStringProperty(this, "title", "");
      this.eventHelper = null;
      var1.task = this;
   }

   protected abstract Object call() throws Exception;

   final void setState(Worker.State var1) {
      this.checkThread();
      Worker.State var2 = this.getState();
      if (var2 != Worker.State.CANCELLED) {
         this.state.set(var1);
         this.setRunning(var1 == Worker.State.SCHEDULED || var1 == Worker.State.RUNNING);
         switch ((Worker.State)this.state.get()) {
            case CANCELLED:
               this.fireEvent(new WorkerStateEvent(this, WorkerStateEvent.WORKER_STATE_CANCELLED));
               this.cancelled();
               break;
            case FAILED:
               this.fireEvent(new WorkerStateEvent(this, WorkerStateEvent.WORKER_STATE_FAILED));
               this.failed();
            case READY:
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
      }

   }

   public final Worker.State getState() {
      this.checkThread();
      return (Worker.State)this.state.get();
   }

   public final ReadOnlyObjectProperty stateProperty() {
      this.checkThread();
      return this.state;
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

   private void setValue(Object var1) {
      this.checkThread();
      this.value.set(var1);
   }

   public final Object getValue() {
      this.checkThread();
      return this.value.get();
   }

   public final ReadOnlyObjectProperty valueProperty() {
      this.checkThread();
      return this.value;
   }

   private void _setException(Throwable var1) {
      this.checkThread();
      this.exception.set(var1);
   }

   public final Throwable getException() {
      this.checkThread();
      return (Throwable)this.exception.get();
   }

   public final ReadOnlyObjectProperty exceptionProperty() {
      this.checkThread();
      return this.exception;
   }

   private void setWorkDone(double var1) {
      this.checkThread();
      this.workDone.set(var1);
   }

   public final double getWorkDone() {
      this.checkThread();
      return this.workDone.get();
   }

   public final ReadOnlyDoubleProperty workDoneProperty() {
      this.checkThread();
      return this.workDone;
   }

   private void setTotalWork(double var1) {
      this.checkThread();
      this.totalWork.set(var1);
   }

   public final double getTotalWork() {
      this.checkThread();
      return this.totalWork.get();
   }

   public final ReadOnlyDoubleProperty totalWorkProperty() {
      this.checkThread();
      return this.totalWork;
   }

   private void setProgress(double var1) {
      this.checkThread();
      this.progress.set(var1);
   }

   public final double getProgress() {
      this.checkThread();
      return this.progress.get();
   }

   public final ReadOnlyDoubleProperty progressProperty() {
      this.checkThread();
      return this.progress;
   }

   private void setRunning(boolean var1) {
      this.checkThread();
      this.running.set(var1);
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

   public final boolean cancel() {
      return this.cancel(true);
   }

   public boolean cancel(boolean var1) {
      boolean var2 = (Boolean)AccessController.doPrivileged(() -> {
         return super.cancel(var1);
      }, (AccessControlContext)null, modifyThreadPerm);
      if (var2) {
         if (this.isFxApplicationThread()) {
            this.setState(Worker.State.CANCELLED);
         } else {
            this.runLater(() -> {
               this.setState(Worker.State.CANCELLED);
            });
         }
      }

      return var2;
   }

   protected void updateProgress(long var1, long var3) {
      this.updateProgress((double)var1, (double)var3);
   }

   protected void updateProgress(double var1, double var3) {
      if (Double.isInfinite(var1) || Double.isNaN(var1)) {
         var1 = -1.0;
      }

      if (Double.isInfinite(var3) || Double.isNaN(var3)) {
         var3 = -1.0;
      }

      if (var1 < 0.0) {
         var1 = -1.0;
      }

      if (var3 < 0.0) {
         var3 = -1.0;
      }

      if (var1 > var3) {
         var1 = var3;
      }

      if (this.isFxApplicationThread()) {
         this._updateProgress(var1, var3);
      } else if (this.progressUpdate.getAndSet(new ProgressUpdate(var1, var3)) == null) {
         this.runLater(() -> {
            ProgressUpdate var1 = (ProgressUpdate)this.progressUpdate.getAndSet((Object)null);
            this._updateProgress(var1.workDone, var1.totalWork);
         });
      }

   }

   private void _updateProgress(double var1, double var3) {
      this.setTotalWork(var3);
      this.setWorkDone(var1);
      if (var1 == -1.0) {
         this.setProgress(-1.0);
      } else {
         this.setProgress(var1 / var3);
      }

   }

   protected void updateMessage(String var1) {
      if (this.isFxApplicationThread()) {
         this.message.set(var1);
      } else if (this.messageUpdate.getAndSet(var1) == null) {
         this.runLater(new Runnable() {
            public void run() {
               String var1 = (String)Task.this.messageUpdate.getAndSet((Object)null);
               Task.this.message.set(var1);
            }
         });
      }

   }

   protected void updateTitle(String var1) {
      if (this.isFxApplicationThread()) {
         this.title.set(var1);
      } else if (this.titleUpdate.getAndSet(var1) == null) {
         this.runLater(new Runnable() {
            public void run() {
               String var1 = (String)Task.this.titleUpdate.getAndSet((Object)null);
               Task.this.title.set(var1);
            }
         });
      }

   }

   protected void updateValue(Object var1) {
      if (this.isFxApplicationThread()) {
         this.value.set(var1);
      } else if (this.valueUpdate.getAndSet(var1) == null) {
         this.runLater(() -> {
            this.value.set(this.valueUpdate.getAndSet((Object)null));
         });
      }

   }

   private void checkThread() {
      if (this.started && !this.isFxApplicationThread()) {
         throw new IllegalStateException("Task must only be used from the FX Application Thread");
      }
   }

   void runLater(Runnable var1) {
      Platform.runLater(var1);
   }

   boolean isFxApplicationThread() {
      return Platform.isFxApplicationThread();
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

   public final void fireEvent(Event var1) {
      this.checkThread();
      this.getEventHelper().fireEvent(var1);
   }

   public EventDispatchChain buildEventDispatchChain(EventDispatchChain var1) {
      this.checkThread();
      return this.getEventHelper().buildEventDispatchChain(var1);
   }

   private static final class TaskCallable implements Callable {
      private Task task;

      private TaskCallable() {
      }

      public Object call() throws Exception {
         this.task.started = true;
         this.task.runLater(() -> {
            this.task.setState(Worker.State.SCHEDULED);
            this.task.setState(Worker.State.RUNNING);
         });

         try {
            Object var1 = this.task.call();
            if (!this.task.isCancelled()) {
               this.task.runLater(() -> {
                  this.task.updateValue(var1);
                  this.task.setState(Worker.State.SUCCEEDED);
               });
               return var1;
            } else {
               return null;
            }
         } catch (Throwable var2) {
            this.task.runLater(() -> {
               this.task._setException(var2);
               this.task.setState(Worker.State.FAILED);
            });
            if (var2 instanceof Exception) {
               throw (Exception)var2;
            } else {
               throw new Exception(var2);
            }
         }
      }

      // $FF: synthetic method
      TaskCallable(Object var1) {
         this();
      }
   }

   private static final class ProgressUpdate {
      private final double workDone;
      private final double totalWork;

      private ProgressUpdate(double var1, double var3) {
         this.workDone = var1;
         this.totalWork = var3;
      }

      // $FF: synthetic method
      ProgressUpdate(double var1, double var3, Object var5) {
         this(var1, var3);
      }
   }
}
