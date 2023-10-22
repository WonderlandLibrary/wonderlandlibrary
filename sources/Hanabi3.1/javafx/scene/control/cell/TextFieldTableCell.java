package javafx.scene.control.cell;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class TextFieldTableCell extends TableCell {
   private TextField textField;
   private ObjectProperty converter;

   public static Callback forTableColumn() {
      return forTableColumn(new DefaultStringConverter());
   }

   public static Callback forTableColumn(StringConverter var0) {
      return (var1) -> {
         return new TextFieldTableCell(var0);
      };
   }

   public TextFieldTableCell() {
      this((StringConverter)null);
   }

   public TextFieldTableCell(StringConverter var1) {
      this.converter = new SimpleObjectProperty(this, "converter");
      this.getStyleClass().add("text-field-table-cell");
      this.setConverter(var1);
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

   public void startEdit() {
      if (this.isEditable() && this.getTableView().isEditable() && this.getTableColumn().isEditable()) {
         super.startEdit();
         if (this.isEditing()) {
            if (this.textField == null) {
               this.textField = CellUtils.createTextField(this, this.getConverter());
            }

            CellUtils.startEdit(this, this.getConverter(), (HBox)null, (Node)null, this.textField);
         }

      }
   }

   public void cancelEdit() {
      super.cancelEdit();
      CellUtils.cancelEdit(this, this.getConverter(), (Node)null);
   }

   public void updateItem(Object var1, boolean var2) {
      super.updateItem(var1, var2);
      CellUtils.updateItem(this, this.getConverter(), (HBox)null, (Node)null, (TextField)this.textField);
   }
}
