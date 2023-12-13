package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TLongLongIterator;
import gnu.trove.procedure.TLongLongProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;

public interface TLongLongMap {
   long getNoEntryKey();

   long getNoEntryValue();

   long put(long var1, long var3);

   long putIfAbsent(long var1, long var3);

   void putAll(Map var1);

   void putAll(TLongLongMap var1);

   long get(long var1);

   void clear();

   boolean isEmpty();

   long remove(long var1);

   int size();

   TLongSet keySet();

   long[] keys();

   long[] keys(long[] var1);

   TLongCollection valueCollection();

   long[] values();

   long[] values(long[] var1);

   boolean containsValue(long var1);

   boolean containsKey(long var1);

   TLongLongIterator iterator();

   boolean forEachKey(TLongProcedure var1);

   boolean forEachValue(TLongProcedure var1);

   boolean forEachEntry(TLongLongProcedure var1);

   void transformValues(TLongFunction var1);

   boolean retainEntries(TLongLongProcedure var1);

   boolean increment(long var1);

   boolean adjustValue(long var1, long var3);

   long adjustOrPutValue(long var1, long var3, long var5);
}
