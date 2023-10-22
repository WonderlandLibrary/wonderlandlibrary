package javafx.scene.control;

import com.sun.javafx.collections.MappingChange;
import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.Logging;
import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
import com.sun.javafx.scene.control.SelectedCellsMap;
import com.sun.javafx.scene.control.TableColumnComparatorBase;
import com.sun.javafx.scene.control.behavior.TableCellBehavior;
import com.sun.javafx.scene.control.behavior.TableCellBehaviorBase;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.WeakHashMap;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.collections.transformation.SortedList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.util.Callback;

@DefaultProperty("items")
public class TableView extends Control {
   static final String SET_CONTENT_WIDTH = "TableView.contentWidth";
   public static final Callback UNCONSTRAINED_RESIZE_POLICY = new Callback() {
      public String toString() {
         return "unconstrained-resize";
      }

      public Boolean call(ResizeFeatures var1) {
         double var2 = TableUtil.resize(var1.getColumn(), var1.getDelta());
         return Double.compare(var2, 0.0) == 0;
      }
   };
   public static final Callback CONSTRAINED_RESIZE_POLICY = new Callback() {
      private boolean isFirstRun = true;

      public String toString() {
         return "constrained-resize";
      }

      public Boolean call(ResizeFeatures var1) {
         TableView var2 = var1.getTable();
         ObservableList var3 = var2.getVisibleLeafColumns();
         Boolean var4 = TableUtil.constrainedResize(var1, this.isFirstRun, var2.contentWidth, var3);
         this.isFirstRun = !this.isFirstRun ? false : !var4;
         return var4;
      }
   };
   public static final Callback DEFAULT_SORT_POLICY = new Callback() {
      public Boolean call(TableView var1) {
         try {
            ObservableList var2 = var1.getItems();
            if (var2 instanceof SortedList) {
               SortedList var7 = (SortedList)var2;
               boolean var4 = var7.comparatorProperty().isEqualTo(var1.comparatorProperty()).get();
               if (!var4 && Logging.getControlsLogger().isEnabled()) {
                  String var5 = "TableView items list is a SortedList, but the SortedList comparator should be bound to the TableView comparator for sorting to be enabled (e.g. sortedList.comparatorProperty().bind(tableView.comparatorProperty());).";
                  Logging.getControlsLogger().info(var5);
               }

               return var4;
            } else if (var2 != null && !var2.isEmpty()) {
               Comparator var3 = var1.getComparator();
               if (var3 == null) {
                  return true;
               } else {
                  FXCollections.sort(var2, var3);
                  return true;
               }
            } else {
               return true;
            }
         } catch (UnsupportedOperationException var6) {
            return false;
         }
      }
   };
   private final ObservableList columns;
   private final ObservableList visibleLeafColumns;
   private final ObservableList unmodifiableVisibleLeafColumns;
   private ObservableList sortOrder;
   private double contentWidth;
   private boolean isInited;
   private final ListChangeListener columnsObserver;
   private final WeakHashMap lastKnownColumnIndex;
   private final InvalidationListener columnVisibleObserver;
   private final InvalidationListener columnSortableObserver;
   private final InvalidationListener columnSortTypeObserver;
   private final InvalidationListener columnComparatorObserver;
   private final InvalidationListener cellSelectionModelInvalidationListener;
   private final WeakInvalidationListener weakColumnVisibleObserver;
   private final WeakInvalidationListener weakColumnSortableObserver;
   private final WeakInvalidationListener weakColumnSortTypeObserver;
   private final WeakInvalidationListener weakColumnComparatorObserver;
   private final WeakListChangeListener weakColumnsObserver;
   private final WeakInvalidationListener weakCellSelectionModelInvalidationListener;
   private ObjectProperty items;
   private BooleanProperty tableMenuButtonVisible;
   private ObjectProperty columnResizePolicy;
   private ObjectProperty rowFactory;
   private ObjectProperty placeholder;
   private ObjectProperty selectionModel;
   private ObjectProperty focusModel;
   private BooleanProperty editable;
   private DoubleProperty fixedCellSize;
   private ReadOnlyObjectWrapper editingCell;
   private ReadOnlyObjectWrapper comparator;
   private ObjectProperty sortPolicy;
   private ObjectProperty onSort;
   private ObjectProperty onScrollTo;
   private ObjectProperty onScrollToColumn;
   private boolean sortLock;
   private TableUtil.SortEventType lastSortEventType;
   private Object[] lastSortEventSupportInfo;
   private static final String DEFAULT_STYLE_CLASS = "table-view";
   private static final PseudoClass PSEUDO_CLASS_CELL_SELECTION = PseudoClass.getPseudoClass("cell-selection");
   private static final PseudoClass PSEUDO_CLASS_ROW_SELECTION = PseudoClass.getPseudoClass("row-selection");

   public TableView() {
      this(FXCollections.observableArrayList());
   }

