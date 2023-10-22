package javafx.scene.control;

import com.sun.javafx.scene.control.skin.NestedTableColumnHeader;
import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.WeakListChangeListener;
import javafx.css.Styleable;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.util.Callback;

public class TableColumn extends TableColumnBase implements EventTarget {
   private static final EventType EDIT_ANY_EVENT;
   private static final EventType EDIT_START_EVENT;
   private static final EventType EDIT_CANCEL_EVENT;
   private static final EventType EDIT_COMMIT_EVENT;
   public static final Callback DEFAULT_CELL_FACTORY;
   private EventHandler DEFAULT_EDIT_COMMIT_HANDLER;
   private ListChangeListener columnsListener;
   private WeakListChangeListener weakColumnsListener;
   private final ObservableList columns;
   private ReadOnlyObjectWrapper tableView;
   private ObjectProperty cellValueFactory;
   private final ObjectProperty cellFactory;
   private ObjectProperty sortType;
   private ObjectProperty onEditStart;
   private ObjectProperty onEditCommit;
   private ObjectProperty onEditCancel;
   private static final String DEFAULT_STYLE_CLASS = "table-column";

   public static EventType editAnyEvent() {
      return EDIT_ANY_EVENT;
   }

   public static EventType editStartEvent() {
      return EDIT_START_EVENT;
   }

   public static EventType editCancelEvent() {
      return EDIT_CANCEL_EVENT;
   }

   public static EventType editCommitEvent() {
      return EDIT_COMMIT_EVENT;
   }

   public TableColumn() {
      this.DEFAULT_EDIT_COMMIT_HANDLER = (var1) -> {
         int var2 = var1.getTablePosition().getRow();
         ObservableList var3 = var1.getTableView().getItems();
         if (var3 != null && var2 >= 0 && var2 < var3.size()) {
            Object var4 = var3.get(var2);
            ObservableValue var5 = this.getCellObservableValue(var4);
            if (var5 instanceof WritableValue) {
               ((WritableValue)var5).setValue(var1.getNewValue());
            }

         }
      };
      this.columnsListener = (var1) -> {
         while(var1.next()) {
            Iterator var2 = var1.getRemoved().iterator();

            TableColumn var3;
            while(var2.hasNext()) {
               var3 = (TableColumn)var2.next();
               if (!this.getColumns().contains(var3)) {
                  var3.setTableView((TableView)null);
                  var3.setParentColumn((TableColumnBase)null);
               }
            }

            var2 = var1.getAddedSubList().iterator();

            while(var2.hasNext()) {
               var3 = (TableColumn)var2.next();
               var3.setTableView(this.getTableView());
            }

            this.updateColumnWidths();
         }

      };
      this.weakColumnsListener = new WeakListChangeListener(this.columnsListener);
      this.columns = FXCollections.observableArrayList();
      this.tableView = new ReadOnlyObjectWrapper(this, "tableView");
      this.cellFactory = new SimpleObjectProperty(this, "cellFactory", DEFAULT_CELL_FACTORY) {
         protected void invalidated() {
            TableView var1 = TableColumn.this.getTableView();
            if (var1 != null) {
               ObservableMap var2 = var1.getProperties();
               if (var2.containsKey("tableRecreateKey")) {
                  var2.remove("tableRecreateKey");
               }

               var2.put("tableRecreateKey", Boolean.TRUE);
            }
         }
      };
      this.getStyleClass().add("table-column");
      this.setOnEditCommit(this.DEFAULT_EDIT_COMMIT_HANDLER);
      this.getColumns().addListener(this.weakColumnsListener);
      this.tableViewProperty().addListener((var1) -> {
         Iterator var2 = this.getColumns().iterator();

         while(var2.hasNext()) {
            TableColumn var3 = (TableColumn)var2.next();
            var3.setTableView(this.getTableView());
         }

      });
   }

   public TableColumn(String var1) {
      this();
      this.setText(var1);
   }

   public final ReadOnlyObjectProperty tableViewProperty() {
      return this.tableView.getReadOnlyProperty();
   }

   final void setTableView(TableView var1) {
      this.tableView.set(var1);
   }

   public final TableView getTableView() {
      return (TableView)this.tableView.get();
   }

