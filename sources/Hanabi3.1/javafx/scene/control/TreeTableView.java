package javafx.scene.control;

import com.sun.javafx.collections.MappingChange;
import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
import com.sun.javafx.scene.control.SelectedCellsMap;
import com.sun.javafx.scene.control.TableColumnComparatorBase;
import com.sun.javafx.scene.control.behavior.TableCellBehaviorBase;
import com.sun.javafx.scene.control.behavior.TreeTableCellBehavior;
import com.sun.javafx.scene.control.skin.TreeTableViewSkin;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javafx.application.Platform;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.event.WeakEventHandler;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.util.Callback;

@DefaultProperty("root")
public class TreeTableView extends Control {
   private static final EventType EDIT_ANY_EVENT;
   private static final EventType EDIT_START_EVENT;
   private static final EventType EDIT_CANCEL_EVENT;
   private static final EventType EDIT_COMMIT_EVENT;
   public static final Callback UNCONSTRAINED_RESIZE_POLICY;
   public static final Callback CONSTRAINED_RESIZE_POLICY;
   public static final Callback DEFAULT_SORT_POLICY;
   private boolean expandedItemCountDirty;
   private Map treeItemCacheMap;
   private final ObservableList columns;
   private final ObservableList visibleLeafColumns;
   private final ObservableList unmodifiableVisibleLeafColumns;
   private ObservableList sortOrder;
   double contentWidth;
   private boolean isInited;
   private final EventHandler rootEvent;
   private final ListChangeListener columnsObserver;
   private final WeakHashMap lastKnownColumnIndex;
   private final InvalidationListener columnVisibleObserver;
   private final InvalidationListener columnSortableObserver;
   private final InvalidationListener columnSortTypeObserver;
   private final InvalidationListener columnComparatorObserver;
   private final InvalidationListener cellSelectionModelInvalidationListener;
   private WeakEventHandler weakRootEventListener;
   private final WeakInvalidationListener weakColumnVisibleObserver;
   private final WeakInvalidationListener weakColumnSortableObserver;
   private final WeakInvalidationListener weakColumnSortTypeObserver;
   private final WeakInvalidationListener weakColumnComparatorObserver;
   private final WeakListChangeListener weakColumnsObserver;
   private final WeakInvalidationListener weakCellSelectionModelInvalidationListener;
   private ObjectProperty root;
   private BooleanProperty showRoot;
   private ObjectProperty treeColumn;
   private ObjectProperty selectionModel;
   private ObjectProperty focusModel;
   private ReadOnlyIntegerWrapper expandedItemCount;
   private BooleanProperty editable;
   private ReadOnlyObjectWrapper editingCell;
   private BooleanProperty tableMenuButtonVisible;
   private ObjectProperty columnResizePolicy;
   private ObjectProperty rowFactory;
   private ObjectProperty placeholder;
   private DoubleProperty fixedCellSize;
   private ObjectProperty sortMode;
   private ReadOnlyObjectWrapper comparator;
   private ObjectProperty sortPolicy;
   private ObjectProperty onSort;
   private ObjectProperty onScrollTo;
   private ObjectProperty onScrollToColumn;
   private boolean sortingInProgress;
   private boolean sortLock;
   private TableUtil.SortEventType lastSortEventType;
   private Object[] lastSortEventSupportInfo;
   private static final String DEFAULT_STYLE_CLASS = "tree-table-view";
   private static final PseudoClass PSEUDO_CLASS_CELL_SELECTION;
   private static final PseudoClass PSEUDO_CLASS_ROW_SELECTION;

   public TreeTableView() {
      this((TreeItem)null);
   }

