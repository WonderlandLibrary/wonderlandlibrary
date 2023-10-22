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
import javafx.scene.input.ZoomEvent;
import javafx.util.Duration;

class ZoomGestureRecognizer implements GestureRecognizer {
   private static double ZOOM_FACTOR_THRESHOLD = 0.1;
   private static boolean ZOOM_INERTIA_ENABLED = true;
   private static double MAX_ZOOMIN_VELOCITY = 3.0;
   private static double MAX_ZOOMOUT_VELOCITY = 0.3333;
   private static double ZOOM_INERTIA_MILLIS = 500.0;
   private static double MAX_ZOOM_IN_FACTOR = 10.0;
   private static double MAX_ZOOM_OUT_FACTOR = 0.1;
   private ViewScene scene;
   private Timeline inertiaTimeline = new Timeline();
   private DoubleProperty inertiaZoomVelocity = new SimpleDoubleProperty();
   private double initialInertiaZoomVelocity = 0.0;
   private double zoomStartTime = 0.0;
   private double lastTouchEventTime = 0.0;
   private ZoomRecognitionState state;
   private Map trackers;
   private int modifiers;
   private boolean direct;
   private int currentTouchCount;
   private boolean touchPointsSetChanged;
   private boolean touchPointsPressed;
   private double centerX;
   private double centerY;
   private double centerAbsX;
   private double centerAbsY;
   private double currentDistance;
   private double distanceReference;
   private double zoomFactor;
   private double totalZoomFactor;
   double inertiaLastTime;

