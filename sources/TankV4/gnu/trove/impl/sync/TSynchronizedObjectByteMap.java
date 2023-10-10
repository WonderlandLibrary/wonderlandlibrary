package gnu.trove.impl.sync;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TObjectByteIterator;
import gnu.trove.map.TObjectByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TObjectByteProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public class TSynchronizedObjectByteMap implements TObjectByteMap, Serializable {
   private static final long serialVersionUID = 1978198479659022715L;
   private final TObjectByteMap m;
   final Object mutex;
   private transient Set keySet = null;
   private transient TByteCollection values = null;

   public TSynchronizedObjectByteMap(TObjectByteMap var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.m = var1;
         this.mutex = this;
      }
   }

   public TSynchronizedObjectByteMap(TObjectByteMap var1, Object var2) {
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

   public boolean containsValue(byte var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.containsValue(var1);
   }

   public byte get(Object var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.get(var1);
   }

   public byte put(Object var1, byte var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.put(var1, var2);
   }

   public byte remove(Object var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.remove(var1);
   }

   public void putAll(Map var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.putAll(var1);
   }

   public void putAll(TObjectByteMap var1) {
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

   public TByteCollection valueCollection() {
      Object var1;
      synchronized(var1 = this.mutex){}
      if (this.values == null) {
         this.values = new TSynchronizedByteCollection(this.m.valueCollection(), this.mutex);
      }

      return this.values;
   }

   public byte[] values() {
      Object var1;
      synchronized(var1 = this.mutex){}
      return this.m.values();
   }

   public byte[] values(byte[] var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.values(var1);
   }

   public TObjectByteIterator iterator() {
      return this.m.iterator();
   }

   public byte getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public byte putIfAbsent(Object var1, byte var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.putIfAbsent(var1, var2);
   }

   public boolean forEachKey(TObjectProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TByteProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TObjectByteProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.forEachEntry(var1);
   }

   public void transformValues(TByteFunction var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      this.m.transformValues(var1);
   }

   public boolean retainEntries(TObjectByteProcedure var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.retainEntries(var1);
   }

   public boolean increment(Object var1) {
      Object var2;
      synchronized(var2 = this.mutex){}
      return this.m.increment(var1);
   }

   public boolean adjustValue(Object var1, byte var2) {
      Object var3;
      synchronized(var3 = this.mutex){}
      return this.m.adjustValue(var1, var2);
   }

   public byte adjustOrPutValue(Object var1, byte var2, byte var3) {
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
