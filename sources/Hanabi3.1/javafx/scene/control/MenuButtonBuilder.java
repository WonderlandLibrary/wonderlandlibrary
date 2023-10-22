package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.geometry.Side;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class MenuButtonBuilder extends ButtonBaseBuilder implements Builder {
   private int __set;
   private Collection items;
   private Side popupSide;

   protected MenuButtonBuilder() {
   }

   public static MenuButtonBuilder create() {
      return new MenuButtonBuilder();
   }

   public void applyTo(MenuButton var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.getItems().addAll(this.items);
      }

      if ((var2 & 2) != 0) {
         var1.setPopupSide(this.popupSide);
      }

   }

   public MenuButtonBuilder items(Collection var1) {
      this.items = var1;
      this.__set |= 1;
      return this;
   }

   public MenuButtonBuilder items(MenuItem... var1) {
      return this.items((Collection)Arrays.asList(var1));
   }

   public MenuButtonBuilder popupSide(Side var1) {
      this.popupSide = var1;
      this.__set |= 2;
      return this;
   }

   public MenuButton build() {
      MenuButton var1 = new MenuButton();
      this.applyTo(var1);
      return var1;
   }
}
