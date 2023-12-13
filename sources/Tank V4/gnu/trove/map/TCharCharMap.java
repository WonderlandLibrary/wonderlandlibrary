package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TCharCharIterator;
import gnu.trove.procedure.TCharCharProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;

public interface TCharCharMap {
   char getNoEntryKey();

   char getNoEntryValue();

   char put(char var1, char var2);

   char putIfAbsent(char var1, char var2);

   void putAll(Map var1);

   void putAll(TCharCharMap var1);

   char get(char var1);

   void clear();

   boolean isEmpty();

   char remove(char var1);

   int size();

   TCharSet keySet();

   char[] keys();

   char[] keys(char[] var1);

   TCharCollection valueCollection();

   char[] values();

   char[] values(char[] var1);

   boolean containsValue(char var1);

   boolean containsKey(char var1);

   TCharCharIterator iterator();

   boolean forEachKey(TCharProcedure var1);

   boolean forEachValue(TCharProcedure var1);

   boolean forEachEntry(TCharCharProcedure var1);

   void transformValues(TCharFunction var1);

   boolean retainEntries(TCharCharProcedure var1);

   boolean increment(char var1);

   boolean adjustValue(char var1, char var2);

   char adjustOrPutValue(char var1, char var2, char var3);
}
