package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TObjectIntIterator;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TObjectIntProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
import java.util.Set;

public interface TObjectIntMap {
   int getNoEntryValue();

   int size();

   boolean isEmpty();

   boolean containsKey(Object var1);

   boolean containsValue(int var1);

   int get(Object var1);

   int put(Object var1, int var2);

   int putIfAbsent(Object var1, int var2);

   int remove(Object var1);

   void putAll(Map var1);

   void putAll(TObjectIntMap var1);

   void clear();

   Set keySet();

   Object[] keys();

   Object[] keys(Object[] var1);

   TIntCollection valueCollection();

   int[] values();

   int[] values(int[] var1);

   TObjectIntIterator iterator();

   boolean increment(Object var1);

   boolean adjustValue(Object var1, int var2);

   int adjustOrPutValue(Object var1, int var2, int var3);

   boolean forEachKey(TObjectProcedure var1);

   boolean forEachValue(TIntProcedure var1);

   boolean forEachEntry(TObjectIntProcedure var1);

   void transformValues(TIntFunction var1);

   boolean retainEntries(TObjectIntProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
