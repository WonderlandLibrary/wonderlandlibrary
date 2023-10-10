package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TObjectShortIterator;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.procedure.TObjectShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import java.util.Map;
import java.util.Set;

public interface TObjectShortMap {
   short getNoEntryValue();

   int size();

   boolean isEmpty();

   boolean containsKey(Object var1);

   boolean containsValue(short var1);

   short get(Object var1);

   short put(Object var1, short var2);

   short putIfAbsent(Object var1, short var2);

   short remove(Object var1);

   void putAll(Map var1);

   void putAll(TObjectShortMap var1);

   void clear();

   Set keySet();

   Object[] keys();

   Object[] keys(Object[] var1);

   TShortCollection valueCollection();

   short[] values();

   short[] values(short[] var1);

   TObjectShortIterator iterator();

   boolean increment(Object var1);

   boolean adjustValue(Object var1, short var2);

   short adjustOrPutValue(Object var1, short var2, short var3);

   boolean forEachKey(TObjectProcedure var1);

   boolean forEachValue(TShortProcedure var1);

   boolean forEachEntry(TObjectShortProcedure var1);

   void transformValues(TShortFunction var1);

   boolean retainEntries(TObjectShortProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
