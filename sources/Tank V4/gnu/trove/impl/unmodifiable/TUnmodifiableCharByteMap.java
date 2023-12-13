package gnu.trove.impl.unmodifiable;

import gnu.trove.TByteCollection;
import gnu.trove.TCollections;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TCharByteIterator;
import gnu.trove.map.TCharByteMap;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TCharByteProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.set.TCharSet;
import java.io.Serializable;
import java.util.Map;

public class TUnmodifiableCharByteMap implements TCharByteMap, Serializable {
   private static final long serialVersionUID = -1034234728574286014L;
   private final TCharByteMap m;
   private transient TCharSet keySet = null;
   private transient TByteCollection values = null;

   public TUnmodifiableCharByteMap(TCharByteMap var1) {
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

   public boolean containsValue(byte var1) {
      return this.m.containsValue(var1);
   }

   public byte get(char var1) {
      return this.m.get(var1);
   }

   public byte put(char var1, byte var2) {
      throw new UnsupportedOperationException();
   }

   public byte remove(char var1) {
      throw new UnsupportedOperationException();
   }

   public void putAll(TCharByteMap var1) {
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

   public TByteCollection valueCollection() {
      if (this.values == null) {
         this.values = TCollections.unmodifiableCollection(this.m.valueCollection());
      }

      return this.values;
   }

   public byte[] values() {
      return this.m.values();
   }

   public byte[] values(byte[] var1) {
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

   public byte getNoEntryValue() {
      return this.m.getNoEntryValue();
   }

   public boolean forEachKey(TCharProcedure var1) {
      return this.m.forEachKey(var1);
   }

   public boolean forEachValue(TByteProcedure var1) {
      return this.m.forEachValue(var1);
   }

   public boolean forEachEntry(TCharByteProcedure var1) {
      return this.m.forEachEntry(var1);
   }

   public TCharByteIterator iterator() {
      return new TCharByteIterator(this) {
         TCharByteIterator iter;
         final TUnmodifiableCharByteMap this$0;

         {
            this.this$0 = var1;
            this.iter = TUnmodifiableCharByteMap.access$000(this.this$0).iterator();
         }

         public char key() {
            return this.iter.key();
         }

         public byte value() {
            return this.iter.value();
         }

         public void advance() {
            this.iter.advance();
         }

         public boolean hasNext() {
            return this.iter.hasNext();
         }

         public byte setValue(byte var1) {
            throw new UnsupportedOperationException();
         }

         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   public byte putIfAbsent(char var1, byte var2) {
      throw new UnsupportedOperationException();
   }

   public void transformValues(TByteFunction var1) {
      throw new UnsupportedOperationException();
   }

   public boolean retainEntries(TCharByteProcedure var1) {
      throw new UnsupportedOperationException();
   }

   public boolean increment(char var1) {
      throw new UnsupportedOperationException();
   }

   public boolean adjustValue(char var1, byte var2) {
      throw new UnsupportedOperationException();
   }

   public byte adjustOrPutValue(char var1, byte var2, byte var3) {
      throw new UnsupportedOperationException();
   }

   static TCharByteMap access$000(TUnmodifiableCharByteMap var0) {
      return var0.m;
   }
}
