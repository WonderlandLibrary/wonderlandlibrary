package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TFloatByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TFloatByteProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;

public interface TFloatByteMap {
   float getNoEntryKey();

   byte getNoEntryValue();

   byte put(float var1, byte var2);

   byte putIfAbsent(float var1, byte var2);

   void putAll(Map var1);

   void putAll(TFloatByteMap var1);

   byte get(float var1);

   void clear();

   boolean isEmpty();

   byte remove(float var1);

   int size();

   TFloatSet keySet();

   float[] keys();

   float[] keys(float[] var1);

   TByteCollection valueCollection();

   byte[] values();

   byte[] values(byte[] var1);

   boolean containsValue(byte var1);

   boolean containsKey(float var1);

   TFloatByteIterator iterator();

   boolean forEachKey(TFloatProcedure var1);

   boolean forEachValue(TByteProcedure var1);

   boolean forEachEntry(TFloatByteProcedure var1);

   void transformValues(TByteFunction var1);

   boolean retainEntries(TFloatByteProcedure var1);

   boolean increment(float var1);

   boolean adjustValue(float var1, byte var2);

   byte adjustOrPutValue(float var1, byte var2, byte var3);
}
