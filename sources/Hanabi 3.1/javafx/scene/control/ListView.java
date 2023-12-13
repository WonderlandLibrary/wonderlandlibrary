package javafx.scene.control;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.css.converters.SizeConverter;
import com.sun.javafx.scene.control.behavior.ListCellBehavior;
import com.sun.javafx.scene.control.skin.ListViewSkin;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.StyleableDoubleProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.util.Callback;

@DefaultProperty("items")
public class ListView extends Control {
   private static final EventType EDIT_ANY_EVENT;
   private static final EventType EDIT_START_EVENT;
   private static final EventType EDIT_CANCEL_EVENT;
   private static final EventType EDIT_COMMIT_EVENT;
   private boolean selectFirstRowByDefault;
   private EventHandler DEFAULT_EDIT_COMMIT_HANDLER;
   private ObjectProperty items;
   private ObjectProperty placeholder;
   private ObjectProperty selectionModel;
   private ObjectProperty focusModel;
   private ObjectProperty orientation;
   private ObjectProperty cellFactory;
   private DoubleProperty fixedCellSize;
   private BooleanProperty editable;
   private ReadOnlyIntegerWrapper editingIndex;
   private ObjectProperty onEditStart;
   private ObjectProperty onEditCommit;
   private ObjectProperty onEditCancel;
   private ObjectProperty onScrollTo;
   private static final String DEFAULT_STYLE_CLASS = "list-view";
   private static final PseudoClass PSEUDO_CLASS_VERTICAL;
   private static final PseudoClass PSEUDO_CLASS_HORIZONTAL;

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

   public ListView() {
      this(FXCollections.observableArrayList());
   }

   public ListView(ObservableList var1) {
      this.selectFirstRowByDefault = true;
      this.DEFAULT_EDIT_COMMIT_HANDLER = (var1x) -> {
         int var2 = var1x.getIndex();
         ObservableList var3 = this.getItems();
         if (var2 >= 0 && var2 < var3.size()) {
            var3.set(var2, var1x.getNewValue());
         }
      };
      this.selectionModel = new SimpleObjectProperty(this, "selectionModel");
      this.getStyleClass().setAll((Object[])("list-view"));
      this.setAccessibleRole(AccessibleRole.LIST_VIEW);
      this.setItems(var1);
      this.setSelectionModel(new ListViewBitSetSelectionModel(this));
      this.setFocusModel(new ListViewFocusModel(this));
      this.setOnEditCommit(this.DEFAULT_EDIT_COMMIT_HANDLER);
      this.getProperties().addListener((var1x) -> {
         if (var1x.wasAdded() && "selectFirstRowByDefault".equals(var1x.getKey())) {
            Boolean var2 = (Boolean)var1x.getValueAdded();
            if (var2 == null) {
               return;
            }

            this.selectFirstRowByDefault = var2;
         }

      });
   }

   public final void setItems(ObservableList var1) {
      this.itemsProperty().set(var1);
   }

   public final ObservableList getItems() {
      return this.items == null ? null : (ObservableList)this.items.get();
   }

