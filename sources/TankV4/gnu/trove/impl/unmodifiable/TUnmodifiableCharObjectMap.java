package gnu.trove.impl.unmodifiable;

import gnu.trove.TCollections;
import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TCharObjectIterator;
import gnu.trove.map.TCharObjectMap;
import gnu.trove.procedure.TCharObjectProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TCharSet;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class TUnmodifiableCharObjectMap implements TCharObjectMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TCharObjectMap m;
   private transient TCharSet keySet = null;
   private transient Collection values = null;

   public TUnmodifiableCharObjectMap(TCharObjectMap var1) {
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

   public boolean containsValue(Object var1) {
      return this.m.containsValue(var1);
   }

   public Object get(char var1) {
      return this.m.get(var1);
   }

   public Object put(char var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public Object remove(char var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TCharObjectMap var1) {
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

   public char getNoEntryKey() {
      return this.m.getNoEntryKey();
   }

   public boolean forEachKey(TCharProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TObjectProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TCharObjectProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TCharObjectIterator iterator() {
      return new TCharObjectIterator(this) {
         TCharObjectIterator iter;
         final TUnmodifiableCharObjectMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableCharObjectMap.access$000(this.this$0).iterator();
         }

         public char key() {
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

   public Object putIfAbsent(char var1, Object var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TObjectFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TCharObjectProcedure var1) {
      throw new UnsupportedOperationException();
   }

   static TCharObjectMap access$000(TUnmodifiableCharObjectMap var0) {
      return var0.m;
   }
}
