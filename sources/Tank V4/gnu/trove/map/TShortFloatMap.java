package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TShortFloatIterator;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.procedure.TShortFloatProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;

public interface TShortFloatMap {
   short getNoEntryKey();

   float getNoEntryValue();

   float put(short var1, float var2);

   float putIfAbsent(short var1, float var2);

   void putAll(Map var1);

   void putAll(TShortFloatMap var1);

   float get(short var1);

   void clear();

   boolean isEmpty();

   float remove(short var1);

   int size();

   TShortSet keySet();

   short[] keys();

   short[] keys(short[] var1);

   TFloatCollection valueCollection();

   float[] values();

   float[] values(float[] var1);

   boolean containsValue(float var1);

   boolean containsKey(short var1);

   TShortFloatIterator iterator();

   boolean forEachKey(TShortProcedure var1);

   boolean forEachValue(TFloatProcedure var1);

   boolean forEachEntry(TShortFloatProcedure var1);

   void transformValues(TFloatFunction var1);

   boolean retainEntries(TShortFloatProcedure var1);

   boolean increment(short var1);

   boolean adjustValue(short var1, float var2);

   float adjustOrPutValue(short var1, float var2, float var3);
}
