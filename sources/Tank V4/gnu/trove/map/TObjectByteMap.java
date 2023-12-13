package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TObjectByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TObjectByteProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
import java.util.Set;

public interface TObjectByteMap {
   byte getNoEntryValue();

   int size();

   boolean isEmpty();

   boolean containsKey(Object var1);

   boolean containsValue(byte var1);

   byte get(Object var1);

   byte put(Object var1, byte var2);

   byte putIfAbsent(Object var1, byte var2);

   byte remove(Object var1);

   void putAll(Map var1);

   void putAll(TObjectByteMap var1);

   void clear();

   Set keySet();

   Object[] keys();

   Object[] keys(Object[] var1);

   TByteCollection valueCollection();

   byte[] values();

   byte[] values(byte[] var1);

   TObjectByteIterator iterator();

   boolean increment(Object var1);

   boolean adjustValue(Object var1, byte var2);

   byte adjustOrPutValue(Object var1, byte var2, byte var3);

   boolean forEachKey(TObjectProcedure var1);

   boolean forEachValue(TByteProcedure var1);

   boolean forEachEntry(TObjectByteProcedure var1);

   void transformValues(TByteFunction var1);

   boolean retainEntries(TObjectByteProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
