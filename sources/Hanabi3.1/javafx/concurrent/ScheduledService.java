package javafx.concurrent;

import java.util.Timer;
import java.util.TimerTask;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Callback;
import javafx.util.Duration;

public abstract class ScheduledService extends Service {
   public static final Callback EXPONENTIAL_BACKOFF_STRATEGY = new Callback() {
      public Duration call(ScheduledService var1) {
         if (var1 == null) {
            return Duration.ZERO;
         } else {
            double var2 = var1.getPeriod() == null ? 0.0 : var1.getPeriod().toMillis();
            double var4 = (double)var1.getCurrentFailureCount();
            return Duration.millis(var2 == 0.0 ? Math.exp(var4) : var2 + var2 * Math.exp(var4));
         }
      }
   };
   public static final Callback LOGARITHMIC_BACKOFF_STRATEGY = new Callback() {
      public Duration call(ScheduledService var1) {
         if (var1 == null) {
            return Duration.ZERO;
         } else {
            double var2 = var1.getPeriod() == null ? 0.0 : var1.getPeriod().toMillis();
            double var4 = (double)var1.getCurrentFailureCount();
            return Duration.millis(var2 == 0.0 ? Math.log1p(var4) : var2 + var2 * Math.log1p(var4));
         }
      }
   };
   public static final Callback LINEAR_BACKOFF_STRATEGY = new Callback() {
      public Duration call(ScheduledService var1) {
         if (var1 == null) {
            return Duration.ZERO;
         } else {
            double var2 = var1.getPeriod() == null ? 0.0 : var1.getPeriod().toMillis();
            double var4 = (double)var1.getCurrentFailureCount();
            return Duration.millis(var2 == 0.0 ? var4 : var2 + var2 * var4);
         }
      }
   };
   private static final Timer DELAY_TIMER = new Timer("ScheduledService Delay Timer", true);
   private ObjectProperty delay;
   private ObjectProperty period;
   private ObjectProperty backoffStrategy;
   private BooleanProperty restartOnFailure;
   private IntegerProperty maximumFailureCount;
   private ReadOnlyIntegerWrapper currentFailureCount;
   private ReadOnlyObjectWrapper cumulativePeriod;
   private ObjectProperty maximumCumulativePeriod;
   private ReadOnlyObjectWrapper lastValue;
   private long lastRunTime;
   private boolean freshStart;
   private TimerTask delayTask;
   private boolean stop;

   public ScheduledService() {
      this.delay = new SimpleObjectProperty(this, "delay", Duration.ZERO);
      this.period = new SimpleObjectProperty(this, "period", Duration.ZERO);
      this.backoffStrategy = new SimpleObjectProperty(this, "backoffStrategy", LOGARITHMIC_BACKOFF_STRATEGY);
      this.restartOnFailure = new SimpleBooleanProperty(this, "restartOnFailure", true);
      this.maximumFailureCount = new SimpleIntegerProperty(this, "maximumFailureCount", Integer.MAX_VALUE);
      this.currentFailureCount = new ReadOnlyIntegerWrapper(this, "currentFailureCount", 0);
      this.cumulativePeriod = new ReadOnlyObjectWrapper(this, "cumulativePeriod", Duration.ZERO);
      this.maximumCumulativePeriod = new SimpleObjectProperty(this, "maximumCumulativePeriod", Duration.INDEFINITE);
      this.lastValue = new ReadOnlyObjectWrapper(this, "lastValue", (Object)null);
      this.lastRunTime = 0L;
      this.freshStart = true;
      this.delayTask = null;
      this.stop = false;
   }

   public final Duration getDelay() {
      return (Duration)this.delay.get();
   }

   public final void setDelay(Duration var1) {
      this.delay.set(var1);
   }

   public final ObjectProperty delayProperty() {
      return this.delay;
   }

   public final Duration getPeriod() {
      return (Duration)this.period.get();
   }

   public final void setPeriod(Duration var1) {
      this.period.set(var1);
   }

   public final ObjectProperty periodProperty() {
      return this.period;
   }

   public final Callback getBackoffStrategy() {
      return (Callback)this.backoffStrategy.get();
   }

   public final void setBackoffStrategy(Callback var1) {
      this.backoffStrategy.set(var1);
   }

   public final ObjectProperty backoffStrategyProperty() {
      return this.backoffStrategy;
   }

   public final boolean getRestartOnFailure() {
      return this.restartOnFailure.get();
   }

   public final void setRestartOnFailure(boolean var1) {
      this.restartOnFailure.set(var1);
   }

   public final BooleanProperty restartOnFailureProperty() {
      return this.restartOnFailure;
   }

   public final int getMaximumFailureCount() {
      return this.maximumFailureCount.get();
   }

   public final void setMaximumFailureCount(int var1) {
      this.maximumFailureCount.set(var1);
   }

   public final IntegerProperty maximumFailureCountProperty() {
      return this.maximumFailureCount;
   }

   public final int getCurrentFailureCount() {
      return this.currentFailureCount.get();
   }

