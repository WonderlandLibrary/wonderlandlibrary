package com.sun.glass.ui;

public final class GestureSupport {
   private static final double THRESHOLD_SCROLL = 1.0;
   private static final double THRESHOLD_SCALE = 0.01;
   private static final double THRESHOLD_EXPANSION = 0.01;
   private static final double THRESHOLD_ROTATE = Math.toDegrees(0.017453292519943295);
   private final GestureState scrolling = new GestureState();
   private final GestureState rotating = new GestureState();
   private final GestureState zooming = new GestureState();
   private final GestureState swiping = new GestureState();
   private double totalScrollX = Double.NaN;
   private double totalScrollY = Double.NaN;
   private double totalScale = 1.0;
   private double totalExpansion = Double.NaN;
   private double totalRotation = 0.0;
   private double multiplierX = 1.0;
   private double multiplierY = 1.0;
   private boolean zoomWithExpansion;

   public GestureSupport(boolean var1) {
      this.zoomWithExpansion = var1;
   }

   private static double multiplicativeDelta(double var0, double var2) {
      return var0 == 0.0 ? Double.NaN : var2 / var0;
   }

   private int setScrolling(boolean var1) {
      return this.scrolling.updateProgress(var1);
   }

   private int setRotating(boolean var1) {
      return this.rotating.updateProgress(var1);
   }

   private int setZooming(boolean var1) {
      return this.zooming.updateProgress(var1);
   }

   private int setSwiping(boolean var1) {
      return this.swiping.updateProgress(var1);
   }

   public boolean isScrolling() {
      return !this.scrolling.isIdle();
   }

   public boolean isRotating() {
      return !this.rotating.isIdle();
   }

   public boolean isZooming() {
      return !this.zooming.isIdle();
   }

   public boolean isSwiping() {
      return !this.swiping.isIdle();
   }

   public void handleScrollingEnd(View var1, int var2, int var3, boolean var4, boolean var5, int var6, int var7, int var8, int var9) {
      this.scrolling.setIdle();
      if (!var5) {
         var1.notifyScrollGestureEvent(3, var2, var4, var5, var3, var6, var7, var8, var9, 0.0, 0.0, this.totalScrollX, this.totalScrollY, this.multiplierX, this.multiplierY);
      }
   }

   public void handleRotationEnd(View var1, int var2, boolean var3, boolean var4, int var5, int var6, int var7, int var8) {
      this.rotating.setIdle();
      if (!var4) {
         var1.notifyRotateGestureEvent(3, var2, var3, var4, var5, var6, var7, var8, 0.0, this.totalRotation);
      }
   }

   public void handleZoomingEnd(View var1, int var2, boolean var3, boolean var4, int var5, int var6, int var7, int var8) {
      this.zooming.setIdle();
      if (!var4) {
         var1.notifyZoomGestureEvent(3, var2, var3, var4, var5, var6, var7, var8, Double.NaN, 0.0, this.totalScale, this.totalExpansion);
      }
   }

   public void handleSwipeEnd(View var1, int var2, boolean var3, boolean var4, int var5, int var6, int var7, int var8) {
      this.swiping.setIdle();
      if (!var4) {
         var1.notifySwipeGestureEvent(3, var2, var3, var4, Integer.MAX_VALUE, Integer.MAX_VALUE, var5, var6, var7, var8);
      }
   }

   public void handleTotalZooming(View var1, int var2, boolean var3, boolean var4, int var5, int var6, int var7, int var8, double var9, double var11) {
      double var13 = this.totalScale;
      double var15 = this.totalExpansion;
      if (this.zooming.doesGestureStart(var4)) {
         var13 = 1.0;
         var15 = 0.0;
      }

      if (!(Math.abs(var9 - var13) < 0.01) || this.zoomWithExpansion && !(Math.abs(var11 - var15) < 0.01)) {
         double var17 = Double.NaN;
         if (this.zoomWithExpansion) {
            var17 = var11 - var15;
         } else {
            var11 = Double.NaN;
         }

         this.totalScale = var9;
         this.totalExpansion = var11;
         int var19 = this.setZooming(var4);
         var1.notifyZoomGestureEvent(var19, var2, var3, var4, var5, var6, var7, var8, multiplicativeDelta(var13, this.totalScale), var17, var9, var11);
      }
   }

   public void handleTotalRotation(View var1, int var2, boolean var3, boolean var4, int var5, int var6, int var7, int var8, double var9) {
      double var11 = this.totalRotation;
      if (this.rotating.doesGestureStart(var4)) {
         var11 = 0.0;
      }

      if (!(Math.abs(var9 - var11) < THRESHOLD_ROTATE)) {
         this.totalRotation = var9;
         int var13 = this.setRotating(var4);
         var1.notifyRotateGestureEvent(var13, var2, var3, var4, var5, var6, var7, var8, var9 - var11, var9);
      }
   }

