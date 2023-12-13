package gnu.trove.impl.unmodifiable;

import gnu.trove.TLongCollection;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.procedure.TLongProcedure;
import java.io.Serializable;
import java.util.Collection;

public class TUnmodifiableLongCollection implements TLongCollection, Serializable {
   private static final long serialVersionUID = 1820017752578914078L;
   final TLongCollection c;

   public TUnmodifiableLongCollection(TLongCollection var1) {
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

   public boolean contains(long var1) {
      return this.c.contains(var1);
   }

   public long[] toArray() {
      return this.c.toArray();
   }

   public long[] toArray(long[] var1) {
      return this.c.toArray(var1);
   }

   public String toString() {
      return this.c.toString();
   }

   public long getNoEntryValue() {
      return this.c.getNoEntryValue();
   }

   public boolean forEach(TLongProcedure var1) {
      return this.c.forEach(var1);
   }

   public TLongIterator iterator() {
      return new TLongIterator(this) {
         TLongIterator i;
         final TUnmodifiableLongCollection this$0;

         {
            this.this$0 = var1;
            this.i = this.this$0.c.iterator();
         }

         public boolean hasNext() {
            return this.i.hasNext();
         }

         public long next() {
            return this.i.next();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public boolean add(long var1) {
      throw new UnsupportedOperationException();
   }

   public boolean remove(long var1) {
      throw new UnsupportedOperationException();
   }

   public boolean containsAll(Collection var1) {
      return this.c.containsAll(var1);
   }

   public boolean containsAll(TLongCollection var1) {
      return this.c.containsAll(var1);
   }

   public boolean containsAll(long[] var1) {
      return this.c.containsAll(var1);
   }

   public boolean addAll(TLongCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(long[] var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(TLongCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(long[] var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(TLongCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(long[] var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }
}
