package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TFloatObjectIterator;
import gnu.trove.procedure.TFloatObjectProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Collection;
import java.util.Map;

public interface TFloatObjectMap {
   float getNoEntryKey();

   int size();

   boolean isEmpty();

   boolean containsKey(float var1);

   boolean containsValue(Object var1);

   Object get(float var1);

   Object put(float var1, Object var2);

   Object putIfAbsent(float var1, Object var2);

   Object remove(float var1);

   void putAll(Map var1);

   void putAll(TFloatObjectMap var1);

   void clear();

   TFloatSet keySet();

   float[] keys();

   float[] keys(float[] var1);

   Collection valueCollection();

   Object[] values();

   Object[] values(Object[] var1);

   TFloatObjectIterator iterator();

   boolean forEachKey(TFloatProcedure var1);

   boolean forEachValue(TObjectProcedure var1);

   boolean forEachEntry(TFloatObjectProcedure var1);

   void transformValues(TObjectFunction var1);

   boolean retainEntries(TFloatObjectProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
