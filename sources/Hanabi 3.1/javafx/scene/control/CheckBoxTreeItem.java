package javafx.scene.control;

import java.util.Iterator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;

public class CheckBoxTreeItem extends TreeItem {
   private static final EventType CHECK_BOX_SELECTION_CHANGED_EVENT;
   private final ChangeListener stateChangeListener;
   private final BooleanProperty selected;
   private final BooleanProperty indeterminate;
   private final BooleanProperty independent;
   private static boolean updateLock;

   public static EventType checkBoxSelectionChangedEvent() {
      return CHECK_BOX_SELECTION_CHANGED_EVENT;
   }

   public CheckBoxTreeItem() {
      this((Object)null);
   }

   public CheckBoxTreeItem(Object var1) {
      this(var1, (Node)null, false);
   }

   public CheckBoxTreeItem(Object var1, Node var2) {
      this(var1, var2, false);
   }

   public CheckBoxTreeItem(Object var1, Node var2, boolean var3) {
      this(var1, var2, var3, false);
   }

   public CheckBoxTreeItem(Object var1, Node var2, boolean var3, boolean var4) {
      super(var1, var2);
      this.stateChangeListener = (var1x, var2x, var3x) -> {
         this.updateState();
      };
      this.selected = new SimpleBooleanProperty(this, "selected", false) {
         protected void invalidated() {
            super.invalidated();
            CheckBoxTreeItem.this.fireEvent(CheckBoxTreeItem.this, true);
         }
      };
      this.indeterminate = new SimpleBooleanProperty(this, "indeterminate", false) {
         protected void invalidated() {
            super.invalidated();
            CheckBoxTreeItem.this.fireEvent(CheckBoxTreeItem.this, false);
         }
      };
      this.independent = new SimpleBooleanProperty(this, "independent", false);
      this.setSelected(var3);
      this.setIndependent(var4);
      this.selectedProperty().addListener(this.stateChangeListener);
      this.indeterminateProperty().addListener(this.stateChangeListener);
   }

   public final void setSelected(boolean var1) {
      this.selectedProperty().setValue(var1);
   }

   public final boolean isSelected() {
      return this.selected.getValue();
   }

   public final BooleanProperty selectedProperty() {
      return this.selected;
   }

   public final void setIndeterminate(boolean var1) {
      this.indeterminateProperty().setValue(var1);
   }

   public final boolean isIndeterminate() {
      return this.indeterminate.getValue();
   }

   public final BooleanProperty indeterminateProperty() {
      return this.indeterminate;
   }

   public final BooleanProperty independentProperty() {
      return this.independent;
   }

   public final void setIndependent(boolean var1) {
      this.independentProperty().setValue(var1);
   }

   public final boolean isIndependent() {
      return this.independent.getValue();
   }

   private void updateState() {
      if (!this.isIndependent()) {
         boolean var1 = !updateLock;
         updateLock = true;
         this.updateUpwards();
         if (var1) {
            updateLock = false;
         }

         if (!updateLock) {
            this.updateDownwards();
         }
      }
   }

   private void updateUpwards() {
      if (this.getParent() instanceof CheckBoxTreeItem) {
         CheckBoxTreeItem var1 = (CheckBoxTreeItem)this.getParent();
         int var2 = 0;
         int var3 = 0;
         Iterator var4 = var1.getChildren().iterator();

         while(true) {
            TreeItem var5;
            do {
               if (!var4.hasNext()) {
                  if (var2 == var1.getChildren().size()) {
                     var1.setSelected(true);
                     var1.setIndeterminate(false);
                  } else if (var2 == 0 && var3 == 0) {
                     var1.setSelected(false);
                     var1.setIndeterminate(false);
                  } else {
                     var1.setIndeterminate(true);
                  }

                  return;
               }

               var5 = (TreeItem)var4.next();
            } while(!(var5 instanceof CheckBoxTreeItem));

            CheckBoxTreeItem var6 = (CheckBoxTreeItem)var5;
            var2 += var6.isSelected() && !var6.isIndeterminate() ? 1 : 0;
            var3 += var6.isIndeterminate() ? 1 : 0;
         }
      }
   }

   private void updateDownwards() {
      if (!this.isLeaf()) {
         Iterator var1 = this.getChildren().iterator();

         while(var1.hasNext()) {
            TreeItem var2 = (TreeItem)var1.next();
            if (var2 instanceof CheckBoxTreeItem) {
               CheckBoxTreeItem var3 = (CheckBoxTreeItem)var2;
               var3.setSelected(this.isSelected());
            }
         }
      }

   }

   private void fireEvent(CheckBoxTreeItem var1, boolean var2) {
      TreeModificationEvent var3 = new TreeModificationEvent(CHECK_BOX_SELECTION_CHANGED_EVENT, var1, var2);
      Event.fireEvent(this, var3);
   }

   static {
      CHECK_BOX_SELECTION_CHANGED_EVENT = new EventType(CheckBoxTreeItem.TreeModificationEvent.ANY, "checkBoxSelectionChangedEvent");
      updateLock = false;
   }

   public static class TreeModificationEvent extends Event {
      private static final long serialVersionUID = -8445355590698862999L;
      private final transient CheckBoxTreeItem treeItem;
      private final boolean selectionChanged;
      public static final EventType ANY;

      public TreeModificationEvent(EventType var1, CheckBoxTreeItem var2, boolean var3) {
         super(var1);
         this.treeItem = var2;
         this.selectionChanged = var3;
      }

      public CheckBoxTreeItem getTreeItem() {
         return this.treeItem;
      }

      public boolean wasSelectionChanged() {
         return this.selectionChanged;
      }

      public boolean wasIndeterminateChanged() {
         return !this.selectionChanged;
      }

      static {
         ANY = new EventType(Event.ANY, "TREE_MODIFICATION");
      }
   }
}
