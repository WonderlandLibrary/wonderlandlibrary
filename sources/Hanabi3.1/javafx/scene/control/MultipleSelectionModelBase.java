package javafx.scene.control;

import com.sun.javafx.collections.MappingChange;
import com.sun.javafx.collections.NonIterableChange;
import com.sun.javafx.scene.control.ReadOnlyUnbackedObservableList;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;

abstract class MultipleSelectionModelBase extends MultipleSelectionModel {
   final BitSet selectedIndices;
   final BitSetReadOnlyUnbackedObservableList selectedIndicesSeq;
   private final ReadOnlyUnbackedObservableList selectedItemsSeq;
   ListChangeListener.Change selectedItemChange;
   private int atomicityCount = 0;

   public MultipleSelectionModelBase() {
      this.selectedIndexProperty().addListener((var1x) -> {
         this.setSelectedItem(this.getModelItem(this.getSelectedIndex()));
      });
      this.selectedIndices = new BitSet();
      this.selectedIndicesSeq = new BitSetReadOnlyUnbackedObservableList(this.selectedIndices);
      final MappingChange.Map var1 = (var1x) -> {
         return this.getModelItem(var1x);
      };
      this.selectedIndicesSeq.addListener(new ListChangeListener() {
         public void onChanged(ListChangeListener.Change var1x) {
            boolean var2;
            for(var2 = false; var1x.next() && !var2; var2 = var1x.wasAdded() || var1x.wasRemoved()) {
            }

            if (var2) {
               if (MultipleSelectionModelBase.this.selectedItemChange != null) {
                  MultipleSelectionModelBase.this.selectedItemsSeq.callObservers(MultipleSelectionModelBase.this.selectedItemChange);
               } else {
                  var1x.reset();
                  MultipleSelectionModelBase.this.selectedItemsSeq.callObservers(new MappingChange(var1x, var1, MultipleSelectionModelBase.this.selectedItemsSeq));
               }
            }

            var1x.reset();
         }
      });
      this.selectedItemsSeq = new ReadOnlyUnbackedObservableList() {
         public Object get(int var1) {
            int var2 = MultipleSelectionModelBase.this.selectedIndicesSeq.get(var1);
            return MultipleSelectionModelBase.this.getModelItem(var2);
         }

         public int size() {
            return MultipleSelectionModelBase.this.selectedIndices.cardinality();
         }
      };
   }

   public ObservableList getSelectedIndices() {
      return this.selectedIndicesSeq;
   }

   public ObservableList getSelectedItems() {
      return this.selectedItemsSeq;
   }

   boolean isAtomic() {
      return this.atomicityCount > 0;
   }

   void startAtomic() {
      ++this.atomicityCount;
   }

   void stopAtomic() {
      this.atomicityCount = Math.max(0, --this.atomicityCount);
   }

   protected abstract int getItemCount();

   protected abstract Object getModelItem(int var1);

   protected abstract void focus(int var1);

   protected abstract int getFocusedIndex();

   void shiftSelection(int var1, int var2, Callback var3) {
      if (var1 >= 0) {
         if (var2 != 0) {
            int var4 = this.selectedIndices.cardinality();
            if (var4 != 0) {
               int var5 = this.selectedIndices.size();
               int[] var6 = new int[var5];
               int var7 = 0;
               boolean var8 = false;
               int var9;
               boolean var10;
               if (var2 > 0) {
                  for(var9 = var5 - 1; var9 >= var1 && var9 >= 0; --var9) {
                     var10 = this.selectedIndices.get(var9);
                     if (var3 == null) {
                        this.selectedIndices.clear(var9);
                        this.selectedIndices.set(var9 + var2, var10);
                     } else {
                        var3.call(new ShiftParams(var9, var9 + var2, var10));
                     }

                     if (var10) {
                        var6[var7++] = var9 + 1;
                        var8 = true;
                     }
                  }

                  this.selectedIndices.clear(var1);
               } else if (var2 < 0) {
                  for(var9 = var1; var9 < var5; ++var9) {
                     if (var9 + var2 >= 0 && var9 + 1 + var2 >= var1) {
                        var10 = this.selectedIndices.get(var9 + 1);
                        if (var3 == null) {
                           this.selectedIndices.clear(var9 + 1);
                           this.selectedIndices.set(var9 + 1 + var2, var10);
                        } else {
                           var3.call(new ShiftParams(var9 + 1, var9 + 1 + var2, var10));
                        }

                        if (var10) {
                           var6[var7++] = var9;
                           var8 = true;
                        }
                     }
                  }
               }

               var9 = this.getSelectedIndex();
               if (var9 >= var1 && var9 > -1) {
                  int var11 = Math.max(0, var9 + var2);
                  this.setSelectedIndex(var11);
                  if (var8) {
                     this.selectedIndices.set(var11, true);
                  } else {
                     this.select(var11);
                  }
               }

               if (var8) {
                  this.selectedIndicesSeq.callObservers(new NonIterableChange.SimplePermutationChange(0, var4, var6, this.selectedIndicesSeq));
               }

            }
         }
      }
   }

