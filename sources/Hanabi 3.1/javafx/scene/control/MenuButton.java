package javafx.scene.control;

import com.sun.javafx.scene.control.skin.MenuButtonSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Side;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;

public class MenuButton extends ButtonBase {
   public static final EventType ON_SHOWING;
   public static final EventType ON_SHOWN;
   public static final EventType ON_HIDING;
   public static final EventType ON_HIDDEN;
   private final ObservableList items;
   private ReadOnlyBooleanWrapper showing;
   private ObjectProperty popupSide;
   private static final String DEFAULT_STYLE_CLASS = "menu-button";
   private static final PseudoClass PSEUDO_CLASS_OPENVERTICALLY;
   private static final PseudoClass PSEUDO_CLASS_SHOWING;

   public MenuButton() {
      this((String)null, (Node)null);
   }

   public MenuButton(String var1) {
      this(var1, (Node)null);
   }

   public MenuButton(String var1, Node var2) {
      this(var1, var2, (MenuItem[])null);
   }

   public MenuButton(String var1, Node var2, MenuItem... var3) {
      this.items = FXCollections.observableArrayList();
      this.showing = new ReadOnlyBooleanWrapper(this, "showing", false) {
         protected void invalidated() {
            MenuButton.this.pseudoClassStateChanged(MenuButton.PSEUDO_CLASS_SHOWING, this.get());
            super.invalidated();
         }
      };
      if (var1 != null) {
         this.setText(var1);
      }

      if (var2 != null) {
         this.setGraphic(var2);
      }

      if (var3 != null) {
         this.getItems().addAll(var3);
      }

      this.getStyleClass().setAll((Object[])("menu-button"));
      this.setAccessibleRole(AccessibleRole.MENU_BUTTON);
      this.setMnemonicParsing(true);
      this.pseudoClassStateChanged(PSEUDO_CLASS_OPENVERTICALLY, true);
   }

   public final ObservableList getItems() {
      return this.items;
   }

   private void setShowing(boolean var1) {
      Event.fireEvent(this, var1 ? new Event(ComboBoxBase.ON_SHOWING) : new Event(ComboBoxBase.ON_HIDING));
      this.showing.set(var1);
      Event.fireEvent(this, var1 ? new Event(ComboBoxBase.ON_SHOWN) : new Event(ComboBoxBase.ON_HIDDEN));
   }

   public final boolean isShowing() {
      return this.showing.get();
   }

   public final ReadOnlyBooleanProperty showingProperty() {
      return this.showing.getReadOnlyProperty();
   }

   public final void setPopupSide(Side var1) {
      this.popupSideProperty().set(var1);
   }

   public final Side getPopupSide() {
      return this.popupSide == null ? Side.BOTTOM : (Side)this.popupSide.get();
   }

   public final ObjectProperty popupSideProperty() {
      if (this.popupSide == null) {
         this.popupSide = new ObjectPropertyBase(Side.BOTTOM) {
            protected void invalidated() {
               Side var1 = (Side)this.get();
               boolean var2 = var1 == Side.TOP || var1 == Side.BOTTOM;
               MenuButton.this.pseudoClassStateChanged(MenuButton.PSEUDO_CLASS_OPENVERTICALLY, var2);
            }

            public Object getBean() {
               return MenuButton.this;
            }

            public String getName() {
               return "popupSide";
            }
         };
      }

      return this.popupSide;
   }

   public void show() {
      if (!this.isDisabled() && !this.showing.isBound()) {
         this.setShowing(true);
      }

   }

   public void hide() {
      if (!this.showing.isBound()) {
         this.setShowing(false);
      }

   }

   public void fire() {
      if (!this.isDisabled()) {
         this.fireEvent(new ActionEvent());
      }

   }

   protected Skin createDefaultSkin() {
      return new MenuButtonSkin(this);
   }

   public void executeAccessibleAction(AccessibleAction var1, Object... var2) {
      switch (var1) {
         case FIRE:
            if (this.isShowing()) {
               this.hide();
            } else {
               this.show();
            }
            break;
         default:
            super.executeAccessibleAction(var1);
      }

   }

   static {
      ON_SHOWING = new EventType(Event.ANY, "MENU_BUTTON_ON_SHOWING");
      ON_SHOWN = new EventType(Event.ANY, "MENU_BUTTON_ON_SHOWN");
      ON_HIDING = new EventType(Event.ANY, "MENU_BUTTON_ON_HIDING");
      ON_HIDDEN = new EventType(Event.ANY, "MENU_BUTTON_ON_HIDDEN");
      PSEUDO_CLASS_OPENVERTICALLY = PseudoClass.getPseudoClass("openvertically");
      PSEUDO_CLASS_SHOWING = PseudoClass.getPseudoClass("showing");
   }
}
