package com.sun.javafx.scene.control;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.menu.CheckMenuItemBase;
import com.sun.javafx.menu.CustomMenuItemBase;
import com.sun.javafx.menu.MenuBase;
import com.sun.javafx.menu.MenuItemBase;
import com.sun.javafx.menu.RadioMenuItemBase;
import com.sun.javafx.menu.SeparatorMenuItemBase;
import java.util.Iterator;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class GlobalMenuAdapter extends Menu implements MenuBase {
   private Menu menu;
   private final ObservableList items = new TrackableObservableList() {
      protected void onChanged(ListChangeListener.Change var1) {
      }
   };

   public static MenuBase adapt(Menu var0) {
      return new GlobalMenuAdapter(var0);
   }

   private GlobalMenuAdapter(Menu var1) {
      super(var1.getText());
      this.menu = var1;
      bindMenuItemProperties(this, var1);
      var1.showingProperty().addListener((var2x) -> {
         if (var1.isShowing() && !this.isShowing()) {
            this.show();
         } else if (!var1.isShowing() && this.isShowing()) {
            this.hide();
         }

      });
      this.showingProperty().addListener((var2x) -> {
         if (this.isShowing() && !var1.isShowing()) {
            var1.show();
         } else if (!this.isShowing() && var1.isShowing()) {
            var1.hide();
         }

      });
      var1.getItems().addListener(new ListChangeListener() {
         public void onChanged(ListChangeListener.Change var1) {
            while(var1.next()) {
               int var2 = var1.getFrom();
               int var3 = var1.getTo();
               List var4 = var1.getRemoved();

               int var5;
               for(var5 = var2 + var4.size() - 1; var5 >= var2; --var5) {
                  GlobalMenuAdapter.this.items.remove(var5);
                  GlobalMenuAdapter.this.getItems().remove(var5);
               }

               for(var5 = var2; var5 < var3; ++var5) {
                  MenuItem var6 = (MenuItem)var1.getList().get(var5);
                  GlobalMenuAdapter.this.insertItem(var6, var5);
               }
            }

         }
      });
      Iterator var2 = var1.getItems().iterator();

      while(var2.hasNext()) {
         MenuItem var3 = (MenuItem)var2.next();
         this.insertItem(var3, this.items.size());
      }

   }

   private void insertItem(MenuItem var1, int var2) {
      Object var3;
      if (var1 instanceof Menu) {
         var3 = new GlobalMenuAdapter((Menu)var1);
      } else if (var1 instanceof CheckMenuItem) {
         var3 = new CheckMenuItemAdapter((CheckMenuItem)var1);
      } else if (var1 instanceof RadioMenuItem) {
         var3 = new RadioMenuItemAdapter((RadioMenuItem)var1);
      } else if (var1 instanceof SeparatorMenuItem) {
         var3 = new SeparatorMenuItemAdapter((SeparatorMenuItem)var1);
      } else if (var1 instanceof CustomMenuItem) {
         var3 = new CustomMenuItemAdapter((CustomMenuItem)var1);
      } else {
         var3 = new MenuItemAdapter(var1);
      }

      this.items.add(var2, var3);
      this.getItems().add(var2, (MenuItem)var3);
   }

   public final ObservableList getItemsBase() {
      return this.items;
   }

   private static void bindMenuItemProperties(MenuItem var0, MenuItem var1) {
      var0.idProperty().bind(var1.idProperty());
      var0.textProperty().bind(var1.textProperty());
      var0.graphicProperty().bind(var1.graphicProperty());
      var0.disableProperty().bind(var1.disableProperty());
      var0.visibleProperty().bind(var1.visibleProperty());
      var0.acceleratorProperty().bind(var1.acceleratorProperty());
      var0.mnemonicParsingProperty().bind(var1.mnemonicParsingProperty());
      var0.setOnAction((var1x) -> {
         var1.fire();
      });
   }

   public void fireValidation() {
      if (this.menu.getOnMenuValidation() != null) {
         Event.fireEvent(this.menu, new Event(MENU_VALIDATION_EVENT));
      }

      Menu var1 = this.menu.getParentMenu();
      if (var1 != null && var1.getOnMenuValidation() != null) {
         Event.fireEvent(var1, new Event(MenuItem.MENU_VALIDATION_EVENT));
      }

   }

   private static class CustomMenuItemAdapter extends CustomMenuItem implements CustomMenuItemBase {
      private CustomMenuItem menuItem;

      private CustomMenuItemAdapter(CustomMenuItem var1) {
         this.menuItem = var1;
         GlobalMenuAdapter.bindMenuItemProperties(this, var1);
      }

      public void fireValidation() {
         if (this.getOnMenuValidation() != null) {
            Event.fireEvent(this.menuItem, new Event(MENU_VALIDATION_EVENT));
         }

         Menu var1 = this.menuItem.getParentMenu();
         if (var1.getOnMenuValidation() != null) {
            Event.fireEvent(var1, new Event(MenuItem.MENU_VALIDATION_EVENT));
         }

      }

      // $FF: synthetic method
      CustomMenuItemAdapter(CustomMenuItem var1, Object var2) {
         this(var1);
      }
   }

   private static class SeparatorMenuItemAdapter extends SeparatorMenuItem implements SeparatorMenuItemBase {
      private SeparatorMenuItem menuItem;

      private SeparatorMenuItemAdapter(SeparatorMenuItem var1) {
         this.menuItem = var1;
         GlobalMenuAdapter.bindMenuItemProperties(this, var1);
      }

      public void fireValidation() {
         if (this.getOnMenuValidation() != null) {
            Event.fireEvent(this.menuItem, new Event(MENU_VALIDATION_EVENT));
         }

         Menu var1 = this.menuItem.getParentMenu();
         if (var1.getOnMenuValidation() != null) {
            Event.fireEvent(var1, new Event(MenuItem.MENU_VALIDATION_EVENT));
         }

      }

      // $FF: synthetic method
      SeparatorMenuItemAdapter(SeparatorMenuItem var1, Object var2) {
         this(var1);
      }
   }

   private static class RadioMenuItemAdapter extends RadioMenuItem implements RadioMenuItemBase {
      private RadioMenuItem menuItem;

      private RadioMenuItemAdapter(RadioMenuItem var1) {
         super(var1.getText());
         this.menuItem = var1;
         GlobalMenuAdapter.bindMenuItemProperties(this, var1);
         this.selectedProperty().bindBidirectional(var1.selectedProperty());
      }

      public void fireValidation() {
         if (this.getOnMenuValidation() != null) {
            Event.fireEvent(this.menuItem, new Event(MENU_VALIDATION_EVENT));
         }

         Menu var1 = this.menuItem.getParentMenu();
         if (var1.getOnMenuValidation() != null) {
            Event.fireEvent(var1, new Event(MenuItem.MENU_VALIDATION_EVENT));
         }

      }

      // $FF: synthetic method
      RadioMenuItemAdapter(RadioMenuItem var1, Object var2) {
         this(var1);
      }
   }

   private static class CheckMenuItemAdapter extends CheckMenuItem implements CheckMenuItemBase {
      private CheckMenuItem menuItem;

      private CheckMenuItemAdapter(CheckMenuItem var1) {
         super(var1.getText());
         this.menuItem = var1;
         GlobalMenuAdapter.bindMenuItemProperties(this, var1);
         this.selectedProperty().bindBidirectional(var1.selectedProperty());
      }

      public void fireValidation() {
         if (this.getOnMenuValidation() != null) {
            Event.fireEvent(this.menuItem, new Event(MENU_VALIDATION_EVENT));
         }

         Menu var1 = this.menuItem.getParentMenu();
         if (var1.getOnMenuValidation() != null) {
            Event.fireEvent(var1, new Event(MenuItem.MENU_VALIDATION_EVENT));
         }

      }

      // $FF: synthetic method
      CheckMenuItemAdapter(CheckMenuItem var1, Object var2) {
         this(var1);
      }
   }

   private static class MenuItemAdapter extends MenuItem implements MenuItemBase {
      private MenuItem menuItem;

      private MenuItemAdapter(MenuItem var1) {
         super(var1.getText());
         this.menuItem = var1;
         GlobalMenuAdapter.bindMenuItemProperties(this, var1);
      }

      public void fireValidation() {
         if (this.menuItem.getOnMenuValidation() != null) {
            Event.fireEvent(this.menuItem, new Event(MenuItem.MENU_VALIDATION_EVENT));
         }

         Menu var1 = this.menuItem.getParentMenu();
         if (var1.getOnMenuValidation() != null) {
            Event.fireEvent(var1, new Event(MenuItem.MENU_VALIDATION_EVENT));
         }

      }

      // $FF: synthetic method
      MenuItemAdapter(MenuItem var1, Object var2) {
         this(var1);
      }
   }
}
