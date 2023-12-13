package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TLongIntIterator;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TLongIntProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;

public interface TLongIntMap {
   long getNoEntryKey();

   int getNoEntryValue();

   int put(long var1, int var3);

   int putIfAbsent(long var1, int var3);

   void putAll(Map var1);

   void putAll(TLongIntMap var1);

   int get(long var1);

   void clear();

   boolean isEmpty();

   int remove(long var1);

   int size();

   TLongSet keySet();

   long[] keys();

   long[] keys(long[] var1);

   TIntCollection valueCollection();

   int[] values();

   int[] values(int[] var1);

   boolean containsValue(int var1);

   boolean containsKey(long var1);

   TLongIntIterator iterator();

   boolean forEachKey(TLongProcedure var1);

   boolean forEachValue(TIntProcedure var1);

   boolean forEachEntry(TLongIntProcedure var1);

   void transformValues(TIntFunction var1);

   boolean retainEntries(TLongIntProcedure var1);

   boolean increment(long var1);

   boolean adjustValue(long var1, int var3);

   int adjustOrPutValue(long var1, int var3, int var4);
}
