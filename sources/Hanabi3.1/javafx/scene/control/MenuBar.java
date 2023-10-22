package javafx.scene.control;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.scene.control.skin.MenuBarSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.StyleOrigin;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleRole;

@DefaultProperty("menus")
public class MenuBar extends Control {
   private ObservableList menus;
   private BooleanProperty useSystemMenuBar;
   private static final String DEFAULT_STYLE_CLASS = "menu-bar";

   public MenuBar() {
      this((Menu[])null);
   }

   public MenuBar(Menu... var1) {
      this.menus = FXCollections.observableArrayList();
      this.getStyleClass().setAll((Object[])("menu-bar"));
      this.setAccessibleRole(AccessibleRole.MENU_BAR);
      if (var1 != null) {
         this.getMenus().addAll(var1);
      }

      ((StyleableProperty)this.focusTraversableProperty()).applyStyle((StyleOrigin)null, Boolean.FALSE);
   }

   public final BooleanProperty useSystemMenuBarProperty() {
      if (this.useSystemMenuBar == null) {
         this.useSystemMenuBar = new StyleableBooleanProperty() {
            public CssMetaData getCssMetaData() {
               return MenuBar.StyleableProperties.USE_SYSTEM_MENU_BAR;
            }

            public Object getBean() {
               return MenuBar.this;
            }

            public String getName() {
               return "useSystemMenuBar";
            }
         };
      }

      return this.useSystemMenuBar;
   }

   public final void setUseSystemMenuBar(boolean var1) {
      this.useSystemMenuBarProperty().setValue(var1);
   }

   public final boolean isUseSystemMenuBar() {
      return this.useSystemMenuBar == null ? false : this.useSystemMenuBar.getValue();
   }

   public final ObservableList getMenus() {
      return this.menus;
   }

   protected Skin createDefaultSkin() {
      return new MenuBarSkin(this);
   }

   public static List getClassCssMetaData() {
      return MenuBar.StyleableProperties.STYLEABLES;
   }

   public List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   /** @deprecated */
   @Deprecated
   protected Boolean impl_cssGetFocusTraversableInitialValue() {
      return Boolean.FALSE;
   }

   private static class StyleableProperties {
      private static final CssMetaData USE_SYSTEM_MENU_BAR = new CssMetaData("-fx-use-system-menu-bar", BooleanConverter.getInstance(), false) {
         public boolean isSettable(MenuBar var1) {
            return var1.useSystemMenuBar == null || !var1.useSystemMenuBar.isBound();
         }

         public StyleableProperty getStyleableProperty(MenuBar var1) {
            return (StyleableProperty)var1.useSystemMenuBarProperty();
         }
      };
      private static final List STYLEABLES;

      static {
         ArrayList var0 = new ArrayList(Control.getClassCssMetaData());
         var0.add(USE_SYSTEM_MENU_BAR);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
