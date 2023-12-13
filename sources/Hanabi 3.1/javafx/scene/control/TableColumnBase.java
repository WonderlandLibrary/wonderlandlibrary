package javafx.scene.control;

import com.sun.javafx.beans.IDProperty;
import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.scene.control.ControlAcceleratorSupport;
import com.sun.javafx.scene.control.skin.Utils;
import java.lang.ref.WeakReference;
import java.text.Collator;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.css.Styleable;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Node;

@IDProperty("id")
public abstract class TableColumnBase implements EventTarget, Styleable {
   static final double DEFAULT_WIDTH = 80.0;
   static final double DEFAULT_MIN_WIDTH = 10.0;
   static final double DEFAULT_MAX_WIDTH = 5000.0;
   public static final Comparator DEFAULT_COMPARATOR = (var0, var1) -> {
      if (var0 == null && var1 == null) {
         return 0;
      } else if (var0 == null) {
         return -1;
      } else if (var1 == null) {
         return 1;
      } else if (!(var0 instanceof Comparable) || var0.getClass() != var1.getClass() && !var0.getClass().isAssignableFrom(var1.getClass())) {
         return Collator.getInstance().compare(var0.toString(), var1.toString());
      } else {
         return var0 instanceof String ? Collator.getInstance().compare(var0, var1) : ((Comparable)var0).compareTo(var1);
      }
   };
   final EventHandlerManager eventHandlerManager;
   private StringProperty text;
   private BooleanProperty visible;
   private ReadOnlyObjectWrapper parentColumn;
   private ObjectProperty contextMenu;
   private StringProperty id;
   private StringProperty style;
   private final ObservableList styleClass;
   private ObjectProperty graphic;
   private ObjectProperty sortNode;
   private ReadOnlyDoubleWrapper width;
   private DoubleProperty minWidth;
   private final DoubleProperty prefWidth;
   private DoubleProperty maxWidth;
   private BooleanProperty resizable;
   private BooleanProperty sortable;
   private BooleanProperty reorderable;
   private BooleanProperty fixed;
   private ObjectProperty comparator;
   private BooleanProperty editable;
   private static final Object USER_DATA_KEY = new Object();
   private ObservableMap properties;

   protected TableColumnBase() {
      this("");
   }

   protected TableColumnBase(String var1) {
      this.eventHandlerManager = new EventHandlerManager(this);
      this.text = new SimpleStringProperty(this, "text", "");
      this.visible = new SimpleBooleanProperty(this, "visible", true) {
         protected void invalidated() {
            Iterator var1 = TableColumnBase.this.getColumns().iterator();

            while(var1.hasNext()) {
               TableColumnBase var2 = (TableColumnBase)var1.next();
               var2.setVisible(TableColumnBase.this.isVisible());
            }

         }
      };
      this.styleClass = FXCollections.observableArrayList();
      this.sortNode = new SimpleObjectProperty(this, "sortNode");
      this.width = new ReadOnlyDoubleWrapper(this, "width", 80.0);
      this.prefWidth = new SimpleDoubleProperty(this, "prefWidth", 80.0) {
         protected void invalidated() {
            TableColumnBase.this.impl_setWidth(TableColumnBase.this.getPrefWidth());
         }
      };
      this.maxWidth = new SimpleDoubleProperty(this, "maxWidth", 5000.0) {
         protected void invalidated() {
            TableColumnBase.this.impl_setWidth(TableColumnBase.this.getWidth());
         }
      };
      this.setText(var1);
   }

   public final StringProperty textProperty() {
      return this.text;
   }

   public final void setText(String var1) {
      this.text.set(var1);
   }

   public final String getText() {
      return (String)this.text.get();
   }

   public final void setVisible(boolean var1) {
      this.visibleProperty().set(var1);
   }

   public final boolean isVisible() {
      return this.visible.get();
   }

   public final BooleanProperty visibleProperty() {
      return this.visible;
   }

   void setParentColumn(TableColumnBase var1) {
      this.parentColumnPropertyImpl().set(var1);
   }

   public final TableColumnBase getParentColumn() {
      return this.parentColumn == null ? null : (TableColumnBase)this.parentColumn.get();
   }

