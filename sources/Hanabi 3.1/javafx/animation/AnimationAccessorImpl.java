package javafx.animation;

import com.sun.scenario.animation.shared.AnimationAccessor;

final class AnimationAccessorImpl extends AnimationAccessor {
   public void setCurrentRate(Animation var1, double var2) {
      var1.impl_setCurrentRate(var2);
   }

   public void playTo(Animation var1, long var2, long var4) {
      var1.impl_playTo(var2, var4);
   }

   public void jumpTo(Animation var1, long var2, long var4, boolean var6) {
      var1.impl_jumpTo(var2, var4, var6);
   }

   public void finished(Animation var1) {
      var1.impl_finished();
   }

   public void setCurrentTicks(Animation var1, long var2) {
      var1.impl_setCurrentTicks(var2);
   }
}
