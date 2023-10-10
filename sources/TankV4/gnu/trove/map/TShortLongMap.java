package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TShortLongIterator;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TShortLongProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;

public interface TShortLongMap {
   short getNoEntryKey();

   long getNoEntryValue();

   long put(short var1, long var2);

   long putIfAbsent(short var1, long var2);

   void putAll(Map var1);

   void putAll(TShortLongMap var1);

   long get(short var1);

   void clear();

   boolean isEmpty();

   long remove(short var1);

   int size();

   TShortSet keySet();

   short[] keys();

   short[] keys(short[] var1);

   TLongCollection valueCollection();

   long[] values();

   long[] values(long[] var1);

   boolean containsValue(long var1);

   boolean containsKey(short var1);

   TShortLongIterator iterator();

   boolean forEachKey(TShortProcedure var1);

   boolean forEachValue(TLongProcedure var1);

   boolean forEachEntry(TShortLongProcedure var1);

   void transformValues(TLongFunction var1);

   boolean retainEntries(TShortLongProcedure var1);

   boolean increment(short var1);

   boolean adjustValue(short var1, long var2);

   long adjustOrPutValue(short var1, long var2, long var4);
}
