package javafx.animation;

import javafx.util.Builder;
import javafx.util.Duration;

/** @deprecated */
@Deprecated
public final class PauseTransitionBuilder extends TransitionBuilder implements Builder {
   private boolean __set;
   private Duration duration;

   protected PauseTransitionBuilder() {
   }

   public static PauseTransitionBuilder create() {
      return new PauseTransitionBuilder();
   }

   public void applyTo(PauseTransition var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.setDuration(this.duration);
      }

   }

   public PauseTransitionBuilder duration(Duration var1) {
      this.duration = var1;
      this.__set = true;
      return this;
   }

   public PauseTransition build() {
      PauseTransition var1 = new PauseTransition();
      this.applyTo(var1);
      return var1;
   }
}
