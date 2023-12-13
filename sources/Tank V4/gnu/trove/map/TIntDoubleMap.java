package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TIntDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TIntDoubleProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;

public interface TIntDoubleMap {
   int getNoEntryKey();

   double getNoEntryValue();

   double put(int var1, double var2);

   double putIfAbsent(int var1, double var2);

   void putAll(Map var1);

   void putAll(TIntDoubleMap var1);

   double get(int var1);

   void clear();

   boolean isEmpty();

   double remove(int var1);

   int size();

   TIntSet keySet();

   int[] keys();

   int[] keys(int[] var1);

   TDoubleCollection valueCollection();

   double[] values();

   double[] values(double[] var1);

   boolean containsValue(double var1);

   boolean containsKey(int var1);

   TIntDoubleIterator iterator();

   boolean forEachKey(TIntProcedure var1);

   boolean forEachValue(TDoubleProcedure var1);

   boolean forEachEntry(TIntDoubleProcedure var1);

   void transformValues(TDoubleFunction var1);

   boolean retainEntries(TIntDoubleProcedure var1);

   boolean increment(int var1);

   boolean adjustValue(int var1, double var2);

   double adjustOrPutValue(int var1, double var2, double var4);
}
