package gnu.trove.impl.unmodifiable;

import gnu.trove.TByteCollection;
import gnu.trove.iterator.TByteIterator;
import gnu.trove.procedure.TByteProcedure;
import java.io.Serializable;
import java.util.Collection;

public class TUnmodifiableByteCollection implements TByteCollection, Serializable {
   private static final long serialVersionUID = 1820017752578914078L;
   final TByteCollection c;

   public TUnmodifiableByteCollection(TByteCollection var1) {
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

   public boolean contains(byte var1) {
      return this.c.contains(var1);
   }

   public byte[] toArray() {
      return this.c.toArray();
   }

   public byte[] toArray(byte[] var1) {
      return this.c.toArray(var1);
   }

   public String toString() {
      return this.c.toString();
   }

   public byte getNoEntryValue() {
      return this.c.getNoEntryValue();
   }

   public boolean forEach(TByteProcedure var1) {
      return this.c.forEach(var1);
   }

   public TByteIterator iterator() {
      return new TByteIterator(this) {
         TByteIterator i;
         final TUnmodifiableByteCollection this$0;

         {
            this.this$0 = var1;
            this.i = this.this$0.c.iterator();
         }

         public boolean hasNext() {
            return this.i.hasNext();
         }

         public byte next() {
            return this.i.next();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public boolean add(byte var1) {
      throw new UnsupportedOperationException();
   }

   public boolean remove(byte var1) {
      throw new UnsupportedOperationException();
   }

   public boolean containsAll(Collection var1) {
      return this.c.containsAll(var1);
   }

   public boolean containsAll(TByteCollection var1) {
      return this.c.containsAll(var1);
   }

   public boolean containsAll(byte[] var1) {
      return this.c.containsAll(var1);
   }

   public boolean addAll(TByteCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(byte[] var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(TByteCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(byte[] var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(TByteCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(byte[] var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }
}
