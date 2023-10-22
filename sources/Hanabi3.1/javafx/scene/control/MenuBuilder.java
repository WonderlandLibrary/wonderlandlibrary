package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.event.EventHandler;

/** @deprecated */
@Deprecated
public class MenuBuilder extends MenuItemBuilder {
   private int __set;
   private Collection items;
   private EventHandler onHidden;
   private EventHandler onHiding;
   private EventHandler onShowing;
   private EventHandler onShown;

   protected MenuBuilder() {
   }

   public static MenuBuilder create() {
      return new MenuBuilder();
   }

   public void applyTo(Menu var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.getItems().addAll(this.items);
      }

      if ((var2 & 2) != 0) {
         var1.setOnHidden(this.onHidden);
      }

      if ((var2 & 4) != 0) {
         var1.setOnHiding(this.onHiding);
      }

      if ((var2 & 8) != 0) {
         var1.setOnShowing(this.onShowing);
      }

      if ((var2 & 16) != 0) {
         var1.setOnShown(this.onShown);
      }

   }

   public MenuBuilder items(Collection var1) {
      this.items = var1;
      this.__set |= 1;
      return this;
   }

   public MenuBuilder items(MenuItem... var1) {
      return this.items((Collection)Arrays.asList(var1));
   }

   public MenuBuilder onHidden(EventHandler var1) {
      this.onHidden = var1;
      this.__set |= 2;
      return this;
   }

   public MenuBuilder onHiding(EventHandler var1) {
      this.onHiding = var1;
      this.__set |= 4;
      return this;
   }

   public MenuBuilder onShowing(EventHandler var1) {
      this.onShowing = var1;
      this.__set |= 8;
      return this;
   }

   public MenuBuilder onShown(EventHandler var1) {
      this.onShown = var1;
      this.__set |= 16;
      return this;
   }

   public Menu build() {
      Menu var1 = new Menu();
      this.applyTo(var1);
      return var1;
   }
}
