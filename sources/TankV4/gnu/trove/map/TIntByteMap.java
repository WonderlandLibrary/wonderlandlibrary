package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TIntByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TIntByteProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;

public interface TIntByteMap {
   int getNoEntryKey();

   byte getNoEntryValue();

   byte put(int var1, byte var2);

   byte putIfAbsent(int var1, byte var2);

   void putAll(Map var1);

   void putAll(TIntByteMap var1);

   byte get(int var1);

   void clear();

   boolean isEmpty();

   byte remove(int var1);

   int size();

   TIntSet keySet();

   int[] keys();

   int[] keys(int[] var1);

   TByteCollection valueCollection();

   byte[] values();

   byte[] values(byte[] var1);

   boolean containsValue(byte var1);

   boolean containsKey(int var1);

   TIntByteIterator iterator();

   boolean forEachKey(TIntProcedure var1);

   boolean forEachValue(TByteProcedure var1);

   boolean forEachEntry(TIntByteProcedure var1);

   void transformValues(TByteFunction var1);

   boolean retainEntries(TIntByteProcedure var1);

   boolean increment(int var1);

   boolean adjustValue(int var1, byte var2);

   byte adjustOrPutValue(int var1, byte var2, byte var3);
}
