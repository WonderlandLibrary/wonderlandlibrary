package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TLongShortIterator;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.procedure.TLongShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;

public interface TLongShortMap {
   long getNoEntryKey();

   short getNoEntryValue();

   short put(long var1, short var3);

   short putIfAbsent(long var1, short var3);

   void putAll(Map var1);

   void putAll(TLongShortMap var1);

   short get(long var1);

   void clear();

   boolean isEmpty();

   short remove(long var1);

   int size();

   TLongSet keySet();

   long[] keys();

   long[] keys(long[] var1);

   TShortCollection valueCollection();

   short[] values();

   short[] values(short[] var1);

   boolean containsValue(short var1);

   boolean containsKey(long var1);

   TLongShortIterator iterator();

   boolean forEachKey(TLongProcedure var1);

   boolean forEachValue(TShortProcedure var1);

   boolean forEachEntry(TLongShortProcedure var1);

   void transformValues(TShortFunction var1);

   boolean retainEntries(TLongShortProcedure var1);

   boolean increment(long var1);

   boolean adjustValue(long var1, short var3);

   short adjustOrPutValue(long var1, short var3, short var4);
}
