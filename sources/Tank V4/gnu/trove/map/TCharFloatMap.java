package gnu.trove.map;

import gnu.trove.TFloatCollection;
import gnu.trove.function.TFloatFunction;
import gnu.trove.iterator.TCharFloatIterator;
import gnu.trove.procedure.TCharFloatProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;

public interface TCharFloatMap {
   char getNoEntryKey();

   float getNoEntryValue();

   float put(char var1, float var2);

   float putIfAbsent(char var1, float var2);

   void putAll(Map var1);

   void putAll(TCharFloatMap var1);

   float get(char var1);

   void clear();

   boolean isEmpty();

   float remove(char var1);

   int size();

   TCharSet keySet();

   char[] keys();

   char[] keys(char[] var1);

   TFloatCollection valueCollection();

   float[] values();

   float[] values(float[] var1);

   boolean containsValue(float var1);

   boolean containsKey(char var1);

   TCharFloatIterator iterator();

   boolean forEachKey(TCharProcedure var1);

   boolean forEachValue(TFloatProcedure var1);

   boolean forEachEntry(TCharFloatProcedure var1);

   void transformValues(TFloatFunction var1);

   boolean retainEntries(TCharFloatProcedure var1);

   boolean increment(char var1);

   boolean adjustValue(char var1, float var2);

   float adjustOrPutValue(char var1, float var2, float var3);
}
