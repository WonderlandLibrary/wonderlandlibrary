package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TByteObjectIterator;
import gnu.trove.procedure.TByteObjectProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TByteSet;
import java.util.Collection;
import java.util.Map;

public interface TByteObjectMap {
   byte getNoEntryKey();

   int size();

   boolean isEmpty();

   boolean containsKey(byte var1);

   boolean containsValue(Object var1);

   Object get(byte var1);

   Object put(byte var1, Object var2);

   Object putIfAbsent(byte var1, Object var2);

   Object remove(byte var1);

   void putAll(Map var1);

   void putAll(TByteObjectMap var1);

   void clear();

   TByteSet keySet();

   byte[] keys();

   byte[] keys(byte[] var1);

   Collection valueCollection();

   Object[] values();

   Object[] values(Object[] var1);

   TByteObjectIterator iterator();

   boolean forEachKey(TByteProcedure var1);

   boolean forEachValue(TObjectProcedure var1);

   boolean forEachEntry(TByteObjectProcedure var1);

   void transformValues(TObjectFunction var1);

   boolean retainEntries(TByteObjectProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
