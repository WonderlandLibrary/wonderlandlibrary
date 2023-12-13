package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TIntCharIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TIntCharProcedure;
import gnu.trove.procedure.TIntProcedure;
import gnu.trove.set.TIntSet;
import java.util.Map;

public interface TIntCharMap {
   int getNoEntryKey();

   char getNoEntryValue();

   char put(int var1, char var2);

   char putIfAbsent(int var1, char var2);

   void putAll(Map var1);

   void putAll(TIntCharMap var1);

   char get(int var1);

   void clear();

   boolean isEmpty();

   char remove(int var1);

   int size();

   TIntSet keySet();

   int[] keys();

   int[] keys(int[] var1);

   TCharCollection valueCollection();

   char[] values();

   char[] values(char[] var1);

   boolean containsValue(char var1);

   boolean containsKey(int var1);

   TIntCharIterator iterator();

   boolean forEachKey(TIntProcedure var1);

   boolean forEachValue(TCharProcedure var1);

   boolean forEachEntry(TIntCharProcedure var1);

   void transformValues(TCharFunction var1);

   boolean retainEntries(TIntCharProcedure var1);

   boolean increment(int var1);

   boolean adjustValue(int var1, char var2);

   char adjustOrPutValue(int var1, char var2, char var3);
}
