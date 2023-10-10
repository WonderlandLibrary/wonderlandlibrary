package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TIntFloatIterator;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TIntFloatProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;

public interface TIntFloatMap {
   int getNoEntryKey();

   float getNoEntryValue();

   float put(int var1, float var2);

   float putIfAbsent(int var1, float var2);

   void putAll(Map var1);

   void putAll(TIntFloatMap var1);

   float get(int var1);

   void clear();

   boolean isEmpty();

   float remove(int var1);

   int size();

   TIntSet keySet();

   int[] keys();

   int[] keys(int[] var1);

   TFloatCollection valueCollection();

   float[] values();

   float[] values(float[] var1);

   boolean containsValue(float var1);

   boolean containsKey(int var1);

   TIntFloatIterator iterator();

   boolean forEachKey(TIntProcedure var1);

   boolean forEachValue(TFloatProcedure var1);

   boolean forEachEntry(TIntFloatProcedure var1);

   void transformValues(TFloatFunction var1);

   boolean retainEntries(TIntFloatProcedure var1);

   boolean increment(int var1);

   boolean adjustValue(int var1, float var2);

   float adjustOrPutValue(int var1, float var2, float var3);
}
