package gnu.trove.map;

import gnu.trove.TLongCollection;
import gnu.trove.function.TLongFunction;
import gnu.trove.iterator.TCharLongIterator;
import gnu.trove.procedure.TCharLongProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TLongProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;

public interface TCharLongMap {
   char getNoEntryKey();

   long getNoEntryValue();

   long put(char var1, long var2);

   long putIfAbsent(char var1, long var2);

   void putAll(Map var1);

   void putAll(TCharLongMap var1);

   long get(char var1);

   void clear();

   boolean isEmpty();

   long remove(char var1);

   int size();

   TCharSet keySet();

   char[] keys();

   char[] keys(char[] var1);

   TLongCollection valueCollection();

   long[] values();

   long[] values(long[] var1);

   boolean containsValue(long var1);

   boolean containsKey(char var1);

   TCharLongIterator iterator();

   boolean forEachKey(TCharProcedure var1);

   boolean forEachValue(TLongProcedure var1);

   boolean forEachEntry(TCharLongProcedure var1);

   void transformValues(TLongFunction var1);

   boolean retainEntries(TCharLongProcedure var1);

   boolean increment(char var1);

   boolean adjustValue(char var1, long var2);

   long adjustOrPutValue(char var1, long var2, long var4);
}
