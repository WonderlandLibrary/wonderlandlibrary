package javafx.animation;

/** @deprecated */
@Deprecated
public abstract class TransitionBuilder extends AnimationBuilder {
   private boolean __set;
   private Interpolator interpolator;
   private double targetFramerate;

   protected TransitionBuilder() {
   }

   public void applyTo(Transition var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.setInterpolator(this.interpolator);
      }

   }

   public TransitionBuilder interpolator(Interpolator var1) {
      this.interpolator = var1;
      this.__set = true;
      return this;
   }

   public TransitionBuilder targetFramerate(double var1) {
      this.targetFramerate = var1;
      return this;
   }
}