   public final ReadOnlyIntegerProperty currentFailureCountProperty() {
      return this.currentFailureCount.getReadOnlyProperty();
   }

   private void setCurrentFailureCount(int var1) {
      this.currentFailureCount.set(var1);
   }

   public final Duration getCumulativePeriod() {
      return (Duration)this.cumulativePeriod.get();
   }

   public final ReadOnlyObjectProperty cumulativePeriodProperty() {
      return this.cumulativePeriod.getReadOnlyProperty();
   }

   void setCumulativePeriod(Duration var1) {
      Duration var2 = var1 != null && !(var1.toMillis() < 0.0) ? var1 : Duration.ZERO;
      Duration var3 = (Duration)this.maximumCumulativePeriod.get();
      if (var3 != null && !var3.isUnknown() && !var2.isUnknown()) {
         if (var3.toMillis() < 0.0) {
            var2 = Duration.ZERO;
         } else if (!var3.isIndefinite() && var2.greaterThan(var3)) {
            var2 = var3;
         }
      }

      this.cumulativePeriod.set(var2);
   }

   public final Duration getMaximumCumulativePeriod() {
      return (Duration)this.maximumCumulativePeriod.get();
   }

   public final void setMaximumCumulativePeriod(Duration var1) {
      this.maximumCumulativePeriod.set(var1);
   }

   public final ObjectProperty maximumCumulativePeriodProperty() {
      return this.maximumCumulativePeriod;
   }

   public final Object getLastValue() {
      return this.lastValue.get();
   }

   public final ReadOnlyObjectProperty lastValueProperty() {
      return this.lastValue.getReadOnlyProperty();
   }

   protected void executeTask(Task var1) {
      assert var1 != null;

      this.checkThread();
      if (this.freshStart) {
         assert this.delayTask == null;

         this.setCumulativePeriod(this.getPeriod());
         long var2 = (long)normalize(this.getDelay());
         if (var2 == 0L) {
            this.executeTaskNow(var1);
         } else {
            this.schedule(this.delayTask = this.createTimerTask(var1), var2);
         }
      } else {
         double var6 = normalize(this.getCumulativePeriod());
         double var4 = (double)(this.clock() - this.lastRunTime);
         if (var4 < var6) {
            assert this.delayTask == null;

            this.schedule(this.delayTask = this.createTimerTask(var1), (long)(var6 - var4));
         } else {
            this.executeTaskNow(var1);
         }
      }

   }

   protected void succeeded() {
      super.succeeded();
      this.lastValue.set(this.getValue());
      Duration var1 = this.getPeriod();
      this.setCumulativePeriod(var1);
      boolean var2 = this.stop;
      this.superReset();

      assert !this.freshStart;

      if (var2) {
         this.cancelFromReadyState();
      } else {
         this.start();
      }

   }

   protected void failed() {
      super.failed();

      assert this.delayTask == null;

      this.setCurrentFailureCount(this.getCurrentFailureCount() + 1);
      if (this.getRestartOnFailure() && this.getMaximumFailureCount() > this.getCurrentFailureCount()) {
         Callback var1 = this.getBackoffStrategy();
         if (var1 != null) {
            Duration var2 = (Duration)var1.call(this);
            this.setCumulativePeriod(var2);
         }

         this.superReset();

         assert !this.freshStart;

         this.start();
      }

   }

   public void reset() {
      super.reset();
      this.stop = false;
      this.setCumulativePeriod(this.getPeriod());
      this.lastValue.set((Object)null);
      this.setCurrentFailureCount(0);
      this.lastRunTime = 0L;
      this.freshStart = true;
   }

   public boolean cancel() {
      boolean var1 = super.cancel();
      this.stop = true;
      if (this.delayTask != null) {
         this.delayTask.cancel();
         this.delayTask = null;
      }

      return var1;
   }

   void schedule(TimerTask var1, long var2) {
      DELAY_TIMER.schedule(var1, var2);
   }

   boolean isFreshStart() {
      return this.freshStart;
   }

   long clock() {
      return System.currentTimeMillis();
   }

   private void superReset() {
      super.reset();
   }

   private TimerTask createTimerTask(final Task var1) {
      assert var1 != null;

      return new TimerTask() {
         public void run() {
            Runnable var1x = () -> {
               ScheduledService.this.executeTaskNow(var1);
               ScheduledService.this.delayTask = null;
            };
            if (ScheduledService.this.isFxApplicationThread()) {
               var1x.run();
            } else {
               ScheduledService.this.runLater(var1x);
            }

         }
      };
   }

   private void executeTaskNow(Task var1) {
      assert var1 != null;

      this.lastRunTime = this.clock();
      this.freshStart = false;
      super.executeTask(var1);
   }

   private static double normalize(Duration var0) {
      if (var0 != null && !var0.isUnknown()) {
         return var0.isIndefinite() ? Double.MAX_VALUE : var0.toMillis();
      } else {
         return 0.0;
      }
   }
}
