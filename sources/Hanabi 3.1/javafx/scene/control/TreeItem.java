package javafx.scene.control;

import com.sun.javafx.event.EventHandlerManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Node;

public class TreeItem implements EventTarget {
   private static final EventType TREE_NOTIFICATION_EVENT;
   private static final EventType EXPANDED_ITEM_COUNT_CHANGE_EVENT;
   private static final EventType BRANCH_EXPANDED_EVENT;
   private static final EventType BRANCH_COLLAPSED_EVENT;
   private static final EventType CHILDREN_MODIFICATION_EVENT;
   private static final EventType VALUE_CHANGED_EVENT;
   private static final EventType GRAPHIC_CHANGED_EVENT;
   private final EventHandler itemListener;
   private boolean ignoreSortUpdate;
   private boolean expandedDescendentCountDirty;
   ObservableList children;
   private final EventHandlerManager eventHandlerManager;
   private int expandedDescendentCount;
   int previousExpandedDescendentCount;
   Comparator lastComparator;
   TreeSortMode lastSortMode;
   private int parentLinkCount;
   private ListChangeListener childrenListener;
   private ObjectProperty value;
   private ObjectProperty graphic;
   private BooleanProperty expanded;
   private ReadOnlyBooleanWrapper leaf;
   private ReadOnlyObjectWrapper parent;

   public static EventType treeNotificationEvent() {
      return TREE_NOTIFICATION_EVENT;
   }

   public static EventType expandedItemCountChangeEvent() {
      return EXPANDED_ITEM_COUNT_CHANGE_EVENT;
   }

   public static EventType branchExpandedEvent() {
      return BRANCH_EXPANDED_EVENT;
   }

   public static EventType branchCollapsedEvent() {
      return BRANCH_COLLAPSED_EVENT;
   }

   public static EventType childrenModificationEvent() {
      return CHILDREN_MODIFICATION_EVENT;
   }

   public static EventType valueChangedEvent() {
      return VALUE_CHANGED_EVENT;
   }

   public static EventType graphicChangedEvent() {
      return GRAPHIC_CHANGED_EVENT;
   }

   public TreeItem() {
      this((Object)null);
   }

   public TreeItem(Object var1) {
      this(var1, (Node)null);
   }

   public TreeItem(Object var1, Node var2) {
      this.itemListener = new EventHandler() {
         public void handle(TreeModificationEvent var1) {
            TreeItem.this.expandedDescendentCountDirty = true;
         }
      };
      this.ignoreSortUpdate = false;
      this.expandedDescendentCountDirty = true;
      this.eventHandlerManager = new EventHandlerManager(this);
      this.expandedDescendentCount = 1;
      this.previousExpandedDescendentCount = 1;
      this.lastComparator = null;
      this.lastSortMode = null;
      this.parentLinkCount = 0;
      this.childrenListener = (var1x) -> {
         this.expandedDescendentCountDirty = true;
         this.updateChildren(var1x);
      };
      this.parent = new ReadOnlyObjectWrapper(this, "parent");
      this.setValue(var1);
      this.setGraphic(var2);
      this.addEventHandler(expandedItemCountChangeEvent(), this.itemListener);
   }

   public final void setValue(Object var1) {
      this.valueProperty().setValue(var1);
   }

   public final Object getValue() {
      return this.value == null ? null : this.value.getValue();
   }

   public final ObjectProperty valueProperty() {
      if (this.value == null) {
         this.value = new ObjectPropertyBase() {
            protected void invalidated() {
               TreeItem.this.fireEvent(new TreeModificationEvent(TreeItem.VALUE_CHANGED_EVENT, TreeItem.this, this.get()));
            }

            public Object getBean() {
               return TreeItem.this;
            }

            public String getName() {
               return "value";
            }
         };
      }

      return this.value;
   }

   public final void setGraphic(Node var1) {
      this.graphicProperty().setValue(var1);
   }

   public final Node getGraphic() {
      return this.graphic == null ? null : (Node)this.graphic.getValue();
   }

