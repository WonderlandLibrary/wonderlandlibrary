package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TFloatLongIterator;
import gnu.trove.procedure.TFloatLongProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;

public interface TFloatLongMap {
   float getNoEntryKey();

   long getNoEntryValue();

   long put(float var1, long var2);

   long putIfAbsent(float var1, long var2);

   void putAll(Map var1);

   void putAll(TFloatLongMap var1);

   long get(float var1);

   void clear();

   boolean isEmpty();

   long remove(float var1);

   int size();

   TFloatSet keySet();

   float[] keys();

   float[] keys(float[] var1);

   TLongCollection valueCollection();

   long[] values();

   long[] values(long[] var1);

   boolean containsValue(long var1);

   boolean containsKey(float var1);

   TFloatLongIterator iterator();

   boolean forEachKey(TFloatProcedure var1);

   boolean forEachValue(TLongProcedure var1);

   boolean forEachEntry(TFloatLongProcedure var1);

   void transformValues(TLongFunction var1);

   boolean retainEntries(TFloatLongProcedure var1);

   boolean increment(float var1);

   boolean adjustValue(float var1, long var2);

   long adjustOrPutValue(float var1, long var2, long var4);
}