   public TableView(ObservableList var1) {
      this.columns = FXCollections.observableArrayList();
      this.visibleLeafColumns = FXCollections.observableArrayList();
      this.unmodifiableVisibleLeafColumns = FXCollections.unmodifiableObservableList(this.visibleLeafColumns);
      this.sortOrder = FXCollections.observableArrayList();
      this.isInited = false;
      this.columnsObserver = new ListChangeListener() {
         public void onChanged(ListChangeListener.Change var1) {
            ObservableList var2 = TableView.this.getColumns();

            ArrayList var3;
            label188:
            do {
               do {
                  if (!var1.next()) {
                     var1.reset();
                     TableView.this.updateVisibleLeafColumns();
                     var3 = new ArrayList();

                     Iterator var20;
                     TableColumn var21;
                     while(var1.next()) {
                        List var14 = var1.getRemoved();
                        List var17 = var1.getAddedSubList();
                        if (var1.wasRemoved()) {
                           var3.addAll(var14);
                           var20 = var14.iterator();

                           while(var20.hasNext()) {
                              var21 = (TableColumn)var20.next();
                              var21.setTableView((TableView)null);
                           }
                        }

                        if (var1.wasAdded()) {
                           var3.removeAll(var17);
                           var20 = var17.iterator();

                           while(var20.hasNext()) {
                              var21 = (TableColumn)var20.next();
                              var21.setTableView(TableView.this);
                           }
                        }

                        TableUtil.removeColumnsListener(var14, TableView.this.weakColumnsObserver);
                        TableUtil.addColumnsListener(var17, TableView.this.weakColumnsObserver);
                        TableUtil.removeTableColumnListener(var1.getRemoved(), TableView.this.weakColumnVisibleObserver, TableView.this.weakColumnSortableObserver, TableView.this.weakColumnSortTypeObserver, TableView.this.weakColumnComparatorObserver);
                        TableUtil.addTableColumnListener(var1.getAddedSubList(), TableView.this.weakColumnVisibleObserver, TableView.this.weakColumnSortableObserver, TableView.this.weakColumnSortTypeObserver, TableView.this.weakColumnComparatorObserver);
                     }

                     TableView.this.sortOrder.removeAll(var3);
                     TableViewFocusModel var15 = TableView.this.getFocusModel();
                     TableViewSelectionModel var18 = TableView.this.getSelectionModel();
                     var1.reset();

                     while(true) {
                        List var22;
                        do {
                           do {
                              if (!var1.next()) {
                                 TableView.this.lastKnownColumnIndex.clear();
                                 var20 = TableView.this.getColumns().iterator();

                                 while(var20.hasNext()) {
                                    var21 = (TableColumn)var20.next();
                                    int var28 = TableView.this.getVisibleLeafIndex(var21);
                                    if (var28 > -1) {
                                       TableView.this.lastKnownColumnIndex.put(var21, var28);
                                    }
                                 }

                                 return;
                              }
                           } while(!var1.wasRemoved());

                           var22 = var1.getRemoved();
                           if (var15 != null) {
                              TablePosition var23 = var15.getFocusedCell();
                              boolean var24 = false;
                              Iterator var9 = var22.iterator();

                              while(var9.hasNext()) {
                                 TableColumn var10 = (TableColumn)var9.next();
                                 var24 = var23 != null && var23.getTableColumn() == var10;
                                 if (var24) {
                                    break;
                                 }
                              }

                              if (var24) {
                                 int var27 = (Integer)TableView.this.lastKnownColumnIndex.getOrDefault(var23.getTableColumn(), 0);
                                 int var30 = var27 == 0 ? 0 : Math.min(TableView.this.getVisibleLeafColumns().size() - 1, var27 - 1);
                                 var15.focus(var23.getRow(), TableView.this.getVisibleLeafColumn(var30));
                              }
                           }
                        } while(var18 == null);

                        ArrayList var25 = new ArrayList(var18.getSelectedCells());
                        Iterator var26 = var25.iterator();

                        while(var26.hasNext()) {
                           TablePosition var29 = (TablePosition)var26.next();
                           boolean var31 = false;
                           Iterator var11 = var22.iterator();

                           while(var11.hasNext()) {
                              TableColumn var12 = (TableColumn)var11.next();
                              var31 = var29 != null && var29.getTableColumn() == var12;
                              if (var31) {
                                 break;
                              }
                           }

                           if (var31) {
                              int var32 = (Integer)TableView.this.lastKnownColumnIndex.getOrDefault(var29.getTableColumn(), -1);
                              if (var32 != -1) {
                                 if (var18 instanceof TableViewArrayListSelectionModel) {
                                    TablePosition var33 = new TablePosition(TableView.this, var29.getRow(), var29.getTableColumn());
                                    var33.fixedColumnIndex = var32;
                                    ((TableViewArrayListSelectionModel)var18).clearSelection(var33);
                                 } else {
                                    var18.clearSelection(var29.getRow(), var29.getTableColumn());
                                 }
                              }
                           }
                        }
                     }
                  }
               } while(!var1.wasAdded());

               var3 = new ArrayList();
               Iterator var4 = var1.getAddedSubList().iterator();

               while(true) {
                  TableColumn var5;
                  do {
                     if (!var4.hasNext()) {
                        continue label188;
                     }

                     var5 = (TableColumn)var4.next();
                  } while(var5 == null);

                  int var6 = 0;
                  Iterator var7 = var2.iterator();

                  while(var7.hasNext()) {
                     TableColumn var8 = (TableColumn)var7.next();
                     if (var5 == var8) {
                        ++var6;
                     }
                  }

                  if (var6 > 1) {
                     var3.add(var5);
                  }
               }
            } while(var3.isEmpty());

            String var13 = "";

            TableColumn var19;
            for(Iterator var16 = var3.iterator(); var16.hasNext(); var13 = var13 + "'" + var19.getText() + "', ") {
               var19 = (TableColumn)var16.next();
            }

            throw new IllegalStateException("Duplicate TableColumns detected in TableView columns list with titles " + var13);
         }
      };
      this.lastKnownColumnIndex = new WeakHashMap();
      this.columnVisibleObserver = (var1x) -> {
         this.updateVisibleLeafColumns();
      };
      this.columnSortableObserver = (var1x) -> {
         Object var2 = ((Property)var1x).getBean();
         if (this.getSortOrder().contains(var2)) {
            this.doSort(TableUtil.SortEventType.COLUMN_SORTABLE_CHANGE, var2);
         }
      };
      this.columnSortTypeObserver = (var1x) -> {
         Object var2 = ((Property)var1x).getBean();
         if (this.getSortOrder().contains(var2)) {
            this.doSort(TableUtil.SortEventType.COLUMN_SORT_TYPE_CHANGE, var2);
         }
      };
      this.columnComparatorObserver = (var1x) -> {
         Object var2 = ((Property)var1x).getBean();
         if (this.getSortOrder().contains(var2)) {
            this.doSort(TableUtil.SortEventType.COLUMN_COMPARATOR_CHANGE, var2);
         }
      };
      this.cellSelectionModelInvalidationListener = (var1x) -> {
         boolean var2 = ((BooleanProperty)var1x).get();
         this.pseudoClassStateChanged(PSEUDO_CLASS_CELL_SELECTION, var2);
         this.pseudoClassStateChanged(PSEUDO_CLASS_ROW_SELECTION, !var2);
      };
      this.weakColumnVisibleObserver = new WeakInvalidationListener(this.columnVisibleObserver);
      this.weakColumnSortableObserver = new WeakInvalidationListener(this.columnSortableObserver);
      this.weakColumnSortTypeObserver = new WeakInvalidationListener(this.columnSortTypeObserver);
      this.weakColumnComparatorObserver = new WeakInvalidationListener(this.columnComparatorObserver);
      this.weakColumnsObserver = new WeakListChangeListener(this.columnsObserver);
      this.weakCellSelectionModelInvalidationListener = new WeakInvalidationListener(this.cellSelectionModelInvalidationListener);
      this.items = new SimpleObjectProperty(this, "items") {
         WeakReference oldItemsRef;

         protected void invalidated() {
            ObservableList var1 = this.oldItemsRef == null ? null : (ObservableList)this.oldItemsRef.get();
            ObservableList var2 = TableView.this.getItems();
            if (var2 == null || var2 != var1) {
               if (!(var2 instanceof SortedList)) {
                  TableView.this.getSortOrder().clear();
               }

               this.oldItemsRef = new WeakReference(var2);
            }
         }
      };
      this.selectionModel = new SimpleObjectProperty(this, "selectionModel") {
         TableViewSelectionModel oldValue = null;

         protected void invalidated() {
            if (this.oldValue != null) {
               this.oldValue.cellSelectionEnabledProperty().removeListener(TableView.this.weakCellSelectionModelInvalidationListener);
            }

            this.oldValue = (TableViewSelectionModel)this.get();
            if (this.oldValue != null) {
               this.oldValue.cellSelectionEnabledProperty().addListener(TableView.this.weakCellSelectionModelInvalidationListener);
               TableView.this.weakCellSelectionModelInvalidationListener.invalidated(this.oldValue.cellSelectionEnabledProperty());
            }

         }
      };
      this.sortLock = false;
      this.lastSortEventType = null;
      this.lastSortEventSupportInfo = null;
      this.getStyleClass().setAll((Object[])("table-view"));
      this.setAccessibleRole(AccessibleRole.TABLE_VIEW);
      this.setItems(var1);
      this.setSelectionModel(new TableViewArrayListSelectionModel(this));
      this.setFocusModel(new TableViewFocusModel(this));
      this.getColumns().addListener(this.weakColumnsObserver);
      this.getSortOrder().addListener((var1x) -> {
         this.doSort(TableUtil.SortEventType.SORT_ORDER_CHANGE, var1x);
      });
      this.getProperties().addListener(new MapChangeListener() {
         public void onChanged(MapChangeListener.Change var1) {
            if (var1.wasAdded() && "TableView.contentWidth".equals(var1.getKey())) {
               if (var1.getValueAdded() instanceof Number) {
                  TableView.this.setContentWidth((Double)var1.getValueAdded());
               }

               TableView.this.getProperties().remove("TableView.contentWidth");
            }

         }
      });
      this.isInited = true;
   }

   public final ObjectProperty itemsProperty() {
      return this.items;
   }

   public final void setItems(ObservableList var1) {
      this.itemsProperty().set(var1);
   }

   public final ObservableList getItems() {
      return (ObservableList)this.items.get();
   }

   public final BooleanProperty tableMenuButtonVisibleProperty() {
      if (this.tableMenuButtonVisible == null) {
         this.tableMenuButtonVisible = new SimpleBooleanProperty(this, "tableMenuButtonVisible");
      }

      return this.tableMenuButtonVisible;
   }

   public final void setTableMenuButtonVisible(boolean var1) {
      this.tableMenuButtonVisibleProperty().set(var1);
   }

   public final boolean isTableMenuButtonVisible() {
      return this.tableMenuButtonVisible == null ? false : this.tableMenuButtonVisible.get();
   }

   public final void setColumnResizePolicy(Callback var1) {
      this.columnResizePolicyProperty().set(var1);
   }

   public final Callback getColumnResizePolicy() {
      return this.columnResizePolicy == null ? UNCONSTRAINED_RESIZE_POLICY : (Callback)this.columnResizePolicy.get();
   }

   public final ObjectProperty columnResizePolicyProperty() {
      if (this.columnResizePolicy == null) {
         this.columnResizePolicy = new SimpleObjectProperty(this, "columnResizePolicy", UNCONSTRAINED_RESIZE_POLICY) {
            private Callback oldPolicy;

            protected void invalidated() {
               if (TableView.this.isInited) {
                  ((Callback)this.get()).call(new ResizeFeatures(TableView.this, (TableColumn)null, 0.0));
                  PseudoClass var1;
                  if (this.oldPolicy != null) {
                     var1 = PseudoClass.getPseudoClass(this.oldPolicy.toString());
                     TableView.this.pseudoClassStateChanged(var1, false);
                  }

                  if (this.get() != null) {
                     var1 = PseudoClass.getPseudoClass(((Callback)this.get()).toString());
                     TableView.this.pseudoClassStateChanged(var1, true);
                  }

                  this.oldPolicy = (Callback)this.get();
               }

            }
         };
      }

      return this.columnResizePolicy;
   }

   public final ObjectProperty rowFactoryProperty() {
      if (this.rowFactory == null) {
         this.rowFactory = new SimpleObjectProperty(this, "rowFactory");
      }

      return this.rowFactory;
   }

   public final void setRowFactory(Callback var1) {
      this.rowFactoryProperty().set(var1);
   }

   public final Callback getRowFactory() {
      return this.rowFactory == null ? null : (Callback)this.rowFactory.get();
   }

