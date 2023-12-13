package cn.hanabi.utils.animation;

public class SmoothAnimation {
   private double value;
   private Animation animation = null;
   private int duration;

   public SmoothAnimation(double initValue, int duration) {
      this.value = initValue;
      this.duration = duration;
   }

   public double get() {
      if (this.animation != null) {
         this.value = this.animation.getNow();
         if (this.animation.getState() == Animation.EnumAnimationState.FINISHED) {
            this.animation = null;
         }
      }

      return this.value;
   }

   public void set(double valueIn) {
      if (this.animation == null || this.animation.getTo() != valueIn) {
         this.animation = new Animation(this.value, valueIn, this.duration);
         this.animation.start();
      }

   }
}
