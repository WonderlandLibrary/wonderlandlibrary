package gnu.trove.map;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.iterator.TDoubleByteIterator;
import gnu.trove.procedure.TByteProcedure;
import gnu.trove.procedure.TDoubleByteProcedure;
import gnu.trove.procedure.TDoubleProcedure;
import gnu.trove.set.TDoubleSet;
import java.util.Map;

public interface TDoubleByteMap {
   double getNoEntryKey();

   byte getNoEntryValue();

   byte put(double var1, byte var3);

   byte putIfAbsent(double var1, byte var3);

   void putAll(Map var1);

   void putAll(TDoubleByteMap var1);

   byte get(double var1);

   void clear();

   boolean isEmpty();

   byte remove(double var1);

   int size();

   TDoubleSet keySet();

   double[] keys();

   double[] keys(double[] var1);

   TByteCollection valueCollection();

   byte[] values();

   byte[] values(byte[] var1);

   boolean containsValue(byte var1);

   boolean containsKey(double var1);

   TDoubleByteIterator iterator();

   boolean forEachKey(TDoubleProcedure var1);

   boolean forEachValue(TByteProcedure var1);

   boolean forEachEntry(TDoubleByteProcedure var1);

   void transformValues(TByteFunction var1);

   boolean retainEntries(TDoubleByteProcedure var1);

   boolean increment(double var1);

   boolean adjustValue(double var1, byte var3);

   byte adjustOrPutValue(double var1, byte var3, byte var4);
}
