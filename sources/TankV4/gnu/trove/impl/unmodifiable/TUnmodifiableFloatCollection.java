package gnu.trove.impl.unmodifiable;

import gnu.trove.TFloatCollection;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.procedure.TFloatProcedure;
import java.io.Serializable;
import java.util.Collection;

public class TUnmodifiableFloatCollection implements TFloatCollection, Serializable {
   private static final long serialVersionUID = 1820017752578914078L;
   final TFloatCollection c;

   public TUnmodifiableFloatCollection(TFloatCollection var1) {
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

   public boolean contains(float var1) {
      return this.c.contains(var1);
   }

   public float[] toArray() {
      return this.c.toArray();
   }

   public float[] toArray(float[] var1) {
      return this.c.toArray(var1);
   }

   public String toString() {
      return this.c.toString();
   }

   public float getNoEntryValue() {
      return this.c.getNoEntryValue();
   }

   public boolean forEach(TFloatProcedure var1) {
      return this.c.forEach(var1);
   }

   public TFloatIterator iterator() {
      return new TFloatIterator(this) {
         TFloatIterator i;
         final TUnmodifiableFloatCollection this$0;

         {
            this.this$0 = var1;
            this.i = this.this$0.c.iterator();
         }

         public boolean hasNext() {
            return this.i.hasNext();
         }

         public float next() {
            return this.i.next();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public boolean add(float var1) {
      throw new UnsupportedOperationException();
   }

   public boolean remove(float var1) {
      throw new UnsupportedOperationException();
   }

   public boolean containsAll(Collection var1) {
      return this.c.containsAll(var1);
   }

   public boolean containsAll(TFloatCollection var1) {
      return this.c.containsAll(var1);
   }

   public boolean containsAll(float[] var1) {
      return this.c.containsAll(var1);
   }

   public boolean addAll(TFloatCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean addAll(float[] var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(TFloatCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean removeAll(float[] var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(Collection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(TFloatCollection var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainAll(float[] var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }
}
