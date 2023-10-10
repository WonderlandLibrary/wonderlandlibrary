package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TFloatFloatIterator;
import gnu.trove.procedure.TFloatFloatProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;

public interface TFloatFloatMap {
   float getNoEntryKey();

   float getNoEntryValue();

   float put(float var1, float var2);

   float putIfAbsent(float var1, float var2);

   void putAll(Map var1);

   void putAll(TFloatFloatMap var1);

   float get(float var1);

   void clear();

   boolean isEmpty();

   float remove(float var1);

   int size();

   TFloatSet keySet();

   float[] keys();

   float[] keys(float[] var1);

   TFloatCollection valueCollection();

   float[] values();

   float[] values(float[] var1);

   boolean containsValue(float var1);

   boolean containsKey(float var1);

   TFloatFloatIterator iterator();

   boolean forEachKey(TFloatProcedure var1);

   boolean forEachValue(TFloatProcedure var1);

   boolean forEachEntry(TFloatFloatProcedure var1);

   void transformValues(TFloatFunction var1);

   boolean retainEntries(TFloatFloatProcedure var1);

   boolean increment(float var1);

   boolean adjustValue(float var1, float var2);

   float adjustOrPutValue(float var1, float var2, float var3);
}
