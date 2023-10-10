package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TLongFloatIterator;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TLongFloatProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;

public interface TLongFloatMap {
   long getNoEntryKey();

   float getNoEntryValue();

   float put(long var1, float var3);

   float putIfAbsent(long var1, float var3);

   void putAll(Map var1);

   void putAll(TLongFloatMap var1);

   float get(long var1);

   void clear();

   boolean isEmpty();

   float remove(long var1);

   int size();

   TLongSet keySet();

   long[] keys();

   long[] keys(long[] var1);

   TFloatCollection valueCollection();

   float[] values();

   float[] values(float[] var1);

   boolean containsValue(float var1);

   boolean containsKey(long var1);

   TLongFloatIterator iterator();

   boolean forEachKey(TLongProcedure var1);

   boolean forEachValue(TFloatProcedure var1);

   boolean forEachEntry(TLongFloatProcedure var1);

   void transformValues(TFloatFunction var1);

   boolean retainEntries(TLongFloatProcedure var1);

   boolean increment(long var1);

   boolean adjustValue(long var1, float var3);

   float adjustOrPutValue(long var1, float var3, float var4);
}
