package javafx.scene.control;

import com.sun.javafx.scene.control.skin.Utils;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

class ControlUtils {
   private ControlUtils() {
   }

   public static void scrollToIndex(Control var0, int var1) {
      Utils.executeOnceWhenPropertyIsNonNull(var0.skinProperty(), (var2) -> {
         Event.fireEvent(var0, new ScrollToEvent(var0, var0, ScrollToEvent.scrollToTopIndex(), var1));
      });
   }

   public static void scrollToColumn(Control var0, TableColumnBase var1) {
      Utils.executeOnceWhenPropertyIsNonNull(var0.skinProperty(), (var2) -> {
         var0.fireEvent(new ScrollToEvent(var0, var0, ScrollToEvent.scrollToColumn(), var1));
      });
   }

   static void requestFocusOnControlOnlyIfCurrentFocusOwnerIsChild(Control var0) {
      Scene var1 = var0.getScene();
      Node var2 = var1 == null ? null : var1.getFocusOwner();
      if (var2 == null) {
         var0.requestFocus();
      } else if (!var0.equals(var2)) {
         for(Parent var3 = var2.getParent(); var3 != null; var3 = var3.getParent()) {
            if (var0.equals(var3)) {
               var0.requestFocus();
               break;
            }
         }
      }

   }

   static ListChangeListener.Change buildClearAndSelectChange(ObservableList var0, final List var1, final int var2) {
      return new ListChangeListener.Change(var0) {
         private final int[] EMPTY_PERM = new int[0];
         private final int removedSize = var1.size();
         private final List firstRemovedRange;
         private final List secondRemovedRange;
         private boolean invalid = true;
         private boolean atFirstRange = true;
         private int from = -1;

         {
            int var4 = var2 >= this.removedSize ? this.removedSize : (var2 < 0 ? 0 : var2);
            this.firstRemovedRange = var1.subList(0, var4);
            this.secondRemovedRange = var1.subList(var4, this.removedSize);
         }

         public int getFrom() {
            this.checkState();
            return this.from;
         }

         public int getTo() {
            return this.getFrom();
         }

         public List getRemoved() {
            this.checkState();
            return this.atFirstRange ? this.firstRemovedRange : this.secondRemovedRange;
         }

         public int getRemovedSize() {
            return this.atFirstRange ? this.firstRemovedRange.size() : this.secondRemovedRange.size();
         }

         protected int[] getPermutation() {
            this.checkState();
            return this.EMPTY_PERM;
         }

         public boolean next() {
            if (this.invalid && this.atFirstRange) {
               this.invalid = false;
               this.from = 0;
               return true;
            } else if (this.atFirstRange && !this.secondRemovedRange.isEmpty()) {
               this.atFirstRange = false;
               this.from = 1;
               return true;
            } else {
               return false;
            }
         }

         public void reset() {
            this.invalid = true;
            this.atFirstRange = true;
         }

         private void checkState() {
            if (this.invalid) {
               throw new IllegalStateException("Invalid Change state: next() must be called before inspecting the Change.");
            }
         }
      };
   }
}
