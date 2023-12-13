package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TObjectDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TObjectDoubleProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
import java.util.Set;

public interface TObjectDoubleMap {
   double getNoEntryValue();

   int size();

   boolean isEmpty();

   boolean containsKey(Object var1);

   boolean containsValue(double var1);

   double get(Object var1);

   double put(Object var1, double var2);

   double putIfAbsent(Object var1, double var2);

   double remove(Object var1);

   void putAll(Map var1);

   void putAll(TObjectDoubleMap var1);

   void clear();

   Set keySet();

   Object[] keys();

   Object[] keys(Object[] var1);

   TDoubleCollection valueCollection();

   double[] values();

   double[] values(double[] var1);

   TObjectDoubleIterator iterator();

   boolean increment(Object var1);

   boolean adjustValue(Object var1, double var2);

   double adjustOrPutValue(Object var1, double var2, double var4);

   boolean forEachKey(TObjectProcedure var1);

   boolean forEachValue(TDoubleProcedure var1);

   boolean forEachEntry(TObjectDoubleProcedure var1);

   void transformValues(TDoubleFunction var1);

   boolean retainEntries(TObjectDoubleProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
