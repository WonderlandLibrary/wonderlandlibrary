package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TIntIntIterator;
import gnu.trove.procedure.TIntIntProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;

public interface TIntIntMap {
   int getNoEntryKey();

   int getNoEntryValue();

   int put(int var1, int var2);

   int putIfAbsent(int var1, int var2);

   void putAll(Map var1);

   void putAll(TIntIntMap var1);

   int get(int var1);

   void clear();

   boolean isEmpty();

   int remove(int var1);

   int size();

   TIntSet keySet();

   int[] keys();

   int[] keys(int[] var1);

   TIntCollection valueCollection();

   int[] values();

   int[] values(int[] var1);

   boolean containsValue(int var1);

   boolean containsKey(int var1);

   TIntIntIterator iterator();

   boolean forEachKey(TIntProcedure var1);

   boolean forEachValue(TIntProcedure var1);

   boolean forEachEntry(TIntIntProcedure var1);

   void transformValues(TIntFunction var1);

   boolean retainEntries(TIntIntProcedure var1);

   boolean increment(int var1);

   boolean adjustValue(int var1, int var2);

   int adjustOrPutValue(int var1, int var2, int var3);
}
