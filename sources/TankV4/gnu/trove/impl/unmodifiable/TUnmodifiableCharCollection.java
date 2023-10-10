package gnu.trove.impl.unmodifiable;

import gnu.trove.TCharCollection;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.procedure.TCharProcedure;
import java.io.Serializable;
import java.util.Collection;

public class TUnmodifiableCharCollection implements TCharCollection, Serializable {
   private static final long serialVersionUID = 1820017752578914078L;
   final TCharCollection c;

   public TUnmodifiableCharCollection(TCharCollection var1) {
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

   public boolean contains(char var1) {
      return this.c.contains(var1);
   }

   public char[] toArray() {
      return this.c.toArray();
   }

   public char[] toArray(char[] var1) {
      return this.c.toArray(var1);
   }

   public String toString() {
      return this.c.toString();
   }

   public char getNoEntryValue() {
      return this.c.getNoEntryValue();
   }

   public boolean forEach(TCharProcedure var1) {
      return this.c.forEach(var1);
   }

   public TCharIterator iterator() {
      return new TCharIterator(this) {
         TCharIterator i;
         final TUnmodifiableCharCollection this$0;

         {
            this.this$0 = var1;
            this.i = this.this$0.c.iterator();
         }

         public boolean hasNext() {
            return this.i.hasNext();
         }

         public char next() {
            return this.i.next();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public boolean add(char var1) {
      throw new UnsupportedOperationException();
   }

   public boolean remove(char var1) {
      throw new UnsupportedOperationException();
   }

   public boolean containsAll(Collection var1) {
      return this.c.containsAll(var1);
   }

   public boolean containsAll(TCharCollection var1) {
      return this.c.containsAll(var1);
   }

   public boolean containsAll(char[] var1) {
      return this.c.containsAll(var1);
   }

   public boolean addAll(TCharCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(char[] var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(TCharCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(char[] var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(TCharCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(char[] var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }
}
