package com.sun.javafx.collections;

import java.util.AbstractList;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public final class MappingChange extends ListChangeListener.Change {
   private final Map map;
   private final ListChangeListener.Change original;
   private List removed;
   public static final Map NOOP_MAP = new Map() {
      public Object map(Object var1) {
         return var1;
      }
   };

   public MappingChange(ListChangeListener.Change var1, Map var2, ObservableList var3) {
      super(var3);
      this.original = var1;
      this.map = var2;
   }

   public boolean next() {
      return this.original.next();
   }

   public void reset() {
      this.original.reset();
   }

   public int getFrom() {
      return this.original.getFrom();
   }

   public int getTo() {
      return this.original.getTo();
   }

   public List getRemoved() {
      if (this.removed == null) {
         this.removed = new AbstractList() {
            public Object get(int var1) {
               return MappingChange.this.map.map(MappingChange.this.original.getRemoved().get(var1));
            }

            public int size() {
               return MappingChange.this.original.getRemovedSize();
            }
         };
      }

      return this.removed;
   }

   protected int[] getPermutation() {
      return new int[0];
   }

   public boolean wasPermutated() {
      return this.original.wasPermutated();
   }

   public boolean wasUpdated() {
      return this.original.wasUpdated();
   }

   public int getPermutation(int var1) {
      return this.original.getPermutation(var1);
   }

   public String toString() {
      int var1;
      for(var1 = 0; this.next(); ++var1) {
      }

      int var2 = 0;
      this.reset();

      while(this.next()) {
         ++var2;
      }

      this.reset();
      StringBuilder var3 = new StringBuilder();
      var3.append("{ ");
      int var4 = 0;

      while(this.next()) {
         if (this.wasPermutated()) {
            var3.append(ChangeHelper.permChangeToString(this.getPermutation()));
         } else if (this.wasUpdated()) {
            var3.append(ChangeHelper.updateChangeToString(this.getFrom(), this.getTo()));
         } else {
            var3.append(ChangeHelper.addRemoveChangeToString(this.getFrom(), this.getTo(), this.getList(), this.getRemoved()));
         }

         if (var4 != var2) {
            var3.append(", ");
         }
      }

      var3.append(" }");
      this.reset();
      var4 = var2 - var1;

      while(var4-- > 0) {
         this.next();
      }

      return var3.toString();
   }

   public interface Map {
      Object map(Object var1);
   }
}
