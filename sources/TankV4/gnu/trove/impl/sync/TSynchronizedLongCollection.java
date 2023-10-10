package gnu.trove.impl.sync;

import gnu.trove.TLongCollection;
import gnu.trove.iterator.TLongIterator;
import gnu.trove.procedure.TLongProcedure;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;

public class TSynchronizedLongCollection implements TLongCollection, Serializable {
   private static final long serialVersionUID = 3053995032091335093L;
   final TLongCollection c;
   final Object mutex;

   public TSynchronizedLongCollection(TLongCollection var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.c = var1;
         this.mutex = this;
      }
   }

   public TSynchronizedLongCollection(TLongCollection var1, Object var2) {
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

   public boolean contains(long var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.c.contains(var1);
   }

   public long[] toArray() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.c.toArray();
   }

   public long[] toArray(long[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.toArray(var1);
   }

   public TLongIterator iterator() {
      return this.c.iterator();
   }

   public boolean add(long var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.c.add(var1);
   }

   public boolean remove(long var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.c.remove(var1);
   }

   public boolean containsAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.containsAll(var1);
   }

   public boolean containsAll(TLongCollection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.containsAll(var1);
   }

   public boolean containsAll(long[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.containsAll(var1);
   }

   public boolean addAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.addAll(var1);
   }

   public boolean addAll(TLongCollection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.addAll(var1);
   }

   public boolean addAll(long[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.addAll(var1);
   }

   public boolean removeAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.removeAll(var1);
   }

   public boolean removeAll(TLongCollection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.removeAll(var1);
   }

   public boolean removeAll(long[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.removeAll(var1);
   }

   public boolean retainAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.retainAll(var1);
   }

   public boolean retainAll(TLongCollection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.retainAll(var1);
   }

   public boolean retainAll(long[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.retainAll(var1);
   }

   public long getNoEntryValue() {
      return this.c.getNoEntryValue();
   }

   public boolean forEach(TLongProcedure var1) {
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