   public final ObjectProperty placeholderProperty() {
      if (this.placeholder == null) {
         this.placeholder = new SimpleObjectProperty(this, "placeholder");
      }

      return this.placeholder;
   }

   public final void setPlaceholder(Node var1) {
      this.placeholderProperty().set(var1);
   }

   public final Node getPlaceholder() {
      return this.placeholder == null ? null : (Node)this.placeholder.get();
   }

   public final ObjectProperty selectionModelProperty() {
      return this.selectionModel;
   }

   public final void setSelectionModel(TableViewSelectionModel var1) {
      this.selectionModelProperty().set(var1);
   }

   public final TableViewSelectionModel getSelectionModel() {
      return (TableViewSelectionModel)this.selectionModel.get();
   }

   public final void setFocusModel(TableViewFocusModel var1) {
      this.focusModelProperty().set(var1);
   }

   public final TableViewFocusModel getFocusModel() {
      return this.focusModel == null ? null : (TableViewFocusModel)this.focusModel.get();
   }

   public final ObjectProperty focusModelProperty() {
      if (this.focusModel == null) {
         this.focusModel = new SimpleObjectProperty(this, "focusModel");
      }

      return this.focusModel;
   }

   public final void setEditable(boolean var1) {
      this.editableProperty().set(var1);
   }

   public final boolean isEditable() {
      return this.editable == null ? false : this.editable.get();
   }

   public final BooleanProperty editableProperty() {
      if (this.editable == null) {
         this.editable = new SimpleBooleanProperty(this, "editable", false);
      }

      return this.editable;
   }

   public final void setFixedCellSize(double var1) {
      this.fixedCellSizeProperty().set(var1);
   }

   public final double getFixedCellSize() {
      return this.fixedCellSize == null ? -1.0 : this.fixedCellSize.get();
   }

   public final DoubleProperty fixedCellSizeProperty() {
      if (this.fixedCellSize == null) {
         this.fixedCellSize = new StyleableDoubleProperty(-1.0) {
            public CssMetaData getCssMetaData() {
               return TableView.StyleableProperties.FIXED_CELL_SIZE;
            }

            public Object getBean() {
               return TableView.this;
            }

            public String getName() {
               return "fixedCellSize";
            }
         };
      }

      return this.fixedCellSize;
   }

   private void setEditingCell(TablePosition var1) {
      this.editingCellPropertyImpl().set(var1);
   }

   public final TablePosition getEditingCell() {
      return this.editingCell == null ? null : (TablePosition)this.editingCell.get();
   }

   public final ReadOnlyObjectProperty editingCellProperty() {
      return this.editingCellPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper editingCellPropertyImpl() {
      if (this.editingCell == null) {
         this.editingCell = new ReadOnlyObjectWrapper(this, "editingCell");
      }

      return this.editingCell;
   }

   private void setComparator(Comparator var1) {
      this.comparatorPropertyImpl().set(var1);
   }

   public final Comparator getComparator() {
      return this.comparator == null ? null : (Comparator)this.comparator.get();
   }

   public final ReadOnlyObjectProperty comparatorProperty() {
      return this.comparatorPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyObjectWrapper comparatorPropertyImpl() {
      if (this.comparator == null) {
         this.comparator = new ReadOnlyObjectWrapper(this, "comparator");
      }

      return this.comparator;
   }

   public final void setSortPolicy(Callback var1) {
      this.sortPolicyProperty().set(var1);
   }

   public final Callback getSortPolicy() {
      return this.sortPolicy == null ? (Callback)DEFAULT_SORT_POLICY : (Callback)this.sortPolicy.get();
   }

   public final ObjectProperty sortPolicyProperty() {
      if (this.sortPolicy == null) {
         this.sortPolicy = new SimpleObjectProperty(this, "sortPolicy", (Callback)DEFAULT_SORT_POLICY) {
            protected void invalidated() {
               TableView.this.sort();
            }
         };
      }

      return this.sortPolicy;
   }

   public void setOnSort(EventHandler var1) {
      this.onSortProperty().set(var1);
   }

   public EventHandler getOnSort() {
      return this.onSort != null ? (EventHandler)this.onSort.get() : null;
   }

   public ObjectProperty onSortProperty() {
      if (this.onSort == null) {
         this.onSort = new ObjectPropertyBase() {
            protected void invalidated() {
               EventType var1 = SortEvent.sortEvent();
               EventHandler var2 = (EventHandler)this.get();
               TableView.this.setEventHandler(var1, var2);
            }

            public Object getBean() {
               return TableView.this;
            }

            public String getName() {
               return "onSort";
            }
         };
      }

      return this.onSort;
   }

   public final ObservableList getColumns() {
      return this.columns;
   }

   public final ObservableList getSortOrder() {
      return this.sortOrder;
   }

   public void scrollTo(int var1) {
      ControlUtils.scrollToIndex(this, var1);
   }

   public void scrollTo(Object var1) {
      if (this.getItems() != null) {
         int var2 = this.getItems().indexOf(var1);
         if (var2 >= 0) {
            ControlUtils.scrollToIndex(this, var2);
         }
      }

   }

   public void setOnScrollTo(EventHandler var1) {
      this.onScrollToProperty().set(var1);
   }

   public EventHandler getOnScrollTo() {
      return this.onScrollTo != null ? (EventHandler)this.onScrollTo.get() : null;
   }

   public ObjectProperty onScrollToProperty() {
      if (this.onScrollTo == null) {
         this.onScrollTo = new ObjectPropertyBase() {
            protected void invalidated() {
               TableView.this.setEventHandler(ScrollToEvent.scrollToTopIndex(), (EventHandler)this.get());
            }

            public Object getBean() {
               return TableView.this;
            }

            public String getName() {
               return "onScrollTo";
            }
         };
      }

      return this.onScrollTo;
   }

   public void scrollToColumn(TableColumn var1) {
      ControlUtils.scrollToColumn(this, var1);
   }

   public void scrollToColumnIndex(int var1) {
      if (this.getColumns() != null) {
         ControlUtils.scrollToColumn(this, (TableColumnBase)this.getColumns().get(var1));
      }

   }

   public void setOnScrollToColumn(EventHandler var1) {
      this.onScrollToColumnProperty().set(var1);
   }

   public EventHandler getOnScrollToColumn() {
      return this.onScrollToColumn != null ? (EventHandler)this.onScrollToColumn.get() : null;
   }

   public ObjectProperty onScrollToColumnProperty() {
      if (this.onScrollToColumn == null) {
         this.onScrollToColumn = new ObjectPropertyBase() {
            protected void invalidated() {
               EventType var1 = ScrollToEvent.scrollToColumn();
               TableView.this.setEventHandler(var1, (EventHandler)this.get());
            }

            public Object getBean() {
               return TableView.this;
            }

            public String getName() {
               return "onScrollToColumn";
            }
         };
      }

      return this.onScrollToColumn;
   }

   public boolean resizeColumn(TableColumn var1, double var2) {
      if (var1 != null && Double.compare(var2, 0.0) != 0) {
         boolean var4 = (Boolean)this.getColumnResizePolicy().call(new ResizeFeatures(this, var1, var2));
         return var4;
      } else {
         return false;
      }
   }

   public void edit(int var1, TableColumn var2) {
      if (this.isEditable() && (var2 == null || var2.isEditable())) {
         if (var1 < 0 && var2 == null) {
            this.setEditingCell((TablePosition)null);
         } else {
            this.setEditingCell(new TablePosition(this, var1, var2));
         }

      }
   }

   public ObservableList getVisibleLeafColumns() {
      return this.unmodifiableVisibleLeafColumns;
   }

   public int getVisibleLeafIndex(TableColumn var1) {
      return this.visibleLeafColumns.indexOf(var1);
   }

   public TableColumn getVisibleLeafColumn(int var1) {
      return var1 >= 0 && var1 < this.visibleLeafColumns.size() ? (TableColumn)this.visibleLeafColumns.get(var1) : null;
   }

   protected Skin createDefaultSkin() {
      return new TableViewSkin(this);
   }

   public void sort() {
      ObservableList var1 = this.getSortOrder();
      Comparator var2 = this.getComparator();
      this.setComparator(var1.isEmpty() ? null : new TableColumnComparatorBase.TableColumnComparator(var1));
      SortEvent var3 = new SortEvent(this, this);
      this.fireEvent(var3);
      if (!var3.isConsumed()) {
         ArrayList var4 = new ArrayList(this.getSelectionModel().getSelectedCells());
         int var5 = var4.size();
         this.getSelectionModel().startAtomic();
         Callback var6 = this.getSortPolicy();
         if (var6 != null) {
            Boolean var7 = (Boolean)var6.call(this);
            this.getSelectionModel().stopAtomic();
            if (var7 != null && var7) {
               if (this.getSelectionModel() instanceof TableViewArrayListSelectionModel) {
                  TableViewArrayListSelectionModel var8 = (TableViewArrayListSelectionModel)this.getSelectionModel();
                  ObservableList var9 = (ObservableList)var8.getSelectedCells();
                  ArrayList var10 = new ArrayList();

                  for(int var11 = 0; var11 < var5; ++var11) {
                     TablePosition var12 = (TablePosition)var4.get(var11);
                     if (!var9.contains(var12)) {
                        var10.add(var12);
                     }
                  }

                  if (!var10.isEmpty()) {
                     NonIterableChange.GenericAddRemoveChange var13 = new NonIterableChange.GenericAddRemoveChange(0, var5, var10, var9);
                     var8.handleSelectedCellsListChangeEvent(var13);
                  }
               }
            } else {
               this.sortLock = true;
               TableUtil.handleSortFailure(var1, this.lastSortEventType, this.lastSortEventSupportInfo);
               this.setComparator(var2);
               this.sortLock = false;
            }

         }
      }
   }

   public void refresh() {
      this.getProperties().put("tableRecreateKey", Boolean.TRUE);
   }

   private void doSort(TableUtil.SortEventType var1, Object... var2) {
      if (!this.sortLock) {
         this.lastSortEventType = var1;
         this.lastSortEventSupportInfo = var2;
         this.sort();
         this.lastSortEventType = null;
         this.lastSortEventSupportInfo = null;
      }
   }

   private void setContentWidth(double var1) {
      this.contentWidth = var1;
      if (this.isInited) {
         this.getColumnResizePolicy().call(new ResizeFeatures(this, (TableColumn)null, 0.0));
      }

   }

   private void updateVisibleLeafColumns() {
      ArrayList var1 = new ArrayList();
      this.buildVisibleLeafColumns(this.getColumns(), var1);
      this.visibleLeafColumns.setAll((Collection)var1);
      this.getColumnResizePolicy().call(new ResizeFeatures(this, (TableColumn)null, 0.0));
   }

   private void buildVisibleLeafColumns(List var1, List var2) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         TableColumn var4 = (TableColumn)var3.next();
         if (var4 != null) {
            boolean var5 = !var4.getColumns().isEmpty();
            if (var5) {
               this.buildVisibleLeafColumns(var4.getColumns(), var2);
            } else if (var4.isVisible()) {
               var2.add(var4);
            }
         }
      }

   }

