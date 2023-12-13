package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TDoubleFloatIterator;
import gnu.trove.procedure.TDoubleFloatProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;

public interface TDoubleFloatMap {
   double getNoEntryKey();

   float getNoEntryValue();

   float put(double var1, float var3);

   float putIfAbsent(double var1, float var3);

   void putAll(Map var1);

   void putAll(TDoubleFloatMap var1);

   float get(double var1);

   void clear();

   boolean isEmpty();

   float remove(double var1);

   int size();

   TDoubleSet keySet();

   double[] keys();

   double[] keys(double[] var1);

   TFloatCollection valueCollection();

   float[] values();

   float[] values(float[] var1);

   boolean containsValue(float var1);

   boolean containsKey(double var1);

   TDoubleFloatIterator iterator();

   boolean forEachKey(TDoubleProcedure var1);

   boolean forEachValue(TFloatProcedure var1);

   boolean forEachEntry(TDoubleFloatProcedure var1);

   void transformValues(TFloatFunction var1);

   boolean retainEntries(TDoubleFloatProcedure var1);

   boolean increment(double var1);

   boolean adjustValue(double var1, float var3);

   float adjustOrPutValue(double var1, float var3, float var4);
}
