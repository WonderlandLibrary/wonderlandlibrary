package javafx.scene.control;

import com.sun.javafx.collections.TrackableObservableList;
import com.sun.javafx.scene.control.Logging;
import java.util.Iterator;
import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;

@DefaultProperty("items")
public class Menu extends MenuItem {
   public static final EventType ON_SHOWING;
   public static final EventType ON_SHOWN;
   public static final EventType ON_HIDING;
   public static final EventType ON_HIDDEN;
   private ReadOnlyBooleanWrapper showing;
   private ObjectProperty onShowing;
   private ObjectProperty onShown;
   private ObjectProperty onHiding;
   private ObjectProperty onHidden;
   private final ObservableList items;
   private static final String DEFAULT_STYLE_CLASS = "menu";
   private static final String STYLE_CLASS_SHOWING = "showing";

   public Menu() {
      this("");
   }

   public Menu(String var1) {
      this(var1, (Node)null);
   }

   public Menu(String var1, Node var2) {
      this(var1, var2, (MenuItem[])null);
   }

   public Menu(String var1, Node var2, MenuItem... var3) {
      super(var1, var2);
      this.onShowing = new ObjectPropertyBase() {
         protected void invalidated() {
            Menu.this.eventHandlerManager.setEventHandler(Menu.ON_SHOWING, (EventHandler)this.get());
         }

         public Object getBean() {
            return Menu.this;
         }

         public String getName() {
            return "onShowing";
         }
      };
      this.onShown = new ObjectPropertyBase() {
         protected void invalidated() {
            Menu.this.eventHandlerManager.setEventHandler(Menu.ON_SHOWN, (EventHandler)this.get());
         }

         public Object getBean() {
            return Menu.this;
         }

         public String getName() {
            return "onShown";
         }
      };
      this.onHiding = new ObjectPropertyBase() {
         protected void invalidated() {
            Menu.this.eventHandlerManager.setEventHandler(Menu.ON_HIDING, (EventHandler)this.get());
         }

         public Object getBean() {
            return Menu.this;
         }

         public String getName() {
            return "onHiding";
         }
      };
      this.onHidden = new ObjectPropertyBase() {
         protected void invalidated() {
            Menu.this.eventHandlerManager.setEventHandler(Menu.ON_HIDDEN, (EventHandler)this.get());
         }

         public Object getBean() {
            return Menu.this;
         }

         public String getName() {
            return "onHidden";
         }
      };
      this.items = new TrackableObservableList() {
         protected void onChanged(ListChangeListener.Change var1) {
            while(var1.next()) {
               Iterator var2 = var1.getRemoved().iterator();

               MenuItem var3;
               while(var2.hasNext()) {
                  var3 = (MenuItem)var2.next();
                  var3.setParentMenu((Menu)null);
                  var3.setParentPopup((ContextMenu)null);
               }

               var2 = var1.getAddedSubList().iterator();

               while(var2.hasNext()) {
                  var3 = (MenuItem)var2.next();
                  if (var3.getParentMenu() != null) {
                     Logging.getControlsLogger().warning("Adding MenuItem " + var3.getText() + " that has already been added to " + var3.getParentMenu().getText());
                     var3.getParentMenu().getItems().remove(var3);
                  }

                  var3.setParentMenu(Menu.this);
                  var3.setParentPopup(Menu.this.getParentPopup());
               }
            }

            if (Menu.this.getItems().size() == 0 && Menu.this.isShowing()) {
               Menu.this.showingPropertyImpl().set(false);
            }

         }
      };
      this.getStyleClass().add("menu");
      if (var3 != null) {
         this.getItems().addAll(var3);
      }

      this.parentPopupProperty().addListener((var1x) -> {
         for(int var2 = 0; var2 < this.getItems().size(); ++var2) {
            MenuItem var3 = (MenuItem)this.getItems().get(var2);
            var3.setParentPopup(this.getParentPopup());
         }

      });
   }