   public static List getClassCssMetaData() {
      return TableView.StyleableProperties.STYLEABLES;
   }

   public List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case COLUMN_COUNT:
            return this.getVisibleLeafColumns().size();
         case ROW_COUNT:
            return this.getItems().size();
         case SELECTED_ITEMS:
            ObservableList var10 = (ObservableList)super.queryAccessibleAttribute(var1, var2);
            ArrayList var11 = new ArrayList();
            Iterator var5 = var10.iterator();

            while(var5.hasNext()) {
               TableRow var6 = (TableRow)var5.next();
               ObservableList var7 = (ObservableList)var6.queryAccessibleAttribute(var1, var2);
               if (var7 != null) {
                  var11.addAll(var7);
               }
            }

            return FXCollections.observableArrayList((Collection)var11);
         case FOCUS_ITEM:
            Node var9 = (Node)super.queryAccessibleAttribute(var1, var2);
            if (var9 == null) {
               return null;
            }

            Node var4 = (Node)var9.queryAccessibleAttribute(var1, var2);
            return var4 != null ? var4 : var9;
         case CELL_AT_ROW_COLUMN:
            TableRow var8 = (TableRow)super.queryAccessibleAttribute(var1, var2);
            return var8 != null ? var8.queryAccessibleAttribute(var1, var2) : null;
         case MULTIPLE_SELECTION:
            TableViewSelectionModel var3 = this.getSelectionModel();
            return var3 != null && var3.getSelectionMode() == SelectionMode.MULTIPLE;
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   public static class TableViewFocusModel extends TableFocusModel {
      private final TableView tableView;
      private final TablePosition EMPTY_CELL;
      private final ListChangeListener itemsContentListener = (var1x) -> {
         var1x.next();
         TablePosition var2 = this.getFocusedCell();
         int var3 = var2.getRow();
         if (var3 != -1 && var1x.getFrom() <= var3) {
            var1x.reset();
            boolean var4 = false;
            boolean var5 = false;
            int var6 = 0;

            int var7;
            for(var7 = 0; var1x.next(); var7 += var1x.getRemovedSize()) {
               var4 |= var1x.wasAdded();
               var5 |= var1x.wasRemoved();
               var6 += var1x.getAddedSize();
            }

            int var8;
            if (var4 && !var5) {
               if (var6 < var1x.getList().size()) {
                  var8 = Math.min(this.getItemCount() - 1, this.getFocusedIndex() + var6);
                  this.focus(var8, var2.getTableColumn());
               }
            } else if (!var4 && var5) {
               var8 = Math.max(0, this.getFocusedIndex() - var7);
               if (var8 < 0) {
                  this.focus(0, (TableColumn)var2.getTableColumn());
               } else {
                  this.focus(var8, var2.getTableColumn());
               }
            }

         }
      };
      private WeakListChangeListener weakItemsContentListener;
      private ReadOnlyObjectWrapper focusedCell;

      public TableViewFocusModel(final TableView var1) {
         this.weakItemsContentListener = new WeakListChangeListener(this.itemsContentListener);
         if (var1 == null) {
            throw new NullPointerException("TableView can not be null");
         } else {
            this.tableView = var1;
            this.EMPTY_CELL = new TablePosition(var1, -1, (TableColumn)null);
            if (var1.getItems() != null) {
               this.tableView.getItems().addListener(this.weakItemsContentListener);
            }

            this.tableView.itemsProperty().addListener(new InvalidationListener() {
               private WeakReference weakItemsRef = new WeakReference(var1.getItems());

               public void invalidated(Observable var1x) {
                  ObservableList var2 = (ObservableList)this.weakItemsRef.get();
                  this.weakItemsRef = new WeakReference(var1.getItems());
                  TableViewFocusModel.this.updateItemsObserver(var2, var1.getItems());
               }
            });
            this.updateDefaultFocus();
         }
      }

      private void updateItemsObserver(ObservableList var1, ObservableList var2) {
         if (var1 != null) {
            var1.removeListener(this.weakItemsContentListener);
         }

         if (var2 != null) {
            var2.addListener(this.weakItemsContentListener);
         }

         this.updateDefaultFocus();
      }

      protected int getItemCount() {
         return this.tableView.getItems() == null ? -1 : this.tableView.getItems().size();
      }

      protected Object getModelItem(int var1) {
         if (this.tableView.getItems() == null) {
            return null;
         } else {
            return var1 >= 0 && var1 < this.getItemCount() ? this.tableView.getItems().get(var1) : null;
         }
      }

      public final ReadOnlyObjectProperty focusedCellProperty() {
         return this.focusedCellPropertyImpl().getReadOnlyProperty();
      }

      private void setFocusedCell(TablePosition var1) {
         this.focusedCellPropertyImpl().set(var1);
      }

      public final TablePosition getFocusedCell() {
         return this.focusedCell == null ? this.EMPTY_CELL : (TablePosition)this.focusedCell.get();
      }

      private ReadOnlyObjectWrapper focusedCellPropertyImpl() {
         if (this.focusedCell == null) {
            this.focusedCell = new ReadOnlyObjectWrapper(this.EMPTY_CELL) {
               private TablePosition old;

               protected void invalidated() {
                  if (this.get() != null) {
                     if (this.old == null || !this.old.equals(this.get())) {
                        TableViewFocusModel.this.setFocusedIndex(((TablePosition)this.get()).getRow());
                        TableViewFocusModel.this.setFocusedItem(TableViewFocusModel.this.getModelItem(((TablePosition)this.getValue()).getRow()));
                        this.old = (TablePosition)this.get();
                     }

                  }
               }

               public Object getBean() {
                  return TableViewFocusModel.this;
               }

               public String getName() {
                  return "focusedCell";
               }
            };
         }

         return this.focusedCell;
      }

      public void focus(int var1, TableColumn var2) {
         if (var1 >= 0 && var1 < this.getItemCount()) {
            TablePosition var3 = this.getFocusedCell();
            TablePosition var4 = new TablePosition(this.tableView, var1, var2);
            this.setFocusedCell(var4);
            if (var4.equals(var3)) {
               this.setFocusedIndex(var1);
               this.setFocusedItem(this.getModelItem(var1));
            }
         } else {
            this.setFocusedCell(this.EMPTY_CELL);
         }

      }

      public void focus(TablePosition var1) {
         if (var1 != null) {
            this.focus(var1.getRow(), var1.getTableColumn());
         }
      }

      public boolean isFocused(int var1, TableColumn var2) {
         if (var1 >= 0 && var1 < this.getItemCount()) {
            TablePosition var3 = this.getFocusedCell();
            boolean var4 = var2 == null || var2.equals(var3.getTableColumn());
            return var3.getRow() == var1 && var4;
         } else {
            return false;
         }
      }

      public void focus(int var1) {
         if (var1 >= 0 && var1 < this.getItemCount()) {
            this.setFocusedCell(new TablePosition(this.tableView, var1, (TableColumn)null));
         } else {
            this.setFocusedCell(this.EMPTY_CELL);
         }

      }

      public void focusAboveCell() {
         TablePosition var1 = this.getFocusedCell();
         if (this.getFocusedIndex() == -1) {
            this.focus(this.getItemCount() - 1, var1.getTableColumn());
         } else if (this.getFocusedIndex() > 0) {
            this.focus(this.getFocusedIndex() - 1, var1.getTableColumn());
         }

      }

      public void focusBelowCell() {
         TablePosition var1 = this.getFocusedCell();
         if (this.getFocusedIndex() == -1) {
            this.focus(0, (TableColumn)var1.getTableColumn());
         } else if (this.getFocusedIndex() != this.getItemCount() - 1) {
            this.focus(this.getFocusedIndex() + 1, var1.getTableColumn());
         }

      }

      public void focusLeftCell() {
         TablePosition var1 = this.getFocusedCell();
         if (var1.getColumn() > 0) {
            this.focus(var1.getRow(), this.getTableColumn(var1.getTableColumn(), -1));
         }
      }

      public void focusRightCell() {
         TablePosition var1 = this.getFocusedCell();
         if (var1.getColumn() != this.getColumnCount() - 1) {
            this.focus(var1.getRow(), this.getTableColumn(var1.getTableColumn(), 1));
         }
      }

      public void focusPrevious() {
         if (this.getFocusedIndex() == -1) {
            this.focus(0);
         } else if (this.getFocusedIndex() > 0) {
            this.focusAboveCell();
         }

      }

      public void focusNext() {
         if (this.getFocusedIndex() == -1) {
            this.focus(0);
         } else if (this.getFocusedIndex() != this.getItemCount() - 1) {
            this.focusBelowCell();
         }

      }

      private void updateDefaultFocus() {
         int var1 = -1;
         if (this.tableView.getItems() != null) {
            Object var2 = this.getFocusedItem();
            if (var2 != null) {
               var1 = this.tableView.getItems().indexOf(var2);
            }

            if (var1 == -1) {
               var1 = this.tableView.getItems().size() > 0 ? 0 : -1;
            }
         }

         TablePosition var4 = this.getFocusedCell();
         TableColumn var3 = var4 != null && !this.EMPTY_CELL.equals(var4) ? var4.getTableColumn() : this.tableView.getVisibleLeafColumn(0);
         this.focus(var1, var3);
      }

      private int getColumnCount() {
         return this.tableView.getVisibleLeafColumns().size();
      }

      private TableColumn getTableColumn(TableColumn var1, int var2) {
         int var3 = this.tableView.getVisibleLeafIndex(var1);
         int var4 = var3 + var2;
         return this.tableView.getVisibleLeafColumn(var4);
      }
   }

