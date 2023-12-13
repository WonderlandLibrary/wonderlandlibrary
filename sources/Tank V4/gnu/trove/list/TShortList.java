package gnu.trove.list;

import gnu.trove.TShortCollection;
import gnu.trove.function.TShortFunction;
import gnu.trove.procedure.TShortProcedure;
import java.util.Random;

public interface TShortList extends TShortCollection {
   short getNoEntryValue();

   int size();

   boolean isEmpty();

   boolean add(short var1);

   void add(short[] var1);

   void add(short[] var1, int var2, int var3);

   void insert(int var1, short var2);

   void insert(int var1, short[] var2);

   void insert(int var1, short[] var2, int var3, int var4);

   short get(int var1);

   short set(int var1, short var2);

   void set(int var1, short[] var2);

   void set(int var1, short[] var2, int var3, int var4);

   short replace(int var1, short var2);

   void clear();

   boolean remove(short var1);

   short removeAt(int var1);

   void remove(int var1, int var2);

   void transformValues(TShortFunction var1);

   void reverse();

   void reverse(int var1, int var2);

   void shuffle(Random var1);

   TShortList subList(int var1, int var2);

   short[] toArray();

   short[] toArray(int var1, int var2);

   short[] toArray(short[] var1);

   short[] toArray(short[] var1, int var2, int var3);

   short[] toArray(short[] var1, int var2, int var3, int var4);

   boolean forEach(TShortProcedure var1);

   boolean forEachDescending(TShortProcedure var1);

   void sort();

   void sort(int var1, int var2);

   void fill(short var1);

   void fill(int var1, int var2, short var3);

   int binarySearch(short var1);

   int binarySearch(short var1, int var2, int var3);

   int indexOf(short var1);

   int indexOf(int var1, short var2);

   int lastIndexOf(short var1);

   int lastIndexOf(int var1, short var2);

   boolean contains(short var1);

   TShortList grep(TShortProcedure var1);

   TShortList inverseGrep(TShortProcedure var1);

   short max();

   short min();

   short sum();
}
