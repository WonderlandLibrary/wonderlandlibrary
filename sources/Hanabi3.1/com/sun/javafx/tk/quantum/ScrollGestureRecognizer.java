package com.sun.javafx.tk.quantum;

import java.security.AccessController;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;

class ScrollGestureRecognizer implements GestureRecognizer {
   private static double SCROLL_THRESHOLD = 10.0;
   private static boolean SCROLL_INERTIA_ENABLED = true;
   private static double MAX_INITIAL_VELOCITY = 1000.0;
   private static double SCROLL_INERTIA_MILLIS = 1500.0;
   private ViewScene scene;
   private ScrollRecognitionState state;
   private Timeline inertiaTimeline;
   private DoubleProperty inertiaScrollVelocity;
   private double initialInertiaScrollVelocity;
   private double scrollStartTime;
   private double lastTouchEventTime;
   private Map trackers;
   private int modifiers;
   private boolean direct;
   private int currentTouchCount;
   private int lastTouchCount;
   private boolean touchPointsSetChanged;
   private boolean touchPointsPressed;
   private double centerX;
   private double centerY;
   private double centerAbsX;
   private double centerAbsY;
   private double lastCenterAbsX;
   private double lastCenterAbsY;
   private double deltaX;
   private double deltaY;
   private double totalDeltaX;
   private double totalDeltaY;
   private double factorX;
   private double factorY;
   double inertiaLastTime;

   ScrollGestureRecognizer(ViewScene var1) {
      this.state = ScrollGestureRecognizer.ScrollRecognitionState.IDLE;
      this.inertiaTimeline = new Timeline();
      this.inertiaScrollVelocity = new SimpleDoubleProperty();
      this.initialInertiaScrollVelocity = 0.0;
      this.scrollStartTime = 0.0;
      this.lastTouchEventTime = 0.0;
      this.trackers = new HashMap();
      this.currentTouchCount = 0;
      this.inertiaLastTime = 0.0;
      this.scene = var1;
      this.inertiaScrollVelocity.addListener((var1x) -> {
         double var2 = this.inertiaTimeline.getCurrentTime().toSeconds();
         double var4 = var2 - this.inertiaLastTime;
         this.inertiaLastTime = var2;
         double var6 = var4 * this.inertiaScrollVelocity.get();
         this.deltaX = var6 * this.factorX;
         this.totalDeltaX += this.deltaX;
         this.deltaY = var6 * this.factorY;
         this.totalDeltaY += this.deltaY;
         this.sendScrollEvent(true, this.centerAbsX, this.centerAbsY, this.currentTouchCount);
      });
   }

   public void notifyBeginTouchEvent(long var1, int var3, boolean var4, int var5) {
      this.params(var3, var4);
      this.touchPointsSetChanged = false;
      this.touchPointsPressed = false;
   }

   public void notifyNextTouchEvent(long var1, int var3, long var4, int var6, int var7, int var8, int var9) {
      switch (var3) {
         case 811:
            this.touchPointsSetChanged = true;
            this.touchPointsPressed = true;
            this.touchPressed(var4, var1, var6, var7, var8, var9);
            break;
         case 812:
            this.touchMoved(var4, var1, var6, var7, var8, var9);
            break;
         case 813:
            this.touchPointsSetChanged = true;
            this.touchReleased(var4, var1, var6, var7, var8, var9);
         case 814:
            break;
         default:
            throw new RuntimeException("Error in Scroll gesture recognition: unknown touch state: " + this.state);
      }

   }

   private void calculateCenter() {
      if (this.currentTouchCount <= 0) {
         throw new RuntimeException("Error in Scroll gesture recognition: touch count is zero!");
      } else {
         double var1 = 0.0;
         double var3 = 0.0;
         double var5 = 0.0;
         double var7 = 0.0;

         TouchPointTracker var10;
         for(Iterator var9 = this.trackers.values().iterator(); var9.hasNext(); var7 += var10.getAbsY()) {
            var10 = (TouchPointTracker)var9.next();
            var1 += var10.getX();
            var3 += var10.getY();
            var5 += var10.getAbsX();
         }

         this.centerX = var1 / (double)this.currentTouchCount;
         this.centerY = var3 / (double)this.currentTouchCount;
         this.centerAbsX = var5 / (double)this.currentTouchCount;
         this.centerAbsY = var7 / (double)this.currentTouchCount;
      }
   }

