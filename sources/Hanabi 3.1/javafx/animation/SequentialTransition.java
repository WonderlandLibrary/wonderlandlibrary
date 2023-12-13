package javafx.animation;

import com.sun.javafx.animation.TickCalculation;
import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.collections.VetoableListDecorator;
import com.sun.scenario.animation.AbstractPrimaryTimer;
import java.util.Arrays;
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

public final class SequentialTransition extends Transition {
   private static final Animation[] EMPTY_ANIMATION_ARRAY = new Animation[0];
   private static final int BEFORE = -1;
   private static final double EPSILON = 1.0E-12;
   private Animation[] cachedChildren;
   private long[] startTimes;
   private long[] durations;
   private long[] delays;
   private double[] rates;
   private boolean[] forceChildSync;
   private int end;
   private int curIndex;
   private long oldTicks;
   private long offsetTicks;
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

   public SequentialTransition(Node var1, Animation... var2) {
      this.cachedChildren = EMPTY_ANIMATION_ARRAY;
      this.curIndex = -1;
      this.oldTicks = 0L;
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
               for(int var4 = 0; var4 < SequentialTransition.this.cachedChildren.length; ++var4) {
                  Animation var5 = SequentialTransition.this.cachedChildren[var4];
                  var5.clipEnvelope.setRate(SequentialTransition.this.rates[var4] * Math.signum(SequentialTransition.this.getCurrentRate()));
               }

               SequentialTransition.this.toggledRate = true;
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
                  var3.rateProperty().removeListener(SequentialTransition.this.childrenListener);
                  var3.totalDurationProperty().removeListener(SequentialTransition.this.childrenListener);
                  var3.delayProperty().removeListener(SequentialTransition.this.childrenListener);
               }

               var2 = var1.getAddedSubList().iterator();

