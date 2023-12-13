package javafx.scene.control.cell;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DefaultStringConverter;

public class TextFieldTreeCell extends DefaultTreeCell {
   private TextField textField;
   private HBox hbox;
   private ObjectProperty converter;

   public static Callback forTreeView() {
      return forTreeView(new DefaultStringConverter());
   }

   public static Callback forTreeView(StringConverter var0) {
      return (var1) -> {
         return new TextFieldTreeCell(var0);
      };
   }

   public TextFieldTreeCell() {
      this((StringConverter)null);
   }

   public TextFieldTreeCell(StringConverter var1) {
      this.converter = new SimpleObjectProperty(this, "converter");
      this.getStyleClass().add("text-field-tree-cell");
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
      if (this.isEditable() && this.getTreeView().isEditable()) {
         super.startEdit();
         if (this.isEditing()) {
            StringConverter var1 = this.getConverter();
            if (this.textField == null) {
               this.textField = CellUtils.createTextField(this, var1);
            }

            if (this.hbox == null) {
               this.hbox = new HBox((double)CellUtils.TREE_VIEW_HBOX_GRAPHIC_PADDING);
            }

            CellUtils.startEdit(this, var1, this.hbox, this.getTreeItemGraphic(), this.textField);
         }

      }
   }

   public void cancelEdit() {
      super.cancelEdit();
      CellUtils.cancelEdit(this, this.getConverter(), this.getTreeItemGraphic());
   }

   public void updateItem(Object var1, boolean var2) {
      super.updateItem(var1, var2);
      CellUtils.updateItem(this, this.getConverter(), this.hbox, this.getTreeItemGraphic(), (TextField)this.textField);
   }

   private Node getTreeItemGraphic() {
      TreeItem var1 = this.getTreeItem();
      return var1 == null ? null : var1.getGraphic();
   }
}
