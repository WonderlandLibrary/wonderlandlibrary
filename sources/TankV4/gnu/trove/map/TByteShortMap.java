package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TByteShortIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TByteShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;

public interface TByteShortMap {
   byte getNoEntryKey();

   short getNoEntryValue();

   short put(byte var1, short var2);

   short putIfAbsent(byte var1, short var2);

   void putAll(Map var1);

   void putAll(TByteShortMap var1);

   short get(byte var1);

   void clear();

   boolean isEmpty();

   short remove(byte var1);

   int size();

   TByteSet keySet();

   byte[] keys();

   byte[] keys(byte[] var1);

   TShortCollection valueCollection();

   short[] values();

   short[] values(short[] var1);

   boolean containsValue(short var1);

   boolean containsKey(byte var1);

   TByteShortIterator iterator();

   boolean forEachKey(TByteProcedure var1);

   boolean forEachValue(TShortProcedure var1);

   boolean forEachEntry(TByteShortProcedure var1);

   void transformValues(TShortFunction var1);

   boolean retainEntries(TByteShortProcedure var1);

   boolean increment(byte var1);

   boolean adjustValue(byte var1, short var2);

   short adjustOrPutValue(byte var1, short var2, short var3);
}
