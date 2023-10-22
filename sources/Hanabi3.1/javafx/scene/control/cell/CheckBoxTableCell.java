package javafx.scene.control.cell;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class CheckBoxTableCell extends TableCell {
   private final CheckBox checkBox;
   private boolean showLabel;
   private ObservableValue booleanProperty;
   private ObjectProperty converter;
   private ObjectProperty selectedStateCallback;

   public static Callback forTableColumn(TableColumn var0) {
      return forTableColumn((Callback)null, (StringConverter)null);
   }

   public static Callback forTableColumn(Callback var0) {
      return forTableColumn(var0, (StringConverter)null);
   }

   public static Callback forTableColumn(Callback var0, boolean var1) {
      StringConverter var2 = !var1 ? null : CellUtils.defaultStringConverter();
      return forTableColumn(var0, var2);
   }

   public static Callback forTableColumn(Callback var0, StringConverter var1) {
      return (var2) -> {
         return new CheckBoxTableCell(var0, var1);
      };
   }

   public CheckBoxTableCell() {
      this((Callback)null, (StringConverter)null);
   }

   public CheckBoxTableCell(Callback var1) {
      this(var1, (StringConverter)null);
   }

   public CheckBoxTableCell(Callback var1, StringConverter var2) {
      this.converter = new SimpleObjectProperty(this, "converter") {
         protected void invalidated() {
            CheckBoxTableCell.this.updateShowLabel();
         }
      };
      this.selectedStateCallback = new SimpleObjectProperty(this, "selectedStateCallback");
      this.getStyleClass().add("check-box-table-cell");
      this.checkBox = new CheckBox();
      this.setGraphic((Node)null);
      this.setSelectedStateCallback(var1);
      this.setConverter(var2);
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
         if (this.showLabel) {
            this.setText(var3.toString(var1));
         }

         this.setGraphic(this.checkBox);
         if (this.booleanProperty instanceof BooleanProperty) {
            this.checkBox.selectedProperty().unbindBidirectional((BooleanProperty)this.booleanProperty);
         }

         ObservableValue var4 = this.getSelectedProperty();
         if (var4 instanceof BooleanProperty) {
            this.booleanProperty = var4;
            this.checkBox.selectedProperty().bindBidirectional((BooleanProperty)this.booleanProperty);
         }

         this.checkBox.disableProperty().bind(Bindings.not(this.getTableView().editableProperty().and(this.getTableColumn().editableProperty()).and(this.editableProperty())));
      }

   }

   private void updateShowLabel() {
      this.showLabel = this.converter != null;
      this.checkBox.setAlignment(this.showLabel ? Pos.CENTER_LEFT : Pos.CENTER);
   }

   private ObservableValue getSelectedProperty() {
      return this.getSelectedStateCallback() != null ? (ObservableValue)this.getSelectedStateCallback().call(this.getIndex()) : this.getTableColumn().getCellObservableValue(this.getIndex());
   }
}
