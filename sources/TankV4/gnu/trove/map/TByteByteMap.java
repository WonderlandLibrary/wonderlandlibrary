package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TByteByteIterator;
import gnu.trove.procedure.TByteByteProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;

public interface TByteByteMap {
   byte getNoEntryKey();

   byte getNoEntryValue();

   byte put(byte var1, byte var2);

   byte putIfAbsent(byte var1, byte var2);

   void putAll(Map var1);

   void putAll(TByteByteMap var1);

   byte get(byte var1);

   void clear();

   boolean isEmpty();

   byte remove(byte var1);

   int size();

   TByteSet keySet();

   byte[] keys();

   byte[] keys(byte[] var1);

   TByteCollection valueCollection();

   byte[] values();

   byte[] values(byte[] var1);

   boolean containsValue(byte var1);

   boolean containsKey(byte var1);

   TByteByteIterator iterator();

   boolean forEachKey(TByteProcedure var1);

   boolean forEachValue(TByteProcedure var1);

   boolean forEachEntry(TByteByteProcedure var1);

   void transformValues(TByteFunction var1);

   boolean retainEntries(TByteByteProcedure var1);

   boolean increment(byte var1);

   boolean adjustValue(byte var1, byte var2);

   byte adjustOrPutValue(byte var1, byte var2, byte var3);
}
