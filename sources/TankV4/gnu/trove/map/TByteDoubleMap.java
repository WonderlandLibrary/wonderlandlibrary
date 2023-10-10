package gnu.trove.map;

import gnu.trove.TDoubleCollection;
import gnu.trove.function.TDoubleFunction;
import gnu.trove.iterator.TByteDoubleIterator;
import gnu.trove.procedure.TByteDoubleProcedure;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TByteSet;
import java.util.Map;

public interface TByteDoubleMap {
   byte getNoEntryKey();

   double getNoEntryValue();

   double put(byte var1, double var2);

   double putIfAbsent(byte var1, double var2);

   void putAll(Map var1);

   void putAll(TByteDoubleMap var1);

   double get(byte var1);

   void clear();

   boolean isEmpty();

   double remove(byte var1);

   int size();

   TByteSet keySet();

   byte[] keys();

   byte[] keys(byte[] var1);

   TDoubleCollection valueCollection();

   double[] values();

   double[] values(double[] var1);

   boolean containsValue(double var1);

   boolean containsKey(byte var1);

   TByteDoubleIterator iterator();

   boolean forEachKey(TByteProcedure var1);

   boolean forEachValue(TDoubleProcedure var1);

   boolean forEachEntry(TByteDoubleProcedure var1);

   void transformValues(TDoubleFunction var1);

   boolean retainEntries(TByteDoubleProcedure var1);

   boolean increment(byte var1);

   boolean adjustValue(byte var1, double var2);

   double adjustOrPutValue(byte var1, double var2, double var4);
}
