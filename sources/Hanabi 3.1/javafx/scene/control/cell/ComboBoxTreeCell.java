package javafx.scene.control.cell;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class ComboBoxTreeCell extends DefaultTreeCell {
   private final ObservableList items;
   private ComboBox comboBox;
   private HBox hbox;
   private ObjectProperty converter;
   private BooleanProperty comboBoxEditable;

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
         return new ComboBoxTreeCell(var0, var1);
      };
   }

   public ComboBoxTreeCell() {
      this(FXCollections.observableArrayList());
   }

   @SafeVarargs
   public ComboBoxTreeCell(Object... var1) {
      this(FXCollections.observableArrayList(var1));
   }

   @SafeVarargs
   public ComboBoxTreeCell(StringConverter var1, Object... var2) {
      this(var1, FXCollections.observableArrayList(var2));
   }

   public ComboBoxTreeCell(ObservableList var1) {
      this((StringConverter)null, (ObservableList)var1);
   }

   public ComboBoxTreeCell(StringConverter var1, ObservableList var2) {
      this.converter = new SimpleObjectProperty(this, "converter");
      this.comboBoxEditable = new SimpleBooleanProperty(this, "comboBoxEditable");
      this.getStyleClass().add("combo-box-tree-cell");
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
      if (this.isEditable() && this.getTreeView().isEditable()) {
         TreeItem var1 = this.getTreeItem();
         if (var1 != null) {
            if (this.comboBox == null) {
               this.comboBox = CellUtils.createComboBox(this, this.items, this.converterProperty());
               this.comboBox.editableProperty().bind(this.comboBoxEditableProperty());
            }

            if (this.hbox == null) {
               this.hbox = new HBox((double)CellUtils.TREE_VIEW_HBOX_GRAPHIC_PADDING);
            }

            this.comboBox.getSelectionModel().select(var1.getValue());
            super.startEdit();
            if (this.isEditing()) {
               this.setText((String)null);
               Node var2 = CellUtils.getGraphic(var1);
               if (var2 != null) {
                  this.hbox.getChildren().setAll((Object[])(var2, this.comboBox));
                  this.setGraphic(this.hbox);
               } else {
                  this.setGraphic(this.comboBox);
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
      Node var3 = CellUtils.getGraphic(this.getTreeItem());
      CellUtils.updateItem(this, this.getConverter(), this.hbox, var3, (ComboBox)this.comboBox);
   }
}