   static class TableViewArrayListSelectionModel extends TableViewSelectionModel {
      private int itemCount = 0;
      private final MappingChange.Map cellToItemsMap = (var1x) -> {
         return this.getModelItem(var1x.getRow());
      };
      private final MappingChange.Map cellToIndicesMap = (var0) -> {
         return var0.getRow();
      };
      private final TableView tableView;
      final ListChangeListener itemsContentListener = (var1x) -> {
         this.updateItemCount();
         List var2 = this.getTableModel();

         while(var1x.next()) {
            if (var1x.wasReplaced() || var1x.getAddedSize() == this.getItemCount()) {
               this.selectedItemChange = var1x;
               this.updateDefaultSelection();
               this.selectedItemChange = null;
               return;
            }

            Object var3 = this.getSelectedItem();
            int var4 = this.getSelectedIndex();
            if (var2 != null && !var2.isEmpty()) {
               int var5;
               if (this.getSelectedIndex() == -1 && this.getSelectedItem() != null) {
                  var5 = var2.indexOf(this.getSelectedItem());
                  if (var5 != -1) {
                     this.setSelectedIndex(var5);
                  }
               } else if (var1x.wasRemoved() && var1x.getRemovedSize() == 1 && !var1x.wasAdded() && var3 != null && var3.equals(var1x.getRemoved().get(0)) && this.getSelectedIndex() < this.getItemCount()) {
                  var5 = var4 == 0 ? 0 : var4 - 1;
                  Object var6 = this.getModelItem(var5);
                  if (!var3.equals(var6)) {
                     this.clearAndSelect(var5);
                  }
               }
            } else {
               this.clearSelection();
            }
         }

         this.updateSelection(var1x);
      };
      final WeakListChangeListener weakItemsContentListener;
      private final SelectedCellsMap selectedCellsMap;
      private final ReadOnlyUnbackedObservableList selectedItems;
      private final ReadOnlyUnbackedObservableList selectedCellsSeq;
      private int previousModelSize;

      public TableViewArrayListSelectionModel(final TableView var1) {
         super(var1);
         this.weakItemsContentListener = new WeakListChangeListener(this.itemsContentListener);
         this.previousModelSize = 0;
         this.tableView = var1;
         this.tableView.itemsProperty().addListener(new InvalidationListener() {
            private WeakReference weakItemsRef = new WeakReference(var1.getItems());

            public void invalidated(Observable var1x) {
               ObservableList var2 = (ObservableList)this.weakItemsRef.get();
               this.weakItemsRef = new WeakReference(var1.getItems());
               TableViewArrayListSelectionModel.this.updateItemsObserver(var2, var1.getItems());
            }
         });
         this.selectedCellsMap = new SelectedCellsMap((var1x) -> {
            this.handleSelectedCellsListChangeEvent(var1x);
         }) {
            public boolean isCellSelectionEnabled() {
               return TableViewArrayListSelectionModel.this.isCellSelectionEnabled();
            }
         };
         this.selectedItems = new ReadOnlyUnbackedObservableList() {
            public Object get(int var1) {
               return TableViewArrayListSelectionModel.this.getModelItem((Integer)TableViewArrayListSelectionModel.this.getSelectedIndices().get(var1));
            }

            public int size() {
               return TableViewArrayListSelectionModel.this.getSelectedIndices().size();
            }
         };
         this.selectedCellsSeq = new ReadOnlyUnbackedObservableList() {
            public TablePosition get(int var1) {
               return (TablePosition)TableViewArrayListSelectionModel.this.selectedCellsMap.get(var1);
            }

            public int size() {
               return TableViewArrayListSelectionModel.this.selectedCellsMap.size();
            }
         };
         ObservableList var2 = this.getTableView().getItems();
         if (var2 != null) {
            var2.addListener(this.weakItemsContentListener);
         }

         this.updateItemCount();
         this.updateDefaultSelection();
         this.cellSelectionEnabledProperty().addListener((var2x) -> {
            this.updateDefaultSelection();
            TableCellBehaviorBase.setAnchor(var1, this.getFocusedCell(), true);
         });
      }

      public ObservableList getSelectedItems() {
         return this.selectedItems;
      }

      public ObservableList getSelectedCells() {
         return (ObservableList)this.selectedCellsSeq;
      }

