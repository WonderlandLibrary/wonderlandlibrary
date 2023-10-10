package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TFloatCharIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TFloatCharProcedure;
import gnu.trove.procedure.TFloatProcedure;
import gnu.trove.set.TFloatSet;
import java.util.Map;

public interface TFloatCharMap {
   float getNoEntryKey();

   char getNoEntryValue();

   char put(float var1, char var2);

   char putIfAbsent(float var1, char var2);

   void putAll(Map var1);

   void putAll(TFloatCharMap var1);

   char get(float var1);

   void clear();

   boolean isEmpty();

   char remove(float var1);

   int size();

   TFloatSet keySet();

   float[] keys();

   float[] keys(float[] var1);

   TCharCollection valueCollection();

   char[] values();

   char[] values(char[] var1);

   boolean containsValue(char var1);

   boolean containsKey(float var1);

   TFloatCharIterator iterator();

   boolean forEachKey(TFloatProcedure var1);

   boolean forEachValue(TCharProcedure var1);

   boolean forEachEntry(TFloatCharProcedure var1);

   void transformValues(TCharFunction var1);

   boolean retainEntries(TFloatCharProcedure var1);

   boolean increment(float var1);

   boolean adjustValue(float var1, char var2);

   char adjustOrPutValue(float var1, char var2, char var3);
}
