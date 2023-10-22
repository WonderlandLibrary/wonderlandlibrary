package com.sun.scenario.animation.shared;

import javafx.animation.Animation;

public abstract class AnimationAccessor {
   public static AnimationAccessor DEFAULT;

   public static AnimationAccessor getDefault() {
      if (DEFAULT != null) {
         return DEFAULT;
      } else {
         Class var0 = Animation.class;

         try {
            Class.forName(var0.getName());
         } catch (ClassNotFoundException var2) {
            assert false : var2;
         }

         assert DEFAULT != null : "The DEFAULT field must be initialized";

         return DEFAULT;
      }
   }

   public abstract void setCurrentRate(Animation var1, double var2);

   public abstract void setCurrentTicks(Animation var1, long var2);

   public abstract void playTo(Animation var1, long var2, long var4);

   public abstract void jumpTo(Animation var1, long var2, long var4, boolean var6);

   public abstract void finished(Animation var1);
}
