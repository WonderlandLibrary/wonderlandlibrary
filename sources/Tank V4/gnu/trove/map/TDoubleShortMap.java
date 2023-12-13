package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.procedure.TDoubleShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;

public interface TDoubleShortMap {
   double getNoEntryKey();

   short getNoEntryValue();

   short put(double var1, short var3);

   short putIfAbsent(double var1, short var3);

   void putAll(Map var1);

   void putAll(TDoubleShortMap var1);

   short get(double var1);

   void clear();

   boolean isEmpty();

   short remove(double var1);

   int size();

   TDoubleSet keySet();

   double[] keys();

   double[] keys(double[] var1);

   TShortCollection valueCollection();

   short[] values();

   short[] values(short[] var1);

   boolean containsValue(short var1);

   boolean containsKey(double var1);

   TDoubleShortIterator iterator();

   boolean forEachKey(TDoubleProcedure var1);

   boolean forEachValue(TShortProcedure var1);

   boolean forEachEntry(TDoubleShortProcedure var1);

   void transformValues(TShortFunction var1);

   boolean retainEntries(TDoubleShortProcedure var1);

   boolean increment(double var1);

   boolean adjustValue(double var1, short var3);

   short adjustOrPutValue(double var1, short var3, short var4);
}
