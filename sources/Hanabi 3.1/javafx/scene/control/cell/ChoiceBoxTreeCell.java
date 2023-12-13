package javafx.scene.control.cell;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class ChoiceBoxTreeCell extends DefaultTreeCell {
   private final ObservableList items;
   private ChoiceBox choiceBox;
   private HBox hbox;
   private ObjectProperty converter;

   @SafeVarargs
   public static Callback forTreeView(Object... var0) {
      return forTreeView(FXCollections.observableArrayList(var0));
   }

   public static Callback forTreeView(ObservableList var0) {
      return forTreeView((StringConverter)null, (ObservableList)var0);
   }

   @SafeVarargs
   public static Callback forTreeView(StringConverter var0, Object... var1) {
      return forTreeView(var0, FXCollections.observableArrayList(var1));
   }

   public static Callback forTreeView(StringConverter var0, ObservableList var1) {
      return (var2) -> {
         return new ChoiceBoxTreeCell(var0, var1);
      };
   }

   public ChoiceBoxTreeCell() {
      this(FXCollections.observableArrayList());
   }

   @SafeVarargs
   public ChoiceBoxTreeCell(Object... var1) {
      this(FXCollections.observableArrayList(var1));
   }

   @SafeVarargs
   public ChoiceBoxTreeCell(StringConverter var1, Object... var2) {
      this(var1, FXCollections.observableArrayList(var2));
   }

   public ChoiceBoxTreeCell(ObservableList var1) {
      this((StringConverter)null, (ObservableList)var1);
   }

   public ChoiceBoxTreeCell(StringConverter var1, ObservableList var2) {
      this.converter = new SimpleObjectProperty(this, "converter");
      this.getStyleClass().add("choice-box-tree-cell");
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
      if (this.isEditable() && this.getTreeView().isEditable()) {
         TreeItem var1 = this.getTreeItem();
         if (var1 != null) {
            if (this.choiceBox == null) {
               this.choiceBox = CellUtils.createChoiceBox(this, this.items, this.converterProperty());
            }

            if (this.hbox == null) {
               this.hbox = new HBox((double)CellUtils.TREE_VIEW_HBOX_GRAPHIC_PADDING);
            }

            this.choiceBox.getSelectionModel().select(var1.getValue());
            super.startEdit();
            if (this.isEditing()) {
               this.setText((String)null);
               Node var2 = this.getTreeItemGraphic();
               if (var2 != null) {
                  this.hbox.getChildren().setAll((Object[])(var2, this.choiceBox));
                  this.setGraphic(this.hbox);
               } else {
                  this.setGraphic(this.choiceBox);
               }
            }

         }
      }
   }

   public void cancelEdit() {
      super.cancelEdit();
      this.setText(this.getConverter().toString(this.getItem()));
      this.setGraphic((Node)null);
   }

   public void updateItem(Object var1, boolean var2) {
      super.updateItem(var1, var2);
      CellUtils.updateItem(this, this.getConverter(), this.hbox, this.getTreeItemGraphic(), (ChoiceBox)this.choiceBox);
   }

   private Node getTreeItemGraphic() {
      TreeItem var1 = this.getTreeItem();
      return var1 == null ? null : var1.getGraphic();
   }
}