   public void clearAndSelect(int var1) {
      if (var1 >= 0 && var1 < this.getItemCount()) {
         boolean var2 = this.isSelected(var1);
         if (!var2 || this.getSelectedIndices().size() != 1 || this.getSelectedItem() != this.getModelItem(var1)) {
            BitSet var3 = new BitSet();
            var3.or(this.selectedIndices);
            var3.clear(var1);
            BitSetReadOnlyUnbackedObservableList var4 = new BitSetReadOnlyUnbackedObservableList(var3);
            this.startAtomic();
            this.clearSelection();
            this.select(var1);
            this.stopAtomic();
            Object var5;
            if (var2) {
               var5 = ControlUtils.buildClearAndSelectChange(this.selectedIndicesSeq, var4, var1);
            } else {
               int var6 = this.selectedIndicesSeq.indexOf(var1);
               var5 = new NonIterableChange.GenericAddRemoveChange(var6, var6 + 1, var4, this.selectedIndicesSeq);
            }

            this.selectedIndicesSeq.callObservers((ListChangeListener.Change)var5);
         }
      } else {
         this.clearSelection();
      }
   }

   public void select(int var1) {
      if (var1 == -1) {
         this.clearSelection();
      } else if (var1 >= 0 && var1 < this.getItemCount()) {
         boolean var2 = var1 == this.getSelectedIndex();
         Object var3 = this.getSelectedItem();
         Object var4 = this.getModelItem(var1);
         boolean var5 = var4 != null && var4.equals(var3);
         boolean var6 = var2 && !var5;
         this.startAtomic();
         if (!this.selectedIndices.get(var1)) {
            if (this.getSelectionMode() == SelectionMode.SINGLE) {
               this.quietClearSelection();
            }

            this.selectedIndices.set(var1);
         }

         this.setSelectedIndex(var1);
         this.focus(var1);
         this.stopAtomic();
         if (!this.isAtomic()) {
            int var7 = Math.max(0, this.selectedIndicesSeq.indexOf(var1));
            this.selectedIndicesSeq.callObservers(new NonIterableChange.SimpleAddChange(var7, var7 + 1, this.selectedIndicesSeq));
         }

         if (var6) {
            this.setSelectedItem(var4);
         }

      }
   }

