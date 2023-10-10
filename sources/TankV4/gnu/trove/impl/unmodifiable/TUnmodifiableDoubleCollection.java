package gnu.trove.impl.unmodifiable;

import gnu.trove.TDoubleCollection;
import gnu.trove.iterator.TDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import java.io.Serializable;
import java.util.Collection;

public class TUnmodifiableDoubleCollection implements TDoubleCollection, Serializable {
   private static final long serialVersionUID = 1820017752578914078L;
   final TDoubleCollection c;

   public TUnmodifiableDoubleCollection(TDoubleCollection var1) {
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

   public boolean contains(double var1) {
      return this.c.contains(var1);
   }

   public double[] toArray() {
      return this.c.toArray();
   }

   public double[] toArray(double[] var1) {
      return this.c.toArray(var1);
   }

   public String toString() {
      return this.c.toString();
   }

   public double getNoEntryValue() {
      return this.c.getNoEntryValue();
   }

   public boolean forEach(TDoubleProcedure var1) {
      return this.c.forEach(var1);
   }

   public TDoubleIterator iterator() {
      return new TDoubleIterator(this) {
         TDoubleIterator i;
         final TUnmodifiableDoubleCollection this$0;

         {
            this.this$0 = var1;
            this.i = this.this$0.c.iterator();
         }

         public boolean hasNext() {
            return this.i.hasNext();
         }

         public double next() {
            return this.i.next();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public boolean add(double var1) {
      throw new UnsupportedOperationException();
   }

   public boolean remove(double var1) {
      throw new UnsupportedOperationException();
   }

   public boolean containsAll(Collection var1) {
      return this.c.containsAll(var1);
   }

   public boolean containsAll(TDoubleCollection var1) {
      return this.c.containsAll(var1);
   }

   public boolean containsAll(double[] var1) {
      return this.c.containsAll(var1);
   }

   public boolean addAll(TDoubleCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(double[] var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(TDoubleCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(double[] var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(TDoubleCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(double[] var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }
}
