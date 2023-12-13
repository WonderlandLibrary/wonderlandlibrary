package javafx.scene.control.cell;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class CheckBoxListCell extends ListCell {
   private final CheckBox checkBox;
   private ObservableValue booleanProperty;
   private ObjectProperty converter;
   private ObjectProperty selectedStateCallback;

   public static Callback forListView(Callback var0) {
      return forListView(var0, CellUtils.defaultStringConverter());
   }

   public static Callback forListView(Callback var0, StringConverter var1) {
      return (var2) -> {
         return new CheckBoxListCell(var0, var1);
      };
   }

   public CheckBoxListCell() {
      this((Callback)null);
   }

   public CheckBoxListCell(Callback var1) {
      this(var1, CellUtils.defaultStringConverter());
   }

   public CheckBoxListCell(Callback var1, StringConverter var2) {
      this.converter = new SimpleObjectProperty(this, "converter");
      this.selectedStateCallback = new SimpleObjectProperty(this, "selectedStateCallback");
      this.getStyleClass().add("check-box-list-cell");
      this.setSelectedStateCallback(var1);
      this.setConverter(var2);
      this.checkBox = new CheckBox();
      this.setAlignment(Pos.CENTER_LEFT);
      this.setContentDisplay(ContentDisplay.LEFT);
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
      if (!var2) {
         StringConverter var3 = this.getConverter();
         Callback var4 = this.getSelectedStateCallback();
         if (var4 == null) {
            throw new NullPointerException("The CheckBoxListCell selectedStateCallbackProperty can not be null");
         }

         this.setGraphic(this.checkBox);
         this.setText(var3 != null ? var3.toString(var1) : (var1 == null ? "" : var1.toString()));
         if (this.booleanProperty != null) {
            this.checkBox.selectedProperty().unbindBidirectional((BooleanProperty)this.booleanProperty);
         }

         this.booleanProperty = (ObservableValue)var4.call(var1);
         if (this.booleanProperty != null) {
            this.checkBox.selectedProperty().bindBidirectional((BooleanProperty)this.booleanProperty);
         }
      } else {
         this.setGraphic((Node)null);
         this.setText((String)null);
      }

   }
}
