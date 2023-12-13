package gnu.trove.map;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.iterator.TCharIntIterator;
import gnu.trove.procedure.TCharIntProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;

public interface TCharIntMap {
   char getNoEntryKey();

   int getNoEntryValue();

   int put(char var1, int var2);

   int putIfAbsent(char var1, int var2);

   void putAll(Map var1);

   void putAll(TCharIntMap var1);

   int get(char var1);

   void clear();

   boolean isEmpty();

   int remove(char var1);

   int size();

   TCharSet keySet();

   char[] keys();

   char[] keys(char[] var1);

   TIntCollection valueCollection();

   int[] values();

   int[] values(int[] var1);

   boolean containsValue(int var1);

   boolean containsKey(char var1);

   TCharIntIterator iterator();

   boolean forEachKey(TCharProcedure var1);

   boolean forEachValue(TIntProcedure var1);

   boolean forEachEntry(TCharIntProcedure var1);

   void transformValues(TIntFunction var1);

   boolean retainEntries(TCharIntProcedure var1);

   boolean increment(char var1);

   boolean adjustValue(char var1, int var2);

   int adjustOrPutValue(char var1, int var2, int var3);
}
