package javafx.scene.control;

import javafx.collections.ObservableList;
import javafx.util.Builder;
import javafx.util.StringConverter;

/** @deprecated */
@Deprecated
public class ChoiceBoxBuilder extends ControlBuilder implements Builder {
   private int __set;
   private StringConverter converter;
   private ObservableList items;
   private SingleSelectionModel selectionModel;
   private Object value;

   protected ChoiceBoxBuilder() {
   }

   public static ChoiceBoxBuilder create() {
      return new ChoiceBoxBuilder();
   }

   public void applyTo(ChoiceBox var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.setConverter(this.converter);
      }

      if ((var2 & 2) != 0) {
         var1.setItems(this.items);
      }

      if ((var2 & 4) != 0) {
         var1.setSelectionModel(this.selectionModel);
      }

      if ((var2 & 8) != 0) {
         var1.setValue(this.value);
      }

   }

   public ChoiceBoxBuilder converter(StringConverter var1) {
      this.converter = var1;
      this.__set |= 1;
      return this;
   }

   public ChoiceBoxBuilder items(ObservableList var1) {
      this.items = var1;
      this.__set |= 2;
      return this;
   }

   public ChoiceBoxBuilder selectionModel(SingleSelectionModel var1) {
      this.selectionModel = var1;
      this.__set |= 4;
      return this;
   }

   public ChoiceBoxBuilder value(Object var1) {
      this.value = var1;
      this.__set |= 8;
      return this;
   }

   public ChoiceBox build() {
      ChoiceBox var1 = new ChoiceBox();
      this.applyTo(var1);
      return var1;
   }
}
