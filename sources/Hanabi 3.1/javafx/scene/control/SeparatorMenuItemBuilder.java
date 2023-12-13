package javafx.scene.control;

/** @deprecated */
@Deprecated
public class SeparatorMenuItemBuilder extends CustomMenuItemBuilder {
   protected SeparatorMenuItemBuilder() {
   }

   public static SeparatorMenuItemBuilder create() {
      return new SeparatorMenuItemBuilder();
   }

   public SeparatorMenuItem build() {
      SeparatorMenuItem var1 = new SeparatorMenuItem();
      this.applyTo(var1);
      return var1;
   }
}
