package gnu.trove.impl.sync;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TObjectDoubleIterator;
import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TObjectDoubleProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class TSynchronizedObjectDoubleMap implements TObjectDoubleMap, Serializable {
   private static final long serialVersionUID = 1978198479659022715L;
   private final TObjectDoubleMap m;
   final Object mutex;
   private transient Set keySet = null;
   private transient TDoubleCollection values = null;

   public TSynchronizedObjectDoubleMap(TObjectDoubleMap var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.m = var1;
         this.mutex = this;
      }
   }

   public TSynchronizedObjectDoubleMap(TObjectDoubleMap var1, Object var2) {
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

   public boolean containsKey(Object var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.containsKey(var1);
   }

   public boolean containsValue(double var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.containsValue(var1);
   }

   public double get(Object var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.get(var1);
   }

   public double put(Object var1, double var2) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.m.put(var1, var2);
   }

   public double remove(Object var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.remove(var1);
   }

   public void putAll(Map var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.putAll(var1);
   }

   public void putAll(TObjectDoubleMap var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.putAll(var1);
   }

   public void clear() {
      Object var1;
      synchronized(var1 = this.mutex){}
      this.m.clear();
   }

   public Set keySet() {
      Object var1;
      synchronized(var1 = this.mutex){}
      if (this.keySet == null) {
         this.keySet = new SynchronizedSet(this.m.keySet(), this.mutex);
      }

      return this.keySet;
   }

   public Object[] keys() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.m.keys();
   }

   public Object[] keys(Object[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.keys(var1);
   }

   public TDoubleCollection valueCollection() {
      Object var1;
      synchronized(var1 = this.mutex){}
      if (this.values == null) {
         this.values = new TSynchronizedDoubleCollection(this.m.valueCollection(), this.mutex);
      }

      return this.values;
   }

   public double[] values() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.m.values();
   }

   public double[] values(double[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.values(var1);
   }

   public TObjectDoubleIterator iterator() {
      return this.m.iterator();
   }

   public double getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public double putIfAbsent(Object var1, double var2) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.m.putIfAbsent(var1, var2);
   }

   public boolean forEachKey(TObjectProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TDoubleProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TObjectDoubleProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachEntry(var1);
   }

   public void transformValues(TDoubleFunction var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.transformValues(var1);
   }

   public boolean retainEntries(TObjectDoubleProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.retainEntries(var1);
   }

   public boolean increment(Object var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.increment(var1);
   }

   public boolean adjustValue(Object var1, double var2) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.m.adjustValue(var1, var2);
   }

   public double adjustOrPutValue(Object var1, double var2, double var4) {
      Object var6;
      synchronized(var6 = this.mutex){}
      return this.m.adjustOrPutValue(var1, var2, var4);
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
