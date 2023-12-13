package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TFloatDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TFloatDoubleProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;

public interface TFloatDoubleMap {
   float getNoEntryKey();

   double getNoEntryValue();

   double put(float var1, double var2);

   double putIfAbsent(float var1, double var2);

   void putAll(Map var1);

   void putAll(TFloatDoubleMap var1);

   double get(float var1);

   void clear();

   boolean isEmpty();

   double remove(float var1);

   int size();

   TFloatSet keySet();

   float[] keys();

   float[] keys(float[] var1);

   TDoubleCollection valueCollection();

   double[] values();

   double[] values(double[] var1);

   boolean containsValue(double var1);

   boolean containsKey(float var1);

   TFloatDoubleIterator iterator();

   boolean forEachKey(TFloatProcedure var1);

   boolean forEachValue(TDoubleProcedure var1);

   boolean forEachEntry(TFloatDoubleProcedure var1);

   void transformValues(TDoubleFunction var1);

   boolean retainEntries(TFloatDoubleProcedure var1);

   boolean increment(float var1);

   boolean adjustValue(float var1, double var2);

   double adjustOrPutValue(float var1, double var2, double var4);
}
