package javafx.scene.control;

import javafx.collections.ObservableList;
import javafx.util.Builder;
import javafx.util.Callback;
import javafx.util.StringConverter;

/** @deprecated */
@Deprecated
public class ComboBoxBuilder extends ComboBoxBaseBuilder implements Builder {
   private int __set;
   private ListCell buttonCell;
   private Callback cellFactory;
   private StringConverter converter;
   private ObservableList items;
   private SingleSelectionModel selectionModel;
   private int visibleRowCount;

   protected ComboBoxBuilder() {
   }

   public static ComboBoxBuilder create() {
      return new ComboBoxBuilder();
   }

   public void applyTo(ComboBox var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setButtonCell(this.buttonCell);
      }

      if ((var2 & 2) != 0) {
         var1.setCellFactory(this.cellFactory);
      }

      if ((var2 & 4) != 0) {
         var1.setConverter(this.converter);
      }

      if ((var2 & 8) != 0) {
         var1.setItems(this.items);
      }

      if ((var2 & 16) != 0) {
         var1.setSelectionModel(this.selectionModel);
      }

      if ((var2 & 32) != 0) {
         var1.setVisibleRowCount(this.visibleRowCount);
      }

   }

   public ComboBoxBuilder buttonCell(ListCell var1) {
      this.buttonCell = var1;
      this.__set |= 1;
      return this;
   }

   public ComboBoxBuilder cellFactory(Callback var1) {
      this.cellFactory = var1;
      this.__set |= 2;
      return this;
   }

   public ComboBoxBuilder converter(StringConverter var1) {
      this.converter = var1;
      this.__set |= 4;
      return this;
   }

   public ComboBoxBuilder items(ObservableList var1) {
      this.items = var1;
      this.__set |= 8;
      return this;
   }

   public ComboBoxBuilder selectionModel(SingleSelectionModel var1) {
      this.selectionModel = var1;
      this.__set |= 16;
      return this;
   }

   public ComboBoxBuilder visibleRowCount(int var1) {
      this.visibleRowCount = var1;
      this.__set |= 32;
      return this;
   }

   public ComboBox build() {
      ComboBox var1 = new ComboBox();
      this.applyTo(var1);
      return var1;
   }
}