   public void select(Object var1) {
      if (var1 == null && this.getSelectionMode() == SelectionMode.SINGLE) {
         this.clearSelection();
      } else {
         Object var2 = null;
         int var3 = 0;

         for(int var4 = this.getItemCount(); var3 < var4; ++var3) {
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
      if (var2 != null && var2.length != 0) {
         int var3 = this.getItemCount();
         int var5;
         if (this.getSelectionMode() == SelectionMode.SINGLE) {
            this.quietClearSelection();

            for(int var4 = var2.length - 1; var4 >= 0; --var4) {
               var5 = var2[var4];
               if (var5 >= 0 && var5 < var3) {
                  this.selectedIndices.set(var5);
                  this.select(var5);
                  break;
               }
            }

            if (this.selectedIndices.isEmpty() && var1 > 0 && var1 < var3) {
               this.selectedIndices.set(var1);
               this.select(var1);
            }

            this.selectedIndicesSeq.callObservers(new NonIterableChange.SimpleAddChange(0, 1, this.selectedIndicesSeq));
         } else {
            ArrayList var8 = new ArrayList();
            var5 = -1;
            if (var1 >= 0 && var1 < var3) {
               var5 = var1;
               if (!this.selectedIndices.get(var1)) {
                  this.selectedIndices.set(var1);
                  var8.add(var1);
               }
            }

            for(int var6 = 0; var6 < var2.length; ++var6) {
               int var7 = var2[var6];
               if (var7 >= 0 && var7 < var3) {
                  var5 = var7;
                  if (!this.selectedIndices.get(var7)) {
                     this.selectedIndices.set(var7);
                     var8.add(var7);
                  }
               }
            }

            if (var5 != -1) {
               this.setSelectedIndex(var5);
               this.focus(var5);
               this.setSelectedItem(this.getModelItem(var5));
            }

            Collections.sort(var8);
            ListChangeListener.Change var9 = createRangeChange(this.selectedIndicesSeq, var8, false);
            this.selectedIndicesSeq.callObservers(var9);
         }

      } else {
         this.select(var1);
      }
   }

   static ListChangeListener.Change createRangeChange(final ObservableList var0, final List var1, final boolean var2) {
      ListChangeListener.Change var3 = new ListChangeListener.Change(var0) {
         private final int[] EMPTY_PERM = new int[0];
         private final int addedSize = var1.size();
         private boolean invalid = true;
         private int pos = 0;
         private int from;
         private int to;

         {
            this.from = this.pos;
            this.to = this.pos;
         }

         public int getFrom() {
            this.checkState();
            return this.from;
         }

         public int getTo() {
            this.checkState();
            return this.to;
         }

         public List getRemoved() {
            this.checkState();
            return Collections.emptyList();
         }

         protected int[] getPermutation() {
            this.checkState();
            return this.EMPTY_PERM;
         }

         public int getAddedSize() {
            return this.to - this.from;
         }

         public boolean next() {
            if (this.pos >= this.addedSize) {
               return false;
            } else {
               int var1x = (Integer)var1.get(this.pos++);
               this.from = var0.indexOf(var1x);
               this.to = this.from + 1;
               int var2x = var1x;

               while(this.pos < this.addedSize) {
                  int var3 = var2x;
                  var2x = (Integer)var1.get(this.pos++);
                  ++this.to;
                  if (var2 && var3 != var2x - 1) {
                     break;
                  }
               }

               if (this.invalid) {
                  this.invalid = false;
                  return true;
               } else {
                  return var2 && this.pos < this.addedSize;
               }
            }
         }

         public void reset() {
            this.invalid = true;
            this.pos = 0;
            this.to = 0;
            this.from = 0;
         }

         private void checkState() {
            if (this.invalid) {
               throw new IllegalStateException("Invalid Change state: next() must be called before inspecting the Change.");
            }
         }
      };
      return var3;
   }

   public void selectAll() {
      if (this.getSelectionMode() != SelectionMode.SINGLE) {
         if (this.getItemCount() > 0) {
            int var1 = this.getItemCount();
            int var2 = this.getFocusedIndex();
            this.clearSelection();
            this.selectedIndices.set(0, var1, true);
            this.selectedIndicesSeq.callObservers(new NonIterableChange.SimpleAddChange(0, var1, this.selectedIndicesSeq));
            if (var2 == -1) {
               this.setSelectedIndex(var1 - 1);
               this.focus(var1 - 1);
            } else {
               this.setSelectedIndex(var2);
               this.focus(var2);
            }

         }
      }
   }

   public void selectFirst() {
      if (this.getSelectionMode() == SelectionMode.SINGLE) {
         this.quietClearSelection();
      }

      if (this.getItemCount() > 0) {
         this.select(0);
      }

   }

   public void selectLast() {
      if (this.getSelectionMode() == SelectionMode.SINGLE) {
         this.quietClearSelection();
      }

      int var1 = this.getItemCount();
      if (var1 > 0 && this.getSelectedIndex() < var1 - 1) {
         this.select(var1 - 1);
      }

   }

   public void clearSelection(int var1) {
      if (var1 >= 0) {
         boolean var2 = this.selectedIndices.isEmpty();
         this.selectedIndices.clear(var1);
         if (!var2 && this.selectedIndices.isEmpty()) {
            this.clearSelection();
         }

         if (!this.isAtomic()) {
            this.selectedIndicesSeq.callObservers(new NonIterableChange.GenericAddRemoveChange(var1, var1, Collections.singletonList(var1), this.selectedIndicesSeq));
         }

      }
   }

   public void clearSelection() {
      BitSetReadOnlyUnbackedObservableList var1 = new BitSetReadOnlyUnbackedObservableList((BitSet)this.selectedIndices.clone());
      this.quietClearSelection();
      if (!this.isAtomic()) {
         this.setSelectedIndex(-1);
         this.focus(-1);
         this.selectedIndicesSeq.callObservers(new NonIterableChange.GenericAddRemoveChange(0, 0, var1, this.selectedIndicesSeq));
      }

   }

   private void quietClearSelection() {
      this.selectedIndices.clear();
   }

   public boolean isSelected(int var1) {
      return var1 >= 0 && var1 < this.selectedIndices.length() ? this.selectedIndices.get(var1) : false;
   }

   public boolean isEmpty() {
      return this.selectedIndices.isEmpty();
   }

   public void selectPrevious() {
      int var1 = this.getFocusedIndex();
      if (this.getSelectionMode() == SelectionMode.SINGLE) {
         this.quietClearSelection();
      }

      if (var1 == -1) {
         this.select(this.getItemCount() - 1);
      } else if (var1 > 0) {
         this.select(var1 - 1);
      }

   }

   public void selectNext() {
      int var1 = this.getFocusedIndex();
      if (this.getSelectionMode() == SelectionMode.SINGLE) {
         this.quietClearSelection();
      }

      if (var1 == -1) {
         this.select(0);
      } else if (var1 != this.getItemCount() - 1) {
         this.select(var1 + 1);
      }

   }

   class BitSetReadOnlyUnbackedObservableList extends ReadOnlyUnbackedObservableList {
      private final BitSet bitset;
      private int lastGetIndex = -1;
      private int lastGetValue = -1;

      public BitSetReadOnlyUnbackedObservableList(BitSet var2) {
         this.bitset = var2;
      }

      public Integer get(int var1) {
         int var2 = MultipleSelectionModelBase.this.getItemCount();
         if (var1 >= 0 && var1 < var2) {
            if (var1 == this.lastGetIndex + 1 && this.lastGetValue < var2) {
               ++this.lastGetIndex;
               this.lastGetValue = this.bitset.nextSetBit(this.lastGetValue + 1);
               return this.lastGetValue;
            } else if (var1 == this.lastGetIndex - 1 && this.lastGetValue > 0) {
               --this.lastGetIndex;
               this.lastGetValue = this.bitset.previousSetBit(this.lastGetValue - 1);
               return this.lastGetValue;
            } else {
               this.lastGetIndex = 0;

               for(this.lastGetValue = this.bitset.nextSetBit(0); this.lastGetValue >= 0 || this.lastGetIndex == var1; this.lastGetValue = this.bitset.nextSetBit(this.lastGetValue + 1)) {
                  if (this.lastGetIndex == var1) {
                     return this.lastGetValue;
                  }

                  ++this.lastGetIndex;
               }

               return -1;
            }
         } else {
            return -1;
         }
      }

      public int size() {
         return this.bitset.cardinality();
      }

      public boolean contains(Object var1) {
         if (!(var1 instanceof Number)) {
            return false;
         } else {
            Number var2 = (Number)var1;
            int var3 = var2.intValue();
            return var3 >= 0 && var3 < this.bitset.length() && this.bitset.get(var3);
         }
      }

      public void reset() {
         this.lastGetIndex = -1;
         this.lastGetValue = -1;
      }
   }

   static class ShiftParams {
      private final int clearIndex;
      private final int setIndex;
      private final boolean selected;

      ShiftParams(int var1, int var2, boolean var3) {
         this.clearIndex = var1;
         this.setIndex = var2;
         this.selected = var3;
      }

      public final int getClearIndex() {
         return this.clearIndex;
      }

      public final int getSetIndex() {
         return this.setIndex;
      }

      public final boolean isSelected() {
         return this.selected;
      }
   }
}