               while(var2.hasNext()) {
                  var3 = (Animation)var2.next();
                  var3.parent = SequentialTransition.this;
                  var3.rateProperty().addListener(SequentialTransition.this.childrenListener);
                  var3.totalDurationProperty().addListener(SequentialTransition.this.childrenListener);
                  var3.delayProperty().addListener(SequentialTransition.this.childrenListener);
               }
            }

            SequentialTransition.this.childrenListener.invalidated(SequentialTransition.this.children);
         }
      }) {
         protected void onProposedChange(List var1, int... var2) {
            IllegalArgumentException var3 = null;

            for(int var4 = 0; var4 < var2.length; var4 += 2) {
               for(int var5 = var2[var4]; var5 < var2[var4 + 1]; ++var5) {
                  SequentialTransition.this.childrenSet.remove(SequentialTransition.this.children.get(var5));
               }
            }

            Iterator var6 = var1.iterator();

            while(var6.hasNext()) {
               Animation var7 = (Animation)var6.next();
               if (var7 == null) {
                  var3 = new IllegalArgumentException("Child cannot be null");
                  break;
               }

               if (!SequentialTransition.this.childrenSet.add(var7)) {
                  var3 = new IllegalArgumentException("Attempting to add a duplicate to the list of children");
                  break;
               }

               if (SequentialTransition.checkCycle(var7, SequentialTransition.this)) {
                  var3 = new IllegalArgumentException("This change would create cycle");
                  break;
               }
            }

            if (var3 != null) {
               SequentialTransition.this.childrenSet.clear();
               SequentialTransition.this.childrenSet.addAll(SequentialTransition.this.children);
               throw var3;
            }
         }
      };
      this.setInterpolator(Interpolator.LINEAR);
      this.setNode(var1);
      this.getChildren().setAll((Object[])var2);
   }

   public SequentialTransition(Animation... var1) {
      this((Node)null, var1);
   }

   public SequentialTransition(Node var1) {
      this.cachedChildren = EMPTY_ANIMATION_ARRAY;
      this.curIndex = -1;
      this.oldTicks = 0L;
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
               for(int var4 = 0; var4 < SequentialTransition.this.cachedChildren.length; ++var4) {
                  Animation var5 = SequentialTransition.this.cachedChildren[var4];
                  var5.clipEnvelope.setRate(SequentialTransition.this.rates[var4] * Math.signum(SequentialTransition.this.getCurrentRate()));
               }

               SequentialTransition.this.toggledRate = true;
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
                  var3.rateProperty().removeListener(SequentialTransition.this.childrenListener);
                  var3.totalDurationProperty().removeListener(SequentialTransition.this.childrenListener);
                  var3.delayProperty().removeListener(SequentialTransition.this.childrenListener);
               }

               var2 = var1.getAddedSubList().iterator();

               while(var2.hasNext()) {
                  var3 = (Animation)var2.next();
                  var3.parent = SequentialTransition.this;
                  var3.rateProperty().addListener(SequentialTransition.this.childrenListener);
                  var3.totalDurationProperty().addListener(SequentialTransition.this.childrenListener);
                  var3.delayProperty().addListener(SequentialTransition.this.childrenListener);
               }
            }

            SequentialTransition.this.childrenListener.invalidated(SequentialTransition.this.children);
         }
      }) {
         protected void onProposedChange(List var1, int... var2) {
            IllegalArgumentException var3 = null;

            for(int var4 = 0; var4 < var2.length; var4 += 2) {
               for(int var5 = var2[var4]; var5 < var2[var4 + 1]; ++var5) {
                  SequentialTransition.this.childrenSet.remove(SequentialTransition.this.children.get(var5));
               }
            }

            Iterator var6 = var1.iterator();

            while(var6.hasNext()) {
               Animation var7 = (Animation)var6.next();
               if (var7 == null) {
                  var3 = new IllegalArgumentException("Child cannot be null");
                  break;
               }

               if (!SequentialTransition.this.childrenSet.add(var7)) {
                  var3 = new IllegalArgumentException("Attempting to add a duplicate to the list of children");
                  break;
               }

               if (SequentialTransition.checkCycle(var7, SequentialTransition.this)) {
                  var3 = new IllegalArgumentException("This change would create cycle");
                  break;
               }
            }

            if (var3 != null) {
               SequentialTransition.this.childrenSet.clear();
               SequentialTransition.this.childrenSet.addAll(SequentialTransition.this.children);
               throw var3;
            }
         }
      };
      this.setInterpolator(Interpolator.LINEAR);
      this.setNode(var1);
   }

   public SequentialTransition() {
      this((Node)null);
   }

   SequentialTransition(AbstractPrimaryTimer var1) {
      super(var1);
      this.cachedChildren = EMPTY_ANIMATION_ARRAY;
      this.curIndex = -1;
      this.oldTicks = 0L;
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
               for(int var4 = 0; var4 < SequentialTransition.this.cachedChildren.length; ++var4) {
                  Animation var5 = SequentialTransition.this.cachedChildren[var4];
                  var5.clipEnvelope.setRate(SequentialTransition.this.rates[var4] * Math.signum(SequentialTransition.this.getCurrentRate()));
               }

               SequentialTransition.this.toggledRate = true;
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
                  var3.rateProperty().removeListener(SequentialTransition.this.childrenListener);
                  var3.totalDurationProperty().removeListener(SequentialTransition.this.childrenListener);
                  var3.delayProperty().removeListener(SequentialTransition.this.childrenListener);
               }

               var2 = var1.getAddedSubList().iterator();

               while(var2.hasNext()) {
                  var3 = (Animation)var2.next();
                  var3.parent = SequentialTransition.this;
                  var3.rateProperty().addListener(SequentialTransition.this.childrenListener);
                  var3.totalDurationProperty().addListener(SequentialTransition.this.childrenListener);
                  var3.delayProperty().addListener(SequentialTransition.this.childrenListener);
               }
            }

            SequentialTransition.this.childrenListener.invalidated(SequentialTransition.this.children);
         }
      }) {
         protected void onProposedChange(List var1, int... var2) {
            IllegalArgumentException var3 = null;

            for(int var4 = 0; var4 < var2.length; var4 += 2) {
               for(int var5 = var2[var4]; var5 < var2[var4 + 1]; ++var5) {
                  SequentialTransition.this.childrenSet.remove(SequentialTransition.this.children.get(var5));
               }
            }

            Iterator var6 = var1.iterator();

            while(var6.hasNext()) {
               Animation var7 = (Animation)var6.next();
               if (var7 == null) {
                  var3 = new IllegalArgumentException("Child cannot be null");
                  break;
               }

               if (!SequentialTransition.this.childrenSet.add(var7)) {
                  var3 = new IllegalArgumentException("Attempting to add a duplicate to the list of children");
                  break;
               }

               if (SequentialTransition.checkCycle(var7, SequentialTransition.this)) {
                  var3 = new IllegalArgumentException("This change would create cycle");
                  break;
               }
            }

            if (var3 != null) {
               SequentialTransition.this.childrenSet.clear();
               SequentialTransition.this.childrenSet.addAll(SequentialTransition.this.children);
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
         var1 = var1.add(var3.getDelay());
         double var4 = Math.abs(var3.getRate());
         var1 = var1.add(var4 < 1.0E-12 ? var3.getTotalDuration() : var3.getTotalDuration().divide(var4));
         if (var1.isIndefinite()) {
            break;
         }
      }

      return var1;
   }

   private double calculateFraction(long var1, long var3) {
      double var5 = (double)var1 / (double)var3;
      return var5 <= 0.0 ? 0.0 : (var5 >= 1.0 ? 1.0 : var5);
   }

   private int findNewIndex(long var1) {
      if (this.curIndex != -1 && this.curIndex != this.end && this.startTimes[this.curIndex] <= var1 && var1 <= this.startTimes[this.curIndex + 1]) {
         return this.curIndex;
      } else {
         boolean var3 = this.curIndex == -1 || this.curIndex == this.end;
         int var4 = !var3 && var1 >= this.oldTicks ? this.curIndex + 1 : 0;
         int var5 = !var3 && this.oldTicks >= var1 ? this.curIndex : this.end;
         int var6 = Arrays.binarySearch(this.startTimes, var4, var5, var1);
         return var6 < 0 ? -var6 - 2 : (var6 > 0 ? var6 - 1 : 0);
      }
   }

   void impl_sync(boolean var1) {
      super.impl_sync(var1);
      if ((!var1 || !this.childrenChanged) && this.startTimes != null) {
         if (var1) {
            int var9 = this.forceChildSync.length;

            for(int var3 = 0; var3 < var9; ++var3) {
               this.forceChildSync[var3] = true;
            }
         }
      } else {
         this.cachedChildren = (Animation[])this.getChildren().toArray(EMPTY_ANIMATION_ARRAY);
         this.end = this.cachedChildren.length;
         this.startTimes = new long[this.end + 1];
         this.durations = new long[this.end];
         this.delays = new long[this.end];
         this.rates = new double[this.end];
         this.forceChildSync = new boolean[this.end];
         long var2 = 0L;
         int var4 = 0;
         Animation[] var5 = this.cachedChildren;
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Animation var8 = var5[var7];
            this.startTimes[var4] = var2;
            this.rates[var4] = Math.abs(var8.getRate());
            if (this.rates[var4] < 1.0E-12) {
               this.rates[var4] = 1.0;
            }

            this.durations[var4] = TickCalculation.fromDuration(var8.getTotalDuration(), this.rates[var4]);
            this.delays[var4] = TickCalculation.fromDuration(var8.getDelay());
            if (this.durations[var4] != Long.MAX_VALUE && this.delays[var4] != Long.MAX_VALUE && var2 != Long.MAX_VALUE) {
               var2 = TickCalculation.add(var2, TickCalculation.add(this.durations[var4], this.delays[var4]));
            } else {
               var2 = Long.MAX_VALUE;
            }

            this.forceChildSync[var4] = true;
            ++var4;
         }

         this.startTimes[this.end] = var2;
         this.childrenChanged = false;
      }

   }

   void impl_start(boolean var1) {
      super.impl_start(var1);
      this.toggledRate = false;
      this.rateProperty().addListener(this.rateListener);
      this.offsetTicks = 0L;
      double var2 = this.getCurrentRate();
      long var4 = TickCalculation.fromDuration(this.getCurrentTime());
      if (var2 < 0.0) {
         this.jumpToEnd();
         this.curIndex = this.end;
         if (var4 < this.startTimes[this.end]) {
            this.impl_jumpTo(var4, this.startTimes[this.end], false);
         }
      } else {
         this.jumpToBefore();
         this.curIndex = -1;
         if (var4 > 0L) {
            this.impl_jumpTo(var4, this.startTimes[this.end], false);
         }
      }

   }

   void impl_pause() {
      super.impl_pause();
      if (this.curIndex != -1 && this.curIndex != this.end) {
         Animation var1 = this.cachedChildren[this.curIndex];
         if (var1.getStatus() == Animation.Status.RUNNING) {
            var1.impl_pause();
         }
      }

   }

   void impl_resume() {
      super.impl_resume();
      if (this.curIndex != -1 && this.curIndex != this.end) {
         Animation var1 = this.cachedChildren[this.curIndex];
         if (var1.getStatus() == Animation.Status.PAUSED) {
            var1.impl_resume();
            var1.clipEnvelope.setRate(this.rates[this.curIndex] * Math.signum(this.getCurrentRate()));
         }
      }

   }

   void impl_stop() {
      super.impl_stop();
      if (this.curIndex != -1 && this.curIndex != this.end) {
         Animation var1 = this.cachedChildren[this.curIndex];
         if (var1.getStatus() != Animation.Status.STOPPED) {
            var1.impl_stop();
         }
      }

      if (this.childrenChanged) {
         this.setCycleDuration(this.computeCycleDuration());
      }

      this.rateProperty().removeListener(this.rateListener);
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

   void impl_playTo(long var1, long var3) {
      this.impl_setCurrentTicks(var1);
      double var5 = this.calculateFraction(var1, var3);
      long var7 = Math.max(0L, Math.min(this.getCachedInterpolator().interpolate(0L, var3, var5), var3));
      int var9 = this.findNewIndex(var7);
      Animation var10 = this.curIndex != -1 && this.curIndex != this.end ? this.cachedChildren[this.curIndex] : null;
      if (this.toggledRate) {
         if (var10 != null && var10.getStatus() == Animation.Status.RUNNING) {
            this.offsetTicks = (long)((double)this.offsetTicks - Math.signum(this.getCurrentRate()) * (double)(this.durations[this.curIndex] - 2L * (this.oldTicks - this.delays[this.curIndex] - this.startTimes[this.curIndex])));
         }

         this.toggledRate = false;
      }

      long var11;
      boolean var13;
      EventHandler var14;
      if (this.curIndex == var9) {
         long var17;
         if (this.getCurrentRate() > 0.0) {
            var11 = TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex]);
            if (var7 >= var11) {
               if (this.oldTicks <= var11 || var10.getStatus() == Animation.Status.STOPPED) {
                  var13 = this.oldTicks <= var11;
                  if (var13) {
                     var10.clipEnvelope.jumpTo(0L);
                  }

                  if (!this.startChild(var10, this.curIndex)) {
                     if (var13) {
                        var14 = var10.getOnFinished();
                        if (var14 != null) {
                           var14.handle(new ActionEvent(this, (EventTarget)null));
                        }
                     }

                     this.oldTicks = var7;
                     return;
                  }
               }

               if (var7 >= this.startTimes[this.curIndex + 1]) {
                  var10.impl_timePulse(TickCalculation.sub(this.durations[this.curIndex], this.offsetTicks));
                  if (var7 == var3) {
                     this.curIndex = this.end;
                  }
               } else {
                  var17 = TickCalculation.sub(var7 - var11, this.offsetTicks);
                  var10.impl_timePulse(var17);
               }
            }
         } else {
            var11 = TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex]);
            if (this.oldTicks >= this.startTimes[this.curIndex + 1] || this.oldTicks >= var11 && var10.getStatus() == Animation.Status.STOPPED) {
               var13 = this.oldTicks >= this.startTimes[this.curIndex + 1];
               if (var13) {
                  var10.clipEnvelope.jumpTo(Math.round((double)this.durations[this.curIndex] * this.rates[this.curIndex]));
               }

               if (!this.startChild(var10, this.curIndex)) {
                  if (var13) {
                     var14 = var10.getOnFinished();
                     if (var14 != null) {
                        var14.handle(new ActionEvent(this, (EventTarget)null));
                     }
                  }

                  this.oldTicks = var7;
                  return;
               }
            }

            if (var7 <= var11) {
               var10.impl_timePulse(TickCalculation.sub(this.durations[this.curIndex], this.offsetTicks));
               if (var7 == 0L) {
                  this.curIndex = -1;
               }
            } else {
               var17 = TickCalculation.sub(this.startTimes[this.curIndex + 1] - var7, this.offsetTicks);
               var10.impl_timePulse(var17);
            }
         }
      } else {
         EventHandler var12;
         Animation var15;
         long var16;
         if (this.curIndex < var9) {
            if (var10 != null) {
               var11 = TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex]);
               if (this.oldTicks <= var11 || var10.getStatus() == Animation.Status.STOPPED && this.oldTicks != this.startTimes[this.curIndex + 1]) {
                  var13 = this.oldTicks <= var11;
                  if (var13) {
                     var10.clipEnvelope.jumpTo(0L);
                  }

                  if (!this.startChild(var10, this.curIndex) && var13) {
                     var14 = var10.getOnFinished();
                     if (var14 != null) {
                        var14.handle(new ActionEvent(this, (EventTarget)null));
                     }
                  }
               }

               if (var10.getStatus() == Animation.Status.RUNNING) {
                  var10.impl_timePulse(TickCalculation.sub(this.durations[this.curIndex], this.offsetTicks));
               }

               this.oldTicks = this.startTimes[this.curIndex + 1];
            }

            this.offsetTicks = 0L;
            ++this.curIndex;

            while(this.curIndex < var9) {
               var15 = this.cachedChildren[this.curIndex];
               var15.clipEnvelope.jumpTo(0L);
               if (this.startChild(var15, this.curIndex)) {
                  var15.impl_timePulse(this.durations[this.curIndex]);
               } else {
                  var12 = var15.getOnFinished();
                  if (var12 != null) {
                     var12.handle(new ActionEvent(this, (EventTarget)null));
                  }
               }

               this.oldTicks = this.startTimes[this.curIndex + 1];
               ++this.curIndex;
            }

            var15 = this.cachedChildren[this.curIndex];
            var15.clipEnvelope.jumpTo(0L);
            if (this.startChild(var15, this.curIndex)) {
               if (var7 >= this.startTimes[this.curIndex + 1]) {
                  var15.impl_timePulse(this.durations[this.curIndex]);
                  if (var7 == var3) {
                     this.curIndex = this.end;
                  }
               } else {
                  var16 = TickCalculation.sub(var7, TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex]));
                  var15.impl_timePulse(var16);
               }
            } else {
               var12 = var15.getOnFinished();
               if (var12 != null) {
                  var12.handle(new ActionEvent(this, (EventTarget)null));
               }
            }
         } else {
            if (var10 != null) {
               var11 = TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex]);
               if (this.oldTicks >= this.startTimes[this.curIndex + 1] || this.oldTicks > var11 && var10.getStatus() == Animation.Status.STOPPED) {
                  var13 = this.oldTicks >= this.startTimes[this.curIndex + 1];
                  if (var13) {
                     var10.clipEnvelope.jumpTo(Math.round((double)this.durations[this.curIndex] * this.rates[this.curIndex]));
                  }

                  if (!this.startChild(var10, this.curIndex) && var13) {
                     var14 = var10.getOnFinished();
                     if (var14 != null) {
                        var14.handle(new ActionEvent(this, (EventTarget)null));
                     }
                  }
               }

               if (var10.getStatus() == Animation.Status.RUNNING) {
                  var10.impl_timePulse(TickCalculation.sub(this.durations[this.curIndex], this.offsetTicks));
               }

               this.oldTicks = this.startTimes[this.curIndex];
            }

            this.offsetTicks = 0L;
            --this.curIndex;

            while(this.curIndex > var9) {
               var15 = this.cachedChildren[this.curIndex];
               var15.clipEnvelope.jumpTo(Math.round((double)this.durations[this.curIndex] * this.rates[this.curIndex]));
               if (this.startChild(var15, this.curIndex)) {
                  var15.impl_timePulse(this.durations[this.curIndex]);
               } else {
                  var12 = var15.getOnFinished();
                  if (var12 != null) {
                     var12.handle(new ActionEvent(this, (EventTarget)null));
                  }
               }

               this.oldTicks = this.startTimes[this.curIndex];
               --this.curIndex;
            }

            var15 = this.cachedChildren[this.curIndex];
            var15.clipEnvelope.jumpTo(Math.round((double)this.durations[this.curIndex] * this.rates[this.curIndex]));
            if (this.startChild(var15, this.curIndex)) {
               if (var7 <= TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex])) {
                  var15.impl_timePulse(this.durations[this.curIndex]);
                  if (var7 == 0L) {
                     this.curIndex = -1;
                  }
               } else {
                  var16 = TickCalculation.sub(this.startTimes[this.curIndex + 1], var7);
                  var15.impl_timePulse(var16);
               }
            } else {
               var12 = var15.getOnFinished();
               if (var12 != null) {
                  var12.handle(new ActionEvent(this, (EventTarget)null));
               }
            }
         }
      }

      this.oldTicks = var7;
   }

   void impl_jumpTo(long var1, long var3, boolean var5) {
      this.impl_setCurrentTicks(var1);
      Animation.Status var6 = this.getStatus();
      if (var6 != Animation.Status.STOPPED || var5) {
         this.impl_sync(false);
         double var7 = this.calculateFraction(var1, var3);
         long var9 = Math.max(0L, Math.min(this.getCachedInterpolator().interpolate(0L, var3, var7), var3));
         int var11 = this.curIndex;
         this.curIndex = this.findNewIndex(var9);
         Animation var12 = this.cachedChildren[this.curIndex];
         double var13 = this.getCurrentRate();
         long var15 = TickCalculation.add(this.startTimes[this.curIndex], this.delays[this.curIndex]);
         if (this.curIndex != var11 && var6 != Animation.Status.STOPPED) {
            if (var11 != -1 && var11 != this.end) {
               Animation var17 = this.cachedChildren[var11];
               if (var17.getStatus() != Animation.Status.STOPPED) {
                  this.cachedChildren[var11].impl_stop();
               }
            }

            int var18;
            if (this.curIndex < var11) {
               for(var18 = var11 == this.end ? this.end - 1 : var11; var18 > this.curIndex; --var18) {
                  this.cachedChildren[var18].impl_jumpTo(0L, this.durations[var18], true);
               }
            } else {
               for(var18 = var11 == -1 ? 0 : var11; var18 < this.curIndex; ++var18) {
                  this.cachedChildren[var18].impl_jumpTo(this.durations[var18], this.durations[var18], true);
               }
            }

            if (var9 >= var15) {
               this.startChild(var12, this.curIndex);
               if (var6 == Animation.Status.PAUSED) {
                  var12.impl_pause();
               }
            }
         }

         if (var11 == this.curIndex) {
            if (var13 == 0.0) {
               this.offsetTicks = (long)((double)this.offsetTicks + (double)(var9 - this.oldTicks) * Math.signum(this.clipEnvelope.getCurrentRate()));
            } else {
               this.offsetTicks += var13 > 0.0 ? var9 - this.oldTicks : this.oldTicks - var9;
            }
         } else if (var13 == 0.0) {
            if (this.clipEnvelope.getCurrentRate() > 0.0) {
               this.offsetTicks = Math.max(0L, var9 - var15);
            } else {
               this.offsetTicks = this.startTimes[this.curIndex] + this.durations[this.curIndex] - var9;
            }
         } else {
            this.offsetTicks = var13 > 0.0 ? Math.max(0L, var9 - var15) : this.startTimes[this.curIndex + 1] - var9;
         }

         var12.clipEnvelope.jumpTo(Math.round((double)TickCalculation.sub(var9, var15) * this.rates[this.curIndex]));
         this.oldTicks = var9;
      }
   }

   private void jumpToEnd() {
      for(int var1 = 0; var1 < this.end; ++var1) {
         if (this.forceChildSync[var1]) {
            this.cachedChildren[var1].impl_sync(true);
         }

         this.cachedChildren[var1].impl_jumpTo(this.durations[var1], this.durations[var1], true);
      }

   }

   private void jumpToBefore() {
      for(int var1 = this.end - 1; var1 >= 0; --var1) {
         if (this.forceChildSync[var1]) {
            this.cachedChildren[var1].impl_sync(true);
         }

         this.cachedChildren[var1].impl_jumpTo(0L, this.durations[var1], true);
      }

   }

   protected void interpolate(double var1) {
   }
}
