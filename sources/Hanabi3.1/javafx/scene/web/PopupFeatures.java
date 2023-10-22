package javafx.scene.web;

import javafx.beans.NamedArg;

public final class PopupFeatures {
   private final boolean menu;
   private final boolean status;
   private final boolean toolbar;
   private final boolean resizable;

   public PopupFeatures(@NamedArg("menu") boolean var1, @NamedArg("status") boolean var2, @NamedArg("toolbar") boolean var3, @NamedArg("resizable") boolean var4) {
      this.menu = var1;
      this.status = var2;
      this.toolbar = var3;
      this.resizable = var4;
   }

   public final boolean hasMenu() {
      return this.menu;
   }

   public final boolean hasStatus() {
      return this.status;
   }

   public final boolean hasToolbar() {
      return this.toolbar;
   }

   public final boolean isResizable() {
      return this.resizable;
   }
}
