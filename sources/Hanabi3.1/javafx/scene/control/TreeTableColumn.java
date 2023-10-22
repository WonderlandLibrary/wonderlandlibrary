package javafx.scene.control;

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

public class TreeTableColumn extends TableColumnBase implements EventTarget {
   private static final EventType EDIT_ANY_EVENT;
   private static final EventType EDIT_START_EVENT;
   private static final EventType EDIT_CANCEL_EVENT;
   private static final EventType EDIT_COMMIT_EVENT;
   public static final Callback DEFAULT_CELL_FACTORY;
   private EventHandler DEFAULT_EDIT_COMMIT_HANDLER;
   private ListChangeListener columnsListener;
   private WeakListChangeListener weakColumnsListener;
   private final ObservableList columns;
   private ReadOnlyObjectWrapper treeTableView;
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

   public TreeTableColumn() {
      this.DEFAULT_EDIT_COMMIT_HANDLER = (var1) -> {
         ObservableValue var2 = this.getCellObservableValue(var1.getRowValue());
         if (var2 instanceof WritableValue) {
            ((WritableValue)var2).setValue(var1.getNewValue());
         }

      };
      this.columnsListener = new ListChangeListener() {
         public void onChanged(ListChangeListener.Change var1) {
            while(var1.next()) {
               Iterator var2 = var1.getRemoved().iterator();

               TreeTableColumn var3;
               while(var2.hasNext()) {
                  var3 = (TreeTableColumn)var2.next();
                  if (!TreeTableColumn.this.getColumns().contains(var3)) {
                     var3.setTreeTableView((TreeTableView)null);
                     var3.setParentColumn((TableColumnBase)null);
                  }
               }

               var2 = var1.getAddedSubList().iterator();

               while(var2.hasNext()) {
                  var3 = (TreeTableColumn)var2.next();
                  var3.setTreeTableView(TreeTableColumn.this.getTreeTableView());
               }

               TreeTableColumn.this.updateColumnWidths();
            }

         }
      };
      this.weakColumnsListener = new WeakListChangeListener(this.columnsListener);
      this.columns = FXCollections.observableArrayList();
      this.treeTableView = new ReadOnlyObjectWrapper(this, "treeTableView");
      this.cellFactory = new SimpleObjectProperty(this, "cellFactory", DEFAULT_CELL_FACTORY) {
         protected void invalidated() {
            TreeTableView var1 = TreeTableColumn.this.getTreeTableView();
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
      this.treeTableViewProperty().addListener((var1) -> {
         Iterator var2 = this.getColumns().iterator();

         while(var2.hasNext()) {
            TreeTableColumn var3 = (TreeTableColumn)var2.next();
            var3.setTreeTableView(this.getTreeTableView());
         }

      });
   }

   public TreeTableColumn(String var1) {
      this();
      this.setText(var1);
   }

   public final ReadOnlyObjectProperty treeTableViewProperty() {
      return this.treeTableView.getReadOnlyProperty();
   }

   final void setTreeTableView(TreeTableView var1) {
      this.treeTableView.set(var1);
   }

   public final TreeTableView getTreeTableView() {
      return (TreeTableView)this.treeTableView.get();
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
         this.sortType = new SimpleObjectProperty(this, "sortType", TreeTableColumn.SortType.ASCENDING);
      }

      return this.sortType;
   }

   public final void setSortType(SortType var1) {
      this.sortTypeProperty().set(var1);
   }

   public final SortType getSortType() {
      return this.sortType == null ? TreeTableColumn.SortType.ASCENDING : (SortType)this.sortType.get();
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
               TreeTableColumn.this.eventHandlerManager.setEventHandler(TreeTableColumn.EDIT_START_EVENT, (EventHandler)this.get());
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
               TreeTableColumn.this.eventHandlerManager.setEventHandler(TreeTableColumn.EDIT_COMMIT_EVENT, (EventHandler)this.get());
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
               TreeTableColumn.this.eventHandlerManager.setEventHandler(TreeTableColumn.EDIT_CANCEL_EVENT, (EventHandler)this.get());
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
         TreeTableView var2 = this.getTreeTableView();
         if (var2 != null && var1 < var2.getExpandedItemCount()) {
            TreeItem var3 = var2.getTreeItem(var1);
            return this.getCellObservableValue(var3);
         } else {
            return null;
         }
      }
   }

   public final ObservableValue getCellObservableValue(TreeItem var1) {
      Callback var2 = this.getCellValueFactory();
      if (var2 == null) {
         return null;
      } else {
         TreeTableView var3 = this.getTreeTableView();
         if (var3 == null) {
            return null;
         } else {
            CellDataFeatures var4 = new CellDataFeatures(var3, this, var1);
            return (ObservableValue)var2.call(var4);
         }
      }
   }

   public String getTypeSelector() {
      return "TreeTableColumn";
   }

   public Styleable getStyleableParent() {
      return this.getTreeTableView();
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
      return null;
   }

   static {
      EDIT_ANY_EVENT = new EventType(Event.ANY, "TREE_TABLE_COLUMN_EDIT");
      EDIT_START_EVENT = new EventType(editAnyEvent(), "EDIT_START");
      EDIT_CANCEL_EVENT = new EventType(editAnyEvent(), "EDIT_CANCEL");
      EDIT_COMMIT_EVENT = new EventType(editAnyEvent(), "EDIT_COMMIT");
      DEFAULT_CELL_FACTORY = new Callback() {
         public TreeTableCell call(TreeTableColumn var1) {
            return new TreeTableCell() {
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
      private final transient TreeTablePosition pos;

      public CellEditEvent(TreeTableView var1, TreeTablePosition var2, EventType var3, Object var4) {
         super(var1, Event.NULL_SOURCE_TARGET, var3);
         if (var1 == null) {
            throw new NullPointerException("TableView can not be null");
         } else {
            this.pos = var2;
            this.newValue = var4;
         }
      }

      public TreeTableView getTreeTableView() {
         return this.pos.getTreeTableView();
      }

      public TreeTableColumn getTableColumn() {
         return this.pos.getTableColumn();
      }

      public TreeTablePosition getTreeTablePosition() {
         return this.pos;
      }

      public Object getNewValue() {
         return this.newValue;
      }

      public Object getOldValue() {
         TreeItem var1 = this.getRowValue();
         return var1 != null && this.pos.getTableColumn() != null ? this.pos.getTableColumn().getCellData(var1) : null;
      }

      public TreeItem getRowValue() {
         TreeTableView var1 = this.getTreeTableView();
         int var2 = this.pos.getRow();
         return var2 >= 0 && var2 < var1.getExpandedItemCount() ? var1.getTreeItem(var2) : null;
      }

      static {
         ANY = TreeTableColumn.EDIT_ANY_EVENT;
      }
   }

   public static class CellDataFeatures {
      private final TreeTableView treeTableView;
      private final TreeTableColumn tableColumn;
      private final TreeItem value;

      public CellDataFeatures(TreeTableView var1, TreeTableColumn var2, TreeItem var3) {
         this.treeTableView = var1;
         this.tableColumn = var2;
         this.value = var3;
      }

      public TreeItem getValue() {
         return this.value;
      }

      public TreeTableColumn getTreeTableColumn() {
         return this.tableColumn;
      }

      public TreeTableView getTreeTableView() {
         return this.treeTableView;
      }
   }
}
