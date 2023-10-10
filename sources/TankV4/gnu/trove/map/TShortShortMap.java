package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TShortShortIterator;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.procedure.TShortShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;

public interface TShortShortMap {
   short getNoEntryKey();

   short getNoEntryValue();

   short put(short var1, short var2);

   short putIfAbsent(short var1, short var2);

   void putAll(Map var1);

   void putAll(TShortShortMap var1);

   short get(short var1);

   void clear();

   boolean isEmpty();

   short remove(short var1);

   int size();

   TShortSet keySet();

   short[] keys();

   short[] keys(short[] var1);

   TShortCollection valueCollection();

   short[] values();

   short[] values(short[] var1);

   boolean containsValue(short var1);

   boolean containsKey(short var1);

   TShortShortIterator iterator();

   boolean forEachKey(TShortProcedure var1);

   boolean forEachValue(TShortProcedure var1);

   boolean forEachEntry(TShortShortProcedure var1);

   void transformValues(TShortFunction var1);

   boolean retainEntries(TShortShortProcedure var1);

   boolean increment(short var1);

   boolean adjustValue(short var1, short var2);

   short adjustOrPutValue(short var1, short var2, short var3);
}
