package javafx.scene.control;

import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class ProgressIndicatorBuilder extends ControlBuilder implements Builder {
   private boolean __set;
   private double progress;

   protected ProgressIndicatorBuilder() {
   }

   public static ProgressIndicatorBuilder create() {
      return new ProgressIndicatorBuilder();
   }

   public void applyTo(ProgressIndicator var1) {
      super.applyTo(var1);
      if (this.__set) {
         var1.setProgress(this.progress);
      }

   }

   public ProgressIndicatorBuilder progress(double var1) {
      this.progress = var1;
      this.__set = true;
      return this;
   }

   public ProgressIndicator build() {
      ProgressIndicator var1 = new ProgressIndicator();
      this.applyTo(var1);
      return var1;
   }
}
