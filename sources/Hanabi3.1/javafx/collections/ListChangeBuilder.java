package javafx.collections;

import com.sun.javafx.collections.ChangeHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

final class ListChangeBuilder {
   private static final int[] EMPTY_PERM = new int[0];
   private final ObservableListBase list;
   private int changeLock;
   private List addRemoveChanges;
   private List updateChanges;
   private SubChange permutationChange;

   private void checkAddRemoveList() {
      if (this.addRemoveChanges == null) {
         this.addRemoveChanges = new ArrayList();
      }

   }

   private void checkState() {
      if (this.changeLock == 0) {
         throw new IllegalStateException("beginChange was not called on this builder");
      }
   }

   private int findSubChange(int var1, List var2) {
      int var3 = 0;
      int var4 = var2.size() - 1;

      while(var3 <= var4) {
         int var5 = (var3 + var4) / 2;
         SubChange var6 = (SubChange)var2.get(var5);
         if (var1 >= var6.to) {
            var3 = var5 + 1;
         } else {
            if (var1 >= var6.from) {
               return var5;
            }

            var4 = var5 - 1;
         }
      }

      return ~var3;
   }

   private void insertUpdate(int var1) {
      int var2 = this.findSubChange(var1, this.updateChanges);
      if (var2 < 0) {
         var2 = ~var2;
         SubChange var3;
         if (var2 > 0 && (var3 = (SubChange)this.updateChanges.get(var2 - 1)).to == var1) {
            var3.to = var1 + 1;
         } else if (var2 < this.updateChanges.size() && (var3 = (SubChange)this.updateChanges.get(var2)).from == var1 + 1) {
            var3.from = var1;
         } else {
            this.updateChanges.add(var2, new SubChange(var1, var1 + 1, (List)null, EMPTY_PERM, true));
         }
      }

   }

   private void insertRemoved(int var1, Object var2) {
      int var3 = this.findSubChange(var1, this.addRemoveChanges);
      SubChange var4;
      if (var3 < 0) {
         var3 = ~var3;
         if (var3 > 0 && (var4 = (SubChange)this.addRemoveChanges.get(var3 - 1)).to == var1) {
            var4.removed.add(var2);
            --var3;
         } else if (var3 < this.addRemoveChanges.size() && (var4 = (SubChange)this.addRemoveChanges.get(var3)).from == var1 + 1) {
            --var4.from;
            --var4.to;
            var4.removed.add(0, var2);
         } else {
            ArrayList var5 = new ArrayList();
            var5.add(var2);
            this.addRemoveChanges.add(var3, new SubChange(var1, var1, var5, EMPTY_PERM, false));
         }
      } else {
         var4 = (SubChange)this.addRemoveChanges.get(var3);
         --var4.to;
         if (var4.from == var4.to && (var4.removed == null || var4.removed.isEmpty())) {
            this.addRemoveChanges.remove(var3);
         }
      }

      for(int var6 = var3 + 1; var6 < this.addRemoveChanges.size(); ++var6) {
         SubChange var7 = (SubChange)this.addRemoveChanges.get(var6);
         --var7.from;
         --var7.to;
      }

   }

   private void insertAdd(int var1, int var2) {
      int var3 = this.findSubChange(var1, this.addRemoveChanges);
      int var4 = var2 - var1;
      SubChange var5;
      if (var3 < 0) {
         var3 = ~var3;
         if (var3 > 0 && (var5 = (SubChange)this.addRemoveChanges.get(var3 - 1)).to == var1) {
            var5.to = var2;
            --var3;
         } else {
            this.addRemoveChanges.add(var3, new SubChange(var1, var2, new ArrayList(), EMPTY_PERM, false));
         }
      } else {
         var5 = (SubChange)this.addRemoveChanges.get(var3);
         var5.to += var4;
      }

      for(int var7 = var3 + 1; var7 < this.addRemoveChanges.size(); ++var7) {
         SubChange var6 = (SubChange)this.addRemoveChanges.get(var7);
         var6.from += var4;
         var6.to += var4;
      }

   }

