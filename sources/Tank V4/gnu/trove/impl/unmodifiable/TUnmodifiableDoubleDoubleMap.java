package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TDoubleDoubleIterator;
import gnu.trove.map.TDoubleDoubleMap;
import gnu.trove.procedure.TDoubleDoubleProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TDoubleSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableDoubleDoubleMap implements TDoubleDoubleMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TDoubleDoubleMap m;
   private transient TDoubleSet keySet = null;
   private transient TDoubleCollection values = null;

   public TUnmodifiableDoubleDoubleMap(TDoubleDoubleMap var1) {
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

   public boolean containsValue(double var1) {
      return this.m.containsValue(var1);
   }

   public double get(double var1) {
      return this.m.get(var1);
   }

   public double put(double var1, double var3) {
      throw new UnsupportedOperationException();
   }

   public double remove(double var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TDoubleDoubleMap var1) {
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

   public double getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public double getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TDoubleProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TDoubleProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TDoubleDoubleProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TDoubleDoubleIterator iterator() {
      return new TDoubleDoubleIterator(this) {
         TDoubleDoubleIterator iter;
         final TUnmodifiableDoubleDoubleMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableDoubleDoubleMap.access$000(this.this$0).iterator();
         }

         public double key() {
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

   public double putIfAbsent(double var1, double var3) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TDoubleFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TDoubleDoubleProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(double var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(double var1, double var3) {
      throw new UnsupportedOperationException();
   }

   public double adjustOrPutValue(double var1, double var3, double var5) {
      throw new UnsupportedOperationException();
   }

   static TDoubleDoubleMap access$000(TUnmodifiableDoubleDoubleMap var0) {
      return var0.m;
   }
}
