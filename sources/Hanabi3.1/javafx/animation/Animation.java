package javafx.animation;

import com.sun.javafx.animation.TickCalculation;
import com.sun.javafx.tk.Toolkit;
import com.sun.scenario.animation.AbstractPrimaryTimer;
import com.sun.scenario.animation.shared.ClipEnvelope;
import com.sun.scenario.animation.shared.PulseReceiver;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.HashMap;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.IntegerPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoublePropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.util.Duration;

public abstract class Animation {
   public static final int INDEFINITE = -1;
   private static final double EPSILON = 1.0E-12;
   private long startTime;
   private long pauseTime;
   private boolean paused = false;
   private final AbstractPrimaryTimer timer;
   private AccessControlContext accessCtrlCtx = null;
   final PulseReceiver pulseReceiver = new PulseReceiver() {
      public void timePulse(long var1) {
         long var3 = var1 - Animation.this.startTime;
         if (var3 >= 0L) {
            if (Animation.this.accessCtrlCtx == null) {
               throw new IllegalStateException("Error: AccessControlContext not captured");
            } else {
               AccessController.doPrivileged(() -> {
                  Animation.this.impl_timePulse(var3);
                  return null;
               }, Animation.this.accessCtrlCtx);
            }
         }
      }
   };
   Animation parent = null;
   ClipEnvelope clipEnvelope;
   private boolean lastPlayedFinished = false;
   private boolean lastPlayedForward = true;
   private DoubleProperty rate;
   private static final double DEFAULT_RATE = 1.0;
   private double oldRate = 1.0;
   private ReadOnlyDoubleProperty currentRate;
   private static final double DEFAULT_CURRENT_RATE = 0.0;
   private ReadOnlyObjectProperty cycleDuration;
   private static final Duration DEFAULT_CYCLE_DURATION;
   private ReadOnlyObjectProperty totalDuration;
   private static final Duration DEFAULT_TOTAL_DURATION;
   private CurrentTimeProperty currentTime;
   private long currentTicks;
   private ObjectProperty delay;
   private static final Duration DEFAULT_DELAY;
   private IntegerProperty cycleCount;
   private static final int DEFAULT_CYCLE_COUNT = 1;
   private BooleanProperty autoReverse;
   private static final boolean DEFAULT_AUTO_REVERSE = false;
   private ReadOnlyObjectProperty status;
   private static final Status DEFAULT_STATUS;
   private final double targetFramerate;
   private final int resolution;
   private long lastPulse;
   private ObjectProperty onFinished;
   private static final EventHandler DEFAULT_ON_FINISHED;
   private final ObservableMap cuePoints = FXCollections.observableMap(new HashMap(0));

   private long now() {
      return TickCalculation.fromNano(this.timer.nanos());
   }

   private void addPulseReceiver() {
      this.accessCtrlCtx = AccessController.getContext();
      this.timer.addPulseReceiver(this.pulseReceiver);
   }

   void startReceiver(long var1) {
      this.paused = false;
      this.startTime = this.now() + var1;
      this.addPulseReceiver();
   }

   void pauseReceiver() {
      if (!this.paused) {
         this.pauseTime = this.now();
         this.paused = true;
         this.timer.removePulseReceiver(this.pulseReceiver);
      }

   }

   void resumeReceiver() {
      if (this.paused) {
         long var1 = this.now() - this.pauseTime;
         this.startTime += var1;
         this.paused = false;
         this.addPulseReceiver();
      }

   }

   public final void setRate(double var1) {
      if (this.rate != null || Math.abs(var1 - 1.0) > 1.0E-12) {
         this.rateProperty().set(var1);
      }

   }

   public final double getRate() {
      return this.rate == null ? 1.0 : this.rate.get();
   }

