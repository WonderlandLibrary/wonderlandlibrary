package javafx.animation;

import com.sun.javafx.animation.TickCalculation;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.collections.VetoableListDecorator;
import com.sun.scenario.animation.AbstractPrimaryTimer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.scene.Node;
import javafx.util.Duration;

public final class ParallelTransition extends Transition {
   private static final Animation[] EMPTY_ANIMATION_ARRAY = new Animation[0];
   private static final double EPSILON = 1.0E-12;
   private Animation[] cachedChildren;
   private long[] durations;
   private long[] delays;
   private double[] rates;
   private long[] offsetTicks;
   private boolean[] forceChildSync;
   private long oldTicks;
   private long cycleTime;
   private boolean childrenChanged;
   private boolean toggledRate;
   private final InvalidationListener childrenListener;
   private final ChangeListener rateListener;
   private ObjectProperty node;
   private static final Node DEFAULT_NODE = null;
   private final Set childrenSet;
   private final ObservableList children;

   public final void setNode(Node var1) {
      if (this.node != null || var1 != null) {
         this.nodeProperty().set(var1);
      }

   }

   public final Node getNode() {
      return this.node == null ? DEFAULT_NODE : (Node)this.node.get();
   }

   public final ObjectProperty nodeProperty() {
      if (this.node == null) {
         this.node = new SimpleObjectProperty(this, "node", DEFAULT_NODE);
      }

      return this.node;
   }

   private static boolean checkCycle(Animation var0, Animation var1) {
      for(Animation var2 = var1; var2 != var0; var2 = var2.parent) {
         if (var2.parent == null) {
            return false;
         }
      }

      return true;
   }

   public final ObservableList getChildren() {
      return this.children;
   }

   public ParallelTransition(Node var1, Animation... var2) {
      this.cachedChildren = EMPTY_ANIMATION_ARRAY;
      this.childrenChanged = true;
      this.childrenListener = (var1x) -> {
         this.childrenChanged = true;
         if (this.getStatus() == Animation.Status.STOPPED) {
            this.setCycleDuration(this.computeCycleDuration());
         }

      };
      this.rateListener = new ChangeListener() {
         public void changed(ObservableValue var1, Number var2, Number var3) {
            if (var2.doubleValue() * var3.doubleValue() < 0.0) {
               for(int var4 = 0; var4 < ParallelTransition.this.cachedChildren.length; ++var4) {
                  Animation var5 = ParallelTransition.this.cachedChildren[var4];
                  var5.clipEnvelope.setRate(ParallelTransition.this.rates[var4] * Math.signum(ParallelTransition.this.getCurrentRate()));
               }

               ParallelTransition.this.toggledRate = true;
            }

         }
      };
      this.childrenSet = new HashSet();
      this.children = new VetoableListDecorator(new TrackableObservableList() {
         protected void onChanged(ListChangeListener.Change var1) {
            while(var1.next()) {
               Iterator var2 = var1.getRemoved().iterator();

               Animation var3;
               while(var2.hasNext()) {
                  var3 = (Animation)var2.next();
                  var3.parent = null;
                  var3.rateProperty().removeListener(ParallelTransition.this.childrenListener);
                  var3.totalDurationProperty().removeListener(ParallelTransition.this.childrenListener);
                  var3.delayProperty().removeListener(ParallelTransition.this.childrenListener);
               }

               var2 = var1.getAddedSubList().iterator();

               while(var2.hasNext()) {
                  var3 = (Animation)var2.next();
                  var3.parent = ParallelTransition.this;
                  var3.rateProperty().addListener(ParallelTransition.this.childrenListener);
                  var3.totalDurationProperty().addListener(ParallelTransition.this.childrenListener);
                  var3.delayProperty().addListener(ParallelTransition.this.childrenListener);
               }
            }

            ParallelTransition.this.childrenListener.invalidated(ParallelTransition.this.children);
         }
      }) {
         protected void onProposedChange(List var1, int... var2) {
            IllegalArgumentException var3 = null;

            for(int var4 = 0; var4 < var2.length; var4 += 2) {
               for(int var5 = var2[var4]; var5 < var2[var4 + 1]; ++var5) {
                  ParallelTransition.this.childrenSet.remove(ParallelTransition.this.children.get(var5));
               }
            }

            Iterator var6 = var1.iterator();

            while(var6.hasNext()) {
               Animation var7 = (Animation)var6.next();
               if (var7 == null) {
                  var3 = new IllegalArgumentException("Child cannot be null");
                  break;
               }

               if (!ParallelTransition.this.childrenSet.add(var7)) {
                  var3 = new IllegalArgumentException("Attempting to add a duplicate to the list of children");
                  break;
               }

               if (ParallelTransition.checkCycle(var7, ParallelTransition.this)) {
                  var3 = new IllegalArgumentException("This change would create cycle");
                  break;
               }
            }

            if (var3 != null) {
               ParallelTransition.this.childrenSet.clear();
               ParallelTransition.this.childrenSet.addAll(ParallelTransition.this.children);
               throw var3;
            }
         }
      };
      this.setInterpolator(Interpolator.LINEAR);
      this.setNode(var1);
      this.getChildren().setAll((Object[])var2);
   }

