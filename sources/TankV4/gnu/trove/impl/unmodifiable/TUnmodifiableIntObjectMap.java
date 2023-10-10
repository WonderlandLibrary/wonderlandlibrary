package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.procedure.TIntObjectProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TIntSet;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class TUnmodifiableIntObjectMap implements TIntObjectMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TIntObjectMap m;
   private transient TIntSet keySet = null;
   private transient Collection values = null;

   public TUnmodifiableIntObjectMap(TIntObjectMap var1) {
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

   public boolean containsKey(int var1) {
      return this.m.containsKey(var1);
   }

   public boolean containsValue(Object var1) {
      return this.m.containsValue(var1);
   }

   public Object get(int var1) {
      return this.m.get(var1);
   }

   public Object put(int var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public Object remove(int var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TIntObjectMap var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(Map var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public TIntSet keySet() {
      if (this.keySet == null) {
         this.keySet = TCollections.unmodifiableSet(this.m.keySet());
      }

      return this.keySet;
   }

   public int[] keys() {
      return this.m.keys();
   }

   public int[] keys(int[] var1) {
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

   public int getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public boolean forEachKey(TIntProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TObjectProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TIntObjectProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TIntObjectIterator iterator() {
      return new TIntObjectIterator(this) {
         TIntObjectIterator iter;
         final TUnmodifiableIntObjectMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableIntObjectMap.access$000(this.this$0).iterator();
         }

         public int key() {
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

   public Object putIfAbsent(int var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TObjectFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TIntObjectProcedure var1) {
      throw new UnsupportedOperationException();
   }

   static TIntObjectMap access$000(TUnmodifiableIntObjectMap var0) {
      return var0.m;
   }
}
