package javafx.scene.control;

import com.sun.javafx.scene.control.skin.ToggleButtonSkin;
import com.sun.javafx.scene.traversal.ParentTraversalEngine;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;

public class ToggleButton extends ButtonBase implements Toggle {
   private BooleanProperty selected;
   private ObjectProperty toggleGroup;
   private static final String DEFAULT_STYLE_CLASS = "toggle-button";
   private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected");

   public ToggleButton() {
      this.initialize();
   }

   public ToggleButton(String var1) {
      this.setText(var1);
      this.initialize();
   }

   public ToggleButton(String var1, Node var2) {
      this.setText(var1);
      this.setGraphic(var2);
      this.initialize();
   }

   private void initialize() {
      this.getStyleClass().setAll((Object[])("toggle-button"));
      this.setAccessibleRole(AccessibleRole.TOGGLE_BUTTON);
      ((StyleableProperty)this.alignmentProperty()).applyStyle((StyleOrigin)null, Pos.CENTER);
      this.setMnemonicParsing(true);
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
               boolean var1 = this.get();
               ToggleGroup var2 = ToggleButton.this.getToggleGroup();
               ToggleButton.this.pseudoClassStateChanged(ToggleButton.PSEUDO_CLASS_SELECTED, var1);
               ToggleButton.this.notifyAccessibleAttributeChanged(AccessibleAttribute.SELECTED);
               if (var2 != null) {
                  if (var1) {
                     var2.selectToggle(ToggleButton.this);
                  } else if (var2.getSelectedToggle() == ToggleButton.this) {
                     var2.clearSelectedToggle();
                  }
               }

            }

            public Object getBean() {
               return ToggleButton.this;
            }

            public String getName() {
               return "selected";
            }
         };
      }

      return this.selected;
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
            private ChangeListener listener = (var1x, var2, var3) -> {
               ToggleButton.this.getImpl_traversalEngine().setOverriddenFocusTraversability(var3 != null ? ToggleButton.this.isSelected() : null);
            };

            protected void invalidated() {
               ToggleGroup var1 = (ToggleGroup)this.get();
               if (var1 != null && !var1.getToggles().contains(ToggleButton.this)) {
                  if (this.old != null) {
                     this.old.getToggles().remove(ToggleButton.this);
                  }

                  var1.getToggles().add(ToggleButton.this);
                  ParentTraversalEngine var2 = new ParentTraversalEngine(ToggleButton.this);
                  ToggleButton.this.setImpl_traversalEngine(var2);
                  var2.setOverriddenFocusTraversability(var1.getSelectedToggle() != null ? ToggleButton.this.isSelected() : null);
                  var1.selectedToggleProperty().addListener(this.listener);
               } else if (var1 == null) {
                  this.old.selectedToggleProperty().removeListener(this.listener);
                  this.old.getToggles().remove(ToggleButton.this);
                  ToggleButton.this.setImpl_traversalEngine((ParentTraversalEngine)null);
               }

               this.old = var1;
            }

            public Object getBean() {
               return ToggleButton.this;
            }

            public String getName() {
               return "toggleGroup";
            }
         };
      }

      return this.toggleGroup;
   }

   public void fire() {
      if (!this.isDisabled()) {
         this.setSelected(!this.isSelected());
         this.fireEvent(new ActionEvent());
      }

   }

   protected Skin createDefaultSkin() {
      return new ToggleButtonSkin(this);
   }

   /** @deprecated */
   @Deprecated
   protected Pos impl_cssGetAlignmentInitialValue() {
      return Pos.CENTER;
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case SELECTED:
            return this.isSelected();
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }
}