   private int compress(List var1) {
      int var2 = 0;
      SubChange var3 = (SubChange)var1.get(0);
      int var4 = 1;

      for(int var5 = var1.size(); var4 < var5; ++var4) {
         SubChange var6 = (SubChange)var1.get(var4);
         if (var3.to != var6.from) {
            var3 = var6;
         } else {
            var3.to = var6.to;
            if (var3.removed != null || var6.removed != null) {
               if (var3.removed == null) {
                  var3.removed = new ArrayList();
               }

               var3.removed.addAll(var6.removed);
            }

            var1.set(var4, (Object)null);
            ++var2;
         }
      }

      return var2;
   }

   ListChangeBuilder(ObservableListBase var1) {
      this.list = var1;
   }

   public void nextRemove(int var1, Object var2) {
      this.checkState();
      this.checkAddRemoveList();
      SubChange var3 = this.addRemoveChanges.isEmpty() ? null : (SubChange)this.addRemoveChanges.get(this.addRemoveChanges.size() - 1);
      if (var3 != null && var3.to == var1) {
         var3.removed.add(var2);
      } else if (var3 != null && var3.from == var1 + 1) {
         --var3.from;
         --var3.to;
         var3.removed.add(0, var2);
      } else {
         this.insertRemoved(var1, var2);
      }

      if (this.updateChanges != null && !this.updateChanges.isEmpty()) {
         int var4 = this.findSubChange(var1, this.updateChanges);
         if (var4 < 0) {
            var4 = ~var4;
         } else {
            SubChange var5 = (SubChange)this.updateChanges.get(var4);
            if (var5.from == var5.to - 1) {
               this.updateChanges.remove(var4);
            } else {
               --var5.to;
               ++var4;
            }
         }

         for(int var6 = var4; var6 < this.updateChanges.size(); ++var6) {
            --((SubChange)this.updateChanges.get(var6)).from;
            --((SubChange)this.updateChanges.get(var6)).to;
         }
      }

   }

   public void nextRemove(int var1, List var2) {
      this.checkState();

      for(int var3 = 0; var3 < var2.size(); ++var3) {
         this.nextRemove(var1, var2.get(var3));
      }

   }

   public void nextAdd(int var1, int var2) {
      this.checkState();
      this.checkAddRemoveList();
      SubChange var3 = this.addRemoveChanges.isEmpty() ? null : (SubChange)this.addRemoveChanges.get(this.addRemoveChanges.size() - 1);
      int var4 = var2 - var1;
      if (var3 != null && var3.to == var1) {
         var3.to = var2;
      } else if (var3 != null && var1 >= var3.from && var1 < var3.to) {
         var3.to += var4;
      } else {
         this.insertAdd(var1, var2);
      }

      if (this.updateChanges != null && !this.updateChanges.isEmpty()) {
         int var5 = this.findSubChange(var1, this.updateChanges);
         if (var5 < 0) {
            var5 = ~var5;
         } else {
            SubChange var6 = (SubChange)this.updateChanges.get(var5);
            this.updateChanges.add(var5 + 1, new SubChange(var2, var6.to + var2 - var1, (List)null, EMPTY_PERM, true));
            var6.to = var1;
            var5 += 2;
         }

         for(int var7 = var5; var7 < this.updateChanges.size(); ++var7) {
            SubChange var10000 = (SubChange)this.updateChanges.get(var7);
            var10000.from += var4;
            var10000 = (SubChange)this.updateChanges.get(var7);
            var10000.to += var4;
         }
      }

   }