   public final DoubleProperty rateProperty() {
      if (this.rate == null) {
         this.rate = new DoublePropertyBase(1.0) {
            public void invalidated() {
               double var1 = Animation.this.getRate();
               if (Animation.this.isRunningEmbedded()) {
                  if (this.isBound()) {
                     this.unbind();
                  }

                  this.set(Animation.this.oldRate);
                  throw new IllegalArgumentException("Cannot set rate of embedded animation while running.");
               } else {
                  if (Math.abs(var1) < 1.0E-12) {
                     if (Animation.this.getStatus() == Animation.Status.RUNNING) {
                        Animation.this.lastPlayedForward = Math.abs(Animation.this.getCurrentRate() - Animation.this.oldRate) < 1.0E-12;
                     }

                     Animation.this.setCurrentRate(0.0);
                     Animation.this.pauseReceiver();
                  } else {
                     if (Animation.this.getStatus() == Animation.Status.RUNNING) {
                        double var3 = Animation.this.getCurrentRate();
                        if (Math.abs(var3) < 1.0E-12) {
                           Animation.this.setCurrentRate(Animation.this.lastPlayedForward ? var1 : -var1);
                           Animation.this.resumeReceiver();
                        } else {
                           boolean var5 = Math.abs(var3 - Animation.this.oldRate) < 1.0E-12;
                           Animation.this.setCurrentRate(var5 ? var1 : -var1);
                        }
                     }

                     Animation.this.oldRate = var1;
                  }

                  Animation.this.clipEnvelope.setRate(var1);
               }
            }

            public Object getBean() {
               return Animation.this;
            }

            public String getName() {
               return "rate";
            }
         };
      }

      return this.rate;
   }

   private boolean isRunningEmbedded() {
      if (this.parent == null) {
         return false;
      } else {
         return this.parent.getStatus() != Animation.Status.STOPPED || this.parent.isRunningEmbedded();
      }
   }

   private void setCurrentRate(double var1) {
      if (this.currentRate != null || Math.abs(var1 - 0.0) > 1.0E-12) {
         ((CurrentRateProperty)this.currentRateProperty()).set(var1);
      }

   }

   public final double getCurrentRate() {
      return this.currentRate == null ? 0.0 : this.currentRate.get();
   }

   public final ReadOnlyDoubleProperty currentRateProperty() {
      if (this.currentRate == null) {
         this.currentRate = new CurrentRateProperty();
      }

      return this.currentRate;
   }

   protected final void setCycleDuration(Duration var1) {
      if (this.cycleDuration != null || !DEFAULT_CYCLE_DURATION.equals(var1)) {
         if (var1.lessThan(Duration.ZERO)) {
            throw new IllegalArgumentException("Cycle duration cannot be negative");
         }

         ((AnimationReadOnlyProperty)this.cycleDurationProperty()).set(var1);
         this.updateTotalDuration();
      }

   }

   public final Duration getCycleDuration() {
      return this.cycleDuration == null ? DEFAULT_CYCLE_DURATION : (Duration)this.cycleDuration.get();
   }

   public final ReadOnlyObjectProperty cycleDurationProperty() {
      if (this.cycleDuration == null) {
         this.cycleDuration = new AnimationReadOnlyProperty("cycleDuration", DEFAULT_CYCLE_DURATION);
      }

      return this.cycleDuration;
   }

   public final Duration getTotalDuration() {
      return this.totalDuration == null ? DEFAULT_TOTAL_DURATION : (Duration)this.totalDuration.get();
   }

   public final ReadOnlyObjectProperty totalDurationProperty() {
      if (this.totalDuration == null) {
         this.totalDuration = new AnimationReadOnlyProperty("totalDuration", DEFAULT_TOTAL_DURATION);
      }

      return this.totalDuration;
   }

   private void updateTotalDuration() {
      int var1 = this.getCycleCount();
      Duration var2 = this.getCycleDuration();
      Duration var3 = Duration.ZERO.equals(var2) ? Duration.ZERO : (var1 == -1 ? Duration.INDEFINITE : (var1 <= 1 ? var2 : var2.multiply((double)var1)));
      if (this.totalDuration != null || !DEFAULT_TOTAL_DURATION.equals(var3)) {
         ((AnimationReadOnlyProperty)this.totalDurationProperty()).set(var3);
      }

      if (this.getStatus() == Animation.Status.STOPPED) {
         this.syncClipEnvelope();
         if (var3.lessThan(this.getCurrentTime())) {
            this.clipEnvelope.jumpTo(TickCalculation.fromDuration(var3));
         }
      }

   }

   public final Duration getCurrentTime() {
      return TickCalculation.toDuration(this.currentTicks);
   }

   public final ReadOnlyObjectProperty currentTimeProperty() {
      if (this.currentTime == null) {
         this.currentTime = new CurrentTimeProperty();
      }

      return this.currentTime;
   }

