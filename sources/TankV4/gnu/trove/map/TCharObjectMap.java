package gnu.trove.map;

import gnu.trove.function.TObjectFunction;
import gnu.trove.iterator.TCharObjectIterator;
import gnu.trove.procedure.TCharObjectProcedure;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.TCharSet;
import java.util.Collection;
import java.util.Map;

public interface TCharObjectMap {
   char getNoEntryKey();

   int size();

   boolean isEmpty();

   boolean containsKey(char var1);

   boolean containsValue(Object var1);

   Object get(char var1);

   Object put(char var1, Object var2);

   Object putIfAbsent(char var1, Object var2);

   Object remove(char var1);

   void putAll(Map var1);

   void putAll(TCharObjectMap var1);

   void clear();

   TCharSet keySet();

   char[] keys();

   char[] keys(char[] var1);

   Collection valueCollection();

   Object[] values();

   Object[] values(Object[] var1);

   TCharObjectIterator iterator();

   boolean forEachKey(TCharProcedure var1);

   boolean forEachValue(TObjectProcedure var1);

   boolean forEachEntry(TCharObjectProcedure var1);

   void transformValues(TObjectFunction var1);

   boolean retainEntries(TCharObjectProcedure var1);

   boolean equals(Object var1);

   int hashCode();
}