   public final ObjectProperty itemsProperty() {
      if (this.items == null) {
         this.items = new SimpleObjectProperty(this, "items");
      }

      return this.items;
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

   public final void setSelectionModel(MultipleSelectionModel var1) {
      this.selectionModelProperty().set(var1);
   }

   public final MultipleSelectionModel getSelectionModel() {
      return this.selectionModel == null ? null : (MultipleSelectionModel)this.selectionModel.get();
   }

   public final ObjectProperty selectionModelProperty() {
      return this.selectionModel;
   }

   public final void setFocusModel(FocusModel var1) {
      this.focusModelProperty().set(var1);
   }

   public final FocusModel getFocusModel() {
      return this.focusModel == null ? null : (FocusModel)this.focusModel.get();
   }

   public final ObjectProperty focusModelProperty() {
      if (this.focusModel == null) {
         this.focusModel = new SimpleObjectProperty(this, "focusModel");
      }

      return this.focusModel;
   }

   public final void setOrientation(Orientation var1) {
      this.orientationProperty().set(var1);
   }

   public final Orientation getOrientation() {
      return this.orientation == null ? Orientation.VERTICAL : (Orientation)this.orientation.get();
   }

   public final ObjectProperty orientationProperty() {
      if (this.orientation == null) {
         this.orientation = new StyleableObjectProperty(Orientation.VERTICAL) {
            public void invalidated() {
               boolean var1 = this.get() == Orientation.VERTICAL;
               ListView.this.pseudoClassStateChanged(ListView.PSEUDO_CLASS_VERTICAL, var1);
               ListView.this.pseudoClassStateChanged(ListView.PSEUDO_CLASS_HORIZONTAL, !var1);
            }

            public CssMetaData getCssMetaData() {
               return ListView.StyleableProperties.ORIENTATION;
            }

            public Object getBean() {
               return ListView.this;
            }

            public String getName() {
               return "orientation";
            }
         };
      }

      return this.orientation;
   }

   public final void setCellFactory(Callback var1) {
      this.cellFactoryProperty().set(var1);
   }

   public final Callback getCellFactory() {
      return this.cellFactory == null ? null : (Callback)this.cellFactory.get();
   }

   public final ObjectProperty cellFactoryProperty() {
      if (this.cellFactory == null) {
         this.cellFactory = new SimpleObjectProperty(this, "cellFactory");
      }

      return this.cellFactory;
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
               return ListView.StyleableProperties.FIXED_CELL_SIZE;
            }

            public Object getBean() {
               return ListView.this;
            }

            public String getName() {
               return "fixedCellSize";
            }
         };
      }

      return this.fixedCellSize;
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

   private void setEditingIndex(int var1) {
      this.editingIndexPropertyImpl().set(var1);
   }

   public final int getEditingIndex() {
      return this.editingIndex == null ? -1 : this.editingIndex.get();
   }

   public final ReadOnlyIntegerProperty editingIndexProperty() {
      return this.editingIndexPropertyImpl().getReadOnlyProperty();
   }

   private ReadOnlyIntegerWrapper editingIndexPropertyImpl() {
      if (this.editingIndex == null) {
         this.editingIndex = new ReadOnlyIntegerWrapper(this, "editingIndex", -1);
      }

      return this.editingIndex;
   }

   public final void setOnEditStart(EventHandler var1) {
      this.onEditStartProperty().set(var1);
   }

   public final EventHandler getOnEditStart() {
      return this.onEditStart == null ? null : (EventHandler)this.onEditStart.get();
   }

   public final ObjectProperty onEditStartProperty() {
      if (this.onEditStart == null) {
         this.onEditStart = new ObjectPropertyBase() {
            protected void invalidated() {
               ListView.this.setEventHandler(ListView.EDIT_START_EVENT, (EventHandler)this.get());
            }

            public Object getBean() {
               return ListView.this;
            }

            public String getName() {
               return "onEditStart";
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
         this.onEditCommit = new ObjectPropertyBase() {
            protected void invalidated() {
               ListView.this.setEventHandler(ListView.EDIT_COMMIT_EVENT, (EventHandler)this.get());
            }

            public Object getBean() {
               return ListView.this;
            }

            public String getName() {
               return "onEditCommit";
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
         this.onEditCancel = new ObjectPropertyBase() {
            protected void invalidated() {
               ListView.this.setEventHandler(ListView.EDIT_CANCEL_EVENT, (EventHandler)this.get());
            }

            public Object getBean() {
               return ListView.this;
            }

            public String getName() {
               return "onEditCancel";
            }
         };
      }

      return this.onEditCancel;
   }

   public void edit(int var1) {
      if (this.isEditable()) {
         this.setEditingIndex(var1);
      }
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
               ListView.this.setEventHandler(ScrollToEvent.scrollToTopIndex(), (EventHandler)this.get());
            }

            public Object getBean() {
               return ListView.this;
            }

            public String getName() {
               return "onScrollTo";
            }
         };
      }

      return this.onScrollTo;
   }

   protected Skin createDefaultSkin() {
      return new ListViewSkin(this);
   }

   public void refresh() {
      this.getProperties().put("listRecreateKey", Boolean.TRUE);
   }

   public static List getClassCssMetaData() {
      return ListView.StyleableProperties.STYLEABLES;
   }

   public List getControlCssMetaData() {
      return getClassCssMetaData();
   }

   public Object queryAccessibleAttribute(AccessibleAttribute var1, Object... var2) {
      switch (var1) {
         case MULTIPLE_SELECTION:
            MultipleSelectionModel var3 = this.getSelectionModel();
            return var3 != null && var3.getSelectionMode() == SelectionMode.MULTIPLE;
         default:
            return super.queryAccessibleAttribute(var1, var2);
      }
   }

   static {
      EDIT_ANY_EVENT = new EventType(Event.ANY, "LIST_VIEW_EDIT");
      EDIT_START_EVENT = new EventType(editAnyEvent(), "EDIT_START");
      EDIT_CANCEL_EVENT = new EventType(editAnyEvent(), "EDIT_CANCEL");
      EDIT_COMMIT_EVENT = new EventType(editAnyEvent(), "EDIT_COMMIT");
      PSEUDO_CLASS_VERTICAL = PseudoClass.getPseudoClass("vertical");
      PSEUDO_CLASS_HORIZONTAL = PseudoClass.getPseudoClass("horizontal");
   }

   static class ListViewFocusModel extends FocusModel {
      private final ListView listView;
      private int itemCount = 0;
      private final InvalidationListener itemsObserver;
      private final ListChangeListener itemsContentListener = (var1x) -> {
         this.updateItemCount();

         while(var1x.next()) {
            int var2 = var1x.getFrom();
            if (this.getFocusedIndex() == -1 || var2 > this.getFocusedIndex()) {
               return;
            }

            var1x.reset();
            boolean var3 = false;
            boolean var4 = false;
            int var5 = 0;

            int var6;
            for(var6 = 0; var1x.next(); var6 += var1x.getRemovedSize()) {
               var3 |= var1x.wasAdded();
               var4 |= var1x.wasRemoved();
               var5 += var1x.getAddedSize();
            }

            if (var3 && !var4) {
               this.focus(Math.min(this.getItemCount() - 1, this.getFocusedIndex() + var5));
            } else if (!var3 && var4) {
               this.focus(Math.max(0, this.getFocusedIndex() - var6));
            }
         }

      };
      private WeakListChangeListener weakItemsContentListener;

      public ListViewFocusModel(final ListView var1) {
         this.weakItemsContentListener = new WeakListChangeListener(this.itemsContentListener);
         if (var1 == null) {
            throw new IllegalArgumentException("ListView can not be null");
         } else {
            this.listView = var1;
            this.itemsObserver = new InvalidationListener() {
               private WeakReference weakItemsRef = new WeakReference(var1.getItems());

               public void invalidated(Observable var1x) {
                  ObservableList var2 = (ObservableList)this.weakItemsRef.get();
                  this.weakItemsRef = new WeakReference(var1.getItems());
                  ListViewFocusModel.this.updateItemsObserver(var2, var1.getItems());
               }
            };
            this.listView.itemsProperty().addListener(new WeakInvalidationListener(this.itemsObserver));
            if (var1.getItems() != null) {
               this.listView.getItems().addListener(this.weakItemsContentListener);
            }

            this.updateItemCount();
            if (this.itemCount > 0) {
               this.focus(0);
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
      }

      protected int getItemCount() {
         return this.itemCount;
      }

      protected Object getModelItem(int var1) {
         if (this.isEmpty()) {
            return null;
         } else {
            return var1 >= 0 && var1 < this.itemCount ? this.listView.getItems().get(var1) : null;
         }
      }

      private boolean isEmpty() {
         return this.itemCount == -1;
      }

      private void updateItemCount() {
         if (this.listView == null) {
            this.itemCount = -1;
         } else {
            ObservableList var1 = this.listView.getItems();
            this.itemCount = var1 == null ? -1 : var1.size();
         }

      }
   }

   static class ListViewBitSetSelectionModel extends MultipleSelectionModelBase {
      private final ListChangeListener itemsContentObserver = new ListChangeListener() {
         public void onChanged(ListChangeListener.Change var1) {
            ListViewBitSetSelectionModel.this.updateItemCount();

            while(true) {
               while(true) {
                  while(var1.next()) {
                     Object var2 = ListViewBitSetSelectionModel.this.getSelectedItem();
                     int var3 = ListViewBitSetSelectionModel.this.getSelectedIndex();
                     if (ListViewBitSetSelectionModel.this.listView.getItems() != null && !ListViewBitSetSelectionModel.this.listView.getItems().isEmpty()) {
                        int var4;
                        if (var3 == -1 && var2 != null) {
                           var4 = ListViewBitSetSelectionModel.this.listView.getItems().indexOf(var2);
                           if (var4 != -1) {
                              ListViewBitSetSelectionModel.this.setSelectedIndex(var4);
                           }
                        } else if (var1.wasRemoved() && var1.getRemovedSize() == 1 && !var1.wasAdded() && var2 != null && var2.equals(var1.getRemoved().get(0)) && ListViewBitSetSelectionModel.this.getSelectedIndex() < ListViewBitSetSelectionModel.this.getItemCount()) {
                           var4 = var3 == 0 ? 0 : var3 - 1;
                           Object var5 = ListViewBitSetSelectionModel.this.getModelItem(var4);
                           if (!var2.equals(var5)) {
                              ListViewBitSetSelectionModel.this.startAtomic();
                              ListViewBitSetSelectionModel.this.clearSelection(var3);
                              ListViewBitSetSelectionModel.this.stopAtomic();
                              ListViewBitSetSelectionModel.this.select(var5);
                           }
                        }
                     } else {
                        ListViewBitSetSelectionModel.this.selectedItemChange = var1;
                        ListViewBitSetSelectionModel.this.clearSelection();
                        ListViewBitSetSelectionModel.this.selectedItemChange = null;
                     }
                  }

                  ListViewBitSetSelectionModel.this.updateSelection(var1);
                  return;
               }
            }
         }
      };
      private final InvalidationListener itemsObserver;
      private WeakListChangeListener weakItemsContentObserver;
      private final ListView listView;
      private int itemCount;
      private int previousModelSize;

      public ListViewBitSetSelectionModel(final ListView var1) {
         this.weakItemsContentObserver = new WeakListChangeListener(this.itemsContentObserver);
         this.itemCount = 0;
         this.previousModelSize = 0;
         if (var1 == null) {
            throw new IllegalArgumentException("ListView can not be null");
         } else {
            this.listView = var1;
            this.itemsObserver = new InvalidationListener() {
               private WeakReference weakItemsRef = new WeakReference(var1.getItems());

               public void invalidated(Observable var1x) {
                  ObservableList var2 = (ObservableList)this.weakItemsRef.get();
                  this.weakItemsRef = new WeakReference(var1.getItems());
                  ListViewBitSetSelectionModel.this.updateItemsObserver(var2, var1.getItems());
               }
            };
            this.listView.itemsProperty().addListener(new WeakInvalidationListener(this.itemsObserver));
            if (var1.getItems() != null) {
               this.listView.getItems().addListener(this.weakItemsContentObserver);
            }

            this.updateItemCount();
            this.updateDefaultSelection();
         }
      }

      private void updateSelection(ListChangeListener.Change var1) {
         var1.reset();
         int var2 = 0;

         while(true) {
            while(true) {
               while(var1.next()) {
                  int var3;
                  if (var1.wasReplaced()) {
                     if (var1.getList().isEmpty()) {
                        this.clearSelection();
                     } else {
                        var3 = this.getSelectedIndex();
                        if (this.previousModelSize == var1.getRemovedSize()) {
                           this.clearSelection();
                        } else if (var3 < this.getItemCount() && var3 >= 0) {
                           this.startAtomic();
                           this.clearSelection(var3);
                           this.stopAtomic();
                           this.select(var3);
                        } else {
                           this.clearSelection();
                        }
                     }
                  } else if (!var1.wasAdded() && !var1.wasRemoved()) {
                     if (var1.wasPermutated()) {
                        var3 = var1.getTo() - var1.getFrom();
                        HashMap var4 = new HashMap(var3);

                        for(int var5 = var1.getFrom(); var5 < var1.getTo(); ++var5) {
                           var4.put(var5, var1.getPermutation(var5));
                        }

                        ArrayList var10 = new ArrayList(this.getSelectedIndices());
                        this.clearSelection();
                        ArrayList var6 = new ArrayList(this.getSelectedIndices().size());

                        int var8;
                        for(int var7 = 0; var7 < var10.size(); ++var7) {
                           var8 = (Integer)var10.get(var7);
                           if (var4.containsKey(var8)) {
                              Integer var9 = (Integer)var4.get(var8);
                              var6.add(var9);
                           }
                        }

                        if (!var6.isEmpty()) {
                           if (var6.size() == 1) {
                              this.select((Integer)var6.get(0));
                           } else {
                              int[] var11 = new int[var6.size() - 1];

                              for(var8 = 0; var8 < var6.size() - 1; ++var8) {
                                 var11[var8] = (Integer)var6.get(var8 + 1);
                              }

                              this.selectIndices((Integer)var6.get(0), var11);
                           }
                        }
                     }
                  } else {
                     var2 += var1.wasAdded() ? var1.getAddedSize() : -var1.getRemovedSize();
                  }
               }

               if (var2 != 0) {
                  this.shiftSelection(var1.getFrom(), var2, (Callback)null);
               }

               this.previousModelSize = this.getItemCount();
               return;
            }
         }
      }

      public void selectAll() {
         int var1 = (Integer)ListCellBehavior.getAnchor(this.listView, -1);
         super.selectAll();
         ListCellBehavior.setAnchor(this.listView, var1, false);
      }

      public void clearAndSelect(int var1) {
         ListCellBehavior.setAnchor(this.listView, var1, false);
         super.clearAndSelect(var1);
      }

      protected void focus(int var1) {
         if (this.listView.getFocusModel() != null) {
            this.listView.getFocusModel().focus(var1);
            this.listView.notifyAccessibleAttributeChanged(AccessibleAttribute.FOCUS_ITEM);
         }
      }

      protected int getFocusedIndex() {
         return this.listView.getFocusModel() == null ? -1 : this.listView.getFocusModel().getFocusedIndex();
      }

      protected int getItemCount() {
         return this.itemCount;
      }

      protected Object getModelItem(int var1) {
         ObservableList var2 = this.listView.getItems();
         if (var2 == null) {
            return null;
         } else {
            return var1 >= 0 && var1 < this.itemCount ? var2.get(var1) : null;
         }
      }

      private void updateItemCount() {
         if (this.listView == null) {
            this.itemCount = -1;
         } else {
            ObservableList var1 = this.listView.getItems();
            this.itemCount = var1 == null ? -1 : var1.size();
         }

      }

      private void updateItemsObserver(ObservableList var1, ObservableList var2) {
         if (var1 != null) {
            var1.removeListener(this.weakItemsContentObserver);
         }

         if (var2 != null) {
            var2.addListener(this.weakItemsContentObserver);
         }

         this.updateItemCount();
         this.updateDefaultSelection();
      }

      private void updateDefaultSelection() {
         int var1 = -1;
         int var2 = -1;
         if (this.listView.getItems() != null) {
            Object var3 = this.getSelectedItem();
            if (var3 != null) {
               var1 = this.listView.getItems().indexOf(var3);
               var2 = var1;
            }

            if (this.listView.selectFirstRowByDefault && var2 == -1) {
               var2 = this.listView.getItems().size() > 0 ? 0 : -1;
            }
         }

         this.clearSelection();
         this.select(var1);
         this.focus(var2);
      }
   }

   public static class EditEvent extends Event {
      private final Object newValue;
      private final int editIndex;
      private static final long serialVersionUID = 20130724L;
      public static final EventType ANY;

      public EditEvent(ListView var1, EventType var2, Object var3, int var4) {
         super(var1, Event.NULL_SOURCE_TARGET, var2);
         this.editIndex = var4;
         this.newValue = var3;
      }

      public ListView getSource() {
         return (ListView)super.getSource();
      }

      public int getIndex() {
         return this.editIndex;
      }

      public Object getNewValue() {
         return this.newValue;
      }

      public String toString() {
         return "ListViewEditEvent [ newValue: " + this.getNewValue() + ", ListView: " + this.getSource() + " ]";
      }

      static {
         ANY = ListView.EDIT_ANY_EVENT;
      }
   }

   private static class StyleableProperties {
      private static final CssMetaData ORIENTATION;
      private static final CssMetaData FIXED_CELL_SIZE;
      private static final List STYLEABLES;

      static {
         ORIENTATION = new CssMetaData("-fx-orientation", new EnumConverter(Orientation.class), Orientation.VERTICAL) {
            public Orientation getInitialValue(ListView var1) {
               return var1.getOrientation();
            }

            public boolean isSettable(ListView var1) {
               return var1.orientation == null || !var1.orientation.isBound();
            }

            public StyleableProperty getStyleableProperty(ListView var1) {
               return (StyleableProperty)var1.orientationProperty();
            }
         };
         FIXED_CELL_SIZE = new CssMetaData("-fx-fixed-cell-size", SizeConverter.getInstance(), -1.0) {
            public Double getInitialValue(ListView var1) {
               return var1.getFixedCellSize();
            }

            public boolean isSettable(ListView var1) {
               return var1.fixedCellSize == null || !var1.fixedCellSize.isBound();
            }

            public StyleableProperty getStyleableProperty(ListView var1) {
               return (StyleableProperty)var1.fixedCellSizeProperty();
            }
         };
         ArrayList var0 = new ArrayList(Control.getClassCssMetaData());
         var0.add(ORIENTATION);
         var0.add(FIXED_CELL_SIZE);
         STYLEABLES = Collections.unmodifiableList(var0);
      }
   }
}
