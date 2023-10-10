package gnu.trove.impl.unmodifiable;

import gnu.trove.TCharCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TShortCharIterator;
import gnu.trove.map.TShortCharMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TShortCharProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableShortCharMap implements TShortCharMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TShortCharMap m;
   private transient TShortSet keySet = null;
   private transient TCharCollection values = null;

   public TUnmodifiableShortCharMap(TShortCharMap var1) {
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

   public boolean containsKey(short var1) {
      return this.m.containsKey(var1);
   }

   public boolean containsValue(char var1) {
      return this.m.containsValue(var1);
   }

   public char get(short var1) {
      return this.m.get(var1);
   }

   public char put(short var1, char var2) {
      throw new UnsupportedOperationException();
   }

   public char remove(short var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TShortCharMap var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(Map var1) {
      throw new UnsupportedOperationException();
   }

   public void clear() {
      throw new UnsupportedOperationException();
   }

   public TShortSet keySet() {
      if (this.keySet == null) {
         this.keySet = TCollections.unmodifiableSet(this.m.keySet());
      }

      return this.keySet;
   }

   public short[] keys() {
      return this.m.keys();
   }

   public short[] keys(short[] var1) {
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

   public short getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public char getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TShortProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TCharProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TShortCharProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TShortCharIterator iterator() {
      return new TShortCharIterator(this) {
         TShortCharIterator iter;
         final TUnmodifiableShortCharMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableShortCharMap.access$000(this.this$0).iterator();
         }

         public short key() {
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

   public char putIfAbsent(short var1, char var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TCharFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TShortCharProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(short var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(short var1, char var2) {
      throw new UnsupportedOperationException();
   }

   public char adjustOrPutValue(short var1, char var2, char var3) {
      throw new UnsupportedOperationException();
   }

   static TShortCharMap access$000(TUnmodifiableShortCharMap var0) {
      return var0.m;
   }
}
