package gnu.trove.impl.sync;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TDoubleIntIterator;
import gnu.trove.map.TDoubleIntMap;
import gnu.trove.procedure.TDoubleIntProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

public class TSynchronizedDoubleIntMap implements TDoubleIntMap, Serializable {
   private static final long serialVersionUID = 1978198479659022715L;
   private final TDoubleIntMap m;
   final Object mutex;
   private transient TDoubleSet keySet = null;
   private transient TIntCollection values = null;

   public TSynchronizedDoubleIntMap(TDoubleIntMap var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.m = var1;
         this.mutex = this;
      }
   }

   public TSynchronizedDoubleIntMap(TDoubleIntMap var1, Object var2) {
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

   public boolean containsValue(int var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.containsValue(var1);
   }

   public int get(double var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.get(var1);
   }

   public int put(double var1, int var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.m.put(var1, var3);
   }

   public int remove(double var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.remove(var1);
   }

   public void putAll(Map var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.putAll(var1);
   }

   public void putAll(TDoubleIntMap var1) {
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

   public TIntCollection valueCollection() {
      Object var1;
      synchronized(var1 = this.mutex){}
      if (this.values == null) {
         this.values = new TSynchronizedIntCollection(this.m.valueCollection(), this.mutex);
      }

      return this.values;
   }

   public int[] values() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.m.values();
   }

   public int[] values(int[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.values(var1);
   }

   public TDoubleIntIterator iterator() {
      return this.m.iterator();
   }

   public double getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public int getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public int putIfAbsent(double var1, int var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.m.putIfAbsent(var1, var3);
   }

   public boolean forEachKey(TDoubleProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TIntProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TDoubleIntProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachEntry(var1);
   }

   public void transformValues(TIntFunction var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.transformValues(var1);
   }

   public boolean retainEntries(TDoubleIntProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.retainEntries(var1);
   }

   public boolean increment(double var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.increment(var1);
   }

   public boolean adjustValue(double var1, int var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.m.adjustValue(var1, var3);
   }

   public int adjustOrPutValue(double var1, int var3, int var4) {
      Object var5;
      synchronized(var5 = this.mutex){}
      return this.m.adjustOrPutValue(var1, var3, var4);
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
