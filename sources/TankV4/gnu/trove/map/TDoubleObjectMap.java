package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TDoubleObjectIterator;
import gnu.trove.procedure.TDoubleObjectProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Collection;
import java.util.Map;

public interface TDoubleObjectMap {
   double getNoEntryKey();

   int size();

   boolean isEmpty();

   boolean containsKey(double var1);

   boolean containsValue(Object var1);

   Object get(double var1);

   Object put(double var1, Object var3);

   Object putIfAbsent(double var1, Object var3);

   Object remove(double var1);

   void putAll(Map var1);

   void putAll(TDoubleObjectMap var1);

   void clear();

   TDoubleSet keySet();

   double[] keys();

   double[] keys(double[] var1);

   Collection valueCollection();

   Object[] values();

   Object[] values(Object[] var1);

   TDoubleObjectIterator iterator();

   boolean forEachKey(TDoubleProcedure var1);

   boolean forEachValue(TObjectProcedure var1);

   boolean forEachEntry(TDoubleObjectProcedure var1);

   void transformValues(TObjectFunction var1);

   boolean retainEntries(TDoubleObjectProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
