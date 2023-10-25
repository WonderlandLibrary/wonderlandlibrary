/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.sun.javafx.PlatformUtil
 *  javafx.beans.InvalidationListener
 *  javafx.collections.ObservableList
 *  javafx.scene.image.Image
 *  javafx.scene.image.ImageView
 *  javafx.scene.input.KeyCharacterCombination
 *  javafx.scene.input.KeyCode
 *  javafx.scene.input.KeyCodeCombination
 *  javafx.scene.input.KeyCombination
 *  javafx.scene.input.KeyCombination$ModifierValue
 */
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
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.tk.quantum.GlassMenuEventHandler;
import com.sun.javafx.tk.quantum.PixelUtils;
import com.sun.prism.Image;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCharacterCombination;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

class GlassSystemMenu
implements TKSystemMenu {
    private List<MenuBase> systemMenus = null;
    private MenuBar glassSystemMenuBar = null;
    private InvalidationListener visibilityListener = observable -> {
        if (this.systemMenus != null) {
            this.setMenus(this.systemMenus);
        }
    };

    GlassSystemMenu() {
    }

    protected void createMenuBar() {
        if (this.glassSystemMenuBar == null) {
            Application application = Application.GetApplication();
            this.glassSystemMenuBar = application.createMenuBar();
            application.installDefaultMenus(this.glassSystemMenuBar);
            if (this.systemMenus != null) {
                this.setMenus(this.systemMenus);
            }
        }
    }

    protected MenuBar getMenuBar() {
        return this.glassSystemMenuBar;
    }

    @Override
    public boolean isSupported() {
        return Application.GetApplication().supportsSystemMenu();
    }

    @Override
    public void setMenus(List<MenuBase> list) {
        this.systemMenus = list;
        if (this.glassSystemMenuBar != null) {
            List<Menu> list2 = this.glassSystemMenuBar.getMenus();
            int n = list2.size();
            for (int i = n - 1; i >= 1; --i) {
                Menu object = list2.get(i);
                this.clearMenu(object);
                this.glassSystemMenuBar.remove(i);
            }
            for (MenuBase menuBase : list) {
                this.addMenu(null, menuBase);
            }
        }
    }

    private void clearMenu(Menu menu) {
        for (int i = menu.getItems().size() - 1; i >= 0; --i) {
            Object object = menu.getItems().get(i);
            if (object instanceof MenuItem) {
                ((MenuItem)object).setCallback(null);
                continue;
            }
            if (!(object instanceof Menu)) continue;
            this.clearMenu((Menu)object);
        }
        menu.setEventHandler(null);
    }

    private void addMenu(Menu menu, MenuBase menuBase) {
        if (menu != null) {
            this.insertMenu(menu, menuBase, menu.getItems().size());
        } else {
            this.insertMenu(menu, menuBase, this.glassSystemMenuBar.getMenus().size());
        }
    }

    private void insertMenu(Menu menu, MenuBase menuBase, int n) {
        Application application = Application.GetApplication();
        Menu menu2 = application.createMenu(this.parseText(menuBase), !menuBase.isDisable());
        menu2.setEventHandler(new GlassMenuEventHandler(menuBase));
        menuBase.visibleProperty().removeListener(this.visibilityListener);
        menuBase.visibleProperty().addListener(this.visibilityListener);
        if (!menuBase.isVisible()) {
            return;
        }
        ObservableList<MenuItemBase> observableList = menuBase.getItemsBase();
        observableList.addListener(change -> {
            while (change.next()) {
                Object object;
                int n;
                int n2 = change.getFrom();
                int n3 = change.getTo();
                List list = change.getRemoved();
                for (n = n2 + list.size() - 1; n >= n2; --n) {
                    object = menu2.getItems();
                    if (n < 0 || object.size() <= n) continue;
                    menu2.remove(n);
                }
                for (n = n2; n < n3; ++n) {
                    object = (MenuItemBase)change.getList().get(n);
                    if (object instanceof MenuBase) {
                        this.insertMenu(menu2, (MenuBase)object, n);
                        continue;
                    }
                    this.insertMenuItem(menu2, (MenuItemBase)object, n);
                }
            }
        });
        for (MenuItemBase menuItemBase : observableList) {
            if (menuItemBase instanceof MenuBase) {
                this.addMenu(menu2, (MenuBase)menuItemBase);
                continue;
            }
            this.addMenuItem(menu2, menuItemBase);
        }
        menu2.setPixels(this.getPixels(menuBase));
        this.setMenuBindings(menu2, menuBase);
        if (menu != null) {
            menu.insert(menu2, n);
        } else {
            this.glassSystemMenuBar.insert(menu2, n);
        }
    }

    private void setMenuBindings(Menu menu, MenuBase menuBase) {
        menuBase.textProperty().addListener(observable -> menu.setTitle(this.parseText(menuBase)));
        menuBase.disableProperty().addListener(observable -> menu.setEnabled(!menuBase.isDisable()));
        menuBase.mnemonicParsingProperty().addListener(observable -> menu.setTitle(this.parseText(menuBase)));
    }

    private void addMenuItem(Menu menu, MenuItemBase menuItemBase) {
        this.insertMenuItem(menu, menuItemBase, menu.getItems().size());
    }

    private void insertMenuItem(final Menu menu, final MenuItemBase menuItemBase, int n) {
        Application application = Application.GetApplication();
        menuItemBase.visibleProperty().removeListener(this.visibilityListener);
        menuItemBase.visibleProperty().addListener(this.visibilityListener);
        if (!menuItemBase.isVisible()) {
            return;
        }
        if (menuItemBase instanceof SeparatorMenuItemBase) {
            if (menuItemBase.isVisible()) {
                menu.insert(MenuItem.Separator, n);
            }
        } else {
            MenuItem.Callback callback = new MenuItem.Callback(){

                @Override
                public void action() {
                    if (menuItemBase instanceof CheckMenuItemBase) {
                        CheckMenuItemBase checkMenuItemBase;
                        checkMenuItemBase.setSelected(!(checkMenuItemBase = (CheckMenuItemBase)menuItemBase).isSelected());
                    } else if (menuItemBase instanceof RadioMenuItemBase) {
                        RadioMenuItemBase radioMenuItemBase = (RadioMenuItemBase)menuItemBase;
                        radioMenuItemBase.setSelected(true);
                    }
                    menuItemBase.fire();
                }

                @Override
                public void validate() {
                    Menu.EventHandler eventHandler = menu.getEventHandler();
                    GlassMenuEventHandler glassMenuEventHandler = (GlassMenuEventHandler)eventHandler;
                    if (glassMenuEventHandler.isMenuOpen()) {
                        return;
                    }
                    menuItemBase.fireValidation();
                }
            };
            MenuItem menuItem = application.createMenuItem(this.parseText(menuItemBase), callback);
            menuItemBase.textProperty().addListener(observable -> menuItem.setTitle(this.parseText(menuItemBase)));
            menuItem.setPixels(this.getPixels(menuItemBase));
            menuItemBase.graphicProperty().addListener(observable -> menuItem.setPixels(this.getPixels(menuItemBase)));
            menuItem.setEnabled(!menuItemBase.isDisable());
            menuItemBase.disableProperty().addListener(observable -> menuItem.setEnabled(!menuItemBase.isDisable()));
            this.setShortcut(menuItem, menuItemBase);
            menuItemBase.acceleratorProperty().addListener(observable -> this.setShortcut(menuItem, menuItemBase));
            menuItemBase.mnemonicParsingProperty().addListener(observable -> menuItem.setTitle(this.parseText(menuItemBase)));
            if (menuItemBase instanceof CheckMenuItemBase) {
                CheckMenuItemBase checkMenuItemBase = (CheckMenuItemBase)menuItemBase;
                menuItem.setChecked(checkMenuItemBase.isSelected());
                checkMenuItemBase.selectedProperty().addListener(observable -> menuItem.setChecked(checkMenuItemBase.isSelected()));
            } else if (menuItemBase instanceof RadioMenuItemBase) {
                RadioMenuItemBase radioMenuItemBase = (RadioMenuItemBase)menuItemBase;
                menuItem.setChecked(radioMenuItemBase.isSelected());
                radioMenuItemBase.selectedProperty().addListener(observable -> menuItem.setChecked(radioMenuItemBase.isSelected()));
            }
            menu.insert(menuItem, n);
        }
    }

    private String parseText(MenuItemBase menuItemBase) {
        String string = menuItemBase.getText();
        if (string == null) {
            return "";
        }
        if (!string.isEmpty() && menuItemBase.isMnemonicParsing()) {
            return string.replaceFirst("_([^_])", "$1");
        }
        return string;
    }

    private Pixels getPixels(MenuItemBase menuItemBase) {
        if (menuItemBase.getGraphic() instanceof ImageView) {
            ImageView imageView = (ImageView)menuItemBase.getGraphic();
            javafx.scene.image.Image image = imageView.getImage();
            if (image == null) {
                return null;
            }
            String string = image.getUrl();
            if (string == null || PixelUtils.supportedFormatType(string)) {
                Image image2 = (Image)Toolkit.getImageAccessor().getPlatformImage(image);
                return image2 == null ? null : PixelUtils.imageToPixels(image2);
            }
        }
        return null;
    }

    private void setShortcut(MenuItem menuItem, MenuItemBase menuItemBase) {
        KeyCombination keyCombination = menuItemBase.getAccelerator();
        if (keyCombination == null) {
            menuItem.setShortcut(0, 0);
        } else if (keyCombination instanceof KeyCodeCombination) {
            KeyCodeCombination keyCodeCombination = (KeyCodeCombination)keyCombination;
            KeyCode keyCode = keyCodeCombination.getCode();
            assert (PlatformUtil.isMac() || PlatformUtil.isLinux());
            int n = this.glassModifiers((KeyCombination)keyCodeCombination);
            if (PlatformUtil.isMac()) {
                int n2 = keyCode.isLetterKey() ? (int)keyCode.getChar().toUpperCase().charAt(0) : keyCode.getCode();
                menuItem.setShortcut(n2, n);
            } else if (PlatformUtil.isLinux()) {
                String string = keyCode.getChar().toLowerCase();
                if ((n & 4) != 0) {
                    menuItem.setShortcut(string.charAt(0), n);
                } else {
                    menuItem.setShortcut(0, 0);
                }
            } else {
                menuItem.setShortcut(0, 0);
            }
        } else if (keyCombination instanceof KeyCharacterCombination) {
            KeyCharacterCombination keyCharacterCombination = (KeyCharacterCombination)keyCombination;
            String string = keyCharacterCombination.getCharacter();
            menuItem.setShortcut(string.charAt(0), this.glassModifiers((KeyCombination)keyCharacterCombination));
        }
    }

    private int glassModifiers(KeyCombination keyCombination) {
        KeyCode keyCode;
        int n;
        int n2 = 0;
        if (keyCombination.getShift() == KeyCombination.ModifierValue.DOWN) {
            ++n2;
        }
        if (keyCombination.getControl() == KeyCombination.ModifierValue.DOWN) {
            n2 += 4;
        }
        if (keyCombination.getAlt() == KeyCombination.ModifierValue.DOWN) {
            n2 += 8;
        }
        if (keyCombination.getShortcut() == KeyCombination.ModifierValue.DOWN) {
            if (PlatformUtil.isLinux()) {
                n2 += 4;
            } else if (PlatformUtil.isMac()) {
                n2 += 16;
            }
        }
        if (keyCombination.getMeta() == KeyCombination.ModifierValue.DOWN) {
            if (PlatformUtil.isLinux()) {
                n2 += 16;
            } else if (PlatformUtil.isMac()) {
                n2 += 16;
            }
        }
        if (keyCombination instanceof KeyCodeCombination && ((n = (keyCode = ((KeyCodeCombination)keyCombination).getCode()).getCode()) >= KeyCode.F1.getCode() && n <= KeyCode.F12.getCode() || n >= KeyCode.F13.getCode() && n <= KeyCode.F24.getCode())) {
            n2 += 2;
        }
        return n2;
    }
}

