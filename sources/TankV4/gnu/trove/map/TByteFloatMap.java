package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TByteFloatIterator;
import gnu.trove.procedure.TByteFloatProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;

public interface TByteFloatMap {
   byte getNoEntryKey();

   float getNoEntryValue();

   float put(byte var1, float var2);

   float putIfAbsent(byte var1, float var2);

   void putAll(Map var1);

   void putAll(TByteFloatMap var1);

   float get(byte var1);

   void clear();

   boolean isEmpty();

   float remove(byte var1);

   int size();

   TByteSet keySet();

   byte[] keys();

   byte[] keys(byte[] var1);

   TFloatCollection valueCollection();

   float[] values();

   float[] values(float[] var1);

   boolean containsValue(float var1);

   boolean containsKey(byte var1);

   TByteFloatIterator iterator();

   boolean forEachKey(TByteProcedure var1);

   boolean forEachValue(TFloatProcedure var1);

   boolean forEachEntry(TByteFloatProcedure var1);

   void transformValues(TFloatFunction var1);

   boolean retainEntries(TByteFloatProcedure var1);

   boolean increment(byte var1);

   boolean adjustValue(byte var1, float var2);

   float adjustOrPutValue(byte var1, float var2, float var3);
}
