package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.procedure.TIntObjectProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TIntSet;
import java.util.Collection;
import java.util.Map;

public interface TIntObjectMap {
   int getNoEntryKey();

   int size();

   boolean isEmpty();

   boolean containsKey(int var1);

   boolean containsValue(Object var1);

   Object get(int var1);

   Object put(int var1, Object var2);

   Object putIfAbsent(int var1, Object var2);

   Object remove(int var1);

   void putAll(Map var1);

   void putAll(TIntObjectMap var1);

   void clear();

   TIntSet keySet();

   int[] keys();

   int[] keys(int[] var1);

   Collection valueCollection();

   Object[] values();

   Object[] values(Object[] var1);

   TIntObjectIterator iterator();

   boolean forEachKey(TIntProcedure var1);

   boolean forEachValue(TObjectProcedure var1);

   boolean forEachEntry(TIntObjectProcedure var1);

   void transformValues(TObjectFunction var1);

   boolean retainEntries(TIntObjectProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
