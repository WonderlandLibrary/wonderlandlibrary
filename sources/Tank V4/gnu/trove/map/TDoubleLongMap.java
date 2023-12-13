package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TDoubleLongIterator;
import gnu.trove.procedure.TDoubleLongProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;

public interface TDoubleLongMap {
   double getNoEntryKey();

   long getNoEntryValue();

   long put(double var1, long var3);

   long putIfAbsent(double var1, long var3);

   void putAll(Map var1);

   void putAll(TDoubleLongMap var1);

   long get(double var1);

   void clear();

   boolean isEmpty();

   long remove(double var1);

   int size();

   TDoubleSet keySet();

   double[] keys();

   double[] keys(double[] var1);

   TLongCollection valueCollection();

   long[] values();

   long[] values(long[] var1);

   boolean containsValue(long var1);

   boolean containsKey(double var1);

   TDoubleLongIterator iterator();

   boolean forEachKey(TDoubleProcedure var1);

   boolean forEachValue(TLongProcedure var1);

   boolean forEachEntry(TDoubleLongProcedure var1);

   void transformValues(TLongFunction var1);

   boolean retainEntries(TDoubleLongProcedure var1);

   boolean increment(double var1);

   boolean adjustValue(double var1, long var3);

   long adjustOrPutValue(double var1, long var3, long var5);
}
