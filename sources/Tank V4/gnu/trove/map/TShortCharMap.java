package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TShortCharIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TShortCharProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;
import java.util.Map;

public interface TShortCharMap {
   short getNoEntryKey();

   char getNoEntryValue();

   char put(short var1, char var2);

   char putIfAbsent(short var1, char var2);

   void putAll(Map var1);

   void putAll(TShortCharMap var1);

   char get(short var1);

   void clear();

   boolean isEmpty();

   char remove(short var1);

   int size();

   TShortSet keySet();

   short[] keys();

   short[] keys(short[] var1);

   TCharCollection valueCollection();

   char[] values();

   char[] values(char[] var1);

   boolean containsValue(char var1);

   boolean containsKey(short var1);

   TShortCharIterator iterator();

   boolean forEachKey(TShortProcedure var1);

   boolean forEachValue(TCharProcedure var1);

   boolean forEachEntry(TShortCharProcedure var1);

   void transformValues(TCharFunction var1);

   boolean retainEntries(TShortCharProcedure var1);

   boolean increment(short var1);

   boolean adjustValue(short var1, char var2);

   char adjustOrPutValue(short var1, char var2, char var3);
}
