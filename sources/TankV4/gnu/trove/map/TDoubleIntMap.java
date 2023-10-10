package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TDoubleIntIterator;
import gnu.trove.procedure.TDoubleIntProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;

public interface TDoubleIntMap {
   double getNoEntryKey();

   int getNoEntryValue();

   int put(double var1, int var3);

   int putIfAbsent(double var1, int var3);

   void putAll(Map var1);

   void putAll(TDoubleIntMap var1);

   int get(double var1);

   void clear();

   boolean isEmpty();

   int remove(double var1);

   int size();

   TDoubleSet keySet();

   double[] keys();

   double[] keys(double[] var1);

   TIntCollection valueCollection();

   int[] values();

   int[] values(int[] var1);

   boolean containsValue(int var1);

   boolean containsKey(double var1);

   TDoubleIntIterator iterator();

   boolean forEachKey(TDoubleProcedure var1);

   boolean forEachValue(TIntProcedure var1);

   boolean forEachEntry(TDoubleIntProcedure var1);

   void transformValues(TIntFunction var1);

   boolean retainEntries(TDoubleIntProcedure var1);

   boolean increment(double var1);

   boolean adjustValue(double var1, int var3);

   int adjustOrPutValue(double var1, int var3, int var4);
}
