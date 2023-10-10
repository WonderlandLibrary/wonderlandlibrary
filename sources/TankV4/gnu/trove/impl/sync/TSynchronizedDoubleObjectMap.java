package gnu.trove.impl.sync;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TDoubleObjectIterator;
import gnu.trove.map.TDoubleObjectMap;
import gnu.trove.procedure.TDoubleObjectProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class TSynchronizedDoubleObjectMap implements TDoubleObjectMap, Serializable {
   private static final long serialVersionUID = 1978198479659022715L;
   private final TDoubleObjectMap m;
   final Object mutex;
   private transient TDoubleSet keySet = null;
   private transient Collection values = null;

   public TSynchronizedDoubleObjectMap(TDoubleObjectMap var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.m = var1;
         this.mutex = this;
      }
   }

   public TSynchronizedDoubleObjectMap(TDoubleObjectMap var1, Object var2) {
      this.m = var1;
      this.mutex = var2;
   }

   public int size() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.m.size();
   }

   public boolean isEmpty() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.m.isEmpty();
   }

   public boolean containsKey(double var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.containsKey(var1);
   }

   public boolean containsValue(Object var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.containsValue(var1);
   }

   public Object get(double var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.get(var1);
   }

   public Object put(double var1, Object var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.m.put(var1, var3);
   }

   public Object remove(double var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.remove(var1);
   }

   public void putAll(Map var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.putAll(var1);
   }

   public void putAll(TDoubleObjectMap var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.putAll(var1);
   }

   public void clear() {
      Object var1;
      synchronized(var1 = this.mutex){}
      this.m.clear();
   }

   public TDoubleSet keySet() {
      Object var1;
      synchronized(var1 = this.mutex){}
      if (this.keySet == null) {
         this.keySet = new TSynchronizedDoubleSet(this.m.keySet(), this.mutex);
      }

      return this.keySet;
   }

   public double[] keys() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.m.keys();
   }

   public double[] keys(double[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.keys(var1);
   }

   public Collection valueCollection() {
      Object var1;
      synchronized(var1 = this.mutex){}
      if (this.values == null) {
         this.values = new SynchronizedCollection(this.m.valueCollection(), this.mutex);
      }

      return this.values;
   }

   public Object[] values() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.m.values();
   }

   public Object[] values(Object[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.values(var1);
   }

   public TDoubleObjectIterator iterator() {
      return this.m.iterator();
   }

   public double getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public Object putIfAbsent(double var1, Object var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.m.putIfAbsent(var1, var3);
   }

   public boolean forEachKey(TDoubleProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TObjectProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TDoubleObjectProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachEntry(var1);
   }

   public void transformValues(TObjectFunction var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.transformValues(var1);
   }

   public boolean retainEntries(TDoubleObjectProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.retainEntries(var1);
   }

   public boolean equals(Object var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.equals(var1);
   }

   public int hashCode() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.m.hashCode();
   }

   public String toString() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.m.toString();
   }

   private void writeObject(ObjectOutputStream var1) throws IOException {
      Object var2;
      synchronized(var2 = this.mutex){}
      var1.defaultWriteObject();
   }
}