   public final ObjectProperty graphicProperty() {
      if (this.graphic == null) {
         this.graphic = new ObjectPropertyBase() {
            protected void invalidated() {
               TreeItem.this.fireEvent(new TreeModificationEvent(TreeItem.GRAPHIC_CHANGED_EVENT, TreeItem.this));
            }

            public Object getBean() {
               return TreeItem.this;
            }

            public String getName() {
               return "graphic";
            }
         };
      }

      return this.graphic;
   }

   public final void setExpanded(boolean var1) {
      if (var1 || this.expanded != null) {
         this.expandedProperty().setValue(var1);
      }
   }

   public final boolean isExpanded() {
      return this.expanded == null ? false : this.expanded.getValue();
   }

   public final BooleanProperty expandedProperty() {
      if (this.expanded == null) {
         this.expanded = new BooleanPropertyBase() {
            protected void invalidated() {
               if (!TreeItem.this.isLeaf()) {
                  EventType var1 = TreeItem.this.isExpanded() ? TreeItem.BRANCH_EXPANDED_EVENT : TreeItem.BRANCH_COLLAPSED_EVENT;
                  TreeItem.this.fireEvent(new TreeModificationEvent(var1, TreeItem.this, TreeItem.this.isExpanded()));
               }
            }

            public Object getBean() {
               return TreeItem.this;
            }

            public String getName() {
               return "expanded";
            }
         };
      }

      return this.expanded;
   }

   private void setLeaf(boolean var1) {
      if (!var1 || this.leaf != null) {
         if (this.leaf == null) {
            this.leaf = new ReadOnlyBooleanWrapper(this, "leaf", true);
         }

         this.leaf.setValue(var1);
      }
   }

   public boolean isLeaf() {
      return this.leaf == null ? true : this.leaf.getValue();
   }

   public final ReadOnlyBooleanProperty leafProperty() {
      if (this.leaf == null) {
         this.leaf = new ReadOnlyBooleanWrapper(this, "leaf", true);
      }

      return this.leaf.getReadOnlyProperty();
   }

   private void setParent(TreeItem var1) {
      this.parent.setValue(var1);
   }

   public final TreeItem getParent() {
      return this.parent == null ? null : (TreeItem)this.parent.getValue();
   }

   public final ReadOnlyObjectProperty parentProperty() {
      return this.parent.getReadOnlyProperty();
   }

   public ObservableList getChildren() {
      if (this.children == null) {
         this.children = FXCollections.observableArrayList();
         this.children.addListener(this.childrenListener);
      }

      if (this.children.isEmpty()) {
         return this.children;
      } else {
         if (!this.ignoreSortUpdate) {
            this.checkSortState();
         }

         return this.children;
      }
   }

   public TreeItem previousSibling() {
      return this.previousSibling(this);
   }

