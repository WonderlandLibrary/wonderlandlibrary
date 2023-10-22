package javafx.scene.control;

import com.sun.javafx.beans.IDProperty;
import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.scene.control.skin.ContextMenuContent;
import com.sun.javafx.scene.control.skin.ContextMenuSkin;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.css.Styleable;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;

@IDProperty("id")
public class MenuItem implements EventTarget, Styleable {
   private final ObservableList styleClass;
   final EventHandlerManager eventHandlerManager;
   private Object userData;
   private ObservableMap properties;
   private StringProperty id;
   private StringProperty style;
   private ReadOnlyObjectWrapper parentMenu;
   private ReadOnlyObjectWrapper parentPopup;
   private StringProperty text;
   private ObjectProperty graphic;
   private ObjectProperty onAction;
   public static final EventType MENU_VALIDATION_EVENT;
   private ObjectProperty onMenuValidation;
   private BooleanProperty disable;
   private BooleanProperty visible;
   private ObjectProperty accelerator;
   private BooleanProperty mnemonicParsing;
   private static final String DEFAULT_STYLE_CLASS = "menu-item";

   public MenuItem() {
      this((String)null, (Node)null);
   }

   public MenuItem(String var1) {
      this(var1, (Node)null);
   }

   public MenuItem(String var1, Node var2) {
      this.styleClass = FXCollections.observableArrayList();
      this.eventHandlerManager = new EventHandlerManager(this);
      this.setText(var1);
      this.setGraphic(var2);
      this.styleClass.add("menu-item");
   }

   public final void setId(String var1) {
      this.idProperty().set(var1);
   }

   public final String getId() {
      return this.id == null ? null : (String)this.id.get();
   }

   public final StringProperty idProperty() {
      if (this.id == null) {
         this.id = new SimpleStringProperty(this, "id");
      }

      return this.id;
   }

   public final void setStyle(String var1) {
      this.styleProperty().set(var1);
   }

   public final String getStyle() {
      return this.style == null ? null : (String)this.style.get();
   }

   public final StringProperty styleProperty() {
      if (this.style == null) {
         this.style = new SimpleStringProperty(this, "style");
      }

      return this.style;
   }

   protected final void setParentMenu(Menu var1) {
      this.parentMenuPropertyImpl().set(var1);
   }

   public final Menu getParentMenu() {
      return this.parentMenu == null ? null : (Menu)this.parentMenu.get();
   }

