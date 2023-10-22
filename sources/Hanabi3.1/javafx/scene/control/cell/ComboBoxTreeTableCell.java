package javafx.scene.control.cell;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeTableCell;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class ComboBoxTreeTableCell extends TreeTableCell {
   private final ObservableList items;
   private ComboBox comboBox;
   private ObjectProperty converter;
   private BooleanProperty comboBoxEditable;

   @SafeVarargs
   public static Callback forTreeTableColumn(Object... var0) {
      return forTreeTableColumn((StringConverter)null, (Object[])var0);
   }

   @SafeVarargs
   public static Callback forTreeTableColumn(StringConverter var0, Object... var1) {
      return forTreeTableColumn(var0, FXCollections.observableArrayList(var1));
   }

   public static Callback forTreeTableColumn(ObservableList var0) {
      return forTreeTableColumn((StringConverter)null, (ObservableList)var0);
   }

   public static Callback forTreeTableColumn(StringConverter var0, ObservableList var1) {
      return (var2) -> {
         return new ComboBoxTreeTableCell(var0, var1);
      };
   }

   public ComboBoxTreeTableCell() {
      this(FXCollections.observableArrayList());
   }

   @SafeVarargs
   public ComboBoxTreeTableCell(Object... var1) {
      this(FXCollections.observableArrayList(var1));
   }

   @SafeVarargs
   public ComboBoxTreeTableCell(StringConverter var1, Object... var2) {
      this(var1, FXCollections.observableArrayList(var2));
   }

   public ComboBoxTreeTableCell(ObservableList var1) {
      this((StringConverter)null, (ObservableList)var1);
   }

   public ComboBoxTreeTableCell(StringConverter var1, ObservableList var2) {
      this.converter = new SimpleObjectProperty(this, "converter");
      this.comboBoxEditable = new SimpleBooleanProperty(this, "comboBoxEditable");
      this.getStyleClass().add("combo-box-tree-table-cell");
      this.items = var2;
      this.setConverter(var1 != null ? var1 : CellUtils.defaultStringConverter());
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

   public final BooleanProperty comboBoxEditableProperty() {
      return this.comboBoxEditable;
   }

   public final void setComboBoxEditable(boolean var1) {
      this.comboBoxEditableProperty().set(var1);
   }

   public final boolean isComboBoxEditable() {
      return this.comboBoxEditableProperty().get();
   }

   public ObservableList getItems() {
      return this.items;
   }

   public void startEdit() {
      if (this.isEditable() && this.getTreeTableView().isEditable() && this.getTableColumn().isEditable()) {
         if (this.comboBox == null) {
            this.comboBox = CellUtils.createComboBox(this, this.items, this.converterProperty());
            this.comboBox.editableProperty().bind(this.comboBoxEditableProperty());
         }

         this.comboBox.getSelectionModel().select(this.getItem());
         super.startEdit();
         this.setText((String)null);
         this.setGraphic(this.comboBox);
      }
   }

   public void cancelEdit() {
      super.cancelEdit();
      this.setText(this.getConverter().toString(this.getItem()));
      this.setGraphic((Node)null);
   }

   public void updateItem(Object var1, boolean var2) {
      super.updateItem(var1, var2);
      CellUtils.updateItem(this, this.getConverter(), (HBox)null, (Node)null, (ComboBox)this.comboBox);
   }
}
