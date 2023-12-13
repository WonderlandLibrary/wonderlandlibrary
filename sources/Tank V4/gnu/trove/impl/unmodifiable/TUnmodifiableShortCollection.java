package gnu.trove.impl.unmodifiable;

import gnu.trove.TShortCollection;
import gnu.trove.iterator.TShortIterator;
import gnu.trove.procedure.TShortProcedure;
import java.io.Serializable;
import java.util.Collection;

public class TUnmodifiableShortCollection implements TShortCollection, Serializable {
   private static final long serialVersionUID = 1820017752578914078L;
   final TShortCollection c;

   public TUnmodifiableShortCollection(TShortCollection var1) {
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

   public boolean contains(short var1) {
      return this.c.contains(var1);
   }

   public short[] toArray() {
      return this.c.toArray();
   }

   public short[] toArray(short[] var1) {
      return this.c.toArray(var1);
   }

   public String toString() {
      return this.c.toString();
   }

   public short getNoEntryValue() {
      return this.c.getNoEntryValue();
   }

   public boolean forEach(TShortProcedure var1) {
      return this.c.forEach(var1);
   }

   public TShortIterator iterator() {
      return new TShortIterator(this) {
         TShortIterator i;
         final TUnmodifiableShortCollection this$0;

         {
            this.this$0 = var1;
            this.i = this.this$0.c.iterator();
         }

         public boolean hasNext() {
            return this.i.hasNext();
         }

         public short next() {
            return this.i.next();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public boolean add(short var1) {
      throw new UnsupportedOperationException();
   }

   public boolean remove(short var1) {
      throw new UnsupportedOperationException();
   }

   public boolean containsAll(Collection var1) {
      return this.c.containsAll(var1);
   }

   public boolean containsAll(TShortCollection var1) {
      return this.c.containsAll(var1);
   }

   public boolean containsAll(short[] var1) {
      return this.c.containsAll(var1);
   }

   public boolean addAll(TShortCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(short[] var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(TShortCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(short[] var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(TShortCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(short[] var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }
}
