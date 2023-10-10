package gnu.trove.impl.sync;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

class SynchronizedCollection implements Collection, Serializable {
   private static final long serialVersionUID = 3053995032091335093L;
   final Collection c;
   final Object mutex;

   SynchronizedCollection(Collection var1, Object var2) {
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

   public boolean contains(Object var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.contains(var1);
   }

   public Object[] toArray() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.c.toArray();
   }

   public Object[] toArray(Object[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.toArray(var1);
   }

   public Iterator iterator() {
      return this.c.iterator();
   }

   public boolean add(Object var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.add(var1);
   }

   public boolean remove(Object var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.remove(var1);
   }

   public boolean containsAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.containsAll(var1);
   }

   public boolean addAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.addAll(var1);
   }

   public boolean removeAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.removeAll(var1);
   }

   public boolean retainAll(Collection var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.c.retainAll(var1);
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