   public final void setDelay(Duration var1) {
      if (this.delay != null || !DEFAULT_DELAY.equals(var1)) {
         this.delayProperty().set(var1);
      }

   }

   public final Duration getDelay() {
      return this.delay == null ? DEFAULT_DELAY : (Duration)this.delay.get();
   }

   public final ObjectProperty delayProperty() {
      if (this.delay == null) {
         this.delay = new ObjectPropertyBase(DEFAULT_DELAY) {
            public Object getBean() {
               return Animation.this;
            }

            public String getName() {
               return "delay";
            }

            protected void invalidated() {
               Duration var1 = (Duration)this.get();
               if (var1.lessThan(Duration.ZERO)) {
                  if (this.isBound()) {
                     this.unbind();
                  }

                  this.set(Duration.ZERO);
                  throw new IllegalArgumentException("Cannot set delay to negative value. Setting to Duration.ZERO");
               }
            }
         };
      }

      return this.delay;
   }

   public final void setCycleCount(int var1) {
      if (this.cycleCount != null || var1 != 1) {
         this.cycleCountProperty().set(var1);
      }

   }

   public final int getCycleCount() {
      return this.cycleCount == null ? 1 : this.cycleCount.get();
   }

   public final IntegerProperty cycleCountProperty() {
      if (this.cycleCount == null) {
         this.cycleCount = new IntegerPropertyBase(1) {
            public void invalidated() {
               Animation.this.updateTotalDuration();
            }

            public Object getBean() {
               return Animation.this;
            }

            public String getName() {
               return "cycleCount";
            }
         };
      }

      return this.cycleCount;
   }

   public final void setAutoReverse(boolean var1) {
      if (this.autoReverse != null || var1) {
         this.autoReverseProperty().set(var1);
      }

   }

   public final boolean isAutoReverse() {
      return this.autoReverse == null ? false : this.autoReverse.get();
   }

   public final BooleanProperty autoReverseProperty() {
      if (this.autoReverse == null) {
         this.autoReverse = new SimpleBooleanProperty(this, "autoReverse", false);
      }

      return this.autoReverse;
   }

   protected final void setStatus(Status var1) {
      if (this.status != null || !DEFAULT_STATUS.equals(var1)) {
         ((AnimationReadOnlyProperty)this.statusProperty()).set(var1);
      }

   }

   public final Status getStatus() {
      return this.status == null ? DEFAULT_STATUS : (Status)this.status.get();
   }

   public final ReadOnlyObjectProperty statusProperty() {
      if (this.status == null) {
         this.status = new AnimationReadOnlyProperty("status", Animation.Status.STOPPED);
      }

      return this.status;
   }

   public final double getTargetFramerate() {
      return this.targetFramerate;
   }

   public final void setOnFinished(EventHandler var1) {
      if (this.onFinished != null || var1 != null) {
         this.onFinishedProperty().set(var1);
      }

   }

   public final EventHandler getOnFinished() {
      return this.onFinished == null ? DEFAULT_ON_FINISHED : (EventHandler)this.onFinished.get();
   }

   public final ObjectProperty onFinishedProperty() {
      if (this.onFinished == null) {
         this.onFinished = new SimpleObjectProperty(this, "onFinished", DEFAULT_ON_FINISHED);
      }

      return this.onFinished;
   }

   public final ObservableMap getCuePoints() {
      return this.cuePoints;
   }

   public void jumpTo(Duration var1) {
      if (var1 == null) {
         throw new NullPointerException("Time needs to be specified.");
      } else if (var1.isUnknown()) {
         throw new IllegalArgumentException("The time is invalid");
      } else if (this.parent != null) {
         throw new IllegalStateException("Cannot jump when embedded in another animation");
      } else {
         this.lastPlayedFinished = false;
         Duration var2 = this.getTotalDuration();
         var1 = var1.lessThan(Duration.ZERO) ? Duration.ZERO : (var1.greaterThan(var2) ? var2 : var1);
         long var3 = TickCalculation.fromDuration(var1);
         if (this.getStatus() == Animation.Status.STOPPED) {
            this.syncClipEnvelope();
         }

         this.clipEnvelope.jumpTo(var3);
      }
   }

