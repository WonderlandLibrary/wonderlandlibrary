package javafx.scene.control.cell;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Cell;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

class CellUtils {
   static int TREE_VIEW_HBOX_GRAPHIC_PADDING = 3;
   private static final StringConverter defaultStringConverter = new StringConverter() {
      public String toString(Object var1) {
         return var1 == null ? null : var1.toString();
      }

      public Object fromString(String var1) {
         return var1;
      }
   };
   private static final StringConverter defaultTreeItemStringConverter = new StringConverter() {
      public String toString(TreeItem var1) {
         return var1 != null && var1.getValue() != null ? var1.getValue().toString() : "";
      }

      public TreeItem fromString(String var1) {
         return new TreeItem(var1);
      }
   };

   static StringConverter defaultStringConverter() {
      return defaultStringConverter;
   }

   static StringConverter defaultTreeItemStringConverter() {
      return defaultTreeItemStringConverter;
   }

   private static String getItemText(Cell var0, StringConverter var1) {
      return var1 == null ? (var0.getItem() == null ? "" : var0.getItem().toString()) : var1.toString(var0.getItem());
   }

   static Node getGraphic(TreeItem var0) {
      return var0 == null ? null : var0.getGraphic();
   }

   static void updateItem(Cell var0, StringConverter var1, ChoiceBox var2) {
      updateItem(var0, var1, (HBox)null, (Node)null, (ChoiceBox)var2);
   }

   static void updateItem(Cell var0, StringConverter var1, HBox var2, Node var3, ChoiceBox var4) {
      if (var0.isEmpty()) {
         var0.setText((String)null);
         var0.setGraphic((Node)null);
      } else if (var0.isEditing()) {
         if (var4 != null) {
            var4.getSelectionModel().select(var0.getItem());
         }

         var0.setText((String)null);
         if (var3 != null) {
            var2.getChildren().setAll((Object[])(var3, var4));
            var0.setGraphic(var2);
         } else {
            var0.setGraphic(var4);
         }
      } else {
         var0.setText(getItemText(var0, var1));
         var0.setGraphic(var3);
      }

   }

   static ChoiceBox createChoiceBox(Cell var0, ObservableList var1, ObjectProperty var2) {
      ChoiceBox var3 = new ChoiceBox(var1);
      var3.setMaxWidth(Double.MAX_VALUE);
      var3.converterProperty().bind(var2);
      var3.getSelectionModel().selectedItemProperty().addListener((var1x, var2x, var3x) -> {
         if (var0.isEditing()) {
            var0.commitEdit(var3x);
         }

      });
      return var3;
   }

   static void updateItem(Cell var0, StringConverter var1, TextField var2) {
      updateItem(var0, var1, (HBox)null, (Node)null, (TextField)var2);
   }

   static void updateItem(Cell var0, StringConverter var1, HBox var2, Node var3, TextField var4) {
      if (var0.isEmpty()) {
         var0.setText((String)null);
         var0.setGraphic((Node)null);
      } else if (var0.isEditing()) {
         if (var4 != null) {
            var4.setText(getItemText(var0, var1));
         }

         var0.setText((String)null);
         if (var3 != null) {
            var2.getChildren().setAll((Object[])(var3, var4));
            var0.setGraphic(var2);
         } else {
            var0.setGraphic(var4);
         }
      } else {
         var0.setText(getItemText(var0, var1));
         var0.setGraphic(var3);
      }

   }

   static void startEdit(Cell var0, StringConverter var1, HBox var2, Node var3, TextField var4) {
      if (var4 != null) {
         var4.setText(getItemText(var0, var1));
      }

      var0.setText((String)null);
      if (var3 != null) {
         var2.getChildren().setAll((Object[])(var3, var4));
         var0.setGraphic(var2);
      } else {
         var0.setGraphic(var4);
      }

      var4.selectAll();
      var4.requestFocus();
   }

   static void cancelEdit(Cell var0, StringConverter var1, Node var2) {
      var0.setText(getItemText(var0, var1));
      var0.setGraphic(var2);
   }

   static TextField createTextField(Cell var0, StringConverter var1) {
      TextField var2 = new TextField(getItemText(var0, var1));
      var2.setOnAction((var3) -> {
         if (var1 == null) {
            throw new IllegalStateException("Attempting to convert text input into Object, but provided StringConverter is null. Be sure to set a StringConverter in your cell factory.");
         } else {
            var0.commitEdit(var1.fromString(var2.getText()));
            var3.consume();
         }
      });
      var2.setOnKeyReleased((var1x) -> {
         if (var1x.getCode() == KeyCode.ESCAPE) {
            var0.cancelEdit();
            var1x.consume();
         }

      });
      return var2;
   }

   static void updateItem(Cell var0, StringConverter var1, ComboBox var2) {
      updateItem(var0, var1, (HBox)null, (Node)null, (ComboBox)var2);
   }

   static void updateItem(Cell var0, StringConverter var1, HBox var2, Node var3, ComboBox var4) {
      if (var0.isEmpty()) {
         var0.setText((String)null);
         var0.setGraphic((Node)null);
      } else if (var0.isEditing()) {
         if (var4 != null) {
            var4.getSelectionModel().select(var0.getItem());
         }

         var0.setText((String)null);
         if (var3 != null) {
            var2.getChildren().setAll((Object[])(var3, var4));
            var0.setGraphic(var2);
         } else {
            var0.setGraphic(var4);
         }
      } else {
         var0.setText(getItemText(var0, var1));
         var0.setGraphic(var3);
      }

   }

   static ComboBox createComboBox(Cell var0, ObservableList var1, ObjectProperty var2) {
      ComboBox var3 = new ComboBox(var1);
      var3.converterProperty().bind(var2);
      var3.setMaxWidth(Double.MAX_VALUE);
      var3.getSelectionModel().selectedItemProperty().addListener((var1x, var2x, var3x) -> {
         if (var0.isEditing()) {
            var0.commitEdit(var3x);
         }

      });
      return var3;
   }
}
