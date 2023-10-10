package io.netty.channel.nio;

import java.nio.channels.SelectionKey;
import java.util.AbstractSet;
import java.util.Iterator;

final class SelectedSelectionKeySet extends AbstractSet {
   private SelectionKey[] keysA = new SelectionKey[1024];
   private int keysASize;
   private SelectionKey[] keysB;
   private int keysBSize;
   private boolean isA = true;

   SelectedSelectionKeySet() {
      this.keysB = (SelectionKey[])this.keysA.clone();
   }

   public boolean add(SelectionKey var1) {
      if (var1 == null) {
         return false;
      } else {
         int var2;
         if (this.isA) {
            var2 = this.keysASize;
            this.keysA[var2++] = var1;
            this.keysASize = var2;
            if (var2 == this.keysA.length) {
               this.doubleCapacityA();
            }
         } else {
            var2 = this.keysBSize;
            this.keysB[var2++] = var1;
            this.keysBSize = var2;
            if (var2 == this.keysB.length) {
               this.doubleCapacityB();
            }
         }

         return true;
      }
   }

   private void doubleCapacityA() {
      SelectionKey[] var1 = new SelectionKey[this.keysA.length << 1];
      System.arraycopy(this.keysA, 0, var1, 0, this.keysASize);
      this.keysA = var1;
   }

   private void doubleCapacityB() {
      SelectionKey[] var1 = new SelectionKey[this.keysB.length << 1];
      System.arraycopy(this.keysB, 0, var1, 0, this.keysBSize);
      this.keysB = var1;
   }

   SelectionKey[] flip() {
      if (this.isA) {
         this.isA = false;
         this.keysA[this.keysASize] = null;
         this.keysBSize = 0;
         return this.keysA;
      } else {
         this.isA = true;
         this.keysB[this.keysBSize] = null;
         this.keysASize = 0;
         return this.keysB;
      }
   }

   public int size() {
      return this.isA ? this.keysASize : this.keysBSize;
   }

   public boolean remove(Object var1) {
      return false;
   }

   public boolean contains(Object var1) {
      return false;
   }

   public Iterator iterator() {
      throw new UnsupportedOperationException();
   }

   public boolean add(Object var1) {
      return this.add((SelectionKey)var1);
   }
}
