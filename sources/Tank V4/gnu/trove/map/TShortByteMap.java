package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TShortByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TShortByteProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;

public interface TShortByteMap {
   short getNoEntryKey();

   byte getNoEntryValue();

   byte put(short var1, byte var2);

   byte putIfAbsent(short var1, byte var2);

   void putAll(Map var1);

   void putAll(TShortByteMap var1);

   byte get(short var1);

   void clear();

   boolean isEmpty();

   byte remove(short var1);

   int size();

   TShortSet keySet();

   short[] keys();

   short[] keys(short[] var1);

   TByteCollection valueCollection();

   byte[] values();

   byte[] values(byte[] var1);

   boolean containsValue(byte var1);

   boolean containsKey(short var1);

   TShortByteIterator iterator();

   boolean forEachKey(TShortProcedure var1);

   boolean forEachValue(TByteProcedure var1);

   boolean forEachEntry(TShortByteProcedure var1);

   void transformValues(TByteFunction var1);

   boolean retainEntries(TShortByteProcedure var1);

   boolean increment(short var1);

   boolean adjustValue(short var1, byte var2);

   byte adjustOrPutValue(short var1, byte var2, byte var3);
}