   public final ReadOnlyObjectProperty parentColumnProperty() {
      return this.parentColumnPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper parentColumnPropertyImpl() {
      if (this.parentColumn == null) {
         this.parentColumn = new ReadOnlyObjectWrapper(this, "parentColumn");
      }

      return this.parentColumn;
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
                  ControlAcceleratorSupport.removeAcceleratorsFromScene(var1.getItems(), (TableColumnBase)TableColumnBase.this);
               }

               ContextMenu var2 = (ContextMenu)this.get();
               this.contextMenuRef = new WeakReference(var2);
               if (var2 != null) {
                  ControlAcceleratorSupport.addAcceleratorsIntoScene(var2.getItems(), TableColumnBase.this);
               }

            }
         };
      }

      return this.contextMenu;
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
      return this.style == null ? "" : (String)this.style.get();
   }

   public final StringProperty styleProperty() {
      if (this.style == null) {
         this.style = new SimpleStringProperty(this, "style");
      }

      return this.style;
   }

   public ObservableList getStyleClass() {
      return this.styleClass;
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

   public final void setSortNode(Node var1) {
      this.sortNodeProperty().set(var1);
   }

   public final Node getSortNode() {
      return (Node)this.sortNode.get();
   }

   public final ObjectProperty sortNodeProperty() {
      return this.sortNode;
   }

   public final ReadOnlyDoubleProperty widthProperty() {
      return this.width.getReadOnlyProperty();
   }

   public final double getWidth() {
      return this.width.get();
   }

   void setWidth(double var1) {
      this.width.set(var1);
   }

   public final void setMinWidth(double var1) {
      this.minWidthProperty().set(var1);
   }

   public final double getMinWidth() {
      return this.minWidth == null ? 10.0 : this.minWidth.get();
   }

   public final DoubleProperty minWidthProperty() {
      if (this.minWidth == null) {
         this.minWidth = new SimpleDoubleProperty(this, "minWidth", 10.0) {
            protected void invalidated() {
               if (TableColumnBase.this.getMinWidth() < 0.0) {
                  TableColumnBase.this.setMinWidth(0.0);
               }

               TableColumnBase.this.impl_setWidth(TableColumnBase.this.getWidth());
            }
         };
      }

      return this.minWidth;
   }

   public final DoubleProperty prefWidthProperty() {
      return this.prefWidth;
   }

   public final void setPrefWidth(double var1) {
      this.prefWidthProperty().set(var1);
   }

   public final double getPrefWidth() {
      return this.prefWidth.get();
   }

   public final DoubleProperty maxWidthProperty() {
      return this.maxWidth;
   }

   public final void setMaxWidth(double var1) {
      this.maxWidthProperty().set(var1);
   }

   public final double getMaxWidth() {
      return this.maxWidth.get();
   }

   public final BooleanProperty resizableProperty() {
      if (this.resizable == null) {
         this.resizable = new SimpleBooleanProperty(this, "resizable", true);
      }

      return this.resizable;
   }

   public final void setResizable(boolean var1) {
      this.resizableProperty().set(var1);
   }

   public final boolean isResizable() {
      return this.resizable == null ? true : this.resizable.get();
   }

   public final BooleanProperty sortableProperty() {
      if (this.sortable == null) {
         this.sortable = new SimpleBooleanProperty(this, "sortable", true);
      }

      return this.sortable;
   }

   public final void setSortable(boolean var1) {
      this.sortableProperty().set(var1);
   }

   public final boolean isSortable() {
      return this.sortable == null ? true : this.sortable.get();
   }

   /** @deprecated */
   @Deprecated
   public final BooleanProperty impl_reorderableProperty() {
      if (this.reorderable == null) {
         this.reorderable = new SimpleBooleanProperty(this, "reorderable", true);
      }

      return this.reorderable;
   }

   /** @deprecated */
   @Deprecated
   public final void impl_setReorderable(boolean var1) {
      this.impl_reorderableProperty().set(var1);
   }

   /** @deprecated */
   @Deprecated
   public final boolean impl_isReorderable() {
      return this.reorderable == null ? true : this.reorderable.get();
   }

   /** @deprecated */
   @Deprecated
   public final BooleanProperty impl_fixedProperty() {
      if (this.fixed == null) {
         this.fixed = new SimpleBooleanProperty(this, "fixed", false);
      }

      return this.fixed;
   }

   /** @deprecated */
   @Deprecated
   public final void impl_setFixed(boolean var1) {
      this.impl_fixedProperty().set(var1);
   }

   /** @deprecated */
   @Deprecated
   public final boolean impl_isFixed() {
      return this.fixed == null ? false : this.fixed.get();
   }

   public final ObjectProperty comparatorProperty() {
      if (this.comparator == null) {
         this.comparator = new SimpleObjectProperty(this, "comparator", DEFAULT_COMPARATOR);
      }

      return this.comparator;
   }

   public final void setComparator(Comparator var1) {
      this.comparatorProperty().set(var1);
   }

   public final Comparator getComparator() {
      return this.comparator == null ? DEFAULT_COMPARATOR : (Comparator)this.comparator.get();
   }

   public final void setEditable(boolean var1) {
      this.editableProperty().set(var1);
   }

   public final boolean isEditable() {
      return this.editable == null ? true : this.editable.get();
   }

   public final BooleanProperty editableProperty() {
      if (this.editable == null) {
         this.editable = new SimpleBooleanProperty(this, "editable", true);
      }

      return this.editable;
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

   public abstract ObservableList getColumns();

   public final Object getCellData(int var1) {
      ObservableValue var2 = this.getCellObservableValue(var1);
      return var2 == null ? null : var2.getValue();
   }

   public final Object getCellData(Object var1) {
      ObservableValue var2 = this.getCellObservableValue(var1);
      return var2 == null ? null : var2.getValue();
   }

   public abstract ObservableValue getCellObservableValue(int var1);

   public abstract ObservableValue getCellObservableValue(Object var1);

   public EventDispatchChain buildEventDispatchChain(EventDispatchChain var1) {
      return var1.prepend(this.eventHandlerManager);
   }

   public void addEventHandler(EventType var1, EventHandler var2) {
      this.eventHandlerManager.addEventHandler(var1, var2);
   }

   public void removeEventHandler(EventType var1, EventHandler var2) {
      this.eventHandlerManager.removeEventHandler(var1, var2);
   }

   /** @deprecated */
   @Deprecated
   public void impl_setWidth(double var1) {
      this.setWidth(Utils.boundedSize(var1, this.getMinWidth(), this.getMaxWidth()));
   }

   void updateColumnWidths() {
      if (!this.getColumns().isEmpty()) {
         double var1 = 0.0;
         double var3 = 0.0;
         double var5 = 0.0;

         TableColumnBase var8;
         for(Iterator var7 = this.getColumns().iterator(); var7.hasNext(); var5 += var8.getMaxWidth()) {
            var8 = (TableColumnBase)var7.next();
            var8.setParentColumn(this);
            var1 += var8.getMinWidth();
            var3 += var8.getPrefWidth();
         }

         this.setMinWidth(var1);
         this.setPrefWidth(var3);
         this.setMaxWidth(var5);
      }

   }

   public final ObservableSet getPseudoClassStates() {
      return FXCollections.emptyObservableSet();
   }
}