      private void updateSelection(ListChangeListener.Change var1) {
         var1.reset();
         int var2 = 0;
         int var3 = -1;

         while(true) {
            while(true) {
               int var5;
               int var7;
               while(var1.next()) {
                  int var4;
                  if (var1.wasReplaced()) {
                     if (var1.getList().isEmpty()) {
                        this.clearSelection();
                     } else {
                        var4 = this.getSelectedIndex();
                        if (this.previousModelSize == var1.getRemovedSize()) {
                           this.clearSelection();
                        } else if (var4 < this.getItemCount() && var4 >= 0) {
                           this.startAtomic();
                           this.clearSelection(var4);
                           this.stopAtomic();
                           this.select(var4);
                        } else {
                           this.clearSelection();
                        }
                     }
                  } else if (!var1.wasAdded() && !var1.wasRemoved()) {
                     if (var1.wasPermutated()) {
                        this.startAtomic();
                        var4 = this.getSelectedIndex();
                        var5 = var1.getTo() - var1.getFrom();
                        HashMap var6 = new HashMap(var5);

                        for(var7 = var1.getFrom(); var7 < var1.getTo(); ++var7) {
                           var6.put(var7, var1.getPermutation(var7));
                        }

                        ArrayList var16 = new ArrayList((ObservableList)this.getSelectedCells());
                        ArrayList var8 = new ArrayList(var16.size());
                        boolean var9 = false;

                        int var10;
                        for(var10 = 0; var10 < var16.size(); ++var10) {
                           TablePosition var11 = (TablePosition)var16.get(var10);
                           int var12 = var11.getRow();
                           if (var6.containsKey(var12)) {
                              int var13 = (Integer)var6.get(var12);
                              var9 = var9 || var13 != var12;
                              var8.add(new TablePosition(var11.getTableView(), var13, var11.getTableColumn()));
                           }
                        }

                        if (var9) {
                           this.quietClearSelection();
                           this.stopAtomic();
                           this.selectedCellsMap.setAll(var8);
                           if (var4 >= 0 && var4 < this.itemCount) {
                              var10 = var1.getPermutation(var4);
                              this.setSelectedIndex(var10);
                              this.focus(var10);
                           }
                        } else {
                           this.stopAtomic();
                        }
                     }
                  } else {
                     var3 = var1.getFrom();
                     var2 += var1.wasAdded() ? var1.getAddedSize() : -var1.getRemovedSize();
                  }
               }

               if (var2 != 0 && var3 >= 0) {
                  ArrayList var14 = new ArrayList(this.selectedCellsMap.size());

                  TablePosition var15;
                  for(var5 = 0; var5 < this.selectedCellsMap.size(); ++var5) {
                     var15 = (TablePosition)this.selectedCellsMap.get(var5);
                     var7 = var15.getRow();
                     int var17 = Math.max(0, var7 < var3 ? var7 : var7 + var2);
                     if (var7 >= var3) {
                        if (var7 == 0 && var2 == -1) {
                           var14.add(new TablePosition(this.getTableView(), 0, var15.getTableColumn()));
                        } else {
                           var14.add(new TablePosition(this.getTableView(), var17, var15.getTableColumn()));
                        }
                     }
                  }

                  var5 = var14.size();
                  if ((var1.wasRemoved() || var1.wasAdded()) && var5 > 0) {
                     var15 = (TablePosition)TableCellBehavior.getAnchor(this.tableView, (Object)null);
                     TablePosition var19;
                     if (var15 != null) {
                        boolean var18 = this.isSelected(var15.getRow(), var15.getTableColumn());
                        if (var18) {
                           var19 = new TablePosition(this.tableView, var15.getRow() + var2, var15.getTableColumn());
                           TableCellBehavior.setAnchor(this.tableView, var19, false);
                        }
                     }

                     this.quietClearSelection();
                     this.blockFocusCall = true;

                     for(var7 = 0; var7 < var5; ++var7) {
                        var19 = (TablePosition)var14.get(var7);
                        this.select(var19.getRow(), var19.getTableColumn());
                     }

                     this.blockFocusCall = false;
                  }
               }

               this.previousModelSize = this.getItemCount();
               return;
            }
         }
      }

      public void clearAndSelect(int var1) {
         this.clearAndSelect(var1, (TableColumn)null);
      }

      public void clearAndSelect(int var1, TableColumn var2) {
         if (var1 >= 0 && var1 < this.getItemCount()) {
            TablePosition var3 = new TablePosition(this.getTableView(), var1, var2);
            boolean var4 = this.isCellSelectionEnabled();
            TableCellBehavior.setAnchor(this.tableView, var3, false);
            if (!var4 || var2 != null) {
               boolean var5 = this.isSelected(var1, var2);
               ArrayList var6 = new ArrayList(this.selectedCellsMap.getSelectedCells());
               if (var5 && var6.size() == 1) {
                  TablePosition var7 = (TablePosition)this.getSelectedCells().get(0);
                  if (this.getSelectedItem() == this.getModelItem(var1) && var7.getRow() == var1 && var7.getTableColumn() == var2) {
                     return;
                  }
               }

               this.startAtomic();
               this.clearSelection();
               this.select(var1, var2);
               this.stopAtomic();
               if (var4) {
                  var6.remove(var3);
               } else {
                  Iterator var9 = var6.iterator();

                  while(var9.hasNext()) {
                     TablePosition var8 = (TablePosition)var9.next();
                     if (var8.getRow() == var1) {
                        var6.remove(var8);
                        break;
                     }
                  }
               }

               Object var10;
               if (var5) {
                  var10 = ControlUtils.buildClearAndSelectChange(this.selectedCellsSeq, var6, var1);
               } else {
                  int var11 = this.selectedCellsSeq.indexOf(var3);
                  var10 = new NonIterableChange.GenericAddRemoveChange(var11, var11 + 1, var6, this.selectedCellsSeq);
               }

               this.handleSelectedCellsListChangeEvent((ListChangeListener.Change)var10);
            }
         }
      }

      public void select(int var1) {
         this.select(var1, (TableColumn)null);
      }

      public void select(int var1, TableColumn var2) {
         if (var1 >= 0 && var1 < this.getItemCount()) {
            if (this.isCellSelectionEnabled() && var2 == null) {
               ObservableList var5 = this.getTableView().getVisibleLeafColumns();

               for(int var4 = 0; var4 < var5.size(); ++var4) {
                  this.select(var1, (TableColumn)var5.get(var4));
               }

            } else {
               TablePosition var3 = new TablePosition(this.getTableView(), var1, var2);
               if (this.getSelectionMode() == SelectionMode.SINGLE) {
                  this.startAtomic();
                  this.quietClearSelection();
                  this.stopAtomic();
               }

               if (TableCellBehavior.hasDefaultAnchor(this.tableView)) {
                  TableCellBehavior.removeAnchor(this.tableView);
               }

               this.selectedCellsMap.add(var3);
               this.updateSelectedIndex(var1);
               this.focus(var1, var2);
            }
         }
      }

      public void select(Object var1) {
         if (var1 == null && this.getSelectionMode() == SelectionMode.SINGLE) {
            this.clearSelection();
         } else {
            Object var2 = null;

            for(int var3 = 0; var3 < this.getItemCount(); ++var3) {
               var2 = this.getModelItem(var3);
               if (var2 != null && var2.equals(var1)) {
                  if (this.isSelected(var3)) {
                     return;
                  }

                  if (this.getSelectionMode() == SelectionMode.SINGLE) {
                     this.quietClearSelection();
                  }

                  this.select(var3);
                  return;
               }
            }

            this.setSelectedIndex(-1);
            this.setSelectedItem(var1);
         }
      }

      public void selectIndices(int var1, int... var2) {
         if (var2 == null) {
            this.select(var1);
         } else {
            int var3 = this.getItemCount();
            int var4;
            if (this.getSelectionMode() == SelectionMode.SINGLE) {
               this.quietClearSelection();

               for(var4 = var2.length - 1; var4 >= 0; --var4) {
                  int var5 = var2[var4];
                  if (var5 >= 0 && var5 < var3) {
                     this.select(var5);
                     break;
                  }
               }

               if (this.selectedCellsMap.isEmpty() && var1 > 0 && var1 < var3) {
                  this.select(var1);
               }
            } else {
               var4 = -1;
               LinkedHashSet var10 = new LinkedHashSet();
               int var7;
               if (var1 >= 0 && var1 < var3) {
                  if (this.isCellSelectionEnabled()) {
                     ObservableList var6 = this.getTableView().getVisibleLeafColumns();

                     for(var7 = 0; var7 < var6.size(); ++var7) {
                        if (!this.selectedCellsMap.isSelected(var1, var7)) {
                           var10.add(new TablePosition(this.getTableView(), var1, (TableColumn)var6.get(var7)));
                        }
                     }
                  } else {
                     boolean var11 = this.selectedCellsMap.isSelected(var1, -1);
                     if (!var11) {
                        var10.add(new TablePosition(this.getTableView(), var1, (TableColumn)null));
                     }
                  }

                  var4 = var1;
               }

               for(int var12 = 0; var12 < var2.length; ++var12) {
                  var7 = var2[var12];
                  if (var7 >= 0 && var7 < var3) {
                     var4 = var7;
                     if (this.isCellSelectionEnabled()) {
                        ObservableList var8 = this.getTableView().getVisibleLeafColumns();

                        for(int var9 = 0; var9 < var8.size(); ++var9) {
                           if (!this.selectedCellsMap.isSelected(var7, var9)) {
                              var10.add(new TablePosition(this.getTableView(), var7, (TableColumn)var8.get(var9)));
                              var4 = var7;
                           }
                        }
                     } else if (!this.selectedCellsMap.isSelected(var7, -1)) {
                        var10.add(new TablePosition(this.getTableView(), var7, (TableColumn)null));
                     }
                  }
               }

               this.selectedCellsMap.addAll(var10);
               if (var4 != -1) {
                  this.select(var4);
               }
            }

         }
      }