   public final ReadOnlyObjectProperty parentMenuProperty() {
      return this.parentMenuPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper parentMenuPropertyImpl() {
      if (this.parentMenu == null) {
         this.parentMenu = new ReadOnlyObjectWrapper(this, "parentMenu");
      }

      return this.parentMenu;
   }

   protected final void setParentPopup(ContextMenu var1) {
      this.parentPopupPropertyImpl().set(var1);
   }

   public final ContextMenu getParentPopup() {
      return this.parentPopup == null ? null : (ContextMenu)this.parentPopup.get();
   }

   public final ReadOnlyObjectProperty parentPopupProperty() {
      return this.parentPopupPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper parentPopupPropertyImpl() {
      if (this.parentPopup == null) {
         this.parentPopup = new ReadOnlyObjectWrapper(this, "parentPopup");
      }

      return this.parentPopup;
   }

   public final void setText(String var1) {
      this.textProperty().set(var1);
   }

   public final String getText() {
      return this.text == null ? null : (String)this.text.get();
   }

   public final StringProperty textProperty() {
      if (this.text == null) {
         this.text = new SimpleStringProperty(this, "text");
      }

      return this.text;
   }

   public final void setGraphic(Node var1) {
      this.graphicProperty().set(var1);
   }

   public final Node getGraphic() {
      return this.graphic == null ? null : (Node)this.graphic.get();
   }

   public final ObjectProperty graphicProperty() {
      if (this.graphic == null) {
         this.graphic = new SimpleObjectProperty(this, "graphic");
      }

      return this.graphic;
   }

   public final void setOnAction(EventHandler var1) {
      this.onActionProperty().set(var1);
   }

   public final EventHandler getOnAction() {
      return this.onAction == null ? null : (EventHandler)this.onAction.get();
   }

   public final ObjectProperty onActionProperty() {
      if (this.onAction == null) {
         this.onAction = new ObjectPropertyBase() {
            protected void invalidated() {
               MenuItem.this.eventHandlerManager.setEventHandler(ActionEvent.ACTION, (EventHandler)this.get());
            }

            public Object getBean() {
               return MenuItem.this;
            }

            public String getName() {
               return "onAction";
            }
         };
      }

      return this.onAction;
   }

   public final void setOnMenuValidation(EventHandler var1) {
      this.onMenuValidationProperty().set(var1);
   }

   public final EventHandler getOnMenuValidation() {
      return this.onMenuValidation == null ? null : (EventHandler)this.onMenuValidation.get();
   }

   public final ObjectProperty onMenuValidationProperty() {
      if (this.onMenuValidation == null) {
         this.onMenuValidation = new ObjectPropertyBase() {
            protected void invalidated() {
               MenuItem.this.eventHandlerManager.setEventHandler(MenuItem.MENU_VALIDATION_EVENT, (EventHandler)this.get());
            }

            public Object getBean() {
               return MenuItem.this;
            }

            public String getName() {
               return "onMenuValidation";
            }
         };
      }

      return this.onMenuValidation;
   }

   public final void setDisable(boolean var1) {
      this.disableProperty().set(var1);
   }

   public final boolean isDisable() {
      return this.disable == null ? false : this.disable.get();
   }

   public final BooleanProperty disableProperty() {
      if (this.disable == null) {
         this.disable = new SimpleBooleanProperty(this, "disable");
      }

      return this.disable;
   }

   public final void setVisible(boolean var1) {
      this.visibleProperty().set(var1);
   }

   public final boolean isVisible() {
      return this.visible == null ? true : this.visible.get();
   }

   public final BooleanProperty visibleProperty() {
      if (this.visible == null) {
         this.visible = new SimpleBooleanProperty(this, "visible", true);
      }

      return this.visible;
   }

   public final void setAccelerator(KeyCombination var1) {
      this.acceleratorProperty().set(var1);
   }

   public final KeyCombination getAccelerator() {
      return this.accelerator == null ? null : (KeyCombination)this.accelerator.get();
   }

   public final ObjectProperty acceleratorProperty() {
      if (this.accelerator == null) {
         this.accelerator = new SimpleObjectProperty(this, "accelerator");
      }

      return this.accelerator;
   }

   public final void setMnemonicParsing(boolean var1) {
      this.mnemonicParsingProperty().set(var1);
   }

   public final boolean isMnemonicParsing() {
      return this.mnemonicParsing == null ? true : this.mnemonicParsing.get();
   }

   public final BooleanProperty mnemonicParsingProperty() {
      if (this.mnemonicParsing == null) {
         this.mnemonicParsing = new SimpleBooleanProperty(this, "mnemonicParsing", true);
      }

      return this.mnemonicParsing;
   }

   public ObservableList getStyleClass() {
      return this.styleClass;
   }

   public void fire() {
      Event.fireEvent(this, new ActionEvent(this, this));
   }

   public void addEventHandler(EventType var1, EventHandler var2) {
      this.eventHandlerManager.addEventHandler(var1, var2);
   }

   public void removeEventHandler(EventType var1, EventHandler var2) {
      this.eventHandlerManager.removeEventHandler(var1, var2);
   }

   public EventDispatchChain buildEventDispatchChain(EventDispatchChain var1) {
      if (this.getParentPopup() != null) {
         this.getParentPopup().buildEventDispatchChain(var1);
      }

      if (this.getParentMenu() != null) {
         this.getParentMenu().buildEventDispatchChain(var1);
      }

      return var1.prepend(this.eventHandlerManager);
   }

   public Object getUserData() {
      return this.userData;
   }

   public void setUserData(Object var1) {
      this.userData = var1;
   }

   public ObservableMap getProperties() {
      if (this.properties == null) {
         this.properties = FXCollections.observableMap(new HashMap());
      }

      return this.properties;
   }

   public String getTypeSelector() {
      return "MenuItem";
   }

   public Styleable getStyleableParent() {
      return (Styleable)(this.getParentMenu() == null ? this.getParentPopup() : this.getParentMenu());
   }

   public final ObservableSet getPseudoClassStates() {
      return FXCollections.emptyObservableSet();
   }

   public List getCssMetaData() {
      return Collections.emptyList();
   }

   /** @deprecated */
   @Deprecated
   public Node impl_styleableGetNode() {
      ContextMenu var1 = this.getParentPopup();
      if (var1 != null && var1.getSkin() instanceof ContextMenuSkin) {
         ContextMenuSkin var2 = (ContextMenuSkin)var1.getSkin();
         if (!(var2.getNode() instanceof ContextMenuContent)) {
            return null;
         } else {
            ContextMenuContent var3 = (ContextMenuContent)var2.getNode();
            VBox var4 = var3.getItemsContainer();
            MenuItem var5 = this;
            ObservableList var6 = var4.getChildrenUnmodifiable();

            for(int var7 = 0; var7 < var6.size(); ++var7) {
               if (var6.get(var7) instanceof ContextMenuContent.MenuItemContainer) {
                  ContextMenuContent.MenuItemContainer var8 = (ContextMenuContent.MenuItemContainer)var6.get(var7);
                  if (var5.equals(var8.getItem())) {
                     return var8;
                  }
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder(this.getClass().getSimpleName());
      boolean var2 = this.id != null && !"".equals(this.getId());
      boolean var3 = !this.getStyleClass().isEmpty();
      if (!var2) {
         var1.append('@');
         var1.append(Integer.toHexString(this.hashCode()));
      } else {
         var1.append("[id=");
         var1.append(this.getId());
         if (!var3) {
            var1.append("]");
         }
      }

      if (var3) {
         if (!var2) {
            var1.append('[');
         } else {
            var1.append(", ");
         }

         var1.append("styleClass=");
         var1.append(this.getStyleClass());
         var1.append("]");
      }

      return var1.toString();
   }

   static {
      MENU_VALIDATION_EVENT = new EventType(Event.ANY, "MENU_VALIDATION_EVENT");
   }
}
