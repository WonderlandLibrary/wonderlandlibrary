package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TLongByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TLongByteProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;

public interface TLongByteMap {
   long getNoEntryKey();

   byte getNoEntryValue();

   byte put(long var1, byte var3);

   byte putIfAbsent(long var1, byte var3);

   void putAll(Map var1);

   void putAll(TLongByteMap var1);

   byte get(long var1);

   void clear();

   boolean isEmpty();

   byte remove(long var1);

   int size();

   TLongSet keySet();

   long[] keys();

   long[] keys(long[] var1);

   TByteCollection valueCollection();

   byte[] values();

   byte[] values(byte[] var1);

   boolean containsValue(byte var1);

   boolean containsKey(long var1);

   TLongByteIterator iterator();

   boolean forEachKey(TLongProcedure var1);

   boolean forEachValue(TByteProcedure var1);

   boolean forEachEntry(TLongByteProcedure var1);

   void transformValues(TByteFunction var1);

   boolean retainEntries(TLongByteProcedure var1);

   boolean increment(long var1);

   boolean adjustValue(long var1, byte var3);

   byte adjustOrPutValue(long var1, byte var3, byte var4);
}
