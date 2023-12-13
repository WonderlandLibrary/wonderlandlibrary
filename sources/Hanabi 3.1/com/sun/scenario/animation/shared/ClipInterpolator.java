package com.sun.scenario.animation.shared;

import javafx.animation.KeyFrame;
import javafx.util.Duration;

public abstract class ClipInterpolator {
   static ClipInterpolator create(KeyFrame[] var0, long[] var1) {
      return (ClipInterpolator)(getRealKeyFrameCount(var0) == 2 ? (var0.length == 1 ? new SimpleClipInterpolator(var0[0], var1[0]) : new SimpleClipInterpolator(var0[0], var0[1], var1[1])) : new GeneralClipInterpolator(var0, var1));
   }

   static int getRealKeyFrameCount(KeyFrame[] var0) {
      int var1 = var0.length;
      return var1 == 0 ? 0 : (var0[0].getTime().greaterThan(Duration.ZERO) ? var1 + 1 : var1);
   }

   abstract ClipInterpolator setKeyFrames(KeyFrame[] var1, long[] var2);

   abstract void interpolate(long var1);

   abstract void validate(boolean var1);
}
