package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TObjectLongIterator;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TObjectLongProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
import java.util.Set;

public interface TObjectLongMap {
   long getNoEntryValue();

   int size();

   boolean isEmpty();

   boolean containsKey(Object var1);

   boolean containsValue(long var1);

   long get(Object var1);

   long put(Object var1, long var2);

   long putIfAbsent(Object var1, long var2);

   long remove(Object var1);

   void putAll(Map var1);

   void putAll(TObjectLongMap var1);

   void clear();

   Set keySet();

   Object[] keys();

   Object[] keys(Object[] var1);

   TLongCollection valueCollection();

   long[] values();

   long[] values(long[] var1);

   TObjectLongIterator iterator();

   boolean increment(Object var1);

   boolean adjustValue(Object var1, long var2);

   long adjustOrPutValue(Object var1, long var2, long var4);

   boolean forEachKey(TObjectProcedure var1);

   boolean forEachValue(TLongProcedure var1);

   boolean forEachEntry(TObjectLongProcedure var1);

   void transformValues(TLongFunction var1);

   boolean retainEntries(TObjectLongProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
