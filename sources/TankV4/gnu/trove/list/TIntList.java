package gnu.trove.list;

import gnu.trove.TIntCollection;
import gnu.trove.function.TIntFunction;
import gnu.trove.procedure.TIntProcedure;
import java.util.Random;

public interface TIntList extends TIntCollection {
   int getNoEntryValue();

   int size();

   boolean isEmpty();

   boolean add(int var1);

   void add(int[] var1);

   void add(int[] var1, int var2, int var3);

   void insert(int var1, int var2);

   void insert(int var1, int[] var2);

   void insert(int var1, int[] var2, int var3, int var4);

   int get(int var1);

   int set(int var1, int var2);

   void set(int var1, int[] var2);

   void set(int var1, int[] var2, int var3, int var4);

   int replace(int var1, int var2);

   void clear();

   boolean remove(int var1);

   int removeAt(int var1);

   void remove(int var1, int var2);

   void transformValues(TIntFunction var1);

   void reverse();

   void reverse(int var1, int var2);

   void shuffle(Random var1);

   TIntList subList(int var1, int var2);

   int[] toArray();

   int[] toArray(int var1, int var2);

   int[] toArray(int[] var1);

   int[] toArray(int[] var1, int var2, int var3);

   int[] toArray(int[] var1, int var2, int var3, int var4);

   boolean forEach(TIntProcedure var1);

   boolean forEachDescending(TIntProcedure var1);

   void sort();

   void sort(int var1, int var2);

   void fill(int var1);

   void fill(int var1, int var2, int var3);

   int binarySearch(int var1);

   int binarySearch(int var1, int var2, int var3);

   int indexOf(int var1);

   int indexOf(int var1, int var2);

   int lastIndexOf(int var1);

   int lastIndexOf(int var1, int var2);

   boolean contains(int var1);

   TIntList grep(TIntProcedure var1);

   TIntList inverseGrep(TIntProcedure var1);

   int max();

   int min();

   int sum();
}
