package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TObjectFloatIterator;
import gnu.trove.map.TObjectFloatMap;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TObjectFloatProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class TUnmodifiableObjectFloatMap implements TObjectFloatMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TObjectFloatMap m;
   private transient Set keySet = null;
   private transient TFloatCollection values = null;

   public TUnmodifiableObjectFloatMap(TObjectFloatMap var1) {
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

   public boolean containsValue(float var1) {
      return this.m.containsValue(var1);
   }

   public float get(Object var1) {
      return this.m.get(var1);
   }

   public float put(Object var1, float var2) {
      throw new UnsupportedOperationException();
   }

   public float remove(Object var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TObjectFloatMap var1) {
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

   public TFloatCollection valueCollection() {
      if (this.values == null) {
         this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
      }

      return this.values;
   }

   public float[] values() {
      return this.m.values();
   }

   public float[] values(float[] var1) {
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

   public float getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TObjectProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TFloatProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TObjectFloatProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TObjectFloatIterator iterator() {
      return new TObjectFloatIterator(this) {
         TObjectFloatIterator iter;
         final TUnmodifiableObjectFloatMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableObjectFloatMap.access$000(this.this$0).iterator();
         }

         public Object key() {
            return this.iter.key();
         }

         public float value() {
            return this.iter.value();
         }

         public void advance() {
            this.iter.advance();
         }

         public boolean hasNext() {
            return this.iter.hasNext();
         }

         public float setValue(float var1) {
            throw new UnsupportedOperationException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public float putIfAbsent(Object var1, float var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TFloatFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TObjectFloatProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(Object var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(Object var1, float var2) {
      throw new UnsupportedOperationException();
   }

   public float adjustOrPutValue(Object var1, float var2, float var3) {
      throw new UnsupportedOperationException();
   }

   static TObjectFloatMap access$000(TUnmodifiableObjectFloatMap var0) {
      return var0.m;
   }
}
