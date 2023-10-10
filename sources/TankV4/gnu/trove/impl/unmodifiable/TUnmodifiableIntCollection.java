package gnu.trove.impl.unmodifiable;

import gnu.trove.TIntCollection;
import gnu.trove.iterator.TIntIterator;
import gnu.trove.procedure.TIntProcedure;
import java.io.Serializable;
import java.util.Collection;

public class TUnmodifiableIntCollection implements TIntCollection, Serializable {
   private static final long serialVersionUID = 1820017752578914078L;
   final TIntCollection c;

   public TUnmodifiableIntCollection(TIntCollection var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.c = var1;
      }
   }

   public int size() {
      return this.c.size();
   }

   public boolean isEmpty() {
      return this.c.isEmpty();
   }

   public boolean contains(int var1) {
      return this.c.contains(var1);
   }

   public int[] toArray() {
      return this.c.toArray();
   }

   public int[] toArray(int[] var1) {
      return this.c.toArray(var1);
   }

   public String toString() {
      return this.c.toString();
   }

   public int getNoEntryValue() {
      return this.c.getNoEntryValue();
   }

   public boolean forEach(TIntProcedure var1) {
      return this.c.forEach(var1);
   }

   public TIntIterator iterator() {
      return new TIntIterator(this) {
         TIntIterator i;
         final TUnmodifiableIntCollection this$0;

         {
            this.this$0 = var1;
            this.i = this.this$0.c.iterator();
         }

         public boolean hasNext() {
            return this.i.hasNext();
         }

         public int next() {
            return this.i.next();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public boolean add(int var1) {
      throw new UnsupportedOperationException();
   }

   public boolean remove(int var1) {
      throw new UnsupportedOperationException();
   }

   public boolean containsAll(Collection var1) {
      return this.c.containsAll(var1);
   }

   public boolean containsAll(TIntCollection var1) {
      return this.c.containsAll(var1);
   }

   public boolean containsAll(int[] var1) {
      return this.c.containsAll(var1);
   }

   public boolean addAll(TIntCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(int[] var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(TIntCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(int[] var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(TIntCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(int[] var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }
}
