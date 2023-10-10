package gnu.trove.set.hash;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.iterator.hash.TObjectHashIterator;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.list.linked.TIntLinkedList;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.io.IOException;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class TLinkedHashSet extends THashSet {
   TIntList order;

   public TLinkedHashSet() {
   }

   public TLinkedHashSet(int var1) {
      super(var1);
   }

   public TLinkedHashSet(int var1, float var2) {
      super(var1, var2);
   }

   public TLinkedHashSet(Collection var1) {
      super(var1);
   }

   public int setUp(int var1) {
      this.order = new TIntArrayList(this, var1) {
         final TLinkedHashSet this$0;

         {
            this.this$0 = var1;
         }

         public void ensureCapacity(int var1) {
            if (var1 > this._data.length) {
               int var2 = Math.max(this.this$0._set.length, var1);
               int[] var3 = new int[var2];
               System.arraycopy(this._data, 0, var3, 0, this._data.length);
               this._data = var3;
            }

         }
      };
      return super.setUp(var1);
   }

   public void clear() {
      super.clear();
      this.order.clear();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder("{");
      boolean var2 = true;

      for(TObjectHashIterator var3 = this.iterator(); var3.hasNext(); var1.append(var3.next())) {
         if (var2) {
            var2 = false;
         } else {
            var1.append(", ");
         }
      }

      var1.append("}");
      return var1.toString();
   }

   public boolean add(Object var1) {
      int var2 = this.insertKey(var1);
      if (var2 < 0) {
         return false;
      } else if (!this.order.add(var2)) {
         throw new IllegalStateException("Order not changed after insert");
      } else {
         this.postInsertHook(this.consumeFreeSlot);
         return true;
      }
   }

   protected void removeAt(int var1) {
      this.order.remove(var1);
      super.removeAt(var1);
   }

   protected void rehash(int var1) {
      TIntLinkedList var2 = new TIntLinkedList(this.order);
      int var3 = this.size();
      Object[] var4 = this._set;
      this.order.clear();
      this._set = new Object[var1];
      Arrays.fill(this._set, FREE);
      TIntIterator var5 = var2.iterator();

      while(var5.hasNext()) {
         int var6 = var5.next();
         Object var7 = var4[var6];
         if (var7 == FREE || var7 == REMOVED) {
            throw new IllegalStateException("Iterating over empty location while rehashing");
         }

         if (var7 != FREE && var7 != REMOVED) {
            int var8 = this.insertKey(var7);
            if (var8 < 0) {
               this.throwObjectContractViolation(this._set[-var8 - 1], var7, this.size(), var3, var4);
            }

            if (!this.order.add(var8)) {
               throw new IllegalStateException("Order not changed after insert");
            }
         }
      }

   }

   protected void writeEntries(ObjectOutput var1) throws IOException {
      TLinkedHashSet.WriteProcedure var2 = new TLinkedHashSet.WriteProcedure(this, var1);
      if (!this.order.forEach(var2)) {
         throw var2.getIoException();
      }
   }

   public TObjectHashIterator iterator() {
      return new TObjectHashIterator(this, this) {
         TIntIterator localIterator;
         int lastIndex;
         final TLinkedHashSet this$0;

         {
            this.this$0 = var1;
            this.localIterator = this.this$0.order.iterator();
         }

         public Object next() {
            this.lastIndex = this.localIterator.next();
            return this.objectAtIndex(this.lastIndex);
         }

         public boolean hasNext() {
            return this.localIterator.hasNext();
         }

         public void remove() {
            this.localIterator.remove();
            this._hash.tempDisableAutoCompaction();
            this.this$0.removeAt(this.lastIndex);
            this._hash.reenableAutoCompaction(false);
         }
      };
   }

   public boolean forEach(TObjectProcedure var1) {
      TLinkedHashSet.ForEachProcedure var2 = new TLinkedHashSet.ForEachProcedure(this, this._set, var1);
      return this.order.forEach(var2);
   }

   public Iterator iterator() {
      return this.iterator();
   }

   class ForEachProcedure implements TIntProcedure {
      boolean changed;
      final Object[] set;
      final TObjectProcedure procedure;
      final TLinkedHashSet this$0;

      public ForEachProcedure(TLinkedHashSet var1, Object[] var2, TObjectProcedure var3) {
         this.this$0 = var1;
         this.changed = false;
         this.set = var2;
         this.procedure = var3;
      }

      public boolean execute(int var1) {
         return this.procedure.execute(this.set[var1]);
      }
   }

   class WriteProcedure implements TIntProcedure {
      final ObjectOutput output;
      IOException ioException;
      final TLinkedHashSet this$0;

      WriteProcedure(TLinkedHashSet var1, ObjectOutput var2) {
         this.this$0 = var1;
         this.output = var2;
      }

      public IOException getIoException() {
         return this.ioException;
      }

      public boolean execute(int var1) {
         try {
            this.output.writeObject(this.this$0._set[var1]);
            return true;
         } catch (IOException var3) {
            this.ioException = var3;
            return false;
         }
      }
   }
}
