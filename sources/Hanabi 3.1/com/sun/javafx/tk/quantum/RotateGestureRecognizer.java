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
import javafx.scene.input.RotateEvent;
import javafx.util.Duration;

class RotateGestureRecognizer implements GestureRecognizer {
   private ViewScene scene;
   private static double ROTATATION_THRESHOLD = 5.0;
   private static boolean ROTATION_INERTIA_ENABLED = true;
   private static double MAX_INITIAL_VELOCITY = 500.0;
   private static double ROTATION_INERTIA_MILLIS = 1500.0;
   private RotateRecognitionState state;
   private Timeline inertiaTimeline;
   private DoubleProperty inertiaRotationVelocity;
   private double initialInertiaRotationVelocity;
   private double rotationStartTime;
   private double lastTouchEventTime;
   Map trackers;
   int modifiers;
   boolean direct;
   private int currentTouchCount;
   private boolean touchPointsSetChanged;
   private boolean touchPointsPressed;
   int touchPointsInEvent;
   long touchPointID1;
   long touchPointID2;
   double centerX;
   double centerY;
   double centerAbsX;
   double centerAbsY;
   double currentRotation;
   double angleReference;
   double totalRotation;
   double inertiaLastTime;

   RotateGestureRecognizer(ViewScene var1) {
      this.state = RotateGestureRecognizer.RotateRecognitionState.IDLE;
      this.inertiaTimeline = new Timeline();
      this.inertiaRotationVelocity = new SimpleDoubleProperty();
      this.initialInertiaRotationVelocity = 0.0;
      this.rotationStartTime = 0.0;
      this.lastTouchEventTime = 0.0;
      this.trackers = new HashMap();
      this.currentTouchCount = 0;
      this.touchPointID1 = -1L;
      this.touchPointID2 = -1L;
      this.totalRotation = 0.0;
      this.inertiaLastTime = 0.0;
      this.scene = var1;
      this.inertiaRotationVelocity.addListener((var1x) -> {
         double var2 = this.inertiaTimeline.getCurrentTime().toSeconds();
         double var4 = var2 - this.inertiaLastTime;
         this.inertiaLastTime = var2;
         this.currentRotation = var4 * this.inertiaRotationVelocity.get();
         this.totalRotation += this.currentRotation;
         this.sendRotateEvent(true);
      });
   }

   public void notifyBeginTouchEvent(long var1, int var3, boolean var4, int var5) {
      this.params(var3, var4);
      this.touchPointsSetChanged = false;
      this.touchPointsPressed = false;
      this.touchPointsInEvent = 0;
   }

