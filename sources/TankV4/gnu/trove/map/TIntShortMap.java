package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TIntShortIterator;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TIntShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;

public interface TIntShortMap {
   int getNoEntryKey();

   short getNoEntryValue();

   short put(int var1, short var2);

   short putIfAbsent(int var1, short var2);

   void putAll(Map var1);

   void putAll(TIntShortMap var1);

   short get(int var1);

   void clear();

   boolean isEmpty();

   short remove(int var1);

   int size();

   TIntSet keySet();

   int[] keys();

   int[] keys(int[] var1);

   TShortCollection valueCollection();

   short[] values();

   short[] values(short[] var1);

   boolean containsValue(short var1);

   boolean containsKey(int var1);

   TIntShortIterator iterator();

   boolean forEachKey(TIntProcedure var1);

   boolean forEachValue(TShortProcedure var1);

   boolean forEachEntry(TIntShortProcedure var1);

   void transformValues(TShortFunction var1);

   boolean retainEntries(TIntShortProcedure var1);

   boolean increment(int var1);

   boolean adjustValue(int var1, short var2);

   short adjustOrPutValue(int var1, short var2, short var3);
}