   public TreeTableView(TreeItem var1) {
      this.expandedItemCountDirty = true;
      this.treeItemCacheMap = new HashMap();
      this.columns = FXCollections.observableArrayList();
      this.visibleLeafColumns = FXCollections.observableArrayList();
      this.unmodifiableVisibleLeafColumns = FXCollections.unmodifiableObservableList(this.visibleLeafColumns);
      this.sortOrder = FXCollections.observableArrayList();
      this.isInited = false;
      this.rootEvent = (var1x) -> {
         EventType var2 = var1x.getEventType();

         boolean var3;
         for(var3 = false; var2 != null; var2 = var2.getSuperType()) {
            if (var2.equals(TreeItem.expandedItemCountChangeEvent())) {
               var3 = true;
               break;
            }
         }

         if (var3) {
            this.expandedItemCountDirty = true;
            this.requestLayout();
         }

      };
      this.columnsObserver = new ListChangeListener() {
         public void onChanged(ListChangeListener.Change var1) {
            ObservableList var2 = TreeTableView.this.getColumns();

            ArrayList var3;
            label188:
            do {
               do {
                  if (!var1.next()) {
                     var1.reset();
                     TreeTableView.this.updateVisibleLeafColumns();
                     var3 = new ArrayList();

                     Iterator var20;
                     TreeTableColumn var21;
                     while(var1.next()) {
                        List var14 = var1.getRemoved();
                        List var17 = var1.getAddedSubList();
                        if (var1.wasRemoved()) {
                           var3.addAll(var14);
                           var20 = var14.iterator();

                           while(var20.hasNext()) {
                              var21 = (TreeTableColumn)var20.next();
                              var21.setTreeTableView((TreeTableView)null);
                           }
                        }

                        if (var1.wasAdded()) {
                           var3.removeAll(var17);
                           var20 = var17.iterator();

                           while(var20.hasNext()) {
                              var21 = (TreeTableColumn)var20.next();
                              var21.setTreeTableView(TreeTableView.this);
                           }
                        }

                        TableUtil.removeColumnsListener(var14, TreeTableView.this.weakColumnsObserver);
                        TableUtil.addColumnsListener(var17, TreeTableView.this.weakColumnsObserver);
                        TableUtil.removeTableColumnListener(var1.getRemoved(), TreeTableView.this.weakColumnVisibleObserver, TreeTableView.this.weakColumnSortableObserver, TreeTableView.this.weakColumnSortTypeObserver, TreeTableView.this.weakColumnComparatorObserver);
                        TableUtil.addTableColumnListener(var1.getAddedSubList(), TreeTableView.this.weakColumnVisibleObserver, TreeTableView.this.weakColumnSortableObserver, TreeTableView.this.weakColumnSortTypeObserver, TreeTableView.this.weakColumnComparatorObserver);
                     }

                     TreeTableView.this.sortOrder.removeAll(var3);
                     TreeTableViewFocusModel var15 = TreeTableView.this.getFocusModel();
                     TreeTableViewSelectionModel var18 = TreeTableView.this.getSelectionModel();
                     var1.reset();

                     while(true) {
                        List var22;
                        do {
                           do {
                              if (!var1.next()) {
                                 TreeTableView.this.lastKnownColumnIndex.clear();
                                 var20 = TreeTableView.this.getColumns().iterator();

                                 while(var20.hasNext()) {
                                    var21 = (TreeTableColumn)var20.next();
                                    int var28 = TreeTableView.this.getVisibleLeafIndex(var21);
                                    if (var28 > -1) {
                                       TreeTableView.this.lastKnownColumnIndex.put(var21, var28);
                                    }
                                 }

                                 return;
                              }
                           } while(!var1.wasRemoved());

                           var22 = var1.getRemoved();
                           if (var15 != null) {
                              TreeTablePosition var23 = var15.getFocusedCell();
                              boolean var24 = false;
                              Iterator var9 = var22.iterator();

                              while(var9.hasNext()) {
                                 TreeTableColumn var10 = (TreeTableColumn)var9.next();
                                 var24 = var23 != null && var23.getTableColumn() == var10;
                                 if (var24) {
                                    break;
                                 }
                              }

                              if (var24) {
                                 int var27 = (Integer)TreeTableView.this.lastKnownColumnIndex.getOrDefault(var23.getTableColumn(), 0);
                                 int var30 = var27 == 0 ? 0 : Math.min(TreeTableView.this.getVisibleLeafColumns().size() - 1, var27 - 1);
                                 var15.focus(var23.getRow(), TreeTableView.this.getVisibleLeafColumn(var30));
                              }
                           }
                        } while(var18 == null);

                        ArrayList var25 = new ArrayList(var18.getSelectedCells());
                        Iterator var26 = var25.iterator();

                        while(var26.hasNext()) {
                           TreeTablePosition var29 = (TreeTablePosition)var26.next();
                           boolean var31 = false;
                           Iterator var11 = var22.iterator();

                           while(var11.hasNext()) {
                              TreeTableColumn var12 = (TreeTableColumn)var11.next();
                              var31 = var29 != null && var29.getTableColumn() == var12;
                              if (var31) {
                                 break;
                              }
                           }

                           if (var31) {
                              int var32 = (Integer)TreeTableView.this.lastKnownColumnIndex.getOrDefault(var29.getTableColumn(), -1);
                              if (var32 != -1) {
                                 if (var18 instanceof TreeTableViewArrayListSelectionModel) {
                                    TreeTablePosition var33 = new TreeTablePosition(TreeTableView.this, var29.getRow(), var29.getTableColumn());
                                    var33.fixedColumnIndex = var32;
                                    ((TreeTableViewArrayListSelectionModel)var18).clearSelection(var33);
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
                  TreeTableColumn var5;
                  do {
                     if (!var4.hasNext()) {
                        continue label188;
                     }

                     var5 = (TreeTableColumn)var4.next();
                  } while(var5 == null);

                  int var6 = 0;
                  Iterator var7 = var2.iterator();

                  while(var7.hasNext()) {
                     TreeTableColumn var8 = (TreeTableColumn)var7.next();
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

            TreeTableColumn var19;
            for(Iterator var16 = var3.iterator(); var16.hasNext(); var13 = var13 + "'" + var19.getText() + "', ") {
               var19 = (TreeTableColumn)var16.next();
            }

            throw new IllegalStateException("Duplicate TreeTableColumns detected in TreeTableView columns list with titles " + var13);
         }
      };
      this.lastKnownColumnIndex = new WeakHashMap();
      this.columnVisibleObserver = (var1x) -> {
         this.updateVisibleLeafColumns();
      };
      this.columnSortableObserver = (var1x) -> {
         TreeTableColumn var2 = (TreeTableColumn)((BooleanProperty)var1x).getBean();
         if (this.getSortOrder().contains(var2)) {
            this.doSort(TableUtil.SortEventType.COLUMN_SORTABLE_CHANGE, var2);
         }
      };
      this.columnSortTypeObserver = (var1x) -> {
         TreeTableColumn var2 = (TreeTableColumn)((ObjectProperty)var1x).getBean();
         if (this.getSortOrder().contains(var2)) {
            this.doSort(TableUtil.SortEventType.COLUMN_SORT_TYPE_CHANGE, var2);
         }
      };
      this.columnComparatorObserver = (var1x) -> {
         TreeTableColumn var2 = (TreeTableColumn)((SimpleObjectProperty)var1x).getBean();
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
      this.root = new SimpleObjectProperty(this, "root") {
         private WeakReference weakOldItem;

         protected void invalidated() {
            TreeItem var1 = this.weakOldItem == null ? null : (TreeItem)this.weakOldItem.get();
            if (var1 != null && TreeTableView.this.weakRootEventListener != null) {
               var1.removeEventHandler(TreeItem.treeNotificationEvent(), TreeTableView.this.weakRootEventListener);
            }

            TreeItem var2 = TreeTableView.this.getRoot();
            if (var2 != null) {
               TreeTableView.this.weakRootEventListener = new WeakEventHandler(TreeTableView.this.rootEvent);
               TreeTableView.this.getRoot().addEventHandler(TreeItem.treeNotificationEvent(), TreeTableView.this.weakRootEventListener);
               this.weakOldItem = new WeakReference(var2);
            }

            TreeTableView.this.getSortOrder().clear();
            TreeTableView.this.expandedItemCountDirty = true;
            TreeTableView.this.updateRootExpanded();
         }
      };
      this.expandedItemCount = new ReadOnlyIntegerWrapper(this, "expandedItemCount", 0);
      this.sortLock = false;
      this.lastSortEventType = null;
      this.lastSortEventSupportInfo = null;
      this.getStyleClass().setAll((Object[])("tree-table-view"));
      this.setAccessibleRole(AccessibleRole.TREE_TABLE_VIEW);
      this.setRoot(var1);
      this.updateExpandedItemCount(var1);
      this.setSelectionModel(new TreeTableViewArrayListSelectionModel(this));
      this.setFocusModel(new TreeTableViewFocusModel(this));
      this.getColumns().addListener(this.weakColumnsObserver);
      this.getSortOrder().addListener((var1x) -> {
         this.doSort(TableUtil.SortEventType.SORT_ORDER_CHANGE, var1x);
      });
      this.getProperties().addListener((var1x) -> {
         if (var1x.wasAdded() && "TableView.contentWidth".equals(var1x.getKey())) {
            if (var1x.getValueAdded() instanceof Number) {
               this.setContentWidth((Double)var1x.getValueAdded());
            }

            this.getProperties().remove("TableView.contentWidth");
         }

      });
      this.isInited = true;
   }

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

   /** @deprecated */
   @Deprecated
   public static int getNodeLevel(TreeItem var0) {
      return TreeView.getNodeLevel(var0);
   }

   public final void setRoot(TreeItem var1) {
      this.rootProperty().set(var1);
   }

   public final TreeItem getRoot() {
      return this.root == null ? null : (TreeItem)this.root.get();
   }

   public final ObjectProperty rootProperty() {
      return this.root;
   }

   public final void setShowRoot(boolean var1) {
      this.showRootProperty().set(var1);
   }

   public final boolean isShowRoot() {
      return this.showRoot == null ? true : this.showRoot.get();
   }

   public final BooleanProperty showRootProperty() {
      if (this.showRoot == null) {
         this.showRoot = new SimpleBooleanProperty(this, "showRoot", true) {
            protected void invalidated() {
               TreeTableView.this.updateRootExpanded();
               TreeTableView.this.updateExpandedItemCount(TreeTableView.this.getRoot());
            }
         };
      }

      return this.showRoot;
   }

   public final ObjectProperty treeColumnProperty() {
      if (this.treeColumn == null) {
         this.treeColumn = new SimpleObjectProperty(this, "treeColumn", (Object)null);
      }

      return this.treeColumn;
   }

   public final void setTreeColumn(TreeTableColumn var1) {
      this.treeColumnProperty().set(var1);
   }

   public final TreeTableColumn getTreeColumn() {
      return this.treeColumn == null ? null : (TreeTableColumn)this.treeColumn.get();
   }

   public final void setSelectionModel(TreeTableViewSelectionModel var1) {
      this.selectionModelProperty().set(var1);
   }

   public final TreeTableViewSelectionModel getSelectionModel() {
      return this.selectionModel == null ? null : (TreeTableViewSelectionModel)this.selectionModel.get();
   }

   public final ObjectProperty selectionModelProperty() {
      if (this.selectionModel == null) {
         this.selectionModel = new SimpleObjectProperty(this, "selectionModel") {
            TreeTableViewSelectionModel oldValue = null;

            protected void invalidated() {
               if (this.oldValue != null) {
                  this.oldValue.cellSelectionEnabledProperty().removeListener(TreeTableView.this.weakCellSelectionModelInvalidationListener);
               }

               this.oldValue = (TreeTableViewSelectionModel)this.get();
               if (this.oldValue != null) {
                  this.oldValue.cellSelectionEnabledProperty().addListener(TreeTableView.this.weakCellSelectionModelInvalidationListener);
                  TreeTableView.this.weakCellSelectionModelInvalidationListener.invalidated(this.oldValue.cellSelectionEnabledProperty());
               }

            }
         };
      }

      return this.selectionModel;
   }

   public final void setFocusModel(TreeTableViewFocusModel var1) {
      this.focusModelProperty().set(var1);
   }

   public final TreeTableViewFocusModel getFocusModel() {
      return this.focusModel == null ? null : (TreeTableViewFocusModel)this.focusModel.get();
   }

   public final ObjectProperty focusModelProperty() {
      if (this.focusModel == null) {
         this.focusModel = new SimpleObjectProperty(this, "focusModel");
      }

      return this.focusModel;
   }

   public final ReadOnlyIntegerProperty expandedItemCountProperty() {
      return this.expandedItemCount.getReadOnlyProperty();
   }

   private void setExpandedItemCount(int var1) {
      this.expandedItemCount.set(var1);
   }

   public final int getExpandedItemCount() {
      if (this.expandedItemCountDirty) {
         this.updateExpandedItemCount(this.getRoot());
      }

      return this.expandedItemCount.get();
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

   private void setEditingCell(TreeTablePosition var1) {
      this.editingCellPropertyImpl().set(var1);
   }

   public final TreeTablePosition getEditingCell() {
      return this.editingCell == null ? null : (TreeTablePosition)this.editingCell.get();
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
               if (TreeTableView.this.isInited) {
                  ((Callback)this.get()).call(new ResizeFeatures(TreeTableView.this, (TreeTableColumn)null, 0.0));
                  PseudoClass var1;
                  if (this.oldPolicy != null) {
                     var1 = PseudoClass.getPseudoClass(this.oldPolicy.toString());
                     TreeTableView.this.pseudoClassStateChanged(var1, false);
                  }

                  if (this.get() != null) {
                     var1 = PseudoClass.getPseudoClass(((Callback)this.get()).toString());
                     TreeTableView.this.pseudoClassStateChanged(var1, true);
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
               return TreeTableView.StyleableProperties.FIXED_CELL_SIZE;
            }

            public Object getBean() {
               return TreeTableView.this;
            }

            public String getName() {
               return "fixedCellSize";
            }
         };
      }

      return this.fixedCellSize;
   }

   public final ObjectProperty sortModeProperty() {
      if (this.sortMode == null) {
         this.sortMode = new SimpleObjectProperty(this, "sortMode", TreeSortMode.ALL_DESCENDANTS);
      }

      return this.sortMode;
   }

   public final void setSortMode(TreeSortMode var1) {
      this.sortModeProperty().set(var1);
   }

   public final TreeSortMode getSortMode() {
      return this.sortMode == null ? TreeSortMode.ALL_DESCENDANTS : (TreeSortMode)this.sortMode.get();
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
               TreeTableView.this.sort();
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
               TreeTableView.this.setEventHandler(var1, var2);
            }

            public Object getBean() {
               return TreeTableView.this;
            }

            public String getName() {
               return "onSort";
            }
         };
      }

      return this.onSort;
   }

   protected void layoutChildren() {
      if (this.expandedItemCountDirty) {
         this.updateExpandedItemCount(this.getRoot());
      }

      super.layoutChildren();
   }

   public void scrollTo(int var1) {
      ControlUtils.scrollToIndex(this, var1);
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
               TreeTableView.this.setEventHandler(ScrollToEvent.scrollToTopIndex(), (EventHandler)this.get());
            }

            public Object getBean() {
               return TreeTableView.this;
            }

            public String getName() {
               return "onScrollTo";
            }
         };
      }

      return this.onScrollTo;
   }

   public void scrollToColumn(TreeTableColumn var1) {
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
               TreeTableView.this.setEventHandler(var1, (EventHandler)this.get());
            }

            public Object getBean() {
               return TreeTableView.this;
            }

            public String getName() {
               return "onScrollToColumn";
            }
         };
      }

      return this.onScrollToColumn;
   }

   public int getRow(TreeItem var1) {
      return TreeUtil.getRow(var1, this.getRoot(), this.expandedItemCountDirty, this.isShowRoot());
   }

   public TreeItem getTreeItem(int var1) {
      if (var1 < 0) {
         return null;
      } else {
         int var2 = this.isShowRoot() ? var1 : var1 + 1;
         if (this.expandedItemCountDirty) {
            this.updateExpandedItemCount(this.getRoot());
         } else if (this.treeItemCacheMap.containsKey(var2)) {
            SoftReference var3 = (SoftReference)this.treeItemCacheMap.get(var2);
            TreeItem var4 = (TreeItem)var3.get();
            if (var4 != null) {
               return var4;
            }
         }

         TreeItem var5 = TreeUtil.getItem(this.getRoot(), var2, this.expandedItemCountDirty);
         this.treeItemCacheMap.put(var2, new SoftReference(var5));
         return var5;
      }
   }

   public int getTreeItemLevel(TreeItem var1) {
      TreeItem var2 = this.getRoot();
      if (var1 == null) {
         return -1;
      } else if (var1 == var2) {
         return 0;
      } else {
         int var3 = 0;

         for(TreeItem var4 = var1.getParent(); var4 != null; var4 = var4.getParent()) {
            ++var3;
            if (var4 == var2) {
               break;
            }
         }

         return var3;
      }
   }

   public final ObservableList getColumns() {
      return this.columns;
   }

   public final ObservableList getSortOrder() {
      return this.sortOrder;
   }

   public boolean resizeColumn(TreeTableColumn var1, double var2) {
      if (var1 != null && Double.compare(var2, 0.0) != 0) {
         boolean var4 = (Boolean)this.getColumnResizePolicy().call(new ResizeFeatures(this, var1, var2));
         return var4;
      } else {
         return false;
      }
   }

   public void edit(int var1, TreeTableColumn var2) {
      if (this.isEditable() && (var2 == null || var2.isEditable())) {
         if (var1 < 0 && var2 == null) {
            this.setEditingCell((TreeTablePosition)null);
         } else {
            this.setEditingCell(new TreeTablePosition(this, var1, var2));
         }

      }
   }

   public ObservableList getVisibleLeafColumns() {
      return this.unmodifiableVisibleLeafColumns;
   }

   public int getVisibleLeafIndex(TreeTableColumn var1) {
      return this.getVisibleLeafColumns().indexOf(var1);
   }

   public TreeTableColumn getVisibleLeafColumn(int var1) {
      return var1 >= 0 && var1 < this.visibleLeafColumns.size() ? (TreeTableColumn)this.visibleLeafColumns.get(var1) : null;
   }

   boolean isSortingInProgress() {
      return this.sortingInProgress;
   }

   public void sort() {
      this.sortingInProgress = true;
      ObservableList var1 = this.getSortOrder();
      Comparator var2 = this.getComparator();
      this.setComparator(var1.isEmpty() ? null : new TableColumnComparatorBase.TreeTableColumnComparator(var1));
      SortEvent var3 = new SortEvent(this, this);
      this.fireEvent(var3);
      if (var3.isConsumed()) {
         this.sortingInProgress = false;
      } else {
         ArrayList var4 = new ArrayList(this.getSelectionModel().getSelectedCells());
         int var5 = var4.size();
         this.getSelectionModel().startAtomic();
         Callback var6 = this.getSortPolicy();
         if (var6 == null) {
            this.sortingInProgress = false;
         } else {
            Boolean var7 = (Boolean)var6.call(this);
            if (this.getSortMode() == TreeSortMode.ALL_DESCENDANTS) {
               HashSet var8 = new HashSet();
               Iterator var9 = var4.iterator();

               label67:
               while(true) {
                  TreeTablePosition var10;
                  do {
                     if (!var9.hasNext()) {
                        break label67;
                     }

                     var10 = (TreeTablePosition)var9.next();
                  } while(var10.getTreeItem() == null);

                  for(TreeItem var11 = var10.getTreeItem().getParent(); var11 != null && var8.add(var11); var11 = var11.getParent()) {
                     var11.getChildren();
                  }
               }
            }

            this.getSelectionModel().stopAtomic();
            if (var7 != null && var7) {
               if (this.getSelectionModel() instanceof TreeTableViewArrayListSelectionModel) {
                  TreeTableViewArrayListSelectionModel var13 = (TreeTableViewArrayListSelectionModel)this.getSelectionModel();
                  ObservableList var14 = var13.getSelectedCells();
                  ArrayList var15 = new ArrayList();

                  for(int var16 = 0; var16 < var5; ++var16) {
                     TreeTablePosition var12 = (TreeTablePosition)var4.get(var16);
                     if (!var14.contains(var12)) {
                        var15.add(var12);
                     }
                  }

                  if (!var15.isEmpty()) {
                     NonIterableChange.GenericAddRemoveChange var17 = new NonIterableChange.GenericAddRemoveChange(0, var5, var15, var14);
                     var13.handleSelectedCellsListChangeEvent(var17);
                  }
               }

               this.getSelectionModel().setSelectedIndex(this.getRow((TreeItem)this.getSelectionModel().getSelectedItem()));
               this.getFocusModel().focus(this.getSelectionModel().getSelectedIndex());
            } else {
               this.sortLock = true;
               TableUtil.handleSortFailure(var1, this.lastSortEventType, this.lastSortEventSupportInfo);
               this.setComparator(var2);
               this.sortLock = false;
            }

            this.sortingInProgress = false;
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

   private void updateExpandedItemCount(TreeItem var1) {
      this.setExpandedItemCount(TreeUtil.updateExpandedItemCount(var1, this.expandedItemCountDirty, this.isShowRoot()));
      if (this.expandedItemCountDirty) {
         this.treeItemCacheMap.clear();
      }

      this.expandedItemCountDirty = false;
   }

   private void updateRootExpanded() {
      if (!this.isShowRoot() && this.getRoot() != null && !this.getRoot().isExpanded()) {
         this.getRoot().setExpanded(true);
      }

   }

   private void setContentWidth(double var1) {
      this.contentWidth = var1;
      if (this.isInited) {
         this.getColumnResizePolicy().call(new ResizeFeatures(this, (TreeTableColumn)null, 0.0));
      }

   }

   private void updateVisibleLeafColumns() {
      ArrayList var1 = new ArrayList();
      this.buildVisibleLeafColumns(this.getColumns(), var1);
      this.visibleLeafColumns.setAll((Collection)var1);
      this.getColumnResizePolicy().call(new ResizeFeatures(this, (TreeTableColumn)null, 0.0));
   }

   private void buildVisibleLeafColumns(List var1, List var2) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         TreeTableColumn var4 = (TreeTableColumn)var3.next();
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
      return TreeTableView.StyleableProperties.STYLEABLES;
   }

   public List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   protected Skin createDefaultSkin() {
      return new TreeTableViewSkin(this);
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case ROW_COUNT:
            return this.getExpandedItemCount();
         case COLUMN_COUNT:
            return this.getVisibleLeafColumns().size();
         case SELECTED_ITEMS:
            ObservableList var10 = (ObservableList)super.queryAccessibleAttribute(var1, var2);
            ArrayList var11 = new ArrayList();
            Iterator var5 = var10.iterator();

            while(var5.hasNext()) {
               TreeTableRow var6 = (TreeTableRow)var5.next();
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
            TreeTableRow var8 = (TreeTableRow)super.queryAccessibleAttribute(var1, var2);
            return var8 != null ? var8.queryAccessibleAttribute(var1, var2) : null;
         case MULTIPLE_SELECTION:
            TreeTableViewSelectionModel var3 = this.getSelectionModel();
            return var3 != null && var3.getSelectionMode() == SelectionMode.MULTIPLE;
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   static {
      EDIT_ANY_EVENT = new EventType(Event.ANY, "TREE_TABLE_VIEW_EDIT");
      EDIT_START_EVENT = new EventType(editAnyEvent(), "EDIT_START");
      EDIT_CANCEL_EVENT = new EventType(editAnyEvent(), "EDIT_CANCEL");
      EDIT_COMMIT_EVENT = new EventType(editAnyEvent(), "EDIT_COMMIT");
      UNCONSTRAINED_RESIZE_POLICY = new Callback() {
         public String toString() {
            return "unconstrained-resize";
         }

         public Boolean call(ResizeFeatures var1) {
            double var2 = TableUtil.resize(var1.getColumn(), var1.getDelta());
            return Double.compare(var2, 0.0) == 0;
         }
      };
      CONSTRAINED_RESIZE_POLICY = new Callback() {
         private boolean isFirstRun = true;

         public String toString() {
            return "constrained-resize";
         }

         public Boolean call(ResizeFeatures var1) {
            TreeTableView var2 = var1.getTable();
            ObservableList var3 = var2.getVisibleLeafColumns();
            Boolean var4 = TableUtil.constrainedResize(var1, this.isFirstRun, var2.contentWidth, var3);
            this.isFirstRun = !this.isFirstRun ? false : !var4;
            return var4;
         }
      };
      DEFAULT_SORT_POLICY = new Callback() {
         public Boolean call(TreeTableView var1) {
            try {
               TreeItem var2 = var1.getRoot();
               if (var2 == null) {
                  return false;
               } else {
                  TreeSortMode var3 = var1.getSortMode();
                  if (var3 == null) {
                     return false;
                  } else {
                     var2.lastSortMode = var3;
                     var2.lastComparator = var1.getComparator();
                     var2.sort();
                     return true;
                  }
               }
            } catch (UnsupportedOperationException var4) {
               return false;
            }
         }
      };
      PSEUDO_CLASS_CELL_SELECTION = PseudoClass.getPseudoClass("cell-selection");
      PSEUDO_CLASS_ROW_SELECTION = PseudoClass.getPseudoClass("row-selection");
   }

   public static class TreeTableViewFocusModel extends TableFocusModel {
      private final TreeTableView treeTableView;
      private final TreeTablePosition EMPTY_CELL;
      private final ChangeListener rootPropertyListener = (var1x, var2x, var3x) -> {
         this.updateTreeEventListener(var2x, var3x);
      };
      private final WeakChangeListener weakRootPropertyListener;
      private EventHandler treeItemListener;
      private WeakEventHandler weakTreeItemListener;
      private ReadOnlyObjectWrapper focusedCell;

      public TreeTableViewFocusModel(TreeTableView var1) {
         this.weakRootPropertyListener = new WeakChangeListener(this.rootPropertyListener);
         this.treeItemListener = new EventHandler() {
            public void handle(TreeItem.TreeModificationEvent var1) {
               if (TreeTableViewFocusModel.this.getFocusedIndex() != -1) {
                  int var2 = 0;
                  if (var1.getChange() != null) {
                     var1.getChange().next();
                  }

                  int var4;
                  do {
                     int var3 = TreeTableViewFocusModel.this.treeTableView.getRow(var1.getTreeItem());
                     if (var1.wasExpanded()) {
                        if (var3 < TreeTableViewFocusModel.this.getFocusedIndex()) {
                           var2 += var1.getTreeItem().getExpandedDescendentCount(false) - 1;
                        }
                     } else if (var1.wasCollapsed()) {
                        if (var3 < TreeTableViewFocusModel.this.getFocusedIndex()) {
                           var2 += -var1.getTreeItem().previousExpandedDescendentCount + 1;
                        }
                     } else if (var1.wasAdded()) {
                        TreeItem var8 = var1.getTreeItem();
                        if (var8.isExpanded()) {
                           for(int var9 = 0; var9 < var1.getAddedChildren().size(); ++var9) {
                              TreeItem var6 = (TreeItem)var1.getAddedChildren().get(var9);
                              var3 = TreeTableViewFocusModel.this.treeTableView.getRow(var6);
                              if (var6 != null && var3 <= TreeTableViewFocusModel.this.getFocusedIndex()) {
                                 var2 += var6.getExpandedDescendentCount(false);
                              }
                           }
                        }
                     } else if (var1.wasRemoved()) {
                        var3 += var1.getFrom() + 1;

                        for(var4 = 0; var4 < var1.getRemovedChildren().size(); ++var4) {
                           TreeItem var5 = (TreeItem)var1.getRemovedChildren().get(var4);
                           if (var5 != null && var5.equals(TreeTableViewFocusModel.this.getFocusedItem())) {
                              TreeTableViewFocusModel.this.focus(Math.max(0, TreeTableViewFocusModel.this.getFocusedIndex() - 1));
                              return;
                           }
                        }

                        if (var3 <= TreeTableViewFocusModel.this.getFocusedIndex()) {
                           var2 += var1.getTreeItem().isExpanded() ? -var1.getRemovedSize() : 0;
                        }
                     }
                  } while(var1.getChange() != null && var1.getChange().next());

                  if (var2 != 0) {
                     TreeTablePosition var7 = TreeTableViewFocusModel.this.getFocusedCell();
                     var4 = var7.getRow() + var2;
                     if (var4 >= 0) {
                        Platform.runLater(() -> {
                           TreeTableViewFocusModel.this.focus(var4, var7.getTableColumn());
                        });
                     }
                  }

               }
            }
         };
         if (var1 == null) {
            throw new NullPointerException("TableView can not be null");
         } else {
            this.treeTableView = var1;
            this.EMPTY_CELL = new TreeTablePosition(var1, -1, (TreeTableColumn)null);
            this.treeTableView.rootProperty().addListener(this.weakRootPropertyListener);
            this.updateTreeEventListener((TreeItem)null, var1.getRoot());
            int var2 = this.getItemCount() > 0 ? 0 : -1;
            TreeTablePosition var3 = new TreeTablePosition(var1, var2, (TreeTableColumn)null);
            this.setFocusedCell(var3);
            var1.showRootProperty().addListener((var1x) -> {
               if (this.isFocused(0)) {
                  this.focus(-1);
                  this.focus(0);
               }

            });
         }
      }

      private void updateTreeEventListener(TreeItem var1, TreeItem var2) {
         if (var1 != null && this.weakTreeItemListener != null) {
            var1.removeEventHandler(TreeItem.expandedItemCountChangeEvent(), this.weakTreeItemListener);
         }

         if (var2 != null) {
            this.weakTreeItemListener = new WeakEventHandler(this.treeItemListener);
            var2.addEventHandler(TreeItem.expandedItemCountChangeEvent(), this.weakTreeItemListener);
         }

      }

      protected int getItemCount() {
         return this.treeTableView.getExpandedItemCount();
      }

      protected TreeItem getModelItem(int var1) {
         return var1 >= 0 && var1 < this.getItemCount() ? this.treeTableView.getTreeItem(var1) : null;
      }

      public final ReadOnlyObjectProperty focusedCellProperty() {
         return this.focusedCellPropertyImpl().getReadOnlyProperty();
      }

      private void setFocusedCell(TreeTablePosition var1) {
         this.focusedCellPropertyImpl().set(var1);
      }

      public final TreeTablePosition getFocusedCell() {
         return this.focusedCell == null ? this.EMPTY_CELL : (TreeTablePosition)this.focusedCell.get();
      }

      private ReadOnlyObjectWrapper focusedCellPropertyImpl() {
         if (this.focusedCell == null) {
            this.focusedCell = new ReadOnlyObjectWrapper(this.EMPTY_CELL) {
               private TreeTablePosition old;

               protected void invalidated() {
                  if (this.get() != null) {
                     if (this.old == null || !this.old.equals(this.get())) {
                        TreeTableViewFocusModel.this.setFocusedIndex(((TreeTablePosition)this.get()).getRow());
                        TreeTableViewFocusModel.this.setFocusedItem(TreeTableViewFocusModel.this.getModelItem(((TreeTablePosition)this.getValue()).getRow()));
                        this.old = (TreeTablePosition)this.get();
                     }

                  }
               }

               public Object getBean() {
                  return TreeTableViewFocusModel.this;
               }

               public String getName() {
                  return "focusedCell";
               }
            };
         }

         return this.focusedCell;
      }

      public void focus(int var1, TreeTableColumn var2) {
         if (var1 >= 0 && var1 < this.getItemCount()) {
            TreeTablePosition var3 = this.getFocusedCell();
            TreeTablePosition var4 = new TreeTablePosition(this.treeTableView, var1, var2);
            this.setFocusedCell(var4);
            if (var4.equals(var3)) {
               this.setFocusedIndex(var1);
               this.setFocusedItem(this.getModelItem(var1));
            }
         } else {
            this.setFocusedCell(this.EMPTY_CELL);
         }

      }

      public void focus(TreeTablePosition var1) {
         if (var1 != null) {
            this.focus(var1.getRow(), var1.getTableColumn());
         }
      }

      public boolean isFocused(int var1, TreeTableColumn var2) {
         if (var1 >= 0 && var1 < this.getItemCount()) {
            TreeTablePosition var3 = this.getFocusedCell();
            boolean var4 = var2 == null || var2.equals(var3.getTableColumn());
            return var3.getRow() == var1 && var4;
         } else {
            return false;
         }
      }

      public void focus(int var1) {
         if (this.treeTableView.expandedItemCountDirty) {
            this.treeTableView.updateExpandedItemCount(this.treeTableView.getRoot());
         }

         if (var1 >= 0 && var1 < this.getItemCount()) {
            this.setFocusedCell(new TreeTablePosition(this.treeTableView, var1, (TreeTableColumn)null));
         } else {
            this.setFocusedCell(this.EMPTY_CELL);
         }

      }

      public void focusAboveCell() {
         TreeTablePosition var1 = this.getFocusedCell();
         if (this.getFocusedIndex() == -1) {
            this.focus(this.getItemCount() - 1, var1.getTableColumn());
         } else if (this.getFocusedIndex() > 0) {
            this.focus(this.getFocusedIndex() - 1, var1.getTableColumn());
         }

      }

      public void focusBelowCell() {
         TreeTablePosition var1 = this.getFocusedCell();
         if (this.getFocusedIndex() == -1) {
            this.focus(0, (TreeTableColumn)var1.getTableColumn());
         } else if (this.getFocusedIndex() != this.getItemCount() - 1) {
            this.focus(this.getFocusedIndex() + 1, var1.getTableColumn());
         }

      }

      public void focusLeftCell() {
         TreeTablePosition var1 = this.getFocusedCell();
         if (var1.getColumn() > 0) {
            this.focus(var1.getRow(), this.getTableColumn(var1.getTableColumn(), -1));
         }
      }

      public void focusRightCell() {
         TreeTablePosition var1 = this.getFocusedCell();
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

      private int getColumnCount() {
         return this.treeTableView.getVisibleLeafColumns().size();
      }

      private TreeTableColumn getTableColumn(TreeTableColumn var1, int var2) {
         int var3 = this.treeTableView.getVisibleLeafIndex(var1);
         int var4 = var3 + var2;
         return this.treeTableView.getVisibleLeafColumn(var4);
      }
   }

   static class TreeTableViewArrayListSelectionModel extends TreeTableViewSelectionModel {
      private final MappingChange.Map cellToItemsMap = (var1x) -> {
         return this.getModelItem(var1x.getRow());
      };
      private final MappingChange.Map cellToIndicesMap = (var0) -> {
         return var0.getRow();
      };
      private TreeTableView treeTableView = null;
      private ChangeListener rootPropertyListener = (var1x, var2, var3) -> {
         this.updateDefaultSelection();
         this.updateTreeEventListener(var2, var3);
      };
      private EventHandler treeItemListener = new EventHandler() {
         public void handle(TreeItem.TreeModificationEvent var1) {
            if (TreeTableViewArrayListSelectionModel.this.getSelectedIndex() != -1 || TreeTableViewArrayListSelectionModel.this.getSelectedItem() != null) {
               TreeItem var2 = var1.getTreeItem();
               if (var2 != null) {
                  int var3 = TreeTableViewArrayListSelectionModel.this.getSelectedIndex();
                  TreeItem var4 = (TreeItem)TreeTableViewArrayListSelectionModel.this.getSelectedItem();
                  TreeTableViewArrayListSelectionModel.this.treeTableView.expandedItemCountDirty = true;
                  int var5 = TreeTableViewArrayListSelectionModel.this.treeTableView.getRow(var2);
                  int var6 = 0;
                  ListChangeListener.Change var7 = var1.getChange();
                  if (var7 != null) {
                     var7.next();
                  }

                  do {
                     int var8 = var7 == null ? 0 : var7.getAddedSize();
                     int var9 = var7 == null ? 0 : var7.getRemovedSize();
                     if (var1.wasExpanded()) {
                        var6 += var2.getExpandedDescendentCount(false) - 1;
                        ++var5;
                     } else {
                        boolean var12;
                        int var16;
                        if (var1.wasCollapsed()) {
                           var2.getExpandedDescendentCount(false);
                           int var25 = var2.previousExpandedDescendentCount;
                           int var28 = TreeTableViewArrayListSelectionModel.this.getSelectedIndex();
                           var12 = var28 >= var5 + 1 && var28 < var5 + var25;
                           boolean var33 = false;
                           boolean var35 = TreeTableViewArrayListSelectionModel.this.isCellSelectionEnabled();
                           ObservableList var36 = TreeTableViewArrayListSelectionModel.this.getTreeTableView().getVisibleLeafColumns();
                           if (!var35) {
                              TreeTableViewArrayListSelectionModel.this.startAtomic();
                           }

                           var16 = var5 + 1;
                           int var37 = var5 + var25;
                           ArrayList var18 = new ArrayList();
                           TreeTableColumn var19 = null;

                           for(int var20 = var16; var20 < var37; ++var20) {
                              if (var35) {
                                 for(int var21 = 0; var21 < var36.size(); ++var21) {
                                    TreeTableColumn var22 = (TreeTableColumn)var36.get(var21);
                                    if (TreeTableViewArrayListSelectionModel.this.isSelected(var20, var22)) {
                                       var33 = true;
                                       TreeTableViewArrayListSelectionModel.this.clearSelection(var20, var22);
                                       var19 = var22;
                                    }
                                 }
                              } else if (TreeTableViewArrayListSelectionModel.this.isSelected(var20)) {
                                 var33 = true;
                                 TreeTableViewArrayListSelectionModel.this.clearSelection(var20);
                                 var18.add(var20);
                              }
                           }

                           if (!var35) {
                              TreeTableViewArrayListSelectionModel.this.stopAtomic();
                           }

                           if (var12 && var33) {
                              TreeTableViewArrayListSelectionModel.this.select(var5, var19);
                           } else if (!var35) {
                              NonIterableChange.GenericAddRemoveChange var38 = new NonIterableChange.GenericAddRemoveChange(var16, var16, var18, TreeTableViewArrayListSelectionModel.this.selectedIndicesSeq);
                              TreeTableViewArrayListSelectionModel.this.selectedIndicesSeq.callObservers(var38);
                           }

                           var6 += -var25 + 1;
                           ++var5;
                        } else {
                           int var15;
                           if (!var1.wasPermutated()) {
                              if (var1.wasAdded()) {
                                 var6 += var2.isExpanded() ? var8 : 0;
                                 var5 = TreeTableViewArrayListSelectionModel.this.treeTableView.getRow((TreeItem)var1.getChange().getAddedSubList().get(0));
                                 TreeTablePosition var24 = (TreeTablePosition)TreeTableCellBehavior.getAnchor(TreeTableViewArrayListSelectionModel.this.treeTableView, (Object)null);
                                 if (var24 != null) {
                                    boolean var27 = TreeTableViewArrayListSelectionModel.this.isSelected(var24.getRow(), var24.getTableColumn());
                                    if (var27) {
                                       TreeTablePosition var30 = new TreeTablePosition(TreeTableViewArrayListSelectionModel.this.treeTableView, var24.getRow() + var6, var24.getTableColumn());
                                       TreeTableCellBehavior.setAnchor(TreeTableViewArrayListSelectionModel.this.treeTableView, var30, false);
                                    }
                                 }
                              } else if (var1.wasRemoved()) {
                                 var6 += var2.isExpanded() ? -var9 : 0;
                                 var5 += var1.getFrom() + 1;
                                 ObservableList var23 = TreeTableViewArrayListSelectionModel.this.getSelectedIndices();
                                 ObservableList var26 = TreeTableViewArrayListSelectionModel.this.getSelectedItems();
                                 TreeItem var29 = (TreeItem)TreeTableViewArrayListSelectionModel.this.getSelectedItem();
                                 List var32 = var1.getChange().getRemoved();

                                 for(int var34 = 0; var34 < var23.size() && !var26.isEmpty(); ++var34) {
                                    var15 = (Integer)var23.get(var34);
                                    if (var15 > var26.size()) {
                                       break;
                                    }

                                    if (var32.size() == 1 && var26.size() == 1 && var29 != null && var29.equals(var32.get(0)) && var3 < TreeTableViewArrayListSelectionModel.this.getItemCount()) {
                                       var16 = var3 == 0 ? 0 : var3 - 1;
                                       TreeItem var17 = TreeTableViewArrayListSelectionModel.this.getModelItem(var16);
                                       if (!var29.equals(var17)) {
                                          TreeTableViewArrayListSelectionModel.this.clearAndSelect(var16);
                                       }
                                    }
                                 }
                              }
                           } else {
                              ArrayList var10 = new ArrayList(TreeTableViewArrayListSelectionModel.this.selectedCellsMap.getSelectedCells());
                              ArrayList var11 = new ArrayList();
                              var12 = false;

                              TreeTablePosition var14;
                              for(Iterator var13 = var10.iterator(); var13.hasNext(); var11.add(new TreeTablePosition(var14, var15))) {
                                 var14 = (TreeTablePosition)var13.next();
                                 var15 = TreeTableViewArrayListSelectionModel.this.treeTableView.getRow(var14.getTreeItem());
                                 if (var14.getRow() != var15) {
                                    var12 = true;
                                 }
                              }

                              if (var12) {
                                 if (TreeTableViewArrayListSelectionModel.this.treeTableView.isSortingInProgress()) {
                                    TreeTableViewArrayListSelectionModel.this.startAtomic();
                                    TreeTableViewArrayListSelectionModel.this.selectedCellsMap.setAll(var11);
                                    TreeTableViewArrayListSelectionModel.this.stopAtomic();
                                 } else {
                                    TreeTableViewArrayListSelectionModel.this.startAtomic();
                                    TreeTableViewArrayListSelectionModel.this.quietClearSelection();
                                    TreeTableViewArrayListSelectionModel.this.stopAtomic();
                                    TreeTableViewArrayListSelectionModel.this.selectedCellsMap.setAll(var11);
                                    int var31 = TreeTableViewArrayListSelectionModel.this.treeTableView.getRow((TreeItem)TreeTableViewArrayListSelectionModel.this.getSelectedItem());
                                    TreeTableViewArrayListSelectionModel.this.setSelectedIndex(var31);
                                    TreeTableViewArrayListSelectionModel.this.focus(var31);
                                 }
                              }
                           }
                        }
                     }
                  } while(var1.getChange() != null && var1.getChange().next());

                  if (var6 != 0) {
                     TreeTableViewArrayListSelectionModel.this.shiftSelection(var5, var6, new Callback() {
                        public Void call(MultipleSelectionModelBase.ShiftParams var1) {
                           TreeTableViewArrayListSelectionModel.this.startAtomic();
                           int var2 = var1.getClearIndex();
                           TreeTablePosition var3 = null;
                           if (var2 > -1) {
                              for(int var4 = 0; var4 < TreeTableViewArrayListSelectionModel.this.selectedCellsMap.size(); ++var4) {
                                 TreeTablePosition var5 = (TreeTablePosition)TreeTableViewArrayListSelectionModel.this.selectedCellsMap.get(var4);
                                 if (var5.getRow() == var2) {
                                    var3 = var5;
                                    TreeTableViewArrayListSelectionModel.this.selectedCellsMap.remove(var5);
                                    break;
                                 }
                              }
                           }

                           if (var3 != null && var1.isSelected()) {
                              TreeTablePosition var6 = new TreeTablePosition(TreeTableViewArrayListSelectionModel.this.treeTableView, var1.getSetIndex(), var3.getTableColumn());
                              TreeTableViewArrayListSelectionModel.this.selectedCellsMap.add(var6);
                           }

                           TreeTableViewArrayListSelectionModel.this.stopAtomic();
                           return null;
                        }
                     });
                  }

               }
            }
         }
      };
      private WeakChangeListener weakRootPropertyListener;
      private WeakEventHandler weakTreeItemListener;
      private final SelectedCellsMap selectedCellsMap;
      private final ReadOnlyUnbackedObservableList selectedItems;
      private final ReadOnlyUnbackedObservableList selectedCellsSeq;

      public TreeTableViewArrayListSelectionModel(TreeTableView var1) {
         super(var1);
         this.weakRootPropertyListener = new WeakChangeListener(this.rootPropertyListener);
         this.treeTableView = var1;
         this.treeTableView.rootProperty().addListener(this.weakRootPropertyListener);
         this.treeTableView.showRootProperty().addListener((var2) -> {
            this.shiftSelection(0, var1.isShowRoot() ? 1 : -1, (Callback)null);
         });
         this.updateTreeEventListener((TreeItem)null, var1.getRoot());
         this.selectedCellsMap = new SelectedCellsMap((var1x) -> {
            this.handleSelectedCellsListChangeEvent(var1x);
         }) {
            public boolean isCellSelectionEnabled() {
               return TreeTableViewArrayListSelectionModel.this.isCellSelectionEnabled();
            }
         };
         this.selectedItems = new ReadOnlyUnbackedObservableList() {
            public TreeItem get(int var1) {
               return TreeTableViewArrayListSelectionModel.this.getModelItem((Integer)TreeTableViewArrayListSelectionModel.this.getSelectedIndices().get(var1));
            }

            public int size() {
               return TreeTableViewArrayListSelectionModel.this.getSelectedIndices().size();
            }
         };
         this.selectedCellsSeq = new ReadOnlyUnbackedObservableList() {
            public TreeTablePosition get(int var1) {
               return (TreeTablePosition)TreeTableViewArrayListSelectionModel.this.selectedCellsMap.get(var1);
            }

            public int size() {
               return TreeTableViewArrayListSelectionModel.this.selectedCellsMap.size();
            }
         };
         this.updateDefaultSelection();
         this.cellSelectionEnabledProperty().addListener((var2) -> {
            this.updateDefaultSelection();
            TableCellBehaviorBase.setAnchor(var1, this.getFocusedCell(), true);
         });
      }

      private void updateTreeEventListener(TreeItem var1, TreeItem var2) {
         if (var1 != null && this.weakTreeItemListener != null) {
            var1.removeEventHandler(TreeItem.expandedItemCountChangeEvent(), this.weakTreeItemListener);
         }

         if (var2 != null) {
            this.weakTreeItemListener = new WeakEventHandler(this.treeItemListener);
            var2.addEventHandler(TreeItem.expandedItemCountChangeEvent(), this.weakTreeItemListener);
         }

      }

      public ObservableList getSelectedItems() {
         return this.selectedItems;
      }

      public ObservableList getSelectedCells() {
         return this.selectedCellsSeq;
      }

      public void clearAndSelect(int var1) {
         this.clearAndSelect(var1, (TableColumnBase)null);
      }

      public void clearAndSelect(int var1, TableColumnBase var2) {
         if (var1 >= 0 && var1 < this.getItemCount()) {
            TreeTablePosition var3 = new TreeTablePosition(this.getTreeTableView(), var1, (TreeTableColumn)var2);
            boolean var4 = this.isCellSelectionEnabled();
            TreeTableCellBehavior.setAnchor(this.treeTableView, var3, false);
            if (!var4 || var2 != null) {
               boolean var5 = this.isSelected(var1, var2);
               ArrayList var6 = new ArrayList(this.selectedCellsMap.getSelectedCells());
               if (var5 && var6.size() == 1) {
                  TreeTablePosition var7 = (TreeTablePosition)this.getSelectedCells().get(0);
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
                     TreeTablePosition var8 = (TreeTablePosition)var9.next();
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
         this.select(var1, (TableColumnBase)null);
      }

      public void select(int var1, TableColumnBase var2) {
         if (var1 >= 0 && var1 < this.getRowCount()) {
            if (this.isCellSelectionEnabled() && var2 == null) {
               ObservableList var5 = this.getTreeTableView().getVisibleLeafColumns();

               for(int var4 = 0; var4 < var5.size(); ++var4) {
                  this.select(var1, (TableColumnBase)var5.get(var4));
               }

            } else {
               TreeTablePosition var3 = new TreeTablePosition(this.getTreeTableView(), var1, (TreeTableColumn)var2);
               if (this.getSelectionMode() == SelectionMode.SINGLE) {
                  this.startAtomic();
                  this.quietClearSelection();
                  this.stopAtomic();
               }

               if (TreeTableCellBehavior.hasDefaultAnchor(this.treeTableView)) {
                  TreeTableCellBehavior.removeAnchor(this.treeTableView);
               }

               this.selectedCellsMap.add(var3);
               this.updateSelectedIndex(var1);
               this.focus(var1, (TreeTableColumn)var2);
            }
         }
      }

      public void select(TreeItem var1) {
         if (var1 == null && this.getSelectionMode() == SelectionMode.SINGLE) {
            this.clearSelection();
         } else {
            int var2 = this.treeTableView.getRow(var1);
            if (var2 > -1) {
               if (this.isSelected(var2)) {
                  return;
               }

               if (this.getSelectionMode() == SelectionMode.SINGLE) {
                  this.quietClearSelection();
               }

               this.select(var2);
            } else {
               this.setSelectedIndex(-1);
               this.setSelectedItem(var1);
            }

         }
      }

      public void selectIndices(int var1, int... var2) {
         if (var2 == null) {
            this.select(var1);
         } else {
            int var3 = this.getRowCount();
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
                     ObservableList var6 = this.getTreeTableView().getVisibleLeafColumns();

                     for(var7 = 0; var7 < var6.size(); ++var7) {
                        if (!this.selectedCellsMap.isSelected(var1, var7)) {
                           var10.add(new TreeTablePosition(this.getTreeTableView(), var1, (TreeTableColumn)var6.get(var7)));
                        }
                     }
                  } else {
                     boolean var11 = this.selectedCellsMap.isSelected(var1, -1);
                     if (!var11) {
                        var10.add(new TreeTablePosition(this.getTreeTableView(), var1, (TreeTableColumn)null));
                     }
                  }

                  var4 = var1;
               }

               for(int var12 = 0; var12 < var2.length; ++var12) {
                  var7 = var2[var12];
                  if (var7 >= 0 && var7 < var3) {
                     var4 = var7;
                     if (this.isCellSelectionEnabled()) {
                        ObservableList var8 = this.getTreeTableView().getVisibleLeafColumns();

                        for(int var9 = 0; var9 < var8.size(); ++var9) {
                           if (!this.selectedCellsMap.isSelected(var7, var9)) {
                              var10.add(new TreeTablePosition(this.getTreeTableView(), var7, (TreeTableColumn)var8.get(var9)));
                              var4 = var7;
                           }
                        }
                     } else if (!this.selectedCellsMap.isSelected(var7, -1)) {
                        var10.add(new TreeTablePosition(this.getTreeTableView(), var7, (TreeTableColumn)null));
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
               TreeTablePosition var3 = null;

               for(int var4 = 0; var4 < this.getTreeTableView().getVisibleLeafColumns().size(); ++var4) {
                  TreeTableColumn var2 = (TreeTableColumn)this.getTreeTableView().getVisibleLeafColumns().get(var4);

                  for(int var5 = 0; var5 < this.getRowCount(); ++var5) {
                     var3 = new TreeTablePosition(this.getTreeTableView(), var5, var2);
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
               for(var6 = 0; var6 < this.getRowCount(); ++var6) {
                  var1.add(new TreeTablePosition(this.getTreeTableView(), var6, (TreeTableColumn)null));
               }

               this.selectedCellsMap.setAll(var1);
               var6 = this.getFocusedIndex();
               if (var6 == -1) {
                  int var7 = this.getItemCount();
                  if (var7 > 0) {
                     this.select(var7 - 1);
                     this.focus((TreeTablePosition)var1.get(var1.size() - 1));
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
            int var7 = this.treeTableView.getVisibleLeafIndex((TreeTableColumn)var2);
            int var8 = this.treeTableView.getVisibleLeafIndex((TreeTableColumn)var4);
            int var9 = Math.min(var7, var8);
            int var10 = Math.max(var7, var8);
            int var11 = Math.min(var1, var3);
            int var12 = Math.max(var1, var3);
            ArrayList var13 = new ArrayList();

            for(int var14 = var11; var14 <= var12; ++var14) {
               if (var14 >= 0 && var14 < var5) {
                  if (!var6) {
                     var13.add(new TreeTablePosition(this.treeTableView, var14, (TreeTableColumn)var2));
                  } else {
                     for(int var15 = var9; var15 <= var10; ++var15) {
                        TreeTableColumn var16 = this.treeTableView.getVisibleLeafColumn(var15);
                        if (var16 != null || !var6) {
                           var13.add(new TreeTablePosition(this.treeTableView, var14, var16));
                        }
                     }
                  }
               }
            }

            var13.removeAll(this.getSelectedCells());
            this.selectedCellsMap.addAll(var13);
            this.stopAtomic();
            this.updateSelectedIndex(var3);
            this.focus(var3, (TreeTableColumn)var4);
            TreeTableColumn var21 = (TreeTableColumn)var2;
            TreeTableColumn var22 = var6 ? (TreeTableColumn)var4 : var21;
            int var23 = this.selectedCellsMap.indexOf(new TreeTablePosition(this.treeTableView, var1, var21));
            int var17 = this.selectedCellsMap.indexOf(new TreeTablePosition(this.treeTableView, var3, var22));
            if (var23 > -1 && var17 > -1) {
               int var18 = Math.min(var23, var17);
               int var19 = Math.max(var23, var17);
               NonIterableChange.SimpleAddChange var20 = new NonIterableChange.SimpleAddChange(var18, var19 + 1, this.selectedCellsSeq);
               this.handleSelectedCellsListChangeEvent(var20);
            }

         }
      }

      public void clearSelection(int var1) {
         this.clearSelection(var1, (TableColumnBase)null);
      }

      public void clearSelection(int var1, TableColumnBase var2) {
         this.clearSelection(new TreeTablePosition(this.getTreeTableView(), var1, (TreeTableColumn)var2));
      }

      private void clearSelection(TreeTablePosition var1) {
         boolean var2 = this.isCellSelectionEnabled();
         int var3 = var1.getRow();
         Iterator var4 = this.getSelectedCells().iterator();

         while(var4.hasNext()) {
            TreeTablePosition var5 = (TreeTablePosition)var4.next();
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
            NonIterableChange var2 = new NonIterableChange(0, 0, this.selectedCellsSeq) {
               public List getRemoved() {
                  return var1;
               }
            };
            this.handleSelectedCellsListChangeEvent(var2);
         }

      }

      private void quietClearSelection() {
         this.startAtomic();
         this.selectedCellsMap.clear();
         this.stopAtomic();
      }

      public boolean isSelected(int var1) {
         return this.isSelected(var1, (TableColumnBase)null);
      }

      public boolean isSelected(int var1, TableColumnBase var2) {
         boolean var3 = this.isCellSelectionEnabled();
         if (var3 && var2 == null) {
            return false;
         } else {
            int var4 = var3 && var2 != null ? this.treeTableView.getVisibleLeafIndex((TreeTableColumn)var2) : -1;
            return this.selectedCellsMap.isSelected(var1, var4);
         }
      }

      public boolean isEmpty() {
         return this.selectedCellsMap.isEmpty();
      }

      public void selectPrevious() {
         if (this.isCellSelectionEnabled()) {
            TreeTablePosition var1 = this.getFocusedCell();
            if (var1.getColumn() - 1 >= 0) {
               this.select(var1.getRow(), this.getTableColumn(var1.getTableColumn(), -1));
            } else if (var1.getRow() < this.getRowCount() - 1) {
               this.select(var1.getRow() - 1, this.getTableColumn(this.getTreeTableView().getVisibleLeafColumns().size() - 1));
            }
         } else {
            int var2 = this.getFocusedIndex();
            if (var2 == -1) {
               this.select(this.getRowCount() - 1);
            } else if (var2 > 0) {
               this.select(var2 - 1);
            }
         }

      }

      public void selectNext() {
         if (this.isCellSelectionEnabled()) {
            TreeTablePosition var1 = this.getFocusedCell();
            if (var1.getColumn() + 1 < this.getTreeTableView().getVisibleLeafColumns().size()) {
               this.select(var1.getRow(), this.getTableColumn(var1.getTableColumn(), 1));
            } else if (var1.getRow() < this.getRowCount() - 1) {
               this.select(var1.getRow() + 1, this.getTableColumn(0));
            }
         } else {
            int var2 = this.getFocusedIndex();
            if (var2 == -1) {
               this.select(0);
            } else if (var2 < this.getRowCount() - 1) {
               this.select(var2 + 1);
            }
         }

      }

      public void selectAboveCell() {
         TreeTablePosition var1 = this.getFocusedCell();
         if (var1.getRow() == -1) {
            this.select(this.getRowCount() - 1);
         } else if (var1.getRow() > 0) {
            this.select(var1.getRow() - 1, var1.getTableColumn());
         }

      }

      public void selectBelowCell() {
         TreeTablePosition var1 = this.getFocusedCell();
         if (var1.getRow() == -1) {
            this.select(0);
         } else if (var1.getRow() < this.getRowCount() - 1) {
            this.select(var1.getRow() + 1, var1.getTableColumn());
         }

      }

      public void selectFirst() {
         TreeTablePosition var1 = this.getFocusedCell();
         if (this.getSelectionMode() == SelectionMode.SINGLE) {
            this.quietClearSelection();
         }

         if (this.getRowCount() > 0) {
            if (this.isCellSelectionEnabled()) {
               this.select(0, var1.getTableColumn());
            } else {
               this.select(0);
            }
         }

      }

      public void selectLast() {
         TreeTablePosition var1 = this.getFocusedCell();
         if (this.getSelectionMode() == SelectionMode.SINGLE) {
            this.quietClearSelection();
         }

         int var2 = this.getRowCount();
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
            TreeTablePosition var1 = this.getFocusedCell();
            if (var1.getColumn() - 1 >= 0) {
               this.select(var1.getRow(), this.getTableColumn(var1.getTableColumn(), -1));
            }

         }
      }

      public void selectRightCell() {
         if (this.isCellSelectionEnabled()) {
            TreeTablePosition var1 = this.getFocusedCell();
            if (var1.getColumn() + 1 < this.getTreeTableView().getVisibleLeafColumns().size()) {
               this.select(var1.getRow(), this.getTableColumn(var1.getTableColumn(), 1));
            }

         }
      }

      private void updateDefaultSelection() {
         this.clearSelection();
         int var1 = this.getItemCount() > 0 ? 0 : -1;
         this.focus(var1, this.isCellSelectionEnabled() ? this.getTableColumn(0) : null);
      }

      private TreeTableColumn getTableColumn(int var1) {
         return this.getTreeTableView().getVisibleLeafColumn(var1);
      }

      private TreeTableColumn getTableColumn(TreeTableColumn var1, int var2) {
         int var3 = this.getTreeTableView().getVisibleLeafIndex(var1);
         int var4 = var3 + var2;
         return this.getTreeTableView().getVisibleLeafColumn(var4);
      }

      private void updateSelectedIndex(int var1) {
         this.setSelectedIndex(var1);
         this.setSelectedItem(this.getModelItem(var1));
      }

      public void focus(int var1) {
         this.focus(var1, (TreeTableColumn)null);
      }

      private void focus(int var1, TreeTableColumn var2) {
         this.focus(new TreeTablePosition(this.getTreeTableView(), var1, var2));
      }

      private void focus(TreeTablePosition var1) {
         if (this.getTreeTableView().getFocusModel() != null) {
            this.getTreeTableView().getFocusModel().focus(var1.getRow(), var1.getTableColumn());
            this.getTreeTableView().notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_ITEM);
         }
      }

      public int getFocusedIndex() {
         return this.getFocusedCell().getRow();
      }

      private TreeTablePosition getFocusedCell() {
         return this.treeTableView.getFocusModel() == null ? new TreeTablePosition(this.treeTableView, -1, (TreeTableColumn)null) : this.treeTableView.getFocusModel().getFocusedCell();
      }

      private int getRowCount() {
         return this.treeTableView.getExpandedItemCount();
      }

      private void handleSelectedCellsListChangeEvent(ListChangeListener.Change var1) {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();

         while(true) {
            List var4;
            int var5;
            TreeTablePosition var6;
            int var7;
            do {
               if (!var1.next()) {
                  var1.reset();
                  this.selectedIndicesSeq.reset();
                  if (this.isAtomic()) {
                     return;
                  }

                  var1.next();
                  boolean var12 = false;
                  if (var1.wasReplaced()) {
                     var5 = var1.getRemovedSize();
                     int var13 = var1.getAddedSize();
                     if (var5 != var13) {
                        var12 = true;
                     } else {
                        for(var7 = 0; var7 < var5; ++var7) {
                           TreeTablePosition var8 = (TreeTablePosition)var1.getRemoved().get(var7);
                           TreeItem var9 = var8.getTreeItem();
                           TreeTablePosition var10 = (TreeTablePosition)var1.getAddedSubList().get(var7);
                           TreeItem var11 = var10.getTreeItem();
                           if (var9 != null && !var9.equals(var11)) {
                              var12 = true;
                              break;
                           }
                        }
                     }
                  } else {
                     var12 = true;
                  }

                  if (var12) {
                     if (this.treeTableView.isSortingInProgress()) {
                        MappingChange.Map var14 = (var0) -> {
                           return var0.getTreeItem();
                        };
                        this.selectedItems.callObservers(new MappingChange(var1, var14, this.selectedItems));
                     } else {
                        this.selectedItems.callObservers(new MappingChange(var1, this.cellToItemsMap, this.selectedItems));
                     }
                  }

                  var1.reset();
                  if (this.selectedItems.isEmpty() && this.getSelectedItem() != null) {
                     this.setSelectedItem((Object)null);
                  }

                  ReadOnlyUnbackedObservableList var15 = (ReadOnlyUnbackedObservableList)this.getSelectedIndices();
                  if (!var2.isEmpty() && var3.isEmpty()) {
                     ListChangeListener.Change var16 = createRangeChange(var15, var2, false);
                     var15.callObservers(var16);
                  } else {
                     var15.callObservers(new MappingChange(var1, this.cellToIndicesMap, var15));
                     var1.reset();
                  }

                  this.selectedCellsSeq.callObservers(new MappingChange(var1, MappingChange.NOOP_MAP, this.selectedCellsSeq));
                  var1.reset();
                  return;
               }

               if (var1.wasRemoved()) {
                  var4 = var1.getRemoved();

                  for(var5 = 0; var5 < var4.size(); ++var5) {
                     var6 = (TreeTablePosition)var4.get(var5);
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
               var6 = (TreeTablePosition)var4.get(var5);
               var7 = var6.getRow();
               if (!this.selectedIndices.get(var7)) {
                  this.selectedIndices.set(var7);
                  var2.add(var7);
               }
            }
         }
      }
   }

   public abstract static class TreeTableViewSelectionModel extends TableSelectionModel {
      private final TreeTableView treeTableView;

      public TreeTableViewSelectionModel(TreeTableView var1) {
         if (var1 == null) {
            throw new NullPointerException("TreeTableView can not be null");
         } else {
            this.treeTableView = var1;
         }
      }

      public abstract ObservableList getSelectedCells();

      public TreeTableView getTreeTableView() {
         return this.treeTableView;
      }

      public TreeItem getModelItem(int var1) {
         return this.treeTableView.getTreeItem(var1);
      }

      protected int getItemCount() {
         return this.treeTableView.getExpandedItemCount();
      }

      public void focus(int var1) {
         this.focus(var1, (TreeTableColumn)null);
      }

      public int getFocusedIndex() {
         return this.getFocusedCell().getRow();
      }

      public void selectRange(int var1, TableColumnBase var2, int var3, TableColumnBase var4) {
         int var5 = this.treeTableView.getVisibleLeafIndex((TreeTableColumn)var2);
         int var6 = this.treeTableView.getVisibleLeafIndex((TreeTableColumn)var4);

         for(int var7 = var1; var7 <= var3; ++var7) {
            for(int var8 = var5; var8 <= var6; ++var8) {
               this.select(var7, this.treeTableView.getVisibleLeafColumn(var8));
            }
         }

      }

      private void focus(int var1, TreeTableColumn var2) {
         this.focus(new TreeTablePosition(this.getTreeTableView(), var1, var2));
      }

      private void focus(TreeTablePosition var1) {
         if (this.getTreeTableView().getFocusModel() != null) {
            this.getTreeTableView().getFocusModel().focus(var1.getRow(), var1.getTableColumn());
         }
      }

      private TreeTablePosition getFocusedCell() {
         return this.treeTableView.getFocusModel() == null ? new TreeTablePosition(this.treeTableView, -1, (TreeTableColumn)null) : this.treeTableView.getFocusModel().getFocusedCell();
      }
   }

   public static class EditEvent extends Event {
      private static final long serialVersionUID = -4437033058917528976L;
      public static final EventType ANY;
      private final Object oldValue;
      private final Object newValue;
      private final transient TreeItem treeItem;

      public EditEvent(TreeTableView var1, EventType var2, TreeItem var3, Object var4, Object var5) {
         super(var1, Event.NULL_SOURCE_TARGET, var2);
         this.oldValue = var4;
         this.newValue = var5;
         this.treeItem = var3;
      }

      public TreeTableView getSource() {
         return (TreeTableView)super.getSource();
      }

      public TreeItem getTreeItem() {
         return this.treeItem;
      }

      public Object getNewValue() {
         return this.newValue;
      }

      public Object getOldValue() {
         return this.oldValue;
      }

      static {
         ANY = TreeTableView.EDIT_ANY_EVENT;
      }
   }

   public static class ResizeFeatures extends ResizeFeaturesBase {
      private TreeTableView treeTable;

      public ResizeFeatures(TreeTableView var1, TreeTableColumn var2, Double var3) {
         super(var2, var3);
         this.treeTable = var1;
      }

      public TreeTableColumn getColumn() {
         return (TreeTableColumn)super.getColumn();
      }

      public TreeTableView getTable() {
         return this.treeTable;
      }
   }

   private static class StyleableProperties {
      private static final CssMetaData FIXED_CELL_SIZE = new CssMetaData("-fx-fixed-cell-size", SizeConverter.getInstance(), -1.0) {
         public Double getInitialValue(TreeTableView var1) {
            return var1.getFixedCellSize();
         }

         public boolean isSettable(TreeTableView var1) {
            return var1.fixedCellSize == null || !var1.fixedCellSize.isBound();
         }

         public StyleableProperty getStyleableProperty(TreeTableView var1) {
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
