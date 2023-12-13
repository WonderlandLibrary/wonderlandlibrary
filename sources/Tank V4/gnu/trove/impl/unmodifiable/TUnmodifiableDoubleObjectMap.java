package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TDoubleObjectIterator;
import gnu.trove.map.TDoubleObjectMap;
import gnu.trove.procedure.TDoubleObjectProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class TUnmodifiableDoubleObjectMap implements TDoubleObjectMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TDoubleObjectMap m;
   private transient TDoubleSet keySet = null;
   private transient Collection values = null;

   public TUnmodifiableDoubleObjectMap(TDoubleObjectMap var1) {
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

   public boolean containsKey(double var1) {
      return this.m.containsKey(var1);
   }

   public boolean containsValue(Object var1) {
      return this.m.containsValue(var1);
   }

   public Object get(double var1) {
      return this.m.get(var1);
   }

   public Object put(double var1, Object var3) {
      throw new UnsupportedOperationException();
   }

   public Object remove(double var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TDoubleObjectMap var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(Map var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public TDoubleSet keySet() {
      if (this.keySet == null) {
         this.keySet = TCollections.unmodifiableSet(this.m.keySet());
      }

      return this.keySet;
   }

   public double[] keys() {
      return this.m.keys();
   }

   public double[] keys(double[] var1) {
      return this.m.keys(var1);
   }

   public Collection valueCollection() {
      if (this.values == null) {
         this.values = Collections.unmodifiableCollection(this.m.valueCollection());
      }

      return this.values;
   }

   public Object[] values() {
      return this.m.values();
   }

   public Object[] values(Object[] var1) {
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

   public double getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public boolean forEachKey(TDoubleProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TObjectProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TDoubleObjectProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TDoubleObjectIterator iterator() {
      return new TDoubleObjectIterator(this) {
         TDoubleObjectIterator iter;
         final TUnmodifiableDoubleObjectMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableDoubleObjectMap.access$000(this.this$0).iterator();
         }

         public double key() {
            return this.iter.key();
         }

         public Object value() {
            return this.iter.value();
         }

         public void advance() {
            this.iter.advance();
         }

         public boolean hasNext() {
            return this.iter.hasNext();
         }

         public Object setValue(Object var1) {
            throw new UnsupportedOperationException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public Object putIfAbsent(double var1, Object var3) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TObjectFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TDoubleObjectProcedure var1) {
      throw new UnsupportedOperationException();
   }

   static TDoubleObjectMap access$000(TUnmodifiableDoubleObjectMap var0) {
      return var0.m;
   }
}
