package gnu.trove.map;

import gnu.trove.TCharCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.iterator.TObjectCharIterator;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TObjectCharProcedure;
import gnu.trove.procedure.TObjectProcedure;
import java.util.Map;
import java.util.Set;

public interface TObjectCharMap {
   char getNoEntryValue();

   int size();

   boolean isEmpty();

   boolean containsKey(Object var1);

   boolean containsValue(char var1);

   char get(Object var1);

   char put(Object var1, char var2);

   char putIfAbsent(Object var1, char var2);

   char remove(Object var1);

   void putAll(Map var1);

   void putAll(TObjectCharMap var1);

   void clear();

   Set keySet();

   Object[] keys();

   Object[] keys(Object[] var1);

   TCharCollection valueCollection();

   char[] values();

   char[] values(char[] var1);

   TObjectCharIterator iterator();

   boolean increment(Object var1);

   boolean adjustValue(Object var1, char var2);

   char adjustOrPutValue(Object var1, char var2, char var3);

   boolean forEachKey(TObjectProcedure var1);

   boolean forEachValue(TCharProcedure var1);

   boolean forEachEntry(TObjectCharProcedure var1);

   void transformValues(TCharFunction var1);

   boolean retainEntries(TObjectCharProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
