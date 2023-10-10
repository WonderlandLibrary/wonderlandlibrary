package gnu.trove.impl.sync;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TLongIntIterator;
import gnu.trove.map.TLongIntMap;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TLongIntProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

public class TSynchronizedLongIntMap implements TLongIntMap, Serializable {
   private static final long serialVersionUID = 1978198479659022715L;
   private final TLongIntMap m;
   final Object mutex;
   private transient TLongSet keySet = null;
   private transient TIntCollection values = null;

   public TSynchronizedLongIntMap(TLongIntMap var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.m = var1;
         this.mutex = this;
      }
   }

   public TSynchronizedLongIntMap(TLongIntMap var1, Object var2) {
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

   public boolean containsValue(int var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.containsValue(var1);
   }

   public int get(long var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.get(var1);
   }

   public int put(long var1, int var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.m.put(var1, var3);
   }

   public int remove(long var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.remove(var1);
   }

   public void putAll(Map var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.putAll(var1);
   }

   public void putAll(TLongIntMap var1) {
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

   public TLongIntIterator iterator() {
      return this.m.iterator();
   }

   public long getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public int getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public int putIfAbsent(long var1, int var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.m.putIfAbsent(var1, var3);
   }

   public boolean forEachKey(TLongProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TIntProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TLongIntProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachEntry(var1);
   }

   public void transformValues(TIntFunction var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.transformValues(var1);
   }

   public boolean retainEntries(TLongIntProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.retainEntries(var1);
   }

   public boolean increment(long var1) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.increment(var1);
   }

   public boolean adjustValue(long var1, int var3) {
      Object var4;
      synchronized(var4 = this.mutex){}
      return this.m.adjustValue(var1, var3);
   }

   public int adjustOrPutValue(long var1, int var3, int var4) {
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
