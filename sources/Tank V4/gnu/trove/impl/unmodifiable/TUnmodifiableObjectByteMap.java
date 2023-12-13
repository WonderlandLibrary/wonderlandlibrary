package gnu.trove.impl.unmodifiable;

import gnu.trove.TByteCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TObjectByteIterator;
import gnu.trove.map.TObjectByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TObjectByteProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class TUnmodifiableObjectByteMap implements TObjectByteMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TObjectByteMap m;
   private transient Set keySet = null;
   private transient TByteCollection values = null;

   public TUnmodifiableObjectByteMap(TObjectByteMap var1) {
      if (var1 == null) {
         throw new NullPointerException();
      } else {
         this.m = var1;
      }
   }

   public int size() {
      return this.m.size();
   }

   public boolean isEmpty() {
      return this.m.isEmpty();
   }

   public boolean containsKey(Object var1) {
      return this.m.containsKey(var1);
   }

   public boolean containsValue(byte var1) {
      return this.m.containsValue(var1);
   }

   public byte get(Object var1) {
      return this.m.get(var1);
   }

   public byte put(Object var1, byte var2) {
      throw new UnsupportedOperationException();
   }

   public byte remove(Object var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TObjectByteMap var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(Map var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public Set keySet() {
      if (this.keySet == null) {
         this.keySet = Collections.unmodifiableSet(this.m.keySet());
      }

      return this.keySet;
   }

   public Object[] keys() {
      return this.m.keys();
   }

   public Object[] keys(Object[] var1) {
      return this.m.keys(var1);
   }

   public TByteCollection valueCollection() {
      if (this.values == null) {
         this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
      }

      return this.values;
   }

   public byte[] values() {
      return this.m.values();
   }

   public byte[] values(byte[] var1) {
      return this.m.values(var1);
   }

   public boolean equals(Object var1) {
      return var1 == this || this.m.equals(var1);
   }

   public int hashCode() {
      return this.m.hashCode();
   }

   public String toString() {
      return this.m.toString();
   }

   public byte getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TObjectProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TByteProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TObjectByteProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TObjectByteIterator iterator() {
      return new TObjectByteIterator(this) {
         TObjectByteIterator iter;
         final TUnmodifiableObjectByteMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableObjectByteMap.access$000(this.this$0).iterator();
         }

         public Object key() {
            return this.iter.key();
         }

         public byte value() {
            return this.iter.value();
         }

         public void advance() {
            this.iter.advance();
         }

         public boolean hasNext() {
            return this.iter.hasNext();
         }

         public byte setValue(byte var1) {
            throw new UnsupportedOperationException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public byte putIfAbsent(Object var1, byte var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TByteFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TObjectByteProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(Object var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(Object var1, byte var2) {
      throw new UnsupportedOperationException();
   }

   public byte adjustOrPutValue(Object var1, byte var2, byte var3) {
      throw new UnsupportedOperationException();
   }

   static TObjectByteMap access$000(TUnmodifiableObjectByteMap var0) {
      return var0.m;
   }
}
