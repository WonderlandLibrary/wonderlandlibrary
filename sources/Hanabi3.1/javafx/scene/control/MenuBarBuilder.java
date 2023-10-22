package javafx.scene.control;

import java.util.Arrays;
import java.util.Collection;
import javafx.util.Builder;

/** @deprecated */
@Deprecated
public class MenuBarBuilder extends ControlBuilder implements Builder {
   private int __set;
   private Collection menus;
   private boolean useSystemMenuBar;

   protected MenuBarBuilder() {
   }

   public static MenuBarBuilder create() {
      return new MenuBarBuilder();
   }

   public void applyTo(MenuBar var1) {
      super.applyTo(var1);
      int var2 = this.__set;
      if ((var2 & 1) != 0) {
         var1.getMenus().addAll(this.menus);
      }

      if ((var2 & 2) != 0) {
         var1.setUseSystemMenuBar(this.useSystemMenuBar);
      }

   }

   public MenuBarBuilder menus(Collection var1) {
      this.menus = var1;
      this.__set |= 1;
      return this;
   }

   public MenuBarBuilder menus(Menu... var1) {
      return this.menus((Collection)Arrays.asList(var1));
   }

   public MenuBarBuilder useSystemMenuBar(boolean var1) {
      this.useSystemMenuBar = var1;
      this.__set |= 2;
      return this;
   }

   public MenuBar build() {
      MenuBar var1 = new MenuBar();
      this.applyTo(var1);
      return var1;
   }
}
