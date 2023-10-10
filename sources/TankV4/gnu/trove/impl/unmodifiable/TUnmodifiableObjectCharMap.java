package gnu.trove.impl.unmodifiable;

import gnu.trove.TCharCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TObjectCharIterator;
import gnu.trove.map.TObjectCharMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TObjectCharProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class TUnmodifiableObjectCharMap implements TObjectCharMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TObjectCharMap m;
   private transient Set keySet = null;
   private transient TCharCollection values = null;

   public TUnmodifiableObjectCharMap(TObjectCharMap var1) {
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

   public boolean containsValue(char var1) {
      return this.m.containsValue(var1);
   }

   public char get(Object var1) {
      return this.m.get(var1);
   }

   public char put(Object var1, char var2) {
      throw new UnsupportedOperationException();
   }

   public char remove(Object var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TObjectCharMap var1) {
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

   public char getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TObjectProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TCharProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TObjectCharProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TObjectCharIterator iterator() {
      return new TObjectCharIterator(this) {
         TObjectCharIterator iter;
         final TUnmodifiableObjectCharMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableObjectCharMap.access$000(this.this$0).iterator();
         }

         public Object key() {
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

   public char putIfAbsent(Object var1, char var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TCharFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TObjectCharProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(Object var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(Object var1, char var2) {
      throw new UnsupportedOperationException();
   }

   public char adjustOrPutValue(Object var1, char var2, char var3) {
      throw new UnsupportedOperationException();
   }

   static TObjectCharMap access$000(TUnmodifiableObjectCharMap var0) {
      return var0.m;
   }
}
