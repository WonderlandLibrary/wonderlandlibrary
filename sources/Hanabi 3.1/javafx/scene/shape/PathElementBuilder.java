package javafx.scene.shape;

/** @deprecated */
@Deprecated
public abstract class PathElementBuilder {
   private boolean __set;
   private boolean absolute;

   protected PathElementBuilder() {
   }

   public void applyTo(PathElement var1) {
      if (this.__set) {
         var1.setAbsolute(this.absolute);
      }

   }

   public PathElementBuilder absolute(boolean var1) {
      this.absolute = var1;
      this.__set = true;
      return this;
   }
}