      public void selectAll() {
         if (this.getSelectionMode() != SelectionMode.SINGLE) {
            ArrayList var1;
            if (this.isCellSelectionEnabled()) {
               var1 = new ArrayList();
               TablePosition var3 = null;

               for(int var4 = 0; var4 < this.getTableView().getVisibleLeafColumns().size(); ++var4) {
                  TableColumn var2 = (TableColumn)this.getTableView().getVisibleLeafColumns().get(var4);

                  for(int var5 = 0; var5 < this.getItemCount(); ++var5) {
                     var3 = new TablePosition(this.getTableView(), var5, var2);
                     var1.add(var3);
                  }
               }

               this.selectedCellsMap.setAll(var1);
               if (var3 != null) {
                  this.select(var3.getRow(), var3.getTableColumn());
                  this.focus(var3.getRow(), var3.getTableColumn());
               }
            } else {
               var1 = new ArrayList();

               int var6;
               for(var6 = 0; var6 < this.getItemCount(); ++var6) {
                  var1.add(new TablePosition(this.getTableView(), var6, (TableColumn)null));
               }

               this.selectedCellsMap.setAll(var1);
               var6 = this.getFocusedIndex();
               if (var6 == -1) {
                  int var7 = this.getItemCount();
                  if (var7 > 0) {
                     this.select(var7 - 1);
                     this.focus((TablePosition)var1.get(var1.size() - 1));
                  }
               } else {
                  this.select(var6);
                  this.focus(var6);
               }
            }

         }
      }

      public void selectRange(int var1, TableColumnBase var2, int var3, TableColumnBase var4) {
         if (this.getSelectionMode() == SelectionMode.SINGLE) {
            this.quietClearSelection();
            this.select(var3, var4);
         } else {
            this.startAtomic();
            int var5 = this.getItemCount();
            boolean var6 = this.isCellSelectionEnabled();
            int var7 = this.tableView.getVisibleLeafIndex((TableColumn)var2);
            int var8 = this.tableView.getVisibleLeafIndex((TableColumn)var4);
            int var9 = Math.min(var7, var8);
            int var10 = Math.max(var7, var8);
            int var11 = Math.min(var1, var3);
            int var12 = Math.max(var1, var3);
            ArrayList var13 = new ArrayList();

            for(int var14 = var11; var14 <= var12; ++var14) {
               if (var14 >= 0 && var14 < var5) {
                  if (!var6) {
                     var13.add(new TablePosition(this.tableView, var14, (TableColumn)var2));
                  } else {
                     for(int var15 = var9; var15 <= var10; ++var15) {
                        TableColumn var16 = this.tableView.getVisibleLeafColumn(var15);
                        if (var16 != null || !var6) {
                           var13.add(new TablePosition(this.tableView, var14, var16));
                        }
                     }
                  }
               }
            }

            var13.removeAll(this.getSelectedCells());
            this.selectedCellsMap.addAll(var13);
            this.stopAtomic();
            this.updateSelectedIndex(var3);
            this.focus(var3, (TableColumn)var4);
            TableColumn var21 = (TableColumn)var2;
            TableColumn var22 = var6 ? (TableColumn)var4 : var21;
            int var23 = this.selectedCellsMap.indexOf(new TablePosition(this.tableView, var1, var21));
            int var17 = this.selectedCellsMap.indexOf(new TablePosition(this.tableView, var3, var22));
            if (var23 > -1 && var17 > -1) {
               int var18 = Math.min(var23, var17);
               int var19 = Math.max(var23, var17);
               NonIterableChange.SimpleAddChange var20 = new NonIterableChange.SimpleAddChange(var18, var19 + 1, this.selectedCellsSeq);
               this.handleSelectedCellsListChangeEvent(var20);
            }

         }
      }

      public void clearSelection(int var1) {
         this.clearSelection(var1, (TableColumn)null);
      }

      public void clearSelection(int var1, TableColumn var2) {
         this.clearSelection(new TablePosition(this.getTableView(), var1, var2));
      }

      private void clearSelection(TablePosition var1) {
         boolean var2 = this.isCellSelectionEnabled();
         int var3 = var1.getRow();
         Iterator var4 = this.getSelectedCells().iterator();

         while(var4.hasNext()) {
            TablePosition var5 = (TablePosition)var4.next();
            if (!var2) {
               if (var5.getRow() == var3) {
                  this.selectedCellsMap.remove(var5);
                  break;
               }
            } else if (var5.equals(var1)) {
               this.selectedCellsMap.remove(var1);
               break;
            }
         }

         if (this.isEmpty() && !this.isAtomic()) {
            this.updateSelectedIndex(-1);
            this.selectedCellsMap.clear();
         }

      }

      public void clearSelection() {
         final ArrayList var1 = new ArrayList(this.getSelectedCells());
         this.quietClearSelection();
         if (!this.isAtomic()) {
            this.updateSelectedIndex(-1);
            this.focus(-1);
            if (!var1.isEmpty()) {
               NonIterableChange var2 = new NonIterableChange(0, 0, this.selectedCellsSeq) {
                  public List getRemoved() {
                     return var1;
                  }
               };
               this.handleSelectedCellsListChangeEvent(var2);
            }
         }

      }

      private void quietClearSelection() {
         this.startAtomic();
         this.selectedCellsMap.clear();
         this.stopAtomic();
      }

      public boolean isSelected(int var1) {
         return this.isSelected(var1, (TableColumn)null);
      }

      public boolean isSelected(int var1, TableColumn var2) {
         boolean var3 = this.isCellSelectionEnabled();
         if (var3 && var2 == null) {
            return false;
         } else {
            int var4 = var3 && var2 != null ? this.tableView.getVisibleLeafIndex(var2) : -1;
            return this.selectedCellsMap.isSelected(var1, var4);
         }
      }

      public boolean isEmpty() {
         return this.selectedCellsMap.isEmpty();
      }

      public void selectPrevious() {
         if (this.isCellSelectionEnabled()) {
            TablePosition var1 = this.getFocusedCell();
            if (var1.getColumn() - 1 >= 0) {
               this.select(var1.getRow(), this.getTableColumn(var1.getTableColumn(), -1));
            } else if (var1.getRow() < this.getItemCount() - 1) {
               this.select(var1.getRow() - 1, this.getTableColumn(this.getTableView().getVisibleLeafColumns().size() - 1));
            }
         } else {
            int var2 = this.getFocusedIndex();
            if (var2 == -1) {
               this.select(this.getItemCount() - 1);
            } else if (var2 > 0) {
               this.select(var2 - 1);
            }
         }

      }

      public void selectNext() {
         if (this.isCellSelectionEnabled()) {
            TablePosition var1 = this.getFocusedCell();
            if (var1.getColumn() + 1 < this.getTableView().getVisibleLeafColumns().size()) {
               this.select(var1.getRow(), this.getTableColumn(var1.getTableColumn(), 1));
            } else if (var1.getRow() < this.getItemCount() - 1) {
               this.select(var1.getRow() + 1, this.getTableColumn(0));
            }
         } else {
            int var2 = this.getFocusedIndex();
            if (var2 == -1) {
               this.select(0);
            } else if (var2 < this.getItemCount() - 1) {
               this.select(var2 + 1);
            }
         }

      }

      public void selectAboveCell() {
         TablePosition var1 = this.getFocusedCell();
         if (var1.getRow() == -1) {
            this.select(this.getItemCount() - 1);
         } else if (var1.getRow() > 0) {
            this.select(var1.getRow() - 1, var1.getTableColumn());
         }

      }

      public void selectBelowCell() {
         TablePosition var1 = this.getFocusedCell();
         if (var1.getRow() == -1) {
            this.select(0);
         } else if (var1.getRow() < this.getItemCount() - 1) {
            this.select(var1.getRow() + 1, var1.getTableColumn());
         }

      }

      public void selectFirst() {
         TablePosition var1 = this.getFocusedCell();
         if (this.getSelectionMode() == SelectionMode.SINGLE) {
            this.quietClearSelection();
         }

         if (this.getItemCount() > 0) {
            if (this.isCellSelectionEnabled()) {
               this.select(0, var1.getTableColumn());
            } else {
               this.select(0);
            }
         }

      }

      public void selectLast() {
         TablePosition var1 = this.getFocusedCell();
         if (this.getSelectionMode() == SelectionMode.SINGLE) {
            this.quietClearSelection();
         }

         int var2 = this.getItemCount();
         if (var2 > 0 && this.getSelectedIndex() < var2 - 1) {
            if (this.isCellSelectionEnabled()) {
               this.select(var2 - 1, var1.getTableColumn());
            } else {
               this.select(var2 - 1);
            }
         }

      }

      public void selectLeftCell() {
         if (this.isCellSelectionEnabled()) {
            TablePosition var1 = this.getFocusedCell();
            if (var1.getColumn() - 1 >= 0) {
               this.select(var1.getRow(), this.getTableColumn(var1.getTableColumn(), -1));
            }

         }
      }

      public void selectRightCell() {
         if (this.isCellSelectionEnabled()) {
            TablePosition var1 = this.getFocusedCell();
            if (var1.getColumn() + 1 < this.getTableView().getVisibleLeafColumns().size()) {
               this.select(var1.getRow(), this.getTableColumn(var1.getTableColumn(), 1));
            }

         }
      }