   public ParallelTransition(Animation... var1) {
      this((Node)null, var1);
   }

   public ParallelTransition(Node var1) {
      this.cachedChildren = EMPTY_ANIMATION_ARRAY;
      this.childrenChanged = true;
      this.childrenListener = (var1x) -> {
         this.childrenChanged = true;
         if (this.getStatus() == Animation.Status.STOPPED) {
            this.setCycleDuration(this.computeCycleDuration());
         }

      };
      this.rateListener = new ChangeListener() {
         public void changed(ObservableValue var1, Number var2, Number var3) {
            if (var2.doubleValue() * var3.doubleValue() < 0.0) {
               for(int var4 = 0; var4 < ParallelTransition.this.cachedChildren.length; ++var4) {
                  Animation var5 = ParallelTransition.this.cachedChildren[var4];
                  var5.clipEnvelope.setRate(ParallelTransition.this.rates[var4] * Math.signum(ParallelTransition.this.getCurrentRate()));
               }

               ParallelTransition.this.toggledRate = true;
            }

         }
      };
      this.childrenSet = new HashSet();
      this.children = new VetoableListDecorator(new TrackableObservableList() {
         protected void onChanged(ListChangeListener.Change var1) {
            while(var1.next()) {
               Iterator var2 = var1.getRemoved().iterator();

               Animation var3;
               while(var2.hasNext()) {
                  var3 = (Animation)var2.next();
                  var3.parent = null;
                  var3.rateProperty().removeListener(ParallelTransition.this.childrenListener);
                  var3.totalDurationProperty().removeListener(ParallelTransition.this.childrenListener);
                  var3.delayProperty().removeListener(ParallelTransition.this.childrenListener);
               }

               var2 = var1.getAddedSubList().iterator();

               while(var2.hasNext()) {
                  var3 = (Animation)var2.next();
                  var3.parent = ParallelTransition.this;
                  var3.rateProperty().addListener(ParallelTransition.this.childrenListener);
                  var3.totalDurationProperty().addListener(ParallelTransition.this.childrenListener);
                  var3.delayProperty().addListener(ParallelTransition.this.childrenListener);
               }
            }

            ParallelTransition.this.childrenListener.invalidated(ParallelTransition.this.children);
         }
      }) {
         protected void onProposedChange(List var1, int... var2) {
            IllegalArgumentException var3 = null;

            for(int var4 = 0; var4 < var2.length; var4 += 2) {
               for(int var5 = var2[var4]; var5 < var2[var4 + 1]; ++var5) {
                  ParallelTransition.this.childrenSet.remove(ParallelTransition.this.children.get(var5));
               }
            }

            Iterator var6 = var1.iterator();

            while(var6.hasNext()) {
               Animation var7 = (Animation)var6.next();
               if (var7 == null) {
                  var3 = new IllegalArgumentException("Child cannot be null");
                  break;
               }

               if (!ParallelTransition.this.childrenSet.add(var7)) {
                  var3 = new IllegalArgumentException("Attempting to add a duplicate to the list of children");
                  break;
               }

               if (ParallelTransition.checkCycle(var7, ParallelTransition.this)) {
                  var3 = new IllegalArgumentException("This change would create cycle");
                  break;
               }
            }

            if (var3 != null) {
               ParallelTransition.this.childrenSet.clear();
               ParallelTransition.this.childrenSet.addAll(ParallelTransition.this.children);
               throw var3;
            }
         }
      };
      this.setInterpolator(Interpolator.LINEAR);
      this.setNode(var1);
   }

