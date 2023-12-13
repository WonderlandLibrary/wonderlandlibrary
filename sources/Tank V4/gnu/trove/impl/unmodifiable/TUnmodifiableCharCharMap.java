package gnu.trove.impl.unmodifiable;

import gnu.trove.TCharCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TCharCharIterator;
import gnu.trove.map.TCharCharMap;
import gnu.trove.procedure.TCharCharProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.set.TCharSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableCharCharMap implements TCharCharMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TCharCharMap m;
   private transient TCharSet keySet = null;
   private transient TCharCollection values = null;

   public TUnmodifiableCharCharMap(TCharCharMap var1) {
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

   public boolean containsValue(char var1) {
      return this.m.containsValue(var1);
   }

   public char get(char var1) {
      return this.m.get(var1);
   }

   public char put(char var1, char var2) {
      throw new UnsupportedOperationException();
   }

   public char remove(char var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TCharCharMap var1) {
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

   public char getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public char getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TCharProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TCharProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TCharCharProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TCharCharIterator iterator() {
      return new TCharCharIterator(this) {
         TCharCharIterator iter;
         final TUnmodifiableCharCharMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableCharCharMap.access$000(this.this$0).iterator();
         }

         public char key() {
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

   public char putIfAbsent(char var1, char var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TCharFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TCharCharProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(char var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(char var1, char var2) {
      throw new UnsupportedOperationException();
   }

   public char adjustOrPutValue(char var1, char var2, char var3) {
      throw new UnsupportedOperationException();
   }

   static TCharCharMap access$000(TUnmodifiableCharCharMap var0) {
      return var0.m;
   }
}
