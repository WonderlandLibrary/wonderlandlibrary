package com.sun.javafx.collections;

import java.util.Collections;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public abstract class NonIterableChange extends ListChangeListener.Change {
   private final int from;
   private final int to;
   private boolean invalid = true;
   private static final int[] EMPTY_PERM = new int[0];

   protected NonIterableChange(int var1, int var2, ObservableList var3) {
      super(var3);
      this.from = var1;
      this.to = var2;
   }

   public int getFrom() {
      this.checkState();
      return this.from;
   }

   public int getTo() {
      this.checkState();
      return this.to;
   }

   protected int[] getPermutation() {
      this.checkState();
      return EMPTY_PERM;
   }

   public boolean next() {
      if (this.invalid) {
         this.invalid = false;
         return true;
      } else {
         return false;
      }
   }

   public void reset() {
      this.invalid = true;
   }

   public void checkState() {
      if (this.invalid) {
         throw new IllegalStateException("Invalid Change state: next() must be called before inspecting the Change.");
      }
   }

   public String toString() {
      boolean var1 = this.invalid;
      this.invalid = false;
      String var2;
      if (this.wasPermutated()) {
         var2 = ChangeHelper.permChangeToString(this.getPermutation());
      } else if (this.wasUpdated()) {
         var2 = ChangeHelper.updateChangeToString(this.from, this.to);
      } else {
         var2 = ChangeHelper.addRemoveChangeToString(this.from, this.to, this.getList(), this.getRemoved());
      }

      this.invalid = var1;
      return "{ " + var2 + " }";
   }

   public static class SimpleUpdateChange extends NonIterableChange {
      public SimpleUpdateChange(int var1, ObservableList var2) {
         this(var1, var1 + 1, var2);
      }

      public SimpleUpdateChange(int var1, int var2, ObservableList var3) {
         super(var1, var2, var3);
      }

      public List getRemoved() {
         return Collections.emptyList();
      }

      public boolean wasUpdated() {
         return true;
      }
   }

   public static class SimplePermutationChange extends NonIterableChange {
      private final int[] permutation;

      public SimplePermutationChange(int var1, int var2, int[] var3, ObservableList var4) {
         super(var1, var2, var4);
         this.permutation = var3;
      }

      public List getRemoved() {
         this.checkState();
         return Collections.emptyList();
      }

      protected int[] getPermutation() {
         this.checkState();
         return this.permutation;
      }
   }

   public static class SimpleAddChange extends NonIterableChange {
      public SimpleAddChange(int var1, int var2, ObservableList var3) {
         super(var1, var2, var3);
      }

      public boolean wasRemoved() {
         this.checkState();
         return false;
      }

      public List getRemoved() {
         this.checkState();
         return Collections.emptyList();
      }
   }

   public static class SimpleRemovedChange extends NonIterableChange {
      private final List removed;

      public SimpleRemovedChange(int var1, int var2, Object var3, ObservableList var4) {
         super(var1, var2, var4);
         this.removed = Collections.singletonList(var3);
      }

      public boolean wasRemoved() {
         this.checkState();
         return true;
      }

      public List getRemoved() {
         this.checkState();
         return this.removed;
      }
   }

   public static class GenericAddRemoveChange extends NonIterableChange {
      private final List removed;

      public GenericAddRemoveChange(int var1, int var2, List var3, ObservableList var4) {
         super(var1, var2, var4);
         this.removed = var3;
      }

      public List getRemoved() {
         this.checkState();
         return this.removed;
      }
   }
}