      private void updateItemsObserver(ObservableList var1, ObservableList var2) {
         if (var1 != null) {
            var1.removeListener(this.weakItemsContentListener);
         }

         if (var2 != null) {
            var2.addListener(this.weakItemsContentListener);
         }

         this.updateItemCount();
         this.updateDefaultSelection();
      }

      private void updateDefaultSelection() {
         int var1 = -1;
         int var2 = -1;
         if (this.tableView.getItems() != null) {
            Object var3 = this.getSelectedItem();
            if (var3 != null) {
               var1 = this.tableView.getItems().indexOf(var3);
            }

            if (var2 == -1) {
               var2 = this.tableView.getItems().size() > 0 ? 0 : -1;
            }
         }

         this.clearSelection();
         this.select(var1, this.isCellSelectionEnabled() ? this.getTableColumn(0) : null);
         this.focus(var2, this.isCellSelectionEnabled() ? this.getTableColumn(0) : null);
      }

      private TableColumn getTableColumn(int var1) {
         return this.getTableView().getVisibleLeafColumn(var1);
      }

      private TableColumn getTableColumn(TableColumn var1, int var2) {
         int var3 = this.getTableView().getVisibleLeafIndex(var1);
         int var4 = var3 + var2;
         return this.getTableView().getVisibleLeafColumn(var4);
      }

      private void updateSelectedIndex(int var1) {
         this.setSelectedIndex(var1);
         this.setSelectedItem(this.getModelItem(var1));
      }

      protected int getItemCount() {
         return this.itemCount;
      }

      private void updateItemCount() {
         if (this.tableView == null) {
            this.itemCount = -1;
         } else {
            List var1 = this.getTableModel();
            this.itemCount = var1 == null ? -1 : var1.size();
         }

      }

      private void handleSelectedCellsListChangeEvent(ListChangeListener.Change var1) {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();

         while(true) {
            List var4;
            int var5;
            TablePosition var6;
            int var7;
            do {
               if (!var1.next()) {
                  var1.reset();
                  this.selectedIndicesSeq.reset();
                  if (this.isAtomic()) {
                     return;
                  }

                  var1.next();
                  boolean var14;
                  if (var1.wasReplaced()) {
                     var5 = var1.getRemovedSize();
                     int var15 = var1.getAddedSize();
                     if (var5 != var15) {
                        var14 = true;
                     } else {
                        var7 = 0;

                        while(true) {
                           if (var7 >= var5) {
                              var14 = false;
                              break;
                           }

                           TablePosition var8 = (TablePosition)var1.getRemoved().get(var7);
                           Object var9 = var8.getItem();
                           boolean var10 = false;

                           for(int var11 = 0; var11 < var15; ++var11) {
                              TablePosition var12 = (TablePosition)var1.getAddedSubList().get(var11);
                              Object var13 = var12.getItem();
                              if (var9.equals(var13)) {
                                 var10 = true;
                                 break;
                              }
                           }

                           if (!var10) {
                              var14 = true;
                              break;
                           }

                           ++var7;
                        }
                     }
                  } else {
                     var14 = true;
                  }

                  if (var14) {
                     if (this.selectedItemChange != null) {
                        this.selectedItems.callObservers(this.selectedItemChange);
                     } else {
                        this.selectedItems.callObservers(new MappingChange(var1, this.cellToItemsMap, this.selectedItems));
                     }
                  }

                  var1.reset();
                  if (this.selectedItems.isEmpty() && this.getSelectedItem() != null) {
                     this.setSelectedItem((Object)null);
                  }

                  ReadOnlyUnbackedObservableList var16 = (ReadOnlyUnbackedObservableList)this.getSelectedIndices();
                  if (!var2.isEmpty() && var3.isEmpty()) {
                     ListChangeListener.Change var17 = createRangeChange(var16, var2, false);
                     var16.callObservers(var17);
                  } else {
                     var16.callObservers(new MappingChange(var1, this.cellToIndicesMap, var16));
                     var1.reset();
                  }

                  this.selectedCellsSeq.callObservers(new MappingChange(var1, MappingChange.NOOP_MAP, this.selectedCellsSeq));
                  var1.reset();
                  return;
               }

               if (var1.wasRemoved()) {
                  var4 = var1.getRemoved();

                  for(var5 = 0; var5 < var4.size(); ++var5) {
                     var6 = (TablePosition)var4.get(var5);
                     var7 = var6.getRow();
                     if (this.selectedIndices.get(var7)) {
                        this.selectedIndices.clear(var7);
                        var3.add(var7);
                     }
                  }
               }
            } while(!var1.wasAdded());

            var4 = var1.getAddedSubList();

            for(var5 = 0; var5 < var4.size(); ++var5) {
               var6 = (TablePosition)var4.get(var5);
               var7 = var6.getRow();
               if (!this.selectedIndices.get(var7)) {
                  this.selectedIndices.set(var7);
                  var2.add(var7);
               }
            }
         }
      }
   }

   public abstract static class TableViewSelectionModel extends TableSelectionModel {
      private final TableView tableView;
      boolean blockFocusCall = false;

      public TableViewSelectionModel(TableView var1) {
         if (var1 == null) {
            throw new NullPointerException("TableView can not be null");
         } else {
            this.tableView = var1;
         }
      }

      public abstract ObservableList getSelectedCells();

      public boolean isSelected(int var1, TableColumnBase var2) {
         return this.isSelected(var1, (TableColumn)var2);
      }

      public abstract boolean isSelected(int var1, TableColumn var2);

      public void select(int var1, TableColumnBase var2) {
         this.select(var1, (TableColumn)var2);
      }

      public abstract void select(int var1, TableColumn var2);

      public void clearAndSelect(int var1, TableColumnBase var2) {
         this.clearAndSelect(var1, (TableColumn)var2);
      }

      public abstract void clearAndSelect(int var1, TableColumn var2);

      public void clearSelection(int var1, TableColumnBase var2) {
         this.clearSelection(var1, (TableColumn)var2);
      }

      public abstract void clearSelection(int var1, TableColumn var2);

      public void selectRange(int var1, TableColumnBase var2, int var3, TableColumnBase var4) {
         int var5 = this.tableView.getVisibleLeafIndex((TableColumn)var2);
         int var6 = this.tableView.getVisibleLeafIndex((TableColumn)var4);

         for(int var7 = var1; var7 <= var3; ++var7) {
            for(int var8 = var5; var8 <= var6; ++var8) {
               this.select(var7, this.tableView.getVisibleLeafColumn(var8));
            }
         }

      }

      public TableView getTableView() {
         return this.tableView;
      }

      protected List getTableModel() {
         return this.tableView.getItems();
      }

      protected Object getModelItem(int var1) {
         return var1 >= 0 && var1 < this.getItemCount() ? this.tableView.getItems().get(var1) : null;
      }

      protected int getItemCount() {
         return this.getTableModel().size();
      }

      public void focus(int var1) {
         this.focus(var1, (TableColumn)null);
      }

      public int getFocusedIndex() {
         return this.getFocusedCell().getRow();
      }

      void focus(int var1, TableColumn var2) {
         this.focus(new TablePosition(this.getTableView(), var1, var2));
         this.getTableView().notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_ITEM);
      }

      void focus(TablePosition var1) {
         if (!this.blockFocusCall) {
            if (this.getTableView().getFocusModel() != null) {
               this.getTableView().getFocusModel().focus(var1.getRow(), var1.getTableColumn());
            }
         }
      }

      TablePosition getFocusedCell() {
         return this.getTableView().getFocusModel() == null ? new TablePosition(this.getTableView(), -1, (TableColumn)null) : this.getTableView().getFocusModel().getFocusedCell();
      }
   }

   public static class ResizeFeatures extends ResizeFeaturesBase {
      private TableView table;

      public ResizeFeatures(TableView var1, TableColumn var2, Double var3) {
         super(var2, var3);
         this.table = var1;
      }

      public TableColumn getColumn() {
         return (TableColumn)super.getColumn();
      }

      public TableView getTable() {
         return this.table;
      }
   }

   private static class StyleableProperties {
      private static final CssMetaData FIXED_CELL_SIZE = new CssMetaData("-fx-fixed-cell-size", SizeConverter.getInstance(), -1.0) {
         public Double getInitialValue(TableView var1) {
            return var1.getFixedCellSize();
         }

         public boolean isSettable(TableView var1) {
            return var1.fixedCellSize == null || !var1.fixedCellSize.isBound();
         }

         public StyleableProperty getStyleableProperty(TableView var1) {
            return (StyleableProperty)var1.fixedCellSizeProperty();
         }
      };
      private static final List STYLEABLES;

      static {
         ArrayList var0 = new ArrayList(Control.getClassCssMetaData());
         var0.add(FIXED_CELL_SIZE);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
