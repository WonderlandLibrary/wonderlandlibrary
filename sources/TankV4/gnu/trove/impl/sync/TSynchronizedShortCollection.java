package gnu.trove.impl.sync;

import gnu.trove.TShortCollection;
import gnu.trove.iterator.TShortIterator;
import gnu.trove.procedure.TShortProcedure;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;

public class TSynchronizedShortCollection implements TShortCollection, Serializable {
   private static final long serialVersionUID = 3053995032091335093L;
   final TShortCollection c;
   final Object mutex;

   public TSynchronizedShortCollection(TShortCollection var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.c = var1;
         this.mutex = this;
      }
   }

   public TSynchronizedShortCollection(TShortCollection var1, Object var2) {
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

   public boolean contains(short var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.contains(var1);
   }

   public short[] toArray() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.c.toArray();
   }

   public short[] toArray(short[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.toArray(var1);
   }

   public TShortIterator iterator() {
      return this.c.iterator();
   }

   public boolean add(short var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.add(var1);
   }

   public boolean remove(short var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.remove(var1);
   }

   public boolean containsAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.containsAll(var1);
   }

   public boolean containsAll(TShortCollection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.containsAll(var1);
   }

   public boolean containsAll(short[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.containsAll(var1);
   }

   public boolean addAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.addAll(var1);
   }

   public boolean addAll(TShortCollection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.addAll(var1);
   }

   public boolean addAll(short[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.addAll(var1);
   }

   public boolean removeAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.removeAll(var1);
   }

   public boolean removeAll(TShortCollection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.removeAll(var1);
   }

   public boolean removeAll(short[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.removeAll(var1);
   }

   public boolean retainAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.retainAll(var1);
   }

   public boolean retainAll(TShortCollection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.retainAll(var1);
   }

   public boolean retainAll(short[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.retainAll(var1);
   }

   public short getNoEntryValue() {
      return this.c.getNoEntryValue();
   }

   public boolean forEach(TShortProcedure var1) {
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
