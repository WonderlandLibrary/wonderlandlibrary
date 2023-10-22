package javafx.scene.control;

import com.sun.javafx.beans.IDProperty;
import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.scene.control.ControlAcceleratorSupport;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
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
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Node;

@DefaultProperty("content")
@IDProperty("id")
public class Tab implements EventTarget, Styleable {
   private StringProperty id;
   private StringProperty style;
   private ReadOnlyBooleanWrapper selected;
   private ReadOnlyObjectWrapper tabPane;
   private final InvalidationListener parentDisabledChangedListener;
   private StringProperty text;
   private ObjectProperty graphic;
   private ObjectProperty content;
   private ObjectProperty contextMenu;
   private BooleanProperty closable;
   public static final EventType SELECTION_CHANGED_EVENT;
   private ObjectProperty onSelectionChanged;
   public static final EventType CLOSED_EVENT;
   private ObjectProperty onClosed;
   private ObjectProperty tooltip;
   private final ObservableList styleClass;
   private BooleanProperty disable;
   private ReadOnlyBooleanWrapper disabled;
   public static final EventType TAB_CLOSE_REQUEST_EVENT;
   private ObjectProperty onCloseRequest;
   private static final Object USER_DATA_KEY;
   private ObservableMap properties;
   private final EventHandlerManager eventHandlerManager;
   private static final String DEFAULT_STYLE_CLASS = "tab";

   public Tab() {
      this((String)null);
   }

   public Tab(String var1) {
      this(var1, (Node)null);
   }

