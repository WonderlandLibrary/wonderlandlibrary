package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TCharShortIterator;
import gnu.trove.map.TCharShortMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TCharShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TCharSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableCharShortMap implements TCharShortMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TCharShortMap m;
   private transient TCharSet keySet = null;
   private transient TShortCollection values = null;

   public TUnmodifiableCharShortMap(TCharShortMap var1) {
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

   public boolean containsValue(short var1) {
      return this.m.containsValue(var1);
   }

   public short get(char var1) {
      return this.m.get(var1);
   }

   public short put(char var1, short var2) {
      throw new UnsupportedOperationException();
   }

   public short remove(char var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TCharShortMap var1) {
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

   public char getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public short getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TCharProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TShortProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TCharShortProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TCharShortIterator iterator() {
      return new TCharShortIterator(this) {
         TCharShortIterator iter;
         final TUnmodifiableCharShortMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableCharShortMap.access$000(this.this$0).iterator();
         }

         public char key() {
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

   public short putIfAbsent(char var1, short var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TShortFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TCharShortProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(char var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(char var1, short var2) {
      throw new UnsupportedOperationException();
   }

   public short adjustOrPutValue(char var1, short var2, short var3) {
      throw new UnsupportedOperationException();
   }

   static TCharShortMap access$000(TUnmodifiableCharShortMap var0) {
      return var0.m;
   }
}