   ZoomGestureRecognizer(ViewScene var1) {
      this.state = ZoomGestureRecognizer.ZoomRecognitionState.IDLE;
      this.trackers = new HashMap();
      this.currentTouchCount = 0;
      this.zoomFactor = 1.0;
      this.totalZoomFactor = 1.0;
      this.inertiaLastTime = 0.0;
      this.scene = var1;
      this.inertiaZoomVelocity.addListener((var1x) -> {
         double var2 = this.inertiaTimeline.getCurrentTime().toSeconds();
         double var4 = var2 - this.inertiaLastTime;
         this.inertiaLastTime = var2;
         double var6 = this.totalZoomFactor;
         this.totalZoomFactor += var4 * this.inertiaZoomVelocity.get();
         this.zoomFactor = this.totalZoomFactor / var6;
         this.sendZoomEvent(true);
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
            throw new RuntimeException("Error in Zoom gesture recognition: unknown touch state: " + this.state);
      }

   }

   private void calculateCenter() {
      if (this.currentTouchCount <= 0) {
         throw new RuntimeException("Error in Zoom gesture recognition: touch count is zero!");
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

   private double calculateMaxDistance() {
      double var1 = 0.0;
      Iterator var3 = this.trackers.values().iterator();

      while(var3.hasNext()) {
         TouchPointTracker var4 = (TouchPointTracker)var3.next();
         double var5 = var4.getAbsX() - this.centerAbsX;
         double var7 = var4.getAbsY() - this.centerAbsY;
         double var9 = var5 * var5 + var7 * var7;
         if (var9 > var1) {
            var1 = var9;
         }
      }

      return Math.sqrt(var1);
   }

   public void notifyEndTouchEvent(long var1) {
      this.lastTouchEventTime = (double)var1;
      if (this.currentTouchCount != this.trackers.size()) {
         throw new RuntimeException("Error in Zoom gesture recognition: touch count is wrong: " + this.currentTouchCount);
      } else {
         double var3;
         double var5;
         double var7;
         if (this.currentTouchCount == 0) {
            if (this.state == ZoomGestureRecognizer.ZoomRecognitionState.ACTIVE) {
               this.sendZoomFinishedEvent();
            }

            if (!ZOOM_INERTIA_ENABLED || this.state != ZoomGestureRecognizer.ZoomRecognitionState.PRE_INERTIA && this.state != ZoomGestureRecognizer.ZoomRecognitionState.ACTIVE) {
               this.reset();
            } else {
               var3 = ((double)var1 - this.zoomStartTime) / 1000000.0;
               if (this.initialInertiaZoomVelocity != 0.0 && var3 < 200.0) {
                  this.state = ZoomGestureRecognizer.ZoomRecognitionState.INERTIA;
                  this.inertiaLastTime = 0.0;
                  var5 = ZOOM_INERTIA_MILLIS / 1000.0;
                  var7 = this.totalZoomFactor + this.initialInertiaZoomVelocity * var5;
                  if (this.initialInertiaZoomVelocity > 0.0) {
                     if (var7 / this.totalZoomFactor > MAX_ZOOM_IN_FACTOR) {
                        var7 = this.totalZoomFactor * MAX_ZOOM_IN_FACTOR;
                        var5 = (var7 - this.totalZoomFactor) / this.initialInertiaZoomVelocity;
                     }
                  } else if (var7 / this.totalZoomFactor < MAX_ZOOM_OUT_FACTOR) {
                     var7 = this.totalZoomFactor * MAX_ZOOM_OUT_FACTOR;
                     var5 = (var7 - this.totalZoomFactor) / this.initialInertiaZoomVelocity;
                  }

                  this.inertiaTimeline.getKeyFrames().setAll((Object[])(new KeyFrame(Duration.millis(0.0), new KeyValue[]{new KeyValue(this.inertiaZoomVelocity, this.initialInertiaZoomVelocity, Interpolator.LINEAR)}), new KeyFrame(Duration.seconds(var5), (var1x) -> {
                     this.reset();
                  }, new KeyValue[]{new KeyValue(this.inertiaZoomVelocity, 0, Interpolator.LINEAR)})));
                  this.inertiaTimeline.playFromStart();
               } else {
                  this.reset();
               }
            }
         } else {
            if (this.touchPointsPressed && this.state == ZoomGestureRecognizer.ZoomRecognitionState.INERTIA) {
               this.inertiaTimeline.stop();
               this.reset();
            }

            if (this.currentTouchCount == 1) {
               if (this.state == ZoomGestureRecognizer.ZoomRecognitionState.ACTIVE) {
                  this.sendZoomFinishedEvent();
                  if (ZOOM_INERTIA_ENABLED) {
                     this.state = ZoomGestureRecognizer.ZoomRecognitionState.PRE_INERTIA;
                  } else {
                     this.reset();
                  }
               }
            } else {
               if (this.state == ZoomGestureRecognizer.ZoomRecognitionState.IDLE) {
                  this.state = ZoomGestureRecognizer.ZoomRecognitionState.TRACKING;
                  this.zoomStartTime = (double)var1;
               }

               this.calculateCenter();
               var3 = this.calculateMaxDistance();
               if (this.touchPointsSetChanged) {
                  this.distanceReference = var3;
               } else {
                  this.zoomFactor = var3 / this.distanceReference;
                  if (this.state == ZoomGestureRecognizer.ZoomRecognitionState.TRACKING && Math.abs(this.zoomFactor - 1.0) > ZOOM_FACTOR_THRESHOLD) {
                     this.state = ZoomGestureRecognizer.ZoomRecognitionState.ACTIVE;
                     this.sendZoomStartedEvent();
                  }

                  if (this.state == ZoomGestureRecognizer.ZoomRecognitionState.ACTIVE) {
                     var5 = this.totalZoomFactor;
                     this.totalZoomFactor *= this.zoomFactor;
                     this.sendZoomEvent(false);
                     this.distanceReference = var3;
                     var7 = ((double)var1 - this.zoomStartTime) / 1.0E9;
                     if (var7 > 1.0E-4) {
                        this.initialInertiaZoomVelocity = (this.totalZoomFactor - var5) / var7;
                        this.zoomStartTime = (double)var1;
                     } else {
                        this.initialInertiaZoomVelocity = 0.0;
                     }
                  }
               }
            }
         }

      }
   }

   private void sendZoomStartedEvent() {
      AccessController.doPrivileged(() -> {
         if (this.scene.sceneListener != null) {
            this.scene.sceneListener.zoomEvent(ZoomEvent.ZOOM_STARTED, 1.0, 1.0, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 16) != 0, this.direct, false);
         }

         return null;
      }, this.scene.getAccessControlContext());
   }

   private void sendZoomEvent(boolean var1) {
      AccessController.doPrivileged(() -> {
         if (this.scene.sceneListener != null) {
            this.scene.sceneListener.zoomEvent(ZoomEvent.ZOOM, this.zoomFactor, this.totalZoomFactor, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 16) != 0, this.direct, var1);
         }

         return null;
      }, this.scene.getAccessControlContext());
   }

   private void sendZoomFinishedEvent() {
      AccessController.doPrivileged(() -> {
         if (this.scene.sceneListener != null) {
            this.scene.sceneListener.zoomEvent(ZoomEvent.ZOOM_FINISHED, 1.0, this.totalZoomFactor, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 16) != 0, this.direct, false);
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
      if (this.state != ZoomGestureRecognizer.ZoomRecognitionState.FAILURE) {
         TouchPointTracker var9 = (TouchPointTracker)this.trackers.get(var1);
         if (var9 == null) {
            this.state = ZoomGestureRecognizer.ZoomRecognitionState.FAILURE;
            throw new RuntimeException("Error in Zoom gesture recognition: released unknown touch point");
         }

         this.trackers.remove(var1);
      }

      --this.currentTouchCount;
   }

   public void touchMoved(long var1, long var3, int var5, int var6, int var7, int var8) {
      if (this.state != ZoomGestureRecognizer.ZoomRecognitionState.FAILURE) {
         TouchPointTracker var9 = (TouchPointTracker)this.trackers.get(var1);
         if (var9 == null) {
            this.state = ZoomGestureRecognizer.ZoomRecognitionState.FAILURE;
            throw new RuntimeException("Error in zoom gesture recognition: reported unknown touch point");
         } else {
            var9.update(var3, (double)var5, (double)var6, (double)var7, (double)var8);
         }
      }
   }

   void reset() {
      this.state = ZoomGestureRecognizer.ZoomRecognitionState.IDLE;
      this.zoomFactor = 1.0;
      this.totalZoomFactor = 1.0;
   }

   static {
      AccessController.doPrivileged(() -> {
         String var0 = System.getProperty("com.sun.javafx.gestures.zoom.threshold");
         if (var0 != null) {
            ZOOM_FACTOR_THRESHOLD = Double.valueOf(var0);
         }

         var0 = System.getProperty("com.sun.javafx.gestures.zoom.inertia");
         if (var0 != null) {
            ZOOM_INERTIA_ENABLED = Boolean.valueOf(var0);
         }

         return null;
      });
   }

   private static enum ZoomRecognitionState {
      IDLE,
      TRACKING,
      ACTIVE,
      PRE_INERTIA,
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
