package javafx.scene.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.scene.Node;

public class CheckMenuItem extends MenuItem {
   private BooleanProperty selected;
   private static final String DEFAULT_STYLE_CLASS = "check-menu-item";
   private static final String STYLE_CLASS_SELECTED = "selected";

   public CheckMenuItem() {
      this((String)null, (Node)null);
   }

   public CheckMenuItem(String var1) {
      this(var1, (Node)null);
   }

   public CheckMenuItem(String var1, Node var2) {
      super(var1, var2);
      this.getStyleClass().add("check-menu-item");
   }

   public final void setSelected(boolean var1) {
      this.selectedProperty().set(var1);
   }

   public final boolean isSelected() {
      return this.selected == null ? false : this.selected.get();
   }

   public final BooleanProperty selectedProperty() {
      if (this.selected == null) {
         this.selected = new BooleanPropertyBase() {
            protected void invalidated() {
               this.get();
               if (CheckMenuItem.this.isSelected()) {
                  CheckMenuItem.this.getStyleClass().add("selected");
               } else {
                  CheckMenuItem.this.getStyleClass().remove("selected");
               }

            }

            public Object getBean() {
               return CheckMenuItem.this;
            }

            public String getName() {
               return "selected";
            }
         };
      }

      return this.selected;
   }
}