   public ParallelTransition() {
      this((Node)null);
   }

   ParallelTransition(AbstractPrimaryTimer var1) {
      super(var1);
      this.cachedChildren = EMPTY_ANIMATION_ARRAY;
      this.childrenChanged = true;
      this.childrenListener = (var1x) -> {
         this.childrenChanged = true;
         if (this.getStatus() == Animation.Status.STOPPED) {
            this.setCycleDuration(this.computeCycleDuration());
         }

      };
      this.rateListener = new ChangeListener() {
         public void changed(ObservableValue var1, Number var2, Number var3) {
            if (var2.doubleValue() * var3.doubleValue() < 0.0) {
               for(int var4 = 0; var4 < ParallelTransition.this.cachedChildren.length; ++var4) {
                  Animation var5 = ParallelTransition.this.cachedChildren[var4];
                  var5.clipEnvelope.setRate(ParallelTransition.this.rates[var4] * Math.signum(ParallelTransition.this.getCurrentRate()));
               }

               ParallelTransition.this.toggledRate = true;
            }

         }
      };
      this.childrenSet = new HashSet();
      this.children = new VetoableListDecorator(new TrackableObservableList() {
         protected void onChanged(ListChangeListener.Change var1) {
            while(var1.next()) {
               Iterator var2 = var1.getRemoved().iterator();

               Animation var3;
               while(var2.hasNext()) {
                  var3 = (Animation)var2.next();
                  var3.parent = null;
                  var3.rateProperty().removeListener(ParallelTransition.this.childrenListener);
                  var3.totalDurationProperty().removeListener(ParallelTransition.this.childrenListener);
                  var3.delayProperty().removeListener(ParallelTransition.this.childrenListener);
               }

               var2 = var1.getAddedSubList().iterator();

               while(var2.hasNext()) {
                  var3 = (Animation)var2.next();
                  var3.parent = ParallelTransition.this;
                  var3.rateProperty().addListener(ParallelTransition.this.childrenListener);
                  var3.totalDurationProperty().addListener(ParallelTransition.this.childrenListener);
                  var3.delayProperty().addListener(ParallelTransition.this.childrenListener);
               }
            }

            ParallelTransition.this.childrenListener.invalidated(ParallelTransition.this.children);
         }
      }) {
         protected void onProposedChange(List var1, int... var2) {
            IllegalArgumentException var3 = null;

            for(int var4 = 0; var4 < var2.length; var4 += 2) {
               for(int var5 = var2[var4]; var5 < var2[var4 + 1]; ++var5) {
                  ParallelTransition.this.childrenSet.remove(ParallelTransition.this.children.get(var5));
               }
            }

            Iterator var6 = var1.iterator();

            while(var6.hasNext()) {
               Animation var7 = (Animation)var6.next();
               if (var7 == null) {
                  var3 = new IllegalArgumentException("Child cannot be null");
                  break;
               }

               if (!ParallelTransition.this.childrenSet.add(var7)) {
                  var3 = new IllegalArgumentException("Attempting to add a duplicate to the list of children");
                  break;
               }

               if (ParallelTransition.checkCycle(var7, ParallelTransition.this)) {
                  var3 = new IllegalArgumentException("This change would create cycle");
                  break;
               }
            }

            if (var3 != null) {
               ParallelTransition.this.childrenSet.clear();
               ParallelTransition.this.childrenSet.addAll(ParallelTransition.this.children);
               throw var3;
            }
         }
      };
      this.setInterpolator(Interpolator.LINEAR);
   }

