package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TDoubleDoubleIterator;
import gnu.trove.procedure.TDoubleDoubleProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;

public interface TDoubleDoubleMap {
   double getNoEntryKey();

   double getNoEntryValue();

   double put(double var1, double var3);

   double putIfAbsent(double var1, double var3);

   void putAll(Map var1);

   void putAll(TDoubleDoubleMap var1);

   double get(double var1);

   void clear();

   boolean isEmpty();

   double remove(double var1);

   int size();

   TDoubleSet keySet();

   double[] keys();

   double[] keys(double[] var1);

   TDoubleCollection valueCollection();

   double[] values();

   double[] values(double[] var1);

   boolean containsValue(double var1);

   boolean containsKey(double var1);

   TDoubleDoubleIterator iterator();

   boolean forEachKey(TDoubleProcedure var1);

   boolean forEachValue(TDoubleProcedure var1);

   boolean forEachEntry(TDoubleDoubleProcedure var1);

   void transformValues(TDoubleFunction var1);

   boolean retainEntries(TDoubleDoubleProcedure var1);

   boolean increment(double var1);

   boolean adjustValue(double var1, double var3);

   double adjustOrPutValue(double var1, double var3, double var5);
}
