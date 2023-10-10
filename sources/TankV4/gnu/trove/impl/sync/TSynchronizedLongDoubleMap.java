package gnu.trove.impl.sync;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TLongDoubleIterator;
import gnu.trove.map.TLongDoubleMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TLongDoubleProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

public class TSynchronizedLongDoubleMap implements TLongDoubleMap, Serializable {
   private static final long serialVersionUID = 1978198479659022715L;
   private final TLongDoubleMap m;
   final Object mutex;
   private transient TLongSet keySet = null;
   private transient TDoubleCollection values = null;

   public TSynchronizedLongDoubleMap(TLongDoubleMap var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.m = var1;
         this.mutex = this;
      }
   }

   public TSynchronizedLongDoubleMap(TLongDoubleMap var1, Object var2) {
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

   public boolean containsKey(long var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.containsKey(var1);
   }

   public boolean containsValue(double var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.containsValue(var1);
   }

   public double get(long var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.get(var1);
   }

   public double put(long var1, double var3) {
      Object var5;
      synchronized(var5 = this.mutex){}
      return this.m.put(var1, var3);
   }

   public double remove(long var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.remove(var1);
   }

   public void putAll(Map var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.putAll(var1);
   }

   public void putAll(TLongDoubleMap var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.putAll(var1);
   }

   public void clear() {
      Object var1;
      synchronized(var1 = this.mutex){}
      this.m.clear();
   }

   public TLongSet keySet() {
      Object var1;
      synchronized(var1 = this.mutex){}
      if (this.keySet == null) {
         this.keySet = new TSynchronizedLongSet(this.m.keySet(), this.mutex);
      }

      return this.keySet;
   }

   public long[] keys() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.m.keys();
   }

   public long[] keys(long[] var1) {
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

   public TLongDoubleIterator iterator() {
      return this.m.iterator();
   }

   public long getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public double getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public double putIfAbsent(long var1, double var3) {
      Object var5;
      synchronized(var5 = this.mutex){}
      return this.m.putIfAbsent(var1, var3);
   }

   public boolean forEachKey(TLongProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TDoubleProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TLongDoubleProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachEntry(var1);
   }

   public void transformValues(TDoubleFunction var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.transformValues(var1);
   }

   public boolean retainEntries(TLongDoubleProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.retainEntries(var1);
   }

   public boolean increment(long var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.increment(var1);
   }

   public boolean adjustValue(long var1, double var3) {
      Object var5;
      synchronized(var5 = this.mutex){}
      return this.m.adjustValue(var1, var3);
   }

   public double adjustOrPutValue(long var1, double var3, double var5) {
      Object var7;
      synchronized(var7 = this.mutex){}
      return this.m.adjustOrPutValue(var1, var3, var5);
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