   protected Node getParentTargetNode() {
      Node var1 = this.getNode();
      return var1 != null ? var1 : (this.parent != null && this.parent instanceof Transition ? ((Transition)this.parent).getParentTargetNode() : null);
   }

   private Duration computeCycleDuration() {
      Duration var1 = Duration.ZERO;
      Iterator var2 = this.getChildren().iterator();

      while(var2.hasNext()) {
         Animation var3 = (Animation)var2.next();
         double var4 = Math.abs(var3.getRate());
         Duration var6 = var4 < 1.0E-12 ? var3.getTotalDuration() : var3.getTotalDuration().divide(var4);
         Duration var7 = var6.add(var3.getDelay());
         if (var7.isIndefinite()) {
            return Duration.INDEFINITE;
         }

         if (var7.greaterThan(var1)) {
            var1 = var7;
         }
      }

      return var1;
   }

   private double calculateFraction(long var1, long var3) {
      double var5 = (double)var1 / (double)var3;
      return var5 <= 0.0 ? 0.0 : (var5 >= 1.0 ? 1.0 : var5);
   }

   private boolean startChild(Animation var1, int var2) {
      boolean var3 = this.forceChildSync[var2];
      if (var1.impl_startable(var3)) {
         var1.clipEnvelope.setRate(this.rates[var2] * Math.signum(this.getCurrentRate()));
         var1.impl_start(var3);
         this.forceChildSync[var2] = false;
         return true;
      } else {
         return false;
      }
   }

