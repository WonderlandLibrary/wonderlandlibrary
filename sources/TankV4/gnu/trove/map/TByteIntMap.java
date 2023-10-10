package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TByteIntIterator;
import gnu.trove.procedure.TByteIntProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;

public interface TByteIntMap {
   byte getNoEntryKey();

   int getNoEntryValue();

   int put(byte var1, int var2);

   int putIfAbsent(byte var1, int var2);

   void putAll(Map var1);

   void putAll(TByteIntMap var1);

   int get(byte var1);

   void clear();

   boolean isEmpty();

   int remove(byte var1);

   int size();

   TByteSet keySet();

   byte[] keys();

   byte[] keys(byte[] var1);

   TIntCollection valueCollection();

   int[] values();

   int[] values(int[] var1);

   boolean containsValue(int var1);

   boolean containsKey(byte var1);

   TByteIntIterator iterator();

   boolean forEachKey(TByteProcedure var1);

   boolean forEachValue(TIntProcedure var1);

   boolean forEachEntry(TByteIntProcedure var1);

   void transformValues(TIntFunction var1);

   boolean retainEntries(TByteIntProcedure var1);

   boolean increment(byte var1);

   boolean adjustValue(byte var1, int var2);

   int adjustOrPutValue(byte var1, int var2, int var3);
}