   public void jumpTo(String var1) {
      if (var1 == null) {
         throw new NullPointerException("CuePoint needs to be specified");
      } else {
         if ("start".equalsIgnoreCase(var1)) {
            this.jumpTo(Duration.ZERO);
         } else if ("end".equalsIgnoreCase(var1)) {
            this.jumpTo(this.getTotalDuration());
         } else {
            Duration var2 = (Duration)this.getCuePoints().get(var1);
            if (var2 != null) {
               this.jumpTo(var2);
            }
         }

      }
   }

   public void playFrom(String var1) {
      this.jumpTo(var1);
      this.play();
   }

   public void playFrom(Duration var1) {
      this.jumpTo(var1);
      this.play();
   }

   public void play() {
      if (this.parent != null) {
         throw new IllegalStateException("Cannot start when embedded in another animation");
      } else {
         switch (this.getStatus()) {
            case STOPPED:
               if (this.impl_startable(true)) {
                  double var1 = this.getRate();
                  if (this.lastPlayedFinished) {
                     this.jumpTo(var1 < 0.0 ? this.getTotalDuration() : Duration.ZERO);
                  }

                  this.impl_start(true);
                  this.startReceiver(TickCalculation.fromDuration(this.getDelay()));
                  if (Math.abs(var1) < 1.0E-12) {
                     this.pauseReceiver();
                  }
               } else {
                  EventHandler var3 = this.getOnFinished();
                  if (var3 != null) {
                     var3.handle(new ActionEvent(this, (EventTarget)null));
                  }
               }
               break;
            case PAUSED:
               this.impl_resume();
               if (Math.abs(this.getRate()) >= 1.0E-12) {
                  this.resumeReceiver();
               }
         }

      }
   }

   public void playFromStart() {
      this.stop();
      this.setRate(Math.abs(this.getRate()));
      this.jumpTo(Duration.ZERO);
      this.play();
   }

   public void stop() {
      if (this.parent != null) {
         throw new IllegalStateException("Cannot stop when embedded in another animation");
      } else {
         if (this.getStatus() != Animation.Status.STOPPED) {
            this.clipEnvelope.abortCurrentPulse();
            this.impl_stop();
            this.jumpTo(Duration.ZERO);
         }

      }
   }

   public void pause() {
      if (this.parent != null) {
         throw new IllegalStateException("Cannot pause when embedded in another animation");
      } else {
         if (this.getStatus() == Animation.Status.RUNNING) {
            this.clipEnvelope.abortCurrentPulse();
            this.pauseReceiver();
            this.impl_pause();
         }

      }
   }

   protected Animation(double var1) {
      this.targetFramerate = var1;
      this.resolution = (int)Math.max(1L, Math.round(6000.0 / var1));
      this.clipEnvelope = ClipEnvelope.create(this);
      this.timer = Toolkit.getToolkit().getPrimaryTimer();
   }

   protected Animation() {
      this.resolution = 1;
      this.targetFramerate = (double)(6000 / Toolkit.getToolkit().getPrimaryTimer().getDefaultResolution());
      this.clipEnvelope = ClipEnvelope.create(this);
      this.timer = Toolkit.getToolkit().getPrimaryTimer();
   }

   Animation(AbstractPrimaryTimer var1) {
      this.resolution = 1;
      this.targetFramerate = (double)(6000 / var1.getDefaultResolution());
      this.clipEnvelope = ClipEnvelope.create(this);
      this.timer = var1;
   }

   Animation(AbstractPrimaryTimer var1, ClipEnvelope var2, int var3) {
      this.resolution = var3;
      this.targetFramerate = (double)(6000 / var3);
      this.clipEnvelope = var2;
      this.timer = var1;
   }

   boolean impl_startable(boolean var1) {
      return TickCalculation.fromDuration(this.getCycleDuration()) > 0L || !var1 && this.clipEnvelope.wasSynched();
   }

   void impl_sync(boolean var1) {
      if (var1 || !this.clipEnvelope.wasSynched()) {
         this.syncClipEnvelope();
      }

   }

   private void syncClipEnvelope() {
      int var1 = this.getCycleCount();
      int var2 = var1 <= 0 && var1 != -1 ? 1 : var1;
      this.clipEnvelope = this.clipEnvelope.setCycleCount(var2);
      this.clipEnvelope.setCycleDuration(this.getCycleDuration());
      this.clipEnvelope.setAutoReverse(this.isAutoReverse());
   }

