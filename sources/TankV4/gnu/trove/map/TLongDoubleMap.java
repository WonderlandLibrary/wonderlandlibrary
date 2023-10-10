package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TLongDoubleIterator;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TLongDoubleProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;

public interface TLongDoubleMap {
   long getNoEntryKey();

   double getNoEntryValue();

   double put(long var1, double var3);

   double putIfAbsent(long var1, double var3);

   void putAll(Map var1);

   void putAll(TLongDoubleMap var1);

   double get(long var1);

   void clear();

   boolean isEmpty();

   double remove(long var1);

   int size();

   TLongSet keySet();

   long[] keys();

   long[] keys(long[] var1);

   TDoubleCollection valueCollection();

   double[] values();

   double[] values(double[] var1);

   boolean containsValue(double var1);

   boolean containsKey(long var1);

   TLongDoubleIterator iterator();

   boolean forEachKey(TLongProcedure var1);

   boolean forEachValue(TDoubleProcedure var1);

   boolean forEachEntry(TLongDoubleProcedure var1);

   void transformValues(TDoubleFunction var1);

   boolean retainEntries(TLongDoubleProcedure var1);

   boolean increment(long var1);

   boolean adjustValue(long var1, double var3);

   double adjustOrPutValue(long var1, double var3, double var5);
}