   public Tab(String var1, Node var2) {
      this.parentDisabledChangedListener = (var1x) -> {
         this.updateDisabled();
      };
      this.styleClass = FXCollections.observableArrayList();
      this.eventHandlerManager = new EventHandlerManager(this);
      this.setText(var1);
      this.setContent(var2);
      this.styleClass.addAll("tab");
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

   final void setSelected(boolean var1) {
      this.selectedPropertyImpl().set(var1);
   }

   public final boolean isSelected() {
      return this.selected == null ? false : this.selected.get();
   }

   public final ReadOnlyBooleanProperty selectedProperty() {
      return this.selectedPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyBooleanWrapper selectedPropertyImpl() {
      if (this.selected == null) {
         this.selected = new ReadOnlyBooleanWrapper() {
            protected void invalidated() {
               if (Tab.this.getOnSelectionChanged() != null) {
                  Event.fireEvent(Tab.this, new Event(Tab.SELECTION_CHANGED_EVENT));
               }

            }

            public Object getBean() {
               return Tab.this;
            }

            public String getName() {
               return "selected";
            }
         };
      }

      return this.selected;
   }

   final void setTabPane(TabPane var1) {
      this.tabPanePropertyImpl().set(var1);
   }

   public final TabPane getTabPane() {
      return this.tabPane == null ? null : (TabPane)this.tabPane.get();
   }

   public final ReadOnlyObjectProperty tabPaneProperty() {
      return this.tabPanePropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper tabPanePropertyImpl() {
      if (this.tabPane == null) {
         this.tabPane = new ReadOnlyObjectWrapper(this, "tabPane") {
            private WeakReference oldParent;

            protected void invalidated() {
               if (this.oldParent != null && this.oldParent.get() != null) {
                  ((TabPane)this.oldParent.get()).disabledProperty().removeListener(Tab.this.parentDisabledChangedListener);
               }

               Tab.this.updateDisabled();
               TabPane var1 = (TabPane)this.get();
               if (var1 != null) {
                  var1.disabledProperty().addListener(Tab.this.parentDisabledChangedListener);
               }

               this.oldParent = new WeakReference(var1);
               super.invalidated();
            }
         };
      }

      return this.tabPane;
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

   public final void setContent(Node var1) {
      this.contentProperty().set(var1);
   }

   public final Node getContent() {
      return this.content == null ? null : (Node)this.content.get();
   }

   public final ObjectProperty contentProperty() {
      if (this.content == null) {
         this.content = new SimpleObjectProperty(this, "content");
      }

      return this.content;
   }

   public final void setContextMenu(ContextMenu var1) {
      this.contextMenuProperty().set(var1);
   }

   public final ContextMenu getContextMenu() {
      return this.contextMenu == null ? null : (ContextMenu)this.contextMenu.get();
   }

   public final ObjectProperty contextMenuProperty() {
      if (this.contextMenu == null) {
         this.contextMenu = new SimpleObjectProperty(this, "contextMenu") {
            private WeakReference contextMenuRef;

            protected void invalidated() {
               ContextMenu var1 = this.contextMenuRef == null ? null : (ContextMenu)this.contextMenuRef.get();
               if (var1 != null) {
                  ControlAcceleratorSupport.removeAcceleratorsFromScene(var1.getItems(), (Tab)Tab.this);
               }

               ContextMenu var2 = (ContextMenu)this.get();
               this.contextMenuRef = new WeakReference(var2);
               if (var2 != null) {
                  ControlAcceleratorSupport.addAcceleratorsIntoScene(var2.getItems(), Tab.this);
               }

            }
         };
      }

      return this.contextMenu;
   }

   public final void setClosable(boolean var1) {
      this.closableProperty().set(var1);
   }

   public final boolean isClosable() {
      return this.closable == null ? true : this.closable.get();
   }

   public final BooleanProperty closableProperty() {
      if (this.closable == null) {
         this.closable = new SimpleBooleanProperty(this, "closable", true);
      }

      return this.closable;
   }

   public final void setOnSelectionChanged(EventHandler var1) {
      this.onSelectionChangedProperty().set(var1);
   }

   public final EventHandler getOnSelectionChanged() {
      return this.onSelectionChanged == null ? null : (EventHandler)this.onSelectionChanged.get();
   }

   public final ObjectProperty onSelectionChangedProperty() {
      if (this.onSelectionChanged == null) {
         this.onSelectionChanged = new ObjectPropertyBase() {
            protected void invalidated() {
               Tab.this.setEventHandler(Tab.SELECTION_CHANGED_EVENT, (EventHandler)this.get());
            }

            public Object getBean() {
               return Tab.this;
            }

            public String getName() {
               return "onSelectionChanged";
            }
         };
      }

      return this.onSelectionChanged;
   }

   public final void setOnClosed(EventHandler var1) {
      this.onClosedProperty().set(var1);
   }

   public final EventHandler getOnClosed() {
      return this.onClosed == null ? null : (EventHandler)this.onClosed.get();
   }

   public final ObjectProperty onClosedProperty() {
      if (this.onClosed == null) {
         this.onClosed = new ObjectPropertyBase() {
            protected void invalidated() {
               Tab.this.setEventHandler(Tab.CLOSED_EVENT, (EventHandler)this.get());
            }

            public Object getBean() {
               return Tab.this;
            }

            public String getName() {
               return "onClosed";
            }
         };
      }

      return this.onClosed;
   }

   public final void setTooltip(Tooltip var1) {
      this.tooltipProperty().setValue(var1);
   }

   public final Tooltip getTooltip() {
      return this.tooltip == null ? null : (Tooltip)this.tooltip.getValue();
   }

   public final ObjectProperty tooltipProperty() {
      if (this.tooltip == null) {
         this.tooltip = new SimpleObjectProperty(this, "tooltip");
      }

      return this.tooltip;
   }

   public final void setDisable(boolean var1) {
      this.disableProperty().set(var1);
   }

   public final boolean isDisable() {
      return this.disable == null ? false : this.disable.get();
   }

   public final BooleanProperty disableProperty() {
      if (this.disable == null) {
         this.disable = new BooleanPropertyBase(false) {
            protected void invalidated() {
               Tab.this.updateDisabled();
            }

            public Object getBean() {
               return Tab.this;
            }

            public String getName() {
               return "disable";
            }
         };
      }

      return this.disable;
   }

   private final void setDisabled(boolean var1) {
      this.disabledPropertyImpl().set(var1);
   }

   public final boolean isDisabled() {
      return this.disabled == null ? false : this.disabled.get();
   }

   public final ReadOnlyBooleanProperty disabledProperty() {
      return this.disabledPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyBooleanWrapper disabledPropertyImpl() {
      if (this.disabled == null) {
         this.disabled = new ReadOnlyBooleanWrapper() {
            public Object getBean() {
               return Tab.this;
            }

            public String getName() {
               return "disabled";
            }
         };
      }

      return this.disabled;
   }

   private void updateDisabled() {
      boolean var1 = this.isDisable() || this.getTabPane() != null && this.getTabPane().isDisabled();
      this.setDisabled(var1);
      Node var2 = this.getContent();
      if (var2 != null) {
         var2.setDisable(var1);
      }

   }

   public final ObjectProperty onCloseRequestProperty() {
      if (this.onCloseRequest == null) {
         this.onCloseRequest = new ObjectPropertyBase() {
            protected void invalidated() {
               Tab.this.setEventHandler(Tab.TAB_CLOSE_REQUEST_EVENT, (EventHandler)this.get());
            }

            public Object getBean() {
               return Tab.this;
            }

            public String getName() {
               return "onCloseRequest";
            }
         };
      }

      return this.onCloseRequest;
   }

   public EventHandler getOnCloseRequest() {
      return this.onCloseRequest == null ? null : (EventHandler)this.onCloseRequest.get();
   }

   public void setOnCloseRequest(EventHandler var1) {
      this.onCloseRequestProperty().set(var1);
   }

   public final ObservableMap getProperties() {
      if (this.properties == null) {
         this.properties = FXCollections.observableMap(new HashMap());
      }

      return this.properties;
   }

   public boolean hasProperties() {
      return this.properties != null && !this.properties.isEmpty();
   }

   public void setUserData(Object var1) {
      this.getProperties().put(USER_DATA_KEY, var1);
   }

   public Object getUserData() {
      return this.getProperties().get(USER_DATA_KEY);
   }

   public ObservableList getStyleClass() {
      return this.styleClass;
   }

   /** @deprecated */
   public EventDispatchChain buildEventDispatchChain(EventDispatchChain var1) {
      return var1.prepend(this.eventHandlerManager);
   }

   /** @deprecated */
   protected void setEventHandler(EventType var1, EventHandler var2) {
      this.eventHandlerManager.setEventHandler(var1, var2);
   }

   Node lookup(String var1) {
      if (var1 == null) {
         return null;
      } else {
         Node var2 = null;
         if (this.getContent() != null) {
            var2 = this.getContent().lookup(var1);
         }

         if (var2 == null && this.getGraphic() != null) {
            var2 = this.getGraphic().lookup(var1);
         }

         return var2;
      }
   }

   List lookupAll(String var1) {
      ArrayList var2 = new ArrayList();
      Set var3;
      if (this.getContent() != null) {
         var3 = this.getContent().lookupAll(var1);
         if (!var3.isEmpty()) {
            var2.addAll(var3);
         }
      }

      if (this.getGraphic() != null) {
         var3 = this.getGraphic().lookupAll(var1);
         if (!var3.isEmpty()) {
            var2.addAll(var3);
         }
      }

      return var2;
   }

   public String getTypeSelector() {
      return "Tab";
   }

   public Styleable getStyleableParent() {
      return this.getTabPane();
   }

   public final ObservableSet getPseudoClassStates() {
      return FXCollections.emptyObservableSet();
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   public static List getClassCssMetaData() {
      return Collections.emptyList();
   }

   static {
      SELECTION_CHANGED_EVENT = new EventType(Event.ANY, "SELECTION_CHANGED_EVENT");
      CLOSED_EVENT = new EventType(Event.ANY, "TAB_CLOSED");
      TAB_CLOSE_REQUEST_EVENT = new EventType(Event.ANY, "TAB_CLOSE_REQUEST_EVENT");
      USER_DATA_KEY = new Object();
   }
}
