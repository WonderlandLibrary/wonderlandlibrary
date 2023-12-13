package javafx.scene.control;

/** @deprecated */
@Deprecated
public class ProgressBarBuilder extends ProgressIndicatorBuilder {
   protected ProgressBarBuilder() {
   }

   public static ProgressBarBuilder create() {
      return new ProgressBarBuilder();
   }

   public ProgressBar build() {
      ProgressBar var1 = new ProgressBar();
      this.applyTo(var1);
      return var1;
   }
}