   public final void setCellValueFactory(Callback var1) {
      this.cellValueFactoryProperty().set(var1);
   }

   public final Callback getCellValueFactory() {
      return this.cellValueFactory == null ? null : (Callback)this.cellValueFactory.get();
   }

   public final ObjectProperty cellValueFactoryProperty() {
      if (this.cellValueFactory == null) {
         this.cellValueFactory = new SimpleObjectProperty(this, "cellValueFactory");
      }

      return this.cellValueFactory;
   }

   public final void setCellFactory(Callback var1) {
      this.cellFactory.set(var1);
   }

   public final Callback getCellFactory() {
      return (Callback)this.cellFactory.get();
   }

   public final ObjectProperty cellFactoryProperty() {
      return this.cellFactory;
   }

   public final ObjectProperty sortTypeProperty() {
      if (this.sortType == null) {
         this.sortType = new SimpleObjectProperty(this, "sortType", TableColumn.SortType.ASCENDING);
      }

      return this.sortType;
   }

   public final void setSortType(SortType var1) {
      this.sortTypeProperty().set(var1);
   }

   public final SortType getSortType() {
      return this.sortType == null ? TableColumn.SortType.ASCENDING : (SortType)this.sortType.get();
   }

   public final void setOnEditStart(EventHandler var1) {
      this.onEditStartProperty().set(var1);
   }

   public final EventHandler getOnEditStart() {
      return this.onEditStart == null ? null : (EventHandler)this.onEditStart.get();
   }

   public final ObjectProperty onEditStartProperty() {
      if (this.onEditStart == null) {
         this.onEditStart = new SimpleObjectProperty(this, "onEditStart") {
            protected void invalidated() {
               TableColumn.this.eventHandlerManager.setEventHandler(TableColumn.EDIT_START_EVENT, (EventHandler)this.get());
            }
         };
      }

      return this.onEditStart;
   }

   public final void setOnEditCommit(EventHandler var1) {
      this.onEditCommitProperty().set(var1);
   }

   public final EventHandler getOnEditCommit() {
      return this.onEditCommit == null ? null : (EventHandler)this.onEditCommit.get();
   }

   public final ObjectProperty onEditCommitProperty() {
      if (this.onEditCommit == null) {
         this.onEditCommit = new SimpleObjectProperty(this, "onEditCommit") {
            protected void invalidated() {
               TableColumn.this.eventHandlerManager.setEventHandler(TableColumn.EDIT_COMMIT_EVENT, (EventHandler)this.get());
            }
         };
      }

      return this.onEditCommit;
   }

   public final void setOnEditCancel(EventHandler var1) {
      this.onEditCancelProperty().set(var1);
   }

   public final EventHandler getOnEditCancel() {
      return this.onEditCancel == null ? null : (EventHandler)this.onEditCancel.get();
   }

   public final ObjectProperty onEditCancelProperty() {
      if (this.onEditCancel == null) {
         this.onEditCancel = new SimpleObjectProperty(this, "onEditCancel") {
            protected void invalidated() {
               TableColumn.this.eventHandlerManager.setEventHandler(TableColumn.EDIT_CANCEL_EVENT, (EventHandler)this.get());
            }
         };
      }

      return this.onEditCancel;
   }

   public final ObservableList getColumns() {
      return this.columns;
   }

   public final ObservableValue getCellObservableValue(int var1) {
      if (var1 < 0) {
         return null;
      } else {
         TableView var2 = this.getTableView();
         if (var2 != null && var2.getItems() != null) {
            ObservableList var3 = var2.getItems();
            if (var1 >= var3.size()) {
               return null;
            } else {
               Object var4 = var3.get(var1);
               return this.getCellObservableValue(var4);
            }
         } else {
            return null;
         }
      }
   }

   public final ObservableValue getCellObservableValue(Object var1) {
      Callback var2 = this.getCellValueFactory();
      if (var2 == null) {
         return null;
      } else {
         TableView var3 = this.getTableView();
         if (var3 == null) {
            return null;
         } else {
            CellDataFeatures var4 = new CellDataFeatures(var3, this, var1);
            return (ObservableValue)var2.call(var4);
         }
      }
   }

   public String getTypeSelector() {
      return "TableColumn";
   }

   public Styleable getStyleableParent() {
      return this.getTableView();
   }

