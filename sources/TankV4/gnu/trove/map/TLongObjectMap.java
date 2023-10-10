package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TLongObjectIterator;
import gnu.trove.procedure.TLongObjectProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TLongSet;
import java.util.Collection;
import java.util.Map;

public interface TLongObjectMap {
   long getNoEntryKey();

   int size();

   boolean isEmpty();

   boolean containsKey(long var1);

   boolean containsValue(Object var1);

   Object get(long var1);

   Object put(long var1, Object var3);

   Object putIfAbsent(long var1, Object var3);

   Object remove(long var1);

   void putAll(Map var1);

   void putAll(TLongObjectMap var1);

   void clear();

   TLongSet keySet();

   long[] keys();

   long[] keys(long[] var1);

   Collection valueCollection();

   Object[] values();

   Object[] values(Object[] var1);

   TLongObjectIterator iterator();

   boolean forEachKey(TLongProcedure var1);

   boolean forEachValue(TObjectProcedure var1);

   boolean forEachEntry(TLongObjectProcedure var1);

   void transformValues(TObjectFunction var1);

   boolean retainEntries(TLongObjectProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
