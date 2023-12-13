package com.sun.javafx.webkit.theme;

import com.sun.webkit.ContextMenu;
import com.sun.webkit.ContextMenuItem;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;

public final class ContextMenuImpl extends ContextMenu {
   private static final Logger log = Logger.getLogger(ContextMenuImpl.class.getName());
   private final ObservableList items = FXCollections.observableArrayList();

   protected void show(ContextMenu.ShowContext var1, int var2, int var3) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "show at [{0}, {1}]", new Object[]{var2, var3});
      }

      javafx.scene.control.ContextMenu var4 = new javafx.scene.control.ContextMenu();
      var4.setOnAction((var1x) -> {
         MenuItem var2 = (MenuItem)var1x.getTarget();
         log.log(Level.FINE, "onAction: item={0}", var2);
         var1.notifyItemSelected(((MenuItemPeer)var2).getItemPeer().getAction());
      });
      var4.getItems().addAll(this.fillMenu());
      PopupMenuImpl.doShow(var4, var1.getPage(), var2, var3);
   }

   protected void appendItem(ContextMenuItem var1) {
      this.insertItem(var1, this.items.size());
   }

   protected void insertItem(ContextMenuItem var1, int var2) {
      if (log.isLoggable(Level.FINE)) {
         log.log(Level.FINE, "item={0}, index={1}", new Object[]{var1, var2});
      }

      if (var1 != null) {
         this.items.remove(var1);
         if (this.items.size() == 0) {
            this.items.add(var1);
         } else {
            this.items.add(var2, var1);
         }

      }
   }

   protected int getItemCount() {
      return this.items.size();
   }

   private MenuItem createMenuItem(ContextMenuItem var1) {
      log.log(Level.FINE, "item={0}", var1);
      if (var1.getType() == 2) {
         MenuImpl var3 = new MenuImpl(var1.getTitle());
         if (var1.getSubmenu() != null) {
            var3.getItems().addAll(((ContextMenuImpl)var1.getSubmenu()).fillMenu());
         }

         return var3;
      } else if (var1.getType() == 0) {
         Object var2 = null;
         if (var1.isChecked()) {
            var2 = new CheckMenuItemImpl(var1);
         } else {
            var2 = new MenuItemImpl(var1);
         }

         ((MenuItem)var2).setDisable(!var1.isEnabled());
         return (MenuItem)var2;
      } else if (var1.getType() == 1) {
         return new SeparatorImpl(var1);
      } else {
         throw new IllegalArgumentException("unexpected item type");
      }
   }

   private ObservableList fillMenu() {
      ObservableList var1 = FXCollections.observableArrayList();
      Iterator var2 = this.items.iterator();

      while(var2.hasNext()) {
         ContextMenuItem var3 = (ContextMenuItem)var2.next();
         var1.add(this.createMenuItem(var3));
      }

      return var1;
   }

   static final class SeparatorImpl extends MenuItem implements MenuItemPeer {
      private final ContextMenuItem itemPeer;

      SeparatorImpl(ContextMenuItem var1) {
         this.itemPeer = var1;
         this.setGraphic(new Separator());
         this.setDisable(true);
      }

      public ContextMenuItem getItemPeer() {
         return this.itemPeer;
      }
   }

   private static final class MenuImpl extends Menu {
      private MenuImpl(String var1) {
         super(var1);
      }

      // $FF: synthetic method
      MenuImpl(String var1, Object var2) {
         this(var1);
      }
   }

   private static final class CheckMenuItemImpl extends CheckMenuItem implements MenuItemPeer {
      private final ContextMenuItem itemPeer;

      private CheckMenuItemImpl(ContextMenuItem var1) {
         this.itemPeer = var1;
      }

      public ContextMenuItem getItemPeer() {
         return this.itemPeer;
      }

      // $FF: synthetic method
      CheckMenuItemImpl(ContextMenuItem var1, Object var2) {
         this(var1);
      }
   }

   private static final class MenuItemImpl extends MenuItem implements MenuItemPeer {
      private final ContextMenuItem itemPeer;

      private MenuItemImpl(ContextMenuItem var1) {
         super(var1.getTitle());
         this.itemPeer = var1;
      }

      public ContextMenuItem getItemPeer() {
         return this.itemPeer;
      }

      // $FF: synthetic method
      MenuItemImpl(ContextMenuItem var1, Object var2) {
         this(var1);
      }
   }

   private interface MenuItemPeer {
      ContextMenuItem getItemPeer();
   }
}
