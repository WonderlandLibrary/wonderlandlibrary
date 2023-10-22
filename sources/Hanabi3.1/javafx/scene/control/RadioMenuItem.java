package javafx.scene.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;

public class RadioMenuItem extends MenuItem implements Toggle {
   private ObjectProperty toggleGroup;
   private BooleanProperty selected;
   private static final String DEFAULT_STYLE_CLASS = "radio-menu-item";
   private static final String STYLE_CLASS_SELECTED = "selected";

   public RadioMenuItem() {
      this((String)null, (Node)null);
   }

   public RadioMenuItem(String var1) {
      this(var1, (Node)null);
   }

   public RadioMenuItem(String var1, Node var2) {
      super(var1, var2);
      this.getStyleClass().add("radio-menu-item");
   }

   public final void setToggleGroup(ToggleGroup var1) {
      this.toggleGroupProperty().set(var1);
   }

   public final ToggleGroup getToggleGroup() {
      return this.toggleGroup == null ? null : (ToggleGroup)this.toggleGroup.get();
   }

   public final ObjectProperty toggleGroupProperty() {
      if (this.toggleGroup == null) {
         this.toggleGroup = new ObjectPropertyBase() {
            private ToggleGroup old;

            protected void invalidated() {
               if (this.old != null) {
                  this.old.getToggles().remove(RadioMenuItem.this);
               }

               this.old = (ToggleGroup)this.get();
               if (this.get() != null && !((ToggleGroup)this.get()).getToggles().contains(RadioMenuItem.this)) {
                  ((ToggleGroup)this.get()).getToggles().add(RadioMenuItem.this);
               }

            }

            public Object getBean() {
               return RadioMenuItem.this;
            }

            public String getName() {
               return "toggleGroup";
            }
         };
      }

      return this.toggleGroup;
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
               if (RadioMenuItem.this.getToggleGroup() != null) {
                  if (this.get()) {
                     RadioMenuItem.this.getToggleGroup().selectToggle(RadioMenuItem.this);
                  } else if (RadioMenuItem.this.getToggleGroup().getSelectedToggle() == RadioMenuItem.this) {
                     RadioMenuItem.this.getToggleGroup().clearSelectedToggle();
                  }
               }

               if (RadioMenuItem.this.isSelected()) {
                  RadioMenuItem.this.getStyleClass().add("selected");
               } else {
                  RadioMenuItem.this.getStyleClass().remove("selected");
               }

            }

            public Object getBean() {
               return RadioMenuItem.this;
            }

            public String getName() {
               return "selected";
            }
         };
      }

      return this.selected;
   }
}
