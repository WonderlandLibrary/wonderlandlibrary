package gnu.trove.impl.sync;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TShortFloatIterator;
import gnu.trove.map.TShortFloatMap;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TShortFloatProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

public class TSynchronizedShortFloatMap implements TShortFloatMap, Serializable {
   private static final long serialVersionUID = 1978198479659022715L;
   private final TShortFloatMap m;
   final Object mutex;
   private transient TShortSet keySet = null;
   private transient TFloatCollection values = null;

   public TSynchronizedShortFloatMap(TShortFloatMap var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.m = var1;
         this.mutex = this;
      }
   }

   public TSynchronizedShortFloatMap(TShortFloatMap var1, Object var2) {
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

   public boolean containsKey(short var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.containsKey(var1);
   }

   public boolean containsValue(float var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.containsValue(var1);
   }

   public float get(short var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.get(var1);
   }

   public float put(short var1, float var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.put(var1, var2);
   }

   public float remove(short var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.remove(var1);
   }

   public void putAll(Map var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.putAll(var1);
   }

   public void putAll(TShortFloatMap var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.putAll(var1);
   }

   public void clear() {
      Object var1;
      synchronized(var1 = this.mutex){}
      this.m.clear();
   }

   public TShortSet keySet() {
      Object var1;
      synchronized(var1 = this.mutex){}
      if (this.keySet == null) {
         this.keySet = new TSynchronizedShortSet(this.m.keySet(), this.mutex);
      }

      return this.keySet;
   }

   public short[] keys() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.m.keys();
   }

   public short[] keys(short[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.keys(var1);
   }

   public TFloatCollection valueCollection() {
      Object var1;
      synchronized(var1 = this.mutex){}
      if (this.values == null) {
         this.values = new TSynchronizedFloatCollection(this.m.valueCollection(), this.mutex);
      }

      return this.values;
   }

   public float[] values() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.m.values();
   }

   public float[] values(float[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.values(var1);
   }

   public TShortFloatIterator iterator() {
      return this.m.iterator();
   }

   public short getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public float getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public float putIfAbsent(short var1, float var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.putIfAbsent(var1, var2);
   }

   public boolean forEachKey(TShortProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TFloatProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TShortFloatProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachEntry(var1);
   }

   public void transformValues(TFloatFunction var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.transformValues(var1);
   }

   public boolean retainEntries(TShortFloatProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.retainEntries(var1);
   }

   public boolean increment(short var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.increment(var1);
   }

   public boolean adjustValue(short var1, float var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.adjustValue(var1, var2);
   }

   public float adjustOrPutValue(short var1, float var2, float var3) {
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
