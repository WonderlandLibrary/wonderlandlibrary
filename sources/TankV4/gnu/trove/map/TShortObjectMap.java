package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TShortObjectIterator;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.procedure.TShortObjectProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Collection;
import java.util.Map;

public interface TShortObjectMap {
   short getNoEntryKey();

   int size();

   boolean isEmpty();

   boolean containsKey(short var1);

   boolean containsValue(Object var1);

   Object get(short var1);

   Object put(short var1, Object var2);

   Object putIfAbsent(short var1, Object var2);

   Object remove(short var1);

   void putAll(Map var1);

   void putAll(TShortObjectMap var1);

   void clear();

   TShortSet keySet();

   short[] keys();

   short[] keys(short[] var1);

   Collection valueCollection();

   Object[] values();

   Object[] values(Object[] var1);

   TShortObjectIterator iterator();

   boolean forEachKey(TShortProcedure var1);

   boolean forEachValue(TObjectProcedure var1);

   boolean forEachEntry(TShortObjectProcedure var1);

   void transformValues(TObjectFunction var1);

   boolean retainEntries(TShortObjectProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
