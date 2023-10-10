package gnu.trove.impl.sync;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TFloatCharIterator;
import gnu.trove.map.TFloatCharMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TFloatCharProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

public class TSynchronizedFloatCharMap implements TFloatCharMap, Serializable {
   private static final long serialVersionUID = 1978198479659022715L;
   private final TFloatCharMap m;
   final Object mutex;
   private transient TFloatSet keySet = null;
   private transient TCharCollection values = null;

   public TSynchronizedFloatCharMap(TFloatCharMap var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.m = var1;
         this.mutex = this;
      }
   }

   public TSynchronizedFloatCharMap(TFloatCharMap var1, Object var2) {
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

   public boolean containsKey(float var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.containsKey(var1);
   }

   public boolean containsValue(char var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.containsValue(var1);
   }

   public char get(float var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.get(var1);
   }

   public char put(float var1, char var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.put(var1, var2);
   }

   public char remove(float var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.remove(var1);
   }

   public void putAll(Map var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.putAll(var1);
   }

   public void putAll(TFloatCharMap var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.putAll(var1);
   }

   public void clear() {
      Object var1;
      synchronized(var1 = this.mutex){}
      this.m.clear();
   }

   public TFloatSet keySet() {
      Object var1;
      synchronized(var1 = this.mutex){}
      if (this.keySet == null) {
         this.keySet = new TSynchronizedFloatSet(this.m.keySet(), this.mutex);
      }

      return this.keySet;
   }

   public float[] keys() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.m.keys();
   }

   public float[] keys(float[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.keys(var1);
   }

   public TCharCollection valueCollection() {
      Object var1;
      synchronized(var1 = this.mutex){}
      if (this.values == null) {
         this.values = new TSynchronizedCharCollection(this.m.valueCollection(), this.mutex);
      }

      return this.values;
   }

   public char[] values() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.m.values();
   }

   public char[] values(char[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.values(var1);
   }

   public TFloatCharIterator iterator() {
      return this.m.iterator();
   }

   public float getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public char getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public char putIfAbsent(float var1, char var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.putIfAbsent(var1, var2);
   }

   public boolean forEachKey(TFloatProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TCharProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TFloatCharProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachEntry(var1);
   }

   public void transformValues(TCharFunction var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.transformValues(var1);
   }

   public boolean retainEntries(TFloatCharProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.retainEntries(var1);
   }

   public boolean increment(float var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.increment(var1);
   }

   public boolean adjustValue(float var1, char var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.adjustValue(var1, var2);
   }

   public char adjustOrPutValue(float var1, char var2, char var3) {
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
