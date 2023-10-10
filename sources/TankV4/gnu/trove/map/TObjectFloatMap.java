package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TObjectFloatIterator;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TObjectFloatProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
import java.util.Set;

public interface TObjectFloatMap {
   float getNoEntryValue();

   int size();

   boolean isEmpty();

   boolean containsKey(Object var1);

   boolean containsValue(float var1);

   float get(Object var1);

   float put(Object var1, float var2);

   float putIfAbsent(Object var1, float var2);

   float remove(Object var1);

   void putAll(Map var1);

   void putAll(TObjectFloatMap var1);

   void clear();

   Set keySet();

   Object[] keys();

   Object[] keys(Object[] var1);

   TFloatCollection valueCollection();

   float[] values();

   float[] values(float[] var1);

   TObjectFloatIterator iterator();

   boolean increment(Object var1);

   boolean adjustValue(Object var1, float var2);

   float adjustOrPutValue(Object var1, float var2, float var3);

   boolean forEachKey(TObjectProcedure var1);

   boolean forEachValue(TFloatProcedure var1);

   boolean forEachEntry(TObjectFloatProcedure var1);

   void transformValues(TFloatFunction var1);

   boolean retainEntries(TObjectFloatProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
