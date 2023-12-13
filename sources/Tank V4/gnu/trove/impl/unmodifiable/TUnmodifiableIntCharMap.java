package gnu.trove.impl.unmodifiable;

import gnu.trove.TCharCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TIntCharIterator;
import gnu.trove.map.TIntCharMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TIntCharProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableIntCharMap implements TIntCharMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TIntCharMap m;
   private transient TIntSet keySet = null;
   private transient TCharCollection values = null;

   public TUnmodifiableIntCharMap(TIntCharMap var1) {
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

   public boolean containsValue(char var1) {
      return this.m.containsValue(var1);
   }

   public char get(int var1) {
      return this.m.get(var1);
   }

   public char put(int var1, char var2) {
      throw new UnsupportedOperationException();
   }

   public char remove(int var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TIntCharMap var1) {
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

   public TCharCollection valueCollection() {
      if (this.values == null) {
         this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
      }

      return this.values;
   }

   public char[] values() {
      return this.m.values();
   }

   public char[] values(char[] var1) {
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

   public char getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TIntProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TCharProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TIntCharProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TIntCharIterator iterator() {
      return new TIntCharIterator(this) {
         TIntCharIterator iter;
         final TUnmodifiableIntCharMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableIntCharMap.access$000(this.this$0).iterator();
         }

         public int key() {
            return this.iter.key();
         }

         public char value() {
            return this.iter.value();
         }

         public void advance() {
            this.iter.advance();
         }

         public boolean hasNext() {
            return this.iter.hasNext();
         }

         public char setValue(char var1) {
            throw new UnsupportedOperationException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public char putIfAbsent(int var1, char var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TCharFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TIntCharProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(int var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(int var1, char var2) {
      throw new UnsupportedOperationException();
   }

   public char adjustOrPutValue(int var1, char var2, char var3) {
      throw new UnsupportedOperationException();
   }

   static TIntCharMap access$000(TUnmodifiableIntCharMap var0) {
      return var0.m;
   }
}
