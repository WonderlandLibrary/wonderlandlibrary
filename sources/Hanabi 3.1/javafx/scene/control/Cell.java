package javafx.scene.control;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.PseudoClass;
import javafx.css.StyleOrigin;
import javafx.css.StyleableProperty;

public class Cell extends Labeled {
   private ObjectProperty item = new SimpleObjectProperty(this, "item");
   private ReadOnlyBooleanWrapper empty = new ReadOnlyBooleanWrapper(true) {
      protected void invalidated() {
         boolean var1 = this.get();
         Cell.this.pseudoClassStateChanged(Cell.PSEUDO_CLASS_EMPTY, var1);
         Cell.this.pseudoClassStateChanged(Cell.PSEUDO_CLASS_FILLED, !var1);
      }

      public Object getBean() {
         return Cell.this;
      }

      public String getName() {
         return "empty";
      }
   };
   private ReadOnlyBooleanWrapper selected = new ReadOnlyBooleanWrapper() {
      protected void invalidated() {
         Cell.this.pseudoClassStateChanged(Cell.PSEUDO_CLASS_SELECTED, this.get());
      }

      public Object getBean() {
         return Cell.this;
      }

      public String getName() {
         return "selected";
      }
   };
   private ReadOnlyBooleanWrapper editing;
   private BooleanProperty editable;
   private static final String DEFAULT_STYLE_CLASS = "cell";
   private static final PseudoClass PSEUDO_CLASS_SELECTED = PseudoClass.getPseudoClass("selected");
   private static final PseudoClass PSEUDO_CLASS_FOCUSED = PseudoClass.getPseudoClass("focused");
   private static final PseudoClass PSEUDO_CLASS_EMPTY = PseudoClass.getPseudoClass("empty");
   private static final PseudoClass PSEUDO_CLASS_FILLED = PseudoClass.getPseudoClass("filled");

   public Cell() {
      this.setText((String)null);
      ((StyleableProperty)this.focusTraversableProperty()).applyStyle((StyleOrigin)null, Boolean.FALSE);
      this.getStyleClass().addAll("cell");
      super.focusedProperty().addListener(new InvalidationListener() {
         public void invalidated(Observable var1) {
            Cell.this.pseudoClassStateChanged(Cell.PSEUDO_CLASS_FOCUSED, Cell.this.isFocused());
            if (!Cell.this.isFocused() && Cell.this.isEditing()) {
               Cell.this.cancelEdit();
            }

         }
      });
      this.pseudoClassStateChanged(PSEUDO_CLASS_EMPTY, true);
   }

   public final ObjectProperty itemProperty() {
      return this.item;
   }

   public final void setItem(Object var1) {
      this.item.set(var1);
   }

   public final Object getItem() {
      return this.item.get();
   }

   public final ReadOnlyBooleanProperty emptyProperty() {
      return this.empty.getReadOnlyProperty();
   }

   private void setEmpty(boolean var1) {
      this.empty.set(var1);
   }

   public final boolean isEmpty() {
      return this.empty.get();
   }

   public final ReadOnlyBooleanProperty selectedProperty() {
      return this.selected.getReadOnlyProperty();
   }

   void setSelected(boolean var1) {
      this.selected.set(var1);
   }

   public final boolean isSelected() {
      return this.selected.get();
   }

   private void setEditing(boolean var1) {
      this.editingPropertyImpl().set(var1);
   }

   public final boolean isEditing() {
      return this.editing == null ? false : this.editing.get();
   }

   public final ReadOnlyBooleanProperty editingProperty() {
      return this.editingPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyBooleanWrapper editingPropertyImpl() {
      if (this.editing == null) {
         this.editing = new ReadOnlyBooleanWrapper(this, "editing");
      }

      return this.editing;
   }

   public final void setEditable(boolean var1) {
      this.editableProperty().set(var1);
   }

   public final boolean isEditable() {
      return this.editable == null ? true : this.editable.get();
   }

   public final BooleanProperty editableProperty() {
      if (this.editable == null) {
         this.editable = new SimpleBooleanProperty(this, "editable", true);
      }

      return this.editable;
   }

   public void startEdit() {
      if (this.isEditable() && !this.isEditing() && !this.isEmpty()) {
         this.setEditing(true);
      }

   }

   public void cancelEdit() {
      if (this.isEditing()) {
         this.setEditing(false);
      }

   }

   public void commitEdit(Object var1) {
      if (this.isEditing()) {
         this.setEditing(false);
      }

   }

   protected void updateItem(Object var1, boolean var2) {
      this.setItem(var1);
      this.setEmpty(var2);
      if (var2 && this.isSelected()) {
         this.updateSelected(false);
      }

   }

   public void updateSelected(boolean var1) {
      if (!var1 || !this.isEmpty()) {
         this.setSelected(var1);
      }
   }

   protected boolean isItemChanged(Object var1, Object var2) {
      return var1 != null ? !var1.equals(var2) : var2 != null;
   }

   /** @deprecated */
   @Deprecated
   protected Boolean impl_cssGetFocusTraversableInitialValue() {
      return Boolean.FALSE;
   }
}
