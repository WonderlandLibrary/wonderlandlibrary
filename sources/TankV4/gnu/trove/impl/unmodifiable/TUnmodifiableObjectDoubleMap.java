package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TObjectDoubleIterator;
import gnu.trove.map.TObjectDoubleMap;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TObjectDoubleProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class TUnmodifiableObjectDoubleMap implements TObjectDoubleMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TObjectDoubleMap m;
   private transient Set keySet = null;
   private transient TDoubleCollection values = null;

   public TUnmodifiableObjectDoubleMap(TObjectDoubleMap var1) {
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

   public boolean containsValue(double var1) {
      return this.m.containsValue(var1);
   }

   public double get(Object var1) {
      return this.m.get(var1);
   }

   public double put(Object var1, double var2) {
      throw new UnsupportedOperationException();
   }

   public double remove(Object var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TObjectDoubleMap var1) {
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

   public TDoubleCollection valueCollection() {
      if (this.values == null) {
         this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
      }

      return this.values;
   }

   public double[] values() {
      return this.m.values();
   }

   public double[] values(double[] var1) {
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

   public double getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TObjectProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TDoubleProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TObjectDoubleProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TObjectDoubleIterator iterator() {
      return new TObjectDoubleIterator(this) {
         TObjectDoubleIterator iter;
         final TUnmodifiableObjectDoubleMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableObjectDoubleMap.access$000(this.this$0).iterator();
         }

         public Object key() {
            return this.iter.key();
         }

         public double value() {
            return this.iter.value();
         }

         public void advance() {
            this.iter.advance();
         }

         public boolean hasNext() {
            return this.iter.hasNext();
         }

         public double setValue(double var1) {
            throw new UnsupportedOperationException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public double putIfAbsent(Object var1, double var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TDoubleFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TObjectDoubleProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(Object var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(Object var1, double var2) {
      throw new UnsupportedOperationException();
   }

   public double adjustOrPutValue(Object var1, double var2, double var4) {
      throw new UnsupportedOperationException();
   }

   static TObjectDoubleMap access$000(TUnmodifiableObjectDoubleMap var0) {
      return var0.m;
   }
}
