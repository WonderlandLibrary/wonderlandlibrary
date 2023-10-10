package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TCharDoubleIterator;
import gnu.trove.procedure.TCharDoubleProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;

public interface TCharDoubleMap {
   char getNoEntryKey();

   double getNoEntryValue();

   double put(char var1, double var2);

   double putIfAbsent(char var1, double var2);

   void putAll(Map var1);

   void putAll(TCharDoubleMap var1);

   double get(char var1);

   void clear();

   boolean isEmpty();

   double remove(char var1);

   int size();

   TCharSet keySet();

   char[] keys();

   char[] keys(char[] var1);

   TDoubleCollection valueCollection();

   double[] values();

   double[] values(double[] var1);

   boolean containsValue(double var1);

   boolean containsKey(char var1);

   TCharDoubleIterator iterator();

   boolean forEachKey(TCharProcedure var1);

   boolean forEachValue(TDoubleProcedure var1);

   boolean forEachEntry(TCharDoubleProcedure var1);

   void transformValues(TDoubleFunction var1);

   boolean retainEntries(TCharDoubleProcedure var1);

   boolean increment(char var1);

   boolean adjustValue(char var1, double var2);

   double adjustOrPutValue(char var1, double var2, double var4);
}