   public void notifyEndTouchEvent(long var1) {
      this.lastTouchEventTime = (double)var1;
      if (this.currentTouchCount != this.trackers.size()) {
         throw new RuntimeException("Error in Scroll gesture recognition: touch count is wrong: " + this.currentTouchCount);
      } else {
         double var3;
         if (this.currentTouchCount < 1) {
            if (this.state == ScrollGestureRecognizer.ScrollRecognitionState.ACTIVE) {
               this.sendScrollFinishedEvent(this.lastCenterAbsX, this.lastCenterAbsY, this.lastTouchCount);
               if (SCROLL_INERTIA_ENABLED) {
                  var3 = ((double)var1 - this.scrollStartTime) / 1000000.0;
                  if (var3 < 300.0) {
                     this.state = ScrollGestureRecognizer.ScrollRecognitionState.INERTIA;
                     this.inertiaLastTime = 0.0;
                     if (this.initialInertiaScrollVelocity > MAX_INITIAL_VELOCITY) {
                        this.initialInertiaScrollVelocity = MAX_INITIAL_VELOCITY;
                     }

                     this.inertiaTimeline.getKeyFrames().setAll((Object[])(new KeyFrame(Duration.millis(0.0), new KeyValue[]{new KeyValue(this.inertiaScrollVelocity, this.initialInertiaScrollVelocity, Interpolator.LINEAR)}), new KeyFrame(Duration.millis(SCROLL_INERTIA_MILLIS * Math.abs(this.initialInertiaScrollVelocity) / MAX_INITIAL_VELOCITY), (var1x) -> {
                        this.reset();
                     }, new KeyValue[]{new KeyValue(this.inertiaScrollVelocity, 0, Interpolator.LINEAR)})));
                     this.inertiaTimeline.playFromStart();
                  } else {
                     this.reset();
                  }
               } else {
                  this.reset();
               }
            }
         } else {
            this.calculateCenter();
            if (this.touchPointsPressed && this.state == ScrollGestureRecognizer.ScrollRecognitionState.INERTIA) {
               this.inertiaTimeline.stop();
               this.reset();
            }

            if (this.touchPointsSetChanged) {
               if (this.state == ScrollGestureRecognizer.ScrollRecognitionState.IDLE) {
                  this.state = ScrollGestureRecognizer.ScrollRecognitionState.TRACKING;
               }

               if (this.state == ScrollGestureRecognizer.ScrollRecognitionState.ACTIVE) {
                  this.sendScrollFinishedEvent(this.lastCenterAbsX, this.lastCenterAbsY, this.lastTouchCount);
                  this.totalDeltaX = 0.0;
                  this.totalDeltaY = 0.0;
                  this.sendScrollStartedEvent(this.centerAbsX, this.centerAbsY, this.currentTouchCount);
               }

               this.lastTouchCount = this.currentTouchCount;
               this.lastCenterAbsX = this.centerAbsX;
               this.lastCenterAbsY = this.centerAbsY;
            } else {
               this.deltaX = this.centerAbsX - this.lastCenterAbsX;
               this.deltaY = this.centerAbsY - this.lastCenterAbsY;
               if (this.state == ScrollGestureRecognizer.ScrollRecognitionState.TRACKING && (Math.abs(this.deltaX) > SCROLL_THRESHOLD || Math.abs(this.deltaY) > SCROLL_THRESHOLD)) {
                  this.state = ScrollGestureRecognizer.ScrollRecognitionState.ACTIVE;
                  this.sendScrollStartedEvent(this.centerAbsX, this.centerAbsY, this.currentTouchCount);
               }

               if (this.state == ScrollGestureRecognizer.ScrollRecognitionState.ACTIVE) {
                  this.totalDeltaX += this.deltaX;
                  this.totalDeltaY += this.deltaY;
                  this.sendScrollEvent(false, this.centerAbsX, this.centerAbsY, this.currentTouchCount);
                  var3 = ((double)var1 - this.scrollStartTime) / 1.0E9;
                  if (var3 > 1.0E-4) {
                     double var5 = Math.sqrt(this.deltaX * this.deltaX + this.deltaY * this.deltaY);
                     this.factorX = this.deltaX / var5;
                     this.factorY = this.deltaY / var5;
                     this.initialInertiaScrollVelocity = var5 / var3;
                     this.scrollStartTime = (double)var1;
                  }

                  this.lastCenterAbsX = this.centerAbsX;
                  this.lastCenterAbsY = this.centerAbsY;
               }
            }
         }

      }
   }

