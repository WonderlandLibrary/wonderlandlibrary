package javafx.scene.control.cell;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class ChoiceBoxTableCell extends TableCell {
   private final ObservableList items;
   private ChoiceBox choiceBox;
   private ObjectProperty converter;

   @SafeVarargs
   public static Callback forTableColumn(Object... var0) {
      return forTableColumn((StringConverter)null, (Object[])var0);
   }

   @SafeVarargs
   public static Callback forTableColumn(StringConverter var0, Object... var1) {
      return forTableColumn(var0, FXCollections.observableArrayList(var1));
   }

   public static Callback forTableColumn(ObservableList var0) {
      return forTableColumn((StringConverter)null, (ObservableList)var0);
   }

   public static Callback forTableColumn(StringConverter var0, ObservableList var1) {
      return (var2) -> {
         return new ChoiceBoxTableCell(var0, var1);
      };
   }

   public ChoiceBoxTableCell() {
      this(FXCollections.observableArrayList());
   }

   @SafeVarargs
   public ChoiceBoxTableCell(Object... var1) {
      this(FXCollections.observableArrayList(var1));
   }

   @SafeVarargs
   public ChoiceBoxTableCell(StringConverter var1, Object... var2) {
      this(var1, FXCollections.observableArrayList(var2));
   }

   public ChoiceBoxTableCell(ObservableList var1) {
      this((StringConverter)null, (ObservableList)var1);
   }

   public ChoiceBoxTableCell(StringConverter var1, ObservableList var2) {
      this.converter = new SimpleObjectProperty(this, "converter");
      this.getStyleClass().add("choice-box-table-cell");
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

   public ObservableList getItems() {
      return this.items;
   }

   public void startEdit() {
      if (this.isEditable() && this.getTableView().isEditable() && this.getTableColumn().isEditable()) {
         if (this.choiceBox == null) {
            this.choiceBox = CellUtils.createChoiceBox(this, this.items, this.converterProperty());
         }

         this.choiceBox.getSelectionModel().select(this.getItem());
         super.startEdit();
         this.setText((String)null);
         this.setGraphic(this.choiceBox);
      }
   }

   public void cancelEdit() {
      super.cancelEdit();
      this.setText(this.getConverter().toString(this.getItem()));
      this.setGraphic((Node)null);
   }

   public void updateItem(Object var1, boolean var2) {
      super.updateItem(var1, var2);
      CellUtils.updateItem(this, this.getConverter(), (HBox)null, (Node)null, (ChoiceBox)this.choiceBox);
   }
}
