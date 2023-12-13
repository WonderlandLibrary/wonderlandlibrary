package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Menu;
import com.sun.glass.ui.MenuBar;
import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Pixels;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.menu.CheckMenuItemBase;
import com.sun.javafx.menu.MenuBase;
import com.sun.javafx.menu.MenuItemBase;
import com.sun.javafx.menu.RadioMenuItemBase;
import com.sun.javafx.menu.SeparatorMenuItemBase;
import com.sun.javafx.tk.TKSystemMenu;
import java.util.Iterator;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

class GlassSystemMenu implements TKSystemMenu {
   private List systemMenus = null;
   private MenuBar glassSystemMenuBar = null;
   private InvalidationListener visibilityListener = (var1) -> {
      if (this.systemMenus != null) {
         this.setMenus(this.systemMenus);
      }

   };

   protected void createMenuBar() {
      if (this.glassSystemMenuBar == null) {
         Application var1 = Application.GetApplication();
         this.glassSystemMenuBar = var1.createMenuBar();
         var1.installDefaultMenus(this.glassSystemMenuBar);
         if (this.systemMenus != null) {
            this.setMenus(this.systemMenus);
         }
      }

   }

   protected MenuBar getMenuBar() {
      return this.glassSystemMenuBar;
   }

   public boolean isSupported() {
      return Application.GetApplication().supportsSystemMenu();
   }

   public void setMenus(List var1) {
      this.systemMenus = var1;
      if (this.glassSystemMenuBar != null) {
         List var2 = this.glassSystemMenuBar.getMenus();
         int var3 = var2.size();

         for(int var4 = var3 - 1; var4 >= 1; --var4) {
            Menu var5 = (Menu)var2.get(var4);
            this.clearMenu(var5);
            this.glassSystemMenuBar.remove(var4);
         }

         Iterator var7 = var1.iterator();

         while(var7.hasNext()) {
            MenuBase var6 = (MenuBase)var7.next();
            this.addMenu((Menu)null, var6);
         }
      }

   }

   private void clearMenu(Menu var1) {
      for(int var2 = var1.getItems().size() - 1; var2 >= 0; --var2) {
         Object var3 = var1.getItems().get(var2);
         if (var3 instanceof MenuItem) {
            ((MenuItem)var3).setCallback((MenuItem.Callback)null);
         } else if (var3 instanceof Menu) {
            this.clearMenu((Menu)var3);
         }
      }

      var1.setEventHandler((Menu.EventHandler)null);
   }

   private void addMenu(Menu var1, MenuBase var2) {
      if (var1 != null) {
         this.insertMenu(var1, var2, var1.getItems().size());
      } else {
         this.insertMenu(var1, var2, this.glassSystemMenuBar.getMenus().size());
      }

   }

   private void insertMenu(Menu var1, MenuBase var2, int var3) {
      Application var4 = Application.GetApplication();
      Menu var5 = var4.createMenu(this.parseText(var2), !var2.isDisable());
      var5.setEventHandler(new GlassMenuEventHandler(var2));
      var2.visibleProperty().removeListener(this.visibilityListener);
      var2.visibleProperty().addListener(this.visibilityListener);
      if (var2.isVisible()) {
         ObservableList var6 = var2.getItemsBase();
         var6.addListener((var2x) -> {
            label34:
            while(true) {
               if (var2x.next()) {
                  int var3 = var2x.getFrom();
                  int var4 = var2x.getTo();
                  List var5x = var2x.getRemoved();

                  int var6;
                  for(var6 = var3 + var5x.size() - 1; var6 >= var3; --var6) {
                     List var7 = var5.getItems();
                     if (var6 >= 0 && var7.size() > var6) {
                        var5.remove(var6);
                     }
                  }

                  var6 = var3;

                  while(true) {
                     if (var6 >= var4) {
                        continue label34;
                     }

                     MenuItemBase var8 = (MenuItemBase)var2x.getList().get(var6);
                     if (var8 instanceof MenuBase) {
                        this.insertMenu(var5, (MenuBase)var8, var6);
                     } else {
                        this.insertMenuItem(var5, var8, var6);
                     }

                     ++var6;
                  }
               }

               return;
            }
         });
         Iterator var7 = var6.iterator();

         while(var7.hasNext()) {
            MenuItemBase var8 = (MenuItemBase)var7.next();
            if (var8 instanceof MenuBase) {
               this.addMenu(var5, (MenuBase)var8);
            } else {
               this.addMenuItem(var5, var8);
            }
         }

         var5.setPixels(this.getPixels(var2));
         this.setMenuBindings(var5, var2);
         if (var1 != null) {
            var1.insert(var5, var3);
         } else {
            this.glassSystemMenuBar.insert(var5, var3);
         }

      }
   }

