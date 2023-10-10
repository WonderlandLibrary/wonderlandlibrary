package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TShortIntIterator;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.procedure.TShortIntProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;

public interface TShortIntMap {
   short getNoEntryKey();

   int getNoEntryValue();

   int put(short var1, int var2);

   int putIfAbsent(short var1, int var2);

   void putAll(Map var1);

   void putAll(TShortIntMap var1);

   int get(short var1);

   void clear();

   boolean isEmpty();

   int remove(short var1);

   int size();

   TShortSet keySet();

   short[] keys();

   short[] keys(short[] var1);

   TIntCollection valueCollection();

   int[] values();

   int[] values(int[] var1);

   boolean containsValue(int var1);

   boolean containsKey(short var1);

   TShortIntIterator iterator();

   boolean forEachKey(TShortProcedure var1);

   boolean forEachValue(TIntProcedure var1);

   boolean forEachEntry(TShortIntProcedure var1);

   void transformValues(TIntFunction var1);

   boolean retainEntries(TShortIntProcedure var1);

   boolean increment(short var1);

   boolean adjustValue(short var1, int var2);

   int adjustOrPutValue(short var1, int var2, int var3);
}
