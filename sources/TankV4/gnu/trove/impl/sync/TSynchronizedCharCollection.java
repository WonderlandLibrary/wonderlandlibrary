package gnu.trove.impl.sync;

import gnu.trove.TCharCollection;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.procedure.TCharProcedure;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;

public class TSynchronizedCharCollection implements TCharCollection, Serializable {
   private static final long serialVersionUID = 3053995032091335093L;
   final TCharCollection c;
   final Object mutex;

   public TSynchronizedCharCollection(TCharCollection var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.c = var1;
         this.mutex = this;
      }
   }

   public TSynchronizedCharCollection(TCharCollection var1, Object var2) {
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

   public boolean contains(char var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.contains(var1);
   }

   public char[] toArray() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.c.toArray();
   }

   public char[] toArray(char[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.toArray(var1);
   }

   public TCharIterator iterator() {
      return this.c.iterator();
   }

   public boolean add(char var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.add(var1);
   }

   public boolean remove(char var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.remove(var1);
   }

   public boolean containsAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.containsAll(var1);
   }

   public boolean containsAll(TCharCollection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.containsAll(var1);
   }

   public boolean containsAll(char[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.containsAll(var1);
   }

   public boolean addAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.addAll(var1);
   }

   public boolean addAll(TCharCollection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.addAll(var1);
   }

   public boolean addAll(char[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.addAll(var1);
   }

   public boolean removeAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.removeAll(var1);
   }

   public boolean removeAll(TCharCollection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.removeAll(var1);
   }

   public boolean removeAll(char[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.removeAll(var1);
   }

   public boolean retainAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.retainAll(var1);
   }

   public boolean retainAll(TCharCollection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.retainAll(var1);
   }

   public boolean retainAll(char[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.retainAll(var1);
   }

   public char getNoEntryValue() {
      return this.c.getNoEntryValue();
   }

   public boolean forEach(TCharProcedure var1) {
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