   public TreeItem previousSibling(TreeItem var1) {
      if (this.getParent() != null && var1 != null) {
         ObservableList var2 = this.getParent().getChildren();
         int var3 = var2.size();
         boolean var4 = true;

         for(int var5 = 0; var5 < var3; ++var5) {
            if (var1.equals(var2.get(var5))) {
               int var6 = var5 - 1;
               return var6 < 0 ? null : (TreeItem)var2.get(var6);
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public TreeItem nextSibling() {
      return this.nextSibling(this);
   }

   public TreeItem nextSibling(TreeItem var1) {
      if (this.getParent() != null && var1 != null) {
         ObservableList var2 = this.getParent().getChildren();
         int var3 = var2.size();
         boolean var4 = true;

         for(int var5 = 0; var5 < var3; ++var5) {
            if (var1.equals(var2.get(var5))) {
               int var6 = var5 + 1;
               return var6 >= var3 ? null : (TreeItem)var2.get(var6);
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public String toString() {
      return "TreeItem [ value: " + this.getValue() + " ]";
   }

   private void fireEvent(TreeModificationEvent var1) {
      Event.fireEvent(this, var1);
   }

   public EventDispatchChain buildEventDispatchChain(EventDispatchChain var1) {
      if (this.getParent() != null) {
         this.getParent().buildEventDispatchChain(var1);
      }

      return var1.append(this.eventHandlerManager);
   }

   public void addEventHandler(EventType var1, EventHandler var2) {
      this.eventHandlerManager.addEventHandler(var1, var2);
   }

   public void removeEventHandler(EventType var1, EventHandler var2) {
      this.eventHandlerManager.removeEventHandler(var1, var2);
   }

   void sort() {
      this.sort(this.children, this.lastComparator, this.lastSortMode);
   }

   private void sort(ObservableList var1, Comparator var2, TreeSortMode var3) {
      if (var2 != null) {
         this.runSort(var1, var2, var3);
         if (this.getParent() == null) {
            TreeModificationEvent var4 = new TreeModificationEvent(childrenModificationEvent(), this);
            var4.wasPermutated = true;
            this.fireEvent(var4);
         }

      }
   }

   private void checkSortState() {
      TreeItem var1 = this.getRoot();
      TreeSortMode var2 = var1.lastSortMode;
      Comparator var3 = var1.lastComparator;
      if (var3 != null && var3 != this.lastComparator) {
         this.lastComparator = var3;
         this.runSort(this.children, var3, var2);
      }

   }

   private void runSort(ObservableList var1, Comparator var2, TreeSortMode var3) {
      if (var3 == TreeSortMode.ALL_DESCENDANTS) {
         this.doSort(var1, var2);
      } else if (var3 == TreeSortMode.ONLY_FIRST_LEVEL && this.getParent() == null) {
         this.doSort(var1, var2);
      }

   }

   private TreeItem getRoot() {
      TreeItem var1 = this.getParent();
      if (var1 == null) {
         return this;
      } else {
         while(true) {
            TreeItem var2 = var1.getParent();
            if (var2 == null) {
               return var1;
            }

            var1 = var2;
         }
      }
   }

   private void doSort(ObservableList var1, Comparator var2) {
      if (!this.isLeaf() && this.isExpanded()) {
         FXCollections.sort(var1, var2);
      }

   }

   int getExpandedDescendentCount(boolean var1) {
      if (var1 || this.expandedDescendentCountDirty) {
         this.updateExpandedDescendentCount(var1);
         this.expandedDescendentCountDirty = false;
      }

      return this.expandedDescendentCount;
   }

   private void updateExpandedDescendentCount(boolean var1) {
      this.previousExpandedDescendentCount = this.expandedDescendentCount;
      this.expandedDescendentCount = 1;
      this.ignoreSortUpdate = true;
      if (!this.isLeaf() && this.isExpanded()) {
         Iterator var2 = this.getChildren().iterator();

         while(var2.hasNext()) {
            TreeItem var3 = (TreeItem)var2.next();
            if (var3 != null) {
               this.expandedDescendentCount += var3.isExpanded() ? var3.getExpandedDescendentCount(var1) : 1;
            }
         }
      }

      this.ignoreSortUpdate = false;
   }

   private void updateChildren(ListChangeListener.Change var1) {
      this.setLeaf(this.children.isEmpty());
      ArrayList var2 = new ArrayList();
      ArrayList var3 = new ArrayList();

      while(var1.next()) {
         var2.addAll(var1.getAddedSubList());
         var3.addAll(var1.getRemoved());
      }

      updateChildrenParent(var3, (TreeItem)null);
      updateChildrenParent(var2, this);
      var1.reset();
      this.fireEvent(new TreeModificationEvent(CHILDREN_MODIFICATION_EVENT, this, var2, var3, var1));
   }

   private static void updateChildrenParent(List var0, TreeItem var1) {
      if (var0 != null) {
         Iterator var2 = var0.iterator();

         while(true) {
            TreeItem var3;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               var3 = (TreeItem)var2.next();
            } while(var3 == null);

            TreeItem var4 = var3.getParent();
            if (var3.parentLinkCount == 0) {
               var3.setParent(var1);
            }

            boolean var5 = var4 != null && var4.equals(var1);
            if (var5) {
               if (var1 == null) {
                  --var3.parentLinkCount;
               } else {
                  ++var3.parentLinkCount;
               }
            }
         }
      }
   }

   static {
      TREE_NOTIFICATION_EVENT = new EventType(Event.ANY, "TreeNotificationEvent");
      EXPANDED_ITEM_COUNT_CHANGE_EVENT = new EventType(treeNotificationEvent(), "ExpandedItemCountChangeEvent");
      BRANCH_EXPANDED_EVENT = new EventType(expandedItemCountChangeEvent(), "BranchExpandedEvent");
      BRANCH_COLLAPSED_EVENT = new EventType(expandedItemCountChangeEvent(), "BranchCollapsedEvent");
      CHILDREN_MODIFICATION_EVENT = new EventType(expandedItemCountChangeEvent(), "ChildrenModificationEvent");
      VALUE_CHANGED_EVENT = new EventType(treeNotificationEvent(), "ValueChangedEvent");
      GRAPHIC_CHANGED_EVENT = new EventType(treeNotificationEvent(), "GraphicChangedEvent");
   }

   public static class TreeModificationEvent extends Event {
      private static final long serialVersionUID = 4741889985221719579L;
      public static final EventType ANY;
      private final transient TreeItem treeItem;
      private final Object newValue;
      private final List added;
      private final List removed;
      private final ListChangeListener.Change change;
      private final boolean wasExpanded;
      private final boolean wasCollapsed;
      private boolean wasPermutated;

      public TreeModificationEvent(EventType var1, TreeItem var2) {
         this(var1, var2, (Object)null);
      }

      public TreeModificationEvent(EventType var1, TreeItem var2, Object var3) {
         super(var1);
         this.treeItem = var2;
         this.newValue = var3;
         this.added = null;
         this.removed = null;
         this.change = null;
         this.wasExpanded = false;
         this.wasCollapsed = false;
      }

      public TreeModificationEvent(EventType var1, TreeItem var2, boolean var3) {
         super(var1);
         this.treeItem = var2;
         this.newValue = null;
         this.added = null;
         this.removed = null;
         this.change = null;
         this.wasExpanded = var3;
         this.wasCollapsed = !var3;
      }

      public TreeModificationEvent(EventType var1, TreeItem var2, List var3, List var4) {
         this(var1, var2, var3, var4, (ListChangeListener.Change)null);
      }

      private TreeModificationEvent(EventType var1, TreeItem var2, List var3, List var4, ListChangeListener.Change var5) {
         super(var1);
         this.treeItem = var2;
         this.newValue = null;
         this.added = var3;
         this.removed = var4;
         this.change = var5;
         this.wasExpanded = false;
         this.wasCollapsed = false;
         this.wasPermutated = var3 != null && var4 != null && var3.size() == var4.size() && var3.containsAll(var4);
      }

      public TreeItem getSource() {
         return this.treeItem;
      }

      public TreeItem getTreeItem() {
         return this.treeItem;
      }

      public Object getNewValue() {
         return this.newValue;
      }

      public List getAddedChildren() {
         return this.added == null ? Collections.emptyList() : this.added;
      }

      public List getRemovedChildren() {
         return this.removed == null ? Collections.emptyList() : this.removed;
      }

      public int getRemovedSize() {
         return this.getRemovedChildren().size();
      }

      public int getAddedSize() {
         return this.getAddedChildren().size();
      }

      public boolean wasExpanded() {
         return this.wasExpanded;
      }

      public boolean wasCollapsed() {
         return this.wasCollapsed;
      }

      public boolean wasAdded() {
         return this.getAddedSize() > 0;
      }

      public boolean wasRemoved() {
         return this.getRemovedSize() > 0;
      }

      public boolean wasPermutated() {
         return this.wasPermutated;
      }

      int getFrom() {
         return this.change == null ? -1 : this.change.getFrom();
      }

      int getTo() {
         return this.change == null ? -1 : this.change.getTo();
      }

      ListChangeListener.Change getChange() {
         return this.change;
      }

      // $FF: synthetic method
      TreeModificationEvent(EventType var1, TreeItem var2, List var3, List var4, ListChangeListener.Change var5, Object var6) {
         this(var1, var2, var3, var4, var5);
      }

      static {
         ANY = TreeItem.TREE_NOTIFICATION_EVENT;
      }
   }
}