   private void setShowing(boolean var1) {
      if (this.getItems().size() != 0 && (!var1 || !this.isShowing())) {
         if (var1) {
            if (this.getOnMenuValidation() != null) {
               Event.fireEvent(this, new Event(MENU_VALIDATION_EVENT));
               Iterator var2 = this.getItems().iterator();

               while(var2.hasNext()) {
                  MenuItem var3 = (MenuItem)var2.next();
                  if (!(var3 instanceof Menu) && var3.getOnMenuValidation() != null) {
                     Event.fireEvent(var3, new Event(MenuItem.MENU_VALIDATION_EVENT));
                  }
               }
            }

            Event.fireEvent(this, new Event(ON_SHOWING));
         } else {
            Event.fireEvent(this, new Event(ON_HIDING));
         }

         this.showingPropertyImpl().set(var1);
         Event.fireEvent(this, var1 ? new Event(ON_SHOWN) : new Event(ON_HIDDEN));
      }
   }

   public final boolean isShowing() {
      return this.showing == null ? false : this.showing.get();
   }

   public final ReadOnlyBooleanProperty showingProperty() {
      return this.showingPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyBooleanWrapper showingPropertyImpl() {
      if (this.showing == null) {
         this.showing = new ReadOnlyBooleanWrapper() {
            protected void invalidated() {
               this.get();
               if (Menu.this.isShowing()) {
                  Menu.this.getStyleClass().add("showing");
               } else {
                  Menu.this.getStyleClass().remove("showing");
               }

            }

            public Object getBean() {
               return Menu.this;
            }

            public String getName() {
               return "showing";
            }
         };
      }

      return this.showing;
   }

   public final ObjectProperty onShowingProperty() {
      return this.onShowing;
   }

   public final void setOnShowing(EventHandler var1) {
      this.onShowingProperty().set(var1);
   }

   public final EventHandler getOnShowing() {
      return (EventHandler)this.onShowingProperty().get();
   }

   public final ObjectProperty onShownProperty() {
      return this.onShown;
   }

   public final void setOnShown(EventHandler var1) {
      this.onShownProperty().set(var1);
   }

   public final EventHandler getOnShown() {
      return (EventHandler)this.onShownProperty().get();
   }

   public final ObjectProperty onHidingProperty() {
      return this.onHiding;
   }

   public final void setOnHiding(EventHandler var1) {
      this.onHidingProperty().set(var1);
   }

   public final EventHandler getOnHiding() {
      return (EventHandler)this.onHidingProperty().get();
   }

   public final ObjectProperty onHiddenProperty() {
      return this.onHidden;
   }

   public final void setOnHidden(EventHandler var1) {
      this.onHiddenProperty().set(var1);
   }

   public final EventHandler getOnHidden() {
      return (EventHandler)this.onHiddenProperty().get();
   }

   public final ObservableList getItems() {
      return this.items;
   }

   public void show() {
      if (!this.isDisable()) {
         this.setShowing(true);
      }
   }

   public void hide() {
      if (this.isShowing()) {
         Iterator var1 = this.getItems().iterator();

         while(var1.hasNext()) {
            MenuItem var2 = (MenuItem)var1.next();
            if (var2 instanceof Menu) {
               Menu var3 = (Menu)var2;
               var3.hide();
            }
         }

         this.setShowing(false);
      }
   }

   public void addEventHandler(EventType var1, EventHandler var2) {
      this.eventHandlerManager.addEventHandler(var1, var2);
   }

   public void removeEventHandler(EventType var1, EventHandler var2) {
      this.eventHandlerManager.removeEventHandler(var1, var2);
   }

   public EventDispatchChain buildEventDispatchChain(EventDispatchChain var1) {
      return var1.prepend(this.eventHandlerManager);
   }

   static {
      ON_SHOWING = new EventType(Event.ANY, "MENU_ON_SHOWING");
      ON_SHOWN = new EventType(Event.ANY, "MENU_ON_SHOWN");
      ON_HIDING = new EventType(Event.ANY, "MENU_ON_HIDING");
      ON_HIDDEN = new EventType(Event.ANY, "MENU_ON_HIDDEN");
   }
}
