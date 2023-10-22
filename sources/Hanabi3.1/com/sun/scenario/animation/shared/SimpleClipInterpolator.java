package com.sun.scenario.animation.shared;

import java.util.HashMap;
import java.util.Iterator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.value.WritableValue;
import javafx.util.Duration;

class SimpleClipInterpolator extends ClipInterpolator {
   private static final KeyFrame ZERO_FRAME;
   private KeyFrame startKeyFrame;
   private KeyFrame endKeyFrame;
   private long endTicks;
   private InterpolationInterval[] interval;
   private int undefinedStartValueCount;
   private long ticks;
   private boolean invalid = true;

   SimpleClipInterpolator(KeyFrame var1, KeyFrame var2, long var3) {
      this.startKeyFrame = var1;
      this.endKeyFrame = var2;
      this.endTicks = var3;
   }

   SimpleClipInterpolator(KeyFrame var1, long var2) {
      this.startKeyFrame = ZERO_FRAME;
      this.endKeyFrame = var1;
      this.endTicks = var2;
   }

   ClipInterpolator setKeyFrames(KeyFrame[] var1, long[] var2) {
      if (ClipInterpolator.getRealKeyFrameCount(var1) != 2) {
         return ClipInterpolator.create(var1, var2);
      } else {
         if (var1.length == 1) {
            this.startKeyFrame = ZERO_FRAME;
            this.endKeyFrame = var1[0];
            this.endTicks = var2[0];
         } else {
            this.startKeyFrame = var1[0];
            this.endKeyFrame = var1[1];
            this.endTicks = var2[1];
         }

         this.invalid = true;
         return this;
      }
   }

   void validate(boolean var1) {
      int var10;
      if (this.invalid) {
         this.ticks = this.endTicks;
         HashMap var2 = new HashMap();
         Iterator var3 = this.endKeyFrame.getValues().iterator();

         while(var3.hasNext()) {
            KeyValue var4 = (KeyValue)var3.next();
            var2.put(var4.getTarget(), var4);
         }

         var10 = var2.size();
         this.interval = new InterpolationInterval[var10];
         int var11 = 0;
         Iterator var5 = this.startKeyFrame.getValues().iterator();

         KeyValue var6;
         while(var5.hasNext()) {
            var6 = (KeyValue)var5.next();
            WritableValue var7 = var6.getTarget();
            KeyValue var8 = (KeyValue)var2.get(var7);
            if (var8 != null) {
               this.interval[var11++] = InterpolationInterval.create(var8, this.ticks, var6, this.ticks);
               var2.remove(var7);
            }
         }

         this.undefinedStartValueCount = var2.values().size();

         for(var5 = var2.values().iterator(); var5.hasNext(); this.interval[var11++] = InterpolationInterval.create(var6, this.ticks)) {
            var6 = (KeyValue)var5.next();
         }

         this.invalid = false;
      } else if (var1) {
         int var9 = this.interval.length;

         for(var10 = var9 - this.undefinedStartValueCount; var10 < var9; ++var10) {
            this.interval[var10].recalculateStartValue();
         }
      }

   }

   void interpolate(long var1) {
      double var3 = (double)var1 / (double)this.ticks;
      int var5 = this.interval.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         this.interval[var6].interpolate(var3);
      }

   }

   static {
      ZERO_FRAME = new KeyFrame(Duration.ZERO, new KeyValue[0]);
   }
}