   private void sendScrollStartedEvent(double var1, double var3, int var5) {
      AccessController.doPrivileged(() -> {
         if (this.scene.sceneListener != null) {
            this.scene.sceneListener.scrollEvent(ScrollEvent.SCROLL_STARTED, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, var5, 0, 0, 0, 0, var1, var3, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 16) != 0, this.direct, false);
         }

         return null;
      }, this.scene.getAccessControlContext());
   }

   private void sendScrollEvent(boolean var1, double var2, double var4, int var6) {
      AccessController.doPrivileged(() -> {
         if (this.scene.sceneListener != null) {
            this.scene.sceneListener.scrollEvent(ScrollEvent.SCROLL, this.deltaX, this.deltaY, this.totalDeltaX, this.totalDeltaY, 1.0, 1.0, var6, 0, 0, 0, 0, var2, var4, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 16) != 0, this.direct, var1);
         }

         return null;
      }, this.scene.getAccessControlContext());
   }

   private void sendScrollFinishedEvent(double var1, double var3, int var5) {
      AccessController.doPrivileged(() -> {
         if (this.scene.sceneListener != null) {
            this.scene.sceneListener.scrollEvent(ScrollEvent.SCROLL_FINISHED, 0.0, 0.0, this.totalDeltaX, this.totalDeltaY, 1.0, 1.0, var5, 0, 0, 0, 0, var1, var3, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 16) != 0, this.direct, false);
         }

         return null;
      }, this.scene.getAccessControlContext());
   }

   public void params(int var1, boolean var2) {
      this.modifiers = var1;
      this.direct = var2;
   }

   public void touchPressed(long var1, long var3, int var5, int var6, int var7, int var8) {
      ++this.currentTouchCount;
      TouchPointTracker var9 = new TouchPointTracker();
      var9.update(var3, (double)var5, (double)var6, (double)var7, (double)var8);
      this.trackers.put(var1, var9);
   }

   public void touchReleased(long var1, long var3, int var5, int var6, int var7, int var8) {
      if (this.state != ScrollGestureRecognizer.ScrollRecognitionState.FAILURE) {
         TouchPointTracker var9 = (TouchPointTracker)this.trackers.get(var1);
         if (var9 == null) {
            this.state = ScrollGestureRecognizer.ScrollRecognitionState.FAILURE;
            throw new RuntimeException("Error in Scroll gesture recognition: released unknown touch point");
         }

         this.trackers.remove(var1);
      }

      --this.currentTouchCount;
   }

   public void touchMoved(long var1, long var3, int var5, int var6, int var7, int var8) {
      if (this.state != ScrollGestureRecognizer.ScrollRecognitionState.FAILURE) {
         TouchPointTracker var9 = (TouchPointTracker)this.trackers.get(var1);
         if (var9 == null) {
            this.state = ScrollGestureRecognizer.ScrollRecognitionState.FAILURE;
            throw new RuntimeException("Error in scroll gesture recognition: reported unknown touch point");
         } else {
            var9.update(var3, (double)var5, (double)var6, (double)var7, (double)var8);
         }
      }
   }

   void reset() {
      this.state = ScrollGestureRecognizer.ScrollRecognitionState.IDLE;
      this.totalDeltaX = 0.0;
      this.totalDeltaY = 0.0;
   }

   static {
      AccessController.doPrivileged(() -> {
         String var0 = System.getProperty("com.sun.javafx.gestures.scroll.threshold");
         if (var0 != null) {
            SCROLL_THRESHOLD = Double.valueOf(var0);
         }

         var0 = System.getProperty("com.sun.javafx.gestures.scroll.inertia");
         if (var0 != null) {
            SCROLL_INERTIA_ENABLED = Boolean.valueOf(var0);
         }

         return null;
      });
   }

   private static enum ScrollRecognitionState {
      IDLE,
      TRACKING,
      ACTIVE,
      INERTIA,
      FAILURE;
   }

   private static class TouchPointTracker {
      double x;
      double y;
      double absX;
      double absY;

      private TouchPointTracker() {
      }

      public void update(long var1, double var3, double var5, double var7, double var9) {
         this.x = var3;
         this.y = var5;
         this.absX = var7;
         this.absY = var9;
      }

      public double getX() {
         return this.x;
      }

      public double getY() {
         return this.y;
      }

      public double getAbsX() {
         return this.absX;
      }

      public double getAbsY() {
         return this.absY;
      }

      // $FF: synthetic method
      TouchPointTracker(Object var1) {
         this();
      }
   }
}