   public void nextPermutation(int var1, int var2, int[] var3) {
      this.checkState();
      int var4 = var1;
      int var5 = var2;
      int[] var6 = var3;
      int var9;
      int var10;
      int var11;
      int var12;
      int var30;
      if (this.addRemoveChanges != null && !this.addRemoveChanges.isEmpty()) {
         int[] var7 = new int[this.list.size()];
         TreeSet var8 = new TreeSet();
         var9 = 0;
         var10 = 0;
         var11 = 0;

         for(var12 = this.addRemoveChanges.size(); var11 < var12; ++var11) {
            SubChange var13 = (SubChange)this.addRemoveChanges.get(var11);

            int var14;
            for(var14 = var9; var14 < var13.from; ++var14) {
               var7[var14 >= var1 && var14 < var2 ? var3[var14 - var1] : var14] = var14 + var10;
            }

            for(var14 = var13.from; var14 < var13.to; ++var14) {
               var7[var14 >= var1 && var14 < var2 ? var3[var14 - var1] : var14] = -1;
            }

            var9 = var13.to;
            var14 = var13.removed != null ? var13.removed.size() : 0;
            int var15 = var13.from + var10;

            for(int var16 = var13.from + var10 + var14; var15 < var16; ++var15) {
               var8.add(var15);
            }

            var10 += var14 - (var13.to - var13.from);
         }

         for(var11 = var9; var11 < var7.length; ++var11) {
            var7[var11 >= var1 && var11 < var2 ? var3[var11 - var1] : var11] = var11 + var10;
         }

         int[] var26 = new int[this.list.size() + var10];
         var12 = 0;

         for(var30 = 0; var30 < var26.length; ++var30) {
            if (var8.contains(var30)) {
               var26[var30] = var30;
            } else {
               while(var7[var12] == -1) {
                  ++var12;
               }

               var26[var7[var12++]] = var30;
            }
         }

         var4 = 0;
         var5 = var26.length;
         var6 = var26;
      }

      int var18;
      if (this.permutationChange != null) {
         int var17;
         if (var4 == this.permutationChange.from && var5 == this.permutationChange.to) {
            for(var17 = 0; var17 < var6.length; ++var17) {
               this.permutationChange.perm[var17] = var6[this.permutationChange.perm[var17] - var4];
            }
         } else {
            var17 = Math.max(this.permutationChange.to, var5);
            var18 = Math.min(this.permutationChange.from, var4);
            int[] var21 = new int[var17 - var18];

            for(var10 = var18; var10 < var17; ++var10) {
               if (var10 >= this.permutationChange.from && var10 < this.permutationChange.to) {
                  var11 = this.permutationChange.perm[var10 - this.permutationChange.from];
                  if (var11 >= var4 && var11 < var5) {
                     var21[var10 - var18] = var6[var11 - var4];
                  } else {
                     var21[var10 - var18] = var11;
                  }
               } else {
                  var21[var10 - var18] = var6[var10 - var4];
               }
            }

            this.permutationChange.from = var18;
            this.permutationChange.to = var17;
            this.permutationChange.perm = var21;
         }
      } else {
         this.permutationChange = new SubChange(var4, var5, (List)null, var6, false);
      }

      TreeSet var19;
      if (this.addRemoveChanges != null && !this.addRemoveChanges.isEmpty()) {
         var19 = new TreeSet();
         HashMap var20 = new HashMap();
         var9 = 0;

         for(var10 = this.addRemoveChanges.size(); var9 < var10; ++var9) {
            SubChange var29 = (SubChange)this.addRemoveChanges.get(var9);

            for(var12 = var29.from; var12 < var29.to; ++var12) {
               if (var12 >= var1 && var12 < var2) {
                  var19.add(var3[var12 - var1]);
               } else {
                  var19.add(var12);
               }
            }

            if (var29.removed != null) {
               if (var29.from >= var1 && var29.from < var2) {
                  var20.put(var3[var29.from - var1], var29.removed);
               } else {
                  var20.put(var29.from, var29.removed);
               }
            }
         }

         this.addRemoveChanges.clear();
         SubChange var23 = null;
         Iterator var24 = var19.iterator();

         while(var24.hasNext()) {
            Integer var31 = (Integer)var24.next();
            if (var23 != null && var23.to == var31) {
               var23.to = var31 + 1;
            } else {
               var23 = new SubChange(var31, var31 + 1, (List)null, EMPTY_PERM, false);
               this.addRemoveChanges.add(var23);
            }

            List var33 = (List)var20.remove(var31);
            if (var33 != null) {
               if (var23.removed != null) {
                  var23.removed.addAll(var33);
               } else {
                  var23.removed = var33;
               }
            }
         }

         var24 = var20.entrySet().iterator();

         while(var24.hasNext()) {
            Map.Entry var32 = (Map.Entry)var24.next();
            Integer var34 = (Integer)var32.getKey();
            var30 = this.findSubChange(var34, this.addRemoveChanges);

            assert var30 < 0;

            this.addRemoveChanges.add(~var30, new SubChange(var34, var34, (List)var32.getValue(), new int[0], false));
         }
      }

      if (this.updateChanges != null && !this.updateChanges.isEmpty()) {
         var19 = new TreeSet();
         var18 = 0;

         for(var9 = this.updateChanges.size(); var18 < var9; ++var18) {
            SubChange var27 = (SubChange)this.updateChanges.get(var18);

            for(var11 = var27.from; var11 < var27.to; ++var11) {
               if (var11 >= var1 && var11 < var2) {
                  var19.add(var3[var11 - var1]);
               } else {
                  var19.add(var11);
               }
            }
         }

         this.updateChanges.clear();
         SubChange var22 = null;
         Iterator var25 = var19.iterator();

         while(true) {
            while(var25.hasNext()) {
               Integer var28 = (Integer)var25.next();
               if (var22 != null && var22.to == var28) {
                  var22.to = var28 + 1;
               } else {
                  var22 = new SubChange(var28, var28 + 1, (List)null, EMPTY_PERM, true);
                  this.updateChanges.add(var22);
               }
            }

            return;
         }
      }
   }

