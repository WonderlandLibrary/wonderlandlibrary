package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TFloatIntIterator;
import gnu.trove.procedure.TFloatIntProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;

public interface TFloatIntMap {
   float getNoEntryKey();

   int getNoEntryValue();

   int put(float var1, int var2);

   int putIfAbsent(float var1, int var2);

   void putAll(Map var1);

   void putAll(TFloatIntMap var1);

   int get(float var1);

   void clear();

   boolean isEmpty();

   int remove(float var1);

   int size();

   TFloatSet keySet();

   float[] keys();

   float[] keys(float[] var1);

   TIntCollection valueCollection();

   int[] values();

   int[] values(int[] var1);

   boolean containsValue(int var1);

   boolean containsKey(float var1);

   TFloatIntIterator iterator();

   boolean forEachKey(TFloatProcedure var1);

   boolean forEachValue(TIntProcedure var1);

   boolean forEachEntry(TFloatIntProcedure var1);

   void transformValues(TIntFunction var1);

   boolean retainEntries(TFloatIntProcedure var1);

   boolean increment(float var1);

   boolean adjustValue(float var1, int var2);

   int adjustOrPutValue(float var1, int var2, int var3);
}
