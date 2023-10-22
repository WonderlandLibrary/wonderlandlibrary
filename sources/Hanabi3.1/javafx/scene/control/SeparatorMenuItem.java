package javafx.scene.control;

import javafx.geometry.Orientation;

public class SeparatorMenuItem extends CustomMenuItem {
   private static final String DEFAULT_STYLE_CLASS = "separator-menu-item";

   public SeparatorMenuItem() {
      super(new Separator(Orientation.HORIZONTAL), false);
      this.getStyleClass().add("separator-menu-item");
   }
}