   public void nextReplace(int var1, int var2, List var3) {
      this.nextRemove(var1, var3);
      this.nextAdd(var1, var2);
   }

   public void nextSet(int var1, Object var2) {
      this.nextRemove(var1, var2);
      this.nextAdd(var1, var1 + 1);
   }

   public void nextUpdate(int var1) {
      this.checkState();
      if (this.updateChanges == null) {
         this.updateChanges = new ArrayList();
      }

      SubChange var2 = this.updateChanges.isEmpty() ? null : (SubChange)this.updateChanges.get(this.updateChanges.size() - 1);
      if (var2 != null && var2.to == var1) {
         var2.to = var1 + 1;
      } else {
         this.insertUpdate(var1);
      }

   }

   private void commit() {
      boolean var1 = this.addRemoveChanges != null && !this.addRemoveChanges.isEmpty();
      boolean var2 = this.updateChanges != null && !this.updateChanges.isEmpty();
      if (this.changeLock == 0 && (var1 || var2 || this.permutationChange != null)) {
         int var3 = (this.updateChanges != null ? this.updateChanges.size() : 0) + (this.addRemoveChanges != null ? this.addRemoveChanges.size() : 0) + (this.permutationChange != null ? 1 : 0);
         if (var3 == 1) {
            if (var1) {
               this.list.fireChange(new SingleChange(finalizeSubChange((SubChange)this.addRemoveChanges.get(0)), this.list));
               this.addRemoveChanges.clear();
            } else if (var2) {
               this.list.fireChange(new SingleChange(finalizeSubChange((SubChange)this.updateChanges.get(0)), this.list));
               this.updateChanges.clear();
            } else {
               this.list.fireChange(new SingleChange(finalizeSubChange(this.permutationChange), this.list));
               this.permutationChange = null;
            }
         } else {
            int var4;
            if (var2) {
               var4 = this.compress(this.updateChanges);
               var3 -= var4;
            }

            if (var1) {
               var4 = this.compress(this.addRemoveChanges);
               var3 -= var4;
            }

            SubChange[] var9 = new SubChange[var3];
            int var5 = 0;
            if (this.permutationChange != null) {
               var9[var5++] = this.permutationChange;
            }

            int var6;
            int var7;
            SubChange var8;
            if (var1) {
               var6 = this.addRemoveChanges.size();

               for(var7 = 0; var7 < var6; ++var7) {
                  var8 = (SubChange)this.addRemoveChanges.get(var7);
                  if (var8 != null) {
                     var9[var5++] = var8;
                  }
               }
            }

            if (var2) {
               var6 = this.updateChanges.size();

               for(var7 = 0; var7 < var6; ++var7) {
                  var8 = (SubChange)this.updateChanges.get(var7);
                  if (var8 != null) {
                     var9[var5++] = var8;
                  }
               }
            }

            this.list.fireChange(new IterableChange(finalizeSubChangeArray(var9), this.list));
            if (this.addRemoveChanges != null) {
               this.addRemoveChanges.clear();
            }

            if (this.updateChanges != null) {
               this.updateChanges.clear();
            }

            this.permutationChange = null;
         }
      }

   }

   public void beginChange() {
      ++this.changeLock;
   }

   public void endChange() {
      if (this.changeLock <= 0) {
         throw new IllegalStateException("Called endChange before beginChange");
      } else {
         --this.changeLock;
         this.commit();
      }
   }

