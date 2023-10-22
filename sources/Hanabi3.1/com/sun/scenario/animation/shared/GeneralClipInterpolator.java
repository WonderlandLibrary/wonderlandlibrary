package com.sun.scenario.animation.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.value.WritableValue;

class GeneralClipInterpolator extends ClipInterpolator {
   private KeyFrame[] keyFrames;
   private long[] keyFrameTicks;
   private InterpolationInterval[][] interval = new InterpolationInterval[0][];
   private int[] undefinedStartValues = new int[0];
   private boolean invalid = true;

   GeneralClipInterpolator(KeyFrame[] var1, long[] var2) {
      this.keyFrames = var1;
      this.keyFrameTicks = var2;
   }

   ClipInterpolator setKeyFrames(KeyFrame[] var1, long[] var2) {
      if (ClipInterpolator.getRealKeyFrameCount(var1) == 2) {
         return ClipInterpolator.create(var1, var2);
      } else {
         this.keyFrames = var1;
         this.keyFrameTicks = var2;
         this.invalid = true;
         return this;
      }
   }

   void validate(boolean var1) {
      int var3;
      int var4;
      if (this.invalid) {
         HashMap var2 = new HashMap();
         var3 = this.keyFrames.length;

         for(var4 = 0; var4 < var3; ++var4) {
            KeyFrame var5 = this.keyFrames[var4];
            if (this.keyFrameTicks[var4] != 0L) {
               break;
            }

            Iterator var6 = var5.getValues().iterator();

            while(var6.hasNext()) {
               KeyValue var7 = (KeyValue)var6.next();
               var2.put(var7.getTarget(), var7);
            }
         }

         HashMap var16 = new HashMap();

         Iterator var10;
         HashSet var17;
         for(var17 = new HashSet(); var4 < var3; ++var4) {
            KeyFrame var18 = this.keyFrames[var4];
            long var8 = this.keyFrameTicks[var4];

            KeyValue var11;
            WritableValue var12;
            for(var10 = var18.getValues().iterator(); var10.hasNext(); var2.put(var12, var11)) {
               var11 = (KeyValue)var10.next();
               var12 = var11.getTarget();
               List var13 = (List)var16.get(var12);
               KeyValue var14 = (KeyValue)var2.get(var12);
               if (var13 == null) {
                  ArrayList var23 = new ArrayList();
                  var16.put(var12, var23);
                  if (var14 == null) {
                     var23.add(InterpolationInterval.create(var11, var8));
                     var17.add(var12);
                  } else {
                     var23.add(InterpolationInterval.create(var11, var8, var14, var8));
                  }
               } else {
                  assert var14 != null;

                  var13.add(InterpolationInterval.create(var11, var8, var14, var8 - ((InterpolationInterval)var13.get(var13.size() - 1)).ticks));
               }
            }
         }

         int var19 = var16.size();
         if (this.interval.length != var19) {
            this.interval = new InterpolationInterval[var19][];
         }

         int var20 = var17.size();
         if (this.undefinedStartValues.length != var20) {
            this.undefinedStartValues = new int[var20];
         }

         int var9 = 0;
         var10 = var16.entrySet().iterator();

         for(int var21 = 0; var21 < var19; ++var21) {
            Map.Entry var22 = (Map.Entry)var10.next();
            this.interval[var21] = new InterpolationInterval[((List)var22.getValue()).size()];
            ((List)var22.getValue()).toArray(this.interval[var21]);
            if (var17.contains(var22.getKey())) {
               this.undefinedStartValues[var9++] = var21;
            }
         }

         this.invalid = false;
      } else if (var1) {
         int var15 = this.undefinedStartValues.length;

         for(var3 = 0; var3 < var15; ++var3) {
            var4 = this.undefinedStartValues[var3];
            this.interval[var4][0].recalculateStartValue();
         }
      }

   }

   void interpolate(long var1) {
      int var3 = this.interval.length;

      label24:
      for(int var4 = 0; var4 < var3; ++var4) {
         InterpolationInterval[] var5 = this.interval[var4];
         int var6 = var5.length;
         long var7 = 0L;

         for(int var9 = 0; var9 < var6 - 1; ++var9) {
            InterpolationInterval var10 = var5[var9];
            long var11 = var10.ticks;
            if (var1 <= var11) {
               double var13 = (double)(var1 - var7) / (double)(var11 - var7);
               var10.interpolate(var13);
               continue label24;
            }

            var7 = var11;
         }

         InterpolationInterval var16 = var5[var6 - 1];
         double var15 = Math.min(1.0, (double)(var1 - var7) / (double)(var16.ticks - var7));
         var16.interpolate(var15);
      }

   }
}
