package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TByteLongIterator;
import gnu.trove.procedure.TByteLongProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;

public interface TByteLongMap {
   byte getNoEntryKey();

   long getNoEntryValue();

   long put(byte var1, long var2);

   long putIfAbsent(byte var1, long var2);

   void putAll(Map var1);

   void putAll(TByteLongMap var1);

   long get(byte var1);

   void clear();

   boolean isEmpty();

   long remove(byte var1);

   int size();

   TByteSet keySet();

   byte[] keys();

   byte[] keys(byte[] var1);

   TLongCollection valueCollection();

   long[] values();

   long[] values(long[] var1);

   boolean containsValue(long var1);

   boolean containsKey(byte var1);

   TByteLongIterator iterator();

   boolean forEachKey(TByteProcedure var1);

   boolean forEachValue(TLongProcedure var1);

   boolean forEachEntry(TByteLongProcedure var1);

   void transformValues(TLongFunction var1);

   boolean retainEntries(TByteLongProcedure var1);

   boolean increment(byte var1);

   boolean adjustValue(byte var1, long var2);

   long adjustOrPutValue(byte var1, long var2, long var4);
}