   public void notifyNextTouchEvent(long var1, int var3, long var4, int var6, int var7, int var8, int var9) {
      ++this.touchPointsInEvent;
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
            throw new RuntimeException("Error in Rotate gesture recognition: unknown touch state: " + this.state);
      }

   }

   private void calculateCenter() {
      if (this.currentTouchCount <= 0) {
         throw new RuntimeException("Error in Rotate gesture recognition: touch count is zero!");
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

   private double getAngle(TouchPointTracker var1, TouchPointTracker var2) {
      double var3 = var2.getAbsX() - var1.getAbsX();
      double var5 = -(var2.getAbsY() - var1.getAbsY());
      double var7 = Math.toDegrees(Math.atan2(var5, var3));
      return var7;
   }

   private double getNormalizedDelta(double var1, double var3) {
      double var5 = -(var3 - var1);
      if (var5 > 180.0) {
         var5 -= 360.0;
      } else if (var5 < -180.0) {
         var5 += 360.0;
      }

      return var5;
   }

   private void assignActiveTouchpoints() {
      boolean var1 = false;
      if (!this.trackers.containsKey(this.touchPointID1)) {
         this.touchPointID1 = -1L;
         var1 = true;
      }

      if (!this.trackers.containsKey(this.touchPointID2)) {
         this.touchPointID2 = -1L;
         var1 = true;
      }

      if (var1) {
         Iterator var2 = this.trackers.keySet().iterator();

         while(var2.hasNext()) {
            Long var3 = (Long)var2.next();
            if (var3 != this.touchPointID1 && var3 != this.touchPointID2) {
               if (this.touchPointID1 == -1L) {
                  this.touchPointID1 = var3;
               } else {
                  if (this.touchPointID2 != -1L) {
                     break;
                  }

                  this.touchPointID2 = var3;
               }
            }
         }
      }

   }

   public void notifyEndTouchEvent(long var1) {
      this.lastTouchEventTime = (double)var1;
      if (this.currentTouchCount != this.trackers.size()) {
         throw new RuntimeException("Error in Rotate gesture recognition: touch count is wrong: " + this.currentTouchCount);
      } else {
         if (this.currentTouchCount == 0) {
            if (this.state == RotateGestureRecognizer.RotateRecognitionState.ACTIVE) {
               this.sendRotateFinishedEvent();
            }

            if (ROTATION_INERTIA_ENABLED && (this.state == RotateGestureRecognizer.RotateRecognitionState.PRE_INERTIA || this.state == RotateGestureRecognizer.RotateRecognitionState.ACTIVE)) {
               double var3 = ((double)var1 - this.rotationStartTime) / 1000000.0;
               if (var3 < 300.0) {
                  this.state = RotateGestureRecognizer.RotateRecognitionState.INERTIA;
                  this.inertiaLastTime = 0.0;
                  if (this.initialInertiaRotationVelocity > MAX_INITIAL_VELOCITY) {
                     this.initialInertiaRotationVelocity = MAX_INITIAL_VELOCITY;
                  } else if (this.initialInertiaRotationVelocity < -MAX_INITIAL_VELOCITY) {
                     this.initialInertiaRotationVelocity = -MAX_INITIAL_VELOCITY;
                  }

                  this.inertiaTimeline.getKeyFrames().setAll((Object[])(new KeyFrame(Duration.millis(0.0), new KeyValue[]{new KeyValue(this.inertiaRotationVelocity, this.initialInertiaRotationVelocity, Interpolator.LINEAR)}), new KeyFrame(Duration.millis(ROTATION_INERTIA_MILLIS * Math.abs(this.initialInertiaRotationVelocity) / MAX_INITIAL_VELOCITY), (var1x) -> {
                     this.reset();
                  }, new KeyValue[]{new KeyValue(this.inertiaRotationVelocity, 0, Interpolator.LINEAR)})));
                  this.inertiaTimeline.playFromStart();
               } else {
                  this.reset();
               }
            }
         } else {
            if (this.touchPointsPressed && this.state == RotateGestureRecognizer.RotateRecognitionState.INERTIA) {
               this.inertiaTimeline.stop();
               this.reset();
            }

            if (this.currentTouchCount == 1) {
               if (this.state == RotateGestureRecognizer.RotateRecognitionState.ACTIVE) {
                  this.sendRotateFinishedEvent();
                  if (ROTATION_INERTIA_ENABLED) {
                     this.state = RotateGestureRecognizer.RotateRecognitionState.PRE_INERTIA;
                  } else {
                     this.reset();
                  }
               }
            } else {
               if (this.state == RotateGestureRecognizer.RotateRecognitionState.IDLE) {
                  this.state = RotateGestureRecognizer.RotateRecognitionState.TRACKING;
                  this.assignActiveTouchpoints();
               }

               this.calculateCenter();
               if (this.touchPointsSetChanged) {
                  this.assignActiveTouchpoints();
               }

               TouchPointTracker var9 = (TouchPointTracker)this.trackers.get(this.touchPointID1);
               TouchPointTracker var4 = (TouchPointTracker)this.trackers.get(this.touchPointID2);
               double var5 = this.getAngle(var9, var4);
               if (this.touchPointsSetChanged) {
                  this.angleReference = var5;
               } else {
                  this.currentRotation = this.getNormalizedDelta(this.angleReference, var5);
                  if (this.state == RotateGestureRecognizer.RotateRecognitionState.TRACKING && Math.abs(this.currentRotation) > ROTATATION_THRESHOLD) {
                     this.state = RotateGestureRecognizer.RotateRecognitionState.ACTIVE;
                     this.sendRotateStartedEvent();
                  }

                  if (this.state == RotateGestureRecognizer.RotateRecognitionState.ACTIVE) {
                     this.totalRotation += this.currentRotation;
                     this.sendRotateEvent(false);
                     this.angleReference = var5;
                     double var7 = ((double)var1 - this.rotationStartTime) / 1.0E9;
                     if (var7 > 1.0E-4) {
                        this.initialInertiaRotationVelocity = this.currentRotation / var7;
                        this.rotationStartTime = (double)var1;
                     }
                  }
               }
            }
         }

      }
   }

   private void sendRotateStartedEvent() {
      AccessController.doPrivileged(() -> {
         if (this.scene.sceneListener != null) {
            this.scene.sceneListener.rotateEvent(RotateEvent.ROTATION_STARTED, 0.0, 0.0, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 16) != 0, this.direct, false);
         }

         return null;
      }, this.scene.getAccessControlContext());
   }

   private void sendRotateEvent(boolean var1) {
      AccessController.doPrivileged(() -> {
         if (this.scene.sceneListener != null) {
            this.scene.sceneListener.rotateEvent(RotateEvent.ROTATE, this.currentRotation, this.totalRotation, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 16) != 0, this.direct, var1);
         }

         return null;
      }, this.scene.getAccessControlContext());
   }

   private void sendRotateFinishedEvent() {
      AccessController.doPrivileged(() -> {
         if (this.scene.sceneListener != null) {
            this.scene.sceneListener.rotateEvent(RotateEvent.ROTATION_FINISHED, 0.0, this.totalRotation, this.centerX, this.centerY, this.centerAbsX, this.centerAbsY, (this.modifiers & 1) != 0, (this.modifiers & 4) != 0, (this.modifiers & 8) != 0, (this.modifiers & 16) != 0, this.direct, false);
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
      if (this.state != RotateGestureRecognizer.RotateRecognitionState.FAILURE) {
         TouchPointTracker var9 = (TouchPointTracker)this.trackers.get(var1);
         if (var9 == null) {
            this.state = RotateGestureRecognizer.RotateRecognitionState.FAILURE;
            throw new RuntimeException("Error in Rotate gesture recognition: released unknown touch point");
         }

         this.trackers.remove(var1);
      }

      --this.currentTouchCount;
   }

   public void touchMoved(long var1, long var3, int var5, int var6, int var7, int var8) {
      if (this.state != RotateGestureRecognizer.RotateRecognitionState.FAILURE) {
         TouchPointTracker var9 = (TouchPointTracker)this.trackers.get(var1);
         if (var9 == null) {
            this.state = RotateGestureRecognizer.RotateRecognitionState.FAILURE;
            throw new RuntimeException("Error in rotate gesture recognition: reported unknown touch point");
         } else {
            var9.update(var3, (double)var5, (double)var6, (double)var7, (double)var8);
         }
      }
   }

   void reset() {
      this.state = RotateGestureRecognizer.RotateRecognitionState.IDLE;
      this.touchPointID1 = -1L;
      this.touchPointID2 = -1L;
      this.currentRotation = 0.0;
      this.totalRotation = 0.0;
   }

   static {
      AccessController.doPrivileged(() -> {
         String var0 = System.getProperty("com.sun.javafx.gestures.rotate.threshold");
         if (var0 != null) {
            ROTATATION_THRESHOLD = Double.valueOf(var0);
         }

         var0 = System.getProperty("com.sun.javafx.gestures.rotate.inertia");
         if (var0 != null) {
            ROTATION_INERTIA_ENABLED = Boolean.valueOf(var0);
         }

         return null;
      });
   }

   private static enum RotateRecognitionState {
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