   private static SubChange[] finalizeSubChangeArray(SubChange[] var0) {
      SubChange[] var1 = var0;
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         SubChange var4 = var1[var3];
         finalizeSubChange(var4);
      }

      return var0;
   }

   private static SubChange finalizeSubChange(SubChange var0) {
      if (var0.perm == null) {
         var0.perm = EMPTY_PERM;
      }

      if (var0.removed == null) {
         var0.removed = Collections.emptyList();
      } else {
         var0.removed = Collections.unmodifiableList(var0.removed);
      }

      return var0;
   }

   private static class IterableChange extends ListChangeListener.Change {
      private SubChange[] changes;
      private int cursor;

      private IterableChange(SubChange[] var1, ObservableList var2) {
         super(var2);
         this.cursor = -1;
         this.changes = var1;
      }

      public boolean next() {
         if (this.cursor + 1 < this.changes.length) {
            ++this.cursor;
            return true;
         } else {
            return false;
         }
      }

      public void reset() {
         this.cursor = -1;
      }

      public int getFrom() {
         this.checkState();
         return this.changes[this.cursor].from;
      }

      public int getTo() {
         this.checkState();
         return this.changes[this.cursor].to;
      }

      public List getRemoved() {
         this.checkState();
         return this.changes[this.cursor].removed;
      }

      protected int[] getPermutation() {
         this.checkState();
         return this.changes[this.cursor].perm;
      }

      public boolean wasUpdated() {
         this.checkState();
         return this.changes[this.cursor].updated;
      }

      private void checkState() {
         if (this.cursor == -1) {
            throw new IllegalStateException("Invalid Change state: next() must be called before inspecting the Change.");
         }
      }

      public String toString() {
         int var1 = 0;
         StringBuilder var2 = new StringBuilder();
         var2.append("{ ");

         for(; var1 < this.changes.length; ++var1) {
            if (this.changes[var1].perm.length != 0) {
               var2.append(ChangeHelper.permChangeToString(this.changes[var1].perm));
            } else if (this.changes[var1].updated) {
               var2.append(ChangeHelper.updateChangeToString(this.changes[var1].from, this.changes[var1].to));
            } else {
               var2.append(ChangeHelper.addRemoveChangeToString(this.changes[var1].from, this.changes[var1].to, this.getList(), this.changes[var1].removed));
            }

            if (var1 != this.changes.length - 1) {
               var2.append(", ");
            }
         }

         var2.append(" }");
         return var2.toString();
      }

      // $FF: synthetic method
      IterableChange(SubChange[] var1, ObservableList var2, Object var3) {
         this(var1, var2);
      }
   }

   private static class SingleChange extends ListChangeListener.Change {
      private final SubChange change;
      private boolean onChange;

      public SingleChange(SubChange var1, ObservableListBase var2) {
         super(var2);
         this.change = var1;
      }

      public boolean next() {
         if (this.onChange) {
            return false;
         } else {
            this.onChange = true;
            return true;
         }
      }

      public void reset() {
         this.onChange = false;
      }

      public int getFrom() {
         this.checkState();
         return this.change.from;
      }

      public int getTo() {
         this.checkState();
         return this.change.to;
      }

      public List getRemoved() {
         this.checkState();
         return this.change.removed;
      }

      protected int[] getPermutation() {
         this.checkState();
         return this.change.perm;
      }

      public boolean wasUpdated() {
         this.checkState();
         return this.change.updated;
      }

      private void checkState() {
         if (!this.onChange) {
            throw new IllegalStateException("Invalid Change state: next() must be called before inspecting the Change.");
         }
      }

      public String toString() {
         String var1;
         if (this.change.perm.length != 0) {
            var1 = ChangeHelper.permChangeToString(this.change.perm);
         } else if (this.change.updated) {
            var1 = ChangeHelper.updateChangeToString(this.change.from, this.change.to);
         } else {
            var1 = ChangeHelper.addRemoveChangeToString(this.change.from, this.change.to, this.getList(), this.change.removed);
         }

         return "{ " + var1 + " }";
      }
   }

   private static class SubChange {
      int from;
      int to;
      List removed;
      int[] perm;
      boolean updated;

      public SubChange(int var1, int var2, List var3, int[] var4, boolean var5) {
         this.from = var1;
         this.to = var2;
         this.removed = var3;
         this.perm = var4;
         this.updated = var5;
      }
   }
}
