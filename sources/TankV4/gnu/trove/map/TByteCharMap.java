package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TByteCharIterator;
import gnu.trove.procedure.TByteCharProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;

public interface TByteCharMap {
   byte getNoEntryKey();

   char getNoEntryValue();

   char put(byte var1, char var2);

   char putIfAbsent(byte var1, char var2);

   void putAll(Map var1);

   void putAll(TByteCharMap var1);

   char get(byte var1);

   void clear();

   boolean isEmpty();

   char remove(byte var1);

   int size();

   TByteSet keySet();

   byte[] keys();

   byte[] keys(byte[] var1);

   TCharCollection valueCollection();

   char[] values();

   char[] values(char[] var1);

   boolean containsValue(char var1);

   boolean containsKey(byte var1);

   TByteCharIterator iterator();

   boolean forEachKey(TByteProcedure var1);

   boolean forEachValue(TCharProcedure var1);

   boolean forEachEntry(TByteCharProcedure var1);

   void transformValues(TCharFunction var1);

   boolean retainEntries(TByteCharProcedure var1);

   boolean increment(byte var1);

   boolean adjustValue(byte var1, char var2);

   char adjustOrPutValue(byte var1, char var2, char var3);
}
