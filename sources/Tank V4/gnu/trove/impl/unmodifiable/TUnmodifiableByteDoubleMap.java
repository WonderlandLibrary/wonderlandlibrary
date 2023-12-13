package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TByteDoubleIterator;
import gnu.trove.map.TByteDoubleMap;
import gnu.trove.procedure.TByteDoubleProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TByteSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableByteDoubleMap implements TByteDoubleMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TByteDoubleMap m;
   private transient TByteSet keySet = null;
   private transient TDoubleCollection values = null;

   public TUnmodifiableByteDoubleMap(TByteDoubleMap var1) {
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

   public boolean containsKey(byte var1) {
      return this.m.containsKey(var1);
   }

   public boolean containsValue(double var1) {
      return this.m.containsValue(var1);
   }

   public double get(byte var1) {
      return this.m.get(var1);
   }

   public double put(byte var1, double var2) {
      throw new UnsupportedOperationException();
   }

   public double remove(byte var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TByteDoubleMap var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(Map var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public TByteSet keySet() {
      if (this.keySet == null) {
         this.keySet = TCollections.unmodifiableSet(this.m.keySet());
      }

      return this.keySet;
   }

   public byte[] keys() {
      return this.m.keys();
   }

   public byte[] keys(byte[] var1) {
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

   public byte getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public double getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TByteProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TDoubleProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TByteDoubleProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TByteDoubleIterator iterator() {
      return new TByteDoubleIterator(this) {
         TByteDoubleIterator iter;
         final TUnmodifiableByteDoubleMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableByteDoubleMap.access$000(this.this$0).iterator();
         }

         public byte key() {
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

   public double putIfAbsent(byte var1, double var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TDoubleFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TByteDoubleProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(byte var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(byte var1, double var2) {
      throw new UnsupportedOperationException();
   }

   public double adjustOrPutValue(byte var1, double var2, double var4) {
      throw new UnsupportedOperationException();
   }

   static TByteDoubleMap access$000(TUnmodifiableByteDoubleMap var0) {
      return var0.m;
   }
}
