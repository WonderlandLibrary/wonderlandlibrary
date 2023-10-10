package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TIntLongIterator;
import gnu.trove.procedure.TIntLongProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;

public interface TIntLongMap {
   int getNoEntryKey();

   long getNoEntryValue();

   long put(int var1, long var2);

   long putIfAbsent(int var1, long var2);

   void putAll(Map var1);

   void putAll(TIntLongMap var1);

   long get(int var1);

   void clear();

   boolean isEmpty();

   long remove(int var1);

   int size();

   TIntSet keySet();

   int[] keys();

   int[] keys(int[] var1);

   TLongCollection valueCollection();

   long[] values();

   long[] values(long[] var1);

   boolean containsValue(long var1);

   boolean containsKey(int var1);

   TIntLongIterator iterator();

   boolean forEachKey(TIntProcedure var1);

   boolean forEachValue(TLongProcedure var1);

   boolean forEachEntry(TIntLongProcedure var1);

   void transformValues(TLongFunction var1);

   boolean retainEntries(TIntLongProcedure var1);

   boolean increment(int var1);

   boolean adjustValue(int var1, long var2);

   long adjustOrPutValue(int var1, long var2, long var4);
}
