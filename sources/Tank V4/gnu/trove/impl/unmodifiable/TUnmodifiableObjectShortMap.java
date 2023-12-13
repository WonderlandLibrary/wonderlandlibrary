package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TObjectShortIterator;
import gnu.trove.map.TObjectShortMap;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.procedure.TObjectShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class TUnmodifiableObjectShortMap implements TObjectShortMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TObjectShortMap m;
   private transient Set keySet = null;
   private transient TShortCollection values = null;

   public TUnmodifiableObjectShortMap(TObjectShortMap var1) {
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

   public boolean containsValue(short var1) {
      return this.m.containsValue(var1);
   }

   public short get(Object var1) {
      return this.m.get(var1);
   }

   public short put(Object var1, short var2) {
      throw new UnsupportedOperationException();
   }

   public short remove(Object var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TObjectShortMap var1) {
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

   public TShortCollection valueCollection() {
      if (this.values == null) {
         this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
      }

      return this.values;
   }

   public short[] values() {
      return this.m.values();
   }

   public short[] values(short[] var1) {
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

   public short getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TObjectProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TShortProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TObjectShortProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TObjectShortIterator iterator() {
      return new TObjectShortIterator(this) {
         TObjectShortIterator iter;
         final TUnmodifiableObjectShortMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableObjectShortMap.access$000(this.this$0).iterator();
         }

         public Object key() {
            return this.iter.key();
         }

         public short value() {
            return this.iter.value();
         }

         public void advance() {
            this.iter.advance();
         }

         public boolean hasNext() {
            return this.iter.hasNext();
         }

         public short setValue(short var1) {
            throw new UnsupportedOperationException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public short putIfAbsent(Object var1, short var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TShortFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TObjectShortProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(Object var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(Object var1, short var2) {
      throw new UnsupportedOperationException();
   }

   public short adjustOrPutValue(Object var1, short var2, short var3) {
      throw new UnsupportedOperationException();
   }

   static TObjectShortMap access$000(TUnmodifiableObjectShortMap var0) {
      return var0.m;
   }
}