   private void setMenuBindings(Menu var1, MenuBase var2) {
      var2.textProperty().addListener((var3) -> {
         var1.setTitle(this.parseText(var2));
      });
      var2.disableProperty().addListener((var2x) -> {
         var1.setEnabled(!var2.isDisable());
      });
      var2.mnemonicParsingProperty().addListener((var3) -> {
         var1.setTitle(this.parseText(var2));
      });
   }

   private void addMenuItem(Menu var1, MenuItemBase var2) {
      this.insertMenuItem(var1, var2, var1.getItems().size());
   }

   private void insertMenuItem(final Menu var1, final MenuItemBase var2, int var3) {
      Application var4 = Application.GetApplication();
      var2.visibleProperty().removeListener(this.visibilityListener);
      var2.visibleProperty().addListener(this.visibilityListener);
      if (var2.isVisible()) {
         if (var2 instanceof SeparatorMenuItemBase) {
            if (var2.isVisible()) {
               var1.insert(MenuItem.Separator, var3);
            }
         } else {
            MenuItem.Callback var5 = new MenuItem.Callback() {
               public void action() {
                  if (var2 instanceof CheckMenuItemBase) {
                     CheckMenuItemBase var1x = (CheckMenuItemBase)var2;
                     var1x.setSelected(!var1x.isSelected());
                  } else if (var2 instanceof RadioMenuItemBase) {
                     RadioMenuItemBase var2x = (RadioMenuItemBase)var2;
                     var2x.setSelected(true);
                  }

                  var2.fire();
               }

               public void validate() {
                  Menu.EventHandler var1x = var1.getEventHandler();
                  GlassMenuEventHandler var2x = (GlassMenuEventHandler)var1x;
                  if (!var2x.isMenuOpen()) {
                     var2.fireValidation();
                  }
               }
            };
            MenuItem var6 = var4.createMenuItem(this.parseText(var2), var5);
            var2.textProperty().addListener((var3x) -> {
               var6.setTitle(this.parseText(var2));
            });
            var6.setPixels(this.getPixels(var2));
            var2.graphicProperty().addListener((var3x) -> {
               var6.setPixels(this.getPixels(var2));
            });
            var6.setEnabled(!var2.isDisable());
            var2.disableProperty().addListener((var2x) -> {
               var6.setEnabled(!var2.isDisable());
            });
            this.setShortcut(var6, var2);
            var2.acceleratorProperty().addListener((var3x) -> {
               this.setShortcut(var6, var2);
            });
            var2.mnemonicParsingProperty().addListener((var3x) -> {
               var6.setTitle(this.parseText(var2));
            });
            if (var2 instanceof CheckMenuItemBase) {
               CheckMenuItemBase var7 = (CheckMenuItemBase)var2;
               var6.setChecked(var7.isSelected());
               var7.selectedProperty().addListener((var2x) -> {
                  var6.setChecked(var7.isSelected());
               });
            } else if (var2 instanceof RadioMenuItemBase) {
               RadioMenuItemBase var8 = (RadioMenuItemBase)var2;
               var6.setChecked(var8.isSelected());
               var8.selectedProperty().addListener((var2x) -> {
                  var6.setChecked(var8.isSelected());
               });
            }

            var1.insert(var6, var3);
         }

      }
   }

