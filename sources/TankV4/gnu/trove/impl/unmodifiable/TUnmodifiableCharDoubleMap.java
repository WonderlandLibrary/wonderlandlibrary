package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TCharDoubleIterator;
import gnu.trove.map.TCharDoubleMap;
import gnu.trove.procedure.TCharDoubleProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TCharSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableCharDoubleMap implements TCharDoubleMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TCharDoubleMap m;
   private transient TCharSet keySet = null;
   private transient TDoubleCollection values = null;

   public TUnmodifiableCharDoubleMap(TCharDoubleMap var1) {
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

   public boolean containsKey(char var1) {
      return this.m.containsKey(var1);
   }

   public boolean containsValue(double var1) {
      return this.m.containsValue(var1);
   }

   public double get(char var1) {
      return this.m.get(var1);
   }

   public double put(char var1, double var2) {
      throw new UnsupportedOperationException();
   }

   public double remove(char var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TCharDoubleMap var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(Map var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public TCharSet keySet() {
      if (this.keySet == null) {
         this.keySet = TCollections.unmodifiableSet(this.m.keySet());
      }

      return this.keySet;
   }

   public char[] keys() {
      return this.m.keys();
   }

   public char[] keys(char[] var1) {
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

   public char getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public double getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TCharProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TDoubleProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TCharDoubleProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TCharDoubleIterator iterator() {
      return new TCharDoubleIterator(this) {
         TCharDoubleIterator iter;
         final TUnmodifiableCharDoubleMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableCharDoubleMap.access$000(this.this$0).iterator();
         }

         public char key() {
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

   public double putIfAbsent(char var1, double var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TDoubleFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TCharDoubleProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(char var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(char var1, double var2) {
      throw new UnsupportedOperationException();
   }

   public double adjustOrPutValue(char var1, double var2, double var4) {
      throw new UnsupportedOperationException();
   }

   static TCharDoubleMap access$000(TUnmodifiableCharDoubleMap var0) {
      return var0.m;
   }
}
