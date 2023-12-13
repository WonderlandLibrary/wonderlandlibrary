package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TLongCharIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TLongCharProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TLongSet;
import java.util.Map;

public interface TLongCharMap {
   long getNoEntryKey();

   char getNoEntryValue();

   char put(long var1, char var3);

   char putIfAbsent(long var1, char var3);

   void putAll(Map var1);

   void putAll(TLongCharMap var1);

   char get(long var1);

   void clear();

   boolean isEmpty();

   char remove(long var1);

   int size();

   TLongSet keySet();

   long[] keys();

   long[] keys(long[] var1);

   TCharCollection valueCollection();

   char[] values();

   char[] values(char[] var1);

   boolean containsValue(char var1);

   boolean containsKey(long var1);

   TLongCharIterator iterator();

   boolean forEachKey(TLongProcedure var1);

   boolean forEachValue(TCharProcedure var1);

   boolean forEachEntry(TLongCharProcedure var1);

   void transformValues(TCharFunction var1);

   boolean retainEntries(TLongCharProcedure var1);

   boolean increment(long var1);

   boolean adjustValue(long var1, char var3);

   char adjustOrPutValue(long var1, char var3, char var4);
}
