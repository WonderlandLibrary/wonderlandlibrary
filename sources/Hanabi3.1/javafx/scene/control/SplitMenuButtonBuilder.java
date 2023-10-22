package javafx.scene.control;

/** @deprecated */
@Deprecated
public class SplitMenuButtonBuilder extends MenuButtonBuilder {
   protected SplitMenuButtonBuilder() {
   }

   public static SplitMenuButtonBuilder create() {
      return new SplitMenuButtonBuilder();
   }

   public SplitMenuButton build() {
      SplitMenuButton var1 = new SplitMenuButton();
      this.applyTo(var1);
      return var1;
   }
}
