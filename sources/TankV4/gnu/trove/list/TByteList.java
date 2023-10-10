package gnu.trove.list;

import gnu.trove.TByteCollection;
import gnu.trove.function.TByteFunction;
import gnu.trove.procedure.TByteProcedure;
import java.util.Random;

public interface TByteList extends TByteCollection {
   byte getNoEntryValue();

   int size();

   boolean isEmpty();

   boolean add(byte var1);

   void add(byte[] var1);

   void add(byte[] var1, int var2, int var3);

   void insert(int var1, byte var2);

   void insert(int var1, byte[] var2);

   void insert(int var1, byte[] var2, int var3, int var4);

   byte get(int var1);

   byte set(int var1, byte var2);

   void set(int var1, byte[] var2);

   void set(int var1, byte[] var2, int var3, int var4);

   byte replace(int var1, byte var2);

   void clear();

   boolean remove(byte var1);

   byte removeAt(int var1);

   void remove(int var1, int var2);

   void transformValues(TByteFunction var1);

   void reverse();

   void reverse(int var1, int var2);

   void shuffle(Random var1);

   TByteList subList(int var1, int var2);

   byte[] toArray();

   byte[] toArray(int var1, int var2);

   byte[] toArray(byte[] var1);

   byte[] toArray(byte[] var1, int var2, int var3);

   byte[] toArray(byte[] var1, int var2, int var3, int var4);

   boolean forEach(TByteProcedure var1);

   boolean forEachDescending(TByteProcedure var1);

   void sort();

   void sort(int var1, int var2);

   void fill(byte var1);

   void fill(int var1, int var2, byte var3);

   int binarySearch(byte var1);

   int binarySearch(byte var1, int var2, int var3);

   int indexOf(byte var1);

   int indexOf(int var1, byte var2);

   int lastIndexOf(byte var1);

   int lastIndexOf(int var1, byte var2);

   boolean contains(byte var1);

   TByteList grep(TByteProcedure var1);

   TByteList inverseGrep(TByteProcedure var1);

   byte max();

   byte min();

   byte sum();
}