   void impl_sync(boolean var1) {
      super.impl_sync(var1);
      int var2;
      int var3;
      if ((!var1 || !this.childrenChanged) && this.durations != null) {
         if (var1) {
            var2 = this.forceChildSync.length;

            for(var3 = 0; var3 < var2; ++var3) {
               this.forceChildSync[var3] = true;
            }
         }
      } else {
         this.cachedChildren = (Animation[])this.getChildren().toArray(EMPTY_ANIMATION_ARRAY);
         var2 = this.cachedChildren.length;
         this.durations = new long[var2];
         this.delays = new long[var2];
         this.rates = new double[var2];
         this.offsetTicks = new long[var2];
         this.forceChildSync = new boolean[var2];
         this.cycleTime = 0L;
         var3 = 0;
         Animation[] var4 = this.cachedChildren;
         int var5 = var4.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            Animation var7 = var4[var6];
            this.rates[var3] = Math.abs(var7.getRate());
            if (this.rates[var3] < 1.0E-12) {
               this.rates[var3] = 1.0;
            }

            this.durations[var3] = TickCalculation.fromDuration(var7.getTotalDuration(), this.rates[var3]);
            this.delays[var3] = TickCalculation.fromDuration(var7.getDelay());
            this.cycleTime = Math.max(this.cycleTime, TickCalculation.add(this.durations[var3], this.delays[var3]));
            this.forceChildSync[var3] = true;
            ++var3;
         }

         this.childrenChanged = false;
      }

   }

   void impl_pause() {
      super.impl_pause();
      Animation[] var1 = this.cachedChildren;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Animation var4 = var1[var3];
         if (var4.getStatus() == Animation.Status.RUNNING) {
            var4.impl_pause();
         }
      }

   }

   void impl_resume() {
      super.impl_resume();
      int var1 = 0;
      Animation[] var2 = this.cachedChildren;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Animation var5 = var2[var4];
         if (var5.getStatus() == Animation.Status.PAUSED) {
            var5.impl_resume();
            var5.clipEnvelope.setRate(this.rates[var1] * Math.signum(this.getCurrentRate()));
         }

         ++var1;
      }

   }

   void impl_start(boolean var1) {
      super.impl_start(var1);
      this.toggledRate = false;
      this.rateProperty().addListener(this.rateListener);
      double var2 = this.getCurrentRate();
      long var4 = TickCalculation.fromDuration(this.getCurrentTime());
      if (var2 < 0.0) {
         this.jumpToEnd();
         if (var4 < this.cycleTime) {
            this.impl_jumpTo(var4, this.cycleTime, false);
         }
      } else {
         this.jumpToStart();
         if (var4 > 0L) {
            this.impl_jumpTo(var4, this.cycleTime, false);
         }
      }

   }

   void impl_stop() {
      super.impl_stop();
      Animation[] var1 = this.cachedChildren;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         Animation var4 = var1[var3];
         if (var4.getStatus() != Animation.Status.STOPPED) {
            var4.impl_stop();
         }
      }

      if (this.childrenChanged) {
         this.setCycleDuration(this.computeCycleDuration());
      }

      this.rateProperty().removeListener(this.rateListener);
   }

   /** @deprecated */
   @Deprecated
   public void impl_playTo(long var1, long var3) {
      this.impl_setCurrentTicks(var1);
      double var5 = this.calculateFraction(var1, var3);
      long var7 = Math.max(0L, Math.min(this.getCachedInterpolator().interpolate(0L, var3, var5), var3));
      int var9;
      if (this.toggledRate) {
         for(var9 = 0; var9 < this.cachedChildren.length; ++var9) {
            if (this.cachedChildren[var9].getStatus() == Animation.Status.RUNNING) {
               long[] var10000 = this.offsetTicks;
               var10000[var9] = (long)((double)var10000[var9] - Math.signum(this.getCurrentRate()) * (double)(this.durations[var9] - 2L * (this.oldTicks - this.delays[var9])));
            }
         }

         this.toggledRate = false;
      }

      Animation[] var10;
      int var11;
      int var12;
      Animation var13;
      boolean var14;
      EventHandler var15;
      if (this.getCurrentRate() > 0.0) {
         var9 = 0;
         var10 = this.cachedChildren;
         var11 = var10.length;

         for(var12 = 0; var12 < var11; ++var12) {
            var13 = var10[var12];
            if (var7 >= this.delays[var9] && (this.oldTicks <= this.delays[var9] || var7 < TickCalculation.add(this.delays[var9], this.durations[var9]) && var13.getStatus() == Animation.Status.STOPPED)) {
               var14 = this.oldTicks <= this.delays[var9];
               if (!this.startChild(var13, var9)) {
                  if (var14) {
                     var15 = var13.getOnFinished();
                     if (var15 != null) {
                        var15.handle(new ActionEvent(this, (EventTarget)null));
                     }
                  }
                  continue;
               }

               var13.clipEnvelope.jumpTo(0L);
            }

            if (var7 >= TickCalculation.add(this.durations[var9], this.delays[var9])) {
               if (var13.getStatus() == Animation.Status.RUNNING) {
                  var13.impl_timePulse(TickCalculation.sub(this.durations[var9], this.offsetTicks[var9]));
                  this.offsetTicks[var9] = 0L;
               }
            } else if (var7 > this.delays[var9]) {
               var13.impl_timePulse(TickCalculation.sub(var7 - this.delays[var9], this.offsetTicks[var9]));
            }

            ++var9;
         }
      } else {
         var9 = 0;
         var10 = this.cachedChildren;
         var11 = var10.length;

         for(var12 = 0; var12 < var11; ++var12) {
            var13 = var10[var12];
            if (var7 < TickCalculation.add(this.durations[var9], this.delays[var9])) {
               if (this.oldTicks >= TickCalculation.add(this.durations[var9], this.delays[var9]) || var7 >= this.delays[var9] && var13.getStatus() == Animation.Status.STOPPED) {
                  var14 = this.oldTicks >= TickCalculation.add(this.durations[var9], this.delays[var9]);
                  if (!this.startChild(var13, var9)) {
                     if (var14) {
                        var15 = var13.getOnFinished();
                        if (var15 != null) {
                           var15.handle(new ActionEvent(this, (EventTarget)null));
                        }
                     }
                     continue;
                  }

                  var13.clipEnvelope.jumpTo(Math.round((double)this.durations[var9] * this.rates[var9]));
               }

               if (var7 <= this.delays[var9]) {
                  if (var13.getStatus() == Animation.Status.RUNNING) {
                     var13.impl_timePulse(TickCalculation.sub(this.durations[var9], this.offsetTicks[var9]));
                     this.offsetTicks[var9] = 0L;
                  }
               } else {
                  var13.impl_timePulse(TickCalculation.sub(TickCalculation.add(this.durations[var9], this.delays[var9]) - var7, this.offsetTicks[var9]));
               }
            }

            ++var9;
         }
      }

      this.oldTicks = var7;
   }

   /** @deprecated */
   @Deprecated
   public void impl_jumpTo(long var1, long var3, boolean var5) {
      this.impl_setCurrentTicks(var1);
      if (this.getStatus() != Animation.Status.STOPPED || var5) {
         this.impl_sync(false);
         double var6 = this.calculateFraction(var1, var3);
         long var8 = Math.max(0L, Math.min(this.getCachedInterpolator().interpolate(0L, var3, var6), var3));
         int var10 = 0;
         Animation[] var11 = this.cachedChildren;
         int var12 = var11.length;

         for(int var13 = 0; var13 < var12; ++var13) {
            Animation var14 = var11[var13];
            Animation.Status var15 = var14.getStatus();
            if (var8 <= this.delays[var10]) {
               this.offsetTicks[var10] = 0L;
               if (var15 != Animation.Status.STOPPED) {
                  var14.clipEnvelope.jumpTo(0L);
                  var14.impl_stop();
               } else if (TickCalculation.fromDuration(var14.getCurrentTime()) != 0L) {
                  var14.impl_jumpTo(0L, this.durations[var10], true);
               }
            } else if (var8 >= TickCalculation.add(this.durations[var10], this.delays[var10])) {
               this.offsetTicks[var10] = 0L;
               if (var15 != Animation.Status.STOPPED) {
                  var14.clipEnvelope.jumpTo(Math.round((double)this.durations[var10] * this.rates[var10]));
                  var14.impl_stop();
               } else if (TickCalculation.fromDuration(var14.getCurrentTime()) != this.durations[var10]) {
                  var14.impl_jumpTo(this.durations[var10], this.durations[var10], true);
               }
            } else {
               if (var15 == Animation.Status.STOPPED) {
                  this.startChild(var14, var10);
                  if (this.getStatus() == Animation.Status.PAUSED) {
                     var14.impl_pause();
                  }

                  this.offsetTicks[var10] = this.getCurrentRate() > 0.0 ? var8 - this.delays[var10] : TickCalculation.add(this.durations[var10], this.delays[var10]) - var8;
               } else {
                  long[] var10000;
                  if (var15 == Animation.Status.PAUSED) {
                     var10000 = this.offsetTicks;
                     var10000[var10] = (long)((double)var10000[var10] + (double)(var8 - this.oldTicks) * Math.signum(this.clipEnvelope.getCurrentRate()));
                  } else {
                     var10000 = this.offsetTicks;
                     var10000[var10] += this.getCurrentRate() > 0.0 ? var8 - this.oldTicks : this.oldTicks - var8;
                  }
               }

               var14.clipEnvelope.jumpTo(Math.round((double)TickCalculation.sub(var8, this.delays[var10]) * this.rates[var10]));
            }

            ++var10;
         }

         this.oldTicks = var8;
      }
   }

   protected void interpolate(double var1) {
   }

   private void jumpToEnd() {
      for(int var1 = 0; var1 < this.cachedChildren.length; ++var1) {
         if (this.forceChildSync[var1]) {
            this.cachedChildren[var1].impl_sync(true);
         }

         this.cachedChildren[var1].impl_jumpTo(this.durations[var1], this.durations[var1], true);
      }

   }

   private void jumpToStart() {
      for(int var1 = this.cachedChildren.length - 1; var1 >= 0; --var1) {
         if (this.forceChildSync[var1]) {
            this.cachedChildren[var1].impl_sync(true);
         }

         this.cachedChildren[var1].impl_jumpTo(0L, this.durations[var1], true);
      }

   }
}
