package gnu.trove.impl.sync;

import gnu.trove.TFloatCollection;
import gnu.trove.iterator.TFloatIterator;
import gnu.trove.procedure.TFloatProcedure;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;

public class TSynchronizedFloatCollection implements TFloatCollection, Serializable {
   private static final long serialVersionUID = 3053995032091335093L;
   final TFloatCollection c;
   final Object mutex;

   public TSynchronizedFloatCollection(TFloatCollection var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.c = var1;
         this.mutex = this;
      }
   }

   public TSynchronizedFloatCollection(TFloatCollection var1, Object var2) {
      this.c = var1;
      this.mutex = var2;
   }

   public int size() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.c.size();
   }

   public boolean isEmpty() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.c.isEmpty();
   }

   public boolean contains(float var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.contains(var1);
   }

   public float[] toArray() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.c.toArray();
   }

   public float[] toArray(float[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.toArray(var1);
   }

   public TFloatIterator iterator() {
      return this.c.iterator();
   }

   public boolean add(float var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.add(var1);
   }

   public boolean remove(float var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.remove(var1);
   }

   public boolean containsAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.containsAll(var1);
   }

   public boolean containsAll(TFloatCollection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.containsAll(var1);
   }

   public boolean containsAll(float[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.containsAll(var1);
   }

   public boolean addAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.addAll(var1);
   }

   public boolean addAll(TFloatCollection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.addAll(var1);
   }

   public boolean addAll(float[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.addAll(var1);
   }

   public boolean removeAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.removeAll(var1);
   }

   public boolean removeAll(TFloatCollection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.removeAll(var1);
   }

   public boolean removeAll(float[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.removeAll(var1);
   }

   public boolean retainAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.retainAll(var1);
   }

   public boolean retainAll(TFloatCollection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.retainAll(var1);
   }

   public boolean retainAll(float[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.retainAll(var1);
   }

   public float getNoEntryValue() {
      return this.c.getNoEntryValue();
   }

   public boolean forEach(TFloatProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.forEach(var1);
   }

   public void clear() {
      Object var1;
      synchronized(var1 = this.mutex){}
      this.c.clear();
   }

   public String toString() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.c.toString();
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      Object var2;
      synchronized(var2 = this.mutex){}
      var1.defaultWriteObject();
   }
}