   private String parseText(MenuItemBase var1) {
      String var2 = var1.getText();
      if (var2 == null) {
         return "";
      } else {
         return !var2.isEmpty() && var1.isMnemonicParsing() ? var2.replaceFirst("_([^_])", "$1") : var2;
      }
   }

   private Pixels getPixels(MenuItemBase var1) {
      if (var1.getGraphic() instanceof ImageView) {
         ImageView var2 = (ImageView)var1.getGraphic();
         Image var3 = var2.getImage();
         if (var3 == null) {
            return null;
         }

         String var4 = var3.impl_getUrl();
         if (var4 == null || PixelUtils.supportedFormatType(var4)) {
            com.sun.prism.Image var5 = (com.sun.prism.Image)var3.impl_getPlatformImage();
            return var5 == null ? null : PixelUtils.imageToPixels(var5);
         }
      }

      return null;
   }

   private void setShortcut(MenuItem var1, MenuItemBase var2) {
      KeyCombination var3 = var2.getAccelerator();
      if (var3 == null) {
         var1.setShortcut(0, 0);
      } else if (var3 instanceof KeyCodeCombination) {
         KeyCodeCombination var4 = (KeyCodeCombination)var3;
         KeyCode var5 = var4.getCode();

         assert PlatformUtil.isMac() || PlatformUtil.isLinux();

         int var6 = this.glassModifiers(var4);
         if (PlatformUtil.isMac()) {
            int var7 = var5.isLetterKey() ? var5.impl_getChar().toUpperCase().charAt(0) : var5.impl_getCode();
            var1.setShortcut(var7, var6);
         } else if (PlatformUtil.isLinux()) {
            String var10 = var5.impl_getChar().toLowerCase();
            if ((var6 & 4) != 0) {
               var1.setShortcut(var10.charAt(0), var6);
            } else {
               var1.setShortcut(0, 0);
            }
         } else {
            var1.setShortcut(0, 0);
         }
      } else if (var3 instanceof KeyCharacterCombination) {
         KeyCharacterCombination var8 = (KeyCharacterCombination)var3;
         String var9 = var8.getCharacter();
         var1.setShortcut(var9.charAt(0), this.glassModifiers(var8));
      }

   }

   private int glassModifiers(KeyCombination var1) {
      int var2 = 0;
      if (var1.getShift() == KeyCombination.ModifierValue.DOWN) {
         ++var2;
      }

      if (var1.getControl() == KeyCombination.ModifierValue.DOWN) {
         var2 += 4;
      }

      if (var1.getAlt() == KeyCombination.ModifierValue.DOWN) {
         var2 += 8;
      }

      if (var1.getShortcut() == KeyCombination.ModifierValue.DOWN) {
         if (PlatformUtil.isLinux()) {
            var2 += 4;
         } else if (PlatformUtil.isMac()) {
            var2 += 16;
         }
      }

      if (var1.getMeta() == KeyCombination.ModifierValue.DOWN) {
         if (PlatformUtil.isLinux()) {
            var2 += 16;
         } else if (PlatformUtil.isMac()) {
            var2 += 16;
         }
      }

      if (var1 instanceof KeyCodeCombination) {
         KeyCode var3 = ((KeyCodeCombination)var1).getCode();
         int var4 = var3.impl_getCode();
         if (var4 >= KeyCode.F1.impl_getCode() && var4 <= KeyCode.F12.impl_getCode() || var4 >= KeyCode.F13.impl_getCode() && var4 <= KeyCode.F24.impl_getCode()) {
            var2 += 2;
         }
      }

      return var2;
   }
}
