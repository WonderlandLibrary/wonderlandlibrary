package gnu.trove.map;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.iterator.TCharShortIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TCharShortProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;

public interface TCharShortMap {
   char getNoEntryKey();

   short getNoEntryValue();

   short put(char var1, short var2);

   short putIfAbsent(char var1, short var2);

   void putAll(Map var1);

   void putAll(TCharShortMap var1);

   short get(char var1);

   void clear();

   boolean isEmpty();

   short remove(char var1);

   int size();

   TCharSet keySet();

   char[] keys();

   char[] keys(char[] var1);

   TShortCollection valueCollection();

   short[] values();

   short[] values(short[] var1);

   boolean containsValue(short var1);

   boolean containsKey(char var1);

   TCharShortIterator iterator();

   boolean forEachKey(TCharProcedure var1);

   boolean forEachValue(TShortProcedure var1);

   boolean forEachEntry(TCharShortProcedure var1);

   void transformValues(TShortFunction var1);

   boolean retainEntries(TCharShortProcedure var1);

   boolean increment(char var1);

   boolean adjustValue(char var1, short var2);

   short adjustOrPutValue(char var1, short var2, short var3);
}
