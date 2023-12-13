package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TCharByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TCharByteProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.set.TCharSet;
import java.util.Map;

public interface TCharByteMap {
   char getNoEntryKey();

   byte getNoEntryValue();

   byte put(char var1, byte var2);

   byte putIfAbsent(char var1, byte var2);

   void putAll(Map var1);

   void putAll(TCharByteMap var1);

   byte get(char var1);

   void clear();

   boolean isEmpty();

   byte remove(char var1);

   int size();

   TCharSet keySet();

   char[] keys();

   char[] keys(char[] var1);

   TByteCollection valueCollection();

   byte[] values();

   byte[] values(byte[] var1);

   boolean containsValue(byte var1);

   boolean containsKey(char var1);

   TCharByteIterator iterator();

   boolean forEachKey(TCharProcedure var1);

   boolean forEachValue(TByteProcedure var1);

   boolean forEachEntry(TCharByteProcedure var1);

   void transformValues(TByteFunction var1);

   boolean retainEntries(TCharByteProcedure var1);

   boolean increment(char var1);

   boolean adjustValue(char var1, byte var2);

   byte adjustOrPutValue(char var1, byte var2, byte var3);
}
