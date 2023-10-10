package gnu.trove.impl.sync;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TIntShortIterator;
import gnu.trove.map.TIntShortMap;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TIntShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TIntSet;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

public class TSynchronizedIntShortMap implements TIntShortMap, Serializable {
   private static final long serialVersionUID = 1978198479659022715L;
   private final TIntShortMap m;
   final Object mutex;
   private transient TIntSet keySet = null;
   private transient TShortCollection values = null;

   public TSynchronizedIntShortMap(TIntShortMap var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.m = var1;
         this.mutex = this;
      }
   }

   public TSynchronizedIntShortMap(TIntShortMap var1, Object var2) {
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

   public boolean containsKey(int var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.containsKey(var1);
   }

   public boolean containsValue(short var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.containsValue(var1);
   }

   public short get(int var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.get(var1);
   }

   public short put(int var1, short var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.put(var1, var2);
   }

   public short remove(int var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.remove(var1);
   }

   public void putAll(Map var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.putAll(var1);
   }

   public void putAll(TIntShortMap var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.putAll(var1);
   }

   public void clear() {
      Object var1;
      synchronized(var1 = this.mutex){}
      this.m.clear();
   }

   public TIntSet keySet() {
      Object var1;
      synchronized(var1 = this.mutex){}
      if (this.keySet == null) {
         this.keySet = new TSynchronizedIntSet(this.m.keySet(), this.mutex);
      }

      return this.keySet;
   }

   public int[] keys() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.m.keys();
   }

   public int[] keys(int[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.keys(var1);
   }

   public TShortCollection valueCollection() {
      Object var1;
      synchronized(var1 = this.mutex){}
      if (this.values == null) {
         this.values = new TSynchronizedShortCollection(this.m.valueCollection(), this.mutex);
      }

      return this.values;
   }

   public short[] values() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.m.values();
   }

   public short[] values(short[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.values(var1);
   }

   public TIntShortIterator iterator() {
      return this.m.iterator();
   }

   public int getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public short getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public short putIfAbsent(int var1, short var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.putIfAbsent(var1, var2);
   }

   public boolean forEachKey(TIntProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TShortProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TIntShortProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachEntry(var1);
   }

   public void transformValues(TShortFunction var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.transformValues(var1);
   }

   public boolean retainEntries(TIntShortProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.retainEntries(var1);
   }

   public boolean increment(int var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.increment(var1);
   }

   public boolean adjustValue(int var1, short var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.adjustValue(var1, var2);
   }

   public short adjustOrPutValue(int var1, short var2, short var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.m.adjustOrPutValue(var1, var2, var3);
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
