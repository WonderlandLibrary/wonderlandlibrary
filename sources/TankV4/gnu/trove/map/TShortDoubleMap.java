package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TShortDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TShortDoubleProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;

public interface TShortDoubleMap {
   short getNoEntryKey();

   double getNoEntryValue();

   double put(short var1, double var2);

   double putIfAbsent(short var1, double var2);

   void putAll(Map var1);

   void putAll(TShortDoubleMap var1);

   double get(short var1);

   void clear();

   boolean isEmpty();

   double remove(short var1);

   int size();

   TShortSet keySet();

   short[] keys();

   short[] keys(short[] var1);

   TDoubleCollection valueCollection();

   double[] values();

   double[] values(double[] var1);

   boolean containsValue(double var1);

   boolean containsKey(short var1);

   TShortDoubleIterator iterator();

   boolean forEachKey(TShortProcedure var1);

   boolean forEachValue(TDoubleProcedure var1);

   boolean forEachEntry(TShortDoubleProcedure var1);

   void transformValues(TDoubleFunction var1);

   boolean retainEntries(TShortDoubleProcedure var1);

   boolean increment(short var1);

   boolean adjustValue(short var1, double var2);

   double adjustOrPutValue(short var1, double var2, double var4);
}