   void impl_start(boolean var1) {
      this.impl_sync(var1);
      this.setStatus(Animation.Status.RUNNING);
      this.clipEnvelope.start();
      this.setCurrentRate(this.clipEnvelope.getCurrentRate());
      this.lastPulse = 0L;
   }

   void impl_pause() {
      double var1 = this.getCurrentRate();
      if (Math.abs(var1) >= 1.0E-12) {
         this.lastPlayedForward = Math.abs(this.getCurrentRate() - this.getRate()) < 1.0E-12;
      }

      this.setCurrentRate(0.0);
      this.setStatus(Animation.Status.PAUSED);
   }

   void impl_resume() {
      this.setStatus(Animation.Status.RUNNING);
      this.setCurrentRate(this.lastPlayedForward ? this.getRate() : -this.getRate());
   }

   void impl_stop() {
      if (!this.paused) {
         this.timer.removePulseReceiver(this.pulseReceiver);
      }

      this.setStatus(Animation.Status.STOPPED);
      this.setCurrentRate(0.0);
   }

   void impl_timePulse(long var1) {
      if (this.resolution == 1) {
         this.clipEnvelope.timePulse(var1);
      } else if (var1 - this.lastPulse >= (long)this.resolution) {
         this.lastPulse = var1 / (long)this.resolution * (long)this.resolution;
         this.clipEnvelope.timePulse(var1);
      }

   }

   abstract void impl_playTo(long var1, long var3);

   abstract void impl_jumpTo(long var1, long var3, boolean var5);

   void impl_setCurrentTicks(long var1) {
      this.currentTicks = var1;
      if (this.currentTime != null) {
         this.currentTime.fireValueChangedEvent();
      }

   }

   void impl_setCurrentRate(double var1) {
      this.setCurrentRate(var1);
   }

   final void impl_finished() {
      this.lastPlayedFinished = true;
      this.impl_stop();
      EventHandler var1 = this.getOnFinished();
      if (var1 != null) {
         try {
            var1.handle(new ActionEvent(this, (EventTarget)null));
         } catch (Exception var3) {
            Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), var3);
         }
      }

   }

   static {
      AnimationAccessorImpl.DEFAULT = new AnimationAccessorImpl();
      DEFAULT_CYCLE_DURATION = Duration.ZERO;
      DEFAULT_TOTAL_DURATION = Duration.ZERO;
      DEFAULT_DELAY = Duration.ZERO;
      DEFAULT_STATUS = Animation.Status.STOPPED;
      DEFAULT_ON_FINISHED = null;
   }

   private class CurrentTimeProperty extends ReadOnlyObjectPropertyBase {
      private CurrentTimeProperty() {
      }

      public Object getBean() {
         return Animation.this;
      }

      public String getName() {
         return "currentTime";
      }

      public Duration get() {
         return Animation.this.getCurrentTime();
      }

      public void fireValueChangedEvent() {
         super.fireValueChangedEvent();
      }

      // $FF: synthetic method
      CurrentTimeProperty(Object var2) {
         this();
      }
   }

   private class AnimationReadOnlyProperty extends ReadOnlyObjectPropertyBase {
      private final String name;
      private Object value;

      private AnimationReadOnlyProperty(String var2, Object var3) {
         this.name = var2;
         this.value = var3;
      }

      public Object getBean() {
         return Animation.this;
      }

      public String getName() {
         return this.name;
      }

      public Object get() {
         return this.value;
      }

      private void set(Object var1) {
         this.value = var1;
         this.fireValueChangedEvent();
      }

      // $FF: synthetic method
      AnimationReadOnlyProperty(String var2, Object var3, Object var4) {
         this(var2, var3);
      }
   }

   private class CurrentRateProperty extends ReadOnlyDoublePropertyBase {
      private double value;

      private CurrentRateProperty() {
      }

      public Object getBean() {
         return Animation.this;
      }

      public String getName() {
         return "currentRate";
      }

      public double get() {
         return this.value;
      }

      private void set(double var1) {
         this.value = var1;
         this.fireValueChangedEvent();
      }

      // $FF: synthetic method
      CurrentRateProperty(Object var2) {
         this();
      }
   }

   public static enum Status {
      PAUSED,
      RUNNING,
      STOPPED;
   }
}