   public List getCssMetaData() {
      return getClassCssMetaData();
   }

   public static List getClassCssMetaData() {
      return Collections.emptyList();
   }

   /** @deprecated */
   @Deprecated
   public Node impl_styleableGetNode() {
      if (!(this.getTableView().getSkin() instanceof TableViewSkin)) {
         return null;
      } else {
         TableViewSkin var1 = (TableViewSkin)this.getTableView().getSkin();
         TableHeaderRow var2 = var1.getTableHeaderRow();
         NestedTableColumnHeader var3 = var2.getRootHeader();
         return this.scan(var3);
      }
   }

   private TableColumnHeader scan(TableColumnHeader var1) {
      if (this.equals(var1.getTableColumn())) {
         return var1;
      } else {
         if (var1 instanceof NestedTableColumnHeader) {
            NestedTableColumnHeader var2 = (NestedTableColumnHeader)var1;

            for(int var3 = 0; var3 < var2.getColumnHeaders().size(); ++var3) {
               TableColumnHeader var4 = this.scan((TableColumnHeader)var2.getColumnHeaders().get(var3));
               if (var4 != null) {
                  return var4;
               }
            }
         }

         return null;
      }
   }

   static {
      EDIT_ANY_EVENT = new EventType(Event.ANY, "TABLE_COLUMN_EDIT");
      EDIT_START_EVENT = new EventType(editAnyEvent(), "EDIT_START");
      EDIT_CANCEL_EVENT = new EventType(editAnyEvent(), "EDIT_CANCEL");
      EDIT_COMMIT_EVENT = new EventType(editAnyEvent(), "EDIT_COMMIT");
      DEFAULT_CELL_FACTORY = new Callback() {
         public TableCell call(TableColumn var1) {
            return new TableCell() {
               protected void updateItem(Object var1, boolean var2) {
                  if (var1 != this.getItem()) {
                     super.updateItem(var1, var2);
                     if (var1 == null) {
                        super.setText((String)null);
                        super.setGraphic((Node)null);
                     } else if (var1 instanceof Node) {
                        super.setText((String)null);
                        super.setGraphic((Node)var1);
                     } else {
                        super.setText(var1.toString());
                        super.setGraphic((Node)null);
                     }

                  }
               }
            };
         }
      };
   }

   public static enum SortType {
      ASCENDING,
      DESCENDING;
   }

   public static class CellEditEvent extends Event {
      private static final long serialVersionUID = -609964441682677579L;
      public static final EventType ANY;
      private final Object newValue;
      private final transient TablePosition pos;

      public CellEditEvent(TableView var1, TablePosition var2, EventType var3, Object var4) {
         super(var1, Event.NULL_SOURCE_TARGET, var3);
         if (var1 == null) {
            throw new NullPointerException("TableView can not be null");
         } else {
            this.pos = var2;
            this.newValue = var4;
         }
      }

      public TableView getTableView() {
         return this.pos.getTableView();
      }

      public TableColumn getTableColumn() {
         return this.pos.getTableColumn();
      }

      public TablePosition getTablePosition() {
         return this.pos;
      }

      public Object getNewValue() {
         return this.newValue;
      }

      public Object getOldValue() {
         Object var1 = this.getRowValue();
         return var1 != null && this.pos.getTableColumn() != null ? this.pos.getTableColumn().getCellData(var1) : null;
      }

      public Object getRowValue() {
         ObservableList var1 = this.getTableView().getItems();
         if (var1 == null) {
            return null;
         } else {
            int var2 = this.pos.getRow();
            return var2 >= 0 && var2 < var1.size() ? var1.get(var2) : null;
         }
      }

      static {
         ANY = TableColumn.EDIT_ANY_EVENT;
      }
   }

   public static class CellDataFeatures {
      private final TableView tableView;
      private final TableColumn tableColumn;
      private final Object value;

      public CellDataFeatures(TableView var1, TableColumn var2, Object var3) {
         this.tableView = var1;
         this.tableColumn = var2;
         this.value = var3;
      }

      public Object getValue() {
         return this.value;
      }

      public TableColumn getTableColumn() {
         return this.tableColumn;
      }

      public TableView getTableView() {
         return this.tableView;
      }
   }
}