   public void handleTotalScrolling(View var1, int var2, boolean var3, boolean var4, int var5, int var6, int var7, int var8, int var9, double var10, double var12, double var14, double var16) {
      this.multiplierX = var14;
      this.multiplierY = var16;
      double var18 = this.totalScrollX;
      double var20 = this.totalScrollY;
      if (this.scrolling.doesGestureStart(var4)) {
         var18 = 0.0;
         var20 = 0.0;
      }

      if (!(Math.abs(var10 - this.totalScrollX) < 1.0) || !(Math.abs(var12 - this.totalScrollY) < 1.0)) {
         this.totalScrollX = var10;
         this.totalScrollY = var12;
         int var22 = this.setScrolling(var4);
         var1.notifyScrollGestureEvent(var22, var2, var3, var4, var5, var6, var7, var8, var9, var10 - var18, var12 - var20, var10, var12, var14, var16);
      }
   }

   public void handleDeltaZooming(View var1, int var2, boolean var3, boolean var4, int var5, int var6, int var7, int var8, double var9, double var11) {
      double var13 = this.totalScale;
      double var15 = this.totalExpansion;
      if (this.zooming.doesGestureStart(var4)) {
         var13 = 1.0;
         var15 = 0.0;
      }

      this.totalScale = var13 * (1.0 + var9);
      if (this.zoomWithExpansion) {
         this.totalExpansion = var15 + var11;
      } else {
         this.totalExpansion = Double.NaN;
      }

      int var17 = this.setZooming(var4);
      var1.notifyZoomGestureEvent(var17, var2, var3, var4, var5, var6, var7, var8, multiplicativeDelta(var13, this.totalScale), var11, this.totalScale, this.totalExpansion);
   }

   public void handleDeltaRotation(View var1, int var2, boolean var3, boolean var4, int var5, int var6, int var7, int var8, double var9) {
      double var11 = this.totalRotation;
      if (this.rotating.doesGestureStart(var4)) {
         var11 = 0.0;
      }

      this.totalRotation = var11 + var9;
      int var13 = this.setRotating(var4);
      var1.notifyRotateGestureEvent(var13, var2, var3, var4, var5, var6, var7, var8, var9, this.totalRotation);
   }

   public void handleDeltaScrolling(View var1, int var2, boolean var3, boolean var4, int var5, int var6, int var7, int var8, int var9, double var10, double var12, double var14, double var16) {
      this.multiplierX = var14;
      this.multiplierY = var16;
      double var18 = this.totalScrollX;
      double var20 = this.totalScrollY;
      if (this.scrolling.doesGestureStart(var4)) {
         var18 = 0.0;
         var20 = 0.0;
      }

      this.totalScrollX = var18 + var10;
      this.totalScrollY = var20 + var12;
      int var22 = this.setScrolling(var4);
      var1.notifyScrollGestureEvent(var22, var2, var3, var4, var5, var6, var7, var8, var9, var10, var12, this.totalScrollX, this.totalScrollY, var14, var16);
   }

   public void handleSwipe(View var1, int var2, boolean var3, boolean var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      int var11 = this.setSwiping(var4);
      var1.notifySwipeGestureEvent(var11, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   public static void handleSwipePerformed(View var0, int var1, boolean var2, boolean var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      var0.notifySwipeGestureEvent(2, var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   public static void handleScrollingPerformed(View var0, int var1, boolean var2, boolean var3, int var4, int var5, int var6, int var7, int var8, double var9, double var11, double var13, double var15) {
      var0.notifyScrollGestureEvent(2, var1, var2, var3, var4, var5, var6, var7, var8, var9, var11, var9, var11, var13, var15);
   }

   public TouchInputSupport.TouchCountListener createTouchCountListener() {
      Application.checkEventThread();
      return (var1, var2, var3, var4) -> {
         boolean var5 = false;
         if (this.isScrolling()) {
            this.handleScrollingEnd(var2, var3, var1.getTouchCount(), var4, false, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
         }

         if (this.isRotating()) {
            this.handleRotationEnd(var2, var3, var4, false, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
         }

         if (this.isZooming()) {
            this.handleZoomingEnd(var2, var3, var4, false, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
         }

      };
   }

   private static class GestureState {
      private StateId id;

      private GestureState() {
         this.id = GestureSupport.GestureState.StateId.Idle;
      }

      void setIdle() {
         this.id = GestureSupport.GestureState.StateId.Idle;
      }

      boolean isIdle() {
         return this.id == GestureSupport.GestureState.StateId.Idle;
      }

      int updateProgress(boolean var1) {
         byte var2 = 2;
         if (this.doesGestureStart(var1) && !var1) {
            var2 = 1;
         }

         this.id = var1 ? GestureSupport.GestureState.StateId.Inertia : GestureSupport.GestureState.StateId.Running;
         return var2;
      }

      boolean doesGestureStart(boolean var1) {
         switch (this.id) {
            case Running:
               return var1;
            case Inertia:
               return !var1;
            default:
               return true;
         }
      }

      // $FF: synthetic method
      GestureState(Object var1) {
         this();
      }

      static enum StateId {
         Idle,
         Running,
         Inertia;
      }
   }
}
