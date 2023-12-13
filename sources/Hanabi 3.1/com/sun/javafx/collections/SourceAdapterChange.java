package com.sun.javafx.collections;

import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class SourceAdapterChange extends ListChangeListener.Change {
   private final ListChangeListener.Change change;
   private int[] perm;

   public SourceAdapterChange(ObservableList var1, ListChangeListener.Change var2) {
      super(var1);
      this.change = var2;
   }

   public boolean next() {
      this.perm = null;
      return this.change.next();
   }

   public void reset() {
      this.change.reset();
   }

   public int getTo() {
      return this.change.getTo();
   }

   public List getRemoved() {
      return this.change.getRemoved();
   }

   public int getFrom() {
      return this.change.getFrom();
   }

   public boolean wasUpdated() {
      return this.change.wasUpdated();
   }

   protected int[] getPermutation() {
      if (this.perm == null) {
         if (this.change.wasPermutated()) {
            int var1 = this.change.getFrom();
            int var2 = this.change.getTo() - var1;
            this.perm = new int[var2];

            for(int var3 = 0; var3 < var2; ++var3) {
               this.perm[var3] = this.change.getPermutation(var1 + var3);
            }
         } else {
            this.perm = new int[0];
         }
      }

      return this.perm;
   }

   public String toString() {
      return this.change.toString();
   }
}
