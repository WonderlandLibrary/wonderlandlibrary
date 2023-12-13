package javafx.scene.control.cell;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class CheckBoxTreeCell extends DefaultTreeCell {
   private final CheckBox checkBox;
   private ObservableValue booleanProperty;
   private BooleanProperty indeterminateProperty;
   private ObjectProperty converter;
   private ObjectProperty selectedStateCallback;

   public static Callback forTreeView() {
      Callback var0 = (var0x) -> {
         return var0x instanceof CheckBoxTreeItem ? ((CheckBoxTreeItem)var0x).selectedProperty() : null;
      };
      return forTreeView(var0, CellUtils.defaultTreeItemStringConverter());
   }

   public static Callback forTreeView(Callback var0) {
      return forTreeView(var0, CellUtils.defaultTreeItemStringConverter());
   }

   public static Callback forTreeView(Callback var0, StringConverter var1) {
      return (var2) -> {
         return new CheckBoxTreeCell(var0, var1);
      };
   }

   public CheckBoxTreeCell() {
      this((var0) -> {
         return var0 instanceof CheckBoxTreeItem ? ((CheckBoxTreeItem)var0).selectedProperty() : null;
      });
   }

   public CheckBoxTreeCell(Callback var1) {
      this(var1, CellUtils.defaultTreeItemStringConverter(), (Callback)null);
   }

   public CheckBoxTreeCell(Callback var1, StringConverter var2) {
      this(var1, var2, (Callback)null);
   }

   private CheckBoxTreeCell(Callback var1, StringConverter var2, Callback var3) {
      this.converter = new SimpleObjectProperty(this, "converter");
      this.selectedStateCallback = new SimpleObjectProperty(this, "selectedStateCallback");
      this.getStyleClass().add("check-box-tree-cell");
      this.setSelectedStateCallback(var1);
      this.setConverter(var2);
      this.checkBox = new CheckBox();
      this.checkBox.setAllowIndeterminate(false);
      this.setGraphic((Node)null);
   }

   public final ObjectProperty converterProperty() {
      return this.converter;
   }

   public final void setConverter(StringConverter var1) {
      this.converterProperty().set(var1);
   }

   public final StringConverter getConverter() {
      return (StringConverter)this.converterProperty().get();
   }

   public final ObjectProperty selectedStateCallbackProperty() {
      return this.selectedStateCallback;
   }

   public final void setSelectedStateCallback(Callback var1) {
      this.selectedStateCallbackProperty().set(var1);
   }

   public final Callback getSelectedStateCallback() {
      return (Callback)this.selectedStateCallbackProperty().get();
   }

   public void updateItem(Object var1, boolean var2) {
      super.updateItem(var1, var2);
      if (var2) {
         this.setText((String)null);
         this.setGraphic((Node)null);
      } else {
         StringConverter var3 = this.getConverter();
         TreeItem var4 = this.getTreeItem();
         this.setText(var3 != null ? var3.toString(var4) : (var4 == null ? "" : var4.toString()));
         this.checkBox.setGraphic(var4 == null ? null : var4.getGraphic());
         this.setGraphic(this.checkBox);
         if (this.booleanProperty != null) {
            this.checkBox.selectedProperty().unbindBidirectional((BooleanProperty)this.booleanProperty);
         }

         if (this.indeterminateProperty != null) {
            this.checkBox.indeterminateProperty().unbindBidirectional(this.indeterminateProperty);
         }

         if (var4 instanceof CheckBoxTreeItem) {
            CheckBoxTreeItem var5 = (CheckBoxTreeItem)var4;
            this.booleanProperty = var5.selectedProperty();
            this.checkBox.selectedProperty().bindBidirectional((BooleanProperty)this.booleanProperty);
            this.indeterminateProperty = var5.indeterminateProperty();
            this.checkBox.indeterminateProperty().bindBidirectional(this.indeterminateProperty);
         } else {
            Callback var6 = this.getSelectedStateCallback();
            if (var6 == null) {
               throw new NullPointerException("The CheckBoxTreeCell selectedStateCallbackProperty can not be null");
            }

            this.booleanProperty = (ObservableValue)var6.call(var4);
            if (this.booleanProperty != null) {
               this.checkBox.selectedProperty().bindBidirectional((BooleanProperty)this.booleanProperty);
            }
         }
      }

   }

   void updateDisplay(Object var1, boolean var2) {
   }
}
