package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TDoubleCharIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TDoubleCharProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;

public interface TDoubleCharMap {
   double getNoEntryKey();

   char getNoEntryValue();

   char put(double var1, char var3);

   char putIfAbsent(double var1, char var3);

   void putAll(Map var1);

   void putAll(TDoubleCharMap var1);

   char get(double var1);

   void clear();

   boolean isEmpty();

   char remove(double var1);

   int size();

   TDoubleSet keySet();

   double[] keys();

   double[] keys(double[] var1);

   TCharCollection valueCollection();

   char[] values();

   char[] values(char[] var1);

   boolean containsValue(char var1);

   boolean containsKey(double var1);

   TDoubleCharIterator iterator();

   boolean forEachKey(TDoubleProcedure var1);

   boolean forEachValue(TCharProcedure var1);

   boolean forEachEntry(TDoubleCharProcedure var1);

   void transformValues(TCharFunction var1);

   boolean retainEntries(TDoubleCharProcedure var1);

   boolean increment(double var1);

   boolean adjustValue(double var1, char var3);

   char adjustOrPutValue(double var1, char var3, char var4);
}
