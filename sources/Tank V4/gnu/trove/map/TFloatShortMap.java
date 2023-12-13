package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TFloatShortIterator;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TFloatShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;

public interface TFloatShortMap {
   float getNoEntryKey();

   short getNoEntryValue();

   short put(float var1, short var2);

   short putIfAbsent(float var1, short var2);

   void putAll(Map var1);

   void putAll(TFloatShortMap var1);

   short get(float var1);

   void clear();

   boolean isEmpty();

   short remove(float var1);

   int size();

   TFloatSet keySet();

   float[] keys();

   float[] keys(float[] var1);

   TShortCollection valueCollection();

   short[] values();

   short[] values(short[] var1);

   boolean containsValue(short var1);

   boolean containsKey(float var1);

   TFloatShortIterator iterator();

   boolean forEachKey(TFloatProcedure var1);

   boolean forEachValue(TShortProcedure var1);

   boolean forEachEntry(TFloatShortProcedure var1);

   void transformValues(TShortFunction var1);

   boolean retainEntries(TFloatShortProcedure var1);

   boolean increment(float var1);

   boolean adjustValue(float var1, short var2);

   short adjustOrPutValue(float var1, short var2, short var3);
}
